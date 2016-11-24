package com.fulaan.learningcenter.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.learningcenter.service.CloudResourceService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.FileType;
import com.pojo.app.IdValuePair;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.repertory.PropertyObject;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.HttpFileConvertUtils;
import com.sys.utils.RespObj;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/cloudres/manager")
public class CloudResourceManagerController extends BaseController {

	private static Logger logger =Logger.getLogger(CloudResourceManagerController.class);
	/**
	 * 	云资源对应qiniu地址
	 */
	public static final String 	CLOUD_RESOURCE_QINIU_NAME=Resources.getProperty("cloud.bucket.name","k6kt-itempool");
	
	
	private CloudResourceService cloudResourceService =new CloudResourceService();

	@Autowired
	private EducationBureauService educationBureauService;
	
	/**
	 * 后台资源库列表页面跳转
	 */
	@UserRoles({UserRole.SYSMANAGE})
	@RequestMapping("/list")
	public ModelAndView resourceList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("resourcemanager/list");
		return mv;
	}
	
	/**
	 * 根据知识点或者章节查询
	 * @param id
	 * @param propertyType
	 * @param pageNo
	 * @return
	 * @throws ResultTooManyException 
	 */
	@UserRoles({UserRole.SYSMANAGE})
	@RequestMapping("/dtolist")
	@ResponseBody
	public String resourceList(@ObjectIdType(isRequire=false) ObjectId id,@RequestParam(defaultValue="") String propertyType,int pageNo) throws ResultTooManyException
	{
		ObjectId kwId=null;
		ObjectId bkId=null;
		if(null!=id)
		{
		 kwId="kno".equalsIgnoreCase(propertyType)?id:null;
		 bkId=!"kno".equalsIgnoreCase(propertyType)?id:null;
		}

		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}

		List<ResourceEntry> dtoList= cloudResourceService.getResourceEntryList(eduId, Constant.EMPTY, kwId, bkId, -1, (pageNo-1)*10, 10);
		Long count=cloudResourceService.getPageCount(eduId, Constant.EMPTY, kwId, bkId, -1);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> formatList = formatItemForListView2(dtoList);
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("cur", pageNo);
		pageMap.put("total", count.intValue());
		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", formatList);

		return JSON.toJSONString(resultMap);
	}
	
	
	
	private List<Map<String, Object>> formatItemForListView2(
			List<ResourceEntry> srcList) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		//ResourceEntry.id->ResourceDictionaryEntry.id
		Map<ObjectId, ObjectId> kwMap =new HashMap<ObjectId, ObjectId>(); //知识点
		Map<ObjectId, ObjectId> psMap =new HashMap<ObjectId, ObjectId>(); //章节
		
		
		ObjectId kwObject=null;
		ObjectId psObject=null;
		for(ResourceEntry ce : srcList)
		{
			if(ce.getScList().size()>0)
			{
				kwObject=ce.getScList().get(0);
				kwMap.put(ce.getID(), kwObject);
			}
			
			if(ce.getPsbsList().size()>0)
			{
				psObject=ce.getPsbsList().get(0);
				psMap.put(ce.getID(), psObject);
			}
		}
		
		//
		Set<ObjectId> totalSet =new HashSet<ObjectId>();
		
		totalSet.addAll(kwMap.values());
		totalSet.addAll(psMap.values());
		
		
		Map<ObjectId, ResourceDictionaryEntry> dictionMap =cloudResourceService.getResourceDictionaryEntryMap(totalSet);
		
		ResourceDictionaryEntry pde;
		PropertyObject po;
		for (ResourceEntry ce : srcList) {
			
			Map<String, Object> m = new HashMap<String, Object>();
	
			FileType type = FileType.getFileType(ce.getType());
			
			m.put("id", ce.getID().toString());
			m.put("fId", "");
			m.put("frId", "");
			m.put("name", "");
			
			
			String iamgePath=ce.getImgUrl();
			
			if(StringUtils.isBlank(iamgePath))
			{
				if(type.equals(FileType.DOC) || type.equals(FileType.DOCX) )
				{
					iamgePath=("/images/resource/word.png");
				}
				if(type.equals(FileType.PPT)  )
				{
					iamgePath=("/images/resource/ppt.png");
				}
				if(type.equals(FileType.MP3)  )
				{
					iamgePath=("/images/resource/mp3.png");
				}
				if(type.equals(FileType.PDF)  )
				{
					iamgePath=("/images/resource/pdf.png");
				}
			}
			m.put("cover", iamgePath);
			
			
			
			m.put("time", DateTimeUtils.convert(ce.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
			
			List<PropertyObject>  zhangjieList=  new ArrayList<PropertyObject>();
			List<PropertyObject>  kwList=  new ArrayList<PropertyObject>();
			
			try
			{
			 //知识点
			 if(ce.getScList().size()>0)
			 {
				 kwObject =ce.getScList().get(0);
				 pde=dictionMap.get(kwObject);
				 if(null!=pde)
				 {
					 for(IdValuePair idv:pde.getParentInfos())
					 {
						  po=new PropertyObject(idv.getId().toString(), idv.getValue().toString());
						  kwList.add(po);
					 }
					 po=new PropertyObject(pde.getID().toString(), pde.getName());
					 kwList.add(po);
				 }
			 }
			 
			
			 
			 //章节
			 if(ce.getPsbsList().size()>0)
			 {
				 psObject =ce.getPsbsList().get(0);
				 pde=dictionMap.get(psObject);
				 if(null!=pde)
				 {
					 for(IdValuePair idv:pde.getParentInfos())
					 {
						  po=new PropertyObject(idv.getId().toString(), idv.getValue().toString());
						  zhangjieList.add(po);
					 }
					 po=new PropertyObject(pde.getID().toString(), pde.getName());
					 zhangjieList.add(po);
				 }
			 }
			}catch(Exception ex)
			{
			}
		
			m.put("ver", zhangjieList);
			m.put("kno", kwList);
			m.put("userName",getSessionValue().getUserName());
			m.put("from", "");
			m.put("fileType", type == null ? "其他格式" : type.getName());
			resultList.add(m);
		}
		return resultList;
	}
	
	/**
	 * 编辑一个资源库的课件页面跳转
	 */
	@UserRoles({UserRole.SYSMANAGE})
	@RequestMapping("/edit")
	public ModelAndView editGitResource(
			 @ObjectIdType ObjectId id,
			@RequestParam(value = "paramFileId", defaultValue = "") String paramFileId) {
		
		ResourceEntry e=cloudResourceService.getResourceEntryById(id);
		if(null==e)
		{
			throw new NullPointerException("Can not find res;"+id.toString());
		}
		ModelAndView mv = uploadAddAndEditPageView1(id, paramFileId);
		mv.setViewName("resourcemanager/edit");
		mv.addObject("resName", e.getName());
		return mv ;
	}
	
	
	
	private ModelAndView uploadAddAndEditPageView1(ObjectId id, String paramFileId) {
		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();

	
		String coverId = new ObjectId().toString();
		String fileId=new ObjectId().toString();
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> vList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> kList = new ArrayList<Map<String, String>>();
		
		ResourceEntry e=cloudResourceService.getResourceEntryById(id);
		
		//章节
		List<ObjectId> psList=e.getPsbsList();
		if(psList.size()>0)
		{
	       ObjectId psObject=	e.getPsbsList().get(0);
	       ResourceDictionaryEntry psRde= cloudResourceService.getResourceDictionaryEntry(psObject);
	       if(null!=psRde)
	       {
	    	   List<IdValuePair> idvList=  psRde.getParentInfos();
	    	   if(idvList.size()>0)
	    	   {
	    		   for(IdValuePair idv:idvList)
	    		   {
	    		     Map<String, String> m = new HashMap<String, String>();
	   			     m.put("id", idv.getId().toString());
	   			     m.put("name", idv.getValue().toString());
	   			     vList.add(m);
	    		   }
	    	   }
	    	   
	    	   Map<String, String> m = new HashMap<String, String>();
 			   m.put("id", psRde.getID().toString());
 			   m.put("name", psRde.getName());
 			   vList.add(m);
	       }
		}
		
	
		//知识点
	
		List<ObjectId> scList=e.getScList();
		if(scList.size()>0)
		{
	       ObjectId scObject=	e.getScList().get(0);
	       ResourceDictionaryEntry kwRde= cloudResourceService.getResourceDictionaryEntry(scObject);
	       if(null!=kwRde)
	       {
	    	   List<IdValuePair> idvList=  kwRde.getParentInfos();
	    	   if(idvList.size()>0)
	    	   {
	    		   for(IdValuePair idv:idvList)
	    		   {
	    		     Map<String, String> m = new HashMap<String, String>();
	   			     m.put("id", idv.getId().toString());
	   			     m.put("name", idv.getValue().toString());
	   			     kList.add(m);
	    		   }
	    	   }
	    	   
	    	   Map<String, String> m = new HashMap<String, String>();
 			   m.put("id", kwRde.getID().toString());
 			   m.put("name", kwRde.getName());
 			  kList.add(m);
	       }
		}
          
		map.put("ver", vList);
		map.put("kno", kList);
		propertyList.add(map);
		

		ModelAndView mv = new ModelAndView();
		mv.addObject("properttList", JSON.toJSONString(propertyList));
		mv.addObject("isCourseware", true);
		mv.addObject("coverId", coverId);
		mv.addObject("fileId", fileId);
		mv.addObject("id", id.toString());
		return mv;
	}
	
	
	/**
	 * 更新一个course
	 * @throws IllegalParamException 
	 */
	@UserRoles({UserRole.SYSMANAGE})
	@RequestMapping("/update/resource")
	@ResponseBody
	public String updateResource(
		    @ObjectIdType ObjectId  id,
			@RequestParam(value = "verStr", defaultValue = "") String verStr,
			@RequestParam(value = "knoStr", defaultValue = "") String knoStr,
			@RequestParam(value = "coverId", defaultValue = "") String coverId,
			@RequestParam(value = "fileId", defaultValue = "") String fileId) throws IllegalParamException {

		if(StringUtils.isBlank(verStr) || StringUtils.isBlank(knoStr) )
		{
			return JSON.toJSONString(RespObj.FAILD);
		}
		ResourceEntry e=cloudResourceService.getResourceEntryById(id);
		if(e==null || !e.getUserId().equals(getUserId()))
		{
			return JSON.toJSONString(RespObj.FAILD);
		}
		
		List<ObjectId> scList =MongoUtils.convert(knoStr);
		List<ObjectId> psList =MongoUtils.convert(verStr);
		
		
		cloudResourceService.updateResource(id, scList, psList);
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	

	/**
	 * 后台资源库新增页面跳转
	 */
	@UserRoles({UserRole.SYSMANAGE})
	@RequestMapping("/add")
	public ModelAndView showAddPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("resourcemanager/add");
		return mv;
	}

	/**
	 * 新增课件信息
	 * @throws IllegalParamException 
	 * @throws IOException 
	 */
	@UserRoles({UserRole.SYSMANAGE})
	@RequestMapping("/addCourseware")
	public String addCourseware(
			@RequestParam(value = "verStr", defaultValue = "") String verStr,
			@RequestParam(value = "knoStr", defaultValue = "") String knoStr,
			@RequestParam(value = "coverId", defaultValue = "") String coverId,
			@RequestParam(value = "fileId", defaultValue = "") String fileId,
			MultipartFile resFile,
			MultipartFile resImage,
			HttpServletRequest req
			) throws IllegalParamException, IOException {

		
		String cloudPath=req.getServletContext().getRealPath("/upload/cloudres");
		
		if(null==resFile )
		{
			throw new IllegalParamException();
		}
		
		if(StringUtils.isBlank(verStr) && StringUtils.isBlank(knoStr) )
		{
			throw new IllegalParamException();
		}
		
		ObjectId resourceId =new ObjectId();
		
		//本地copy文件
		String extendName=FilenameUtils.getExtension(resFile.getOriginalFilename());
		File resLocalFile =new File(cloudPath,resourceId+"."+extendName);
		resLocalFile.createNewFile();
		resFile.transferTo(resLocalFile);
		
		try {
			QiniuFileUtils.uploadToQiNiu(new FileInputStream(resLocalFile),CLOUD_RESOURCE_QINIU_NAME,resourceId+"."+extendName);
		} catch (Exception e1) {
			logger.error("", e1);
			return "redirect:/cloudres/manager/list.do";
		}
		
		
		String resImage1="";
		
		if(null!=resImage)
		{
		String extendImageName=FilenameUtils.getExtension(resImage.getOriginalFilename());
		File newImageFile =new File(cloudPath,resourceId+"."+extendImageName);
		newImageFile.createNewFile();
		resImage.transferTo(newImageFile);
		resImage1="/upload/cloudres/"+resourceId+"."+extendImageName;
		}
		
		
		FileType ft=FileType.getFileType(extendName);
		
		
		
//		List<ObjectId> scList =MongoUtils.convert(knoStr);
//		List<ObjectId> psList =MongoUtils.convert(verStr);
		
		List<ObjectId> scList =new ArrayList<ObjectId>();
		List<ObjectId> psList =new ArrayList<ObjectId>();
		
		
		if(StringUtils.isNotBlank(knoStr))
		{
			scList =MongoUtils.convert(knoStr);
		}
		
		if(StringUtils.isNotBlank(verStr))
		{
			psList =MongoUtils.convert(verStr);
		}

		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		
		ResourceEntry e=new ResourceEntry(ft.getType(), FilenameUtils.getBaseName(resFile.getOriginalFilename()).trim(), resFile.getSize(), resImage1,
				resourceId+"."+extendName, getUserId(), new ObjectId(getSessionValue().getSchoolId()), scList, psList,0,0,1,ebe.getID(),new ArrayList<ObjectId>(),"资源板块前端");
		
		e.setID(resourceId);
	
		if(ft.equals(FileType.DOC) || ft.equals(FileType.DOCX))
	    {
	    	File pdfFile =new File(cloudPath, resourceId.toString()+".pdf");
	 	    File swfFile =new File(cloudPath, resourceId.toString()+".swf");
	 	    if(!swfFile.exists())
	 	    {
	 	    	 HttpFileConvertUtils.word2PdfAndSwf(resLocalFile, swfFile, pdfFile);
	 	    	 
	 			try {
	 				QiniuFileUtils.uploadToQiNiu(new FileInputStream(pdfFile),CLOUD_RESOURCE_QINIU_NAME,resourceId.toString()+".pdf");
	 			} catch (Exception e1) {
	 				logger.error("", e1);
	 			}
	 			
	 			
	 			try {
	 				QiniuFileUtils.uploadToQiNiu(new FileInputStream(swfFile),CLOUD_RESOURCE_QINIU_NAME,resourceId.toString()+".swf");
	 			} catch (Exception e1) {
	 				logger.error("", e1);
	 			}
	 			
	 	    }
	    }
	    
	    if(ft.equals(FileType.PDF))
	    {
	 	    File swfFile =new File(cloudPath, resourceId.toString()+".swf");
	 	    if(!swfFile.exists())
	 	    {
	 	    	 HttpFileConvertUtils.convertPdfToSwf(resLocalFile,swfFile.getAbsolutePath());
	 	    	 
	 	    	try {
	 				QiniuFileUtils.uploadToQiNiu(new FileInputStream(swfFile),CLOUD_RESOURCE_QINIU_NAME,resourceId.toString()+".swf");
	 			} catch (Exception e1) {
	 				logger.error("", e1);
	 			}
	 	    }
	    }
	    
	    if(ft.equals(FileType.PPT)    || ft.equals(FileType.PPTX) )
	    {
	    	File pdfFile =new File(cloudPath, resourceId.toString()+".pdf");
	 	    File swfFile =new File(cloudPath, resourceId.toString()+".swf");
	 	    if(!swfFile.exists())
	 	    {
	 	    	//HttpFileConvertUtils.convertPPToSwf(resLocalFile, swfFile.getAbsolutePath());;
	 	    	HttpFileConvertUtils.convertPPToPdf(resLocalFile, pdfFile.getAbsolutePath());
				HttpFileConvertUtils.convertPdfToSwf(pdfFile,swfFile.getAbsolutePath());
				
				
				try {
	 				QiniuFileUtils.uploadToQiNiu(new FileInputStream(pdfFile),CLOUD_RESOURCE_QINIU_NAME,resourceId.toString()+".pdf");
	 			} catch (Exception e1) {
	 				logger.error("", e1);
	 			}
	 			
	 			
	 			try {
	 				QiniuFileUtils.uploadToQiNiu(new FileInputStream(swfFile),CLOUD_RESOURCE_QINIU_NAME,resourceId.toString()+".swf");
	 			} catch (Exception e1) {
	 				logger.error("", e1);
	 			}
	 	    }
	    }
	
	    
		if(ft.equals(FileType.DOC) || ft.equals(FileType.TXT) || ft.equals(FileType.DOCX) || ft.equals(FileType.PPT)   || ft.equals(FileType.PPTX)   || ft.equals(FileType.PDF) ||ft.equals(FileType.MP3) )
		{
		  QiniuFileUtils.uploadFile(resourceId+"."+extendName, new FileInputStream(resLocalFile), QiniuFileUtils.TYPE_DOCUMENT);
		}
		else if(ft.equals(FileType.FLASH)) //
		{
			QiniuFileUtils.uploadFile(resourceId+"."+extendName, new FileInputStream(resLocalFile), QiniuFileUtils.TYPE_FLASH);
			e.setVideoSourceType(VideoSourceType.SWF_CLOUD_CLASS.getType());
		}
		else
		{
			QiniuFileUtils.uploadVideoFile(resourceId, resourceId+"."+ft.getName(), new FileInputStream(resLocalFile), QiniuFileUtils.TYPE_USER_VIDEO);
			e.setVideoSourceType(VideoSourceType.USER_VIDEO.getType());
		}
			
			
		cloudResourceService.addResource(e);
		
		return "redirect:/cloudres/manager/list.do";
	}
	
}
