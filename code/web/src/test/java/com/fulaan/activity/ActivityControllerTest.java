package com.fulaan.activity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.activity.Activity;
import com.pojo.activity.ActivityView;
import com.pojo.activity.enums.ActStatus;
import com.pojo.activity.enums.ActVisibility;
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
public class ActivityControllerTest {
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
    * pc端发起活动
    * */
    @Test
    public void promote() throws Exception {
        ActivityView activityView=new ActivityView();
        activityView.setGuestIds(new ObjectId().toString());
        activityView.setMessage("hello I am jerry ");
        Activity activity=new Activity();
        activity.setAttendCount(100);
        activity.setCoverImage("/dsaasd/das");
        activity.setCreateDate(new Date());
        activity.setDescription(" i do not know");
        activity.setDiscuss(21);
        activity.setEventEndDate(new Date());
        activity.setEventStartDate(new Date());
        activity.setImage(13);
        activity.setIsFriend(true);
        activity.setLocation("金沙江路");
        activity.setMemberCount(300);
        activity.setName("一起开黑");
        activity.setOrganizer(new ObjectId().toString());
        activity.setOrganizerImageUrl("/adsa/dasdasas");
        activity.setOrganizerName("乔恩");
        activity.setOrganizerRole(0);
        activity.setStatus(ActStatus.ACTIVE);
        activity.setVisible(ActVisibility.PUBLIC);
        activityView.setAct(activity);
        String json=JSON.toJSONString(activityView);
        mockMvc.perform(post("/activity/promote.do").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(json)).
                andExpect(status().isOk()).andReturn();
    }

    @Test
    public void myOrganizedActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/myOrganizedActivity.do").param("pageSize", "10").param("page", "1"))
                .andExpect(status().isOk()).andReturn();
        String str=mvcResult.getResponse().getContentAsString();
        System.out.println(str);
    }
    @Test
    public void launchActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/launch.do"))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    public void activityMain() throws Exception {
        mockMvc.perform(get("/activity/activityMain.do")).andExpect(status().isOk());
    }
    @Test
    public void invitationCount() throws Exception {
        MvcResult mvcResult= mockMvc.perform(get("/activity/invitationCount.do")).andExpect(status().isOk()).andReturn();
        String str=mvcResult.getResponse().getContentAsString();
        System.out.println(str);
    }
    @Test
    public void selectHotActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/selectHotActivity.do").param("page","-1").param("pageSize","10"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void actTrackList() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/actTrackList.do").param("page","-1").param("pageSize","10"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

    }
    @Test
    public void friendsActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/friendsActivity.do").param("page","-1").param("pageSize","10"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void recommendActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/recommendActivity.do").param("page","-1").param("pageSize","10"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void myAttendActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/myAttendActivity.do").param("page","-1").param("pageSize","10"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void activityDetail() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/activityDetail.do").param("activityId","550fa0d2d3278e23a3243268"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void usersInActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/usersInActivity.do").
                param("activityId", "550fa0d2d3278e23a3243268").
                param("page", "-1").
                param("pageSize", "10"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void myFriends() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/myFriends.do")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void inviteOneFriend() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/inviteOneFriend.do").
                param("activityId","5507f213e05fbb8bdc17cd88").
                param("msg","c大叔大婶").
                param("friendId","5507dd60e05f620c85dbdc47")
        ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void actInvitation() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/actInvitation.do").
                        param("activityId","5507f213e05fbb8bdc17cd88").
                        param("userId","5507f213e05fbb8bdc17cd88")
        ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void joinActivity() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/joinActivity.do").
                        param("activityId","5507f213e05fbb8bdc17cd88").
                        param("userId","5507f213e05fbb8bdc17cd88").
                        param("fromDevice", "1")
        ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    /*
    *
    * 上传文件 TODO
    *
    * */
    @Test
    public void uploadPic() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/uploadPic.do").
                        param("activityId","5507f213e05fbb8bdc17cd88")
        ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void addDiscuss() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/adddiscuss.do").
                        param("actId","54fe875fe05ff94c544ae70a").param("content","21212121")
        ).andExpect(status().isOk()).andReturn();
    }
    @Test
    public void mobile$adddiscuss() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/mobile/adddiscuss.do").
                        param("actId","54fe875fe05ff94c544ae70a").param("content","21212121")
        ).andExpect(status().isOk()).andReturn();
    }
    @Test
    public void addSubdiscuss() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/addSubdiscuss.do").
                        param("actId","54fe875fe05ff94c544ae70a").param("content","21212121").param("repId","54fe875fe05ff94c544ae70a")
        ).andExpect(status().isOk()).andReturn();
    }
    @Test
    public void invite() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/invite.do").
                        param("actId","54fe875fe05ff94c544ae70a").param("guestIds","54fe875fe05ff94c544ae70a,54fe875fe05ff94c544ae70a,54fe875fe05ff94c544ae70a")
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void view11() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/view.do").param("actId","54fe875fe05ff94c544ae70a")).andExpect(view().name("active/view")).andReturn();
    }
    @Test
    public void cancel() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/cancel.do").param("actId","54fe875fe05ff94c544ae70a")).andExpect(status().isOk()).andReturn();
    }

    @Test
    public  void attend() throws Exception {
        MvcResult mvcResult=mockMvc.perform((get("/activity/attend.do").
                        param("actId", "54fe875fe05ff94c544ae70a").param("fromDevice","0"))
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void quit() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/quit.do").
                        param("actId","550fe142d3277347564c4ccc")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void accept() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/accept.do").
                        param("id","550fe142d3277347564c4ccc").param("fromDevice","0")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void reject() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/reject.do").
                        param("id","550fe142d3277347564c4ccc")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void hesitate() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/hesitate.do").
                        param("id","550fe142d3277347564c4ccc")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void recentActTracks() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/recentActTracks.do").
                        param("page","1").
                        param("pageSize","10")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void hot() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/hot.do").
                        param("topN","10")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void deleteInvitation() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/deleteInvitation.do").
                        param("actInvitationId","5507dd60e05f620c85dbdc47")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void activityDiscuss(){

    }
    @Test
    public void activityPicture() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/activity/activityPicture.do").
                        param("activityId","5507f213e05fbb8bdc17cd88").
                        param("page", "1").
                        param("pageSize", "10")
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

    }
    @Test
    public void friendSearch() throws Exception {
        mockMvc.perform(get("/activity/friendSearch.do")
        ).andExpect(status().isOk()).andExpect(view().name("active/friend-search"));
    }
}
