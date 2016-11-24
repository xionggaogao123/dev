package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;


/**
 * 考试和习题
 * <pre>
 * collectionName:exercises
 * </pre>
 * <pre>
 * {
 *  ty:种类 1为考试 2为课后小练习
 *  ti:老师ID
 *  cis:班级ID集合
 *  [
 *      
 *  ]
 *  nm:名字
 *  qp:word问题文档路径
 *  ap:答案文档路径
 *  st:状态 0没有配置 1已经配置 
 *  ts:总分数
 *  tt:总时间，单位分钟,老数据是分钟
 *  lut:最后更新时间
 *  sts:
 *  [
 *   {
 *    id:学生ID
 *    v:时间
 *   }
 *  ]
 *  gu: 优秀学生试卷，保存的是用户ID
 *  [
 *   
 *  ]
 * }
 * </pre>
 * @author fourer
 */
public class ExerciseEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7759310037863062623L;
	
	
	public ExerciseEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	/**
	 * 课后小练习使用此构造方法
	 * @param type
	 * @param teacherId
	 * @param name
	 * @param questionPath
	 */
	public ExerciseEntry(int type, ObjectId teacherId,
                         String name, String questionPath) {
		this(type,teacherId,null,name,questionPath,Constant.EMPTY,0,0,0, System.currentTimeMillis(),null);
	}
	
	public ExerciseEntry(int type, ObjectId teacherId, List<ObjectId> classIds,
                         String name, String questionPath, String answerPath) {
		this(type,teacherId,classIds,name,questionPath,answerPath,0,0,30, System.currentTimeMillis(),null);
	}
	
	public ExerciseEntry(int type, ObjectId teacherId, List<ObjectId> classIds,
                         String name, String questionPath, String answerPath, int state,
                         int totalScore, int totalTime, long lastUpdateTime, List<IdValuePair> submitStudents) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ty", type)
		.append("ti", teacherId)
		.append("cis", MongoUtils.convert(classIds))
		.append("nm", name)
		.append("qp", questionPath)
		.append("ap", answerPath)
		.append("st", state)
		.append("ts", totalScore)
		.append("tt", totalTime)
		.append("lut", lastUpdateTime)
		.append("sts", MongoUtils.convert(MongoUtils.fetchDBObjectList(submitStudents)))
		.append("gu", new BasicDBList());
		;
		setBaseEntry(baseEntry);
	}

	public List<IdValuePair> getSubmitStudents() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sts");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setSubmitStudents(List<IdValuePair> submitStudents) {
		setSimpleValue("sts",  MongoUtils.convert(MongoUtils.fetchDBObjectList(submitStudents)));
	}
	
	
	public List<ObjectId> getGoodUser() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("gu");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setGoodUser(List<ObjectId> goodUser) {
		setSimpleValue("gu",  MongoUtils.convert(goodUser));
	}
	
	
	


	public int getType() {
		return getSimpleIntegerValue("ty");
	}

	public void setType(int type) {
		setSimpleValue("ty", type);
	}

	public ObjectId getTeacherId() {
		return getSimpleObjecIDValue("ti");
	}
	public void setTeacherId(ObjectId teacherId) {
		setSimpleValue("ti", teacherId);
	}
	public List<ObjectId> getClassIds() {
		return MongoUtils.getFieldObjectIDs(this, "cis");
	}
	public void setClassIds(List<ObjectId> classIds) {
		setSimpleValue("cis", classIds);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public String getQuestionPath() {
		return getSimpleStringValue("qp");
	}
	public void setQuestionPath(String questionPath) {
		setSimpleValue("qp", questionPath);
	}
	public String getAnswerPath() {
		return getSimpleStringValue("ap");
	}
	public void setAnswerPath(String answerPath) {
		setSimpleValue("ap", answerPath);
	}
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public int getTotalScore() {
		return getSimpleIntegerValue("ts");
	}
	public void setTotalScore(int totalScore) {
		setSimpleValue("ts", totalScore);
	}
	public int getTotalTime() {
		return getSimpleIntegerValue("tt");
	}
	public void setTotalTime(int totalTime) {
		setSimpleValue("tt", totalTime);
	}
	public long getLastUpdateTime() {
		return getSimpleLongValue("lut");
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		setSimpleValue("lut", lastUpdateTime);
	}



}
