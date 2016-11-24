package com.fulaan.examresult.service;

import com.db.exam.ExamDao;
import com.db.exam.ScoreDao;
import com.db.examscoreanalysis.ThreePlusThreeScoreDao;
import com.db.examscoreanalysis.ScoreSummaryDao;
import com.db.examscoreanalysis.ZongFenDao;
import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.TermUtil;
import com.fulaan.zouban.dto.SubjectConfDTO;
import com.fulaan.zouban.service.SubjectConfService;
import com.fulaan.zouban.service.XuanKeService;
import com.mongodb.BasicDBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePairDTO1;
import com.pojo.exam.*;
import com.pojo.exam.ScoreDTO;
import com.pojo.examresult.Score;
import com.pojo.examscoreanalysis.*;
import com.pojo.school.ClassEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.SubjectConfEntry;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fl on 2016/8/12.
 */
@Service
public class ScoreSummaryService {

    private ScoreSummaryDao scoreSummaryDao = new ScoreSummaryDao();
    private ThreePlusThreeScoreDao threePlusThreeScoreDao = new ThreePlusThreeScoreDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private ExamDao examDao = new ExamDao();
    private ScoreDao scoreDao = new ScoreDao();
    private ClassDao classDao = new ClassDao();
    private ZongFenDao zongFenDao = new ZongFenDao();
    private SchoolDao schoolDao = new SchoolDao();
    private UserDao userDao = new UserDao();
    @Autowired
    private XuanKeService xuanKeService;
    @Autowired
    private SubjectConfService subjectConfService;


    /**
     * 获取分数总结
     * @param examId
     * @param subjectId null表示所有学科  否则为具体学科
     * @param type  -1 全部  1 班级层面  2 年级层面
     * @return
     */
    public List<ScoreSummaryDTO> getScoreSummaryByExamId(ObjectId examId, ObjectId subjectId, int type){
        List<ScoreSummaryDTO> scoreSummaryDTOs = new ArrayList<ScoreSummaryDTO>();
        List<ScoreSummaryEntry> scoreSummaryEntries = scoreSummaryDao.getScoreSummaryByExamIdSubjectId(examId, subjectId, type, Constant.FIELDS);
        if(scoreSummaryEntries.size() > 0){
            for(ScoreSummaryEntry entry : scoreSummaryEntries){
                scoreSummaryDTOs.add(new ScoreSummaryDTO(entry));
            }
        }
        return scoreSummaryDTOs;
    }

    public Map<String, Boolean> getZoufenState(ObjectId examId){
        Map<String, Boolean> states = new HashMap<String, Boolean>();
        ExamEntry examEntry = examDao.load(examId.toString());
        List<ExamSubjectEntry> examSubjectEntries = examEntry.getExamSubject();
        List<String> subjectNames = new ArrayList<String>();
        for(ExamSubjectEntry examSubjectEntry : examSubjectEntries){
            subjectNames.add(examSubjectEntry.getSubjectName());
        }
        List<String> yswNames = new ArrayList<String>();
        Collections.addAll(yswNames, "语文", "数学", "英语");
        List<String> allNames = new ArrayList<String>();
        Collections.addAll(allNames, "物理", "化学", "生物", "地理", "历史", "政治");
        Boolean ysw = subjectNames.containsAll(yswNames);
        Boolean all = ysw && subjectNames.containsAll(allNames);
        states.put("ysw", ysw);
        states.put("all", all);
        return states;
    }

    /**
     * 查询考试总分
     * @param examId
     * @return
     */
    public List<ZongFenDTO> getZongFenDTOsByExamId(ObjectId examId, int page, int pageSize){
        ExamEntry examEntry = examDao.load(examId.toString());
        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(examEntry.getGradeId());
        Map<ObjectId, String> classId_classNameMap = new HashMap<ObjectId, String>();
        for(ClassEntry classEntry : classEntries){
            classId_classNameMap.put(classEntry.getID(), classEntry.getName());
        }

        List<ZongFenDTO> list = new ArrayList<ZongFenDTO>();
        List<ZongFenEntry> zongFenEntries = zongFenDao.getZongFenByExamId(examId, (page - 1) * pageSize, pageSize);
        List<ObjectId> studentIds = MongoUtils.getFieldObjectIDs(zongFenEntries, "stuid");
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(studentIds, new BasicDBObject("nm",1).append("sn", 1));
        for(ZongFenEntry zongFenEntry : zongFenEntries){
            ZongFenDTO zongFenDTO = new ZongFenDTO(zongFenEntry);
            zongFenDTO.setClassName(classId_classNameMap.get(zongFenEntry.getAdminClassId()));
            UserEntry userEntry = userEntryMap.get(zongFenEntry.getStudentId());
            if(userEntry == null)
                continue;
            zongFenDTO.setStudentName(userEntry.getUserName());
            zongFenDTO.setStudentNo(userEntry.getStudyNum());
            list.add(zongFenDTO);
        }
        return list;
    }

    /**
     * 某次考试总分记录数
     * @param examId
     * @return
     */
    public int countZongFenByExamId(ObjectId examId){
        return zongFenDao.countByExamId(examId);
    }

