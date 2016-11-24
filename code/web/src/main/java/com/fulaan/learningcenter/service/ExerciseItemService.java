package com.fulaan.learningcenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pojo.user.AvatarType;
import com.sys.utils.AvatarUtils;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.school.ExerciseAnswerDao;
import com.db.school.ExerciseItemDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.app.SimpleDTO;
import com.pojo.exercise.ExerciseAnswerDTO;
import com.pojo.exercise.ExerciseAnswerEntry;
import com.pojo.exercise.ExerciseItemEntry;
import com.pojo.exercise.ExerciseItemStateDTO;
import com.pojo.exercise.StudentExerciseAnswerDTO;
import com.pojo.exercise.ExerciseItemStateDTO.UserItemAnswerDTO;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.exercise.ExerciseMixItem;
import com.pojo.exercise.ExerciseMixItemInfoDTO;
import com.pojo.exercise.ExerciseSubmitAnswerDTO;
import com.pojo.exercise.ExerciseSubmitAnswerDTO.Answer;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;

import org.springframework.stereotype.Service;

/**
 * 测试题目service
 * @author fourer
 *
 */
@Service
public class ExerciseItemService {

	private ExerciseItemDao exerciseItemDao =new ExerciseItemDao();
	private ExerciseAnswerDao exerciseAnswerDao =new ExerciseAnswerDao();
	private UserDao userDao =new UserDao();
	
	/**
	 * 一次插入多个大题
	 * @param list
	 */
	public void addEntrys(List<ExerciseItemEntry> list)
	{
		exerciseItemDao.addEntrys(list);
	}
	
	/**
	 * 根据documentId 查询
	 * @param documentId
	 * @param fields
	 * @return
	 */
	public List<ExerciseItemEntry> getExerciseItemEntrys(ObjectId documentId, DBObject fields)
	{
		return exerciseItemDao.getExerciseItemEntrys(documentId, fields);
	}
	
	
	/**
	 * 根据文档ID删除题目设置
	 * @param docId
	 */
	public void deleteExerciseItems(ObjectId docId)
	{
		exerciseItemDao.deleteExerciseItems(docId);
	}
	
	
	/**
	 * 根据小题号查询
	 * @param titleId
	 * @param fields
	 * @return
	 */
	public ExerciseItemEntry getExerciseItemEntryByTitleId(ObjectId titleId,ObjectId docId, DBObject fields)
	{
		return exerciseItemDao.getExerciseItemEntryByTitleId(titleId, docId, fields);
	}
	
	
	
	/**
	 * 统计每个文档题目个数
	 * @param ids
	 * @return
	 */
	public Map<ObjectId, Integer> statItemCount(Collection<ObjectId> ids)
	{
		return exerciseItemDao.statItemCount(ids);
	}
	
	
	/**
	 * 增加一个答题图片
	 * @param id
	 * @param pair
	 */
	public void addAnswerImage(ObjectId id,IdValuePair pair)
	{
		exerciseAnswerDao.addImage(id, pair);
	}

	public void addTempAnswerImage(ObjectId stuId, ObjectId titleId,IdValuePair pair)
	{
		exerciseAnswerDao.addTempImage(stuId, titleId, pair);
	}
	/**
	 * 删除一个答题图片
	 * @param id
	 * @param pair
	 */
	public void removeAnswerImage(ObjectId id,IdValuePair pair)
	{
		exerciseAnswerDao.removeImage(id, pair);
	}

	public void removeTempAnswerImage(ObjectId userId, ObjectId titleId,IdValuePair pair)
	{
		exerciseAnswerDao.removeTempImage(userId, titleId, pair);
	}
	
	
	/**
	 * 答案详情
	 * @param id
	 * @return
	 */
	public ExerciseAnswerEntry getExerciseAnswerEntry(ObjectId id)
	{
		return exerciseAnswerDao.getExerciseAnswerEntry(id);
	}
	
