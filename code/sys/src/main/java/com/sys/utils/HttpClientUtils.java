package com.sys.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;


/**
 * 处理rest风格请求
 * @author fourer
 *
 */
public class HttpClientUtils {


	public static String get(String url) throws IOException {
		CloseableHttpClient http = HttpClientBuilder.create().build();
		 HttpGet get = new HttpGet(url);
		HttpResponse response = http.execute(get);
		 if (response.getStatusLine().getStatusCode() == 200) {  
		    HttpEntity entity = response.getEntity();
		    String res= convertStreamToString(new InputStreamReader(
					entity.getContent(), "UTF-8"));
		    return res;
		} 
		return null;
	}

	/**
	 *  http请求
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
     */
	public static String get(String url,Map<String,String> params)  throws IOException{

		String encodeUrl = url + "?" + urlEncode(params);
		return get(encodeUrl);
	}

	/**
	 * encode url
	 * @param map
	 * @return
	 */
	public static String urlEncode(Map<String,String> map) {

		String retVal = "";
		if(map == null) return retVal;
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = map.get(key);
			try {
				String encodeValue = URLEncoder.encode(value, "UTF8");
				retVal += key + "=" + encodeValue + "&";

			} catch (UnsupportedEncodingException ex) {

			}
		}

		return retVal.substring(0,retVal.length() - 1);
	}


	public static String strURLEncodeUTF8(String value){

		try {
			return URLEncoder.encode(value, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取流文件
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStream(String url) throws IOException{

		CloseableHttpClient http = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		HttpResponse response = http.execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();

			System.out.println(entity.getContent() + "===");
			return entity.getContent();
		}
		return null;
	}

	public static String convertStreamToString(java.io.InputStreamReader inputStream) {
        BufferedReader reader = new BufferedReader(inputStream);
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line);      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                inputStream.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
        }
        return sb.toString();
    }
	
	
}
