package com.db.temp;

import com.db.activity.ActInvitationDao;
import com.db.activity.ActivityDao;
import com.db.activity.ActivityTrackDao;
import com.db.user.UserDao;


import com.pojo.activity.ActivityDiscuss;

import com.pojo.activity.ActivityEntry;
import com.pojo.user.UserEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/8/28.
 */
public class DeletActivity {
    private UserDao userDao = new UserDao();
    private ActivityDao activityDao = new ActivityDao();
    private ActivityTrackDao activityTrackDao = new ActivityTrackDao();
    private ActInvitationDao actInvitationDao = new ActInvitationDao();

    /**
     * 根据用户名删除所有记录
     *
     * @param userName
     */
    public void deleteActivityByUserName(String userName) {
        // 找到用户id
        UserEntry userEntry = userDao.findByName(userName);

        //王子恒8
        if (userEntry != null) {
            ObjectId userId = userEntry.getID();
            List<ActivityEntry> activityEntryList = activityDao.myOrganizedActivity(userId, 0, 1000);
            if (activityEntryList != null && !activityEntryList.isEmpty()) {
                List<ObjectId> actIds = new ArrayList<ObjectId>();
                for (ActivityEntry activityEntry : activityEntryList) {
                    actIds.add(activityEntry.getID());
                }
                //删除actTrack中记录
                activityTrackDao.deleteByActivityId(actIds);
                //删除actInvation记录
                actInvitationDao.deleteByActivityIds(actIds);
                //删除activity记录
                activityDao.deleteByActivityIds(actIds);
            }
        }
    }

    public static void main(String[] args) {

        new DeletActivity().deleteAllSpeak(args[0]);

    }

    /**
     * 删除该用户的所有发言
     *
     * @param userName
     */
    public void deleteAllSpeak(String userName) {
        // 找到用户id
        UserEntry userEntry = userDao.findByName(userName);
        if (userEntry != null) {
            ObjectId userId = userEntry.getID();
            List<ActivityEntry> activityEntryList = activityDao.getAllList();
            if (activityEntryList != null && !activityEntryList.isEmpty()) {
                for (ActivityEntry activityEntry : activityEntryList) {
                    List<ActivityDiscuss> activityDiscussList = activityEntry.getActDiscusses();
                    if (activityDiscussList != null && !activityDiscussList.isEmpty()) {
                        for (ActivityDiscuss activityDiscuss : activityDiscussList) {
                            if (activityDiscuss.getUserId().equals(userId)) {
                                activityDao.deleteDiscuss(activityEntry.getID(), activityDiscuss);
                                //break;
                            }
                        }
                    }
                }
            }
        }
    }
}
