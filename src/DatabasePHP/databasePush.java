package DatabasePHP;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;

import com.example.Wifin.WifiReceiver;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import Database.databaseGetSet;
/**
 * Database Push Class for communicating to the database on the server
 * 
 * */
public class databasePush {
	
	//static databaseGetSet dgs = new databaseGetSet();
	//private WifiReceiver wifiR = new WifiReceiver();

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
	    HttpClient httpClient = new DefaultHttpClient();   
	    HttpPost httppost = new HttpPost(url);
	    
	    try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
	        HttpResponse response = httpClient.execute(httppost);
	        
	        System.out.println("Response:  "  + response.getEntity() + " for mac address: "  + nameValuePairs.get(0));
			
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
	 *  */
	public void postInsertData(databaseGetSet dgs) {
		
		//The url for inserting the data into the database.
		String url = "http://deco3801-007.uqcloud.net/phpmyadmin/insertDB.php";
		
//		String url = "http://localhost/phpmyadmin/insertDB2.php";
		
	    // Add your data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("WifiMacAddress", dgs.getMacAddress()));
	    nameValuePairs.add(new BasicNameValuePair("WifiSSID", dgs.getSSID()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLatitude", dgs.getLatitude()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLongtitude", dgs.getLongtitude()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLocation", dgs.getLocation()));
	    
	//    System.out.println("postInsertData():  " + nameValuePairs.get(0));
	     
	    setUpHttpPost(url,nameValuePairs);

	}//end of postInsertData()
	
	
	
	public void postConnectData(){
		String url = "http://deco3801-007.uqcloud.net/phpmyadmin/db_connect2.php";
		
		String username = "admin";
		String hostname = "localhost";
		String password = "hebrew4belly!dice";
		String database = "UnnamedWifiStrengthVisualisation";
				
	    // Add your data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("UserName", username ));
	    nameValuePairs.add(new BasicNameValuePair("Password", hostname ));
	    nameValuePairs.add(new BasicNameValuePair("HostName", password ));
	    nameValuePairs.add(new BasicNameValuePair("Database", database ));
//	    System.out.println(nameValuePairs);
	    setUpHttpPost(url,nameValuePairs);
	    
	}
	

	
//	public static void main(String[] args){
//		databaseGetSet dgs = new databaseGetSet();
//		dgs.setMacAddress("94-22-00-00-11-22");
//		dgs.setSSID("	test ing 123");
//		dgs.setLatitude("-27.500780");
//		dgs.setLongtitude("153.012733");
//		dgs.setLocation("GPS South UQ");
//		
////		postConnectData();
//		postInsertData(dgs);
//		
//		System.out.println("END");
//	}

}
