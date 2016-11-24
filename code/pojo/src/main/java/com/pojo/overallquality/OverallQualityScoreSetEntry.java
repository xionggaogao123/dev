package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
 * 综合素质项目分数
 * <pre>
 * collectionName:overallQualityScoreSet
 * </pre>
 * <pre>
 {
 si:学校ID
 scn:分值名称
 ty:类型
 sc:分值数
 st:是否删除
 }
 * </pre>
 * Created by guojing on 2016/8/4.
 */
public class OverallQualityScoreSetEntry extends BaseDBObject {

    private static final long serialVersionUID = 7936856358592747487L;

    public OverallQualityScoreSetEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public OverallQualityScoreSetEntry(
            ObjectId schoolId,
            String scoreName,
            String type,
            int scoreNum
    ) {
        this(
                schoolId,
                scoreName,
                type,
                scoreNum,
                DeleteState.NORMAL
        );
    }

    public OverallQualityScoreSetEntry(
            ObjectId schoolId,
            String scoreName,
            String type,
            int scoreNum,
            DeleteState ds
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("snm", scoreName)
                .append("ty", type)
                .append("sc", scoreNum)
                .append("st", ds.getState());
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public String getScoreName() {
        return getSimpleStringValue("snm");
    }

    public void setScoreName(String scoreName) {
        setSimpleValue("snm", scoreName);
    }

    public String getType() {
        return getSimpleStringValue("ty");
    }

    public void setType(String type) {
        setSimpleValue("ty", type);
    }

    public int getScoreNum() {
        return getSimpleIntegerValue("sc");
    }

    public void setScoreNum(int scoreNum) {
        setSimpleValue("sc", scoreNum);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }

    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
