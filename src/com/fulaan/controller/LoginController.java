package com.fulaan.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.common.CommonResult;
import com.fulaan.common.ProjectContent;
import com.fulaan.dao.base.BaseDao;
import com.fulaan.entity.LoginInfo;
import com.fulaan.entity.Staff;
import com.fulaan.service.LoginInfoService;
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
	
	@Resource
	LoginInfoService loginInfoService;
	
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
		
		List<Staff> staffList = (List<Staff>) loginInfoService.find(sql, new String[]{loginName, encryPwd});
		if(staffList == null || staffList.size() <= 0) {
			request.setAttribute("errorMsg", "用户名或密码错误");
			return USER_LOGIN_PAGE;
		}
		
		Staff staff = staffList.get(0);
		if(staff.getIsDeleted() != null && 
				staff.getIsDeleted() == ProjectContent.DELETED_FLAG) {
			request.setAttribute("errorMsg", "该用户已被禁用");
			return USER_LOGIN_PAGE;
		}
		
		session.setAttribute(ProjectContent.LOGIN_USER_IN_SESSION, staff);
		
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setStaff(staff);
		loginInfo.setLoginTime(new Date());
		loginInfoService.save(loginInfo);
		
		return "redirect:/project/list";
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
	
	/**
	 * 修改用户密码
	 * @param request
	 * @param session
	 * @param oldPw 旧密码
	 * @param newPw 新密码
	 * @param renewPw 输入第二次新密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changePw", method = RequestMethod.POST)
	public CommonResult changPw(HttpServletRequest request,
			HttpSession session,
			@RequestParam String oldPw,
			@RequestParam String newPw,
			@RequestParam String renewPw) {
		
		CommonResult result = null;
		
		Staff loginedStaff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		
		if(!newPw.equals(renewPw)) {
			result = new CommonResult(1, "error", "两次输入的密码不一致");
			return result;
		}
		
		String encryPw = loginedStaff.getPassword();
		String encryOldPw = MD5Util.MD5Encode(oldPw);
		
		if(!encryPw.equals(encryOldPw)) {
			result = new CommonResult(1, "error", "原密码不正确");
			return result;
		}
		
		String encryNewPw = MD5Util.MD5Encode(newPw);
		loginedStaff.setPassword(encryNewPw);
		loginInfoService.update(loginedStaff);
		
		result = new CommonResult(0, "success", "修改密码成功");
		
		return result;
	}
	
}
