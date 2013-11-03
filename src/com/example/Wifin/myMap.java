package com.example.Wifin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
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

/**
* execute map class with activity interface
**/
public class myMap extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,LocationListener,SensorEventListener 
{
	
	private static final String LOG_TAG = "WifinAPP";
	
	/**
	* define google map class
	**/
	protected GoogleMap map;	    
	
	/**
	* context for this myMap class
	**/
	private final Context c = this;	    
	
	/**
	* the path to store temporary json file
	**/
	private static final String PATH = "/storage/emulated/0/Android/data/com.example.Wifin/cache/testJson.json";
	
	/**
	* the text editor to type password for wifi connection
	**/
	private EditText password;
	
	/**
	* the text editor to type user name for wifi connection
	**/
	//private EditText username;
	
	/**
	* reference to wifi connect class
	**/
	private wifiConnect wc;
	
	/**
	* A request to connect to Location Services
	**/    
	private LocationRequest mLocationRequest;
		    
	/**
	* Stores the current instantiation of the location client in this object
	**/      
	private LocationClient mLocationClient;	    
	
	/**
	* an integer variable to store colors, with format of 0x00000000
	**/    
	private int color;
	
	/**
	* define the display assembly compass picture
	**/     
	private ImageView image;
	    
	/**
	* define the distance text view between you and target
	**/     
	private TextView distanceText;
	
	/**
	* device sensor manager
	**/      
	private SensorManager mSensorManager;
	
	/**
	* record the compass picture angle turned
	**/    
	private float heading = 0f;
	
	/**
	* record the bearing of current location and target location
	**/    
	private float bearing =0;
	
	/**
	* Initial location when trigger compass function
	**/ 
	private Location test;
	
	/**
	* record the distance between current location and target location
	**/ 
	private float distance =0;
	
