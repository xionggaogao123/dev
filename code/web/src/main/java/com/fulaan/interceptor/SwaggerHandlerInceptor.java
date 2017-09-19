package com.fulaan.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by scott on 2017/9/15.
 */
public class SwaggerHandlerInceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object arg2) throws Exception {
        System.out.println(request.getContextPath());
        if(request.getRequestURI().contains("swagger-resources/configuration/ui.do")) {
            response.sendRedirect("/entrance.do");
            return false;
        }else{
            return true;
        }
    }
}
