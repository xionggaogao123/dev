package com.db.user;

import com.db.base.BaseDao;
import com.db.base.SynDao;
import com.db.factory.MongoFacroty;
import com.mongodb.*;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.user.UserTag;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.regex.Pattern;


/**
 * 用户Dao ,包括学生和老师等信息
 * index:nm_chatid_cid_si_ir
 * {"nm":1,"chatid":1,"cid":1,"si":1,"ir":1}
 *
 * @author fourer
 */
public class UserDao extends BaseDao {

    private static final Logger logger = Logger.getLogger(SynDao.class);

    /**
     * 添加用户
     *
     * @param e
     * @return
     */
    public ObjectId addUserEntry(UserEntry e) {
        /**
         * 如果没有聊天账号，则_id代替
         */
        if (StringUtils.isBlank(e.getChatId())) {
            if (null == e.getID()) {
                e.setID(new ObjectId());
            }
            e.setChatId(e.getID().toString());
        }
        if (e.getAvatar().equals("")) {
            e.setAvatar("head-default-head.jpg");
        }


        if (e.getRole() == UserRole.HEADMASTER.getRole()) {
            e.setRole(UserRole.HEADMASTER.getRole() | UserRole.TEACHER.getRole());
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 更新字段值
     *
     * @param userId
     * @param field     nm和sex不允许更新
     * @param value
     * @param isNeedSyn 是否需要同步
     * @throws IllegalParamException
     */
    public void update(ObjectId userId, String field, Object value, boolean isNeedSyn) throws IllegalParamException {
        if (StringUtils.isBlank(field) || "nm".equals(field))
            throw new IllegalParamException();
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject valueDBO = new BasicDBObject(field, value);
        if (isNeedSyn) {
            valueDBO.append(Constant.FIELD_SYN, Constant.SYN_YES_NEED);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新环信标志
     *
     * @param uid
     */
    public void updateHuanXin(ObjectId uid) {
        BasicDBObject query = new BasicDBObject(Constant.ID,uid);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ieasd",1));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query,update);
    }

    /**
     * 更新多个字段值
     *
     * @param userId
     * @param pairs
     */
    public void update(ObjectId userId, FieldValuePair... pairs) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject valueDBO = new BasicDBObject();
        for (FieldValuePair pair : pairs) {
            valueDBO.append(pair.getField(), pair.getValue());
        }
        valueDBO.append(Constant.FIELD_SYN, Constant.SYN_YES_NEED);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }


    /**
     * 根据用户id查询
     *
     * @param userId
     * @return
     */
    public UserEntry getUserEntry(ObjectId userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据用户id查询
     *
     * @param userId
     * @return
     */
    public UserEntry getUserEntryByChatid(String userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("chatid", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据家长ID查询
     *
     * @param parentId
     * @return
     */
    public UserEntry getUserEntryByParentId(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject("cid", parentId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据id集合查询多个用户
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public List<UserEntry> getUserEntryList(Collection<ObjectId> ids, DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据id集合查询多个用户（分页）
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public List<UserEntry> getUserEntryListByPage(Collection<ObjectId> ids, DBObject sort, int skip, int limit, DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, sort, skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 获取后台管理的数据
     *
     * @param ids
     * @return
     */
    public int getForumInfoCount(Collection<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
    }


    /**
     * 根据id集合查询多个用户map
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap(Collection<ObjectId> ids, DBObject fields) {
        Map<ObjectId, UserEntry> retMap = new HashMap<ObjectId, UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("ir", 0);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 根据id集合查询多个用户map
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap2(Collection<ObjectId> ids, DBObject fields) {
        Map<ObjectId, UserEntry> retMap = new HashMap<ObjectId, UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 根据用户名查id
     *
     * @return
     */
    public List<ObjectId> getUserIdsByUserName(Collection<String> userNames) {

        BasicDBObject query = new BasicDBObject("nm", new BasicDBObject(Constant.MONGO_IN, userNames));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject(Constant.ID, 1));
        List<ObjectId> retList = new ArrayList<ObjectId>();
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retList.add(e.getID());
            }
        }
        return retList;
    }


    /**
     * 根据用户名查询，用于登录
     *
     * @param userName
     * @return
     */
    public List<UserEntry> searchUsers(String userName, int page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名统计总数
     *
     * @param userName
     * @return
     */
    public int countUsers(String userName) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
    }


    /**
     * 根据用户名精确查询，用于用户登录
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserByUserName(String userName) {
        BasicDBObject query = new BasicDBObject("nm", userName.toLowerCase()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                if (e.getUserName().equalsIgnoreCase(userName)) {
                    return e;
                }
            }
        }
        return null;
    }


    /**
     * 根据用户登录名精确查询，用于用户登录
     *
     * @param loginName
     * @return
     */
    public UserEntry searchUserByLoginName(String loginName) {
        BasicDBObject query = new BasicDBObject("logn", loginName.toLowerCase()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                if (e.getLoginName().equalsIgnoreCase(loginName)) {
                    return e;
                }
            }
        }
        return null;
    }


    /**
     * 根据用户手机登录，用于用户登录
     *
     * @return
     */
    public UserEntry searchUserByMobile(String mobile) {
        BasicDBObject query = new BasicDBObject("mn", mobile.toLowerCase()).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            return e;

        }
        return null;
    }

    /**
     * 更新用户手机
     *
     * @param uid
     * @param mobile
     * @return
     */
    public int updateUserMobile(ObjectId uid, String mobile) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject()
                .append("mn", mobile);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        WriteResult writeResult = update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
        return writeResult.getN();
    }


    /**
     * 根据用户邮箱，用于用户登录
     *
     * @return
     */
    public UserEntry searchUserByEmail(String email) {
        BasicDBObject query = new BasicDBObject("e", email.toLowerCase()).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);

        }
        return null;
    }


    /**
     * 查询校领导
     *
     * @param schoolId
     * @return
     */
    public List<UserEntry> getSchoolLeader(ObjectId schoolId) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("si", schoolId).append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.HEADMASTER.getRole()));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            if (UserRole.isHeadmaster(userEntry.getRole())) {
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }


