package com.fulaan.interceptor;


import com.fulaan.cache.CacheHandler;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.forum.service.FLogService;
import com.pojo.app.SessionValue;
import com.pojo.forum.FLogDTO;
import com.sys.constants.Constant;
import com.sys.exceptions.UnLoginException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.fulaan.controller.BaseController.SESSION_VALUE;


public class SessionValueInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(SessionValueInterceptor.class);

    private static final String sso_homepage = "/user/homepage.do?type=sso";


    @Autowired
    private FLogService fLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object arg2) throws Exception {
        HandlerMethod method = (org.springframework.web.method.HandlerMethod) arg2;
        SessionNeedless s = method.getMethodAnnotation(SessionNeedless.class);
        String ui = getCookieUserKeyValue(request);
        if (null != s) {
            if (StringUtils.isNotBlank(ui)) {
                SessionValue sv = CacheHandler.getSessionValue(ui);
                if (null != sv && !sv.isEmpty()) {
                    request.setAttribute(SESSION_VALUE, sv);
                }
            }
            return true;
        } else {
            if (StringUtils.isNotBlank(ui)) {
                SessionValue sv = CacheHandler.getSessionValue(ui);
                if (null != sv && !sv.isEmpty()) {
                    request.setAttribute(SESSION_VALUE, sv);
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
    }

    /**
     * 日志
     *
     * @param ui
     * @param path
     * @param method
     */
    private void handleLogService(String ui, String path, String method) {

        SessionValue sv = null;
        if (StringUtils.isNotBlank(ui)) {
            sv = CacheHandler.getSessionValue(ui);
        }
        FLogDTO fLogDTO = new FLogDTO();
        fLogDTO.setActionName(method);
        if (null != sv && !sv.isEmpty()) {
            fLogDTO.setPersonId(sv.getId());
        }
        fLogDTO.setPath(path);
        fLogDTO.setTime(System.currentTimeMillis());
        fLogService.addFLog(fLogDTO);
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