	/**
	* execute this method when myMap class been called
	**/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.activity_map);
	    
	    // Create a new global location parameters object
	    mLocationRequest = LocationRequest.create();
	    
	    //Set the update interval
	    mLocationRequest.setInterval(10000);
	    
	    // Use high accuracy
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	    
	    // Set the interval ceiling to one minute
	    mLocationRequest.setFastestInterval(2000);
	    
	    //Create a new location client, using the enclosing class to handle callbacks.
	    mLocationClient = new LocationClient(this, this, this);
        
	    // our compass image 
	    image = (ImageView) this.findViewById(R.id.image_compass);
	        
	    //show distance between you and AP
	    distanceText = (TextView) this.findViewById(R.id.text_distance);
	        
	    // initialize your android device sensor capabilities
	    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    
	    /**
		* call isgoogleplay method, check the availability of google play services
		**/
	    if(isgoogleplay())
	    {
	    	//connect location client here
	    	mLocationClient.connect();
	    	
	        //call map setup method
	    	setUpMapIfNeeded();
	            
	        /**
		    * on info marker click listener, for wifi connection function
			**/
		    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		    {
		    	//on maker info window on click event
		    	@Override
			    public void onInfoWindowClick(Marker marker) 
		    	{
		    		final String ssid = marker.getTitle();	
		    		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		    		LayoutInflater inflater = (LayoutInflater) c.getSystemService(
		    				Context.LAYOUT_INFLATER_SERVICE );
				    builder.setView(inflater.inflate(R.layout.dialog_signin, null))
				        .setTitle("Network Connection")		        
						.setMessage("Connect to "+ssid)
						.setCancelable(false)
						.setPositiveButton("Connect",new DialogInterface.OnClickListener() 
						{
							//dialog connect button on-click listener
							public void onClick(DialogInterface dialog,int id) 
							{
								//username = (EditText) findViewById(R.id.username);
								password = (EditText) findViewById(R.id.password);
								wc=new wifiConnect();
								wc.connectWPA(password,ssid,c);
							}
						})
						.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
						{
							//dialog cancel button on-click listener
							public void onClick(DialogInterface dialog,int id) 
							{
								//dialog dismiss
								dialog.cancel();
							}
						}).show();
				}
			});
		}
	}
	
	/**
	* method check map availability and call setup google map method
	**/ 
	private void setUpMapIfNeeded(){
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
	
	/**
	* method to check google play services availability
	**/
	private boolean isgoogleplay()
    {
		// the integer returned from google play services utility
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if(resp == ConnectionResult.SUCCESS){
			return true;
			}
		
		else{
			// if not available, it will shown a error dialog
			((Dialog)GooglePlayServicesUtil.getErrorDialog(resp,this,10)).show();
			return false;
			}
		}
	
	/**
	* method to enable location update
	* @param locclt - android location client
	**/
	public void enableupdate(LocationClient locclt)
	{
		//enable location update request
		LocationRequest locationrequest = LocationRequest.create();  
		
	    //get most accurate locations for this update method
		locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); 
		
	    // set location listener update interval time 100 millisecond
	    locationrequest.setInterval(100);       
	    
	    //request location update
	    locclt.requestLocationUpdates(locationrequest,this);
	    }
	
	/**
	* method to setup google map
	**/
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

	/**
	* method to call json reader class retrieve data 
	**/
	protected void retrieveAndAddLocation() throws IOException {
		
		//define a custom type apinfo array
		final List<apinfo> retrieved;
		
		//Read Json file generated by query
		try {
			
	        InputStream abc = new FileInputStream(PATH);
	        
	        InputStreamReader in = new InputStreamReader(abc,"UTF-8");
	        
	        // Initialize jsonReader class
	        jsonReader jr = new jsonReader();
	        
	        //get a apinfo array list from temporary json path
	        retrieved = jr.readJsonStream(in);
	        
	        //create a new location
	        test = new Location("fakeprovider");
	        
	        test.setLatitude(retrieved.get(0).getlat());
	        test.setLongitude(retrieved.get(0).getlon());
	        }catch (IOException e) {
	        	System.out.println("file not exist!!!!");
	            Log.e(LOG_TAG, "Error connecting to service", e);
	            throw new IOException("Error connecting to service", e);
	        }
	        // Create markers for all access point in apinfo list.
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
	
	/**
	* method to add marker to google map  
	**/
	private void createMarkersFromJson(List<apinfo> aplist) throws JSONException {
		
		for (int i = 0; i < aplist.size(); i++) {
			
			// Create a marker for each access point in the JSON data.
			map.addMarker(new MarkerOptions()
			   .title(aplist.get(i).gettitle())
			   .snippet("Signal Strength:"+aplist.get(i).getlevel()+"dBm"+"\n"+"MAC:"+aplist.get(i).getmac()+"\n"+aplist.get(i).getctype())
	           .position(new LatLng(
	        		   aplist.get(i).getlat(),
	        		   aplist.get(i).getlon())))
	           //.setIcon(icon)
	           ;
			
			//color overlay for each marker, set different color base on different signal strength
			//range >-65 dBm
			if(aplist.get(i).getlevel()>-65){
				color = 0x8099CC00;
				}
			
			//-80 < range <-65 dBm
			if(-80<aplist.get(i).getlevel()&&aplist.get(i).getlevel()<-65){
				color = 0x80ffbb33;
				}
			
			//range <-80 dBm
			if(-80>aplist.get(i).getlevel()){
				color = 0x80ff4444;
				}
			
			map.addCircle(new CircleOptions()
			   .center(new LatLng(
					   aplist.get(i).getlat(),
					   aplist.get(i).getlon()))
			   .radius(12)
			   .fillColor(color)
			   .strokeWidth(0));
			}
		}	    
	
	/**
	* location change listener
	* @param location - current location information
	**/
	@Override
	public void onLocationChanged(Location location) {
		
		//create a latlon to store current location fetched
		LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
		
		//create a auto zoom when location changed
		map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		map.animateCamera(CameraUpdateFactory.zoomTo(25f));
		
		//re-calculate bearing
		bearing = mLocationClient.getLastLocation().bearingTo(test);
		
		//re-calculate distance
		distance = mLocationClient.getLastLocation().distanceTo(test);
		
		//change heading while location changed
		heading = (bearing-heading)*-1;
		
		//show distance information on text view
		DecimalFormat df = new DecimalFormat("#.##");
		distanceText.setText(df.format(distance)+"m");
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
	
	/**
	 * location client connection failed listener
	 * @param connectionResult- GMS location ConnectionResult
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		System.out.println("onConnectionFailed!!!!!!!!!");
		}
	
	/**
	 * location client on-connected listener
	 * @param connectionHint- bundle parameter for connection
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		LatLng latlng = new LatLng(mLocationClient.getLastLocation()
				.getLatitude(),mLocationClient.getLastLocation().getLongitude());
	    map.moveCamera( CameraUpdateFactory.newLatLngZoom(latlng,25f));
	    startPeriodicUpdates();
		}
	
	/**
	 * location client disconnect listener
	 */
	@Override
	public void onDisconnected() {
		stopPeriodicUpdates();
		}
	
	/**
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
	    
	/**
	 * Called when the Activity is restarted, even before it becomes visible.
	 */
	@Override
	public void onStart() {
		super.onStart();
		
		//Connect the client. Don't re-start any requests here;
		//instead, wait for onResume()
		mLocationClient.connect();
		}
	
	/**
	 * this activity on resume
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		//re-register listener for compass sensor manager
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
	    SensorManager.SENSOR_DELAY_GAME);	    
		super.onResume();
	    }
	
	/**
	 * this activity on pause
	 */
	@Override
	protected void onPause()
	{
		//unregister listener for compass sensor manager
		mSensorManager.unregisterListener(this);
		super.onPause();
		}
	
	/**
	 * auto created when use sensor listener
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
			// no need use here
		}
	
	/**
	 * Sensor changed listener
	 */
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