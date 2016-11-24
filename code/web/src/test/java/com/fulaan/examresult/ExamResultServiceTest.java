package com.fulaan.examresult;

import com.db.examresult.PerformanceDao;
import com.db.factory.MongoFacroty;
import com.fulaan.examresult.service.ExamResultService;
import com.pojo.examresult.*;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fl on 2015/6/18.
 */

/**
 * 测试数据
 * examId : 55836bb32dacba2c73f55bcd
 * name:期末考试      第一单元练习
 * type:期末考试
 * schoolId:657994077f727a970e6aff05（一中）   657994077f727a970e6aff06（二中）
 * gradeId : 657994077f727a971e6aff06
 * classList：考试班级列表
 *     [
 *      {
 *          657994077f727a970e6aff07  三年级一班
 *          657994077f727a970e6aff08  三年级二班
 *       }
 *     ]
 *     date：考试日期   2015年6月19号 9:00
 *     fullScore：满分  100
 *     failScore：及格分  60
 *     performanceList:学生成绩
 *     [
 *     {
 *         studentId:学生的id 657994077f727a970e6aff10~9
 *         name：学生的姓名   Tom0~9
 *         classId:班级的Id   657994077f727a970e6aff07
 *         score：学生的成绩
 *         [
 *          {
 *              subjectId:考试科目   657994077f727a970e6aff20 语文   657994077f727a970e6aff21 数学
 *              subjectScore:考试成绩
 *              absence:缺考
 *              exemption:免考
 *           }
 *         ]
 *     }
 *     ]
 *     schoolYear:学年  2014-2015学年第二学期
 *     isGradeExam:是否为年级统一考试
 *     subjectList:考试科目
 *     [
 *     {}
 *     ]
 *
 *
 */
public class ExamResultServiceTest {
    ExamResultService examResultService = new ExamResultService();
//    PerformanceDao performanceDao = new PerformanceDao();
//    ObjectId examId = new ObjectId("558cb7e22dac8e9187c8a540");
//    ObjectId classId = new ObjectId("657994077f727a970e6aff07");
//    ObjectId subjectId = new ObjectId("657994077f727a970e6aff21");
//    ObjectId studentId = new ObjectId("657994077f727a970e6aff14");

    /**
     * 添加考试记录
     */
//    @Test
//    public void addExam() {
//        List<Score> scoreList = new ArrayList<Score>();
//        Score score = new Score(new ObjectId("657994077f727a970e6aff20"),"语文", null, null, null, 100, 60);
//        scoreList.add(score);
//        Score score1 = new Score(new ObjectId("657994077f727a970e6aff21"),"数学", null, null, null, 100, 60);
//        scoreList.add(score1);
//        List<ObjectId> performanceList = new ArrayList<ObjectId>();
//        PerformanceEntry performanceEntry0 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff10"),"Tom0", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry0));
//        PerformanceEntry performanceEntry1 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff11"),"Tom1", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry1));
//        PerformanceEntry performanceEntry2 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff12"),"Tom2", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry2));
//        PerformanceEntry performanceEntry3 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff13"),"Tom3", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry3));
//        PerformanceEntry performanceEntry4 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff14"),"Tom4", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry4));
//        PerformanceEntry performanceEntry5 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff15"),"Tom5", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry5));
//        PerformanceEntry performanceEntry6 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff16"),"Tom6", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry6));
//        PerformanceEntry performanceEntry7 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff17"),"Tom7", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry7));
//        PerformanceEntry performanceEntry8 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff18"),"Tom8", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry8));
//        PerformanceEntry performanceEntry9 = new PerformanceEntry(examId,new ObjectId("657994077f727a970e6aff19"),"Tom9", new ObjectId("657994077f727a970e6aff07"), scoreList);
//        performanceList.add(performanceDao.save(performanceEntry9));
//
//        List<ObjectId> classList = new ArrayList<ObjectId>();
//        classList.add(new ObjectId("657994077f727a970e6aff07"));
//        classList.add(new ObjectId("657994077f727a970e6aff08"));
//        List<ObjectId> subjectList = new ArrayList<ObjectId>();
//        subjectList.add(new ObjectId("657994077f727a970e6aff20"));
//        subjectList.add(new ObjectId("657994077f727a970e6aff21"));
//        ExamResultEntry examResultEntry = new ExamResultEntry();
//        examResultEntry.setPerformanceList(performanceList);
//        examResultEntry.setDate("2015年6月19号 9:00");
//        examResultEntry.setGradeId(new ObjectId("657994077f727a971e6aff06"));
//        examResultEntry.setIsGradeExam(1);
//        examResultEntry.setType("期末考试");
//        examResultEntry.setName("期末考试");
//        examResultEntry.setClassList(classList);
//        examResultEntry.setSubjectList(subjectList);
//        examResultEntry.setSchoolId(new ObjectId("657994077f727a970e6aff05"));
//        examResultEntry.setSchoolYear("2014-2015学年第二学期");
////        examResultService.addExam(examResultEntry);
//        System.out.println(MongoFacroty.getAppDB());
//    }

