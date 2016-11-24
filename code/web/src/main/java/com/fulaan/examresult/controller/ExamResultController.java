package com.fulaan.examresult.controller;


import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.examresult.service.ExamScoreExcelService;
import com.fulaan.examresult.service.PerformanceService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.CollectionUtil;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.NameValuePair;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.examregional.ScoreSection;
import com.pojo.examresult.*;
import com.pojo.school.*;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by fl on 2015/6/15.
 */
@Controller
@RequestMapping("/score")
public class ExamResultController extends BaseController {
    @Autowired
    ExamResultService examResultService ;
    @Autowired
    ClassService classService;
    @Autowired
    TeacherClassSubjectService teacherClassSubjectService;
    @Autowired
    SchoolService schoolService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    UserService userService;
    @Autowired
    ExamScoreExcelService examScoreExcelService;
    @Autowired
    EducationBureauService educationBureauService;
    @Autowired
    private ExperienceService experienceService;

//   //for test
//    ExamResultService examResultService = new ExamResultService();
//    ClassService classService = new ClassService();
//    TeacherClassSubjectService teacherClassSubjectService = new TeacherClassSubjectService();
//    SchoolService schoolService = new SchoolService();
//    PerformanceService performanceService = new PerformanceService();


    @RequestMapping("/teacher")
    public String teachermainpage(@RequestParam(required=false,defaultValue="1")int a) {
        if(a==10000)
            return "examresult/teachermainc";
        return "examresult/teachermain";
    }

    @RequestMapping("/teacher/input")
    public String teacherinputpage(@RequestParam(required=false,defaultValue="1")int a) {
        if (a==10000)
            return "examresult/teacherinputc";
        return "examresult/teacherinput";
    }

    @RequestMapping("/teacher/semester")
    public String teachersemesterpage(@RequestParam(required=false,defaultValue="1")int a) {
        if (a==10000)
            return "examresult/teachersemesterc";
        return "examresult/teachersemester";
    }

    @RequestMapping("/teacher/editscore")
    public String teachereditscorepage(String examId,String examName, @RequestParam(required=false,defaultValue="1")int a,Map<String, Object> model) {
        model.put("examId", examId);
        model.put("examName", examName);
        if (a==10000)
            return "examresult/teachereditscorec";
        return "examresult/teachereditscore";
    }


    @RequestMapping("/student")
    public String studentmainpage(@RequestParam(required=false,defaultValue="1")int a) {
        if (a==10000)
            return "examresult/studentmainc";
        return "examresult/studentmain";
    }

    @RequestMapping("/student/semester")
    public String studentsemesterpage(@RequestParam(required=false,defaultValue="1")int a) {
        if (a==10000)
            return "examresult/studentsemesterc";
        return "examresult/studentsemester";
    }

    @RequestMapping("/student/history")
    public String studenthistorypage(@RequestParam(required=false,defaultValue="1")int a) {
        if (a==10000)
            return "examresult/studenthistoryc";
        return "examresult/studenthistory";
    }


    @RequestMapping("/manager")
    public String managermainpage() {
        return "examresult/managermain";
    }

    @RequestMapping("/manager/input")
    public String managerinputpage() {
        return "examresult/managerinput";
    }

    @RequestMapping("/header")
    public String headerpage(@RequestParam(required = false,defaultValue = "1")int a){
        if(a==10000){
            return "examresult/headerpagec";
        }
        return "examresult/headerpage";
    }

    @RequestMapping("/header/semester")
    public String headersemester(@RequestParam(required = false,defaultValue = "1")int a){
        if(a==10000){
            return "examresult/headersemesterc";
        }
        return "examresult/headersemester";
    }

    @RequestMapping("/educationBureau")
    public String educationBureau(@RequestParam(required=false,defaultValue="1")int a){
        if(a==10000)
           return "examresult/educationBureauc";
        return "examresult/educationBureau";
    }

    @RequestMapping("/jointExam")
    public String jointExam(){return "examresult/jointexam";}






//==========================================================老师首页===============================================================================
    /**
     * 供查询的信息
     * @return
     */
    @RequestMapping("/getExamSelection")
    @ResponseBody
    public Map<String, Object> getExamSelection() {
        ObjectId userId = getUserId();
        Map<String, Object> model = new HashMap<String, Object>();
        String schoolId = getSessionValue().getSchoolId();
        SchoolEntry schoolEntry = schoolService.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        //通过老师的id查询所教班级和科目//不包括兴趣班
        List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoByTeacherId(userId);
        for(ClassInfoDTO classInfoDTO : classInfoDTOList) {
            String classId = classInfoDTO.getId();
            classIds.add(new ObjectId(classId));
        }
        List<TeacherClassSubjectDTO> teacherClassSubjectDTOList = teacherClassSubjectService.getTeacherClassSubjectDTOList(userId,classIds);
        for(TeacherClassSubjectDTO tcsDTO : teacherClassSubjectDTOList) {
            Map<String, Object> info = new HashMap<String, Object>();
            IdValuePairDTO classInfo = tcsDTO.getClassInfo();
            ObjectId classId = classInfo.getId();
            String className = (String)classInfo.getValue();
            String gradeName="";
            IdValuePairDTO subjectInfo = tcsDTO.getSubjectInfo();
            ObjectId subjectId = subjectInfo.getId();
            String subjectName = (String)subjectInfo.getValue();
            info.put("classId", classId.toString());
            info.put("className", className);
            info.put("subjectId", subjectId.toString());
            info.put("subjectName",subjectName);
            ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
            ObjectId gradeId = classEntry.getGradeId();
            info.put("gradeId", gradeId.toString());
            List<Grade> gradeList = schoolEntry.getGradeList();
            for(Grade grade : gradeList) {
                if(grade.getGradeId().equals(gradeId)) {
                    gradeName = grade.getName();
                    info.put("gradeName", gradeName);
                }
            }
            List<ExamResultEntry> examResultEntryList = examResultService.getExamList(classId, subjectId, null);
            List<ExamInfoDTO> examIdNameDTOList = new ArrayList<ExamInfoDTO>();
            if(examResultEntryList != null) {  //如果存在考试
                for (ExamResultEntry examResultEntry : examResultEntryList) {
                    ExamInfoDTO examIdNameDTO = new ExamInfoDTO(examResultEntry.getID().toString(), examResultEntry.getName(), examResultEntry.getSchoolYear());
                    examIdNameDTO.setIsGrade(examResultEntry.getIsGradeExam());
                    examIdNameDTO.setClassSize(examResultEntry.getClassList().size());
                    examIdNameDTOList.add(examIdNameDTO);
                }
            }
            info.put("examList", examIdNameDTOList);
            model.put(className + "(" + subjectName + ")", info);
        }
        model.put("code", 200);
        return model;
    }

