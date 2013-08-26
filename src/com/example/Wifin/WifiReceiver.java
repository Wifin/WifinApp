package com.example.Wifin;

import java.util.HashMap;
import java.util.List;

import Database.databaseGetSet;
import DatabasePHP.databasePush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiReceiver extends BroadcastReceiver
{

	List<ScanResult> wifilist;
	int size=0;
	double ap_rssi;
	double ap_feq;
	double distance;
	String ssid;
	
	
    private databaseGetSet dgs = new databaseGetSet();
	private databasePush dp = new databasePush();
	
	@Override
	public void onReceive(Context c, Intent i) {
	    // TODO Auto-generated method stub	
//		System.out.println("111");
		if (i.getAction().equals(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifilist = ((WifiManager) c.getSystemService(Context.WIFI_SERVICE)).getScanResults();
		
		
		
	}
	
	public void getwifilist (MainActivity ma, myLocation loc)
	{
		if (wifilist!=null)
		{	
		try {
			size = wifilist.size()-1;
	        while (size >= 0) 
	    	{	
	        	//Some sort of mechanism is required to check for the BSSID
//	        	 if(!item.containsValue(ma.ITEM_KEY)){
	        		ap_rssi = Double.valueOf(wifilist.get(size).level);
	        		ap_feq = Double.valueOf(wifilist.get(size).frequency);
	        		ssid = String.valueOf(wifilist.get(size).SSID);
			    	distance=Math.pow(10, (Math.abs(ap_rssi)-20*Math.log10(ap_feq)-32.44)/20)*1000;
					
				  //  System.out.println("distance: "+(Math.round(distance*100))/100.0);
				    
		        	//input the data to the database using the get/set methods.
				   // System.out.println("MAC ADDRESS : " + (String)wifilist.get(size).BSSID);
				    
			    	dgs.setMacAddress((String)wifilist.get(size).BSSID);
		        	dgs.setSSID((String) wifilist.get(size).SSID);
		        	
		        	setDataBaseData(loc);
		        	//new updateDatabase().execute("");
		        	HashMap<String, String> item = new HashMap<String, String>();
		        	item.put(ma.ITEM_KEY, wifilist.get(size).SSID +"   "+"Distance:"+(Math.round(distance*100))/100.0+"m");
		        	
					if(ma.arraylist.size() < 4 ){
						ma.arraylist.add(item);
						//size--;	
					}//end of if arraysize < 5
					
					System.out.println(ma.arraylist);
					size--;	
					
						
//		    	  }// end of if(item.contain) 
			    	
			    }
			}
			catch (Exception e) {

			}
		}
	}
	
	private void setDataBaseData(myLocation mlocal){
    	dgs.setLongtitude("" + mlocal.mCurrentLocation.getLongitude());
    	dgs.setLatitude("" + mlocal.mCurrentLocation.getLatitude());
    	System.out.println("Wifi Details: " + dgs.getMacAddress() + ", " + dgs.getSSID()  + ", " + dgs.getLatitude()
    			 + ", " + dgs.getLongtitude() + ", "+ dgs.getLocation());

    	new updateDatabase().execute("");
    }
	 
	private class updateDatabase extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//System.out.println("ASYNCTASK");
			String url = "https://deco3801-007.uqcloud.net/phpmyadmin/insertDB.php";
			dp.postInsertData(dgs);
				
		//	System.out.println("ASYNCTASK done");
			return "";
		}// end of doInBackground
	    	

		
	    }

}
