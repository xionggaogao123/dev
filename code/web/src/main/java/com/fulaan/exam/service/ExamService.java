package com.fulaan.exam.service;

import com.db.exam.ExamDao;
import com.db.exam.ScoreDao;
import com.db.examresult.PerformanceDao;
import com.db.examscoreanalysis.ThreePlusThreeScoreDao;
import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.db.zouban.ZouBanCourseDao;
import com.db.zouban.ZoubanStateDao;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.TermUtil;
import com.fulaan.zouban.dto.SubjectConfDTO;
import com.fulaan.zouban.service.SubjectConfService;
import com.fulaan.zouban.service.XuanKeService;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.PageDTO;
import com.pojo.exam.*;
import com.pojo.examresult.PerformanceEntry;
import com.pojo.examscoreanalysis.ThreePlusThreeScoreEntry;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZouBanCourseEntry;
import com.pojo.zouban.ZoubanState;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Caocui on 2015/7/22.
 * 考试信息服务类
 */
@Service
public final class ExamService {
    private ExamDao examDao = new ExamDao();
    private ClassDao classDao = new ClassDao();
    private UserDao userDao = new UserDao();
    private ScoreDao scoreDao = new ScoreDao();
    private PerformanceDao performanceDao = new PerformanceDao();
    private ZoubanStateDao zoubanStateDao = new ZoubanStateDao();
    private ThreePlusThreeScoreDao threePlusThreeScoreDao = new ThreePlusThreeScoreDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private XuanKeService xuanKeService;
    @Autowired
    private SubjectConfService subjectConfService;

    /**
     * 获取成绩信息列表
     *
     * @return
     */
    public PageDTO<ExamDTO> findExamByGradeId(final String gradeId, int page) {
        int count = examDao.loadExamCount(gradeId);
        List<ExamDTO> examDTOs = Collections.EMPTY_LIST;
        int pageTotal = (count + Constant.TEN - 1) / Constant.TEN;
        if (pageTotal > 0) {
            page = page > pageTotal ? pageTotal : page;
            List<ExamEntry> entries = examDao.findExamByGradeId(gradeId, page);
            examDTOs = new ArrayList<ExamDTO>(entries.size());
            for (ExamEntry entry : entries) {
                examDTOs.add(new ExamDTO(entry));
            }
        }
        PageDTO<ExamDTO> pageDTO = new PageDTO<ExamDTO>();
        pageDTO.setCount(pageTotal);
        pageDTO.setList(examDTOs);
        return pageDTO;
    }

    /**
     * 获取考试成绩详情
     *
     * @return
     */
    public ExamDTO loadExam(final String examId) {
        ExamEntry entry = examDao.load(examId);
        if (entry != null) {
            return new ExamDTO(entry);
        }
        return null;
    }
    
    /**
     * 获取考试Entry
     *
     * @return
     */
    public ExamEntry findExamEntry(final String examId) {
    	return examDao.load(examId);
        
    }

    /**
     * 获取考试科目的配置信息
     *
     * @param examId
     * @param subjectId
     * @return
     */
    public ExamSubjectEntry loadExamSubject(final String examId, final String subjectId) {
        return examDao.loadExamSubject(examId, subjectId);
    }


