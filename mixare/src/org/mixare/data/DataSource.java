/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.mixare.data;

import org.mixare.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The DataSource class is able to create the URL where the information about a
 * place can be found.
 * 
 * @author hannes
 * @author Xu Duangui - edited the constructors and enum TYPE. 
 * 
 */
public class DataSource extends Activity {
	//------------------------
	//
	// VARIABLES USED FOR DataSource
	//
	//------------------------
	
	//------------------------
	// PRIAVTE VARIABLES
	//------------------------
	/** Name for the data source*/
	private String name;
	/** URL link for the data source*/
	private String url;
	/** Status of data ssource*/
	private boolean enabled;
	/** TYPE of data source*/
	private TYPE type;
	/** the display for data source*/
	private DISPLAY display;
	
	//EDITED : DG CHANGE THE FILE TO READ ONLY OUR OWN DATA
	/** There is only one type, mixare*/
	public enum TYPE {
		MIXARE   };

	/** The type of Display*/
	public enum DISPLAY {
		CIRCLE_MARKER, NAVIGATION_MARKER, IMAGE_MARKER
	};

	
	/**
	 * Empty constructor 
	 * */
	public DataSource() {

	}
	
	/**
	 * DataSoruce constructor for the type of data source. 
	 * @param name -  name of data source 
	 * @param url - url of data source 
	 * @param type - type of dataSource 
	 * @param display -  type of display 
	 * @param enabled -  status of data source */
	public DataSource(String name, String url, TYPE type, DISPLAY display,
			boolean enabled) {
		this.name = name;
		this.url = url;
//		this.url = "http://deco3801-007.uqcloud.net/mixare/testJSON.json";
		this.type = TYPE.MIXARE;
		this.display = display;
		this.enabled = enabled;
		Log.d("mixare", "New Datasource - with display! " + name + " " + url + " " + type + " "
				+ display + " " + enabled);
	}
	/**
	 * DataSoruce constructor for the type of data source. 
	 * @param name -  name of data source 
	 * @param url - url of data source 
	 * @param typeInt - type of dataSource in integer form
	 * @param displayInt -  type of display in integer form
	 * @param enabled -  status of data source */
	public DataSource(String name, String url, int typeInt, int displayInt,
			boolean enabled) {
//		TYPE typeEnum = TYPE.MIXARE;
		DISPLAY displayEnum = DISPLAY.values()[displayInt];
		this.name = name;
		this.url = url;
//		this.url = "http://deco3801-007.uqcloud.net/mixare/testJSON.json";
		this.type = TYPE.MIXARE;
		this.display = displayEnum;
		this.enabled = enabled;
		
		Log.d("mixare", "New Datasource! - all int" + name + " " + url + " " + type + " "
				+ display + " " + enabled);
	}

	/**
	 * DataSoruce constructor for the type of data source. 
	 * @param name -  name of data source 
	 * @param url - url of data source 
	 * @param typeString - type of dataSource in String
	 * @param displayString -  type of display in String
	 * @param enabledString -  status of data source in string*/
	public DataSource(String name, String url, String typeString,
			String displayString, String enabledString) {
//		TYPE typeEnum = TYPE.values()[Integer.parseInt(typeString)];
		DISPLAY displayEnum = DISPLAY.values()[Integer.parseInt(displayString)];
		Boolean enabledBool = Boolean.parseBoolean(enabledString);
		this.name = name;
		this.url = url;
//		this.url = "http://deco3801-007.uqcloud.net/mixare/testJSON.json";
		this.type = TYPE.MIXARE;
		this.display = displayEnum;
		this.enabled = enabledBool;
		
		Log.d("mixare", "New Datasource! - all strings" + name + " " + url + " " + type + " "
				+ display + " " + enabled);
	}
	
	//EDITED : DG CHANGE THE FILE TO READ ONLY OUR OWN DATA
	/** This method will create the request parameters for the data source.
	 * @param lat - wifi latitude 
	 * @param lon -  wifi longtitude
	 * @param alt -wifi altitude
	 * @param radius -  radius of marker
	 * @return the string representation of the request  */
	public String createRequestParams(double lat, double lon, double alt,
			float radius) {
		String ret = "";
		if (!ret.startsWith("file://")) {
			ret += "?latitude=" + Double.toString(lat) + "&longitude="
					+ Double.toString(lon) + "&altitude="
					+ Double.toString(alt) + "&radius="
					+ Double.toString(radius);
		}

		return ret;
	}
	
