package Database;



import java.sql.*; 

public class databaseGetSet {
	
	private databaseConnection dbc = new databaseConnection();
	
	private String macAddress , ssid , latitude, longtitude , strength, location;
	
	public databaseGetSet(){
	}
	
	/**
	 * Set values for Mac Address
	 * */
	public void setMacAddress(String m){
		this.macAddress = m ;
	}
	/**
	 * Get the values of the Mac Address
	 * */
	public String getMacAddress(){
		return macAddress;
	}
	
	public void setSSID(String s){		
		this.ssid = s ;
	}
	public String getSSID(){
		return ssid;
	}
	
	/**
	 * Set values for latitude
	 * */
	public void setLatitude(String l){
		this.latitude = l ;
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
		this.longtitude = l ;
	}
	/**
	 * Get the values of the Mac Address
	 * */
	public String getLongtitude(){
		return longtitude;
	}
	
	public void setLocation(String l){
		this.location = l ;
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
		this.strength = s ;
	}
	/**
	 * Get the values of the frequency
	 * */
	public String getSignalStrength(){
		return strength;
	}
	

}
