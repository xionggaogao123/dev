package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 科目配置
 * {
 * xkid:选课id---------------->xuanKeId
 * subid:学科id------------------->subjectId
 * advtm:等级考课时---------------->advanceTime
 * siptm:合格考课时-------------->simpleTime
 * type:课程类型--------------->参见ZoubanType
 * ausers:学生列表-----------------------> List<IdValuePair> advUsers
 * susers:学生列表-----------------------> List<IdValuePair> simUsers
 *
 * //新增
 * clsid :行政班id------------------->List<IdNamePair>   分组走班课用，每个分组关联不同行政班
 * clsnum:分班数---------------->classNumber
 * //新增字段分组
 * odev:单双周课程类型--------------->OddEvenType单双周学科类型(1:单周 2:双周)
 *
 * //以下字段已废弃
 * iffc:是否分层--------------->ifFengCeng 1 分层教学,0 不分层教学
 * advmax:等级考最大人数
 * advmin:等级考最小人数
 * sipmax:合格考最大人数
 * sipmin:合格考最小人数
 * explain:说明
 *
 * }
 * Created by wang_xinxin on 2015/9/21.
 */
public class SubjectConfEntry extends BaseDBObject {
    public SubjectConfEntry(){}

    public SubjectConfEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public SubjectConfEntry(ObjectId xuanKeId, ObjectId subjectId, int advanceTime, int simpleTime, int ifFengCeng, int type, String explain) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("xkid", xuanKeId)
                .append("subid", subjectId)
                .append("advtm", advanceTime)
                .append("siptm", simpleTime)
                .append("iffc", ifFengCeng)
                .append("type", type)
                .append("ausers", MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<IdValuePair>())))
                .append("susers", MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<IdValuePair>())))
                .append("advmax", 0)
                .append("advmin", 0)
                .append("sipmax", 0)
                .append("sipmin", 0)
                .append("explain", explain);
        setBaseEntry(baseEntry);

    }

    public int getAdvanceMax() {
        return getSimpleIntegerValue("advmax");
    }

    public void setAdvanceMax(int advanceMax) {
        setSimpleValue("advmax", advanceMax);
    }

    public int getAdvanceMin() {
        return getSimpleIntegerValue("advmin");
    }

    public void setAdvanceMin(int advanceMin) {
        setSimpleValue("advmin", advanceMin);
    }

    public int getSimpleMax() {
        return getSimpleIntegerValue("sipmax");
    }

    public void setSimpleMax(int simpleMax) {
        setSimpleValue("sipmax", simpleMax);
    }

    public int getSimpleMin() {
        return getSimpleIntegerValue("sipmin");
    }

    public void setSimpleMin(int simpleMin) {
        setSimpleValue("sipmin", simpleMin);
    }

    public ObjectId getXuanKeId() {
        return getSimpleObjecIDValue("xkid");
    }

    public void setXuanKeId(ObjectId xuanKeId) {
        setSimpleValue("xkid", xuanKeId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subid");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subid", subjectId);
    }

    public int getAdvanceTime() {
        return getSimpleIntegerValue("advtm");
    }

    public void setAdvanceTime(int advanceTime) {
        setSimpleValue("advtm", advanceTime);
    }

    public int getSimpleTime() {
        return getSimpleIntegerValue("siptm");
    }

    public void setSimpleTime(int simpleTime) {
        setSimpleValue("siptm", simpleTime);
    }

    public int getIfFengCeng() {
        return getSimpleIntegerValue("iffc");
    }

    public void setIfFengCeng(int ifFengCeng) {
        setSimpleValue("iffc", ifFengCeng);
    }

    public int getType() {
        return getSimpleIntegerValue("type");
    }

    public void setType(int type) {
        setSimpleValue("type", type);
    }

    public String getExplain() {
        return getSimpleStringValue("explain");
    }

    public void setExplain(String explain) {
        setSimpleValue("explain", explain);
    }

    public List<IdValuePair> getAdvUsers() {
        List<IdValuePair> retList = new ArrayList<IdValuePair>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("ausers");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new IdValuePair((BasicDBObject) o));
            }
        }
        return retList;
    }

    public void setAdvUsers(List<IdValuePair> advUsers) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(advUsers);
        setSimpleValue("ausers", MongoUtils.convert(list));
    }

    public List<IdValuePair> getSimUsers() {
        List<IdValuePair> retList = new ArrayList<IdValuePair>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("susers");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new IdValuePair((BasicDBObject) o));
            }
        }
        return retList;
    }

    public void setSimUsers(List<IdValuePair> simUsers) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(simUsers);
        setSimpleValue("susers", MongoUtils.convert(list));
    }

    public List<ObjectId> getClassInfo() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("clsid");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add((ObjectId) o);
            }
        }
        return retList;
    }

    public void setClassInfo(List<ObjectId> classIdList) {
        setSimpleValue("clsid", MongoUtils.convert(classIdList));
    }

    public int getClassNumber(){
        if (getBaseEntry().containsField("clsnum")) {
            return getSimpleIntegerValueDef("clsnum",0);
        } else {
            return 0;
        }
    }

    public void setClassNumber(int classNumber){
        setSimpleValue("clsnum",classNumber);
    }

    public int getOddEvenType(){
        return getSimpleIntegerValueDef("odev",0);
    }

    public void setOddEvenType(int oddEvenType){
        setSimpleValue("odev",oddEvenType);
    }

}
