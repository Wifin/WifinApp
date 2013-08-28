package com.example.Wifin;

import android.net.wifi.WifiManager;

public class myWifi
{
	WifiManager wifi;
	WifiReceiver receiverWifi;
	
	public void scanWifi()
	{	
		
		OpenWifi();
		wifi.startScan();
		System.out.println("wifiscan= " + wifi.startScan());
		
		
	}
	
	public void OpenWifi()
	{
		if (!wifi.isWifiEnabled())
		{
			wifi.setWifiEnabled(true);
		}
	}
		
}
