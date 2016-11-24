package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 学生测试题目答案
 * <pre>
 * collectionName:exerciseanswers
 * </pre>
 * <pre>
 * {
 *  di:文档ID 对应exerciseEntry的_id
 *  ti:文档题目ID  对应exerciseItemEntry的_id
 *  ui:用户ID
 *  an:题目答案
 *  so:用户得分 ；如果是非主观题，有具体值；主观题开始为-1
 *  ir:是否正确   0不正确 1正确 （此字段只针对于非主观题，默认-1） 
 *  ims[]:上传的图片,多张
 * }
 * </pre>
 * @author fourer
 */
public class ExerciseAnswerEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -409428005661338505L;
	
	
	
	
	public ExerciseAnswerEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public ExerciseAnswerEntry(ObjectId documentId, ObjectId titleId, ObjectId userId,
                               String userAnswer, double userScore, int isRight, List<IdValuePair> images) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("di", documentId)
		.append("ti", titleId)
		.append("ui", userId)
		.append("an", userAnswer)
		.append("so", userScore)
		.append("ir", isRight)
		.append("ims", MongoUtils.convert(MongoUtils.fetchDBObjectList(images)))
		;
		setBaseEntry(baseEntry);
	}
	
	public List<IdValuePair> getImages() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ims");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}


	public void setImages(List<IdValuePair> images) {
		
		List<DBObject> list= MongoUtils.fetchDBObjectList(images);
		setSimpleValue("ims", MongoUtils.convert(list));
	}

	public ObjectId getDocumentId() {
		return getSimpleObjecIDValue("di");
	}
	public void setDocumentId(ObjectId documentId) {
		setSimpleValue("di", documentId);
	}
	public ObjectId getTitleId() {
		return getSimpleObjecIDValue("ti");
	}
	public void setTitleId(ObjectId titleId) {
		setSimpleValue("ti", titleId);
	}
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}
	public String getUserAnswer() {
		return getSimpleStringValue("an");
	}
	public void setUserAnswer(String userAnswer) {
		setSimpleValue("an", userAnswer);
	}
	public double getUserScore() {
		return getSimpleDoubleValue("so");
	}
	public void setUserScore(double userScore) {
		setSimpleValue("so", userScore);
	}
	public int getIsRight() {
		return getSimpleIntegerValue("ir");
	}
	public void setIsRight(int isRight) {
		setSimpleValue("ir", isRight);
	}
	

}