	/**
	 * 增加一个学生答案
	 * @param e
	 * @return
	 */
	public ObjectId addAnswerEntry(ExerciseAnswerEntry e, Boolean isTemp)
	{
		if(isTemp)
			exerciseAnswerDao.addTempEntry(e);
		return exerciseAnswerDao.addEntry(e);
	}
	
	
	/**
	 * 得到一个学生的试卷的答题情况
	 * @param did
	 * @param ui
	 * @return
	 * @throws ResultTooManyException 
	 */
	public List<StudentExerciseAnswerDTO> getStudentExerciseAnswerDTOs(ObjectId did,ObjectId ui) throws ResultTooManyException
	{
		
		List<ExerciseMixItem> list= exerciseItemDao.getExerciseMixItems(did, Constant.FIELDS);
		
		Map<ObjectId,StudentExerciseAnswerDTO> answerMap =new HashMap<ObjectId, StudentExerciseAnswerDTO>();
		
		for(ExerciseMixItem e:list)
		{
			answerMap.put(e.getItemId(), new StudentExerciseAnswerDTO(e));
		}
		
		List<ExerciseAnswerEntry> ansList=exerciseAnswerDao.getListByDocIdAndItemId(did, null, ui, Constant.FIELDS);
		
		for(ExerciseAnswerEntry ee:ansList)
		{
			
			StudentExerciseAnswerDTO dto=answerMap.get(ee.getTitleId());
			if(null!=dto)
			{
				dto.setAnswerId(ee.getID().toString());
				dto.setAnswer(ee.getUserAnswer());
				if(ee.getImages().size()>0)
				{
						for(IdValuePair idv:ee.getImages())
						{
							SimpleDTO sdto =new SimpleDTO(idv.getId().toString(), null, idv.getValue().toString());
							dto.getIamge().add(sdto);
						}
				}
				dto.setScore(ee.getUserScore());
			}
		}
		
		
		
		
		
		List<StudentExerciseAnswerDTO> retList=new  ArrayList<StudentExerciseAnswerDTO>(answerMap.values());
		
		
		
		
		
		
		
		Collections.sort(retList, new Comparator<StudentExerciseAnswerDTO>() {
			@Override
			public int compare(StudentExerciseAnswerDTO o1, StudentExerciseAnswerDTO o2) {
				
				try
				{
					String[] arr1=o1.getTitleId().split(Constant.SEPARATE_LINE);
					String[] arr2=o2.getTitleId().split(Constant.SEPARATE_LINE);
					int sort1=Integer.parseInt(arr1[0])*1000+Integer.parseInt(arr1[1]);
					int sort2=Integer.parseInt(arr2[0])*1000+Integer.parseInt(arr2[1]);
					return sort1-sort2;
				}catch(Exception ex)
				{
					
				}
				return 0;
			}
		});
		
		return retList;
	}
	
	
	
	/**
	 * 得到文档题目list
	 * @param id
	 * @return
	 */
	public List<ExerciseMixItemInfoDTO> getDocumentTitleDTOs(ObjectId id)
	{
		List<ExerciseMixItemInfoDTO> retList =new ArrayList<ExerciseMixItemInfoDTO>();
		List<ExerciseMixItem> list= exerciseItemDao.getExerciseMixItems(id, Constant.FIELDS);
		for(ExerciseMixItem e:list)
		{
			retList.add(new ExerciseMixItemInfoDTO(e));
		}
		
		Collections.sort(retList, new Comparator<ExerciseMixItemInfoDTO>() {
			@Override
			public int compare(ExerciseMixItemInfoDTO o1, ExerciseMixItemInfoDTO o2) {
				
				try
				{
					String[] arr1=o1.getTitleId().split(Constant.UNDERLINE);
					String[] arr2=o2.getTitleId().split(Constant.UNDERLINE);
					int sort1=Integer.parseInt(arr1[0])*1000+Integer.parseInt(arr1[1]);
					int sort2=Integer.parseInt(arr2[0])*1000+Integer.parseInt(arr2[1]);
					return sort1-sort2;
				}catch(Exception ex)
				{
					
				}
				return 0;
			}
		});
		return retList;
	}
	
