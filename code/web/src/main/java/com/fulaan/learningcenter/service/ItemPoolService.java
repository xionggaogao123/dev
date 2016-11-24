package com.fulaan.learningcenter.service;

import com.alibaba.fastjson.JSON;
import com.db.itempool.ItemPoolDao;
import com.db.itempool.StudentErrorItemDao;
import com.db.itempool.StudentExerciseDao;
import com.db.itempool.TestPaperDao;
import com.db.resources.ResourceDictionaryDao;
import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.fulaan.cache.CacheHandler;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.*;
import com.pojo.itempool.StudentErrorItemEntry.Item;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.school.*;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class ItemPoolService {

	
	private static final Logger logger =Logger.getLogger(ItemPoolService.class);
	
    private ResourceDictionaryDao dictionaryDao =new ResourceDictionaryDao();
	private StudentExerciseDao studentExerciseDao =new StudentExerciseDao();
	private StudentErrorItemDao studentErrorItemDao =new StudentErrorItemDao();
    private TestPaperDao testPaperDao =new TestPaperDao();
    private ItemPoolDao itemPoolDao =new ItemPoolDao();
	private ClassDao classDao = new ClassDao();
	private UserDao userDao = new UserDao();
	private ClassService classService = new ClassService();
	private SchoolService schoolService = new SchoolService();
	private TeacherClassSubjectService teacherClassSubjectService = new TeacherClassSubjectService();
	private ResourceDictionaryService resourceDictionaryService = new ResourceDictionaryService();

    /**
	 * 根据ID集合查询
	 * @param ids
	 * @return
	 */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(Collection<ObjectId> ids)
    {
    	 return dictionaryDao.getResourceDictionaryEntrys(ids);
    }
    
    
    /**
     * 详情
     * @param id
     * @return
     */
    public ResourceDictionaryEntry getResourceDictionaryEntry(ObjectId id)
    {
    	return dictionaryDao.getResourceDictionaryEntry(id);
    }
    
    
    public Map<ObjectId,ResourceDictionaryEntry> getResourceDictionaryEntryMap(Collection<ObjectId> ids)
    {
    	Map<ObjectId,ResourceDictionaryEntry> retMap =new HashMap<ObjectId, ResourceDictionaryEntry>();
    	List<ResourceDictionaryEntry> list =getResourceDictionaryEntrys(ids);
    	for(ResourceDictionaryEntry e:list)
    	{
    		retMap.put(e.getID(), e);
    	}
    	return retMap;
    }
    
    /**
     * 根据类型查询资源
     * @param type
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(int type)
    {
    	return dictionaryDao.getResourceDictionaryEntrys(type);
    }
    
    /**
     * 根据父节点查询
     * @param parentId
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId,int type)
    {
    	return dictionaryDao.getResourceDictionaryEntrys(parentId,type);
    }
    
    
    /**
     * 根据父节点查询
     * @param parentId
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId,int type,int skip,int limit)
    {
    	return dictionaryDao.getResourceDictionaryEntrys(parentId, type, skip, limit);
    }
    
    /**
     * 根据父类集合查询
     * @param parentIds
     * @return
     */
    public  List<ResourceDictionaryEntry> getResourceDictionaryEntrysByParents(Collection<ObjectId> parentIds,int type)
    {
    	return dictionaryDao.getResourceDictionaryEntrysByParents(parentIds,type,null);
    }
    
    /**
     * 根据父类集合查询
     * @param parentIds
     * @return
     */
    public  Map<ObjectId,ResourceDictionaryEntry> getResourceDictionaryEntryMapByParents(Collection<ObjectId> parentIds,int type)
    {
	    Map<ObjectId,ResourceDictionaryEntry> retMap =new HashMap<ObjectId, ResourceDictionaryEntry>();
		List<ResourceDictionaryEntry> list =getResourceDictionaryEntrysByParents(parentIds,type);
		for(ResourceDictionaryEntry e:list)
		{
			retMap.put(e.getID(), e);
		}
		return retMap;
    }
    
   
    /**
     * 根据科目查询题目类型
     * @param subject
     * @return
     */
    public List<ResourceDictionaryEntry> getItemTypeBySubject(ObjectId subject)
    {
		List<ObjectId> ids = new ArrayList<ObjectId>();
		ids.add(subject);
    	return dictionaryDao.getItemTypeBySubject(ids);
    }
	/**
	 * 学生的练习情况
	 * @param userId
	 * @param state
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<StudeneExerciseDTO> getStudeneExerciseDTOs(ObjectId userId,int state,int skip,int limit)
	{
		List<StudeneExerciseDTO> retList =new ArrayList<StudeneExerciseDTO>();
		List<StudentExerciseEntry>  list=studentExerciseDao.getStudentExerciseEntrys(userId, state, skip, limit);

		for(StudentExerciseEntry e:list)
		{
			try
			{
				String subjectName=SubjectType.getSubjectType(e.getSubjectType()).getName();
			    retList.add(new StudeneExerciseDTO(e,subjectName));
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		return retList;
	}
	
	/**
	 * 学生和家长的练习情况
	 * @param userIds
	 * @param state
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<StudeneExerciseDTO> getStudeneExerciseDTOs(List<ObjectId> userIds,int state,int skip,int limit)
	{
		List<StudeneExerciseDTO> retList =new ArrayList<StudeneExerciseDTO>();
		List<StudentExerciseEntry>  list=studentExerciseDao.getStudentExerciseEntrys(userIds, state, skip, limit);

		for(StudentExerciseEntry e:list)
		{
			try
			{
				String subjectName=SubjectType.getSubjectType(e.getSubjectType()).getName();
			    retList.add(new StudeneExerciseDTO(e,subjectName));
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		return retList;
	}
	
	
	/**
	 * 通过资源name找到SubjectType
	 * @param subjectId
	 * @return
	 */
	public SubjectType getSubjectType(ObjectId subjectId)
	{
		ResourceDictionaryEntry res=getResourceDictionaryEntry(subjectId);
		return SubjectType.getSubjectType(res.getName());
	}

	/**
	 * 获得学生练习
	 * @param id
	 * @return
	 */
	public StudentExerciseEntry getStudentExerciseEntry(ObjectId id)
	{
		return studentExerciseDao.getStudentExerciseEntry(id,Constant.FIELDS);
	}

	/**
	 * 删除学生练习
	 * @param id
	 */
	public void deleteStudentExercise(ObjectId id)
	{
		studentExerciseDao.deleteStudentExercise(id);
	}
	
	/**
	 * 根据难易程度和知识点查询题目个数
	 * @param kws
	 * @param level
	 * @return
	 */
	public List<IdNameValuePairDTO> getcount(Collection<ObjectId> kws,int level)
	{
		List<IdNameValuePairDTO> retList =new ArrayList<IdNameValuePairDTO>();
		List<BasicDBObject> list =itemPoolDao.count(kws, level);
		for(BasicDBObject dbo:list)
		{
			try
			{
				int type=dbo.getInt(Constant.ID);
				ExerciseItemType itemtype =ExerciseItemType.getExerciseItemType(type);
				IdNameValuePairDTO dto =new IdNameValuePairDTO();
				dto.setName(itemtype.getName());
				dto.setValue(dbo.getInt("count"));
				retList.add(dto);
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		return retList;
	}
	
	
	/**
	 * 得到学生每个科目的错题情况
	 * @param userid
	 * @return
	 */
	public List<IdNameValuePairDTO> getStudentSubjectErrorItemcount(ObjectId userid)
	{
		List<IdNameValuePairDTO> retList =new ArrayList<IdNameValuePairDTO>();
		List<IdNameValuePair> list=studentErrorItemDao.getSubjects(userid);
		
		IdNameValuePairDTO dto;
		for(IdNameValuePair ivp:list)
		{
			try
			{
				dto =new IdNameValuePairDTO();
				dto.setId(ivp.getIntId());
				dto.setName(ivp.getName());
				dto.setValue(ivp.getValue());
				retList.add(dto);
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		return retList;
	}
	
	
	/**
	 * 获得学生错题集知识面
	 * @param userId
	 * @param subject
	 * @return
	 */
	public List<IdValuePairDTO> getErrorItemScope(ObjectId userId,int subject)
	{
		List<IdValuePairDTO> retList =new ArrayList<IdValuePairDTO>();
		Set<ObjectId> ids=studentErrorItemDao.getErrorItemScopes(userId,subject);
		List<ResourceDictionaryEntry> list=dictionaryDao.getResourceDictionaryEntrys(ids);
		for(ResourceDictionaryEntry e:list)
		{
			IdValuePairDTO dto =new IdValuePairDTO(e.getID(), e.getName());
			retList.add(dto);
		}
		return retList;
	}
	
	
	/**
	 * 根据科目查询用户已经做过的题目ID
	 * @param userId
	 * @param subject
	 * @return
	 */
	public List<ObjectId> getDonedItemIds(ObjectId userId,int subject)
	{
		return studentErrorItemDao.getItemIds(userId, subject);
	}
	
	/**
	 * 得到题库题目
	 * @param id
	 * @return
	 */
	public ItemPoolEntry getItemPoolEntry(ObjectId id)
	{
		return itemPoolDao.getItemPoolEntry(id, true);
	}
	
	/**
	 * 根据用户ID和错题ID得到错题情况
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public StudentErrorItemEntry.Item getErrorItem(ObjectId userId,ObjectId itemId)
	{
		return studentErrorItemDao.getErrorItem(userId, itemId);
	}
	
	/**
	 * 查询构建ItemDTO
	 * @param item
	 * @return
	 */
	public ItemDTO getItemDTO(StudentErrorItemEntry.Item item)
	{
		if(null==item)
			return null;
		ItemPoolEntry ipl=itemPoolDao.getItemPoolEntry(item.getOriId(), true);
		
		List<ObjectId> scList=ipl.getScList();
		ResourceDictionaryEntry res=null;
		if(scList.size()>0)
		{
			res=	dictionaryDao.getResourceDictionaryEntry(scList.get(0));
		}
		return new ItemDTO(item, ipl, res);
	}
	
	/**
	 * 某个科目的错误信息
	 * @param userId
	 * @param subject
	 * @return
	 */
	public IdNameValuePair getSubject(ObjectId userId,int subject)
	{
		return studentErrorItemDao.getSubject(userId, subject);
	}
	
	/**
	 * 添加学生练习
	 */
	public ObjectId addStudentExerciseEntry(StudentExerciseEntry e)
	{
		return studentExerciseDao.addStudentExerciseEntry(e);
	}

	/**
	 * 
	 * @param gtys   学段
	 * @param subjectType  学科
	 * @param level  难易程度
	 * @param itemType  题型（连线，画图等等）
	 * @param scs  知识点集合
	 * @param psbs  教材集合
	 * @param origItemType  题型(选择、判断等等)
	 * @param owns  题目拥有者
	 * @param order
	 * @param field
	 * @param skip
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<ItemPoolEntry> getItemPoolEntryList(ObjectId eduId,ObjectId gtys, ObjectId subjectType, int level,
			List<ObjectId> itemType, List<ObjectId> scs, List<ObjectId> psbs,List<Integer> origItemType,List<ObjectId> owns, DBObject order,DBObject field,Integer skip,Integer limit) throws Exception
	{
		return itemPoolDao.getItemPoolEntryList(eduId, gtys, subjectType, level, itemType, scs, psbs, origItemType, owns, null, order, field, skip, limit, true);
	}
	
	
	
    
	/**
	 * 
	 * @param
	 * @param
	 * @param cltys
	 * @param
	 * @param psbs2
	 * @param
	 * @param donedItemList
	 * @return
	 * @throws Exception
	 */
	public List<ObjectId> getItemPoolIdsList(ObjectId eduId, List<ObjectId> cltys,List<ObjectId> psbs2,List<Integer> origItemType,List<ObjectId> donedItemList,List<ObjectId> owns) throws Exception
	{
		return itemPoolDao.getItemPoolIdsList(eduId, null, cltys, psbs2, origItemType, donedItemList, owns);
	}
	
	/**
	 * 根据查询条件，返回每个题目类型的题库ID集合
	 * 注意：不是全部返回，根据count来定
	 * @param gtys
	 * @param subjectType
	 * @param level
	 * @param itemType
	 * @param
	 * @param count
	 * @return
	 * @throws Exception
	 */
	
	public Map<com.pojo.exercise.ExerciseItemType,List<ObjectId>> getItemMap(ObjectId eduId, ObjectId gtys, ObjectId subjectType, int level,
			List<ObjectId> itemType, List<ObjectId> scs, List<ObjectId> psbs,List<Integer> origItemType,int count) throws Exception
	{
		return itemPoolDao.getItemList(eduId, gtys, subjectType, level, itemType, scs,  psbs,origItemType, count);
	}
	
	

	
	
	/**
	 * 更新学生练习的题目ID；选题完毕时调用
	 * @param id
	 * @param itemId
	 * @param type
	 */
	public void updateItem(ObjectId id,ObjectId itemId,ExerciseItemType type)
	{
		studentExerciseDao.updateItem(id, itemId, type);
	}
	
	public void updateItem(ObjectId id, ObjectId stage, ObjectId subject,int itemType, int level, double score, String book, String knowledge,
						   String question, String answer, String parse, int zc, int sc){
		ItemPoolEntry item=getItemPoolEntry(id);

		item.setXueduan(stage);
		item.setSubject(subject);
		item.setOrigType(itemType);
		item.setLevel(level);
		item.setScore(score);
		item.setQuestion(question);
		item.setAnswer(answer);
		item.setParseAnser(parse);
		String[] books = book.split(",");
		List<ObjectId> bookList = new ArrayList<ObjectId>();
		for(String s : books){
			bookList.add(new ObjectId(s));
		}
		item.setPsbList(bookList);
		String[] knowledges = knowledge.split(",");
		List<ObjectId> knowledgeList = new ArrayList<ObjectId>();
		for(String s : knowledges){
			knowledgeList.add(new ObjectId(s));
		}
		item.setScList(knowledgeList);
		ItemPoolEntry.ObjectiveItem objectiveItem = new ItemPoolEntry.ObjectiveItem("", 0, 0);
		if(1 == itemType){
			objectiveItem.setAnswer(answer);
			objectiveItem.setZuSelectCount(zc);
			objectiveItem.setSelectCount(sc);
		}
		item.setItem(objectiveItem);
		itemPoolDao.updateItem(item);
	}
	
	/**
	 * 更新一个练习答案；学生提交练习时调用
	 * @param id
	 * @param itemId
	 * @param type
	 * @param answer
	 * @param
	 */
	public void updateItemAnswer(ObjectId id,ObjectId itemId,ExerciseItemType type,String answer,int right)
	{
		studentExerciseDao.updateItemAnswer(id, itemId, type, answer, right);
	}
	
	

	/**
	 * 将试卷更新为完成状态
	 * @param id
	 */
	public void updateExerciseStateToFinish(ObjectId id)
	{
		studentExerciseDao.updateExerciseStateToFinish(id);
	}
	

	/**
	 * 处理学生的错题情况
	 * @param studentId 学生ID
	 * @param itemPoolEntry 题库
	 * @param myAnswer 我的答案
	 */
	public void handleStudentErrorItem(ObjectId studentId,ItemPoolEntry itemPoolEntry,String myAnswer) 
	{
		
		List<ObjectId> scList=itemPoolEntry.getScList();
		ObjectId scId=null;
		if(scList.size()>0)
		{
			scId=	scList.get(0);
		}
		
		
		boolean isExists= studentErrorItemDao.isExists(studentId);
		
		SubjectType type=getSubjectType(itemPoolEntry.getSubject());
		if(isExists) //如果存在
		{
			StudentErrorItemEntry.Item item=studentErrorItemDao.getErrorItem(studentId,itemPoolEntry.getID());
			if(null!=item)//该题目存在的情况
			{
				studentErrorItemDao.updateItem(studentId, itemPoolEntry.getID());
			}else
			{
				//科目是否存在
				IdNameValuePair value=studentErrorItemDao.getSubject(studentId, type.getId());
				if(null==value)
				{
					IdNameValuePair pair =new IdNameValuePair(type.getId(), type.getName(), Constant.ZERO);
					studentErrorItemDao.addSubject(studentId, pair);
				}
				item =new Item(itemPoolEntry.getID(), type.getId(), scId, myAnswer);
				studentErrorItemDao.addItem(studentId, type.getId(), item);
			}
		}
		else
		{
			IdNameValuePair pair =new IdNameValuePair(type.getId(), type.getName(), Constant.ONE);
			StudentErrorItemEntry sie =new StudentErrorItemEntry(studentId, pair, itemPoolEntry.getID(), scId,myAnswer);
			studentErrorItemDao.addStudentErrorItem(sie);
		}
	}
	
	
	/**
	 * 得到老师组卷
	 * @param userId
	 * @param grade
	 * @param subject
	 * @param state
	 * @param fields
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<TestPaperEntry> getTestPaperEntrys(ObjectId userId,int grade,int subject,int state,DBObject fields,int skip,int limit)
	{
		return testPaperDao.getTestPaperEntrys(userId, grade, subject, state, fields, skip, limit);
	}
	
	
	/**
	 * 得到老师组卷DTO
	 * @param userId
	 * @param grade
	 * @param subject
	 * @param state
	 * @param fields
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<TestPaperDetailDTO> getTestPaperDTOs(ObjectId userId,int grade,int subject,int state,DBObject fields,int skip,int limit)
	{
		List<ResourceDictionaryEntry> resList = dictionaryDao.getResourceDictionaryEntrys(2);
		Map<ObjectId, String> resMap = new HashMap<ObjectId, String>();
		if(null != resList && resList.size()>0){
			for(ResourceDictionaryEntry entry : resList){
				resMap.put(entry.getID(), entry.getName());
			}

		}
		List<TestPaperDetailDTO> retList =new ArrayList<TestPaperDetailDTO>(limit);
		List<TestPaperEntry> eList =getTestPaperEntrys(userId, grade, subject, state, fields, skip, limit);
		for(TestPaperEntry e:eList)
		{
			ObjectId subjectId = e.getSubject();
			TestPaperDetailDTO dto = new TestPaperDetailDTO(e);
			dto.setSubject(resMap.get(subjectId));
			UserEntry userEntry = userDao.getUserEntry(e.getTeacherId(), Constant.FIELDS);
			if(userEntry != null){
				dto.setUserName(userEntry.getUserName());
			} else {
				dto.setUserName("匿名");
			}
			retList.add(dto);
		}
		return retList;
	}
	
	/**
	 * 删除老师组卷
	 * @param id
	 */
	public void deleteTestPaper(ObjectId id)
	{
		testPaperDao.delete(id);
	}
	
	/**
	 * 组卷详情
	 * @param id
	 * @return
	 */
	public TestPaperEntry getTestPaperEntry(ObjectId id)
	{
		return testPaperDao.getTestPaperEntry(id);
	}
	
	/**
	 * 根据ID查询，返回map
	 * @param ids
	 * @param fields
	 * @return
	 */
	public Map<ObjectId, ItemPoolEntry> getItemPoolEntryMap(Collection<ObjectId> ids,DBObject fields)
	{
		Map<ObjectId, ItemPoolEntry> map = new HashMap<ObjectId, ItemPoolEntry>();
		map.putAll(itemPoolDao.getItemPoolEntryMap(ids, fields,true));
		map.putAll(itemPoolDao.getItemPoolEntryMap(ids, fields,false));
		return map;
	}
	
	/**
	 * 老师添加试卷
	 * @param e
	 * @return
	 */
	public ObjectId addTestPaperEntry(TestPaperEntry e)
	{
		return testPaperDao.addTestPaperEntry(e);
	}
	
	/**
	 * 删除老师试卷中的某个题目
	 * @param paperId
	 * @param type
	 * @param tid
	 */
	public void deleteTestPaperItemById(ObjectId paperId,ExerciseItemType type,ObjectId tid)
	{
		testPaperDao.deleteItem(paperId, type, tid);
	}
	/**
	 * 修改试卷，添加某个题目
	 * @param paperId
	 * @param type
	 * @param tid
	 */
	public void addItemToTestPaper(ObjectId paperId,ExerciseItemType type,ObjectId tid)
	{
		testPaperDao.addItem(paperId, type, tid);
	}
	/**
	 * 调整题目顺序时调用
	 * @param paperId
	 * @param type
	 * @param ids
	 */
	public void updateItems(ObjectId paperId,ExerciseItemType type,List<ObjectId> ids)
	{
		testPaperDao.updateItems(paperId, type, ids);
	}
	/**
	 * 试卷发布到班级
	 * @param id
	 * @param classes
	 */
	public void pushTestPaper(ObjectId id,List<ObjectId> classes)
	{
		testPaperDao.addClass(id, classes);
	}

	/**
	 * 用户间试卷推送
	 * @param id 试卷id
	 * @param userId 用户id
	 */
	public void copyTestPaperForUser(ObjectId id, ObjectId userId){
		//更新试卷状态
		testPaperDao.update(id, new BasicDBObject("st", 3));
		//copy试卷
		TestPaperEntry testPaperEntry = testPaperDao.getTestPaperEntry(id);
		testPaperEntry.setID(new ObjectId());
		testPaperEntry.setClasses(null);
		testPaperEntry.setTeacherId(testPaperEntry.getUi());
		testPaperEntry.setUi(userId);
		ObjectId newId = testPaperDao.addTestPaperEntry(testPaperEntry);
		System.out.println(id.toString());
		System.out.println(newId.toString());
	}
	
	

	/**
	 * 查询,缓存数据，并且返回第一个数据
	 * @param userId
	 * @param orderBy
	 * @param subject
	 * @param skip
	 * @param limit
	 * @return
	 */
	public StudentErrorItemEntry.Item getItem(ObjectId userId,int orderBy,int subject,int skip,int limit)
	{
		List<StudentErrorItemEntry.Item> list= studentErrorItemDao.getItems(userId, orderBy,subject, skip, limit);
		
		if(null!=list && list.size()>0)
		{
			String errorDatakey=	MessageFormat.format(CacheHandler.CACHE_ERRORITEM_DATA, String.valueOf(userId), String.valueOf(orderBy));
			String errorDataBegin=MessageFormat.format(CacheHandler.CACHE_ERRORITEM_BEGIN, String.valueOf(userId), String.valueOf(orderBy));
			StringBuilder builder =new StringBuilder();
			for(StudentErrorItemEntry.Item item:list)
			{
				builder.append(item.getOriId().toString()).append(Constant.COMMA);
			}
			CacheHandler.cache(errorDatakey, builder.toString(), Constant.SECONDS_IN_DAY);
			CacheHandler.cache(errorDataBegin, String.valueOf(skip), Constant.SECONDS_IN_DAY);
			
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param myId
	 * @param itemId 当前题目ID
	 * @param order 数序
	 * @param direction 0 上一题 1 下一题
	 * @return
	 */
	public StudentErrorItemEntry.Item getItem(ObjectId myId,ObjectId itemId,int order,int direction)
	{
		//存储的ID数据
		String errorDatakey=	MessageFormat.format(CacheHandler.CACHE_ERRORITEM_DATA,String.valueOf(myId),String.valueOf(order));
		
		String strData=CacheHandler.getStringValue(errorDatakey);
		String thisId="";
		if(StringUtils.isNotBlank(strData))
		{
			String[] strDataArr=StringUtils.split(strData, Constant.COMMA);
			List<String> itemIdList =Arrays.asList(strDataArr);
			int index =itemIdList.indexOf(itemId.toString());
			if(index>=0)
			{
				if(direction==0 && index!=0)
					thisId=strDataArr[index-1];
				if(direction==1 && index!=strDataArr.length-1)
					thisId=strDataArr[index+1];
				if(StringUtils.isNotBlank(thisId))
				{
					return getErrorItem(myId, new ObjectId(thisId));
				}
			}
		}
		return null;
	}
	
	/**
	 * 学生删除
	 * @param userId
	 * @param item
	 */
	public void deleteErrorItem(ObjectId userId,StudentErrorItemEntry.Item item)
	{
		studentErrorItemDao.removeItem(userId, item);
	}

	/**
	 * @author shanchao
	 * 根据资源字典表节点查找题目
	 * @param itemType  题型（选择、判断、填空、主观）
	 * @param level  难易程度
	 * @param user   用户（云题库，教师题库，校本题库，学生错题本）
	 * @param select  资源字典表节点(最后一级)
	 * @param isBook  是否是教材筛选（true：教材删选，false：知识点筛选）
	 * @param page 页数
	 * @param pageSize 每页个数
	 * @return
	 */
	public Map<String, Object> getItems(ObjectId eduId, int itemType, int level, String user, List<ObjectId> select, Boolean isBook, String regular, int page, int pageSize){
		Map<String, Object> retmap = new HashMap<String, Object>();
		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
		List<ObjectId> scs = null;
		List<ObjectId> psbs = null;
		if(isBook){
			psbs = select;
		} else {
			scs = select;
		}
		List<Integer> oty = new ArrayList<Integer>();
		if(0 == itemType){
			oty.add(1);
			oty.add(3);
			oty.add(4);
			oty.add(5);
		} else {
			oty.add(itemType);
		}
		List<ObjectId> owns = new ArrayList<ObjectId>();
		Boolean isCloud = true;
		if(!user.equals("")){
			owns.add(new ObjectId(user));
			isCloud = false;
		}
		List<ItemPoolEntry> itemPoolEntries = new ArrayList<ItemPoolEntry>();
		int count = 10;
		try {
			itemPoolEntries = itemPoolDao.getItemPoolEntryList(eduId, null, null, level, null, scs, psbs, oty, owns, regular, new BasicDBObject("_id",-1), Constant.FIELDS, (page - 1) * pageSize, pageSize, isCloud);
			count = itemPoolDao.countItemPoolEntryList(eduId, null, null, level, null, scs, psbs, oty, owns, isCloud);
		} catch(Exception e){
			e.printStackTrace();
		}
		if(null!=itemPoolEntries && itemPoolEntries.size()>0){
			List<ObjectId> scopeList=new ArrayList<ObjectId>();
			if(null != itemPoolEntries){
				for(ItemPoolEntry entry : itemPoolEntries){
					scopeList.addAll(entry.getScList());
				}
			}
			Map<ObjectId,ResourceDictionaryEntry> map = getResourceDictionaryEntryMap(scopeList);
			ResourceDictionaryEntry kpe;
			for(ItemPoolEntry e:itemPoolEntries) {
				try {
					kpe=map.get(e.getScList().get(0));
					itemDTOList.add(new ItemDTO(e, kpe));
				}catch(Exception ex) {
					logger.error("", ex);
				}
			}
		}
		retmap.put("dtoList", itemDTOList);
		retmap.put("count", count);
		return retmap;
	}

	/**
	 * 查找题目的详细信息，包括全部的资源字典表路径
	 * @param id
	 * @return
	 */
	public Map<String, Object> getItemDetail(ObjectId id){
		Map<String, Object> retMap = new HashMap<String, Object>();
		ItemPoolEntry itemPoolEntry = itemPoolDao.getItemPoolEntry(id, false);
		retMap.put("id", id.toString());
		retMap.put("itemType", itemPoolEntry.getOrigType());
		retMap.put("level", itemPoolEntry.getLevel());
		retMap.put("score", itemPoolEntry.getScore());
		retMap.put("question", itemPoolEntry.getQuestion());
		retMap.put("answer", itemPoolEntry.getAnswer());
		retMap.put("parse", itemPoolEntry.getParseAnser());
		ItemPoolEntry.ObjectiveItem objectiveItem = itemPoolEntry.getItem();
		retMap.put("kg_an", objectiveItem.getAnswer());
		retMap.put("kg_zc", objectiveItem.getZuSelectCount());
		retMap.put("kg_sc", objectiveItem.getSelectCount());
		List<ObjectId> book = itemPoolEntry.getPsbList();//教材版本
		List<List<IdValuePairDTO>> bookList = new ArrayList<List<IdValuePairDTO>>();
		for(ObjectId objectId : book){
			ResourceDictionaryEntry resourceDictionaryEntry = dictionaryDao.getResourceDictionaryEntry(objectId);
			List<IdValuePairDTO> idValuePairDTOList = new ArrayList<IdValuePairDTO>();
			List<IdValuePair> idValuePairs = resourceDictionaryEntry.getParentInfos();
			for(IdValuePair pair : idValuePairs){
				idValuePairDTOList.add(new IdValuePairDTO(pair));
			}
			idValuePairDTOList.add(new IdValuePairDTO(resourceDictionaryEntry.getID(), resourceDictionaryEntry.getName()));
			bookList.add(idValuePairDTOList);
		}
		List<ObjectId> knowledge = itemPoolEntry.getScList();//知识点
		List<List<IdValuePairDTO>> knowledgeList = new ArrayList<List<IdValuePairDTO>>();
		for(ObjectId objectId : knowledge){
			ResourceDictionaryEntry resourceDictionaryEntry = dictionaryDao.getResourceDictionaryEntry(objectId);
			List<IdValuePairDTO> idValuePairDTOList = new ArrayList<IdValuePairDTO>();
			List<IdValuePair> idValuePairs = resourceDictionaryEntry.getParentInfos();
			for(IdValuePair pair : idValuePairs){
				idValuePairDTOList.add(new IdValuePairDTO(pair));
			}
			idValuePairDTOList.add(new IdValuePairDTO(resourceDictionaryEntry.getID(), resourceDictionaryEntry.getName()));
			knowledgeList.add(idValuePairDTOList);
		}
		retMap.put("bookList", bookList);
		retMap.put("knowledgeList", knowledgeList);
		return retMap;
	}

	/**
	 * 推送
	 * @param id 被推送题目id
	 * @param ownerId 推送到用户的id（教师id、学校id）
	 */
	public void push(ObjectId id, ObjectId ownerId, Boolean isCloud){
		ItemPoolEntry itemPoolEntry = itemPoolDao.getItemPoolEntry(id, isCloud);
		String answer = itemPoolEntry.getAnswer();
		answer = answer.replaceAll("(<br/>|：|答案)", "");
		String parse = itemPoolEntry.getParseAnser();
		if(answer.contains("解析")){
			String[] ans = answer.split("解析");
			answer = ans[0];
			if(ans.length>1){
				parse = ans[1];
			}

		}

		ItemPoolEntry newItemPoolEntry = new ItemPoolEntry(
				itemPoolEntry.getXueduan(),
				itemPoolEntry.getSubject(),
				itemPoolEntry.getLevel(),
				itemPoolEntry.getItemType(),
				itemPoolEntry.getScList(),
				itemPoolEntry.getScore(),
				itemPoolEntry.getQuestion(),
				answer,
				parse,
				itemPoolEntry.getItem(),
				itemPoolEntry.getPsbList(),
				itemPoolEntry.getGradeList(),
				itemPoolEntry.getOrigType()
				);
		List<ObjectId> ownerList = new ArrayList<ObjectId>();
		ownerList.add(ownerId);
		newItemPoolEntry.setOwnList(ownerList);
		ObjectId newid = itemPoolDao.addItemPoolEntry(newItemPoolEntry, false);
		System.out.println(id + "     " + newid);
	}

	/**
	 * 删除
	 * @param id
	 * @param ownerId
	 * @param isCloud
	 */
	public void remove(ObjectId id, ObjectId ownerId, Boolean isCloud){
		itemPoolDao.remove(id, ownerId, isCloud);
	}

	/**
	 * 删除
	 * @param id
	 * @param isCloud
	 */
	public void eduRemove(ObjectId id, ObjectId eduId, Boolean isCloud){
		itemPoolDao.eduRemove(id, eduId, isCloud);
	}

	/**
	 * 新增单个题目
	 * @param stage
	 * @param subject
	 * @param itemType
	 * @param level
	 * @param score
	 * @param book
	 * @param knowledge
	 * @param question
	 * @param answer
	 * @param parse
	 * @param ownerId
	 */
	public ObjectId save(String id, ObjectId stage,  ObjectId subject,int itemType, int level, double score, String book, String knowledge,
					 String question, String answer, String parse, ObjectId ownerId, int zc, int sc){
		ItemPoolEntry itemPoolEntry = new ItemPoolEntry();
		if("" != id){
			itemPoolEntry.setID(new ObjectId(id));
		}
		itemPoolEntry.setXueduan(stage);
		itemPoolEntry.setSubject(subject);
		itemPoolEntry.setOrigType(itemType);
		itemPoolEntry.setLevel(level);
		itemPoolEntry.setScore(score);
		itemPoolEntry.setQuestion(question);
		itemPoolEntry.setAnswer(answer);
		itemPoolEntry.setParseAnser(parse);
		List<ObjectId> ownerList = new ArrayList<ObjectId>();
		ownerList.add(ownerId);
		itemPoolEntry.setOwnList(ownerList);
		String[] books = book.split(",");
		List<ObjectId> bookList = new ArrayList<ObjectId>();
		for(String s : books){
			bookList.add(new ObjectId(s));
		}
		itemPoolEntry.setPsbList(bookList);
		String[] knowledges = knowledge.split(",");
		List<ObjectId> knowledgeList = new ArrayList<ObjectId>();
		for(String s : knowledges){
			knowledgeList.add(new ObjectId(s));
		}
		itemPoolEntry.setScList(knowledgeList);
		ItemPoolEntry.ObjectiveItem objectiveItem = new ItemPoolEntry.ObjectiveItem("", 0, 0);
		if(1 == itemType){
			objectiveItem.setAnswer(answer);
			objectiveItem.setZuSelectCount(zc);
			objectiveItem.setSelectCount(sc);
		}
		itemPoolEntry.setItem(objectiveItem);
		return itemPoolDao.addItemPoolEntry(itemPoolEntry, false);
	}


	/**
	 * 新增单个题目
	 * @param stage
	 * @param subject
	 * @param itemType
	 * @param level
	 * @param score
	 * @param book
	 * @param knowledge
	 * @param question
	 * @param answer
	 * @param parse
	 * @param ownerId
	 */
	public ObjectId save(ObjectId eduId, ObjectId userId, int isSaved, ObjectId stage,  ObjectId subject,int itemType, int level, double score, String book, String knowledge,
						 String question, String answer, String parse, ObjectId ownerId, int zc, int sc){
		ItemPoolEntry itemPoolEntry = new ItemPoolEntry();

		itemPoolEntry.setEduId(eduId);
		itemPoolEntry.setUserId(userId);
		itemPoolEntry.setIsSaved(isSaved);
		itemPoolEntry.setOwnType(1);
		itemPoolEntry.setNoEdIds(new ArrayList<ObjectId>());
		itemPoolEntry.setXueduan(stage);
		itemPoolEntry.setSubject(subject);
		itemPoolEntry.setOrigType(itemType);
		itemPoolEntry.setLevel(level);
		itemPoolEntry.setScore(score);
		itemPoolEntry.setQuestion(question);
		itemPoolEntry.setAnswer(answer);
		itemPoolEntry.setParseAnser(parse);
		List<ObjectId> ownerList = new ArrayList<ObjectId>();
		ownerList.add(ownerId);
		itemPoolEntry.setOwnList(ownerList);
		String[] books = book.split(",");
		List<ObjectId> bookList = new ArrayList<ObjectId>();
		for(String s : books){
			bookList.add(new ObjectId(s));
		}
		itemPoolEntry.setPsbList(bookList);
		String[] knowledges = knowledge.split(",");
		List<ObjectId> knowledgeList = new ArrayList<ObjectId>();
		for(String s : knowledges){
			knowledgeList.add(new ObjectId(s));
		}
		itemPoolEntry.setScList(knowledgeList);
		ItemPoolEntry.ObjectiveItem objectiveItem = new ItemPoolEntry.ObjectiveItem("", 0, 0);
		if(1 == itemType){
			objectiveItem.setAnswer(answer);
			objectiveItem.setZuSelectCount(zc);
			objectiveItem.setSelectCount(sc);
		}
		itemPoolEntry.setItem(objectiveItem);
		return itemPoolDao.addItemPoolEntry(itemPoolEntry, true);
	}
	/**
	 * 批量保存题目
	 * @param itemPoolEntries
	 * @return
	 */
	public String save(List<ItemPoolEntry> itemPoolEntries){
		itemPoolDao.addItemPoolEntrys(itemPoolEntries, false);
		String ids = "";
		int i = 1;
		for(ItemPoolEntry entry : itemPoolEntries){
			if(1 != i){
				ids += "," + entry.getID().toString();
			} else {
				ids += entry.getID().toString();
			}
			i++;
			System.out.println(entry.getID().toString());
		}
		return ids;
	}


	/**
	 * 根据老师id查找学生错题本中的题目
	 * @param itemType 题型
	 * @param level 难度
	 * @param teacherId 老师id
	 * @param select 知识点
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> getStudentErrorItems(int itemType, int level,ObjectId teacherId, List<ObjectId> select, int page, int pageSize){
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		List<ClassEntry> classEntryList=classDao.getClassEntryByTeacherId(teacherId, Constant.FIELDS);
		if(null != classEntryList){
			for(ClassEntry entry : classEntryList){
				studentIds.addAll(entry.getStudents());
			}
		}
		int begin = (page - 1) * pageSize;
		int end = page * pageSize;

		List<ObjectId> itemIds =  studentErrorItemDao.getOriIds(studentIds, select);
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<ObjectId> itemIdList = new ArrayList<ObjectId>();
		if(null != itemIds){
			int i = 0;
			for(ObjectId id : itemIds){
				if(map.containsKey(id.toString())){
					map.put(id.toString(),map.get(id.toString())+1);
				} else {
					if(i>=begin && i<end){
						itemIdList.add(id);
					}
					map.put(id.toString(),1);
					i++;
				}
			}
		}
		//题型
		List<Integer> oty = new ArrayList<Integer>();
		if(0 == itemType){
			oty.add(1);
			oty.add(3);
			oty.add(4);
			oty.add(5);
		} else {
			oty.add(itemType);
		}
		List<ItemPoolEntry> itemPoolEntries = itemPoolDao.getItemPoolEntryList(itemIds, level, oty, begin, end, Constant.FIELDS);
		Map<String, Object> retMap = new HashMap<String, Object>();

		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
		if(null!=itemPoolEntries && itemPoolEntries.size()>0){
			List<ObjectId> scopeList=new ArrayList<ObjectId>();
			if(null != itemPoolEntries){
				for(ItemPoolEntry entry : itemPoolEntries){
					scopeList.addAll(entry.getScList());
				}
			}
			Map<ObjectId,ResourceDictionaryEntry> map1 = getResourceDictionaryEntryMap(scopeList);
			ResourceDictionaryEntry kpe;
			for(ItemPoolEntry e:itemPoolEntries) {
				try {
					kpe=map1.get(e.getScList().get(0));
					ItemDTO itemDTO = new ItemDTO(e, kpe);
					itemDTO.setTotalCount(map.get(e.getID().toString()));
					itemDTOList.add(itemDTO);
				}catch(Exception ex) {
					logger.error("", ex);
				}
			}
		}
		retMap.put("dtoList", itemDTOList);
		retMap.put("count", map.size());
		return retMap;
	}

	/**
	 * 老师统计所带班级学生在某道题目上的错误情况
	 * @param teacherId
	 * @param itemId
	 * @return
	 */
	public List<Map<String, Object>> getErrorItemEntrys(ObjectId teacherId, ObjectId itemId){
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		List<ClassEntry> classEntryList=classDao.getClassEntryByTeacherId(teacherId, Constant.FIELDS);
		Map<ObjectId, String> stuClassNameMap = new HashMap<ObjectId, String>();
		if(null != classEntryList){
			for(ClassEntry entry : classEntryList){
				studentIds.addAll(entry.getStudents());
				for(ObjectId id : entry.getStudents()){
					stuClassNameMap.put(id, entry.getName());
				}
			}
		}
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		Map<ObjectId,UserEntry> userEntryList = userDao.getUserEntryMap(studentIds, new BasicDBObject("nm", 1));
		List<StudentErrorItemEntry> errorItemEntries = studentErrorItemDao.getErrorItemEntrys(studentIds, itemId);
		if(null != errorItemEntries){
			for(StudentErrorItemEntry entry : errorItemEntries){
				ObjectId studentId = entry.getUi();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", studentId.toString());
				map.put("name", userEntryList.get(studentId).getUserName());
				map.put("className", stuClassNameMap.get(studentId));
				List<Item> items = entry.getItems();
				for(Item item : items){
					if(item.getOriId().equals(itemId)){
						map.put("count", item.getCount());
						break;
					}
				}
				retList.add(map);

			}
		}
		return retList;
	}

	public Map<String, Object> getXueduanAndSubject(ObjectId teacherId){
		Map<String, Object> model = new HashMap<String, Object>();
		List<ClassInfoDTO> classInfoDTOs = classService.findClassInfoByTeacherId(teacherId);
		if(null != classInfoDTOs && classInfoDTOs.size() > 0){
			String gradeId = classInfoDTOs.get(0).getGradeId();
			try {
				List<Grade> gradeList = schoolService.getTeacherGrades(teacherId);
				if(null != gradeList){
					for(Grade grade : gradeList){
						if(grade.getGradeId().toString().equals(gradeId)){
							int gradeType = grade.getGradeType();
							if(gradeType >= 1 && gradeType <= 6){
								model.put("xueduan", "55d41e47e0b064452581269a");
							} else if(gradeType >= 7 && gradeType <= 9){
								model.put("xueduan", "55d41e47e0b064452581269c");
							} else if(gradeType >= 10 && gradeType <= 12){
								model.put("xueduan", "55d41e47e0b064452581269e");
							}
						}
					}
				}
			} catch (IllegalParamException e){
				e.printStackTrace();
			}

			List<ObjectId> classIds = new ArrayList<ObjectId>();
			classIds.add(new ObjectId(classInfoDTOs.get(0).getId()));
			List<TeacherClassSubjectDTO>  tcsList = teacherClassSubjectService.getTeacherClassSubjectDTOList(teacherId, classIds);
			if(null != tcsList && tcsList.size() > 0){
				String subjectName = (String)tcsList.get(0).getSubjectInfo().getValue();
				model.put("subjectName", subjectName);
			}
		}

		return model;
	}

	public List<ItemDTO> getItemPoolEntryListByParam(ObjectId eduId, String searchName, int isSaved, String typeId, String propertyType, int pageNo, int pageSize, int itemType, int level) {
		List<ItemDTO> reList=new ArrayList<ItemDTO>();
		ObjectId xueduanId=null;
		ObjectId subjectId=null;
		List<ObjectId> ptIds=new ArrayList<ObjectId>();
		if(!"ALL".equals(typeId)) {
			ResourceDictionaryEntry rdEntry = resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(typeId));
			if (rdEntry != null){
				if (rdEntry.getType() == 1) {
					xueduanId = rdEntry.getID();
				} else if (rdEntry.getType() == 2) {
					subjectId = rdEntry.getID();
				} else {
					int propertyTypeInt=0;
					if ("tcv".equals(propertyType)) {
						propertyTypeInt=6;
					} else {
						propertyTypeInt=9;
					}

					if (rdEntry.getType() == propertyTypeInt) {
						ptIds.add(rdEntry.getID());
					} else {
						List<ObjectId> rdIds = getResourceDictionaryEntryIds(rdEntry.getID(), propertyTypeInt);
						ptIds.addAll(rdIds);
					}
				}
			}
		}
		int skip = pageNo < 1 ? 0 : ((pageNo - 1) * pageSize);
		List<ItemPoolEntry> ipList = itemPoolDao.getItemPoolEntryListByParam(eduId, searchName, isSaved, xueduanId, subjectId, propertyType, ptIds, itemType, level, skip, pageSize);
		List<ObjectId> userIds=new ArrayList<ObjectId>();
		List<ObjectId> bkKwIds=new ArrayList<ObjectId>();
		for(ItemPoolEntry item:ipList){
			if(item.getUserId()!=null){
				userIds.add(item.getUserId());
			}
			if(item.getPsbList()!=null&&item.getPsbList().size()>0) {
				bkKwIds.addAll(item.getPsbList());
			}
			if(item.getScList()!=null&&item.getScList().size()>0) {
				bkKwIds.addAll(item.getScList());
			}
		}
		Map<ObjectId, UserEntry> userMap=userDao.getUserEntryMap(userIds,new BasicDBObject("nm",1).append("si", 1));
		Map<ObjectId,ResourceDictionaryEntry> rdMap = getResourceDictionaryEntryMap(bkKwIds);
		DateTimeUtils time=new DateTimeUtils();
		for(ItemPoolEntry item:ipList){
			ItemDTO itemDTO=new ItemDTO(item);
			itemDTO.setIsSaved(item.getIsSaved());
			itemDTO.setUploadTime(time.getLongToStrTimeTwo(item.getID().getTime()));
			if(item.getUserId()!=null){
				UserEntry userEntry=userMap.get(item.getUserId());
				if(userEntry!=null){
					itemDTO.setUserName(userEntry.getUserName());
				}
			}else{
				itemDTO.setUserName("管理员");
			}
			ResourceDictionaryEntry RD=null;
			String bk="";
			if(item.getPsbList()!=null&&item.getPsbList().size()>0) {
				for (ObjectId sclId : item.getPsbList()) {
					RD= rdMap.get(sclId);
					if(RD!=null) {
						List<IdValuePair> idValueList = RD.getParentInfos();
						int i=0;
						for (IdValuePair idValue : idValueList) {
							i++;
							if ("".equals(bk)) {
								bk += idValue.getValue().toString();
							} else if (!bk.contains(RD.getName())) {
								if(i%2==0){
									bk += "/" + idValue.getValue().toString()+"<br>";
								}else if((i-1)%2==0) {
									bk += idValue.getValue().toString();
								}else {
									bk += "/" + idValue.getValue().toString();
								}
							}
						}
						bk += "/" + RD.getName();
					}
				}
			}
			itemDTO.setBk(bk);
			String kw="";
			if(item.getScList()!=null&&item.getScList().size()>0) {
				for (ObjectId sclId : item.getScList()) {
					RD= rdMap.get(sclId);
					if(RD!=null) {
						List<IdValuePair> idValueList = RD.getParentInfos();
						int i=0;
						for (IdValuePair idValue : idValueList) {
							i++;
							if ("".equals(kw)) {
								kw += idValue.getValue().toString();
							} else if (!kw.contains(RD.getName())) {
								if(i%2==0){
									kw += "/" + idValue.getValue().toString()+"<br>";
								}else if((i-1)%2==0) {
									kw += idValue.getValue().toString();
								}else {
									kw += "/" + idValue.getValue().toString();
								}
							}
						}
						kw += "/" + RD.getName();
					}
				}
			}
			itemDTO.setKw(kw);
			reList.add(itemDTO);
		}
		return reList;
	}

	public List<ObjectId> getResourceDictionaryEntryIds(ObjectId subject, int typeInt) {
		List<ObjectId> verIds = getItemTypeBySubject(subject, typeInt);
		return verIds;
	}

	/**
	 * 根据父节点查询子节点
	 * @param subject
	 * @return
	 */
	public List<ObjectId> getItemTypeBySubject(ObjectId subject, int typeInt)
	{
		List<ResourceDictionaryEntry> list=resourceDictionaryService.getItemTypeBySubject(subject, typeInt, new BasicDBObject("ty",1));
		List<ObjectId> ids=new ArrayList<ObjectId>();
		for (ResourceDictionaryEntry entry : list) {
			ids.add(entry.getID());
		}
		return ids;
	}


	public int getItemPoolEntryCountByParam(ObjectId eduId, String searchName, int isSaved, String typeId, String propertyType, int itemType, int level) {
		ObjectId xueduanId=null;
		ObjectId subjectId=null;
		List<ObjectId> ptIds=new ArrayList<ObjectId>();
		if(!"ALL".equals(typeId)) {
			ResourceDictionaryEntry rdEntry = resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(typeId));
			if (rdEntry != null){
				if (rdEntry.getType() == 1) {
					xueduanId = rdEntry.getID();
				} else if (rdEntry.getType() == 2) {
					subjectId = rdEntry.getID();
				} else {
					int propertyTypeInt=0;
					if ("tcv".equals(propertyType)) {
						propertyTypeInt=6;
					} else {
						propertyTypeInt=9;
					}

					if (rdEntry.getType() == propertyTypeInt) {
						ptIds.add(rdEntry.getID());
					} else {
						List<ObjectId> rdIds = getResourceDictionaryEntryIds(rdEntry.getID(), propertyTypeInt);
						ptIds.addAll(rdIds);
					}
				}
			}
		}
		return itemPoolDao.getItemPoolEntryCountByParam(eduId, searchName, isSaved, xueduanId, subjectId, propertyType, ptIds, itemType, level);
	}


	public ItemDTO getItemDTODetail(ObjectId id) {
		ItemPoolEntry item=getItemPoolEntry(id);
		List<ObjectId> userIds=new ArrayList<ObjectId>();
		List<ObjectId> bkKwIds=new ArrayList<ObjectId>();
		if(item.getUserId()!=null){
			userIds.add(item.getUserId());
		}
		if(item.getPsbList()!=null&&item.getPsbList().size()>0) {
			bkKwIds.addAll(item.getPsbList());
		}
		if(item.getScList()!=null&&item.getScList().size()>0) {
			bkKwIds.addAll(item.getScList());
		}
		UserEntry user = userDao.getUserEntry(id, new BasicDBObject("nm",1).append("si", 1));
		Map<ObjectId,ResourceDictionaryEntry> rdMap = getResourceDictionaryEntryMap(bkKwIds);
		DateTimeUtils time=new DateTimeUtils();
		ItemDTO itemDTO=new ItemDTO(item);
		itemDTO.setIsSaved(item.getIsSaved());
		itemDTO.setUploadTime(time.getLongToStrTimeTwo(item.getID().getTime()));
		if(user!=null){
			itemDTO.setUserName(user.getUserName());
		}else{
			itemDTO.setUserName("管理员");
		}
		ResourceDictionaryEntry RD=null;
		String bk="";
		if(item.getPsbList()!=null&&item.getPsbList().size()>0) {
			for (ObjectId sclId : item.getPsbList()) {
				RD= rdMap.get(sclId);
				if(RD!=null) {
					List<IdValuePair> idValueList = RD.getParentInfos();
					int i=0;
					for (IdValuePair idValue : idValueList) {
						i++;
						if ("".equals(bk)) {
							bk += idValue.getValue().toString();
						} else if (!bk.contains(RD.getName())) {
							bk += "--" + idValue.getValue().toString();
						}
					}
					bk += "--" + RD.getName();
				}
			}
		}
		itemDTO.setBk(bk);
		String kw="";
		if(item.getScList()!=null&&item.getScList().size()>0) {
			for (ObjectId sclId : item.getScList()) {
				RD= rdMap.get(sclId);
				if(RD!=null) {
					List<IdValuePair> idValueList = RD.getParentInfos();
					int i=0;
					for (IdValuePair idValue : idValueList) {
						i++;
						if ("".equals(kw)) {
							kw += idValue.getValue().toString();
						} else if (!kw.contains(RD.getName())) {
							kw += "--" + idValue.getValue().toString();
						}
					}
					kw += "--" + RD.getName();
				}
			}
		}
		itemDTO.setKw(kw);
		return itemDTO;
	}


	public void getItemDTODetail(ObjectId id, Map<String, Object> model) {
		ItemPoolEntry item=getItemPoolEntry(id);
		List<ObjectId> userIds=new ArrayList<ObjectId>();
		List<ObjectId> bkKwIds=new ArrayList<ObjectId>();
		if(item.getUserId()!=null){
			userIds.add(item.getUserId());
		}
		if(item.getPsbList()!=null&&item.getPsbList().size()>0) {
			bkKwIds.addAll(item.getPsbList());
		}
		if(item.getScList()!=null&&item.getScList().size()>0) {
			bkKwIds.addAll(item.getScList());
		}
		UserEntry user = userDao.getUserEntry(id, new BasicDBObject("nm",1).append("si", 1));
		Map<ObjectId,ResourceDictionaryEntry> rdMap = getResourceDictionaryEntryMap(bkKwIds);
		DateTimeUtils time=new DateTimeUtils();
		ItemDTO itemDTO=new ItemDTO(item);
		itemDTO.setIsSaved(item.getIsSaved());
		itemDTO.setUploadTime(time.getLongToStrTimeTwo(item.getID().getTime()));
		if(user!=null){
			itemDTO.setUserName(user.getUserName());
		}else{
			itemDTO.setUserName("管理员");
		}


		ResourceDictionaryEntry RD=null;

		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> vList = new ArrayList<Map<String, String>>();
		if(item.getPsbList()!=null&&item.getPsbList().size()>0) {
			for (ObjectId sclId : item.getPsbList()) {
				RD= rdMap.get(sclId);
				if(RD!=null) {
					List<IdValuePair> idValueList = RD.getParentInfos();
					for (IdValuePair idValue : idValueList) {
						Map<String, String> m = new HashMap<String, String>();
						m.put("id", idValue.getId().toString());
						m.put("name", idValue.getValue().toString());
						vList.add(m);
					}
					Map<String, String> m2 = new HashMap<String, String>();
					m2.put("id", RD.getID().toString());
					m2.put("name", RD.getName());
					vList.add(m2);
				}
			}
		}
		map.put("ver", vList);

		List<Map<String, String>> kList = new ArrayList<Map<String, String>>();
		if(item.getScList()!=null&&item.getScList().size()>0) {
			for (ObjectId sclId : item.getScList()) {
				RD= rdMap.get(sclId);
				if(RD!=null) {
					List<IdValuePair> idValueList = RD.getParentInfos();
					for (IdValuePair idValue : idValueList) {
						Map<String, String> m = new HashMap<String, String>();
						m.put("id", idValue.getId().toString());
						m.put("name", idValue.getValue().toString());
						kList.add(m);
					}
					Map<String, String> m2 = new HashMap<String, String>();
					m2.put("id", RD.getID().toString());
					m2.put("name", RD.getName());
					kList.add(m2);
				}
			}
		}
		map.put("kno", kList);

		propertyList.add(map);
		model.put("proList", JSON.toJSONString(propertyList));
		model.put("itemDTO", itemDTO);
	}


	public void updateStatus(ObjectId id, int isSaved) throws IllegalParamException{
		itemPoolDao.update(id,"is",isSaved);
	}
}
