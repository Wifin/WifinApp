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
//import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	
    TextView txtlocation;
    TextView txtstatus;
    TextView txtinstant;
    Button btn_loc;
    Button btn_ser;
    Button bnt_aploc;
    ListView wifi_lv;
    myLocation mlocal;
    myWifi mwifi;
    WifiReceiver mreceiver;
    SimpleAdapter adapter;
    //ArrayAdapter<HashMap<String, String>> adapter;
    ArrayList<HashMap<String, String>> arraylist;
    String ITEM_KEY;
    
  
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		arraylist = new ArrayList<HashMap<String, String>>();
		ITEM_KEY = "key";
		
		//adapter = new SimpleAdapter(
				//this, 
				//arraylist, 
				//R.layout.activity_main,
				//new String[] { ITEM_KEY },
				//new int[] { R.id.listView_wifi });
		//adapter = new ArrayAdapter<HashMap<String, String>> (this,R.layout.activity_main,arraylist); 
		adapter = new SimpleAdapter(
                this, 
                arraylist, 
                android.R.layout.simple_list_item_1,
                new String[] { ITEM_KEY },
                new int[] { android.R.id.text1 });
		
		
		txtlocation= (TextView) findViewById(R.id.locView);
		btn_loc  = (Button) this.findViewById(R.id.button_location);
		btn_ser = (Button) this.findViewById(R.id.button_services);
		bnt_aploc = (Button) this.findViewById(R.id.button_aploc);
		wifi_lv =(ListView)findViewById(R.id.listView_wifi);
		mlocal = new myLocation();
		mwifi =new myWifi();
		mreceiver =new WifiReceiver();
		
		mlocal.checkGoogleplay(this);		
		wifi_lv.setAdapter(adapter);
		
		//mwifi.scanWifi(this.getApplicationContext());
		
		mwifi.wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		 
		    btn_loc.setOnClickListener(new View.OnClickListener() {
		    
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				   arraylist.clear();
				   mlocal.getlocation();
				   txtlocation.setText(mlocal.mCurrentLocation.getLatitude() + "," + mlocal.mCurrentLocation.getLongitude());
				   mwifi.scanWifi();
				   mreceiver.getwifilist(MainActivity.this);	
				   adapter.notifyDataSetChanged();
			}
		    });
		    
		    
		    btn_ser.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					  mlocal.getlocation();
					  
					  if( ((Button)v).getText().equals("Start Location Service")){
						   ((Button) v).setText("Stop Location Service");						   
						   mlocal.getupdate();
		    	           System.out.println("222");
					   }
					   else{
						   mlocal.removeupdate();
						   ((Button) v).setText("Start Location Service");
					   }
				}
			    });		    
			
			bnt_aploc.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					
				}
			});		
		    		    
	    	    	  	  	    
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void onPause() {
        unregisterReceiver(mreceiver);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(mreceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }


}
