package com.db.activity;

import com.pojo.activity.ActTrackEntry;
import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActTrackType;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2015/3/12.
 */
public class ActivityTrackDaoTest {

    private ActivityTrackDao activityTrackDao=new ActivityTrackDao();

    @Test
    public void  findActTrack(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        objectIdList.add(new ObjectId("5501413ce05f78d5872aaac0"));

        List<ActTrackEntry> actTrackEntryList=activityTrackDao.findActTrack(objectIdList, 0, 100);

    }


    @Test
    public void  insertActTrack(){
        ActTrackEntry actTrackEntry=new ActTrackEntry();
        actTrackEntry.setActTrackDevice(ActTrackDevice.FromAndroid);
        actTrackEntry.setActTrackType(ActTrackType.ATTEND);
        actTrackEntry.setCreateTime(System.currentTimeMillis());
        actTrackEntry.setRelatedId(new ObjectId("54fe685ae05f50618b1b1456"));
        actTrackEntry.setUserId(new ObjectId("54fe685ae05f50618b1b1456"));
        activityTrackDao.insertActTrack(actTrackEntry);
    }

    @Test
    public void  findActTrackCount(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        objectIdList.add(new ObjectId("5501413ce05f78d5872aaac0"));
        int count=activityTrackDao.findActTrackCount(objectIdList);
    }
}
