package com.fulaan.base;

import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.Platform;
import com.pojo.app.SessionValue;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import org.bson.types.ObjectId;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;


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
        Cookie[] cookies = getRequest().getCookies();
        if (null != cookies && cookies.length > 0) {
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
        if (client.contains("iPhone") || client.contains("iOS")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")) {
            pf = Platform.Android;
        }
        return pf;
    }

    protected String getProtectedMobile(final String mobile) {
        if (mobile != null && mobile.length() == 11) {
            String prefix = mobile.substring(0, 3);
            String stufix = mobile.substring(7, 11);
            return prefix + "****" + stufix;
        }
        return mobile;
    }

    protected String uploadAvatarToQiniu(String avatar) throws IOException {
        File savedFile = File.createTempFile(new ObjectId().toString(), ".jpg");
        BufferedImage bufferedImage = ImageIO.read(new URL(avatar));
        ImageIO.write(bufferedImage, "JPG", savedFile);
        ObjectId key = new ObjectId();
        try {
            QiniuFileUtils.uploadFile(key.toString(), new FileInputStream(savedFile), QiniuFileUtils.TYPE_IMAGE);
        } catch (IllegalParamException e) {
            e.printStackTrace();
            return avatar;
        }
        return key.toString();
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