    /**
     * 取得满分,用于画图时确定最大值
     * @return
     */
    @RequestMapping("/getFullScore")
    @ResponseBody
    public Map<String, Object> getFullScore(String examId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId subId = new ObjectId(subjectId);
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        if (performanceEntryList != null) {
            PerformanceEntry performanceEntry = performanceEntryList.get(0);
            List<Score> scoreList = performanceEntry.getScoreList();
            for (Score score : scoreList) {
                if (score.getSubjectId().equals(subId)) {
                    model.put("fullScore", score.getFullScore());
                }
            }
        }
        model.put("code", 200);
        return model;
    }
    /**
     * 考试班级成绩对比
     * @param examId
     * @param subjectId
     * @return
     */

    @RequestMapping("/getClassScoreCompare")
    @ResponseBody
    public Map<String, Object> getClassScoreCompare(String examId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<String> classList = new ArrayList<String>();
        List<Double> classScore = new ArrayList<Double>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        List<ObjectId> classIds = examResultService.getExamResultEntry(new ObjectId(examId)).getClassList();
        for (ObjectId class_id : classIds) {
            Double averageScore = examResultService.getAveragePerformance(performanceEntryList, new ObjectId(subjectId), class_id);
            ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(class_id.toString());
            classList.add(classInfoDTO.getClassName());
            classScore.add(Math.round(averageScore*10)/10.0);
        }
        model.put("classList", classList);
        model.put("classScore", classScore);
        model.put("code", 200);
        return model;
    }

    /**
     * 考试成绩分布
     * @param examId
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getClassScoreDistribution")
    @ResponseBody
    public Map<String, Object> getClassScoreDistribution(String examId, String classId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        List<String> category = new ArrayList<String>();
        List<Integer> countList = new ArrayList<Integer>();
        if(performanceEntryList!=null && performanceEntryList.size()>0) {
            List<ScoreDistributionDTO> scoreDistributionDTOList = examResultService.getScoreDistribution(performanceEntryList, new ObjectId(subjectId), new ObjectId(classId));
            for (ScoreDistributionDTO sd : scoreDistributionDTOList) {
                countList.add(sd.getNum() == null ? 0 : sd.getNum());
                category.add(sd.getDistribution());
            }
        }
        model.put("category", category);
        model.put("countList", countList);
        model.put("code", 200);
        return model;
    }

    /**
     * 考试班级均分、年级均分
     * @param examId
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getClassScoreAverage")
    @ResponseBody
    public Map<String, Object> getClassScoreAverage(String examId, String classId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        Double gradeScore = examResultService.getAveragePerformance(performanceEntryList, new ObjectId(subjectId), null);
        model.put("gradeScore", Math.round(gradeScore*10)/10.0);
        Double classScore = examResultService.getAveragePerformance(performanceEntryList, new ObjectId(subjectId), new ObjectId(classId));
        model.put("classScore", Math.round(classScore*10)/10.0);
        model.put("code", 200);
        return model;
    }

    /**
     * 考试班级及格率、优秀率
     * @param examId
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getClassScorePercent")
    @ResponseBody
    public Map<String, Object> getClassScorePercent(String examId, String classId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        if(performanceEntryList!=null && performanceEntryList.size()>0) {
            Integer cer = examResultService.getExcellentRate(performanceEntryList, new ObjectId(subjectId), new ObjectId(classId));
            Integer cpr = examResultService.getPassRate(performanceEntryList, new ObjectId(subjectId), new ObjectId(classId));
            model.put("cer", cer);
            model.put("cpr", cpr);
        } else {
            model.put("cer", 0);
            model.put("cpr", 0);
        }
        model.put("code", 200);
        return model;
    }

    /**
     * 考试年级及格率、优秀率
     * @param subjectId
     * @return
     */
    @RequestMapping("/getGradeScorePercent")
    @ResponseBody
    public Map<String, Object> getGradeScorePercent(String examId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        Integer ger = examResultService.getExcellentRate(performanceEntryList, new ObjectId(subjectId), null);
        Integer gpr = examResultService.getPassRate(performanceEntryList, new ObjectId(subjectId), null);
        model.put("ger", ger);
        model.put("gpr", gpr);
        model.put("code", 200);
        return model;
    }

    /**
     * 考试学生成绩列表
     * @param examId
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getClassScoreList")
    @ResponseBody
    public Map<String, Object> getClassScoreList(String examId, String classId, String subjectId, Integer fullScore, Integer hundred) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        List<StuScoreDTO> stuScoreDTOList = examResultService.getClassRankingList(performanceEntryList, new ObjectId(classId), new ObjectId(subjectId),fullScore, hundred);
        model.put("stuScoreDTOList", stuScoreDTOList);
        model.put("code", 200);
        return model;
    }

//=========================================================老师学期成绩==============================================

    /**
     * 得到当前学期
     * @return
     */
    @RequestMapping("/getCurrentTerm")
    @ResponseBody
    public Map<String, Object> getCurrentTerm(){
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("currentTerm", examResultService.getCurrentTerm());
        model.put("code", 200);
        return model;
    }

    /**
     * 学期班级成绩对比
     * @param classId
     * @param subjectId
     * @return
     */
//    @RequestMapping("/getSemesterScoreCompare")
//    @ResponseBody
//    public Map<String, Object> getSemesterScoreCompare(String classId, String subjectId) {
//        Map<String, Object> model = new HashMap<String, Object>();
//        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
//        List<ClassInfoDTO> classInfoDTOList = classService.getGradeClassesInfo(classEntry.getGradeId().toString());
//        List<String> classList = new ArrayList<String>();
//        List<Double> normalScoreList = new ArrayList<Double>();
//        List<Double> midtermScoreList = new ArrayList<Double>();
//        List<Double> endtermScoreList = new ArrayList<Double>();
//        for(ClassInfoDTO classInfoDTO : classInfoDTOList) {
//            String class_id = classInfoDTO.getId();
//            ScoreDTO scoreDTO = examResultService.getClassScoreDTO(new ObjectId(subjectId), new ObjectId(class_id));
//            if(scoreDTO.getUsualScore() == null) {
//                normalScoreList.add(0.0);
//            } else {
//                normalScoreList.add(Math.round(scoreDTO.getUsualScore() * 10) / 10.0);
//            }
//            if(scoreDTO.getMidtermScore() == null) {
//                midtermScoreList.add(0.0);
//            } else {
//                midtermScoreList.add(Math.round(scoreDTO.getMidtermScore()*10)/10.0);
//            }
//            if(scoreDTO.getFinalScore() == null) {
//                endtermScoreList.add(0.0);
//            } else {
//                endtermScoreList.add(Math.round(scoreDTO.getFinalScore()*10)/10.0);
//            }
//            scoreDTO.setClassName(classInfoDTO.getClassName());
//            classList.add(scoreDTO.getClassName());
//            if(class_id.equals(classId)) {
//                model.put("scoreDTO", scoreDTO);
//            }
//        }
//        model.put("classList", classList);
//        model.put("normalScoreList", normalScoreList);
//        model.put("midtermScoreList", midtermScoreList);
//        model.put("endtermScoreList", endtermScoreList);
//        model.put("code", 200);
//        return  model;
//    }

