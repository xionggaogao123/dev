package com.fulaan.learningcenter.service;

import com.db.factory.MongoFacroty;
import com.db.school.ClassDao;
import com.db.school.ExerciseAnswerDao;
import com.db.school.ExerciseDao;
import com.db.school.TeacherClassSubjectDao;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.PageDTO;
import com.pojo.app.SimpleDTO;
import com.pojo.exam.StudentExamResDTO;
import com.pojo.exercise.ExerciseAnswerEntry;
import com.pojo.exercise.ExerciseDTO;
import com.pojo.exercise.ExerciseEntry;
import com.pojo.exercise.ExerciseMixItem;
import com.pojo.exercise.StudentExerciseAnswerDTO;
import com.pojo.school.ClassEntry;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.HttpFileConvertUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * 测试service，包括练习，考试等
 * @author fourer
 *
 */
@Service
public class ExerciseService {

	private static final Logger logger=Logger.getLogger(ExerciseService.class);
	private ExerciseDao exerciseDao =new ExerciseDao();
	private ClassDao classDao =new ClassDao();
	private ExerciseAnswerDao exerciseAnswerDao =new ExerciseAnswerDao();
	private TeacherClassSubjectDao teacherClassSubjectDao =new TeacherClassSubjectDao();
	private UserService userService=new UserService();
	
