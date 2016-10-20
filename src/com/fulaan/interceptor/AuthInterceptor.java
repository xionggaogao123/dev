package com.fulaan.interceptor;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.fulaan.auth.annotation.Authority;
import com.fulaan.auth.annotation.SuperAdmin;
import com.fulaan.common.CommonResult;
import com.fulaan.common.ProjectContent;
import com.fulaan.entity.AuthFunction;
import com.fulaan.entity.AuthRole;
import com.fulaan.entity.Staff;
import com.fulaan.service.AuthRoleService;

/**
 * @author xusy
 * 权限拦截器
 */
public class AuthInterceptor extends HandlerInterceptorAdapter{

	@Resource
	AuthRoleService authRoleService;
	
	@Override
	public boolean preHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse,
			Object paramObject) throws Exception {
		
		if(paramObject instanceof HandlerMethod) {
			
			HandlerMethod method = (HandlerMethod) paramObject;
			Authority auth = method.getMethodAnnotation(Authority.class); // 获取自定义权限注解
			ResponseBody responseBodyAnno = method.getMethodAnnotation(ResponseBody.class); // 获取ResponseBody注解
			SuperAdmin superAdminAnnp = method.getMethodAnnotation(SuperAdmin.class); // superAdmin用户特殊处理
			
			HttpSession session = paramHttpServletRequest.getSession();
			Staff loginedUser = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
			
			if(auth != null) { // 权限控制
				String authModuel = auth.module().toString();
				String authFunction = auth.function().toString();
				String functionFlag = authModuel + "_" + authFunction;
				
				String roleType = loginedUser.getIsPrjOwner();
				if(roleType == null || "".equals(roleType)) {
					roleType = "0"; // 默认user角色
				}
				int roleId = 0;
				switch (roleType) {
				case "0": // user角色
					roleId = 3;
					break;
				case "1": // admin角色
					roleId = 2;
					break;
				case "2": // superAdmin角色
					roleId = 1;
					break;
				default:
					break;
				}
				
				AuthRole role = authRoleService.get(AuthRole.class, roleId);
				if(role == null) {
					throw new Exception("No This Role");
				}
				
				if(roleId == 1 && superAdminAnnp != null) { // 设置superAdmin标志，在具体的业务代码中处理
					session.setAttribute(ProjectContent.SUPERADMIN_FLAG, true);
				} else {
					session.removeAttribute(ProjectContent.SUPERADMIN_FLAG);
				}
				
				List<AuthFunction> functionList = role.getFunctionList();
				for(AuthFunction af : functionList) {
					if(af.getFunctionFlag().equals(functionFlag)) { // 具有该权限
						return true;
					}
				}
				
				if(responseBodyAnno != null) { // ajax请求，返回json信息
					CommonResult result = new CommonResult(1, "error", "没有相关执行权限");
					paramHttpServletResponse.setCharacterEncoding("UTF-8");
					paramHttpServletResponse.setContentType("application/json; charset=utf-8");
					paramHttpServletResponse.getWriter().write(JSON.toJSONString(result));
				} else { // 非ajax请求
					paramHttpServletResponse.setCharacterEncoding("UTF-8");
					paramHttpServletResponse.getWriter().write("<div style='text-align:center'><font color='red' style='font-size:30px;text-align:center'>没有相关执行权限</font></div>");
				}
				
				return false;
			}
			
			return true;
			
		} else {
			return super.preHandle(paramHttpServletRequest, paramHttpServletResponse, paramObject);
		}
		
	}

}