    /**
     * 学期班级成绩对比  新接口
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getSemesterScoreCompare")
    @ResponseBody
    public Map<String, Object> getSemesterScoreCompare(String classId, @ObjectIdType ObjectId subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<String> classList = new ArrayList<String>();
        List<Double> normalScoreList = new ArrayList<Double>();
        List<Double> midtermScoreList = new ArrayList<Double>();
        List<Double> endtermScoreList = new ArrayList<Double>();
        List<ScoreDTO> scoreDTOs = examResultService.getClassScoreDTOByGradeId(subjectId, classEntry.getGradeId());
        for(ScoreDTO scoreDTO : scoreDTOs){
            classList.add(scoreDTO.getClassName());
            normalScoreList.add(scoreDTO.getUsualScore());
            midtermScoreList.add(scoreDTO.getMidtermScore());
            endtermScoreList.add(scoreDTO.getFinalScore());
            if(scoreDTO.getClassName().equals(classEntry.getName())){
                model.put("scoreDTO", scoreDTO);
            }
        }
        model.put("classList", classList);
        model.put("normalScoreList", normalScoreList);
        model.put("midtermScoreList", midtermScoreList);
        model.put("endtermScoreList", endtermScoreList);
        model.put("code", 200);
        return  model;
    }

    /**
     * 学期成绩
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getSemesterScore")
    @ResponseBody
    public Map<String, Object> getSemesterScore(String classId, String subjectId, int order) {
        Map<String, Object> model = new HashMap<String, Object>();
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<ScoreDTO> scoreDTOList = new ArrayList<ScoreDTO>();
        List<ObjectId> studentList = classEntry.getStudents();
        for(ObjectId studentId : studentList) {
            UserDetailInfoDTO user = userService.getUserInfoById(studentId.toString());
            if(user == null){
                continue;
            }
            ScoreDTO scoreDTO = examResultService.getStuScoreDTO(new ObjectId(subjectId), studentId, new ObjectId(classId));
            String name = user.getUserName();
            scoreDTO.setStudentName(name);
            scoreDTOList.add(scoreDTO);
        }
        switch (order) {
            case 1 : Collections.sort(scoreDTOList,new SortByUsualScore());break;
            case 2 : Collections.sort(scoreDTOList,new SortByMidScore());break;
            case 3 : Collections.sort(scoreDTOList,new SortByFinalScore());break;
            default: Collections.sort(scoreDTOList,new SortByFinalScore());break;
        }
        model.put("scoreDTOList", scoreDTOList);

        int totalStu = scoreDTOList.size();
        int avei = 0, avej = 0;
        int midi = 0, midj = 0;
        int endi = 0, endj = 0;
        List<ExamResultEntry>  examResultEntryList = examResultService.getExamList(new ObjectId(classId), new ObjectId(subjectId), null);
        if(examResultEntryList != null) {
            List<Score> scoreList = new ArrayList<Score>();
            List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(examResultEntryList.get(0).getID(), null, null, null);
            if (performanceEntryList != null) {
                PerformanceEntry performanceEntry = performanceEntryList.get(0);
                scoreList = performanceEntry.getScoreList();
            }
            double excellentScore = 90;
            double failScore = 60;
            for(Score score : scoreList) {
                if(score.getSubjectId().equals(new ObjectId(subjectId))) {
                    excellentScore = score.getFullScore()*0.9;
                    failScore = score.getFailScore();
                }
            }
            for(ScoreDTO scoreDTO : scoreDTOList) {
                Double aveScore = scoreDTO.getUsualScore() == null ? 0 : scoreDTO.getUsualScore();
                if (aveScore >= excellentScore) {
                    avei++;
                    avej++;
                } else if (aveScore >= failScore) {
                    avej++;
                }

                Double midScore = scoreDTO.getMidtermScore() == null ? 0 : scoreDTO.getMidtermScore();
                if (midScore >= excellentScore) {
                    midi++;
                    midj++;
                } else if (midScore >= failScore) {
                    midj++;
                }

                Double endScore = scoreDTO.getFinalScore() == null ? 0 : scoreDTO.getFinalScore();
                if (endScore >= excellentScore) {
                    endi++;
                    endj++;
                } else if (endScore >= failScore) {
                    endj++;
                }
            }
        }
        model.put("avecer", Math.round(avei*100.0/totalStu));
        model.put("avecpr", Math.round(avej*100.0/totalStu));
        model.put("midcer", Math.round(midi*100.0/totalStu));
        model.put("midcpr", Math.round(midj*100.0/totalStu));
        model.put("endcer", Math.round(endi*100.0/totalStu));
        model.put("endcpr", Math.round(endj*100.0/totalStu));
        model.put("code", 200);
        return  model;
    }

    /**
     * 学期学生成绩列表
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getSemesterScoreList")
    @ResponseBody
    public Map<String, Object> getSemesterScoreList(String classId, String subjectId, int order) {
        Map<String, Object> model = new HashMap<String, Object>();
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<ScoreDTO> scoreDTOList = new ArrayList<ScoreDTO>();
        List<ObjectId> studentList = classEntry.getStudents();
        for(ObjectId studentId : studentList) {
            ScoreDTO scoreDTO = examResultService.getStuScoreDTO(new ObjectId(subjectId), studentId, new ObjectId(classId));
            String name = userService.getUserInfoById(studentId.toString()).getUserName();
            scoreDTO.setStudentName(name);
            scoreDTOList.add(scoreDTO);
        }
        switch (order) {
            case 1 : Collections.sort(scoreDTOList,new SortByUsualScore());break;
            case 2 : Collections.sort(scoreDTOList,new SortByMidScore());break;
            case 3 : Collections.sort(scoreDTOList,new SortByFinalScore());break;
            default: Collections.sort(scoreDTOList,new SortByFinalScore());break;
        }
        model.put("scoreDTOList", scoreDTOList);
        model.put("code", 200);
        return  model;
    }

    //===========================================老师新建考试========================================================

    //选择列表
    @RequestMapping("/getClassSubjectSelection")
    @ResponseBody
    public Map<String, Object> getClassSubjectSelection(String gradeId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId subId = new ObjectId(subjectId);
        List<IdNameDTO> classList = new ArrayList<IdNameDTO>();
        //通过老师的id查询所教班级和科目//不包括兴趣班
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoByTeacherId(getUserId());
        for(ClassInfoDTO classInfoDTO : CollectionUtil.safeList(classInfoDTOList)) {
            if(classInfoDTO.getGradeId().equals(gradeId)) {
                String classId = classInfoDTO.getId();
                classIds.add(new ObjectId(classId));
            }
        }
        List<TeacherClassSubjectDTO> teacherClassSubjectDTOList = teacherClassSubjectService.getTeacherClassSubjectDTOList(getUserId(),classIds);
        for(TeacherClassSubjectDTO tcs : CollectionUtil.safeList(teacherClassSubjectDTOList)) {
            if(tcs.getSubjectInfo().getId().equals(subId)) {
                classList.add(new IdNameDTO(tcs.getClassInfo()));
            }
        }
        model.put("classList", classList);
        model.put("code", 200);
        return model;
    }


    //创建考试
    @RequestMapping("/createClassExam")
    @ResponseBody
    public boolean createClassExam(String name,String gradeId,String classList,String date, Integer fullScore, Integer failScore, String subjectList) {
        String schoolId = getSessionValue().getSchoolId();
        List<ObjectId> class_List = new ArrayList<ObjectId>();
        List<ObjectId> subject_List = new ArrayList<ObjectId>();
        for(String classId : classList.split(",")) {
            class_List.add(new ObjectId(classId));
        }
        for(String subjectId : subjectList.split(",")) {
            subject_List.add(new ObjectId(subjectId));
        }
        String schoolYear = examResultService.getCurrentTerm();
        ExamResultEntry examResultEntry = new ExamResultEntry(name, "其他", new ObjectId(schoolId), new ObjectId(gradeId), class_List, date, schoolYear, 0, subject_List);
        examResultService.addExam(examResultEntry, subject_List, fullScore, failScore);
        return true;
    }

    /**
     * 已创建考试列表
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getExamResultList")
    @ResponseBody
    public Map<String, Object> getExamResultList(String classId, String subjectId, int order) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<ExamInfoDTO> examList = new ArrayList<ExamInfoDTO>();
        List<ExamResultEntry> examResultEntryList = examResultService.getExamList(new ObjectId(classId), new ObjectId(subjectId), 0);
        Integer totalStu = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS).getStudents().size();
        if(examResultEntryList != null) {
            for (ExamResultEntry examResultEntry : examResultEntryList) {
                ObjectId examId = examResultEntry.getID();
                List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(examId, null, null, null);
                Integer process = examResultService.getRateSchedule(performanceEntryList, new ObjectId(subjectId), new ObjectId(classId));
                ExamInfoDTO exam = new ExamInfoDTO(examResultEntry.getName(), examId.toString(), process + "/" + totalStu, examResultEntry.getDate());
                exam.setIsGrade(examResultEntry.getIsGradeExam());
                examList.add(exam);
            }
        }
        if(order == 1) {
            Collections.sort(examList,new SortByDate());
        } else {
            Collections.sort(examList,new SortByProcess());
        }
        model.put("examList", examList);
        model.put("code", 200);
        return model;
    }

    /**
     * 取得编辑考试回显信息
     * @return
     */
    @RequestMapping("/getExamInfo")
    @ResponseBody
    public Map<String, Object> getExamInfo(String examId, String subId) {
        ObjectId subjectId = new ObjectId(subId);
        Map<String, Object> model = new HashMap<String, Object>();
        ExamResultEntry examResultEntry = examResultService.getExamResultEntry(new ObjectId(examId));
        model.put("examName", examResultEntry.getName());
        model.put("date", examResultEntry.getDate());
        List<Score> scoreList = new ArrayList<Score>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        if (performanceEntryList != null) {
            PerformanceEntry performanceEntry = performanceEntryList.get(0);
            scoreList = performanceEntry.getScoreList();
        }
        for(Score score : scoreList) {
            if(score.getSubjectId().equals(subjectId)) {
                model.put("fullScore", score.getFullScore());
                model.put("failScore", score.getFailScore());
            }
        }
        List<ObjectId> classes = examResultEntry.getClassList();
        List<String> classList = new ArrayList<String>();
        for(ObjectId classId : classes) {
            classList.add(classId.toString());
        }
        model.put("classList", classList);
        model.put("code", 200);
        return model;
    }