	/**
	 * 根据文档ID查询ExerciseMixItem，返回Map
	 * @param id
	 * @return
	 */
	public Map<ObjectId,ExerciseMixItem> getExerciseMixItemList(ObjectId id)
	{
		Map<ObjectId,ExerciseMixItem> retMap =new HashMap<ObjectId, ExerciseMixItem>();
		List<ExerciseMixItem> list= exerciseItemDao.getExerciseMixItems(id, Constant.FIELDS);
		for(ExerciseMixItem item:list)
		{
			retMap.put(item.getItemId(), item);
		}
		return retMap;
	}
	
	/**
	 * 得到文档题目list,按照题目题号排序
	 * @param id
	 * @return
	 */
	public List<ExerciseMixItem> getDocumentTitleEntrys(ObjectId id)
	{
		List<ExerciseMixItem> list= exerciseItemDao.getExerciseMixItems(id, Constant.FIELDS);
		Collections.sort(list, new Comparator<ExerciseMixItem>() {
			@Override
			public int compare(ExerciseMixItem o1, ExerciseMixItem o2) {
				
				try
				{
					String[] arr1=o1.getTitleId().split(Constant.UNDERLINE);
					String[] arr2=o2.getTitleId().split(Constant.UNDERLINE);
					int sort1=Integer.parseInt(arr1[0])*1000+Integer.parseInt(arr1[1]);
					int sort2=Integer.parseInt(arr2[0])*1000+Integer.parseInt(arr2[1]);
					return sort1-sort2;
				}catch(Exception ex)
				{
					
				}
				return 0;
			}
		});
		return list;
	}
	
