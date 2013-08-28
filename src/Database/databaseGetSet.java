package Database;



import java.sql.*; 

public class databaseGetSet {
	
	private databaseConnection dbc = new databaseConnection();
	
	private String macAddress , ssid , latitude, longtitude , strength, location;
	private double distance;
	
	/**
	 * Set values for Mac Address
	 * */
	public void setMacAddress(String m){
		macAddress = m ;
	}
	/**
	 * Get the values of the Mac Address
	 * */
	public String getMacAddress(){
		return macAddress;
	}
	
	public void setSSID(String s){		
		ssid = s ;
	}
	public String getSSID(){
		return ssid;
	}
	
	/**
	 * Set values for latitude
	 * */
	public void setLatitude(String l){
		latitude = l ;
	}
	/**
	 * Get the values of the latitude
	 * */
	public String getLatitude(){
		return latitude;
	}
	
	/**
	 * Set values for longtitude
	 * */
	public void setLongtitude(String l){
		longtitude = l ;
	}
	/**
	 * Get the values of the Mac Address
	 * */
	public String getLongtitude(){
		return longtitude;
	}
	
	public void setLocation(String l){
		location = l ;
	}
	/**
	 * Get the values of the Mac Address
	 * */
	public String getLocation(){
		return location;
	}
	
	
	/**
	 * Set values for SignalStrength
	 * */
	public void setSignalStrength(String s){
		strength = s ;
	}
	/**
	 * Get the values of the SignalStrength
	 * */
	public String getSignalStrength(){
		return strength;
	}
	
	/**
	 * Set values for Distance
	 * */
	public void setDistance(Double d){
		distance = d ;
	}
	/**
	 * Get the values of the Distance
	 * */
	public Double getDistance(){
		return distance;
	}

}
