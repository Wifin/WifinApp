package com.example.Wifin;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * class for splash screen
 */
public class SplashScreen extends Activity{
	//execute when this class been called
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//call initTimer method
		initTimer(); 
	    }
	//set a timer for how long this splash screen will stay
	private void initTimer(){
		Timer t = new Timer();
		TimerTask timerTask = new TimerTask(){
			//after splash screen activity, start MainActivity
			@Override
			public void run(){
				Intent intent = new Intent(SplashScreen.this,MainActivity.class);
				startActivity(intent);
				finish();
				}
			};
			//time for this splash set to 3 seconds
			t.schedule(timerTask, 3000);
			}
}
