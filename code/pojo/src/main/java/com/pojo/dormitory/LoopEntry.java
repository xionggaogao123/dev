package com.pojo.dormitory;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/9/12.
 */
public class LoopEntry extends BaseDBObject {

    private static final long serialVersionUID = 2649430926166800146L;

    public LoopEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public LoopEntry(String loopName,ObjectId dormitoryId,String remark) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("lnm", loopName)
                .append("did", dormitoryId)
                .append("rmk", remark)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }
    public ObjectId getDormitoryId() {
        return getSimpleObjecIDValue("did");
    }
    public void setDormitoryId(ObjectId dormitoryId) {
        setSimpleValue("did", dormitoryId);
    }
    public String getRemark() {
        return getSimpleStringValue("rmk");
    }
    public void setRemark(String remark) {
        setSimpleValue("rmk",remark);
    }
    public String getLoopName() {
        return getSimpleStringValue("lnm");
    }
    public void setLoopName(String loopName) {
        setSimpleValue("lnm",loopName);
    }
}
