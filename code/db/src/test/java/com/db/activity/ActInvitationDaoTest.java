package com.db.activity;

import com.pojo.activity.ActInvitationEntry;
import com.pojo.activity.enums.ActIvtStatus;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Created by yan on 2015/3/10.
 */
public class ActInvitationDaoTest {

    private ActInvitationDao actInvitationDao=new ActInvitationDao();

    @Test
    public void updateInvitationStatus(){

        actInvitationDao.updateInvitationStatus(new ObjectId("54fe6aace05fff36853f24d8"), ActIvtStatus.REJECT.getState());
        System.out.println("11");

    }
    @Test
    public void insertActInvitation(){
        ActInvitationEntry actInvitationEntry=new ActInvitationEntry(new ObjectId("54fe685ae05f50618b1b1456"),
                new ObjectId("54fe685ae05f50618b1b1456"),"扯淡的飞机",ActIvtStatus.HESITATE.getState());
//        actInvitationEntry.setActId(new ObjectId("54fe685ae05f50618b1b1456"));
//        actInvitationEntry.setGuestId(new ObjectId("54fe685ae05f50618b1b1456"));
//        actInvitationEntry.setStatus(ActIvtStatus.HESITATE.getState());
//        actInvitationEntry.setMsg("扯淡的飞机");
        actInvitationDao.insertActInvitation(actInvitationEntry);//ok
    }
    @Test
    public void findInvitationById(){
        ActInvitationEntry actInvitationEntry1=actInvitationDao.findInvitationById(new ObjectId("54fe6aace05fff36853f24d8"));//ok
    }
    @Test
    public void findInvitation(){
        ActInvitationEntry actInvitationEntry2=actInvitationDao.findInvitation(new ObjectId("54fe685ae05f50618b1b1456"), new ObjectId("54fe685ae05f50618b1b1456"));
        System.out.println("a");
    }
    @Test
    public void deleteActInvitationById(){
        actInvitationDao.deleteActInvitationById(new ObjectId("54fe685ae05f50618b1b1456"));
    }
    @Test
    public void selectInvitationCount(){
        int count=actInvitationDao.selectInvitationCount(new ObjectId("54fe685ae05f50618b1b1456"),ActIvtStatus.HESITATE);
    }
}
