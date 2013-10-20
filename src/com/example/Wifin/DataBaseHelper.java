package com.example.Wifin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.Wifin/databases/";
 
    private static String DB_NAME = "wifin.db";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
//   	    if(checkDataBase()){
//    		//do nothing - database already exist
//    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
//    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
//    private boolean checkDataBase(){
// 
//    	SQLiteDatabase checkDB = null;
// 
//    	try{
//    		String myPath = DB_PATH + DB_NAME;
//    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
// 
//    	}catch(SQLiteException e){
// 
//    		//database does't exist yet.
// 
//    	}
// 
//    	if(checkDB != null){
// 
//    		checkDB.close();
// 
//    	}
//    	
//    	//if DB not null return true, else fasle
//    	return checkDB != null ? true : false;
//    }
    
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
     * This is done by transfering bytestream.
     * */
    public void copyDataBase() throws IOException{
 
    	//url of my webserver link point to my db
    	Thread dx = new Thread() {
    	    public void run(){
    	        try{    	
    	            URL url = new URL("http://14.202.106.108/wifin.db");  	
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
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
    
    /**
     * database query select mac and ssid, get lat and lon
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