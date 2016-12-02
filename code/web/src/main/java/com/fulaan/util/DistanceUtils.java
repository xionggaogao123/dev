package com.fulaan.util;

/**
 * Created by moslpc on 2016/12/2.
 */
public class DistanceUtils {

    /**
     * 计算地图上两点之间的距离(单位千米)
     *
     * @return
     */
    public static Double distance(double longitude, double latitude, double long2,
                                  double lat2) {
        double a, b, R;
        R = 6371; // 地球半径
        latitude = latitude * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = latitude - lat2;
        b = (longitude - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(
                Math.sqrt(sa2 * sa2 + Math.cos(latitude)
                        * Math.cos(lat2) * sb2 * sb2));
        return d * 1000;
    }
}
