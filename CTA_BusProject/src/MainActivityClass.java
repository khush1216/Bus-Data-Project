import java.util.*;

import com.mysql.jdbc.Connection;

public class MainActivityClass {
	
	private static ArrayList<String> locAttr = new ArrayList<String>();
	
	public static void main(String args[]) {
		
		//test connection
		DbConnection dbobj = new DbConnection();
		Connection connObject = dbobj.dbConnect();
		
		//getting all routes
		Set<String> distRoutes1 = new HashSet<String>();
		distRoutes1 = dbobj.getDistinctRoutes(connObject);
	
		//map for stop_ids and corresponding bus routes
		Map<String,ArrayList<String>> map1 = new HashMap<String,ArrayList<String>>();
		map1 = dbobj.getStopIds(connObject);
		
		//writing to csv
		StoreInCSV str = new StoreInCSV();
		try {
		str.createCSV(map1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
		
		//maps the bus routes to number of stops each one contains
		Map<String,Integer> map_stopId_routes = new HashMap<String,Integer>();
		map_stopId_routes = dbobj.mapLongestBusRoute(map1, distRoutes1, connObject);
		
		
		//getting the longest bus route
		dbobj.getLongestBusRoute(map_stopId_routes);
		
		//stop that appears on most bus-routes
		dbobj.getStopHasMostRoutes(map1);
		
		//map for distinct routes vs total no of stops corresponding to each route
		try {
			str.csvRouteVSStops(map_stopId_routes);
			str.csvStopsVSnoOfRoutes(dbobj.mapStopsVSNoOfRoutes());
			}
		catch(Exception e) {
			e.printStackTrace();
			}	
				
		//making an http request to get the distance between two points based on location/ get other location details as a JSON object
		String stop_id = "260";
		
		try {
			locAttr = dbobj.getLocation(stop_id, connObject);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//String urlReq1 = "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyC6LGASLYK2Ts6cckVhy3k_l1LwEsrKCJo";

		
		//send http request to get geocode
		HttpRequestGeocode geoObj = new HttpRequestGeocode();
		String shortestDistAPI = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=41.86564859,-87.75467392&destinations=41.86578791,-87.74732747&key=AIzaSyC6LGASLYK2Ts6cckVhy3k_l1LwEsrKCJo";				
		String actualLocationAPI = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+locAttr.get(0)+","+locAttr.get(1).trim()	+"&key=AIzaSyC6LGASLYK2Ts6cckVhy3k_l1LwEsrKCJo";


//		//geoObj.makeHttpRequest(actualLocationAPI);
		geoObj.makeHttpRequest(shortestDistAPI);
		
		//close connection
		dbobj.dbDisconnect(connObject);
		
	}

}
