package com.fulaan.competition.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.Competition.CompetitionDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.competition.CompetitionBatch;
import com.pojo.competition.CompetitionEntry;
import com.pojo.competition.CompetitionItem;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

@Service
public class CompetitionService {
	private static final Logger logger = Logger.getLogger(CompetitionService.class);
	private CompetitionDao competitionDao = new CompetitionDao();
	
	
	/**
	 * 添加一条评比信息
	 * @param e
	 * @return
	 */
	public ObjectId addCompetitionEntry(CompetitionEntry e)
	{
		return competitionDao.addCompetitionEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的评比信息
	 * @param id
	 * @return
	 */
	public CompetitionEntry getCompetitionEntry(ObjectId id)
	{
		return competitionDao.getCompetitionEntry(id);
	}
	
	/**
	 * 删除一条评比信息
	 * @param id
	 */
	public void deleteCompetition(ObjectId id){
		competitionDao.deleteCompetition(id);
	}
	
	/**
	 * 根据ID更新一条评比信息的评比项目
	 */
	public void updateCompetitionItems(ObjectId id,List<CompetitionItem> competitionItems){

		competitionDao.updateCompetitionItems(id, competitionItems);
		
	}
	
	/**
	 * 根据ID更新一条评比信息的评比批次
	 */
	public void updateCompetitionBatches(ObjectId id,List<CompetitionBatch> competitionBatches){

		competitionDao.updateCompetitionBatches(id, competitionBatches);
	}
	/**
	 * 根据评比ID和评比批次ID查询评比分数记录
	 * @param id
	 * @param competitionBatches
	 */
	public void updateCompetition(ObjectId id,String competitionName,String competitionPostscript,
			List<ObjectId> competitionRange,int redFlagNum){

		competitionDao.updateCompetition(id, competitionName, competitionPostscript, competitionRange,redFlagNum);
	}
	
	/**
	 * 根据学校ID和学期查询所有评比信息
	 * @param id
	 * @return
	 */
	public List<CompetitionEntry> getCompetitionsBySchoolIdAndTermType(ObjectId schoolId,String termType)
	{
		return competitionDao.getCompetitionsBySchoolIdAndTermType(schoolId, termType);
	}
	
	/**
	 * 修改某一个评比中某一个评比项目的信息
	 */
	public void updateCompetitionItemForOne(ObjectId competitionId,ObjectId itemId,String itemName,String itemPostscript,int itemFullScore)
	{
		competitionDao.updateCompetitionItemForOne(competitionId, itemId, itemName, itemPostscript, itemFullScore);
	}
	
	/**
	 * 为某一个评比新增一个评比项目
	 */
	public void addCompetitionItemforCompetition(ObjectId competitionId,CompetitionItem item)
	{
		competitionDao.addCompetitionItemforCompetition(competitionId, item);
	}
	
	/**
	 * 删除一个评比的一个评比项目
	 */
	public void deleteCompetitionItemForCompetition(ObjectId competitionId,CompetitionItem item)
	{
		competitionDao.deleteCompetitionItemForCompetition(competitionId, item);
	}

	public CompetitionEntry getCompetitionEntryByParam(ObjectId id, List<ObjectId> batchIds) {
		if(batchIds.size()>0){
			return competitionDao.getCompetitionEntryByParam(id, batchIds);
		}else{
			return competitionDao.getCompetitionEntry(id);
		}
	}

}
