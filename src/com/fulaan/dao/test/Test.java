package com.fulaan.dao.test;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fulaan.dao.base.BaseDao;
import com.fulaan.entity.Staff;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
	
	@Resource
	BaseDao baseDao;
	
	@org.junit.Test
	public void test() {
		Staff s = baseDao.get(Staff.class, 1);
		System.out.println(s.getJobTitle());
	}
	
	public static void main(String[] args) {
		System.out.println(Staff.class.getSimpleName());
	}
}
