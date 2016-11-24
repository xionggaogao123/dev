package com.fulaan.examresult.service;

import com.db.examresult.ExamResultDao;
import com.db.examresult.ExamSummaryDao;
import com.db.examresult.JointExamDao;
import com.db.examresult.PerformanceDao;
import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.fulaan.examresult.controller.SubjectExamDTO;
import com.fulaan.examresult.controller.SubjectRateDTO;
import com.fulaan.myschool.controller.SubjectView;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.NameValuePair;
import com.pojo.examregional.*;
import com.pojo.examresult.*;
import com.pojo.examresult.ScoreDTO;
import com.pojo.school.ClassEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fl on 2015/6/15.
 */
@Service
public class ExamResultService {

    ClassDao classDao = new ClassDao();
    UserDao userDao = new UserDao();
    ExamResultDao examResultDao = new ExamResultDao();
    PerformanceDao performanceDao = new PerformanceDao();
    SchoolDao schoolDao = new SchoolDao();
    private JointExamDao jointExamDao = new JointExamDao();
    private ExamSummaryDao examSummaryDao = new ExamSummaryDao();

    
    
    public String getTerm(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if (month < 9 && month >= 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if(month >= 9){
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        return schoolYear;
    }
	
	 /**
     * 得到当前学年可用学期
     * @return
     */
    public List<String> getUsableTerm(){
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
        return schoolYear;
    }
    
    
    
    /**
     * 得到当前学期
     * @return
     */
    public String getCurrentTerm() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if (month < 9 && month >= 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if(month >= 9){
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        return schoolYear;
    }


    /**
     * 得到某班级的某次考试成绩
     * @param examId
     * @param classId
     * @return
     */
    public Map<ObjectId, PerformanceEntry> getPerformanceEntryMap(ObjectId examId, ObjectId classId, ObjectId stuId, List<ObjectId> examList) {
        List<PerformanceEntry> performanceEntryList = getPerformanceEntryList(examId, classId, stuId, examList);
        Map<ObjectId, PerformanceEntry> performanceEntryMap = new HashMap<ObjectId, PerformanceEntry>();
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            performanceEntryMap.put(performanceEntry.getId(), performanceEntry);
        }
        return performanceEntryMap;
    }

    /**
     * 得到某班级某次考试的成绩列表
     * @param examId
     * @return
     */
    public List<PerformanceEntry> getPerformanceEntryList(ObjectId examId, ObjectId classId, ObjectId stuId, List<ObjectId> examList) {
        List<PerformanceEntry> performanceEntryList = performanceDao.getPerformanceEntryList(examId, classId, stuId, examList, null);
        if(performanceEntryList.size() == 0) {
            ExamResultEntry examResultEntry = examResultDao.getExamResultEntry(examId);
            if(examResultEntry != null) {
                performanceEntryList = performanceDao.getPerformanceEntryList(examResultEntry.getPerformanceList(), classId, stuId);
            }
        }
        return performanceEntryList;
    }


    /**
     * 组建考试相关Entry，用于新建考试和编辑考试
     * @param e
     * @param subjectList
     * @param fullScore
     * @param failScore
     * @return
     */
    public ExamResultEntry buildExam(ExamResultEntry e, List<ObjectId> subjectList, Integer fullScore, Integer failScore) {
        ObjectId examId = e.getID();
        //生成本次考试学科-成绩对应表
        List<Score> scoreList = new ArrayList<Score>();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(e.getSchoolId(),Constant.FIELDS);
        List<Subject> subjects = schoolEntry.getSubjects();
        for(ObjectId subjectId : subjectList) {
            for(Subject subject : subjects) {
                if(subject.getSubjectId().equals(subjectId)) {
                    Score score = new Score(subjectId,subject.getName(), null, 0, 0, fullScore, failScore);
                    scoreList.add(score);
                    break;
                }
            }
        }
        //根据e中的classList查出学生列表，新建PerformanceEntry;
        List<PerformanceEntry> pList = new ArrayList<PerformanceEntry>();
        for(ObjectId classId :e.getClassList()) {
            ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
            List<ObjectId> students = classEntry.getStudents();
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(students, Constant.FIELDS);
            for(ObjectId stu : students) {
                UserEntry student = userEntryMap.get(stu);
                String studentName = student.getUserName();
                PerformanceEntry performanceEntry = new PerformanceEntry(examId,stu,studentName,classId, scoreList);
                pList.add(performanceEntry);
            }
        }
        List<ObjectId> performanceList = performanceDao.save(pList);
//        e.setPerformanceList(performanceList);
        return e;
    }

    /**
     * 添加一条考试记录
     * @param e
     * @param subjectList
     */
    public void addExam(ExamResultEntry e, List<ObjectId> subjectList, Integer fullScore, Integer failScore) {
        examResultDao.add(e);
        buildExam(e, subjectList, fullScore, failScore);
        examResultDao.updateInfo(e.getID(), e);
    }


    /**
     * 更新成绩
     * @param performanceId
     * @param subjectId
     * @param subjectScore
     */
    public void updateScore(ObjectId performanceId, ObjectId subjectId, Double subjectScore, Integer absence, Integer exemption) {
        performanceDao.updateScore(performanceId, subjectId, subjectScore, absence, exemption);
    }

    /**
     * 根据ID查询考试
     * @param examId
     * @return
     */
    public ExamResultEntry getExamResultEntry(ObjectId examId) {
        return examResultDao.getExamResultEntry(examId);
    }

    /**
     * 查询某次考试某个班某科目的平均成绩
     * @param classId
     * @param subjectId
     * @return
     */
    public double getAveragePerformance(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId classId) {
        if(performanceEntryList == null)
            return 0;
        double totalScore = 0;  //总分数
        int totalStudent = 0;  //总学生数
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            if(classId == null || performanceEntry.getClassId().equals(classId)) {
                List<Score> scoreList = performanceEntry.getScoreList();
                if(subjectId.equals(new ObjectId("000000000000000000000000"))) {//表示求总分
                    for (Score score : scoreList) {
                            totalScore += score.getSubjectScore();
                    }
                    totalStudent += 1;
                } else {
                    for (Score score : scoreList) {
                        if (score.getSubjectId().equals(subjectId)) {
                            totalScore += score.getSubjectScore();
                            totalStudent += 1;
                        }
                    }
                }
            }
        }
        if(0 != totalStudent) {
            return totalScore/totalStudent;
        }
        return 0;
    }

    /**
     * 查询某次考试某个同学某科目的成绩
     * @param performanceEntryList
     * @param subjectId
     * @param studentId
     * @return
     */
    public Double getSubjectScore(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId studentId) {
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            if(performanceEntry.getStudentId().equals(studentId)) {
                List<Score> scoreList = performanceEntry.getScoreList();
                if (subjectId.equals(new ObjectId("000000000000000000000000"))) {//表示求总分
                    double totalScore = 0;
                    for (Score score : scoreList) {
                        totalScore += score.getSubjectScore();
                    }
                    return totalScore;
                } else {
                    for (Score score : scoreList) {
                        if (score.getSubjectId().equals(subjectId)) {
                            return score.getSubjectScore();
                        }
                    }
                }
            }
        }

        return 0.0;
    }


    /**
     * 查询某次考试某个同学所有科目的成绩
     * @param performanceEntryList
     * @param studentId
     * @return
     */
    public Map<String, Double> getSubjectScoreMap(List<PerformanceEntry> performanceEntryList, ObjectId studentId) {
        Map<String, Double> subjectScoreMap = new HashMap<String, Double>();
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            if(performanceEntry.getStudentId().equals(studentId)) {
                List<Score> scoreList = performanceEntry.getScoreList();
                for(Score score : scoreList) {
                    subjectScoreMap.put(score.getSubjectName(), score.getObjSubjectScore());
                }
                return subjectScoreMap;
            }
        }

