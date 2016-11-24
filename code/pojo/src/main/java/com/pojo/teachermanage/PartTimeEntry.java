package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师社会兼职信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 *  time任职时间
 *  unit任职单位
 *  position职务
 *  open公开
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class PartTimeEntry extends BaseDBObject {
    private static final long serialVersionUID = -9219757374074826406L;

    public PartTimeEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public PartTimeEntry(ObjectId userId,ObjectId schoolId,String time,String unit,String position,int open) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si",schoolId)
                .append("unit", unit)
                .append("time",time)
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
    public String getUnit() {
        return getSimpleStringValue("unit");
    }
    public void setUnit(String unit) {
        setSimpleValue("unit",unit);
    }
    public String getTime() {
        return getSimpleStringValue("time");
    }
    public void setTime(String time) {
        setSimpleValue("time",time);
    }
    public String getPosition() {
        return getSimpleStringValue("pos");
    }
    public void setPosition(String position) {
        setSimpleValue("pos",position);
    }
    public int getOpen() {
        return getSimpleIntegerValue("open");
    }
    public void setOpen(int open) {
        setSimpleValue("open",open);
    }
}
