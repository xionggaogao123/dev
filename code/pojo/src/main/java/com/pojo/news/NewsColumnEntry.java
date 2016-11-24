package com.pojo.news;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by qiangm on 2015/7/2.
 */
public class NewsColumnEntry extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3557591395883715056L;

	/**
     * name 栏目名称 cn
     * dir 目录 dir
     * schoolId 学校id sid
     * educationId 教育局Id eid
     */
    public NewsColumnEntry() {
        BasicDBObject basicDBObject=new BasicDBObject();
        basicDBObject.append("cn","").append("dir", "").append("sid",null).append("eid",null);
    }

    public NewsColumnEntry(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }

    public String getColumnName()
    {
        return getSimpleStringValue("cn");
    }
    public String getColumnDir(){return getSimpleStringValue("dir");}

    public ObjectId getSchoolId()
    {
        return getSimpleObjecIDValue("sid");
    }

    public ObjectId getEducationId()
    {
        return getSimpleObjecIDValue("eid");
    }

    public void setColumnName(String columnName)
    {
        setSimpleValue("cn",columnName);
    }

    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("sid",schoolId);
    }

    public void setColumnDir(String columnDir){setSimpleValue("dir",columnDir);}

    public void setEducationId(ObjectId educationId)
    {
        setSimpleValue("eid",educationId);
    }
}
