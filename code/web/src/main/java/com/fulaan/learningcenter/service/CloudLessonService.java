package com.fulaan.learningcenter.service;

import com.db.cloudlesson.CloudLessonDao;
import com.db.cloudlesson.CloudLessonTypeDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.db.video.VideoDao;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.BasicDBObject;
import com.pojo.cloudlesson.CloudLessonDTO;
import com.pojo.cloudlesson.CloudLessonEntry;
import com.pojo.cloudlesson.CloudLessonTypeDTO;
import com.pojo.cloudlesson.CloudLessonTypeEntry;
import com.pojo.school.GradeType;
import com.pojo.school.SchoolType;
import com.pojo.user.UserEntry;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 云课程service
 * @author fourer
 *
 */
public class CloudLessonService {

	private static final Logger logger =Logger.getLogger(CloudLessonService.class);
	
	
	private CloudLessonTypeDao dao =new CloudLessonTypeDao();
	private CloudLessonDao cloudClassDao =new CloudLessonDao();
	private VideoDao videoDao =new VideoDao();
	private UserDao userDao=new UserDao();
	private SchoolDao schoolDao=new SchoolDao();

    /**
	 * 根据学校类型，年级类型和科目查询云课程类别
	 * @param school
	 * @param grade
	 * @param subject
	 * @return
	 */
	public List<CloudLessonTypeDTO> getCloudLessonTypeDTOs(int school,int grade,int subject)
	{
		List<CloudLessonTypeDTO> retList =new ArrayList<CloudLessonTypeDTO>();
		List<CloudLessonTypeEntry> list=dao.getList(school, subject, grade);
		if(null!=list && !list.isEmpty())
		{
			
			for(CloudLessonTypeEntry e:list)
			{
				retList.add(new CloudLessonTypeDTO(e));
			}
		}
		return retList;
	}
	
	
	/**
	 * 查询云课程视频列表数目
	 * @param grades
	 * @param subject
	 * @param CloudClassType
	 * @return
	 */
	public int getCloudLessonCount(List<Integer> grades,int subject,ObjectId CloudClassType ,String searchName, ObjectId eduId)
	{
		return cloudClassDao.countCloudLessonEntry(subject, grades, CloudClassType,searchName, eduId);
	}
	
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public CloudLessonEntry getCloudLessonEntry(ObjectId id)
	{
		return cloudClassDao.getCloudLessonEntry(id);
	}
	
	/**
	 * 目前情况是一个云课程只对应一个视频，而一个视频可以属于多个课程
	 * 查询云课程视频列表
	 * @param grades
	 * @param subject
	 * @param cloudLessonType
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<CloudLessonDTO> getCloudLessonDTO(List<Integer> grades,int subject,ObjectId cloudLessonType,String searchName, ObjectId eduId, int skip,int limit ) throws ResultTooManyException
	{
		List<CloudLessonDTO> retList =new ArrayList<CloudLessonDTO>();
		Map<ObjectId, List<CloudLessonEntry>>  cceMap= cloudClassDao.getCloudLessonEntryMap(subject, grades, cloudLessonType,searchName, eduId, Constant.FIELDS, skip, limit);
		Map<ObjectId, VideoEntry> videoMap=videoDao.getVideoEntryMap(cceMap.keySet(), Constant.FIELDS);
		
		VideoEntry ve=null;
		CloudLessonDTO dto=null;
		for(Map.Entry<ObjectId, List<CloudLessonEntry>> entry:cceMap.entrySet())
		{
			ve=videoMap.get(entry.getKey());
			if(null!=ve)
			{
					for(CloudLessonEntry cle:entry.getValue())
					{
						try
						{
						    dto =new CloudLessonDTO(cle, ve);
		                    //dto.setImageUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,entry.getValue().getImageUrl()));
		                    if(ve.getVideoSourceType() == 1) {//video
		                        dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO,ve.getBucketkey()));
		                    }
		                    else if(ve.getVideoSourceType() == 0){//flash
		                        dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH,ve.getBucketkey()));
		                    }
		                    else if(ve.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
		                    	dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()));
		                    }
						  retList.add(dto);
						}catch(Exception ex)
						{
							logger.error("", ex);
						}
					}
			}
		}
		return retList;
	}

    /**
     * 查询全部云课程信息
     * @return
     */
    public Set<ObjectId> getAllCloudLessonIdList() {
        return cloudClassDao.getAllCloudLessonIdList();
    }

