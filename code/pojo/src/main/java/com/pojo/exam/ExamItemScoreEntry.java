package com.pojo.exam;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         考试小分分数Entry类 
 *         collectionName : examitemscore 
 *         考试Id : eid(examId) 
 *         科目Id : sid(subjectId) 
 *         序号id : iid (itemId)
 *         学生id : stid()studentId)
 *         分数 : sc(score)
 */
public class ExamItemScoreEntry extends BaseDBObject{
	public ExamItemScoreEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ExamItemScoreEntry(ObjectId examId,ObjectId subjectId,String itemId,String studentId,double score) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("eid", examId)
													 .append("sid", subjectId)
													 .append("iid", itemId)
													 .append("stid", studentId)
													 .append("sc", score);

		setBaseEntry(baseEntry);

	}
	
	public ObjectId getExamId() {
		return getSimpleObjecIDValue("eid");
	}
	public void setExamId(String examId) {
		setSimpleValue("eid", examId);
	}
	
	public ObjectId getSubjectId() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSubjectId(String subjectId) {
		setSimpleValue("sid", subjectId);
	}
	
	public String getItemId() {
		return getSimpleStringValue("iid");
	}
	public void setItemId(String itemId) {
		setSimpleValue("iid", itemId);
	}
	
	public String getStudentId() {
		return getSimpleStringValue("stid");
	}
	public void setStudentId(String studentId) {
		setSimpleValue("stid", studentId);
	}
	
	public double getScore() {
		return getSimpleDoubleValue("sc");
	}

	public void setScore(double score) {
		setSimpleValue("sc", score);
	}
}
