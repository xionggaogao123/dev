package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

import java.util.Date;

/**
 *  {
 type:形象币类型 详见ImageCurrencyType
 sc:分值
 opt:操作时间
 }
 * Created by guojing on 2016/8/22.
 */
public class StuOverallQualityInfo extends BaseDBObject {
    private static final long serialVersionUID = 7936854568592747487L;

    public StuOverallQualityInfo(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    /**
     * 构造器
     *
     */
    public StuOverallQualityInfo(int type, int score)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("ty", type)
                .append("sc", score)
                .append("opt", new Date().getTime());
        setBaseEntry(dbo);
    }

    /**
     * 构造器
     *
     */
    public StuOverallQualityInfo(int type, int score, long operateTime)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("ty", type)
                .append("sc", score)
                .append("opt", operateTime);
        setBaseEntry(dbo);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
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