    /**
     * 修改成绩
     *
     * @param examDTO
     * @return
     */
    public void update(final ExamDTO examDTO) throws Exception{
        if (StringUtils.isEmpty(examDTO.getId())) {
            ExamEntry examEntry = examDTO.getEntry();
            examEntry.setIsGra(1);
            examDao.save(examEntry);

            List<SubjectScoreEntry> scoreList = new ArrayList<SubjectScoreEntry>(examDTO.getExamSubjectDTO().size());
            for (ExamSubjectDTO subjectDTO : examDTO.getExamSubjectDTO()) {
                scoreList.add(new SubjectScoreEntry(new ObjectId(subjectDTO.getSubjectId()), subjectDTO.getSubjectName(),
                        Constant.ZERO, Constant.ZERO, subjectDTO.getFullMarks(), subjectDTO.getFullMarks() * 0.6f, Constant.ZERO, Constant.ZERO));
            }
            //新增考试，预初始化各班级考试成绩信息，分考场时修改学生考试的考场信息
            //获取年级的学生信息
            List<ClassEntry> classes = classDao.findClassEntryByGradeId(new ObjectId(examDTO.getGradeId()));
            List<ScoreEntry> entryScore;
            UserEntry entryUser;
            for (ClassEntry clazz : classes) {
                entryScore = new ArrayList<ScoreEntry>(clazz.getTotalStudent());
                for (ObjectId obj : clazz.getStudents()) {
                    entryUser = userDao.getUserEntry(obj, new BasicDBObject("nm", ""));
                    entryScore.add(new ScoreEntry(examEntry.getID(),
                            entryUser.getID(), entryUser.getUserName(),
                            clazz.getID(), clazz.getName(), null, "",
                            scoreList, "", ""));
                }
                scoreDao.add(entryScore);
            }
        } else {
            //修改记录
            examDao.update(examDTO);

            //获取当前传入的老记录编码
            List<ObjectId> oldObjId = new ArrayList<ObjectId>(examDTO.getExamSubjectDTO().size());
            for (ExamSubjectDTO dto : examDTO.getExamSubjectDTO()) {
                if (!StringUtils.isEmpty(dto.getId())) {
                    oldObjId.add(new ObjectId(dto.getSubjectId()));
                }
            }
            //去除被删除的科目信息
            examDao.deleteExamSubject(oldObjId, new ObjectId(examDTO.getId()));

            //删除被删除的科目信息对应的成绩
            scoreDao.deleteExamSubject(new ObjectId(examDTO.getId()), oldObjId);

            //更新科目编码列表

            //修改课程信息
            for (ExamSubjectDTO dto : examDTO.getExamSubjectDTO()) {
                if (StringUtils.isEmpty(dto.getId())) {
                    examDao.saveExamSubject(new ObjectId(examDTO.getId()), dto);
                    //为本次考试的学生预添加科目成绩
                    scoreDao.addScoreSubject(new ObjectId(examDTO.getId()), dto);
                } else {
                    examDao.updateExamSubject(new ObjectId(examDTO.getId()), dto);
                    scoreDao.updateExamScoreFullMarks(new ObjectId(examDTO.getId()), dto);
                }
            }

        }
    }

