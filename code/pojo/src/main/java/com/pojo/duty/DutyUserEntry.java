package com.pojo.duty;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/6.
 */
public class DutyUserEntry extends BaseDBObject {
    private static final long serialVersionUID = 2667025210058166437L;

    public DutyUserEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DutyUserEntry(ObjectId dutyId,ObjectId userId,double pay,int type,long inTime,long outTime,String ip,String content,List<LessonWare> lessonWareList) {
        super();
        List<DBObject> list = MongoUtils.fetchDBObjectList(lessonWareList);
        BasicDBObject baseEntry =new BasicDBObject()
                .append("dtid", dutyId)
                .append("ui", userId)
                .append("pay",pay)
                .append("type",type)
                .append("it", inTime)
                .append("ot", outTime)
                .append("ip", ip)
                .append("con",content)
                .append("dcl", MongoUtils.convert(list))
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }
    public String getIp() {
        return getSimpleStringValue("ip");
    }
    public void setIp(String ip) {
        setSimpleValue("ip", ip);
    }
    public long getOutTime() {
        return getSimpleLongValue("ot");
    }
    public void setOutTime(long outTime) {
        setSimpleValue("ot", outTime);
    }
    public long getInTime() {
        return getSimpleLongValue("it");
    }
    public void setInTime(long inTime) {
        setSimpleValue("it", inTime);
    }
    public double getPay() {
        return getSimpleDoubleValue("pay");
    }
    public void setPay(double pay) {
        setSimpleValue("pay", pay);
    }
    public ObjectId getDutyId() {
        return getSimpleObjecIDValue("dtid");
    }
    public void setDutyId(ObjectId dutyId) {
        setSimpleValue("dtid", dutyId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }
    public int getType(){
        return getSimpleIntegerValue("type");
    }
    public void setType(int type) {
        setSimpleValue("type",type);
    }
    public String getContent() {
        return getSimpleStringValue("con");
    }
    public void setContent(String content) {
        setSimpleValue("con",content);
    }
    public String getfilePath() {
        return getSimpleStringValue("fpath");
    }
    public void setFilePath(String filePath) {
        setSimpleValue("fpath",filePath);
    }
    public String getRealName() {
        return getSimpleStringValue("rn");
    }
    public void setRealName(String realName) {
        setSimpleValue("rn",realName);
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
}
