package com.fulaan.learningcenter.service;

import com.db.resources.CloudResourceDao;
import com.db.resources.ResourceDao;
import com.db.resources.ResourceDictionaryDao;
import com.db.user.UserDao;
import com.fulaan.cache.CacheHandler;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FileType;
import com.pojo.app.IdValuePair;
import com.pojo.app.PageDTO;
import com.pojo.resources.ResourceDTO;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.user.UserEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.props.Resources;
import com.sys.serialize.ListObjectSerializable;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.HttpFileConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * 资源service
 * @author fourer
 *
 */
public class CloudResourceService {

	private static final Logger logger=Logger.getLogger(CloudResourceService.class);

	private UserDao userDao=new UserDao();
	private ResourceDao resourceDao =new ResourceDao();
	private CloudResourceDao cloudResourceDao=new CloudResourceDao();

	private ResourceDictionaryDao resourceDictionaryDao =new ResourceDictionaryDao();

	private ResourceDictionaryService resourceDictionaryService =new ResourceDictionaryService();
	
	public static final String 	CLOUD_RESOURCE_QINIU_URL=Resources.getProperty("cloud.bucket.url","http://7xj25c.com1.z0.glb.clouddn.com/");

	private static final List<String> VIDEO_TYPE = Arrays.asList(new String[]{".avi", ".mp4", ".mov", ".mpg",".flv",".wmv",".mkv"});
	private static final List<String> IMAGE_TYPE = Arrays.asList(new String[]{".jpg", ".jpeg", ".gif", ".bmp", ".png"});
	private static final List<String> SWF_TYPE = Arrays.asList(new String[]{".doc", ".pdf",".ppt"});
	
