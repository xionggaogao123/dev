package com.pojo.itempool;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.utils.MongoUtils;


/**
 * 老师组卷
 * <pre>
 * collectionName:testpapers
 * </pre>
 * <pre>
 * {
 *  ui:
 *  nm:名称
 *  gr:年级；对应GradeType
 *  sty:科目；
 *  ti:时间；单位为分钟
 *  st:状态   1已经完成 2 已经推送至班级 3 已经推送到校本
 *  tid:学校id
 *  ch:选择题列表
 *  [
 *   
 *  ]
 *  tf:判断题列表
 *  [
 *   
 *  ]
 *  gap:填空题列表
 *  [
 *   
 *  ]
 *  sub：主观题列表
 *  [
 *   
 *  ]
 *  
 *  
 *  
 *  cls:推送班级
 *  [
 *  ]
 * }
 * </pre>
 * @author fourer
 */
public class TestPaperEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TestPaperEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	
	public TestPaperEntry(ObjectId ui, String name, ObjectId grage,ObjectId subject,
			int time,int count) {
		this(ui, name, grage, subject, time, 1, null, null, null, null, null);
	}
	
	
	
	public TestPaperEntry(ObjectId ui, String name, ObjectId type, ObjectId subject,
			int time, int state,List<Integer> classes,List<ObjectId> choices, List<ObjectId> trueFalses,List<ObjectId> gaps,List<ObjectId> subjectives ) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("ui", ui)
		.append("nm", name)
		.append("gr", type)
		.append("sty", subject)
		.append("ti", time)
		.append("st", state)
		.append("cls", MongoUtils.convert(classes))
		.append("ch", MongoUtils.convert(choices))
		.append("tf", MongoUtils.convert(trueFalses))
		.append("gap", MongoUtils.convert(gaps))
		.append("sub", MongoUtils.convert(subjectives))
		;
		
		setBaseEntry(dbo);
	}
	
	public ObjectId getUi() {
		return getSimpleObjecIDValue("ui");
	}

	public void setUi(ObjectId ui) {
		setSimpleValue("ui", ui);
	}

	public String getName() {
		return getSimpleStringValue("nm");
	}

	public void setName(String name) {
		setSimpleValue("nm", name);
	}

	public ObjectId getGrage() {
		return getSimpleObjecIDValue("gr");
		//return getSimpleIntegerValue("gr");
	}

	public void setGrage(ObjectId grage) {
		setSimpleValue("gr", grage);
	}

	public ObjectId getSubject() {
		return getSimpleObjecIDValue("sty");
	}

	public void setSubject(ObjectId subject) {
		setSimpleValue("sty", subject);
	}

	public int getTime() {
		return getSimpleIntegerValue("ti");
	}

	public void setTime(int time) {
		setSimpleValue("ti", time);
	}

	public int getState() {
		return getSimpleIntegerValue("st");
	}

	public void setState(int state) {
		setSimpleValue("st", state);
	}

	public ObjectId getTeacherId(){
		if(getBaseEntry().containsField("tid")){
			return getSimpleObjecIDValue("tid");
		}
		return getSimpleObjecIDValue("ui");
	}

	public void setTeacherId(ObjectId teacherId){
		setSimpleValue("tid", teacherId);
	}

	public List<ObjectId> getQuestions(ExerciseItemType type) {
		 List<ObjectId> itemList =new ArrayList<ObjectId>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue(type.getField());
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					itemList.add(  (ObjectId)o);
				}
			}
			return itemList;
	}

	public void setQuestions(List<ObjectId> questions,ExerciseItemType type) {
		setSimpleValue(type.getField(),MongoUtils.convert(questions) );
	}


	public List<ObjectId> getClasses() {
		 List<ObjectId> itemList =new ArrayList<ObjectId>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("cls");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					itemList.add(  (ObjectId)o);
				}
			}
			return itemList;
	}


	public void setClasses(List<ObjectId> classes) {
		setSimpleValue("cls",MongoUtils.convert(classes) );
	}

	
	
}
