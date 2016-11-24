package com.fulaan.repertory.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.comment.service.CommentService;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.preparation.service.PreparationService;
import com.fulaan.repertory.service.CoursewareService;
import com.fulaan.resources.service.CloudResourceService;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.review.service.ReviewService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.FileType;
import com.pojo.app.IdValuePair;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.preparation.PreparationEntry;
import com.pojo.repertory.CoursewareEntry;
import com.pojo.repertory.PropertyObject;
import com.pojo.repertory.RepertoryProperty;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.review.ReviewEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/courseware")
public class CoursewareController extends BaseController {
	@Autowired
	private UserService userService;

	@Autowired
	private CoursewareService coursewareService;

	@Autowired
	private EducationBureauService educationBureauService;

	@Autowired
	private ResourceDictionaryService resourceDictionaryService;

	@Autowired
	private CloudResourceService resourceService;

	@Autowired
	private PreparationService preparationService;
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private CommentService commentService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private SimpleDateFormat publishTimeSDF = new SimpleDateFormat("yyyy-MM-dd");
	
	
	private static final List<String> VIDEO_TYPE = Arrays.asList(new String[]{".avi", ".mp4", ".mov", ".mpg",".flv",".wmv",".mkv"});
	private static final List<String> MUSIC_TYPE = Arrays.asList(new String[]{".mp3" });
    private static final List<String> IMAGE_TYPE = Arrays.asList(new String[]{".jpg", ".jpeg", ".gif", ".png"});
    private static final List<String> SWF_TYPE = Arrays.asList(new String[]{".pdf", ".doc"});
    
    
	


    private int convertQiniuFileType(String fileType) {
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
	 * 后台资源列表页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView showList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ziyuanguanli/ziyuanguanli");
		return mv;
	}

	/**
	 * 后台资源库列表页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/gitList")
	@ResponseBody
	public ModelAndView showGitList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ziyuanguanli/ziyuanGit");
		return mv;
	}

	/**
	 * 后台资源库新增页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/add")
	@ResponseBody
	public ModelAndView showAddPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ziyuanguanli/addziyuan");
		return mv;
	}

	private ModelAndView uploadAddAndEditPageView(String id, String paramFileId) {
		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();

		CoursewareEntry ce = coursewareService.getCoursewareEntry(new ObjectId(id));
		String coverId = "";
		String fileId;
		if (ce != null) {
			coverId = ce.getCoverId().toString();
			fileId = ce.getFileId().toString();
			List<RepertoryProperty> rpList = ce.getPropList();
			for (RepertoryProperty rp : rpList) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, String>> vList = new ArrayList<Map<String, String>>();
				List<Map<String, String>> kList = new ArrayList<Map<String, String>>();
				for (PropertyObject vpo : rp.getVersionList()) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("id", vpo.getId());
					m.put("name", vpo.getName());
					vList.add(m);
				}
				for (PropertyObject kpo : rp.getKnowledgeList()) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("id", kpo.getId());
					m.put("name", kpo.getName());
					kList.add(m);
				}

				map.put("ver", vList);
				map.put("kno", kList);
				propertyList.add(map);
			}
		} else {
			fileId = paramFileId;
			PreparationEntry pe = preparationService
					.getPreparationEntryById(new ObjectId(id));
			if (pe != null) {
				coverId = pe.getPreparationCover();
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, String>> vList = new ArrayList<Map<String, String>>();

				ResourceDictionaryEntry rde = resourceDictionaryService
						.getResourceDictionaryEntry(new ObjectId(pe.getPart()));

				for (IdValuePair ivp : rde.getParentInfos()) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("id", ivp.getId().toString());
					m.put("name", ivp.getValue().toString());
					vList.add(m);
				}
				Map<String, String> m = new HashMap<String, String>();
				m.put("id", rde.getID().toString());
				m.put("name", rde.getName().toString());
				vList.add(m);

				map.put("ver", vList);

				propertyList.add(map);
			} else {
				ReviewEntry re = reviewService.getReviewEntry(id);
				if (re != null) {
					coverId = re.getReviewCover();
					Map<String, Object> map = new HashMap<String, Object>();
					List<Map<String, String>> vList = new ArrayList<Map<String, String>>();

					ResourceDictionaryEntry rde = resourceDictionaryService
							.getResourceDictionaryEntry(new ObjectId(re
									.getPart()));

					for (IdValuePair ivp : rde.getParentInfos()) {
						Map<String, String> m = new HashMap<String, String>();
						m.put("id", ivp.getId().toString());
						m.put("name", ivp.getValue().toString());
						vList.add(m);
					}
					Map<String, String> m = new HashMap<String, String>();
					m.put("id", rde.getID().toString());
					m.put("name", rde.getName().toString());
					vList.add(m);

					map.put("ver", vList);

					propertyList.add(map);
				}
			}
		}

		ModelAndView mv = new ModelAndView();
		mv.addObject("properttList", JSON.toJSONString(propertyList));
		mv.addObject("isCourseware", ce != null);
		mv.addObject("coverId", coverId);
		mv.addObject("fileId", fileId);
		mv.addObject("id", id);
		return mv;
	}

	/**
	 * 新增至资源库页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/addToGit")
	@ResponseBody
	public ModelAndView addToGit(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "paramFileId", defaultValue = "") String paramFileId) {
		ModelAndView mv = uploadAddAndEditPageView(id, paramFileId);
		mv.setViewName("ziyuanguanli/addToGit");
		return mv;
	}

	/**
	 * 编辑一个资源库的课件页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/editGitCourseware")
	@ResponseBody
	public ModelAndView editGitCourseware(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "paramFileId", defaultValue = "") String paramFileId) {
		ModelAndView mv = uploadAddAndEditPageView(id, paramFileId);
		mv.setViewName("ziyuanguanli/editZiyuan");
		return mv;
	}

	/**
	 * 删除一个courseware课件
	 */
	@SessionNeedless
	@RequestMapping("/delCourseware")
	@ResponseBody
	public RespObj delCourseware(
			@RequestParam(value = "id", defaultValue = "") String id) {
		CoursewareEntry ce = coursewareService.getCoursewareEntry(new ObjectId(id));
		coursewareService.deleteCourseware(new ObjectId(id));
		resourceService.removeById(ce.getFileId());
		return RespObj.SUCCESS;

	}

