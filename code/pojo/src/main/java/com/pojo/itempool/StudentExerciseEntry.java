package com.pojo.itempool;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;


/**
 * 学生练习情况
 * <pre>
 * collectionName:studentexercise
 * </pre>
 * <pre>
 *  {   ui:用户名
	 *  nm:名字
	 *  sty:科目；对应SubjectType
	 *  tot:总的题目个数
	 *  st:是否已经完成；0没有，1已经完成
	 *  t:创建时间或者完成时间
	 *  
	 *  gr:年级；对应Grade.ty
	 *  kws:知识点
	 *  [
	 *   
	 *  ]
	 *  lev:难易程度
	 *  
	 *  ch：选择题
	 *  [
	 *   {
	 *    id:题库ID
	 *    nm:答案
	 *    v:答案正确还是错误   1正确 0错误
	 *   }
	 *  ]
	 *  tf:判断题
	 *  [
	 *   
	 *  ]
	 *  gap:填空
	 *  [
	 *   
	 *  ]
	 *  sub:主观
	 *  [
	 *   
	 *  ]
	 *  
 *  }
 * </pre>
 * @author fourer
 */
public class StudentExerciseEntry extends BaseDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2454953834851008564L;
	
	
	public StudentExerciseEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public StudentExerciseEntry(ObjectId ui,String name, int subjectType, List<IdNameValuePair> choices, List<IdNameValuePair> trueFalses,List<IdNameValuePair> gaps,List<IdNameValuePair> subjectives,List<ObjectId> kws,int grade,int level) {
		this(ui,name, subjectType, Constant.ZERO,choices,trueFalses,gaps,subjectives,kws,grade,level);
		
	}
	
	
	
	public StudentExerciseEntry(ObjectId ui,String name,int type, 
			 int state,List<IdNameValuePair> choices, List<IdNameValuePair> trueFalses,List<IdNameValuePair> gaps,List<IdNameValuePair> subjectives,List<ObjectId> kws,int grade,int level) {
		super();
		
		int count =0;
		if(null!=choices) count+=choices.size();
		if(null!=trueFalses) count+=trueFalses.size();
		if(null!=gaps) count+=gaps.size();
		if(null!=subjectives) count+=subjectives.size();
		
		
		BasicDBObject dbo =new BasicDBObject()
		.append("ui", ui)
		.append("nm", name)
		.append("sty", type)
		.append("tot", count)
		.append("st", state)
		.append("kws", MongoUtils.convert(kws))
		.append("gr", grade)
		.append("lev", level)
		.append("t", System.currentTimeMillis());
		
		List<DBObject> list1=MongoUtils.fetchDBObjectList(choices);
		dbo.append("ch", MongoUtils.convert(list1));
		
		List<DBObject> list2=MongoUtils.fetchDBObjectList(trueFalses);
		dbo.append("tf", MongoUtils.convert(list2));
		
		List<DBObject> list3=MongoUtils.fetchDBObjectList(gaps);
		dbo.append("gap", MongoUtils.convert(list3));
		
		List<DBObject> list4=MongoUtils.fetchDBObjectList(subjectives);
		dbo.append("sub", MongoUtils.convert(list4));
		
		setBaseEntry(dbo);
	}
	public int getLevel() {
		return getSimpleIntegerValue("lev");
	}

	public void setLevel(int level) {
		setSimpleValue("lev", level);
	}

	public int getGrade() {
		return getSimpleIntegerValue("gr");
	}

	public void setGrade(int grade) {
		setSimpleValue("gr", grade);
	}
	
	

	public List<ObjectId> getKwList() {
		List<ObjectId> kwList = new ArrayList<ObjectId>();
		BasicDBList list = (BasicDBList) getSimpleObjectValue("kws");
		if (null != list && !list.isEmpty()) {
			for (Object o : list) {
				kwList.add((ObjectId)o);
			}
		}
		return kwList;
	}

	public void setKwList(List<ObjectId> kwList) {
		setSimpleValue("kws", MongoUtils.convert(kwList));
	}

	public long getTime() {
		return getSimpleLongValue("t");
	}
	public void setTime(long time) {
		setSimpleValue("t", time);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public int getSubjectType() {
		return getSimpleIntegerValue("sty");
	}
	public void setSubjectType(int subjectType) {
		setSimpleValue("sty", subjectType);
	}
	public int getTotalCount() {
		return getSimpleIntegerValue("tot");
	}
	public void setTotalCount(int totalCount) {
		setSimpleValue("tot", totalCount);
	}
	
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}

	public ObjectId getUserId() {
	return	getSimpleObjecIDValue("ui");
	}

	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}

	/**
	 * 根据题型得到做题情况
	 * @param type
	 * @return
	 */
	public List<com.pojo.app.IdNameValuePair> getList(ExerciseItemType type) {
		
		List<com.pojo.app.IdNameValuePair> items = new ArrayList<IdNameValuePair>();
		BasicDBList list = (BasicDBList) getSimpleObjectValue(type.getField());
		if (null != list && !list.isEmpty()) {
			for (Object o : list) {
				items.add(new IdNameValuePair((BasicDBObject) o));
			}
		}
		return items;
	}

	
	/**
	 * 得到做题正确的数目
	 * @return
	 */
	public int getRightCount()
	{
		int count =0;
		
		for(ExerciseItemType thisType:ExerciseItemType.values())
		{
			if(thisType==ExerciseItemType.MULTICHOICE)
				continue;
			if(null!=this.getList(thisType))
			{
				for(IdNameValuePair p:this.getList(thisType))
				{
					if(  null!=p.getValue() &&  p.getValue().equals(Constant.ONE))
					{
						count++;
					}
				}
			}
		}
		
		return count;
	}
	
	
	
	public int getFinishCount()
	{
        int count =0;
		
		for(ExerciseItemType thisType:ExerciseItemType.values())
		{
			if(thisType==ExerciseItemType.MULTICHOICE)
				continue;
			if(this.getList(thisType).size()>0)
			{
				for(IdNameValuePair p:this.getList(thisType))
				{
					if(  StringUtils.isNotBlank(p.getName()))
					{
						count++;
					}
				}
			}
		}
		
		return count;
	}
	
}
