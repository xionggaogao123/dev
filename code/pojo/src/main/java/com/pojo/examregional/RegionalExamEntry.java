package com.pojo.examregional;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.NameValuePair;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * 区域联考表
 * @author lujiang 2015/10/27
 * 对应数据库集合 Constant.COLLECTION_REGIONAL_EXAM="jointexam"
 * 考试名称:nm
 * 年级名称：gnm
 * 年级类型:gty(int)
 * 考试类型：ty(默认区域联考)
 * 考试日期:date(long)
 * 考试科目:sbj(list)
 * 学校:sch(list)
 * 学期:term
 * 教育局id：eid
 * 分数段:ss  List<ScoreSection>
 * 等第设置gs List<NameValuePair>  name等第名称  value百分比值
 * 是否完成排名：rf(0：未完成，1：完成)
 * 是否删除：isr(0:未删除，1：已删除)
 */
public class RegionalExamEntry extends BaseDBObject{
	public RegionalExamEntry(BasicDBObject baseEntry){
		super(baseEntry);
	};
	public RegionalExamEntry(String name,String gradeName,int gradeType,String examType,long examDate,List<RegionalSubjectItem> examSubject,
			List<RegionalSchItem> school,String term,ObjectId educationId,int isRemove){
		super();
		 BasicDBObject baseEntry = new BasicDBObject().append("nm",name )
				 .append("gnm", gradeName)
				 .append("gty",gradeType)
				 .append("ty", examType)
				 .append("date", examDate)
				 .append("sbj",MongoUtils.convert(MongoUtils.fetchDBObjectList(examSubject)))
				 .append("sch",MongoUtils.convert(MongoUtils.fetchDBObjectList(school)))
				 .append("term", term)
				 .append("eid", educationId)
				 .append("isr", isRemove)
				 .append("ss", null)
				 .append("gs", null)
				 .append("rf", 1);
		 setBaseEntry(baseEntry);
	}
	public String getName(){
		return getSimpleStringValue("nm");
	}
	public void setName(String name){
		setSimpleValue("nm", name);
	}
	public String getGradeName(){
		return getSimpleStringValue("gnm");
	}
	public void setGradeName(String gradeName){
		setSimpleValue("gnm",gradeName);
	}
	public int getGradeType(){
		return getSimpleIntegerValue("gty");
	}
	public void setGradeType(int gradeType){
		setSimpleValue("gty",gradeType);
	}
	public String getExamType(){
		return getSimpleStringValue("ty");
	}
	public void setExamType(String examType){
		setSimpleValue("ty",examType);
	}
	public long getExamDate(){
		return getSimpleLongValue("date");
	}
	public void setExamDate(long examDate){
		setSimpleValue("date",examDate);
	}
	public String getTerm(){
		return getSimpleStringValue("term");
	}
	public void setTerm(String term){
		setSimpleValue("term",term);
	}
	public List<RegionalSubjectItem> getExamSubject(){
		List<RegionalSubjectItem> resultList = new ArrayList<RegionalSubjectItem>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sbj");
		if(null!=list && !list.isEmpty()) {
			for(Object o:list) {
				resultList.add(new RegionalSubjectItem((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setExamSubject(List<RegionalSubjectItem> examSubject){
		List<DBObject> list=MongoUtils.fetchDBObjectList(examSubject);
		setSimpleValue("sbj",MongoUtils.convert(list));
	}
	
	
	public List<RegionalSchItem> getSchool(){
		List<RegionalSchItem> resultList = new ArrayList<RegionalSchItem>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sch");
		if(null!=list && !list.isEmpty()) {
			for(Object o:list) {
				resultList.add(new RegionalSchItem((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setSchool(List<RegionalSchItem> school){
		List<DBObject> list=MongoUtils.fetchDBObjectList(school);
		setSimpleValue("sch",MongoUtils.convert(list));
	}

	public List<ScoreSection> getScoreSection(){
		List<ScoreSection> resultList = new ArrayList<ScoreSection>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ss");
		if(null!=list && !list.isEmpty()) {
			for(Object o:list) {
				resultList.add(new ScoreSection((BasicDBObject)o));
			}
		}
		return resultList;
	}

	public void setScoreSection(List<ScoreSection> scoreSection){
		List<DBObject> list=MongoUtils.fetchDBObjectList(scoreSection);
		setSimpleValue("ss",MongoUtils.convert(list));
	}

	public ObjectId getEducationId(){
		return getSimpleObjecIDValue("eid");
	}
	public void setEducationId(ObjectId educationId){
		setSimpleValue("eid",educationId);
	}
	public int getIsRemove(){
		return getSimpleIntegerValue("isr");
	}
	public void setIsRemove(int isRemove){
		setSimpleValue("isr",isRemove);
	}
	public int getRankingFlag(){
		if(getBaseEntry().containsField("rf")) {
			return getSimpleIntegerValue("rf");
		}
		return 0;
	}
	public void setRankingFlag(int rankingFlag){
		setSimpleValue("rf", rankingFlag);
	}

	public List<NameValuePair> getGradeSetting(){
		List<NameValuePair> resultList = new ArrayList<NameValuePair>();
		if(getBaseEntry().containsField("gs")) {
			BasicDBList list = (BasicDBList) getSimpleObjectValue("gs");
			if (null != list && !list.isEmpty()) {
				for (Object o : list) {
					resultList.add(new NameValuePair((BasicDBObject) o));
				}
			}
		}
		return resultList;
	}

	public void setGradeSetting(List<NameValuePair> scoreSection){
		List<DBObject> list=MongoUtils.fetchDBObjectList(scoreSection);
		setSimpleValue("gs",MongoUtils.convert(list));
	}

}
