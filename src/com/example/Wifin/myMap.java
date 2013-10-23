package com.example.Wifin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class myMap extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,LocationListener,SensorEventListener {

	 
	 private static final String LOG_TAG = "WifinAPP";
	 
	    protected GoogleMap map;	    
	    final Context c = this;	    
	    private static final String PATH = "/storage/emulated/0/Android/data/com.example.Wifin/cache/testJson.json";
	    private EditText password;
	    private EditText username;
	    private wifiConnect wc;
	    // A request to connect to Location Services
	    private LocationRequest mLocationRequest;
	    // Stores the current instantiation of the location client in this object
	    LocationClient mLocationClient;
	    
	    int color;
	    
		// define the display assembly compass picture
	    private ImageView image;
	    
	    private TextView distanceText;

	    // device sensor manager
	    private SensorManager mSensorManager;
	    
	    // record the compass picture angle turned
	    private float heading = 0f;
	    private float bearing =0;
	    private Location test;
	    private float distance =0;
	    	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_map);
	        // Create a new global location parameters object
	        mLocationRequest = LocationRequest.create();
	        /*
	         * Set the update interval
	         */
	        mLocationRequest.setInterval(10000);

	        // Use high accuracy
	        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	        // Set the interval ceiling to one minute
	        mLocationRequest.setFastestInterval(2000);
	        
	        /*
	         * Create a new location client, using the enclosing class to
	         * handle callbacks.
	         */
	        mLocationClient = new LocationClient(this, this, this);
	        
//	        btn_navi = (ImageButton) findViewById(R.id.button_navi);
//			btn_navi.setBackgroundResource(R.drawable.abc);
			
			// our compass image 
	        image = (ImageView) this.findViewById(R.id.image_compass);
	        
	        //show distance between you and AP
	        distanceText = (TextView) this.findViewById(R.id.text_distance);
	        
	     // initialize your android device sensor capabilities
	        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	        
//	        btn_navi.setOnClickListener(new View.OnClickListener() {
//			    
//			    @Override
//		        public void onClick(View v) 
//			    {
//			    	  Intent i = new Intent();
//			    	  i.setAction(Intent.ACTION_VIEW);
//			    	  //url of our JSON file.
//			    	  String url = "file:///storage/emulated/0/Android/data/com.example.Wifin/cache/testJson.json";
//			    	  i.setDataAndType(Uri.parse(url), "application/mixare-json");
//			    	  startActivity(i);
//			    }
//	        });
	        
	        if(isgoogleplay())
	        {
	            mLocationClient.connect();
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
						.setMessage("Connect to "+ssid)
						.setCancelable(false)
						.setPositiveButton("Connect",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked,start connect wifi
								username = (EditText) findViewById(R.id.username);
								password = (EditText) findViewById(R.id.password);
								wc=new wifiConnect();
								wc.connectEAP(username,password, ssid, c);
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
	            //create a location
		        test = new Location("fakeprovider");
		        test.setLatitude(retrieved.get(0).getlat());
		        test.setLongitude(retrieved.get(0).getlon());
	            
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
	    	
	            for (int i = 0; i < aplist.size(); i++) {
	                
	                // Create a marker for each city in the JSON data.
	                map.addMarker(new MarkerOptions()
	                    .title(aplist.get(i).gettitle())
	                    .snippet("Signal Strength:"+aplist.get(i).getlevel()+"dBm"+"\n"+"MAC:"+aplist.get(i).getmac()+"\n"+aplist.get(i).getctype())
	                    .position(new LatLng(
	                    		aplist.get(i).getlat(),
	                    		aplist.get(i).getlon())));
	                
	                if(aplist.get(i).getlevel()<-60)
	                {
	                	color = 0xff00ff00;
	                }
	                
	                if(-80<aplist.get(i).getlevel()&&aplist.get(i).getlevel()<-60)
	                {
	                	color = 0xfff3e800;
	                }
	                
	                if(-80<aplist.get(i).getlevel())
	                {
	                	color = 0xFFFF0000;
	                }
	                
	                map.addCircle(new CircleOptions()
	    	              .center(new LatLng(
	    	                  aplist.get(i).getlat(),
	    	                  aplist.get(i).getlon()))
	    	              //.radius(radius)
	    	              //.fillColor(Color.parseColor(allColors[n]))
	    	                  .radius(10)
	    	                  .fillColor(color)
	    	              .strokeWidth(0));
	                
	                
	              }
	    }	    
	    
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());			
			map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
			map.animateCamera(CameraUpdateFactory.zoomTo(30f));
			bearing = mLocationClient.getLastLocation().bearingTo(test);
	        distance = mLocationClient.getLastLocation().distanceTo(test);
	        heading = (bearing-heading)*-1;
	        distanceText.setText(distance+"m");
		}
		
		 /**
	     * In response to a request to start updates, send a request
	     * to Location Services
	     */
	    private void startPeriodicUpdates() {
	        mLocationClient.requestLocationUpdates(mLocationRequest, this);
	    }
	    
	    /**
	     * In response to a request to stop updates, send a request to
	     * Location Services
	     */
	    private void stopPeriodicUpdates() {
	        mLocationClient.removeLocationUpdates(this);
	    }

		@Override
		public void onConnectionFailed(ConnectionResult connectionResult) {
		    System.out.println("onConnectionFailed!!!!!!!!!");
		}

		@Override
		public void onConnected(Bundle connectionHint) {
			LatLng latlng = new LatLng(mLocationClient.getLastLocation().getLatitude(),mLocationClient.getLastLocation().getLongitude());
	    	map.moveCamera( CameraUpdateFactory.newLatLngZoom(latlng,30f));
	    	startPeriodicUpdates();
		}

		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			stopPeriodicUpdates();
		}
		
	    /*
	     * Called when the Activity is no longer visible at all.
	     * Stop updates and disconnect.
	     */
	    @Override
	    public void onStop() {

	        // If the client is connected
	        if (mLocationClient.isConnected()) {
	            stopPeriodicUpdates();
	        }
	     // After disconnect() is called, the client is considered "dead".
	        mLocationClient.disconnect();

	        super.onStop();
	    }
	    
	    /*
	     * Called when the Activity is restarted, even before it becomes visible.
	     */
	    @Override
	    public void onStart() {

	        super.onStart();

	        /*
	         * Connect the client. Don't re-start any requests here;
	         * instead, wait for onResume()
	         */
	        mLocationClient.connect();

	    }
	    
	    @Override
	    protected void onResume() {
	    	mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
	                SensorManager.SENSOR_DELAY_GAME);
	        super.onResume();
	    }
	    
	    @Override
	    protected void onPause() 
	  	{
	    	mSensorManager.unregisterListener(this);
	        super.onPause();
	      }
	    
	    @Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// no need use here		
		}

		@Override
		public void onSensorChanged(SensorEvent event){
			// get the angle around the z-axis rotated
	        float degree = Math.round(event.values[0]);

	        // create a rotation animation (reverse turn degree degrees)
	        RotateAnimation ra = new RotateAnimation(
	                heading, 
	                -degree,
	                Animation.RELATIVE_TO_SELF, 0.5f, 
	                Animation.RELATIVE_TO_SELF,
	                0.5f);

	        // how long the animation will take place
	        ra.setDuration(210);

	        // set the animation after the end of the reservation status
	        ra.setFillAfter(true);

	        // Start the animation
	        image.startAnimation(ra);
	        heading = -degree;
	    }
}