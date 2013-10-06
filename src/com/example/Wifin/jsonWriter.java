package com.example.Wifin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.util.JsonWriter;

public class jsonWriter {
	JsonWriter writer;
	public void writeJson(OutputStream out,int size,String ssid,String mac,double lat,double lon) throws IOException {
	     writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
	     writer.setIndent("  ");
	     jsonFinal(writer,size,ssid,mac,lat,lon);
	}
	//level1
	public void jsonFinal (JsonWriter writer,int size,String ssid,String mac,double lat,double lon) throws IOException{
	     writer.beginObject();
	     writer.name("status").value("OK");
	     writer.name("num_results").value(size);
	     writer.name("results");
	     writeLocationArray(writer,ssid,mac,lat,lon);
	     writer.endObject();
	 }
	 //level2
	 public void writeLocationArray(JsonWriter writer,String ssid,String mac,double lat,double lon) throws IOException {
	     writer.beginArray();
	     writeLocation(writer,ssid,mac,lat,lon);
	     writer.endArray();
	 }
	 //level3
	 public void writeLocation(JsonWriter writer,String ssid,String mac,double lat,double lon) throws IOException {
	     writer.beginObject();
	     writer.name("lat").value(lat);
	     writer.name("lng").value(lon);
	     writer.name("title").value(ssid);
	     writer.endObject();
	     System.out.println("Write successful!");
	 }
}
