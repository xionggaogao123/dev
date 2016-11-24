package com.fulaan.homeschool.service;

import com.db.notice.NoticeDao;
import com.db.user.UserDao;
import com.fulaan.utils.KeyWordFilterUtil;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.BasicDBObject;
import com.pojo.calendar.Event;
import com.pojo.notice.NoticeDTO;
import com.pojo.notice.NoticeEntry;
import com.pojo.notice.NoticeReadsEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 通知service
 * @author fourer
 *
 */
public class NoticeService {

	private static final Logger logger =Logger.getLogger(NoticeService.class);
	
	
	private NoticeDao noticeDao =new NoticeDao();
	private UserDao userDao =new UserDao();
	
	
	/**
	 * 得到某人未读通知数量
	 * @param ur
	 * @param userId
	 * @param schoolId
	 * @param childId
	 * @return
	 */
	public Integer getNoReadCount(int ur,ObjectId userId,ObjectId schoolId,ObjectId childId )
	{
		return noticeDao.getNoReadCount(ur, userId, schoolId, childId);
	}
	/**
	 * 老师添加一个通知
	 * @param e
	 * @return
	 */
	public ObjectId addNotice(NoticeEntry e)
	{
		return noticeDao.addNoticeEntry(e);
	}
	/**
	 * 用户阅读通知
	 * @param id
	 * @param userId
	 */
	public void readNotice(ObjectId id,ObjectId userId)
	{
		noticeDao.userReadNotice(id,userId);
	}
	
	
	/**
	 * 查询通知DTO
	 * @param ur
	 * @param userId
	 * @param schoolId
	 * @param childId  子女ID；如果是家长角色,该参数有值
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 * @throws IllegalParamException 
	 */
	public List<NoticeDTO> getNoticeDTOs(int ur,ObjectId userId,ObjectId schoolId,ObjectId childId,int type, int skip,int limit) throws ResultTooManyException, IllegalParamException
	{
		if(limit>Constant.TWENTY)
		{
			throw new IllegalParamException();
		}
		List<NoticeDTO> retList =new ArrayList<NoticeDTO>();
		List<NoticeEntry> list= noticeDao.getNoticeEntrys(ur, userId, schoolId,childId, type, skip, limit);
		List<ObjectId> userIds=MongoUtils.getFieldObjectIDs(list, "ti");
		Map<ObjectId, UserEntry> userMap=userDao.getUserEntryMap(userIds, new BasicDBObject("nm",1).append("avt", 1));
		Map<ObjectId, NoticeReadsEntry> noticeReadsMap=noticeDao.getNoticeReadsEntryMap(MongoUtils.getFieldObjectIDs(list, "_id"));
		NoticeDTO ne=null;
		UserEntry ue;
		for(NoticeEntry e:list)
		{
			try
			{
				ne=new NoticeDTO(e);
				ue=userMap.get(e.getTeacherId());
				ne.setTeacherName(ue.getUserName());
				ne.setAvator(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, ue.getAvatar()));
				ne.setTitle(KeyWordFilterUtil.getReplaceStrTxtKeyWords(e.getName(), "*", 2));
				ne.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(e.getContent(),"*",2));
				NoticeReadsEntry nre=noticeReadsMap.get(e.getID());
				ne.setTotalCount(e.getTotalUser());
				if(null!=nre)
				{
					Set<ObjectId> alreadyUserIdSet =new HashSet<ObjectId>(nre.getReadUsers());
					
					if(alreadyUserIdSet.contains(userId))
					{
						ne.setAlready(Constant.ONE);
					}
					ne.setAlreadyCount(nre.getTotalReadUser()>=ne.getTotalCount()?ne.getTotalCount():nre.getTotalReadUser());
				}
				if(e.getTeacherId().equals(userId))
				{
					ne.setIsSelfOper(Constant.ONE);
					ne.setAlready(Constant.ONE);
				}
				
				retList.add(ne);
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		return retList;
	}
	
	/**
	 * 总数，用于分页
	 * @param ur
	 * @param userId
	 * @param schoolId
	 * @param childId  子女ID；如果是家长角色,该参数有值
	 * @return
	 */
	public int count(int ur,ObjectId userId,ObjectId schoolId,ObjectId childId,int type)
	{
		int count=noticeDao.count(ur, userId, schoolId,childId, type);
		return count;
	}
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public NoticeEntry getNoticeEntry(ObjectId id)
	{
		return noticeDao.getNoticeEntry(id);
	}
	/**
	 * 删除
	 * @param id
	 */
	public void deleteNotice(ObjectId id)
	{
		noticeDao.deleteNotice(id);
	}
	
	/**
	 * 置顶功能实现；
	 * @param id
	 */
	public void updateIsTop(ObjectId id,int top)
	{
		noticeDao.updateIsTop(id,top);
	}
	
	/**
	 * 将通知转化成事件，供日历部分调用
	 * @param ur
	 * @param userId
	 * @param schoolId
	 * @param childId  子女ID；如果是家长角色,该参数有值
	 * @param beginTime
	 * @param endTime
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<Event> getNoticeEvent(int ur,ObjectId userId,ObjectId schoolId,  ObjectId  childId ,long beginTime, long endTime,int skip,int limit )
	{
		return noticeDao.getNoticeEvent(ur, userId, schoolId,childId, beginTime, endTime, skip, limit);
	}
	
	/**
	 * 将通知作为事件
	 * @param id
	 * @return
	 */
	public Event getNoticeEvent(ObjectId id)
	{
		return noticeDao.getNoticeEvent(id);
	}

    /**
     * 获取统计对象通知发布信息
     * @param usIds
     * @param dslId
     * @param delId
     * @param skip
     * @param limit
     * @param orderBy
     * @return
     */
    public List<NoticeEntry> getNoticePublishByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, int skip, int limit, String orderBy) {
        return noticeDao.getNoticePublishByParamList(usIds,dslId,delId,skip,limit, Constant.FIELDS,orderBy);
    }

	/**
	 * 获取统计对象通知发布信息
	 * @param usIds
	 * @param dslId
	 * @param delId
	 * @param skip
	 * @param limit
	 * @param orderBy
	 * @return
	 */
	public List<NoticeEntry> getNoticePublishByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, int skip, int limit, BasicDBObject fields, String orderBy) {
		return noticeDao.getNoticePublishByParamList(usIds,dslId,delId,skip,limit, fields,orderBy);
	}

    /**
     * 获取统计对象通知发布信息数量
     * @param usIds
     * @param dslId
     * @param delId
     * @return
     */
    public int selNoticePublishCount(List<ObjectId> usIds, ObjectId dslId, ObjectId delId) {
        return noticeDao.selNoticePublishCount(usIds,dslId,delId);
    }

	/**
	 * 得到通知的阅读情况
	 * @param ids
	 * @return
	 */
	public List< NoticeReadsEntry> getNoticeReadsEntryListByParam(Collection<ObjectId> ids, ObjectId dslId, ObjectId delId, BasicDBObject fields){
		return noticeDao.getNoticeReadsEntryListByParam(ids,dslId,delId,fields);
	}
}
