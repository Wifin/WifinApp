package com.example.Wifin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.JsonWriter;

/**
 * class for scan wifi
 */
public class  myWifi extends BroadcastReceiver{
	
	/** 
	 * Variable for managing all wifi aspects of WiFi connectivity
	 * **
	 */
	public WifiManager wifi;
	
	/** 
	 * dialog to show scan progressing
	 */
	private ProgressDialog dialog;
	
	/** 
	 * a list to store wifi scan result
	 */
	private List<ScanResult> wifilist;
	
	/** 
	 * create a json writer
	 */
	private JsonWriter writer;
    
    /** 
	 * recall data base helper 
	 */
    private DataBaseHelper dbhelp;
    
    /** 
	 * an increasing integer to record the loop times
	 */
    int count;
    
    /** 
	 * FileOutputStream for Json
	 */
    private FileOutputStream out;
    
    /** 
	 * path and filename for store Json
	 */
    private File file;
	
	/** 
	 * the method to start scan WiFi
	 * @param c - context indicator
	 */
	public void scanWifi(Context c){	
		
		//call open wifi method, check & enable wifi for your android device
		OpenWifi();
		
		//start scan wifi
		wifi.startScan();
		
		//show dialog box while scanning
		dialog = ProgressDialog.show(c,null, "Scanning...");
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

	/** 
	 * Receive wifi information, and write json file after receive
	 * @param c - context of receiver
	 * @param i - intent of this receiver
	 * **
	 */
	@Override
	public void onReceive(Context c, Intent i) {
		if (i.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
		{
			dbhelp = new DataBaseHelper(c);
			wifilist = new ArrayList<ScanResult>();
			wifilist = ((WifiManager) c.getSystemService(Context.WIFI_SERVICE)).getScanResults();
		    dialog.dismiss();
            file = new File(c.getExternalCacheDir(),"testJson.json");		
	        
            try
	        {
	        	out = new FileOutputStream(file);
	        	writeJson(out);
	        }
	        catch(IOException e)
	        {
	        	e.printStackTrace();
	            System.out.println("nothing been write!!");
	        }
	        
	        wifiRescan();
		}
	}	
	
	/** 
	 * set a timer rescan wifi once it receive the previously update
	 * **
	 */
	private void wifiRescan() 
    {
        Timer t = new Timer();
        TimerTask timerTask = new TimerTask()
        { 
            @Override
            public void run()
            {
            	wifi.startScan();
            }
        };
        
        // set 3s time interval
        t.schedule(timerTask, 3000);
    }
	
	/** 
     * main json write method to execute write json
     * @param out - the output stream indicated which and where you will write the json file
	 */	
	public void writeJson(OutputStream out) throws IOException {
	    writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
	    writer.setIndent("    ");
		jsonFinal(writer);}
	
	/** 
     * Write status,num_results,and call write json array method
	 * @param writer - variables of JsonWriter
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
	 */
	public void writeLocationArray(JsonWriter writer) throws IOException {
		count = 0;
		writer.beginArray();
 		//determine whether query out something or not
		while(count < wifilist.size()){
		    Cursor cur = dbhelp.wifinquery(wifilist.get(count).BSSID,wifilist.get(count).SSID);
		    if (cur.getCount()!=0){
		        String mac = cur.getString(cur.getColumnIndex("mac"));
	 		    String ssid = cur.getString(cur.getColumnIndex("ssid"));
	 		    Double lat = cur.getDouble(cur.getColumnIndex("lat"));
	 		    Double lon = cur.getDouble(cur.getColumnIndex("lon"));
	 		    int level = wifilist.get(count).level;
	 		    String ctype=wifilist.get(count).capabilities;
			    apinfo aps= new apinfo(lat,lon,ssid,level,mac,ctype);
     		    try {
     		    	writeLocation(writer,aps);
     		    }catch (IOException e){
     		    	e.printStackTrace();
     		    	}
     		    cur.close(); 			
     	        count++;}
		    else{
		    	count++;
		    	}
		    }
		writer.endArray();
		}
	
	/** 
     * Write location and other informations to json file
	 * @param writer - JsonWriter variable
	 * @param ap - complex ap information
	 */	
	public void writeLocation(JsonWriter writer,apinfo ap) throws IOException {
	     writer.beginObject();
	     writer.name("id").value(String.valueOf(count));
	     writer.name("lat").value(String.valueOf(ap.getlat()));
	     writer.name("lng").value(String.valueOf(ap.getlon()));
	     writer.name("elevation").value("0");
	     writer.name("title").value(ap.gettitle());
	     writer.name("distance").value("0");
	     writer.name("has_detail_page").value("1");
	     writer.name("webpage").value("");
	     writer.name("level").value(ap.getlevel());
	     writer.name("mac").value(ap.getmac());
	     writer.name("capabilities").value(ap.getctype());
	     writer.endObject();
	     }		
}