	/**
	 * 得到练习的答题情况
	 * @param documentId
	 * @param titleId
	 * @param userId
	 * @param fields
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<ExerciseAnswerEntry> getListByDocIdAndItemId(ObjectId documentId, ObjectId titleId,ObjectId userId, DBObject fields) throws ResultTooManyException
	{
		return exerciseAnswerDao.getListByDocIdAndItemId(documentId, titleId,userId, fields);
	}
	
	/**
	 *  得到考试答案以及统计情况
	 * @param documentId
	 * @param studentIds 如果是小练习情况，为该班班级学生ID
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<ExerciseItemStateDTO> getDocumentTitleAndAnswerDTOList(ObjectId documentId,Set<ObjectId> studentIds) throws ResultTooManyException
	{
	
		Map<ObjectId, ExerciseItemStateDTO> titleAndAnswerMap =new HashMap<ObjectId, ExerciseItemStateDTO>();
		Map<ObjectId, ExerciseMixItem> docTitleMap =new HashMap<ObjectId, ExerciseMixItem>();
		
		List<ExerciseMixItem> list= exerciseItemDao.getExerciseMixItems(documentId, Constant.FIELDS);
		ExerciseItemStateDTO statDTO;
		for(ExerciseMixItem e:list)
		{
			docTitleMap.put(e.getItemId(), e);
			statDTO=new ExerciseItemStateDTO(e);
			titleAndAnswerMap.put(e.getItemId(), statDTO);
		}
		List<ExerciseAnswerEntry>  docAndAnswerList = exerciseAnswerDao.getListByDocIdAndItemId(documentId, null,null, Constant.FIELDS);
		ExerciseMixItem title;
		for(ExerciseAnswerEntry eaEntry:docAndAnswerList)
		{
			if(null!=studentIds && !studentIds.contains(eaEntry.getUserId()))
			{
				continue;
			}
			
			title=docTitleMap.get(eaEntry.getTitleId());
			statDTO=titleAndAnswerMap.get(eaEntry.getTitleId());
			if(null==title || null==eaEntry || null ==statDTO)
			{
				continue;
			}
			handleItemStateDTO(statDTO, title, eaEntry);
		}
		List<ExerciseItemStateDTO> retList= new ArrayList<ExerciseItemStateDTO>(titleAndAnswerMap.values());
		

		Collections.sort(retList, new Comparator<ExerciseItemStateDTO>() {
			@Override
			public int compare(ExerciseItemStateDTO o1, ExerciseItemStateDTO o2) {
				
				try
				{
					String[] arr1=o1.getTitleId().split(Constant.SEPARATE_LINE);
					String[] arr2=o2.getTitleId().split(Constant.SEPARATE_LINE);
					int sort1=Integer.parseInt(arr1[0])*1000+Integer.parseInt(arr1[1]);
					int sort2=Integer.parseInt(arr2[0])*1000+Integer.parseInt(arr2[1]);
					return sort1-sort2;
				}catch(Exception ex)
				{
					
				}
				return 0;
			}
		});
		
		
		return retList;
	}


	/**
	 * 得到某个题目的具体答案以及统计情况
	 * @param docId
	 * @param titleId
	 * @param studentIds 之查找某个班级的学生
	 * @return 
	 * @throws ResultTooManyException
	 */
	public ExerciseItemStateDTO getDocumentTitleAndAnswerDTO(ObjectId docId,ObjectId titleId,Set<ObjectId> studentIds) throws ResultTooManyException
	{
		ExerciseMixItem title= exerciseItemDao.getExerciseItemEntry(docId,titleId);
		List<ExerciseAnswerEntry>  docAndAnswerList = exerciseAnswerDao.getListByDocIdAndItemId(docId, titleId, null, Constant.FIELDS);
		if(null!=studentIds && studentIds.size()>Constant.ZERO)
		{
		  List<ExerciseAnswerEntry> studentAnswerList=new ArrayList<ExerciseAnswerEntry>();
		  for(ExerciseAnswerEntry eae:docAndAnswerList)
		  {
			  if(studentIds.contains(eae.getUserId()))
			  {
				  studentAnswerList.add(eae);
			  }
		  }
		  docAndAnswerList=studentAnswerList;
		}
		
		ExerciseItemStateDTO answer=new ExerciseItemStateDTO(title);
		List<ObjectId> userIds =MongoUtils.getFieldObjectIDs(docAndAnswerList, "ui");
		Map<ObjectId, UserEntry> userMaps=userDao.getUserEntryMap(studentIds, new BasicDBObject("nm",1).append("avt",1));
		UserEntry ue= null;
		UserItemAnswerDTO userAnswerDTO=null;
		for(ExerciseAnswerEntry anser:docAndAnswerList)
		{
			ue=userMaps.get(anser.getUserId());
			if(null==ue) {
				continue;
			}
			userAnswerDTO =new UserItemAnswerDTO(anser);
			userAnswerDTO.setName(ue.getUserName());
			userAnswerDTO.setAvatar(AvatarUtils.getAvatar(ue.getAvatar(), AvatarType.MIN_AVATAR.getType()));
			answer.addUserAnswer(ue.getID().toString(), userAnswerDTO);
			handleItemStateDTO(answer, title, anser);
		}
		List<UserItemAnswerDTO> unCommitted = new ArrayList<UserItemAnswerDTO>();
		studentIds.removeAll(userIds);
		for(ObjectId stuId : studentIds){
			ue=userMaps.get(stuId);
			if(null==ue) {
				continue;
			}
			UserItemAnswerDTO userItemAnswerDTO = new UserItemAnswerDTO();
			userItemAnswerDTO.setName(ue.getUserName());
			unCommitted.add(userItemAnswerDTO);
		}
		answer.setUnCommitted(unCommitted);
		return answer;
	}
	
	
	/**
	 * 根据ExerciseMixItem 和 ExerciseAnswerEntry 创建 ExerciseItemStateDTO
	 * @param statDTO
	 * @param title
	 * @param eaEntry
	 */
	private void handleItemStateDTO(ExerciseItemStateDTO statDTO,
			ExerciseMixItem title, ExerciseAnswerEntry eaEntry) {
		statDTO.increaseAnswerCount();
		if(title.getType()== ExerciseItemType.SINGLECHOICE.getType() || title.getType()== ExerciseItemType.TRUE_OR_FALSE.getType() || title.getType()== ExerciseItemType.GAP.getType()) //自动批改
		{
//			boolean isRight =isAnswerRight(title,eaEntry);
			boolean isRight =eaEntry.getIsRight()==1;
			if(isRight)
			{
				statDTO.increaseRight();
			}
		}
		if(title.getType()== ExerciseItemType.SINGLECHOICE.getType())
		{
			if(StringUtils.isNotBlank(eaEntry.getUserAnswer()))
			{
				
				String[] ansArr=StringUtils.split(eaEntry.getUserAnswer(),Constant.COMMA);
				for(String s:ansArr)
				{
						statDTO.increaseAnswer(s==null ? "" : s);
				}
			}
		}
		if(title.getType()== ExerciseItemType.TRUE_OR_FALSE.getType()  )
		{
				statDTO.increaseAnswer(eaEntry.getUserAnswer()==null ? "" : eaEntry.getUserAnswer());
		}
		
		if(title.getType()== ExerciseItemType.SUBJECTIVE.getType() || title.getType()== ExerciseItemType.GAP.getType())
		{
			if(eaEntry.getUserScore()>Constant.NEGATIVE_ONE)
			{
				statDTO.increaseScoreCount();
				statDTO.increaseAnswer(String.valueOf(eaEntry.getUserScore()));
			}
		}
	}
	/**
	 * 查看学生答题情况;用于学生查看
	 * @param studentId 学生ID
	 * @param docId 文档id
	 * @return
	 */
	public List<ExerciseAnswerDTO> getStudentDocumentAnswerDTOs(ObjectId studentId,ObjectId docId, Boolean isTemp)
	{
		List<ExerciseAnswerDTO> retList =new ArrayList<ExerciseAnswerDTO>();
		List<ExerciseMixItem> titleList= exerciseItemDao.getExerciseMixItems(docId, Constant.FIELDS);
		
		List<ObjectId> docList =new ArrayList<ObjectId>();
		docList.add(docId);
		List<ExerciseAnswerEntry> answerList= exerciseAnswerDao.getList(studentId, docList, Constant.FIELDS, isTemp);
		Map<ObjectId, ExerciseAnswerEntry> answerMap =new HashMap<ObjectId, ExerciseAnswerEntry>();
		
		for(ExerciseAnswerEntry answer:answerList)
		{
			answerMap.put(answer.getTitleId(), answer);
		}
		
		ExerciseAnswerEntry answer;
		for(ExerciseMixItem title:titleList)
		{
			answer=answerMap.get(title.getItemId());
			retList.add(new ExerciseAnswerDTO(title,answer));
		}
		return retList;
		
	}

