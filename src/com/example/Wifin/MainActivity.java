package com.example.Wifin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/** 
 * Main activity java, the one contains home layout and all viewed elements
 */
public class MainActivity extends Activity 
{
	/**
     * textview for display user location
     */
    TextView txtlocation;

	/**
     * textview for display ap location
     */
    TextView txtaploc;
   
    /**
     * listview for display WiFi information
     */
    ListView wifi_lv;
    
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
    
	/**
     * button for enable update location services
     */
    ImageButton btn_map;
    
    myWifi mwifi;
    
    DataBaseHelper dbhelp;
    
    /**
     * Base on content view Wifin/res/layout/activity_main.xml, execute once program run
     */
    @Override
	public void onCreate(Bundle savedInstanceState)
	{	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_cam = (ImageButton) findViewById(R.id.button_camera);
		btn_map = (ImageButton) findViewById(R.id.button_map);
		
		dbhelp = new DataBaseHelper(this);
		
		try {
			   dbhelp.createDataBase();
			}
	    catch (IOException e) {
			e.printStackTrace();
		}   
		
		if(dbhelp.checkDataBase()){
			mwifi=new myWifi();
	        mwifi.wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
	        mwifi.scanWifi(this); 
		}
		else{System.out.println("got problem to download database");}
        
		/** 
		 * following are three button onClick Listener
		 * **
		 */
		btn_cam.setOnClickListener(new View.OnClickListener() {
		    
		    @Override
	        public void onClick(View v) 
		    {						    	
		    	  Intent i = new Intent();
		    	  i.setAction(Intent.ACTION_VIEW);
		    	  //url of our JSON file.
		    	  String url = "file:///storage/emulated/0/Android/data/com.example.Wifin/cache/testJson.json";
		    	  i.setDataAndType(Uri.parse(url), "application/mixare-json");
		    	  startActivity(i);
			 }
		 });
		
        btn_map.setOnClickListener(new View.OnClickListener() {
		    
		    @Override
	        public void onClick(View v) 
		    {	
		    	//onClick link to Google Map activity
		    	Intent i = new Intent(MainActivity.this, myMap.class);
		        startActivity(i);
			 }
		 });		    		    
	    	    	  	  	    
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