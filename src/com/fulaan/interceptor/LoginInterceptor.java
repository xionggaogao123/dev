package com.fulaan.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.common.ProjectContent;
import com.fulaan.entity.Staff;

public class LoginInterceptor implements HandlerInterceptor{

	/**
	 * 拦截前处理
	 * */
	@Override
	public boolean preHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse,
			Object paramObject) throws Exception {
		System.out.println("处理拦截......");
		HttpSession session = paramHttpServletRequest.getSession();
		Staff staff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		if(staff != null) {
			return true;
		}
		String context = paramHttpServletRequest.getContextPath();
		System.out.println(context);
		paramHttpServletResponse.sendRedirect(context + "/user/login");
		
		return false;
	}

	/**
	 * 拦截后处理
	 * */
	@Override
	public void postHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse,
			Object paramObject, ModelAndView paramModelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, Object paramObject, Exception paramException)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
