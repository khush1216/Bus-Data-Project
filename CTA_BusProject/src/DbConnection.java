import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DbConnection {
	
	private ArrayList<ArrayList<String>> routeList2 = new ArrayList<ArrayList<String>>();
	private Map<String,Integer> mapStopVSnoOfRoutes = new HashMap<String,Integer>();
	
	//get connection
	public Connection dbConnect() {
		Connection con = null;
		
		try {
			con = (Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/ridesharing","root","password123!");
			if(con != null) {
				
				System.out.println("Connection established!!");
			}
			else {
				System.out.println("Something went wrong!");
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return con;
		
	}
	
	//stop connection
	public void dbDisconnect(Connection con) {
		if(con != null) {
			try {
				con.close();
				System.out.println("Connection Closed!");
			}
			catch(SQLException e) {
				System.out.println("Unable to close the connection.");
				e.printStackTrace();
			}
		}
	}
	
	//get the actual route list separated by commas from a single string of routes
	public ArrayList<ArrayList<String>> getUniqueRoutesFromList(ArrayList<String> routeList) {
		
		for(String x: routeList) {
			ArrayList<String> smallList = new ArrayList<String>();
			try {
				String xc[] = x.split(",");
				for(int i=0;i<xc.length;i++) {
					smallList.add(xc[i]);
				}
				}
			catch(Exception e) {
				smallList.add(x);	
			}
			
			routeList2.add(smallList);
		}
		//print 
		//System.out.println(Arrays.toString(routeList2.toArray()));
		return routeList2;

	}
	
	//get set of distinct routes
	public Set<String> getDistinctRoutes(Connection con) {
		
		ArrayList<String> routeList = new ArrayList<String>();
		Set<String> distinctRoutes = new HashSet<String>();
		
		Statement stmt = null;
		String query = "select routes from cta_dataset";
		
		
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				
				routeList.add(rs.getString("routes"));	
			}
			//print
//			for(String x:routeList) {
//				System.out.println(x);
//			}
			
			getUniqueRoutesFromList(routeList);
			
			for(List x1: routeList2) {
				
				for(int i=0;i<x1.size();i++) {
					distinctRoutes.add((String) x1.get(i));
				}
			}
			
			//print
//			for(String x:distinctRoutes) {
//				System.out.println(x);
//				
//			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return distinctRoutes;

	}
	
	//get latitude and longitude in an arraylist to find shortest path between 2 points
	public ArrayList<String> getLocation(String stop_id, Connection con) throws SQLException {
		
		Statement stmt = null;
		String loc="";
		String query = "select location from cta_dataset where stop_id like "+ "\"" + stop_id + "\"";
		ArrayList<String> locAttributes = new ArrayList<String>();
		
		try {
			
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				loc = rs.getString("location");
				//System.out.println(rs.getString("location"));
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			String splitLoc[] = loc.split(",");
			locAttributes.add(splitLoc[0].substring(1));
			locAttributes.add(splitLoc[1].substring(0, splitLoc[1].length()-1));
	        if (stmt != null) { stmt.close(); }
	    }
		
		return locAttributes;
	}	
	
	//get the map of stopids vs the list of routes in each
	public Map<String,ArrayList<String>> getStopIds(Connection con) {
		Statement stmt = null;
		String query = "select stop_id from cta_dataset";
		Map<String,ArrayList<String>> mapID_routes = new HashMap<String,ArrayList<String>>();

		
		ArrayList<String> stop_idsL = new ArrayList<String>();
		
		try {
			stmt = (Statement)con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				stop_idsL.add(rs.getString("stop_id"));
			}
			 //System.out.println(stop_idsL.size());
			 //System.out.println(Arrays.toString(stop_idsL.toArray()));

			 
			 Iterator<String> it1 = stop_idsL.iterator();
			 Iterator<ArrayList<String>> it2 = routeList2.iterator();
			 
			 while(it1.hasNext() && it2.hasNext()) {
				 mapID_routes.put(it1.next(), it2.next());
			 }
			
			for(int i=0;i<stop_idsL.size();i++) {
//				if(routeList2.get(i).get(0) == "") {
//					mapID_routes.put(stop_idsL.get(i), null);
//
//				}
				//else {
				mapID_routes.put(stop_idsL.get(i), routeList2.get(i));
				//}
			}
			//print
//			for (Map.Entry<String, ArrayList<String>> entry : mapID_routes.entrySet()) {
//			    System.out.println(entry.getKey()+" : "+entry.getValue());
//			}
//			System.out.println(mapID_routes.size());
//			
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}

		
		return mapID_routes;
	}
	
	//maps the bus routes to number of stops each one contains
	public Map<String,Integer> mapLongestBusRoute(Map<String,ArrayList<String>> map1,Set<String> distinctRoutes, Connection con) {
		
		Map<String,Integer> res = new HashMap<String,Integer>();
		for(String x: distinctRoutes) {
			
			for (Map.Entry<String, ArrayList<String>> entry : map1.entrySet()) {
				
				
				
				if(entry.getValue().contains(x)) {
					if(res.containsKey(x)) {
						int value = res.get(x) + 1;
						res.put(x, value);
					}
					else {
						res.put(x, 1);
					}	
				}
			}
		}
		
		//print
//		for (Map.Entry<String,Integer> entry : res.entrySet()) {
//		    System.out.println(entry.getKey()+" : "+entry.getValue());
//		}
////		
		return res;
				
	}
		
	//get the longest bus route
	public String getLongestBusRoute(Map<String,Integer> mapStopidsRoutes) {
		
		int maxVal = 0;
		String key = "";
		for (Map.Entry<String, Integer> entry : mapStopidsRoutes.entrySet()) {
			if(entry.getValue() > maxVal) {
				maxVal = entry.getValue();
				key = entry.getKey();
			}
		}
		
		System.out.println("The bus route - " +key+ " - has the longest route with - " + mapStopidsRoutes.get(key) + " - stops");
		
		return key;
			
		
	}
	
	//returns the private map of stopIDS vs no of routes
	public Map<String,Integer> mapStopsVSNoOfRoutes(){
		return mapStopVSnoOfRoutes;
	}
	
	//stop that has the most routes
	public String getStopHasMostRoutes(Map<String,ArrayList<String>> mapStopsRoutes) {
		int sizeOfRouteList = 0;
		String resultStop = "";
		
		for(Map.Entry<String,ArrayList<String>> entry:mapStopsRoutes.entrySet() ) {
			
			mapStopVSnoOfRoutes.put(entry.getKey(), entry.getValue().size());
			
			if(entry.getValue().size() > sizeOfRouteList) {
				sizeOfRouteList = entry.getValue().size();
				resultStop = entry.getKey();
			}
			
		}
		
		System.out.println("The stop that has maximum bus Routes is - "+resultStop + mapStopsRoutes.get(resultStop).toString());
		
		return resultStop;
		
	}
	
}