        return null;
    }

    /**
     * 得到排名和学生总数
     * @param performanceEntryList
     * @param subjectId
     * @param studentId
     * @param classId
     * @return
     */
    public int[] getRankAndTotalStu(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId studentId, ObjectId classId) {
        Double stuScore = getSubjectScore(performanceEntryList, subjectId, studentId);
        if(stuScore == null) {
            stuScore = 0.0;
        }
        int totalStu = 0;
        int ranking = 1;
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            if(classId == null || performanceEntry.getClassId().equals(classId)) {
                List<Score> scoreList = performanceEntry.getScoreList();
                if(subjectId.equals(new ObjectId("000000000000000000000000"))){//表示求总分
                    double totalSubScore = 0;
                    for (Score score : scoreList) {
                        totalSubScore += score.getSubjectScore();
                    }
                    if (totalSubScore > stuScore) {
                        ranking++;
                    }
                    totalStu++;
                } else {
                    for (Score score : scoreList) {
                        if (score.getSubjectId().equals(subjectId)) {
                            if(score.getSubjectScore() > stuScore) {
                                ranking ++;
                            }
                            totalStu++;
                        }
                    }
                }
            }
        }
        int[] data = new int[2];
        data[0] = ranking;
        data[1] = totalStu;
        return data;
    }

    /**
     * 查询某次考试的排名
     * @param performanceEntryList
     * @param subjectId
     * @param classId 有值表示班级，null表示年级
     * @param studentId
     * @return
     */
    public int getRanking(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId studentId, ObjectId classId) {
        int[] data = getRankAndTotalStu(performanceEntryList, subjectId, studentId, classId);
        return data[0];
    }

    /**
     * 打败百分之多少的同学
     * @param performanceEntryList
     * @param subjectId
     * @param classId 有值表示班级，null表示年级
     * @param studentId
     * @return
     */
    public int getDefeatRate(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId studentId, ObjectId classId) {
        int[] data = getRankAndTotalStu(performanceEntryList, subjectId, studentId, classId);
        int ranking = data[0];
        int totalStu = data[1];
        if(ranking==1) ranking=0;

        return totalStu == 0 ? 100 :(totalStu-ranking)*100/totalStu;
    }

    /**
     * 得到超过对比分数的比率
     * @param performanceEntryList
     * @param subjectId
     * @param classId
     * @param compareScore
     * @return
     */
    public int getRate(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId classId, int compareScore) {
        int student = 0;
        int totalStudent = 0;
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            if(classId == null || performanceEntry.getClassId().equals(classId)) {
                List<Score> scoreList = performanceEntry.getScoreList();
                for (Score score : scoreList) {
                    if(score.getSubjectId().equals(subjectId)) {
                        totalStudent ++;
                        if(score.getSubjectScore() >= compareScore) {
                            student ++;
                        }
                    }
                }
            }
        }
        if(totalStudent == 0)
            return 0;
        return student*100/totalStudent;
    }

    /**
     * 查询某次考试某科目优秀率
     * @param performanceEntryList
     * @param subjectId
     * @param classId 有值表示班级，null表示年级
     * @return
     */
    public int getExcellentRate(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId classId) {
        int excellentScore = 0;
        List<Score> scoreList1 = performanceEntryList.get(0).getScoreList();
        for(Score score : scoreList1) {
            if(score.getSubjectId().equals(subjectId)) {
                excellentScore = score.getFullScore() * 9 / 10;
            }
        }
        return getRate(performanceEntryList, subjectId, classId, excellentScore);
    }

    /**
     * 查询某次考试某科目及格率
     * @param performanceEntryList
     * @param subjectId
     * @param classId 有值表示班级，null表示年级
     * @return
     */
    public int getPassRate(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId classId) {
        int passScore = 0;
        List<Score> scoreList1 = performanceEntryList.get(0).getScoreList();
        for(Score score : scoreList1) {
            if(score.getSubjectId().equals(subjectId)) {
                passScore = score.getFailScore();
            }
        }
        return getRate(performanceEntryList, subjectId, classId, passScore);
    }

    /**
     * 成绩分布
     * @param performanceEntryList
     * @param subjectId
     * @param classId
     * @return
     */
    public List<ScoreDistributionDTO> getScoreDistribution(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId classId) {
        int passScore = 0;
        int fullScore = 0;
        List<Score> scoreList1 = performanceEntryList.get(0).getScoreList();
        for(Score score : scoreList1) {
            if(score.getSubjectId().equals(subjectId)) {
                passScore = score.getFailScore();
                fullScore = score.getFullScore();
            }
        }
        double stuScore = 0;
        int excellentScore = fullScore*9/10;  //优秀分
        int excellentScore_1 = excellentScore - 1;
        int excellentStu = 0;  //优秀学生数
        int goodScore = fullScore*8/10;  //良好分
        int goodScore_1 = goodScore - 1;
        int goodStu = 0;  //良好学生数
        int passScore_1 = passScore - 1;
        int passStu = 0;  //及格学生数
        int failStu = 0;  //不及格学生数
        String excellent = excellentScore + "-" + fullScore + "分";
        String good = goodScore + "-" + excellentScore_1 + "分";
        String pass = passScore + "-" + goodScore_1 + "分";
        String fail = "0-" + passScore_1 + "分";
        for(PerformanceEntry performanceEntry : performanceEntryList) {
            if(performanceEntry.getClassId().equals(classId)) {
                List<Score> scoreList = performanceEntry.getScoreList();
                for(Score score : scoreList) {
                    if(score.getSubjectId().equals(subjectId)) {
                        stuScore = score.getSubjectScore();
                    }
                }
               if(stuScore >= excellentScore) {
                   excellentStu ++;
               } else if(stuScore >= goodScore) {
                   goodStu ++;
               } else if(stuScore >= passScore) {
                   passStu ++;
               } else {
                   failStu ++;
               }
            }
        }
        List<ScoreDistributionDTO> scoreDistributionDTOs = new ArrayList<ScoreDistributionDTO>();
        ScoreDistributionDTO scoreDistributionDTO4 = new ScoreDistributionDTO(fail, failStu);
        scoreDistributionDTOs.add(scoreDistributionDTO4);
        ScoreDistributionDTO scoreDistributionDTO3 = new ScoreDistributionDTO(pass, passStu);
        scoreDistributionDTOs.add(scoreDistributionDTO3);
        ScoreDistributionDTO scoreDistributionDTO2 = new ScoreDistributionDTO(good, goodStu);
        scoreDistributionDTOs.add(scoreDistributionDTO2);
        ScoreDistributionDTO scoreDistributionDTO1 = new ScoreDistributionDTO(excellent, excellentStu);
        scoreDistributionDTOs.add(scoreDistributionDTO1);
        return scoreDistributionDTOs;
    }

    /**
     * 查询某次考试全班同学的成绩及班级排名和年级排名
     * @param performanceEntryList
     * @param classId
     * @return
     */
    public List<StuScoreDTO> getClassRankingList(List<PerformanceEntry> performanceEntryList, ObjectId classId, ObjectId subjectId, Integer fullScore, Integer hundred) {
        List<StuScoreDTO> stuScoreDTOs = new ArrayList<StuScoreDTO>();
        if(performanceEntryList !=null && performanceEntryList.size() > 0) {
            for (PerformanceEntry performanceEntry : performanceEntryList) {
                if (performanceEntry.getClassId().equals(classId)) {
                    StuScoreDTO stuScoreDTO = new StuScoreDTO();
                    stuScoreDTO.setScore(0.0);
                    stuScoreDTO.setStudentName(performanceEntry.getStudentName());
                    List<Score> scoreList = performanceEntry.getScoreList();
                    for (Score score : scoreList) {
                        if (score.getSubjectId().equals(subjectId)) {
                            if (hundred == 1 && score.getObjSubjectScore() != null) {//百分制
                                stuScoreDTO.setScore(score.getObjSubjectScore() * 100 / fullScore);
                            } else {
                                stuScoreDTO.setScore(score.getSubjectScore());
                            }
                        }
                    }
                    stuScoreDTO.setClassRanking(getRanking(performanceEntryList, subjectId, performanceEntry.getStudentId(), classId));
                    stuScoreDTO.setGradeRanking(getRanking(performanceEntryList, subjectId, performanceEntry.getStudentId(), null));
                    stuScoreDTOs.add(stuScoreDTO);
                }
            }
            Collections.sort(stuScoreDTOs);
        }
        return stuScoreDTOs;
    }


    /**
     * 查询某同学一学期的平时成绩，期中成绩，期末成绩
     * @param subjectId
     * @param studentId
     * @return
     */
    public ScoreDTO getStuScoreDTO(ObjectId subjectId, ObjectId studentId, ObjectId classId) {
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntryList = examResultDao.getExamList(null, classId, subjectId, schoolYear, null);
        List<ObjectId> examIdList = MongoUtils.getFieldObjectIDs(examResultEntryList);
        ScoreDTO scoreDTO = new ScoreDTO();
        ObjectId midExamId = null;
        ObjectId endExamId = null;
        if(examResultEntryList != null) {
            for (ExamResultEntry e : examResultEntryList) {
                if(e.getType().equals("期中考试") || e.getType().equals("期中")) {
                    midExamId = e.getID();
                } else if(e.getType().equals("期末考试") || e.getType().equals("期末")) {

                    endExamId = e.getID();
                }
            }
            List<PerformanceEntry> pList = getPerformanceEntryList(null, null, studentId, examIdList);//所有成绩记录
            List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
            scoreDTO.setFinalScore(0.0);
            scoreDTO.setMidtermScore(0.0);
            if(pList != null) {
                for (PerformanceEntry performanceEntry : pList) {
                    if (performanceEntry.getExamId().equals(midExamId)) {
                        List<Score> scoreList = performanceEntry.getScoreList();
                        for (Score score : scoreList) {
                            if (score.getSubjectId().equals(subjectId)) {
                                scoreDTO.setMidtermScore(score.getSubjectScore());
                            }
                        }
                    } else if (performanceEntry.getExamId().equals(endExamId)) {
                        List<Score> scoreList = performanceEntry.getScoreList();
                        for (Score score : scoreList) {
                            if (score.getSubjectId().equals(subjectId)) {
                                scoreDTO.setFinalScore(score.getSubjectScore());
                            }
                        }
                    } else {
                        performanceEntryList.add(performanceEntry);
                    }
                }
            }
            Double usualScore = getAveragePerformance(performanceEntryList, subjectId, classId);
            scoreDTO.setUsualScore(usualScore);

        } else {
            scoreDTO.setMidtermScore(0.0);
            scoreDTO.setFinalScore(0.0);
            scoreDTO.setUsualScore(0.0);
        }
        return scoreDTO;
    }