	/**
	 * 查询云课程视频列表数目
	 * @param grades
	 * @param subject
	 * @param CloudClassType
	 * @return
	 */
	public int getCloudLessonCount(ObjectId eduId, int isSaved, List<Integer> grades,int subject,ObjectId CloudClassType ,String searchName)
	{
		return cloudClassDao.countCloudLessonEntry(eduId, isSaved, subject, grades, CloudClassType,searchName);
	}

	/**
	 * 目前情况是一个云课程只对应一个视频，而一个视频可以属于多个课程
	 * 查询云课程视频列表
	 * @param grades
	 * @param subject
	 * @param cloudLessonType
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<CloudLessonDTO> getCloudLessonDTO(ObjectId eduId, int isSaved, List<Integer> grades, int subject, ObjectId cloudLessonType, String searchName, int skip, int limit ) throws ResultTooManyException
	{
		List<CloudLessonDTO> retList =new ArrayList<CloudLessonDTO>();
		Map<ObjectId, List<CloudLessonEntry>> cceMap= cloudClassDao.getCloudLessonEntryMap(eduId, isSaved, subject, grades, cloudLessonType,searchName, Constant.FIELDS, skip, limit);
		Map<ObjectId, VideoEntry> videoMap=videoDao.getVideoEntryMap(cceMap.keySet(), Constant.FIELDS);

		Set<ObjectId> userIds=new HashSet<ObjectId>();


		List<ObjectId> clteIds=new ArrayList<ObjectId>();
		for(Map.Entry<ObjectId, List<CloudLessonEntry>> entry:cceMap.entrySet()) {
			for(CloudLessonEntry cle:entry.getValue())
			{
				if(cle.getUserId()!=null){
					userIds.add(cle.getUserId());
				}

				if(null!=cle.getCloudClassTypes()&&cle.getCloudClassTypes().size()>0) {
					clteIds.addAll(cle.getCloudClassTypes());

				}
			}
		}

		Map<ObjectId, UserEntry> userMap= null;
		if(userIds.size()>0){
			userMap = userDao.getUserEntryMap(userIds, new BasicDBObject("nm", Constant.ONE));
		}

		Map<ObjectId, CloudLessonTypeEntry> clteMap= null;
		if(clteIds.size()>0){
			clteMap = dao.getCloudLessonEntryMap(clteIds);
		}

		VideoEntry ve=null;
		CloudLessonDTO dto=null;
		for(Map.Entry<ObjectId, List<CloudLessonEntry>> entry:cceMap.entrySet())
		{
			ve=videoMap.get(entry.getKey());
			if(null!=ve)
			{
				for(CloudLessonEntry cle:entry.getValue())
				{
					try
					{
						dto =new CloudLessonDTO(cle, ve);
						String typeStr="";
						if(null!=cle.getCloudClassTypes()&&cle.getCloudClassTypes().size()>0){
							for(ObjectId clteId: cle.getCloudClassTypes()){
								CloudLessonTypeEntry typeEntry = clteMap.get(clteId);
								if(typeEntry!=null) {
									if ("".equals(typeStr)) {
										typeStr = typeEntry.getName();
									} else {
										typeStr += "," + typeEntry.getName();
									}
								}
							}
						}
						String schoolType="";
						if(null!=cle.getCloudClassGradeTypes()&&cle.getCloudClassGradeTypes().size()>0){
							for(Integer typeInt:cle.getCloudClassGradeTypes()) {
								if(typeInt!=null) {
									GradeType type = GradeType.getGradeType(typeInt);
									String sType = SchoolType.getSchoolTypeName(type.getSchoolType());
									if (!schoolType.contains(sType)) {
										if ("".equals(schoolType)) {
											schoolType = sType;
										} else {
											schoolType += "," + sType;
										}
									}
								}
							}
						}
						dto.setSchoolTypeName(schoolType);
						dto.setTypeName(typeStr);
						if(cle.getUserId()==null){
							dto.setCreaterName("管理员");
						}else{
							UserEntry userEntry = userMap.get(cle.getUserId());
							if(userEntry!=null){
								dto.setCreaterName(userEntry.getUserName());
							}
						}
						//dto.setImageUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,entry.getValue().getImageUrl()));
						if(ve.getVideoSourceType() == 1) {//video
							dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO,ve.getBucketkey()));
						}
						else if(ve.getVideoSourceType() == 0){//flash
							dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH,ve.getBucketkey()));
						}
						else if(ve.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
							dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()));
						}
						retList.add(dto);
					}catch(Exception ex)
					{
						ex.printStackTrace();
						logger.error("", ex);
					}
				}
			}
		}

		Collections.sort(retList, new Comparator<CloudLessonDTO>() {
			public int compare(CloudLessonDTO obj1, CloudLessonDTO obj2) {
				return obj2.getUploadTime().compareTo(obj1.getUploadTime());
			}
		});


		return retList;
	}

	/**
	 * 删除一个微课资源
	 * @param id
	 */
	public void deleteCloudLesson(ObjectId eduId,ObjectId id,ObjectId videoId) {
		CloudLessonEntry clentry=cloudClassDao.getCloudLessonEntry(id);
        if(null!=clentry.getEduId()&&clentry.getEduId().equals(eduId)){
			cloudClassDao.deleteCloudLessonEntry(id);
			videoDao.deleteVideoEntry(videoId);
		}else{
			List<ObjectId> noEdIds =clentry.getNoEdIds();
			if(noEdIds==null){
				noEdIds = new ArrayList<ObjectId>();
			}
			noEdIds.add(eduId);
			clentry.setNoEdIds(noEdIds);
			cloudClassDao.updCloudLessonEntry(clentry);
		}
	}

