package com.fulaan.level.service;

import java.util.List;


import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.level.LevelDao;
import com.pojo.level.LevelEntry;

/**
 * 等级设置service类
 * @author cxy
 *
 */
@Service
public class LevelService {

	
	private LevelDao levelDao = new LevelDao();
	
	/**
	 * 添加一条等级信息
	 * @param e
	 * @return
	 */
	public ObjectId addLevelEntry(LevelEntry e)
	{
		return levelDao.addLevelEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的等级信息
	 * @param id
	 * @return
	 */
	public LevelEntry getLevelEntry(ObjectId id)
	{
		return levelDao.getLevelEntry(id);
	}
	
	/**
	 * 删除一条等级
	 * @param id
	 */
	public void deleteLevel(ObjectId id){
		levelDao.deleteLevel(id);
	}
	
	/**
	 * 根据ID更新一条等级信息
	 */
	public void updateLevel(ObjectId id,String levelName,int scoreRange){
		levelDao.updateLevel(id, levelName, scoreRange);
	}
	
	/**
	 * 查询本校所有的等级记录
	 * @return
	 */
	public List<LevelEntry> queryLevelsBySchoolId(ObjectId schoolId){
		return levelDao.queryLevelsBySchoolId(schoolId);
	}
}
