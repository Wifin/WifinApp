package com.example.Wifin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/** 
 * Main activity java, the one contains home layout and all viewed elements
 */
public class MainActivity extends Activity
{   
    /**
     * create a arraylist to store wifi information
     */
    ArrayList<HashMap<String, String>> arraylist;
    
    /**
     * hashmap key
     */
    String ITEM_KEY;
    
    /**
     * create simple adapter to update all wifi info to listview (each array by line)
     */
    SimpleAdapter adapter;
    
    /**
     * button for activate AR camera
     */
    ImageButton btn_cam;
    
    ImageButton btn_navi;
    
	/**
     * button for enable update location services
     */
    ImageButton btn_map;
    
    myWifi mwifi;
    
    DataBaseHelper dbhelp;
    
    Context c= this;
    
    TextView wifiText;
    
    /**
     * Base on content view Wifin/res/layout/activity_main.xml, execute once program run
     */
    @Override
	public void onCreate(Bundle savedInstanceState)
	{	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//btn_cam = (ImageButton) findViewById(R.id.button_camera);
		//btn_map = (ImageButton) findViewById(R.id.button_map);
		wifiText = (TextView) findViewById(R.id.text_wifi);
        
        dbhelp=new DataBaseHelper(c);
        mwifi=new myWifi();
	    mwifi.wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    WifiInfo wifiInfo = mwifi.wifi.getConnectionInfo();
        
        try {
			   dbhelp.createDataBase();
			}
		catch (IOException e) {
			e.printStackTrace();
		}   
		
		if(dbhelp.checkDataBase()){
		    mwifi.scanWifi(c);	    		
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Connected to: "+wifiInfo.getSSID()+"\n");
		sb.append("Signal Strenth: "+wifiInfo.getRssi()+"dBm"+"\n");
		sb.append("Link Speed: "+wifiInfo.getLinkSpeed()+"Mbps");
		
		wifiText.setText(sb);
        
	}
    
    public void mapClickEvent(View view) {
    	//onClick link to Google Map activity
    	Intent i = new Intent(MainActivity.this, myMap.class);
        startActivity(i);
    }
    
    public void cameraClickEvent(View view) {
    	
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        //url of our JSON file.
        String url = "file:///storage/emulated/0/Android/data/com.example.Wifin/cache/testJson.json";
        i.setDataAndType(Uri.parse(url), "application/mixare-json");
        startActivity(i);
    }
    
	@Override
    protected void onResume() {
    	registerReceiver(mwifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }
    
    @Override
    protected void onPause() 
  	{
    	unregisterReceiver(mwifi);
        super.onPause();
      }
    
    
    
    /** 
	 * option menu
	 * **
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}