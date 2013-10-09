package com.example.Wifin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

public class myMap extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,LocationListener {

	 
	 private static final String LOG_TAG = "WifinAPP";
	 
	    protected GoogleMap map;	    
	    final Context c = this;	    
	    private static final String PATH = "/storage/emulated/0/Android/data/com.example.Wifin/cache/testJson.json";
	    //myWifi mwifi;
 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //mwifi=new myWifi();
	        //mwifi.wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
	        //mwifi.scanWifi(c);
	        
	        if(isgoogleplay())
	        {
	            setContentView(R.layout.activity_map);
	            setUpMapIfNeeded();
	            
	            /*
				* on info marker click listener, wifi connection function
				**/
		        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
					@Override
					public void onInfoWindowClick(Marker marker) {
						// TODO Auto-generated method stub
				    	final String ssid = marker.getTitle();	
				    	AlertDialog.Builder builder = new AlertDialog.Builder(c);
				    	LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
				    	builder.setView(inflater.inflate(R.layout.dialog_signin, null))
				        .setTitle("Network Connection")		        
						.setMessage("Connect to"+ssid)
						.setCancelable(false)
						.setPositiveButton("Connect",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked,start connect wifi
								wificonnector(ssid);
							}
						  })
						.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						}).show();
					}
				});
	        
	        }
	        
	    }
	 
	    //@Override
	    //protected void onResume() {
	    //	registerReceiver(mwifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	    //    super.onResume();
	    //    setUpMapIfNeeded();
	    //}
	    
	    //@Override
	    //protected void onPause() 
	  	//{
	    //    unregisterReceiver(mwifi);
	    //    super.onPause();
	    //  }
	 
	    private void setUpMapIfNeeded() {
	    	 //initialize map here
	        if (map == null) {
	            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	                    .getMap();
	            if (map != null) {
	                setUpMap();
			    	map.setMyLocationEnabled(true);
	            }
	        }
	    }
	    
	    private boolean isgoogleplay()
		{
			int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
			if(resp == ConnectionResult.SUCCESS){
				return true;
			}
			else{
				((Dialog)GooglePlayServicesUtil.getErrorDialog(resp,this,10)).show();
				return false;
			}
		}
	    
	    public void enableupdate(LocationClient locclt)
	    {
	    	//enable location update request
	    	LocationRequest locationrequest = LocationRequest.create();    	    	
	    	//get most accurate locations for this update method
	    	locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);  	
	        // set location listener update interval time 100 millisecond
	    	locationrequest.setInterval(100);       
	        locclt.requestLocationUpdates(locationrequest,this);
	    }//end of getupdate
	 
	    private void setUpMap() {
	        // Retrieve the city data from the web service
	        // In a worker thread since it's a network operation.
	        new Thread(new Runnable() {
	            public void run() {
	                try {
	                	retrieveAndAddLocation();
	                } catch (IOException e) {
	                    Log.e(LOG_TAG, "Cannot retrive access-points", e);
	                    return;
	                }
	            }
	        }).start();
	    }
	    
	    protected void retrieveAndAddLocation() throws IOException {
	    	
	        final List<apinfo> retrieved;
	        try {
	            //Read Json file generated by query
	        	InputStream abc = new FileInputStream(PATH);
	            InputStreamReader in = new InputStreamReader(abc,"UTF-8");
	            // Read the JSON data into the StringBuilder
	            jsonReader jr = new jsonReader();
	            retrieved = jr.readJsonStream(in);
	        }catch (IOException e) {
	        	System.out.println("file not exist!!!!");
	            Log.e(LOG_TAG, "Error connecting to service", e);
	            throw new IOException("Error connecting to service", e);
	        }
	        // Create markers for the city data.
	        // Must run this on the UI thread since it's a UI operation.
	        runOnUiThread(new Runnable() {
	            public void run() {
	                try {
	                    createMarkersFromJson(retrieved);
	                } catch (JSONException e) {
	                    Log.e(LOG_TAG, "Error processing JSON", e);
	                }
	            }
	        });
	    }
	    
	    void createMarkersFromJson(List<apinfo> aplist) throws JSONException {	        
	        // add something
	        int radius = 230;
	        String[] allColors = getResources().getStringArray(R.array.colors);
	        for(int n=0; n<5; n++){
	            for (int i = 0; i < aplist.size(); i++) {
	                
	                // Create a marker for each city in the JSON data.
	            	System.out.println("Test getlat:"+aplist.get(i).getlat()+"\n getlon:"+aplist.get(i).getlon());
	                map.addMarker(new MarkerOptions()
	                    .title(aplist.get(i).gettitle())
	                    .snippet("Signal Strength:"+aplist.get(i).getlevel()+"dBm"+"\n MAC:"+aplist.get(i).getmac())
	                    .position(new LatLng(
	                    		aplist.get(i).getlat(),
	                    		aplist.get(i).getlon())));
	                    
	                map.addCircle(new CircleOptions()
	    	              .center(new LatLng(
	    	                  aplist.get(i).getlat(),
	    	                  aplist.get(i).getlon()))
	    	              .radius(radius)
	    	              .fillColor(Color.parseColor(allColors[n]))
	    	              .strokeWidth(0));
	                
	              }
	                radius -= 30;
	             
	        }
	    }
	    
	    /*
	     * connect WiFi method
	     **/
	    public void wificonnector(String ssid) 
	    {	    	
	    	EditText password = (EditText) findViewById(R.id.password);
	    	//get ssid from marker title
	    	String networkSSID = ssid;
	    	WifiConfiguration conf = new WifiConfiguration();
	    	conf.SSID = "\"" + networkSSID + "\"";
	    	conf.preSharedKey = "\""+ password +"\"";
	    	//add conf to Android wifi manager
	    	WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
	    	wifiManager.addNetwork(conf);
	    	
	    	List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
	    	for( WifiConfiguration i : list ) {
	    	    if(i.SSID != null && i.SSID.equals(conf.SSID)) {
	    	         wifiManager.disconnect();
	    	         wifiManager.enableNetwork(i.networkId, true);
	    	         wifiManager.reconnect();              
	    	         
	    	         break;
	    	    }           
	    	 }
	    }
	   
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());	
			
			map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
			map.animateCamera(CameraUpdateFactory.zoomTo(10));
		}

		@Override
		public void onConnectionFailed(ConnectionResult result) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnected(Bundle connectionHint) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}
}