    /**
     * 新版更新，加入3+3
     * 把考场信息和成绩信息分开存放
     * @param examDTO
     * @throws Exception
     */
    public void updateNew(final ExamDTO examDTO) throws Exception{
        //如果是3+3考试，判断是否排课完成
        int type = examDTO.getType();
        if(type == 0){
            throw new Exception("请先选择模式");
        }
        if(type == 1 || checkThreePlusThree(new ObjectId(examDTO.getGradeId()))) {//普通模式 或 满足3+3模式
            if (StringUtils.isEmpty(examDTO.getId())) {
                ExamEntry examEntry = examDTO.getEntry();
                examEntry.setIsGra(1);
                examDao.save(examEntry);

                List<SubjectScoreEntry> scoreList = new ArrayList<SubjectScoreEntry>(examDTO.getExamSubjectDTO().size());
                List<ObjectId> subjectIds = new ArrayList<ObjectId>();
                for (ExamSubjectDTO subjectDTO : examDTO.getExamSubjectDTO()) {
                    scoreList.add(new SubjectScoreEntry(new ObjectId(subjectDTO.getSubjectId()), subjectDTO.getSubjectName(),
                            Constant.ZERO, Constant.ZERO, subjectDTO.getFullMarks(), subjectDTO.getFullMarks() * 0.6f, Constant.ZERO, Constant.ZERO));
                    subjectIds.add(new ObjectId(subjectDTO.getSubjectId()));
                }
                if(type == 2){
                    isSubjectSupported(subjectIds, examEntry);
                }
                //新增考试，预初始化各班级考试成绩信息，分考场时修改学生考试的考场信息
                //获取年级的学生信息
                List<ClassEntry> classes = classDao.findClassEntryByGradeId(new ObjectId(examDTO.getGradeId()));
                List<ScoreEntry> entryScore;
                List<ThreePlusThreeScoreEntry> threePlusThreeScoreEntries = new ArrayList<ThreePlusThreeScoreEntry>();
                UserEntry entryUser;
                for (ClassEntry clazz : classes) {
                    entryScore = new ArrayList<ScoreEntry>(clazz.getTotalStudent());
                    for (ObjectId obj : clazz.getStudents()) {
                        entryUser = userDao.getUserEntry(obj, new BasicDBObject("nm", ""));
                        entryScore.add(new ScoreEntry(examEntry.getID(),
                                entryUser.getID(), entryUser.getUserName(),
                                clazz.getID(), clazz.getName(), null, "",
                                scoreList, "", ""));
                    }
                    threePlusThreeScoreEntries.addAll(bulidThreePlusThreeScoreEntries(clazz,subjectIds, examEntry, type == 2));
                    scoreDao.add(entryScore);
                }
                if(threePlusThreeScoreEntries.size() > 0){
                    threePlusThreeScoreDao.insert(threePlusThreeScoreEntries);
                }
            } else {
                //修改记录
                examDao.update(examDTO);
                ObjectId examId = new ObjectId(examDTO.getId());
                ExamEntry examEntry = examDao.load(examDTO.getId());

                //获取当前传入的老记录编码
                List<ObjectId> oldObjId = new ArrayList<ObjectId>(examDTO.getExamSubjectDTO().size());
                List<ObjectId> allSubjectIds = new ArrayList<ObjectId>();
                for (ExamSubjectDTO dto : examDTO.getExamSubjectDTO()) {
                    allSubjectIds.add(new ObjectId(dto.getSubjectId()));
                    if (!StringUtils.isEmpty(dto.getId())) {
                        oldObjId.add(new ObjectId(dto.getSubjectId()));
                    }
                }
                if(type == 2){
                    isSubjectSupported(allSubjectIds, examEntry);
                }
                //去除被删除的科目信息
                examDao.deleteExamSubject(oldObjId, examId);

                //删除被删除的科目信息对应的成绩
                threePlusThreeScoreDao.removeScoreEntries(examId, oldObjId);

                //修改课程信息
                List<ObjectId> subjectIds = new ArrayList<ObjectId>();
                for (ExamSubjectDTO dto : examDTO.getExamSubjectDTO()) {
                    if (StringUtils.isEmpty(dto.getId())) {
                        examDao.saveExamSubject(new ObjectId(examDTO.getId()), dto);
                        //为本次考试的学生预添加科目成绩
                        scoreDao.addScoreSubject(new ObjectId(examDTO.getId()), dto);
                        subjectIds.add(new ObjectId(dto.getSubjectId()));
                    } else {
                        examDao.updateExamSubject(new ObjectId(examDTO.getId()), dto);
                        scoreDao.updateExamScoreFullMarks(new ObjectId(examDTO.getId()), dto);
                    }
                }

                //给新增加的学科添加成绩记录
                if(subjectIds.size() > 0){
                    List<ClassEntry> classes = classDao.findClassEntryByGradeId(new ObjectId(examDTO.getGradeId()));
                    List<ThreePlusThreeScoreEntry> threePlusThreeScoreEntries = new ArrayList<ThreePlusThreeScoreEntry>();
                    for (ClassEntry clazz : classes) {
                        threePlusThreeScoreEntries.addAll(bulidThreePlusThreeScoreEntries(clazz, subjectIds, examEntry, type == 2));
                    }
                    threePlusThreeScoreDao.insert(threePlusThreeScoreEntries);
                }

            }
        } else {
            throw new Exception("本学年走班课未发布课表，不能新建走班考试，请先完成分班排课");
        }
    }

