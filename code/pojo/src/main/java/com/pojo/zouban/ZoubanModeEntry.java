package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.NameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/13.
 *
 * 走班模式配置
 *
 * sid: schoolId        学校id
 * snm: schoolName      学校名称
 * md: mode[]           走班模式 0: 无走班 1: 3个逻辑位置模式 2: 虚拟班模式
 * gl: gradeList[]      年级列表(一个年级一种模式)
 */
public class ZoubanModeEntry extends BaseDBObject {
    public ZoubanModeEntry() {
        super();
    }

    public ZoubanModeEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }


    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public String getSchoolName() {
        return getSimpleStringValue("snm");
    }

    public void setSchoolName(String schoolName) {
        setSimpleValue("snm", schoolName);
    }

    public List<NameValuePair> getMode() {
        List<NameValuePair> mode = new ArrayList<NameValuePair>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("md");
        if (basicDBList != null) {
            for (Object o : basicDBList) {
                mode.add(new NameValuePair((BasicDBObject) o));
            }
        }

        return mode;
    }

    public void setMode(List<NameValuePair> mode) {
        setSimpleValue("md", MongoUtils.convert(MongoUtils.fetchDBObjectList(mode)));
    }

    public List<IdNameValuePair> getGradeList() {
        List<IdNameValuePair> gradeList = new ArrayList<IdNameValuePair>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("gl");
        if (basicDBList != null) {
            for (Object o : basicDBList) {
                gradeList.add(new IdNameValuePair((BasicDBObject) o));
            }
        }

        return gradeList;
    }


    public void setGradeList(List<IdNameValuePair> gradeList) {
        setSimpleValue("gl", MongoUtils.convert(MongoUtils.fetchDBObjectList(gradeList)));
    }


}