	/**
	 * 学生提交答题
	 * @param userId
	 * @param docId
	 * @param dto
	 * @param alreadyTitleIDs 已经上传过图片的timu
	 */
	public void studentSubmitAnswer(ObjectId userId,ObjectId docId,ExerciseSubmitAnswerDTO dto,List<ObjectId> alreadyTitleIDs, Boolean isTemp)
	{

		List<ExerciseAnswerEntry> insertList =new ArrayList<ExerciseAnswerEntry>();
		List<ExerciseMixItem> titleList= exerciseItemDao.getExerciseMixItems(docId, Constant.FIELDS);
		Map<ObjectId, ExerciseMixItem> titleMap =new HashMap<ObjectId, ExerciseMixItem>();
		
		
		
		
		
		for(ExerciseMixItem e:titleList)
		{
			titleMap.put(e.getItemId(), e);
		}
		ExerciseAnswerEntry answerEntry=null;
		ExerciseMixItem titleEntry=null;
		ObjectId titleId;
		List<IdValuePair> iamge=null;
		for(Answer an:dto.getAnswerList())
		{
			if(null== an.getTitleId() ||  !ObjectId.isValid(an.getTitleId()))
			{
				continue;
			}
			titleId=new ObjectId(an.getTitleId());
			titleEntry=titleMap.get(titleId);
			if(null==titleEntry)
			{
				continue;
			}
			//主观题
			if(titleEntry.getType()== ExerciseItemType.SUBJECTIVE.getType())
			{
			  answerEntry=new ExerciseAnswerEntry(docId, titleId, userId, an.getAnswer(), Constant.NEGATIVE_ONE, Constant.NEGATIVE_ONE, iamge);
			}
			else
			{
				boolean isRight =isAnswerRight(titleEntry,an.getAnswer());
				
				
				double score=isRight?titleEntry.getScore():Constant.DEFAULT_VALUE_DOUBLE;
				
				String answer=an.getAnswer();
				if(StringUtils.isNotBlank(answer))
				{
					answer=answer.replace(Constant.COMMA, Constant.EMPTY);
				}
				answerEntry=new ExerciseAnswerEntry(docId, titleId, userId, answer, score, isRight?Constant.ONE:Constant.ZERO, iamge);
			}
			if(null!=alreadyTitleIDs  &&   alreadyTitleIDs.indexOf(titleId) >=0     )
			{
				exerciseAnswerDao.updateAnswer(docId,titleId,userId,answerEntry.getUserAnswer(),answerEntry.getUserScore(), answerEntry.getIsRight(), isTemp);
			}
			else
			{
			  insertList.add(answerEntry);
			}
		}
		if(insertList.size() > 0){
			exerciseAnswerDao.addSExerciseAnswerEntrys(insertList, isTemp);
		}

	}
	/**
	 * 更改学生分数
	 * @param ip
	 */
	public void updateStudentAnswerScore(IdValuePair ip) {
		exerciseAnswerDao.updateScore(ip.getId(), (Double)ip.getValue());
	}
	/**
	 * 更改答案
	 * @param ip
	 */
	public void updateAnswer(ObjectId id,String answer,double userScore)
	{
		exerciseAnswerDao.updateAnswer(id,answer,userScore);
	}
	

	
	/**
	 * 根据_id集合查询
	 * @param ids
	 * @param fields
	 * @return
	 */
	public List<ExerciseAnswerEntry>  getList(List<ObjectId> ids,DBObject fields)
	{
		return exerciseAnswerDao.getList(ids, fields);
	}
	
