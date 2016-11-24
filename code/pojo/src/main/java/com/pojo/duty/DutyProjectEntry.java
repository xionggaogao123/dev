package com.pojo.duty;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/6/29.
 */
public class DutyProjectEntry extends BaseDBObject {
    private static final long serialVersionUID = 7169266512245892316L;

    public DutyProjectEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DutyProjectEntry(ObjectId dutyId,int index,String orgId,String content) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("dtid",dutyId)
                .append("idx", index)
                .append("orgid", orgId)
                .append("con",content)
                .append("ir",Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public void setDutyId(ObjectId dutyId) {
        setSimpleValue("dtid", dutyId);
    }
    public ObjectId getDutyId() {
        return getSimpleObjecIDValue("dtid");
    }
    public int getIndex() {
        return getSimpleIntegerValue("idx");
    }
    public void setIndex(int index) {
        setSimpleValue("idx",index);
    }
    public String getOrgId() {
        return getSimpleStringValue("orgid");
    }
    public void setOrgId(String orgId) {
        setSimpleValue("orgid",orgId);
    }
    public String getContent() {
        return getSimpleStringValue("con");
    }

    public void setContent(String content) {
        setSimpleValue("con",content);
    }
}
