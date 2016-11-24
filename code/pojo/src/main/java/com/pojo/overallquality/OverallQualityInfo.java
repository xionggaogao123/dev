package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 *  {
 iti:项目ID
 sc:分值
 opt:操作时间
 }
 * Created by guojing on 2016/8/22.
 */
public class OverallQualityInfo extends BaseDBObject {
    private static final long serialVersionUID = 7936854568592747487L;

    public OverallQualityInfo(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    /**
     * 构造器
     *
     */
    public OverallQualityInfo(ObjectId itemId, int score)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("iti", itemId)
                .append("sc", score)
                .append("opt", new Date().getTime());
        setBaseEntry(dbo);
    }

    /**
     * 构造器
     *
     */
    public OverallQualityInfo(ObjectId itemId, int score, long operateTime)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("iti", itemId)
                .append("sc", score)
                .append("opt", operateTime);
        setBaseEntry(dbo);
    }

    public ObjectId getItemId() {
        return getSimpleObjecIDValue("iti");
    }

    public void setItemId(ObjectId itemId) {
        setSimpleValue("iti", itemId);
    }

    public int getScore() {
        return getSimpleIntegerValueDef("sc", 0);
    }

    public void setScore(int score) {
        setSimpleValue("sc",score);
    }

    public long getOperateTime() {
        return getSimpleLongValue("opt");
    }

    public void setOperateTime(long operateTime) {
        setSimpleValue("opt",operateTime);
    }

}