    /**
     * 考试评分
     */
//    @Test
//    public void addStudentScore() {
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a536"), new ObjectId("657994077f727a970e6aff20"), 59, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a536"), new ObjectId("657994077f727a970e6aff21"), 49, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a537"), new ObjectId("657994077f727a970e6aff20"), 65, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a537"), new ObjectId("657994077f727a970e6aff21"), 62, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a538"), new ObjectId("657994077f727a970e6aff20"), 73, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a538"), new ObjectId("657994077f727a970e6aff21"), 76, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a539"), new ObjectId("657994077f727a970e6aff20"), 85, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a539"), new ObjectId("657994077f727a970e6aff21"), 86, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53a"), new ObjectId("657994077f727a970e6aff20"), 96, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53a"), new ObjectId("657994077f727a970e6aff21"), 97, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53b"), new ObjectId("657994077f727a970e6aff20"), 94, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53b"), new ObjectId("657994077f727a970e6aff21"), 93, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53c"), new ObjectId("657994077f727a970e6aff20"), 82, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53c"), new ObjectId("657994077f727a970e6aff21"), 87, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53d"), new ObjectId("657994077f727a970e6aff20"), 78, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53d"), new ObjectId("657994077f727a970e6aff21"), 74, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53e"), new ObjectId("657994077f727a970e6aff20"), 65, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53e"), new ObjectId("657994077f727a970e6aff21"), 64, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53f"), new ObjectId("657994077f727a970e6aff20"), 51, null, null);
//        examResultService.updateScore(new ObjectId("558cb7e22dac8e9187c8a53f"), new ObjectId("657994077f727a970e6aff21"), 34, null, null);
//        System.out.println("success");
//        System.out.println(MongoFacroty.getAppDB());
//    }

//    /**
//     * 查询某次考试某个班某科目的平均成绩
//     */
//    @Test
//    public void getAveragePerformance() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getAveragePerformance(performanceEntryMap, classId, subjectId));
//    }
//
//    /**
//     * 查询某次考试某个同学某科目的成绩
//     */
//    @Test
//    public void getSubjectScore() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getSubjectScore(performanceEntryMap, subjectId, studentId));
//    }
//
//    /**
//     * 查询某次考试的班级排名
//     */
//    @Test
//    public void getClassRanking() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getClassRanking(performanceEntryMap, subjectId, classId, studentId));
//    }
//
//    /**
//     * 查询某次考试的年级排名
//     */
//    @Test
//    public void getGradeRanking() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getGradeRanking(performanceEntryMap, subjectId, studentId));
//    }
//
//    /**
//     * 查询某次考试某科目班级优秀率
//     */
//    @Test
//    public void getClassExcellentRate() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getClassExcellentRate(performanceEntryMap, subjectId, classId) + "%");
//    }
//
//    /**
//     * 查询某次考试某科目年级优秀率
//     */
//    @Test
//    public void getGradeExcellentRate() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getGradeExcellentRate(performanceEntryMap, subjectId) + "%");
//    }
//
//    /**
//     * 查询某次考试某科目班级及格率
//     */
//    @Test
//    public void getClassPassRate() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getClassPassRate(performanceEntryMap, subjectId, classId) + "%");
//    }
//
//    /**
//     * 查询某次考试某科目年级及格率
//     */
//    @Test
//    public void getGradePassRate() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getGradePassRate(performanceEntryMap, subjectId) + "%");
//    }
//
//    /**
//     * 成绩分布
//     */
//    @Test
//    public void getScoreDistribution() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        List<ScoreDistributionDTO> list = examResultService.getScoreDistribution(performanceEntryMap, subjectId, classId);
//        for(ScoreDistributionDTO sd : list) {
//            System.out.println(sd.getDistribution() + " -- " + sd.getNum());
//        }
//    }
//
//    /**
//     * 打败班级百分之多少的同学
//     */
//    @Test
//    public void getClassDebateRate() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        System.out.println(examResultService.getClassDefeatRate(performanceEntryMap, subjectId, classId, studentId) + "%");
//    }

