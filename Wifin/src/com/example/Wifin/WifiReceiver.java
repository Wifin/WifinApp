package com.example.Wifin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.SimpleAdapter;

public class WifiReceiver extends BroadcastReceiver
{

	List<ScanResult> wifilist;
    double ap_rssi;	
    double ap_feq;    
    double distance;
    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist;
    SimpleAdapter adapter;
    int size = 0;
    myWifi mwifi;
    	
	
	@Override
	public void onReceive(Context c, Intent i) {
	    // TODO Auto-generated method stub		
		
		//mwifi=new myWifi();
		
		if (i.getAction().equals(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		{
			wifilist = mwifi.wifi.getScanResults();
			size =wifilist.size();
		}
			
		
	}
	
	public void getwifilist()
	{	
				
		System.out.println("444");
     
		arraylist = new ArrayList<HashMap<String, String>>();
		size = size - 1;
        while (size >= 0) 
    	{		    
		
		    arraylist.clear();	
		    ap_rssi = Double.valueOf(wifilist.get(size).level);
		    ap_feq = Double.valueOf(wifilist.get(size).frequency);
		
		    distance=Math.pow(10, (Math.abs(ap_rssi)-20*Math.log10(ap_feq)-32.44)/20);
		
		    System.out.println("distance:"+(Math.round(distance*100))/100.0);
		    System.out.println("555");
            	
            HashMap<String, String> item = new HashMap<String, String>();
			item.put(ITEM_KEY, wifilist.get(size).SSID+""+(Math.round(distance*100))/100.0);
			arraylist.add(item);
			size--;
            adapter.notifyDataSetChanged();		
    	  }
        
		
		
	    //implement push data below here			
	}
	

}
