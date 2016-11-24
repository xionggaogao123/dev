package com.pojo.moralculture;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * 德育项目管理
 * <pre>
 * collectionName:moralCultureManage
 * </pre>
 * <pre>
 {
     nm:名称
     si:学校ID
     cb：创建人id
     ct:创建时间
     ub:修改人id
     ut:修改时间
     st:是否删除
 }
 * </pre>
 * @author jing.guo
 */
public class MoralCultureManageEntry extends BaseDBObject {
    private static final long serialVersionUID = 7936859648592747487L;

    public MoralCultureManageEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MoralCultureManageEntry(
            String moralCultureName,
            ObjectId schoolId,
            ObjectId createBy
    ) {
        this(
                moralCultureName,
                schoolId,
                createBy,
                new Date().getTime(),
                createBy,
                new Date().getTime(),
                DeleteState.NORMAL
        );
    }

    public MoralCultureManageEntry(
            String moralCultureName,
            ObjectId schoolId,
            ObjectId createBy,
            long createTime,
            ObjectId updateBy,
            long updateTime,
            DeleteState ds
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", moralCultureName)
                .append("si", schoolId)
                .append("cb", createBy)
                .append("ct",createTime)
                .append("ub", updateBy)
                .append("ut", updateTime)
                .append("st", ds.getState());
        setBaseEntry(baseEntry);
    }

    public String getMoralCultureName() {
        return getSimpleStringValue("nm");
    }

    public void setMoralCultureName(String moralCultureName) {
        setSimpleValue("nm", moralCultureName);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getCreateBy() {
        return getSimpleObjecIDValue("cb");
    }

    public void setCreateBy(ObjectId createBy) {
        setSimpleValue("cb", createBy);
    }

    public long getCreateTime() {
        return getSimpleLongValue("ct");
    }

    public void setCreateTime(long createTime) {
        setSimpleValue("ct",createTime);
    }

    public ObjectId getUpdateBy() {
        return getSimpleObjecIDValue("ub");
    }

    public void setUpdateBy(ObjectId updateBy) {
        setSimpleValue("ub", updateBy);
    }

    public long getUpdateTime() {
        return getSimpleLongValue("ut");
    }

    public void setUpdateTime(long updateTime) {
        setSimpleValue("ut",updateTime);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }

    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
