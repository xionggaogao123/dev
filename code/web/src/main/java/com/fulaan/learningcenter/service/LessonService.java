package com.fulaan.learningcenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.fulaan.utils.KeyWordFilterUtil;
import com.mongodb.BasicDBObject;


import org.bson.types.ObjectId;

import com.db.lesson.LessonDao;
import com.db.user.UserDao;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.emarket.Comment;
import com.pojo.emarket.CommentDTO;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonWare;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import org.springframework.stereotype.Service;

/**
 * 课程service
 * @author fourer
 *
 */
@Service
public class LessonService {

	private LessonDao lessonDao =new LessonDao();
	private UserDao userDao  =new UserDao();
	
	
	public ObjectId addLessonEntry(LessonEntry e)
	{
		return lessonDao.addLessonEntry(e);
	}
	
	
	public void addLessonEntrys(List<LessonEntry> es)
	{
		lessonDao.addLessonEntrys(es);
	}
	
	/**
	 * 根据目录删除
	 * @param ids
	 */
	public void deleteByDirs(Collection<ObjectId> dirs)
	{
		lessonDao.deleteByDirs(dirs);
	}
	
	
	/**
	 * 通过ID删除
	 * @param ids
	 */
	public void deleteByIds(Collection<ObjectId> ids)
	{
		lessonDao.deleteByIds(ids);
	}
	
	/**
	 * 
	 * @param id
	 * @param ware
	 */
	public void addLessonWare(ObjectId id,LessonWare ware)
	{
		lessonDao.addLessonWare(id, ware);
	}
	
	
	
	/**
	 * 给多个lesson添加课件
	 * @param id
	 * @param ware
	 */
	public void addLessonWare(Collection<ObjectId> ids,LessonWare ware)
	{
		lessonDao.addLessonWare(ids, ware);
	}
	
	/**
	 * 根据目录查询课程
	 * @param dirs
	 * @param fields
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<LessonEntry> getLessonEntryList(Collection<ObjectId> dirs,DBObject fields) 
	{
		Map<ObjectId, LessonEntry> map= lessonDao.getLessonEntryMap(dirs, fields);
		return new ArrayList<LessonEntry>(map.values());
	}


	/**
	 * 根据目录查询课程
	 * @param dirs
	 * @param fields
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<LessonEntry> getLessonEntryList(Collection<ObjectId> dirs,long startTime,long endTime,DBObject fields)
	{
		Map<ObjectId, LessonEntry> map= lessonDao.getLessonEntryMap(dirs,startTime,endTime, fields);
		return new ArrayList<LessonEntry>(map.values());
	}
	
	public List<LessonEntry> getLessonEntryList(ObjectId dirs,DBObject fields) 
	{
		Map<ObjectId, LessonEntry> map= lessonDao.getLessonEntryMap(dirs, fields);
		return new ArrayList<LessonEntry>(map.values());
	}
	
	
	/**
	 * 根据dir查询得到数目
	 * @param dirs
	 * @return
	 */
	public int count(Collection<ObjectId> dirs)
	{
		return lessonDao.count(dirs);
	}
	
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public LessonEntry getLessonEntry( ObjectId id)
	{
		return lessonDao.getLessonEntry(id,Constant.ZERO,Constant.FIVE);
	}
	
	
	
	
	/**
	 * 得到评论列表
	 * @param id
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<CommentDTO> getCommentList(ObjectId id,int skip,int limit)
	{
		List<CommentDTO> retList =new ArrayList<CommentDTO>();
		LessonEntry e =lessonDao.getLessonEntry(id, skip, limit);
		if(null!=e)
		{
			List<Comment> comments= e.getCommentList();
			List<ObjectId> userIdList=MongoUtils.getFieldObjectIDs(comments, "ui");
			Map<ObjectId, UserEntry> userInfoMap=userDao.getUserEntryMap(userIdList, Constant.FIELDS);
			
			CommentDTO dto=null;
			UserEntry u=null;
			for(Comment c:comments)
			{
				u=userInfoMap.get(c.getUi());
				if(null==u)
					continue;
				dto =new CommentDTO(c);
				dto.setComment(KeyWordFilterUtil.getReplaceStrTxtKeyWords(c.getComment(), "*", 2));
				dto.setName(u.getNickName());
				dto.setAvatar(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, u.getAvatar()));
				retList.add(dto);
			}
			
		}
		return retList;
	}



	/**
	 * 删除一个视频
	 * @param id
	 * @param videoId
	 */
	public void removeVideo(ObjectId id,ObjectId videoId)
	{
		 lessonDao.removeVideo(id, videoId);
	}

	/**
	 * 删除学习附件
	 * @param id
	 * @param ware
	 */
	public void removeWare(ObjectId id,LessonWare ware)
	{
		lessonDao.removeWare(id, ware);
	}
	
	

	
	
	/**
	 * 更新名字
	 * @param id
	 * @param newName
	 * @throws IllegalParamException
	 */
	public void  updateName(ObjectId id,String newName) throws IllegalParamException
	{
			lessonDao.update(id, "nm", newName);
	}
	
	public void updateCoverImage(ObjectId id,String imageUrl)throws IllegalParamException
    {
        lessonDao.update(id,"im",imageUrl);
    }
	
