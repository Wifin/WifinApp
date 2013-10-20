package com.example.Wifin;

import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.widget.EditText;

public class wifiConnect {
	
	public void connectWPA(EditText password,String ssid,Context c) 
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
	
	public void connectEAP(EditText username,EditText password,String ssid,Context c)
	{
	    WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig(); 
	    WifiConfiguration wifiConfig = new WifiConfiguration();
		wifiConfig.SSID = ssid; 
		wifiConfig.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
		wifiConfig.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
		enterpriseConfig.setIdentity("\""+ username +"\"");
		enterpriseConfig.setPassword("\""+ password +"\"");
		enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP); 
		wifiConfig.enterpriseConfig = enterpriseConfig;
		
	}
}
