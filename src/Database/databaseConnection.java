package Database;

import java.sql.*; 


/**
 * The class for the database connection and adding / retrieving of data
 * 
 * */
public class databaseConnection {
	/**
	 * The connection class variable. 
	 * Used for establishing connection to database
	 * */
	private  Connection conn = null;
	
	/**
	 * The Statement class instance for executing the statement
	 * */
	private  Statement state = null;
	
	/**
	 * The ResultSet class instance for retrieving the result from database
	 * */
	private  ResultSet rs  = null;
	
	public databaseConnection(){
	}

	/**
	 *  The method will set up the connection to the database 
	 */
	public Connection setUpConnection(String dir, String user, String pass){
		try{
			// calling for the jdbc driver of mysql
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dir, user, pass);

		}catch (Exception e){
			//handling of exception.
			System.out.println("ERROR - cannont connect to database!");
		}	
		return conn;
	}// end of setUpConnection()
	
	/**
	 * This method will add the Wifi detected into the database.
	 * */
	public boolean AddWifi(Connection con, String mAdd, String stmt){
		try {
			String mac = "11-22-00-00-00-00";
			String ssid = "BigPond1";
			String latitude = "-27.499768"; 
			String longtitude = "152.967758";
			String frequency = "50";
			
			state = con.createStatement();
			
			if(!getMacAddress(state, mAdd)){
				System.err.println("MAC ADDRESS IN USED!");
				return false;
			}
			
			
//			String insertWifi = "INSERT INTO wifi " +
//					"(WifiMacAddress, WifiSSID, WifiLatitude, " +
//					"WifiLongtitude, WifiFrequency)" +
//					"VALUES ('" + mac +"','" + ssid + "'," +
//							"'" + latitude +"','" + longtitude +"'," +
//							"'" + frequency +"')";
//			
//			String insertWifiStr = "INSERT INTO WifiStrength" +
//					"(SignalStrength, WifiMacAddress)" +
//					"VALUES ( '60' , '"+ mac +"' )" ;
//			
//			state.execute(insertWifi);
//			state.execute(insertWifiStr);
			state.execute(stmt);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
		return true;
	}// end of AddWifi()
	
  /**
   * This method will perform a check on the Mac Address in the database.
   * This will serve as a prevention for data duplication. 
   * 
   * @param mac - the mac address to be check on
   * @return true if mac address does not appear in database. 
   * @throws SQLException
   * */
  private boolean getMacAddress(Statement stmt, String mac){
	  try {
		  //Query for retrieving the mac address.
		  String query = "SELECT WifiMacAddress FROM Wifi";

		  rs = stmt.executeQuery(query);
		  while(rs.next()){
			  if(rs.getString("WifiMacAddress").contains(mac)){
				  return false;
			  }
		  }

	} catch (Exception e) {
		// TODO Auto-generated catch block
		 System.err.println(e);
	}
	  return true;  
	}// end of getMacAddress()
	
	public void viewDB(){
		try {
			String query = "SELECT * FROM Wifi ";
		
			state = conn.createStatement();
			rs = state.executeQuery(query);
			
			while(rs.next()){
				String dataInDB = rs.getString(1) + " " + rs.getString(2)+ " " 
								+ rs.getString(3) + " " + rs.getString(4) + " "
								+ rs.getString(5);
				System.out.println(dataInDB);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end of viewDB()
	
	/**
	 * The method for closing a connection for the database.
	 * */
	private void closeConnection(){
		try {			
			conn.close();
			state.close();
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end of closeConnection()
	
//	public static void main(String [] args){
//		setUpConnection();
//		AddWifi(); 	
//		//viewDB();
//		closeConnection();
//		
//	}
}
