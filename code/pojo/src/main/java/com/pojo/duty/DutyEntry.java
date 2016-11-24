package com.pojo.duty;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 值班表
 * schoolId
 * date 日期
 * project 值班项目
 * users值班人员
 * timedesc时间描述
 * index  当前一周是一年中的第几周
 * times时间段
 * pay值班薪酬
 * intime签到时间
 * outtime签退时间
 * ip值班IP地址
 * 附件
 * Created by wang_xinxin on 2016/6/28.
 */
public class DutyEntry extends BaseDBObject {
    private static final long serialVersionUID = -69617816861891783L;

    public DutyEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DutyEntry(ObjectId schoolId,long date,int index,String num,ObjectId projectId,ObjectId dutyTimeId,int year) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("dt",date)
                .append("idx",index)
                .append("num",num)
                .append("pid", projectId)
                .append("dtid", dutyTimeId)
                .append("year",year)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public int getIndex() {
        return getSimpleIntegerValue("idx");
    }
    public void setIndex(int index) {
        setSimpleValue("idx",index);
    }
    public String getNum() {
        return getSimpleStringValue("num");
    }
    public void setNum(String num) {
        setSimpleValue("num",num);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public ObjectId getDutyTimeId() {
        return getSimpleObjecIDValue("dtid");
    }
    public void setDutyTimeId(ObjectId dutyTimeId) {
        setSimpleValue("dtid", dutyTimeId);
    }
    public long getDate() {
        return getSimpleLongValue("dt");
    }
    public void setDate(long date) {
        setSimpleValue("dt", date);
    }
    public ObjectId getProjectId() {
        return getSimpleObjecIDValue("pid");
    }
    public void setProjectId(ObjectId projectId) {
        setSimpleValue("pid", projectId);
    }
    public int getYear() {
        return getSimpleIntegerValue("year");
    }
    public void setYear(int year) {
        setSimpleValue("year",year);
    }


}
