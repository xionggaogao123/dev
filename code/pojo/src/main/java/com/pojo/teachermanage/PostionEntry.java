package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师行政职务
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 * ui
 *  jtime工作时间jobtime
 *  org工作单位organization
 *  pos职务position
 *  open公开
 *
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class PostionEntry extends BaseDBObject {
    private static final long serialVersionUID = -7718926967266144110L;

    public PostionEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public PostionEntry(ObjectId userId,ObjectId schoolId,String jobtime,String organization,String position,int open) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si",schoolId)
                .append("jtime", jobtime)
                .append("org",organization)
                .append("pos", position)
                .append("open", open);
        setBaseEntry(dbo);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si",schoolId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui",userId);
    }
    public String getPosition() {
        return getSimpleStringValue("pos");
    }
    public void setPosition(String position) {
        setSimpleValue("pos",position);
    }
    public String getOrganization() {
        return getSimpleStringValue("org");
    }
    public void setOrganization(String organization) {
        setSimpleValue("org",organization);
    }
    public String getJobtime() {
        return getSimpleStringValue("jtime");
    }
    public void setJobtime(String jobtime) {
        setSimpleValue("jtime",jobtime);
    }
    public int getOpen() {
        return getSimpleIntegerValue("open");
    }
    public void setOpen(int open) {
        setSimpleValue("open",open);
    }
}
