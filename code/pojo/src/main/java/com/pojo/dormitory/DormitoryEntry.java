package com.pojo.dormitory;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 {
 nm:宿舍楼名称
 si:学校ID
 ui:创建ID
 remark:备注
 }
 * Created by wang_xinxin on 2016/9/12.
 */
public class DormitoryEntry extends BaseDBObject {

    private static final long serialVersionUID = 1184447000333984948L;

    public DormitoryEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DormitoryEntry(String dormitoryName,ObjectId schoolId,String remark) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("dnm", dormitoryName)
                .append("si", schoolId)
                .append("rmk", remark)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public String getRemark() {
        return getSimpleStringValue("rmk");
    }
    public void setRemark(String remark) {
        setSimpleValue("rmk",remark);
    }
    public String getDormitoryName() {
        return getSimpleStringValue("dnm");
    }
    public void setDormitoryName(String dormitoryName) {
        setSimpleValue("dnm",dormitoryName);
    }
}
