package com.pojo.docflow;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * {
 * te:学期--->term
 * ti:标题--->title
 * uid:发起人Id-->userId
 * did:部门Id--->departmentId
 * pub[]:发布范围--->publishList
 * unr[]:未读人员--->unread
 * ic:是否需要审核--->ifCheck
 * ckl[]:审阅历史--->checkList
 * con：公文正文--->content
 * docs[]:附件列表--->docList
 * st:当前状态--->state 详见CheckStateEnum
 * ci:最新有效审阅id-->checkId
 * si:学校id----》schoolId
 * }
 * Created by qiangm on 2015/8/17.
 */
public class DocFlowEntry extends BaseDBObject {
    public DocFlowEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DocFlowEntry(String term, String title, ObjectId userId,
                        ObjectId departmentId, List<ObjectId> publish, List<ObjectId> unread, Integer ifCheck,
                        List<DocCheckEntry> docCheckEntries,
                        String content, List<IdUserFilePair> docs, Integer state,ObjectId checkId,ObjectId schoolId) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("te", term)
                .append("ti", title)
                .append("uid", userId)
                .append("did", departmentId)
                .append("pub", MongoUtils.convert(publish))
                .append("unr", MongoUtils.convert(unread))
                .append("ic", ifCheck)
                .append("ckl", MongoUtils.convert(MongoUtils.fetchDBObjectList(docCheckEntries)))
                .append("con", content)
                .append("docs", MongoUtils.convert(MongoUtils.fetchDBObjectList(docs)))
                .append("st", state)
                .append("ci",checkId)
                .append("si",schoolId);
        setBaseEntry(dbo);
    }

    public DocFlowEntry() {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("te", "")
                .append("ti", "")
                .append("uid", null)
                .append("did", null)
                .append("pub", new BasicDBList())
                .append("unr", new BasicDBList())
                .append("ic", 0)
                .append("ckl", new BasicDBList())
                .append("con", "")
                .append("docs", new BasicDBList())
                .append("st", 0)
                .append("ci",null)
                .append("si",null);
        setBaseEntry(dbo);
    }

    public String getTerm() {
        return getSimpleStringValue("te");
    }

    public void setTerm(String term) {
        setSimpleValue("te", term);
    }

    public String getTitle() {
        return getSimpleStringValue("ti");
    }

    public void setTitle(String title) {
        setSimpleValue("ti", title);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }

    public ObjectId getDepartmentId() {
        return getSimpleObjecIDValue("did");
    }

    public void setDepartmentId(ObjectId departmentId) {
        setSimpleValue("did", departmentId);
    }

    public List<ObjectId> getPublishList() {
        List<ObjectId> publishList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("pub");
        if (list != null && !list.isEmpty()) {
            for (Object o : list) {
                publishList.add((ObjectId) o);
            }
        }
        return publishList;
    }

    public void setPublishList(List<ObjectId> publishList) {
        setSimpleValue("pub", MongoUtils.convert(publishList));
    }

    public List<ObjectId> getUnreadList() {
        List<ObjectId> publishList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("unr");
        if (list != null && !list.isEmpty()) {
            for (Object o : list) {
                publishList.add((ObjectId) o);
            }
        }
        return publishList;
    }

    public void setUnreadList(List<ObjectId> unreadList) {
        setSimpleValue("unr", MongoUtils.convert(unreadList));
    }

    public Integer getIfCheck() {
        return getSimpleIntegerValue("ic");
    }

    public void setIfCheck(Integer ifCheck) {
        setSimpleValue("ic", ifCheck);
    }

    public List<DocCheckEntry> getDocCheckList() {
        List<DocCheckEntry> docCheckDTOs = new ArrayList<DocCheckEntry>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("ckl");
        if (list != null && !list.isEmpty()) {
            for (Object o : list) {
                docCheckDTOs.add(new DocCheckEntry((BasicDBObject) o));
            }
        }
        return docCheckDTOs;
    }

    public void setDocCheckList(List<DocCheckEntry> docCheckList) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(docCheckList);
        setSimpleValue("ckl", MongoUtils.convert(list));
    }

    public String getContent() {
        return getSimpleStringValue("con");
    }

    public void setContent(String content) {
        setSimpleValue("con", content);
    }

    public List<IdUserFilePair> getDocFile() {
        List<IdUserFilePair> retList = new ArrayList<IdUserFilePair>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("docs");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new IdUserFilePair((BasicDBObject) o));
            }
        }
        return retList;
    }

    public void setDocFile(List<IdUserFilePair> df) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(df);
        setSimpleValue("docs", MongoUtils.convert(list));
    }

    public Integer getState() {
        return getSimpleIntegerValue("st");
    }

    public void setState(Integer state) {
        setSimpleValue("st", state);
    }

    public ObjectId getCheckId()
    {
        return getSimpleObjecIDValue("ci");
    }

    public void setCheckId(ObjectId checkId)
    {
        setSimpleValue("ci",checkId);
    }

    public ObjectId getSchoolId()
    {
        if(this.getBaseEntry().containsField("si"))
            return getSimpleObjecIDValue("si");
        return new ObjectId();
    }
    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("si",schoolId);
    }

}
