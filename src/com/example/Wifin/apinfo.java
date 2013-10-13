package com.example.Wifin;
/**
 * complex type for store custom wifi info 
 */
public class apinfo {

		private double lat, lon;
		private String title,mac;
		private int level;

		public apinfo (double lat, double lon, String title, int level,String mac) {
		    super();
		    this.lat = lat;
		    this.lon = lon;
		    this.title = title;
		    this.level = level;
		    this.mac = mac;
		}
		public double getlat() {
		    return lat;
		}
		public void set_lat(double num) {
		    this.lat = num;
		}
		public double getlon() {
		    return lon;
		}
		public void setlon(double num) {
		    this.lon = num;
		}
		public String gettitle() {
		    return title;
		}
		public void set_title(String message) {
		    this.title = message;
		}
		public int getlevel() {
		    return level;
		}
		public void set_level(int num) {
		    this.level = num;
		}
		public String getmac() {
		    return mac;
		}
		public void set_mac(String str) {
		    this.mac = str;
		}
}