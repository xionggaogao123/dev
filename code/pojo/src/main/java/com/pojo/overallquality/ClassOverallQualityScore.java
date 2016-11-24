package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 *
 * {
 sci:分值id
 sc:分值
 }
 * Created by guojing on 2016/8/22.
 */
public class ClassOverallQualityScore extends BaseDBObject {
    private static final long serialVersionUID = 7936854568592747487L;

    public ClassOverallQualityScore(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    /**
     * 构造器
     *
     */
    public ClassOverallQualityScore(ObjectId scoreId, String type, int score)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("sci", scoreId)
                .append("ty", type)
                .append("sc", score);
        setBaseEntry(dbo);
    }

    public ObjectId getScoreId() {
        return getSimpleObjecIDValue("sci");
    }

    public void setScoreId(ObjectId scoreId) {
        setSimpleValue("sci", scoreId);
    }

    public String getType() {
        return getSimpleStringValue("ty");
    }

    public void setType(String type) {
        setSimpleValue("ty", type);
    }

    public int getScore() {
        return getSimpleIntegerValueDef("sc", 0);
    }

    public void setScore(int score) {
        setSimpleValue("sc",score);
    }

}
