package com.fulaan.video.service;

import com.db.lesson.LessonDao;
import com.db.user.UserDao;
import com.db.video.VideoViewRecordDao;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.BasicDBObject;
import com.pojo.lesson.LessonEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.video.VideoViewRecordEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 视频记录service
 * @author fourer
 *
 */
public class VideoViewRecordService {

	private VideoViewRecordDao videoViewRecordDao =new VideoViewRecordDao();
	private LessonDao lessonDao =new LessonDao();
	private UserDao userDao =new UserDao();
	
	
	/**
	 * 查找看过某个视频的同学
	 * @param lessonId
	 * @param skip
	 * @param limit
	 * @return
	 * @throws IllegalParamException 
	 * @throws ResultTooManyException 
	 */
	public List<UserInfoDTO> getVideoViewRecordUserInfo(ObjectId lessonId,int skip,int limit) throws IllegalParamException
	{
		List<UserInfoDTO> retList =new ArrayList<UserInfoDTO>(limit);
		LessonEntry lessonEntry =lessonDao.getLessonEntry(lessonId,new BasicDBObject("vis",1));
		if(null==lessonEntry)
		{
			throw new IllegalParamException();
		}
		List<VideoViewRecordEntry> list=videoViewRecordDao.getVideoViewRecordEntrys(lessonEntry.getVideoIds(), Constant.ONE);
		List<ObjectId> students =new ArrayList<ObjectId>();
		for(VideoViewRecordEntry videoViewRecordEntry:list)
		{
			students.add(videoViewRecordEntry.getUserInfo().getId());
			if(students.size()>=limit)
				break;
		}
		List<UserEntry> userList=userDao.getUserEntryList(students, new BasicDBObject("nm",1).append("avt", 1));
		
		for(UserEntry ue:userList)
		{
			try
			{
				UserInfoDTO dto =new UserInfoDTO();
				dto.setAvt(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, ue.getAvatar()));
				dto.setName(ue.getUserName());
				dto.setId(ue.getID().toString());
				retList.add(dto);
			}catch(Exception ex)
			{
			}
		}
		
		return retList;
	}

    public List<VideoViewRecordEntry> getVideoViewRecordByVideoId(List<ObjectId> videoIds,List<ObjectId>userId,long startTime,long endTime){

        List<VideoViewRecordEntry> videoViewRecordEntryList = videoViewRecordDao.getVideoViewRecordEntrysPeriod(videoIds,userId,
                -1,startTime,endTime);

        return videoViewRecordEntryList;
    }

    /**
     * 查询学生课程视频观看数
     * @param stuIds
     * @param videoIds
     * @param dslId
     * @param delId
     * @return
     */
    public int showLessonCount(List<ObjectId> stuIds, Set<ObjectId> videoIds, ObjectId dslId, ObjectId delId) {
        //获取观看的云课程视频信息
        /*List<VideoViewRecordEntry> videos=getVideoViewRecordByParamList(stuIds, dslId, delId, "");
        int count=0;
        for(VideoViewRecordEntry item:videos){
            if(videoIds.contains(item.getVideoInfo().getId())) {
                count++;
            }
        }*/
        int count=videoViewRecordDao.showLessonCount(stuIds,videoIds,dslId,delId);
        return count;
    }

    /*public List<VideoViewRecordEntry> getClassesWatchByParamList(List<ObjectId> usIds, List<ObjectId> videoIds, ObjectId dslId, ObjectId delId, int skip, int limit, String orderBy) {
        return videoViewRecordDao.getClassesWatchByParamList(usIds, videoIds, dslId, delId, skip, limit, Constant.FIELDS, orderBy);
    }*/

    /**
     * 获取观看的视频信息
     * @param usIds
     * @param dslId
     * @param delId
     * @param orderBy
     * @return
     */
    public List<VideoViewRecordEntry> getVideoViewRecordByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, String orderBy) {
        return videoViewRecordDao.getVideoViewRecordByParamList(usIds, dslId, delId, Constant.FIELDS, orderBy);
    }


    public void addVideoViewRecordEntry(VideoViewRecordEntry ve){
        videoViewRecordDao.addVideoViewRecord(ve);
    }

}
