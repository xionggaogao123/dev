package com.fulaan.teacherevaluation.service;

import com.db.school.SchoolDao;
import com.db.teacherevaluation.*;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO1;
import com.pojo.app.NameValuePair;
import com.pojo.school.SchoolEntry;
import com.pojo.teacherevaluation.*;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by fl on 2016/4/19.
 */
@Service
public class EvaluationConfigService {
    private UserDao userDao = new UserDao();
    private SchoolDao schoolDao = new SchoolDao();
    private MemberGroupDao memberGroupDao = new MemberGroupDao();
    private ProportionDao proportionDao = new ProportionDao();
    private ElementDao elementDao = new ElementDao();
    private SettingDao settingDao = new SettingDao();
    private EvaluationItemDao evaluationItemDao = new EvaluationItemDao();

    /**
     * 下载全校老师列表
     * @param schoolId
     * @param response
     */
    public void exportExcel(ObjectId schoolId, HttpServletResponse response){
        //获取全校老师、校长、管理员列表
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);

        HSSFWorkbook wb = new HSSFWorkbook();
        writeRule(wb);
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("教师考核");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id");
        sheet.setColumnWidth(0, 8000);
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        sheet.setColumnWidth(1, 5000);
        cell = row.createCell(2);
        cell.setCellValue("组别");
        sheet.setColumnWidth(2, 3000);
        cell = row.createCell(3);
        cell.setCellValue("是否报名");
        sheet.setColumnWidth(3, 3000);
        cell = row.createCell(4);
        cell.setCellValue("考核小组");
        sheet.setColumnWidth(4, 3000);
        List<Integer> roles = formateEvaluationUserRoles();
        int rowNo = 1;
        int skip = 0;
        int limit = 500;
        while (true){
            List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(schoolId, roles, new BasicDBObject("nm", 1).append("r", 1), skip, limit);
            if(userEntries.size() > 0){
                for(int i=0; i<userEntries.size(); i++){
                    UserEntry userEntry = userEntries.get(i);
                    row = sheet.createRow(rowNo);

                    cell = row.createCell(0);
                    cell.setCellValue(userEntry.getID().toString());
                    cell = row.createCell(1);
                    cell.setCellValue(userEntry.getUserName());
                    rowNo ++;
                }
                skip += limit;
            } else {
                break;
            }
        }