	private boolean isAnswerRight(ExerciseMixItem title,ExerciseAnswerEntry answer)
	{
		return isAnswerRight(title,answer.getUserAnswer());
	}
	private boolean isAnswerRight(ExerciseMixItem title,String answer)
	{
		if(StringUtils.isBlank(answer))
		{
			return false;
		}
		String rightAnswer=title.getAnswers().toLowerCase();
		String userAnswer=answer.toLowerCase();
		if(title.getType()== ExerciseItemType.SINGLECHOICE.getType())
		{
			
			
			String [] ansArr=StringUtils.split(userAnswer,Constant.COMMA);
			
			if(ansArr.length!=rightAnswer.length())
			{
				return false;
			}
			for(String ans:ansArr)
			{
				if(rightAnswer.indexOf(ans)<0)
				{
					return false;
				}
			}
			return true;
		}
		
		if(title.getType()== ExerciseItemType.TRUE_OR_FALSE.getType() || title.getType()== ExerciseItemType.GAP.getType() )
		{
			return rightAnswer.equals(userAnswer);
		}
		return false;
	}

	/**
	 * 得到学生的总成绩
	 * @param eid
	 * @param stuId
	 * @return
	 */
	public Double getStuScore(ObjectId eid, ObjectId stuId) {
		try {
			List<ExerciseAnswerEntry> list = exerciseAnswerDao.getListByDocIdAndItemId(eid, null, stuId, Constant.FIELDS);
			Double score = 0D;
			for(ExerciseAnswerEntry exerciseAnswerEntry : list) {
				Double itemScore = exerciseAnswerEntry.getUserScore();
				if(itemScore != -1) {
					score += itemScore;
				}
			}
			return score;
		} catch (ResultTooManyException e) {
			e.printStackTrace();
		}
		return 0D;
	}
	
	
	
	
}
