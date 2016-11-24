package com.fulaan.school;

import com.alibaba.fastjson.JSON;
import com.db.school.SchoolDao;
import com.pojo.activity.Activity;
import com.pojo.activity.ActivityView;
import com.pojo.activity.enums.ActStatus;
import com.pojo.activity.enums.ActVisibility;
import com.pojo.news.News;
import com.pojo.school.SchoolEntry;
import org.bson.types.ObjectId;
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

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:E:/k6kt-git3/k6kt/code/web/src/main/webapp/WEB-INF/spring-servlet.xml")
public class SchoolControllerTest {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void simple() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));
    }
    @Test
    public void newActivity4MobileTest() throws Exception {
        /*
        *移动端发起活动
        *
        * */
        mockMvc.perform(post("/activity/promoteActivity.do").
                        param("eventStartDate", "2014-12-23 2012:12:12").
                        param("eventEndDate","2014-12-23 2012:12:12").
                        param("location","").
                        param("description","娱乐活动").
                        param("visible","0").
                        param("memberCount", "20").
                        param("coverImage", "asdasd").
                        param("name", "dadas").
                        param("fromDevice","1")
        ).andExpect(status().isOk());
    }


    /*
    *
    * 我的同事页面
    *
    * */
    @Test
    public void mycoll() throws Exception {
        mockMvc.perform(get("/myschool/mycoll.do")).andExpect(view().name("")).andExpect(status().isOk());
    }
    @Test
    public void selteacher() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myschool/selteacher.do").
                param("gradeId","54fe875fe05ff94c544ae70a").
                param("subjectId","54fe875fe05ff94c544ae70a")).andExpect(status().isOk()).andReturn();
        String str=mvcResult.getResponse().getContentAsString();
        System.out.println(str);

    }

    @Test
    public void managesubject() throws Exception {
        mockMvc.perform(get("/myschool/managesubject.do")).andExpect(view().name("")).andExpect(status().isOk());
    }

    @Test
    public void sublist() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myschool/sublist.do")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void gradelist() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myschool/gradelist.do")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void upsub() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myschool/upsub.do")
                .param("subjectId","5514f3e3d327b5214226b868").param("newSubjectName","science")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void addsub() throws Exception {
        String objectIds=new ObjectId()+","+new ObjectId()+","+new ObjectId();
        MvcResult mvcResult=mockMvc.perform(get("/myschool/addsub.do").param("subjectName", "nature").param("gradeArray",objectIds)).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void delsub() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/myschool/delsub.do")
                .param("subjectId","5514f42bd32707876db3d7d1")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void manageteacher() throws Exception {
        mockMvc.perform(get("/myschool/manageteacher.do")).andExpect(view().name("/"));
    }

    @Test
    public void addteacher() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addteacher.do").
                param("teacherName","teacher chen").
                param("jobNum","0008").
                param("permission", "1")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void upteach() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upteach.do").
                param("teacherName","teacher hao").
                param("teacherId", "5514fddad327ae3d3462c26e").
                param("jobNum","007").
                param("permission", "2").param("isManage","0")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void delteach() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/delteach.do").
                param("teacherId","551500d0d327518429de1695")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



    @Test
    public void teacherlist() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/teacherlist.do").
                param("keyWord","teac").param("page","1").param("pageSize","10")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void initpwd() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/initpwd.do")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void mangeschool() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/mangeschool.do")).andExpect(view().name("/")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void upschool() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upschool.do").
                        param("schoolId","5507e3e1e05f571eda874e9d").
                        param("schoolName","chechedan").
                        param("schoolLevel","1,1,0")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void upnewinitpwd() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upnewinitpwd.do").param("newPwd","119")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void addgrade() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addgrade.do").
                param("gradename","119").
                param("currentgradeid","3").
                param("teacherid",new ObjectId().toString())).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void upgrade() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upgrade.do").
                param("gradeid","55150f63d3273391b7523130").
                param("gradename","110").
                param("currentgradeid","6").
                param("teacherid",new ObjectId().toString())).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



    @Test
    public void delgrade() throws Exception {
//        SchoolDao schoolDao=new SchoolDao();
//        SchoolEntry schoolEntry=new SchoolEntry(2,"fulan ",new ObjectId(),"112233");
//        schoolEntry.setID(new ObjectId("5507e3e1e05f571eda874e9d"));
//        schoolDao.addSchoolEntry(schoolEntry);

        MvcResult mvcResult =mockMvc.perform(get("/myschool/delgrade.do").
                param("gradeid","55151dc4d327a1fceb6e53ba")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void addclass() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addclass.do").
                param("classname","chgedan").param("teacherid","55151dced327b7f1b5223960").param("gradeid","55151dced327b7f1b5223960")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void delclassinfo() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/delclassinfo.do").param("classid","55152cf7d327f5de5611e52f")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void upclass() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upclass.do").
                param("classid","55152cf7d327f5de5611e52f").
                param("classname", "chedanppoo").
                param("teacherid", "55152cf7d327f5de5611e52f")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void classlist() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/classlist.do").
                param("gradeid","55151dced327b7f1b5223960")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testulist() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/testulist.do").
                param("classid","55152cf7d327f5de5611e52f")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void addtesub() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addtesub.do").
                param("classid","55152cf7d327f5de5611e52f").
                param("teacherid",new ObjectId().toString()).
                param("subjectid",new ObjectId().toString())).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void deltesub() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/deltesub.do").
                param("tclId","55153281d327f82db991f868")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



    @Test
    public void uptesub() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/uptesub.do").
                param("tclid","5515333ad327a8bdb993a5e2").
                param("teacherid", "55153281d327f82db991f868").
                param("subjectid","55153281d327f82db991f868")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }





    @Test
    public void checkstunm() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/checkstunm.do").
                param("userName","5515333ad327a8bdb993a5e2").
                param("number", "55153281d327f82db991f868")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }




    @Test
    public void addstu() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addstu.do").
                param("classid", "55152cf7d327f5de5611e52f").
                param("stuusername", "sadadaslll").
                param("stnickname", "aoiiqoi").
                param("sex","1").
                param("ptusername", "juipolih")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



    @Test
    public void delstu() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/delstu.do").
                param("classid", "55152cf7d327f5de5611e52f").
                param("studentid", "5515382cd3279ec35b29c5c7")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



    @Test
    public void upstu() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upstu.do").
                param("stnickname", "dasdasdasdasdas").
                param("sex", "1").
                param("studentid", "5515384cd327a15e666a31a1")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void resetpwd() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/resetpwd.do").
                param("studentid", "5515384cd327a15e666a31a0")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void changestu() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/changestu.do").
                param("oldClassId", "55152cf7d327f5de5611e52f").
                param("newClassId", "551539f2d32745854fab405d").
                param("studentId", "5515384cd327a15e666a31a0")).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void managenews() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/managenews.do")).andExpect(view().name("/")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void addnews() throws Exception {
        News news=new News();
        news.setContent("asdassssssssssssssssssssssssssssssssssssssssssssssss");
        news.setTitle("so do i");
        news.setUserId(new ObjectId().toString());
        String json=JSON.toJSONString(news);
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addnews.do").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(json)).
                andExpect(view().name("/")).andReturn();
    }


    @Test
    public void delnews() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/delnews.do").param("newsId","55153cf6d32765d66c915dc0")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



    @Test
    public void upnewsp() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upnewsp.do")).
                andExpect(view().name("/")).andReturn();
    }


    @Test
    public void upnews() throws Exception {
        News news=new News();
        news.setId("55153cdad32736a02aa8bd2c");
        news.setContent("hhhhhhhhhhhhhhhhhhhhhhh");
        news.setTitle("do i so");
        news.setUserId(new ObjectId().toString());
        String json=JSON.toJSONString(news);
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upnews.do").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(json)).
                andExpect(status().isOk()).andReturn();
    }

    @Test
    public void newslist() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/newslist.do").param("page","1").param("pageSize","10")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

