package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师成果信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 *  introduce成果说明
 *  level级别
 *  time聘任时间
 *  open公开
 *
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class ResultEntry extends BaseDBObject {
    private static final long serialVersionUID = 6825085114364256825L;

    public ResultEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }
    public ResultEntry(ObjectId userId,ObjectId schoolId,String introduce,String level,String time,int open) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si",schoolId)
                .append("itro", introduce)
                .append("level",level)
                .append("time", time)
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
    public String getIntroduce() {
        return getSimpleStringValue("itro");
    }
    public void setIntroduce(String introduce) {
        setSimpleValue("itro",introduce);
    }
    public String getLevel() {
        return getSimpleStringValue("level");
    }
    public void setLevel(String level) {
        setSimpleValue("level",level);
    }
    public String getTime() {
        return getSimpleStringValue("time");
    }
    public void setTime(String time) {
        setSimpleValue("time",time);
    }
    public int getOpen() {
        return getSimpleIntegerValue("open");
    }
    public void setOpen(int open) {
        setSimpleValue("open",open);
    }
}
