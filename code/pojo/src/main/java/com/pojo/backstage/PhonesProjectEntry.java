package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by taoto.chan on 2018年9月20日10:05:06
 * id		主键                         ObjectId
 * projectName	项目名称
 * projectDockPeople		项目对接人
 * schoolName	学校名称
 * accessClass	使用班级
 * accessObj	使用对象
 * contactInfo	联系方式
 * address		地址
 */
public class PhonesProjectEntry extends BaseDBObject {

    public PhonesProjectEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject) dbObject);
    }

    public PhonesProjectEntry() {
    }


    //新增构造
    public PhonesProjectEntry(String projectName,
                              String projectDockPeople,
                              String schoolName,
                              String accessClass,
                              String accessObj,
                              String contactInfo,
                              String address

    ) {
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("projectName", projectName)
                .append("projectDockPeople", projectDockPeople)
                .append("schoolName", schoolName)
                .append("accessClass", accessClass)
                .append("accessObj", accessObj)
                .append("contactInfo", contactInfo)
                .append("address", address)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    //更新构造
    public PhonesProjectEntry(ObjectId id,
                              String projectName,
                              String projectDockPeople,
                              String schoolName,
                              String accessClass,
                              String accessObj,
                              String contactInfo,
                              String address

    ) {
        BasicDBObject basicDBObject = new BasicDBObject()
                .append(Constant.ID, id)
                .append("projectName", projectName)
                .append("projectDockPeople", projectDockPeople)
                .append("schoolName", schoolName)
                .append("accessClass", accessClass)
                .append("accessObj", accessObj)
                .append("contactInfo", contactInfo)
                .append("address", address)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }


    public String getProjectName() {
        return getSimpleStringValue("projectName");
    }

    public void setProjectName(String projectName) {
        setSimpleValue("projectName", projectName);
    }

    public String getProjectDockPeople() {
        return getSimpleStringValue("projectDockPeople");
    }

    public void setProjectDockPeople(String projectDockPeople) {
        setSimpleValue("projectDockPeople", projectDockPeople);
    }

    public String getSchoolName() {
        return getSimpleStringValue("schoolName");
    }

    public void setSchoolName(String schoolName) {
        setSimpleValue("schoolName", schoolName);
    }

    public String getAccessClass() {
        return getSimpleStringValue("accessClass");
    }

    public void setAccessClass(String accessClass) {
        setSimpleValue("accessClass", accessClass);
    }

    public String getAccessObj() {
        return getSimpleStringValue("accessObj");
    }

    public void setAccessObj(String accessObj) {
        setSimpleValue("accessObj", accessObj);
    }
    public String getContactInfo() {
        return getSimpleStringValue("contactInfo");
    }

    public void setContactInfo(String contactInfo) {
        setSimpleValue("contactInfo", contactInfo);
    }

    public String getAddress() {
        return getSimpleStringValue("address");
    }

    public void setAddress(String address) {
        setSimpleValue("address", address);
    }


}
