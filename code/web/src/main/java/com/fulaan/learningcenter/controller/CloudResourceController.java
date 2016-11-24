package com.fulaan.learningcenter.controller;

import com.alibaba.fastjson.JSON;
import com.db.resources.ResourceDao;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.learningcenter.service.CloudResourceService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.FileType;
import com.pojo.app.PageDTO;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.lesson.LessonWare;
import com.pojo.resources.ResourceDTO;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 资源controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/cloudres")
public class CloudResourceController extends BaseController {

	private static final  Logger logger =Logger.getLogger(CloudResourceController.class);
	
	private CloudResourceService cloudResourceService =new CloudResourceService();
	private DirService dirService =new DirService();
	private LessonService lessonService =new LessonService();
	@Autowired
	private EducationBureauService educationBureauService;

	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/lessonWareMge")
	public String cloudLessonManage(Map<String, Object> model) {
		return "kejianziyuanguanli/lessonWareMge";
	}

	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/addLessonWareToGit")
	public String cloudLessonGit(
			@RequestParam(value = "id", defaultValue = "") String id,
			Map<String, Object> model) {
		cloudResourceService.getLessonWareDetail(new ObjectId(id),model);
		model.put("isSaved","0");
		return "kejianziyuanguanli/addLessonWareToGit";
	}

	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/editLessonWare")
	public String editLessonWare(
			@RequestParam(value = "id", defaultValue = "") String id,
			Map<String, Object> model) {
		cloudResourceService.getLessonWareDetail(new ObjectId(id),model);
		model.put("isSaved","1");
		return "kejianziyuanguanli/addLessonWareToGit";
	}

	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/lessonWareGit")
	public String lessonWareGit(Map<String, Object> model) {
		return "kejianziyuanguanli/lessonWareGit";
	}

