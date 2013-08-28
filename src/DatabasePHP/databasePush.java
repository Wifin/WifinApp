package DatabasePHP;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import Database.databaseGetSet;

/**
 * Database Push Class for communicating to the database on the server
 * 
 * */
public class databasePush {
	
	/**
	* The method to perform the PUSH method to the server. 
	* 
	* @param 
	* 		url - the url where the PHP file is located.
	* 
	* @param 
	* 		nameValuePairs - the list where all the details required 
	* 						 for the push method.
	* 
	* @exception 
	* 		UnsupportedEncodingException - the encoding error of the nameValuePairs.
	* @exception 
	* 		ClientProtocolException - Unable to connect to the 
	* 								  server due to protocol issue. 
	* @exception 
	* 		IOException - HttpClient not getting the right input.
	* */
	private void setUpHttpPost(String url,  List<NameValuePair> nameValuePairs){
		//Setting up the HTTP Client
	    HttpClient httpClient = new DefaultHttpClient();   
	    //Setting up the HTTP Post method
	    HttpPost httppost = new HttpPost(url);
	    
	    try {
	    	//Set the entity of the HTTP Post
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
	        HttpResponse response = httpClient.execute(httppost);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
   
	}//end of setUpHttpPost()
	
	/**
	 * This method will create the url and list for storing the data. 
	 * The setUpHttpPost() method will be called to connect to the server.
	 * 
	 * @param dgs - the instance of databaseGetSet
	 *  */
	public void postInsertData(databaseGetSet dgs) {
		
		//The url for inserting the wifi data into the database.
		String url = "http://deco3801-007.uqcloud.net/phpmyadmin/insertDB.php";
	
		 //List to store the data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("WifiMacAddress", dgs.getMacAddress()));
	    nameValuePairs.add(new BasicNameValuePair("WifiSSID", dgs.getSSID()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLatitude", dgs.getLatitude()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLongtitude", dgs.getLongtitude()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLocation", dgs.getLocation()));
	    
	    //The url for inserting the wifi distance data into the database.
	    String urlDistance = "http://deco3801-007.uqcloud.net/phpmyadmin/insertDBDistance.php";
	    //List to store the data
	    List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
	    nameValuePairs2.add(new BasicNameValuePair("WifiMacAddress", dgs.getMacAddress()));
	    nameValuePairs2.add(new BasicNameValuePair("Distance", "" + dgs.getDistance()));
	    nameValuePairs2.add(new BasicNameValuePair("SignalStrength", dgs.getSignalStrength()));
	    
	    //System.out.println("postInsertData():  DISTANCE: " + nameValuePairs2.get(1));
	     
	    //Calling the method for which the HTTP Post method is executed.
	    setUpHttpPost(url,nameValuePairs);  
	    setUpHttpPost(urlDistance,nameValuePairs2);

	}//end of postInsertData()

}
