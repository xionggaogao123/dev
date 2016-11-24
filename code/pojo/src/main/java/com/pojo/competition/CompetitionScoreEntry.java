package com.pojo.competition;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 评比分数
 * collectionName : competitionscore
 * @author cxy
 * 评比id : coid (competitionId)
 * 年级id ：grid (gradeId)
 * 年级名称 : grna (gradeName)
 * 班级id : clid (classId)
 * 班级名称 : clna (className)
 * 评比批次Id : baid (batchId)
 * 评比项目Id : itid (itemId)
 * 分数 : cs (competitionScore) double
 * 评比项目明细：cids (CompetitionItemDetails)
 * 	        CompetitionItemDetail
 * 			[
 * 				评比项目Id : itdid(itemDetailId)
 *              项目明细名称 : itdn(itemDetailName)
 *              项目明细说明 : itd(itemDetail)
 * 			]
 */
public class CompetitionScoreEntry extends BaseDBObject{
	public CompetitionScoreEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public CompetitionScoreEntry(ObjectId competitionId,ObjectId gradeId,ObjectId classId,ObjectId batchId,ObjectId itemId,double competitionScore) {
		super();

		BasicDBObject baseEntry = new BasicDBObject()
				.append("coid", competitionId)
				.append("grid", gradeId)
//				.append("grna", gradeName)
				.append("clid", classId)
//				.append("clna", className)
				.append("baid", batchId)
				.append("itid", itemId)
				.append("cs", competitionScore);

		setBaseEntry(baseEntry);
	}
	
	public ObjectId getCompetitionId() {
		return getSimpleObjecIDValue("coid");
	}
	public void setCompetitionId(String competitionId) {
		setSimpleValue("coid", competitionId);
	}
	
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("grid");
	}
	public void setGradeId(String gradeId) {
		setSimpleValue("grid", gradeId);
	}
	
//	public String getGradeName() {
//		return getSimpleStringValue("grna");
//	}
//
//	public void setGradeName(String gradeName) {
//		setSimpleValue("grna", gradeName);
//	}
	
	public ObjectId getClassId() {
		return getSimpleObjecIDValue("clid");
	}
	public void setClassId(String classId) {
		setSimpleValue("clid", classId);
	}
	
//	public String getClassName() {
//		return getSimpleStringValue("clna");
//	}
//
//	public void setClassName(String className) {
//		setSimpleValue("clna", className);
//	}
	
	public ObjectId getBatchId() {
		return getSimpleObjecIDValue("baid");
	}
	public void setBatchId(String batchId) {
		setSimpleValue("baid", batchId);
	}
	
	public ObjectId getItemId() {
		return getSimpleObjecIDValue("itid");
	}
	public void setItemId(String itemId) {
		setSimpleValue("itid", itemId);
	}
	
	public double getCompetitionScore() {
		return getSimpleDoubleValue("cs");
	}

	public void setCompetitionScore(double competitionScore) {
		setSimpleValue("cs", competitionScore);
	}

	public List<CompetitionItemDetail> getCompetitionItemDetails() {
		List<CompetitionItemDetail> detailList =new ArrayList<CompetitionItemDetail>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cids");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				detailList.add(new CompetitionItemDetail((BasicDBObject)o));
			}
		}
		return detailList;
	}

	public void setCompetitionItemDetails(Collection<CompetitionItemDetail> competitionItemDetails) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(competitionItemDetails);
		setSimpleValue("cids", MongoUtils.convert(list));
	}
}
