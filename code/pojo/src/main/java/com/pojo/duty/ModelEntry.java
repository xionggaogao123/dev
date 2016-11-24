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
 * Created by wang_xinxin on 2016/7/17.
 */
public class ModelEntry extends BaseDBObject {
    private static final long serialVersionUID = -6719628044557361817L;

    public ModelEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ModelEntry(ObjectId dutyModelId,long date,int index,String num,ObjectId projectId,ObjectId dutyTimeId,List<ObjectId> userIds) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("dmid", dutyModelId)
                .append("dt",date)
                .append("idx",index)
                .append("num",num)
                .append("pid", projectId)
                .append("dtid", dutyTimeId)
                .append("uids", MongoUtils.convert(userIds))
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public int getIndex() {
        return getSimpleIntegerValue("idx");
    }
    public void setIndex(String index) {
        setSimpleValue("idx",index);
    }
    public String getNum() {
        return getSimpleStringValue("num");
    }
    public void setNum(String num) {
        setSimpleValue("num",num);
    }
    public ObjectId getDutyModelId() {
        return getSimpleObjecIDValue("dmid");
    }
    public void setDutyModelId(ObjectId dutyModelId) {
        setSimpleValue("dmid", dutyModelId);
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
    public List<ObjectId> getUserIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("uids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((ObjectId)o));
            }
        }
        return retList;
    }

    public void setUserIds(List<ObjectId> userIds) {
        setSimpleValue("uids", MongoUtils.convert(userIds));
    }
}
