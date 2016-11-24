package com.pojo.docflow;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * {
 *     "id":
 *     "uid":->userId
 *     "nm":-->fileName
 *     "v"--->fileValue
 * }
 * 用于上传文件，并附带上传者Id
 * Created by qiangm on 2015/8/25.
 */
public class IdUserFilePair extends BaseDBObject {

    private static final long serialVersionUID = -5322761195713239997L;

    public IdUserFilePair(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public IdUserFilePair(ObjectId id) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("id", id)
                .append("uid",null)
                .append("nm", null)
                .append("v", null);
        setBaseEntry(baseEntry);
    }

    public IdUserFilePair(ObjectId id,ObjectId userId,String name, Object value) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("id", id)
                .append("uid",userId)
                .append("nm", name)
                .append("v", value);
        setBaseEntry(baseEntry);
    }




    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }
    public void setId(ObjectId id) {
        setSimpleValue("id", id);
    }

    public int getIntId() {
        return getSimpleIntegerValue("id");
    }
    public void setIntId(int id) {
        setSimpleValue("id", id);
    }

    public ObjectId getUserId()
    {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId)
    {
        setSimpleValue("uid",userId);
    }


    public Object getValue() {
        return getSimpleObjectValue("v");
    }
    public void setValue(Object value) {
        setSimpleValue("v", value);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

}
