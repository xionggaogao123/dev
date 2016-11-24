package com.pojo.WebSpiderSchool;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * _id
 * sn:schoolName
 * st:schoolType
 * pr:province
 * ci:city
 * co:country
 * icon:schoolIcon
 * tn:tyeacherNum;
 * cn:classNum
 * sid:schoolId(k6kt中的)
 * test:0/1是否是试点校 0不是，1是
 * Created by qiangm on 2016/3/16.
 */
public class WebSpiderSchool extends BaseDBObject {
    public WebSpiderSchool()
    {
        super();
    }
    public WebSpiderSchool(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public WebSpiderSchool(String schoolName,int type,ObjectId province,ObjectId city,ObjectId country,String icon,
            int teacherNum,int classNum)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sn",schoolName)
                .append("st",type)
                .append("pr",province)
                .append("ci", city)
                .append("co",country)
                .append("icon",icon)
                .append("tn",teacherNum)
                .append("cn",classNum)
                .append("sid",null)
                .append("test",0);
        setBaseEntry(basicDBObject);
    }
    public WebSpiderSchool(String schoolName,int type,ObjectId province,ObjectId city,ObjectId country,String icon,
                           int teacherNum,int classNum,int test)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sn", schoolName)
                .append("st",type)
                .append("pr",province)
                .append("ci", city)
                .append("co",country)
                .append("icon",icon)
                .append("tn",teacherNum)
                .append("cn",classNum)
                .append("sid", null)
                .append("test",test);
        setBaseEntry(basicDBObject);
    }
    public WebSpiderSchool(String schoolName,int type,ObjectId province,ObjectId city,ObjectId country,String icon,
                           int teacherNum,int classNum,ObjectId schoolId)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sn",schoolName)
                .append("st", type)
                .append("pr",province)
                .append("ci", city)
                .append("co",country)
                .append("icon",icon)
                .append("tn",teacherNum)
                .append("cn",classNum)
                .append("sid",schoolId)
                .append("test",0);
        setBaseEntry(basicDBObject);
    }
    public void setSchoolName(String schoolName)
    {
        setSimpleValue("sn",schoolName);
    }
    public String getSchoolName()
    {
        return getSimpleStringValue("sn");
    }

    public void setSchoolType(int type)
    {
        setSimpleValue("st",type);
    }
    public int getSchoolType()
    {
        return getSimpleIntegerValue("st");
    }
    public void setProvince(ObjectId province)
    {
        setSimpleValue("pr",province);
    }
    public ObjectId getProvince()
    {
        return getSimpleObjecIDValue("pr");
    }
    public void setCity(ObjectId city)
    {
        setSimpleValue("ci",city);
    }
    public ObjectId getCity()
    {
        return getSimpleObjecIDValue("ci");
    }

    public void setCountry(ObjectId country)
    {
        setSimpleValue("co",country);
    }
    public ObjectId getCountry()
    {
        return getSimpleObjecIDValue("co");
    }

    public void setIcon(String icon)
    {
        setSimpleValue("icon",icon);
    }
    public String getIcon()
    {
        return getSimpleStringValue("icon");
    }

    public void setTeachewrNum(int num)
    {
        setSimpleValue("tn",num);
    }
    public int getTeacherNum()
    {
        return getSimpleIntegerValue("tn");
    }
    public void setClassNum(int num)
    {
        setSimpleValue("cn",num);
    }
    public int getClassNum()
    {
        return getSimpleIntegerValue("cn");
    }
    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("sid",schoolId);
    }
    public ObjectId getSchoolId()
    {
        if(this.getBaseEntry().containsField("sid")) {
            return getSimpleObjecIDValue("sid");
        }
        else
        {
            return null;
        }
    }
    public void setTest(int test)
    {
        setSimpleValue("test",test);
    }
    public int getTest()
    {
        if(this.getBaseEntry().containsField("test")) {
            return getSimpleIntegerValue("test");
        }
        else
        {
            return 0;
        }
    }
}
