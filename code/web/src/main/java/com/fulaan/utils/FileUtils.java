package com.fulaan.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import sun.misc.BASE64Decoder;

public class FileUtils {

    public static InputStream BaseToInputStream(String base64string){  
        ByteArrayInputStream stream = null;
        try {
        BASE64Decoder decoder = new BASE64Decoder(); 
        byte[] bytes1 = decoder.decodeBuffer(base64string);  
        stream = new ByteArrayInputStream(bytes1);  
    } catch (Exception e) {
    // TODO: handle exception
    }
            return stream;  
        } 
}
