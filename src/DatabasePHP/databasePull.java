package DatabasePHP;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import Database.databaseJSON;

public class databasePull {
 
    static InputStream is = null;
    static JSONObject jObj;
    static String json = "";
 
    // constructor
    public databasePull() {
 
    }
    
    private static JSONObject setUpHttpPull(String url,  List<NameValuePair> nameValuePairs){
	    try {
	        DefaultHttpClient httpClient = new DefaultHttpClient();
           // String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            System.out.println(httpResponse.getEntity());
//            HttpEntity httpEntity = httpResponse.getEntity();
//            is = httpEntity.getContent();
//            StatusLine statusLine = httpResponse.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
//            if (statusCode == 200) {
            	 HttpEntity httpEntity = httpResponse.getEntity();
                 is = httpEntity.getContent();
                 StringBuilder builder = new StringBuilder();
              BufferedReader reader = new BufferedReader(new InputStreamReader(is));
              String line;
              while ((line = reader.readLine()) != null) {
                builder.append(line);
              }
              is.close();
              json = builder.toString();
//		}
            
            jObj = new JSONObject(json);
	    }catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (JSONException e) {
           // Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        
        System.out.println(jObj);
        return jObj;
        
   
	}//end of setUpHttpPull()
    
	/**
	 * This method will create the url and list for storing the data. 
	 * The setUpHttpPost() method will be called to connect to the server.
	 *  */
	public static void pullQueryDB() {
		
		//The url for inserting the data into the database.
		String url = "http://deco3801-007.uqcloud.net/phpmyadmin/queryDB.php";
		
//		String url = "http://localhost/phpmyadmin/insertDB2.php";
		
	    // Add your data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    
	    JSONObject jobj = setUpHttpPull(url,nameValuePairs);

//		try {
//			JSONArray dataArray = jobj.getJSONArray("Wifi");
//			new databaseJSON().createJSON(dataArray);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//JSONObject data = jArray.

	}//end of postInsertData()
	
	public static void main(String[] args){
		
			pullQueryDB();
	
		
	}
 
}
 
