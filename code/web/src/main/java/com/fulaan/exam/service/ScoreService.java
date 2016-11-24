package com.fulaan.exam.service;

import com.db.exam.ExamDao;
import com.db.exam.ScoreDao;
import com.db.school.ClassDao;
import com.fulaan.examresult.service.ScoreSummaryService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.zouban.dto.SubjectConfDTO;
import com.fulaan.zouban.dto.XuanKeDTO;
import com.fulaan.zouban.service.StudentXuankeService;
import com.fulaan.zouban.service.XuanKeService;
import com.pojo.app.IdValuePair;
import com.pojo.app.PageDTO;
import com.pojo.exam.*;
import com.pojo.exam.ScoreDTO;
import com.pojo.school.ClassEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.ImportException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Caocui on 2015/7/22.
 * 成绩信息服务类s
 */
@Service
public class ScoreService {
    private ScoreDao scoreDao = new ScoreDao();
    private ExamDao examDao = new ExamDao();
    private ClassDao classDao = new ClassDao();
    @Autowired
    private StudentXuankeService studentXuankeService;
    @Autowired
    private ScoreSummaryService scoreSummaryService;
    @Autowired
    private XuanKeService xuanKeService;

    /**
     * 生成考场的导入模板
     *
     * @param util
     * @param roomId
     * @param examId
     * @param subjects
     */
    public void createTemplateByExamRoom(final ExportUtil util, String roomId, String examId, List<ExamSubjectDTO> subjects) {
        List<ScoreEntry> scoreEntries = scoreDao.findScoreListByExamRoomId(roomId, examId);
        ExamEntry examEntry = this.examDao.load(examId);
        exportTemplate(util, subjects, scoreEntries, examEntry);
        String className = scoreEntries.size() > 0 ? scoreEntries.get(0).getClassName() : Constant.EMPTY;
        util.setFileName(String.format("%s_%s_%s", examEntry.getName(), className, "成绩导入模板.xlsx"));
    }

    /**
     * 根据班级编码导出模板
     */
    public void createTemplateByClass(final ExportUtil util, final String classId, String examId, List<ExamSubjectDTO> subjects) {
        ExamEntry examEntry = this.examDao.load(examId);
        List<ScoreEntry> scoreEntries = scoreDao.findScoreList(examId, new String[]{classId}, null);
        exportTemplate(util, subjects, scoreEntries, examEntry);
        String className = scoreEntries.size() > 0 ? scoreEntries.get(0).getClassName() : Constant.EMPTY;
        util.setFileName(String.format("%s_%s_%s", examEntry.getName(), className, "成绩导入模板.xlsx"));
    }

    /**
     * 根据年级编码导出模板
     */
    public void createTemplateByGrade(final ExportUtil util, String examId, List<ExamSubjectDTO> subjects) {
        ExamEntry examEntry = this.examDao.load(examId);
        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(examEntry.getGradeId());
        String[] classIds = new String[classEntries.size()];
        for(int i=0; i<classEntries.size(); i++){
            classIds[i] = classEntries.get(i).getID().toString();
        }
        List<ScoreEntry> scoreEntries = scoreDao.findScoreList(examId, classIds, null);
        exportTemplate(util, subjects, scoreEntries, examEntry);
        String gradeName = Constant.EMPTY;
        util.setFileName(String.format("%s_%s_%s", examEntry.getName(), gradeName, "成绩导入模板.xlsx"));
    }

    /**
     * 输出模板
     *
     * @param util
     * @param subjects
     * @param scoreEntries
     * @param examEntry
     */
    private void exportTemplate(final ExportUtil util, List<ExamSubjectDTO> subjects, List<ScoreEntry> scoreEntries, ExamEntry examEntry) {
        List<String> datas = new ArrayList<String>(subjects.size() + Constant.FOUR);
        datas.add("标识");
        datas.add("姓名");
        datas.add("班级");
        datas.add("考场");
        for (ExamSubjectDTO pair : subjects) {
            datas.add(pair.getSubjectName() + " {" + pair.getSubjectId() + '}');
        }
        util.addTitle(datas.toArray());

        //填充模板的数据区域
        datas.clear();
        ScoreDTO scoreDTO;
        for (ScoreEntry entry : scoreEntries) {
            scoreDTO = new ScoreDTO(entry);
            datas.add(scoreDTO.getId());
            datas.add(scoreDTO.getStudentName());
            datas.add(scoreDTO.getClassName());
            datas.add(scoreDTO.getExamRoomName());
//            for (ExamSubjectDTO pair : subjects) {
//                datas.add(scoreDTO.getExamScore().get(pair.getSubjectId()) == null
//                        ? "0" : scoreDTO.getExamScore().get(pair.getSubjectId()).getScore());
//            }
            util.appendRow(datas.toArray());
            datas.clear();
        }
    }

