package com.pojo.lancustom;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 11:35
 * @Description:
 */
public class MonetaryAddrEntry extends BaseDBObject {
    public MonetaryAddrEntry() {

    }

    public MonetaryAddrEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MonetaryAddrEntry(String name,
                             String telphone,
                             String area,
                             String detail,
                             ObjectId userId) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("name", name)
                .append("telphone", telphone)
                .append("area", area)
                .append("detail", detail)
                .append("uid", userId)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public MonetaryAddrEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }


    public String getName() {
        return getSimpleStringValue("name");
    }

    public void setName(String name) {
        setSimpleValue("name", name);
    }

    public String getTelphone() {
        return getSimpleStringValue("telphone");
    }

    public void setTelphone(String telphone) {
        setSimpleValue("telphone", telphone);
    }

    public String getArea() {
        return getSimpleStringValue("area");
    }

    public void setArea(String area) {
        setSimpleValue("area", area);
    }

    public String getDetail() {
        return getSimpleStringValue("detail");
    }

    public void setDetail(String detail) {
        setSimpleValue("detail", detail);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }

    public int getIsRemove() {
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove) {
        setSimpleValue("isr", isRemove);
    }
}
