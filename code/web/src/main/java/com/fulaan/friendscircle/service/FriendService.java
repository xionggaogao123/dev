package com.fulaan.friendscircle.service;

import com.db.activity.ActivityDao;
import com.db.activity.FriendApplyDao;
import com.db.activity.FriendDao;
import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.fulaan.pojo.User;
import com.mongodb.BasicDBObject;
import com.pojo.activity.Activity;
import com.pojo.activity.ActivityEntry;
import com.pojo.activity.FriendApply;
import com.pojo.activity.FriendApplyEntry;
import com.pojo.school.ClassEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ChenHao on 14-10-9.
 * <p>
 * 关于对象的存储方式：  用一张大hash  存储  key 为 wbjh-userInfo
 * hash的 key 为userInfo 的Id value为userInfo序列化后的String
 */

@Service
public class FriendService {


    private FriendDao friendDao = new FriendDao();
    private FriendApplyDao friendApplyDao = new FriendApplyDao();
    private UserDao userDao = new UserDao();
    private ClassDao classDao = new ClassDao();
    private ActivityDao activityDao = new ActivityDao();

    /*
    * 删除好友关系
    * */
    public int deleteByUserId1AndUserId2(String id1, String id2) {
        friendDao.deleteOneFriend(new ObjectId(id1), new ObjectId(id2));
        friendDao.deleteOneFriend(new ObjectId(id2), new ObjectId(id1));
        return 1;
    }


