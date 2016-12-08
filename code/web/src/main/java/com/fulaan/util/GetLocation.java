package com.fulaan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by admin on 2016/12/7.
 */
public class GetLocation {

    /**
    * @param addr
    * 查询的地址
    * @return
    * @throws IOException
    */
    public static String[] getCoordinate(String addr) throws IOException {
        String address = "";
        String lat = "";
        String lng = "";
        try {
            address = java.net.URLEncoder.encode(addr,"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/geocoder/v2/?"
                +"ak=4rcKAZKG9OIl0wDkICSLx8BA&output=json&address=%s",address);
        URL myURL = null;
        URLConnection httpsConn = null;
        //进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {

        }
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    if(data.contains("\"lat\":")){
                        lat = data.substring(data.indexOf("\"lat\":")
                                + ("\"lat\":").length(), data.indexOf("},\"precise\""));
                        lng = data.substring(data.indexOf("\"lng\":")
                                + ("\"lng\":").length(), data.indexOf(",\"lat\""));
                    }

                }
                insr.close();
            }
        } catch (IOException e) {
        }
        return new String[]{lng,lat};
    }

    public static void main(String[] args) throws Exception{
        Object[] o = getCoordinate("杭州市余杭区高教路");

        System.out.println(o[0]);//经度
        System.out.println(o[1]);//纬度
    }
}
