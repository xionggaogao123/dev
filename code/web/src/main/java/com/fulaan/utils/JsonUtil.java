package com.fulaan.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private JsonUtil(){}
    public static String toJson(Object o){
        return null;
    }
    public static Object fromJson(String jsonText){
//    	return JSON.toJavaObject((JSON)(JSON.parse(jsonText)), Object.class);
    	return new Object();
    }
    
    /**
     * Convert Json to Map
     *
     * @throws java.io.IOException
     * @throws com.fasterxml.jackson.core.JsonParseException
     */
    public static Map<String, Object> Json2Map(String jsonStr) {

    	
    	Gson gson =new Gson();
    
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            // convert JSON string to Map
            map = mapper.readValue(jsonStr, Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    
    public static void main(String[] args){
    	/*String str = "abcd";
    	str = toJson(str);
    	System.out.println(str);*/
//    	JSON json = (JSON) fromJson("abc");
    	System.out.println(fromJson("abc"));
    }
}
