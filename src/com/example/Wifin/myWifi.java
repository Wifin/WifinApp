/** 
 * Scan WiFi
 * **
 */
package com.example.Wifin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class myWifi extends BroadcastReceiver
{
	/** 
	 * Variable for managing all wifi aspects of WiFi connectivity
	 * **
	 */
	WifiManager wifi;
	ProgressDialog dialog;
	List<ScanResult> wifilist;
	private static int size=0;
    private static int count=0;
    jsonWriter jw = new jsonWriter();
    /** 
	 * FileOutputStream for Json
	 * **
	 */
    FileOutputStream out;
    /** 
	 * path and filename for store Json
	 * **
	 */
    File file;
	
	/** 
	 * the method to start scan WiFi
	 * @param c - indicator
	 * **
	 */
	public void scanWifi(Context c)
	{	
		
		OpenWifi();
		wifi.startScan();
		dialog = ProgressDialog.show(c,null, "Scanning...");
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

	@Override
	public void onReceive(Context c, Intent i) {
		DataBaseHelper dbhelp = new DataBaseHelper(c);
		if (i.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
		{	
			wifilist = new ArrayList<ScanResult>();
			wifilist = ((WifiManager) c.getSystemService(Context.WIFI_SERVICE)).getScanResults();
		    dialog.dismiss();
            file = new File(c.getFilesDir(),"testJson.json");
            System.out.println("Dir of testjSon:"+c.getFilesDir());
			try {
				out = new FileOutputStream(file);				
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		    int listnum = wifilist.size();
	        
	        if(listnum!=0)
	        {
	        	size = listnum-1;
	        	//JSONObject jo = new JSONObject();
	        	while (count <= size) 
	        	{
	        		Cursor cur = dbhelp.wifinquery(wifilist.get(count).BSSID,wifilist.get(count).SSID);
	        		if (cur.getCount()!=0){
	        		String mac = cur.getString(cur.getColumnIndex("mac"));
	        		String ssid = cur.getString(cur.getColumnIndex("ssid"));
	        		Double lat = cur.getDouble(cur.getColumnIndex("lat"));
	        		Double lon = cur.getDouble(cur.getColumnIndex("lon"));
	        		try {
						jw.writeJson(out, listnum, ssid, mac, lat, lon);
					} catch (IOException e) {
						e.printStackTrace();
					}
	        		cur.close();
	        		count++;}
	        		else{count++;}
	        	}
	        	//close writer after loop
	        	try {
					jw.writer.close();
				} catch (IOException e) {
				}
	        }
	        else{System.out.println("no wifi detected");}
		}
	}
	
		
}