    /**
     * 检查科目是否在走班课和非走班课中
     * @return
     */
    private void isSubjectSupported(List<ObjectId> subjectIds, ExamEntry examEntry) throws Exception{
        List<ObjectId> supportedSubjects = new ArrayList<ObjectId>();
        List<SubjectConfDTO> zoubanSubjects = subjectConfService.getSubjectConfList(TermUtil.getSchoolYear(), examEntry.getGradeId().toString(), 1, examEntry.getSchoolId().toString());
        List<SubjectConfDTO> feizoubanSubjects = subjectConfService.getSubjectConfList(TermUtil.getSchoolYear(), examEntry.getGradeId().toString(), 2, examEntry.getSchoolId().toString());
        zoubanSubjects.addAll(feizoubanSubjects);
        for(SubjectConfDTO confDTO : zoubanSubjects){
            supportedSubjects.add(new ObjectId(confDTO.getSubjectId()));
        }
        for(ObjectId subjectId : subjectIds){
            if(!supportedSubjects.contains(subjectId)){
                throw new Exception("目前只支持语、数、英、物、化、生、政、史、地、九门科目");
            }
        }

    }

    private List<ThreePlusThreeScoreEntry> bulidThreePlusThreeScoreEntries(ClassEntry classEntry, List<ObjectId> subjectIds, ExamEntry examEntry, Boolean isThreePlusThree){
        List<ThreePlusThreeScoreEntry> threePlusThreeScoreEntries = new ArrayList<ThreePlusThreeScoreEntry>();
        List<ObjectId> studentIds = classEntry.getStudents();
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(studentIds, new BasicDBObject("nm", 1));
        List<SubjectConfDTO> zoubanSubjects = new ArrayList<SubjectConfDTO>();
        if(isThreePlusThree)
            zoubanSubjects = subjectConfService.getSubjectConfList(TermUtil.getSchoolYear(), examEntry.getGradeId().toString(), 1, examEntry.getSchoolId().toString());
        List<ObjectId> zoubanSubjectIds = new ArrayList<ObjectId>();
        for(SubjectConfDTO subjectConfDTO : zoubanSubjects){
            zoubanSubjectIds.add(new ObjectId(subjectConfDTO.getSubjectId()));
        }
        for (ObjectId studentId : studentIds) {
            UserEntry userEntry = userEntryMap.get(studentId);
            if(userEntry == null){
                continue;
            }
            for(ObjectId subjectId : subjectIds){
                ZouBanCourseEntry zouBanCourseEntry = getTeachingClassId(studentId, subjectId, examEntry.getGradeId());
                if(zouBanCourseEntry == null && isThreePlusThree)
                    continue;
                //走班模式 && 走班科目 ？ 教学班名称 ：行政班名称
                String className = isThreePlusThree && zoubanSubjectIds.contains(studentId) ? zouBanCourseEntry.getClassName() : classEntry.getName();
                threePlusThreeScoreEntries.add(new ThreePlusThreeScoreEntry(examEntry.getID(), studentId, subjectId, classEntry.getID(),
                        zouBanCourseEntry==null ? null : zouBanCourseEntry.getID(), 0, 0, 0, 0, 0, className));
            }
        }
        return threePlusThreeScoreEntries;
    }

    private ZouBanCourseEntry getTeachingClassId(ObjectId studentId, ObjectId subjectId, ObjectId gradeId){
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getStudentChooseZB(studentId, TermUtil.getSchoolYear(), gradeId, subjectId);
        return zouBanCourseEntry;
    }

    private Boolean checkThreePlusThree(ObjectId gradeId){
        String schoolYear = TermUtil.getSchoolYear();
        ZoubanState zoubanState = zoubanStateDao.findZoubanState(gradeId, schoolYear);
        if(zoubanState != null && zoubanState.getState() >= 5){
            return true;
        }
        return false;
    }