	//------------------------
	// Get and Set Methods.
	//------------------------
	
	//EDITED : DG CHANGE THE FILE TO READ ONLY OUR OWN DATA
	/** The color for the data source 
	 * @return the color code */
	public int getColor() {
		return Color.RED;
	}
	//EDITED : DG CHANGE THE FILE TO READ ONLY OUR OWN DATA
	/** The launcher for the drawing 
	 * @return launcher */
	public int getDataSourceIcon() {
		return R.drawable.ic_launcher;
	}

	public int getDisplayId() {
		return this.display.ordinal();
	}

	public int getTypeId() {
		return this.type.ordinal();
	}

	public DISPLAY getDisplay() {
		return this.display;
	}

	public TYPE getType() {
		return this.type;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	public String getName() {
		return this.name;
	}

	public String getUrl() {
		return this.url;
	}

	public String serialize() {
		return this.getName() + "|" + this.getUrl() + "|" + this.getTypeId()
				+ "|" + this.getDisplayId() + "|" + this.getEnabled();
	}

	public void setEnabled(boolean isChecked) {
		this.enabled = isChecked;
	}

	@Override
	public String toString() {
		return "DataSource [name=" + name + ", url=" + url + ", enabled="
				+ enabled + ", type=" + type + ", display=" + display + "]";
	}

	/**
	 * Check the minimum required data
	 * 
	 * @return boolean
	 */
	public boolean isWellFormed() {
		boolean out = false;
		if (isUrlWellFormed() || getName() != null || !getName().isEmpty()) {
			out = true;
		}
		return out;
	}

	public boolean isUrlWellFormed() {
		return getUrl() != null || !getUrl().isEmpty()
				|| "http://".equalsIgnoreCase(getUrl());
	}
	
	//------------------------
	//
	// METHODS FROM MIXARE, not used in WIFIN
	//
	//------------------------
	/** 
	 * This method will create the form for the user to input their own data. 
	 * This is not implemented in WIFIN.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.datasourcedetails);
			final EditText nameField = (EditText) findViewById(R.id.name);
			final EditText urlField = (EditText) findViewById(R.id.url);
			final Spinner typeSpinner = (Spinner) findViewById(R.id.type);
			final Spinner displaySpinner = (Spinner) findViewById(R.id.displaytype);
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				if (extras.containsKey("DataSourceId")) {
					String fields[] = DataSourceStorage.getInstance().getFields(
							extras.getInt("DataSourceId"));
					nameField.setText(fields[0], TextView.BufferType.EDITABLE);
					urlField.setText(fields[1], TextView.BufferType.EDITABLE);
					typeSpinner.setSelection(Integer.parseInt(fields[2]) - 3);
					displaySpinner.setSelection(Integer.parseInt(fields[3]));
				}
			}

		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				final EditText nameField = (EditText) findViewById(R.id.name);
				String name = nameField.getText().toString();
				final EditText urlField = (EditText) findViewById(R.id.url);
				String url = urlField.getText().toString();
				final Spinner typeSpinner = (Spinner) findViewById(R.id.type);
				int typeId = (int) typeSpinner.getItemIdAtPosition(typeSpinner
						.getSelectedItemPosition());
				final Spinner displaySpinner = (Spinner) findViewById(R.id.displaytype);
				int displayId = (int) displaySpinner
						.getItemIdAtPosition(displaySpinner
								.getSelectedItemPosition());

				// TODO: fix the weird hack for type!
				DataSource newDS = new DataSource(name, url, typeId + 3, displayId,
						true);

				int index = DataSourceStorage.getInstance().getSize();
				Bundle extras = getIntent().getExtras();
				if (extras != null) {
					if (extras.containsKey("DataSourceId")) {
						index = extras.getInt("DataSourceId");
					}
				}
				DataSourceStorage.getInstance().add("DataSource" + index,
						newDS.serialize());
			}

			return super.onKeyDown(keyCode, event);
		}

		@Override
		protected void onPause() {
			super.onPause();
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			int base = Menu.FIRST;
			menu.add(base, base, base, R.string.cancel);
			return super.onCreateOptionsMenu(menu);

		}

		@Override
		public boolean onMenuItemSelected(int featureId, MenuItem item) {
			switch (item.getItemId()) {
			case Menu.FIRST:
				finish();
				break;
			}
			return super.onMenuItemSelected(featureId, item);
		}


}