    /*
    *查找好友
    * */
    public List<UserDetailInfoDTO> findMyFriends(String id) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(id));
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }

    public List<String> findFiendIds(String id) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(id));
        List<String> ids = new ArrayList<String>();
        for (ObjectId objectId : objectIdList) {
            if (objectId != null)
                ids.add(objectId.toString());
        }
        return ids;
    }

    public List<String> findIdfFiendIds(String id) {
        List<ObjectId> objectIdList = friendDao.findMyIdfFriendIds(new ObjectId(id));
        List<String> ids = new ArrayList<String>();
        for (ObjectId objectId : objectIdList) {
            if (objectId != null)
                ids.add(objectId.toString());
        }
        return ids;
    }


    public List<Activity> findFriendsActivities(String id, Integer page, Integer pageSize) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(id));
        if (objectIdList == null || objectIdList.isEmpty()) {
            return new ArrayList<Activity>();
        }

        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 0) pageSize = 10;
        if (pageSize > 10)
            pageSize = 10;
        int begin = (page - 1) * pageSize;
        List<ActivityEntry> activityEntryList = new ActivityDao().selectActivityByUserIds(objectIdList, begin, pageSize);
        return ActivityEntry2ActivityDTO(activityEntryList);
    }

    public List<Activity> ActivityEntry2ActivityDTO(List<ActivityEntry> activityEntries) {
        List<Activity> activityList = new ArrayList<Activity>();
        for (ActivityEntry activityEntry : activityEntries) {

            if (activityEntry.getOrganizerId() != null)//为了过滤脏数据
            {
                Activity activity = new Activity(activityEntry);
                UserEntry userInfo = userDao.getUserEntry(new ObjectId(activityEntry.getOrganizerId().toString()), Constant.FIELDS);//getUserInfoById(activityEntry.getOrganizerId().toString());

                activity.setOrganizerName(userInfo.getUserName());
                activity.setOrganizerRole(userInfo.getRole());
                activity.setOrganizerImageUrl("http://7xiclj.com1.z0.glb.clouddn.com/" + userInfo.getAvatar());
                activityList.add(activity);
            }
        }
        return activityList;
    }

    public int findFriendsActivityCount(String userId) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(userId));
        if (objectIdList == null || objectIdList.isEmpty()) {
            return 0;
        }
        int count = activityDao.findFriendsActivityCount(objectIdList);
        return count;
    }

    /*
    *统计好友数量
    * */
    public int countFriend(String userId) {
        int count = friendDao.countFriend(new ObjectId(userId));
        return count;
    }

    /*
    * 判定好友关系
    * */
    public boolean isFriend(String u1, String u2) {
        return friendDao.isFriend(new ObjectId(u1), new ObjectId(u2));
    }

    /*
    *查找角色是老师的好友
    *
    * */
    public List<UserDetailInfoDTO> selectFriendIsTeacher(String userId) {
        return selectFriendByRole(userId, UserRole.TEACHER.getRole());
    }

    /*
    * 依据角色抽取不同的好友
    *
    * */
    private List<UserDetailInfoDTO> selectFriendByRole(String userId, int role) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(userId));
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        List<UserDetailInfoDTO> needEntryList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            if ((userEntry.getRole() & role) == role) {
                needEntryList.add(new UserDetailInfoDTO(userEntry));
            }
        }
        return needEntryList;
    }

    public List<UserDetailInfoDTO> selectFriendIsParent(String userId) {
        return selectFriendByRole(userId, UserRole.PARENT.getRole());
    }

    public List<UserDetailInfoDTO> selectFriendIsLeader(String userId) {
        return selectFriendByRole(userId, UserRole.HEADMASTER.getRole());
    }

    public List<UserDetailInfoDTO> selectFriendIsStudent(String userId) {
        return selectFriendByRole(userId, UserRole.STUDENT.getRole());
    }

    /**
     * 获取全部好友
     *
     * @param userId
     * @return
     */
    public List<UserDetailInfoDTO> selectAllFriend(String userId) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(userId));
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        List<UserDetailInfoDTO> needEntryList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            needEntryList.add(new UserDetailInfoDTO(userEntry));
        }
        return needEntryList;
    }

    /*
    * 添加好友申请
    *
    * */
    public void addFriendApply(FriendApply friendApply) {
        FriendApplyEntry friendApplyEntry = friendApply.exportEntry();
        friendApplyDao.insertApply(friendApplyEntry);
    }

    /*
    * 依据学校推荐好友
    *
    * */
    public List<UserDetailInfoDTO> recommendFriendBySchool(String schoolId, String userId, Integer begin, Integer pageSize) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(userId));
        List<UserEntry> userEntryList = friendDao.recommendFriendBySchool(new ObjectId(schoolId), objectIdList, begin, pageSize);

        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            userInfoDTO4WBList.add(new UserDetailInfoDTO(userEntry, 1));
        }
        return userInfoDTO4WBList;
    }

    /*
    * 推荐好友数量
    *
    * */
    public int recommendFriendBySchoolCount(String schoolId, String userId) {
        List<ObjectId> objectIdList = friendDao.findMyFriendIds(new ObjectId(userId));
        int count = friendDao.recommendFriendBySchoolCount(new ObjectId(schoolId), objectIdList);
        return count;
    }

    /**
     * 统计所有好友人数
     *
     * @param userName
     * @param schoolType
     * @param roleType
     * @param schoolId
     * @param allUserIds
     * @return
     */
    public int countAllFriends(String userName, Integer schoolType,
                               Integer roleType, String schoolId, List<ObjectId> allUserIds) {
        int total = 0;
        if (schoolType.equals(1)) {
            if (roleType.equals(-1)) {
                total = userDao.countUsers(userName);
            } else {
                total = userDao.countUsersWithRole(userName, roleType);
            }
        } else if (schoolType.equals(2)) {
            if (roleType.equals(-1)) {
                total = userDao.countUsersWithSchool(userName, new ObjectId(schoolId));
            } else {
                total = userDao.countUsersWithSchoolAndRole(userName, new ObjectId(schoolId), roleType);
            }
        } else if (schoolType.equals(3)) {
            //List<ObjectId> allUserIds=getAllUserIds(gradeIds, roleType);
            total = userDao.countUsersInGrade(userName, allUserIds);
        }
        return total;
    }

    /*
    *
    * 学校类型 1全部学校  2本校  3本年级
    * 角色类型 -1全部 1老师 4家长 0学生 对应数据库
    * */
    public List<UserDetailInfoDTO> searchUsers(String userName, Integer schoolType,
                                               Integer roleType, String schoolId, List<ObjectId> allUserIds, int page, int pageSize) {
        List<UserEntry> userEntryList = new CopyOnWriteArrayList<UserEntry>();
        if (schoolType.equals(1)) {
            if (roleType.equals(-1)) {
                userEntryList = userDao.searchUsers(userName, page, pageSize);
            } else {
                userEntryList = userDao.searchUsersWithRole(userName, roleType, page, pageSize);
            }
        } else if (schoolType.equals(2)) {
            if (roleType.equals(-1)) {
                userEntryList = userDao.searchUsersWithSchool(userName, new ObjectId(schoolId), page, pageSize);
            } else {
                userEntryList = userDao.searchUsersWithSchoolAndRole(userName, new ObjectId(schoolId), roleType, page, pageSize);
            }
        } else if (schoolType.equals(3)) {
            userEntryList = userDao.searchUsersInGrade(userName, allUserIds, page, pageSize);//载从该列表中查找符合条件的好友
        }

        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBList.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBList;
    }

    /**
     * 根据年级和角色获取用户列表
     *
     * @param gradeId
     * @param type
     * @return
     */
    public List<ObjectId> getAllUserIds(List<ObjectId> gradeId, int type) {
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject();
        query.append("gid", gradeId);
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeId);
        if (type == -1)//全部
        {
            for (ClassEntry classEntry : classEntryList) {
                userIds.addAll(classEntry.getStudents());
                userIds.addAll(classEntry.getTeachers());//老师
                List<ObjectId> studentIds = new ArrayList<ObjectId>();
                studentIds.addAll(classEntry.getStudents());
                if (classEntry.getStudents().size() != 0)
                    userIds.addAll(userDao.getParentIds(studentIds));//家长
            }
        } else if (type == 1)//学生
        {
            for (ClassEntry classEntry : classEntryList) {
                userIds.addAll(classEntry.getStudents());
            }
        } else if (type == 2)//老师
        {
            for (ClassEntry classEntry : classEntryList) {
                userIds.addAll(classEntry.getTeachers());
            }
        } else if (type == 4)//家长
        {
            List<ObjectId> studentIds = new ArrayList<ObjectId>();
            for (ClassEntry classEntry : classEntryList) {
                studentIds.addAll(classEntry.getStudents());
            }
            if (studentIds.size() > 0)
                userIds = userDao.getParentIds(studentIds);
        }
        return userIds;
    }

    /**
     * 根据用户查找年级Id
     *
     * @param userId
     * @return
     */
    public List<ObjectId> getGradeIdByUser(String userId) {
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);
        int role = new UserDetailInfoDTO(userEntry).getRole();
        if (UserRole.isStudent(role)) {
            gradeIds.add(classDao.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS).getGradeId());
        } else if (UserRole.isParent(role)) {
            ObjectId parentId = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS).getConnectIds().get(0);
            gradeIds.add(classDao.getClassEntryByStuId(parentId, Constant.FIELDS).getGradeId());
        } else {
            List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(new ObjectId(userId), Constant.FIELDS);
            for (ClassEntry classEntry : classEntryList) {
                if (classEntry.getGradeId() != null && !gradeIds.contains(classEntry.getGradeId()))
                    gradeIds.add(classEntry.getGradeId());
            }
        }
        return gradeIds;
    }

    /**
     * 获取我的好友
     *
     * @param uid
     * @return
     */
    public List<User> getFrinds(ObjectId uid) {
        List<ObjectId> friendIds = friendDao.findMyFriendIds(uid);
        HashSet h = new HashSet(friendIds);
        friendIds.clear();
        friendIds.addAll(h);

        if (friendIds.contains(uid)) {
            friendIds.remove(uid);
        }
        return get(friendIds);
    }


    /**
     * 获取我的伙伴
     *
     * @param uid
     * @return
     */
    public List<User> getParters(ObjectId uid) {
        List<ObjectId> friendIds = friendDao.findMyIdfFriendIds(uid);
        return get(friendIds);
    }

    public List<ObjectId> getObjectPartners(ObjectId uid) {
        return friendDao.findMyIdfFriendIds(uid);
    }

    public List<ObjectId> getObjectFriends(ObjectId uid) {
        return friendDao.findMyFriendIds(uid);
    }


    /**
     * 添加好友
     *
     * @param uid
     * @param fid
     */
    public void addFriend(ObjectId uid, ObjectId fid) {
        if (recordIsExist(uid)) {
            friendDao.addOneFriend(uid, fid);
            return;
        }
        List<ObjectId> fids = new ArrayList<ObjectId>();
        fids.add(fid);
        friendDao.addFriendEntry(uid, fids);
    }

    /**
     * 是否有记录存在
     *
     * @param uid
     * @return
     */
    public boolean recordIsExist(ObjectId uid) {
        return friendDao.recordIsExist(uid);
    }

    private List<User> get(List<ObjectId> uids) {
        List<User> users = new ArrayList<User>();
        for (ObjectId oid : uids) {
            UserEntry userEntry = userDao.findByObjectId(oid);
            if (userEntry != null) {
                User user = new User();
                user.setId(userEntry.getID().toString());
                user.setAvator(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                user.setUserName(userEntry.getUserName());
                user.setNickName(userEntry.getNickName());
                user.setSex(userEntry.getSex());
                user.setUserId(userEntry.getID().toString());
                users.add(user);
            }
        }
        return users;
    }


}
