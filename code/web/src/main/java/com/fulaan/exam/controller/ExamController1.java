package com.fulaan.exam.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.base.controller.BaseController;
import com.fulaan.exam.service.ExamRoomService;
import com.fulaan.exam.service.ExamService;
import com.fulaan.exam.service.ScoreService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.utils.ExportUtil;
import com.pojo.app.PageDTO;
import com.pojo.exam.ExamDTO;
import com.pojo.exam.ExamRoomDTO;
import com.pojo.exam.ExamRoomEntry;
import com.pojo.exam.ExamSubjectDTO;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.TeacherClassSubjectDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;

/**
 * Created by Caocui on 2015/7/22.
 * 考试信息控制器
 */
@Controller
@RequestMapping("/exam1")
public class ExamController1 extends BaseController {
	 @Autowired
	    private ExamService examService;
	    @Autowired
	    private SchoolService schoolService;
	    @Autowired
	    private ScoreService scoreService;
	    @Autowired
	    private TeacherClassSubjectService teacherClassSubjectService;
	    @Autowired
	    private ExamRoomService examRoomService;
	    @Autowired
	    private ClassService classService;
	    //考试管理页面
	    private static final String PAGE_EXAM_MANAGE_LIST = "exam/manage";

	    /**
	     * 转向考试管理页面
	     *
	     * @return
	     */
	    @RequestMapping("/list")
	    public ModelAndView list() {
	        ModelAndView view = new ModelAndView();
	        //获取学校年级信息
	        List<GradeView> grades = this.schoolService.findGradeList(getSessionValue().getSchoolId());
	        view.addObject("grades", grades);

	        String gradeId = grades.isEmpty() ? null : grades.get(0).getId();
	        view.addObject("gradeId", gradeId);
	        view.addObject("gradeName", grades.isEmpty() ? null : grades.get(0).getName());
	        //获取年级科目信息
	        view.addObject("subjects", this.schoolService.
	                findSubjectListBySchoolIdAndGradeId(getSessionValue().getSchoolId(), gradeId));
	        view.addObject("currDate", DateTimeUtils.getCurrDate());
	        view.addObject("sessionValue", getSessionValue());
//			List<ClassEntry> classEntries = classService.findClassEntryByMasterId(getUserId());
//			if(classEntries.size() > 0){
				view.addObject("isClassMaster", true);
//			} else {
//				view.addObject("isClassMaster", false);
//			}
	        view.setViewName(PAGE_EXAM_MANAGE_LIST);
	        return view;
	    }

	    /**
	     * 获取考试列表
	     *
	     * @param gradeId
	     * @return
	     */
	    @RequestMapping("/loadExam")
	    @ResponseBody
	    public RespObj loadExam(String gradeId, int page) {
	        RespObj result = new RespObj(Constant.FAILD_CODE);
	        //获取考试列表信息
	        PageDTO<ExamDTO> pageData = examService.findExamByGradeId(gradeId, page);
	        //如果是教师获取当前登录人的可视课程信息,设置课程是否可以录入成绩
	        boolean isTeacher = UserRole.isTeacher(getSessionValue().getUserRole());
	        if (isTeacher) {
	            List<ClassInfoDTO> classInfoDTOs = classService.findClassByGradeId(gradeId);
				if(null != classInfoDTOs) {
					//如果是教师，获取教师的所有本年级的班级信息
					List<ObjectId> clazzList = new ArrayList<ObjectId>(classInfoDTOs.size());
					for (ClassInfoDTO dto : classInfoDTOs) {
						clazzList.add(new ObjectId(dto.getId()));
					}
					//获取当前教师在本年级任教的所有班级
					List<TeacherClassSubjectDTO> teacherClassSubjectDTOs = teacherClassSubjectService.getTeacherClassSubjectDTOList(new ObjectId(getSessionValue().getId()), clazzList);
					//获取教师的所有本年级的课程信息
					Set<String> subjects = new HashSet<String>(teacherClassSubjectDTOs.size());
					for (TeacherClassSubjectDTO dto : teacherClassSubjectDTOs) {
						subjects.add(dto.getSubjectInfo().getIdStr());
					}
					for (ExamDTO examDTO : pageData.getList()) {
						examDTO.setOpenFlag(subjects);
					}
				}
	        }
	        result.setMessage(pageData);
	        result.setCode(Constant.SUCCESS_CODE);
	        return result;
	    }

	    /**
	     * 获取年级科目信息
	     *
	     * @param gradeId 年级编码
	     * @return
	     */
	    @RequestMapping("/gradeSubject")
	    @ResponseBody
	    public RespObj gradeSubject(final String gradeId) {
	        RespObj result = new RespObj(Constant.FAILD_CODE);
	        //获取年级科目信息
	        result.setMessage(this.schoolService.
	                findSubjectListBySchoolIdAndGradeId(getSessionValue().getSchoolId(), gradeId));
	        result.setCode(Constant.SUCCESS_CODE);
	        return result;
	    }

