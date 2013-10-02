package com.example.Wifin;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_splash);
	        initTimer(); 
	    }
	    private void initTimer() 
	    {
	        Timer t = new Timer();
	        TimerTask timerTask = new TimerTask()
	        {           
	            @Override
	            public void run()
	            {
	                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
	                startActivity(intent);
	                finish();
	            }
	        };
	        t.schedule(timerTask, 3000);
	    }

}