	public void getMicroLessonDetail(ObjectId id, ObjectId videoId,Map<String, Object> model) {
		CloudLessonEntry clentry=cloudClassDao.getCloudLessonEntry(id);
		VideoEntry ventry=videoDao.getVideoEntryById(videoId);
		CloudLessonDTO dto =new CloudLessonDTO(clentry, ventry);

		if(ventry.getVideoSourceType() == 1) {//video
			dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO,ventry.getBucketkey()));
		}
		else if(ventry.getVideoSourceType() == 0){//flash
			dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH,ventry.getBucketkey()));
		}
		else if(ventry.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
			dto.setPath(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ventry.getBucketkey()));
		}

		if(null!=clentry.getCloudClassTypes()&&clentry.getCloudClassTypes().size()>0){
			dto.setType(clentry.getCloudClassTypes().get(0).toString());
		}
		if(null!=clentry.getCloudClassGradeTypes()&&clentry.getCloudClassGradeTypes().size()>0){
			for(Integer typeInt:clentry.getCloudClassGradeTypes()) {
				GradeType type = GradeType.getGradeType(typeInt);
				dto.setGradeType(type.getId());
				dto.setSchoolType(type.getSchoolType());
			}
		}
		model.put("dto",dto);
	}

	public void updCloudLessonEntry(CloudLessonEntry cloudLessonEntry) {
		cloudClassDao.updCloudLessonEntry(cloudLessonEntry);
	}

	public void addCloudLessonEntry(CloudLessonEntry cloudLessonEntry) {
		cloudClassDao.addCloudLessonEntry(cloudLessonEntry);
	}
}
