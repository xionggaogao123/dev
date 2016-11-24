package com.pojo.educationbureau;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.examregional.EducationSubject;
import com.pojo.school.Grade;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * /**
 * 教育局
 * <pre>
 * collectionName:educationbureau
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  pr:省份
 *  ci:城市
 *  co:区县
 *  scrd:学校入驻时间
 *  uis[]:教育局用户
 *  sis[]:学校
 *  logo:教育局logo
 *  crt:创建时间
 *  upt:修改时间
 *  grs: 参见Grade
 [
 {
 *  gid:年级ID
 *  nm:年级名称
 *  ty:年级类型
 *  ld:年级组长
 }
 .....
 ]
 subs:学校科目列表; EducationSubject
 [
 {
 *		 si:科目ID
 *       nm:名字
 *       gis:年级ID
 *       [
 *			
 *       ]
 *     }
 ]
 * oc:是否开通云平台 0:关闭，1：开通
 * st:是否删除
 * }
 * </pre>
 * Created by guojing on 2015/5/14.
 */
public class EducationBureauEntry extends BaseDBObject {
    private static final long serialVersionUID = 7105040754235610727L;

    public EducationBureauEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public EducationBureauEntry(String educationName, String province, String city, String county, long createTime, long updateTime) {
        this(
             educationName,
             province,
             city,
             county,
             "",
             "",
             new ArrayList<ObjectId>(), //userIds
             new ArrayList<ObjectId>(),  //schoolIds
             createTime,
             updateTime,
             DeleteState.NORMAL
        );
    }

    public EducationBureauEntry(String educationName, String province, String city, String county,
                                String educationLogo, String schoolCreateDate,List<ObjectId> userIds,
                                List<ObjectId> schoolIds, long createTime, long updateTime) {
        this(
                educationName,
                province,
                city,
                county,
                educationLogo,
                schoolCreateDate,
                userIds,
                schoolIds,
                createTime,
                updateTime,
                DeleteState.NORMAL
        );
    }

    public EducationBureauEntry(String educationName, String province, String city, String county,
                                String educationLogo, String schoolCreateDate,List<ObjectId> userIds,
                                List<ObjectId> schoolIds, long createTime, long updateTime, DeleteState ds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("nm", educationName)
                .append("pr", province)
                .append("ci", city)
                .append("co", county)
                .append("scrd", schoolCreateDate)
                .append("uis", MongoUtils.convert(userIds))
                .append("sis", MongoUtils.convert(schoolIds))
                .append("logo",educationLogo)
                .append("crt", createTime)
                .append("upt", updateTime)
                .append("oc",Constant.ZERO)
                .append("st", ds.getState());
        setBaseEntry(dbo);
    }

    public String getEducationName() {
        return getSimpleStringValue("nm");
    }

    public void setEducationName(String educationName) {
        setSimpleValue("nm", educationName);
    }

    public String getProvince() {
        return getSimpleStringValue("pr");
    }

    public void setProvince(String province) {
        setSimpleValue("pr", province);
    }

    public String getCity() {
        return getSimpleStringValue("ci");
    }

    public void setCity(String city) {
        setSimpleValue("ci", city);
    }

    public String getCounty() {
        return getSimpleStringValue("co");
    }

    public void setCounty(String county) {
        setSimpleValue("co", county);
    }

    public String getEducationLogo() {
        return getSimpleStringValue("logo");
    }

    public void setEducationLogo(String educationLogo) {
        setSimpleValue("logo", educationLogo);
    }

    public String getSchoolCreateDate() {
        return getSimpleStringValue("scrd");
    }

    public void setSchoolCreateDate(String schoolCreateDate) {
        setSimpleValue("scrd", schoolCreateDate);
    }

    public List<ObjectId> getUserIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("uis");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }

    public void setUserIds(List<ObjectId> userIds) {
        setSimpleValue("uis", MongoUtils.convert(userIds));
    }

    public List<ObjectId> getSchoolIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("sis");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }

    public void setSchoolIds(List<ObjectId> schoolIds) {
        setSimpleValue("sis", MongoUtils.convert(schoolIds));
    }

    public Long getCreateTime() {
        return getSimpleLongValue("crt");
    }

    public void setCreateTime(Long createTime) {
        setSimpleValue("crt", createTime);
    }

    public Long getUpdateTime() {
        return getSimpleLongValue("upt");
    }

    public void setUpdateTime(Long updateTime) {
        setSimpleValue("upt", updateTime);
    }

    public List<EducationSubject> getSubjects() {

        List<EducationSubject> retList =new ArrayList<EducationSubject>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("subs");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new EducationSubject((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setSubjects(List<EducationSubject> subjects) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(subjects);
        setSimpleValue("subs", MongoUtils.convert(list));
    }

    public List<Grade> getGradeList() {
        List<Grade> gradeList =new ArrayList<Grade>(Constant.FIVE);
        BasicDBList list =(BasicDBList)getSimpleObjectValue("grs");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                gradeList.add(new Grade((BasicDBObject)o));
            }
        }
        return gradeList;
    }
    public void setGradeList(Collection<Grade> gradeList) {
        List<DBObject> list=MongoUtils.fetchDBObjectList(gradeList);
        setSimpleValue("grs", MongoUtils.convert(list));
    }

    public int getOpenCloud() {
        return getSimpleIntegerValueDef("oc", 0);
    }

    public void setOpenCloud(int openCloud) {
        setSimpleValue("oc", openCloud);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }

    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