    /**
     * 前x名分布
     * @param examId
     * @param type ZongFenEntry.YSW  ZongFenEntry.ALL
     * @return
     */
    public List<ZongFenSummaryDTO> getZongFenSummary(ObjectId examId, int type){
        List<ZongFenSummaryDTO> dtos = new ArrayList<ZongFenSummaryDTO>();
        List<ZongFenEntry> zongFenEntries = zongFenDao.getZongFenByExamId(examId, 0, 10000);
        int gradeCount_10 = count(zongFenEntries, type, 10);
        int gradeCount_50 = count(zongFenEntries, type, 50);
        int gradeCount_100 = count(zongFenEntries, type, 100);
        int gradeCount_200 = count(zongFenEntries, type, 200);

        Map<ObjectId, List<ZongFenEntry>> classId_ZongFenEntryListMap = new HashMap<ObjectId, List<ZongFenEntry>>();
        for(ZongFenEntry zongFenEntry : zongFenEntries){
            ObjectId classId = zongFenEntry.getAdminClassId();
            List<ZongFenEntry> entries = classId_ZongFenEntryListMap.get(classId);
            if(entries == null)
                entries = new ArrayList<ZongFenEntry>();
            entries.add(zongFenEntry);
            classId_ZongFenEntryListMap.put(classId, entries);
        }
        List<ClassEntry> classEntries = classDao.getClassEntryByIds(classId_ZongFenEntryListMap.keySet(), new BasicDBObject("nm", 1));
        Map<ObjectId, ClassEntry> classId_ClassEntryMap = new HashMap<ObjectId, ClassEntry>();
        for(ClassEntry classEntry : classEntries){
            classId_ClassEntryMap.put(classEntry.getID(), classEntry);
        }

        for(Map.Entry<ObjectId, List<ZongFenEntry>> entry : classId_ZongFenEntryListMap.entrySet()){
            ObjectId classId = entry.getKey();
            ClassEntry classEntry = classId_ClassEntryMap.get(classId);
            List<ZongFenEntry> list = entry.getValue();
            int count_10 = count(list, type, 10);
            int count_50 = count(list, type, 50);
            int count_100 = count(list, type, 100);
            int count_200 = count(list, type, 200);
            double rate_10 = Math.round(count_10 * 10000.0 / gradeCount_10) / 100.0;
            double rate_50 = Math.round(count_50 * 10000.0 / gradeCount_50) / 100.0;
            double rate_100 = Math.round(count_100 * 10000.0 / gradeCount_100) / 100.0;
            double rate_200 = Math.round(count_200 * 10000.0 / gradeCount_200) / 100.0;
            double zongfenAvg = getClassZongFenAvg(list);
            double yswZongfenAvg = getYSWClassZongFenAvg(list);
            ZongFenSummaryDTO zongFenSummaryDTO = new ZongFenSummaryDTO(classEntry.getName(), count_100, count_10, count_200,count_50,
                    rate_100, rate_10, rate_200, rate_50, zongfenAvg, 0, yswZongfenAvg, 0);
            dtos.add(zongFenSummaryDTO);
        }
        setRank(dtos);
        return dtos;
    }

    private void setRank(List<ZongFenSummaryDTO> dtos){
        setZongfenRank(dtos);
        setYSWZongfenRank(dtos);
    }

    private void setZongfenRank(List<ZongFenSummaryDTO> dtos){
        Collections.sort(dtos, new Comparator<ZongFenSummaryDTO>() {
            @Override
            public int compare(ZongFenSummaryDTO dto1, ZongFenSummaryDTO dto2) {
                Double zongfen1 = dto1.getAllAvg();
                Double zongfen2 = dto2.getAllAvg();
                return zongfen2.compareTo(zongfen1);
            }
        });

        double lastAvg = 10000;
        int lastRank = 0;
        double avg = 0;
        int i = 0;
        for(ZongFenSummaryDTO dto : dtos){
            i++;
            avg = dto.getAllAvg();
            if(avg < lastAvg){
                lastRank = i;
                lastAvg = avg;
            }
            dto.setAllGradeRank(lastRank);
        }
    }

    private void setYSWZongfenRank(List<ZongFenSummaryDTO> dtos){
        Collections.sort(dtos, new Comparator<ZongFenSummaryDTO>() {
            @Override
            public int compare(ZongFenSummaryDTO dto1, ZongFenSummaryDTO dto2) {
                Double zongfen1 = dto1.getYswAvg();
                Double zongfen2 = dto2.getYswAvg();
                return zongfen2.compareTo(zongfen1);
            }
        });

        double lastAvg = 10000;
        int lastRank = 0;
        double avg = 0;
        int i = 0;
        for(ZongFenSummaryDTO dto : dtos){
            i++;
            avg = dto.getYswAvg();
            if(avg < lastAvg){
                lastRank = i;
                lastAvg = avg;
            }
            dto.setYswGradeRank(lastRank);
        }
    }

    private double getClassZongFenAvg(List<ZongFenEntry> list){
        if(list.size() > 0){
            double zongfen = 0;
            for(ZongFenEntry zongFenEntry : list){
                zongfen += zongFenEntry.getZongFen();
            }
            return Math.round(zongfen / list.size() * 100) / 100.0;
        }
        return 0;
    }

    private double getYSWClassZongFenAvg(List<ZongFenEntry> list){
        if(list.size() > 0){
            double zongfen = 0;
            for(ZongFenEntry zongFenEntry : list){
                zongfen += zongFenEntry.getYswZongFen();
            }
            return Math.round(zongfen / list.size() * 100) / 100.0;
        }
        return 0;
    }

    /**
     * 计算前x名的人数
     * @param zongFenEntries
     * @param type
     * @param limit
     * @return
     */
    private int count(List<ZongFenEntry> zongFenEntries, int type, int limit){
        int count = 0;
        for(ZongFenEntry entry : zongFenEntries){
            if(entry.getGradeRankByType(type) <= limit)
                count ++;
        }
        return count;
    }



    /**
     * 导出成绩输入模板
     * @param examId
     */
    public void exportScoreTmplExcel(ObjectId examId){

    }

