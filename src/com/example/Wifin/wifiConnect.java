package com.example.Wifin;

import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.EditText;

public class wifiConnect {
	public void wificonnector(EditText password,String ssid,Context c) 
    {
    	//get ssid from marker title
    	String networkSSID = ssid;
    	WifiConfiguration conf = new WifiConfiguration();
    	conf.SSID = "\"" + networkSSID + "\"";
    	conf.preSharedKey = "\""+ password +"\"";
    	//add conf to Android wifi manager
    	WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE); 
    	wifiManager.addNetwork(conf);
    	
    	List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
    	for( WifiConfiguration i : list ) {
    	    if(i.SSID != null && i.SSID.equals(conf.SSID)) {
    	         wifiManager.disconnect();
    	         wifiManager.enableNetwork(i.networkId, true);
    	         wifiManager.reconnect();              
    	         
    	         break;
    	    }           
    	 }
    }
}