    /**
     * 导出数据
     *
     * @param examId   考试编码
     * @param classIds 班级编码
     * @param subjects 科目编码
     * @param oreder   排序  class OR score
     * @return
     */
    public void exportScoreByClass(final ExportUtil util, final String examId, final String[] classIds, String[] subjects, final String oreder) {
        ExamEntry examEntry = examDao.load(examId);
        List<ScoreDTO> exportData = loadDetail(examId, classIds, subjects, oreder);
        String className = exportData.size() > 0 ? exportData.get(0).getClassName() : Constant.EMPTY;
        exportScore(util, subjects, examEntry, exportData);
        util.setFileName(String.format("%s_%s_%s", examEntry.getName(), className, "考试成绩.xlsx"));
    }

    /**
     * 导出考试成绩
     *
     * @param util
     * @param examId
     * @param roomId
     * @param subjects
     */
    public void exportScoreByRoom(final ExportUtil util, final String examId, String roomId, String[] subjects) {
        ExamEntry examEntry = examDao.load(examId);
        List<ScoreDTO> exportData = loadDetailByExamRoomId(examId, subjects, roomId);
        String examRoomName = exportData.size() > 0 ? exportData.get(0).getExamRoomName() : Constant.EMPTY;
        exportScore(util, subjects, examEntry, exportData);
        util.setFileName(String.format("%s_%s_%s", examEntry.getName(), examRoomName, "考试成绩.xlsx"));
    }

    /**
     * 输出成绩文件
     *
     * @param util
     * @param subjects
     * @param examEntry
     * @param exportData
     */
    private void exportScore(final ExportUtil util, String[] subjects, ExamEntry examEntry, List<ScoreDTO> exportData) {
        //导出内容为科目名称、姓名，考场，班级，编码标识
        List<String> datas = new ArrayList<String>(subjects.length + Constant.FOUR);
        datas.add("姓名");
        datas.add("班级");
        datas.add("考场");
        List<ExamSubjectEntry> examSubjectEntry = examEntry.getExamSubject();
        List<String> subArray = Arrays.asList(subjects);
        for (ExamSubjectEntry pair : examSubjectEntry) {
            if (subArray.contains(pair.getSubjectId().toString())) {
                datas.add(pair.getSubjectName());
            }
        }
        util.addTitle(datas.toArray());
        //获取导出数据
        for (ScoreDTO scoreDTO : exportData) {
            datas.clear();
            datas.add(scoreDTO.getStudentName());
            datas.add(scoreDTO.getClassName());
            datas.add(scoreDTO.getExamRoomName());
            for (String sub : subjects) {
                datas.add(scoreDTO.getSubjectScore(sub));
            }
            util.appendRow(datas.toArray());
        }
    }

    /**
     * 根据条件获取成绩信息
     *
     * @param examId   考试编码
     * @param classIds 班级编码
     * @param subjects 科目编码
     * @param oreder   排序  class OR score
     * @return
     */
    public List<ScoreDTO> loadDetail(final String examId, final String[] classIds, String[] subjects, final String oreder) {
        List<ScoreEntry> scoreEntries = scoreDao.findScoreList(examId, classIds, oreder);
        List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>(scoreEntries.size());
        for (ScoreEntry scoreEntry : scoreEntries) {
            scoreDTOs.add(new ScoreDTO(scoreEntry));
        }

        //删除不需要的成绩数据
        if (subjects != null && subjects.length != 0) {
            Map<String, SubjectScoreDTO> score;
            //剔除数据
            for (ScoreDTO dto : scoreDTOs) {
                score = dto.getExamScore();
                //剔除数据
                for (String subj : subjects) {
                    if (!score.containsKey(subj)) {
                        score.remove(subj);
                    }
                }
            }
        }
        return scoreDTOs;
    }

