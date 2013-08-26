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
	
	static databaseGetSet dgs = new databaseGetSet();
	

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
//		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//
//		DefaultHttpClient client = new DefaultHttpClient();
//
//		SchemeRegistry registry = new SchemeRegistry();
//		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//		registry.register(new Scheme("https", socketFactory, 443));
//		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//		DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//
//		// Set verifier     
//		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

	    // Create a new HttpClient and Post Header
	    HttpClient httpClient = new DefaultHttpClient();   
	    HttpPost httppost = new HttpPost(url);
	    
	    try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
	        HttpResponse response = httpClient.execute(httppost);
	    //    Log.e("Responce-->", "after execute the http response");
	        
	        System.out.println(response);
			
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
	public void postInsertData() {
		
//		String url = "https://deco3801-007.uqcloud.net/phpmyadmin/insertDB.php";
		String url = "http://localhost/phpmyadmin/insertDB2.php";
	    // Add your data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("WifiMacAddress", dgs.getMacAddress()));
	    nameValuePairs.add(new BasicNameValuePair("WifiSSID", dgs.getSSID()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLatitude", dgs.getLatitude()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLongtitude", dgs.getLongtitude()));
	    nameValuePairs.add(new BasicNameValuePair("WifiLocation", dgs.getLocation()));
	    
	    setUpHttpPost(url,nameValuePairs);
	   // System.out.println(nameValuePairs);
	        
	}//end of postInsertData()
	
	private static String displayResponse(HttpResponse res) throws IOException{
		
		InputStream inputStream = res.getEntity().getContent();
		
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

		BufferedReader reader = new BufferedReader(inputStreamReader);
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append((line + "\n"));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	        	inputStreamReader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	public void postConnectData(){
		String url = "https://deco3801-007.uqcloud.net/phpmyadmin/db_connect2.php";
		
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
	    System.out.println(nameValuePairs);
	    setUpHttpPost(url,nameValuePairs);
	    
	}
	

	
//	public static void main(String[] args){
//		
//		dgs.setMacAddress("94-22-00-00-11-22");
//		dgs.setSSID("	test ing 123");
//		dgs.setLatitude("-27.500780");
//		dgs.setLongtitude("153.012733");
//		dgs.setLocation("GPS South UQ");
//		
////		postConnectData();
//		postInsertData();
//		
//		System.out.println("END");
//	}

}
