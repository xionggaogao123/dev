package com.fulaan_old.friendscircle.service;

import com.db.activity.ActivityTrackDao;
import com.db.activity.FriendApplyDao;
import com.db.activity.FriendDao;
import com.db.user.UserDao;
import com.pojo.activity.FriendApply;
import com.pojo.activity.FriendApplyEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ChenHao on 14-10-20.
 */
@Service
public class FriendApplyService {

  private FriendApplyDao friendApplyDao = new FriendApplyDao();
  private FriendDao friendDao = new FriendDao();
  private ActivityTrackDao activityTrackDao = new ActivityTrackDao();
  private UserDao userDao = new UserDao();


  /**
   * 申请加为好友
   *
   * @param content    附言
   * @param userId     发起人Id
   * @param responseId 响应人Id
   */
  public void insertApply(String content, String userId, String responseId) {
    FriendApplyEntry friendApplyEntry = new FriendApplyEntry();
    friendApplyEntry.setApplyDate(System.currentTimeMillis());
    friendApplyEntry.setContent(content);
    friendApplyEntry.setRespond(new ObjectId(responseId));
    friendApplyEntry.setRespondDate(System.currentTimeMillis());
    friendApplyEntry.setUserId(new ObjectId(userId));
    friendApplyDao.insertApply(friendApplyEntry);
  }

  /**
   * 接受好友请求
   * @param applyId
   * @return
   */
  public boolean acceptApply(String applyId) {
    FriendApplyEntry friendApplyEntry = friendApplyDao.findFriendApplyById(new ObjectId(applyId));
    boolean k = friendDao.isFriend(friendApplyEntry.getRespondent(), friendApplyEntry.getUserId());
    if (k) {
      friendApplyDao.deleteApplyById(new ObjectId(applyId));
      return k;
    }
    String userId = friendApplyEntry.getUserId().toString();
    String respondId = friendApplyEntry.getRespondent().toString();

    //添加好友   加两次  无语的dao方法原子性  hh
    boolean m = friendDao.recordIsExist(new ObjectId(userId));
    boolean n = friendDao.recordIsExist(new ObjectId(respondId));
    List<ObjectId> friendIds = new ArrayList<ObjectId>();
    if(m){
      friendDao.addOneFriend(new ObjectId(userId), new ObjectId(respondId));
    }else{
      friendIds.add(new ObjectId(respondId));
      friendDao.addFriendEntry(new ObjectId(userId), friendIds);
    }

    if(n){
      friendDao.addOneFriend(new ObjectId(respondId), new ObjectId(userId));
    }else{
      friendIds.clear();
      friendIds.add(new ObjectId(userId));
      friendDao.addFriendEntry(new ObjectId(respondId), friendIds);
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
    int count = friendApplyDao.countNoResponseReply(new ObjectId(userId));
    return count;
  }

  public List<FriendApply> findFriendApplyList(String userId, Integer skip, Integer size) {
    List<FriendApplyEntry> friendApplyEntries = friendApplyDao.findFriendApplyList(new ObjectId(userId), skip, size);

    List<FriendApply> friendApplyList = new ArrayList<FriendApply>();
    for (FriendApplyEntry friendApplyEntry : friendApplyEntries) {
      if (friendApplyEntry.getUserId() != null && friendApplyEntry.getRespondent() != null)//为了过滤脏数据
        friendApplyList.add(new FriendApply(friendApplyEntry));
    }
    return friendApplyList;
  }


  /**
   * 不带分页功能（查看多少人申请好友）
   *
   * @param userId
   * @return
   */
  public List<FriendApply> findFriendApplyListByCondition(String userId) {
    return findFriendApplyListByCondition(userId,0);
  }


  /**
   * 不带分页功能（查看多少人申请好友）
   *
   * @param userId
   * @return
   */
  public List<FriendApply> findFriendApplys(String userId,int accepted,int page,int pageSize) {
    List<FriendApplyEntry> friendApplyEntries = friendApplyDao.findFriendApplys(new ObjectId(userId),accepted,page,pageSize);

    return getFriendApplyList(friendApplyEntries);
  }


  public List<FriendApply> getFriendApplyList(List<FriendApplyEntry> friendApplyEntries){
    List<FriendApply> friendApplyList = new ArrayList<FriendApply>();
    for (FriendApplyEntry friendApplyEntry : friendApplyEntries) {
      //为了过滤脏数据
      if (friendApplyEntry.getUserId() != null && friendApplyEntry.getRespondent() != null) {
        FriendApply friendApply = new FriendApply(friendApplyEntry);
        UserEntry userEntry = userDao.getUserEntry(friendApplyEntry.getUserId(), Constant.FIELDS);
        friendApply.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
        long time = friendApplyEntry.getApplyDate();
        long nowTime = System.currentTimeMillis();
        long day = (nowTime - time) / (1000 * 60 * 60 * 24);
        friendApply.setTime(day);

        if(StringUtils.isNotBlank(userEntry.getNickName())){
          friendApply.setNickName(userEntry.getNickName());
        }else{
          friendApply.setNickName(userEntry.getUserName());
        }
        List<UserEntry.UserTagEntry> userTagEntries=userEntry.getUserTag();
        List<String> tagEntries=new ArrayList<String>();
        for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
          tagEntries.add(userTagEntry.getTag());
        }
        friendApply.setTags(tagEntries);
        friendApplyList.add(friendApply);
      }
    }
    return friendApplyList;
  }
  /**
   * 不带分页功能（查看多少人申请好友）
   *
   * @param userId
   * @return
   */
  public List<FriendApply> findFriendApplyListByCondition(String userId,int accepted) {
    List<FriendApplyEntry> friendApplyEntries = friendApplyDao.findFriendApplyListByCondition(new ObjectId(userId),accepted);


    return getFriendApplyList(friendApplyEntries);
  }