//    =========================================================扩展克管理==========================


    @Test
    public void addexpand() throws Exception {
        String str="";
        for(int i=0;i< 10;i++){
            str+=","+new ObjectId().toString();
        }
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addexpand.do").
                param("interestClassName", "我不知道").
                param("teacherid", "55153cdad32736a02aa8bd2c").
                param("studentCount", "10").
                param("classtime", "11,21,32").
                param("subjectid", "55153cdad32736a02aa8bd2c").
                param("opentime", "2015/3/12+23:23").
                param("closetime", "2015/3/12+23:23").
                param("coursetype", "1").
                param("firstteam", "1").
                param("secondteam", "1").
                param("gradeArry", str.substring(1))).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void delexpand() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/delexpand.do").
                param("classid", "551ca99ed327fc8838b9035c")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void upexpand() throws Exception {
        String str="";
        for(int i=0;i< 10;i++){
            str+=","+new ObjectId().toString();
        }
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upexpand.do").
                param("interestClassName", "我hhhh知道").
                param("teacherid", "55153cdad32736a02aa8bd2c").
                param("classid", "551ca966d327d5f518065aa5").
                param("studentCount", "10").
                param("classtime", "11,21,32").
                param("subjectid", "55153cdad32736a02aa8bd2c").
                param("opentime", "2015/3/12+23:23").
                param("closetime", "2015/3/12+23:23").
                param("coursetype", "1").
                param("firstteam", "1").
                param("secondteam", "1").
                param("gradeArry", str.substring(1))).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void upexpstat() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/upexpstat.do").
                param("classid", "551ca966d327d5f518065aa5")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void allstu() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/allstu.do").
                param("classid", "551ca966d327d5f518065aa5")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void addstu2expand() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/addstu2expand.do").
                param("expandClassId", "551ca966d327d5f518065aa5").param("studentId",new ObjectId().toString())).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void delstu4expand() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/delstu4expand.do").
                param("expandClassId", "551ca966d327d5f518065aa5").param("studentId","551cac97d32704846b0d03a5")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



    @Test
    public void expandlist() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/expandlist.do")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void newTerm() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/myschool/newterm.do")).
                andExpect(status().isOk()).andReturn();
    }


}
