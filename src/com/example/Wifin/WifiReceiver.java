package com.example.Wifin;

import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver
{

	List<ScanResult> wifilist;
	int size=0;
	double ap_rssi;
	double ap_feq;
	double distance;
	
	
	@Override
	public void onReceive(Context c, Intent i) {
	    // TODO Auto-generated method stub	
		System.out.println("111");
		if (i.getAction().equals(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifilist = ((WifiManager) c.getSystemService(Context.WIFI_SERVICE)).getScanResults();
		
	}
	
	public void getwifilist (MainActivity ma)
	{
		if (wifilist!=null)
		{	
		
		try {
			size =wifilist.size()-1;
	        while (size >= 0) 
	    	{		    
			
	        	ma.arraylist.clear();	
			    ap_rssi = Double.valueOf(wifilist.get(size).level);
			    ap_feq = Double.valueOf(wifilist.get(size).frequency);
			
			    distance=Math.pow(10, (Math.abs(ap_rssi)-20*Math.log10(ap_feq)-32.44)/20);
			
			    System.out.println("distance:"+(Math.round(distance*100))/100.0);
	            	
	            HashMap<String, String> item = new HashMap<String, String>();
				item.put(ma.ITEM_KEY, wifilist.get(size).SSID+""+(Math.round(distance*100))/100.0);
				ma.arraylist.add(item);
				size--;	
	    	  }
			}
			catch (Exception cat) {

			}
		}
	}

}
