package com.fulaan.controller;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.dao.base.BaseDao;
import com.fulaan.entity.Staff;

@Controller
@RequestMapping("/test")
public class TestController {

	@Resource
	BaseDao baseDao;
	
	@ResponseBody
	@RequestMapping("/success")
	public String returnString() {
		
		Session session = baseDao.getSession();
		Staff s = (Staff) session.get(Staff.class, 1);
		System.out.println(s.getJobTitle());
		s = (Staff) session.get(Staff.class, 1);
		System.out.println(s.getJobTitle());
		
		return "{\"test\":\"success\"}";
	}
	
}
