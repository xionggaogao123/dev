package com.fulaan.base;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jerry on 2016/12/19.
 * 基础 服务类 复用代码
 */
public class BaseService {

    /**
     * 得到ip地址
     *
     * @return ip
     */
    protected String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据距离得到 大于 等于 小于 关系
     * @param distance
     * @return
     */
    protected String filterDistance(long distance) {
        if(distance <= 500) {
            return "≤500m";
        } else if(distance <= 1000) {
            return "≤1km";
        } else if(distance <= 2000) {
            return "≤2km";
        } else if(distance <= 5000) {
            return "≤5km";
        }
        return ">5km";
    }

}
