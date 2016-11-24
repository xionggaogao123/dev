package com.pojo.moralculture;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 德育项目成绩信息
 * <pre>
 * </pre>
 * <pre>
 {
     pi:德育项目id
     ps:德育项目成绩
     crt:创建时间
     upt:修改时间
 }
 * </pre>
 * @author jing.guo
 */
public class MoralCultureScoreInfo extends BaseDBObject {
    private static final long serialVersionUID = 7936854568592747487L;

    public MoralCultureScoreInfo(BasicDBObject baseEntry) {
        super(baseEntry);
    }


    /**
     * 构造器
     *
     */
    public MoralCultureScoreInfo(ObjectId projectId, String projectScore, long createTime,
                   long updateTime)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("pi", projectId)
                .append("ps", projectScore)
                .append("crt", createTime)
                .append("upt", updateTime)
                ;
        setBaseEntry(dbo);
    }

    public MoralCultureScoreInfo(ObjectId projectId,
                   String projectScore,long updateTime)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("pi", projectId)
                .append("ps", projectScore)
                .append("upt", updateTime)
                ;
        setBaseEntry(dbo);
    }

    public ObjectId getProjectId() {
        return getSimpleObjecIDValue("pi");
    }

    public void setProjectId(ObjectId projectId) {
        setSimpleValue("pi", projectId);
    }

    public String getProjectScore() {
        return getSimpleStringValue("ps");
    }

    public void setProjectScore(String projectScore) {
        setSimpleValue("ps",projectScore);
    }

    public long getCreateTime() {
        return getSimpleLongValue("crt");
    }

    public void setCreateTime(long createTime) {
        setSimpleValue("crt",createTime);
    }

    public long getUpdateTime() {
        return getSimpleLongValue("upt");
    }

    public void setUpdateTime(long updateTime) {
        setSimpleValue("upt",updateTime);
    }
}
