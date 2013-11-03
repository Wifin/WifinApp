package com.example.Wifin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** 
 * class for handling database
 */
public class DataBaseHelper extends SQLiteOpenHelper{
	
	/** 
	 * The Android's default system path of your application database.
	 */
    private static String DB_PATH = "/data/data/com.example.Wifin/databases/";
 
    /** 
	 * your database name to store location information
	 */
    private static String DB_NAME = "wifin.db";
 
    /** 
	 * use SQLite database
	 */
    private SQLiteDatabase myDataBase; 
 
    /** 
	 * Context for DataBaseHelper class
	 */
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to
     * the application assets and resources.
     * 
     * @param context - context for DataBaseHelper method
     */
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    
    /**
    * Creates a empty database on the system and rewrites it with your own database.
    */
    public void createDataBase() throws IOException{
    	
    	//By calling this method and empty database will be created into the default system path
        //of your application so we are gonna be able to overwrite that database with our database.
        this.getReadableDatabase();
        
        //try to copy database to DB_PATH directory
        try {
        	copyDataBase();
        	} catch (IOException e) {
        		throw new Error("Error copying database");
        		}
        }
    
    /**
     * boolean method to check whether database existing
     * @return if database exist in that place, return true, if not return false
     */
    public boolean checkDataBase(){
    	String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if(file.exists()){
            return true;
        }
        else{
            return false;
        }    
     }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     */
    public void copyDataBase() throws IOException{
    	
    	//URL of my web server linked to my database source file
    	Thread dx = new Thread() {
    	    public void run(){
    	        try{    	
    	            URL url = new URL("your db file location");  	
    	            //open a connection
    	            url.openConnection();
    	
    	            //Open your local db as the input stream
    	            InputStream input = new BufferedInputStream(url.openStream());
 
    	            // Path to the just created empty db
    	            String outFileName = DB_PATH + DB_NAME;
 
    	            //Open the empty db as the output stream
    	            OutputStream output = new FileOutputStream(outFileName);
 
    	            //transfer bytes from the inputfile to the outputfile
    	            byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
    	        catch (Exception e)
    	        {
    		        e.printStackTrace();
                    Log.i("ERROR ON DOWNLOADING FILES", "ERROR IS" +e);
    	        }
    	    }
    	};
    	dx.start();
 
    }
 
    /**
     * open database
     */
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
    
    /**
     * Database query by select mac and ssid, get lat and lon
     * 
     * @param mac - MAC address for access point
     * @param ssid - SSID name for access point
     * 
     * @return cur - all informations about this access point(mac,ssid,lat,lon).
     * */
    public Cursor wifinquery(String mac,String ssid)            
    {  
    	String[] tableColumns = new String[] {"mac","ssid","lat","lon"};
        String whereClause = "mac = ? AND ssid = ?";
    	//mac,ssid is value here used to do the query
        String[] whereArgs = new String[] {mac,ssid};
    	String orderBy = "ssid";

    	openDataBase();
    	//cursor query method
        Cursor cur = myDataBase.query("wifinTb", tableColumns, whereClause, whereArgs, null, null, orderBy);
        cur.moveToFirst();
        return cur;
    }
 
    /**
     * close database when not using
     */
    @Override
	public synchronized void close() {
    	
    	if(myDataBase != null)
    		
    		myDataBase.close();
    	
    	super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {		
		
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
}