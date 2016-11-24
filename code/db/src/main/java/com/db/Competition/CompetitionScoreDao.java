package com.db.Competition;

import java.util.ArrayList;
import java.util.List;

import com.pojo.competition.CompetitionItemDetail;
import com.pojo.competition.CompetitionItemDetailEntry;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.competition.CompetitionScoreEntry;
import com.sys.constants.Constant;
/**
 * 评比分数Dao
 * @author cxy
 *
 */
public class CompetitionScoreDao extends BaseDao{
	/**
	 * 添加一条评比分数信息
	 * @param e
	 * @return
	 */
	public ObjectId addCompetitionScoreEntry(CompetitionScoreEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的评比分数信息
	 * @param id
	 * @return
	 */
	public CompetitionScoreEntry getCompetitionScoreEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new CompetitionScoreEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 根据各种ID查询某特定的评比分数信息
	 * @param id
	 * @return
	 */
	public CompetitionScoreEntry getCompetitionScoreEntryByIds(ObjectId competitionId,ObjectId batchId,ObjectId classId,ObjectId itemId)
	{
		DBObject query =new BasicDBObject("coid",competitionId).append("baid", batchId).append("clid",classId).append("itid",itemId);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new CompetitionScoreEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 删除一条评比分数
	 * @param id
	 */
	public void deleteCompetitionScore(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query);
	}
	
	/**
	 * 删除一个评比的所有评比分数
	 * @param id
	 */
	public void deleteAllCompetitionScoresOfCompetition(ObjectId competitionId){
		DBObject query =new BasicDBObject("coid",competitionId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query);
	}
	
	/**
	 * 删除一个评比项目的所有评比分数
	 * @param id
	 */
	public void deleteAllCompetitionScoresOfCompetitionItem(ObjectId itemId){
		DBObject query =new BasicDBObject("itid",itemId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query);
	}
	
	/**
	 * 删除一个评比某个批次下某个年级的所有评比分数
	 * @param id
	 */
	public void deleteAllCompetitionScoresOfCompetitionBatchAndGradeId(ObjectId batchId,ObjectId gradeId){
		DBObject query =new BasicDBObject("baid",batchId).append("grid", gradeId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query);
	}
	/**
	 * 根据ID更新一条评比分数信息
	 */
	public void updateCompetitionScoreById(ObjectId id,double competitionScore){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("cs", competitionScore));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query, updateValue);
		
	}
	
	/**
	 * 根据相关评比ID信息更新一条评比分数信息
	 */
	public void updateCompetitionScoreByIds(ObjectId competitionId,ObjectId classId,ObjectId batchId,ObjectId itemId,int competitionScore){

		DBObject query =new BasicDBObject("coid",competitionId)
											.append("clid", classId)
											.append("baid", batchId)
											.append("itid", itemId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("cs", competitionScore));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query, updateValue);
		
	}

	/**
	 * 根据相关评比ID信息更新一条评比分数信息
	 */
	public void updateCompetitionScoreByIds(ObjectId competitionId,ObjectId itemId,int competitionScore){

		DBObject query =new BasicDBObject("coid",competitionId)
				.append("itid", itemId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject()
						.append("cs", competitionScore));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query, updateValue);
	}


	/**
	 * 根据评比ID和评比批次ID查询评比分数记录
	 * @return
	 */
	public List<CompetitionScoreEntry> queryCompetitionScoresByCompetitionIdAndBatchId(ObjectId competitionId,ObjectId competitionBatchId){
		BasicDBObject query = new BasicDBObject();
		query.append("coid", competitionId)
			 .append("baid",competitionBatchId);
		DBObject orderBy = new BasicDBObject("grna",Constant.DESC).append("clna",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_COMPETITION_SCORE,query,Constant.FIELDS,orderBy);
        List<CompetitionScoreEntry> resultList = new ArrayList<CompetitionScoreEntry>();
        for(DBObject dbObject:dbObjects){
        	CompetitionScoreEntry competitionScoreEntry = new CompetitionScoreEntry((BasicDBObject)dbObject);
        	resultList.add(competitionScoreEntry);
        }
		return resultList;
	}
	
	/**
	 * 根据评比ID和评比批次ID和年级班级查询评比分数记录
	 * @return
	 */
	public List<CompetitionScoreEntry> queryCompetitionScoresByCompetitionIdAndBatchIdAndGradeIdAndClassId(ObjectId competitionId,
															ObjectId competitionBatchId,ObjectId gradeId,ObjectId classId){
		BasicDBObject query = new BasicDBObject();
		query.append("coid", competitionId)
			 .append("baid",competitionBatchId)
			 .append("grid",gradeId)
			 .append("clid",classId);
		DBObject orderBy = new BasicDBObject("clna",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_COMPETITION_SCORE,query,Constant.FIELDS,orderBy);
        List<CompetitionScoreEntry> resultList = new ArrayList<CompetitionScoreEntry>();
        for(DBObject dbObject:dbObjects){
        	CompetitionScoreEntry competitionScoreEntry = new CompetitionScoreEntry((BasicDBObject)dbObject);
        	resultList.add(competitionScoreEntry);
        }
		return resultList;
	}

	/**
	 * 查询评比分数记录
	 * @return
	 */
	public List<CompetitionScoreEntry> getCompetitionScoreEntryByItemId(ObjectId itemId) {
		BasicDBObject query = new BasicDBObject();
		query.append("itid", itemId);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_COMPETITION_SCORE,query,Constant.FIELDS);
		List<CompetitionScoreEntry> resultList = new ArrayList<CompetitionScoreEntry>();
		for(DBObject dbObject:dbObjects){
			CompetitionScoreEntry competitionScoreEntry = new CompetitionScoreEntry((BasicDBObject)dbObject);
			resultList.add(competitionScoreEntry);
		}
		return resultList;
	}

	/**
	 * 评比分数记录 增加 修改评比项目明细
	 * @param id
	 * @param entry
	 */
	public void updateCompetitionItemDetailsById(ObjectId id, CompetitionScoreEntry entry) {
		DBObject query =new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(entry.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_SCORE, query, updateValue);
	}
}