	/**
	 * 跳转到新增微课资源
	 *  @return
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/cloudAdd")
	public String cloudAdd(Map<String, Object> model) {
		return "kejianziyuanguanli/addLessonWare";
	}

	/**
	 * 前台新增课件
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/saveLessonWare")
	@ResponseBody
	public String addCloudCourseware(
			@RequestParam(value = "verId", defaultValue = "") String verId,
			@RequestParam(value = "coverId", defaultValue = "") String coverId,
			@RequestParam(value = "fileId", defaultValue = "") String fileId
	) {
		String coverUrl ="";
		if(!"".equals(coverId)) {
			coverUrl=cloudResourceService.getCloudResourceEntryById(new ObjectId(coverId)).getImgUrl();
			cloudResourceService.removeCloudResourceEntryById(new ObjectId(coverId));
		}
		ResourceEntry entry=cloudResourceService.getResourceEntryById(new ObjectId(fileId));
		entry.setIsSaved(1);
		entry.setImgUrl(coverUrl);
		List<ObjectId> psbList =new ArrayList<ObjectId>();
		psbList.add(new ObjectId(verId));
		entry.setPsbsList(psbList);
		cloudResourceService.updateResourceEntry(entry);
		return JSON.toJSONString(RespObj.SUCCESS);
	}

	/**
	 * 前台新增课件
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/updLessonWare")
	@ResponseBody
	public String updLessonWare(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "imageUrl", defaultValue = "") String imageUrl,
			@RequestParam(value = "verId", defaultValue = "") String verId,
			@RequestParam(value = "coverId", defaultValue = "") String coverId
	) {
		ResourceEntry entry=cloudResourceService.getResourceEntryById(new ObjectId(id));
		if("".equals(imageUrl)){
			String coverUrl ="";
			if(!"".equals(coverId)) {
				coverUrl = cloudResourceService.getCloudResourceEntryById(new ObjectId(coverId)).getImgUrl();
				cloudResourceService.removeCloudResourceEntryById(new ObjectId(coverId));
			}
			entry.setImgUrl(coverUrl);
		}
		entry.setIsSaved(1);
		List<ObjectId> psbList =new ArrayList<ObjectId>();
		psbList.add(new ObjectId(verId));
		entry.setPsbsList(psbList);
		cloudResourceService.updateResourceEntry(entry);
		return JSON.toJSONString(RespObj.SUCCESS);
	}


	/**
	 * 查询前端列表数据
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/queryCloudList")
	@ResponseBody
	public String queryCloudList(
			@RequestParam(value = "searchName", defaultValue = "") String searchName,
			@RequestParam(value = "typeId", defaultValue = "") String typeId,
			@RequestParam(value = "isSaved", defaultValue = "0") int isSaved,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法添加资源!";
			return JSON.toJSONString(respObj);
		}
		PageDTO<ResourceDTO> retList = cloudResourceService.getCloudResourcePageList(ebe.getID(),searchName, typeId, isSaved, pageNo, pageSize);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("page", pageNo);
		resultMap.put("pageSize", pageSize);
		resultMap.put("datas", retList);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 将查询结果进行组装
	 *
	 * @param srcList
	 * @return
	 */
	private List<Map<String, Object>> formatResultList(
			List<ResourceDictionaryEntry> srcList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ResourceDictionaryEntry e : srcList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", e.getID().toString());
			m.put("name", e.getName());
			list.add(m);
		}
		return list;
	}

	/**
	 * 查询微课信息
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/getLessonWare")
	@ResponseBody
	public Map<String, Object> getLessonWare(
			@RequestParam(value = "id", defaultValue = "") String id) {
		Map<String, Object> model = new HashMap<String, Object>();
		cloudResourceService.getLessonWareDetail(new ObjectId(id),model);
		return model;
	}

	/**
	 * 删除一个课件资源
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/delLessonWare")
	@ResponseBody
	public String delLessonWare(
			@RequestParam(value = "id", defaultValue = "") String id) {
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法删除资源!";
			return JSON.toJSONString(respObj);
		}
		cloudResourceService.deleteLessonWare(ebe.getID(), new ObjectId(id));
		return JSON.toJSONString(RespObj.SUCCESS);
	}

	/**
	 * 文件上传
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	public void upload(@RequestParam(value = "oldId", defaultValue = "") String oldId, HttpServletRequest request, HttpServletResponse response) {
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId= ebe.getID();
		try {
			resultMap.put("data", cloudResourceService.uploadFile(oldId, eduId, request, this.getUserId(), "file"));
			resultMap.put("code", "200");
		} catch (IOException e) {
			e.printStackTrace();
			resultMap.put("code", "500");
			resultMap.put("data", "上传文件失败");
		} catch (IllegalParamException e) {
			e.printStackTrace();
			resultMap.put("code", "500");
			resultMap.put("data", "上传文件失败");
		} finally {
			PrintWriter pw;
			try {
				pw = response.getWriter();
				pw.write(JSON.toJSONString(resultMap));
				pw.flush();
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// return JSON.toJSONString(respObj);
	}













	
	/**
	 * 	云资源对应qiniu地址
	 */
	@RequestMapping("/load")
	public String loadResource(Map<String, Object> model)
	{
		int isStudent=Constant.ZERO;
		if(UserRole.isStudentOrParent(getSessionValue().getUserRole()))
		{
			isStudent=Constant.ONE;
		}
		
		model.put("isStudent", isStudent);
		return "learningcenter/cloudresource";
	}
	
	/**
	 * 获取云资源数量
	 * @param name
	 * @param kwId 知识点ID
	 * @param bkId 课程ID
	 * @param type 1 视频;2音频;3WORD;4PPT;5PDF;
	 * @return
	 * @throws ResultTooManyException
	 */
	@RequestMapping("/pagecount")
	@ResponseBody
	public RespObj getPageCount(String name,@ObjectIdType(isRequire=false) ObjectId kwId,@ObjectIdType(isRequire=false) ObjectId bkId,@RequestParam(defaultValue="-1") Integer type) throws ResultTooManyException
	{
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}
		Long count= cloudResourceService.getPageCount(eduId, name, kwId, bkId,type);
		return new RespObj(Constant.SUCCESS_CODE,count);
	}
	
	/**
	 * 查询数据
	 * @param name
	 * @param kwId 知识点ID
	 * @param bkId 课程ID 
	 * @param type 1 视频;2音频;3WORD;4PPT;5PDF;
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 * @throws IllegalParamException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<ResourceDTO> getResourceDTOList(String name,@ObjectIdType(isRequire=false) ObjectId kwId,@ObjectIdType(isRequire=false) ObjectId bkId,@RequestParam(defaultValue="-1") Integer type,@RequestParam(defaultValue="0") Integer skip,@RequestParam(defaultValue="10") Integer limit,@RequestParam(defaultValue="1") Integer isCache) throws ResultTooManyException, IllegalParamException, ClassNotFoundException, IOException
	{
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}
		return cloudResourceService.getResourceDTOList(eduId, name, kwId, bkId,type, skip, Constant.TWELVE,isCache);
	}
	
	
	/**
	 * 增加推送或者观看次数
	 * @param id
	 * @param type 0 观看此数 1 推送次数
	 * @return
	 */
	@RequestMapping("/increase")
	@ResponseBody
	public RespObj increaseNumber(@ObjectIdType ObjectId id, @RequestParam(defaultValue="1") Integer type)
	{
		cloudResourceService.increaseNumber(id,type);
		return RespObj.SUCCESS;
	}
	
	/**
	 * 推送一个资源到多个目录
	 * @param dirIds
	 * @param id
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push")
	@ResponseBody
	public RespObj push(String dirIds, @ObjectIdType ObjectId id,HttpServletRequest req) throws FileNotFoundException, IOException
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		if(StringUtils.isBlank(dirIds))
		{
			obj.setMessage("请选择目录");
			return obj;
		}
		
		ObjectId userId=getUserId();
		
        Set<ObjectId> myDirIds=dirService.getDirIds(userId, new ObjectId(getSessionValue().getSchoolId()));
		
		List<ObjectId> dirIdList=new ArrayList<ObjectId>();
		try {
			dirIdList = MongoUtils.convert(dirIds);
		} catch (IllegalParamException e1) {
			logger.error("", e1);
			obj.setMessage("参数错误");
			return obj;
		}
		
		for(ObjectId did:dirIdList)
		{
			if(!myDirIds.contains(did))
			{
				obj.setMessage("参数错误");
				return obj;
			}
		}
		
		List<DirEntry> dirs=dirService.getDirEntryListByIds(dirIdList, Constant.FIELDS);
		if(dirs.size()!=dirIdList.size())
		{
			obj.setMessage("参数错误");
			return obj;
		}
		
		
		ResourceEntry re =cloudResourceService.getResourceEntryById(id);
		if(null==re)
		{
			logger.error("can not find ResourceEntry;"+id);
			obj.setMessage("参数错误");
			return obj;
		}

		
	    List<ObjectId> videoIds =new ArrayList<ObjectId>();
	    
	    List<LessonEntry> LessonEntryList =new ArrayList<LessonEntry>(dirs.size());
	    for(DirEntry dir:dirs)
	    {
			LessonEntry e =new LessonEntry(re.getName(), //name
					re.getName(), //content
					LessonType.BACKUP_LESSON, //type
					userId,//ui
					dir.getID(),  //dirId
					re.getImgUrl(), //imgUrl
					videoIds,//videoIds
					System.currentTimeMillis(), //lastUpdateTime
	                videoIds.size(),
					Constant.ZERO,
					Constant.ZERO,
					new ArrayList<LessonWare>(),
					null,
					id,
					Constant.ONE
					);
			LessonEntryList.add(e);
	    }
	    
	    List<ObjectId> videoList =null;
	    List<LessonWare> list=null;
	    
	    //
	   // re.setType(1);
	    
	    if(Arrays.binarySearch(ResourceDao.VIDEO_TYPES, re.getType())>=0) //视频文件
    	{
    		videoList=Arrays.asList(re.getID());
    	}
    	else
    	{
    		//sureSwfFileExists(re,req);
    		//课件默认存在本地
    		//确保文件存在本地；doc,docx,pdf转变成swf
    		FileType ty=	FileType.getFileType(re.getType());
    		LessonWare lw =new LessonWare(ty.getName(), re.getName(), re.getPersistentId().toString()) ;
    		lw.setId(re.getID());
    		//lw.setPath("/upload/cloudres/"+re.getName()+"."+ty.getName());
    		lw.setPath("");
    		list=Arrays.asList(lw);
    	}
	    
	    
	    for(LessonEntry le:LessonEntryList)
	    {
	    	if(null!=videoList)
	    	{
	    		le.setVideoIds(videoList);
	    		le.setVideoCount(Constant.ONE);
	    	}
	    	if(null!=list)
	    	{
	    		le.setLessonWareList(list);
	    		le.setDocumentCount(Constant.ONE);
	    	}
	    }
	    
		
	  lessonService.addLessonEntrys(LessonEntryList);
	 // cloudResourceService.increaseNumber(id,Constant.ONE);
	  logger.info("push cloud lesson;dirId="+dirIds+";lessonId="+id);
	  return RespObj.SUCCESS;
	}
	
	/**
	 * 浏览文本性资源
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/view/txt")
	@ResponseBody
	public RespObj textResourceView(@ObjectIdType ObjectId id,HttpServletRequest req) throws Exception
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		ResourceEntry re =cloudResourceService.getResourceEntryById(id);
		if(null==re)
		{
			logger.error("can not find ResourceEntry;"+id);
			obj.setMessage("参数错误");
			return obj;
		}
		FileType ft =FileType.getFileType(re.getType());
	    File downFile = sureResFileExists(re,  req,ft);
	    String code =FileUtil.getTextFileEncoding(downFile);
	    String text=  FileUtils.readFileToString(downFile,code);
	    text= text.replaceAll("\r\n", "<br>");
	    return  new RespObj(Constant.SUCCESS_CODE, text);
	}

	/**
	 * 浏览swf
	 * @param id
	 * @return
	 * @throws IOException 
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/view/swf")
	public String swfResourceView(@ObjectIdType ObjectId id,Map<String, Object> model,HttpServletRequest req) throws IOException, IllegalParamException
	{
		ResourceEntry re =cloudResourceService.getResourceEntryById(id);
		if(null==re)
		{
			logger.error("can not find ResourceEntry;"+id);
			throw new IllegalParamException();
		}
		//sureSwfFileExists(re, req);
		//model.put("pdfPath", "/upload/cloudres/5640510a63e76eeb6d156912.pdf");
		//model.put("swfPath", "/upload/cloudres/5640510a63e76eeb6d156912.swf");
		
		cloudResourceService.increaseNumber(id,0);
		model.put("pdfPath", CloudResourceService.CLOUD_RESOURCE_QINIU_URL+id+".pdf");
		model.put("swfPath", CloudResourceService.CLOUD_RESOURCE_QINIU_URL+id+".swf");
		
			
	    return  "learningcenter/swf_view";
	}



	/**
	 * 文件下载
	 * @param id
	 * @param response
	 * @return
	 * @throws IllegalParamException
	 * @throws PermissionUnallowedException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	@RequestMapping("/down")
	public String downDoc(@ObjectIdType ObjectId id, HttpServletResponse response) throws IllegalParamException, UnsupportedEncodingException, FileNotFoundException
	{
		ResourceEntry e=cloudResourceService.getResourceEntryById(id);
		if(null==e)
		{
			logger.info("Cannot find resource; id="+id.toString());
			return null;
		}

		FileType ft= FileType.getFileType(e.getType());
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		String name = e.getName();
		if(e.getName().lastIndexOf('.')>0) {
			name = e.getName().substring(0,e.getName().lastIndexOf('.'));
		}
		name =name.replaceAll(" +","")+"."+ft.getName();
		response.setHeader( "Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"), "ISO8859-1" ) );

		//String path=req.getServletContext().getRealPath("/upload/cloudres");


		//File f=new File(path, e.getID()+"."+ft.getName());
		String fileType="."+ft.getName();
		int qiniutype = cloudResourceService.convertQiniuFileType(fileType);
		String url=cloudResourceService.CLOUD_RESOURCE_QINIU_URL+e.getID()+"."+ft.getName();
		try {
			InputStream inputStream = QiniuFileUtils.downFileByUrl(url);
			if(null==inputStream) {
				inputStream=QiniuFileUtils.downFile(qiniutype, e.getBucketkey());
			}
			if(null==inputStream) {
				inputStream=QiniuFileUtils.downFile(QiniuFileUtils.TYPE_USER_VIDEO, e.getBucketkey());
			}
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.close();
			inputStream.close();
		}  catch (IOException ex) {
			logger.error("", ex);
		}

		return null;
	}
	
	/**
	 * 将云课程推送到多个备课空间
	 * @param resourceId
	 * @param ids
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push/buckups")
	@ResponseBody
	public RespObj pushToMultiBackUp(@ObjectIdType ObjectId resourceId, String ids) throws IllegalParamException
	{
	
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		ResourceEntry re =cloudResourceService.getResourceEntryById(resourceId);
		if(null==re)
		{
			logger.error("can not find ResourceEntry;"+resourceId);
			obj.setMessage("参数错误");
			return obj;
		}
         ids=ids.replaceAll("a_", "");
		
		List<ObjectId> objectids =MongoUtils.convert(ids);
		
	    
	    List<ObjectId> videoList =null;
	    List<LessonWare> list=null;
	  
	    
	    if(Arrays.binarySearch(ResourceDao.VIDEO_TYPES, re.getType())>=0) //视频文件
    	{
    		videoList=Arrays.asList(re.getID());
    	}
    	else
    	{
    		FileType ty=	FileType.getFileType(re.getType());
    		LessonWare lw =new LessonWare(ty.getName(), re.getName(), re.getPersistentId().toString()) ;
    		lw.setId(re.getID());
    		lw.setPath("");
    		list=Arrays.asList(lw);
    	}
	    
	    
	    if(null!=videoList)
    	{
    		lessonService.addVideo(objectids, videoList.get(0));
    	}
    	if(null!=list)
    	{
    		lessonService.addLessonWare(objectids, list.get(0));
    	}

	   return RespObj.SUCCESS;
	
	}
	
	
	private File sureResFileExists(ResourceEntry re,  HttpServletRequest req,FileType ft)
			throws FileNotFoundException, IOException {
		String path=req.getServletContext().getRealPath("/upload/cloudres");
		File downFile=new File(path, re.getID()+"."+ft.getName());
	    if(!downFile.exists())
	    {
	    	   int type =QiniuFileUtils.getType(ft);
	    	   InputStream stream =QiniuFileUtils.downFile(type, re.getBucketkey());
		       OutputStream fileStream =new FileOutputStream(downFile);
		       byte[] buff = new byte[2048];  
		       int bytesRead;  
		       while (-1 != (bytesRead = stream.read(buff, 0, buff.length))) {  
		        	fileStream.write(buff, 0, bytesRead);  
		       }
		       stream.close();
		       fileStream.close(); 
	    }
	    return downFile;
	}
	
}
