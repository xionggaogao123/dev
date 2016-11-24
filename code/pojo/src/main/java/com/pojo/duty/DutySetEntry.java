package com.pojo.duty;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 值班设置
 * schoolId 学校
 * userId 用户
 * userIds 值班老师
 * type 填报类型
 * num  前后多少分钟
 * Created by wang_xinxin on 2016/6/29.
 */
public class DutySetEntry extends BaseDBObject {
    private static final long serialVersionUID = 6557058667923520317L;

    public DutySetEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DutySetEntry(ObjectId schoolId,ObjectId userId,List<ObjectId> userIds,int type,int num,String ip,int year,int week,String explain) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("ui", userId)
                .append("uids", MongoUtils.convert(userIds))
                .append("type", type)
                .append("num",num)
                .append("ip",ip)
                .append("year",year)
                .append("week", week)
                .append("exp",explain)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }
    public int getType() {
        return getSimpleIntegerValue("type");
    }
    public void setType(int type) {
        setSimpleValue("type",type);
    }
    public int getNum() {
        return getSimpleIntegerValue("num");
    }
    public void setNum(int num) {
        setSimpleValue("num",num);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }
    public List<ObjectId> getUserIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("uids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setUserIds(List<ObjectId> userIds) {
        setSimpleValue("uids", MongoUtils.convert(userIds));
    }

    public String getIp() {
        return getSimpleStringValue("ip");
    }
    public void setIp(String ip) {
        setSimpleValue("ip",ip);
    }
    public int getYear() {
        return getSimpleIntegerValue("year");
    }
    public void setYear(int year) {
        setSimpleValue("year",year);
    }
    public int getWeek() {
        return getSimpleIntegerValue("week");
    }
    public void setWeek(int week) {
        setSimpleValue("week",week);
    }
    public String getExplain() {
        return getSimpleStringValue("exp");
    }
    public void setExplain(String explain) {
        setSimpleValue("exp",explain);
    }

}