	    /**
	     * 保存考试信息
	     *
	     * @return
	     */
	    @RequestMapping("/save")
	    @ResponseBody
	    public RespObj save(ExamDTO examDTO, HttpServletRequest request) {
	        //修改考试信息
	        examDTO.setExamSubjectDTO(fetchExamSubject(request));
	        List<ClassInfoDTO> classInfoDTOs = classService.findClassByGradeId(examDTO.getGradeId());
			if(classInfoDTOs.size() <= 0){
				RespObj respObj = new RespObj(Constant.FAILD_CODE, "该年级下没有班级");
				return respObj;
			}
	        List<String> strings = new ArrayList<String>(classInfoDTOs.size());
	        for (ClassInfoDTO dto : classInfoDTOs) {
	            strings.add(dto.getId());
	        }
	        examDTO.setClassList(strings);
	        examDTO.setSchoolId(getSessionValue().getSchoolId());
			try {
				examService.updateNew(examDTO);
			} catch (Exception e) {
				e.printStackTrace();
				RespObj respObj = new RespObj(Constant.FAILD_CODE, e.getMessage());
				return respObj;
			}
			return RespObj.SUCCESS;
	    }

	    /**
	     * 获取请求中的考试科目
	     *
	     * @param request
	     * @return
	     */
	    private List<ExamSubjectDTO> fetchExamSubject(HttpServletRequest request) {
	        List<ExamSubjectDTO> dtos = new ArrayList<ExamSubjectDTO>();
	        String[] ids = request.getParameterValues("examSubjectDTOs.id");
	        String[] subjectIds = request.getParameterValues("examSubjectDTOs.subjectId");
	        String[] subjectNames = request.getParameterValues("examSubjectDTOs.subjectName");
	        String[] fullMarks = request.getParameterValues("examSubjectDTOs.fullMarks");
	        String[] youXiuScores = request.getParameterValues("examSubjectDTOs.youXiuScore");
	        String[] failScores = request.getParameterValues("examSubjectDTOs.failScore");
			String[] diFenScores = request.getParameterValues("examSubjectDTOs.diFenScore");
	        String[] examDates = request.getParameterValues("examSubjectDTOs.examDate");
	        String[] times = request.getParameterValues("examSubjectDTOs.time");
	        ExamSubjectDTO subjectDTO;
	        if (ids != null && ids.length != 0) {
	            for (int i = 0, len = ids.length; i < len; i++) {
	                subjectDTO = new ExamSubjectDTO();
	                subjectDTO.setId(ids[i]);
	                subjectDTO.setSubjectName(subjectNames[i]);
	                subjectDTO.setSubjectId(subjectIds[i]);
	                subjectDTO.setFullMarks(StringUtils.isEmpty(fullMarks[i]) ? 100 : Integer.parseInt(fullMarks[i]));
	                subjectDTO.setYouXiuScore(StringUtils.isEmpty(youXiuScores[i]) ? 90 : Integer.parseInt(youXiuScores[i]));
	                subjectDTO.setFailScore(StringUtils.isEmpty(failScores[i]) ? 60 : Integer.parseInt(failScores[i]));
	                subjectDTO.setDiFenScore(StringUtils.isEmpty(diFenScores[i]) ? 30 : Integer.parseInt(diFenScores[i]));
	                subjectDTO.setExamDate(examDates[i]);
	                subjectDTO.setTime(times[i]);
	                dtos.add(subjectDTO);
	            }
	        }
	        return dtos;
	    }

	    /**
	     * 删除考试信息
	     *
	     * @return
	     */
	    @RequestMapping("/delete")
	    @ResponseBody
	    public RespObj delete(final String examId) {
	        examService.delete(examId);
	        return RespObj.SUCCESS;
	    }

	    /**
	     * 更新考试科目的各成绩录入开关
	     *
	     * @param subject 考试科目信息
	     * @return
	     */
	    @RequestMapping("/updateOpenStatus")
	    @ResponseBody
	    public RespObj updateExamSubjectOpenStatus(ExamSubjectDTO subject) {
	        examService.updateExamSubjectOpenStatus(subject);
	        return RespObj.SUCCESS;
	    }


	    /**
	     * 锁定排考场功能
	     *
	     * @param examId
	     * @return
	     */
	    @RequestMapping("/lockExam")
	    @ResponseBody
	    public RespObj lockExam(final String examId, final int isLock) {
	        examService.lockArrange(examId, isLock);
	        return RespObj.SUCCESS;
	    }