        outPutWorkBook(wb, response, schoolEntry.getName());

    }

    private void writeRule(HSSFWorkbook wb){
        HSSFSheet sheet = wb.createSheet("填写规则");

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("1、");
        cell = row.createCell(1);
        cell.setCellValue("请在工作表《教师考核》的C列和D列中填写数字");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("2、");
        cell = row.createCell(1);
        cell.setCellValue("C列中相同数字的人为同一组，数字从1开始，1表示第1组，2表示第2组，以此类推，不参与考核的老师请不要填写");

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("3、");
        cell = row.createCell(1);
        cell.setCellValue("D列中只能填1不填，1表示报名，不填表示不报名");

        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("3、");
        cell = row.createCell(1);
        cell.setCellValue("E列中只能填1、2或不填，1表示考核小组领导，2表示考核小组其他成员");
    }

    private List<Integer> formateEvaluationUserRoles(){
        List<Integer> roles = new ArrayList<Integer>();
        roles.add(UserRole.TEACHER.getRole());
        roles.add(UserRole.TEACHER.getRole()|UserRole.HEADMASTER.getRole());
        roles.add(UserRole.TEACHER.getRole()|UserRole.ADMIN.getRole());
        roles.add(UserRole.HEADMASTER.getRole());
        roles.add(UserRole.ADMIN.getRole());
        roles.add(UserRole.TEACHER.getRole()|UserRole.ADMIN.getRole()|UserRole.HEADMASTER.getRole());
        return roles;
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(schoolName + "教师考核.xls", "UTF-8"));
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
     * 导入分组情况
     * @param inputStream
     * @param schoolId
     * @return
     * @throws Exception
     */
    public void importExcel(InputStream inputStream, ObjectId schoolId, ObjectId evaluationId) throws Exception{
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet studentSheet = workbook.getSheet("教师考核");
        int rowNum = studentSheet.getLastRowNum();

        List<ObjectId> leaders = new ArrayList<ObjectId>();
        List<ObjectId> members = new ArrayList<ObjectId>();
        Map<Integer, MemberGroupEntry.TeacherGroup> teacherGroupMap = new TreeMap<Integer, MemberGroupEntry.TeacherGroup>();
        for (int i = 1; i <= rowNum; i++) {
            String id_string = studentSheet.getRow(i).getCell(0).getStringCellValue();
            ObjectId id = new ObjectId(id_string);
            HSSFCell cell = studentSheet.getRow(i).getCell(2);
            HSSFCell stateCell = studentSheet.getRow(i).getCell(3);
            if(cell != null) {
                double cellValue = cell.getNumericCellValue();
                int groupNo = (int)cellValue;
                int state = -1;
                if(stateCell != null){
                    state = (int)stateCell.getNumericCellValue();
                }
                if(groupNo != 0){
                    if (!teacherGroupMap.containsKey(groupNo)) {
                        MemberGroupEntry.TeacherGroup teacherGroup = new MemberGroupEntry.TeacherGroup("第" + groupNo + "组");
                        teacherGroup.addTeacher(id, state);
                        teacherGroupMap.put(groupNo, teacherGroup);
                    } else {
                        MemberGroupEntry.TeacherGroup teacherGroup = teacherGroupMap.get(groupNo);
                        teacherGroup.addTeacher(id, state);
                    }
                }
            }



            cell = studentSheet.getRow(i).getCell(4);
            if(cell != null) {
                double leaderFlag = cell.getNumericCellValue();
                if (leaderFlag == 1) {
                    UserEntry userEntry = userDao.getUserEntry(id, Constant.FIELDS);
                    if(UserRole.isHeadmaster(userEntry.getRole())){
                        leaders.add(id);
                    }
                } else if (leaderFlag == 2) {
                    members.add(id);
                }
            }
        }
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroupMap.values()){
            int teacherNo = teacherGroup.getGroupTeachers().size();
            teacherGroup.setNum(teacherNo);
            teacherGroup.setLiangNum(teacherNo);
        }
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        memberGroupEntry.setLeaders(leaders);
        memberGroupEntry.setMembers(members);
        memberGroupEntry.setTeacherGroups(new ArrayList<MemberGroupEntry.TeacherGroup>(teacherGroupMap.values()));
        memberGroupDao.saveMemberGroup(memberGroupEntry);
    }

    private String formateYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if (month < 9) {
            schoolYear = (year - 1) + "-" + year;
        } else{
            schoolYear = year + "-" + (year + 1);
        }
        return schoolYear;
    }

    /**
     * 获取人员分组情况
     * @param schoolId
     * @param evaluationId
     * @return
     */
    public MemberGroupDTO getMemberGroup(ObjectId evaluationId, ObjectId schoolId){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<Integer> roles = formateEvaluationUserRoles();
        List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(schoolId, roles, new BasicDBObject("nm", 1), 0, 10000);
        Map<ObjectId, UserEntry> userEntryMap = formateUserEntryMap(userEntries);
        MemberGroupDTO memberGroupDTO = new MemberGroupDTO(memberGroupEntry, userEntryMap);
        return memberGroupDTO;
    }

    public ObjectId addTeacherGroup(ObjectId evaluationId){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        ObjectId groupId = new ObjectId();
        MemberGroupEntry.TeacherGroup teacherGroup = new MemberGroupEntry.TeacherGroup(groupId, "新分组", 0, 0);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        teacherGroups.add(teacherGroup);
        memberGroupEntry.setTeacherGroups(teacherGroups);
        memberGroupDao.saveMemberGroup(memberGroupEntry);
        return groupId;
    }

    public void deleteTeacherGroup(ObjectId evaluationId, ObjectId groupId){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            if(teacherGroup.getGroupId().equals(groupId)){
                memberGroupDao.deleteTeacherGroup(evaluationId, teacherGroup);
                break;
            }
        }
    }

    public void updateTeacherGroup(ObjectId evaluationId, ObjectId groupId, String name, int num, int lnum){
        memberGroupDao.updateTeacherGroup(evaluationId, groupId, name, num, lnum);
    }



    /**
     *添加一个老师
     * @param evaluationId
     * @param type 1:领导小组领导  2：领导小组其他成员  3：老师小组
     * @param teachers
     * @param groupId
     */
    public void addTeacher(ObjectId evaluationId, int type, List<ObjectId> teachers, ObjectId groupId){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        if(type == 1){
            memberGroupEntry.addTeacherToLeaders(teachers);
        } else if(type == 2){
            memberGroupEntry.addTeacherToMembers(teachers);
        } else {
            memberGroupEntry.addTeacherToTeacherGroup(groupId, teachers);
        }
        memberGroupDao.saveMemberGroup(memberGroupEntry);
    }

    /**
     *删除一个老师
     * @param evaluationId
     * @param type 1:领导小组领导  2：领导小组其他成员  3：老师小组
     * @param teacherId
     * @param groupId
     */
    public void deleteTeacher(ObjectId evaluationId, int type, ObjectId teacherId, ObjectId groupId){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        if(type == 1){
            memberGroupEntry.removeTeacherFromLeaders(teacherId);
        } else if(type == 2){
            memberGroupEntry.removeTeacherFromMembers(teacherId);
        } else {
            memberGroupEntry.removeTeacherFromTeacherGroup(groupId, teacherId);
            evaluationItemDao.removeItem(evaluationId, teacherId);
        }
        memberGroupDao.saveMemberGroup(memberGroupEntry);
    }

    /**
     * 得到已选老师、未选老师列表
     * @param type  1:领导小组领导  2：领导小组其他成员  3：老师小组
     * @param groupId
     * @return
     */
    public Map<String, Object> getTeachers(ObjectId schoolId, ObjectId evaluationId, int type, ObjectId groupId){
        Map<String, Object> map;
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        if(type == 1){
            map = getLeaderTeachers(schoolId, memberGroupEntry);
        } else if(type == 2){
            map = getMemberTeachers(schoolId, memberGroupEntry);
        } else {
            map = getGroupTeachers(schoolId, memberGroupEntry, groupId);
        }
        return map;
    }

    private Map<String, Object> getLeaderTeachers(ObjectId schoolId, MemberGroupEntry memberGroupEntry){
        Map<String, Object> map = new HashMap<String, Object>();
        List<Integer> roles = new ArrayList<Integer>();
        roles.add(UserRole.HEADMASTER.getRole() | UserRole.TEACHER.getRole());
        List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(schoolId, roles, new BasicDBObject("nm", 1), 0, 10000);
        List<ObjectId> users = MongoUtils.getFieldObjectIDs(userEntries);
        List<ObjectId> leaders = memberGroupEntry.getLeaders();
        users.removeAll(leaders);
        Map<ObjectId, UserEntry> userEntryMap = formateUserEntryMap(userEntries);
        map.put("candidate", getUsers(userEntryMap, users));
        map.put("existence", getUsers(userEntryMap, leaders));
        return map;
    }

    private Map<String, Object> getMemberTeachers(ObjectId schoolId, MemberGroupEntry memberGroupEntry){
        Map<String, Object> map = new HashMap<String, Object>();
        List<Integer> roles = formateEvaluationUserRoles();
//        roles.remove(4);
        List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(schoolId, roles, new BasicDBObject("nm", 1), 0, 10000);
        List<ObjectId> users = MongoUtils.getFieldObjectIDs(userEntries);
        List<ObjectId> members = memberGroupEntry.getMembers();
        users.removeAll(members);
        Map<ObjectId, UserEntry> userEntryMap = formateUserEntryMap(userEntries);
        map.put("candidate", getUsers(userEntryMap, users));
        map.put("existence", getUsers(userEntryMap, members));
        return map;
    }

    private Map<String, Object> getGroupTeachers(ObjectId schoolId, MemberGroupEntry memberGroupEntry, ObjectId groupId){
        Map<String, Object> map = new HashMap<String, Object>();
        List<Integer> roles = formateEvaluationUserRoles();
//        roles.remove(4);
        List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(schoolId, roles, new BasicDBObject("nm", 1), 0, 10000);
        List<ObjectId> users = MongoUtils.getFieldObjectIDs(userEntries);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        List<ObjectId> members = new ArrayList<ObjectId>();
        List<ObjectId> teachers = new ArrayList<ObjectId>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            members.addAll(teacherGroup.getGroupTeacherIds());
            if(teacherGroup.getGroupId().equals(groupId)){
                teachers.addAll(teacherGroup.getGroupTeacherIds());
                map.put("groupName", teacherGroup.getGroupName());
                map.put("allStuNo", teacherGroup.getGroupTeachers().size());
                map.put("num", teacherGroup.getNum());
                map.put("lnum", teacherGroup.getLiangNum());
            }
        }
        users.removeAll(members);
        Map<ObjectId, UserEntry> userEntryMap = formateUserEntryMap(userEntries);
        map.put("candidate", getUsers(userEntryMap, users));
        map.put("existence", getUsers(userEntryMap, teachers));
        return map;
    }

    private Map<ObjectId, UserEntry> formateUserEntryMap(List<UserEntry> userEntries){
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        for(UserEntry userEntry : userEntries){
            userEntryMap.put(userEntry.getID(), userEntry);
        }
        return userEntryMap;
    }

    private List<Map<String, Object>> getUsers(Map<ObjectId, UserEntry> userEntryMap, List<ObjectId> userIds){
        List<Map<String, Object>> userEntries = new ArrayList<Map<String, Object>>();
        if(userIds.size() > 0){
            for(ObjectId userId : userIds){
                UserEntry userEntry = userEntryMap.get(userId);
                if(userEntry != null){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", userEntry.getID().toString());
                    map.put("name", userEntry.getUserName());
                    userEntries.add(map);
                }
            }
        }
        return userEntries;
    }


    /**
     * 得到评分比重情况
     * @param evaluationId
     * @return
     */
    public ProportionDTO getProportion(ObjectId evaluationId){
        ProportionEntry proportionEntry = proportionDao.getProportionEntryByEvalationId(evaluationId);
        return new ProportionDTO(proportionEntry);
    }

    /**
     * 新增或更新比重情况
     * @param proportionDTO
     */
    public ObjectId addProportion(ProportionDTO proportionDTO, ObjectId evaluationId) throws Exception{
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        checkNum(memberGroupEntry, proportionDTO);
        proportionDTO.setEvaluationId(evaluationId.toString());
        proportionDTO.setSchoolId(memberGroupEntry.getSchoolId().toString());
        proportionDTO.setYear(memberGroupEntry.getYear());
        ProportionEntry proportionEntry = proportionDTO.exportEntry();
        return proportionDao.addProportion(proportionEntry);
    }

    /**
     * 检查去掉最高分x人和最低分x人是否合理，不能把人全部去掉
     * @param memberGroupEntry
     * @param proportionDTO
     */
    private void checkNum(MemberGroupEntry memberGroupEntry, ProportionDTO proportionDTO) throws Exception{
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            int teacherSize = teacherGroup.getGroupTeachers().size();
            if(teacherSize - proportionDTO.getHuPingMax() - proportionDTO.getHuPingMin() - 1 < 1){//还要除去自己不能给自己打分
                throw new Exception(teacherGroup.getGroupName() + "老师总数为" + teacherSize+ "，分数至少要有1个有效");
            }
        }
        int leaderSize = memberGroupEntry.getLeaders().size();
        int leadMemberSize = memberGroupEntry.getMembers().size();
        if(leaderSize - proportionDTO.getLeaderMax() - proportionDTO.getLeaderMin() < 1){
            throw new Exception("考核小组领导总数为" + leaderSize+ "，分数至少要有1个有效");
        }
        if(leadMemberSize - proportionDTO.getGroupMax() - proportionDTO.getGroupMin() < 1){
            throw new Exception("考核小组成员总数为" + leadMemberSize+ "，分数至少要有1个有效");
        }
    }


    /**
     * 考核元素或量化成绩列表
     * @param evaluationId
     * @param type
     * @return
     */
    public List<ElementDTO> getElements(ObjectId evaluationId, int type){
        List<ElementDTO> elementDTOs = new ArrayList<ElementDTO>();
        List<ElementEntry> elementEntries = elementDao.getElements(evaluationId, type);
        if(elementEntries.size() > 0){
            for(ElementEntry elementEntry : elementEntries){
                elementDTOs.add(new ElementDTO(elementEntry));
            }
        }
        return elementDTOs;
    }

    /**
     * 增加考核元素或量化成绩
     * @param evaluationId
     * @param type
     * @param name
     * @param score
     * @return
     */
    public List<ElementDTO> addElement(ObjectId evaluationId, int type, String name, double score){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        ElementEntry elementEntry = new ElementEntry(memberGroupEntry.getSchoolId(), memberGroupEntry.getYear(), evaluationId, name, score, type, new ArrayList<IdValuePair>());
        elementDao.addElement(elementEntry);
        return getElements(evaluationId, type);
    }

    /**
     * 更新考核元素或量化成绩
     * @param evaluationId
     * @param type
     * @param name
     * @param score
     * @param elementId
     * @return
     */
    public List<ElementDTO> updateElement(ObjectId evaluationId, int type, String name, double score, ObjectId elementId){
        elementDao.updateElement(elementId, score, name);
        return getElements(evaluationId, type);
    }

    /**
     * 删除考核元素或量化成绩
     * @param evaluationId
     * @param type
     * @param elementId
     * @return
     */
    public List<ElementDTO> deleteElement(ObjectId evaluationId, int type, ObjectId elementId){
        elementDao.deleteElement(elementId);
        return getElements(evaluationId, type);
    }


    /**
     * 获取考核内容
     * @param elementId
     * @return
     */
    public List<IdValuePairDTO1> getContents(ObjectId elementId){
        ElementEntry elementEntry = elementDao.getElement(elementId);
        List<IdValuePair> idValuePairs = elementEntry.getElementContent();
        List<IdValuePairDTO1> contents = new ArrayList<IdValuePairDTO1>();
        if(idValuePairs.size() > 0){
            for(IdValuePair pair : idValuePairs){
                contents.add(new IdValuePairDTO1(pair));
            }
        }
        return contents;
    }

    /**
     * 增加考核内容
     * @param elementId
     * @param value
     * @return
     */
    public List<IdValuePairDTO1> addContent(ObjectId elementId, String value){
        IdValuePair content = new IdValuePair(new ObjectId(), value);
        elementDao.addContent(elementId, content);
        return getContents(elementId);
    }

    /**
     * 更新考核内容
     * @param elementId
     * @param contentId
     * @param value
     * @return
     */
    public List<IdValuePairDTO1> updateContent(ObjectId elementId, ObjectId contentId, String value){
        elementDao.updateContent(elementId, contentId, value);
        return getContents(elementId);
    }

    /**
     * 删除考核内容
     * @param elementId
     * @param contentId
     * @return
     */
    public List<IdValuePairDTO1> deleteContent(ObjectId elementId, ObjectId contentId){
        ElementEntry elementEntry = elementDao.getElement(elementId);
        List<IdValuePair> contents = elementEntry.getElementContent();
        for(IdValuePair content : contents){
            if(content.getId().equals(contentId)){
                elementDao.deleteContent(elementId, content);
                break;
            }
        }
        return getContents(elementId);
    }

    /**
     * 获取其他设置详情
     * @param evaluationId
     * @return
     */
    public SettingDTO getSetting(ObjectId evaluationId){
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        return new SettingDTO(settingEntry);
    }

    public void updateRule(ObjectId evaluationId, String rule){
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        settingEntry.setRule(rule);
        settingDao.addSetting(settingEntry);
    }

    public void updateTime(ObjectId evaluationId, String evaluationTimeBegin, String evaluationTimeEnd){
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        settingEntry.setEvaluationTimeBegin(DateTimeUtils.getStrToLongTime(evaluationTimeBegin, DateTimeUtils.DATE_YYYY_MM_DD));
        settingEntry.setEvaluationTimeEnd(DateTimeUtils.getStrToLongTime(evaluationTimeEnd, DateTimeUtils.DATE_YYYY_MM_DD));
        settingDao.addSetting(settingEntry);
    }

    public void updateGrade(ObjectId evaluationId,List<SettingDTO.GradeSettingDTO> gradeSettingDTOs){
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        List<SettingEntry.GradeSetting> gradeSettings = new ArrayList<SettingEntry.GradeSetting>();
        for(SettingDTO.GradeSettingDTO gradeSettingDTO : gradeSettingDTOs){
            gradeSettings.add(gradeSettingDTO.exportEntry());
        }
        settingEntry.setGradeSettings(gradeSettings);
        settingDao.addSetting(settingEntry);
    }

    public void updateMode(ObjectId evaluationId, int mode, String grades){
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        settingEntry.setMode(mode);
        List<NameValuePair> modeGrades = new ArrayList<NameValuePair>();
        String[] strings = grades.split(";");
        for(String string : strings){
            String[] modeGrade = string.split(",");
            modeGrades.add(new NameValuePair(modeGrade[0], Double.parseDouble(modeGrade[1])));
        }
        settingEntry.setModeGrades(modeGrades);
        settingDao.addSetting(settingEntry);
    }








}
