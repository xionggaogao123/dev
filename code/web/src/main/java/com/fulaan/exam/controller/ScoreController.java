package com.fulaan.exam.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.exam.service.ExamService;
import com.fulaan.exam.service.ScoreService;
import com.fulaan.examregional.service.RegionalExamService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.ImportExcelUtil;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.exam.ExamDTO;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ExamSubjectDTO;
import com.pojo.exam.ExamSubjectEntry;
import com.pojo.exam.ScoreDTO;
import com.pojo.exam.ScoreEntry;
import com.pojo.exam.SubjectScoreDTO;
import com.pojo.exam.SubjectScoreEntry;
import com.pojo.examregional.RegionalExamEntry;
import com.pojo.examregional.RegionalSubjectItem;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.TeacherClassSubjectDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import com.sys.utils.RespObj;

/**
 * 考试成绩控制转发
 * Created by Caocui on 2015/7/22.
 */
@Controller
@RequestMapping("/score1")
public class ScoreController extends BaseController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ExamService examService;

    @Autowired
    private TeacherClassSubjectService teacherClassSubjectService;

    @Autowired
    private ClassService classService;
    
    @Autowired
    private RegionalExamService regionalExamService;
      
    /**
     * 更新单科成绩信息
     */
    @SessionNeedless
    @RequestMapping("/save")
    @ResponseBody
    public RespObj save(String examId, String scoreId, String subjectId, String examScore) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if (StringUtils.isEmpty(examId) || StringUtils.isEmpty(scoreId) || StringUtils.isEmpty(subjectId) || StringUtils.isEmpty(examScore)) {
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("参数错误");
            return respObj;
        }
        //成绩信息不为空
        ExamSubjectEntry examSubjectEntry = examService.loadExamSubject(examId, subjectId);
        if (!isNum(examScore)) {
            respObj.setCode(Constant.FAILD_CODE);
            //数值校验
            respObj.setMessage("成绩不合法");
            return respObj;
        } else if (examSubjectEntry == null) {
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("参数错误");
            return respObj;
        } else {
            double num = Double.parseDouble(examScore);
            if (num < 0 || num > examSubjectEntry.getFullMarks()) {
                //范围校验
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setMessage("成绩不合法，超出总分范围");
                return respObj;
            }
        }
        Map<String, SubjectScoreDTO> scoreDTOMap = new HashMap<String, SubjectScoreDTO>(Constant.ONE);
        SubjectScoreDTO subjectScoreDTO = new SubjectScoreDTO();
        subjectScoreDTO.setScore(examScore);
        subjectScoreDTO.setSubjectId(subjectId);
        scoreDTOMap.put(subjectId, subjectScoreDTO);
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setExamId(examId);
        scoreDTO.setId(scoreId);
        scoreDTO.setExamScore(scoreDTOMap);
        scoreService.update(scoreDTO);
        respObj.setMessage("成绩信息保存成功");
        return respObj;
    }

    private boolean isNum(String str) {
        return str.matches("^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 获取考试成绩详情
     *
     * @param examId
     * @param classId
     * @return
     */
    @SessionNeedless
    @RequestMapping("/detail")
    @ResponseBody
    public RespObj scoreDetail(String examId, String gradeId, String[] classId, String[] subject, String order) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if (classId == null) {
            List<IdValuePair> idValuePairs = (List<IdValuePair>) this.getClass(gradeId, examId).getMessage();
            List<String> clazz = new ArrayList<String>(idValuePairs.size());
            for (IdValuePair valuePair : idValuePairs) {
                clazz.add(valuePair.getId().toString());
            }
            classId = (String[]) clazz.toArray();
        }
        List<ScoreDTO> scoreDTOs = scoreService.loadDetail(examId, classId, subject, order);
        respObj.setMessage(scoreDTOs);
        return respObj;
    }

    /**
     * 根据考场加载考试成绩
     *
     * @param examId
     * @param roomId
     * @param subject
     * @return
     */
    @SessionNeedless
    @RequestMapping("/detailByRoom")
    @ResponseBody
    public RespObj detaiByExamRoom(String examId, String roomId, String[] subject) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage(scoreService.loadDetailByExamRoomId(examId, subject, roomId));
        return respObj;
    }


    /**
     * 查询缺免考数据信息
     *
     * @param examId
     * @param classId
     * @param showType
     * @return
     */
    @SessionNeedless
    @RequestMapping("/qmstatus")
    @ResponseBody
    public RespObj loadQMExamStatus(String examId, String classId, String showType, String subject, int page) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        classId = "ALL".equals(classId) ? Constant.EMPTY : classId;
        respObj.setMessage(scoreService.loadQMExamStatus(examId, classId, showType, subject, page));
        return respObj;
    }

    /**
     * 修改缺免考状态
     *
     * @param scoreId
     * @param subjectId
     * @param showType
     * @return
     */
    @SessionNeedless
    @RequestMapping("/updateQMStatus")
    @ResponseBody
    public RespObj updateQMStatus(String scoreId, String subjectId, int showType) {
        scoreService.updateQMStatus(scoreId, subjectId, showType);
        return RespObj.SUCCESS;
    }

    /**
     * 根据班级输出考试成绩模板
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping("/tempByClass")
    public String getTemplateByClassId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            String classId = StringUtils.trim(request.getParameter("classId"));
            String examId = StringUtils.trim(request.getParameter("examId"));
            if (StringUtils.isEmpty(classId) || StringUtils.isEmpty(examId)) {
                return null;
            }

            //创建考试成绩模板
            this.scoreService.createTemplateByClass(util, classId, examId, (List<ExamSubjectDTO>) this.getSubject(examId, classId).getMessage());
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
     * 根据考场输出考试成绩模板
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping("/tempByExamRoom")
    public String getTemplateByExamRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //客户端不缓存
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            String roomId = StringUtils.trim(request.getParameter("roomId"));
            String examId = StringUtils.trim(request.getParameter("examId"));
            if (StringUtils.isEmpty(roomId) || StringUtils.isEmpty(examId)) {
                return null;
            }
            this.scoreService.createTemplateByExamRoom(util,roomId, examId, (List<ExamSubjectDTO>) this.getSubject(examId, null).getMessage());
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
     * 根据年级输出考试成绩模板
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping("/tempByGrade")
    public String getTemplateByGrade(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            String examId = StringUtils.trim(request.getParameter("examId"));
            if (StringUtils.isEmpty(examId)) {
                return null;
            }

            //创建考试成绩模板
            this.scoreService.createTemplateByGrade(util, examId, (List<ExamSubjectDTO>) this.getSubject(examId, null).getMessage());
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
        return null;
    }

    private class ImportScore implements ImportExcelUtil.ISaveData {
        private String examId;

        public ImportScore(String examId) {
            this.examId = examId;
        }

        @Override
        public void save(List data) throws ImportException {
            scoreService.updateByImport(data, examId, UserRole.isHeadmaster(getSessionValue().getUserRole())
                    || UserRole.isManager(getSessionValue().getUserRole()));
        }
    }

    /**
     * 导入考试信息
     */
    @SessionNeedless
    @RequestMapping("/import")
    public void importScore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        response.setContentType("text/html;charset=UTF-8");
        if (StringUtils.isEmpty(request.getParameter("examId"))) {
            respObj.setMessage("考试信息错误");
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
            return;
        }
        final ImportExcelUtil scoreImport = new ImportExcelUtil(0, 0, new ImportExcelUtil.IConvertRow() {
            private List<String> titleId = null;

            @Override
            public Object convert(List<String> rowData, List<String> titles) {
                //首次转换得到导入的科目信息
                if (titleId == null) {
                    titleId = new ArrayList<String>(titles.size());
                    for (String t : titles) {
                        if (t.indexOf('{') > Constant.NEGATIVE_ONE && t.indexOf('}') > Constant.NEGATIVE_ONE) {
                            titleId.add(t.substring(t.lastIndexOf('{') + 1, t.lastIndexOf('}')));
                        } else {
                            titleId.add(null);
                        }
                    }
                }
                ScoreDTO scoreDTO = new ScoreDTO();
                scoreDTO.setId(rowData.get(0));
                //成绩个数为去除公共列之后的其他列
                Map<String, SubjectScoreDTO> mapScore = new HashMap<String, SubjectScoreDTO>(titles.size() - Constant.FOUR);
                if (titles.size() > Constant.FOUR) {
                    SubjectScoreDTO subjectScore;
                    for (int i = Constant.FOUR; i < titleId.size(); i++) {
                        //获取考试成绩
                        subjectScore = new SubjectScoreDTO();
                        subjectScore.setSubjectId(titleId.get(i));
                        subjectScore.setScore(rowData.get(i) == null || StringUtils.isEmpty(rowData.get(i)) ?
                                "0" : rowData.get(i));
                        if (titleId.get(i) != null)
                            mapScore.put(titleId.get(i), subjectScore);
                    }
                }
                scoreDTO.setExamScore(mapScore);
                return scoreDTO;
            }
        }, new ImportScore(request.getParameter("examId")));
        try {
            scoreImport.importData(request, getSessionValue().getId(), "scoreData");
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

    /**
     * 导出考试成绩
     */
    @SessionNeedless
    @RequestMapping("/exportByClass")
    public String exportScore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            String[] classIds = request.getParameterValues("classId");
            String[] subjects = request.getParameterValues("subject");
            String examId = StringUtils.trim(request.getParameter("examId"));
            String gradeId = StringUtils.trim(request.getParameter("gradeId"));
            String orderBy = StringUtils.trim(request.getParameter("orderBy"));
            if (classIds == null) {
                List<IdValuePair> idValuePairs = (List<IdValuePair>) this.getClass(gradeId, examId).getMessage();
                List<String> clazz = new ArrayList<String>(idValuePairs.size());
                for (IdValuePair valuePair : idValuePairs) {
                    clazz.add(valuePair.getId().toString());
                }
                classIds = (String[]) clazz.toArray();
            }
            this.scoreService.exportScoreByClass(util, examId, classIds, subjects, orderBy);
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
     * 导出考试成绩
     */
    @SessionNeedless
    @RequestMapping("/exportByExid")
    public String exportScoreByRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String examId = StringUtils.trim(request.getParameter("examId"));
        List<ScoreEntry> scoreList = regionalExamService.loadExam(examId);
        if(scoreList == null || scoreList.size() == 0){
        	return null;
        	//TODO
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            List<String> datas = new ArrayList<String>();
            
            datas.add("校名次");
            datas.add("区域名次");
			datas.add("姓名");
			datas.add("班级");
			datas.add("总分");
			
			List<SubjectScoreEntry> subjectList = scoreList.get(0).getExamScore();
			
			 for(int i=0;i<subjectList.size();i++){
	            	SubjectScoreEntry subject = subjectList.get(i);
 	            	datas.add(subject.getSubjectName().toString());
	         }
			
			util.addTitle(datas.toArray());
			datas.clear();
			
            for(ScoreEntry se : scoreList){
            	double a=se.getScoreSum();
            	datas.add(se.getSchoolRanking() + "");
            	datas.add(se.getAeraRanking() + "");
            	datas.add(se.getStudentName());
            	datas.add(se.getClassName());
            	datas.add(String.format("%.2f", a) + "");
            	for(SubjectScoreEntry sse : se.getExamScore()){
            		double b=sse.getScore();
            		datas.add(String.format("%.2f", b) + "");
            	}
            	util.appendRow(datas.toArray());
     			datas.clear();
            }
            		
            util.setFileName(String.format("%s_%s", sdf.format(new Date()), "_区域联考成绩表.xlsx"));
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
        }
        return fileName;
    }

    /**
     * 根据年级获取班级信息
     *
     * @param gradeId
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getClass")
    @ResponseBody
    public RespObj getClass(final String gradeId, final String examId) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<IdValuePairDTO> classes = null;
        if (gradeId != null) {
            boolean isAdmin = UserRole.isHeadmaster(getSessionValue().getUserRole())
                    || UserRole.isManager(getSessionValue().getUserRole());
            boolean isTeacher = UserRole.isTeacher(getSessionValue().getUserRole());
            //获取年级中的所有班级
            List<ClassInfoDTO> classInfoDTOs = classService.findClassByGradeId(gradeId);
            if (isAdmin) {
                //如果是管理员，直接获取该年级的所有班级信息
                classes = new ArrayList<IdValuePairDTO>(classInfoDTOs.size());
                for (ClassInfoDTO classInfoDTO : classInfoDTOs) {
                    classes.add(new IdValuePairDTO(new ObjectId(classInfoDTO.getId()), classInfoDTO.getClassName()));
                }
            } else if (isTeacher) {
                //如果是教师，获取教师的所有本年级的班级信息
                List<ObjectId> clazzList = new ArrayList<ObjectId>(classInfoDTOs.size());
                for (ClassInfoDTO dto : classInfoDTOs) {
                    clazzList.add(new ObjectId(dto.getId()));
                }
                //获取当前教师在本年级任教的所有班级
                List<TeacherClassSubjectDTO> teacherClassSubjectDTOs = teacherClassSubjectService.getTeacherClassSubjectDTOList(new ObjectId(getSessionValue().getId()), clazzList);

                //此集合用户过滤重复的班级信息
                List<ObjectId> existClass = new ArrayList<ObjectId>(teacherClassSubjectDTOs.size());

                //当前考试信息中该教师的所有班级信息
                classes = new ArrayList<IdValuePairDTO>(teacherClassSubjectDTOs.size());
                for (TeacherClassSubjectDTO dto : teacherClassSubjectDTOs) {
                    if (dto.getClassId() == null || existClass.contains(dto.getClassId())) {
                        continue;
                    }
                    existClass.add(dto.getClassId());
                    classes.add(dto.getClassInfo());
                }
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(2);
        data.put("clazz", classes == null ? Collections.EMPTY_LIST : classes);
        if (classes != null && !classes.isEmpty()) {
            data.put("subject", this.getSubject(examId, classes.get(0).getIdStr()).getMessage());
        } else {
            data.put("subject", Collections.EMPTY_LIST);
        }
        respObj.setMessage(data);
        return respObj;
    }

    /**
     * 根据年级获取班级信息
     *
     * @param classId
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getSubject")
    @ResponseBody
    public RespObj getSubject(final String examId, final String classId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        if (examId == null) {
            respObj.setMessage("参数错误");
        }
        ExamDTO examDTO = examService.loadExam(examId);
        List<ExamSubjectDTO> subjects = examDTO.getExamSubjectDTO();

        boolean isAdmin = UserRole.isHeadmaster(getSessionValue().getUserRole())
                || UserRole.isManager(getSessionValue().getUserRole());
        boolean isTeacher = UserRole.isTeacher(getSessionValue().getUserRole());
        if (isTeacher && !isAdmin) {
            List<ObjectId> listClass;
            if (!StringUtils.isEmpty(classId)) {
                listClass = new ArrayList<ObjectId>(1);
                listClass.add(new ObjectId(classId));
            } else {
                List<ClassInfoDTO> classInfoDTOs = classService.findClassByGradeId(examDTO.getGradeId());
                listClass = new ArrayList<ObjectId>(classInfoDTOs.size());
                for (ClassInfoDTO one : classInfoDTOs) {
                    listClass.add(new ObjectId(one.getId()));
                }
            }
            //获取当前教师在本班级任教的课程
            List<TeacherClassSubjectDTO> teacherClassSubjectDTOs =
                    teacherClassSubjectService.getTeacherClassSubjectDTOList(
                            new ObjectId(getSessionValue().getId()), listClass);
            List<ExamSubjectDTO> teacherExamSub = new ArrayList<ExamSubjectDTO>(teacherClassSubjectDTOs.size());//教师当前班级的考试课程
            subjects = examService.loadExam(examId).getExamSubjectDTO();

            List<String> addedSub = new ArrayList<String>(teacherClassSubjectDTOs.size());
            //获取参加考试的课程
            for (TeacherClassSubjectDTO teacherClassSubjectDTO : teacherClassSubjectDTOs) {
                for (ExamSubjectDTO examSubjectDTO : subjects) {
                    //该课程为添加过，并且属于该考试的课程
                    if (!addedSub.contains(examSubjectDTO.getSubjectId()) &&
                            examSubjectDTO.getSubjectId().equals(teacherClassSubjectDTO.getSubjectInfo().getIdStr())) {
                        addedSub.add(examSubjectDTO.getSubjectId());
                        teacherExamSub.add(examSubjectDTO);
                    }
                }
            }
            subjects = teacherExamSub;
        }
        respObj.setCode(Constant.SUCCESS_CODE);
        respObj.setMessage(subjects == null ? Collections.EMPTY_LIST : subjects);
        return respObj;
    }


    /**
     * 转向考场成绩导入
     *
     * @param examId
     * @return
     */
    @SessionNeedless
    @RequestMapping("/inputByExamRoom")
    @ResponseBody
    public RespObj inputByExamRoom(final String examId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        Map<String, Object> retData = new HashMap<String, Object>(2);
        ExamDTO examDTO = examService.loadExam(examId);
        retData.put("subject", this.getSubject(examId, null).getMessage());
        retData.put("room", examDTO.getRoomUsed());
        respObj.setMessage(retData);
        respObj.setCode(Constant.SUCCESS_CODE);
        return respObj;
    }
    
    /**
     * 根据校名词或班级名称排序查看
     * Created by Zoukai on 2015/11
     * @param schoolRanking "sr" 学校排名
     * @param aeraRanking "ar" 区域排名
     */
    @SessionNeedless
    @RequestMapping("/findByRanking")
    @ResponseBody
    public String findByRaking( String exid,
							   int skip,
							   int size){
    	List<ScoreEntry> srcList=scoreService.findByRanking(new ObjectId(exid),skip,size);
    	List<Map<String, Object>> formatList = formatItemForListView(srcList);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", formatList);
		return JSON.toJSONString(resultMap);
    }
    
    /**
     * 根据考试编码查询考试信息
     * @param exid "exId"
     */
    @SessionNeedless
    @RequestMapping("/findByExid")
    @ResponseBody
    public Map<String, Object> findByExid(String exid,
    						 int skip,
			   				 int size){
    	List<ScoreEntry> srcList=scoreService.findByExid(new ObjectId(exid),skip,size);
    	List<Map<String, Object>> formatList = formatItemForListView(srcList);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", formatList);
		return resultMap;

    }
    
   
	/**
	 * 数据组装
	 * @param srcList
	 * @return
	 */
	private List<Map<String, Object>> formatItemForListView(
			List<ScoreEntry> srcList) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (ScoreEntry ce : srcList) {
			double score=ce.getScoreSum();
			List<SubjectScoreEntry> list=ce.getExamScore();
			Map<String, Object> m = new HashMap<String, Object>();
			List<String> sList=new ArrayList<String>();
			for(int i=0;i<list.size();i++){
				double subScore=list.get(i).getScore();
				String reScore=String.format("%.2f", subScore);
				sList.add(reScore);
			}
			
			m.put("exId", ce.getExamId());
			m.put("stuNm", ce.getStudentName());
			m.put("sList", sList);
			m.put("cna", ce.getClassName());
			m.put("enm", ce.getExamNum());
			m.put("rna", ce.getExamRoomName());
			m.put("suc", String.format("%.2f", score));
			m.put("ern", ce.getExamRoomNumber());
			m.put("aid", ce.getAreaExamId());
			m.put("sr", ce.getSchoolRanking());
			m.put("ar", ce.getAeraRanking());
			resultList.add(m);
		}
		return resultList;
	}
	
	@SessionNeedless
	@RequestMapping("/chakan")
	@ResponseBody
	public ModelAndView showList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("exam/schoolExamArea");

	
		return mv;
	}
	
	@SessionNeedless
	@RequestMapping("/rankList")
	@ResponseBody
	public ModelAndView rankingList(String exId,int skip,int size){
		ExamEntry ee = examService.findExamEntry(exId);
		if(ee == null){
			
		}
		RegionalExamEntry ree = regionalExamService.loadRegionalExamEntry(ee.getRegionalExamId());
		List<RegionalSubjectItem> subList = ree.getExamSubject();
		List<String> rel=new ArrayList<String>();
		List<Integer> fenshu=new ArrayList<Integer>();
		int zong = 0;
		for(int i=0;i<subList.size();i++){
			String name=subList.get(i).getName();
			int fens=(int) subList.get(i).getFull();
			rel.add(name);
			fenshu.add(fens);
			zong +=fenshu.get(i);
		}

		int total=scoreService.countExamStudent(exId);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("exam/scoreRank");
		mv.addObject("exId", exId);
		mv.addObject("total",total);
		mv.addObject("kemu",rel);
		mv.addObject("fenshu",fenshu);
		mv.addObject("zongfen",zong);
		return mv;
	}
}
