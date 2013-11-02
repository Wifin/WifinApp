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
package org.mixare;

import static android.view.KeyEvent.KEYCODE_CAMERA;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.mixare.data.DataHandler;
import org.mixare.data.DataSource;
import org.mixare.gui.RadarPoints;
import org.mixare.lib.MixUtils;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.ScreenLine;
import org.mixare.lib.marker.Marker;
import org.mixare.lib.render.Camera;
import org.mixare.mgr.downloader.DownloadManager;
import org.mixare.mgr.downloader.DownloadRequest;
import org.mixare.mgr.downloader.DownloadResult;


import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is able to update the markers and the radar. It also handles some
 * user events
 * 
 * @author Xu Duangui - edited and added the features of the pop up window (loadConnectionWindow)
 */
public class DataView {

	//------------------------
	//
	// VARIABLES USED FOR DATAVIEW
	//
	//------------------------
	
	//------------------------
	// PRIAVTE VARIABLES
	//------------------------
	/** current context */
	private MixContext mixContext;
	/** is the view Inited */
	private boolean isInit;
	/** Activity instance */
	private Activity activity;
	/** width and height of the view */
	private int width, height;
	/**
	 * _NOT_ the android camera, the class that takes care of the transformation
	 */
	private Camera cam;
	/**Instance of MixState class*/
	private MixState state = new MixState();
	/** The view can be "frozen" for debug purposes */
	private boolean frozen;
	/** how many times to re-attempt download */
	private int retry;
	/** The location instance which can used for updating the current location */
	private Location curFix;
	/** Instance of dataHandler for processing of data */
	private DataHandler dataHandler = new DataHandler();
	/** The radius size. set at 50 */
	private float radius = 50;
	/** timer to refresh the browser */
	private Timer refresh = null;
	/** Refresh rate*/
	private final long refreshDelay = 8 * 1000; // refresh every 8 seconds
	/** has the launcher launch ?*/
	private boolean isLauncherStarted;
	/** List to store user interface events */
	private ArrayList<UIEvent> uiEvents = new ArrayList<UIEvent>();
	/** Instance of radar point class to create the radar*/
	private RadarPoints radarPoints = new RadarPoints();
	/** The left screen line */
	private ScreenLine lrl = new ScreenLine();
	/** The right screen line */
	private ScreenLine rrl = new ScreenLine();
	/** The x and y values */
	private float rx = 10, ry = 20;
	private float addX = 0, addY = 0;
	/** List to store markers */	
	private List<Marker> markers;
	

	//------------------------
	//
	// Methods from Mixare
	//
	//------------------------	
	/**
	 * Constructor for data view 
	 * 
	 * @param ctx - mixContext instance.
	 */
	public DataView(MixContext ctx) {
		this.mixContext = ctx;
	}

	/**
	 * Get the mix context
	 * 
	 * @return mixContext instance.
	 */
	public MixContext getContext() {
		return mixContext;
	}

	/**
	 * Get the status of Launcher 
	 * 
	 * @return status of launcher
	 */
	public boolean isLauncherStarted() {
		return isLauncherStarted;
	}

	/**
	 * Is the screen frozen ?
	 * 
	 * @return status of the frozen variable.
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * Set the frozen status
	 * 
	 * @param frozen - the status of frozen.
	 */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	/**
	 * Get radius for the radar
	 * 
	 * @return radius
	 */
	public float getRadius() {
		return radius;
	}
	/**
	 * Set the radius
	 * 
	 * @param radius - the radius to be set.
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}
	/**
	 * Get the datahandler instance
	 * 
	 * @return DataHandler instance.
	 */
	public DataHandler getDataHandler() {
		return dataHandler;
	}
	/**
	 * Status of the details view
	 * 
	 * @return status of the detail view.
	 */
	public boolean isDetailsView() {
		return state.isDetailsView();
	}
	/**
	 * Set the status of the details view
	 * 
	 * @return DataHandler instance.
	 */
	public void setDetailsView(boolean detailsView) {
		state.setDetailsView(detailsView);
	}

	/**
	 * Set the location for the last download to the current download. 
	 * */
	public void doStart() {
		state.nextLStatus = MixState.NOT_STARTED;
		mixContext.getLocationFinder().setLocationAtLastDownload(curFix);
	}

	/**
	 * To get the status of "initialised "
	 * @return status of the isInit
	 * */
	public boolean isInited() {
		return isInit;
	}
	/**
	 * Set the activity to create the textbox for displaying of wifi details.
	 * @param activity -  the instance of activity 
	 * */
	public void setActivity(Activity activity){
		this.activity = activity;
	}

