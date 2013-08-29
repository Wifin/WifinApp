/** 
 * Scan WiFi
 * **
 */
package com.example.Wifin;

import android.net.wifi.WifiManager;

public class myWifi
{
	/** 
	 * Variable for managing all wifi aspects of WiFi connectivity
	 * **
	 */
	WifiManager wifi;
	
	/** 
	 * the method to start scan WiFi
	 * **
	 */
	public void scanWifi()
	{	
		
		OpenWifi();
		wifi.startScan();
		System.out.println("wifiscan=" + wifi.startScan());
		
		
	}
	
	/** 
	 * the method to enable wifi on android device
	 * 
	 * @param true - return true for setWifiEnable
	 * **
	 */
	public void OpenWifi()
	{
		if (!wifi.isWifiEnabled())
		{
			wifi.setWifiEnabled(true);
		}
	}
	
		
}