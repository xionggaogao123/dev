package com.fulaan.friendscircle.service;

import com.db.activity.ActivityTrackDao;
import com.db.activity.FriendApplyDao;
import com.db.activity.FriendDao;
import com.pojo.activity.Friend;
import com.pojo.activity.FriendApply;
import com.pojo.activity.FriendApplyEntry;
import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActTrackType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHao on 14-10-20.
 */
@Service
public class FriendApplyService {

    private FriendApplyDao friendApplyDao=new FriendApplyDao();
    private FriendDao friendDao=new FriendDao();
    private ActivityTrackDao activityTrackDao=new ActivityTrackDao();


    public boolean acceptApply(String applyId, String fromDevice) {
        FriendApplyEntry friendApplyEntry=friendApplyDao.findFriendApplyById(new ObjectId(applyId));
        boolean k=friendDao.isFriend(friendApplyEntry.getRespondent(),friendApplyEntry.getUserId());
        if(k) return k;
        String userId=friendApplyEntry.getUserId().toString();
        String respondId=friendApplyEntry.getRespondent().toString();

        //添加好友   加两次  无语的dao方法原子性  hh
        boolean m=friendDao.recordIsExist(new ObjectId(userId));
        boolean n=friendDao.recordIsExist(new ObjectId(respondId));

        List<ObjectId> friendIds=new ArrayList<ObjectId>();
        friendIds.add(new ObjectId(respondId));
        if(!m) friendDao.addFriendEntry(new ObjectId(userId),friendIds);

        friendIds.clear();
        friendIds.add(new ObjectId(userId));
        if(!n) friendDao.addFriendEntry(new ObjectId(respondId),friendIds);


        friendDao.addOneFriend(new ObjectId(userId),new ObjectId(respondId));
        friendDao.addOneFriend(new ObjectId(respondId),new ObjectId(userId));

        //添加成为好友の动态
        if("1".equals(fromDevice)){
            int type=ActTrackDevice.FromAndroid.getState();
            activityTrackDao.insertActTrack(ActTrackType.FRIEND.getState(),userId,respondId,System.currentTimeMillis(),type);
        }else if("2".equals(fromDevice)){
            int type=ActTrackDevice.FromIOS.getState();
            activityTrackDao.insertActTrack(ActTrackType.FRIEND.getState(),userId,respondId,System.currentTimeMillis(),type);
        }else { //0
            int type=ActTrackDevice.FromPC.getState();
            activityTrackDao.insertActTrack(ActTrackType.FRIEND.getState(),userId,respondId,System.currentTimeMillis(),type);
        }
        //更新申请状态
        friendApplyDao.acceptApply(new ObjectId(applyId));
        return true;
    }

    public boolean refuseApply(String applyId) {
        friendApplyDao.refuseApply(new ObjectId(applyId));
        return true;
    }



    public int countNoResponseReply(String userId) {
        int count=friendApplyDao.countNoResponseReply(new ObjectId(userId));
        return count;
    }
    public List<FriendApply> findFriendApplyList(String userId, Integer skip, Integer size) {
        List<FriendApplyEntry> friendApplyEntries=friendApplyDao.findFriendApplyList(new ObjectId(userId),skip,size);

        List<FriendApply> friendApplyList=new ArrayList<FriendApply>();
        for(FriendApplyEntry friendApplyEntry:friendApplyEntries){
        	if(friendApplyEntry.getUserId()!=null && friendApplyEntry.getRespondent()!=null)//为了过滤脏数据
        		friendApplyList.add(new FriendApply(friendApplyEntry));
        }
        return friendApplyList;
    }

    public FriendApply getFriendApplyDetail(String id) {
        FriendApplyEntry friendApplyEntry=friendApplyDao.findFriendApplyById(new ObjectId(id));
        return new FriendApply(friendApplyEntry);
    }

    public List<FriendApply> findFriendApplyList(String userId) {
        List<FriendApplyEntry> friendApplyEntries=friendApplyDao.findFriendApplyList(new ObjectId(userId));
        List<FriendApply> friendApplyList=new ArrayList<FriendApply>();

        for(FriendApplyEntry friendApplyEntry:friendApplyEntries){
        	if(friendApplyEntry.getUserId()!=null && friendApplyEntry.getRespondent()!=null)//为了过滤脏数据
        	{
        		FriendApply friendApply=new FriendApply(friendApplyEntry);
        		friendApplyList.add(friendApply);
        	}
        }
        return friendApplyList;
    }

    public List<FriendApply> findApplyBySponsorIdAndRespondentId(String sponsorId, String userId) {
        List<FriendApplyEntry> friendApplyEntries=friendApplyDao.findApplyBySponsorIdAndRespondentId(new ObjectId(sponsorId), new ObjectId(userId));
        List<FriendApply> friendApplyList=new ArrayList<FriendApply>();
        for(FriendApplyEntry friendApplyEntry:friendApplyEntries){
            if(friendApplyEntry.getUserId()!=null && friendApplyEntry.getRespondent()!=null){//为了过滤脏数据
                FriendApply friendApply=new FriendApply(friendApplyEntry);
                friendApplyList.add(friendApply);
            }
        }
        return friendApplyList;
    }

    public void updateApplyByIds(List<ObjectId> applyIds) {
        friendApplyDao.updateApplyByIds(applyIds);
    }
}
