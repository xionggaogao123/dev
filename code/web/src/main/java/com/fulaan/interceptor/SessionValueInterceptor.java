package com.fulaan.interceptor;


import com.db.user.UserActiveRecordDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.cache.CacheHandler;
import com.pojo.app.SessionValue;
import com.pojo.user.UserActiveRecordEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.UnLoginException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.fulaan.base.BaseController.SESSION_VALUE;


public class SessionValueInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(SessionValueInterceptor.class);

    private static final String sso_homepage = "/user/homepage.do?type=sso";

    private static UserActiveRecordDao userActiveRecordDao = new UserActiveRecordDao();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object arg2) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept");
        if(arg2 instanceof  HandlerMethod) {
            HandlerMethod method = (HandlerMethod) arg2;
            SessionNeedless s = method.getMethodAnnotation(SessionNeedless.class);
            String ui = getCookieUserKeyValue(request);
            if (null != s) {
                if (StringUtils.isNotBlank(ui)) {
                    SessionValue sv = CacheHandler.getSessionValue(ui);
                    if (null != sv && !sv.isEmpty()) {
                        request.setAttribute(SESSION_VALUE, sv);
                        String userId=sv.getId();
                        if(StringUtils.isNotEmpty(userId)){
                            UserActiveRecordEntry
                                    recordEntry=userActiveRecordDao.getEntryByUserId(new ObjectId(userId));
                            if(null!=recordEntry){
                                userActiveRecordDao.updateActiveTime(new ObjectId(userId),System.currentTimeMillis());
                            }else{
                                UserActiveRecordEntry entry=new UserActiveRecordEntry(new ObjectId(userId),System.currentTimeMillis());
                                userActiveRecordDao.saveEntry(entry);
                            }
                        }
                    }
                }
                return true;
            } else {
                if (StringUtils.isNotBlank(ui)) {
                    SessionValue sv = CacheHandler.getSessionValue(ui);
                    if (null != sv && !sv.isEmpty()) {
                        request.setAttribute(SESSION_VALUE, sv);
                        String userId=sv.getId();
                        if(StringUtils.isNotEmpty(userId)) {
                            UserActiveRecordEntry
                                    recordEntry = userActiveRecordDao.getEntryByUserId(new ObjectId(userId));
                            if (null != recordEntry) {
                                userActiveRecordDao.updateActiveTime(new ObjectId(userId), System.currentTimeMillis());
                            } else {
                                UserActiveRecordEntry entry = new UserActiveRecordEntry(new ObjectId(userId), System.currentTimeMillis());
                                userActiveRecordDao.saveEntry(entry);
                            }
                        }
                        return true;
                    }
                }
                String requestURL = getFullURL(request);

                if (StringUtils.isNotBlank(requestURL) && requestURL.equalsIgnoreCase(sso_homepage)) {
                    response.sendRedirect("/user/sso/redirect.do");
                } else {
                    logger.info(method);
                    throw new UnLoginException();
                }
                return false;
            }
        }else if(arg2 instanceof WebSocketHttpRequestHandler){
            return true;
        }else {
            return true;
        }
    }

    private String getCookieUserKeyValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constant.COOKIE_USER_KEY)) {
                    return cookie.getValue();
                }
            }
        }
        return Constant.EMPTY;
    }

    private String getFullURL(HttpServletRequest arg0) {
        String path = arg0.getRequestURI();
        String queryString = arg0.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            return path + "?" + queryString;
        }
        return path;
    }

}
