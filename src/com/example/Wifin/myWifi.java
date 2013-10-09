/** 
 * Scan WiFi
 * **
 */
package com.example.Wifin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.JsonWriter;

public class myWifi extends BroadcastReceiver
{
	/** 
	 * Variable for managing all wifi aspects of WiFi connectivity
	 * **
	 */
	WifiManager wifi;
	ProgressDialog dialog;
	List<ScanResult> wifilist;
    JsonWriter writer;
    DataBaseHelper dbhelp;
    int count;
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
		if (i.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
		{
			dbhelp = new DataBaseHelper(c);
			wifilist = new ArrayList<ScanResult>();
			wifilist = ((WifiManager) c.getSystemService(Context.WIFI_SERVICE)).getScanResults();
		    dialog.dismiss();
            file = new File(c.getExternalCacheDir(),"testJson.json");
            System.out.println("this is where file stored:"+c.getExternalCacheDir());			
	        try
	        {
	        	System.out.println("this message Should been see first");
	        	out = new FileOutputStream(file);
	        	writeJson(out);
	        }
	        catch(IOException e)
	        {
	        	e.printStackTrace();
	            System.out.println("nothing been write!!");
	        }
		}
	}
	
	/** 
     * Write status,num_results,and call write json array method
	 * @param writer - JsonWriter variable
	 * @param c - indicator
	 * @param size- size of the discovered ap in db
	 * **
	 */	
	public void writeJson(OutputStream out) throws IOException {
	    writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
	    writer.setIndent("    ");
		jsonFinal(writer);}
	
	/** 
     * Write status,num_results,and call write json array method
	 * @param writer - JsonWriter variable
	 * @param c - indicator
	 * **
	 */	
	public void jsonFinal(JsonWriter writer) throws IOException{
			writer.beginObject();
		    writer.name("status").value("OK");
		    writer.name("num_results").value("0");
		    writer.name("results");
		    writeLocationArray(writer);
		    writer.endObject();
		    writer.close();
		 }
	
	/** 
	 * Write array information in Json file, loop method base on wifilist.size()
	 * 
	 * @param writer - JsonWriter variable
	 * @param ap - complex type of apinfo
	 * **
	 */
	public void writeLocationArray(JsonWriter writer) throws IOException {
		count = 0;
		writer.beginArray();
 		//determine whether query out something or not       	
		while(count < wifilist.size())
	    {
		    Cursor cur = dbhelp.wifinquery(wifilist.get(count).BSSID,wifilist.get(count).SSID);
		    if (cur.getCount()!=0)
		    {
		        String mac = cur.getString(cur.getColumnIndex("mac"));
	 		    String ssid = cur.getString(cur.getColumnIndex("ssid"));
	 		    Double lat = cur.getDouble(cur.getColumnIndex("lat"));
	 		    Double lon = cur.getDouble(cur.getColumnIndex("lon"));
	 		    int level = wifilist.get(count).level;
			    apinfo aps= new apinfo(lat,lon,ssid,level,mac);
     		    try 
     		    {
     		        writeLocation(writer,aps);
     		    }
     		    catch (IOException e)
     		    {
     			    e.printStackTrace();
     		    }
     		    cur.close(); 			
     	        count++;
		     }
		     else{count++;}
	     }
         writer.endArray();
         
	}
	
	/** 
     * WriteLocation to json file
	 * @param writer - JsonWriter variable
	 * @param ap - complex ap information
	 * **
	 */	
	public void writeLocation(JsonWriter writer,apinfo ap) throws IOException {
	     writer.beginObject();	     
	     writer.name("lat").value(String.valueOf(ap.getlat()));
	     writer.name("lng").value(String.valueOf(ap.getlon()));
	     writer.name("elevation").value("0");
	     writer.name("title").value(ap.gettitle());
	     writer.name("distance").value("20");
	     writer.name("has_detail_page").value("1");
	     writer.name("webpage").value("");
	     writer.name("level").value(ap.getlevel());
	     writer.name("mac").value(ap.getmac());
	     writer.endObject();
	 }
		
}