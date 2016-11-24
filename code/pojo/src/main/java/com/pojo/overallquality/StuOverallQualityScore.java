package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 *
 * {
 sci:分值id
 sc:分值
 }
 * Created by guojing on 2016/8/22.
 */
public class StuOverallQualityScore extends BaseDBObject {
    private static final long serialVersionUID = 7936854568592747487L;

    public StuOverallQualityScore(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    /**
     * 构造器
     *
     */
    public StuOverallQualityScore(int type, int score)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("ty", type)
                .append("sc", score);
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

}
