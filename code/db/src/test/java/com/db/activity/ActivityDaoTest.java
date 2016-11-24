package com.db.activity;

import com.pojo.activity.ActivityDiscuss;
import com.pojo.activity.ActivityEntry;
import com.pojo.activity.enums.ActStatus;

import com.pojo.activity.enums.ActVisibility;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2015/3/10.
 */
public class ActivityDaoTest {

    ActivityDao activityDao=new ActivityDao();


    @Test
    public void selectHotActivity() {
        List<ActivityEntry> activityEntryList=activityDao.selectHotActivity(new ObjectId("54fe685ae05f50618b1b1456"),0,100);
    }
    
   
    
    
    

    @Test
    public void selectActivityWhereActIdIn() {
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe875fe05ff94c544ae70a"));
        objectIdList.add(new ObjectId("54fe8975e05f00d342933404"));
        List<ActivityEntry> activityEntryList=activityDao.selectActivityWhereActIdIn(objectIdList);

    }
    @Test
    public void selectActivityByUserIds() {
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        List<ActivityEntry> activityEntryList=activityDao.selectActivityByUserIds(objectIdList,0,100);
    }
    @Test
    public void findFriendsActivityCount() {
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        int count=activityDao.findFriendsActivityCount(objectIdList);
    }

    /*
    *
    *和当前用户相关的活动 （相关是指 参加 或者发起）
    * */
    @Test
    public void activityRelation2me() {
        List<ActivityEntry> activityEntryList=activityDao.activityRelation2me(new ObjectId("54fe685ae05f50618b1b1456"));
    }
    @Test
    public void recommendActivityOnlySchool() {
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        List<ActivityEntry> activityEntryList=activityDao.recommendActivityOnlySchool(new ObjectId("54fe685ae05f50618b1b1456"),objectIdList,0,100);
    }
    @Test
    public void recommendActivityOnlySchoolCount(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        int count=activityDao.recommendActivityOnlySchoolCount(new ObjectId("54fe685ae05f50618b1b1456"),objectIdList );

    }
    /*
    * 我发起的活动
    *
    * */
    @Test
    public void myOrganizedActivity() {
       List<ActivityEntry> activityEntryList= activityDao.myOrganizedActivity(new ObjectId("54fe685ae05f50618b1b1456"), 0, 5);
        System.out.println(11);
    }
    /*
    *
    *
    * */

    @Test
    public void myOrganizedActivityCount() {
        int count=activityDao.myOrganizedActivityCount(new ObjectId("54fe685ae05f50618b1b1456"));
        System.out.println("11");
    }

    /*
    * 我参加的活动
    *
    * */

    @Test
    public void myAttendActivity() {
        List<ActivityEntry> activityEntryList=activityDao.myAttendActivity(new ObjectId("54fe685ae05f50618b1b1456"),0,10);
        System.out.println("23");
    }
    @Test
    public void myAttendActivityCount() {
        int count=activityDao.myAttendActivityCount(new ObjectId("54fe685ae05f50618b1b1456"));
        System.out.println("23");
    }
    @Test
    public void userInActivityCount() {
        int count=activityDao.userInActivityCount(new ObjectId("54fe875fe05ff94c544ae70a"));

    }
    @Test
    public void findActivityById() {
        ActivityEntry activityEntry=activityDao.findActivityById(new ObjectId("54fe875fe05ff94c544ae70a"));
        System.out.println("");
    }
    @Test
    public void insertAttend() {
        activityDao.insertAttend(new ObjectId("54fe875fe05ff94c544ae70a"),new ObjectId("54fe875fe05ff94c544ae70a"));

    }
    
    
    @Test
    public void insertActivity() {
        ActivityEntry activityEntry1=new ActivityEntry();
        activityEntry1.setActEndDate(System.currentTimeMillis());
        activityEntry1.setActName("sfdsfddsf");
        activityEntry1.setActStartDate(System.currentTimeMillis());
        activityEntry1.setActStatus(ActStatus.ACTIVE.getState());
        activityEntry1.setActVisibility(ActVisibility.PUBLIC.getState());
        activityEntry1.setAttendCount(2);
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        for(int i=0;i<10;i++){
            objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        }
        activityEntry1.setAttendIds(objectIdList);
        activityEntry1.setCoverImage("/isadas");
        activityEntry1.setCreateDate(System.currentTimeMillis());
        activityEntry1.setDescription("budong");
        activityEntry1.setDiscussCount(1000);
        activityEntry1.setImageCount(800);
        activityEntry1.setLocation("大渡河路");
        activityEntry1.setMemberCount(20);
        activityEntry1.setOrganizerId(new ObjectId("54fe685ae05f50618b1b1456"));
        activityEntry1.setRegionId(new ObjectId("54fe685ae05f50618b1b1456"));
        activityEntry1.setSchoolId(new ObjectId("54fe685ae05f50618b1b1456"));

        
        
        ActivityEntry a =new ActivityEntry();
    	List<ActivityDiscuss> activityDiscussEntries =new ArrayList<ActivityDiscuss>();
    	
    	
    	ActivityDiscuss e =new ActivityDiscuss();
    	activityDiscussEntries.add(e);
    	
    	
    	a.setActDiscusses(activityDiscussEntries);
    	
       activityDao.insertActivity(a);
    }


    @Test
    public void updateDiscussAndImgCount() {
        activityDao.updateDiscussAndImgCount(new ObjectId("54fe875fe05ff94c544ae70a"));
    }
    @Test
    public void updateActivityStatus() {
        activityDao.updateActivityStatus(new ObjectId("54fe875fe05ff94c544ae70a"),ActStatus.CLOSE);
    }
    @Test
    public void quitActivity() {
        activityDao.quitActivity(new ObjectId("54fe875fe05ff94c544ae70a"),
                new ObjectId("54fe685ae05f50618b1b1456"));
    }

    @Test
    public void updateAttendCount(){
        activityDao.updateAttendCount(new ObjectId("54fe875fe05ff94c544ae70a"));
    }
}