    /**
     * 删除考试信息
     *
     * @param id
     */
    public void delete(final String id) {
        examDao.delete(id);
    }

    /**
     * 修改考试科目信息
     *
     * @param examSubjectDTO
     */
    public void updateExamSubjectOpenStatus(final ExamSubjectDTO examSubjectDTO) {
        examDao.updateExamSubjectOpenTime(examSubjectDTO);
    }

    /**
     * 锁定分配考场
     *
     * @param examId
     */
    public void lockArrange(final String examId, final int isLock) {
        examDao.lockArrange(examId, isLock);
    }

    /**
     * 导出考试的排考场信息
     *
     * @param util
     * @param examId
     * @param classId
     * @param roomId
     */
    public void exportArrangeInfo(final String schoolId, final ExportUtil util, final String examId, final String classId, final String roomId) {
        List<String> datas = new ArrayList<String>(Constant.FIVE);
        if (StringUtils.isNotEmpty(classId)) {
            datas.add("班级");
            datas.add("考场名称");
            datas.add("考场编号");
        } else {
            datas.add("考场名称");
            datas.add("考场编号");
            datas.add("班级");
        }
        datas.add("考号");
        datas.add("考生姓名");

//        List<ScoreDTO> scoreDTOs = scoreDao.loadArrangeInfo(examId, classId, roomId);
        List<ScoreDTO> scoreDTOs = scoreService.loadArrangeInfo(schoolId, examId, classId, roomId);
        List<Map<String, Object>> zbsubjects = scoreDTOs.get(0).getZoubanSubject();
        if(zbsubjects!=null && zbsubjects.size()>0){
            for(Map<String, Object> map : zbsubjects){
                datas.add((String)map.get("name"));
            }
        }
        util.addTitle(datas.toArray());


        ExamEntry examEntry = examDao.load(examId);
        //获取导出数据
        if (StringUtils.isNotEmpty(classId)) {//按班级导出
            for (ScoreDTO scoreDTO : scoreDTOs) {
                datas.clear();
                datas.add(scoreDTO.getClassName());
                datas.add(scoreDTO.getExamRoomName());
                datas.add(scoreDTO.getExamRoomNumber());
                datas.add(scoreDTO.getExamNumber());
                datas.add(scoreDTO.getStudentName());

                List<Map<String, Object>> zbsubs = scoreDTO.getZoubanSubject();
                if(zbsubs!=null && zbsubs.size()>0){
                    for(Map<String, Object> map : zbsubs){
                        String type = "";
                        if((Integer)map.get("ty") == 1){
                            type = "等级考";
                        } else if((Integer)map.get("ty") == 2){
                            type = "合格考";
                        }
                        datas.add(type);
                    }
                }
                util.appendRowForKW(datas.toArray());
            }
        } else {//按考场导出或导出全部
            for (ScoreDTO scoreDTO : scoreDTOs) {
                datas.clear();
                datas.add(scoreDTO.getExamRoomName());
                datas.add(scoreDTO.getExamRoomNumber());
                datas.add(scoreDTO.getClassName());
                datas.add(scoreDTO.getExamNumber());
                datas.add(scoreDTO.getStudentName());

                List<Map<String, Object>> zbsubs = scoreDTO.getZoubanSubject();
                if(zbsubs!=null && zbsubs.size()>0){
                    for(Map<String, Object> map : zbsubs){
                        String type = "";
                        if((Integer)map.get("ty") == 1){
                            type = "等级考";
                        } else if((Integer)map.get("ty") == 2){
                            type = "合格考";
                        }
                        datas.add(type);
                    }
                }
                util.appendRowForKW(datas.toArray());
            }
        }
        util.setFileName(String.format("%s_%s", examEntry.getName(), "_考场安排.xlsx"));
    }

