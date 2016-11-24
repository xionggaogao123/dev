package com.db.activity;

import com.db.base.SynDao;
import com.pojo.activity.FriendApplyEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.List;

/**
 * Created by yan on 2015/3/12.
 */
public class FriendApplyDaoTest {

    private FriendApplyDao friendApplyDao=new FriendApplyDao();
    @Test
    public void countNoResponseReply(){
        int count=friendApplyDao.countNoResponseReply(new ObjectId("54fe685ae05f50618b1b1456"));

    }
    @Test
    public void insertApply(){
        FriendApplyEntry friendApplyEntry=new FriendApplyEntry();
        friendApplyEntry.setApplyDate(System.currentTimeMillis());
        friendApplyEntry.setContent("dfgfd");
        friendApplyEntry.setRespond(new ObjectId("54fe685ae05f50618b1b1456"));
        friendApplyEntry.setRespondDate(System.currentTimeMillis());
        friendApplyEntry.setUserId(new ObjectId("54fe685ae05f50618b1b1456"));
        friendApplyDao.insertApply(friendApplyEntry);
    }
    @Test
    public void findFriendApplyList(){
        List<FriendApplyEntry> friendApplyEntryList=friendApplyDao.findFriendApplyList(new ObjectId("54fe685ae05f50618b1b1456"));
    }
    @Test
    public void findFriendApplyListPage(){
        List<FriendApplyEntry> friendApplyEntryList=friendApplyDao.findFriendApplyList(new ObjectId("54fe685ae05f50618b1b1456"),0,100);
    }
    @Test
    public void findFriendApplyById(){
        FriendApplyEntry friendApplyEntry=friendApplyDao.findFriendApplyById(new ObjectId("55015a9ce05f36b1274c2687"));
    }
    @Test
    public void acceptApply(){
        friendApplyDao.acceptApply(new ObjectId("55015a9ce05f36b1274c2687"));
    }
    @Test
    public void refuseApply(){
        friendApplyDao.refuseApply(new ObjectId("55015a9ce05f36b1274c2687"));
    }

}