	/**
	 * 更新课后练习
	 * @param id
	 * @param exerciseId
	 * @throws IllegalParamException
	 */
	public void  updateExercise(ObjectId id,ObjectId  exerciseId) throws IllegalParamException
	{
			lessonDao.update(id, "exl", exerciseId);
	}
	

	/**
	 * 删除课后练习
	 * @param id
	 * @throws IllegalParamException
	 */
	public void  deleteExe(ObjectId id) throws IllegalParamException
	{
			lessonDao.update(id, "exl", null);
	}
	
	
	/**
	 * 更新多个字段值
	 * @param
	 * @param pairs
	 */
	public void update(Collection<ObjectId> ids,FieldValuePair... pairs) 
	{
		lessonDao.update(ids, pairs);
	}
	
	
	/**
	 * 按照源和用户ID查询
	 * @param sourceId
	 * @param userId
	 * @param fields
	 * @return
	 */
	public Set<ObjectId> getLessonEntryIdSet(ObjectId sourceId,ObjectId userId,DBObject fields)
	{
		Map<ObjectId, LessonEntry> map= lessonDao.getLessonEntryMap(sourceId, userId, fields);
		return map.keySet();
	}
	
	/**
	 * 判断要给课程是否属于dirs
	 * @param id
	 * @param dirs
	 * @return
	 */
	public boolean isExists(ObjectId id,Collection<ObjectId> dirs)
	{
		return lessonDao.isExists(id, dirs);
	}
	
	
	
	/**
	 * 添加一个评论
	 * @param lessonid
	 * @param comment
	 */
    public void addComment(ObjectId lessonid,Comment comment)
    {
    	LessonEntry e=getLessonEntry(lessonid);
    	if(e.getCommentCount()>=Constant.MAX_LESSON_COMMENT_INT)
    	{
    		lessonDao.deleteComment(lessonid);
    	}
    	lessonDao.addComment(lessonid, comment);
    }

	/**
	 * 删除用户一条评论
	 * @param lessonId
	 * @param userId
	 * @param time
	 */
	public void deleteComment(ObjectId lessonId, ObjectId userId, long time){
		lessonDao.deleteComment(lessonId, userId, time);
	}
	
    
    /**
     * 增加课程的推送次数
     * @param id
     */
    public void increasePushCount(ObjectId id)
    {
    	lessonDao.increasePushCount(id);
    }
    
    
    /**
     * 添加一个视频
     * @param id
     * @param videoId
     */
    public void addVideo(ObjectId id,ObjectId videoId)
    {
    	lessonDao.addVideo(id, videoId);
    }
    
    
    /**
     * 给多个lesson添加一个视频
     * @param id
     * @param videoId
     */
    public void addVideo(Collection<ObjectId> ids,ObjectId videoId)
    {
    	lessonDao.addVideo(ids, videoId);
    }

    /**
     * 获取用户备课空间、班级课程上传数目
     * @param dirids
     * @param lessonType
     * @param dsl
     * @param del
     */
    public int selLessonCount(List<ObjectId> dirids, int lessonType, long dsl, long del) {
        return lessonDao.selLessonCount(dirids, lessonType, dsl, del);
    }

    /**
     * 获取用户备课空间、班级课程信息
     * @param dirids
     * @param type
     * @param dsl
     * @param del
     * @param skip
     * @param limit
     * @param orderBy
     * @return
     */
    public List<LessonEntry> getLessonEntryByParamList(List<ObjectId> dirids, int type, long dsl, long del, int skip, int limit, String orderBy) {
        DBObject fields =new BasicDBObject("ui",Constant.ONE)
                .append("di", Constant.ONE)
                .append("nm", Constant.ONE)
                .append("ty", Constant.ONE)
                .append("con", Constant.ONE)
                .append("lut",Constant.ONE)
                .append("vis",Constant.ONE)
                .append("vc", Constant.ONE);
        return lessonDao.getLessonEntryByParamList(dirids,type,dsl,del,skip,limit,fields,orderBy);
    }

	/**
	 * 获取用户备课空间、班级课程信息
	 * @param dirids
	 * @param type
	 * @param dsl
	 * @param del
	 * @param skip
	 * @param limit
	 * @param orderBy
	 * @return
	 */
	public List<LessonEntry> getLessonEntryByParamList(List<ObjectId> dirids, int type, long dsl, long del, int skip, int limit, DBObject fields, String orderBy) {
		return lessonDao.getLessonEntryByParamList(dirids,type,dsl,del,skip,limit,fields,orderBy);
	}

	public List<LessonEntry> findAllClassLesson(){
		return lessonDao.findAllCLassLesson();
	}
	
	/**
	 * 根据lessonId查询，返回map
	 * @param ids
	 * @param fields
	 * @return
	 */
	public Map<ObjectId, LessonEntry> getLessonEntryMapByIDs(Collection<ObjectId> ids,DBObject fields)
	{
		return lessonDao.getLessonEntryMapByIDs(ids, fields);
	}

	/**
	 * 更新课件
	 * @param lessonId
	 * @param wareId
	 */
	public void updateWare(ObjectId lessonId, ObjectId wareId) {
		lessonDao.updateWare(lessonId,wareId);
	}
}
