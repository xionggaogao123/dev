package com.fulaan.myclass;

import com.alibaba.fastjson.JSON;
import com.fulaan.myclass.controller.UserIdAndScoreView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:E:/k6kt-git3/k6kt/code/web/src/main/webapp/WEB-INF/spring-servlet.xml")
public class MyClassControllerTest {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void myclass4stu() throws Exception {
        mockMvc.perform(get("/myclass/myclass4stu.do"))
                .andExpect(status().isOk())
                .andExpect(view().name(""));
    }

    @Test
    public void stulist() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myclass/stulist.do").param("classId", "5518f143d327a424571c7210"))
                .andExpect(status().isOk())
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void myclass4tea() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myclass/myclass4tea.do").param("classId", "5518f143d327a424571c7210"))
                .andExpect(view().name("")).andReturn();
    }

    @Test
    public void addexp() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myclass/addexp.do").param("classId", "5518f143d327a424571c7210"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    public void saveexp() throws Exception {
        List<UserIdAndScoreView> userIdAndScoreViewList =new ArrayList<UserIdAndScoreView>();
        UserIdAndScoreView userIdAndScoreView =new UserIdAndScoreView();
        userIdAndScoreView.setUserId("5507dd60e05f620c85dbdc47");
        userIdAndScoreView.setScore(2);
        userIdAndScoreViewList.add(userIdAndScoreView);

        String json=JSON.toJSONString(userIdAndScoreViewList);
        mockMvc.perform(get("/myclass/saveexp.do").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(json).param("scoreData",json))
                .andExpect(status().isOk());
    }


    @Test
    public void statstus() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myclass/statstus.do").param("classId", "5518f143d327a424571c7210"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(status().isOk()).andReturn();
    }


    @Test
    public void stastu() throws Exception {
        mockMvc.perform(get("/myclass/stastu.do").param("studentId", "5518f143d327a424571c7210"))
                .andExpect(status().isOk())
                .andExpect(view().name(""));
    }
}
