package com.example.Wifin;

//import Jama.Matrix;

public class apLocation 
{
	        //assuming elevation = 0
			int earthR = 6371;
			double LatA = 37.418436;
			double LonA = -121.963477;
			double DistA = 0.265710701754;
			double LatB = 37.417243;
			double LonB = -121.961889;
			double DistB = 0.234592423446;
			double LatC = 37.418692;
			double LonC = -121.960194;
			double DistC =0.0548954278262;
			
			//using authalic sphere
			//if using an ellipsoid this step is slightly different
			//Convert geodetic Lat/Long to ECEF xyz
			//1. Convert Lat/Long to radians
			//2. Convert Lat/Long(radians) to ECEF
			double xA = earthR *(Math.toRadians(Math.cos(LatA)) * Math.toRadians(Math.cos(LonA)));
			double yA = earthR *(Math.toRadians(Math.cos(LatA)) * Math.toRadians(Math.sin(LonA)));
			//double zA = earthR *(Math.sin(Math.toRadians(LatA)));

			double xB = earthR *(Math.toRadians(Math.cos(LatB)) * Math.toRadians(Math.cos(LonB)));
			double yB = earthR *(Math.toRadians(Math.cos(LatB)) * Math.toRadians(Math.sin(LonB)));
			//double zB = earthR *(Math.sin(Math.toRadians(LatB)));

			double xC = earthR *(Math.toRadians(Math.cos(LatC)) * Math.toRadians(Math.cos(LonC)));
			double yC = earthR *(Math.toRadians(Math.cos(LatC)) * Math.toRadians(Math.sin(LonC)));
			//double zC = earthR *(Math.sin(Math.toRadians(LatC)));
		
			
			//double[][] p0 = new double [][]{{xA, yA, zA}};
			//double[][] p1 = new double [][]{{xB, yB, zB}};
			//double[][] p2 = new double [][]{{xC, yC, zC}};
			
			//Matrix A = new Matrix(p0);
			//Matrix B = new Matrix(p1);
			//Matrix C = new Matrix(p2);
			
			
			//from wikipedia
		    //transform to get circle 1 at origin
			//transform to get circle 2 on x axis
			//Matrix ex = B.minus(A);
			//Matrix ey= (C.minus(A)).minus(ex);
			double d = Math.abs(xB-xA);
			double i = Math.abs(xC-xA);
			double j = Math.abs(yC-yA);
			
			//from wikipedia
			//plug and chug using above values
			double x = (Math.pow(DistA,2) - Math.pow(DistB,2) + Math.pow(d,2))/(2*d);
		    double y = ((Math.pow(DistA,2) - Math.pow(DistC,2) + Math.pow(i,2) + Math.pow(j,2))/(2*j)) - ((i/j)*x);

			double a=6378137;
			double b=6356752.3142;
		    double p=(x+y)/2;	
		    //Convert back to degree
		    double lat = Math.atan((b*Math.sin(3*y))/(p*Math.cos(3*y))) + LatA;
		    double lon = Math.atan(y/x) + LonA;
			
			
		public double getlatitude()
		{
			
			return lat;
		}
		
		public double getlongitude()
		{
			
			return lon;
		}

	
}



