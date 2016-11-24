package com.fulaan.itemquestions.controller;

import com.alibaba.fastjson.JSON;
import com.db.questions.ItemStoreDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.itemquestions.service.ItemStoreService;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.user.service.UserService;
import com.pojo.app.IdValuePair;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.questions.ItemProperty;
import com.pojo.questions.ItemStoreEntry;
import com.pojo.questions.PropertiesObj;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author zoukai
 *
 */

@Controller
@RequestMapping("/itemstore")
public class ItemStoreController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private ResourceDictionaryService resourceDictionaryService;
	@Autowired
	private EducationBureauService educationBureauService;

	@Autowired
	private ItemStoreService iss;


	

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	
 
	/**
	 * 后台题库编辑页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/editor")
	@ResponseBody
	public ModelAndView showEditPage(
			@RequestParam(value = "id", defaultValue = "") String id) {
		ModelAndView mv = uploadAddAndEditPageView(id);
		mv.setViewName("tiku/editTiku");

		

		return mv;
	}

	// 后台题库编辑
	private ModelAndView uploadAddAndEditPageView(String id) {
		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();

		ItemStoreEntry ce = iss.getItemStoreEntry(new ObjectId(id));
		String questionTopic = ce.getquestionTopic();
		String contentOfQuestion = ce.getcontentOfQuestion();
		String answerAnalysis = ce.getanswerAnalysis();
		String rightAnswer = ce.getrightAnswer();
		List<ItemProperty> rpList = ce.getproperties();
		for (ItemProperty rp : rpList) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, String>> vList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> kList = new ArrayList<Map<String, String>>();
			for (PropertiesObj vpo : rp.getTeachingVersion()) {
				Map<String, String> m = new HashMap<String, String>();
				m.put("id", vpo.getId());
				m.put("name", vpo.getName());
				vList.add(m);
			}
			for (PropertiesObj kpo : rp.getKnowledgePoint()) {
				Map<String, String> m = new HashMap<String, String>();
				m.put("id", kpo.getId());
				m.put("name", kpo.getName());
				kList.add(m);
			}

			map.put("ver", vList);
			map.put("kno", kList);
			propertyList.add(map);
		}

		ModelAndView mv = new ModelAndView();
		mv.addObject("properttList", JSON.toJSONString(propertyList));
		mv.addObject("id", id);
		mv.addObject("questionTopic", questionTopic);
		mv.addObject("contentOfQuestion", contentOfQuestion);
		mv.addObject("answerAnalysis", answerAnalysis);
		mv.addObject("rightAnswer", rightAnswer);
		return mv;
	}

	/**
	 * 更新一个item
	 */
	@SessionNeedless
	@RequestMapping("/updateTiku")
	@ResponseBody
	public String updateCourseware(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "verStr", defaultValue = "") String verStr,
			@RequestParam(value = "knoStr", defaultValue = "") String knoStr,
			@RequestParam(value = "questionTopic", defaultValue = "") String questionTopic,
			@RequestParam(value = "contentOfQuestion", defaultValue = "") String contentOfQuestion,
			@RequestParam(value = "rightAnswer", defaultValue = "") String rightAnswer,

			
			@RequestParam(value = "answerAnalysis", defaultValue = "") String answerAnalysis) {

			


		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue()
				.getId()));

