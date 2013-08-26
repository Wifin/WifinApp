package Database;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class databaseXML {
	
	public databaseXML(){}
	
	public static void createXML(ResultSet rs){
		int i = 1;
		try{
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbfactory.newDocumentBuilder();
			
			// Wifi Details elements
			Document dataDoc = db.newDocument();
			
			 Element mainRootElement = dataDoc.createElement("Wifi_Strength_Visualization");
			 dataDoc.appendChild(mainRootElement);
		
			 while(rs.next()){
				 	mainRootElement.appendChild(getWifi(rs, dataDoc , i));
					i ++ ;
				}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dataDoc);
			StreamResult result = new StreamResult(new File("C:/Users/duangui/desktop/testfile.xml"));
			
			transformer.transform(source, result);

		}catch(Exception e){
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private static Node getWifi(ResultSet r, Document doc, int i){

		String wifiNo = "Wifi_" + ("" + i);
		
		Element wifi = doc.createElement(wifiNo);
		
		try{
			wifi.appendChild(getWifiElement(doc, "WifiMacAddress" , r.getString("WifiMacAddress")));
			wifi.appendChild(getWifiElement(doc, "WifiSSID" , r.getString("WifiSSID")));
			wifi.appendChild(getWifiElement(doc, "WifiLatitude" , r.getString("WifiLatitude")));
			wifi.appendChild(getWifiElement(doc, "WifiLongtitude" , r.getString("WifiLongtitude")));
			wifi.appendChild(getWifiElement(doc, "WifiFrequency" , r.getString("WifiFrequency")));
			wifi.appendChild(getWifiElement(doc, "SignalStrength" , r.getString("SignalStrength")));
		}catch(Exception e){}

		return wifi;
	}
	
	private static Node getWifiElement(Document doc, String name , String value){
		Element wifiElement = doc.createElement(name);
		wifiElement.appendChild(doc.createTextNode(value));
		return wifiElement;
	}
	
	private static ResultSet connectToDatabase(){
		databaseConnection dbc = new databaseConnection();
			
		String directory = "jdbc:mysql://127.0.0.1:3306/WifiStrengthDB";
		String user = "root"; 
		String password =  "manu1990";
			
		String query = "SELECT * FROM wifi, wifiStrength " +
				"WHERE wifi.WifiMacAddress = wifistrength.WifiMacAddress";
		
		Connection conn = null;
		Statement stmt  = null;
		ResultSet rs = null;
		
		try{
			conn = (Connection) dbc.setUpConnection(directory, user, password);
			stmt  = conn.createStatement();
			rs = stmt.executeQuery(query);
			
		}catch(Exception e){}
		
		return rs;
	}
	
	
	public static void main(String[] args){
		
		createXML(connectToDatabase());
	}

}



//Element wifiDetailsElement = dataDoc.createElement("WifiDetails");
//wifiDetailsElement.setAttribute("Number of Wifi", "" + i);
//dataDoc.appendChild(wifiDetailsElement);
//
////WifiMacAddress element 
//Element WifiMacAddressElement = dataDoc.createElement("WifiMacAddress");
//wifiDetailsElement.appendChild(WifiMacAddressElement);
//
////WifiSSID element 
//Element WifiSSIDElement = dataDoc.createElement("WifiSSID");
//wifiDetailsElement.appendChild(WifiSSIDElement);
//
////WifiLatitude element 
//Element WifiLatitudeElement = dataDoc.createElement("WifiLatitude");
//wifiDetailsElement.appendChild(WifiLatitudeElement);
//
////WifiLongtitude element 
//Element WifiLongtitudeElement = dataDoc.createElement("WifiLongtitude");
//wifiDetailsElement.appendChild(WifiLongtitudeElement);
//
////WifiFrequency element 
//Element WifiFrequencyElement = dataDoc.createElement("WifiFrequency");
//wifiDetailsElement.appendChild(WifiFrequencyElement);
//
////WifiSignalStrength element 
//Element WifiSignalStrengthElement = dataDoc.createElement("WifiSignalStrength");
//wifiDetailsElement.appendChild(WifiSignalStrengthElement);