    /**
     * 按考号导出
     * @param examId
     * @param classId
     * @param roomId
     */
    public void exportArrangeInfo1(final String schoolId, final String examId, final String classId, final String roomId, HttpServletResponse response) {
        List<ScoreDTO> scoreDTOs = scoreService.loadArrangeInfo(schoolId, examId, classId, roomId);
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行



        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 30); // 字体高度
        font.setFontName("宋体"); // 字体

        HSSFFont font1 = workbook.createFont();
        font1.setFontHeightInPoints((short) 8); // 字体高度
        font1.setFontName("宋体"); // 字体

        int rowNo = 0;
        int i = 0;
        Row row = sheet.createRow(rowNo);
        for (ScoreDTO scoreDTO : scoreDTOs) {
            if(i % 4 == 0){
                row = sheet.createRow(rowNo);
                rowNo ++;
            }

            row.setHeight((short)2000);
            Cell cell = row.createCell(i % 4);
            cell.setCellStyle(cellStyle);

            String subs = "";
            List<Map<String, Object>> zbsubs = scoreDTO.getZoubanSubject();
            if(zbsubs!=null && zbsubs.size()>0){
                for(Map<String, Object> map : zbsubs){
                    String type = "";
                    if((Integer)map.get("ty") == 1){
                        type = "等级考";
                    } else if((Integer)map.get("ty") == 2){
                        type = "合格考";
                    }
                    subs += map.get("name").toString() + "(" + type + ") ";
                }
            }
            if(!subs.equals("")){
                subs = "\n" + subs;
            }


            String[] subStr = {
                    scoreDTO.getStudentName(), "考场编号" + scoreDTO.getExamRoomNumber() + " 考号" + scoreDTO.getExamNumber() + subs
            };
            String sText = subStr[0] + "\n" + subStr[1];
            HSSFRichTextString textString = new HSSFRichTextString(sText);
            textString.applyFont(
                    sText.indexOf(subStr[0]),
                    sText.indexOf(subStr[0]) + subStr[0].length(),
                    font
            );
            textString.applyFont(
                    sText.indexOf(subStr[1]),
                    sText.indexOf(subStr[1]) + subStr[1].length(),
                    font1
            );
            cell.setCellValue(textString);


            i++;
        }


        ExamEntry examEntry = examDao.load(examId);