	/**
	 * 忽略一个courseware课件
	 */
	@SessionNeedless
	@RequestMapping("/ignoreCourseware")
	@ResponseBody
	public RespObj ignoreCourseware(
			@RequestParam(value = "id", defaultValue = "") String id) {
		coursewareService.ignoreCourseware(new ObjectId(id));
		return RespObj.SUCCESS;

	}

	/**
	 * 删除一个备课或评课的课件
	 */
	@SessionNeedless
	@RequestMapping("/delCoursewarePreRev")
	@ResponseBody
	public RespObj delCoursewarePreRev(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "fId", defaultValue = "") String fId) {
		coursewareService.deleteCourseware(new ObjectId(id));
		ObjectId oid = new ObjectId(id);
		ObjectId ofId = new ObjectId(fId);
		preparationService.delFileForPreparation(oid, ofId);
		reviewService.delFileForReview(oid, ofId); 
		resourceService.removeById(new ObjectId(fId));
		return RespObj.SUCCESS;

	}

	/**
	 * 忽略一个备课或评课的课件
	 */
	@SessionNeedless
	@RequestMapping("/ignoreCoursewarePreRev")
	@ResponseBody
	public RespObj ignoreCoursewarePreRev(
			@RequestParam(value = "fId", defaultValue = "") String fId) {
		resourceService.ignoreResource(new ObjectId(fId));
		return RespObj.SUCCESS;

	}

	/**
	 * 更新一个course
	 */
	@SessionNeedless
	@RequestMapping("/updateCourseware")
	@ResponseBody
	public String updateCourseware(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "verStr", defaultValue = "") String verStr,
			@RequestParam(value = "knoStr", defaultValue = "") String knoStr,
			@RequestParam(value = "coverId", defaultValue = "") String coverId,
			@RequestParam(value = "fileId", defaultValue = "") String fileId) {

		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue()
				.getId()));