    /**
     * 逻辑删除用户；
     * 此处必须是逻辑删除
     *
     * @param uid
     */
    public void logicRemoveUser(ObjectId uid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 批量逻辑删除用户
     * 此处必须是逻辑删除
     *
     * @param ids
     */
    public void removeUserLogic(Collection<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public List<UserEntry> searchUsersWithRole(String userName, Integer roleType, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("r", roleType).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名和角色统计人数
     *
     * @param userName
     * @param roleType
     * @return
     */
    public int countUsersWithRole(String userName, Integer roleType) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("r", roleType).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    public List<UserEntry> searchUsersWithSchool(String userName, ObjectId schoolId, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("si", schoolId).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名和年级查找，此处已经获取到年级内所有用户
     *
     * @param userName
     * @param usersInGrade
     * @param page
     * @return
     */
    public List<UserEntry> searchUsersInGrade(String userName, List<ObjectId> usersInGrade, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, usersInGrade)).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名和年级统计总数
     *
     * @param userName
     * @param usersInGrade
     * @return
     */
    public int countUsersInGrade(String userName, List<ObjectId> usersInGrade) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, usersInGrade)).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    /**
     * 根据用户名和学校Id统计好友总数
     *
     * @param userName
     * @param schoolId
     * @return
     */
    public int countUsersWithSchool(String userName, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("si", schoolId).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    public List<UserEntry> searchUsersWithSchoolAndRole(String userName, ObjectId schoolId, Integer roleType, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("r", roleType).append("si", schoolId).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名、学校Id、角色类型统计好友总数
     *
     * @param userName
     * @param schoolId
     * @param roleType
     * @return
     */
    public int countUsersWithSchoolAndRole(String userName, ObjectId schoolId, Integer roleType) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("r", roleType).append("si", schoolId).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    public List<UserEntry> findUserEntriesLimitRole(List<ObjectId> objectIdList, BasicDBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objectIdList)).append("r", UserRole.STUDENT.getRole()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    public void resetPwd(ObjectId id, String initPwd) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pw", initPwd));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public void updateNickNameAndSexById(ObjectId objectId, String stnickname, int sex) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nnm", stnickname).append("sex", sex));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public void updateNickNameById(ObjectId objectId, String stnickname) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nnm", stnickname));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 通过学校查询
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getUserEntryBySchoolIdList(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("r", 1));
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public List<UserEntry> getUserEntryBySchoolId(ObjectId schoolId, List<Integer> role, BasicDBObject fields, int skip, int limit) {
        BasicDBObject query = new BasicDBObject().append("ir", Constant.ZERO);
        if (schoolId != null) {
            query.append("si", schoolId);
        }
        if (role != null) {
            query.append("r", new BasicDBObject(Constant.MONGO_IN, role));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("r", 1), skip, limit);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public int countUserEntryBySchoolId(ObjectId schoolId, List<Integer> role) {
        BasicDBObject query = new BasicDBObject().append("ir", Constant.ZERO);
        if (schoolId != null) {
            query.append("si", schoolId);
        }
        if (role != null) {
            query.append("r", new BasicDBObject(Constant.MONGO_IN, role));
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);

        return count;
    }

    public List<UserEntry> getInfoByName(String field, String name) {
        BasicDBObject query = new BasicDBObject(field, name).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }

    public List<UserEntry> findEntryByName(String field, String userName) {
        return getInfoByName(field, userName);
    }

    public List<UserEntry> getUserInfoBySchoolid(ObjectId schoolID, DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("si", schoolID).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, Constant.MONGO_SORTBY_ASC);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public void updateUserGroupList(String id, IdValuePair idvalue) {
        BasicDBObject query = new BasicDBObject("chatid", id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("gli", idvalue.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public List<UserEntry> findParentEntryByStuId(ObjectId stuId) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("cid", stuId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (dbObjectList != null && dbObjectList.size() != 0) {
            for (DBObject dbObject : dbObjectList) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }

    public void deleteUserGroupList(String id, IdValuePair idvalue) {
        BasicDBObject query = new BasicDBObject("chatid", id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("gli", idvalue.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public void updateIntroduction(String introduce, ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("int", introduce));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 批量修改用户密码
     *
     * @param userIds
     * @param password
     */
    public boolean resetSelectPassword(List<ObjectId> userIds, String password) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, userIds));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pw", password));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
        return true;
    }

    /**
     * 批量修改一个班级的学生学号
     *
     * @param userEntries
     */
    public void updateStudents(List<UserEntry> userEntries) {
        for (UserEntry userEntry : userEntries) {
            BasicDBObject query = new BasicDBObject("nm", userEntry.getUserName());
            BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sn", userEntry.getStudyNum()).append("jo", userEntry.getJob()));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
        }

    }

    /**
     * 修改学号
     *
     * @param userId
     * @param stuNum
     */
    public void updateStudentNum(ObjectId userId, String stuNum) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sn", stuNum));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 修改职务
     *
     * @param userId
     * @param stuJob
     */
    public void updateStudentJob(ObjectId userId, String stuJob) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("jo", stuJob));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }


    /**
     * 通过学校查询本学校的老师(在职老师、用于薪资制表数据)
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 通过学校查询本学校的学生
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getStudentEntryBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO)
                .append("r", UserRole.STUDENT.getRole());
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, Constant.MONGO_SORTBY_ASC);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public void updSchoolHomeDate(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("shd", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);

    }

    public void updFamilyHomeDate(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("fhd", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 根据学生Id列表查找所有的家长Id
     *
     * @param studentIds
     * @return
     */
    public List<ObjectId> getParentIds(List<ObjectId> studentIds) {
        BasicDBObject query = new BasicDBObject();
        query.append("cid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userIdList.add(userEntry.getID());
        }
        return userIdList;
    }

    public List<UserEntry> getParentEntrys(List<ObjectId> studentIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject();
        query.append("cid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntries = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntries.add(userEntry);
        }
        return userEntries;
    }


    /**
     * 改方法只用于统计使用；
     *
     * @param skip
     * @param limit
     * @return
     */
    @Deprecated
    public List<UserEntry> getAllEntrys(int skip, int limit) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, new BasicDBObject("lad", new BasicDBObject(Constant.MONGO_GT, 0)), Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (dbObjectList != null && dbObjectList.size() > 0) {

            for (DBObject dbo : dbObjectList) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbo);
                retList.add(userEntry);
            }
        }

        return retList;
    }

    public List<UserEntry> findUserRoleInfoBySchoolIds(List<ObjectId> schoolIds) {
        BasicDBObject query = new BasicDBObject("si", new BasicDBObject(Constant.MONGO_IN, schoolIds)).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("r", Constant.ONE));
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }


