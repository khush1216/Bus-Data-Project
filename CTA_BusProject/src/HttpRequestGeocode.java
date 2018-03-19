import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpRequestGeocode {
	
	private HttpURLConnection connection = null;

	
	//make an http request
	public void makeHttpRequest(String targetURL) {
	
		
		try {
			URL url = 	new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    connection.getInputStream(), Charset.forName("UTF-8")));
			
		   String inputLine;
		   while ((inputLine = in.readLine()) != null) 
	            System.out.println(inputLine);
	        in.close();  
		   
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
				
	}

}
