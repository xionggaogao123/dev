package com.fulaan.examregional.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.db.examregional.RegionalExamDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.exam.service.ScoreService;
import com.fulaan.examregional.service.RegionalExamService;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.ImportExcelUtil;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ExamSubjectEntry;
import com.pojo.exam.ScoreEntry;
import com.pojo.exam.SubjectScoreEntry;
import com.pojo.examregional.ExamSummaryEntry;
import com.pojo.examregional.RegionalExamEntry;
import com.pojo.examregional.RegionalSchItem;
import com.pojo.examregional.RegionalSubjectItem;
import com.pojo.examregional.SubjectDetails;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/regional")
public class RegionalExamController extends BaseController {
	private RegionalExamDao regionalExamDao = new RegionalExamDao();
	@Autowired
	private RegionalExamService regionalExamService;
	@Autowired
	private ExamResultService examResultService;
	@Autowired
	private ScoreService scoreService;

	/**
	 * 录入页面的跳转
	 */
	@SessionNeedless
	@RequestMapping("/inputGrade")
	@ResponseBody
	public ModelAndView showInputPage(
			@RequestParam(value = "id", defaultValue = "") String id) {
		ModelAndView  mav = new ModelAndView();
		ObjectId sessionSchoolId = new ObjectId(getSessionValue().getSchoolId());
		ExamEntry examEntry = regionalExamService.findExamEntryById(new ObjectId(id));
		//查询本学校参加区域联考的eid
		ObjectId myschoolId=examEntry.getSchoolId();
		ObjectId examId = examEntry.getRegionalExamId();
		List<RegionalSchItem> schoolList=regionalExamService.findRegionalExamById(new ObjectId(examId.toString())).getSchool();
		if(myschoolId!=null && myschoolId.equals(sessionSchoolId)){
			mav.addObject("examId", examId);
		}
		for(RegionalSchItem schoolInfo: schoolList){
			if(schoolInfo.getSchoolId()!=null && schoolInfo.getSchoolId().equals(sessionSchoolId)){
				mav.addObject("Flag", schoolInfo.getFlag());
			}
		}
		mav.addObject("id", id);
		mav.addObject("titleName", examEntry.getName());
		mav.setViewName("exam/inputGrade");
		return mav;
	}
	/**
	 * 学校提交到教育局
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SessionNeedless
	@RequestMapping("/toEduDepart")
	@ResponseBody
	public RespObj toEduDepart(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
		RespObj result = new RespObj(Constant.FAILD_CODE);
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		RegionalExamEntry re = regionalExamService.findRegionalExamById(new ObjectId(id));
		List<RegionalSchItem> schoolList = re.getSchool();
		for(RegionalSchItem school : schoolList){
			if(school.getSchoolId()!=null && school.getSchoolId().equals(schoolId)){
				calculateAndRankScore(re,school);
				school.setFlag(Constant.ONE);
			}
		}
		//更新
		regionalExamService.updateJointexamById(id,schoolList);
		result.setCode(Constant.SUCCESS_CODE);
		result.setMessage(Constant.SUCCESS);
		return result;
	}
	/**
	 * 提交到教育局时调用的计算排名等方法
	 * @param regionalexamEntry
	 * @param shcoolItem
	 * @throws Exception
	 */
	private void calculateAndRankScore(RegionalExamEntry regionalexamEntry,RegionalSchItem shcoolItem) throws Exception{
		List<ScoreEntry> performanceList = sortWithScore(scoreService.findByExidWithoutPage(shcoolItem.getExamId()));
		List<RegionalSubjectItem> subjectList = regionalexamEntry.getExamSubject();
		Map<String,SubjectDetail> subjectMap = new HashMap<String,SubjectDetail>();
		for(RegionalSubjectItem si : subjectList){
			subjectMap.put(si.getSubjectId().toString(),new SubjectDetail(si));
		}
		for(ScoreEntry se : performanceList){
			scoreService.updateSchoolRankById(se.getID(), se.getSchoolRanking());
			List<SubjectScoreEntry> subSubjectList = se.getExamScore();
			for(SubjectScoreEntry subSe : subSubjectList){
				SubjectDetail targetDetail = subjectMap.get(subSe.getSubjectId().toString());
				if(targetDetail == null){
					throw new Exception("计算排名出错1！科目未找到！科目名为:" + subSe.getSubjectName());
				}
				double targetScore = subSe.getScore();
				if(targetDetail.getMax() == -1.0 || targetDetail.getMax() < targetScore){
					targetDetail.setMax(targetScore);
				}
				if(targetDetail.getMin() == -1.0 || targetDetail.getMin() > targetScore){
					targetDetail.setMin(targetScore);
				}
				targetDetail.setSumScore(targetDetail.getSumScore() + targetScore);
				targetDetail.checkPassAndGrate(targetScore);
			}
		}
		ExamSummaryEntry summaryEntry = regionalExamService.getExamSummaryEntryByExamIdAndSchoolId(regionalexamEntry.getID(),shcoolItem.getSchoolId());
		List<SubjectDetails> sdList = summaryEntry.getSubjectDetails();
		int stuNum = summaryEntry.getStudentNumber();
		double csAll = 0;
		for(SubjectDetails sd : sdList){
			SubjectDetail targetDetail = subjectMap.get(sd.getSubjectId().toString());
			if(targetDetail == null){
				throw new Exception("计算排名出错2！科目未找到！科目名为:" + sd.getSubjectName());
			}
			targetDetail.caculate(stuNum);
			sd.setAverageScore(targetDetail.getAvg());
			sd.setCompositeScores(targetDetail.getCS());
			sd.setExcellentNumber(targetDetail.getGrateNum());
			sd.setExcellentRate(targetDetail.getGratePer());
			sd.setMaxScore(targetDetail.getMax());
			sd.setMinScore(targetDetail.getMin());
			sd.setPassNumber(targetDetail.getPassNum());
			sd.setPassRate(targetDetail.getPassPer());
			csAll = csAll + targetDetail.getCS();
		}
		regionalExamService.updateExamSummaryById(summaryEntry.getID(),csAll,sdList);

	}
	/**6
	 * 根据总分进行排序
	 * @param srcList
	 * @return
	 */
	private List<ScoreEntry> sortWithScore(List<ScoreEntry> srcList){
		Comparator<ScoreEntry> comparator = new Comparator<ScoreEntry>() {
			@Override
			public int compare(ScoreEntry m1, ScoreEntry m2) {
				double s1 = m1.getScoreSum();
				double s2 = m2.getScoreSum();
				if(s1 > s2){
					return -1;
				}
				if(s1 < s2){
					return 1;
				}
				return 0;
			}
		};
		Collections.sort(srcList, comparator);
		double lastScore = 0D;
		int lastRank = 1;
		for(int i=0;i<srcList.size();i++){
			ScoreEntry scoreEntry = srcList.get(i);
			if(lastScore == scoreEntry.getScoreSum()){
				scoreEntry.setchoolRanking(lastRank);
			} else {
				scoreEntry.setchoolRanking(i+1);
				lastRank = i + 1;
				lastScore = scoreEntry.getScoreSum();
			}

		}
		return srcList;
	}

