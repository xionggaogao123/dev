package com.fulaan.competition.service;

import com.db.Competition.CompetitionItemDetailDao;
import com.mongodb.DBObject;
import com.pojo.competition.CompetitionItemDetailEntry;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompetitionItemDetailService {
	private static final Logger logger = Logger.getLogger(CompetitionItemDetailService.class);
	private CompetitionItemDetailDao competitionItemDetailDao = new CompetitionItemDetailDao();
	
	
	/**
	 * 添加修改评比项目详细信息
	 * @param addList
	 * @return
	 */
	public void addOrEditCompetitionItemDetails(List<DBObject> addList, List<CompetitionItemDetailEntry> updList, List<ObjectId> delDetailIds)
	{
		if(addList!=null&&addList.size()>0){
			competitionItemDetailDao.addCompetitionItemDetailEntryList(addList);
		}
		for(CompetitionItemDetailEntry entry : updList){
			competitionItemDetailDao.updCompetitionItemDetailEntry(entry);
		}
		if(delDetailIds!=null&&delDetailIds.size()>0){
			delCompetitionItemDetailById(delDetailIds);
		}
	}

	/**
	 * 删除通过id删除
	 * @param ids
	 * @return
	 */
	public void delCompetitionItemDetailById(List<ObjectId> ids) {
		competitionItemDetailDao.removeCompetitionItemDetailEntryByIds(ids);
	}

	/**
	 * 删除通过id删除
	 * @param id
	 * @return
	 */
	public void delCompetitionItemDetailById(ObjectId id) {
		competitionItemDetailDao.removeCompetitionItemDetailEntryById(id);
	}

	/**
	 * 删除通过itemId删除
	 * @param itemId
	 * @return
	 */
	public void delCompetitionItemDetailByItemId(ObjectId itemId) {
		competitionItemDetailDao.removeCompetitionItemDetailEntryByItemId(itemId);
	}

	/**
	 * 查询评比项目明细
	 * @param itemId
	 * @return
	 */
	public List<Map<String,String>> getCompetitionItemDetailsByItemId(ObjectId itemId) {
		List<Map<String,String>> resList = new ArrayList<Map<String,String>>();
		List<CompetitionItemDetailEntry> list=getCompetitionItemDetailListByItemId(itemId);
		for(CompetitionItemDetailEntry entry : list){
			Map<String,String> mb = new HashMap<String,String>();
			mb.put("id", entry.getID().toString());
			mb.put("itemId", entry.getItemId().toString());
			mb.put("itemDetailName", entry.getItemDetailName());
			resList.add(mb);
		}
		return resList;
	}

	/**
	 * 查询评比项目明细
	 * @param itemId
	 * @return
	 */
	public List<CompetitionItemDetailEntry> getCompetitionItemDetailListByItemId(ObjectId itemId) {
		List<CompetitionItemDetailEntry> list=competitionItemDetailDao.getCompetitionItemDetailsByItemId(itemId);
		return list;
	}

	/**
	 * 查询评比项目明细
	 * @param itemId
	 * @return
	 */
	public Map<String,CompetitionItemDetailEntry> getCompetitionItemDetailMapByItemId(ObjectId itemId) {
		Map<String,CompetitionItemDetailEntry> map=new HashMap<String, CompetitionItemDetailEntry>();
		List<CompetitionItemDetailEntry> list=competitionItemDetailDao.getCompetitionItemDetailsByItemId(itemId);
		for(CompetitionItemDetailEntry entry : list){
			map.put(entry.getID().toString(),entry);
		}
		return map;
	}
	/**
	 * 查询评比项目明细
	 * @return
	 */
	public Map<String,List<CompetitionItemDetailEntry>> getCompetitionItemDetailsMapByComId(ObjectId comId) {
		Map<String,List<CompetitionItemDetailEntry>> map=new HashMap<String, List<CompetitionItemDetailEntry>>();
		List<CompetitionItemDetailEntry> list=competitionItemDetailDao.getCompetitionItemDetailsByComId(comId);
		for(CompetitionItemDetailEntry entry : list){
			List<CompetitionItemDetailEntry> sublist=map.get(entry.getItemId().toString());
			if(sublist==null){
				sublist=new ArrayList<CompetitionItemDetailEntry>();
			}
			sublist.add(entry);
			map.put(entry.getItemId().toString(),sublist);
		}
		return map;
	}
}
