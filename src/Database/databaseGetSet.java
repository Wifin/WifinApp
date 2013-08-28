package Database;



import java.sql.*; 
/**
 * This class contains all the helper methods for storing and retrieving details
 * of the wifi
 * 
 * */
public class databaseGetSet {
	// The variables to store the wifi's details.
	private String macAddress , ssid , latitude, longtitude, 
	strength, location, distance;
	
	/**
	 * Set values for Mac Address
	 * 
	 * @param m - value of MacAddress
	 * */
	public void setMacAddress(String m){
		macAddress = m ;
	}
	/**
	 * Get the values of the Mac Address
	 * 
	 * @return the MacAddress
	 * */
	public String getMacAddress(){
		return macAddress;
	}
	/**
	 * Set the values of the SSID(Wifi name)
	 * 
	 * @param s - value of SSID
	 * */
	public void setSSID(String s){		
		ssid = s ;
	}
	/**
	 * Get the values of the SSID(Wifi name)
	 * 
	 * @return the SSID
	 * */
	public String getSSID(){
		return ssid;
	}
	
	/**
	 * Set values for Latitude
	 * 
	 * @param l - value of Latitude
	 * */
	public void setLatitude(String l){
		latitude = l ;
	}
	/**
	 * Get the values of the latitude
	 * 
	 * @return the Latitude
	 * */
	public String getLatitude(){
		return latitude;
	}
	
	/**
	 * Set values for longitude
	 * 
	 * @param l - value of longitude
	 * */
	public void setLongtitude(String l){
		longtitude = l ;
	}
	/**
	 * Get the values of the longitude
	 * 
	 * @return the longitude
	 * */
	public String getLongtitude(){
		return longtitude;
	}
	/**
	 * Set the values of the location
	 * */
	public void setLocation(String l){
		location = l ;
	}
	/**
	 * Get the values of the location
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
	public void setDistance(String d){
		distance = d ;
	}
	/**
	 * Get the values of the Distance
	 * */
	public String getDistance(){
		return distance;
	}
	

}