	    /**
	     * 对考试分配考场信息
	     *
	     * @param examId
	     * @param examroom
	     * @return
	     */
	    @RequestMapping("/arrange")
	    @ResponseBody
	    public RespObj arrangeExamRoom(String examId, String[] examroom, int type) {
	        RespObj respObj = new RespObj(Constant.FAILD_CODE);
	        if (StringUtils.isEmpty(examId) || examroom == null || examroom.length == 0) {
	            respObj.setMessage("参数错误");
	            return respObj;
	        }

	        List<String> classId = examService.loadExam(examId).getClassList();
	        Map<String, List<ObjectId>> classStudent = new HashMap<String, List<ObjectId>>(classId.size());
	        ClassEntry classEntry;
	        for (int i = 0, len = classId.size(); i < len; i++) {
	            classEntry = classService.getClassEntryById(new ObjectId(classId.get(i)), Constant.FIELDS);
	            if (classEntry == null) {
	                respObj.setMessage("参加考试的班级不存在");
	                return respObj;
	            }
	            classStudent.put(classId.get(i).toString(), classEntry.getStudents());
	        }
	        List<ExamRoomDTO> roomInfos = new ArrayList<ExamRoomDTO>(examroom.length);
	        ExamRoomEntry entry;
	        for (int i = 0, len = examroom.length; i < len; i++) {
	            entry = examRoomService.getExamRoomEntry(new ObjectId(examroom[i]));
	            if (entry == null) {
	                respObj.setMessage("选中的考场不存在");
	                return respObj;
	            }
	            roomInfos.add(new ExamRoomDTO(entry));
	        }

			try {
				respObj.setMessage(examService.arrangeExamRoom(examId, classStudent, roomInfos, type));
				respObj.setCode(Constant.SUCCESS_CODE);
			} catch (Exception e){
				respObj.setMessage(e.getMessage());
			} finally {
				return respObj;
			}

	    }

	    /**
	     * 获取考试的考场分配信息
	     *
	     * @param examId
	     * @return
	     */
	    @RequestMapping("/arrangeInfo")
	    @ResponseBody
	    public RespObj arrangeInfo(final String examId) {
	        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
	        Map<String, Object> retData = new HashMap<String, Object>(Constant.THREE);
	        //获取考试信息
	        retData.put("examInfo", examService.loadExam(examId));
	        //获取本次考试的学生总记录
	        retData.put("countStu", scoreService.countExamStudent(examId));
	        //获取已分配考场的学生总记录
	        retData.put("countUnArr", scoreService.countStudentNotArranged(examId));
	        //获取本校考场
	        retData.put("examroom", examRoomService.queryExamRoomsBySchoolId(new ObjectId(getSessionValue().getSchoolId())));
	        respObj.setMessage(retData);
	        return respObj;
	    }

	    /**
	     * 获取考试的考场分配信息
	     *
	     * @param examId
	     * @return
	     */
	    @RequestMapping("/arrangeDetail")
	    @ResponseBody
	    public RespObj arrangeInfo(final String examId, final String classId, final String roomId) {
	        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
	        respObj.setMessage(scoreService.loadArrangeInfo(getSchoolId().toString(), examId, classId, roomId));
	        return respObj;
	    }

	    /**
	     * 加载考试的班级和考场信息
	     *
	     * @param examId
	     * @return
	     */
	    @RequestMapping("/loadClassAndExamRoom")
	    @ResponseBody
	    public RespObj loadClassAndExamRoom(final String examId) {
	        RespObj respObj = new RespObj(Constant.FAILD_CODE);
	        if("".equals(examId)){
	        	return respObj;
	        }
	        Map<String, Object> retData = new HashMap<String, Object>(2);
	        ExamDTO examDTO = examService.loadExam(examId);
	        retData.put("class", classService.findClassByGradeId(examDTO.getGradeId()));
//	        retData.put("room", examRoomService.queryExamRoomsBySchoolId(new ObjectId(getSessionValue().getSchoolId())));
	        retData.put("room", examRoomService.queryExamRoomsByExamId(new ObjectId(examId)));
	        respObj.setMessage(retData);
	        respObj.setCode(Constant.SUCCESS_CODE);
	        return respObj;
	    }

	    /**
	     * 导出考场分配信息
	     *
	     * @return
	     */
	    @RequestMapping("/exportArrangeInfo")
	    public String exportArrangeInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        response.setContentType("application/octet-stream;charset=UTF-8");
	        response.addHeader("Pargam", "no-cache");
	        response.addHeader("Cache-Control", "no-cache");
	        ExportUtil util = null;
	        try {
	            util = new ExportUtil();
	            String classId = StringUtils.trim(request.getParameter("classId"));
	            String examId = StringUtils.trim(request.getParameter("examId"));
	            String roomId = StringUtils.trim(request.getParameter("roomId"));
	            if (StringUtils.isEmpty(examId)) {
	                return null;
	            }

	            //创建考试成绩模板
	            this.examService.exportArrangeInfo(getSchoolId().toString(), util, examId, classId, roomId);
	            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
	            util.getBook().write(response.getOutputStream());
	        }catch (Exception e){
				e.printStackTrace();
			} finally {
	            if (util != null) {
	                util.destroy();
	            }
	        }
	        return null;
	    }

	/**
	 * 按标签导出考试安排信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/exportArrangeInfo1")
	public String exportArrangeInfo1(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			String classId = StringUtils.trim(request.getParameter("classId"));
			String examId = StringUtils.trim(request.getParameter("examId"));
			String roomId = StringUtils.trim(request.getParameter("roomId"));
			if (StringUtils.isEmpty(examId)) {
				return null;
			}

			//创建考试成绩模板
			this.examService.exportArrangeInfo1(getSchoolId().toString(), examId, classId, roomId, response);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
	        String agent = request.getHeader("User-Agent");
	        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
	            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
	        } else {
	            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
	        }
	        return fileName;
	    }
}
