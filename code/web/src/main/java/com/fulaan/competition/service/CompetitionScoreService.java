package com.fulaan.competition.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.competition.CompetitionItemDetail;
import com.pojo.competition.CompetitionItemDetailEntry;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.Competition.CompetitionScoreDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.competition.CompetitionScoreEntry;
import com.sys.constants.Constant;

@Service
public class CompetitionScoreService {
	private static final Logger logger = Logger.getLogger(CompetitionService.class);
	private CompetitionScoreDao competitionScoreDao = new CompetitionScoreDao();
	
	/**
	 * 添加一条评比分数信息
	 * @param e
	 * @return
	 */
	public ObjectId addCompetitionScoreEntry(CompetitionScoreEntry e)
	{
		return competitionScoreDao.addCompetitionScoreEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的评比分数信息
	 * @param id
	 * @return
	 */
	public CompetitionScoreEntry getCompetitionScoreEntry(ObjectId id)
	{
		return competitionScoreDao.getCompetitionScoreEntry(id);
	}
	
	/**
	 * 根据各种ID查询某特定的评比分数信息
	 * @param id
	 * @return
	 */
	public CompetitionScoreEntry getCompetitionScoreEntryByIds(ObjectId competitionId,ObjectId batchId,ObjectId classId,ObjectId itemId)
	{
		return competitionScoreDao.getCompetitionScoreEntryByIds(competitionId, batchId, classId, itemId);
	}
	
	/**
	 * 删除一条评比分数
	 * @param id
	 */
	public void deleteCompetitionScore(ObjectId id){
		competitionScoreDao.deleteCompetitionScore(id);
	}
	
	/**
	 * 删除一个评比的所有评比分数
	 * @param id
	 */
	public void deleteAllCompetitionScoresOfCompetition(ObjectId competitionId){
		competitionScoreDao.deleteAllCompetitionScoresOfCompetition(competitionId);
	}
	
	/**
	 * 删除一个评比项目的所有评比分数
	 * @param id
	 */
	public void deleteAllCompetitionScoresOfCompetitionItem(ObjectId itemId){
		competitionScoreDao.deleteAllCompetitionScoresOfCompetitionItem(itemId);
	}
	
	/**
	 * 删除一个评比某个批次下某个年级的所有评比分数
	 * @param id
	 */
	public void deleteAllCompetitionScoresOfCompetitionBatchAndGradeId(ObjectId batchId,ObjectId gradeId){
		competitionScoreDao.deleteAllCompetitionScoresOfCompetitionBatchAndGradeId(batchId, gradeId);
	}
	
	/**
	 * 根据ID更新一条评比分数信息
	 */
	public void updateCompetitionScoreById(ObjectId id,double competitionScore){

		competitionScoreDao.updateCompetitionScoreById(id, competitionScore);
		
	}
	
	/**
	 * 根据相关评比ID信息更新一条评比分数信息
	 */
	public void updateCompetitionScoreByIds(ObjectId competitionId,ObjectId classId,ObjectId batchId,ObjectId itemId,int competitionScore){
		competitionScoreDao.updateCompetitionScoreByIds(competitionId, classId, batchId, itemId, competitionScore);
	}

	/**
	 * 根据相关评比ID信息更新一条评比分数信息
	 */
	public void updateCompetitionScoreByIds(ObjectId competitionId,ObjectId itemId,int competitionScore){
		competitionScoreDao.updateCompetitionScoreByIds(competitionId, itemId, competitionScore);
	}
	
	/**
	 * 根据评比ID和评比批次ID查询评比分数记录
	 * @return
	 */
	public List<CompetitionScoreEntry> queryCompetitionScoresByCompetitionIdAndBatchId(ObjectId competitionId,ObjectId competitionBatchId){
		return competitionScoreDao.queryCompetitionScoresByCompetitionIdAndBatchId(competitionId, competitionBatchId);
	}
	
	/**
	 * 根据评比ID和评比批次ID和年级班级查询评比分数记录
	 * @return
	 */
	public List<CompetitionScoreEntry> queryCompetitionScoresByCompetitionIdAndBatchIdAndGradeIdAndClassId(ObjectId competitionId,
															ObjectId competitionBatchId,ObjectId gradeId,ObjectId classId){
		return competitionScoreDao.queryCompetitionScoresByCompetitionIdAndBatchIdAndGradeIdAndClassId(competitionId, competitionBatchId, gradeId, classId);
	}

	/**
	 * 查询评比分数记录
	 * @return
	 */
	public List<CompetitionScoreEntry> getCompetitionScoreEntryByItemId(ObjectId itemId) {
		return competitionScoreDao.getCompetitionScoreEntryByItemId(itemId);
	}

	/**
	 * 评比分数记录 编辑评比项目明细
	 * @return
	 */
	public void editCompetitionItemDetails(ObjectId itemId, List<CompetitionItemDetail> addList, Map<String,CompetitionItemDetail> updMap, List<ObjectId> delDetailIds) {
		List<CompetitionScoreEntry> scoreEntryList = getCompetitionScoreEntryByItemId(itemId);
		for(CompetitionScoreEntry scoreEntry : scoreEntryList){
			List<CompetitionItemDetail> detailList =scoreEntry.getCompetitionItemDetails();
			if (detailList == null) {
				detailList=new ArrayList<CompetitionItemDetail>();
			}
			if (updMap!=null&&updMap.size()>0) {
				for(CompetitionItemDetail detail : detailList){
					CompetitionItemDetail itemDetail = updMap.get(detail.getItemDetailId().toString());
					if(itemDetail!=null) {
						if (!detail.getItemDetailName().equals(itemDetail.getItemDetailName())) {
							detail.setItemDetailName(itemDetail.getItemDetailName());
							detail.setItemDetail("");
						}
					}
				}
			}
			if (addList!=null&&addList.size()>0) {
				detailList.addAll(addList);
			}
			if(delDetailIds!=null&&delDetailIds.size()>0){
				for(int i=detailList.size()-1;i>=0;i--){
					CompetitionItemDetail detail=detailList.get(i);
					if(delDetailIds.contains(detail.getItemDetailId())){
						detailList.remove(i);
					}
				}
			}
			scoreEntry.setCompetitionItemDetails(detailList);
			competitionScoreDao.updateCompetitionItemDetailsById(scoreEntry.getID(), scoreEntry);
		}
	}

	/**
	 * 更新该评比的批次项目评比明细信息
	 */
	public void updateCompetitionScoreItemDetailById(ObjectId id, Map<String, String> updDetailMap) {
		CompetitionScoreEntry scoreEntry=getCompetitionScoreEntry(id);
		if(scoreEntry!=null&&scoreEntry.getCompetitionItemDetails()!=null) {
			for (CompetitionItemDetail detail : scoreEntry.getCompetitionItemDetails()) {
				String itemDetail = updDetailMap.get(detail.getItemDetailId().toString());
				if(itemDetail!=null){
					detail.setItemDetail(itemDetail);
				}
			}
		}
		competitionScoreDao.updateCompetitionItemDetailsById(scoreEntry.getID(), scoreEntry);
	}
}
