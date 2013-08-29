package com.example.Wifin;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
     * button for getting location and wifi information
     */
    Button btn_loc;
    

	/**
     * button for enable update location services
     */
    Button btn_ser;
    
    /**
     * button for get ap's location
     */
    Button bnt_aploc;
    
    /**
     * call service classes
     */
    myLocation mlocal;
    myWifi mwifi;
    WifiReceiver mreceiver;
    apLocation aploc;
    
    
    /**
     * Base on content view Wifin/res/layout/activity_main.xml, execute once program run
     */
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtlocation= (TextView) findViewById(R.id.locView);
		txtaploc = (TextView) findViewById(R.id.aplocView);
		wifi_lv =(ListView)findViewById(R.id.listView_wifi);
		arraylist = new ArrayList<HashMap<String, String>>();
		ITEM_KEY = "key";	
		adapter = new SimpleAdapter(this, arraylist, android.R.layout.simple_list_item_1,new String[] { ITEM_KEY },new int[] { android.R.id.text1 });
		btn_loc  = (Button) this.findViewById(R.id.button_location);
		btn_ser = (Button) this.findViewById(R.id.button_services);
		bnt_aploc = (Button) this.findViewById(R.id.button_aploc);
		
		//call each class
		mlocal = new myLocation();
		mwifi =new myWifi();
		mreceiver =new WifiReceiver();
		aploc = new apLocation();
		
		//call method checkGoogleplay() from class myLocation
		mlocal.checkGoogleplay(this);
		
		//set adapter to ListView wifi_lv
		wifi_lv.setAdapter(adapter);
		
		//declare variable "wifi" for myWifi.java
		mwifi.wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		
		/** 
		 * following are three button onClick Listener
		 * **
		 */
		btn_loc.setOnClickListener(new View.OnClickListener() {
		    
		    @Override
	        public void onClick(View v) 
		    {				
		      
		    	//clean previously arraylist
		    	arraylist.clear();
		    	
		    	//get location
		        mlocal.getlocation(); 
		        
		        //display location information to Textview
		        txtlocation.setText(mlocal.mCurrentLocation.getLatitude() + "," + mlocal.mCurrentLocation.getLongitude());
			    
		        //start scan wifi
		        mwifi.scanWifi();
			    
			    //call getwifilist() getting wifi info and calculate distance
			    mreceiver.getwifilist(MainActivity.this, mlocal);
			    
			    adapter.notifyDataSetChanged();
			 }
		 });
		    
		    
		 btn_ser.setOnClickListener(new View.OnClickListener() {			
			
		     @Override
			 public void onClick(View v) {
				
			     mlocal.getlocation();
					  
				 if( ((Button)v).getText().equals("Start Location Service"))
				 {
					 ((Button) v).setText("Stop Location Service");						   
					 mlocal.getupdate();
					
					 }
			     else
			     {
				     mlocal.removeupdate();
				     ((Button) v).setText("Start Location Service");
					 }
				  }
	      });		    
			
		  bnt_aploc.setOnClickListener(new View.OnClickListener() {
				
		      @Override
			  public void onClick(View v) {
				
				  txtaploc.setText(aploc.getlatitude()+","+aploc.getlongitude());					
			
			  }
	      });		
		    		    
	    	    	  	  	    
	  }
    
    /** 
	 * wifi receiver onPause
	 * **
	 */
  	protected void onPause() 
  	{
        unregisterReceiver(mreceiver);
        super.onPause();
      }

  	/** 
	 * wifi receiver onResume
	 * **
	 */
    protected void onResume() 
    {
        registerReceiver(mreceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
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