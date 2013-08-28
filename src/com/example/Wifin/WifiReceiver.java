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
	public void getwifilist (MainActivity ma, myLocation loc)
	{
		// need to explain briefly this loop does what ? 
		if (wifilist!=null)
		{	
		try {
			size = wifilist.size()-1;
	        while (size >= 0) 
	    	{	
	        	//DG: Some sort of mechanism is required to check for the BSSID
//	        	 if(!item.containsValue(ma.ITEM_KEY)){
	        	
	        	//All this also need to explain abit
	        	ap_rssi = Double.valueOf(wifilist.get(size).level);
	        	ap_feq = Double.valueOf(wifilist.get(size).frequency);
	        	ssid = String.valueOf(wifilist.get(size).SSID);
			    distance = Math.pow(10, (Math.abs(ap_rssi)-
			    			20*Math.log10(ap_feq)-32.44)/20)*1000;
			    //rounding off the distance to 2 decimal point.
			    distance = (Math.round(distance*100))/100.0;
			    System.out.println("Distance : "  +distance);
			    
		       	//DG: input the data to the database using the get/set methods.	 
			   	dgs.setMacAddress((String)wifilist.get(size).BSSID);
		       	dgs.setSSID((String) wifilist.get(size).SSID);
		       	setDataBaseData(loc);

		       	HashMap<String, String> item = new HashMap<String, String>();
		       	item.put(ma.ITEM_KEY, wifilist.get(size).SSID + wifilist.get(size).level +"\n"+"Distance:"+  distance +"m");
		        System.out.println();
				if(ma.arraylist.size() < 4 ){
					ma.arraylist.add(item);
				//	Collections.sort(ma.arraylist);
				}//end of if arraysize < 5
				size--;	
//		    	  }// end of if(item.contain) 
			    }// end of while loop
			}
			catch (Exception e) {
			}
		}//end of if loop
	}//end of getwifilist()
	
//	class compareSignalStrength implements Comparator<ScanResult>{
//
//		@Override
//		public int compare(ScanResult lhs, ScanResult rhs) {
//			// TODO Auto-generated method stub
//			return (lhs.level < rhs.level ? -1 : (lhs.level==rhs.level ? 0 : 1));
//		}
//	}
	
	/** 
	 * ScanResult sorted by signal level
	 * 
	 */
//	@Override
//	public int compare(ScanResult lhs, ScanResult rhs) {
//		// TODO Auto-generated method stub
//		return (lhs.level < rhs.level ? -1 : (lhs.level==rhs.level ? 0 : 1));
//	}
	/**
	 * This method will perform the updating of the database. 
	 * 
	 * @param mlocal - instance of myLocation
	 * */
	private void setDataBaseData(myLocation mlocal){
    	dgs.setLongtitude("" + mlocal.mCurrentLocation.getLongitude());
    	dgs.setLatitude("" + mlocal.mCurrentLocation.getLatitude());
    	
    	new updateDatabase().execute("");
    	
    	System.out.println("Wifi Details: " + dgs.getMacAddress() + ", " + dgs.getSSID()  + ", " + dgs.getLatitude()
    			 + ", " + dgs.getLongtitude() + ", "+ dgs.getLocation());

    	
    }
	
	/**
	 * The private class to perform the updating of the database activity 
	 * */
	private class updateDatabase extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//System.out.println("ASYNCTASK");
//			String url = "https://deco3801-007.uqcloud.net/phpmyadmin/insertDB.php";
			
			dp.postInsertData(dgs);
				
		//	System.out.println("ASYNCTASK done");
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
			//System.out.println("ASYNCTASK");
//			String url = "https://deco3801-007.uqcloud.net/phpmyadmin/insertDB.php";
			
			dpull.pullQueryDB();
				
		//	System.out.println("ASYNCTASK done");
			return null;
		}// end of doInBackground
	}//end of updateDatabase class

}
