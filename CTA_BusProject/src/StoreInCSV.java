import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class StoreInCSV {
	
	public void csvStopsVSnoOfRoutes(Map<String,Integer> map_Stop_Sum) throws IOException{
		FileWriter writer = new FileWriter("C:/Users/Khushbu/Desktop/uic/main/DBMS/civisAnalyticsDemo/StopsVSNoOfRoutes2.csv");
		BufferedWriter bw = new BufferedWriter(writer);
		for(Map.Entry<String,Integer> entry : map_Stop_Sum.entrySet()) {
			//writer.append(entry.getKey()).append(",").append(entry.getValue().toString()).append(System.getProperty("line.seperator"));
			String content = entry.getKey() + ","+ entry.getValue().toString() + "\n";
			bw.write(content);
		}
		bw.close();
	}
	
	public void createCSV(Map<String,ArrayList<String>> map_stopID_routes) throws IOException {
		FileWriter writer = new FileWriter("C:\\Users\\Khushbu\\Desktop\\uic\\main\\DBMS\\civisAnalyticsDemo\\temp.csv");
		BufferedWriter bw = new BufferedWriter(writer);
		for(Map.Entry<String,ArrayList<String>> entry : map_stopID_routes.entrySet()) {
			//writer.append(entry.getKey()).append(",").append(entry.getValue().toString()).append(System.getProperty("line.seperator"));
			String content = entry.getKey() + ","+ entry.getValue().toString() + "\n";
			bw.write(content);
		}
		bw.close();
		
		
	}
	
	public void csvRouteVSStops(Map<String,Integer> map_Route_sum) throws IOException {
		
		FileWriter writer = new FileWriter("C:/Users/Khushbu/Desktop/uic/main/DBMS/civisAnalyticsDemo/RoutesVSNoOfStops2.csv");
		BufferedWriter bw = new BufferedWriter(writer);
		for(Map.Entry<String,Integer> entry : map_Route_sum.entrySet()) {
			//writer.append(entry.getKey()).append(",").append(entry.getValue().toString()).append(System.getProperty("line.seperator"));
			String content = entry.getKey() + ","+ entry.getValue().toString() + "\n";
			bw.write(content);
		}
		bw.close();
		
		
		
	}

}
