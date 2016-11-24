package com.pojo.indicator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 *
 * <pre>
 * {
 *  zbid:指标id
 *  ty: 1根节点 2 叶子节点
 *  zbpid:父节点
 *  zbps：[{ id :唯一性标示 }]:父节点集合
 *  lel:级别
 *  sty: 打分种类
 *  ssc: 分值
 * }
 * </pre>
 * Created by guojing on 2016/11/8.
 */
public class InterestEvaluate extends BaseDBObject {
    private static final long serialVersionUID = 7933557028492747487L;

    public InterestEvaluate(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public InterestEvaluate(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public InterestEvaluate(
            ObjectId zhiBiaoId,
            int scoreType,
            String score
    ){
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("zbid", zhiBiaoId)
                .append("sty", scoreType)
                .append("ssc", score);
        setBaseEntry(dbo);
    }

    public ObjectId getZhiBiaoId() {
        return getSimpleObjecIDValue("zbid");
    }

    public void setZhiBiaoId(ObjectId zhiBiaoId) {
        setSimpleValue("zbid", zhiBiaoId);
    }

    public int getScoreType() {
        return getSimpleIntegerValueDef("sty",0);
    }

    public void setScoreType(int scoreType) {
        setSimpleValue("sty", scoreType);
    }

    public String getScore() {
        return getSimpleStringValueDef("ssc","");
    }

    public void setScore(String score) {
        setSimpleValue("ssc", score);
    }
}