//    public ScoreDTO getStuScoreDTO(ObjectId subjectId, ObjectId studentId, ObjectId classId) {
//        String schoolYear = getCurrentTerm();
//        List<ExamResultEntry> examResultEntryList = examResultDao.getExamList(null, classId, subjectId, schoolYear, null);
//        ScoreDTO scoreDTO = new ScoreDTO();
//        if(examResultEntryList != null) {
//            List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
//            for (ExamResultEntry e : examResultEntryList) {
//                List<PerformanceEntry> pList = getPerformanceEntryList(e.getID(), null, studentId, null);
//                if(e.getType().equals("期中考试") || e.getType().equals("期中")) {
//                    Double midScore = getAveragePerformance(pList, subjectId, classId);
//                    scoreDTO.setMidtermScore(midScore);
//                } else if(e.getType().equals("期末考试") || e.getType().equals("期末")) {
//                    Double finalScore = getAveragePerformance(pList, subjectId, classId);
//                    scoreDTO.setFinalScore(finalScore);
//                } else if(e.getType().equals("其他")) {
////                    List<PerformanceEntry> perList = getPerformanceEntryList(e.getID(), null, studentId, null);
//                    if(pList != null) {
//                        performanceEntryList.addAll(pList);
//                    }
//                }
//            }
//            Double usualScore = getAveragePerformance(performanceEntryList, subjectId, classId);
//            scoreDTO.setUsualScore(usualScore);
//
//        } else {
//            scoreDTO.setMidtermScore(null);
//            scoreDTO.setFinalScore(null);
//            scoreDTO.setUsualScore(0.0);
//        }
//        return scoreDTO;
//    }

    /**
     * 查询某同学一学期的平均成绩
     * @param subjectId
     * @param studentId
     * @return
     */
    public double getStuSemesterScore(ObjectId subjectId, ObjectId studentId, ObjectId classId) {
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntries = examResultDao.getExamList(null, classId, subjectId, schoolYear, null);
        List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
        if(examResultEntries != null) {
            for (ExamResultEntry e : examResultEntries) {
                List<PerformanceEntry> pList = getPerformanceEntryList(e.getID(), null, studentId, null);
                if(pList != null) {
                    performanceEntryList.addAll(pList);
                }
            }
        }
        if(performanceEntryList == null) {
            return 0;
        }
        return  getAveragePerformance(performanceEntryList, subjectId, null);
    }

    /**
     * 查询某个班的所有考试记录
     * @param classId
     * @return
     */
    public List<ExamResultEntry> getExamList(ObjectId classId, ObjectId subjectId, Integer isGrade) {
        String schoolYear = getCurrentTerm();
        return examResultDao.getExamList(null, classId, subjectId, schoolYear, isGrade);

    }

    /**
     * 某次考试的名称、成绩、班级均分、排名
     * @param subjectId
     * @return
     */
    public SubjectExamDTO getSubjectExamDTO(ExamResultEntry examResultEntry, ObjectId classId, ObjectId subjectId, ObjectId stuId) {
        SubjectExamDTO subjectExamDTO = new SubjectExamDTO();
        subjectExamDTO.setExamName(examResultEntry.getName());
        Double subjectScore = 0D;
        Double classAveScore = 0D;
        Integer classRanking = 0;
        List<PerformanceEntry> performanceEntryList = getPerformanceEntryList(examResultEntry.getID(), classId, null, null);
        if(performanceEntryList!=null && performanceEntryList.size()>0){
            subjectScore = getSubjectScore(performanceEntryList, subjectId, stuId);
            classAveScore = getAveragePerformance(performanceEntryList, subjectId, classId);
            classRanking = getRanking(performanceEntryList, subjectId, stuId, classId);
        }
        subjectExamDTO.setScore(subjectScore);
        subjectExamDTO.setClassAverageScore(classAveScore);
        subjectExamDTO.setClassRanking(classRanking);
        subjectExamDTO.setDate(examResultEntry.getDate());
        return subjectExamDTO;
    }
    /**
     * 查询某个班一学期的平时成绩、期中成绩、期末成绩
     * @param subjectId
     * @param classId
     * @return
     */
    public ScoreDTO getClassScoreDTO(ObjectId subjectId, ObjectId classId) {
        ScoreDTO scoreDTO = new ScoreDTO();
        Double usualScore = 0.0;
        int num = 0;
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntries = examResultDao.getExamList(null, classId, subjectId, schoolYear, null);
        if(examResultEntries != null) {
            for (ExamResultEntry examResultEntry : examResultEntries) {
                List<PerformanceEntry> performanceEntryList = getPerformanceEntryList(examResultEntry.getID(), null, null, null);
                if (examResultEntry.getType().equals("期中考试") || examResultEntry.getType().equals("期中")) {
                    Double midtermScore = getAveragePerformance(performanceEntryList, subjectId, classId);
                    scoreDTO.setMidtermScore(midtermScore);
                } else if (examResultEntry.getType().equals("期末考试") || examResultEntry.getType().equals("期末")) {
                    Double finalScore = getAveragePerformance(performanceEntryList, subjectId, classId);
                    scoreDTO.setFinalScore(finalScore);
                } else {
                    usualScore += getAveragePerformance(performanceEntryList, subjectId, classId);
                    num++;
                }

            }
            if(num == 0) {
                usualScore = null;
            } else {
                usualScore = usualScore / num;
            }
            scoreDTO.setUsualScore(usualScore);
        } else {
            scoreDTO.setMidtermScore(0.0);
            scoreDTO.setFinalScore(0.0);
            scoreDTO.setUsualScore(0.0);
        }

        return  scoreDTO;
    }

    public List<ScoreDTO> getClassScoreDTOByGradeId(ObjectId subjectId, ObjectId gradeId){
        List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>();
        List<ClassEntry> classEntryList=classDao.findClassEntryByGradeId(gradeId);
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntries = examResultDao.getExamList(gradeId, null, subjectId, schoolYear, null);
        List<ObjectId> examIds = MongoUtils.getFieldObjectIDs(examResultEntries);
        List<PerformanceEntry> performanceEntrys = getPerformanceEntryList(null, null, null, examIds);

        List<ObjectId> midtermExamIds = new ArrayList<ObjectId>();
        List<ObjectId> finalTermExamIds = new ArrayList<ObjectId>();
        List<ObjectId> usualTermExamIds  = new ArrayList<ObjectId>();
        if(examResultEntries != null) {
            for (ExamResultEntry examResultEntry : examResultEntries) {
                if (examResultEntry.getType().equals("期中考试") || examResultEntry.getType().equals("期中")) {
                   midtermExamIds.add(examResultEntry.getID());
                } else if (examResultEntry.getType().equals("期末考试") || examResultEntry.getType().equals("期末")) {
                    finalTermExamIds.add(examResultEntry.getID());
                } else {
                    usualTermExamIds.add(examResultEntry.getID());
                }
            }
        }

        Map<String, List<PerformanceEntry>> perMap = getPerMap(performanceEntrys, midtermExamIds, finalTermExamIds);
        Map<ObjectId, Double> midMap = scoreMap(perMap.get("mid"), subjectId);
        Map<ObjectId, Double> finalMap = scoreMap(perMap.get("final"), subjectId);
        Map<ObjectId, Double> usualMap = scoreMap(perMap.get("usual"), subjectId);
        for(ClassEntry classEntry : classEntryList){
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setClassName(classEntry.getName());
            scoreDTO.setMidtermScore(midMap.get(classEntry.getID())==null ? 0.0 : midMap.get(classEntry.getID()));
            scoreDTO.setFinalScore(finalMap.get(classEntry.getID()) == null ? 0.0 : finalMap.get(classEntry.getID()));
            scoreDTO.setMidtermScore(usualMap.get(classEntry.getID())==null ? 0.0 : usualMap.get(classEntry.getID()));
            scoreDTOs.add(scoreDTO);
        }

        return scoreDTOs;
    }

    private Map<String, List<PerformanceEntry>> getPerMap(List<PerformanceEntry> performanceEntries,
                                                          List<ObjectId> midtermExamIds,
                                                          List<ObjectId> finalTermExamIds){
        List<PerformanceEntry> midPerformences = new ArrayList<PerformanceEntry>();
        List<PerformanceEntry> finalPerformances = new ArrayList<PerformanceEntry>();
        List<PerformanceEntry> usualPerformances = new ArrayList<PerformanceEntry>();
        for(PerformanceEntry performanceEntry : performanceEntries){
            if(midtermExamIds.contains(performanceEntry.getExamId())){
                midPerformences.add(performanceEntry);
            } else if(finalTermExamIds.contains(performanceEntry.getExamId())){
                finalPerformances.add(performanceEntry);
            } else {
                usualPerformances.add(performanceEntry);
            }
        }

        Map<String, List<PerformanceEntry>> map = new HashMap<String, List<PerformanceEntry>>();
        map.put("mid", midPerformences);
        map.put("final", finalPerformances);
        map.put("usual", usualPerformances);
        return map;
    }

    private Map<ObjectId, Double> scoreMap(List<PerformanceEntry> performanceEntries, ObjectId subjectId){
        Map<ObjectId, Double> scoreMap = new HashMap<ObjectId, Double>();
        Map<ObjectId, Integer> numMap = new HashMap<ObjectId, Integer>();
        for(PerformanceEntry performanceEntry : performanceEntries){
            List<Score> scoreList = performanceEntry.getScoreList();
            double subjectScore = 0.0;
            for (Score score : scoreList) {
                if (score.getSubjectId().equals(subjectId)) {
                    subjectScore= score.getSubjectScore();
                }
            }

            ObjectId classId = performanceEntry.getClassId();
            if(scoreMap.containsKey(classId)){
                scoreMap.put(classId, scoreMap.get(classId) + subjectScore);
                numMap.put(classId, numMap.get(classId) + 1);
            } else {
                scoreMap.put(classId, subjectScore);
                numMap.put(classId, 1);
            }
        }

        for (Map.Entry<ObjectId, Double> entry : scoreMap.entrySet()) {
            double score = entry.getValue() / numMap.get(entry.getKey());
            entry.setValue(Math.round(score*10)/10.0);
        }
        return scoreMap;
    }

    public List<ScoreDTO> getClassScoreDTOForAllSubject(List<SubjectView> subjectViewList, ObjectId classId){
        List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>();
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntries = examResultDao.getExamList(null, classId, null, schoolYear, null);
        List<ObjectId> examIds = MongoUtils.getFieldObjectIDs(examResultEntries);
        List<PerformanceEntry> performanceEntrys = getPerformanceEntryList(null, classId, null, examIds);

        List<ObjectId> midtermExamIds = new ArrayList<ObjectId>();
        List<ObjectId> finalTermExamIds = new ArrayList<ObjectId>();
        List<ObjectId> usualTermExamIds  = new ArrayList<ObjectId>();
        if(examResultEntries != null) {
            for (ExamResultEntry examResultEntry : examResultEntries) {
                if (examResultEntry.getType().equals("期中考试") || examResultEntry.getType().equals("期中")) {
                    midtermExamIds.add(examResultEntry.getID());
                } else if (examResultEntry.getType().equals("期末考试") || examResultEntry.getType().equals("期末")) {
                    finalTermExamIds.add(examResultEntry.getID());
                } else {
                    usualTermExamIds.add(examResultEntry.getID());
                }
            }
        }

        Map<String, List<PerformanceEntry>> perMap = getPerMap(performanceEntrys, midtermExamIds, finalTermExamIds);
        Map<ObjectId, Double> midMap = scoreMap(perMap.get("mid"));
        Map<ObjectId, Double> finalMap = scoreMap(perMap.get("final"));
        Map<ObjectId, Double> usualMap = scoreMap(perMap.get("usual"));
        for(SubjectView subjectView : subjectViewList){
            ObjectId subjectId = new ObjectId(subjectView.getId());
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setSubjectName(subjectView.getName());
            scoreDTO.setMidtermScore(midMap.get(subjectId)==null ? 0.0 : midMap.get(subjectId));
            scoreDTO.setFinalScore(finalMap.get(subjectId) == null ? 0.0 : finalMap.get(subjectId));
            scoreDTO.setUsualScore(usualMap.get(subjectId) == null ? 0.0 : usualMap.get(subjectId));
            scoreDTOs.add(scoreDTO);
        }

        return scoreDTOs;
    }

    private Map<ObjectId, Double> scoreMap(List<PerformanceEntry> performanceEntries){
        Map<ObjectId, Double> scoreMap = new HashMap<ObjectId, Double>();
        Map<ObjectId, Integer> numMap = new HashMap<ObjectId, Integer>();
        for(PerformanceEntry performanceEntry : performanceEntries){
            List<Score> scoreList = performanceEntry.getScoreList();
            for (Score score : scoreList) {
                ObjectId subjectId = score.getSubjectId();
                if(scoreMap.containsKey(subjectId)){
                    scoreMap.put(subjectId, scoreMap.get(subjectId) + score.getSubjectScore());
                    numMap.put(subjectId, numMap.get(subjectId) + 1);
                } else {
                    scoreMap.put(subjectId, score.getSubjectScore());
                    numMap.put(subjectId, 1);
                }
            }
        }

        for (Map.Entry<ObjectId, Double> entry : scoreMap.entrySet()) {
            double score = entry.getValue() / numMap.get(entry.getKey());
            entry.setValue(Math.round(score*10)/10.0);
        }
        return scoreMap;
    }

    public List<SubjectRateDTO> getSubjectRateDTOList(List<SubjectView> subjectViewList, ObjectId classId){
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntries = examResultDao.getExamList(null, classId, null, schoolYear, null);
        List<ObjectId> examIds = MongoUtils.getFieldObjectIDs(examResultEntries);
        List<PerformanceEntry> performanceEntrys = getPerformanceEntryList(null, classId, null, examIds);

        List<ObjectId> midtermExamIds = new ArrayList<ObjectId>();
        List<ObjectId> finalTermExamIds = new ArrayList<ObjectId>();
        List<ObjectId> usualTermExamIds  = new ArrayList<ObjectId>();
        if(examResultEntries != null) {
            for (ExamResultEntry examResultEntry : examResultEntries) {
                if (examResultEntry.getType().equals("期中考试") || examResultEntry.getType().equals("期中")) {
                    midtermExamIds.add(examResultEntry.getID());
                } else if (examResultEntry.getType().equals("期末考试") || examResultEntry.getType().equals("期末")) {
                    finalTermExamIds.add(examResultEntry.getID());
                } else {
                    usualTermExamIds.add(examResultEntry.getID());
                }
            }
        }

        Map<String, List<PerformanceEntry>> perMap = getPerMap(performanceEntrys, midtermExamIds, finalTermExamIds);
        Map<ObjectId, Long[]> midMap = scoreRateMap(perMap.get("mid"));
        Map<ObjectId, Long[]> finalMap = scoreRateMap(perMap.get("final"));
        Map<ObjectId, Long[]> usualMap = scoreRateMap(perMap.get("usual"));
        List<SubjectRateDTO> subjectRateDTOs = new LinkedList<SubjectRateDTO>();
        for(SubjectView subjectView : subjectViewList){
            ObjectId subjectId = new ObjectId(subjectView.getId());
            SubjectRateDTO subjectRateDTO = new SubjectRateDTO();
            subjectRateDTO.setSubjectName(subjectView.getName());
            Long[] usualRate = usualMap.get(subjectId);
            subjectRateDTO.setAvecer(usualRate == null ? 0L :usualRate[0]);
            subjectRateDTO.setAvecpr(usualRate == null ? 0L :usualRate[1]);
            Long[] midRate = midMap.get(subjectId);
            subjectRateDTO.setMidcer(midRate == null ? 0L :midRate[0]);
            subjectRateDTO.setMidcpr(midRate == null ? 0L :midRate[1]);
            Long[] finalRate = finalMap.get(subjectId);
            subjectRateDTO.setEndcer(finalRate == null ? 0L :finalRate[0]);
            subjectRateDTO.setEndcpr(finalRate == null ? 0L :finalRate[1]);
            subjectRateDTOs.add(subjectRateDTO);
        }

        return subjectRateDTOs;
    }

    private Map<ObjectId, Long[]> scoreRateMap(List<PerformanceEntry> performanceEntries){
        Map<ObjectId, Integer> passMap = new HashMap<ObjectId, Integer>();
        Map<ObjectId, Integer> excelentMap = new HashMap<ObjectId, Integer>();
        Map<ObjectId, Integer> numMap = new HashMap<ObjectId, Integer>();
        for(PerformanceEntry performanceEntry : performanceEntries){
            List<Score> scoreList = performanceEntry.getScoreList();
            for (Score score : scoreList) {
                ObjectId subjectId = score.getSubjectId();
                double subjectScore = score.getSubjectScore();
                if(numMap.containsKey(subjectId)){
                    numMap.put(subjectId, numMap.get(subjectId) + 1);
                    if(subjectScore > 90){
                        excelentMap.put(subjectId, excelentMap.get(subjectId) + 1);
                    }
                    if(subjectScore > 90){
                        passMap.put(subjectId, passMap.get(subjectId) + 1);
                    }
                } else {
                    numMap.put(subjectId, 1);
                    if(subjectScore > 90){
                        excelentMap.put(subjectId, 1);
                    } else {
                        excelentMap.put(subjectId, 0);
                    }
                    if(subjectScore > 90){
                        passMap.put(subjectId, 1);
                    } else {
                        passMap.put(subjectId, 0);
                    }

                }
            }
        }

        Map<ObjectId, Long[]> retMap = new HashMap<ObjectId, Long[]>();
        for (Map.Entry<ObjectId, Integer> entry : numMap.entrySet()) {
            ObjectId subjectId = entry.getKey();
            Long[] rate = new Long[2];
            long excelentRate = excelentMap.get(subjectId) * 100/ entry.getValue();
            long passRate = passMap.get(subjectId) * 100 / entry.getValue();
            rate[0] = excelentRate;
            rate[1] =passRate;
            retMap.put(subjectId, rate);
        }
        return retMap;
    }

    /**
     * 查询某个年级一学期的平均成绩
     * @param subjectId
     * @param gradeId
     * @return
     */
    public double getGradeSemesterScore(ObjectId subjectId, ObjectId gradeId) {
        List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntries = examResultDao.getExamList(gradeId, null, subjectId, schoolYear, null);
        if (examResultEntries != null){
            for (ExamResultEntry examResultEntry : examResultEntries) {
                List<PerformanceEntry> pList = getPerformanceEntryList(examResultEntry.getID(), null, null, null);
                if(pList != null) {
                    performanceEntryList.addAll(pList);
                }
            }
        }
        return  getAveragePerformance(performanceEntryList, subjectId, null);
    }


    /**
     * 打分进度
     * @param performanceEntryList
     * @return
     */
    public Integer getRateSchedule(List<PerformanceEntry> performanceEntryList, ObjectId subjectId, ObjectId classId) {
        int num = 0;
        if(performanceEntryList!=null && performanceEntryList.size() > 0) {
            for (PerformanceEntry performanceEntry : performanceEntryList) {
                if (performanceEntry.getClassId().equals(classId)) {
                    List<Score> scoreList = performanceEntry.getScoreList();
                    for (Score score : scoreList) {
                        if (score.getSubjectId().equals(subjectId)) {
                            if (score.getAbsence() != 0 || score.getObjSubjectScore() != null || score.getExemption() != 0) {
                                num++;
                            }
                        }
                    }
                }
            }
        }
        return  num;
    }

    /**
     * 老师删除考试
     * @param examId
     * @return
     */
    public boolean teacherDeleteExam(ObjectId examId, ObjectId classId) {
        ExamResultEntry examResultEntry = examResultDao.getExamResultEntry(examId);
        Map<ObjectId, PerformanceEntry> performanceEntryMap = getPerformanceEntryMap(examId, classId, null, null);
        if(performanceEntryMap.size() > 0) {
            List<ObjectId> performanceList = new ArrayList<ObjectId>(performanceEntryMap.keySet());
            int classSize = examResultEntry.getClassList().size();
            PerformanceEntry per = performanceDao.getPerformanceById(performanceList.get(0));
            int scoreSize = per.getScoreList().size();
            if (1 == classSize) {//单班级考试
                if (1 == scoreSize) {//单学科
                    examResultDao.delete(examId);
                }

            } else {//多班级考试
                if (1 == scoreSize) {//单学科
                    examResultDao.deleteClass(examId, classId);
                }

            }
        } else {
            examResultDao.delete(examId);
        }

        return true;
    }

    /**
     * 老师编辑考试
     * @return
     */
    public boolean teacherEditExam(ObjectId subjectId, ExamResultEntry examResultEntry,Integer fullScore, Integer failScore, boolean isGrade) {
        ObjectId examId = examResultEntry.getID();
        if(!isGrade) {//班级考试
            //删除原来的performanceList
            List<PerformanceEntry> performanceEntryList = getPerformanceEntryList(examId, null, null, null);
            if(performanceEntryList.get(0).getExamId() == null) {//向下兼容
                for(PerformanceEntry p : performanceEntryList) {
                    performanceDao.delete(p.getId());
                }
            } else {
                performanceDao.deleteByExamId(examId);
            }
            //添加新的performanceList
            List<ObjectId> subjectList = new ArrayList<ObjectId>();
            subjectList.add(subjectId);
            buildExam(examResultEntry, subjectList, fullScore, failScore);
            examResultDao.updateInfo(examId, examResultEntry);
        } else {
            performanceDao.updateFullFailScoreByExamId(examId, subjectId, fullScore, failScore);
        }
        return true;
    }

    /**
     * 用于更新
     * @param examId
     * @param subjectId
     * @param fullScore
     * @param failScore
     */
    public void editFullFailScore(ObjectId examId, ObjectId subjectId, Integer fullScore, Integer failScore) {
        performanceDao.updateFullFailScoreByExamId(examId, subjectId, fullScore, failScore);
    }

    /**
     * 管理员删除考试
     * @param examId
     * @return
     */
    public boolean managerDeleteExam(ObjectId examId) {
        List<PerformanceEntry> performanceEntryList = getPerformanceEntryList(examId, null, null, null);
        examResultDao.delete(examId);
        if(performanceEntryList.get(0).getExamId() == null) {//向下兼容
            for(PerformanceEntry p : performanceEntryList) {
                performanceDao.delete(p.getId());
            }
        } else {
            performanceDao.deleteByExamId(examId);
        }
        return true;
    }


    /**
     * 获取班级的平时、期中、期末的合格率、优秀率
     * @param classId
     * @param subjectId
     * @param totalStu
     * @param passScore
     * @param excellentScore
     * @return
     */
    public SubjectRateDTO getRate(ObjectId classId, ObjectId subjectId, int totalStu, int passScore, int excellentScore){
        SubjectRateDTO subjectRateDTO = new SubjectRateDTO();
        String schoolYear = getCurrentTerm();
        List<ExamResultEntry> examResultEntryList = examResultDao.getExamList(null, classId, subjectId, schoolYear, null);
        if(examResultEntryList != null) {
            List<ObjectId> examList = new ArrayList<ObjectId>();
            for (ExamResultEntry e : examResultEntryList) {
                if(e.getType().equals("期中考试") || e.getType().equals("期中")) {
                    int pass = performanceDao.findPassCount(e.getID(), subjectId, passScore);
                    int exce = performanceDao.findPassCount(e.getID(), subjectId, excellentScore);
                    subjectRateDTO.setMidcer(Math.round(exce * 100.0 / totalStu));
                    subjectRateDTO.setMidcpr(Math.round(pass * 100.0 / totalStu));
                } else if(e.getType().equals("期末考试") || e.getType().equals("期末")) {
                    int pass = performanceDao.findPassCount(e.getID(), subjectId, passScore);
                    int exce = performanceDao.findPassCount(e.getID(), subjectId, excellentScore);
                    subjectRateDTO.setEndcer(Math.round(exce * 100.0 / totalStu));
                    subjectRateDTO.setEndcpr(Math.round(pass * 100.0 / totalStu));
                } else if(e.getType().equals("其他")) {
                    examList.add(e.getID());
                }
            }
            int pass = performanceDao.findPassCount(examList, subjectId, passScore);
            int exce = performanceDao.findPassCount(examList, subjectId, excellentScore);
            subjectRateDTO.setAvecer(Math.round(exce * 100.0 / totalStu/examList.size()));
            subjectRateDTO.setAvecpr(Math.round(pass * 100.0 / totalStu/examList.size()));
        } else {
            subjectRateDTO.setAvecer(0);
            subjectRateDTO.setAvecpr(0);
            subjectRateDTO.setMidcer(0);
            subjectRateDTO.setMidcpr(0);
            subjectRateDTO.setEndcer(0);
            subjectRateDTO.setEndcpr(0);
        }
        return subjectRateDTO;
    }

