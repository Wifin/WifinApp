/*
 * Copyright (C) 2012- Peer internet solutions & Finalist IT Group
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
package org.mixare.data.convert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.POIMarker;
import org.mixare.data.DataHandler;
import org.mixare.lib.HtmlUnescape;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.marker.Marker;
/**
 * A data processor for custom urls or data, Responsible for converting raw data (to json and then) to marker data.
 * @author A. Egal
 * @author Xu Duangui - edited the load method to contain the signal level of the wifi. 
 */
public class MixareDataProcessor extends DataHandler implements DataProcessor{
	//------------------------
	//
	// VARIABLES USED FOR MixareDataProcessor
	//
	//------------------------
	
	//------------------------
	// PUBLIC VARIABLES
	//------------------------
	/** The max size of the JSON array.*/
	public static final int MAX_JSON_OBJECTS = 1000;
	
	@Override
	public String[] getUrlMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}

	@Override
	public String[] getDataMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}
	
	@Override
	public boolean matchesRequiredType(String type) {
		return true; //this datasources has no required type, it will always match.
	}
	
	/**
	 * This method load the data from the datasource. This will call the 
	 * MixareDataProcessor class to create the markers from the data source. And
	 * add them in to a marker list. 
	 * 
	 * @param url - the url of the data source 
	 * @param taskId - the tasak ID of the source 
	 * @param colour -  the color for the data source
	 * 
	 * @return a list of markers.
	 * 
	 * @exception JSONException will be thrown if there is an error in reading the source.
	 * */
	@Override
	public List<Marker> load(String rawData, int taskId, int colour) throws JSONException {
		List<Marker> markers = new ArrayList<Marker>();
		JSONObject root = convertToJSON(rawData);
		JSONArray dataArray = root.getJSONArray("results");
		int top = Math.min(MAX_JSON_OBJECTS, dataArray.length());
//        System.out.println(dataArray.length());
		for (int i = 0; i < top; i++) {
			JSONObject jo = dataArray.getJSONObject(i);
			
			Marker ma = null;
			if (jo.has("title") && jo.has("lat") && jo.has("lng")
					&& jo.has("elevation")) {

				String id = "";
				if(jo.has("id"))
						id = jo.getString("id");
				
//				Log.v(MixView.TAG, "processing Mixare JSON object");
				String link=null;
		
				if(jo.has("has_detail_page") && jo.getInt("has_detail_page")!=0 && jo.has("webpage"))
					link=jo.getString("webpage");
				
					
				ma = new POIMarker(
						id,
						HtmlUnescape.unescapeHTML(jo.getString("title"), 0), 
						jo.getDouble("lat"), 
						jo.getDouble("lng"), 
						jo.getDouble("elevation"), 
						link, 
						jo.getString("level") ,
						taskId, colour);
				ma.draw(new PaintScreen());
				markers.add(ma);
			}
		}
		return markers;		
	}
	/**
	 * This method will convert the raw data into a JSONObject. 
	 * 
	 * @param rawData - the raw data of the data source 
	 * 
	 * @return JSONObject of the raw data
	 * 
	 * @exception RuntimeException if the rawData is invalid.
	 * */
	private JSONObject convertToJSON(String rawData){
		try {
			return new JSONObject(rawData);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
