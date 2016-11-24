package com.pojo.jointexam;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.exam.ExamSubjectEntry;
import com.pojo.utils.MongoUtils;

/**
 * 
 * @author Administrator
 *
 *
 *  考试名称：name
 *  年级名称：gnm
 *  年级类型：gty(int)
 *  考试类型：ty
 *  考试时间：date（）
 *  考试科目：sbj(List<T>)
 *  学校：sch（List<T>）
 *  学期；term
 *  教育局id：eid
 *  是否删除：isr
 */
public class JointExamEntry extends BaseDBObject{
	/**
     * 已删除
     */
    public static final int ISREMOVE_DELETED = 1;
    /**
     * 正常状态
     */
    public static final int ISREMOVE_NOT_DELETE = 0;
	
	public JointExamEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
	public JointExamEntry(String name,String gradeName,int gradeType,String type ,Long date ,
List<JointSubjectEntry> subject,List<JointShcoolEntry> school,String term,String eductionId,int isRemove){
		
		super();
        BasicDBObject baseEntry = new BasicDBObject()
        .append("name", name)
        .append("gnm", gradeName)
        .append("gty", gradeType)
        .append("ty", type)
        .append("date", date).append("term", term)
        .append("eid", eductionId)
        .append("fg", isRemove)
        .append("sbj", MongoUtils.convert(MongoUtils.fetchDBObjectList(subject)))
        .append("sch", MongoUtils.convert(MongoUtils.fetchDBObjectList(school)));
        setBaseEntry(baseEntry);
	}
	
	    public int getFlag(){
	    	return getSimpleIntegerValue("isr");
	    }
	    public void setFlag(int isRemove){
	    	setSimpleValue("isr",isRemove);
	    }
	    public String getName() {
	        return getSimpleStringValue("name");
	    }

	    public void setName(String name) {
	        setSimpleValue("name", name);
	    }
	    public String getEductionId() {
	        return getSimpleStringValue("eid");
	    }

	    public void setEductionId(String eductionId) {
	        setSimpleValue("eid", eductionId);
	    }
	    public String getGradeName() {
	        return getSimpleStringValue("gnm");
	    }

	    public void setGradeName(String gradeName) {
	        setSimpleValue("nm", gradeName);
	    }
	    public int getGradeType() {
	        return getSimpleIntegerValue("gty");
	    }

	    public void setGradeType(int gradeType) {
	        setSimpleValue("gty", gradeType);
	    }
	    public String getType() {
	        return getSimpleStringValue("ty");
	    }

	    public void setType(String type) {
	        setSimpleValue("ty", type);
	    }
	    public Long getDate() {
	        return getSimpleLongValue("date");
	    }

	    public void setDate(Long date) {
	        setSimpleValue("date", date);
	    }
	    public String getTrem() {
	        return getSimpleStringValue("trem");
	    }

	    public void setTrem(String trem) {
	        setSimpleValue("trem", trem);
	    }
	    public List<JointSubjectEntry> getSubject() {
	        BasicDBList list = (BasicDBList) getSimpleObjectValue("sbj");
	        List<JointSubjectEntry> retList;
	        if (null != list && !list.isEmpty()) {
	            retList = new ArrayList<JointSubjectEntry>(list.size());
	            for (Object o : list) {
	                retList.add(new JointSubjectEntry((BasicDBObject) o));
	            }
	        } else {
	            retList = new ArrayList<JointSubjectEntry>(0);
	        }
	        return retList;
	    }

	    public void setSubject(List<JointShcoolEntry> subject) {
	        setSimpleValue("sbj", MongoUtils.convert(MongoUtils.fetchDBObjectList(subject)));
	    }
	    
	    public List<JointShcoolEntry> getSchool() {
	        BasicDBList list = (BasicDBList) getSimpleObjectValue("sch");
	        List<JointShcoolEntry> retList;
	        if (null != list && !list.isEmpty()) {
	            retList = new ArrayList<JointShcoolEntry>(list.size());
	            for (Object o : list) {
	                retList.add(new JointShcoolEntry((BasicDBObject) o));
	            }
	        } else {
	            retList = new ArrayList<JointShcoolEntry>(0);
	        }
	        return retList;
	    }

	    public void setSchool(List<JointShcoolEntry> school) {
	        setSimpleValue("sch", MongoUtils.convert(MongoUtils.fetchDBObjectList(school)));
	    }
	    
}
