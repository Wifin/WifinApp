package com.example.Wifin;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class myLocation implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,LocationListener
{

	private String TAG = this.getClass().getSimpleName();
	int resultCode;
	LocationClient mLocationClient;
	LocationRequest mLocationRequest;
	Location mCurrentLocation;
	TextView txtinstant;
	
	
	public void checkGoogleplay(Context mContext)
	{
		
		resultCode =GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
    	
	    if(ConnectionResult.SUCCESS == resultCode){
	    	Log.d("Location Updates",
                    "Google Play services is available.");
		    mLocationClient = new LocationClient(mContext, this,this);
            mLocationClient.connect();
	    }
	    else{
		    Toast.makeText(mContext, "Google Play Service Error " + resultCode,Toast.LENGTH_LONG).show();
	    } 	    		
		
	}
	
	public void getlocation()
    {
    	if(mLocationClient!=null && mLocationClient.isConnected()){
    		 mCurrentLocation = mLocationClient.getLastLocation();
		   }
    }
    
    public void getupdate()
    {
    	
    	   mLocationRequest = LocationRequest.create();
    	   mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
           mLocationRequest.setInterval(100);//time-unit-millisecond
           mLocationClient.requestLocationUpdates(mLocationRequest,this);
    }
       
    public void removeupdate()
    {
    	mLocationClient.removeLocationUpdates(this);
    }
	

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onConnectionFailed");
		//txtstatus.setText("Connection Status : Fail");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onConnected");
		//txtstatus.setText("Connection Status : Connected");		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDisconnected");
		//txtstatus.setText("Connection Status : Disconnected");
	}




	@Override
	public void onLocationChanged(Location mlocation) {
		// TODO Auto-generated method stub
		
	}
    
	
}