//		EducationBureauEntry ebe = educationBureauService
//				.selEducationByUserId(ue.getID());
		int role=getSessionValue().getUserRole();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法添加资源!";
			return JSON.toJSONString(respObj);
		}

		List<RepertoryProperty> properties = new ArrayList<RepertoryProperty>();
		String[] verArr = verStr.split(",");
		String[] knoArr = knoStr.split(",");
		if (verArr.length != knoArr.length) {
			return JSON.toJSONString(RespObj.FAILD);
		}
		for (int i = 0; i < verArr.length; i++) {
			ResourceDictionaryEntry ve = resourceDictionaryService
					.getResourceDictionaryEntry(new ObjectId(verArr[i]));
			ResourceDictionaryEntry ke = resourceDictionaryService
					.getResourceDictionaryEntry(new ObjectId(knoArr[i]));
			properties.add(new RepertoryProperty(uploadPropertyObjectList(ve),
					uploadPropertyObjectList(ke)));
		}
		CoursewareEntry ce = coursewareService.getCoursewareEntry(new ObjectId(id));
		if(!StringUtils.isBlank(coverId) && coverId.equals(ce.getCoverId())){
			coursewareService.updateCourseware(new ObjectId(id), coverId, new ObjectId(fileId), properties);
		}else{
			String coverUrl = resourceService.getResourceEntryById(new ObjectId(coverId)).getImgUrl();
			coursewareService.updateCourseware(new ObjectId(id), coverUrl, new ObjectId(fileId), properties);
			resourceService.removeById(new ObjectId(coverId));
		}
		
		return JSON.toJSONString(RespObj.SUCCESS);

	}

	/**
	 * 添加courseware类型到git库中
	 */
	@SessionNeedless
	@RequestMapping("/addCoursewareToGit")
	@ResponseBody
	public String addCoursewareToGit(
			@RequestParam(value = "id", defaultValue = "") String id) {
		coursewareService.saveToGit(new ObjectId(id));
		return JSON.toJSONString(RespObj.SUCCESS);

	}

	/**
	 * 新增课件信息
	 */
	@SessionNeedless
	@RequestMapping("/addCourseware")
	@ResponseBody
	public String addCourseware(
			@RequestParam(value = "verStr", defaultValue = "") String verStr,
			@RequestParam(value = "knoStr", defaultValue = "") String knoStr,
			@RequestParam(value = "coverId", defaultValue = "") String coverId,
			@RequestParam(value = "fileId", defaultValue = "") String fileId) {
		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue()
				.getId()));

//		EducationBureauEntry ebe = educationBureauService
//				.selEducationByUserId(ue.getID());
		int role=getSessionValue().getUserRole();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法添加资源!";
			return JSON.toJSONString(respObj);
		}

		List<RepertoryProperty> properties = new ArrayList<RepertoryProperty>();
		String[] verArr = verStr.split(",");
		String[] knoArr = knoStr.split(",");
		if (verArr.length != knoArr.length) {
			return JSON.toJSONString(RespObj.FAILD);
		}
		for (int i = 0; i < verArr.length; i++) {
			ResourceDictionaryEntry ve = resourceDictionaryService
					.getResourceDictionaryEntry(new ObjectId(verArr[i]));
			ResourceDictionaryEntry ke = resourceDictionaryService
					.getResourceDictionaryEntry(new ObjectId(knoArr[i]));
			properties.add(new RepertoryProperty(uploadPropertyObjectList(ve),
					uploadPropertyObjectList(ke)));
		}
		if(coverId.indexOf(":") > -1){
			CoursewareEntry ce = new CoursewareEntry(ue.getID(), ue.getUserName(),
					System.currentTimeMillis(),coverId ,
					new ObjectId(fileId), properties, 1, ebe.getID(), "资源板块后端");

			coursewareService.addCourseware(ce);
		}else{
			String coverUrl = resourceService.getResourceEntryById(new ObjectId(coverId)).getImgUrl();
			CoursewareEntry ce = new CoursewareEntry(ue.getID(), ue.getUserName(),
					System.currentTimeMillis(),coverUrl ,
					new ObjectId(fileId), properties, 1, ebe.getID(), "资源板块后端");

			coursewareService.addCourseware(ce);
			resourceService.removeById(new ObjectId(coverId));
		}
		return JSON.toJSONString(RespObj.SUCCESS);
	}


	private List<PropertyObject> uploadPropertyObjectList(
			ResourceDictionaryEntry rde) {
		List<PropertyObject> poList = new ArrayList<PropertyObject>();
		for (IdValuePair ivp : rde.getParentInfos()) {
			poList.add(new PropertyObject(ivp.getId().toString(), ivp
					.getValue().toString()));
		}
		poList.add(new PropertyObject(rde.getID().toString(), rde.getName()));
		return poList;
	}

	/**
	 * 根据分页信息进行数据筛选
	 * 
	 * @param src
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<? extends Object> getListByPage(List<? extends Object> src,
			int pageNo, int pageSize) {
		List<Object> list = new ArrayList<Object>();
		int startIndex = (pageNo - 1) * pageSize;
		int endIndex = (pageNo * pageSize) - 1;
		if (src.size() < pageNo * pageSize) {
			endIndex = src.size() - 1;
		}
		for (int i = startIndex; i < endIndex + 1; i++) {
			list.add(src.get(i));
		}
		return list;
	}

	private List<Map<String, Object>> formatItemForListView(
			List<CoursewareEntry> srcList) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (CoursewareEntry ce : srcList) {
			if (ce.getIsIgnore() == 1) {
				continue;
			}
			Map<String, Object> m = new HashMap<String, Object>();
			ResourceEntry fre = resourceService.getResourceEntryById(ce
					.getFileId());
			FileType type = FileType.getFileType(fre.getType());
			List<RepertoryProperty> propList = ce.getPropList();
			m.put("id", ce.getID().toString());
			m.put("fId", "");
			m.put("frId", ce.getFileId().toString());
			m.put("name", fre.getName());
			m.put("cover", ce.getCoverId());
			m.put("time", sdf.format(new Date(ce.getTimestamp())));
			m.put("ver", propList.get(0).getVersionList());
			m.put("kno", propList.get(0).getKnowledgeList());
			m.put("userName", ce.getUserName());
			m.put("from", ce.getResourceFrom());
			m.put("fileType", type == null ? "其他格式" : type.getName());
			resultList.add(m);
		}
		return resultList;
	}

	/**
	 * 查询已入库课件信息
	 */
	@SessionNeedless
	@RequestMapping("/querySavedList")
	@ResponseBody
	public String querySavedList(
			@RequestParam(value = "typeInt", defaultValue = "-1") int typeInt,
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "propertyType", defaultValue = "") String propertyType,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue()
				.getId()));