    /**
     * 编辑班级考试
     * @param examId
     * @param subjectId
     * @param name
     * @param classList
     * @param date
     * @param fullScore
     * @param failScore
     * @return
     */
    @RequestMapping("/teacherEditClassExam")
    @ResponseBody
    public boolean teacherEditClassExam(String examId, String subjectId, String name,String classList,String date, Integer fullScore, Integer failScore) {
        ExamResultEntry examResultEntry = examResultService.getExamResultEntry(new ObjectId(examId));
        examResultEntry.setName(name);
        examResultEntry.setDate(date);
        List<ObjectId> class_List = new ArrayList<ObjectId>();
        for(String classId : classList.split(",")) {
            class_List.add(new ObjectId(classId));
        }
        examResultEntry.setClassList(class_List);
        examResultService.teacherEditExam(new ObjectId(subjectId), examResultEntry, fullScore, failScore,false);

        return true;
    }

    /**
     * 删除某次考试
     * @param examId
     * @return
     */
    @RequestMapping("/teacherDeleteExam")
    @ResponseBody
    public boolean teacherDeleteExam(String examId, String classId) {
        return examResultService.teacherDeleteExam(new ObjectId(examId), new ObjectId(classId));
    }
    //===========================================老师成绩录入==============================================

    /**
     * 列表
     * @param examId
     * @param classId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getEditScoreList")
    @ResponseBody
    public Map<String, Object> getEditScoreList(String examId, String classId, String subjectId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<PerformanceDTO> perDTOList = new ArrayList<PerformanceDTO>();
        Integer fullScore = 0;
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), new ObjectId(classId), null, null);
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            PerformanceDTO p = new PerformanceDTO();
            p.setPerformanceId(performanceEntry.getId().toString());
            p.setStudentName(performanceEntry.getStudentName());
            List<Score> scoreList = performanceEntry.getScoreList();
            for(Score score : scoreList) {
                if(score.getSubjectId().toString().equals(subjectId)){
                    p.setSubjectScore(score.getObjSubjectScore());
                    p.setAbsence(score.getAbsence());
                    p.setExemption(score.getExemption());
                    fullScore = score.getFullScore();
                }
            }

            perDTOList.add(p);
        }
        model.put("perDTOList", perDTOList);
        model.put("fullScore", fullScore);
        model.put("code", 200);
        return model;
    }

    /**
     * 保存打分
     * @param performanceList
     * @param scoreList
     * @param subjectId
     * @return
     */
    @RequestMapping("/saveEditScoreList")
    @ResponseBody
    public boolean saveEditScoreList(String performanceList, String scoreList, String absList, String exeList, String subjectId) {

        String[] perId = performanceList.split(",");
        String[] score = scoreList.split(",");
        String[] abs = absList.split(",");
        String[] exe = exeList.split(",");

        for(int i=0; i< score.length-1; i++) {
            ObjectId performanceId = new ObjectId(perId[i]);
            ObjectId subId = new ObjectId(subjectId);
            Double subjectScore;
            if(score[i].equals("")) {
                subjectScore = null;
            } else {
                subjectScore = Double.parseDouble(score[i]);
            }
            Integer absence = Integer.parseInt(abs[i]);
            Integer exemption = Integer.parseInt(exe[i]);
            examResultService.updateScore(performanceId, subId, subjectScore, absence, exemption);
        }
        ExpLogType expLogType = ExpLogType.PERFORMANCE_ANALYSIS;
        String userId=getUserId().toString();
        experienceService.updateScore(userId, expLogType, subjectId);
        /*if () {
            map.put("scoreMsg", expLogType.getDesc());
            map.put("score", expLogType.getExp()+"");
        }*/
        return true;
    }