	/**
	 * 得到试卷学生结果
	 * @param did
	 * @return
	 * @throws ResultTooManyException
	 * @throws IllegalParamException 
	 */
	public List<StudentExamResDTO> getStudentExamResDTOs(ExerciseEntry e ) throws ResultTooManyException, IllegalParamException
	{
		Map<ObjectId,StudentExamResDTO> resMap =new HashMap<ObjectId, StudentExamResDTO>();
		
		Set<ObjectId> gSet =new HashSet<ObjectId>(e.getGoodUser());
		List<ExerciseAnswerEntry> answerMap=exerciseAnswerDao.getListByDocIdAndItemId(e.getID(), null, null, Constant.FIELDS);
		for(ExerciseAnswerEntry ae:answerMap)
		{
			if(!resMap.containsKey(ae.getUserId()))
			{
				StudentExamResDTO dto =new StudentExamResDTO();
				dto.setScore(ae.getUserScore());
				dto.setUserId(ae.getUserId().toString());
				dto.setIsGoods(gSet.contains(ae.getUserId())?1:0);
				if(ae.getUserScore()==Constant.NEGATIVE_ONE)
				{
					dto.setIsHandled(Constant.ZERO);
				}
				dto.setTime(DateTimeUtils.convert(ae.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
				resMap.put(ae.getUserId(), dto);
			}
			else
			{
				StudentExamResDTO thisDto=resMap.get(ae.getUserId());
				
				if(ae.getUserScore()>0)
				{
				 thisDto.setScore(thisDto.getScore()+ae.getUserScore());
				}
				if(ae.getUserScore()==Constant.NEGATIVE_ONE)
				{
					thisDto.setIsHandled(Constant.ZERO);
				}
			}
		}
		
		
		Map<ObjectId, UserEntry> map=userService.getUserEntryMap(resMap.keySet(), new BasicDBObject("nm",1));
		for(Map.Entry<ObjectId, UserEntry> entry:map.entrySet())
		{
			resMap.get(entry.getKey()).setUserName(entry.getValue().getUserName());
		}
		
		
		return new ArrayList<StudentExamResDTO>(resMap.values());
		
	}
	
	/**
	 * 添加一个优秀试卷
	 * @param id
	 * @param uid
	 */
	public void addGoodUser(ObjectId id,ObjectId uid)
	{
		exerciseDao.addGoodUser(id, uid);
	}
	
	

	/**
	 * 删除一个优秀试卷
	 * @param id
	 * @param uid
	 */
	public void removeGoodUser(ObjectId id,ObjectId uid)
	{
		exerciseDao.removeGoodUser(id, uid);
	}
	
	
	
	
	
	
	/**
	 * 上传考试试卷
	 * @param e
	 */
	public ObjectId uploadTestPaper(ExerciseEntry e)
	{
		return exerciseDao.add(e);
	}
	/**
	 * 通过班级ID查询考试总数
	 * @param classid
	 * @return
	 */
	public int countExercise(ObjectId classid)
	{
		return exerciseDao.count(classid);
	}
	/**
	 * 学生提交考试
	 * @param id
	 * @param studentId
	 */
	public void studentSubmit(ObjectId id,ObjectId studentId)
	{
		exerciseDao.studentSubmit(id, studentId);
	}

	/**
	 * 学生查看班级文档
	 * @param type 状态
	 * @param studentId 学生
	 * @param classId 学生所在班级
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<ExerciseDTO>  getStudentClassDocs (int type,ObjectId studentId,  ObjectId classId, int skip,int limit)
	{
		List<ExerciseDTO> retList =new ArrayList<ExerciseDTO>();
	
		List<ExerciseEntry> classDocList = exerciseDao.getExerciseEntryList( classId, Constant.FIELDS, skip, limit);
		/**
		 * 获取班级文档ID
		 */
		List<ObjectId> docIds=MongoUtils.getFieldObjectIDs(classDocList, Constant.ID);
		
		List<ExerciseAnswerEntry> answerList= exerciseAnswerDao.getList(studentId,docIds, new BasicDBObject("di",1).append("so", 1), false);
		Set<ObjectId> alreadyAnswerDocIds =new HashSet<ObjectId>(MongoUtils.getFieldObjectIDs(answerList,"di"));
		
		Set<ObjectId> notScoreExamIds =new HashSet<ObjectId>();
		//主观题还没有评分的考试
		for(ExerciseAnswerEntry eae:answerList)
		{
			if(eae.getUserScore()==-1D)
			{
				notScoreExamIds.add(eae.getDocumentId());
			}
		}
		
		ExerciseDTO dto;
		for(ExerciseEntry doc:classDocList)
		{
			
			dto=new ExerciseDTO(doc);
			
			if(null!=studentId)
			{
			  if(doc.getGoodUser().indexOf(studentId)>=Constant.ZERO)
			  {
				  dto.setIsGoods(Constant.ONE);
			  }
			}
			
			if(alreadyAnswerDocIds.contains(doc.getID()))
			{
				dto.setState(Constant.ONE);//已经作答
			}
			
			if(alreadyAnswerDocIds.contains(doc.getID()) && !notScoreExamIds.contains(doc.getID()))
			{
				dto.setState(Constant.TWO);//已经作答，老师已经给出分数
			}
			retList.add(dto);
		}
		return retList;
	}
	/**
	 * 分页
	 * @param type
	 * @param teacherId
	 * @param skip
	 * @param limit
	 * @return
	 */
	public PageDTO<ExerciseDTO> getPageDTO(int type,ObjectId teacherId,int skip,int limit)
	{
		PageDTO<ExerciseDTO> dto =new PageDTO<ExerciseDTO>();
		List<ExerciseDTO> list =getTeacherClassDocs(type, teacherId, skip, limit);
		
		for(ExerciseDTO exerciseDTO:list)
		{
			if(exerciseDTO.getSubmitStudent()==exerciseDTO.getTotalStudent())
			{
				exerciseDTO.setIsFinish(Constant.ONE);
			}
		}
		
		int count=exerciseDao.count(type, teacherId);
		dto.setCount(count);
		dto.setList(list);
		return dto;
	}

	/**
	 * 根据班级id获取考试分页
	 * add by miaoqiang
	 * @param type
	 * @param classId
	 * @param skip
	 * @param limit
	 * @return
	 */
	public PageDTO<ExerciseDTO> getExamPageDTO(int type,ObjectId classId,int skip,int limit)
	{
		PageDTO<ExerciseDTO> dto =new PageDTO<ExerciseDTO>();
		List<ExerciseDTO> list =getExamplePaper(type, classId, skip, limit);
		int count=exerciseDao.countByClassId(type, classId);
		dto.setCount(count);
		dto.setList(list);
		return dto;
	}

	/**
	 * 根据班级id获取本班考试
	 * add by miaoqiang
	 * @param type
	 * @param classId
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<ExerciseDTO>  getExamplePaper (int type,ObjectId classId,int skip,int limit)
	{
		List<ExerciseDTO> retList =new ArrayList<ExerciseDTO>();

		List<ExerciseEntry> list = exerciseDao.getExerciseEntryListByClassId(type, classId, Constant.FIELDS, skip, limit);

		ExerciseDTO dto;
		for(ExerciseEntry e:list)
		{
			dto =new ExerciseDTO(e);
			dto.setTotalStudent(0);//取本班人数
			retList.add(dto);
		}
		return retList;
	}
	/**
	 * 老师查询查询班级文档
	 * @param type
	 * @param teacherId
	 * @return
	 */
	public List<ExerciseDTO>  getTeacherClassDocs (int type,ObjectId teacherId,int skip,int limit)
	{
		List<ExerciseDTO> retList =new ArrayList<ExerciseDTO>();
		
		List<ExerciseEntry> list = exerciseDao.getExerciseEntryList(type, teacherId, Constant.FIELDS,skip,limit);
		
		List<TeacherClassSubjectEntry> tcs=   teacherClassSubjectDao.getTeacherClassSubjectEntryList(teacherId, null, Constant.FIELDS);
		
		List<ObjectId> classIdList =new ArrayList<ObjectId>();
		
		for(TeacherClassSubjectEntry e:tcs)
		{
			classIdList.add(e.getClassInfo().getId());
		}
		
		DBObject fields =new BasicDBObject("nm",Constant.ONE).append("ts", Constant.ONE);
		Map<ObjectId, ClassEntry> calssInfoMap=classDao.getClassEntryMap(classIdList, fields);
		
		ExerciseDTO dto;
		Set<ObjectId> unSet =null;
		ClassEntry ce;
		IdValuePairDTO unIdDto;
		for(ExerciseEntry e:list)
		{
			dto =new ExerciseDTO(e);
			unSet=new HashSet<ObjectId>(calssInfoMap.keySet());
			int count=0;
			for(IdValuePairDTO idDto:dto.getAlreadClasses())
			{
				ce=calssInfoMap.get(idDto.getId());
				if(null==ce)
				{
					continue;
				}
				unSet.remove(idDto.getId());
				idDto.setValue(ce.getName());
				count+=ce.getTotalStudent();
			}
			dto.setTotalStudent(count);
			if(unSet.size()>0)
			{
				for(ObjectId id:unSet)
				{
					ce=calssInfoMap.get(id);
					if(null!=ce)
					{
						unIdDto =new IdValuePairDTO(id, ce.getName());
						dto.addIdValuePairDTO(unIdDto);
					}
				}
			}
			retList.add(dto);
		}
		return retList;
	}
	
	/**
	 * 得到文档详情
	 * @param id
	 * @return
	 */
	public ExerciseEntry getExerciseEntry(ObjectId id)
	{
		return exerciseDao.getExerciseEntry(id);
	}
	
	
	/**
	 * 添加一个推送班级
	 * @param id
	 * @param classID
	 */
	public void addClassId(ObjectId id,ObjectId classID)
	{
		exerciseDao.addClassId(id, classID);
	}
	
	/**
	 * 删除一个班级文档
	 */
	public void delete(ObjectId id,ObjectId teacherId) 
	{
		exerciseDao.delete(id, teacherId);
	}
	
	/**
	 * 更新字段值
	 * @param id
	 * @param pairs
	 */
	 public void update(ObjectId id,FieldValuePair... pairs )
	 {
		 exerciseDao.update(id, pairs);
	 }

    /**
     * 查询考试访问数
     * @param usIds
     * @param type
     * @param dsl
     * @param del
     * @return
     */
    public int selPaperCount(List<ObjectId> usIds, int type, long dsl, long del) {
        return exerciseDao.selPaperCount(usIds, type, dsl,del);
    }

    /**
     * 获取统计对象上传的试卷信息
     * @param usIds
     * @param type
     * @param dsl
     * @param del
     * @param skip
     * @param limit
     * @param orderBy
     * @return
     */
    public List<ExerciseEntry> getPapersUploadByParamList(List<ObjectId> usIds, int type, long dsl, long del, int skip, int limit, String orderBy) {
        return exerciseDao.getPapersUploadByParamList(usIds, type, dsl, del, skip, limit, Constant.FIELDS, orderBy);
    }

    /**
     * 转化word，生成pdf和swf，并且上传到七牛
     * @param file
     * @param id
     * @throws Exception
     */
    public void convertPdfAndSwfFile(HttpServletRequest req,MultipartFile file,ObjectId id) throws Exception
    {
        File wordFile=null;
        File pdfFile=null;
        File swfFile=null;
        try
        {
        	String path=req.getServletContext().getRealPath("/upload/exam");
            wordFile =new File(path, id.toString()+"_"+file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), wordFile);
            pdfFile =new File(path, id.toString()+".pdf");
            swfFile =new File(path, id.toString()+".swf");
            logger.info("begin create pdf and swf file ;id="+id.toString());
            HttpFileConvertUtils.word2PdfAndSwf(wordFile, swfFile, pdfFile);
            logger.info("pdf and swf file are created ;id="+id.toString());
            logger.info("begin upload file server;");
            logger.info("begin upload word file ;id="+id.toString()+";the size="+wordFile.length());
            QiniuFileUtils.uploadFile(id.toString() + FilenameUtils.getExtension(file.getOriginalFilename()), new FileInputStream(wordFile), QiniuFileUtils.TYPE_DOCUMENT);
            logger.info("begin upload pdf file ;id="+id.toString()+";the size="+pdfFile.length());
            QiniuFileUtils.uploadFile(id.toString()+".pdf", new FileInputStream(pdfFile), QiniuFileUtils.TYPE_DOCUMENT);
            logger.info("begin upload swf file ;id=:"+id.toString()+";the size="+swfFile.length());
            QiniuFileUtils.uploadFile(id.toString()+".swf", new FileInputStream(swfFile), QiniuFileUtils.TYPE_DOCUMENT);
        }catch(Exception ex)
        {
            logger.error("", ex);
            throw ex;
        }
    }
}
