package com.db.activity;

import com.pojo.user.UserEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2015/3/12.
 */
public class FriendDaoTest {

    private FriendDao friendDao=new FriendDao();

    @Test
    public void  countFriend(){
        int count=friendDao.countFriend(new ObjectId("55015a9ce05f36b1274c2687"));
    }
    @Test
    public void  findMyFriendIds(){
        List<ObjectId> objectIdList=friendDao.findMyFriendIds(new ObjectId("55015a9ce05f36b1274c2687"));
    }
    @Test
    public void  isFriend(){
        boolean k=friendDao.isFriend(new ObjectId("55015a9ce05f36b1274c2687"), new ObjectId("550162b8e05f5e86f7b9eedd"));
    }
    @Test
    public void  recommendFriendBySchool(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("550162b8e05f5e86f7b9eedd"));
        List<UserEntry> friendEntries=friendDao.recommendFriendBySchool(new ObjectId("55015a9ce05f36b1274c2687"),objectIdList,0,100);
    }
    @Test
    public void  recommendFriendBySchoolCount(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("550162b8e05f5e86f7b9eedd"));
        int count=friendDao.recommendFriendBySchoolCount(new ObjectId("55015a9ce05f36b1274c2687"),objectIdList);
    }
    @Test
    public void  deleteOneFriend(){
        friendDao.deleteOneFriend(new ObjectId("55015a9ce05f36b1274c2687"), new ObjectId("54fe685ae05f50618b1b1456"));
    }
    @Test
    public void  addOneFriend(){
        friendDao.addOneFriend(new ObjectId("55015a9ce05f36b1274c2687"), new ObjectId("550162b8e05f5e86f7b9eedd"));
    }
    @Test
    public void recordIsExist(){
        boolean k=friendDao.recordIsExist(new ObjectId("55015a9ce05f36b1274c2687"));

    }
    @Test
    public void addFriendEntry(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        objectIdList.add(new ObjectId("54fe685ae05f50618b1b1456"));
        friendDao.addFriendEntry(new ObjectId("55015a9ce05f36b1274c2687"),objectIdList);
    }

}
