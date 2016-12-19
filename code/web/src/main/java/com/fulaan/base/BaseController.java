package com.fulaan.base;

import com.pojo.app.Platform;
import com.pojo.app.SessionValue;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;


public class BaseController {

    public static final String SESSION_VALUE = "sessionValue";

    protected SessionValue getSessionValue() {
        HttpServletRequest request = getRequest();
        return (SessionValue) request.getAttribute(SESSION_VALUE);
    }

    protected ObjectId getUserId() {
        SessionValue sv = getSessionValue();
        if (null != sv && !sv.isEmpty()) {
            return new ObjectId(sv.getId());
        }
        return null;
    }

    /**
     * 得到ip地址
     *
     * @return ip
     */
    protected String getIP() {
        HttpServletRequest request = getRequest();
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

    protected String getCookieValue(String cookieName) {
        HttpServletRequest request = getRequest();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return Constant.EMPTY;
    }

    /**
     * 获取平台
     *
     * @return Platform
     */
    protected Platform getPlatform() {
        HttpServletRequest request = getRequest();
        String client = request.getHeader("User-Agent");
        Platform pf = Platform.PC;
        if (client.contains("iPhone")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")) {
            pf = Platform.Android;
        } else if (client.contains("iOS")) {
            pf = Platform.IOS;
        }
        return pf;
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    protected HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
    }


}


