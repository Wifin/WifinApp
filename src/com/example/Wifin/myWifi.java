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

public class myWifi
{
	WifiManager wifi;
	List<ScanResult> wifilist;
	int size = 0;
	double ap_rssi;	
    double ap_feq;    
    double distance;
    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
	
	public void scanWifi()
	{
		
		OpenWifi();
		wifi.startScan();
		System.out.println("333" + wifi.startScan());
		
	}
	
	public void OpenWifi()
	{
		if (!wifi.isWifiEnabled())
		{
			wifi.setWifiEnabled(true);
		}
	}
	
	class myReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			{
				wifilist = wifi.getScanResults();
			}
			
		}
		
	}
	
	
	public void getwifilist()
	{	
		
        for(int i = 0; i < wifilist.size(); i++)
        {		    
		
		    arraylist.clear();	
		    ap_rssi = Double.valueOf(wifilist.get(i).level);
		    ap_feq = Double.valueOf(wifilist.get(i).frequency);
		
		    distance=Math.pow(10, (Math.abs(ap_rssi)-20*Math.log10(ap_feq)-32.44)/20);
		
		    System.out.println("distance:"+(Math.round(distance*100))/100.0);
		    System.out.println("555");
            	
            HashMap<String, String> item = new HashMap<String, String>();
			item.put(ITEM_KEY, wifilist.get(i).SSID+""+(Math.round(distance*100))/100.0);
			arraylist.add(item);
            adapter.notifyDataSetChanged();		
    	  }
	}


	
}
