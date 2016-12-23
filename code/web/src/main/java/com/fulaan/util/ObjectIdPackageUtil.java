package com.fulaan.util;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by admin on 2016/12/23.
 */
public class ObjectIdPackageUtil {
    public static void main(String[] args){
        ObjectId id=new ObjectId("579961cbde04cb783df3074f");
        System.out.println(id.toString());
        System.out.println(getPackage(id));
        System.out.println(removePackage("1469669835"));
    }

    public static String getPackage(ObjectId id){
        System.out.println(id.getTime());
        return String.valueOf(id.getTime()/1000L);
    }

    public static ObjectId removePackage(String id){
        long dateTime=Long.parseLong(id);
        long time=dateTime*1000L;
        System.out.println(time);
        return new ObjectId(new Date(time));
    }
}