    /**
     * 导入成绩
     */
    public void importScoreTmplExcel(List<ScoreDTO> data, final ObjectId examId) throws Exception{
        List<ScoreEntry> scoreEntries = scoreDao.findByExid(examId);
        Map<String, ObjectId> scoreIdStudentIdMap = new HashMap<String, ObjectId>();
        for(ScoreEntry scoreEntry : scoreEntries){
            scoreIdStudentIdMap.put(scoreEntry.getID().toString(), scoreEntry.getStudentId());
        }
        for(ScoreDTO scoreDTO : data){
            ObjectId studentId = scoreIdStudentIdMap.get(scoreDTO.getId());
            Map<String, SubjectScoreDTO> stringSubjectScoreDTOMap = scoreDTO.getExamScore();
            for(Map.Entry<String, SubjectScoreDTO> entry : stringSubjectScoreDTOMap.entrySet()){
                ObjectId subjectId = new ObjectId(entry.getKey());
                SubjectScoreDTO subjectScoreDTO = entry.getValue();
                double score = Double.parseDouble(subjectScoreDTO.getScore());
                threePlusThreeScoreDao.update(examId, studentId, subjectId, new FieldValuePair("sc", score));
            }
        }
        //另起一个线程处理数据
        Runnable hander = new Runnable() {
            @Override
            public void run() {
                try {
                    scoreAnalysis(examId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread analysis = new Thread(hander);
        analysis.start();

    }


    /**
     * 分析、产生中间数据
     * @param examId
     */
    public void scoreAnalysis(ObjectId examId) throws Exception{
        //先删掉历史数据
        scoreSummaryDao.removeByExamId(examId);

        ExamEntry examEntry = examDao.load(examId.toString());
        ObjectId gradeId = examEntry.getGradeId();
        List<ExamSubjectEntry> examSubjectEntries = examEntry.getExamSubject();
        Map<ObjectId, ExamSubjectEntry> examSubjectEntryMap = new HashMap<ObjectId, ExamSubjectEntry>();
        for(ExamSubjectEntry examSubjectEntry : examSubjectEntries){
            examSubjectEntryMap.put(examSubjectEntry.getSubjectId(), examSubjectEntry);
        }
        List<ThreePlusThreeScoreEntry> scoreEntries = threePlusThreeScoreDao.getScoreEntries(examId, null, Constant.FIELDS);
        Map<ObjectId, List<ThreePlusThreeScoreEntry>> subjectId_ScoreEntriesMap = splitBySubjectId(scoreEntries);
        for(Map.Entry<ObjectId, List<ThreePlusThreeScoreEntry>> entry : subjectId_ScoreEntriesMap.entrySet()){
            ObjectId subjectId = entry.getKey();
            ExamSubjectEntry examSubjectEntry = examSubjectEntryMap.get(subjectId);
            String subjectName = examSubjectEntry.getSubjectName();
            List<ThreePlusThreeScoreEntry> scoreEntriesWithSameSubjectId = entry.getValue();
            setGradeRank(scoreEntriesWithSameSubjectId);
            ScoreSummaryEntry gradeSummaryEntry = bulidSummaryForGradeLevel(scoreEntriesWithSameSubjectId, gradeId, examSubjectEntry);
            gradeSummaryEntry.setSubjectName(subjectName);
            scoreSummaryDao.saveScoreSummary(gradeSummaryEntry);
            Map<ObjectId, List<ThreePlusThreeScoreEntry>> classId_ScoreEntriesMap = null;
            Boolean isAdminClass = isAdminClass(examEntry, subjectId.toString());
            if(isAdminClass){//行政班
                classId_ScoreEntriesMap = splitByAdminClassId(scoreEntriesWithSameSubjectId);
            } else {
                classId_ScoreEntriesMap = splitByTeachingClassId(scoreEntriesWithSameSubjectId);
            }
            List<ScoreSummaryEntry> scoreSummaryEntries = new ArrayList<ScoreSummaryEntry>();
            for(Map.Entry<ObjectId, List<ThreePlusThreeScoreEntry>> entry1 : classId_ScoreEntriesMap.entrySet()){
                List<ThreePlusThreeScoreEntry> scoreEntriesWithSameClass = entry1.getValue();
                setClassRank(scoreEntriesWithSameClass);
                ObjectId classId = entry1.getKey();
                ScoreSummaryEntry classSummaryEntry = bulidSummaryForClassLevel(scoreEntriesWithSameClass, gradeId, classId, examSubjectEntry);
                classSummaryEntry.setSubjectName(subjectName);
                if(isAdminClass){
                    ClassEntry classEntry = classDao.getClassEntryById(classId, new BasicDBObject("nm", 1));
                    classSummaryEntry.setClassName(classEntry.getName());
                } else {
                    ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(classId);
                    classSummaryEntry.setClassName(zouBanCourseEntry.getClassName());
                }
                scoreSummaryEntries.add(classSummaryEntry);
            }
            calculateScoreSummaryRank(scoreSummaryEntries);
            scoreSummaryDao.insertScoreSummaries(scoreSummaryEntries);
        }
        for(ThreePlusThreeScoreEntry scoreEntry : scoreEntries){
            threePlusThreeScoreDao.save(scoreEntry);
        }
        createZongfen(scoreEntries, examEntry);
    }

    private void calculateScoreSummaryRank(List<ScoreSummaryEntry> scoreSummaryEntries){
        Collections.sort(scoreSummaryEntries, new Comparator<ScoreSummaryEntry>() {
            @Override
            public int compare(ScoreSummaryEntry entry1, ScoreSummaryEntry entry2) {
                Double avg1 = entry1.getAvg();
                Double avg2 = entry2.getAvg();
                return avg2.compareTo(avg1);
            }
        });
        double lastAvg = 10000;
        int lastRank = 0;
        double avg = 0;
        int i = 0;
        for(ScoreSummaryEntry summaryEntry : scoreSummaryEntries){
            i++;
            avg = summaryEntry.getAvg();
            if(avg < lastAvg){
                lastRank = i;
                lastAvg = avg;
            }
            summaryEntry.setRank(lastRank);
        }
    }

    private void createZongfen(List<ThreePlusThreeScoreEntry> scoreEntries, ExamEntry examEntry){
        //先删掉历史数据
        zongFenDao.remove(examEntry.getID());

        List<ObjectId> yswSubjectIds = yswSubjectIds(examEntry.getSchoolId());
        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(examEntry.getGradeId());
        List<ZongFenEntry> zongFenEntries = new ArrayList<ZongFenEntry>();
        Map<ObjectId, List<ThreePlusThreeScoreEntry>> studentId_ScoreEntriesMap = splitByStudentId(scoreEntries);
        for(ClassEntry classEntry : classEntries){
            List<ZongFenEntry> classZongFenEntries = new ArrayList<ZongFenEntry>();
            List<ObjectId> students = classEntry.getStudents();
            for(ObjectId studentId : students){
                List<ThreePlusThreeScoreEntry> studentScores = studentId_ScoreEntriesMap.get(studentId);
                double zongfen = getZongFen(studentScores);
                double yswZongfen = getYSWZongFen(studentScores, yswSubjectIds);
                ZongFenEntry zongFenEntry = new ZongFenEntry(examEntry.getID(), classEntry.getID(), studentId, yswZongfen, 0, 0, zongfen, 0, 0);
                classZongFenEntries.add(zongFenEntry);
            }
            calculateClassRank(classZongFenEntries);
            zongFenEntries.addAll(classZongFenEntries);
        }
        calculateGradeRank(zongFenEntries);
        zongFenDao.save(zongFenEntries);
    }

    private double getZongFen(List<ThreePlusThreeScoreEntry> scoreEntries){
        double score = 0;
        for(ThreePlusThreeScoreEntry scoreEntry : scoreEntries){
            score += scoreEntry.getScore();
        }
        return score;
    }

    private double getYSWZongFen(List<ThreePlusThreeScoreEntry> scoreEntries, List<ObjectId> yswSubjectIds){
        double score = 0;
        for(ThreePlusThreeScoreEntry scoreEntry : scoreEntries) {
            if (yswSubjectIds.contains(scoreEntry.getSubjectId())) {
                score += scoreEntry.getScore();
            }
        }
        return score;
    }

    private List<ObjectId> yswSubjectIds(ObjectId schoolId){
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, new BasicDBObject("subs", 1));
        List<Subject> subjects = schoolEntry.getSubjects();
        List<ObjectId> yswSubjectIds = new ArrayList<ObjectId>();
        for(Subject subject : subjects){
            String subjectName = subject.getName();
            if("语文".equals(subjectName) || "数学".equals(subjectName) || "英语".equals(subjectName)){
                yswSubjectIds.add(subject.getSubjectId());
            }
        }
        return yswSubjectIds;
    }

    private void calculateClassRank(List<ZongFenEntry> scoreEntries){
        //语数英班级排名
        sortByYSWZongFen(scoreEntries);
        double lastScore = 10000;
        int lastRank = 0;
        double score = 0;
        int i = 0;
        for(ZongFenEntry entry : scoreEntries){
            i++;
            score = entry.getYswZongFen();
            if(score < lastScore){
                lastRank = i;
                lastScore = score;
            }
            entry.setYswClassRank(lastRank);
        }

        //全科班级排名
        sortByZongFen(scoreEntries);
        lastScore = 10000;
        lastRank = 0;
        score = 0;
        i = 0;
        for(ZongFenEntry entry : scoreEntries){
            i++;
            score = entry.getZongFen();
            if(score < lastScore){
                lastRank = i;
                lastScore = score;
            }
            entry.setClassRank(lastRank);
        }
    }

    private void calculateGradeRank(List<ZongFenEntry> scoreEntries){
        //语数英班级排名
        sortByYSWZongFen(scoreEntries);
        double lastScore = 10000;
        int lastRank = 0;
        double score = 0;
        int i = 0;
        for(ZongFenEntry entry : scoreEntries){
            i++;
            score = entry.getZongFen();
            if(score < lastScore){
                lastRank = i;
                lastScore = score;
            }
            entry.setYswGradeRank(lastRank);
        }

        //全科班级排名
        sortByZongFen(scoreEntries);
        lastScore = 10000;
        lastRank = 0;
        score = 0;
        i = 0;
        for(ZongFenEntry entry : scoreEntries){
            i++;
            score = entry.getZongFen();
            if(score < lastScore){
                lastRank = i;
                lastScore = score;
            }
            entry.setGradeRank(lastRank);
        }
    }

    private void sortByZongFen(List<ZongFenEntry> scoreEntries){
        Collections.sort(scoreEntries, new Comparator<ZongFenEntry>() {
            @Override
            public int compare(ZongFenEntry entry1, ZongFenEntry entry2) {
                Double zongfen1 = entry1.getZongFen();
                Double zongfen2 = entry2.getZongFen();
                return zongfen2.compareTo(zongfen1);
            }
        });
    }

    private void sortByYSWZongFen(List<ZongFenEntry> scoreEntries){
        Collections.sort(scoreEntries, new Comparator<ZongFenEntry>() {
            @Override
            public int compare(ZongFenEntry entry1, ZongFenEntry entry2) {
                Double zongfen1 = entry1.getYswZongFen();
                Double zongfen2 = entry2.getYswZongFen();
                return zongfen2.compareTo(zongfen1);
            }
        });
    }

    /**
     * 判断考试科目是否应该和行政班绑定
     * @param examEntry
     * @param subjectId
     * @return true 和行政班绑定   false 和教学班绑定
     * @throws Exception
     */
    private Boolean isAdminClass(ExamEntry examEntry, String subjectId) throws Exception{
        if(examEntry.getType() == 1){
            return true;
        }
        List<SubjectConfDTO> zoubanSubjects = subjectConfService.getSubjectConfList(TermUtil.getSchoolYear(), examEntry.getGradeId().toString(), 1, examEntry.getSchoolId().toString());
        for(SubjectConfDTO confDTO : zoubanSubjects){
            if(confDTO.getSubjectId().equals(subjectId)){
                return false;
            }
        }
        List<SubjectConfDTO> feizoubanSubjects = subjectConfService.getSubjectConfList(TermUtil.getSchoolYear(), examEntry.getGradeId().toString(), 2, examEntry.getSchoolId().toString());
        for(SubjectConfDTO confDTO : feizoubanSubjects){
            if(confDTO.getSubjectId().equals(subjectId)){
                return true;
            }
        }
        throw new Exception("走班科目配置错误");
    }

    /**
     * 把不同学科的成绩数据按学科划分
     * @param scoreEntries
     * @return
     */
    private Map<ObjectId, List<ThreePlusThreeScoreEntry>> splitByStudentId(List<ThreePlusThreeScoreEntry> scoreEntries){
        Map<ObjectId, List<ThreePlusThreeScoreEntry>> studentId_ScoreEntriesMap = new HashMap<ObjectId, List<ThreePlusThreeScoreEntry>>();
        for(ThreePlusThreeScoreEntry threePlusThreeScoreEntry : scoreEntries){
            ObjectId studentId = threePlusThreeScoreEntry.getStudentId();
            List<ThreePlusThreeScoreEntry> list = studentId_ScoreEntriesMap.get(studentId);
            if(null == list){
                list = new ArrayList<ThreePlusThreeScoreEntry>();
            }
            list.add(threePlusThreeScoreEntry);
            studentId_ScoreEntriesMap.put(studentId, list);
        }
        return studentId_ScoreEntriesMap;
    }

    /**
     * 把不同学科的成绩数据按学科划分
     * @param scoreEntries
     * @return
     */
    private Map<ObjectId, List<ThreePlusThreeScoreEntry>> splitBySubjectId(List<ThreePlusThreeScoreEntry> scoreEntries){
        Map<ObjectId, List<ThreePlusThreeScoreEntry>> subjectId_ScoreEntriesMap = new HashMap<ObjectId, List<ThreePlusThreeScoreEntry>>();
        for(ThreePlusThreeScoreEntry threePlusThreeScoreEntry : scoreEntries){
            ObjectId subjectId = threePlusThreeScoreEntry.getSubjectId();
            List<ThreePlusThreeScoreEntry> list = subjectId_ScoreEntriesMap.get(subjectId);
            if(null == list){
                list = new ArrayList<ThreePlusThreeScoreEntry>();
            }
            list.add(threePlusThreeScoreEntry);
            subjectId_ScoreEntriesMap.put(subjectId, list);
        }
        return subjectId_ScoreEntriesMap;
    }

    /**
     * 同一学科不同行政班的成绩按行政班划分
     * @param scoreEntries
     * @return
     */
    private Map<ObjectId, List<ThreePlusThreeScoreEntry>> splitByAdminClassId(List<ThreePlusThreeScoreEntry> scoreEntries){
        Map<ObjectId, List<ThreePlusThreeScoreEntry>> adminClassId_ScoreEntriesMap = new HashMap<ObjectId, List<ThreePlusThreeScoreEntry>>();
        for(ThreePlusThreeScoreEntry threePlusThreeScoreEntry : scoreEntries){
            ObjectId adminClassId = threePlusThreeScoreEntry.getAdminClassId();
            List<ThreePlusThreeScoreEntry> list = adminClassId_ScoreEntriesMap.get(adminClassId);
            if(null == list){
                list = new ArrayList<ThreePlusThreeScoreEntry>();
            }
            list.add(threePlusThreeScoreEntry);
            adminClassId_ScoreEntriesMap.put(adminClassId, list);
        }
        return adminClassId_ScoreEntriesMap;
    }

    /**
     * 同一学科不同行教学班的成绩按教学班划分
     * @param scoreEntries
     * @return
     */
    private Map<ObjectId, List<ThreePlusThreeScoreEntry>> splitByTeachingClassId(List<ThreePlusThreeScoreEntry> scoreEntries){
        Map<ObjectId, List<ThreePlusThreeScoreEntry>> teachingClassId_ScoreEntriesMap = new HashMap<ObjectId, List<ThreePlusThreeScoreEntry>>();
        for(ThreePlusThreeScoreEntry threePlusThreeScoreEntry : scoreEntries){
            ObjectId teachingClassId = threePlusThreeScoreEntry.getTeachingClassId();
            List<ThreePlusThreeScoreEntry> list = teachingClassId_ScoreEntriesMap.get(teachingClassId);
            if(null == list) {
                list = new ArrayList<ThreePlusThreeScoreEntry>();
            }
            list.add(threePlusThreeScoreEntry);
            teachingClassId_ScoreEntriesMap.put(teachingClassId, list);
        }
        return teachingClassId_ScoreEntriesMap;
    }

    /**
     * 对成绩排序
     * @param scoreEntries
     */
    private void sortScoreEntries(List<ThreePlusThreeScoreEntry> scoreEntries){
        Collections.sort(scoreEntries, new Comparator<ThreePlusThreeScoreEntry>() {
            @Override
            public int compare(ThreePlusThreeScoreEntry entry1, ThreePlusThreeScoreEntry entry2) {
                Double score1 = entry1.getScore();
                Double score2 = entry2.getScore();
                return score2.compareTo(score1);
            }
        });
    }

    private void setGradeRank(List<ThreePlusThreeScoreEntry> scoreEntries){
        sortScoreEntries(scoreEntries);
        double lastScore = 1000;
        int lastRank = 0;
        double score = 0;
        int i = 0;
        for(ThreePlusThreeScoreEntry entry : scoreEntries){
            i++;
            score = entry.getScore();
            if(score < lastScore){
                lastRank = i;
                lastScore = score;
            }
            entry.setGradeRank(lastRank);
        }
    }

    private void setClassRank(List<ThreePlusThreeScoreEntry> scoreEntries){
        sortScoreEntries(scoreEntries);
        double lastScore = 1000;
        int lastRank = 0;
        double score = 0;
        int i = 0;
        for(ThreePlusThreeScoreEntry entry : scoreEntries){
            i++;
            score = entry.getScore();
            if(score < lastScore){
                lastRank = i;
                lastScore = score;
            }
            entry.setClassRank(lastRank);
        }
    }


    /**
     * 成绩总结（班级层次）
     * @param scoreEntries  同一班级同一学科的成绩
     * @param gradeId 年级id
     * @param classId 班级id
     * @return
     */
    private ScoreSummaryEntry bulidSummaryForClassLevel(List<ThreePlusThreeScoreEntry> scoreEntries, ObjectId gradeId, ObjectId classId, ExamSubjectEntry examSubjectEntry){
        return bulidSummary(scoreEntries, gradeId, classId, 1, examSubjectEntry);
    }

    /**
     * 成绩总结（年级层次）
     * @param scoreEntries 同一年级同一学科的成绩
     * @param gradeId 年级id
     * @return
     */
    private ScoreSummaryEntry bulidSummaryForGradeLevel(List<ThreePlusThreeScoreEntry> scoreEntries, ObjectId gradeId, ExamSubjectEntry examSubjectEntry){
       return bulidSummary(scoreEntries, gradeId, null, 2, examSubjectEntry);
    }

    /**
     * 成绩总结
     * @param scoreEntries 成绩原始数据
     * @param gradeId 年级id
     * @param classId 班级层次时必须  班级id
     * @param type 1 班级层次  2 年级层次
     * @return
     */
    private ScoreSummaryEntry bulidSummary(List<ThreePlusThreeScoreEntry> scoreEntries, ObjectId gradeId, ObjectId classId, int type, ExamSubjectEntry examSubjectEntry){
        int count = scoreEntries.size();
        if(count <= 0){
            return null;
        }
        double max = 0;
        double min = 200;
        double totalScore = 0;
        double avg = 0;
        int youXiuCount = 0;
        int heGeCount = 0;
        int diFenCount = 0;
        for(ThreePlusThreeScoreEntry threePlusThreeScoreEntry : scoreEntries){
            double score = threePlusThreeScoreEntry.getScore();
            if(score > max)
                max = score;
            if(score < min)
                min = score;
            if(score >= examSubjectEntry.getYouXiuScore())
                youXiuCount ++;
            if(score >= examSubjectEntry.getFailScore())
                heGeCount ++;
            if(score < examSubjectEntry.getDiFenScore())
                diFenCount ++;
            totalScore += score;
        }
        avg = totalScore / count;
        avg = Math.round(avg * 100) / 100.0;
        double youXiuRate = Math.round(youXiuCount * 10000.0 / count) / 100.0;
        double heGeRate = Math.round(heGeCount * 10000.0 / count) / 100.0;
        double diFenRate = Math.round(diFenCount * 10000.0 / count) / 100.0;
        ThreePlusThreeScoreEntry threePlusThreeScoreEntry = scoreEntries.get(0);
        if(2 == type)
            classId = null;
        return new ScoreSummaryEntry(threePlusThreeScoreEntry.getExamId(), threePlusThreeScoreEntry.getSubjectId(), "", classId, "",  gradeId, max, min, avg,
                count, heGeCount, heGeRate, youXiuCount, youXiuRate, diFenCount, diFenRate, 0, type);
    }

    /**
     * 考试科目列表
     * @param examId
     * @return
     */
    public List<IdValuePairDTO1> getExamSubjects(ObjectId examId){
        List<IdValuePairDTO1> list = new ArrayList<IdValuePairDTO1>();
        ExamEntry examEntry = examDao.load(examId.toString());
        List<ExamSubjectEntry> subjectEntries = examEntry.getExamSubject();
        for(ExamSubjectEntry examSubjectEntry : subjectEntries){
            IdValuePairDTO1 dto1 = new IdValuePairDTO1(examSubjectEntry.getSubjectId().toString(), examSubjectEntry.getSubjectName());
            list.add(dto1);
        }
        return list;
    }

    /**
     * 学生成绩列表
     * @param examId
     * @param subjectId
     * @param page
     * @param size
     * @return
     */
    public List<ThreePlusThreeScoreDTO> getScoreEntries(ObjectId examId, ObjectId subjectId, int page, int size) throws Exception{
        List<ThreePlusThreeScoreDTO> scoreDTOs = new ArrayList<ThreePlusThreeScoreDTO>();
        List<ThreePlusThreeScoreEntry> scoreEntries = threePlusThreeScoreDao.getScoreEntries(examId, subjectId, (page - 1) * size, size, Constant.FIELDS);
        List<ObjectId> studentIds = MongoUtils.getFieldObjectIDs(scoreEntries, "stuid");
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(studentIds, new BasicDBObject("nm", 1).append("sn", 1));
        for(ThreePlusThreeScoreEntry scoreEntry : scoreEntries){
            ThreePlusThreeScoreDTO scoreDTO = new ThreePlusThreeScoreDTO(scoreEntry);
            UserEntry userEntry = userEntryMap.get(scoreEntry.getStudentId());
            if(userEntry == null) {
                continue;
            }
            scoreDTO.setStudentName(userEntry.getUserName());
            scoreDTO.setStudentNo(userEntry.getStudyNum());
            scoreDTOs.add(scoreDTO);
        }
        return scoreDTOs;
    }

    /**
     * 数量
     * @param examId
     * @param subjectId
     * @return
     */
    public int countScoreEntries(ObjectId examId, ObjectId subjectId){
        return threePlusThreeScoreDao.countScoreEntries(examId, subjectId);
    }

    /**
     * 三率一分表
     * @param util
     * @param examId
     */
    public void exportResult_3lv1fen(final ExportUtil util, final ObjectId examId) {
        ExamEntry examEntry = examDao.load(examId.toString());

        List<String> datas = new ArrayList<String>();
        SXSSFWorkbook wb = util.getBook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 1);//左边框
        cellStyle.setBorderTop((short) 1);//上边框
        cellStyle.setBorderRight((short) 1);//右边框
        cellStyle.setBorderBottom((short) 1);//右边框

        List<ExamSubjectEntry> subjectEntries = examEntry.getExamSubject();
        for(ExamSubjectEntry subjectEntry : subjectEntries){
            datas.clear();
            datas.add(subjectEntry.getSubjectName());
            util.appendRow(datas.toArray());
            datas.clear();
            datas.add("班级");
            datas.add("平均分");
            datas.add("最高分");
            datas.add("最低分");
            datas.add("优秀人数");
            datas.add("优秀率");
            datas.add("合格人数");
            datas.add("合格率");
            datas.add("低分人数");
            datas.add("低分率");
            datas.add("排名");
            util.appendRow(cellStyle, datas.toArray());

            List<ScoreSummaryDTO> list = getScoreSummaryByExamId(examId, subjectEntry.getSubjectId(), 1);
            for(ScoreSummaryDTO summaryDTO : list){
                datas.clear();
                datas.add(summaryDTO.getClassName());
                datas.add(summaryDTO.getAvg() + "");
                datas.add(summaryDTO.getMax() + "");
                datas.add(summaryDTO.getMin() + "");
                datas.add(summaryDTO.getYouXiuCount() + "");
                datas.add(summaryDTO.getYouXiuRate() + "");
                datas.add(summaryDTO.getHeGeCount() + "");
                datas.add(summaryDTO.getHeGeRate() + "");
                datas.add(summaryDTO.getDiFenCount() + "");
                datas.add(summaryDTO.getDiFenRate() + "");
                datas.add(summaryDTO.getRank() + "");
                util.appendRow(cellStyle, datas.toArray());
            }
            datas.clear();
            util.appendRow(datas.toArray());
        }
        util.setFileName(String.format("%s_%s", examEntry.getName(), "_三率一分统计表.xlsx"));
    }

    /**
     * 各科排名表
     * @param util
     * @param examId
     */
    public void export_subjectsRanking(final ExportUtil util, final ObjectId examId) throws Exception{
        ExamEntry examEntry = examDao.load(examId.toString());

        List<String> datas = new ArrayList<String>();
        SXSSFWorkbook wb = util.getBook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 1);//左边框
        cellStyle.setBorderTop((short) 1);//上边框
        cellStyle.setBorderRight((short) 1);//右边框
        cellStyle.setBorderBottom((short) 1);//右边框

        List<ExamSubjectEntry> subjectEntries = examEntry.getExamSubject();
        for(ExamSubjectEntry subjectEntry : subjectEntries){
            datas.clear();
            datas.add(subjectEntry.getSubjectName());
            util.appendRow(datas.toArray());
            datas.clear();
            datas.add("名次");
            datas.add("班级");
            datas.add("学号");
            datas.add("姓名");
            datas.add("分数");
            util.appendRow(cellStyle, datas.toArray());
            List<ThreePlusThreeScoreDTO> list = getScoreEntries(examId, subjectEntry.getSubjectId(), 1, 10000);
            for(ThreePlusThreeScoreDTO scoreDTO : list){
                datas.clear();
                datas.add(scoreDTO.getGradeRank() + "");
                datas.add(scoreDTO.getClassName() + "");
                datas.add(scoreDTO.getStudentNo() + "");
                datas.add(scoreDTO.getStudentName() + "");
                datas.add(scoreDTO.getScore() + "");
                util.appendRow(cellStyle, datas.toArray());
            }
        }
        util.setFileName(String.format("%s_%s", examEntry.getName(), "_各科排名表.xlsx"));
    }

    /**
     * 年级学生总排名表
     * @param util
     * @param examId
     * @throws Exception
     */
    public void export_zongbiao(final ExportUtil util, final ObjectId examId) throws Exception{
        ExamEntry examEntry = examDao.load(examId.toString());
        List<ExamSubjectEntry> subjectEntries = examEntry.getExamSubject();

        List<String> datas = new ArrayList<String>();
        SXSSFWorkbook wb = util.getBook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 1);//左边框
        cellStyle.setBorderTop((short) 1);//上边框
        cellStyle.setBorderRight((short) 1);//右边框
        cellStyle.setBorderBottom((short) 1);//右边框

        datas.clear();
        datas.add("班级");
        datas.add("学号");
        datas.add("姓名");
        for(ExamSubjectEntry subjectEntry : subjectEntries){
            datas.add(subjectEntry.getSubjectName());
            datas.add(subjectEntry.getSubjectName());
            datas.add(subjectEntry.getSubjectName());
        }
        util.appendRow(cellStyle, datas.toArray());
        datas.clear();
        datas.add("班级");
        datas.add("学号");
        datas.add("姓名");
        for(ExamSubjectEntry subjectEntry : subjectEntries){
            datas.add("成绩");
            datas.add("班级排名");
            datas.add("年级排名");
        }
        util.appendRow(cellStyle, datas.toArray());

        Sheet sheet = util.getSheet();
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        for(int i=0; i<subjectEntries.size(); i++){
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 3*i+3, 3*i+5));
        }

        Map<String, ThreePlusThreeScoreEntry> map = new HashMap<String, ThreePlusThreeScoreEntry>();
        List<ThreePlusThreeScoreEntry> threePlusThreeScoreEntries = threePlusThreeScoreDao.getScoreEntries(examId, null, Constant.FIELDS);
        Set<ObjectId> studentIds = new HashSet<ObjectId>();
        for(ThreePlusThreeScoreEntry scoreEntry : threePlusThreeScoreEntries){
            studentIds.add(scoreEntry.getStudentId());
            map.put(scoreEntry.getStudentId() + "_" + scoreEntry.getSubjectId(), scoreEntry);
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(studentIds, new BasicDBObject("nm", 1).append("sn", 1));


        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(examEntry.getGradeId());
        for(ClassEntry classEntry : classEntries){
            List<ObjectId> stus = classEntry.getStudents();
            for(ObjectId stuId : stus){
                UserEntry userEntry = userEntryMap.get(stuId);
                if(userEntry == null)
                    continue;
                datas.clear();
                datas.add(classEntry.getName());
                datas.add(userEntry.getStudyNum());
                datas.add(userEntry.getUserName());
                for(ExamSubjectEntry subjectEntry : subjectEntries){
                    ThreePlusThreeScoreEntry scoreEntry = map.get(stuId.toString() + "_" + subjectEntry.getSubjectId().toString());
                    if(scoreEntry == null){
                        datas.add( "");
                        datas.add("");
                        datas.add("");
                    } else {
                        datas.add(scoreEntry.getScore() + "");
                        datas.add(scoreEntry.getClassRank() + "");
                        datas.add(scoreEntry.getGradeRank() + "");
                    }
                }
                util.appendRow(cellStyle, datas.toArray());
            }
        }
        util.setFileName(String.format("%s_%s", examEntry.getName(), "_学生总排名表.xlsx"));
    }

    /**
     * 前X名分布表
     * @param util
     * @param examId
     * @throws Exception
     */
    public void export_topX(final ExportUtil util, final ObjectId examId) throws Exception{
        ExamEntry examEntry = examDao.load(examId.toString());
        List<ExamSubjectEntry> subjectEntries = examEntry.getExamSubject();

        List<String> datas = new ArrayList<String>();
        SXSSFWorkbook wb = util.getBook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 1);//左边框
        cellStyle.setBorderTop((short) 1);//上边框
        cellStyle.setBorderRight((short) 1);//右边框
        cellStyle.setBorderBottom((short) 1);//右边框

        datas.clear();
        datas.add("班级");
        datas.add("学号");
        datas.add("姓名");
        for(ExamSubjectEntry subjectEntry : subjectEntries){
            datas.add(subjectEntry.getSubjectName());
            datas.add(subjectEntry.getSubjectName());
            datas.add(subjectEntry.getSubjectName());
        }
        util.appendRow(cellStyle, datas.toArray());
        datas.clear();
        datas.add("班级");
        datas.add("学号");
        datas.add("姓名");
        for(ExamSubjectEntry subjectEntry : subjectEntries){
            datas.add("成绩");
            datas.add("班级排名");
            datas.add("年级排名");
        }
        util.appendRow(cellStyle, datas.toArray());

        Sheet sheet = util.getSheet();
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        for(int i=0; i<subjectEntries.size(); i++){
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 3*i+3, 3*i+5));
        }

        Map<String, ThreePlusThreeScoreEntry> map = new HashMap<String, ThreePlusThreeScoreEntry>();
        List<ThreePlusThreeScoreEntry> threePlusThreeScoreEntries = threePlusThreeScoreDao.getScoreEntries(examId, null, Constant.FIELDS);
        Set<ObjectId> studentIds = new HashSet<ObjectId>();
        for(ThreePlusThreeScoreEntry scoreEntry : threePlusThreeScoreEntries){
            studentIds.add(scoreEntry.getStudentId());
            map.put(scoreEntry.getStudentId() + "_" + scoreEntry.getSubjectId(), scoreEntry);
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(studentIds, new BasicDBObject("nm", 1).append("sn", 1));


        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(examEntry.getGradeId());
        for(ClassEntry classEntry : classEntries){
            List<ObjectId> stus = classEntry.getStudents();
            for(ObjectId stuId : stus){
                UserEntry userEntry = userEntryMap.get(stuId);
                if(userEntry == null)
                    continue;
                datas.clear();
                datas.add(classEntry.getName());
                datas.add(userEntry.getStudyNum());
                datas.add(userEntry.getUserName());
                for(ExamSubjectEntry subjectEntry : subjectEntries){
                    ThreePlusThreeScoreEntry scoreEntry = map.get(stuId.toString() + "_" + subjectEntry.getSubjectId().toString());
                    if(scoreEntry == null){
                        datas.add( "");
                        datas.add("");
                        datas.add("");
                    } else {
                        datas.add(scoreEntry.getScore() + "");
                        datas.add(scoreEntry.getClassRank() + "");
                        datas.add(scoreEntry.getGradeRank() + "");
                    }
                }
                util.appendRow(cellStyle, datas.toArray());
            }
        }
        util.setFileName(String.format("%s_%s", examEntry.getName(), "_学生总排名表.xlsx"));
    }

    private Map<String, ThreePlusThreeScoreEntry> formateStuSubScoreEntryMap(ObjectId examId){
        Map<String, ThreePlusThreeScoreEntry> map = new HashMap<String, ThreePlusThreeScoreEntry>();
        List<ThreePlusThreeScoreEntry> threePlusThreeScoreEntries = threePlusThreeScoreDao.getScoreEntries(examId, null, Constant.FIELDS);
        for(ThreePlusThreeScoreEntry scoreEntry : threePlusThreeScoreEntries){
            map.put(scoreEntry.getStudentId() + "_" + scoreEntry.getSubjectId(), scoreEntry);
        }
        return map;
    }


}
