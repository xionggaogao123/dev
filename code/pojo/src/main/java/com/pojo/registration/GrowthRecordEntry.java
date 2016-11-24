package com.pojo.registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * @author cxy 
 * 2015-9-27 16:42:03
 * 成长档案Entry类 
 * collectionName : growth 
 * 学生Id : uid(userId)
 * 学期 : tt(termType)
 * 学校Id : scid(schoolId)
 * 突出表现 : gp(goodPerformance)
 * 班主任评语 : mc(masterComment)
 * 创建日期 : ti (time)
 * lvs(levels) : 级别  参见LevelObject
		   [
		   		{
		   			id: ID
					nm : name 名称 
					sr : scoreRange 分数范围
		   		}
		   ]
 * 素质教育[] : qes(qualityEducation) 参见QualityObject
 * [
        {
           nm(name) : 评价项目名称
           lv(level) : 评价等级
		   subs[ 参见SubQualityObject
		   		{
		   			nm(name) : 评价项目名称
		   			rq(requirement) : 主要表现和要求
		   			slv(subLevel) : 自我评价等级
		   			tlv(subLevel) : 教师评价等级
		   		}
		   ]
		.....
     ]
 *         
 */
public class GrowthRecordEntry  extends BaseDBObject {
	public GrowthRecordEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public GrowthRecordEntry(ObjectId userId, String termType, ObjectId schoolId, String goodPerformance,
										String masterComment, List<LevelObject> levelList,List<QualityObject> qualityEducationList) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("uid", userId)
													 .append("tt",termType)
													 .append("scid",schoolId)
													 .append("gp",goodPerformance)
													 .append("mc",masterComment)
													 .append("ti",System.currentTimeMillis())
													 .append("lvs",MongoUtils.convert(MongoUtils.fetchDBObjectList(levelList)))
													 .append("qes",MongoUtils.convert(MongoUtils.fetchDBObjectList(qualityEducationList)));

		setBaseEntry(baseEntry);
	}
	public GrowthRecordEntry(ObjectId userId, String termType, ObjectId schoolId,
										List<LevelObject> levelList, List<QualityObject> qualityEducationList) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("uid", userId)
													 .append("tt",termType)
													 .append("scid",schoolId)
													 .append("gp",Constant.EMPTY)
													 .append("mc",Constant.EMPTY)
													 .append("ti",System.currentTimeMillis())
													 .append("lvs",MongoUtils.convert(MongoUtils.fetchDBObjectList(levelList)))
													 .append("qes",MongoUtils.convert(MongoUtils.fetchDBObjectList(qualityEducationList)));

		setBaseEntry(baseEntry);
	}
	
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("uid");
	}
	public void setUserId(String userId) {
		setSimpleValue("uid", userId);
	}
	
	public String getTermType() {
		return getSimpleStringValue("tt");
	}
	public void setTermType(String termType) {
		setSimpleValue("tt", termType);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("scid", schoolId);
	}
	
	public String getGoodPerformance() {
		return getSimpleStringValue("gp");
	}
	public void setGoodPerformance(String goodPerformance) {
		setSimpleValue("gp", goodPerformance);
	}
	
	public String getMasterComment() {
		return getSimpleStringValue("mc");
	}
	public void setMasterComment(String masterComment) {
		setSimpleValue("mc", masterComment);
	}
	
	public List<QualityObject> getQualityEducationList() {
		List<QualityObject> resultList =new ArrayList<QualityObject>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("qes");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new QualityObject((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setQualityEducationList(Collection<QualityObject> qualityEducationList) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(qualityEducationList);
		setSimpleValue("qes", MongoUtils.convert(list));
	}
	
	public List<LevelObject> getLevels() {
		List<LevelObject> resultList =new ArrayList<LevelObject>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("lvs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new LevelObject((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setLevels(Collection<LevelObject> levels) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(levels);
		setSimpleValue("lvs", MongoUtils.convert(list));
	}
}
