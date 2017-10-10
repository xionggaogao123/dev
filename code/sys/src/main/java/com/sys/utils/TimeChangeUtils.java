package com.sys.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by James on 2017/10/10.
 */
public class TimeChangeUtils {


    //时间转换格式
    public static String getChangeTime(long time){
        //获得当前时间
        long current=System.currentTimeMillis();
        long ctime = current-time;
        if(current < time){
            return "刚刚";
        }else if(ctime < 60000){//1分钟内
            return "刚刚";
        }else if(ctime < 3600000){//1小时内
            return ctime/60000 +"分钟前";
        }else{
            //日期
            String str  = DateTimeUtils.getLongToStrTimeTwo(time);
            Calendar rightNow  =  Calendar.getInstance();
            Calendar rightNow2  =  Calendar.getInstance();
            rightNow.setTime(new Date(current));
            rightNow2.setTime(new Date(time));

            int year = rightNow.get(Calendar.YEAR);
            int month = rightNow.get(Calendar.MONTH)+1; //第一个月从0开始，所以得到月份＋1
            int day = rightNow.get(rightNow.DAY_OF_MONTH);
            int hour24 = rightNow.get(rightNow.HOUR_OF_DAY);

            int year2 = rightNow2.get(Calendar.YEAR);
            int month2 = rightNow2.get(Calendar.MONTH)+1; //第一个月从0开始，所以得到月份＋1
            int day2 = rightNow2.get(rightNow.DAY_OF_MONTH);
            int hour242 = rightNow2.get(rightNow.HOUR_OF_DAY);
            if(year != year2){
                return str;
            }else if(month != month2){
                return str;
            }else if(day != day2){
                int cday = day - day2;
                if(cday == 1){
                    return "昨天";
                }else if(cday ==2){
                    return "前天";
                }else{
                    return cday + "日前";
                }
            }else{
                int chour = hour24 - hour242;
                return chour + "小时前";
            }

        }
    }

    //时间转换格式
    public static String getChangeStringTime(String time2){
        long time = DateTimeUtils.getStrToLongTime(time2);
        //获得当前时间
        long current=System.currentTimeMillis();
        long ctime = current-time;
        if(current < time){
            return "刚刚";
        }else if(ctime < 60000){//1分钟内
            return "刚刚";
        }else if(ctime < 3600000){//1小时内
            return ctime/60000 +"分钟前";
        }else{
            //日期
            String str  = DateTimeUtils.getLongToStrTimeTwo(time);
            Calendar rightNow  =  Calendar.getInstance();
            Calendar rightNow2  =  Calendar.getInstance();
            rightNow.setTime(new Date(current));
            rightNow2.setTime(new Date(time));

            int year = rightNow.get(Calendar.YEAR);
            int month = rightNow.get(Calendar.MONTH)+1; //第一个月从0开始，所以得到月份＋1
            int day = rightNow.get(rightNow.DAY_OF_MONTH);
            int hour24 = rightNow.get(rightNow.HOUR_OF_DAY);

            int year2 = rightNow2.get(Calendar.YEAR);
            int month2 = rightNow2.get(Calendar.MONTH)+1; //第一个月从0开始，所以得到月份＋1
            int day2 = rightNow2.get(rightNow.DAY_OF_MONTH);
            int hour242 = rightNow2.get(rightNow.HOUR_OF_DAY);
            if(year != year2){
                return str;
            }else if(month != month2){
                return str;
            }else if(day != day2){
                int cday = day - day2;
                if(cday == 1){
                    return "昨天";
                }else if(cday ==2){
                    return "前天";
                }else{
                    return cday + "日前";
                }
            }else{
                int chour = hour24 - hour242;
                return chour + "小时前";
            }

        }
    }
    public static void main(String[] args){
        String str = getChangeTime(1507403543663l);
        System.out.println(str);
    }
}