//		EducationBureauEntry ebe = educationBureauService
//				.selEducationByUserId(ue.getID());
		int role=getSessionValue().getUserRole();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法查询资源!";
			return JSON.toJSONString(respObj);
		}
		List<CoursewareEntry> srcList;
		if ("ver".equals(propertyType)) {
			srcList = coursewareService.getCoursewaresSavedByIdInVersion(id,
					ebe.getID());
		} else {
			srcList = coursewareService.getCoursewaresSavedByIdInKnowledge(id,
					ebe.getID());
		}
		List<CoursewareEntry> pagedList = (List<CoursewareEntry>) getListByPage(
				srcList, pageNo, pageSize);

		List<Map<String, Object>> formatList = formatItemForListView(pagedList);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("cur", pageNo);
		pageMap.put("total", srcList.size());
		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", formatList);

		return JSON.toJSONString(resultMap);

	}

	/**
	 * 查询未入库课件信息
	 */
	@SessionNeedless
	@RequestMapping("/queryK6KTUnSavedList")
	@ResponseBody
	public String queryUnSavedList(
			@RequestParam(value = "typeInt", defaultValue = "-1") int typeInt,
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "propertyType", defaultValue = "") String propertyType,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue()
				.getId()));

//		EducationBureauEntry ebe = educationBureauService
//				.selEducationByUserId(ue.getID());
		int role=getSessionValue().getUserRole();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法查询资源!";
			return JSON.toJSONString(respObj);
		}
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		// 课件上传部分
		List<CoursewareEntry> courseList;
		if ("ver".equals(propertyType)) {
			courseList = coursewareService.getCoursewaresNotSavedByIdInVersion(
					id, ebe.getID());
		} else {
			courseList = coursewareService
					.getCoursewaresNotSavedByIdInKnowledge(id, ebe.getID());
		}
		List<Map<String, Object>> formatCourseList = formatItemForListView(courseList);
		allList.addAll(formatCourseList);
		if ("ver".equals(propertyType)) {
			// 集体备课和评课议课部分
			String columName = null;
			if(typeInt == 1){
				columName = "ett";
			}
			if(typeInt == 2){
				columName = "es";
			}
			if(typeInt == 3){
				columName = "tv";
			}
			if(typeInt == 4){
				columName = "eg";
			}
			if(typeInt == 5){
				columName = "ch";
			}
			if(typeInt == 6){
				columName = "pat";
			}
			List<PreparationEntry> pList;
			List<ReviewEntry> rList;
			if (columName == null) {
				pList = preparationService.getAllPreparationEntries(ebe.getID());
				rList = reviewService.getAllReviewEntries(ebe.getID());
			} else {
				pList = preparationService
						.getPreparationEntriesByResourceDictionaryId(id,
								columName,ebe.getID());
				rList = reviewService.getReviewEntriesByResourceDictionaryId(
						id, columName,ebe.getID());
			}
			List<Map<String, Object>> pFomartList = new ArrayList<Map<String, Object>>();
			for (PreparationEntry pe : pList) {
				for (ObjectId rId : pe.getCoursewareList()) {
					ResourceEntry re = resourceService
							.getResourceEntryById(rId);
					if (re == null || re.getIsIgnore() == 1) {
						continue;
					}
					Map<String, Object> m = new HashMap<String, Object>();
					ResourceEntry fre = resourceService
							.getResourceEntryById(rId);
					ResourceDictionaryEntry rde = resourceDictionaryService
							.getResourceDictionaryEntry(new ObjectId(pe
									.getPart()));

					List<PropertyObject> vpList = new ArrayList<PropertyObject>();
					for (IdValuePair ivp : rde.getParentInfos()) {
						vpList.add(new PropertyObject(ivp.getId().toString(),
								ivp.getValue().toString()));
					}
					vpList.add(new PropertyObject(rde.getID().toString(), rde
							.getName()));

					FileType type = FileType.getFileType(fre.getType());
					m.put("id", pe.getID().toString());
					m.put("fId", rId.toString());
					m.put("name", fre.getName());
					m.put("cover", pe.getPreparationCover());
					m.put("time", sdf.format(new Date(pe.getPublishTime())));
					m.put("ver", vpList);
					m.put("kno", new ArrayList<PropertyObject>()
							.add(new PropertyObject("", "/")));
					m.put("userName", pe.getPublishUserName());
					m.put("from", "集体备课");
					m.put("fileType", type == null ? "其他格式" : type.getName());
					pFomartList.add(m);
				}
			}
			allList.addAll(pFomartList);

			List<Map<String, Object>> rFomartList = new ArrayList<Map<String, Object>>();
			for (ReviewEntry rre : rList) {
				List<ObjectId> allRObjectIdList = rre.getCoursewareList();
				allRObjectIdList.add(rre.getClassRecord());
				for (ObjectId rId : allRObjectIdList) {
					ResourceEntry re = resourceService
							.getResourceEntryById(rId);
					if (re == null || re.getIsIgnore() == 1) {
						continue;
					}
					Map<String, Object> m = new HashMap<String, Object>();
					ResourceEntry fre = resourceService
							.getResourceEntryById(rId);
					ResourceDictionaryEntry rde = resourceDictionaryService
							.getResourceDictionaryEntry(new ObjectId(rre
									.getPart()));

					List<PropertyObject> vpList = new ArrayList<PropertyObject>();
					for (IdValuePair ivp : rde.getParentInfos()) {
						vpList.add(new PropertyObject(ivp.getId().toString(),
								ivp.getValue().toString()));
					}
					vpList.add(new PropertyObject(rde.getID().toString(), rde
							.getName()));

					FileType type = FileType.getFileType(fre.getType());
					m.put("id", rre.getID().toString());
					m.put("fId", rId.toString());
					m.put("name", fre.getName());
					m.put("cover", rre.getReviewCover());
					m.put("time", sdf.format(new Date(rre.getPublishTime())));
					m.put("ver", vpList);
					m.put("kno", new ArrayList<PropertyObject>()
							.add(new PropertyObject("", "/")));
					m.put("userName", rre.getPublishUserName());
					m.put("from", "评课议课");
					m.put("fileType", type == null ? "其他格式" : type.getName());
					rFomartList.add(m);
				}
			}
			allList.addAll(rFomartList);
		}
		sortByTime(allList);
		List<Map<String, Object>> pagedList = (List<Map<String, Object>>) getListByPage(
				allList, pageNo, pageSize);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("cur", pageNo);
		pageMap.put("total", allList.size());
		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", pagedList);

		return JSON.toJSONString(resultMap);

	}

	
	/**
	 * 根据时间排序，倒序
	 * @param srcList
	 */
	private void sortByTime(List<Map<String,Object>> srcList){
		Comparator<Map<String,Object>> comparator = new Comparator<Map<String,Object>>() {
            @Override
            public int compare(Map<String,Object> m1, Map<String,Object> m2) {
                String s1 = m1.get("time").toString();
                String s2 = m2.get("time").toString();
            	if(s1.compareTo(s2) > 0){
            		return -1;
            	}
            	if(s1.compareTo(s2) < 0){
            		return 1;
            	}
            	return 0;
            }
        };
        Collections.sort(srcList, comparator);
	}
	/**
	 * 去重复
	 * @param srcList
	 * @return
	 */
	private List<Map<String, Object>>  clearRepeat(List<Map<String, Object>> srcList){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<String> ids = new ArrayList<String>();
		for(Map<String, Object> m : srcList){
			if(!ids.contains(m.get("frId"))){
				list.add(m);
				ids.add(m.get("frId").toString());
			}
			
		}
		return list;
	}



	/**
	 * 获取Git查看附件地址
	 */
	@SessionNeedless
	@RequestMapping("/getResourceUrlForGit")
	@ResponseBody
	public String getResourceUrlForGit(@RequestParam(value = "id", defaultValue = "") String id) {
		String url = "";
		if("".equals(id)){
			return url;
		}
		CoursewareEntry ce = coursewareService.getCoursewareEntry(new ObjectId(id));
		ResourceEntry re = resourceService.getResourceEntryById(ce.getFileId());
		String fileSuffix ="";
		String fileName = re.getName();
		int indexOf = fileName.lastIndexOf('.');
		int qiniuType =0;
		if(indexOf>-1){
			fileSuffix = fileName.substring(indexOf).toLowerCase();
		}else{
			FileType fy = FileType.getFileType(re.getType());
			fileSuffix ="."+ fy.getName().toLowerCase();
			qiniuType=fy.getType();
		}

		qiniuType = convertQiniuFileType(fileSuffix);
		String fileType = "o";
		if(VIDEO_TYPE.contains(fileSuffix)){
			fileType = "v";
		}
		if(MUSIC_TYPE.contains(fileSuffix)){
			fileType = "m";
		}
		if(IMAGE_TYPE.contains(fileSuffix)){
			fileType = "i";
		}
		if(SWF_TYPE.contains(fileSuffix)){
			fileType = "s";
		}
		if(SWF_TYPE.contains(fileSuffix)){
			url = re.getImgUrl();
			if("".equals(url)){
				url="http://7xj25c.com1.z0.glb.clouddn.com/"+re.getID()+fileSuffix;
			}
		}else{
			url = QiniuFileUtils.getPath(qiniuType, re.getBucketkey());
			if("".equals(url)){
				url = QiniuFileUtils.getPath(qiniuType, re.getBucketkey());
			}
		}
		Map<String ,String> m = new HashMap<String,String>();
		m.put("url",url);
		return JSON.toJSONString(m);
	}
	
	
	/**
	 * 查询管理页面附件查看地址
	 */
	@SessionNeedless
	@RequestMapping("/getResourceUrlForManagerPage")
	@ResponseBody
	public String getResourceUrlForManagerPage(@RequestParam(value = "id", defaultValue = "") String id,
											   @RequestParam(value = "fId", defaultValue = "") String fId) {
		ObjectId targetId = null;
		String url = "";
		if("".equals(id)){
			return url;
		}
		CoursewareEntry ce = coursewareService.getCoursewareEntry(new ObjectId(id));
		PreparationEntry pe = preparationService.getPreparationEntryById(new ObjectId(id));
		ReviewEntry rve = reviewService.getReviewEntry(id);
		
		
		
		if(ce != null){
			targetId = ce.getFileId();
		}
		if(pe != null){
			targetId = new ObjectId(fId);
		}
		if(rve !=null){
			targetId = new ObjectId(fId);
		}
		if(targetId == null){
			return url;
		}
		ResourceEntry re = resourceService.getResourceEntryById(targetId);
		
		String fileName = re.getName();
		String fileSuffix = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
		int qiniuType = convertQiniuFileType(fileSuffix);
		
		if(SWF_TYPE.contains(fileSuffix)){
			url = re.getImgUrl();
		}else{
			url = QiniuFileUtils.getPath(qiniuType, re.getBucketkey());
		}
		
		
		Map<String ,String> m = new HashMap<String,String>();
		m.put("url",url);
		return JSON.toJSONString(m);
	}
	
	
}
