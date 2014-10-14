package com.example.traveljoin.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;

public class ApiInterface {
	protected static final int CONNECTION_TIMEOUT = 3000;  
    protected static final int WAIT_RESPONSE_TIMEOUT = 5000;  
    
	public ApiResult GET(String url){
		InputStream inputStream = null;
		String result = "";
		ApiResult api_result = null;
		
		try {

			// create HttpClient
			
			//HttpParams httpParameters = new BasicHttpParams();
			//HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			//HttpConnectionParams.setSoTimeout(httpParameters, WAIT_RESPONSE_TIMEOUT);
			//HttpConnectionParams.setTcpNoDelay(httpParameters, true);
			//httpParameters.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// make GET request to the given URL
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			//httpGet.setHeader("Content-Type", "application/x-www-form-url-encoded; charset=utf-8");
			HttpResponse httpResponse = httpclient.execute(httpGet);

			//GET STATUS_CODE
			int status = httpResponse.getStatusLine().getStatusCode();
			
			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();						
			
			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "";
			
            api_result = new ApiResult(status, result);

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return api_result;
	}

//	public ApiResult GET_URL(String url){
//		HttpURLConnection urlConnection = null;
//		InputStream inputStream = null;
//		String result = "";
//		ApiResult api_result = null;
//		
//		try {
//
//			// create HttpClient
//			URL connection_url = new URL(url);
//			urlConnection = (HttpURLConnection) connection_url.openConnection();
//			
//			//GET STATUS_CODE
//			int status = urlConnection.getResponseCode();			
//			inputStream  = new BufferedInputStream(urlConnection.getInputStream());		    										
//			
//			// convert inputstream to string
//			if(inputStream != null)
//				result = convertInputStreamToString(inputStream);
//			else
//				result = "";
//			
//            api_result = new ApiResult(status, result);           		     
//		} 		
//		catch (Exception e) {
//			Log.d("InputStream", e.getLocalizedMessage());
//		}
//		finally { 		     
//			urlConnection.disconnect();
//		}
//
//		return api_result;
//	}
	

    public ApiResult POST(String url, Object object, String method){
        InputStream inputStream = null;
        String result = "";
        ApiResult api_result = null;
        
        try {

            // 1. create HttpClient
        	HttpParams params = new BasicHttpParams();
        	params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpClient httpclient = new DefaultHttpClient(params);

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            JSONObject jsonObject = null;
            
            // 3. build jsonObject
            if (object instanceof Poi){
            	if (method == "delete"){
            		jsonObject = new JSONObject();
            		jsonObject.put("id", ((Poi) object).getId() );
            	}
            	else{
            		jsonObject = ((Poi) object).toJSON();	
            	}            	
            }
            
            if (object instanceof Group) {
            	jsonObject = ((Group) object).toJSON();
            }
            
            if (object instanceof User) {
            	jsonObject = ((User) object).toJSON();
            }
            
            if (object instanceof Tour){
            	if (method == "delete"){
            		jsonObject = new JSONObject();
            		jsonObject.put("id", ((Tour) object).getId() );
            	}
            	else{
            		jsonObject = ((Tour) object).toJSON();	
            	}            	
            }
            	

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

			//9. GET STATUS_CODE
            int status = httpResponse.getStatusLine().getStatusCode();
            
            // 10. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // 11. convert inputstream to string
            if(inputStream != null)
            	result = convertInputStreamToString(inputStream);
            else
            	result = "";
            
            api_result = new ApiResult(status, result);            

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return api_result;
    }
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
//    public boolean isConnected(){
//    	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
//    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//    	    if (networkInfo != null && networkInfo.isConnected()) 
//    	    	return true;
//    	    else
//    	    	return false;	
//    }
    

}