	/**
	 * This method will initialise the augemnted view on the camera. The view 
	 * will be set using the device's screen width and height. 
	 * 
	 * @param widthInit - Width of the screen
	 * @param heightInit - Height of the screeen 
	 * */
	public void init(int widthInit, int heightInit) {
		try {
			width = widthInit;
			height = heightInit;

			cam = new Camera(width, height, true);
			cam.setViewAngle(Camera.DEFAULT_VIEW_ANGLE);

			lrl.set(0, -RadarPoints.RADIUS);
			lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
			lrl.add(rx + RadarPoints.RADIUS, ry + RadarPoints.RADIUS);
			rrl.set(0, -RadarPoints.RADIUS);
			rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
			rrl.add(rx + RadarPoints.RADIUS, ry + RadarPoints.RADIUS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		frozen = false;
		isInit = true;
	}
	/**
	 * This method will request for the data via the url the user had input. 
	 * The request will be send to the mixcontext class and datasource class for 
	 * the processing of the data. 
	 * 
	 * @param url - the url for the data.*/
	public void requestData(String url) {
//		url = "http://deco3801-007.uqcloud.net/mixare/testJSON.json";
		DownloadRequest request = new DownloadRequest(new DataSource(
				"LAUNCHER", url, DataSource.TYPE.MIXARE,
				DataSource.DISPLAY.CIRCLE_MARKER, true));
		mixContext.getDataSourceManager().setAllDataSourcesforLauncher(
				request.getSource());
		mixContext.getDownloadManager().submitJob(request);
		state.nextLStatus = MixState.PROCESSING;
		}


	/** Drawing of the update to the markers and radar. This method will run 
	 * 	continuously to set the view of the AR based on the user inputs. 
	 * 
	 * 	@param dw - the paint screen instance */
	public void draw(PaintScreen dw) {
		mixContext.getRM(cam.transform);
		curFix = mixContext.getLocationFinder().getCurrentLocation();

		state.calcPitchBearing(cam.transform);

		// Load Layer
		if (state.nextLStatus == MixState.NOT_STARTED && !frozen) {
			loadDrawLayer();
			markers = new ArrayList<Marker>();
		}
		else if (state.nextLStatus == MixState.PROCESSING) {
			DownloadManager dm = mixContext.getDownloadManager();
			DownloadResult dRes = null;

			markers.addAll(downloadDrawResults(dm, dRes));
			
			if (dm.isDone()) {
				retry = 0;
				state.nextLStatus = MixState.DONE;
				
				dataHandler = new DataHandler();
				dataHandler.addMarkers(markers);
				dataHandler.onLocationChanged(curFix);
				
				//START THE REFRESH RATE!
				if (refresh == null) { // start the refresh timer if it is null
					refresh = new Timer(false);
					Date date = new Date(System.currentTimeMillis()
							+ refreshDelay);
					refresh.schedule(new TimerTask() {

						@Override
						public void run() {
							callRefreshToast();
							refresh();
						}
					}, date, refreshDelay);
				}
			}
		}

		// Update markers
		dataHandler.updateActivationStatus(mixContext);
		for (int i = dataHandler.getMarkerCount() - 1; i >= 0; i--) {
			Marker ma = dataHandler.getMarker(i);
			// if (ma.isActive() && (ma.getDistance() / 1000f < radius || ma
			// instanceof NavigationMarker || ma instanceof SocialMarker)) {
			if (ma.isActive() && (ma.getDistance() / 1000f < radius)) {

				// To increase performance don't recalculate position vector
				// for every marker on every draw call, instead do this only
				// after onLocationChanged and after downloading new marker
				// if (!frozen)
				// ma.update(curFix);
				if (!frozen)
					ma.calcPaint(cam, addX, addY);
				ma.draw(dw);
//				System.out.println("SIGNAL STR = " + ma.getSignal());
				
			}
		}

		// Draw Radar 
		// CHECK DRAW RADAR METHOD TO GET THE RADAR TO BE DRAWN 
		drawRadar(dw);
		
		// Get next event
		UIEvent evt = null;
		synchronized (uiEvents) {
			if (uiEvents.size() > 0) {
				evt = uiEvents.get(0);
				uiEvents.remove(0);
			}
		}
		if (evt != null) {
			switch (evt.type) {
			case UIEvent.KEY:
				handleKeyEvent((KeyEvent) evt);
				break;
			case UIEvent.CLICK:
				handleClickEvent((ClickEvent) evt);
				break;
			}
		}
		state.nextLStatus = MixState.PROCESSING;
	}
	


	/**
	 * Part of draw function, loads the layer.
	 */
	private void loadDrawLayer(){
		if (mixContext.getStartUrl().length() > 0) {
			requestData(mixContext.getStartUrl());
			isLauncherStarted = true;
		}

		else {
			double lat = curFix.getLatitude(), lon = curFix.getLongitude(), alt = curFix
					.getAltitude();
			state.nextLStatus = MixState.PROCESSING;
			mixContext.getDataSourceManager().requestDataFromAllActiveDataSource(lat, lon, alt,	radius);
		}

		// if no datasources are activated
		if (state.nextLStatus == MixState.NOT_STARTED)
			state.nextLStatus = MixState.DONE;
	}
	
	/** 
	 * Getting the list of markers from the data source. 
	 * @param dm - the download manager instance 
	 * @param dRes - the download result from the source. 
	 *   */
	private List<Marker> downloadDrawResults(DownloadManager dm, DownloadResult dRes){
		List<Marker> markers = new ArrayList<Marker>();
		while ((dRes = dm.getNextResult()) != null) {
			if (dRes.isError() && retry < 3) {
				retry++;
				mixContext.getDownloadManager().submitJob(
						dRes.getErrorRequest());
				// Notification
				// Toast.makeText(mixContext, dRes.errorMsg,
				// Toast.LENGTH_SHORT).show();
			}
			
			if(!dRes.isError()) {
				if(dRes.getMarkers() != null){
					//jLayer = (DataHandler) dRes.obj;
					Log.i(MixView.TAG,"Adding Markers");
					markers.addAll(dRes.getMarkers());

				}
			}
		}
		return markers;
	}
	

	/**
	 * Handles drawing radar and direction.
	 * @param PaintScreen screen that radar will be drawn to
	 */
	private void drawRadar(PaintScreen dw) {
		String dirTxt = "";
		int bearing = (int) state.getCurBearing();
		int range = (int) (state.getCurBearing() / (360f / 16f));
		// TODO: get strings from the values xml file
		if (range == 15 || range == 0)
			dirTxt = getContext().getString(R.string.N);
		else if (range == 1 || range == 2)
			dirTxt = getContext().getString(R.string.NE);
		else if (range == 3 || range == 4)
			dirTxt = getContext().getString(R.string.E);
		else if (range == 5 || range == 6)
			dirTxt = getContext().getString(R.string.SE);
		else if (range == 7 || range == 8)
			dirTxt = getContext().getString(R.string.S);
		else if (range == 9 || range == 10)
			dirTxt = getContext().getString(R.string.SW);
		else if (range == 11 || range == 12)
			dirTxt = getContext().getString(R.string.W);
		else if (range == 13 || range == 14)
			dirTxt = getContext().getString(R.string.NW);

		radarPoints.view = this;
		dw.paintObj(radarPoints, rx, ry, -state.getCurBearing(), 1);
		dw.setFill(false);
		dw.setColor(Color.argb(150, 0, 0, 220));
		dw.paintLine(lrl.x, lrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS + 15);
		dw.paintLine(rrl.x, rrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS + 15);
		dw.setColor(Color.rgb(255, 255, 255));
		dw.setFontSize(12);

		radarText(dw, MixUtils.formatDist(radius * 10), rx
				+ RadarPoints.RADIUS, ry + RadarPoints.RADIUS * 2 - 10, false);
		radarText(dw, "" + bearing + ((char) 176) + " " + dirTxt, rx
				+ RadarPoints.RADIUS, ry - 5, true);
	}
	
	
	/**
	 * Handles the key events of the AR view
	 * @param evt - the event of the user.
	 */
	private void handleKeyEvent(KeyEvent evt) {
		/** Adjust marker position with keypad */
		final float CONST = 10f;
		switch (evt.keyCode) {
		case KEYCODE_DPAD_LEFT:
			addX -= CONST;
			break;
		case KEYCODE_DPAD_RIGHT:
			addX += CONST;
			break;
		case KEYCODE_DPAD_DOWN:
			addY += CONST;
			break;
		case KEYCODE_DPAD_UP:
			addY -= CONST;
			break;
		case KEYCODE_DPAD_CENTER:
			frozen = !frozen;
			break;
		case KEYCODE_CAMERA:
			frozen = !frozen;
			break; // freeze the overlay with the camera button
		default: //if key is set, then ignore event
				break;
		}
	}

	/**
	 * Get the status of the clicked event
	 * @param evt - the click event.
	 */
	boolean handleClickEvent(ClickEvent evt) {
		boolean evtHandled = false;

		// Handle event
		if (state.nextLStatus == MixState.DONE) {
			// the following will traverse the markers in ascending order (by
			// distance) the first marker that
			// matches triggers the event.
			//TODO handle collection of markers. (what if user wants the one at the back)
			for (int i = 0; i < dataHandler.getMarkerCount() && !evtHandled; i++) {
				Marker pm = dataHandler.getMarker(i);
				evtHandled = pm.fClick(evt.x, evt.y, mixContext, state);
				
				loadConnectionWindow(evtHandled ,  pm);
			}
		}
		return evtHandled;
	}
	
	//------------------------
	//
	// Methods from Team For Jason
	//
	//------------------------	
	/**
	 * This method will create a "pop-up" window with the wifi details. 
	 * The marker has to be clicked first.
	 * 
	 * @param evt - the status of the event
	 * @param marker -  the marker which the event happened on*/
	private void loadConnectionWindow(boolean evt, Marker marker){
		if(evt == true){
			
			System.out.println("Marker: " + marker.getTitle());	
			try {
				final String ssid  = marker.getTitle();
				
				StringBuilder content = new StringBuilder();
				content.append("SSID: " + ssid);
				content.append("\n");
				content.append("Signal Strength: " + marker.getSignal() );
				
//				AlertDialog.Builder connectWindow = new AlertDialog.Builder(mixContext); 
				
				
				//Setting the pop up window/ toast
				LayoutInflater inflater = activity.getLayoutInflater();
				
				if(inflater.equals(null)){
					mixContext.doPopUp(content.toString());
				}
				View layout = inflater.inflate(R.layout.connection,
						  (ViewGroup) activity.findViewById(R.id.connection_layout_id));
				layout.setBackgroundColor(Color.LTGRAY);
				
				TextView text = (TextView) layout.findViewById(R.id.WifiDetail);
				text.setText(content);
				
				Toast toast = new Toast(mixContext);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Drawing of the radar text on the the screen. 
	 * @param dw - paint screen instance for drawing
	 * @param txt - text of the radar 
	 * @param x - the x location
	 * @param y - the y location
	 * @param bg - boolean status to draw the text, if true draw*/
	private void radarText(PaintScreen dw, String txt, float x, float y, boolean bg) {
		float padw = 4, padh = 2;
		float w = dw.getTextWidth(txt) + padw * 2;
		float h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
		if (bg) {
			dw.setColor(Color.rgb(0, 0, 0));
			dw.setFill(true);
			dw.paintRect(x - w / 2, y - h / 2, w, h);
			dw.setColor(Color.rgb(255, 255, 255));
			dw.setFill(false);
			dw.paintRect(x - w / 2, y - h / 2, w, h);
		}
		
		dw.paintText(padw + x - w / 2, padh + dw.getTextAsc() + y - h / 2, txt,
				false);
	}
	/**
	 * Creating a click event and adding it to the event list.
	 * @param x - the x location
	 * @param y - the y location
	 */
	public void clickEvent(float x, float y) {
		synchronized (uiEvents) {
			uiEvents.add(new ClickEvent(x, y));
		}
	}
	/**
	 * Creating a key event and adding it to the event list.
	 * @param keyCode -  the key code 
	 */
	public void keyEvent(int keyCode) {
		synchronized (uiEvents) {
			uiEvents.add(new KeyEvent(keyCode));
		}
	}
	/**
	 * Clear the event list.
	 */
	public void clearEvents() {
		synchronized (uiEvents) {
			uiEvents.clear();
		}
	}

	/**
	 * Cancel the timer 
	 * */
	public void cancelRefreshTimer() {
		if (refresh != null) {
			refresh.cancel();
		}
	}
	
	/**
	 * Re-downloads the markers, and draw them on the map.
	 */
	public void refresh(){
		state.nextLStatus = MixState.NOT_STARTED;
	}
	/**
	 * Call the refresh toast. Toast is the "pop up" in the AR view.
	 * */
	private void callRefreshToast(){
		mixContext.getActualMixView().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(
						mixContext,
						mixContext.getResources()
								.getString(R.string.refreshing),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}

//------------------------
//
// Inner Class by mixare.
//
//------------------------	

/**UI event class*/
class UIEvent {
	public static final int CLICK = 0;
	public static final int KEY = 1;

	public int type;
}
/**Click event class*/
class ClickEvent extends UIEvent {
	public float x, y;

	public ClickEvent(float x, float y) {
		this.type = CLICK;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
/**Key event class*/
class KeyEvent extends UIEvent {
	public int keyCode;

	public KeyEvent(int keyCode) {
		this.type = KEY;
		this.keyCode = keyCode;
	}

	@Override
	public String toString() {
		return "(" + keyCode + ")";
	}
}