    /**
     * 根据考场加载考试成绩
     *
     * @param examId
     * @param roomId
     * @param subjects
     */
    public List<ScoreDTO> loadDetailByExamRoomId(final String examId, String[] subjects, String roomId) {
        List<ScoreEntry> scoreEntries = scoreDao.findScoreListByExamRoomId( examId, roomId);
        List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>(scoreEntries.size());
        for (ScoreEntry scoreEntry : scoreEntries) {
            scoreDTOs.add(new ScoreDTO(scoreEntry));
        }

        //删除不需要的成绩数据
        if (subjects != null && subjects.length != 0) {
            Map<String, SubjectScoreDTO> score;
            //剔除数据
            for (ScoreDTO dto : scoreDTOs) {
                score = dto.getExamScore();
                //剔除数据
                for (String subj : subjects) {
                    if (!score.containsKey(subj)) {
                        score.remove(subj);
                    }
                }
            }
        }
        return scoreDTOs;
    }


    /**
     * 修改成绩
     *
     * @param scoreDTO
     * @return
     */
    public void update(final ScoreDTO scoreDTO) {
        scoreDao.updateScore(scoreDTO);
    }

    /**
     * 获取考试中缺免考数据信息
     *
     * @param examId
     * @param classId
     * @param showType
     * @param page
     * @return
     */
    public PageDTO<ScoreDTO> loadQMExamStatus(final String examId, final String classId, String showType, String subjectId, int page) {
        int count = scoreDao.countQmExamStatus(examId, classId, showType, subjectId);
        List<ScoreDTO> scoreDTOs = Collections.EMPTY_LIST;
        int pageTotal = (count + Constant.TEN - 1) / Constant.TEN;
        if (pageTotal > 0) {
            page = page > pageTotal ? pageTotal : page;
            List<ScoreEntry> scoreEntries = scoreDao.loadQMExamStatus(examId, classId, showType, subjectId, page);
            scoreDTOs = new ArrayList<ScoreDTO>(scoreEntries.size());
            for (ScoreEntry scoreEntry : scoreEntries) {
                scoreDTOs.add(new ScoreDTO(scoreEntry));
            }
        }
        PageDTO<ScoreDTO> pageDTO = new PageDTO<ScoreDTO>();
        pageDTO.setList(scoreDTOs);
        pageDTO.setCount(pageTotal);
        return pageDTO;
    }

    /**
     * 缺免考设置
     *
     * @param scoreId
     * @param subjectId
     * @param showType
     */
    public void updateQMStatus(String scoreId, String subjectId, int showType) {
        scoreDao.updateQMStatus(scoreId, subjectId, showType);
    }

    /**
     * 导入数据
     *
     * @param data
     */
    public void updateByImport(List<ScoreDTO> data, String examId, boolean isManage) throws ImportException {
        if (!data.isEmpty() && !isManage) {
            //校验科目成绩是否可以编辑
            Set<String> subjects = data.get(0).getExamScore().keySet();
            ExamSubjectEntry entry;
            for (String one : subjects) {
                entry = examDao.loadExamSubject(examId, one);
                if (entry == null) {
                    throw new ImportException("导入成绩科目未找到");
                }
                if (!new ExamSubjectDTO(entry).isCanEdit()) {
                    throw new ImportException(entry.getSubjectName() + "成绩当前不可以编辑");
                }
            }
        }
        scoreDao.updateByImport(data);
        try{
            scoreSummaryService.importScoreTmplExcel(data, new ObjectId(examId));
        } catch (Exception e){
            e.printStackTrace();
            throw new ImportException(e.getMessage());
        }
    }

    /**
     * 获取考试学生总数
     *
     * @param examId
     * @return
     */
    public int countExamStudent(final String examId) {
        return scoreDao.countExamStudent(new ObjectId(examId));
    }

    /**
     * 获取未分配考场的学生总数
     *
     * @param examId
     * @return
     */
    public int countStudentNotArranged(final String examId) {
        return scoreDao.countStudentNotArranged(new ObjectId(examId));
    }


