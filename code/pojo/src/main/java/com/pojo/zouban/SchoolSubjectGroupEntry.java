package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/** 学校学科组合
 * Created by fl on 2016/7/16.
 * sid : 学校id
 * year ：学年
 * gid : 年级id
 * sgs : 学科组合列表
 *
 */
public class SchoolSubjectGroupEntry extends BaseDBObject {

    public SchoolSubjectGroupEntry(){}

    public SchoolSubjectGroupEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public SchoolSubjectGroupEntry(ObjectId schoolId, String year, ObjectId gradeId, List<SubjectGroup> subjectGroups){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("year", year)
                .append("gid", gradeId)
                .append("sgs", MongoUtils.convert(MongoUtils.fetchDBObjectList(subjectGroups)))
                ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid", schoolId);
    }

    public String getYear(){
        return getSimpleStringValue("year");
    }

    public void setYear(String year){
        setSimpleValue("year", year);
    }

    public ObjectId getGradeId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId){
        setSimpleValue("gid", gradeId);
    }

    public List<SubjectGroup> getSubjectGroups(){
        List<SubjectGroup> subjectGroups = new ArrayList<SubjectGroup>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("sgs");
        for(Object o : list){
            subjectGroups.add(new SubjectGroup((BasicDBObject)o));
        }
        return subjectGroups;
    }

    public void setSubjectGroups(List<SubjectGroup> subjectGroups){
        setSimpleValue("sgs", MongoUtils.convert(MongoUtils.fetchDBObjectList(subjectGroups)));
    }


    /**
     * 学科组合
     * id : id
     * nm : name
     * isp : 是否开放
     * adv ：组合中等级考科目列表
     * sim ：组合中合格考科目列表
     */
     public static class SubjectGroup extends BaseDBObject{

         public SubjectGroup(){}

         public SubjectGroup(BasicDBObject baseEntry){
             setBaseEntry(baseEntry);
         }

         public SubjectGroup(ObjectId id, String name, Boolean isPublic, List<ObjectId> adv, List<ObjectId> sim){
             BasicDBObject baseEntry = new BasicDBObject()
                     .append("id", id)
                     .append("nm", name)
                     .append("isp", isPublic)
                     .append("adv", MongoUtils.convert(adv))
                     .append("sim", MongoUtils.convert(sim));
             setBaseEntry(baseEntry);
         }

         public ObjectId getId(){
             return getSimpleObjecIDValue("id");
         }

         public void setId(ObjectId id){
             setSimpleValue("id", id);
         }

        public String getName(){
            return getSimpleStringValue("nm");
        }

        public void setName(String name){
            setSimpleValue("nm", name);
        }

         public Boolean getIsPublic(){
             return getSimpleBoolean("isp");
         }

         public void setIsPublic(Boolean isPublic){
             setSimpleValue("isp", isPublic);
         }

         public List<ObjectId> getAdvSubjects(){
             List<ObjectId> retList = new ArrayList<ObjectId>();
             BasicDBList list =(BasicDBList)getSimpleObjectValue("adv");
             for(Object o : list){
                 retList.add((ObjectId)o);
             }
             return retList;
         }

         public void setAdvSubjects(List<ObjectId> advSubjects){
             setSimpleValue("adv", MongoUtils.convert(advSubjects));
         }

         public List<ObjectId> getSimSubjects(){
             List<ObjectId> retList = new ArrayList<ObjectId>();
             BasicDBList list =(BasicDBList)getSimpleObjectValue("sim");
             for(Object o : list){
                 retList.add((ObjectId)o);
             }
             return retList;
         }

         public void setSimSubjects(List<ObjectId> simSubjects){
             setSimpleValue("sim", MongoUtils.convert(simSubjects));
         }

     }

}
