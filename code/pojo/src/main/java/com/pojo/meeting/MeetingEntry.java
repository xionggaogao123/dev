package com.pojo.meeting;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * collectionName:meet
 * </pre>
 * <pre>
 * {
 * ui:用户ID
 * si:学校ID
 * nm:名称
 * ty:类型 1会务2活动
 * ot:开始时间
 * et:结束时间
 * cause:事由
 * process:议程
 * users:人员
 * cinus:签到人员
 * order:顺序
 * issue:议题
 * dcl:文档列表
 * approvalui:审批人
 * modelType:是否是模板0 1模板
 *
 * ir:是否删除 0没有删除 1已经删除
 * }
 *
 * Created by wang_xinxin on 2016/7/29.
 */
public class MeetingEntry extends BaseDBObject {
    private static final long serialVersionUID = 3445537838367189943L;
    public MeetingEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MeetingEntry(ObjectId userId, ObjectId schoolId,String name,int type,long openTime,long endTime,String cause,String process,List<ObjectId> userIds,List<String> chatIds,
                        String roomId,String order,String issue,
            List<LessonWare> lessonWareList,String approvalUserId,List<IdValuePair>  approvalUserIds,int modelType,String modelName,int status) {
        super();
        List<DBObject> list = MongoUtils.fetchDBObjectList(lessonWareList);
        BasicDBObject baseEntry =new BasicDBObject()
                .append("ui", userId)
                .append("si", schoolId)
                .append("nm", name)
                .append("mnm",modelName)
                .append("ty", type)
                .append("ot", openTime)
                .append("et", endTime)
                .append("cause", cause)
                .append("pres", process)
                .append("users", MongoUtils.convert(userIds))
                .append("chats",MongoUtils.convert(chatIds))
                .append("rid",roomId)
                .append("order", order)
                .append("issue", issue)
                .append("dcl", MongoUtils.convert(list))
                .append("apui", approvalUserId)
                .append("apuis", MongoUtils.convert(MongoUtils.fetchDBObjectList(approvalUserIds)))
                .append("modeltp", modelType)
                .append("apty",0)
                .append("st", status)
                .append("cinus",MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<IdValuePair>())))
                .append("conus",MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<IdValuePair>())))
                .append("issues",MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<IdValuePair>())))
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }
    public List<ObjectId> getUserIds() {
        return MongoUtils.getFieldObjectIDs(this, "users");
    }
    public void setUserIds(List<ObjectId> userIds) {
        setSimpleValue("users", MongoUtils.convert(userIds));
    }

    public List<String> getChatIds() {
        List<String> chatIds = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("chats");
        if(null!=list && !list.isEmpty())
        {
            chatIds =new ArrayList<String>();
            for(Object o:list)
            {
                chatIds.add(o.toString());
            }
        }
        return chatIds;
    }
    public void setChatIds(List<String> chatIds) {
        BasicDBList chats = null;
        if(chatIds!=null&& chatIds.size()>0)
        {
            chats = new BasicDBList();
            for(String cid : chatIds)
            {
                chats.add(new ObjectId(cid));
            }
        }

        setSimpleValue("chats", chats);
    }

    public List<IdValuePair> getApprovalUserIds() {
        List<IdValuePair> retList =new ArrayList<IdValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("apuis");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setApprovalUserIds(List<IdValuePair> approvalUserIds) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(approvalUserIds);
        setSimpleValue("apuis",  MongoUtils.convert(list));
    }

    public List<IdValuePair> getCheckUsers() {
        List<IdValuePair> retList =new ArrayList<IdValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("cinus");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setCheckUsers(List<IdValuePair> checkUsers) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(checkUsers);
        setSimpleValue("cinus",  MongoUtils.convert(list));
    }

    public List<IdValuePair> getCheckOutUsers() {
        List<IdValuePair> retList =new ArrayList<IdValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("conus");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setCheckOutUsers(List<IdValuePair> checkOutUsers) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(checkOutUsers);
        setSimpleValue("conus",  MongoUtils.convert(list));
    }

    public String getOrder() {
        return getSimpleStringValue("order");
    }
    public void setOrder(String order) {
        setSimpleValue("order", order);
    }
    public String getApprovalUserId() {
        return getSimpleStringValue("apui");
    }
    public void setApprovalUserId(String approvalUserId) {
        setSimpleValue("apui", approvalUserId);
    }
    public String getIssue() {
        return getSimpleStringValue("issue");
    }
    public void setIssue(String issue) {
        setSimpleValue("issue", issue);
    }
    public long getOpenTime() {
        return getSimpleLongValue("ot");
    }
    public void setOpenTime(long openTime) {
        setSimpleValue("ot", openTime);
    }
    public long getEndTime() {
        return getSimpleLongValue("et");
    }
    public void setEndTime(long endTime) {
        setSimpleValue("et", endTime);
    }
    public String getCause() {
        return getSimpleStringValue("cause");
    }
    public void setCause(String cause) {
        setSimpleValue("cause", cause);
    }
    public int getType() {
        return getSimpleIntegerValue("ty");
    }
    public void setType(int type) {
        setSimpleValue("ty",type);
    }
    public int getStatus() {
        return getSimpleIntegerValue("st");
    }
    public void setStatus(int status) {
        setSimpleValue("st",status);
    }
    public int getModelType() {
        return getSimpleIntegerValue("modeltp");
    }
    public void setModelType(int modelType) {
        setSimpleValue("modeltp",modelType);
    }
    public int getApprovalType() {
        return getSimpleIntegerValue("apty");
    }
    public void setApprovalType(int approvalType) {
        setSimpleValue("apty",approvalType);
    }
    public String getProcess() {
        return getSimpleStringValue("pres");
    }
    public void setProcess(String process) {
        setSimpleValue("pres", process);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }
    public String getModelName() {
        return getSimpleStringValue("mnm");
    }
    public void setModelName(String modelName) {
        setSimpleValue("mnm", modelName);
    }
    public String getRoomId() {
        return getSimpleStringValue("rid");
    }
    public void setRoomId(String roomId) {
        setSimpleValue("rid", roomId);
    }

    public List<LessonWare> getLessonWareList() {
        List<LessonWare> lessonWareList =new ArrayList<LessonWare>();

        BasicDBList list =(BasicDBList)getSimpleObjectValue("dcl");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                lessonWareList.add(  new LessonWare((BasicDBObject)o));
            }
        }
        return lessonWareList;
    }

    public List<BasicDBObject> getLessonWareDBOList() {
        List<BasicDBObject> lessonWareList =new ArrayList<BasicDBObject>();
        if(null!=getLessonWareList() && !getLessonWareList().isEmpty())
        {
            for(LessonWare o:getLessonWareList())
            {
                lessonWareList.add(o.getBaseEntry());
            }
        }
        return lessonWareList;
    }

    public void setLessonWareList(List<LessonWare> lessonWareList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(lessonWareList);
        setSimpleValue("dcl",  MongoUtils.convert(list));
    }

    public List<IdValuePair> getIssues() {
        List<IdValuePair> retList =new ArrayList<IdValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("issues");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setIssues(List<IdValuePair> issues) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(issues);
        setSimpleValue("issues",  MongoUtils.convert(list));
    }
}
