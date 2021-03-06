package com.example.Wifin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.util.JsonReader;

/**
 * class for read json file
 */
public class jsonReader {
	
	/**
	 * main method to execute json reader
	 * @param in - the source json input stream for prepared for read
	 */
	public List<apinfo> readJsonStream(InputStreamReader in) throws IOException {
		JsonReader reader = new JsonReader(in);
		try {
			return readLocationArray(reader);
			}
		finally {
			reader.close();
			}
		}
	     
	/**
	 * read the structure of a json file
	 * @param reader - json reader
	 */
	public List<apinfo> readLocationArray(JsonReader reader) throws IOException {
        List<apinfo> messages = new ArrayList<apinfo>();
        String status = null;
        int num = 0;
        reader.beginObject();
        while (reader.hasNext()){
        	String name = reader.nextName();
            if (name.equals("status")) {
                status = reader.nextString();
            }else if (name.equals("num_results")){
	            num = reader.nextInt();
	        }else if (name.equals("results")){
	        	reader.beginArray();
	        	while(reader.hasNext())
	        	{
            	    messages.add(readMessage(reader));
	        	}
            	reader.endArray();
            	}
            }
        reader.endObject();
        return messages;
        }
	
	/**
	 * get the details data in each json file, return into custom type apinfo
	 * @param reader - json reader
	 */
	public apinfo readMessage(JsonReader reader) throws IOException {
	    Double lat = null;
	    Double lon = null;
	    String title = null;
	    int level = 0;
	    String mac = null;
	    String ctype =null;
	     
	    reader.beginObject();
	    while (reader.hasNext()) {
	        String name = reader.nextName();
	        if (name.equals("lat")) {
	            lat = reader.nextDouble();
	        } else if (name.equals("lng")) {
	            lon = reader.nextDouble();
	        } else if (name.equals("title")) {
	            title = reader.nextString();
	        } else if (name.equals("level")) {
	            level = reader.nextInt();
	        }else if (name.equals("mac")) {
		        mac = reader.nextString();
	        }else if (name.equals("capabilities")) {
	        	ctype = reader.nextString();
	        }else {
	            reader.skipValue();
	        }
	     }
	     reader.endObject();
	     
	     return new apinfo(lat, lon, title, level,mac,ctype);
	   }

}