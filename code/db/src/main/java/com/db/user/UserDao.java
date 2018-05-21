package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.pojo.app.FieldValuePair;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import org.apache.commons.lang.StringUtils;
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
        BasicDBObject query = new BasicDBObject(Constant.ID, uid);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ieasd", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 更新手机
     */
    public void updateHuanXinFromPhone(List<ObjectId> uids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,uids));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("mn","12345678900"));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 更新用户名
     */
    public void updateHuanXinFromName(ObjectId uids,String name) {
        BasicDBObject query = new BasicDBObject(Constant.ID, uids);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nm",name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
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
        return dbo == null ? null : new UserEntry(dbo);
    }

    public UserEntry getJiaUserEntry(String jiaUserId) {
        BasicDBObject query = new BasicDBObject("gugc", jiaUserId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return dbo == null ? null : new UserEntry(dbo);
    }



    public UserEntry getGenerateCodeEntry(String generateCode){
        BasicDBObject query = new BasicDBObject("gugc", generateCode);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        return dbo == null ? null : new UserEntry(dbo);
    }
    
    public UserEntry getEntryByPhone(String phone){
        BasicDBObject query = new BasicDBObject("mn", phone);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        return dbo == null ? null : new UserEntry(dbo);
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
        return dbo == null ? null : new UserEntry(dbo);
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
        for (DBObject dbo : list) {
            retList.add(new UserEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 根据id集合查询多个用户
     *
     * @return
     */
    public List<UserEntry> getUserEntryListFromDelPhone(String phone) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("mn",phone).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        for (DBObject dbo : list) {
            retList.add(new UserEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 根据id集合查询多个用户
     *
     * @return
     */
    public UserEntry getUserEntryFromUserName(String name) {
        BasicDBObject query = new BasicDBObject("nm",name).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        return dbo == null ? null : new UserEntry(dbo);
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
        for (DBObject dbo : list) {
            retList.add(new UserEntry((BasicDBObject) dbo));
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
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        for (DBObject dbo : list) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            retMap.put(e.getID(), e);
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
        for (DBObject dbo : list) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            retList.add(e.getID());
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
        BasicDBObject query = new BasicDBObject("nm", userName).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public List<UserEntry> searchUsersByUserNames(Set<String> userName) {
        BasicDBObject query = new BasicDBObject("nm", new BasicDBObject(Constant.MONGO_IN,userName)).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
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

    public int updateBindUserMobile(ObjectId uid, String mobile) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject()
                .append("bmn", mobile);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        WriteResult writeResult = update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
        return writeResult.getN();
    }

    public void clearUserMobile(String mobile) {
        BasicDBObject query = new BasicDBObject()
                .append("mn", mobile);
        BasicDBObject updateValue = new BasicDBObject()
                .append("mn", "");
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);

        BasicDBObject query2 = new BasicDBObject()
                .append("nm", mobile);
        BasicDBObject updateValue2 = new BasicDBObject()
                .append("nm", new ObjectId().toString());
        BasicDBObject update2 = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue2);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query2, update2);
    }

    public void clearUserEmail(String email) {
        BasicDBObject query = new BasicDBObject()
                .append("e", email);
        BasicDBObject updateValue = new BasicDBObject()
                .append("e", "");
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);

        BasicDBObject query2 = new BasicDBObject()
                .append("nm", email);
        BasicDBObject updateValue2 = new BasicDBObject()
                .append("nm", new ObjectId().toString());
        BasicDBObject update2 = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue2);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query2, update2);
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
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
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
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
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
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
    }


    public void resetPwd(ObjectId id, String pwd) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pw", pwd));
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

    public void updateSexById(ObjectId objectId, int sex) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sex", sex));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public void updateEmailById(ObjectId objectId, String email) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("e", email));
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

    private List<UserEntry> getInfoByName(String field, String name) {
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


    public boolean existUserInfo(String userName, String nickName) {
        BasicDBObject query = new BasicDBObject();
        if (StringUtils.isNotBlank(userName)) {
            query.append("nm", userName);
        }
        if (StringUtils.isNotBlank(nickName)) {
            query.append("nnm", nickName);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return count > 0;
    }

    public List<UserEntry> getEntriesByUserName(String field, String userName, int page, int pageSize) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = getQueryCondition(field, userName);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
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

    public void updateUserEmailStatusById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ems", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
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


    public UserEntry findByUserId(ObjectId id) {
        FieldValuePair fieldValuePair = new FieldValuePair(Constant.ID, id);
        return query(fieldValuePair);
    }

    public UserEntry findByUserName(String userName) {
        BasicDBObject query = new BasicDBObject()
                .append("nm", userName.toLowerCase());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        return dbo == null ? null : new UserEntry((BasicDBObject) dbo);
    }

    public UserEntry findByEmail(String email) {
        FieldValuePair fieldValuePair = new FieldValuePair("e", email);
        return query(fieldValuePair);
    }

    public UserEntry findByMobile(String mobile){
        FieldValuePair fieldValuePair = new FieldValuePair("mn", mobile);
        return query(fieldValuePair);
    }

    public UserEntry findByPersonalID(String personalId){
        FieldValuePair fieldValuePair = new FieldValuePair("gugc", personalId);
        return query(fieldValuePair);
    }

    public long score(ObjectId uid) {
        UserEntry entry = findByUserId(uid);
        return entry.getForumScore();
    }

    public void delete(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
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


    public void pushUserTag(ObjectId uid, UserEntry.UserTagEntry userTagEntry) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("ustg", userTagEntry.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public void pushUserTags(ObjectId uid, List<UserEntry.UserTagEntry> tags) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBList dbList = new BasicDBList();
        for (UserEntry.UserTagEntry userTagEntry : tags) {
            dbList.add(userTagEntry.getBaseEntry());
        }

        BasicDBObject clearTag = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ustg", Constant.DEFAULT_VALUE_ARRAY));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, clearTag);

        BasicDBObject update = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("ustg", new BasicDBObject(Constant.MONGO_EACH, dbList)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public boolean tagIsExist(ObjectId uid, int code) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid)
                .append("ustg.co", code);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query) == 1;
    }

    public void pullUserTag(ObjectId uid, int code) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_PULL, new BasicDBObject("ustg", new BasicDBObject("co", code)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public void addEntry(UserEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME,entry.getBaseEntry());
    }

    public int updateUserBirthDateAndSex(ObjectId uid, int sex,long birthDate,String avatar,String nickName) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject();
        if(birthDate!=-1) {
            updateValue.append("bir", birthDate);
        }
        if(StringUtils.isNotBlank(avatar)) {
            updateValue.append("avt", avatar);
        }
        if(sex!=-1) {
            updateValue.append("sex", sex);
        }
        if(StringUtils.isNotBlank(nickName)) {
            updateValue.append("nnm", nickName);
        }
        if(updateValue.size()>0) {
            BasicDBObject update = new BasicDBObject()
                    .append(Constant.MONGO_SET, updateValue);
            WriteResult writeResult = update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
            return writeResult.getN();
        }else{
            return 1;
        }

    }
    public int updateUserSex(ObjectId uid, int sex) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject();
        if(sex!=-1) {
            updateValue.append("sex", sex);
        }
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        WriteResult writeResult = update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
        return writeResult.getN();
    }
    public int updateAvater(ObjectId uid, String avatar) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject();
        if(StringUtils.isNotBlank(avatar)) {
            updateValue.append("avt", avatar);
        }
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        WriteResult writeResult = update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
        return writeResult.getN();
    }
    public int updateUserNickName(ObjectId uid, String nickName) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject();
        if(StringUtils.isNotBlank(nickName)) {
            updateValue.append("nnm", nickName);
        }
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        WriteResult writeResult = update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
        return writeResult.getN();
    }


    /**
     * 环信账户问题处理
     */
    public List<UserEntry> getUserEntries(int page,int pageSize){
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("ieasd",1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query,Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                userEntryList.add(new UserEntry(dbObject));
            }
        }
        return userEntryList;
    }


    public List<ObjectId> filterAvailableObjectIds(List<ObjectId> userIds){
        List<ObjectId> uuIds = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                UserEntry userEntry=new UserEntry(dbObject);
                uuIds.add(userEntry.getID());
            }
        }
        return uuIds;
    }
}
