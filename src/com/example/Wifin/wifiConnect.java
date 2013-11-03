package com.example.Wifin;

import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.widget.EditText;

/**
 * class for connect wireless network function
 */
public class wifiConnect {
	
	/**
	 * method to connect WPA encryption(pre-share key) type of wireless network
	 * 
	 * @param password - the edited text for store password
	 * @param ssid - the edited text for store ssid name
	 * @param c - Context for wifiConnect class
	 */
	public void connectWPA(EditText password,String ssid,Context c) 
    {
    	//get ssid from marker title
    	String networkSSID = ssid;
    	
    	//initialize the wifi configuration
    	WifiConfiguration conf = new WifiConfiguration();
    	
    	//get ssid and password from edit text
    	conf.SSID = "\"" + networkSSID + "\"";
    	conf.preSharedKey = "\""+ password +"\"";
    	
    	//add configure to Android wifi manager
    	WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE); 
    	wifiManager.addNetwork(conf);
    	
    	List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
    	
    	//if it's connected to wifi, disconnect the current one, and reconnect to new wifi
    	for( WifiConfiguration i : list ) {
    	    if(i.SSID != null && i.SSID.equals(conf.SSID)) {
    	         wifiManager.disconnect();
    	         wifiManager.enableNetwork(i.networkId, true);
    	         wifiManager.reconnect();              
    	         
    	         break;
    	    }           
    	 }
    }
	
	/**
	 * method to connect EAP encryption type of wireless network
	 * Warning: this method required android API 18 to call, currently not using
	 * 
	 * @param password - the edited text for store password
	 * @param ssid - the edited text for store ssid name
	 * @param c - Context for wifiConnect class
	 */
	public void connectEAP(EditText username,EditText password,String ssid,Context c)
	{
	    //initialize the wifiEnterprise config
		WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig(); 
		
		//initialize the wifi configuration
	    WifiConfiguration wifiConfig = new WifiConfiguration();
	    
	    //get ssid from marker title
		wifiConfig.SSID = ssid;
		
		wifiConfig.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
		wifiConfig.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
		enterpriseConfig.setIdentity("\""+ username +"\"");
		enterpriseConfig.setPassword("\""+ password +"\"");
		enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP); 
		wifiConfig.enterpriseConfig = enterpriseConfig;
		
	}
}
