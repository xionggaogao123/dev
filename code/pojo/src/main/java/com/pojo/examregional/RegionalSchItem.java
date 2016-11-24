package com.pojo.examregional;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 学校集合，依附于RegionalExamEntry
 * @author lujiang
 * 学校名称：nm
 * 学校id:id
 * 学校对应考试的id：eid
 * 是否提交：fg (0:未提交,1:已提交)
 */
public class RegionalSchItem extends BaseDBObject{
	public RegionalSchItem(BasicDBObject baseEntry){
		super(baseEntry);
	}
	public RegionalSchItem(String name,ObjectId schoolId,ObjectId examId){
		super();
		BasicDBObject baseEntry = new BasicDBObject().append("nm", name)
				.append("id", schoolId)
				.append("eid", examId)
				.append("fg", Constant.ZERO);
		setBaseEntry(baseEntry);
	}
	public String getName(){
		return getSimpleStringValue("nm");
	}
	public void setName(String name){
		setSimpleValue("nm",name);
	}
	public ObjectId getSchoolId(){
		return getSimpleObjecIDValue("id");
	}
	public void setSchoolId(ObjectId schoolId){
		setSimpleValue("id",schoolId);
	}
	public ObjectId getExamId(){
		return getSimpleObjecIDValue("eid");
	}
	public void setExamId(ObjectId examId ){
		setSimpleValue("eid",examId);
	}
	public int getFlag(){
		return getSimpleIntegerValue("fg");
	}
	public void setFlag(int flag){
		setSimpleValue("fg",flag);
	}
}
