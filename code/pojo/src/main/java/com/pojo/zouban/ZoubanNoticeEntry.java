package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/11/9.
 * {
 * 调课通知
 * _id
 * te:------------------->term
 * sid:------------------->schoolId
 * gid:------------------>gradeId
 * ty:------------------->type 1.周内调课  2.长期调课(多周)
 * unm:------------------->userName 调课申请人
 * des-------------------->description 调课说明
 * week:----------------->week 调整周(1,2,3......)
 * con:------------------->content 详细内容
 * cl:-------------------->classIdList 调课班级
 * del:-------------------->delete  0:未删除 1:已删除
 * st:-------------------->state 0:未发布 1:已发布
 * }
 */
public class ZoubanNoticeEntry extends BaseDBObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5194897301382996948L;




	public ZoubanNoticeEntry() {
        super();
    }

    public ZoubanNoticeEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public ZoubanNoticeEntry(String term, ObjectId schoolId, ObjectId gradeId, String userName, int type, String description,
                             List<Integer> weekList, List<NoticeDetail> noticeDetails, List<ObjectId> classIdList,int state) {
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("te", term)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("unm", userName)
                .append("ty", type)
                .append("des", description)
                .append("week", MongoUtils.convert(weekList))
                .append("con", MongoUtils.convert(MongoUtils.fetchDBObjectList(noticeDetails)))
                .append("cl", MongoUtils.convert(classIdList))
                .append("del", 0)
                .append("st",state);
        setBaseEntry(basicDBObject);
    }


    public ZoubanNoticeEntry(String term, ObjectId schoolId, ObjectId gradeId, String userName, int type, String description,
                             List<Integer> weekList) {
        this(term, schoolId, gradeId, userName, type, description, weekList, new ArrayList<NoticeDetail>(), new ArrayList<ObjectId>(), 0);
    }


    public String getTerm() {
        return getSimpleStringValue("te");
    }

    public void setTerm(String term) {
        setSimpleValue("te", term);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public String getUserName() {
        return getSimpleStringValue("unm");
    }

    public void setUserName(String userName) {
        setSimpleValue("unm", userName);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
    }

    public int getState(){ return getSimpleIntegerValue("st");}

    public void setState(int state) { setSimpleValue("st",state);}

    public String getDes() {
        return getSimpleStringValue("des");
    }

    public void setDes(String des) {
        setSimpleValue("des", des);
    }

    public List<Integer> getWeek() {
        List<Integer> weekList = new ArrayList<Integer>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("week");
        for (Object o : basicDBList) {
            weekList.add((Integer) o);
        }
        return weekList;
    }

    public void setWeek(List<Integer> week) {
        setSimpleValue("week", MongoUtils.convert(week));
    }


    public List<NoticeDetail> getNoticeDetail() {
        List<NoticeDetail> noticeDetails = new ArrayList<NoticeDetail>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("con");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                noticeDetails.add(new NoticeDetail((BasicDBObject) o));
            }
        }
        return noticeDetails;
    }

    public void setNoticeDetail(List<NoticeDetail> noticeDetailList) {
        setSimpleValue("con", MongoUtils.convert(MongoUtils.fetchDBObjectList(noticeDetailList)));
    }


    public List<ObjectId> getClassIdList() {
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("cl");
        for (Object o : basicDBList) {
            classIdList.add((ObjectId) o);
        }
        return classIdList;
    }

    public void setClassIdList(List<ObjectId> classIdList) {
        setSimpleValue("cl", MongoUtils.convert(classIdList));
    }

    public int getDelete() {
        return getSimpleIntegerValue("del");
    }

    public void setDelete(int delete) {
        setSimpleValue("del", delete);
    }




    /**
     * 通知详情
     * {
     * tnm:----------------------->teacherName
     * clsnm------------------------>className
     * cnm:----------------------->courseName
     * ot------------------------>原上课时间
     * nt------------------------>新上课时间
     * }
     */
    public static class NoticeDetail extends BaseDBObject {
        public NoticeDetail() {
            super();
        }

        public NoticeDetail(BasicDBObject basicDBObject) {
            super(basicDBObject);
        }

        public NoticeDetail(String className, String courseName, String teacherName, String oldTime, String newTime) {
            BasicDBObject basicDBObject = new BasicDBObject()
                    .append("clsnm", className)
                    .append("cnm", courseName)
                    .append("tnm", teacherName)
                    .append("ot", oldTime)
                    .append("nt", newTime);
            setBaseEntry(basicDBObject);
        }

        public String getClassName() {
            return getSimpleStringValue("clsnm");
        }

        public void setClassName(String className) {
            setSimpleValue("clsnm", className);
        }

        public String getCourseName() {
            return getSimpleStringValue("cnm");
        }

        public void setCourseName(String courseName) {
            setSimpleValue("cnm", courseName);
        }

        public String getTeacherName() {
            return getSimpleStringValue("tnm");
        }

        public void setTeacherName(String teacherName) {
            setSimpleValue("tnm", teacherName);
        }

        public String getOldTime() {
            return getSimpleStringValue("ot");
        }

        public void setOldTime(String oldTime) {
            setSimpleValue("ot", oldTime);
        }

        public String getNewTime() {
            return getSimpleStringValue("nt");
        }

        public void setNewTime(String newTime) {
            setSimpleValue("nt", newTime);
        }
    }


}