//		EducationBureauEntry ebe = educationBureauService
//				.selEducationByUserId(ue.getID());
		int role=getSessionValue().getUserRole();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法添加题目!";
			return JSON.toJSONString(respObj);
		}

		List<ItemProperty> properties = new ArrayList<ItemProperty>();
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
			properties.add(new ItemProperty(uploadPropertyObjectList(ve),
					uploadPropertyObjectList(ke)));
		}


		

		iss.updateItem(new ObjectId(id) , questionTopic, contentOfQuestion, rightAnswer, answerAnalysis, properties);

		return JSON.toJSONString(RespObj.SUCCESS);

	}

	/**
	 * 后台资源库新增页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView showListPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tiku/tikuguanli");
		return mv;
	}

	/**
	 * 题库新增页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/addtiku")
	@ResponseBody
	public ModelAndView showList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tiku/addtiku");

	
		return mv;
	}


	
	



	// 添加新题
	@SessionNeedless
	@RequestMapping("/addItem")
	@ResponseBody
	public String addController(
			@RequestParam(value = "verStr", defaultValue = "") String verStr,
			@RequestParam(value = "knoStr", defaultValue = "") String knoStr,
			@RequestParam(value = "questionTopic", defaultValue = "") String questionTopic,
			@RequestParam(value = "contentOfQuestion", defaultValue = "") String contentOfQuestion,
			@RequestParam(value = "rightAnswer", defaultValue = "") String rightAnswer,
			@RequestParam(value = "answerAnalysis", defaultValue = "") String answerAnalysis) {

		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue()
				.getId()));

		List<ItemProperty> properties = new ArrayList<ItemProperty>();
		String[] verArr = verStr.split(",");
		String[] knoArr = knoStr.split(",");
		if (verArr.length != knoArr.length) {
			return JSON.toJSONString(RespObj.FAILD);

			
			
//			for(int i=0;i<verArr.length;i++){
//				ResourceDictionaryEntry ve = resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(verArr[i]));
//				ResourceDictionaryEntry ke = resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(knoArr[i]));
//				properties.add(new ItemProperty(uploadPropertyObjectList(ve), uploadPropertyObjectList(ke)));
//			}
			
//			ItemStoreEntry ce = new ItemStoreEntry(questionTopic,contentOfQuestion,rightAnswer, 
//					answerAnalysis,new Date().getTime(), ue.getUserName(), ue.getID(), properties,1);
//			iss.addItem(ce);
//			return JSON.toJSONString(RespObj.SUCCESS);

		}
		for (int i = 0; i < verArr.length; i++) {
			ResourceDictionaryEntry ve = resourceDictionaryService
					.getResourceDictionaryEntry(new ObjectId(verArr[i]));
			ResourceDictionaryEntry ke = resourceDictionaryService
					.getResourceDictionaryEntry(new ObjectId(knoArr[i]));
			properties.add(new ItemProperty(uploadPropertyObjectList(ve),
					uploadPropertyObjectList(ke)));
		}

		ItemStoreEntry ce = new ItemStoreEntry(questionTopic,
				contentOfQuestion, rightAnswer, answerAnalysis,
				new Date().getTime(), ue.getUserName(), ue.getID(), properties,
				1);

		iss.addItem(ce);
		return JSON.toJSONString(RespObj.SUCCESS);
	}

	private List<PropertiesObj> uploadPropertyObjectList(
			ResourceDictionaryEntry rde) {
		List<PropertiesObj> poList = new ArrayList<PropertiesObj>();
		for (IdValuePair ivp : rde.getParentInfos()) {
			poList.add(new PropertiesObj(ivp.getId().toString(), ivp.getValue()
					.toString()));
		}
		poList.add(new PropertiesObj(rde.getID().toString(), rde.getName()));
		return poList;
	}

	/**
	 * 查询所有问题
	 */
	@SessionNeedless
	@RequestMapping("/allItem")
	@ResponseBody
	public String querySavedList(
			@RequestParam(value = "questionTopic", defaultValue = "") String questionTopic,
			@RequestParam(value = "typeInt", defaultValue = "-1") int typeInt,
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "propertyType", defaultValue = "") String propertyType,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		List<ItemStoreEntry> srcList = new ArrayList<ItemStoreEntry>();
		Collection<ItemStoreEntry> c1 = iss.getItemNotSavedByIdInKnowledge(id,
				questionTopic);
		Collection<ItemStoreEntry> c2 = iss.getItemNotSavedByIdInVersion(id,
				questionTopic);
		Collection<ItemStoreEntry> c3 = iss.getItemSavedByIdInKnowledge(id,
				questionTopic);
		Collection<ItemStoreEntry> c4 = iss.getItemSavedByIdInVersion(id,
				questionTopic);

		if ("tcv".equals(propertyType)) {
			srcList.addAll(c2);
			srcList.addAll(c4);

		} else {
			srcList.addAll(c1);
			srcList.addAll(c3);
		}
		List<ItemStoreEntry> pagedList = (List<ItemStoreEntry>) getListByPage(srcList, pageNo, pageSize);

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
	 * 根据分页信息进行数据筛选
	 * 
	 * @param src
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<? extends Object> getListByPage(List<? extends Object> src,int pageNo, int pageSize) {
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
			List<ItemStoreEntry> srcList) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (ItemStoreEntry ce : srcList) {
			Map<String, Object> m = new HashMap<String, Object>();
			List<ItemProperty> propList = ce.getproperties();
			m.put("id", ce.getID().toString());
			m.put("qt", ce.getquestionTopic());
			m.put("pti", sdf.format(new Date(ce.getPublishTime())));
			m.put("tcv", propList.get(0).getTeachingVersion());
			m.put("kpn", propList.get(0).getKnowledgePoint());
			m.put("coq", ce.getcontentOfQuestion());
			m.put("ria", ce.getrightAnswer());
			m.put("ani", ce.getanswerAnalysis());
			m.put("pun", ce.getpublishUserName());
			m.put("is", ce.getIsSaved());
			resultList.add(m);
		}
		return resultList;
	}

	@SessionNeedless
	@RequestMapping("/removeItem")
	@ResponseBody
	public RespObj ignoreitem(
			@RequestParam(value = "id", defaultValue = "") String id) {
		iss.deleteItem(new ObjectId(id));
		return RespObj.SUCCESS;

	}

	@SessionNeedless
	@RequestMapping("/all")
	@ResponseBody
	public Map<String, Object> findByQuanBuController(
			@RequestParam(value = "questionTopic", defaultValue = "") String questionTopic,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		ItemStoreDao dt = new ItemStoreDao();

		int skip=(pageNo-1)*pageSize;
		List<ItemStoreEntry> list = dt.fandAll(questionTopic,skip,pageSize);
		int total=dt.fandAllCount(questionTopic);
		//List<ItemStoreEntry> pagedList = (List<ItemStoreEntry>) getListByPage(list, pageNo, pageSize);
		List<Map<String, Object>> formatList = formatItemForListView(list);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("cur", pageNo);
		pageMap.put("total", total);
		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", formatList);

		return resultMap;
	}

	@SessionNeedless
	@RequestMapping("/queryOneItem")
	@ResponseBody
	public String queryOneItem(
			@RequestParam(value = "id", defaultValue = "") String id) {
		ItemStoreEntry ise = iss.getItemStoreEntry(new ObjectId(id));
		Map<String, Object> map = new HashMap<String, Object>();
		List<ItemProperty> props = ise.getproperties();
		
		List<PropertiesObj> ver;
		List<PropertiesObj> kno;
		List<String> verList = new ArrayList<String>();
		List<String> knoList = new ArrayList<String>();
		
		for(int i=0;i<props.size();i++){
			StringBuffer verBuffer = new StringBuffer();
			StringBuffer knoBuffer = new StringBuffer();
			ver = props.get(i).getTeachingVersion();
			kno = props.get(i).getKnowledgePoint();
			verBuffer.append(ver.get(0).getName());
			verBuffer.append("--");
			verBuffer.append(ver.get(1).getName());
			verBuffer.append("--");
			verBuffer.append(ver.get(2).getName());
			verBuffer.append("--");
			verBuffer.append(ver.get(3).getName());
			verBuffer.append("--");
			verBuffer.append(ver.get(4).getName());
			verBuffer.append("--");
			verBuffer.append(ver.get(5).getName());

			knoBuffer.append(kno.get(0).getName());
			knoBuffer.append("--");
			knoBuffer.append(kno.get(1).getName());
			knoBuffer.append("--");
			knoBuffer.append(kno.get(2).getName());
			knoBuffer.append("--");
			knoBuffer.append(kno.get(3).getName());
			
			verList.add(verBuffer.toString());
			knoList.add(knoBuffer.toString());
			
		}
		String answer = ise.getrightAnswer();
		map.put("verList", verList);
		map.put("knoList", knoList);
		map.put("content", ise.getcontentOfQuestion());
		map.put("answer", answer);
		map.put("why", ise.getanswerAnalysis());
		map.put("is", ise.getIsSaved());
		map.put("qt", ise.getquestionTopic());
		String f = ise.getquestionTopic();
		
		try {
			if (f.equals("选择题")) {
				// String regex = "[A-Z]";
				String regex = "_";
				String[] a1 = answer.split(regex);
				
				StringBuffer sb = new StringBuffer();
				String[] result = new String[a1.length];
				for (int i = 0; i < a1.length; i++) {
					
					String regex1 = "[0-9]";
					String[] a2 = a1[i].split(regex1);
					a2[0]=a2[0].replace(',',' ');
					result[i] = a2[0];
				}
				map.put("answer", result);
			}
			
			if(f.equals("填空题")){
				
				String[] b=answer.split(",");
				String[] result = new String[b.length];
				for(int i=0;i<b.length;i++){
					result[i] = b[i];
					
				}
				map.put("answer",result);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(map);

	}

	@SessionNeedless
	@RequestMapping("/updateStatus1")
	@ResponseBody
	public RespObj updateStatus1(
			@RequestParam(value = "id", defaultValue = "") String id) {
		ItemStoreDao dt = new ItemStoreDao();
		dt.updateStatus(new ObjectId(id), 1);
		return RespObj.SUCCESS;

	}

	@SessionNeedless
	@RequestMapping("/updateStatus2")
	@ResponseBody
	public RespObj updateStatus2(
			@RequestParam(value = "id", defaultValue = "") String id) {
		ItemStoreDao dt = new ItemStoreDao();
		dt.updateStatus(new ObjectId(id), 2);
		return RespObj.SUCCESS;

	}
}