	/**
	 * 添加一个资源
	 * @param e
	 * @return
	 */
	public ObjectId  addResource(ResourceEntry e)
	{
		return resourceDao.addResource(e);
	}
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	public ResourceEntry getResourceEntryById(ObjectId id)
	{
		return resourceDao.getResourceEntryById(id);
	}
	
	
	/**
	 * 查找数量
	 * @param name
	 * @param kwId
	 * @param bkId
	 * @return
	 * @throws ResultTooManyException
	 */
	public Long getPageCount(ObjectId eduId, String name,ObjectId kwId,ObjectId bkId,Integer type) throws ResultTooManyException
	{
		List<ObjectId> kwList =null==kwId?null:Arrays.asList(kwId);
		List<ObjectId> bkList =null==bkId?null:Arrays.asList(bkId);
	    Long count=resourceDao.countResourceEntry(eduId, type, name, null, null, kwList, bkList);
	    return count;
	}
	
	
	/**
	 * 
	 * @param name 查询的名字
	 * @param kwId 知识点ID
	 * @param bkId 章节ID
	 * @param type 1 视频;2音频;3WORD;4PPT;5PDF;
	 * @param skip
	 * @param limit
	 * @param isCache 是否要走缓存 0不经过 1经过
	 * @return
	 * @throws ResultTooManyException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public List<ResourceDTO> getResourceDTOList(ObjectId eduId, String name,ObjectId kwId,ObjectId bkId,Integer type,int skip,int limit ,int isCache) throws ResultTooManyException, ClassNotFoundException, IOException
	{
		List<ResourceDTO> dtoList =new ArrayList<ResourceDTO>();
		boolean isDefault=false;
		
		if(StringUtils.isBlank(name) &&  null==kwId && null!=bkId && bkId.equals(new ObjectId("55d41e47e0b0644525819109")) &&  type==Constant.NEGATIVE_ONE && skip==Constant.ZERO && limit==Constant.TWELVE)
		{
			isDefault=true;
		}
		
		if(isDefault && Constant.ONE==isCache)
		{
			ListObjectSerializable<ResourceDTO> ser=new ListObjectSerializable<ResourceDTO>();
			byte[] byteArr=CacheHandler.getBytesValue(CacheHandler.CACHE_CLOUDRESOURCE_DEFAULT_KEY);
			if(null!=byteArr)
			{
				List<ResourceDTO> list=ser.deserialize(byteArr);
				if(null!=list && list.size()>0)
				{
					dtoList.addAll(list);
					return dtoList;
				}
			}
		}
		List<ObjectId> kwList =null==kwId?null:Arrays.asList(kwId);
		List<ObjectId> bkList =null==bkId?null:Arrays.asList(bkId);
		List<ResourceEntry>  list=resourceDao.getResourceEntryList(eduId, type, name, null, null, kwList, bkList, skip, limit);
		ResourceDTO dto;
		for(ResourceEntry e:list)
	    {
		    	try
		    	{
		    		dto=new ResourceDTO(e);
					FileType ft= FileType.getFileType(e.getType());
		    		if(e.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
		    			 dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, e.getBucketkey()));
	                }else if(e.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType())
	                {
	                     dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, e.getBucketkey()));
	                }else if(e.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType()||ft.getType()==7)
	                {
	                      dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, e.getBucketkey()));
	                }
	                else if(Arrays.binarySearch(FileType.PDF_TYPES,e.getType())>=0)
	                {
						if(e.getBucketkey().contains(".doc")||e.getBucketkey().contains(".ppt")||e.getBucketkey().contains(".pdf")){
							dto.setUrl(CloudResourceService.CLOUD_RESOURCE_QINIU_URL + e.getID() + ".pdf");
						}else{
							dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, e.getID().toString()));
						}
	                }
		    	    dtoList.add(dto);
		    	}catch(Exception ex)
		    	{
		    		logger.error("", ex);
		    	}
		 }
		
		if(isDefault && dtoList.size()>0 && Constant.ONE==isCache)
		{
			ListObjectSerializable<ResourceDTO> ser=new ListObjectSerializable<ResourceDTO>();
			byte[] bytes=ser.serialize(dtoList);
			CacheHandler.cache(CacheHandler.CACHE_CLOUDRESOURCE_DEFAULT_KEY, bytes, Constant.SECONDS_IN_HOUR);
		}
		return dtoList;
	}
	
	
	/**
	 * 根据ID查询资源
	 * @param col
	 * @param fields
	 * @return
	 */
	public Map<ObjectId, ResourceEntry> getResourceEntryMap(Collection<ObjectId> col,DBObject fields)
	{
		return resourceDao.getResourceEntryMap(col, fields);
	}
	
	
	/**
	 * 
	 * @param name 查询的名字
	 * @param kwId 知识点ID
	 * @param bkId 章节ID
	 * @param type 1 视频;2音频;3WORD;4PPT;5PDF;
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<ResourceEntry> getResourceEntryList(ObjectId eduId,String name,ObjectId kwId,ObjectId bkId,Integer type,int skip,int limit) throws ResultTooManyException
	{
		List<ObjectId> kwList =null==kwId?null:Arrays.asList(kwId);
		List<ObjectId> bkList =null==bkId?null:Arrays.asList(bkId);
		
		return resourceDao.getResourceEntryList(eduId, Constant.NEGATIVE_ONE, name, null, null, kwList, bkList, skip, limit);
	}
	
	
	/**
	 * 增加推动或者观看次数
	 * @param id 
	 * @param type 0 观看此数 1 推送次数
	 */
	public void increaseNumber(ObjectId id,int type)
	{
		resourceDao.increaseNumber(id, type);
	}
	
	/**
	 * 查询资源字典
	 * @param ids
	 * @return
	 */
	public Map<ObjectId, ResourceDictionaryEntry> getResourceDictionaryEntryMap(Collection<ObjectId> ids)
	{
		return resourceDictionaryDao.getResourceDictionaryEntryMap(ids);
	}
	
	
	/**
	 * 根据Id查询
	 * @param id
	 * @return
	 */
	public ResourceDictionaryEntry getResourceDictionaryEntry(ObjectId id)
	{
		return resourceDictionaryDao.getResourceDictionaryEntry(id);
	}
	
	/**
	 * 更新一个资源所属的知识点和章节
	 * @param id
	 * @param scList 知识点
	 * @param psbList 章节
	 */
	public void updateResource(ObjectId id,List<ObjectId> scList,List<ObjectId> psbList)
	{
		 resourceDao.updateResource(id, scList, psbList);
	}
	
	
	public void update(ObjectId id,String field,Object value) throws IllegalParamException
	{
		try
		{
		  resourceDao.update(id, field, value);
		}catch(Exception ex)
		{
			
		}
	}
	
	
	
