package com.fulaan_old.friendscircle.service;

import com.db.activity.*;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.activity.*;
import com.pojo.activity.enums.ActIvtStatus;
import com.pojo.activity.enums.ActStatus;
import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActTrackType;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by hao on 2014/10/22.
 */
@Service
public class ActivityService {

    private ActivityDao activityDao = new ActivityDao();
    private UserDao userDao = new UserDao();
    private FriendDao friendDao = new FriendDao();
    private ActivityTrackDao activityTrackDao = new ActivityTrackDao();
    private ActInvitationDao actInvitationDao = new ActInvitationDao();
    private ActivityDiscussDao activityDiscussDao = new ActivityDiscussDao();

    /*
    *
    *
    * */
    public String add(Activity act, ActTrackDevice actTrackDevice) {
        //插入活动信息
        ActivityEntry activityEntry = act.exportActivity();
        UserEntry userEntry = userDao.getUserEntry(activityEntry.getOrganizerId(), new BasicDBObject("si", 1));
        ObjectId schoolId = userEntry.getSchoolID();
        SchoolEntry schoolEntry = new SchoolDao().getSchoolEntry(schoolId, Constant.FIELDS);
        List<ObjectId> attIds = new ArrayList<ObjectId>();
        attIds.add(activityEntry.getOrganizerId());

        activityEntry.setSchoolId(schoolEntry.getID());
        activityEntry.setRegionId(schoolEntry.getRegionId());
        activityEntry.setAttendIds(attIds);

        ObjectId actId = activityDao.insertActivity(activityEntry);
        //插入动态
        activityTrackDao.insertActTrack(ActTrackType.PROMOTE.getState(),
                act.getOrganizer(),
                actId.toString(),
                System.currentTimeMillis(),
                actTrackDevice.ordinal());
        return actId.toString();
    }

    public String add(ActInvitation actIvt) {
        ActInvitationEntry actInvitation = actIvt.exportEntry();
        ObjectId invitationId = actInvitationDao.insertActInvitation(actInvitation);
        return invitationId.toString();
    }

    public Activity getAct(String actId) {
        ActivityEntry activityEntry = activityDao.findActivityById(new ObjectId(actId));
        return new Activity(activityEntry);
    }

    public List<ActAttend> getAttendDetails(String actId) {
        ActivityEntry activityEntry = activityDao.findActivityById(new ObjectId(actId));
        List<ObjectId> userIds = activityEntry.getAttendIds();
        List<UserEntry> userEntryList = userDao.getUserEntryList(userIds, Constant.FIELDS);
        List<ActAttend> actAttends = new ArrayList<ActAttend>();
        for (UserEntry userEntry : userEntryList) {
            ActAttend actAttend = new ActAttend();
            actAttend.setUserId(userEntry.getID().toString());
            actAttend.setActivityId(actId);
            actAttend.setNickName(userEntry.getNickName());
            actAttends.add(actAttend);
        }
        return actAttends;
    }

    public List<Activity> getHotActivity(String geoId, int begin, int end) {
        List<ActivityEntry> activityEntries = activityDao.selectHotActivity(new ObjectId(geoId), begin, end);
        List<Activity> activityList = new ArrayList<Activity>();
        for (ActivityEntry activityEntry : activityEntries) {
            activityList.add(new Activity(activityEntry));
        }
        return activityList;
    }

    public void updateActivityStatus(String actId, ActStatus status) {
        //更新活动状态
        activityDao.updateActivityStatus(new ObjectId(actId), status);
        List<ObjectId> activityList = new ArrayList<ObjectId>();
        activityList.add(new ObjectId(actId));
        activityTrackDao.deleteByActivityId(activityList);
    }

    public void add(ActAttend attend, ActTrackDevice actTrackDevice) {
        //添加人员参加活动的信息
        activityDao.insertAttend(new ObjectId(attend.getActivityId()), new ObjectId(attend.getUserId()));
        //更新活动参与人员数量
        activityDao.updateAttendCount(new ObjectId(attend.getActivityId()));
        //添加参加活动动态
        ActivityTrackDao activityTrackDao = new ActivityTrackDao();
        activityTrackDao.insertActTrack(ActTrackType.ATTEND.getState(),
                attend.getUserId(),
                attend.getActivityId(),
                System.currentTimeMillis(),
                actTrackDevice.ordinal());
    }

