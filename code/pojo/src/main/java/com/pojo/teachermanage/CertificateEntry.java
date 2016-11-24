package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师证书信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 *  ui
 *  time获得时间
 *  name证书名称
 *  record成绩
 *  open公开
 *
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class CertificateEntry extends BaseDBObject {
    private static final long serialVersionUID = -990360172716899290L;

    public CertificateEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public CertificateEntry(ObjectId userId,ObjectId schoolId,String time,String name,String record,int open) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si",schoolId)
                .append("time", time)
                .append("name",name)
                .append("record", record)
                .append("open", open);
        setBaseEntry(dbo);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui",userId);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si",schoolId);
    }
    public String getTime() {
        return getSimpleStringValue("time");
    }
    public void setTime(String time) {
        setSimpleValue("time",time);
    }
    public String getName() {
        return getSimpleStringValue("name");
    }
    public void setName(String name) {
        setSimpleValue("name",name);
    }
    public String getRecord() {
        return getSimpleStringValue("record");
    }
    public void setRecord(String record) {
        setSimpleValue("record",record);
    }
    public int getOpen() {
        return getSimpleIntegerValue("open");
    }
    public void setOpen(int open) {
        setSimpleValue("open",open);
    }
}