	 public void updateVideoUpdateStatus(ObjectId videoId,int uploadStatus) {
	        try {
	        	resourceDao.update(videoId, "us", uploadStatus);
	        }
	        catch (IllegalParamException il){

	        }
	    }

	public PageDTO<ResourceDTO> getCloudResourcePageList(ObjectId eduId, String searchName, String typeId, int isSaved, int pageNo, int pageSize) {
		List<ObjectId> verIds = resourceDictionaryService.getResourceDictionaryEntryIds(typeId, 6);
		int count = getCloudResourceCount(eduId, searchName, verIds, isSaved);
		int skip=(pageNo-1)*pageSize;
		List<ResourceDTO> list = getCloudResourceList(eduId, searchName, verIds, isSaved, skip, pageSize);
		return new PageDTO<ResourceDTO>(count, list);
	}

	public List<ResourceDTO> getCloudResourceList(ObjectId eduId, String searchName, List<ObjectId> verIds, int isSaved, int skip, int limit) {
		List<ResourceEntry>  list=resourceDao.getCloudResourceList(eduId, searchName, verIds, isSaved, skip, limit);

		List<ResourceDTO> dtoList = new ArrayList<ResourceDTO>();

		Set<ObjectId> userIds=new HashSet<ObjectId>();

		for(ResourceEntry cle: list)
		{
			if(cle.getUserId()!=null){
				userIds.add(cle.getUserId());
			}
		}

		Map<ObjectId, UserEntry> userMap= null;
		if(userIds.size()>0){
			userMap = userDao.getUserEntryMap(userIds, new BasicDBObject("nm", Constant.ONE));
		}

		ResourceDTO dto;
		for(ResourceEntry e:list)
		{
			try
			{
				dto=new ResourceDTO(e);
				String imgUrl="";
				if(null!=e.getImgUrl()&&!"".equals(e.getImgUrl())) {
					if (e.getImgUrl().contains("/upload/")) {
						imgUrl = "http://www.k6kt.com" + e.getImgUrl();
					} else {
						imgUrl = e.getImgUrl();
					}
				}else{
					imgUrl = "http://www.k6kt.com/img/coursecommon.png";
					FileType fy = FileType.getFileType(e.getType());
					if(fy!=null) {
						String fileTypeDes = fy.getName().toLowerCase();
						if ("doc".equals(fileTypeDes) || "docx".equals(fileTypeDes)) {
							imgUrl = "http://www.k6kt.com/img/coursedoc.png";
						} else if ("ppt".equals(fileTypeDes) || "pptx".equals(fileTypeDes)) {
							imgUrl = "http://www.k6kt.com/img/courseppt.png";
						} else if ("pdf".equals(fileTypeDes)) {
							imgUrl = "http://www.k6kt.com/img/coursepdf.png";
						}
					}
				}
				dto.setImageUrl(imgUrl);
				if(e.getPsbsList()!=null&&e.getPsbsList().size()>0){
					String termType="";
					String subjectName="";
					String bookType="";
					String gradeName="";
					String chapterName="";
					String partName="";
					for(ObjectId psbId :e.getPsbsList()) {
						ResourceDictionaryEntry RD6 = resourceDictionaryService.getResourceDictionaryEntry(psbId);
						if(RD6!=null&&RD6.getType()==6) {
							List<IdValuePair> idValueList = RD6.getParentInfos();
							if ("".equals(termType)) {
								termType = idValueList.get(0).getValue().toString();
							} else if (!termType.contains(idValueList.get(0).getValue().toString())) {
								termType += "," + idValueList.get(0).getValue().toString();
							}

							if ("".equals(subjectName)) {
								subjectName = idValueList.get(1).getValue().toString();
							} else if (!subjectName.contains(idValueList.get(1).getValue().toString())) {
								subjectName += "," + idValueList.get(1).getValue().toString();
							}

							if ("".equals(bookType)) {
								bookType = idValueList.get(2).getValue().toString();
							} else if (!bookType.contains(idValueList.get(2).getValue().toString())) {
								bookType += "," + idValueList.get(2).getValue().toString();
							}

							if ("".equals(gradeName)) {
								gradeName = idValueList.get(3).getValue().toString();
							} else if (!gradeName.contains(idValueList.get(3).getValue().toString())) {
								gradeName += "," + idValueList.get(3).getValue().toString();
							}

							if ("".equals(chapterName)) {
								chapterName = idValueList.get(4).getValue().toString();
							} else if (!chapterName.contains(idValueList.get(4).getValue().toString())) {
								chapterName += "," + idValueList.get(4).getValue().toString();
							}
						}
						if("".equals(partName)){
							partName=RD6.getName();
						}else if(!partName.contains(RD6.getName())) {
							partName+=","+RD6.getName();
						}
					}
					dto.setTermType(termType);
					dto.setSubjectName(subjectName);
					dto.setBookType(bookType);
					dto.setGradeName(gradeName);
					dto.setChapterName(chapterName);
					dto.setPartName(partName);
				}else{
					dto.setTermType("");
					dto.setSubjectName("");
					dto.setBookType("");
					dto.setGradeName("");
					dto.setChapterName("");
					dto.setPartName("");
				}

				if(e.getUserId()==null){
					dto.setUserName("管理员");
				}else{
					UserEntry userEntry = userMap.get(e.getUserId());
					if(userEntry!=null){
						dto.setUserName(userEntry.getUserName());
					}
				}
				DateTimeUtils time=new DateTimeUtils();
				dto.setCreateTime(time.getLongToStrTimeTwo(e.getID().getTime()));
                if(e.getResourceFrom()!=null) {
                    dto.setFrom(e.getResourceFrom());
                }else{
                    dto.setFrom("资源板块后端");
                }
				FileType ft= FileType.getFileType(e.getType());
				//dto
				if(e.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
					dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, e.getBucketkey()));
				}else if(e.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType())
				{
					dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, e.getBucketkey()));
				}else if(e.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType()||ft.getType()==7)
				{
					dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, e.getBucketkey()));
				}
				else if(Arrays.binarySearch(FileType.PDF_TYPES,e.getType())>=0)
				{
					if(e.getBucketkey().contains(".doc")||e.getBucketkey().contains(".ppt")||e.getBucketkey().contains(".pdf")){
						dto.setUrl(CloudResourceService.CLOUD_RESOURCE_QINIU_URL + e.getID() + ".pdf");
					}else{
						dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, e.getID().toString()));
					}
				}
				dtoList.add(dto);
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}

		return dtoList;
	}

	public int getCloudResourceCount(ObjectId eduId, String searchName, List<ObjectId> verIds, int isSaved) {
		int count = resourceDao.getCloudResourceCount(eduId, searchName, verIds, isSaved);
		return count;
	}

	public void getLessonWareDetail(ObjectId id, Map<String, Object> model) {
		ResourceEntry e = getResourceEntryById(id);
		ResourceDTO dto=new ResourceDTO(e);
		FileType ft= FileType.getFileType(e.getType());
		if(e.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
			dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, e.getBucketkey()));
		}else if(e.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType())
		{
			dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, e.getBucketkey()));
		}else if(e.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType()||ft.getType()==7)
		{
			dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, e.getBucketkey()));
		}
		else if(Arrays.binarySearch(FileType.PDF_TYPES,e.getType())>=0)
		{
			if(e.getBucketkey().contains(".doc")||e.getBucketkey().contains(".ppt")||e.getBucketkey().contains(".pdf")){
				dto.setUrl(CloudResourceService.CLOUD_RESOURCE_QINIU_URL + e.getID() + ".pdf");
			}else{
				dto.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, e.getID().toString()));
			}
		}

		if(e.getPsbsList()!=null&&e.getPsbsList().size()>0){
			ResourceDictionaryEntry RD6 = resourceDictionaryService.getResourceDictionaryEntry(e.getPsbsList().get(0));
			ResourceDictionaryEntry RD5 = resourceDictionaryService.getResourceDictionaryEntry(RD6.getParentId());
			ResourceDictionaryEntry RD4 = resourceDictionaryService.getResourceDictionaryEntry(RD5.getParentId());
			ResourceDictionaryEntry RD3 = resourceDictionaryService.getResourceDictionaryEntry(RD4.getParentId());
			ResourceDictionaryEntry RD2 = resourceDictionaryService.getResourceDictionaryEntry(RD3.getParentId());
			ResourceDictionaryEntry RD1 = resourceDictionaryService.getResourceDictionaryEntry(RD2.getParentId());
			dto.setTermType(RD1.getID().toString());
			dto.setSubjectName(RD2.getID().toString());
			dto.setBookType(RD3.getID().toString());
			dto.setGradeName(RD4.getID().toString());
			dto.setChapterName(RD5.getID().toString());
			dto.setPartName(RD6.getID().toString());
		}

		model.put("dto",dto);
	}

	public int convertQiniuFileType(String fileType) {
		if (VIDEO_TYPE.contains(fileType)) {
			return QiniuFileUtils.TYPE_VIDEO;
		} else if (".swf".equals(fileType) || ".flv".equals(fileType)) {
			return QiniuFileUtils.TYPE_FLASH;
		} else if (".mp3".equals(fileType)) {
			return QiniuFileUtils.TYPE_SOUND;
		} else if (".ppt".equals(fileType) || ".pdf".equals(fileType)
				|| ".doc".equals(fileType) || ".docx".equals(fileType)) {
			return QiniuFileUtils.TYPE_DOCUMENT;
		} else if (IMAGE_TYPE.contains(fileType)) {
			return QiniuFileUtils.TYPE_IMAGE;
		} else {
			return QiniuFileUtils.TYPE_DOCUMENT;
		}
	}

	public void deleteLessonWare(ObjectId eduId, ObjectId id) {
		ResourceEntry e = getResourceEntryById(id);
		if(null!=e.getEduId()&&e.getEduId().equals(eduId)){
			resourceDao.deleteLessonWareEntry(id);
		}else{
			List<ObjectId> noEdIds =e.getNoEdIds();
			if(noEdIds==null){
				noEdIds = new ArrayList<ObjectId>();
			}
			noEdIds.add(eduId);
			e.setNoEdIds(noEdIds);
			resourceDao.updLessonWareEntry(e);
		}
	}

	public List<String> uploadFile(String oldId, ObjectId eduId, HttpServletRequest request, ObjectId userId, String fileField) throws IOException, IllegalParamException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> multipartFiles = multipartRequest.getFiles(fileField);
		ResourceEntry resourceEntry;
		ObjectId objectId;
		List<String> resource = new ArrayList<String>(multipartFiles.size());
		for (MultipartFile multipartFile : multipartFiles) {
			String qiniuKey = UUID.randomUUID().toString().replace(Constant.SEPARATE_LINE, Constant.EMPTY);
			String realName = multipartFile.getOriginalFilename();
			String fileTypeWithoutPoint = realName.substring(realName.lastIndexOf('.') + 1).toLowerCase();
			String fileType = realName.substring(realName.lastIndexOf('.')).toLowerCase();
			FileType ft = FileType.getFileType(fileTypeWithoutPoint);
			int type;
			if(ft == null){
				type = -1;
			}else{
				type = ft.getType();
			}
			int qiniutype = convertQiniuFileType(fileType);
			if(SWF_TYPE.contains(fileType)){
				String tempDirPath = getWebRootAbsolutePath() + "upload" + File.separator + "swftemp" + File.separator;
				File tempDirFile = new File(getWebRootAbsolutePath() + "upload" + File.separator + "swftemp");
				if (!tempDirFile.exists()) {
					tempDirFile.mkdirs();
				}
				InputStream inputStream = multipartFile.getInputStream();
				String srcFileName = UUID.randomUUID().toString();
				String srcFilePath = tempDirPath + srcFileName + fileType;
				File srcFile = new File(tempDirPath + srcFileName + fileType);
				if (!srcFile.exists()) {
					srcFile.createNewFile();
				}
				FileOutputStream fileOutputStream = new FileOutputStream(srcFile);
				int k=-1;
				while((k=inputStream.read())!=-1){
					fileOutputStream.write(k);
				}
				if (fileOutputStream != null) {
					fileOutputStream.flush();
					fileOutputStream.close();
				}
				String swfFileName = srcFileName + "_convert";
				String swfFilePath = tempDirPath + srcFileName + ".swf";

				//转换
				if(".doc".equals(fileType)){
					HttpFileConvertUtils.convertWord2Swf(srcFilePath, swfFilePath);
				}else if(".pdf".equals(fileType)){
					HttpFileConvertUtils.convertPdfToSwf(srcFile, swfFilePath);
				}else if(".ppt".equals(fileType)){
					HttpFileConvertUtils.convertPPToSwf(srcFile, swfFilePath);
				}

				ObjectId srcId =null;
                if("".equals(oldId)){
					//上传原文件
					resourceEntry = new ResourceEntry(type, realName, multipartFile.getSize(), null, qiniuKey, userId, null, null, null,0,0,1,eduId,new ArrayList<ObjectId>(),"资源板块前端");
					srcId = resourceDao.addResource(resourceEntry);
				}else{
					srcId=new ObjectId(oldId);
					resourceEntry = resourceDao.getResourceEntryById(srcId);
					resourceEntry.setType(type);
					resourceEntry.setLength(multipartFile.getSize());
					resourceEntry.setBucketkey(qiniuKey);
					resourceDao.updateResourceEntry(resourceEntry);
				}
				resource.add(srcId.toString());
				QiniuFileUtils.uploadFile(qiniuKey, multipartFile.getInputStream(), qiniutype);
				//上传swf
				//String swfKey = UUID.randomUUID().toString().replace(Constant.SEPARATE_LINE, Constant.EMPTY);
				File swfFile = new File(tempDirPath + srcFileName + ".swf");
				//FileType swfFt = FileType.getFileType("swf");
				//ResourceEntry swfRe = new ResourceEntry(swfFt==null?-1:swfFt.getType(), realName + "_swf", multipartFile.getSize(), null, swfKey, userId, null, null, null,0,0,1,eduId,new ArrayList<ObjectId>(),"资源板块前端");
                //ObjectId swfId = resourceDao.addResource(resourceEntry);
				QiniuFileUtils.uploadFile(srcId.toString(), new FileInputStream(swfFile), QiniuFileUtils.TYPE_FLASH);
				if(!".doc".equals(fileType)) {
					resourceDao.update(srcId, "iurl", QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, srcId.toString()));
				}
			}else{
				if("".equals(oldId)) {
					resourceEntry = new ResourceEntry(type, realName, multipartFile.getSize(), null, qiniuKey, userId, null, null, null, 0, 0, 1, eduId, new ArrayList<ObjectId>(), "资源板块前端");
					objectId = resourceDao.addResource(resourceEntry);
				}else{
					objectId=new ObjectId(oldId);
					resourceEntry = resourceDao.getResourceEntryById(objectId);
					resourceEntry.setType(type);
					resourceEntry.setLength(multipartFile.getSize());
					resourceEntry.setBucketkey(qiniuKey);
					resourceDao.updateResourceEntry(resourceEntry);
				}
				resource.add(objectId.toString());

				if (qiniutype == 4) {
					QiniuFileUtils.uploadVideoFile(objectId, qiniuKey, multipartFile.getInputStream(), QiniuFileUtils.TYPE_VIDEO);
				} else {
					QiniuFileUtils.uploadFile(qiniuKey, multipartFile.getInputStream(), qiniutype);
					if (qiniutype == QiniuFileUtils.TYPE_IMAGE) {
						resourceDao.update(objectId, "iurl", QiniuFileUtils.getPath(qiniutype, qiniuKey));
					}
				}
			}
		}
		return resource;
	}

	/**
	 *
	 * @return WebRoot目录的绝对路径
	 */
	public static String getWebRootAbsolutePath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}
		java.net.URL url = classLoader.getResource("");
		String ROOT_CLASS_PATH = url.getPath() + File.separator;
		File rootFile = new File(ROOT_CLASS_PATH);
		String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + File.separator;
		File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);
		String SERVLET_CONTEXT_PATH = webInfoDir.getParent() + File.separator;
		return SERVLET_CONTEXT_PATH;
	}

	public void updateResourceEntry(ResourceEntry entry) {
		resourceDao.updateResourceEntry(entry);
	}


	public ResourceEntry getCloudResourceEntryById(ObjectId id) {
		return cloudResourceDao.getResourceEntryById(id);
	}

	public void removeCloudResourceEntryById(ObjectId id) {
		cloudResourceDao.removeById(id);
	}
}