	/**
	 * 学生成绩列表
	 * areaExamId
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/scoreList")
	@ResponseBody
	public String findJointExamByEID(String areaExamId, int pageNo, int pageSize){
		RespObj respObj = new RespObj(Constant.FAILD_CODE);
		int startIndex = (pageNo - 1) * pageSize;
		List<ScoreEntry> scoreList = regionalExamService.loadExamByPage(areaExamId, startIndex, pageSize);
		int scoreEntryCount = regionalExamService.countScoreEntryService(new ObjectId(areaExamId));
		if(scoreList.size()<1){
			respObj.setMessage("该场考试学生成绩未初始化或无学生成绩！请联系管理员！");
			return JSONObject.toJSON(respObj).toString();
		}

		List<Map<String,Object>> scoreDto = new ArrayList<Map<String,Object>>();
		for(ScoreEntry s : scoreList){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("studentName", s.getStudentName());
			map.put("className", s.getClassName());
			map.put("scoreSum", String.format("%.2f", s.getScoreSum()));
			List<SubjectScoreEntry> subjectScore = s.getExamScore();
			List<Object> list = new ArrayList<Object>();
			double titleFullScore =0;
			for(SubjectScoreEntry ss: subjectScore){
				Map<String,Object> subjectMap = new HashMap<String,Object>();
				String subName = ss.getSubjectName();
				double fullScore = ss.getFull();
				titleFullScore += fullScore;
				double subscore = ss.getScore();
				subjectMap.put("subName", subName);
				subjectMap.put("fullScore", fullScore);
				subjectMap.put("subscore",String.format("%.2f", subscore));
				list.add(subjectMap);
			}
			map.put("titleFullScore", titleFullScore);
			map.put("subList", list);
			scoreDto.add(map);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("cur", pageNo);
		pageMap.put("total", scoreEntryCount);

		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", scoreDto);
		resultMap.put("code", Constant.SUCCESS_CODE);
//		List<Map<String,Object>> pageList = (List<Map<String,Object>>) getListByPage(scoreDto, pageNo, pageSize);
		return JSONObject.toJSON(resultMap).toString();
	}

	/**
	 * 导入区域联考成绩信息
	 */
	@SessionNeedless
	@RequestMapping("/import")
	public void importProperty(HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final RespObj respObj = new RespObj(Constant.FAILD_CODE);
		response.setContentType("text/html;charset=UTF-8");
		final ImportExcelUtil scoreImport = new ImportExcelUtil(0, 0, new ImportExcelUtil.IConvertRow() {
			@Override
			public Object convert(List<String> rowData, List<String> titles) throws ImportException {
				if(!("标识".equals(titles.get(0)) && "姓名".equals(titles.get(1)) && "班级".equals(titles.get(2)))){
					throw new ImportException("导入的文件错误、破损，请重新选择!");
				}
				List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
				for(int i=3;i<titles.size();i++){
					Map<String,String> map = new HashMap<String,String>();
					String subjectTitle = titles.get(i);
					String subjectId = subjectTitle.substring(subjectTitle.indexOf("{") + 1,subjectTitle.length() - 1);
					map.put("id", rowData.get(0));
					map.put("sid", subjectId);
					String afterTrim = rowData.get(i).trim();
					if(isNum(afterTrim)){
						map.put("score", afterTrim);
					}else{
						String errorMsg=rowData.get(2)+"班【"+rowData.get(1)+"】"+subjectTitle.substring(0, subjectTitle.indexOf("{"))+"成绩错误！";
						throw new ImportException(errorMsg);
					}
					resultList.add(map);
				}

				return resultList;
			}
		}, new ImportProperty());
		try {
			scoreImport.importData(request, getSessionValue().getId(), "examItemData");
			respObj.setCode(Constant.SUCCESS_CODE);
			response.getWriter().write(JSONObject.toJSON(respObj).toString());
		} catch (IllegalParamException e) {
			respObj.setMessage(e.getMessage());
			response.getWriter().write(JSONObject.toJSON(respObj).toString());
		} catch (ImportException e) {
			respObj.setMessage(e.getMessage());
			response.getWriter().write(JSONObject.toJSON(respObj).toString());
		} catch (Exception e) {
			respObj.setMessage("导入文件处理错误");
			response.getWriter().write(JSONObject.toJSON(respObj).toString());
		}

	}
	//导入分数验证
	private boolean isNum(String str) {
		return str.matches("^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	private class ImportProperty implements ImportExcelUtil.ISaveData {
		public ImportProperty() {
			super();
		}

		@Override
		public void save(List data) throws ImportException {
//            scoreService.updateByImport(data, examId, UserRole.isHeadmaster(getSessionValue().getUserRole())
//                    || UserRole.isManager(getSessionValue().getUserRole()));
			//这里写入数据库
			for(List<Map<String,String>> l : (List<List<Map<String,String>>>)data){
				float sumScore = 0;
				String targetId = l.get(0).get("id");
				ScoreEntry scoreEntry = regionalExamService.loadPerformanceById(targetId);
				List<SubjectScoreEntry> subjectScoreList = scoreEntry.getExamScore();
				for(int i=0;i<l.size();i++){
					float score = Float.parseFloat(l.get(i).get("score"));//科目分数
					boolean isHave = false;
					for(SubjectScoreEntry sse : subjectScoreList){
						if(sse.getSubjectId()!= null && sse.getSubjectId().equals(l.get(i).get("sid"))){
							if(score>sse.getFull()){
								throw new ImportException("分数数据异常！"+ scoreEntry.getClassName()+"班【"+ scoreEntry.getStudentName()+"】"+sse.getSubjectName()+"科目,满分为:【"+sse.getFull()+"】");
							}
							sse.setScore(score);
							isHave = true;
							break;
						}
					}
					if(!isHave){
						throw new ImportException();
					}
					sumScore += score;//总分
				}
				//执行更新
				regionalExamService.updatePerformanceById(targetId, sumScore, subjectScoreList);

			}
		}
	}

	/**
	 * 导出录入区域联考成绩模板
	 */
	@SessionNeedless
	@RequestMapping("/exportModel")
	@ResponseBody
	public String exportModel(HttpServletRequest request, HttpServletResponse response)  throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		RespObj respObj = new RespObj(Constant.FAILD_CODE);
		//查询
		if (StringUtils.isEmpty(request.getParameter("areaExamId"))) {
			respObj.setMessage("导出信息错误！");
			return JSONObject.toJSON(respObj).toString();
		}

		String areaExamId = request.getParameter("areaExamId");
		List<ScoreEntry> scoreList = regionalExamService.loadExam(areaExamId);
		ExamEntry examEntry = regionalExamService.findExamEntryById(new ObjectId(areaExamId));
		if(scoreList.size() < 1){
			respObj.setMessage("该场考试学生成绩未初始化或无学生成绩！请联系管理员！");
			return JSONObject.toJSON(respObj).toString();
		}

		response.setContentType("application/octet-stream;charset=UTF-8");
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		ExportUtil util = null;
		try {
			util = new ExportUtil();
			List<String> datas = new ArrayList<String>();

			datas.add("标识");
			datas.add("姓名");
			datas.add("班级");

			List<SubjectScoreEntry> subjectList = scoreList.get(0).getExamScore();

			for(int i=0;i<subjectList.size();i++){
				SubjectScoreEntry subject = subjectList.get(i);
				datas.add(subject.getSubjectName().toString()+
						"{"+subject.getSubjectId().toString()+"}");
			}

			util.addTitle(datas.toArray());
			datas.clear();

			for(ScoreEntry se : scoreList){
				datas.add(se.getID().toString());
				datas.add(se.getStudentName());
				datas.add(se.getClassName());
//            	for(SubjectScoreEntry sse : se.getExamScore()){
//            		datas.add(String.format("%.2f", sse.getScore()) + "");
//            	}
				util.appendRow(datas.toArray());
				datas.clear();
			}

			util.setFileName(String.format("%s_%s", sdf.format(new Date()), examEntry.getSchoolYear()+"_"+examEntry.getName()+"区域联考成绩录入模板.xlsx"));
			response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
			util.getBook().write(response.getOutputStream());
		} finally {
			if (util != null) {
				util.destroy();
			}
		}
		return null;
	}
	/**
	 * 获取导出文件的名称
	 *
	 * @param request
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
		String agent = request.getHeader("User-Agent");
		if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
			fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
		} else {
			fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
			fileName = fileName.replace("+", " ");
		}
		return fileName;
	}

	/**
	 * 教育局
	 * 根据教育局id查询本年的学期信息
	 */

