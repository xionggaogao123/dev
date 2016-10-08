package com.fulaan.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fulaan.common.ProjectContent;
import com.fulaan.dao.base.BaseDao;
import com.fulaan.entity.Staff;
import com.fulaan.util.MD5Util;

@Controller
@RequestMapping("/user")
public class LoginController {
	
	/**
	 * 用户登录页面
	 */
	public static final String USER_LOGIN_PAGE = "login";
	
	/**
	 * 添加新项目页
	 */
	private static final String PROJECT_ADD_PAGE = "newProject";
	
	@Resource
	BaseDao baseDao;
	
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,
			HttpServletResponse response) {
		return USER_LOGIN_PAGE;
	}
	
	/**
	 * 验证用户登录
	 * @param request
	 * @param response
	 * @param loginName 登录名
	 * @param password  密码
	 * @return
	 */
	@RequestMapping(value = "/toLogin", method = RequestMethod.POST)
	public String toLogin(HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			@RequestParam(defaultValue = "") String loginName,
			@RequestParam(defaultValue = "") String password) {
		
		if((loginName == null || "".equals(loginName.trim()))
				|| (password == null || "".equals(password.trim()))) { // 用户名和密码不能为空
			request.setAttribute("errorMsg", "用户名或密码不能为空");
			return USER_LOGIN_PAGE;
		}
		
		String sql = "from Staff where loginName = ? and password = ?";
		String encryPwd = MD5Util.MD5Encode(password);
		
		List<Staff> staffList = (List<Staff>) baseDao.find(sql, new String[]{loginName, encryPwd});
		if(staffList == null || staffList.size() <= 0) {
			request.setAttribute("errorMsg", "用户名或密码错误");
			return USER_LOGIN_PAGE;
		}
		
		Staff staff = staffList.get(0);
		session.setAttribute(ProjectContent.LOGIN_USER_IN_SESSION, staff);
		
		return PROJECT_ADD_PAGE;
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session) {
		
		session.removeAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		
		return USER_LOGIN_PAGE;
	}
	
}