    @RequestMapping("/exportExcel")
    @ResponseBody
    public boolean exportExamScoreExcel(String examId, String classId, String subjectId, String examTitle, HttpServletResponse response) {
        Map<String, Object> data = getEditScoreList(examId, classId, subjectId);
        examScoreExcelService.exportExamScoreExcel((List<PerformanceDTO>)data.get("perDTOList"), response, examTitle);
        return true;

    }

    @RequestMapping("/import")
    @ResponseBody
    public boolean importData(@RequestParam("file") MultipartFile file,String examId, String subjectId) throws Exception {
        //获取班级ID为了校验上传文件是否合适
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls")) {
            //throw new Exception("文件格式错误！");
            return false;
        }
        examScoreExcelService.importStudentScore(file.getInputStream(), new ObjectId(examId), new ObjectId(subjectId));
        ExpLogType expLogType = ExpLogType.PERFORMANCE_ANALYSIS;
        String userId=getUserId().toString();
        experienceService.updateScore(userId, expLogType,subjectId);
        return true;
    }
//==============================================学生首页=================================================================

    private ObjectId getStudentId() {
        String userId = getSessionValue().getId();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        if (UserRole.isParent(userInfoDTO4WB.getRole())) {//如果是家长转换成学生
            userInfoDTO4WB = userService.findStuInfoByParentId(userId);
        }
        return new ObjectId(userInfoDTO4WB.getId());
    }

    /**
     * 学生页面查询列表
     * @return
     */
    @RequestMapping("/stuGetExamSelection")
    @ResponseBody
    public Map<String, Object> stuGetExamSelection() {
        ObjectId stuId = getStudentId();
        Map<String, Object> model = new HashMap<String, Object>();
        String schoolId = getSessionValue().getSchoolId();
        String stuName = getSessionValue().getUserName();

        //查询学生的班级列表
        List<ClassEntry> classList = classService.getStudentClassList(stuId);
        for(ClassEntry classEntry : classList) {
            Map<String, Object> exams = new HashMap<String, Object>();
            ObjectId classId = classEntry.getID();
            exams.put("classId", classId.toString());
            List<ExamResultEntry> examResultEntryList = examResultService.getExamList(classId, null, null);
            if(null != examResultEntryList) {  //存在考试
                for (ExamResultEntry examResultEntry : examResultEntryList) {
                    List<ObjectId> subjects = examResultEntry.getSubjectList();
//                    List<ObjectId> subjects = performanceService.getSubjectListByExamId(examResultEntry.getID());
                    Map<String, Object> exam = new HashMap<String, Object>();
                    exam.put("examId", examResultEntry.getID().toString());
                    exam.put("schoolYear", examResultEntry.getSchoolYear());
                    exam.put("stuName", stuName);
                    List<SubjectView> subjectViewList = new ArrayList<SubjectView>();
                    for (ObjectId subId : subjects) {
                        SubjectView subjectView = schoolService.findSubjectBySchoolIdAndSubId(schoolId, subId.toString());
                        subjectViewList.add(subjectView);
                    }
                    SubjectView subjectView = new SubjectView();
                    subjectView.setId("000000000000000000000000");
                    subjectView.setName("总分");
                    subjectViewList.add(subjectView);
                    exam.put("subjectViewList", subjectViewList);
                    exams.put(examResultEntry.getName(), exam);
                }
            }
            model.put(classEntry.getName(), exams);
        }
        model.put("code", 200);
        return  model;
    }


    /**
     * 打败了班级、年级百分之多少的同学
     * @param examId
     * @param subjectId
     * @return
     */
    @RequestMapping("/getClassDefeatRate")
    @ResponseBody
    public Map<String, Integer> getClassDefeatRate(String examId, String subjectId, String classId) {
        ObjectId stuId = getStudentId();
        Map<String, Integer> model = new HashMap<String, Integer>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        Integer classDR = 100;
        Integer gradeDR = 100;
        if(performanceEntryList!= null && performanceEntryList.size()>0) {
            classDR = examResultService.getDefeatRate(performanceEntryList, new ObjectId(subjectId), stuId, new ObjectId(classId));
            gradeDR = examResultService.getDefeatRate(performanceEntryList, new ObjectId(subjectId), stuId, null);
        }
            model.put("classDR", classDR);
            model.put("gradeDR", gradeDR);
        model.put("code", 200);
        return model;
    }

    /**
     * 考试成绩，班级均分，年级均分
     * @param examId
     * @param subjectId
     * @param classId
     * @return
     */
    @RequestMapping("/getStuScore")
    @ResponseBody
    public Map<String, Double> getStuScore(String examId, String subjectId, String classId) {
        ObjectId stuId = getStudentId();
        Map<String, Double> model = new HashMap<String, Double>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        Double stuScore = 0D;
        Double classScore = 0D;
        Double gradeScore = 0D;
        if(performanceEntryList!= null && performanceEntryList.size()>0) {
            stuScore = examResultService.getSubjectScore(performanceEntryList, new ObjectId(subjectId), stuId);
            classScore = examResultService.getAveragePerformance(performanceEntryList, new ObjectId(subjectId), new ObjectId(classId));
            gradeScore = examResultService.getAveragePerformance(performanceEntryList, new ObjectId(subjectId), null);
        }
            model.put("stuScore", stuScore);
            model.put("classScore", Math.round(classScore*10)/10.0);
            model.put("gradeScore", Math.round(gradeScore*10)/10.0);
            model.put("code", 200D);
        return model;
    }

    /**
     * 班级排名，年级排名
     * @param examId
     * @param subjectId
     * @param classId
     * @return
     */
    @RequestMapping("/getStuRanking")
    @ResponseBody
    public Map<String, Integer> getStuRanking(String examId, String subjectId, String classId) {
        ObjectId stuId = getStudentId();
        Map<String, Integer> model = new HashMap<String, Integer>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        Integer classRanking = 1;
        Integer gradeRanking = 1;
        if(performanceEntryList!= null && performanceEntryList.size()>0) {
            classRanking = examResultService.getRanking(performanceEntryList, new ObjectId(subjectId), stuId, new ObjectId(classId));
            gradeRanking = examResultService.getRanking(performanceEntryList, new ObjectId(subjectId), stuId, null);
        }
            model.put("classRanking", classRanking);
            model.put("gradeRanking", gradeRanking);
        model.put("code", 200);
        return model;
    }


    /**
     * 学生某次考试成绩列表
     * @return
     */
    @RequestMapping("/getSubScoreList")
    @ResponseBody
    public Map<String, Object> getSubScoreList(String examId) {
        ObjectId stuId = getStudentId();
        Map<String, Object> model = new HashMap<String, Object>();
        List<Double> scoreList = new ArrayList<Double>();
        List<String> subNameList = new ArrayList<String>();
        Map<String, Double> subScoreList = new HashMap<String, Double>();
        List<PerformanceEntry> performanceEntryList = examResultService.getPerformanceEntryList(new ObjectId(examId), null, null, null);
        if(performanceEntryList != null && performanceEntryList.size()>0) {
            subScoreList = examResultService.getSubjectScoreMap(performanceEntryList, stuId);
            subNameList = new ArrayList<String>(subScoreList.keySet());
            for (String subName : subNameList) {
                scoreList.add(subScoreList.get(subName));
            }
        }
        model.put("scoreList", scoreList);
        model.put("subNameList",subNameList);
        model.put("chinese", subScoreList.get("语文"));
        model.put("math", subScoreList.get("数学"));
        model.put("english", subScoreList.get("英语"));
        model.put("code", 200);
        return model;
    }

    //==============================================================学生学期成绩=================================================

    /**
     * 一学期的个人成绩、年级成绩
     * @param classId
     * @return
     */
    @RequestMapping("/getSemesterStuScore")
    @ResponseBody
    public Map<String, Object> getSemesterStuScore(String classId) {
        ObjectId stuId = getStudentId();
        Map<String, Object> model = new HashMap<String, Object>();
        List<String> subNameList = new ArrayList<String>();
        List<Double> stuScoreList = new ArrayList<Double>();
        List<Double> gradeScoreList = new ArrayList<Double>();
        //查询班级的所有科目
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<SubjectView> subjectViewList = schoolService.findSubjectListBySchoolIdAndGradeId(classEntry.getSchoolId().toString(), classEntry.getGradeId().toString());
        for(SubjectView subjectView : subjectViewList) {
            double stuScore = examResultService.getStuSemesterScore(new ObjectId(subjectView.getId()), stuId, new ObjectId(classId));
            double gradeScore = examResultService.getGradeSemesterScore(new ObjectId(subjectView.getId()), classEntry.getGradeId());
            subNameList.add(subjectView.getName());
            stuScoreList.add(Math.round(stuScore*10)/10.0);
            gradeScoreList.add(Math.round(gradeScore*10)/10.0);
        }
        model.put("subNameList", subNameList);
        model.put("stuScoreList", stuScoreList);
        model.put("gradeScoreList", gradeScoreList);
        model.put("code", 200);
        return model;
    }

    /**
     * 学期各科的平时成绩、期中成绩、期末成绩
     * @param classId
     * @return
     */
    @RequestMapping("/getAMFScore")
    @ResponseBody
    public Map<String, Object> getAMFScore(String classId) {
        ObjectId stuId = getStudentId();
        Map<String, Object> model = new HashMap<String, Object>();
        List<String> subNameList = new ArrayList<String>();
        List<Double> aveScoreList = new ArrayList<Double>();
        List<Double> midScoreList = new ArrayList<Double>();
        List<Double> finScoreList = new ArrayList<Double>();
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<SubjectView> subjectViewList = schoolService.findSubjectListBySchoolIdAndGradeId(classEntry.getSchoolId().toString(), classEntry.getGradeId().toString());
        for(SubjectView subjectView : subjectViewList) {
            ScoreDTO scoreDTO = examResultService.getStuScoreDTO(new ObjectId(subjectView.getId()), stuId, new ObjectId(classId));
            subNameList.add(subjectView.getName());
            aveScoreList.add(Math.round(scoreDTO.getUsualScore()*10)/10.0);
            midScoreList.add(scoreDTO.getMidtermScore());
            finScoreList.add(scoreDTO.getFinalScore());
//            model.put(subjectView.getName(), scoreDTO);
        }
        model.put("subNameList", subNameList);
        model.put("aveScoreList", aveScoreList);
        model.put("midScoreList", midScoreList);
        model.put("finScoreList", finScoreList);
        model.put("code", 200);
        return  model;
    }
