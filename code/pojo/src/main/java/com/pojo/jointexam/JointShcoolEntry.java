package com.pojo.jointexam;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 
 * @author Administrator
 *学校名称：nm
 *学校id：schoolId
 *学校对应的考试id：eid
 *是否提交：fg
 */
public class JointShcoolEntry extends BaseDBObject {
	
	
	 public JointShcoolEntry(BasicDBObject baseEntry) {
	        super(baseEntry);
	    }
	 public JointShcoolEntry(
			 String name,String schoolId,String examId,int submit){
		 super();
	        BasicDBObject baseEntry = new BasicDBObject()
	        .append("nm", name)
	        .append("id", schoolId)
	        .append("eid", examId)
	        .append("fg", submit);
	        setBaseEntry(baseEntry);
	 }
	 public String getName() {
	        return getSimpleStringValue("nm");
	    }

	    public void setName(String name) {
	        setSimpleValue("nm", name);
	    }
	    public String getSchoolId() {
	        return getSimpleStringValue("id");
	    }

	    public void setSchoolId(String schoolId) {
	        setSimpleValue("nm", schoolId);
	    }
	    public String getExamId() {
	        return getSimpleStringValue("eid");
	    }

	    public void setExamId(String examId) {
	        setSimpleValue("nm", examId);
	    }
	    public int getSubmit() {
	        return getSimpleIntegerValue("fg");
	    }

	    public void setSubmit(String submit) {
	        setSimpleValue("nm", submit);
	    }
}