    public ActInvitation getInvitation(String actId, String userId) {
        ActInvitationEntry invitationEntry = actInvitationDao.findInvitation(new ObjectId(actId), new ObjectId(userId));
        ActInvitation actInvitation = new ActInvitation(invitationEntry);
        if (actInvitation.getId() == null) return null;
        return actInvitation;
    }

    public void quitAct(String actId, String userId) {
        activityDao.quitActivity(new ObjectId(actId), new ObjectId(userId));
    }

    public List<ActTrack> getActTrackList(String userId, int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        int begin = (page - 1) * pageSize;
        ActivityTrackDao activityTrackDao = new ActivityTrackDao();
        List<ObjectId> objectIdList = new FriendDao().findMyFriendIds(new ObjectId(userId));
        objectIdList.add(new ObjectId(userId));
        List<ActTrackEntry> actTrackEntryList = activityTrackDao.findActTrack(objectIdList, begin, pageSize);
        List<ActTrack> actTrackList = new ArrayList<ActTrack>();

        Set<ObjectId> activityIds = new HashSet<ObjectId>();

        for (ActTrackEntry actTrackEntry : actTrackEntryList) {
            if (actTrackEntry.getRelatedId() != null && actTrackEntry.getUserId() != null) {//过滤脏数据
                ActTrack actTrack = new ActTrack(actTrackEntry);
                if (actTrackEntry.getActTrackType() == 0 || actTrackEntry.getActTrackType() == 2 || actTrackEntry.getActTrackType() == 3) {
                    ObjectId activityId = actTrackEntry.getRelatedId();
                    Activity activity = new Activity();
                    actTrack.setActivity(activity);
                    activityIds.add(activityId);
                } else if (actTrackEntry.getActTrackType() == 1) {//添加好友
                    UserEntry userEntry = userDao.getUserEntry(actTrackEntry.getRelatedId(), Constant.FIELDS);
                    actTrack.setRelateUserName(userEntry.getUserName());
                }
                long k = System.currentTimeMillis() - actTrack.getCreateTime().getTime();
                actTrack.setTimeMsg(getTimeMsg(k));
                UserDetailInfoDTO userEntry = new UserDetailInfoDTO(userDao.getUserEntry(actTrackEntry.getUserId(), Constant.FIELDS));
                actTrack.setUserName(userEntry.getUserName());
                actTrack.setUserImgUrl(userEntry.getImgUrl());
                actTrackList.add(actTrack);
            }
        }
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        Map<ObjectId, ActivityEntry> activityEntryMap = activityDao.findActivityMapByIds(activityIds);
        for (ActTrack actTrack : actTrackList) {

            if (actTrack.getActivity() != null) {
                ActivityEntry activityEntry = activityEntryMap.get(new ObjectId(actTrack.getRelateId()));
                if (activityEntry != null) {
                    Activity activity = new Activity(activityEntry);
                    actTrack.setActivity(activity);
                    userIds.add(activityEntry.getOrganizerId());
                }
            }
        }
        /*for(int m=0;m<actTrackList.size();m++)
        {
            ActTrack actTrack = actTrackList.get(m);
            if (actTrack.getActivity() != null) {
                ActivityEntry activityEntry = activityEntryMap.get(new ObjectId(actTrack.getRelateId()));
                if (activityEntry != null) {
                    Activity activity = new Activity(activityEntry);
                    if(activity.getStatus().getState()==1)//活动已取消
                    {
                        actTrackList.remove(m);
                        m--;
                    }
                    else {
                        actTrack.setActivity(activity);
                        userIds.add(activityEntry.getOrganizerId());
                    }
                }
            }
        }*/
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, new BasicDBObject("nm", 1).append("r", 1).append("avt", 1));
        for (ActTrack actTrack : actTrackList) {
            if (actTrack.getActivity() != null) {
                Activity activity = actTrack.getActivity();
                if (!ObjectId.isValid(activity.getOrganizer())) {
                    activity.setOrganizer(new ObjectId().toString());
                }
                UserEntry userEntry = userEntryMap.get(new ObjectId(activity.getOrganizer()));
                if (userEntry != null) {
                    activity.setOrganizerName(userEntry.getUserName());
                    activity.setOrganizerRole(userEntry.getRole());
                    activity.setOrganizerImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
                }
            }
        }
        return actTrackList;
    }

    /**
     * 根据发布时间返回多久前发布
     *
     * @param time
     * @return
     */
    public String getTimeMsg(long time) {

        long second = time / 1000;
        if (second < 300) {//小于五分钟
            return "刚刚";
        } else if (second < 60 * 60) {//五分钟到一小时之间
            long minute = second / 60;
            return minute + "分钟前";
        } else if (second < 60 * 60 * 24) {//一小时到一天之间
            long hour = second / 60 / 60;
            return hour + "小时前";
        } else {
            long day = second / 60 / 60 / 24;
            return day + "天前";
        }
    }

    public int findActTrackCount(String userId) {

        List<ObjectId> ids = friendDao.findMyFriendIds(new ObjectId(userId));
        ids.add(new ObjectId(userId));
        int count = activityTrackDao.findActTrackCount(ids);
        return count;
    }

    public ActInvitation selectInvitationById(String id) {
        ActInvitationEntry actInvitationEntry = actInvitationDao.findInvitationById(new ObjectId(id));
        if (actInvitationEntry == null) return null;
        return new ActInvitation(actInvitationEntry);
    }

    public void updateInvitationStatus(String inviteId, ActIvtStatus state) {
        actInvitationDao.updateInvitationStatus(new ObjectId(inviteId), state.getState());
    }

    public Activity selectActAttend(String actId, String attUserId) {
        ActivityEntry activityEntry = activityDao.findActivityByIdAndAttendUserId(new ObjectId(actId), new ObjectId(attUserId));
        if (activityEntry == null) return null;
        return new Activity(activityEntry);
    }

    public List<Activity> selectHotActivity(String geoId, Integer page, Integer pageSize) {
        List<Activity> activityList = new ArrayList<Activity>();
        List<ActivityEntry> activityEntryList = activityDao.selectHotActivity(new ObjectId(geoId), page, pageSize);
        for (ActivityEntry activityEntry : activityEntryList) {
            activityList.add(new Activity(activityEntry));
        }
        return activityList;
    }

    public List<Activity> myAttendActivity(String userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 5;
        int begin = (page - 1) * pageSize;
        List<ActivityEntry> activityEntryList = activityDao.myAttendActivity(new ObjectId(userId), begin, pageSize);

        List<Activity> activityList = new ArrayList<Activity>();
        for (ActivityEntry activityEntry : activityEntryList) {
            UserDetailInfoDTO userInfo = getUserInfoById(activityEntry.getOrganizerId().toString());
            Activity activity = new Activity(activityEntry);
            activity.setOrganizerName(userInfo.getUserName());
            //activityList.add(new Activity(activityEntry));
            activity.setOrganizerRole(userInfo.getRole());
            activity.setOrganizerImageUrl(userInfo.getImgUrl());
            activityList.add(activity);
        }
        return activityList;
    }

    /**
     * 临时添加，
     *
     * @param id
     * @return
     */
    public UserDetailInfoDTO getUserInfoById(String id) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        if (userEntry == null) return null;
        return new UserDetailInfoDTO(userEntry);
    }


    public List<Activity> myOrganizedActivity(String userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        int begin = (page - 1) * pageSize;
        List<ActivityEntry> activityEntryList = activityDao.myOrganizedActivity(new ObjectId(userId), begin, pageSize);
        List<Activity> activityList = new ArrayList<Activity>();
        for (ActivityEntry activityEntry : activityEntryList) {
            Activity activity = new Activity(activityEntry);
            UserDetailInfoDTO userInfo = getUserInfoById(activityEntry.getOrganizerId().toString());
            activity.setOrganizerName(userInfo.getUserName());
            activity.setOrganizerRole(userInfo.getRole());
            activity.setOrganizerImageUrl(userInfo.getImgUrl());
            activityList.add(activity);
        }
        return activityList;
    }

    public int myOrganizedActivityCount(String userId) {
        int count = activityDao.myOrganizedActivityCount(new ObjectId(userId));
        return count;
    }

    public List<UserDetailInfoDTO> usersInActivity(String activityId, Integer page, Integer pageSize) {
        ActivityEntry activityEntry = activityDao.findActivityById(new ObjectId(activityId));
        List<ObjectId> objectIdList = activityEntry.getAttendIds();
        int begin = (page - 1) * pageSize;

        if (begin > objectIdList.size()) return null;
        if (pageSize + begin > objectIdList.size()) pageSize = objectIdList.size();

        objectIdList = objectIdList.subList(begin, pageSize);
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);

        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }

    public int userInActivityCount(String activityId) {
        int count = activityDao.userInActivityCount(new ObjectId(activityId));
        return count;
    }

    /*
    *
    * 查找活动评论 --所有
    *
    * */
    public List<ActivityDiscussDTO> selectActDises(String actId) {
        List<ActivityDiscuss> activityDiscussList = activityDiscussDao.findDiscussByActId(new ObjectId(actId));
        List<ActivityDiscussDTO> activityDiscussDTOs = new ArrayList<ActivityDiscussDTO>();
        for (ActivityDiscuss activityDiscuss : activityDiscussList) {
            ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO(activityDiscuss);
            activityDiscussDTOs.add(activityDiscussDTO);
        }
        return activityDiscussDTOs;
    }

    /*
    *
    * 查找活动评论  分页
    * */
    public Map<String, Object> findActDiscuss(String activityId, Integer page, Integer pageSize) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<ActivityDiscuss> activityDiscussList = activityDiscussDao.findDiscussByActId(new ObjectId(activityId));

        List<ActivityDiscussDTO> activityDiscussDTOs = new ArrayList<ActivityDiscussDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (ActivityDiscuss activityDiscuss : activityDiscussList) {
            activityDiscussDTOs.add(new ActivityDiscussDTO(activityDiscuss));
            userIds.add(activityDiscuss.getUserId());
        }

        LinkedHashMap<String, ActivityDiscussDTO> map1 = new LinkedHashMap<String, ActivityDiscussDTO>();
        for (int i = 0; i < activityDiscussDTOs.size(); i++) {
            ActivityDiscussDTO activityDiscussDTO = activityDiscussDTOs.get(i);
            map1.put(activityDiscussDTO.getId(), activityDiscussDTO);
        }

        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(userIds, new BasicDBObject("nm", 1).append("r", 1).append("avt", 1));
        for (ActivityDiscussDTO discussDTO : activityDiscussDTOs) {
            String userId = discussDTO.getUserId();
            if (ObjectId.isValid(userId)) {
                UserEntry userEntry = userMap.get(new ObjectId(userId));
                discussDTO.setUserName(userEntry.getUserName());
                discussDTO.setUserImage(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
            }
        }

        Set<String> actSet = map1.keySet();
        List<ActivityDiscussDTO> activities = new ArrayList<ActivityDiscussDTO>();
        for (String integer : actSet) {
            ActivityDiscussDTO activityDiscussDTO = map1.get(integer);
            //如果大于0 说明是子回复
            if (activityDiscussDTO.getRepId() != null && activityDiscussDTO.getRepId() != "") {
                map1.get(activityDiscussDTO.getRepId()).getSubDiscussList().add(activityDiscussDTO);
            } else {//子回复 不计入count中
                activities.add(activityDiscussDTO);
            }
        }
        Collections.reverse(activities);
        activityDiscussDTOs = fenye(activityDiscussDTOs, page, pageSize);
        returnMap.put("total", activityDiscussDTOs.size());
        returnMap.put("discuss", activityDiscussDTOs);
        return returnMap;
    }

    /*
    *
    * 查找活动评论图片
    *
    * */
    public Map<String, Object> findActDiscussPicture(String activityId, Integer page, Integer pageSize) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<ActivityDiscussDTO> activityDiscussDTOList = activityPicture(activityId);
        LinkedHashMap<String, ActivityDiscussDTO> map1 = new LinkedHashMap<String, ActivityDiscussDTO>();
        for (int i = 0; i < activityDiscussDTOList.size(); i++) {
            ActivityDiscussDTO activityDiscussDTO = activityDiscussDTOList.get(i);
            map1.put(activityDiscussDTO.getId(), activityDiscussDTO);
        }
        Set<String> actSet = map1.keySet();
        List<ActivityDiscussDTO> activities = new ArrayList<ActivityDiscussDTO>();
        for (String integer : actSet) {
            ActivityDiscussDTO activityDiscussDTO = map1.get(integer);
            //如果大于0 说明是子回复
            if (activityDiscussDTO.getRepId() != null && activityDiscussDTO.getRepId() != "") {
                map1.get(activityDiscussDTO.getRepId()).getSubDiscussList().add(activityDiscussDTO);
            } else {//子回复 不计入count中
                activities.add(activityDiscussDTO);
            }
        }
        Collections.reverse(activities);
        activities = fenye(activities, page, pageSize);
        returnMap.put("total", activities.size());
        returnMap.put("discuss", activities);
        return returnMap;
    }

    /*
    *
    * 插入活动评论
    * */
    public void insertActDiscuss(String actId, ActivityDiscussDTO ad) {
        //更新讨论和图片数量
        activityDao.updateDiscussAndImgCount(new ObjectId(actId), ad.getImage());
        Long time = System.currentTimeMillis();
        //插入动态信息
        activityTrackDao.insertActTrack(ActTrackType.REPLY.getState(),
                ad.getUserId(),
                actId, time,
                ActTrackDevice.FromPC.getState());
        //插入讨论信息
        ActivityDiscuss activityDiscuss = ad.exportEntry();
        activityDiscuss.setDate(time);
        activityDiscussDao.insertDiscuss(new ObjectId(actId), activityDiscuss);
    }

    /*
    *
    * 我参加的活动数量
    *
    * */
    public int myAttendActivityCount(String userId) {
        int count = activityDao.myAttendActivityCount(new ObjectId(userId));
        return count;
    }

    /*
    *
    *当前用户活动邀请数量
    * */
    public int selectInvitationCount(String userId) {
        int count = actInvitationDao.selectInvitationCount(new ObjectId(userId), ActIvtStatus.INVITE);
        return count;
    }

    /*
    * 活动idList 查找活动List
    *
    * */
    public List<Activity> selectCount(List<String> ids) {
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (String id : ids) {
            if (ObjectId.isValid(id))
                objectIdList.add(new ObjectId(id));
        }
        List<ActivityEntry> activityEntryList = activityDao.selectActivityWhereActIdIn(objectIdList);
        List<Activity> activityList = new ArrayList<Activity>();
        for (ActivityEntry activityEntry : activityEntryList) {
            Activity activity = new Activity(activityEntry);
            activityList.add(activity);
        }
        return activityList;
    }

    public int deleteActInvitationById(String actInvitationId) {
        actInvitationDao.deleteActInvitationById(new ObjectId(actInvitationId));
        return 1;
    }

    /*
    *
    * 推荐活动 仅限于本校内
    *
    * */
    public List<Activity> recommendActivityOnlySchool(String schoolId, String userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        int begin = (page - 1) * pageSize;
        List<ActivityEntry> activityEntryList = activityDao.activityRelation2me(new ObjectId(userId));
        List<ObjectId> ids = new ArrayList<ObjectId>();
        for (ActivityEntry activityEntry : activityEntryList) {
            ids.add(activityEntry.getID());
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(ids, Constant.FIELDS);//这里查到的为空
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        for (UserEntry userEntry : userEntryList) {
            userEntryMap.put(userEntry.getID(), userEntry);
        }
        List<ActivityEntry> acts = activityDao.recommendActivityOnlySchool(new ObjectId(schoolId), ids, begin, pageSize);
        List<Activity> activityList = new ArrayList<Activity>();
        for (ActivityEntry activityEntry : acts) {
            UserDetailInfoDTO userInfo = getUserInfoById(activityEntry.getOrganizerId().toString());
            Activity activity = new Activity(activityEntry);
            activity.setOrganizerName(userInfo.getUserName());
            activity.setOrganizerRole(userInfo.getRole());
            activity.setOrganizerImageUrl(userInfo.getImgUrl());
            activityList.add(activity);
        }
        //根据角色 排序 校领导发布的活动优先
        Collections.sort(activityList, new Comparator<Activity>() {
            @Override
            public int compare(Activity o1, Activity o2) {
                int role1 = o1.getOrganizerRole();
                int role2 = o2.getOrganizerRole();
                role1 = Math.abs(role1 - 2);
                role2 = Math.abs(role2 - 2);
                if (role1 != role2)
                    return role1 - role2;
                return o1.getAttendCount() - o2.getAttendCount();
            }
        });
        return activityList;
    }

    /*
    *
    * 推荐活动数量
    *
    * */
    public int recommendActivityOnlySchoolCount(String schoolID, String userId) {
        ActivityDao activityDao = new ActivityDao();
        List<ActivityEntry> activityEntryList = activityDao.activityRelation2me(new ObjectId(userId));

        List<ObjectId> ids = new ArrayList<ObjectId>();
        for (ActivityEntry activityEntry : activityEntryList) {
            ids.add(activityEntry.getID());
        }
        int count = activityDao.recommendActivityOnlySchoolCount(new ObjectId(schoolID), ids);
        return count;
    }

    /*
    * 分页工具方法
    * */
    private List<ActivityDiscussDTO> fenye(List<ActivityDiscussDTO> activities, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        int size = activities.size();
        int begin = (page - 1) * pageSize;
        int end = begin + pageSize;
        if (pageSize > size) {
            return activities;
        }
        if (begin > size) {
            activities = activities.subList(0, pageSize);
            return activities;
        }
        if (end > size) {
            activities = activities.subList(begin, size);
            return activities;
        }
        activities = activities.subList(begin, end);
        return activities;
    }

    //抽取活动评论中的图片
    private List<ActivityDiscussDTO> activityPicture(String activityId) {
        ActivityDiscussDao activityDiscussDao = new ActivityDiscussDao();
        List<ActivityDiscuss> activityDiscussDTOs = activityDiscussDao.findDiscussByActId(new ObjectId(activityId));
        List<ActivityDiscussDTO> discuss = new ArrayList<ActivityDiscussDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (ActivityDiscuss activityDiscuss : activityDiscussDTOs) {
            List<String> imageList = activityDiscuss.getImageList();
            if (imageList != null && !imageList.isEmpty() && !StringUtils.isBlank(imageList.get(0))) {
                discuss.add(new ActivityDiscussDTO(activityDiscuss));
                userIds.add(activityDiscuss.getUserId());
            }
        }
        UserDao userDao = new UserDao();
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(userIds, new BasicDBObject("nm", 1).append("r", 1).append("avt", 1));
        for (ActivityDiscussDTO discussDTO : discuss) {
            String userId = discussDTO.getUserId();
            if (ObjectId.isValid(userId)) {
                UserEntry userEntry = userMap.get(new ObjectId(userId));
                discussDTO.setUserName(userEntry.getUserName());
                discussDTO.setUserImage(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
            }
        }
        return discuss;
    }

    /*
    *
    * 查找出席活动id
    *
    * */
    public List<String> findAttendIds(String id) {
        ActivityDao activityDao = new ActivityDao();
        ActivityEntry activityEntry = activityDao.findActivityById(new ObjectId(id));
        List<ObjectId> objectIdList = activityEntry.getAttendIds();
        List<String> userIds = new ArrayList<String>();
        for (ObjectId objectId : objectIdList) {
            userIds.add(objectId.toString());
        }
        return userIds;
    }

    /**
     * 删除好友圈回复以及评论
     *
     * @param actId
     * @param replyId
     */
    public void deleteActivity(ObjectId actId, ObjectId replyId, boolean hasPic) {
        activityDao.deleteDiscussById(actId, replyId, hasPic);
    }
}
