/** 
 * location services
 * using google-play-services-lib
 * **
 */

package com.example.Wifin;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class myLocation implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,LocationListener
{
	/** 
	 * log tag for google-play services connection status
	 * **
	 */
	private String TAG = this.getClass().getSimpleName();
	/** 
	 * variable to check google-play services availability
	 * **
	 */
	int resultCode;
	/** 
	 * variable for use LocationClient services
	 * **
	 */
	LocationClient mLocationClient;
	/** 
	 * variable for use location update services
	 * **
	 */
	LocationRequest mLocationRequest;
	/** 
	 * variable for stored last-known location
	 * **
	 */
	Location mCurrentLocation;
	
	/** 
	 * This method is to check google-play services availability,
	 if google-play service available, enable location services
	 * 
	 * @param mContext - context for checkGoogleplay method
	 * 
	 * **
	 */
	public void checkGoogleplay(Context mContext)
	{
		resultCode =GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
    	
	    if(ConnectionResult.SUCCESS == resultCode){
	    	Log.d("Location Updates",
                    "Google Play services is available.");
	    	
	    	//LocationClient(context,connectionCallbacks,connectionFailed Listener)
		    mLocationClient = new LocationClient(mContext, this,this);
		    mLocationClient.connect();
	    }
	    else{
		    Toast.makeText(mContext, "Google Play Service Error " + resultCode,Toast.LENGTH_LONG).show();
	    } 	    		
		
	}//end of checkGoogleplay
	
	/** 
	 * This method is for getting last-known location,
     if mLocationClient is null, get current location
	 * **
	 */
	public void getlocation()
    {
    	if(mLocationClient!=null && mLocationClient.isConnected()==true)
    	{
    		Log.d("Location Updates",
                    "locationClient is connected");
            mCurrentLocation = mLocationClient.getLastLocation();
		}
    	else{
    		Log.d("Location Updates",
                    "locationClient can't connected :(");
    	}
    }//end of getlocation
    
	/** 
	 * enable location update
	 * 
	 * @param mLocationRequest - for update location information
	 * 
	 * **
	 */
    public void getupdate()
    {
    	//enable location update request
    	mLocationRequest = LocationRequest.create();
    	
    	//get most accurate locations for this update method
    	mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    	
        // set location listener update interval time 100 millisecond
        mLocationRequest.setInterval(100);
        
        //action for update location for "mLocationRequest"
        mLocationClient.requestLocationUpdates(mLocationRequest,this);
    }//end of getupdate
      
    /** 
	 * disable location update
	 * **
	 */
    public void removeupdate()
    {
    	mLocationClient.removeLocationUpdates(this);
    }//end of removeupdate
	

    /** 
	 * Connection Failed listener for GooglePlayServicesClient.OnConnectionFailedListener 
	 interface *implements under myLocation class
	 * **
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.i(TAG, "onConnectionFailed");
		//txtstatus.setText("Connection Status : Fail");
	}
	
	/** 
	 * Connect listener for GooglePlayServicesClient.ConnectionCallbacks
	 interface *implements under myLocation class
	 * **
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(TAG, "onConnected");
		//txtstatus.setText("Connection Status : Connected");		
	}
    
	/** 
	 * Disconnect listener for GooglePlayServicesClient.ConnectionCallbacks
	 interface *implements under myLocation class
	 * **
	 */
	@Override
	public void onDisconnected() {
		Log.i(TAG, "onDisconnected");
		//txtstatus.setText("Connection Status : Disconnected");
	}
    
		
	/** 
	 * Location changed listener for LocationListener
	 interface *implements under myLocation class
	 *
	 *@param mlocation - private variable for Location
	 * **
	 */
	@Override
	public void onLocationChanged(Location mlocation) {
		// location onChanged listener
		
	}
    
	
}