//====================================================学生历史成绩=========================================================

    /**
     * 选择列表
     * @param classId
     * @return
     */
    @RequestMapping("/getSemesterSelection")
    @ResponseBody
    public Map<String, Object>  getSemesterSelection(String classId) {
        List<String> schoolYear = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        if (month < 9 && month >= 2) {
            schoolYear.add((year - 1) + "-" + year + "学年第二学期");
            schoolYear.add((year - 1) + "-" + year + "学年第一学期");
        } else if(month >= 9) {
            schoolYear.add(year + "-" + (year + 1) + "学年第一学期");
        } else {
            schoolYear.add((year - 1) + "-" + year + "学年第一学期");
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("schoolYear", schoolYear);
        //查询班级的所有科目
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<SubjectView> subjectViewList = schoolService.findSubjectListBySchoolIdAndGradeId(classEntry.getSchoolId().toString(), classEntry.getGradeId().toString());
        model.put("subjectViewList", subjectViewList);
        model.put("code", 200);
        return model;
    }
    /**
     * 某学科一学期的历史成绩
     * @param subjectId
     * @return
     */
    @RequestMapping("/getHistorySubjectScore")
    @ResponseBody
    public Map<String, Object> getHistorySubjectScore(String schoolYear, String classId, String subjectId, Integer order) {
        ObjectId stuId = getStudentId();
        Map<String, Object> model = new HashMap<String, Object>();
        List<String> examNameList = new ArrayList<String>();
        List<Double> stuScoreList = new ArrayList<Double>();
        List<Double> classScoreList = new ArrayList<Double>();
        List<Integer> classRanking = new ArrayList<Integer>();
        List<SubjectExamDTO> subjectExamDTOs = new ArrayList<SubjectExamDTO>();
        List<ExamResultEntry> examResultEntryList = examResultService.getExamList(new ObjectId(classId), new ObjectId(subjectId), null);
        if(examResultEntryList != null) {
            for (ExamResultEntry examResultEntry : examResultEntryList) {
                if (examResultEntry.getSchoolYear().equals(schoolYear)) {
                    SubjectExamDTO subjectExamDTO = examResultService.getSubjectExamDTO(examResultEntry, new ObjectId(classId), new ObjectId(subjectId), stuId);
                    subjectExamDTOs.add(subjectExamDTO);
                }
            }
        }
        if(order == 0) {
            Collections.sort(subjectExamDTOs, new SortByDate1());
        } else if(order == 1) {
            Collections.sort(subjectExamDTOs, new SortByScore());
        } else if(order == 2) {
            Collections.sort(subjectExamDTOs, new SortByClassScore());
        } else if(order == 3) {
            Collections.sort(subjectExamDTOs, new SortByClassRanking());
        }
        for(SubjectExamDTO subjectExamDTO : subjectExamDTOs) {
            examNameList.add(subjectExamDTO.getExamName());
            stuScoreList.add(subjectExamDTO.getScore());
            classScoreList.add(Math.round(subjectExamDTO.getClassAverageScore()*10)/10.0);
            classRanking.add(subjectExamDTO.getClassRanking());
        }
        model.put("examNameList", examNameList);
        model.put("stuScoreList", stuScoreList);
        model.put("classScoreList",classScoreList);
        model.put("classRanking", classRanking);
        model.put("subExamList",subjectExamDTOs);
        model.put("code", 200);
        return model;
    }


//============================================================班主任首页================================================================

    /**
     * 班主任首页选择列表
     * @return
     */
    @RequestMapping("/getMasterSelection")
    @ResponseBody
    public Map<String, Object> getMasterSelection() {
        String masterId = getSessionValue().getId();
        Map<String, Object> model = new HashMap<String, Object>();
        SchoolEntry schoolEntry;
        try {
            schoolEntry = schoolService.getSchoolEntryByUserId(new ObjectId(masterId));
        } catch (IllegalParamException e) {
            e.printStackTrace();
            return null;
        }
        //班主任所在年级的全部班级
        List<ClassEntry> classList = classService.findClassEntryByMasterId(new ObjectId(masterId));
        for(ClassEntry classEntry : classList) {
            Map<String, Object> exams = new HashMap<String, Object>();
            ObjectId classId = classEntry.getID();
            exams.put("classId", classId);
            List<ExamResultEntry> examResultEntryList = examResultService.getExamList(classId, null, null);
            for(ExamResultEntry examResultEntry : examResultEntryList) {
                List<ObjectId> subjects = examResultEntry.getSubjectList();
                Map<String, Object> exam = new HashMap<String, Object>();
                exam.put("examId", examResultEntry.getID());
                exam.put("schoolYear", examResultEntry.getSchoolYear());
                List<SubjectView> subjectViewList = new ArrayList<SubjectView>();
                for(ObjectId subId : subjects) {
                    SubjectView subjectView = schoolService.findSubjectBySchoolIdAndSubId(schoolEntry.getID().toString(), subId.toString());
                    subjectViewList.add(subjectView);
                }
                SubjectView subjectView = new SubjectView();
                subjectView.setId("000000000000000000000000");
                subjectView.setName("总分");
                subjectViewList.add(subjectView);
                exam.put("subjectViewList", subjectViewList);
                exams.put(examResultEntry.getName(), exam);
            }
            model.put(classEntry.getName(), exams);
        }
        model.put("code", 200);
        return  model;
    }

    //==================================================教育局=====================================================
    @RequestMapping("/getSchoolList")
    @ResponseBody
    public Map<String, Object> getSchoolList(){
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId userId = getUserId();
        EducationBureauEntry educationBureauEntry = educationBureauService.selEducationByUserId(userId.toString());
        List<ObjectId> schoolIds = educationBureauEntry.getSchoolIds();
        List<SchoolDTO> schoolDTOList = new ArrayList<SchoolDTO>();
        if (schoolIds != null && schoolIds.size()>0) {
            for (ObjectId schoolId : schoolIds) {
                SchoolEntry schoolEntry = schoolService.getSchoolEntry(schoolId,Constant.FIELDS);
                if (schoolEntry!=null) {
                    SchoolDTO schoolDTO = new SchoolDTO();
                    schoolDTO.setLogo(schoolEntry.getLogo());
                    schoolDTO.setSchoolId(schoolEntry.getID().toString());
                    schoolDTOList.add(schoolDTO);
                }
            }
        }
        model.put("schoolDTOList", schoolDTOList);
        model.put("code", 200);
        return model;
    }

    @RequestMapping("/getclasslist")
    @ResponseBody
    public Map<String, Object> getClassList(@RequestParam(required = false, defaultValue = "")String schoolId, String gradeId){
        if(schoolId.equals("")){
            schoolId = getSessionValue().getSchoolId();
        }
       Map<String, SubjectView> subjectViewMap = schoolService.findSubjectViewMapBySchoolIdAndGradeId(schoolId, gradeId);
        Map<String, Object> model = new HashMap<String, Object>();
        List<ClassInfoDTO> classList = classService.findClassByGradeId(gradeId);
        if (classList!=null && classList.size()>0) {
            for (ClassInfoDTO classEntry : classList) {
                Map<String, Object> exams = new HashMap<String, Object>();
                ObjectId classId = new ObjectId(classEntry.getId());
                exams.put("classId", classId.toString());
                List<ExamResultEntry> examResultEntryList = examResultService.getExamList(classId, null, null);
                if (null != examResultEntryList) {  //存在考试
                    for (ExamResultEntry examResultEntry : examResultEntryList) {
                        List<ObjectId> subjects = examResultEntry.getSubjectList();
                        Map<String, Object> exam = new HashMap<String, Object>();
                        exam.put("examId", examResultEntry.getID().toString());
                        exam.put("schoolYear", examResultEntry.getSchoolYear());
                        List<SubjectView> subjectViewList = new ArrayList<SubjectView>();
                        for (ObjectId subId : subjects) {
                            SubjectView subjectView = subjectViewMap.get(subId.toString());
                            subjectViewList.add(subjectView);
                        }
//                    SubjectView subjectView = new SubjectView();
//                    subjectView.setId("000000000000000000000000");
//                    subjectView.setName("总分");
//                    subjectViewList.add(subjectView);
                        exam.put("subjectViewList", subjectViewList);
                        exams.put(examResultEntry.getName(), exam);
                    }
                }
                model.put(classEntry.getClassName(), exams);
            }
        }
        model.put("code", 200);
        return  model;
    }

    @RequestMapping("/getclasslist1")
    @ResponseBody
    public Map<String, Object> getClassList1(String gradeId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        model.put("classList", classInfoDTOList);
        model.put("code", 200);
        return model;
    }

    @RequestMapping("/getSubjectList")
    @ResponseBody
    public Map<String, Object> getSubjectList(@RequestParam(required = false, defaultValue = "")String schoolId, String gradeId){
        if(schoolId.equals("")){
            schoolId = getSessionValue().getSchoolId();
        }
        Map<String, Object> model = new HashMap<String, Object>();
        List<SubjectView> subjectViewList = schoolService.findSubjectListBySchoolIdAndGradeId(schoolId, gradeId);
        model.put("subjectViewList", subjectViewList);
        model.put("code", 200);
        return model;
    }
    @RequestMapping("/getSubjectScoreList")
    @ResponseBody
    public Map<String, Object> getSubjectScoreList(@RequestParam(required = false, defaultValue = "")String schoolId, String gradeId, String classId) {
        if(schoolId.equals("")){
            schoolId = getSessionValue().getSchoolId();
        }
        Map<String, Object> model = new HashMap<String, Object>();
        List<SubjectView> subjectViewList = schoolService.findSubjectListBySchoolIdAndGradeId(schoolId, gradeId);
        Map<String, SubjectView> subjectMap = new HashMap<String, SubjectView>();
        if(subjectViewList!=null && !subjectViewList.isEmpty()) {
            for (SubjectView subjectView : subjectViewList) {
                subjectMap.put(subjectView.getId().toString(), subjectView);
            }
        }

        List<ScoreDTO> scoreDTOList = examResultService.getClassScoreDTOForAllSubject(subjectViewList, new ObjectId(classId));
        model.put("scoreDTOList", scoreDTOList);
        model.put("code", 200);
        return model;
    }

    @RequestMapping("/getSubjectRateList")
    @ResponseBody
   public Map<String, Object> getSubjectRateList(@RequestParam(required = false, defaultValue = "")String schoolId, String gradeId, String classId){
        if(schoolId.equals("")){
            schoolId = getSessionValue().getSchoolId();
        }
       Map<String, Object> model = new HashMap<String, Object>();
        List<SubjectView> subjectViewList = schoolService.findSubjectListBySchoolIdAndGradeId(schoolId, gradeId);
        Map<String, SubjectView> subjectMap = new HashMap<String, SubjectView>();
        if(subjectViewList!=null && !subjectViewList.isEmpty()) {
            for (SubjectView subjectView : subjectViewList) {
                subjectMap.put(subjectView.getId().toString(), subjectView);
            }
        }

        List<SubjectRateDTO> subjectRateDTOList = examResultService.getSubjectRateDTOList(subjectViewList, new ObjectId(classId));
        model.put("subjectRateDTOList", subjectRateDTOList);
        model.put("code", 200);
       return model;
   }

    //==================================================区域联考===========================================================

    /**
     * 得到区域联考信息
     * @param jointExamId
     * @return
     */
    @RequestMapping("/getExamSummary")
    @ResponseBody
    public Map<String, Object> getExamSummary(@ObjectIdType ObjectId jointExamId){
//        ExamResultService examResultService = new ExamResultService();
//        jointExamId = new ObjectId("562d99812dac52108161c939");
        return examResultService.getJointExam(jointExamId);
    }

    /**
     * 学生成绩汇总表
     * @param areaExamId
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getAreaStudentScore")
    @ResponseBody
    public Map<String, Object> getAreaStudentScore(@ObjectIdType ObjectId areaExamId, int page, int pageSize){
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> rows = examResultService.getAreaStudentScore(areaExamId, (page-1)*pageSize, pageSize);
        model.put("rows", rows);
        model.put("total", examResultService.findPerformanceCountByAreaId(areaExamId));
        model.put("page", page);
        return model;
    }

    /**
     * 保存分数段，并查询成绩分布
     * @param beginList
     * @param endList
     * @param jointExamId
     * @return
     */
    @RequestMapping("/setScoreSection")
    @ResponseBody
    public List<Map<String, Object>> setScoreSection(String beginList, String endList, @ObjectIdType ObjectId jointExamId){
        Map<String, Object> model = new HashMap<String, Object>();
        String[] beginArray = beginList.split(",");
        String[] endArray = endList.split(",");
        List<ScoreSection> scoreSectionList = new ArrayList<ScoreSection>();
        for(int i=0; i< beginArray.length; i++) {
            Integer begin = Integer.parseInt(beginArray[i]);
            Integer end = Integer.parseInt(endArray[i]);
            ScoreSection scoreSection = new ScoreSection(begin, end);
            scoreSectionList.add(scoreSection);
        }
        examResultService.updateScoreSection(jointExamId, scoreSectionList);
        model.put("msg","success");
        return getScoreDistribution(jointExamId);
//        return model;
    }

    /**
     * 查询成绩分布
     * @param jointExamId
     * @return
     */
    @RequestMapping("/getScoreDistribution")
    @ResponseBody
    public List<Map<String, Object>> getScoreDistribution(@ObjectIdType ObjectId jointExamId){
        return examResultService.getScoreDistribution(jointExamId);
    }

    /**
     * 学校综合排名、学生区域名次
     * @param jointExamId
     * @return
     */
    @RequestMapping("/rankScore")
    @ResponseBody
    public RespObj rankScore(@ObjectIdType ObjectId jointExamId){
         examResultService.rankScore(jointExamId);
        return RespObj.SUCCESS;
    }

    /**
     * 设置等级
     * @param names
     * @param percents
     * @param jointExamId
     * @return
     */
    @RequestMapping("/setGradeSetting")
    @ResponseBody
    public RespObj setGradeSetting(String names, String percents, @ObjectIdType ObjectId jointExamId){
        String[] nameArray = names.split(",");
        String[] percentArray = percents.split(",");
        List<NameValuePair> scoreSectionList = new ArrayList<NameValuePair>();
        for(int i=0; i< nameArray.length; i++) {
            String name = nameArray[i];
            Integer percent = Integer.parseInt(percentArray[i]);
            NameValuePair grade = new NameValuePair(name, percent);
            scoreSectionList.add(grade);
        }
        examResultService.updateGrades(jointExamId, scoreSectionList);
        return getSubjectDistribution(jointExamId);
    }

    /**
     * 学科统计
     * @param jointExamId
     * @return
     */
    @RequestMapping("/getSubjectDistribution")
    @ResponseBody
    public RespObj getSubjectDistribution(@ObjectIdType ObjectId jointExamId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            List<Map<String, Object>> list = examResultService.getSubjectDistributions(jointExamId);
            respObj.setMessage(list);
        } catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }



}