//    /**
//     * 查询某次考试全班同学的成绩及班级排名和年级排名
//     * @return
//     */
//    @Test
//    public void getClassRankingList() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        List<StuScoreDTO> stuScoreDTOs = examResultService.getClassRankingList(performanceEntryMap, classId, subjectId,100,1);
//        System.out.println("StuName  score  classRanking   gradeRanking");
//        for(StuScoreDTO stuScoreDTO : stuScoreDTOs) {
//            System.out.println(stuScoreDTO.getStudentName() + "    " + stuScoreDTO.getScore() + "    " + stuScoreDTO.getClassRanking() + "    " + stuScoreDTO.getGradeRanking());
//
//        }
//    }
//
//    /**
//     * 查询某同学一学期的平时成绩，期中成绩，期末成绩
//     * @return
//     */
//    @Test
//    public void getStuScoreDTO() {
//        ScoreDTO scoreDTO = examResultService.getStuScoreDTO(subjectId, studentId, classId);
//        System.out.println("name  " + scoreDTO.getStudentName());
//        System.out.println("usualScore   " + scoreDTO.getUsualScore());
//        System.out.println("midScore   " + scoreDTO.getMidtermScore());
//        System.out.println("finalScore    " + scoreDTO.getFinalScore());
//    }
//
//    /**
//     * 查询某个班的所有考试
//     */
//    @Test
//    public void getExamMapByClassId(){
//        Map<ObjectId, String> m =  examResultService.getExamMapByClassId(classId);
//        Set<ObjectId> s = m.keySet();
//        for(ObjectId examId : s) {
//            System.out.println(examId.toString() + "----" + m.get(examId));
//        }
//
//    }
//
//
//    /**
//     * 查询某个班某科目的所有考试
//     */
//    @Test
//    public void getExamMapByClassIdSubjectId(){
//        List<ExamResultEntry> examResultEntryList =  examResultService.getExamListByClassId(classId,subjectId);
//
//        for(ExamResultEntry examResultEntry : examResultEntryList) {
//            System.out.println(examResultEntry.getID() + "----" + examResultEntry.getName());
//        }
//
//    }
//
//    /**
//     * 查询某个班一学期的平时成绩、期中成绩、期末成绩
//     */
//    @Test
//    public void getClassScoreDTO() {
//        ScoreDTO scoreDTO = examResultService.getClassScoreDTO(subjectId, classId);
//        System.out.println("usual  " + scoreDTO.getUsualScore());
//        System.out.println("midScore   " + scoreDTO.getMidtermScore());
//        System.out.println("finalScore    " + scoreDTO.getFinalScore());
//    }
//
//    /**
//     * 打分进度
//     */
//    @Test
//    public void getRateSchedule() {
//        Map<ObjectId, PerformanceEntry> performanceEntryMap = examResultService.getPerformanceEntryMap(examId);
//        Integer process = examResultService.getRateSchedule ( performanceEntryMap, subjectId, classId);
//        System.out.println(process);
//    }
//
//    /**
//     * 更新成绩
//     */
//    @Test
//    public void updateScore() {
//        ObjectId performanceId = new ObjectId("559ccd3c2dac25e80d3b041e");
//        ObjectId subjectId = new ObjectId("55935198f6f28b7261c9bf7e");
//        examResultService.updateScore(performanceId, subjectId, 99.0, 0, 0);
//
//    }

    @Test
    public void testRankScore(){
        ObjectId jointExamId = new ObjectId("562d99812dac52108161c939");
        examResultService.rankScore(jointExamId);
    }

}
