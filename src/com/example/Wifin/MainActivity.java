package com.example.Wifin;

import java.io.IOException;

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
import android.widget.TextView;

/**
 * Main activity java, handle home page of this application
 */
public class MainActivity extends Activity{
	
	/**
	 * call myWifi class
	 */
	private myWifi mwifi;
    
	/**
	 * call DataBaseHelper class
	 */
    private DataBaseHelper dbhelp;
    
    /**
	 * context for MainActivity
	 */
    private Context c= this;
    
    /**
	 * TextView for display current network connection information
	 */
    private TextView wifiText;
    
    /**
	 * String builder to store current network connection info
	 */
    private StringBuilder sb;
    
    /**
     * Base on content view activity_main.xml, execute once program run
     */
    @Override
	public void onCreate(Bundle savedInstanceState){
    	
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		    //scan wifi once open this application
			mwifi.scanWifi(c);	    		
		}
		
		//get current network connection information
		sb = new StringBuilder();		
		sb.append("Connected to: "+wifiInfo.getSSID()+"\n");
		sb.append("Signal Strenth: "+wifiInfo.getRssi()+"dBm"+"\n");
		sb.append("Link Speed: "+wifiInfo.getLinkSpeed()+"Mbps");		
		wifiText.setText(sb);
		
    }
    
    /**
     * button for navigate user to myMap activity
     */
    public void mapClickEvent(View view) {
    	//onClick link to Google Map activity
    	Intent i = new Intent(MainActivity.this, myMap.class);
        startActivity(i);
    }
    
    /**
     * button for navigate user to AR camera activity
     */
    public void cameraClickEvent(View view) {
    	
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        //url of our JSON file.
        String url = "file:///storage/emulated/0/Android/data/com.example.Wifin/cache/testJson.json";
        i.setDataAndType(Uri.parse(url), "application/mixare-json");
        startActivity(i);
    }
    
    /**
     * onResume listener for this class, register wifi receiver
     */
	@Override
    protected void onResume() {
    	registerReceiver(mwifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }
    
	/**
     * onPause listener for this class, unregister wifi receiver
     */
    @Override
    protected void onPause() 
  	{
    	unregisterReceiver(mwifi);
        super.onPause();
      }   
    
    /** 
	 * option menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}