package com.example.Wifin;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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
    
  
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtlocation= (TextView) findViewById(R.id.locView);
		btn_loc  = (Button) this.findViewById(R.id.button_location);
		btn_ser = (Button) this.findViewById(R.id.button_services);
		bnt_aploc = (Button) this.findViewById(R.id.button_aploc);
		wifi_lv =(ListView)findViewById(R.id.listView_wifi);
		mlocal = new myLocation();
		mwifi =new myWifi();
		
		mwifi.wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		mwifi.adapter = new SimpleAdapter(this, mwifi.arraylist, R.layout.activity_main,new String[] { mwifi.ITEM_KEY }, new int[] { R.id.listView_wifi });
		
		mlocal.checkGoogleplay(this);
		
		wifi_lv.setAdapter(mwifi.adapter);
		
		
		 
		    btn_loc.setOnClickListener(new View.OnClickListener() {
		    
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				   mlocal.getlocation();
				   txtlocation.setText(mlocal.mCurrentLocation.getLatitude() + "," + mlocal.mCurrentLocation.getLongitude());
				   mwifi.scanWifi();
				   mwifi.getwifilist();			   
				   System.out.println("111");
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


}
