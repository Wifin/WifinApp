package com.example.Wifin;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
		    	  String url = "http://deco3801-007.uqcloud.net/mixare/testJSON.json";
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