//=========================和考试整合=======================================
    /**
     * 得到exercise关联的examResultEntry
     * @param eid
     * @return
     */
    public ExamResultEntry getExamResultEntryByEid(ObjectId eid) {
        return examResultDao.getExamResultEntryByEid(eid);
    }

    /**
     * 练习增加推送班级时，相应地增加performance
     * @param eid
     * @param classId
     * @return
     */
    public boolean addClassPerformance(ObjectId eid, ObjectId classId) {
        ExamResultEntry examResultEntry = getExamResultEntryByEid(eid);
        if(examResultEntry != null) {
            List<PerformanceEntry> performanceEntryList = getPerformanceEntryList(examResultEntry.getID(), null, null, null);
            List<Score> scoreList = performanceEntryList.get(0).getScoreList();
            scoreList.get(0).setSubjectScore(null);
            List<PerformanceEntry> pList = new ArrayList<PerformanceEntry>();
            ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
            List<ObjectId> students = classEntry.getStudents();
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(students, Constant.FIELDS);
            for (ObjectId stu : students) {
                UserEntry student = userEntryMap.get(stu);
                String studentName = student.getUserName();
                PerformanceEntry performanceEntry = new PerformanceEntry(examResultEntry.getID(), stu, studentName, classId, scoreList);
                pList.add(performanceEntry);
            }
            performanceDao.save(pList);
        }
        return true;
    }

    /**
     * 更新学生成绩
     * @param eid
     * @param stuId
     * @param score
     */
    public void updateStuScore(ObjectId eid, ObjectId stuId, Double score) {
        ExamResultEntry examResultEntry = getExamResultEntryByEid(eid);
        if(examResultEntry != null) {
            ObjectId examId = examResultEntry.getID();
            performanceDao.updateScore(examId, stuId, score);
        }
    }

    //==============================================区域联考=================================================================

    public Map<String, Object> getJointExam(ObjectId jointExamId){
        Map<String, Object> map = new HashMap<String, Object>();
        RegionalExamEntry regionalExamEntry = jointExamDao.find(jointExamId);
        map.put("regionalExam", new RegionalExamDTO(regionalExamEntry));

        List<RegionalSchItem> schItems = regionalExamEntry.getSchool();
        List<ObjectId> tjSchoolIds = new ArrayList<ObjectId>();
        if(schItems!=null){
            for(RegionalSchItem schItem : schItems){
                if(schItem.getFlag() == 1){
                    tjSchoolIds.add(schItem.getSchoolId());
                }
            }
        }
        List<ExamSummaryEntry> examSummaryEntryList = examSummaryDao.findExamSummaryList(jointExamId);
        List<ExamSummaryDTO> dtoList = new ArrayList<ExamSummaryDTO>();
        if(examSummaryEntryList!=null && examSummaryEntryList.size()>0) {
            for (ExamSummaryEntry examSummaryEntry : examSummaryEntryList) {
                if(tjSchoolIds.contains(examSummaryEntry.getSchoolId())){
                    dtoList.add(new ExamSummaryDTO(examSummaryEntry));
                }
            }
        }
        map.put("examSummaryDTOList", dtoList);
        List<String> subs = new ArrayList<String>();
        List<RegionalSubjectItem> regionalSubjectItems = regionalExamEntry.getExamSubject();
        for(RegionalSubjectItem item : regionalSubjectItems){
            subs.add(item.getName());
        }
        map.put("subs", subs);
        if(dtoList.size() > 0){
            map.put("summary",getSummary(dtoList));
        }


        double totalScore = 0;
        List<RegionalSubjectItem> subjectItemList = regionalExamEntry.getExamSubject();
        for(RegionalSubjectItem item : subjectItemList){
            totalScore += item.getFull();
        }
        map.put("totalScore", totalScore);
        return map;
    }

    private ExamSummaryDTO getSummary(List<ExamSummaryDTO> dtoList){
        ExamSummaryDTO examSummaryDTO = new ExamSummaryDTO();
        examSummaryDTO.setSchoolName("总平均");
        List<SubjectDetailsDTO> subList = new ArrayList<SubjectDetailsDTO>();
        int size = dtoList.get(0).getSubjectDetailsDTOList().size();//学科总数
        for(int i=0; i<size; i++){
            SubjectDetailsDTO dto = new SubjectDetailsDTO();
            dto.setMinScore(1000);
            subList.add(dto);
        }
        examSummaryDTO.setSubjectDetailsDTOList(subList);
        for(ExamSummaryDTO dto : dtoList){
            examSummaryDTO.setStudentNum(examSummaryDTO.getStudentNum() + dto.getStudentNum());
            List<SubjectDetailsDTO> subjectDetailsDTOList = dto.getSubjectDetailsDTOList();
            for(int i=0; i<size; i++){
                SubjectDetailsDTO subDto = subList.get(i);
                SubjectDetailsDTO subjectDetailsDTO = subjectDetailsDTOList.get(i);
                subDto.setAverageScore(subDto.getAverageScore() + subjectDetailsDTO.getAverageScore());
                if(subjectDetailsDTO.getMaxScore() > subDto.getMaxScore()){
                    subDto.setMaxScore(subjectDetailsDTO.getMaxScore());
                }
                if(subjectDetailsDTO.getMinScore() < subDto.getMinScore()){
                    subDto.setMinScore(subjectDetailsDTO.getMinScore());
                }
                subDto.setPassNum(subDto.getPassNum() + subjectDetailsDTO.getPassNum());
                subDto.setExcellentNumber(subDto.getExcellentNumber() + subjectDetailsDTO.getExcellentNumber());
            }
        }
        for(SubjectDetailsDTO subjectDetailsDTO : subList){
            double averageScore = subjectDetailsDTO.getAverageScore() / dtoList.size();
            subjectDetailsDTO.setAverageScore(Math.round(averageScore*100)/100.0);
            if(examSummaryDTO.getStudentNum() == 0){
                subjectDetailsDTO.setExcellentRate(0);
                subjectDetailsDTO.setPassRate(0);
            } else {
                double excellentRate = subjectDetailsDTO.getExcellentNumber()*1D / examSummaryDTO.getStudentNum();
                subjectDetailsDTO.setExcellentRate(Math.round(excellentRate*100)/100.0);
                double passRate = subjectDetailsDTO.getPassNum()*1D / examSummaryDTO.getStudentNum();
                subjectDetailsDTO.setPassRate(Math.round(passRate * 100) / 100.0);
            }
        }
        //计算比均和超均
        double biJun;
        double chaoJun;
        for(int i=0; i<size; i++){
            SubjectDetailsDTO summary = examSummaryDTO.getSubjectDetailsDTOList().get(i);
            double sumAvgScore = summary.getAverageScore();
            for(ExamSummaryDTO dto : dtoList){
                SubjectDetailsDTO sub = dto.getSubjectDetailsDTOList().get(i);
                biJun = sub.getAverageScore() / sumAvgScore;
                biJun = Math.round(biJun * 100)/100.0;
                sub.setBiJun(biJun);
                chaoJun = Math.round((biJun-1) * 100)/100.0;
                sub.setChaoJun(chaoJun);
            }
        }

        return examSummaryDTO;
    }

    /**
     * 学生成绩汇总表
     * @param areaExamId
     * @param skip
     * @param pageSize
     * @return
     */
    public List<Map<String, Object>> getAreaStudentScore(ObjectId areaExamId, int skip, int pageSize){
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        //构建学校考试id--学校名称map
        Map<ObjectId, String> schIdNameMap = new HashMap<ObjectId, String>();
        RegionalExamEntry regionalExamEntry = jointExamDao.find(areaExamId);
        List<RegionalSchItem> schItemList = regionalExamEntry.getSchool();
        if(schItemList!=null && schItemList.size()>0){
            for(RegionalSchItem item : schItemList){
                schIdNameMap.put(item.getExamId(), item.getName());
            }
        }
        //构建科目满分映射
        List<RegionalSubjectItem> subjectItems = regionalExamEntry.getExamSubject();
        Map<ObjectId, Double> subjectFullScoreMap = new HashMap<ObjectId, Double>();
        double examFullScore = 0;
        for(RegionalSubjectItem subjectItem : subjectItems){
            examFullScore += subjectItem.getFull();
            subjectFullScoreMap.put(subjectItem.getSubjectId(), subjectItem.getFull());
        }

        List<NameValuePair> gradeSetting = regionalExamEntry.getGradeSetting();

        List<PerformanceEntry> performanceEntryList = performanceDao.findPerformanceListByAreaId(areaExamId, skip, pageSize);
        if(performanceEntryList!=null && performanceEntryList.size()>0){
            for(PerformanceEntry performanceEntry : performanceEntryList){
                Map<String, Object> performanceMap = new HashMap<String, Object>();
                performanceMap.put("areaRanking", performanceEntry.getAreaRanking());
                performanceMap.put("schRanking", performanceEntry.getSchoolRanking());
                performanceMap.put("stuName", performanceEntry.getStudentName());
                performanceMap.put("schName", schIdNameMap.get(performanceEntry.getExamId()));
                String scoreSumAndGrade = bulidScoreAndGrade(performanceEntry.getScoreSum(), gradeSetting, examFullScore);
                performanceMap.put("scoreSum", scoreSumAndGrade);
                List<IdNameValuePairDTO> subjectScoreList = new ArrayList<IdNameValuePairDTO>();
                List<Score> scoreList = performanceEntry.getScoreList();
                if(scoreList!=null && scoreList.size()>0){
                    for(Score score : scoreList){
                        String scoreAndGrade = bulidScoreAndGrade(score.getSubjectScore(), gradeSetting, subjectFullScoreMap.get(score.getSubjectId()));
                        IdNameValuePairDTO dto = new IdNameValuePairDTO(null, score.getSubjectName(), scoreAndGrade);
                        subjectScoreList.add(dto);
                    }
                }
                performanceMap.put("subScore", subjectScoreList);
                retList.add(performanceMap);
            }
        }
        return retList;
    }

    private String bulidScoreAndGrade(double score, List<NameValuePair> gradeSettings, double fullScore){
        String grade = "";
        for(NameValuePair gradeSetting : gradeSettings){
            double gradeScore = (Integer)gradeSetting.getValue() * fullScore / 100;
            if(score >= gradeScore){
                grade = gradeSetting.getName();
                break;
            }
        }

        return score + "(" + grade + ")";
    }

    /**
     * 区域联考总人数
     * @param areaExamId
     * @return
     */
    public int findPerformanceCountByAreaId(ObjectId areaExamId){
        return performanceDao.findPerformanceCountByAreaId(areaExamId);
    }

    /**
     * 查询成绩分布
     * @param jointExamId
     * @return
     */
    public List<Map<String, Object>> getScoreDistribution(ObjectId jointExamId){
        List<Map<String, Object>> model = new ArrayList<Map<String, Object>>();
        RegionalExamEntry regionalExamEntry = jointExamDao.find(jointExamId);
        List<ScoreSection> scoreSectionList = regionalExamEntry.getScoreSection();
        if(null != scoreSectionList && scoreSectionList.size()>0){
            List<RegionalSchItem> schoolItemList = regionalExamEntry.getSchool();
            if(null != schoolItemList && schoolItemList.size()>0){
                for(RegionalSchItem schItem : schoolItemList){
                    if(schItem.getFlag() == 1) {
                        Map<String, Object> map = buildSchScoreSection(schItem, scoreSectionList);
                        model.add(map);
                    }
                }

            }
        } else {
            return model;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        double maxScore = 0d;
        List<ScoreSectionDTO> scoreSectionDTOs = new ArrayList<ScoreSectionDTO>();
        for(ScoreSection scoreSection : scoreSectionList){
            ScoreSectionDTO scoreSectionDTO = new ScoreSectionDTO(scoreSection);
            scoreSectionDTO.setNum(0);
            scoreSectionDTOs.add(scoreSectionDTO);
        }

        List<RegionalSchItem> schItems = regionalExamEntry.getSchool();
        List<ObjectId> examIds = new ArrayList<ObjectId>();
        if(schItems!=null){
            for(RegionalSchItem schItem : schItems){
                if(schItem.getFlag() == 1){
                    examIds.add(schItem.getExamId());
                }
            }
        }
        List<PerformanceEntry> performanceEntryList = performanceDao.findPerformanceListByAreaId(jointExamId,examIds, new BasicDBObject("suc",1));
        if(null != performanceEntryList && performanceEntryList.size() > 0){
            for(PerformanceEntry entry : performanceEntryList){
                double score = entry.getScoreSum();
                if(score > maxScore){
                    maxScore = score;
                }
                for(ScoreSectionDTO scoreSection : scoreSectionDTOs){
                    if(score>=scoreSection.getBeginScore() && score<=scoreSection.getEndScore()){
                        scoreSection.setNum(scoreSection.getNum() + 1);
                    }
                }
            }
        }
        map.put("schoolName", "总计");
        map.put("maxScore",maxScore);
        map.put("scoreSection", scoreSectionDTOs);
        model.add(map);

        return model;
    }

    private Map<String, Object> buildSchScoreSection(RegionalSchItem regionalSchItem, List<ScoreSection> scoreSectionList){
        Map<String, Object> map = new HashMap<String, Object>();
        double maxScore = 0d;
        List<ScoreSectionDTO> scoreSectionDTOs = new ArrayList<ScoreSectionDTO>();
        for(ScoreSection scoreSection : scoreSectionList){
            ScoreSectionDTO scoreSectionDTO = new ScoreSectionDTO(scoreSection);
            scoreSectionDTO.setNum(0);
            scoreSectionDTOs.add(scoreSectionDTO);
        }
        List<PerformanceEntry> performanceEntryList = performanceDao.findPerformanceList(regionalSchItem.getExamId());
        if(null != performanceEntryList && performanceEntryList.size() > 0){
            for(PerformanceEntry entry : performanceEntryList){
                double score = entry.getScoreSum();
                if(score > maxScore){
                    maxScore = score;
                }
                for(ScoreSectionDTO scoreSection : scoreSectionDTOs){
                    if(score>=scoreSection.getBeginScore() && score<=scoreSection.getEndScore()){
                        scoreSection.setNum(scoreSection.getNum() + 1);
                    }
                }
            }
        }
        map.put("schoolName", regionalSchItem.getName());
        map.put("maxScore",maxScore);
        map.put("scoreSection", scoreSectionDTOs);
        return map;
    }

    /**
     * 更新分数段
     * @param jointExamId
     * @param scoreSections
     */
    public void updateScoreSection(ObjectId jointExamId, List<ScoreSection> scoreSections){
        RegionalExamEntry regionalExamEntry = jointExamDao.find(jointExamId);
        regionalExamEntry.setScoreSection(scoreSections);
        jointExamDao.update(regionalExamEntry);
    }

    public void rankScore(ObjectId jointExamId){
        //============================examSummaryEntry 排序 ========================
        List<ExamSummaryEntry> examSummaryEntryList = examSummaryDao.findExamSummaryList(jointExamId);
        //排总评
        Collections.sort(examSummaryEntryList);
        Collections.reverse(examSummaryEntryList);
        for(int i=0; i<examSummaryEntryList.size(); i++){
            examSummaryEntryList.get(i).setCompositeRanking(i+1);
        }
        //分科排序
        int size = examSummaryEntryList.get(0).getSubjectDetails().size();
        for(int i=0; i<size; i++) {
            List<SubjectDetails> subjectDetailsList = new ArrayList<SubjectDetails>();
            for (ExamSummaryEntry examSummaryEntry : examSummaryEntryList) {
                subjectDetailsList.add(examSummaryEntry.getSubjectDetails().get(i));
            }
            Collections.sort(subjectDetailsList);
            Collections.reverse(subjectDetailsList);
            for(int j=0; j<subjectDetailsList.size(); j++){
                subjectDetailsList.get(j).setCompositeRanking(j+1);
            }
        }
        //持久化
        for(ExamSummaryEntry examSummaryEntry : examSummaryEntryList){
            examSummaryDao.update(examSummaryEntry);
        }

        //=========================performanceEntry 排序 =========================
        List<PerformanceEntry> performanceEntryList = performanceDao.findPerformanceListByAreaId(jointExamId,null, Constant.FIELDS);
        if(performanceEntryList!=null && performanceEntryList.size()>0){
            Collections.sort(performanceEntryList);
            Collections.reverse(performanceEntryList);
            double lastPerformance = 0D;
            int lastRank = 1;
            for(int i=0; i<performanceEntryList.size(); i++){
                PerformanceEntry performanceEntry = performanceEntryList.get(i);
                if(lastPerformance == performanceEntry.getScoreSum()){
                    performanceDao.updateAreaRanking(performanceEntry.getId(),lastRank);
                } else {
                    lastRank = i + 1;
                    performanceDao.updateAreaRanking(performanceEntry.getId(),lastRank);
                }
                lastPerformance = performanceEntry.getScoreSum();
            }
            //更新排名状态
            jointExamDao.update(jointExamId, new BasicDBObject("rf",1));
        }

    }

    /**
     * 更新等级
     * @param jointExamId
     * @param grades
     */
    public void updateGrades(ObjectId jointExamId, List<NameValuePair> grades){
        RegionalExamEntry regionalExamEntry = jointExamDao.find(jointExamId);
        regionalExamEntry.setGradeSetting(grades);
        jointExamDao.update(regionalExamEntry);
    }

    public List<Map<String, Object>> getSubjectDistributions(ObjectId jointExamId) throws Exception{
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        RegionalExamEntry regionalExamEntry = jointExamDao.find(jointExamId);
        List<NameValuePair> gradeSetting = regionalExamEntry.getGradeSetting();
        if(gradeSetting.size() <= 0){
            throw new Exception("还未设置等级，请先设置等级");
        }

        List<RegionalSchItem> schItems = regionalExamEntry.getSchool();
        List<ObjectId> examIds = new ArrayList<ObjectId>();//已提交的学校
        if(schItems!=null){
            for(RegionalSchItem schItem : schItems){
                if(schItem.getFlag() == 1){
                    examIds.add(schItem.getExamId());
                }
            }
        }
        List<PerformanceEntry> performanceEntryList = performanceDao.findPerformanceListByAreaId(jointExamId,examIds, Constant.FIELDS);
        if(null != performanceEntryList && performanceEntryList.size() > 0){
            List<RegionalSubjectItem> subjectItems = regionalExamEntry.getExamSubject();
            List<ObjectId> subjectIds = new ArrayList<ObjectId>();
            for(RegionalSubjectItem subjectItem : subjectItems){
                subjectIds.add(subjectItem.getSubjectId());
            }

            Map<ObjectId, List<Double>> subjectScoresMap = formateSubjectScoresMap(performanceEntryList, subjectIds);

            for(RegionalSubjectItem subjectItem : subjectItems){
                List<Double> scores = subjectScoresMap.get(subjectItem.getSubjectId());
                Map<String, Object> map = formateSubjectDistribution(subjectItem, scores, gradeSetting);
                list.add(map);
            }

        }

        return list;
    }

    private Map<ObjectId, List<Double>> formateSubjectScoresMap(List<PerformanceEntry> performanceEntries, List<ObjectId> subjectIds){
        Map<ObjectId, List<Double>> map = new LinkedHashMap<ObjectId, List<Double>>();
        for(ObjectId subjectId : subjectIds){
            map.put(subjectId, new ArrayList<Double>());
        }

        ObjectId subjectId;
        for(PerformanceEntry performanceEntry : performanceEntries){
            List<Score> scores = performanceEntry.getScoreList();
            for(Score score : scores){
                if(score.getAbsence()==0 && score.getExemption()==0){
                    subjectId = score.getSubjectId();
                    map.get(subjectId).add(score.getSubjectScore());
                }
            }
        }
        return map;
    }

    private Map<String, Object> formateSubjectDistribution(RegionalSubjectItem subjectItem, List<Double> scores, List<NameValuePair> gradeSetting){
        Map<String, Object> map = new HashMap<String, Object>();
        int stuCount = scores.size();
        double totalScore = 0;
        double avgScore = 0;
        int passNum = 0;
        double passRate = 0;
        List<GradeSection> gradeDistributions = new ArrayList<GradeSection>(gradeSetting.size());
        for(NameValuePair grade : gradeSetting){
            GradeSection dto = new GradeSection(grade.getName(), (Integer)grade.getValue(), (Integer)grade.getValue() * 1.0/100*subjectItem.getFull(), 0, 0);
            gradeDistributions.add(dto);
        }


        for(Double score : scores){
            totalScore += score;
            if(score >= subjectItem.getPass())
                passNum++;
            for(GradeSection grade : gradeDistributions){
                if(score >= grade.getMinScore()){
                    grade.countInc();
                    break;
                }
            }
        }

        avgScore = Math.round(totalScore / stuCount * 100) / 100.0;
        passRate = Math.round(passNum * 1.0 / stuCount * 10000) / 100.0;

        for(GradeSection grade : gradeDistributions){//计算比率
            double rate = Math.round(grade.getCount() * 10000.0 / stuCount) / 100.0;
            grade.setRate(rate);
        }

        map.put("subNm", subjectItem.getName());
        map.put("stuCount", stuCount);
        map.put("totalScore", totalScore);
        map.put("avgScore", avgScore);
        map.put("passNum", passNum);
        map.put("passRate", passRate);
        map.put("grades", gradeDistributions);

        return map;
    }



}
