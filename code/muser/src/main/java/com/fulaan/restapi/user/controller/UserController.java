package com.fulaan.restapi.user.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.sys.utils.RespObj;

/**
 * user micro service rest api
 * @author fourer
 *
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	/**
	 * 用户登录
	 * @param loginName
	 * @param passWord
	 * @return
	 */
	@SessionNeedless
	@RequestMapping(value = "/login/{loginName}/{passWord}",method= RequestMethod.GET)
	public RespObj login(@PathVariable String loginName,@PathVariable String passWord)
	{
		System.out.println(loginName+","+passWord);
		return RespObj.SUCCESS;
	}
}
