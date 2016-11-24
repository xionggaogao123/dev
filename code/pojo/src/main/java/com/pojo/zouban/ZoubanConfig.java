package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * _id:
 * sid:
 * zbm:走班模式 0无走班 1晋元 2格致 3长征 4株洲
 * Created by qiangm on 2016/3/11.
 */
public class ZoubanConfig extends BaseDBObject {
    public ZoubanConfig()
    {
        super();
    }
    public ZoubanConfig(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public ZoubanConfig(ObjectId schoolId,int mode)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("zbm", mode);
        setBaseEntry(basicDBObject);
    }
    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("sid",schoolId);
    }
    public ObjectId getSchoolId()
    {
        return getSimpleObjecIDValue("sid");
    }

    public void setZoubanMode(int mode)
    {
        setSimpleValue("zbm",mode);
    }
    public int getZoubanMode()
    {
        return getSimpleIntegerValue("zbm");
    }
}
