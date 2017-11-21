package com.fulaan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scott on 2017/11/20.
 */
public class GetApkSize {

    public static Map<String,Object> getFilePath(File file){
        FileChannel fc= null;
        Map<String,Object> retMap=new HashMap<String,Object>();
        try {
            if (file.exists() && file.isFile()){
                FileInputStream fis= new FileInputStream(file);
                fc= fis.getChannel();
                System.out.println(fc.size());
                retMap.put("size",fc.size());
                retMap.put("apkSize",getApkSize(fc.size()));
            }else{
                System.out.println("file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (null!=fc){
                try{
                    fc.close();
                }catch(IOException e){
                    System.out.println(e);
                }
            }
        }
        return retMap;
    }

    public static Double divide(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static String getApkSize(long size){
        long s_MB=1024L*1024L;
        long s_KB=1024L;
        String result;
        if(size>=s_MB){
            double m=divide((double)size,(double)s_MB,2);
            result=m+"MB";
        }else if(size<s_MB&&size>=s_KB){
            double k=divide((double)size,(double)s_KB,2);
            result=k+"KB";
        }else {
            result=size+"B";
        }
        return result;
    }

    public static void main(String[] args) {
        File file=new File("D:\\apkSize\\com.miui.calculator.apk");
        Map<String,Object> result=getFilePath(file);
        if(null!=result.get("size")){
            long ss=Long.valueOf(String.valueOf(result.get("size")));
            System.out.println(ss);
        }
        if(null!=result.get("apkSize")){
            String ss=String.valueOf(result.get("apkSize"));
            System.out.println(ss);
        }
    }
}
