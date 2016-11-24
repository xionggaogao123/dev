package com.fulaan.calendar.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.sys.utils.RespObj;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:D:\\k6kt_git\\code\\web\\src\\main\\webapp\\WEB-INF\\spring-servlet.xml")
public class CalendarControllerTest {

	 private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;
    
    
    
    
//	public RespObj addEvent( String type, String title,
//			String content, String beginTime, String endTime, String lp,
//			String dv, String edt, String edv, String operId)
//			
			
			
    @Test
    public void add() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/calendar/add.do")
        		.param("type","2")
        		.param("title","神龙大侠")
        		.param("content", "dd")
        		.param("beginTime", "1432281960000")
        		.param("endTime", "1432285560000")
        		.param("lp", "1")
        		.param("dv", "0")
        		.param("edt", "0")
        		.param("edv", "")
        		.param("operId", "")
        		
        		
        		)
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
