package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.HomeWorkEntry;
import com.pojo.school.StudentSubmitHomeWork;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 作业操作类
 *
 * @author fourer
 */
public class HomeWorkDao extends BaseDao {
    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addHomeWorkEntry(HomeWorkEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 学生提交作业
     *
     * @param id  作业ID
     * @param ssh 提交内容
     */
    public void updateSubmitHomeWork(ObjectId id, StudentSubmitHomeWork ssh) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("sul", ssh.getBaseEntry()))
                .append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()))
                .append(Constant.MONGO_INC, new BasicDBObject("sc", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, updateValue);
    }

    /**
     * 根据附件ID得到作业
     *
     * @param docId 附件ID
     * @return
     */
    public HomeWorkEntry findHomeworkEntryByDocId(ObjectId docId) {
        BasicDBObject query = new BasicDBObject();
        query.put("sul.df.id", docId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new HomeWorkEntry((BasicDBObject) dbo);
        }
        return null;

    }

    /**
     * 详情,不包含作业提交
     *
     * @param id 作业ID
     * @return
     */
    public HomeWorkEntry findHomeWorkEntry(ObjectId id) {

        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new HomeWorkEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 删除作业
     *
     * @param id 作业ID
     */
    public void removeHomeworkEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query);
    }

    /**
     * 根据老师和班级ID查询
     *
     * @param teacherid 必须
     * @param classId   不为null时生效
     * @return
     * @throws ResultTooManyException
     */
    public List<HomeWorkEntry> findHomeWorkEntrys(ObjectId teacherid, ObjectId classId, int skip, int limit, int type, ObjectId subjectId, String term, int contentType) throws ResultTooManyException {
        List<HomeWorkEntry> retList = new ArrayList<HomeWorkEntry>();
        BasicDBObject query = new BasicDBObject("ver", 1);
        if (null != term) {
            query.put("tm", term);
        }
        if (null != teacherid) {
            query.put("ti", teacherid);
        }
        if (null != classId && !classId.toString().equals("000000000000000000000000")) {
            query.put("cs.id", classId);
        }
        if (!subjectId.toString().equals("000000000000000000000000")) {
            query.put("sbj", subjectId);
        }
        if (type != 3) {
            query.append("ty", type);
        }
        if (contentType == 1) {
            query.put("vi", new BasicDBObject(Constant.MONGO_GT, 0));
        } else if (contentType == 2) {
            query.put("fi", new BasicDBObject(Constant.MONGO_GT, 0));
        } else if (contentType == 3) {
            query.put("ex", new BasicDBObject(Constant.MONGO_GT, 0));
        } else if (contentType == 4) {
            query.put("vo", new BasicDBObject(Constant.MONGO_GT, 0));
        }
        if (query.isEmpty())
            throw new ResultTooManyException();

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            HomeWorkEntry e;
            for (DBObject dbo : list) {
                e = new HomeWorkEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 根据教师列表获取作业，适用于校长查看全校老师的作业
     *
     * @param teacheridList 教师列表
     * @param classId       班级ID
     * @param skip          跳过条目数
     * @param limit         获取条目数
     * @return
     * @throws ResultTooManyException
     */
    public List<HomeWorkEntry> findHomeWorkEntrys(List<ObjectId> teacheridList, ObjectId classId, int skip, int limit, int type, ObjectId subjectId, String term) throws ResultTooManyException {
        List<HomeWorkEntry> retList = new ArrayList<HomeWorkEntry>();
        BasicDBObject query = new BasicDBObject("ver", 1).append("tm", term);
        query.append("ti", new BasicDBObject(Constant.MONGO_IN, teacheridList));
        if (null != classId && !classId.toString().equals("000000000000000000000000")) {
            query.append("cs.id", classId);
        }
        if (type != 3) {
            query.append("ty", type);
        }
        if (!subjectId.toString().equals("000000000000000000000000")) {
            query.put("sbj", subjectId);
        }
        if (query.isEmpty())
            throw new ResultTooManyException();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            HomeWorkEntry e;
            for (DBObject dbo : list) {
                e = new HomeWorkEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 作业列表
     *
     * @param classId   班级ID
     * @param subjectId 科目ID
     * @param skip      跳过条目数
     * @param limit     获取条目数
     * @return
     */
    public List<HomeWorkEntry> findHomeWorkEntryList(ObjectId classId, ObjectId subjectId, int skip, int limit, Integer type, String schoolYear) {
        List<HomeWorkEntry> homeWorkEntryList = new ArrayList<HomeWorkEntry>();
        BasicDBObject query = new BasicDBObject("cs.id", classId).append("tm", schoolYear);
        if (type != null) {
            query.append("ty", type);
        }
        if (subjectId != null) {
            query.append("sbj", subjectId);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            HomeWorkEntry e;
            for (DBObject dbo : list) {
                e = new HomeWorkEntry((BasicDBObject) dbo);
                homeWorkEntryList.add(e);
            }
            return homeWorkEntryList;
        }
        return null;
    }

    /**
     * 返回作业数量
     *
     * @param teacherId 教师ID
     * @param classId   班级ID
     * @return
     * @throws ResultTooManyException
     */
    public int findHomeWorkEntrysCount(ObjectId teacherId, ObjectId classId, int type, ObjectId subjectId, String term, int contentType) throws ResultTooManyException {
        BasicDBObject query = new BasicDBObject("ver", 1);
        if (null != term) {
            query.put("tm", term);
        }
        if (null != teacherId) {
            query.put("ti", teacherId);
        }
        if (null != classId) {
            query.put("cs.id", classId);
        }
        if (type != 3) {
            query.append("ty", type);
        }
        if (!subjectId.toString().equals("000000000000000000000000")) {
            query.put("sbj", subjectId);
        }
        if (contentType == 1) {
            query.put("vi", 1);
        } else if (contentType == 2) {
            query.put("fi", 1);
        } else if (contentType == 3) {
            query.put("ex", 1);
        } else if (contentType == 4) {
            query.put("vo", 1);
        }
        if (query.isEmpty())
            throw new ResultTooManyException();
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query);
    }

    /**
     * 统计班级所有老师的作业数量，适用于校长
     * author：qinagm
     *
     * @param teacherIds 教师列表
     * @param classId    班级ID
     * @return
     * @throws ResultTooManyException
     */
    public int findAllHomeWorkEntrysCount(List<ObjectId> teacherIds, ObjectId classId) throws ResultTooManyException {
        BasicDBObject query = new BasicDBObject();
        if (teacherIds.size() != 0) {
            query.put("ti", new BasicDBObject(Constant.MONGO_IN, teacherIds));
        }
        if (null != classId) {
            query.put("cs.id", classId);
        }
        if (query.isEmpty())
            throw new ResultTooManyException();
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query);
    }


    /**
     * 得到学生提交的作业
     *
     * @param id      作业ID
     * @param classId 班级ID
     * @param skip    跳过条目数
     * @param limit   获取条目数
     * @return
     */
    public List<StudentSubmitHomeWork> findStudentSubmitHomeWorks(ObjectId id, ObjectId classId, int skip, int limit, ObjectId userId) {

        List<StudentSubmitHomeWork> list = new ArrayList<StudentSubmitHomeWork>();
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject(Constant.ID, id));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("sul", 1));
        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$sul");
        DBObject matchClassDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("sul.ci", classId));
        DBObject matchStuDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("sul.si", userId));
        DBObject sortDBO = new BasicDBObject(Constant.MONGO_SORT, new BasicDBObject("sul.ti", Constant.DESC));
        DBObject skipDBO = new BasicDBObject(Constant.MONGO_SKIP, skip);
        DBObject limitDBO = new BasicDBObject(Constant.MONGO_LIMIT, limit);

        AggregationOutput output;
        try {
            if (userId == null) {
                output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, matchDBO,
                        projectDBO, unbindDBO, matchClassDBO, sortDBO, skipDBO, limitDBO);
            } else {
                output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, matchDBO,
                        projectDBO, unbindDBO, matchClassDBO, matchStuDBO, sortDBO, skipDBO, limitDBO);
            }
            Iterator<DBObject> iter = output.results().iterator();
            while (iter.hasNext()) {
                BasicDBObject dbo = (BasicDBObject) iter.next().get("sul");
                list.add(new StudentSubmitHomeWork(dbo));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 得到学生提交的作业
     *
     * @param id      作业ID
     * @param classId 班级ID
     * @return
     */
    public List<StudentSubmitHomeWork> findStudentSubmitHomeWorks(ObjectId id, ObjectId classId) {

        List<StudentSubmitHomeWork> list = new ArrayList<StudentSubmitHomeWork>();
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject(Constant.ID, id));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("sul", 1));
        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$sul");
        DBObject matchClassDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("sul.ci", classId));
        DBObject sortDBO = new BasicDBObject(Constant.MONGO_SORT, new BasicDBObject("sul.ti", Constant.DESC));

        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, matchDBO,
                    projectDBO, unbindDBO, matchClassDBO, sortDBO);
            Iterator<DBObject> iter = output.results().iterator();
            while (iter.hasNext()) {
                BasicDBObject dbo = (BasicDBObject) iter.next().get("sul");
                list.add(new StudentSubmitHomeWork(dbo));
            }

        } catch (Exception e) {
        }
        return list;
    }


    /**
     * 得到提交作业的数量
     *
     * @param id 作业ID
     * @return
     */
    public int findStudentSubmitHomeWorksCount(ObjectId id) {

        List<StudentSubmitHomeWork> list = new ArrayList<StudentSubmitHomeWork>();
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject(Constant.ID, id));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("sul", 1));
        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$sul");
        DBObject sortDBO = new BasicDBObject(Constant.MONGO_SORT, new BasicDBObject("sul.ti", Constant.DESC));
        DBObject skipDBO = new BasicDBObject(Constant.MONGO_SKIP, 0);
        DBObject limitDBO = new BasicDBObject(Constant.MONGO_LIMIT, 100000);

        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, matchDBO, projectDBO, unbindDBO, sortDBO, skipDBO, limitDBO);
            Iterator<DBObject> iter = output.results().iterator();
            while (iter.hasNext()) {
                BasicDBObject dbo = (BasicDBObject) iter.next().get("sul");
                list.add(new StudentSubmitHomeWork(dbo));
            }

        } catch (Exception e) {
        }
        return list.size();
    }


    /**
     * 查询作业访问数
     *
     * @param usIds
     * @param dslId
     * @param delId
     * @return
     */
    public int findSelHomeWorkCount(List<ObjectId> usIds, ObjectId dslId, ObjectId delId) {
        BasicDBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, usIds));
        BasicDBList dblist = new BasicDBList();
        if (dslId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_GTE, dslId)));
        }
        if (dslId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query);
    }

    /**
     * 获取统计对象上传的作业信息
     *
     * @param usIds
     * @param dslId
     * @param delId
     * @param skip
     * @param limit
     * @param fields
     * @param orderBy
     * @return
     */
    public List<HomeWorkEntry> findHomeworkUploadByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, int skip, int limit, BasicDBObject fields, String orderBy) {
        List<HomeWorkEntry> retList = new ArrayList<HomeWorkEntry>();
        BasicDBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, usIds));

        BasicDBList dblist = new BasicDBList();
        if (dslId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_GTE, dslId)));
        }
        if (delId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }

        BasicDBObject sort = null;
        if (!"".equals(orderBy)) {
            sort = new BasicDBObject(orderBy, Constant.DESC);
        } else {
            sort = new BasicDBObject(Constant.ID, Constant.DESC);
        }
        List<DBObject> list = new ArrayList<DBObject>();
        if (skip >= 0 && limit > 0) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, fields, sort, skip, limit);
        } else {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, fields, sort);
        }

        for (DBObject dbo : list) {
            retList.add(new HomeWorkEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 获取统计对象作业完成信息
     *
     * @param usIds
     * @param dsl
     * @param del
     * @param orderBy
     * @return
     */
    public List<HomeWorkEntry> findJobCompletionByParamList(List<ObjectId> usIds, long dsl, long del, String orderBy) {
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("sul.si", new BasicDBObject(Constant.MONGO_IN, usIds)));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("nm", 1).append("con", 1).append("sul", 1));
        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$sul");
        BasicDBObject searchDBO = new BasicDBObject();

        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("sul.ti", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("sul.ti", new BasicDBObject(Constant.MONGO_LTE, del)));
        }

        if (dblist.size() > 0) {
            searchDBO.append(Constant.MONGO_AND, dblist);
        }

        DBObject matchDBO1 = new BasicDBObject(Constant.MONGO_MATCH, searchDBO);

        DBObject order = new BasicDBObject();
        if (!"".equals(orderBy)) {
            order.put(orderBy, Constant.DESC);
        } else {
            order.put("sul.ti", Constant.DESC);
        }
        DBObject sortDBO = new BasicDBObject(Constant.MONGO_SORT, order);

        List<HomeWorkEntry> list = new ArrayList<HomeWorkEntry>();

        Map<ObjectId, HomeWorkEntry> map = new HashMap<ObjectId, HomeWorkEntry>();

        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, matchDBO, projectDBO, unbindDBO, matchDBO1, sortDBO);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject homeWorkEntry;
            BasicDBObject studentSubmitHomeWork;
            String name = "";
            String content = "";
            ObjectId id = null;
            while (iter.hasNext()) {
                homeWorkEntry = (BasicDBObject) iter.next();
                HomeWorkEntry temp;
                id = homeWorkEntry.getObjectId(Constant.ID);
                temp = map.get(id);

                if (temp != null) {
                    studentSubmitHomeWork = (BasicDBObject) homeWorkEntry.get("sul");
                    temp.getSubmitList().add(new StudentSubmitHomeWork(studentSubmitHomeWork));
                } else {
                    name = homeWorkEntry.getString("nm");
                    content = homeWorkEntry.getString("con");
                    studentSubmitHomeWork = (BasicDBObject) homeWorkEntry.get("sul");
                    List<StudentSubmitHomeWork> childlist = new ArrayList<StudentSubmitHomeWork>();
                    childlist.add(new StudentSubmitHomeWork(studentSubmitHomeWork));
                    temp = new HomeWorkEntry(name, content, childlist);
                    temp.setID(id);
                }
                map.put(id, temp);
            }
            for (HomeWorkEntry value : map.values()) {
                list.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 编辑作业
     *
     * @param homeWorkEntry
     */
    public void updateHomeWork(HomeWorkEntry homeWorkEntry) {
        ObjectId homeworkId = homeWorkEntry.getID();
        DBObject query = new BasicDBObject(Constant.ID, homeworkId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, homeWorkEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, updateValue);

    }

    /**
     * 批阅，更新批阅字段为1
     *
     * @param homeworkId 作业ID
     * @param studentId  学生ID
     * @param time       学生提交作业时间
     */
    public void updateHomeworkCorrectFiled(ObjectId homeworkId, ObjectId studentId, long time) {
        DBObject query = new BasicDBObject(Constant.ID, homeworkId).append("sul.si", studentId).append("sul.ti", time);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sul.$.cor", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, updateValue);
    }


    /**
     * 老师回复学生
     *
     * @param homeworkId 作业ID
     * @param studentId  学生ID
     * @param time       学生提交时间
     * @param sshwList   回复内容
     */
    public void updateTeacherReply(ObjectId homeworkId, ObjectId studentId, long time, List<StudentSubmitHomeWork> sshwList) {
        DBObject query = new BasicDBObject(Constant.ID, homeworkId).append("sul.si", studentId).append("sul.ti", time);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sul.$.hf", MongoUtils.convert(MongoUtils.fetchDBObjectList(sshwList))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, updateValue);
    }

    /**
     * 查找所有的老作业，用于转换成新作业
     *
     * @return
     */
    public List<HomeWorkEntry> findAllOldHomeworkEntry() {
        DBObject query = new BasicDBObject("ver", new BasicDBObject(Constant.MONGO_NE, 1));
        List<DBObject> list = new ArrayList<DBObject>();
        if (query != null) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS);
        }
        if (null != list && !list.isEmpty()) {
            List<HomeWorkEntry> examResultEntries = new ArrayList<HomeWorkEntry>();
            for (DBObject dbo : list) {
                HomeWorkEntry e = new HomeWorkEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }

    public List<HomeWorkEntry> findAllHomeworkEntry() {
        DBObject query = new BasicDBObject("ver", 1);
        List<DBObject> list = new ArrayList<DBObject>();
        if (query != null) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS);
        }
        if (null != list && !list.isEmpty()) {
            List<HomeWorkEntry> examResultEntries = new ArrayList<HomeWorkEntry>();
            for (DBObject dbo : list) {
                HomeWorkEntry e = new HomeWorkEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }


    /**
     * 删除学生提交的作业
     *
     * @param homeworkId
     * @param userId
     * @param time
     */
    public void deleteStuSubmitHW(ObjectId homeworkId, ObjectId userId, long time) {
        DBObject query = new BasicDBObject(Constant.ID, homeworkId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("sul", new BasicDBObject("si", userId).append("ti", time)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, updateValue);
    }

    /**
     * k6kt小助手所有作业
     *
     * @return
     */
    public List<HomeWorkEntry> findHomeWorkList() {
        List<HomeWorkEntry> retList = new ArrayList<HomeWorkEntry>();
        BasicDBObject query = new BasicDBObject("ver", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            HomeWorkEntry e;
            for (DBObject dbo : list) {
                e = new HomeWorkEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }
}

