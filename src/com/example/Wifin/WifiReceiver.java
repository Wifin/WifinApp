package com.example.Wifin;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import Database.databaseGetSet;
import DatabasePHP.databasePull;
import DatabasePHP.databasePush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiReceiver extends BroadcastReceiver //implements Comparator<ScanResult>
{

	/**
	 * List to store the wifi detected. 
	 * */
	List<ScanResult> wifilist;
	
	/**
	 * Counter for the calculation of the distance
	 * */
	int size=0;
	
	/**
	 * the Access point of the RSSI, MacAddress ??
	 * */
	double ap_rssi;
	
	/**
	 * the Access point of the frequency ??
	 * */
	double ap_feq;
	
	/**
	 * Variable to store distance
	 * */
	double distance;
	
	/**
	 * Variable to store SSID
	 * */
	String ssid;
	
	/**
	 * An instance of DatabaseGetSet
	 * */
    private databaseGetSet dgs = new databaseGetSet();
    
    /**
	 * An instance of databasePush
	 * */
	private databasePush dp = new databasePush();
	
	 /**
		 * An instance of databasePush
		 * */
		private databasePull dpull = new databasePull();
		
	/** 
	 * onReceive listener
	 * check if received any WiFi result, then stored them to wifilist
	 * 
	 * @param wifilist - list for WiFi scan result
	 */
	@Override
	public void onReceive(Context c, Intent i) {
	    // TODO Auto-generated method stub	
		if (i.getAction().equals(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifilist = ((WifiManager) c.getSystemService(
				Context.WIFI_SERVICE)).getScanResults();
		//Collections.sort(wifilist);
	}
	
	/**
	 * Check if wifilist is empty, if not,
	 * collect WiFi data and calculate distance between for each AP
	 * 
	 * @param ma - for call MainActivity Activity
	 * @param loc - the interface of myLocation
	 * 
	 */
	public void getwifilist (MainActivity ma, myLocation loc){
		// this loop is for calculating distance & putting them 
		// with WiFi information to arraylist
		if (wifilist!=null)
		{	
		try {
			size = wifilist.size()-1;
	        while (size >= 0) 
	    	{	
	        	//get signal strength from wifilist    
	        	ap_rssi = Double.valueOf(wifilist.get(size).level);
	        	
	        	//get signal frequency from wifilist    
	        	ap_feq = Double.valueOf(wifilist.get(size).frequency);
	        	ssid = String.valueOf(wifilist.get(size).SSID);
	        	
	        	//calculate distance between you and AP
			    distance = Math.pow(10, (Math.abs(ap_rssi)-
			    			20*Math.log10(ap_feq)-32.44)/20)*1000;
			    
			    //rounding off the distance to 2 decimal point.
			    distance = (Math.round(distance*100))/100.0;

			    //The set methods to pass the wifi details to the database.
			   	dgs.setMacAddress(""+ wifilist.get(size).BSSID);
			   	dgs.setDistance("" + distance);
			   	dgs.setSignalStrength( "" + ap_feq);
		       	dgs.setSSID((String) wifilist.get(size).SSID);
		       	//Calling the method to pass the lat/long.
		       	setDataBaseData(loc);

		       	//Create a hashmap and store wifi information
		       	HashMap<String, String> item = new HashMap<String, String>();
		       	item.put(ma.ITEM_KEY, wifilist.get(size).SSID + wifilist.get(size).level 
		       			+"\n"+"Distance:"+  distance +"m");
		        System.out.println();
				if(ma.arraylist.size() < 4 ){
					ma.arraylist.add(item);
				}//end of if arraysize < 5
				size--;	
			    }// end of while loop
			}
			catch (Exception e) {
			}
		}//end of if loop
	}//end of getwifilist()
	/**
	 * This method will perform the updating of the database. 
	 * 
	 * @param mlocal - instance of myLocation
	 * */
	private void setDataBaseData(myLocation mlocal){
		//Passing the values of longitude and latitude for database.
    	dgs.setLongtitude("" + mlocal.mCurrentLocation.getLongitude());
    	dgs.setLatitude("" + mlocal.mCurrentLocation.getLatitude());
    	
    	//Calling the Async class to execute the connection to database.
    	new updateDatabase().execute("");;
    }
	
	/**
	 * The private class to perform the updating of the database activity 
	 * */
	private class updateDatabase extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//Calling the postInsertData method which will update the database.
			dp.postInsertData(dgs);
			return null;
		}// end of doInBackground
	}//end of updateDatabase class
	
	/**
	 * The private class to perform the updating of the database activity 
	 * */
	private class queryDatabase extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//Calling the pullQueryDB method which will update the database.
			dpull.pullQueryDB();
			return null;
		}// end of doInBackground
	}//end of queryDatabase class

}