  public FriendApply getFriendApplyDetail(String id) {
    FriendApplyEntry friendApplyEntry = friendApplyDao.findFriendApplyById(new ObjectId(id));
    return new FriendApply(friendApplyEntry);
  }

  public List<FriendApply> findFriendApplyList(String userId) {
    List<FriendApplyEntry> friendApplyEntries = friendApplyDao.findFriendApplyList(new ObjectId(userId));
    List<FriendApply> friendApplyList = new ArrayList<FriendApply>();

    for (FriendApplyEntry friendApplyEntry : friendApplyEntries) {
      if (friendApplyEntry.getUserId() != null && friendApplyEntry.getRespondent() != null)//为了过滤脏数据
      {
        FriendApply friendApply = new FriendApply(friendApplyEntry);
        friendApplyList.add(friendApply);
      }
    }
    return friendApplyList;
  }

  public List<FriendApply> findApplyBySponsorIdAndRespondentId(String sponsorId, String userId) {
    List<FriendApplyEntry> friendApplyEntries = friendApplyDao.findApplyBySponsorIdAndRespondentId(new ObjectId(sponsorId), new ObjectId(userId));
    List<FriendApply> friendApplyList = new ArrayList<FriendApply>();
    for (FriendApplyEntry friendApplyEntry : friendApplyEntries) {
      if (friendApplyEntry.getUserId() != null && friendApplyEntry.getRespondent() != null) {//为了过滤脏数据
        FriendApply friendApply = new FriendApply(friendApplyEntry);
        friendApplyList.add(friendApply);
      }
    }
    return friendApplyList;
  }

  public void updateApplyByIds(List<ObjectId> applyIds) {
    friendApplyDao.updateApplyByIds(applyIds);
  }


  public Map<ObjectId,FriendApplyEntry> getFriendApplyMap(ObjectId sponsorId, List<ObjectId> respondentIds){
    return friendApplyDao.getFriendApplyMap(sponsorId, respondentIds);
  }
}