    /**
     * 获取考试的排考场情况
     *
     * @param examId
     * @param classId
     * @param roomId
     * @return
     */
    public List<ScoreDTO> loadArrangeInfo(final String schoolId, final String examId, final String classId, final String roomId) {
        List<ScoreDTO> scoreDTOList =  scoreDao.loadArrangeInfo(examId, classId, roomId);

        ExamEntry examEntry = examDao.load(examId);
        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf("2015-2016学年", examEntry.getGradeId().toString(), 1, schoolId);
        if(xuanKeDTO != null){//存在走班
            List<String> ss = new ArrayList<String>();
            List<SubjectConfDTO> subjectConfDTOs = xuanKeDTO.getSubConfList();
            if(subjectConfDTOs!=null && subjectConfDTOs.size()>0){
                for(SubjectConfDTO subjectConfDTO : subjectConfDTOs){
                    ss.add(subjectConfDTO.getSubjectId());
                }
            }

            List<ObjectId> zbSubjects = new ArrayList<ObjectId>();
            List<ExamSubjectEntry> examSubjectEntries = examEntry.getExamSubject();
            if(examSubjectEntries != null && examSubjectEntries.size() > 0){
                for(ExamSubjectEntry examSubjectEntry : examSubjectEntries){
                    if(ss.contains(examSubjectEntry.getSubjectId().toString())) {
                        zbSubjects.add(examSubjectEntry.getSubjectId());
                    }
                }
            }

            if(scoreDTOList!= null && scoreDTOList.size()>0){
                List<ObjectId> stuIds = new ArrayList<ObjectId>();
                for(ScoreDTO scoreDTO : scoreDTOList){
                    stuIds.add(new ObjectId(scoreDTO.getStudentId()));
                }
                Map<String,Map<String,List<IdValuePair>>> model = studentXuankeService.getStudentChooseEntrys(stuIds, new ObjectId(xuanKeDTO.getXuankeId()), schoolId);
                if(model.size() > 0){
                    for(ScoreDTO scoreDTO : scoreDTOList){
                        Map<String,List<IdValuePair>> xuanke = model.get(scoreDTO.getStudentId());
                        List<IdValuePair> adv = xuanke.get("adv");
                        List<IdValuePair> sim = xuanke.get("sim");
                        if(adv.size() <= 0){
                            break;
                        }
                        List<Map<String, Object>> zoubanSubject = new ArrayList<Map<String, Object>>();
                        for(ObjectId subjectId : zbSubjects){
                            Boolean flag = false;
                            for(IdValuePair idValuePair : adv){
                                if(idValuePair.getId().equals(subjectId)){
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("id", subjectId.toString());
                                    map.put("name", idValuePair.getValue());
                                    map.put("ty", 1);
                                    zoubanSubject.add(map);
                                    flag = true;
                                    break;
                                }
                            }
                            if(!flag) {
                                for (IdValuePair idValuePair : sim) {
                                    if (idValuePair.getId().equals(subjectId)) {
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("id", subjectId.toString());
                                        map.put("name", idValuePair.getValue());
                                        map.put("ty", 2);
                                        zoubanSubject.add(map);
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                            if(!flag) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("id", subjectId.toString());
                                map.put("name", "未选");
                                map.put("ty", 0);
                                zoubanSubject.add(map);
                            }
                        }

                        scoreDTO.setZoubanSubject(zoubanSubject);
                    }
                }

            }
        }


        return scoreDTOList;
    }

    /**
     * 根据校名词或班级名称排序查看
     * Created by Zoukai on 2015/11
     */
    public List<ScoreEntry> findByRanking(ObjectId exid,int skip, int size){
        ScoreDao sd=new ScoreDao();
        List<ScoreEntry> list=sd.findByRanking(exid,skip,size);
        return list;
    }

    /**
     * 根据考试编码查询考试信息
     * @param exid "exId"
     */
    public List<ScoreEntry> findByExid(ObjectId exid,int skip, int size){
        List<ScoreEntry> list=scoreDao.findByExid(exid,skip,size);
        return list;
    }

    /**
     * 根据考试编码查询考试信息 （无分页）
     * @param exid "exId"
     */
    public List<ScoreEntry> findByExidWithoutPage(ObjectId exid){
        List<ScoreEntry> list = scoreDao.findByExid(exid);
        return list;
    }

    /**
     * 根据ID更新学校Ranking
     */
    public void updateSchoolRankById(ObjectId id , int rank){
        scoreDao.updateSchoolRankById(id, rank);

    }
}