    public List<UserEntry> findUserEntriesLimitRoleAndKeyword(List<ObjectId> objectIdList, String keyword, BasicDBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objectIdList)).append("r", UserRole.STUDENT.getRole());
        if (!StringUtils.isBlank(keyword)) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    public boolean existUserInfo(String userName, String nickName) {
        BasicDBObject query = new BasicDBObject();
        if (StringUtils.isNotBlank(userName)) {
            query.append("nm", userName);
        }
        if (StringUtils.isNotBlank(nickName)) {
            query.append("nnm", nickName);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        if (count > 0) return true;
        return false;
    }

    public UserEntry existUserInfoByQQ(String qq) {
        BasicDBObject query = new BasicDBObject();
        if (!"".equals(qq)) {
            query.append("qq", qq);
        }
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;

    }


    /**
     * 根据昵称名查找用户信息
     *
     * @param userName
     * @return
     */
    public List<UserEntry> getUserInf(String userName) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject();
        Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.CASE_INSENSITIVE);
        query.append("nnm", new BasicDBObject(Constant.MONGO_REGEX, pattern));

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<UserEntry> getEntriesByUserName(String field, String userName, int page, int pageSize, String lastId) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = getQueryCondition(field, userName);
        DB myMongo = MongoFacroty.getAppDB();
        DBCollection userCollection = myMongo.getCollection(Constant.COLLECTION_USER_NAME);
        DBCursor limit = null;
        if (page == 1) {
            limit = userCollection.find(query)
                    .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
        } else {
            if (StringUtils.isNotBlank(lastId)) {
                limit = userCollection
                        .find(new BasicDBObject(Constant.ID, new BasicDBObject(
                                Constant.MONGO_GT, new ObjectId(lastId))))
                        .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
            } else {
                limit = userCollection.find(query).skip((page - 1) * pageSize)
                        .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
            }
        }

        while (limit.hasNext()) {
            retList.add(new UserEntry((BasicDBObject) limit.next()));
        }
        return retList;
    }

    public UserEntry getJudgeByRegular(String regular) {
        BasicDBObject query = getQueryCondition("nm", regular);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        } else {
            return null;
        }
    }

    public BasicDBObject getQueryCondition(String field, String userName) {
        BasicDBObject query = new BasicDBObject();
        Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.CASE_INSENSITIVE);
        query.append(field, new BasicDBObject(Constant.MONGO_REGEX, pattern)).append("ir", Constant.ZERO);
        return query;
    }


    public int countEntriesByUserName(String field, String userName) {
        BasicDBObject query = getQueryCondition(field, userName);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
    }

    public List<UserEntry> getUserInfByAvt(String avt) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject();
        Pattern pattern = Pattern.compile("^.*" + avt + ".*$", Pattern.CASE_INSENSITIVE);
        query.append("avt", new BasicDBObject(Constant.MONGO_REGEX, pattern));

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<UserEntry> selUserEntryList(Set<ObjectId> usIds, int skip, int limit, String orderBy) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, usIds));
        BasicDBObject sort = null;
        if (!"".equals(orderBy)) {
            sort = new BasicDBObject(orderBy, Constant.DESC);
        } else {
            sort = new BasicDBObject("_id", Constant.ASC);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, sort, skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    /**
     * 更新经验值
     *
     * @param userId
     */
    public void updateExperenceValue(ObjectId userId) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("exp", 20));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新帖子数值
     */
    public void updatePostCountValue(ObjectId userId) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("psc", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新论坛积分
     *
     * @param userId
     * @param score  积分
     */
    public void updateForumScoreValue(ObjectId userId, long score) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("frs", score));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }


    public void updateForumExpAndScore(ObjectId userId, long score, long forumExperience) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("frs", score)).append(Constant.MONGO_INC, new BasicDBObject("fexp", forumExperience));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public void updateForum(ObjectId userId, long score, long forumExperience) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pti", System.currentTimeMillis()));
        updateValue.append(Constant.MONGO_INC, new BasicDBObject("frs", score)).append(Constant.MONGO_INC, new BasicDBObject("psc", 1))
                .append(Constant.MONGO_INC, new BasicDBObject("fexp", forumExperience));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新论坛积分
     *
     * @param userId
     */
    public void updateForumExperience(ObjectId userId, long exp) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("fexp", exp));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 设置论坛积分
     *
     * @param userId
     * @param exp
     */
    public void setForumExperience(ObjectId userId, long exp) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("fexp", exp));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新上次访问ip
     *
     * @param userId
     * @param interviewIP
     */
    public void updateInterviewIPValue(ObjectId userId, String interviewIP) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ivp", interviewIP));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新上次活动时间
     *
     * @param userId
     */
    public void updateInterviewTimeValue(ObjectId userId) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("iti", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }


    /**
     * 更新上次退出时间
     *
     * @param userId
     */
    public void updateQuitTimeValue(ObjectId userId) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("qti", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新上次发表时间
     *
     * @param userId
     */
    public void updateInterviewPostTimeValue(ObjectId userId) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pti", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新统计在线时间
     *
     * @param userId
     */
    public void updateStatisticTimeValue(ObjectId userId, long time) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("sti", time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 通过学校查询本学校的老师
     *
     * @param schoolIds
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolIds(Collection<ObjectId> schoolIds, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", new BasicDBObject(Constant.MONGO_IN, schoolIds))
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("_id", 1).append("nm", 1));
        List<UserEntry> userids = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userids.add(userEntry);
        }
        return userids;
    }


    /**
     * 根据条件查询用户
     *
     * @param role
     * @param noUserIds
     * @return
     */
    public List<UserEntry> getUserListByParam(int role, String userName, Set<ObjectId> noUserIds) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject();
        if (UserRole.isEducation(role)) {
            query.append("r", new BasicDBObject(Constant.MONGO_GTE, role));
        }
        if (noUserIds.size() > 0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, noUserIds));
        }
        if (StringUtils.isNotBlank(userName)) {
            // query.append("nm", MongoUtils.buildRegex(userName));
            query.append("nnm", userName);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 按发帖总数排序查询用户
     */
    public List<UserEntry> getUserListByPostCount(int pageSize, String flag) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject sort = new BasicDBObject();
        if ("1".equals(flag)) {
            sort.append("psc", Constant.DESC);
        } else if ("2".equals(flag)) {
            sort.append(Constant.ID, Constant.DESC);
        }
        //BasicDBObject fields = new BasicDBObject();
//        fields.;
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query,
                null, sort, 0, pageSize);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 只做统计用，可以删除
     *
     * @param fields
     * @return
     */
    public List<UserEntry> getUserList(String userName, BasicDBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject();

        query.append("nm", MongoUtils.buildRegex(userName));

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 只做统计用，可以删除
     *
     * @param fields
     * @return
     */
    public List<UserEntry> getUserListOp(String userName, BasicDBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject();

        query.append("nm", userName);

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据用户名获取userInfo
     *
     * @param name
     * @return
     */
    public UserEntry getUserEntryByName(String name) {
        BasicDBObject query = new BasicDBObject("nm", name);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, null);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据绑定用户名获取userInfo
     *
     * @return
     */
    public UserEntry getUserEntryByBindName(String bindName) {
        BasicDBObject query = new BasicDBObject("bnm", bindName);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, null);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    @Deprecated
    public UserEntry getUserEntryByName(String name, ObjectId sid) {
        BasicDBObject query = new BasicDBObject("nm", name).append("si", sid);

        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    @Deprecated
    public List<ObjectId> getUserForEBusiness(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("si", 1), 1, 420);
        List<ObjectId> userids = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userids.add(userEntry.getID());
        }
        return userids;
    }

    /**
     * 增加减少经验值
     *
     * @param userId
     * @param exp
     */
    public void updateExperenceValue(ObjectId userId, int exp) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("exp", exp));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * @param skip
     * @param limit
     * @return
     */
    public List<UserEntry> searchUser(String name, int jinyan, int skip, int limit) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        if (StringUtils.isNotEmpty(name)) {
            // query.append("nm", MongoUtils.buildRegex(name));

            query.append("nnm", name);
        }
        if (jinyan != 2) {
            query.append("ijy", jinyan);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, null, new BasicDBObject("_id", -1), skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;

    }


    /**
     * @param skip
     * @param limit
     * @return
     */
    @Deprecated
    public List<UserEntry> searchUserByChatid(int skip, int limit) {
        List<UserEntry> retList = new ArrayList<UserEntry>();

        BasicDBList ll = new BasicDBList();
        ll.add(new BasicDBObject("chatid", ""));

        ll.add(new BasicDBObject("chatid", null));


        BasicDBObject query = new BasicDBObject(Constant.MONGO_OR, ll);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("_id", 1).append("chatid", 1), new BasicDBObject("_id", -1), skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;

    }

    /**
     * count
     *
     * @param name
     * @return
     */
    public int searchUserCount(String name, int jinyan) {
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        if (StringUtils.isNotEmpty(name)) {
            //query.append("nm", MongoUtils.buildRegex(name));
            query.append("nnm", name);
        }
        if (jinyan != 2) {
            query.append("ijy", jinyan);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
    }

    /**
     * 通过角色查询K6KT
     *
     * @param fields
     * @return
     */
    public List<UserEntry> getK6ktEntryByRoles(BasicDBObject fields) {
//        BasicDBObject query=new BasicDBObject("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.K6KT_HELPER.getRole()))
//                .append("ir", Constant.ZERO);
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        query.append("r", UserRole.K6KT_HELPER.getRole());

        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("_id", -1));
        List<UserEntry> userlist = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userlist.add(userEntry);
        }
        return userlist;
    }

    /**
     * @param id
     * @param jinyan
     */
    public void isJinyan(ObjectId id, int jinyan) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ijy", jinyan).append("jydt", new Date().getTime()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 解除禁言
     *
     * @param id
     */
    public void updateJinYanTime(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("jydt", new BasicDBObject(Constant.MONGO_LTE, new Date().getTime() - 604800000));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ijy", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public List<ObjectId> findIdListByNickName(String userName, String nickName) {
        BasicDBObject query = new BasicDBObject();
        if (!"".equals(userName)) {
            query.append("nm", userName);
        }
        if (!"".equals(nickName)) {
            query.append("nnm", nickName);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<ObjectId> useridList = new ArrayList<ObjectId>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry.getID());
            }
        }
        return useridList;
    }

    public List<UserEntry> findUserInf(String userName, String nickName) {
        BasicDBObject query = new BasicDBObject();
        if (!"".equals(userName)) {
            query.append("nm", userName);
        }
        if (!"".equals(nickName)) {
            query.append("nnm", nickName);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<UserEntry> useridList = new ArrayList<UserEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry);
            }
        }
        return useridList;
    }

    public List<ObjectId> findIdListByUserName(String userName) {
        BasicDBObject query = new BasicDBObject("nnm", userName);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<ObjectId> useridList = new ArrayList<ObjectId>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry.getID());
            }
        }
        return useridList;
    }

    public List<UserEntry> getUserInfoList(String keyword, int postion, String schoolId, List<ObjectId> userids) {
        BasicDBObject query = new BasicDBObject("si", new ObjectId(schoolId)).append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(keyword)) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        if (postion != 0) {
            query.append("pos", postion);
        }
        if (userids != null && userids.size() != 0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, userids));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<UserEntry> useridList = new ArrayList<UserEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry);
            }
        }
        return useridList;
    }

    public void updateChat(String chatId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(chatId));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ic", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public void updateChat2(String chatId) {
        BasicDBObject query = new BasicDBObject("chatid", chatId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ic", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 获取学校下面的管理员以及校长
     *
     * @param schoolIds
     * @param fields
     * @return
     */
    public List<UserEntry> getHeadmasterBySchoolIdsList(List<ObjectId> schoolIds, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", new BasicDBObject(Constant.MONGO_IN, schoolIds)).append("ir", Constant.ZERO)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.HEADMASTER.getRole()));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public void updateUserNameAndSexById(ObjectId id, String stname, int sex) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nm", stname).append("sex", sex));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public void updateUserEmailStatusById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ems", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    //查询统计数据（注册）
    public List<UserEntry> testGet() {
        BasicDBObject query = new BasicDBObject("rt", new BasicDBObject(Constant.MONGO_GTE, 1468313113397L));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    //=========================================================新增=========================================
    public void updateRole(ObjectId uid, UserRole role) {
        BasicDBObject query = new BasicDBObject(Constant.ID, uid);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", role.getRole()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public List<UserInfoDTO> getMasters() {

        List<UserInfoDTO> userInfoDTOs = new ArrayList<UserInfoDTO>();
        BasicDBObject query = new BasicDBObject("r", UserRole.DISCUSS_SECTION_MANAGER.getRole());
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        for (DBObject dbo : list) {
            userInfoDTOs.add(new UserInfoDTO(new UserEntry(dbo)));
        }
        return userInfoDTOs;
    }

    public List<UserInfoDTO> getManagers() {

        List<UserInfoDTO> userInfoDTOs = new ArrayList<UserInfoDTO>();
        BasicDBObject query = new BasicDBObject("r", UserRole.DISCUSS_MANAGER.getRole());
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        for (DBObject dbo : list) {
            userInfoDTOs.add(new UserInfoDTO(new UserEntry(dbo)));
        }
        return userInfoDTOs;
    }


    public UserEntry findByObjectId(ObjectId id) {

        FieldValuePair fieldValuePair = new FieldValuePair(Constant.ID,id);
        return query(fieldValuePair);


//        BasicDBObject query = new BasicDBObject(Constant.ID,id);
//        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_COMMUNITY,Constant.FIELDS,query);
//        return dbo == null ? null : new UserEntry(dbo);
    }

    public UserEntry findByName(String userName) {
        BasicDBObject query = new BasicDBObject()
                .append("nm", userName.toLowerCase());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (dbo != null) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public UserEntry findByEmail(String email) {
        FieldValuePair fieldValuePair = new FieldValuePair("e", email);
        return query(fieldValuePair);
    }

    public UserEntry findByPhone(String phone) {
        FieldValuePair fieldValuePair = new FieldValuePair("mn", phone);
        return query(fieldValuePair);
    }

    public long score(ObjectId uid) {
        UserEntry entry = findByObjectId(uid);
        return entry.getForumScore();
    }

    public ObjectId insert(UserEntry user) {
        return null;
    }

    public ObjectId delete(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
        return new ObjectId();
    }

    public ObjectId delete(List<ObjectId> ids) {
        return null;
    }

    public ObjectId update(UserEntry user) {
        return null;
    }

    public boolean updateUserPermission(ObjectId uid, UserRole role) {
        DBObject query = new BasicDBObject(Constant.ID, uid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", role.getRole()));
        WriteResult writeResult = update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
        return writeResult.getUpsertedId() != null;
    }

    /**
     * 精确查询，且的概念
     *
     * @param pairs
     * @return
     */
    private UserEntry query(FieldValuePair... pairs) {
        BasicDBObject query = new BasicDBObject();
        for (FieldValuePair pair : pairs) {
            query.append(pair.getField(), pair.getValue());
        }
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (dbo != null) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public List<ObjectId> getAllUserId() {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject field = new BasicDBObject(Constant.ID, true);
        List<DBObject> ids = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, field);
        List<ObjectId> list = new ArrayList<ObjectId>();
        for (DBObject dbObject : ids) {
            list.add((ObjectId) dbObject.get("_id"));
        }
        return list;
    }

    /**
     * 更新论坛积分
     *
     * @param userId
     * @param score  积分
     */
    public void updateScoreValue(ObjectId userId, long score) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("frs", score));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    private String getCollection() {
        return Constant.COLLECTION_USER_NAME;
    }


    public void pushUserTag(ObjectId uid, UserEntry.UserTagEntry userTagEntry) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("ustg", userTagEntry.getBaseEntry()));
        update(MongoFacroty.getAppDB(), getCollection(), query, update);
    }

    public boolean tagIsExist(ObjectId uid,int code) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID,uid)
                .append("ustg.co",code);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query) == 1;
    }

    public void pullUserTag(ObjectId uid, int code) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_PULL, new BasicDBObject("ustg", new BasicDBObject("co", code)));
        update(MongoFacroty.getAppDB(), getCollection(), query, update);
    }

    /**
     * 获取 用户id
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<ObjectId> findUserIdByPage(int page, int pageSize) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject field = new BasicDBObject(Constant.ID, 1);
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, -1);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, field, orderBy, (page - 1) * pageSize, pageSize);
        List<ObjectId> list = new ArrayList<ObjectId>();
        for (DBObject dbo : dbos) {
            list.add((ObjectId) dbo.get(Constant.ID));
        }
        return list;
    }

    public int countUserAmount() {
        BasicDBObject query = new BasicDBObject();
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
    }
}
