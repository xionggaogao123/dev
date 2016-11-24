package com.db.docflow;

import com.db.base.BaseDao;
import com.db.educationbureau.EducationBureauDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.docflow.CheckStateEnum;
import com.pojo.docflow.DocCheckEntry;
import com.pojo.docflow.DocFlowEntry;
import com.pojo.docflow.IdUserFilePair;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/8/17.
 */
public class DocFlowDao extends BaseDao {

    private EducationBureauDao educationBureauDao=new EducationBureauDao();
    /**
     * 根据条件统计公文总数目
     *
     * @param userId 用户id
     * @param term 学期
     * @param isHandle 0未处理 1已处理
     * @param keyWords 关键字
     * @param type 0公文 1审阅 2 我的公文
     * @param departmentIds 用户是校长和管理员时返回该校部门列表
     * @return
     */
    public int getDocFlowCount(ObjectId userId, String term, Boolean isHandle, String keyWords, int type, List<ObjectId> departmentIds) {
        BasicDBObject query = generalSql(userId, term, isHandle, keyWords, type, departmentIds);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
    }
    public int getDocFlowCountEdu(ObjectId educationId, String term, List<ObjectId> schoolIdList, String keyWords, int type) {
        BasicDBObject query = generalSqlEdu(educationId, term, schoolIdList, keyWords, type);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
    }
    //获取教育局公文
    public List<DocFlowEntry> getPublishedDocFlowListEdu(ObjectId educationId, String term, String keyWords, int page, int pageSize, List<ObjectId> schoolIds) {
        List<DocFlowEntry> docFlowEntries = new ArrayList<DocFlowEntry>();
        BasicDBObject queryBase = new BasicDBObject();
        queryBase.append("te", term);
        if (!keyWords.equals("")) {//关键词从标题中搜索
            queryBase.append("ti", MongoUtils.buildRegex(keyWords));
        }
        if (schoolIds.size() > 1) {
            queryBase.append("pub", new BasicDBObject(Constant.MONGO_IN, schoolIds));
        } else {
            queryBase.append("pub", schoolIds.get(0));
        }
        queryBase.append("st", CheckStateEnum.PUBLISH.getIndex());
        queryBase.append("did",educationId);

        List<DBObject> list;
        BasicDBObject sort = new BasicDBObject();
        sort.append(Constant.ID, -1);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, queryBase, Constant.FIELDS, sort, 0, pageSize*(page-1));
        if (list != null && !list.isEmpty()) {
            for (DBObject dbObject : list) {
                docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
            }
        }
        return docFlowEntries;
    }
    //获取学校公文---教育局使用，不考虑已读未读
    public List<DocFlowEntry> getSchoolDocFlowListEdu( String term, String keyWords, int page, int pageSize, ObjectId schoolId) {
        List<DocFlowEntry> docFlowEntries = new ArrayList<DocFlowEntry>();
        BasicDBObject queryBase = new BasicDBObject();
        queryBase.append("te", term);
        if (!keyWords.equals("")) {//关键词从标题中搜索
            queryBase.append("ti", MongoUtils.buildRegex(keyWords));
        }
        queryBase.append("si", schoolId);
        queryBase.append("st", CheckStateEnum.PUBLISH.getIndex());

        //int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
        List<DBObject> list;
        BasicDBObject sort = new BasicDBObject();
        sort.append(Constant.ID, -1);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, queryBase, Constant.FIELDS, sort, 0, pageSize*(page-1));
        if (list != null && !list.isEmpty()) {
            for (DBObject dbObject : list) {
                docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
            }
        }

        return docFlowEntries;
    }

    /**
     * 获取已经发布的公文，先获取未读的，再获取已读的
     *
     * @param userId
     * @param term
     * @param keyWords
     * @param page
     * @param pageSize
     * @return
     */
    public List<DocFlowEntry> getPublishedDocFlowList(ObjectId userId, String term, String keyWords, int page, int pageSize, List<ObjectId> departmentIds) {
        List<DocFlowEntry> docFlowEntries = new ArrayList<DocFlowEntry>();
        BasicDBObject queryBase = new BasicDBObject();
        queryBase.append("te", term);
        if (!keyWords.equals("")) {//关键词从标题中搜索
            queryBase.append("ti", MongoUtils.buildRegex(keyWords));
        }
        //ObjectId educationId=educationBureauDao.selEducationByUserId(userId).getID();
        if (departmentIds.size() == 0) {
            queryBase.append("pub", userId);
        }
        //queryBase.append("pub", userId);
        else {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("did", new BasicDBObject(Constant.MONGO_IN, departmentIds)));
            values.add(new BasicDBObject("pub", userId));
            queryBase.append(Constant.MONGO_OR, values);
        }
        queryBase.append("st", CheckStateEnum.PUBLISH.getIndex());

        BasicDBObject query = new BasicDBObject(queryBase);
        query.append("unr", userId);

        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
        List<DBObject> list;
        BasicDBObject sort = new BasicDBObject();
        sort.append(Constant.ID, -1);
        if (count <= (page - 1) * pageSize) {//已读的取pageSize个，跳过pageSize*(page-1)-count个
            query = queryBase;
            query.append("unr", new BasicDBObject(Constant.MONGO_NE, userId));
            int skip = pageSize * (page - 1) - count;
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, Constant.FIELDS, sort, skip, pageSize);
            if (list != null && !list.isEmpty()) {
                for (DBObject dbObject : list) {
                    docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
                }
            }
        } else {//未读的取count-pageSize(page-1)个，跳过pageSize(page-1)个；已读的取pageSize*page-count个，跳过0个
            int skip = pageSize * (page - 1);
            int amount = count - pageSize * (page - 1);
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, Constant.FIELDS, sort, skip, amount);
            if (list != null && !list.isEmpty()) {
                for (DBObject dbObject : list) {
                    docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
                }
            }
            if (count < page * pageSize) {
                query = queryBase;
                query.append("unr", new BasicDBObject(Constant.MONGO_NE, userId));
                amount = pageSize * page - count;
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, Constant.FIELDS, sort, 0, amount);
                if (list != null && !list.isEmpty()) {
                    for (DBObject dbObject : list) {
                        docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
                    }
                }
            }
        }
        return docFlowEntries;
    }

    /**
     * 获取我的公文，被驳回可编辑的有限获取
     *
     * @param userId 用户id
     * @param term 学期
     * @param keyWords 关键字
     * @param page 当前页
     * @param pageSize 每页数量
     * @return 公文列表
     */
    public List<DocFlowEntry> getMyDocFlowList(ObjectId userId, String term, String keyWords, int page, int pageSize) {
        List<DocFlowEntry> docFlowEntries = new ArrayList<DocFlowEntry>();
        BasicDBObject queryBase = new BasicDBObject();
        queryBase.append("te", term);
        if (!keyWords.equals("")) {//关键词从标题中搜索
            queryBase.append("ti", MongoUtils.buildRegex(keyWords));
        }
        queryBase.append("uid", userId);

        BasicDBObject query_Edit =new BasicDBObject(queryBase);//查询可编辑的
        query_Edit.append("st", CheckStateEnum.REJECT.getIndex());
        query_Edit.append("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("uid", userId).append("op", -1)));
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query_Edit);

        BasicDBObject query_Read = new BasicDBObject(queryBase);//查询只读的
        BasicDBList basicDBList1 = new BasicDBList();
        basicDBList1.add(new BasicDBObject("st", new BasicDBObject(Constant.MONGO_NE, CheckStateEnum.REJECT.getIndex())));
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_NE, userId)));
        basicDBList.add(new BasicDBObject("op", -1));
        basicDBList1.add(new BasicDBObject("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject(Constant.MONGO_AND, basicDBList))));
        query_Read.append(Constant.MONGO_OR, basicDBList1);

        List<DBObject> list;//查询结果
        if (count <= (page - 1) * pageSize) {//不可编辑的取pageSize个，跳过pageSize*(page-1)-count个
            int skip = pageSize * (page - 1) - count;
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query_Read, Constant.FIELDS, new BasicDBObject(Constant.ID, -1), skip, pageSize);
            if (list != null && !list.isEmpty()) {
                for (DBObject dbObject : list) {
                    docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
                }
            }
        } else {//可编辑的取count-pageSize(page-1)个，跳过pageSize(page-1)个；不可编辑的取pageSize*page-count个，跳过0个
            int skip = pageSize * (page - 1);
            int amount = count - pageSize * (page - 1);
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query_Edit, Constant.FIELDS, new BasicDBObject(Constant.ID, -1), skip, amount);
            if (list != null && !list.isEmpty()) {
                for (DBObject dbObject : list) {
                    docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
                }
            }
            if (count < page * pageSize) {
                skip = 0;
                amount = pageSize * page - count;
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query_Read, Constant.FIELDS, new BasicDBObject(Constant.ID, -1), skip, amount);
                if (list != null && !list.isEmpty()) {
                    for (DBObject dbObject : list) {
                        docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
                    }
                }
            }
        }
        return docFlowEntries;
    }

    /**
     * 获取已处理和未处理的公文
     *
     * @param userId   用户id
     * @param term     学期（用于选择）
     * @param isHandle 已处理1，未处理0
     * @param keyWords 关键字
     * @param page     当前页
     * @param pageSize 每页显示数
     * @return DocFlowEntry列表
     */
    public List<DocFlowEntry> getDocFlowList(ObjectId userId, String term, Boolean isHandle, String keyWords, int page, int pageSize) {
        List<DocFlowEntry> docFlowEntries = new ArrayList<DocFlowEntry>();
        BasicDBObject query = generalSql(userId, term, isHandle, keyWords, 1, new ArrayList<ObjectId>());
        BasicDBObject sort = new BasicDBObject();
        sort.append(Constant.ID, -1);
        int skip = (page - 1) * pageSize;
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, Constant.FIELDS, sort, skip, pageSize);
        if (list != null && !list.isEmpty()) {
            for (DBObject dbObject : list) {
                docFlowEntries.add(new DocFlowEntry((BasicDBObject) dbObject));
            }
        }
        return docFlowEntries;
    }

    /**
     * 根据条件生成查询语句
     *
     * @param userId   用户id
     * @param term     学期
     * @param isHandle 未处理0，已处理1
     * @param keyWords 关键词
     * @param type     0公文，1审阅，2我的公文
     * @return 查询语句
     */
    public BasicDBObject generalSql(ObjectId userId, String term, Boolean isHandle, String keyWords, int type, List<ObjectId> departmentIds) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        if (!keyWords.equals(""))//关键词从标题中搜索
        {
            query.append("ti", MongoUtils.buildRegex(keyWords));
        }
        switch (type) {
            case 0://公文:已发布的，而且本人在发布范围内
                if (departmentIds.size() > 0) {
                    BasicDBList values = new BasicDBList();
                    values.add(new BasicDBObject("did", new BasicDBObject(Constant.MONGO_IN, departmentIds)));
                    values.add(new BasicDBObject("pub", userId));
                    query.append(Constant.MONGO_OR, values);
//                    query.append("did", new BasicDBObject(Constant.MONGO_IN, departmentIds));
                } else {
                    query.append("pub", userId);
                }
                query.append("st", CheckStateEnum.PUBLISH.getIndex());
                break;
            case 1://审阅
                if (isHandle) {//已处理,本人在审阅历史中，且审阅意见不为空
                    List<Integer> stateList = new ArrayList<Integer>();
                    stateList.add(CheckStateEnum.AGREE.getIndex());
                    stateList.add(CheckStateEnum.REJECT.getIndex());
                    stateList.add(CheckStateEnum.PUBLISH.getIndex());
                    stateList.add(CheckStateEnum.ABANDON.getIndex());
                    stateList.add(CheckStateEnum.TRANSPOND.getIndex());
                    BasicDBList queryList = new BasicDBList();
                    queryList.add(new BasicDBObject("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH,
                            new BasicDBObject("uid", userId).append("op", new BasicDBObject(Constant.MONGO_IN, stateList)))));
                    //queryList.add(new BasicDBObject("st", CheckStateEnum.REVOCATE.getIndex()));
                    queryList.add(new BasicDBObject("ckl",new BasicDBObject(Constant.MONGO_ELEMATCH,
                            new BasicDBObject("uid", userId).append("st", CheckStateEnum.REVOCATE.getIndex()))));
                    query.append(Constant.MONGO_OR, queryList);
                } else {//未处理，而且本人在审阅历史中，审阅意见为-1
                    BasicDBList queryList = new BasicDBList();
                    queryList.add(new BasicDBObject("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH,
                            new BasicDBObject("uid", userId).append("op", CheckStateEnum.UNDO.getIndex()))).append("st", new BasicDBObject(Constant.MONGO_NE, CheckStateEnum.REJECT.getIndex())));

                    queryList.add(new BasicDBObject("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("uid", userId).
                            append("op", CheckStateEnum.UNDO.getIndex()))).append("st", CheckStateEnum.REJECT.getIndex()).append("uid", new BasicDBObject(Constant.MONGO_NE, userId)));//被驳回而且不是本人
                    query.append(Constant.MONGO_OR, queryList);
                }
                break;
            case 2://我的公文
                query.append("uid", userId);
                break;
        }
        return query;
    }

    /**
     * 教育局使用
     * @param educationId
     * @param term
     * @param schoolIdList
     * @param keyWords
     * @param type
     * @return
     */
    public BasicDBObject generalSqlEdu(ObjectId educationId, String term, List<ObjectId> schoolIdList, String keyWords, int type) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        if (!keyWords.equals(""))//关键词从标题中搜索
        {
            query.append("ti", MongoUtils.buildRegex(keyWords));
        }
        switch (type) {
            case 0://教育局公文
                if(schoolIdList.size()>1)
                    query.append("pub",new BasicDBObject(Constant.MONGO_IN,schoolIdList));
                else
                    query.append("pub",schoolIdList.get(0));
                query.append("did",educationId);
                query.append("st", CheckStateEnum.PUBLISH.getIndex());
                break;
            case 1://学校公文
                query.append("si",schoolIdList.get(0));
                query.append("st", CheckStateEnum.PUBLISH.getIndex());
                break;
            case 2://撰写公文
                break;
        }
        return query;
    }
    /**
     * 添加公文
     *
     * @param docFlowEntry 插入对象
     * @return true/false
     */
    public ObjectId addDocFlow(DocFlowEntry docFlowEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, docFlowEntry.getBaseEntry());
        return docFlowEntry.getID();
    }

    /**
     * 修改公文
     *
     * @param docFlowEntry 公文实体类
     * @return true/false
     */
    public boolean updateDocFlow(DocFlowEntry docFlowEntry, List<DocCheckEntry> docCheckEntries, ObjectId stateId, int type) {
        BasicDBObject query = new BasicDBObject(Constant.ID, docFlowEntry.getID());
        BasicDBObject update = new BasicDBObject();
        update.append("ti", docFlowEntry.getTitle());
        update.append("pub", docFlowEntry.getPublishList());
        update.append("unr", docFlowEntry.getUnreadList());
        update.append("con", docFlowEntry.getContent());
        List<BasicDBObject> fileList = new ArrayList<BasicDBObject>();
        for (IdUserFilePair pair : docFlowEntry.getDocFile()) {
            fileList.add(pair.getBaseEntry());
        }
        update.append("docs", fileList);
        update.append("ci", stateId);
        if (type == 2) {//被驳回后修改的，需要修改最新状态
            update.append("st", CheckStateEnum.MODIFY.getIndex());
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        List<BasicDBObject> docCheckList = new ArrayList<BasicDBObject>();
        for (DocCheckEntry docCheckEntry : docCheckEntries) {
            docCheckList.add(docCheckEntry.getBaseEntry());
        }

        updateValue.append(Constant.MONGO_PUSHALL, new BasicDBObject("ckl", docCheckList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, updateValue);
        if (type == 2) {//我的公文修改，需要删除原有记录
            DocCheckEntry docCheckEntry = getDocCheckEntry(docFlowEntry.getID(), docFlowEntry.getUserId());
            if (docCheckEntry != null) {
                update(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query,
                        new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("ckl", docCheckEntry.getBaseEntry())));
            }
        }
        return true;
    }

    /**
     * 获取当前公文最新审核DocCheckEntry
     *
     * @param docId  公文id
     * @param userId 用户id
     * @return 公文审核实体类
     */
    public DocCheckEntry getDocCheckEntry(ObjectId docId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, docId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, Constant.FIELDS);
        DocFlowEntry docFlowEntry = new DocFlowEntry((BasicDBObject) dbObject);
        List<DocCheckEntry> checkEntries = docFlowEntry.getDocCheckList();
        if (checkEntries != null && !checkEntries.isEmpty()) {
            for (DocCheckEntry docCheckEntry : checkEntries) {
                if (docCheckEntry.getOPinion() == -1 && docCheckEntry.getUserId().equals(userId)) {
                    return docCheckEntry;
                }
            }
        }
        return null;
    }

    /**
     * 获取公文详情
     *
     * @param docId 公文id
     * @return 公文实体类
     */
    public DocFlowEntry getDocDetailById(ObjectId docId) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, docId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, Constant.FIELDS);
        return new DocFlowEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取未读公文数量  userId在发布范围内也在未读范围内
     *
     * @param term   学期
     * @param userId 用户id
     * @return 未读数量
     */
    public int getUnreadCount(String term, ObjectId userId, List<ObjectId> departmentIds) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        if (departmentIds.size() > 0) {//管理员或者校长
            query.append("did", new BasicDBObject(Constant.MONGO_IN, departmentIds));
        } else {
            query.append("pub", userId);
        }
        query.append("unr", userId);
        query.append("st", CheckStateEnum.PUBLISH.getIndex());
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
    }

    /**
     * 获取未审阅的公文数量----审核列表有该人，而且审核状态是-1
     *
     * @param term   学期
     * @param userId 用户id
     * @return 未审阅数量
     */
    public int getUnCheckCount(String term, ObjectId userId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        BasicDBList queryList = new BasicDBList();
        queryList.add(new BasicDBObject("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH,
                new BasicDBObject("uid", userId).append("op", CheckStateEnum.UNDO.getIndex()))).append("st", new BasicDBObject(Constant.MONGO_NE, CheckStateEnum.REJECT.getIndex())));
        queryList.add(new BasicDBObject("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("uid", userId).
                append("op", CheckStateEnum.UNDO.getIndex()))).append("st", CheckStateEnum.REJECT.getIndex()).append("uid", new BasicDBObject(Constant.MONGO_NE, userId)));//被驳回而且不是本人
        query.append(Constant.MONGO_OR, queryList);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
    }

    /**
     * 获取可编辑的数量---作者是本人，且被拨回到自己
     *
     * @param term   学期
     * @param userId 用户id
     * @return 被驳回到自己的数量
     */
    public int getRejectCount(String term, ObjectId userId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("uid", userId);
        query.append("st", CheckStateEnum.REJECT.getIndex());
        query.append("ckl", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("uid", userId).append("op", -1)));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
    }

    /**
     * 获取公文最新审阅id
     *
     * @param docId 公文id
     * @return 公文审阅id
     */
    public ObjectId getDocCurrentState(ObjectId docId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, docId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, Constant.FIELDS);
        DocFlowEntry docFlowEntry = new DocFlowEntry((BasicDBObject) dbObject);
        List<DocCheckEntry> docCheckEntries = docFlowEntry.getDocCheckList();
        for (DocCheckEntry docCheckEntry : docCheckEntries) {
            if (docCheckEntry.getOPinion() == -1)
                return docCheckEntry.getId();
        }
        return null;
    }

    /**
     * 审阅公文
     *
     * @param docId 公文id
     * @param handleType 审阅意见
     * @param receiveId 下一位接收人，无则“”
     * @param receiveDepartmentId 下一位接收部门，无责 “”
     * @param remark 备注
     * @return true/false
     */
    public boolean checkDoc(ObjectId docId, int handleType, ObjectId receiveId, ObjectId receiveDepartmentId,
                            String remark, List<IdUserFilePair> fileList) {
        BasicDBObject query = new BasicDBObject(Constant.ID, docId);
        BasicDBObject update = new BasicDBObject();
        BasicDBObject updateValue = new BasicDBObject();
        DocCheckEntry docCheckEntry;
        ObjectId currentId = getDocCurrentState(docId);
        ObjectId newObjectId = new ObjectId();
        query.append("ckl.op", -1);
        //先写入自己的审阅意见
        if (handleType >= 0 && handleType <= 4) {
            query.append("ckl.id", currentId);
            update.append("ckl.$.id", newObjectId);
            update.append("ckl.$.op", handleType);
            update.append("ckl.$.re", remark);
            update.append("st", handleType);
            update.append("ci", newObjectId);
            updateValue.append(Constant.MONGO_SET, update);
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, updateValue);
        }
        //再加入下一位审阅人
        query = new BasicDBObject(Constant.ID, docId);
        updateValue = new BasicDBObject();
        if (receiveId != null) {
            docCheckEntry = new DocCheckEntry(new ObjectId(), receiveId, receiveDepartmentId, -1, "");
            updateValue.append(Constant.MONGO_PUSH, new BasicDBObject("ckl", docCheckEntry.getBaseEntry()));
        }
        if (fileList != null && !fileList.isEmpty()) {
            List<BasicDBObject> dbObjects = new ArrayList<BasicDBObject>();
            for (IdUserFilePair idNameValuePair : fileList) {
                dbObjects.add(idNameValuePair.getBaseEntry());
            }
            updateValue.append(Constant.MONGO_PUSHALL, new BasicDBObject("docs", dbObjects));
        }
        if (updateValue.size() > 0)
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, updateValue);
        return true;
    }

    /**
     * 撤销公文---修改公文状态
     *
     * @param docId 公文id
     * @return true/false
     */
    public boolean revDoc(ObjectId docId,DocCheckEntry docCheckEntry) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject();
        query.append(Constant.ID, docId);
        update.append(Constant.MONGO_SET, new BasicDBObject("st", 5).append("ci",docCheckEntry.getId()));
        update.append(Constant.MONGO_PUSH,new BasicDBObject("ckl",docCheckEntry.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, update);
        return true;
    }

    /**
     * 删除公文
     *
     * @param docId 公文id
     * @return true/false
     */
    public boolean delDoc(ObjectId docId) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, docId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query);
        return true;
    }

    /**
     * 更新阅读人数,即从未读列表中把当前用户去除
     * @param userId 用户id
     * @param docId 公文id
     */
    public void updateUnread(ObjectId userId, ObjectId docId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, docId);
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_PULL, new BasicDBObject("unr", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DOC_FLOW_NAME, query, updateValue);
    }
}
