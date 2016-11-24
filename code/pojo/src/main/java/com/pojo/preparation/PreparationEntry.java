package com.pojo.preparation;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         集体备课Entry类 
 *         collectionName : preparation 
 *         发布人id: puid(publishUserId)
 *         发布人name:puna(publishUserName)
 *         发布人学校id: scid(schoolId)
 *         发布人学校name: scna(schoolName)
 *         备课名称 : pna(preparationName)
 *         学段	   : ett(educationTermType)
 *         学科	   : es(educationSubject)
 *         教材版本 : tv(textbookVersion)
 *         年级 	   : eg(educationGrade)
 *         章  	   : ch(chapter)
 *         节	   : pat(part)
 *         发布时间 : pt(publishTime) long
 *         备课封面 : pc(preparationCover) 
 *         开始时间 : st(startTime) long
 *         结束时间 : et(endTime) long
 *		        正文	   : pco(preparationContent)
 *         课件[] 	 : cs(courseware)
 *         备选课件[] : csb(coursewareBack)
 *         教育局ID  ： edid(educationBureauId)
 */
public class PreparationEntry extends BaseDBObject{
	
	public PreparationEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public PreparationEntry(ObjectId publishUserId,String publishUserName,ObjectId schoolId,String schoolName,String preparationName,String educationTermType,String educationSubject,String textbookVersion,String educationGrade,
			String chapter,String part,long publishTime,String preparationCover,long startTime,long endTime,String preparationContent,
			List<ObjectId> courseware,List<ObjectId> coursewareBack,ObjectId educationBureauId) {
			super();

			BasicDBObject baseEntry = new BasicDBObject().append("puid",publishUserId)
														 .append("puna", publishUserName)
														 .append("schid", schoolId)
														 .append("scna", schoolName)
														 .append("pna", preparationName)
									 					 .append("ett", educationTermType)
									 					 .append("es", educationSubject)
									 					 .append("tv", textbookVersion)
									 					 .append("eg", educationGrade)
									 					 .append("ch", chapter)
									 					 .append("pat", part)
									 					 .append("pt", publishTime)
									 					 .append("pc", preparationCover)
									 					 .append("st", startTime)
									 					 .append("et", endTime)
									 					 .append("pco", preparationContent)
									 					 .append("edid", educationBureauId)
									 					 .append("cs", MongoUtils.convert(courseware))
									 					 .append("csb", MongoUtils.convert(coursewareBack));

			setBaseEntry(baseEntry);
	}
	
	public ObjectId getPublishUserId() {
		return getSimpleObjecIDValue("puid");
	}
	public void setPublishUserId(String publishUserId) {
		setSimpleValue("puid", publishUserId);
	}
	
	public String getPublishUserName() { 
		return getSimpleStringValue("puna");
	}
	public void setPublishUserName(String publishUserName) {
		setSimpleValue("puna", publishUserName);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoodId(String schoolId) {
		setSimpleValue("scid", schoolId);
	}
	
	public String getSchoolName() { 
		return getSimpleStringValue("scna");
	}
	public void setSchoolName(String schoolName) {
		setSimpleValue("scna", schoolName);
	}
	
	public String getPreparationName() {
		return getSimpleStringValue("pna");
	}
	public void setPreparationName(String preparationName) {
		setSimpleValue("pna", preparationName);
	}
	
	public String getEducationTermType() {
		return getSimpleStringValue("ett");
	}
	public void setEducationTermType(String educationTermType) {
		setSimpleValue("ett", educationTermType);
	}
	
	public String getEducationSubject() {
		return getSimpleStringValue("es");
	}
	public void setEducationSubject(String educationSubject) {
		setSimpleValue("es", educationSubject);
	}
	
	public String getTextbookVersion() {
		return getSimpleStringValue("tv");
	}
	public void setTextbookVersion(String textbookVersion) {
		setSimpleValue("tv", textbookVersion);
	}
	
	public String getEducationGrade() {
		return getSimpleStringValue("eg");
	}
	public void setEducationGrade(String educationGrade) {
		setSimpleValue("eg", educationGrade);
	}
	
	public String getChapter() {
		return getSimpleStringValue("ch");
	}
	public void setChapter(String chapter) {
		setSimpleValue("ch", chapter);
	}
	
	public String getPart() {
		return getSimpleStringValue("pat");
	}
	public void setPart(String part) {
		setSimpleValue("pat", part);
	}
	
	public long getPublishTime() {
		return getSimpleLongValue("pt");
	}
	public void setPublishTime(long publishTime) {
		setSimpleValue("pt", publishTime);
	}
	
	public String getPreparationCover() {
		return getSimpleStringValue("pc");
	}
	public void setPreparationCover(String preparationCover) {
		setSimpleValue("pc", preparationCover);
	}
	
	public long getStartTime() {
		return getSimpleLongValue("st");
	}
	public void setStartTime(long startTime) {
		setSimpleValue("st", startTime);
	}
	
	public long getEndTime() {
		return getSimpleLongValue("et");
	}
	public void setEndTime(long endTime) {
		setSimpleValue("et", endTime);
	}
	
	public String getPreparationContent() {
		return getSimpleStringValue("pco");
	}
	public void setPreparationContent(String preparationContent) {
		setSimpleValue("pco", preparationContent);
	}
	
	public List<ObjectId> getCoursewareList() {
		List<ObjectId> resultList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cs");
		if(null!=list && !list.isEmpty())
		{
				for(Object o:list)
				{
					resultList.add( (ObjectId)o);
				}
		}
		return resultList;
	}
	public void setCoursewareList(List<ObjectId> coursewareList) {
		setSimpleValue("cs",  MongoUtils.convert(coursewareList));
	}
	
	public List<ObjectId> getCoursewareBackList() {
		List<ObjectId> resultList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("csb");
		if(null!=list && !list.isEmpty())
		{
				for(Object o:list)
				{
					resultList.add( (ObjectId)o);
				}
		}
		return resultList;
	}
	public void setCoursewareBackList(List<ObjectId> coursewareBackList) {
		setSimpleValue("csb",  MongoUtils.convert(coursewareBackList));
	}
}
