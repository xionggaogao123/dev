package com.fulaan.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
	//@RequestMapping("/success")
	public String returnString() {
		
		Session session = baseDao.getSession();
		Staff s = (Staff) session.get(Staff.class, 1);
		System.out.println(s.getJobTitle());
		s = (Staff) session.get(Staff.class, 1);
		System.out.println(s.getJobTitle());
		
		return "{\"test\":\"success\"}";
	}
	
	public static void main(String[] args) {
		double i = 141d / 174 * 100;
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println(df.format(i));
		System.out.println(new BigDecimal(i * 100).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
		int rankGap = 141;
		int prevGradeRank = 174;
		double advanceRate = rankGap / (double) prevGradeRank * 100;
		System.out.println(advanceRate);
	}
	
}
