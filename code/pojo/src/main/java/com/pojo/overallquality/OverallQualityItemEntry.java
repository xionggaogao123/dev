package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
 * 综合素质评比项目
 * <pre>
 * collectionName:overallQualityItem
 * </pre>
 * <pre>
 {
 si:学校ID
 nm:项目名称
 ssi:分值id
 st:是否删除
 }
 * Created by guojing on 2016/8/4.
 */
public class OverallQualityItemEntry extends BaseDBObject {

    private static final long serialVersionUID = 7936856358592747487L;

    public OverallQualityItemEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public OverallQualityItemEntry(
            ObjectId schoolId,
            String itemName,
            ObjectId scoreSetId
    ) {
        this(
                schoolId,
                itemName,
                scoreSetId,
                DeleteState.NORMAL
        );
    }

    public OverallQualityItemEntry(
            ObjectId schoolId,
            String itemName,
            ObjectId scoreSetId,
            DeleteState ds
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("nm", itemName)
                .append("ssi", scoreSetId)
                .append("st", ds.getState());
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public String getItemName() {
        return getSimpleStringValue("nm");
    }

    public void setItemName(String itemName) {
        setSimpleValue("nm", itemName);
    }

    public ObjectId getScoreSetId() {
        return getSimpleObjecIDValue("ssi");
    }

    public void setScoreSetId(ObjectId scoreSetId) {
        setSimpleValue("ssi", scoreSetId);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }

    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
