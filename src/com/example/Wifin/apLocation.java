package com.example.Wifin;

public class apLocation 
{
		//calculate AP location variables
		double d1 =25.13;//distance 1
		double d2 =15.52;//distance 2
		double d3 =30.88;//distance 3
		double x1 =47.014580;//latitude 1
		double x2 =46.589069;//latitude 2
		double x3 =46.573967;//latitude 3
		double y1 =97.402342;//longitude 1
		double y2 =99.347992;//longitude 2
		double y3 =97.130127;//longitude 3
		double x;//latitude ap
		double y;//longitude ap
		
		public double getlatitude()
		{
			x=((Math.pow(x2,2)-Math.pow(x1,2)+Math.pow(y2,2)-Math.pow(y1,2)-Math.pow(d1,2)-Math.pow(d2,2))*(y3-y2)-(Math.pow(x3,2)-Math.pow(x2,2)+Math.pow(y3,2)-Math.pow(y2,2)-Math.pow(d2,2)-Math.pow(d3,2))*(y2-y1))/(2*((x2-x1)*(y3-y2)-(x3-x2)*(y2-y1)));			
			System.out.println(((Math.round(x*100))/100.0));
			return ((Math.round(x*1000000))/1000000.0);
		}
		
		public double getlongitude()
		{
			y=((Math.pow(x2,2)-Math.pow(x1,2)+Math.pow(y2,2)-Math.pow(y1,2)-Math.pow(d1,2)-Math.pow(d2,2))*(x3-x2)-(Math.pow(x3,2)-Math.pow(x2,2)+Math.pow(y3,2)-Math.pow(y2,2)-Math.pow(d2,2)-Math.pow(d3,2))*(x2-x1))/(2*((y2-y1)*(x3-x2)-(y3-y2)*(x2-x1)));
			System.out.println(((Math.round(y*100))/100.0));
			return ((Math.round(y*100))/100.0);
		}

	
}
