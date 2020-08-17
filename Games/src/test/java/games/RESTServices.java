
package games;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonParserType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class RESTServices {
		
		//Function to get statusCode for sent service
		//Returns status code
		public int getStatusCode(String url) throws  IOException 
		{
			 int statusCode=0;
			 HashMap<Integer, ResponseBody> hm = new HashMap<Integer, ResponseBody>();
		     HttpUriRequest request = new HttpGet(url);
		     HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
		     try
		     {
		    	 statusCode = httpResponse.getStatusLine().getStatusCode();
		    	 Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);	    
		     }
		     catch (AssertionError error)
		     {	    			    	 
		    		 System.out.println("Exception - "+ statusCode+" occured.");		 
		     }
		     return statusCode;
		}
		
		//Function to get response body of a sent request to dictionary webservice url
		//Returns Status Code, Response Body in Hash Map format
		public HashMap<Integer, ResponseBody> getResponse(String url) throws  IOException 
		{
			 int statusCode=0;
			 HashMap<Integer, ResponseBody> hm = new HashMap<Integer, ResponseBody>();
			//Verify connection
		     HttpUriRequest request = new HttpGet(url);
		     HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
		     try
		     {
		    	 statusCode = httpResponse.getStatusLine().getStatusCode();
		    	 Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);	    
		     }
		     catch (AssertionError error)
		     {
		    	 System.out.println("Error: " +"Service returned nothing for this request");
		     }
		     
		     // Verify type
		     String jsonMimeType = "application/json";	   
		     // Then
		     String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();
		     //Assert.assertEquals( jsonMimeType, mimeType );
		     
		     //Send request and receive response
		     RestAssured.baseURI = url;
		     RequestSpecification httpRequest = RestAssured.given();
		     Response response = httpRequest.get(url);
		     ResponseBody body = response.getBody();
		     //System.out.println("Response Body is: " + body.asString());
		     hm.put(statusCode, body);
		     return hm;
		}
		
		//Function to Compare two hashmaps
		//Returns true or false
		public boolean compareMap(LinkedHashMap<String, String> map1, LinkedHashMap<String, String> map2) 
		{
		    if (map1 == null || map2 == null)
		        return false;

		    for (String ch1 : map1.keySet()) {
		        if (!map1.get(ch1).equalsIgnoreCase(map2.get(ch1)))
		            return false;

		    }
		    for (String ch2 : map2.keySet()) {
		        if (!map2.get(ch2).equalsIgnoreCase(map1.get(ch2)))
		            return false;

		    }

		    return true;
		}
			
		//Function to parse json received from dictionary webservice
		//Returns String array of word details
		@SuppressWarnings("null")
		public ArrayList<String> parseJsonArrary(HashMap<Integer,ResponseBody> hm)
		{
			ArrayList<String> ar = new ArrayList<String>();
			ResponseBody body = null;
			int statusCode = 0;
			String[] str00 = null;
			
			for(int key:hm.keySet())
			{
				statusCode = key;
				body = hm.get(key);
			}
			
			if(statusCode!=200)
			{
				str00[0] = "Invalid Word";
				ar.add("Invalid Word");
				return ar;
			}
			String content = body.asString();
			
			int j=0,in=0;
			
			try {				
				Object jparse = new JSONParser().parse(content);				
				//parse json array
				JSONArray json = (JSONArray) jparse;
				for (; in < json.size(); in++) 
				{
						
						Object json0 = new JSONParser().parse(json.get(in).toString());
						JSONObject jo0 = (JSONObject) json0;
						Set<String> nodes0 = jo0.keySet();
						Iterator<String> it0 = nodes0.iterator();						
						//inbuilt json objects
						int t=0;			
						while (it0.hasNext()) 
						{
								int j1=0;								
								String str0 = it0.next().toString();
								String val0 = jo0.get(str0).toString();
								//System.out.println(key);
								if ( jo0.get(str0) instanceof JSONObject )
								{
									
									Object json00 = new JSONParser().parse(jo0.get(str0).toString());
									JSONObject jo00 = (JSONObject) json00;
									Set<String> nodes00 = jo00.keySet();
									Set<String> nodes01 = jo00.keySet();
									//System.out.println(nodes00.toString());
									//System.out.println(str00);									
									Iterator<String> it01 = nodes01.iterator();
									Object[] arr = jo00.keySet().toArray();
									//str00 = new String[t+arr.length];
									for(t=0;t<arr.length;t++)
									{
										if(arr[t].toString()!=null)
										{
											//str00[t]=(arr[t].toString());
											ar.add(arr[t].toString());
										}
									}
		//							System.out.println(str00);
								}
								else
								{
									t=t+1;
									if(str0!=null)
									{
										//str00[t] = str0;
										ar.add(str0);
									}									
								}
								
							}
						}
											
			} catch (ParseException e) {
				e.printStackTrace();
			}			
			return ar;
		}				
}
