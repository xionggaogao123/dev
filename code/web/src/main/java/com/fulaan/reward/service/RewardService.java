package com.fulaan.reward.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.reward.RewardDao;
import com.pojo.reward.RewardEntry;

/**
 * 奖惩service
 * @author cxy
 *
 */
@Service
public class RewardService {
	
	private static final Logger logger = Logger.getLogger(RewardService.class);
	
	private RewardDao rewardDao = new RewardDao();
	
	/**
	 * 添加一条奖惩信息
	 * @param e
	 * @return
	 */
	public ObjectId addRewardEntry(RewardEntry e)
	{
		rewardDao.addRewardEntry(e);
		return e.getID();
	}
	
	/**
	 * 根据传的参数进行查询，
	 * @param gradeId default(null)
	 * @param classId default(null)
	 * @param rewardType default(ALL)
	 * @param rewardGrade default(ALL)
	 * @param studentName default(ALL)
	 * @return
	 */
	public List<RewardEntry> queryRewardsByfields(ObjectId gradeId,ObjectId classId,String rewardType,
			String rewardGrade,String studentName,ObjectId schoolId){
		
		
		return rewardDao.queryRewardsByfields(gradeId,classId,rewardType,rewardGrade,studentName,schoolId);
	}
	
	/**
	 * 根据ID更新一条奖惩信息
	 * @param id
	 * @param rewardType
	 * @param rewardGrade
	 * @param rewardDate
	 * @param rewardContent
	 * @param departments
	 * @param classes
	 */
	public void updateReward(ObjectId id,String rewardType,String rewardGrade,long rewardDate,
			String rewardContent,List<ObjectId> departments,List<ObjectId> classes){
		
		rewardDao.updateReward( id, rewardType, rewardGrade, rewardDate, rewardContent, departments, classes);
		
	}
	
	/**
	 * 删除一条
	 * @param id
	 */
	public void deleteReward(ObjectId id){
		
		rewardDao.deleteReward(id);
	}
	
	/**
	 * 查询某个学生的所有奖惩记录
	 * @param studentId 学生ID,String
	 * @return
	 */
	public List<RewardEntry> queryRewardsByUserId(String studentId){
		return rewardDao.queryRewardsByUserId(studentId);
	}
}
