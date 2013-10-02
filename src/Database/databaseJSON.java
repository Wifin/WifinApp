package Database;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class databaseJSON {
	
	public static void createJSON(JSONArray dataArray){
		try {

			JSONObject jsonMain = new JSONObject();

			int i = 0;
	
			 while(i < dataArray.length()){
				 JSONObject jsonObj = new JSONObject();
				 
				 for (i = 0; i < dataArray.length(); i++){
					 
					 JSONObject wifi = dataArray.getJSONObject(i);
					 jsonObj.put("WifiMacAddress", wifi.get("WifiMacAddress"));
				 	 jsonObj.put("WifiSSID"  , wifi.get("WifiSSID"));
				 	 jsonObj.put("WifiLatitude" , wifi.get("WifiLatitude"));
				 	 jsonObj.put("WifiLongtitude"  , wifi.get("WifiLongtitude"));
				 	 jsonObj.put("WifiLocation"  , wifi.get("WifiLocation"));		 
				 }
					 
				 jsonMain.put("Results" , jsonObj);
//				 System.out.println(hmData);
//				 jsonObj.put(i , hmData);
				 i+=1 ;
			}
			 
		//	 jsonObj.putAll(hmData);
			
			// write the content into xml file
			FileWriter file = new FileWriter("C:/Users/duangui/desktop/testJSON.json");
			file.write(jsonMain.toString()); 
			file.close();

			System.out.println(jsonMain);
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace();
		}
	}

//	public static void main(String[] args){
//		
//		createJSON();
//	}

}
