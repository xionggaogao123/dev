package com.db.activity;

import com.pojo.activity.ActivityDiscuss;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2015/3/12.
 */
public class ActivityDiscussDTODaoTest {
    private  ActivityDiscussDao activityDiscussDao=new ActivityDiscussDao();
    @Test
    public void insertDiscuss(){
        ActivityDiscuss activityDiscuss =new ActivityDiscuss();
        List<String>stringList=new ArrayList<String>();
        stringList.add(new ObjectId().toString());
        activityDiscuss.setContent("asdadsa");
        activityDiscuss.setDate(System.currentTimeMillis());
        activityDiscuss.setImageList(stringList);
        activityDiscuss.setRepId(new ObjectId("54fe875fe05ff94c544ae70a"));
        activityDiscuss.setUserId(new ObjectId("54fe875fe05ff94c544ae70a"));
        activityDiscussDao.insertDiscuss(new ObjectId("54fe875fe05ff94c544ae70a"), activityDiscuss);

    }
    @Test
    public void findDiscussByActId(){
        List<ActivityDiscuss> activityDiscussList =activityDiscussDao.findDiscussByActId(new ObjectId("54fe875fe05ff94c544ae70a"));
    }
}