	@SessionNeedless
	@RequestMapping("/findTrem")
	@ResponseBody
	public String fandByEducationd() {
		String uid = getSessionValue().getId();
		RegionalExamDao red = new RegionalExamDao();
		ObjectId eid = red.selEducationByUserId(new ObjectId(uid));

		List<Map<String, Object>> lists = regionalExamService.tremFind(eid);
		Map<String,  Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", lists);
		return JSON.toJSONString(resultMap);

	}
	/**
	 * 分页
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
	/**
	 * 教育局
	 * 根据学期信息查询区域联考信息
	 */
	@SessionNeedless
	@RequestMapping("/findByEducationTrem")
	@ResponseBody
	public String jointDetail(
			@RequestParam(value = "term", defaultValue = "") String term,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		String uid= getSessionValue().getId();
		RegionalExamDao red=new RegionalExamDao();
		ObjectId eid=red.selEducationByUserId(new ObjectId(uid));
		List<Map<String,Object>> lists=regionalExamService.findByTrem(term, eid);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> pageList = (List<Map<String,Object>>) getListByPage(lists, pageNo, pageSize);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("cur", pageNo);
		pageMap.put("total", lists.size());
		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", pageList);
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 教育局
	 * 根据每个联考项目的id查询这次联考的所有信息
	 */
	@SessionNeedless
	@RequestMapping("/findByJointId")
	@ResponseBody
	public String allByJointId(String id) {
//		id="563ac100be754c909218edd5";
		List<Map<String, Object>> ree = regionalExamService.allById(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", ree);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 跳转到新增区域联考页面
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/newRegional")
	public ModelAndView findIndex(){
		ModelAndView mav=new ModelAndView();

		mav.addObject("term", examResultService.getTerm(System.currentTimeMillis()));

		mav.setViewName("exam/newRegionalExam");
		return mav;
	}


	/**
	 * 获取教育局id
	 * @param
	 * @return
	 */
	public ObjectId getEducationId(){
		String userId = new BaseController().getSessionValue().getId();
		ObjectId eduId = regionalExamService.getEduId(userId);
		return eduId;
	}
	/**
	 * 加载年级列表
	 */
	@SessionNeedless
	@RequestMapping("/loadGradeList")
	@ResponseBody
	public RespObj loadGradeList(){
		RespObj result = new RespObj(Constant.FAILD_CODE);
		List<Object> list = regionalExamService.getGradeNameList(getEducationId());
		result.setMessage(list);
		result.setCode(Constant.SUCCESS_CODE);
		return result;
	}
	/**
	 * 加载科目列表
	 */
	@SessionNeedless
	@RequestMapping("/loadSubList")
	@ResponseBody
	public RespObj loadSubList(@RequestParam(value = "gradeType", defaultValue = "0") int gradeType){
		RespObj result = new RespObj(Constant.FAILD_CODE);
		List<Object> list = regionalExamService.getSubjectsList(getEducationId(),gradeType);
		result.setMessage(list);
		result.setCode(Constant.SUCCESS_CODE);
		return result;
	}
	/**
	 * 加载学校列表
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/loadSchList")
	@ResponseBody
	public RespObj loadSchList(@RequestParam(value = "gradeType", defaultValue = "0") int gradeType){
		RespObj result = new RespObj(Constant.FAILD_CODE);
		List<ObjectId> schIdlist = regionalExamService.getSchoolIdList(getEducationId());
		List<Object> schList = regionalExamService.getSchoolNameId(schIdlist,gradeType);
		result.setMessage(schList);
		result.setCode(Constant.SUCCESS_CODE);
		return result;
	}

	/**
	 * 新增区域联考
	 *
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addRegionalExam")
	@ResponseBody
	public RespObj addRegionalExam(@RequestParam(value = "term", defaultValue = "") String term,
								   @RequestParam(value = "examName", defaultValue = "") String examName,
								   @RequestParam(value = "gradeType", defaultValue = "") int gradeType,
								   @RequestParam(value = "gradeName", defaultValue = "") String gradeName,
								   @RequestParam(value = "gradeId", defaultValue = "") String gradeId,
								   @RequestParam(value = "startTime", defaultValue = "") String startTime,
								   @RequestParam(value = "subjectStr", defaultValue = "") String subjectStr,
								   @RequestParam(value = "schoolStr", defaultValue = "") String schoolStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		RespObj resultObj = new RespObj(Constant.FAILD_CODE);
		try {
			ObjectId eduId = getEducationId();
			ObjectId regionalExamId = new ObjectId();
			long startDate_long = sdf.parse(startTime).getTime();
			int studyPeriod = getStudyPeriodByGradeType(gradeType);
			List<RegionalSchItem> schoolItemList = new ArrayList<RegionalSchItem>();
			String[] schoolStrArr = schoolStr.split("%");

			List<ExamSubjectEntry> examSubjectList_school = new ArrayList<ExamSubjectEntry>();
			List<RegionalSubjectItem> examSubjectList_regional = new ArrayList<RegionalSubjectItem>();
			List<ObjectId> subjecIdList = new ArrayList<ObjectId>();
			List<SubjectDetails> summarySubjectList = new ArrayList<SubjectDetails>();
			String[] subjectStrArr = subjectStr.split("%");
			for(String subStr : subjectStrArr){
				String[] subSubArr = subStr.split("#");
				String subName = subSubArr[0];
				ObjectId subId = new ObjectId(subSubArr[1]);
				double subFull = Double.parseDouble(subSubArr[2]);
				double subGrate = Double.parseDouble(subSubArr[3]);
				double subPass = Double.parseDouble(subSubArr[4]);
				long subDateLong = sdf.parse(subSubArr[5]).getTime();
				String subDateStr = subSubArr[5];
				String subTimeStr = subSubArr[6];
				examSubjectList_school.add(new ExamSubjectEntry(subId, subName,
						(int)subFull, ((int)subFull) * 0.9, subPass, ((int)subFull) * 0.3,subTimeStr, subDateStr,
						"", 0, null, null));
				examSubjectList_regional.add(new RegionalSubjectItem(subName, subId, (float)subFull, (float)subGrate,
						(float)subPass, subDateLong, subTimeStr));
				summarySubjectList.add(new SubjectDetails(subId, subName));
				subjecIdList.add(subId);
			}
			for(String schStr : schoolStrArr){
				String[] subStrArr = schStr.split("#");
				RegionalSchItem rsi = new RegionalSchItem(subStrArr[1],new ObjectId(subStrArr[0]),null);
				List<ObjectId> schoolSubjectIdList = regionalExamService.getSchoolSubjectIdListByRegionalSubjectIdList(subjecIdList,eduId,rsi.getSchoolId());

				//首先，根据现有的regionalExamId创建出每个学校相应的考试Entry(examresult表),同时初始化perfomance
				List<ObjectId> classIdListForSchool = regionalExamService.getClassIdBySchoolIAndType(rsi.getSchoolId(), gradeType);
				ExamEntry ee = new ExamEntry(examName, startDate_long, new ObjectId(gradeId), gradeName,
						3, "", examSubjectList_school,
						0, 0, rsi.getSchoolId(), term,
						3, classIdListForSchool ,
						schoolSubjectIdList, regionalExamId, studyPeriod);
				ObjectId schoolExamId = regionalExamService.createExamForSchool(ee, examSubjectList_regional);
				rsi.setExamId(schoolExamId);
				schoolItemList.add(rsi);
				//初始化区域联考汇总表并存储
				ExamSummaryEntry ese = new ExamSummaryEntry(regionalExamId, rsi.getSchoolId(), rsi.getName(),
						regionalExamService.countStudentNumByClassIds(classIdListForSchool), 0.0, 0, summarySubjectList, 0);
				regionalExamService.saveExamSummary(ese);
			}
			//开始组装教育局区域联考用Entry,并存储
			RegionalExamEntry ree = new RegionalExamEntry(examName, gradeName,
					gradeType, "区域联考", startDate_long,
					examSubjectList_regional, schoolItemList,
					term, eduId, 0);
			ree.setID(regionalExamId);
			regionalExamService.save(ree);
		} catch (Exception e) {
			e.printStackTrace();
			resultObj.setMessage("新增区域联考失败！请联系管理员。");
			return resultObj;
		}
		resultObj.setMessage("新增区域联考成功！");
		resultObj.setCode(Constant.SUCCESS_CODE);
		return resultObj;
	}
	/**
	 * 根据年级type获取学段int值
	 * @param gradeType
	 * @return
	 */
	private int getStudyPeriodByGradeType(int gradeType){
		if( gradeType < 7 ){
			return 1;
		}else if( gradeType < 10){
			return 2;
		}else if(gradeType < 13){
			return 3;
		}else{
			return 4;
		}

	}

	/**
	 * 学校 根据学校id查询本年的学期信息
	 */

	@SessionNeedless
	@RequestMapping("/Trem")
	@ResponseBody
	public String trem() {
		ExamResultService es=new ExamResultService();
		List<String> trem=es.getUsableTerm();
		Map<String,  Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", trem);
		return JSON.toJSONString(resultMap);
	}



	/**
	 * 学校 根据学校id查询本年的学期信息
	 * 学校
	 * 根据学校id查询本年的学期信息
	 */

	@SessionNeedless
	@RequestMapping("/findSchoolTrem")
	@ResponseBody
	public String fandBySchid() {
		String sid = getSessionValue().getSchoolId();
		List<Map<String, Object>> lists = regionalExamService.tremSchFind(sid);
		Map<String,  Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", lists);
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 学校
	 * 根据学期信息查询区域联考信息
	 */
	@SessionNeedless
	@RequestMapping("/findBySchoolTrem")
	@ResponseBody
	public String schoolJointDetail(
			@RequestParam(value = "schoolYear", defaultValue = "") String schoolYear,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		String sid= getSessionValue().getSchoolId();
		//if(){}
		//String schoolYear="2015-2016学年第一学期";
		List<Map<String,Object>> lists=regionalExamService.findBySchoolTrem(schoolYear, new ObjectId(sid));
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> pageList = (List<Map<String,Object>>) getListByPage(lists, pageNo, pageSize);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("cur", pageNo);
		pageMap.put("total", lists.size());
		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", pageList);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 根据联考id删除
	 */
	@SessionNeedless
	@RequestMapping("/removeAll")
	@ResponseBody
	public String deleteAll(String JointId){
		String str=regionalExamService.remove(JointId);
		return str;
	}
	/**
	 *
	 */
	@SessionNeedless
	@RequestMapping("/schoolList")
	@ResponseBody
	public ModelAndView showList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("exam/schoolExamArea");
		return mv;
	}
	@SessionNeedless
	@RequestMapping("/educationList")
	@ResponseBody
	public ModelAndView showLiswdwt() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("exam/educationExamArea");
		return mv;
	}
	/**
	 * 再次导入区域联考成绩改为未提交到教育局
	 * @param
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/toEduDeparts")
	@ResponseBody
	public RespObj toEduDeparts(@RequestParam(value = "id", defaultValue = "") String id) {
		RespObj result = new RespObj(Constant.FAILD_CODE);
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		List<RegionalSchItem> schoolList=regionalExamService.findRegionalExamById(new ObjectId(id)).getSchool();
		for(RegionalSchItem school : schoolList){
			if(school.getSchoolId()!=null && school.getSchoolId().equals(schoolId)){
				school.setFlag(Constant.ZERO);
			}
		}
		//更新
		regionalExamService.updateJointexamById(id,schoolList);
		result.setCode(Constant.SUCCESS_CODE);
		result.setMessage(Constant.SUCCESS);
		return result;
	}

	class SubjectDetail{
		double grate;
		double pass;
		String name;
		String id;
		double sumScore;
		double avg;
		double max;
		double min;
		int passNum;
		int grateNum;
		double passPer;
		double gratePer;

		public SubjectDetail(RegionalSubjectItem rsi){
			this.grate = rsi.getExcellent();
			this.pass = rsi.getPass();
			this.id = rsi.getSubjectId().toString();
			this.name = rsi.getName();
			this.max = -1.0;
			this.min = -1.0;
		}

		/**
		 * 获取综合分数
		 */
		public double getCS(){
			return (this.avg * 0.4) + (this.passPer * 0.3) + (this.gratePer * 0.3);
		}

		public void caculate(int stuNum){
			this.avg = this.sumScore / stuNum;
			this.gratePer = this.grateNum * 1.0 / stuNum;
			this.passPer = this.passNum * 1.0 / stuNum;

		}
		public void checkPassAndGrate(double score){
			if(score >= this.pass){
				this.passNum = this.passNum + 1;
			}
			if(score >= this.grate){
				this.grateNum = this.grateNum + 1;
			}
		}

		public double getGrate() {
			return grate;
		}
		public void setGrate(double grate) {
			this.grate = grate;
		}
		public double getPass() {
			return pass;
		}
		public void setPass(double pass) {
			this.pass = pass;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public double getSumScore() {
			return sumScore;
		}
		public void setSumScore(double sumScore) {
			this.sumScore = sumScore;
		}
		public double getMax() {
			return max;
		}
		public void setMax(double max) {
			this.max = max;
		}
		public double getMin() {
			return min;
		}
		public void setMin(double min) {
			this.min = min;
		}
		public int getPassNum() {
			return passNum;
		}
		public void setPassNum(int passNum) {
			this.passNum = passNum;
		}
		public int getGrateNum() {
			return grateNum;
		}
		public void setGrateNum(int grateNum) {
			this.grateNum = grateNum;
		}
		public double getPassPer() {
			return passPer;
		}
		public void setPassPer(double passPer) {
			this.passPer = passPer;
		}
		public double getGratePer() {
			return gratePer;
		}
		public void setGratePer(double gratePer) {
			this.gratePer = gratePer;
		}
		public double getAvg() {
			return avg;
		}
		public void setAvg(double avg) {
			this.avg = avg;
		}

	}
}