        outPutWorkBook(workbook, response, examEntry.getName());
    }

    private void outPutWorkBook(HSSFWorkbook wb, HttpServletResponse response, String schoolName){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(schoolName + "_考场安排.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 考生考场编排
     *
     * @param classStudent
     * @param roomInfos
     */
    public Map<String, Integer> arrangeExamRoom(final String examId, final Map<String, List<ObjectId>> classStudent, List<ExamRoomDTO> roomInfos, int type) throws Exception{
        List<List<ObjectId>> student = new ArrayList<List<ObjectId>>(Constant.THREE);
        if(type == 0){
            unionClass(classStudent, student);
        } else {
            unionClassByPerformance(classStudent, student, examId);
        }

        List<RoomSeat> roomSeats = new ArrayList<RoomSeat>(roomInfos.size());
        RoomSeat roomSeat;
        for (ExamRoomDTO examRoomDTO : roomInfos) {
            roomSeat = new RoomSeat(examRoomDTO, student);
            roomSeat.setStuSeat();
            roomSeats.add(roomSeat);
        }
        Map<String, Integer> seatArrange = new HashMap<String, Integer>(roomInfos.size());
        //保存编排信息
        int curr;
        //清除已编排考场信息
        ObjectId examID = new ObjectId(examId);
        examDao.deleteExamRoom(examID);
        scoreDao.clearExamRoom(examID);
        int arrangedStudent = 0;
        for (RoomSeat seat : roomSeats) {
            curr = 0;
            for (int i = 0; i < seat.getROWS(); i++) {
                for (int j = 0; j < seat.getCOLS(); j++) {
                    //如果当前座位没有人，继续下一座位
                    if (seat.getSeat()[i][j] == null) {
                        continue;
                    }

                    //超过考场编排人数时结束
                    if (++curr > seat.getCurrSize()) {
                        break;
                    }
                    scoreDao.updateExamSeat(examID, seat.getRoom(), seat.getSeat()[i][j], curr < 10 ? "0" + curr : curr + Constant.EMPTY);
                }
            }
            arrangedStudent += seat.getCurrSize();
            examDao.addExamRoom(examID, seat.getCurrSize(), seat.getRoom());
            seatArrange.put(seat.getRoom().getId(), seat.getCurrSize());
        }
        int countStudent = scoreDao.countExamStudent(examID);
        examDao.updateExamroomArrange(examID, arrangedStudent >= countStudent ? 1 : 0);
        return seatArrange;
    }

    /**
     * 合并班级学生
     *
     * @param classStudent
     * @param lists
     */
    private void unionClass(Map<String, List<ObjectId>> classStudent, List<List<ObjectId>> lists) {
        //TODO 根据2为数组的奇偶坐标分为2组数据和辅助数据
        List<ObjectId> one = new LinkedList<ObjectId>();
        List<ObjectId> two = new LinkedList<ObjectId>();

        lists.add(one);
        lists.add(two);
        boolean hasOnly = classStudent.size() % 2 != 0;
        Iterator<String> iterator = classStudent.keySet().iterator();
        int curr = 0;
        List<ObjectId> three;
        int oneSize, twoSize, threeSize;
        while (iterator.hasNext()) {
            //如果有单独数据
            if (++curr == classStudent.size() && hasOnly) {
                oneSize = one.size();
                twoSize = two.size();
                three = classStudent.get(iterator.next());
                threeSize = three.size();
                //平分最后一个班级的数据
                if (oneSize - twoSize > threeSize) {
                    two.addAll(three);
                } else if (twoSize - oneSize > threeSize) {
                    one.addAll(three);
                } else if (twoSize > oneSize) {
                    int oneAdd = twoSize - oneSize + (threeSize - (twoSize - oneSize)) / 2;
                    //最后一个班级的数据，第一组加在开始，第二组加载结束，保证不在一个考场之中重复
                    one.addAll(0, three.subList(0, oneAdd));
                    two.addAll(three.subList(oneAdd, threeSize));
                } else {
                    int twoAdd = oneSize - twoSize + (threeSize - (twoSize - oneSize)) / 2;
                    one.addAll(0, three.subList(twoAdd, threeSize));
                    two.addAll(three.subList(0, twoAdd));
                }
                break;
            }
            //将数据分为2组
            if (curr % 2 == 0) {
                one.addAll(classStudent.get(iterator.next()));
            } else {
                two.addAll(classStudent.get(iterator.next()));
            }
        }
        Comparator<ObjectId> comparator = new Comparator<ObjectId>() {
            @Override
            public int compare(ObjectId o1, ObjectId o2) {
                int randomO1 = new Random(o1.hashCode()).nextInt();
                int randomO2 = new Random(o2.hashCode()).nextInt();
                return randomO1 > randomO2 ? 1 : randomO2 > randomO1 ? -1 : 0;
            }
        };
        //安排考场顺序随机分配

        Collections.sort(one, comparator);
        Collections.sort(two, comparator);
    }

    /**
     * 按成绩合并班级学生
     * @param classStudent
     * @param lists
     */
    private void unionClassByPerformance(Map<String, List<ObjectId>> classStudent, List<List<ObjectId>> lists, String examId) throws Exception{
        //TODO 根据2为数组的奇偶坐标分为2组数据和辅助数据
        List<ObjectId> one = new LinkedList<ObjectId>();
        List<ObjectId> two = new LinkedList<ObjectId>();

        lists.add(one);
        lists.add(two);

        ExamEntry examEntry = examDao.load(examId);
        List<ExamEntry> examEntries = examDao.findExamByGradeId(examEntry.getGradeId().toString(), 1);
        if(examEntries.size() <= 1){
            throw new Exception("没有过往考试成绩，无法按考试成绩排考场");
        }
        ObjectId exId = examEntries.get(examEntries.size() - 1).getID();
        if(examId.equals(exId.toString())){
            exId = examEntries.get(examEntries.size() - 2).getID();
        }
        List<PerformanceEntry> performanceEntries = performanceDao.getPerformanceEntryList(exId, null, null, null, null);
        Map<ObjectId, Double> stuScoreMap = new HashMap<ObjectId, Double>();
        for(PerformanceEntry performanceEntry : performanceEntries){
            stuScoreMap.put(performanceEntry.getStudentId(), performanceEntry.getScoreSum());
        }

        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        for(Map.Entry<String, List<ObjectId>> entry : classStudent.entrySet()){
            stuIds.addAll(entry.getValue());
        }

        List<IdValuePairDTO> stuScores = new ArrayList<IdValuePairDTO>();
        for(ObjectId stuId : stuIds){
            double score = stuScoreMap.get(stuId) == null ? 0 : stuScoreMap.get(stuId);
            stuScores.add(new IdValuePairDTO(stuId, score));
        }

        Comparator<IdValuePairDTO> comparator = new Comparator<IdValuePairDTO>() {
            @Override
            public int compare(IdValuePairDTO o1, IdValuePairDTO o2) {
                Double score1 = (Double)o1.getValue();
                Double score2 = (Double)o2.getValue();
                return score2.compareTo(score1);
            }
        };

        Collections.sort(stuScores, comparator);


        int cur = 0;
        for(IdValuePairDTO stuScore : stuScores){
            if(cur % 2 == 0){
                one.add(stuScore.getId());
            } else {
                two.add(stuScore.getId());
            }
            cur++;
        }
    }


    /**
     * 考场序号
     */
    private class RoomSeat {
        private final ObjectId[][] seat;
        private final ExamRoomDTO room;
        private int currSize = 0;
        private final int COLS = 6;
        private final int ROWS;
        private LinkedList<ObjectId> one;
        private LinkedList<ObjectId> two;

        private RoomSeat(ExamRoomDTO room, List<List<ObjectId>> student) {
            this.room = room;
            one = student.size() > 0 ? (LinkedList) student.get(0) : null;
            two = student.size() > 1 ? (LinkedList) student.get(1) : null;
            ROWS = (room.getExamRoomSitNumber() + COLS - Constant.ONE) / COLS;
            seat = new ObjectId[ROWS][COLS];
        }

        private void setStuSeat() {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    //如果没有学生信息可以编排，结束编排
                    if ((one == null || one.isEmpty()) && (two == null || two.isEmpty())) {
                        break;
                    }
                    //如果当前编排数达到考场总位数
                    if (currSize + 1 > room.getExamRoomSitNumber()) {
                        break;
                    }
                    if ((i + j) % 2 == 0) {
                        //获取第一组数据中的学生
                        if(one == null || one.isEmpty()){
                        	seat[i][j] = two.removeFirst();
                        }else{
                        	seat[i][j] =  one.removeFirst();
                        }
                    } else {
                        //第二组数据
                    	if(two == null || two.isEmpty()){
                    		seat[i][j] = one.removeFirst();
                    	}else{
                    		seat[i][j] = two.removeFirst();
                    	}
                    }
                    if (seat[i][j] != null) {
                        ++currSize;
                    }
                }
            }
        }

        public int getCOLS() {
            return COLS;
        }

        public int getROWS() {
            return ROWS;
        }

        public ObjectId[][] getSeat() {
            return seat;
        }

        public int getCurrSize() {
            return currSize;
        }

        public ExamRoomDTO getRoom() {
            return room;
        }
    }
}
