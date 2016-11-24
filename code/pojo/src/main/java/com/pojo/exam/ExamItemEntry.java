package com.pojo.exam;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         考试小分设置分类Entry类 
 *         collectionName : examitem 
 *         考试Id : eid(examId) 
 *         科目Id : sid(subjectId) 
 *         序号id : iid (itemId)
 *         名称 : na(name)
 *         分数 : sc(score)
 *         父Id : pid(parentId)
 *         是否叶节点 : il(isLeaf)0为否1为是
 */
public class ExamItemEntry extends BaseDBObject{
	public ExamItemEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ExamItemEntry(ObjectId examId,ObjectId subjectId,String itemId,String name,double score,String parentId,int isLeaf) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("eid", examId)
													 .append("sid", subjectId)
													 .append("iid", itemId)
													 .append("na", name)
													 .append("sc", score)
													 .append("pid", parentId)
													 .append("il", isLeaf);

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
	
	public String getName() {
		return getSimpleStringValue("na");
	}
	public void setName(String name) {
		setSimpleValue("na", name);
	}
	
	public double getScore() {
		return getSimpleDoubleValue("sc");
	}

	public void setScore(double score) {
		setSimpleValue("sc", score);
	}
	
	public String getParentId() {
		return getSimpleStringValue("pid");
	}
	public void setParentId(String parentId) {
		setSimpleValue("pid", parentId);
	}
}
