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

import java.text.DecimalFormat;

import org.mixare.LocalMarker;
import org.mixare.lib.MixUtils;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.TextObj;

import android.graphics.Color;
import android.graphics.Path;
import android.location.Location;

/**
 * This markers represent the points of interest.
 * On the screen they appear as circles, since this
 * class inherits the draw method of the Marker.
 * 
 * @author hannes 
 * @author Xu Duangui - edited to suits the needs of WIFIN
 * 
 */
public class POIMarker extends LocalMarker {

	//------------------------
	//
	// VARIABLES USED FOR POI MARKER
	//
	//------------------------

	//------------------------
	// PRIAVTE VARIABLES
	//------------------------
	private static Double sig;
	private static int color;

	public static final int MAX_OBJECTS = 20;
	public static final int OSM_URL_MAX_OBJECTS = 5;
	
	/** 
	 * Constructor for POI Marker
	 * @param id - Wifi Mac Address
	 * @param title - Wifi Name
	 * @param latitude - Wifi Latitude
	 * @param longtitude - Wifi Longtitude
	 * @param altitude - Wifi altitude 
	 * @param link - URL link for wifi details if there is any 
	 * @param signal -  signal strength for wifi
	 * @param type - type of wifi 
	 * @param color - color for the markers. */
	public POIMarker(String id, String title, double latitude, double longitude,
			double altitude, String URL, String signal, int type, int color) {	
		super(id, title, latitude, longitude, altitude, URL, signal , type, color);
	}

	@Override
	public void update(Location curGPSFix) {
		super.update(curGPSFix);
	}

	@Override
	public int getMaxObjects() {
		return MAX_OBJECTS;
	}
	
	/**
	 * The draw circle method will draw the markers on to the AR screen. This will 
	 * override the drawCircle in LocalMarker which in turn 
	 * override the drawCircle in Marker
	 * 
	 * @param dw - paint screen instance for drawing
	 * */
	@Override
	public void drawCircle(PaintScreen dw) {
		if (isVisible) {
			float maxHeight = dw.getHeight();
			dw.setStrokeWidth(maxHeight / 100f);

				dw.setColor(Color.RED);
				dw.setFill(true);
			// draw circle with radius depending on distance
			// 0.44 is approx. vertical fov in radians
//			double angle = 2.0 * Math.atan2(10, distance);
			double radius = 75;
					
//					Math.min(
//					Math.min(angle / 0.44 * maxHeight, maxHeight),
//					maxHeight / 25f);
		
			// drawing of the circles based on distance and signal
			drawCircleWithInput(dw, radius);
		}
	}
	/**
	 * This method will draw the markers based on the distance from users to the wifi access points. 
	 * And the signal strength of the access points is also considered. 
	 * 
	 * @param dw - paint screen instance for drawing
	 * @param radius -  the radius for the marker
	 * */
	private void drawCircleWithInput(PaintScreen dw, double radius){
			sig = Double.valueOf(signal);
			/*
			 * Signal strength to determine color of the marker.
			 * - boundary to be set over here. */
			if (sig >= -65.0 && sig <= 0){   // 0 - 65
				color = 0x8099CC00;
				dw.setColor(color);
				dw.setFill(true);
				
			}else if (sig < -65.0 && sig > -80.0){    //66-80
				color = 0x80ffbb33;
				dw.setColor(color);
				dw.setFill(true);
				
			}
			else if (sig <= -80.0){ //80 and above
				color = 0x80ff4444;
//				dw.setColor(Color.RED);
				dw.setFill(true);
			}
			
			/*
			 * Distance to determine the size of the marker. 
			 * - boundary to be set over here. */
			if(distance >= 1 && distance < 5){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius* 1.5));
			} 
			else if(distance >= 5 && distance < 10){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius*2));
			} 
			else if(distance >= 10 && distance < 20){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius*2.5));
			} 
			else if(distance >= 20 && distance < 30){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius*3));
			} 
			else if(distance >= 30 && distance < 60){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius*3.5));
			} 
			else if(distance >= 60 && distance < 100){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius*4));
			} 
			else if(distance >= 100 && distance < 150){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius*4.5));
			} 
			else if(distance >= 150 && distance < 200){
				dw.paintCircle(cMarker.x, cMarker.y, (float) (radius*5));
			} 
	}
	/**
	 * This method will draw the text box that corresponds to the marker. 
	 * @param dw - paint screen to draw the text block
	 * */
	@Override
	public void drawTextBlock(PaintScreen dw) {
		float maxHeight = Math.round(dw.getHeight() / 10f) + 1;
		// TODO: change textblock only when distance changes

		String textStr = "";

		double d = distance;
		DecimalFormat df = new DecimalFormat("@#");

		if (d < 500.0) {
			
			textStr = title + " (" +  signal + " dBm)";
//			textStr = title;
		} 

		textBlock = new TextObj(textStr, Math.round(maxHeight / 2f) + 1, 250,
				dw, underline);

		if (isVisible) {
			// based on the distance set the colour
			if (sig < 50.0) {
				textBlock.setBgColor(Color.argb(128, 52, 52, 52));
				textBlock.setBorderColor(Color.rgb(255, 104, 91));
			} else {
				textBlock.setBgColor(Color.argb(128, 0, 0, 0));
				textBlock.setBorderColor(Color.rgb(255, 255, 255));
			}
			//dw.setColor(DataSource.getColor(type));

			float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y,
					signMarker.x, signMarker.y);
			txtLab.prepare(textBlock);
			dw.setStrokeWidth(1f);
			dw.setFill(true);
			dw.paintObj(txtLab, signMarker.x - txtLab.getWidth() / 2,
					signMarker.y + maxHeight, currentAngle + 90, 1);

		}
	}
	
	/**
	 * This method will draw an arrow on the screen. 
	 * @param dw - paint screen to draw the text block
	 * */
	public void drawArrow(PaintScreen dw) {
		if (isVisible) {
//			System.out.println("Arrow is drawing");
			float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y, signMarker.x, signMarker.y);
			float maxHeight = Math.round(dw.getHeight() / 10f) + 1;

			//dw.setColor(DataSource.getColor(type));
			dw.setStrokeWidth(maxHeight / 10f);
			dw.setFill(false);
			
			Path arrow = new Path();
			float radius = maxHeight / 1.5f;
			float x=0;
			float y=0;
			arrow.moveTo(x-radius/3, y+radius);
			arrow.lineTo(x+radius/3, y+radius);
			arrow.lineTo(x+radius/3, y);
			arrow.lineTo(x+radius, y);
			arrow.lineTo(x, y-radius);
			arrow.lineTo(x-radius, y);
			arrow.lineTo(x-radius/3,y);
			arrow.close();
			
			dw.paintPath(arrow,cMarker.x,cMarker.y,radius*2,radius*2,currentAngle+90,1);			
		}
	}

	

}
