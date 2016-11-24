package com.fulaan.questionnaire.service;

import com.db.questionnaire.QuestionnaireDao;
import com.db.school.ClassDao;
import com.db.school.DepartmentDao;
import com.db.school.InterestClassDao;
import com.db.user.UserDao;
import com.fulaan.letter.controller.LetterController;
import com.fulaan.utils.SpringContextUtil;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.mongodb.BasicDBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.InterestClassStudent;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by qinbo on 15/3/16.
 */
public class QuestionnaireService {

    private QuestionnaireDao questionnaireDao = new QuestionnaireDao();
    private ClassDao classDao = new ClassDao();
    private InterestClassDao interestClassDao = new InterestClassDao();
    private DepartmentDao departmentDao = new DepartmentDao();
    private UserDao userDao = new UserDao();

    public ObjectId addQuestionnaire(QuestionnaireEntry questionnaireEntry){
       return questionnaireDao.add(questionnaireEntry);
    }

    public QuestionnaireEntry findOne(String surveyId) throws IllegalParamException{
        if(!ObjectId.isValid(surveyId))
        {
            throw new IllegalParamException("the id ["+surveyId+" ] is valid!!");
        }

        return questionnaireDao.findOne(new ObjectId(surveyId));
    }

    public List<QuestionnaireEntry> findSurveysBySchoolId(String schoolId,List<ObjectId> classIds,int page,int size, ObjectId userId, int role, int onlyMyself) throws Exception {
        if(!ObjectId.isValid(schoolId))
        {
            throw new IllegalParamException("the id ["+schoolId+" ] is valid!!");
        }
        List<QuestionnaireEntry> surveyList = questionnaireDao.getSurveyBySchoolId(new ObjectId(schoolId), classIds, page * size, size, userId, role, onlyMyself);

        return surveyList;
    }

    /** 读取当前用户可见的调查数量
     * @param schoolId 学校id
     * @param classIds 老师或学生所在的班级列表
     * @return 数量
     * @throws Exception
     */
    public int getQuestionnaireCountBySchoolId(String schoolId,List<ObjectId> classIds, ObjectId userId, int role, int onlyMyself)throws Exception
    {
        if(!ObjectId.isValid(schoolId))
        {
            throw new IllegalParamException("the id ["+schoolId+" ] is valid!!");
        }

        return questionnaireDao.getQuestionnaireCountbySchoolId(new ObjectId(schoolId),classIds, userId, role, onlyMyself);
    }

    public QuestionnaireEntry updateRespondents(Map<String,List<Object>> respondents,
                                         String surveyId) throws IllegalParamException{
        if(!ObjectId.isValid(surveyId))
        {
            throw new IllegalParamException("the id ["+surveyId+" ] is valid!!");
        }
        FieldValuePair fvpResp = new FieldValuePair("res",respondents);
        questionnaireDao.update(new ObjectId(surveyId),fvpResp);

        return findOne(surveyId);
    }

    /** 删除调查
     * @param surveyId 要删除的调查的id
     */
    public void removeById(String surveyId) throws IllegalParamException{
        if(!ObjectId.isValid(surveyId))
        {
            throw new IllegalParamException("the id ["+surveyId+" ] is valid!!");
        }
        questionnaireDao.remove(new ObjectId(surveyId));
    }

    public void updateIsPublic(ObjectId qId, int isPublic){
        questionnaireDao.updateIsPublic(qId, isPublic);
    }



    private List<byte[]> getImages(String[] charts) throws IOException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        List<byte[]> chartBytes = new ArrayList<byte[]>();
        if(charts != null) {
            for (String chartStr : charts) {
                chartBytes.add(base64Decoder.decodeBuffer(chartStr.substring(chartStr.indexOf(",") + 1)));
            }
        }
        return chartBytes;
    }

    private List<Multiset<Object>> statisticAnswer(Map<String, List<Object>> respondents) {
        List<Multiset<Object>> countList = null;
        if (respondents != null) {
            countList = Lists.newArrayList();
            for (List<Object> answers : respondents.values()) {
                for (int i = 0; i < answers.size(); i++) {
                    Object answer = answers.get(i);
                    Multiset<Object> countSet;
                    try {
                        countSet = countList.get(i);
                    } catch (IndexOutOfBoundsException outE) {
                        countSet = HashMultiset.create();
                        countList.add(i, countSet);
                    }
                    if (answer instanceof List) {
                        countSet.addAll((List) answer);
                    } else {
                        countSet.add(answer);
                    }
                }
            }
        }
        return countList;
    }
    private HSSFCellStyle getTitleStyle(HSSFWorkbook wb) {
        HSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont titleFont = wb.createFont();
        titleFont.setColor(HSSFColor.WHITE.index);
        titleStyle.setFont(titleFont);
        return titleStyle;
    }
    private String getAnswerOpt(int i) {
        String[] answerOpts = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        return answerOpts[i];
    }
    public HSSFWorkbook getExcel(String questionnaireId, String[] charts) throws IOException {
        QuestionnaireEntry questionnaire = questionnaireDao.findOne(new ObjectId(questionnaireId));
        //转换图片
        List<byte[]> chartBytes = getImages(charts);
        //统计选项
        Map<String, List<Object>> respondents = questionnaire.getRespondent();
        List<Multiset<Object>> countList = statisticAnswer(respondents);
        //生成Excel
        HSSFWorkbook wb = new HSSFWorkbook();
        int chartIdx = 0;
        for (int i = 0; i < questionnaire.getAnswerSheet().size(); i++) {
            Integer question = questionnaire.getAnswerSheet().get(i);
            String title = "Q" + (i + 1);
            HSSFSheet sheet = wb.createSheet(title);
            //标题
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
            HSSFRow titleRow = sheet.createRow(0);
            HSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(getTitleStyle(wb));
            if (question != 0 && question < 10000) {
                //选择题
                byte[] image = chartBytes.get(chartIdx++);
                if (image != null) {
                    //插入图片
                    //HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                    //HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 2, (short) 8, 16);
                    //HSSFPicture picture = patriarch.createPicture(anchor, wb.addPicture(image, HSSFWorkbook.PICTURE_TYPE_PNG));
                    //picture.setLineStyle(HSSFPicture.LINESTYLE_SOLID);
                }
                HSSFRow tableHeaderRow = sheet.createRow(19);
                sheet.addMergedRegion(new CellRangeAddress(19, 19, 1, 6));
                tableHeaderRow.createCell(1).setCellValue("答案选项");
                tableHeaderRow.createCell(7).setCellValue("回复情况");
                int optCount = question > 0 ? question : -question;
                Multiset<Object> counts = null;
                if (countList != null) {
                    counts = countList.get(i);
                }
                for (int j = 0; j < optCount; j++) {
                    int rownum = 20 + j;
                    HSSFRow tableRow = sheet.createRow(rownum);
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 1, 6));
                    tableRow.createCell(1).setCellValue(getAnswerOpt(j));
                    tableRow.createCell(7).setCellValue(counts == null ? 0 : counts.count(j));
                }
            } else {
                //填空题
                HSSFRow tableHeaderRow = sheet.createRow(1);
                tableHeaderRow.createCell(0).setCellValue("答案");
                if (countList != null) {
                    Multiset<Object> counts = countList.get(i);
                    int rowNum = 2;
                    for (Object o : counts.elementSet()) {
                        String answer = o+"";
                        HSSFRow tableRow = sheet.createRow(rowNum);
                        tableRow.createCell(0).setCellValue(answer);
                        rowNum++;
                    }
                }
            }
        }
        return wb;
    }

    //所有答案放在一个sheet里
    public HSSFWorkbook getExcel1(String questionnaireId, String[] charts){
        QuestionnaireEntry questionnaire = questionnaireDao.findOne(new ObjectId(questionnaireId));

        //统计选项
        Map<String, List<Object>> respondents = questionnaire.getRespondent();
        //生成Excel
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("详情");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;
        for (int i = 0; i < questionnaire.getAnswerSheet().size(); i++) {
            String title = "Q" + (i + 1);
            cell = row.createCell(i);
            cell.setCellValue(title);
        }
        int rowNo = 1;
        for (Map.Entry<String, List<Object>> entry : respondents.entrySet()) {
            try {
                row = sheet.createRow(rowNo);
                List<Object> respondent = entry.getValue();

                for (int i = 0; i < questionnaire.getAnswerSheet().size(); i++) {
                    cell = row.createCell(i);
                    Object resp = respondent.get(i);

                    Integer question = questionnaire.getAnswerSheet().get(i);
                    String answer = "";
                    if (question != 0 && question < 10000) {//选择

                        if (question > 0) {//单选
                            answer = getAnswerOpt((Integer) resp);
                        } else {//多选
                            List<Integer> answers = (List<Integer>) resp;
                            for (Integer integer : answers) {
                                answer += getAnswerOpt(integer);
                            }
                        }
                        cell.setCellValue(answer);
                    } else if (question == 0) {
                        answer = (String) resp;
                        cell.setCellValue(answer);
                    } else {
                        cell.setCellValue((Integer) resp);
                    }


                }

                rowNo++;
            } catch (Exception e){
                e.printStackTrace();
            }
        }



        return wb;
    }



    public CustomXWPFDocument getWord(String questionnaireId, String[] charts) {
        CustomXWPFDocument doc = new CustomXWPFDocument();
        QuestionnaireEntry questionnaire = questionnaireDao.findOne(new ObjectId(questionnaireId));

//        try {
            //List<byte[]> images = getImages(charts);
            Map<String, List<Object>> respondents = questionnaire.getRespondent();
            List<Multiset<Object>> countList = statisticAnswer(respondents);
            XWPFParagraph titleParagraph = doc.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = titleParagraph.createRun();
            run.setBold(true);
            run.setText(questionnaire.getName());
            //int imgIdx = 0;
            for (int i = 0; i < questionnaire.getAnswerSheet().size(); i++) {
                int question = questionnaire.getAnswerSheet().get(i);
                XWPFParagraph questionParagraph = doc.createParagraph();
                XWPFRun questionRun = questionParagraph.createRun();
                questionRun.setBold(true);
                questionRun.setText("Q" + (i + 1));
                if (question != 0&& question<10000) {
                    //选择题
                    //XWPFParagraph imgParagraph = doc.createParagraph();
                    //String picId = doc.addPictureData(images.get(imgIdx), XWPFDocument.PICTURE_TYPE_PNG);
                    //doc.createPicture(imgParagraph, doc.getAllPictures().size() - 1, 200, 200, "");
                    //imgIdx++;
                    int optCount = question > 0 ? question : -question;
                    XWPFTable answerTable = doc.createTable(optCount + 1, 2);
                    XWPFTableRow headerRow = answerTable.getRow(0);
                    headerRow.getCell(0).setText("答案选项");
                    headerRow.getCell(1).setText("回复情况");
                    for (int j = 0; j < optCount; j++) {
                        int rowNum = j + 1;
                        XWPFTableRow answerRow = answerTable.getRow(rowNum);
                        answerRow.getCell(0).setText(getAnswerOpt(j));
                        Multiset<Object> countSet = countList == null ? null : countList.get(i);
                        answerRow.getCell(1).setText(String.valueOf(countSet == null ? 0 : countSet.count(j)));
                    }
                } else {
                    if (countList != null) {
                        Set<Object> answers = countList.get(i).elementSet();
                        XWPFTable answerTable = doc.createTable(answers.size() + 1, 1);
                        XWPFTableRow headerRow = answerTable.getRow(0);
                        headerRow.getCell(0).setText("答案");
                        int rowNum = 1;
                        for (Object obj : answers) {
                            String answer =  obj+"";
                            answerTable.getRow(rowNum++).getCell(0).setText(answer);
                        }
                    }
                }
                XWPFParagraph userParagraph = doc.createParagraph();
                XWPFRun userRun = userParagraph.createRun();
                userRun.setText("受访人数 " + (respondents == null ? 0 : respondents.size()));
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidFormatException e) {
//            e.printStackTrace();
//        }
        return doc;
    }

    public int countQuestionedUsers(ObjectId questionId, ObjectId schoolId){
        List<UserEntry> questionedUsers = new ArrayList<UserEntry>();
        QuestionnaireEntry entry = questionnaireDao.findOne(questionId);
        List<String> classIdsAndDepartmentIds = entry.getClassIds();
        List<ObjectId> ids = new ArrayList<ObjectId>();
        if(entry.getIsPlatform() == 1){//全平台
            return countQuestionedUserFromAllSchool(entry, null);
        } else
        if(classIdsAndDepartmentIds == null){//全校
            return countQuestionedUserFromAllSchool(entry, schoolId);
        } else {
            ids = MongoUtils.convertToObjectIdList(classIdsAndDepartmentIds);
            questionedUsers.addAll(getQuestionedUserFromClasses(entry, ids));
            questionedUsers.addAll(getQuestionedUserFromInterestClasses(entry, ids));
            questionedUsers.addAll(getQuestionedUserFromDepartments(entry, ids));
            return questionedUsers.size();
        }
    }


    public List<Map<String, Object>> getQuestionedUsers(ObjectId questionId, ObjectId schoolId, int page, int pageSize){
        List<UserEntry> questionedUsers = new ArrayList<UserEntry>();
        QuestionnaireEntry entry = questionnaireDao.findOne(questionId);
        List<String> classIdsAndDepartmentIds = entry.getClassIds();
        List<ObjectId> ids = new ArrayList<ObjectId>();
        if(entry.getIsPlatform() == 1){//全平台
            questionedUsers = getQuestionedUserFromPlatForm(entry, (page-1)*pageSize, pageSize);
        } else
        if(classIdsAndDepartmentIds == null){//全校
            questionedUsers = getQuestionedUserFromAllSchool(entry, schoolId, (page-1)*pageSize, pageSize);
        } else {
            ids = MongoUtils.convertToObjectIdList(classIdsAndDepartmentIds);
            questionedUsers.addAll(getQuestionedUserFromClasses(entry, ids));
            questionedUsers.addAll(getQuestionedUserFromInterestClasses(entry, ids));
            questionedUsers.addAll(getQuestionedUserFromDepartments(entry, ids));

            List<Map<String, Object>> data = buildData(entry, questionedUsers, schoolId, ids);
            List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
            int size = countQuestionedUsers(questionId, schoolId);
            for(int i=0; i<pageSize-1; i++){
                int index = (page - 1) * pageSize + i;
                if(index >= size){
                    break;
                }
                userList.add(data.get(index));
            }
            return userList;
        }

        return buildData(entry, questionedUsers, schoolId, ids);

    }

    private List<UserEntry> getQuestionedUserFromPlatForm(QuestionnaireEntry entry, int skip, int limit){
        List<Integer> roles = getQuestionnaireUserRoles(entry);
        List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(null, roles, new BasicDBObject("nm", 1).append("r", 1).append("cid", 1), skip, limit);
        return userEntries;
    }

    private List<UserEntry> getQuestionedUserFromAllSchool(QuestionnaireEntry entry, ObjectId schoolId, int skip, int limit){
        List<Integer> roles = getQuestionnaireUserRoles(entry);
        List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(schoolId, roles, new BasicDBObject("nm", 1).append("r", 1).append("cid", 1), skip, limit);
        return userEntries;
    }

    private int countQuestionedUserFromAllSchool(QuestionnaireEntry entry, ObjectId schoolId){
        List<Integer> roles = getQuestionnaireUserRoles(entry);
        return userDao.countUserEntryBySchoolId(schoolId, roles);
    }

    private List<Integer> getQuestionnaireUserRoles(QuestionnaireEntry entry){
        List<Integer> roles = new ArrayList<Integer>();
        int flag = 0;
        if(entry.getTeacherRespondent() == 1){
            roles.add(UserRole.TEACHER.getRole());
            roles.add(UserRole.TEACHER.getRole()|UserRole.HEADMASTER.getRole());
            roles.add(UserRole.TEACHER.getRole()|UserRole.ADMIN.getRole());
            flag += 1;
        }
        if(entry.getStudentRespondent() == 1) {
            roles.add(UserRole.STUDENT.getRole());
            flag += 1;
        }
        if(entry.getParentRespondent() == 1){
            roles.add(UserRole.PARENT.getRole());
            flag += 1;
        }
        if(entry.getHeadmasterRespondent() == 1){
            roles.add(UserRole.HEADMASTER.getRole());
            flag += 1;
        }
        if(flag == 4){
            return null;
        }
        return roles;
    }

    private List<UserEntry> getQuestionedUserFromClasses(QuestionnaireEntry entry, List<ObjectId> classIds){
        List<UserEntry> questionedUsers = new ArrayList<UserEntry>();
        List<ClassEntry> classEntries = classDao.getClassEntryByIds(classIds, Constant.FIELDS);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> studentsIds = new ArrayList<ObjectId>();
        for(ClassEntry classEntry : classEntries){
            if(entry.getTeacherRespondent() == 1){
                userIds.addAll(classEntry.getTeachers());
            }
            if(entry.getStudentRespondent() == 1) {
                userIds.addAll(classEntry.getStudents());
                studentsIds.addAll(classEntry.getStudents());
            }

        }
        questionedUsers.addAll(userDao.getUserEntryList(userIds, new BasicDBObject("nm", 1).append("r", 1).append("cid", 1)));
        if(entry.getTeacherRespondent() == 1){
            questionedUsers.addAll(userDao.getParentEntrys(studentsIds, new BasicDBObject("nm", 1).append("r", 1).append("cid", 1)));
        }
        return questionedUsers;
    }

    private List<UserEntry> getQuestionedUserFromInterestClasses(QuestionnaireEntry entry, List<ObjectId> classIds){
        List<UserEntry> questionedUsers = new ArrayList<UserEntry>();
        List<InterestClassEntry> classEntries = interestClassDao.findInterestClassEntrysByIds(classIds, Constant.FIELDS);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> studentsIds = new ArrayList<ObjectId>();
        for(InterestClassEntry classEntry : classEntries){
            if(entry.getTeacherRespondent() == 1){
                userIds.add(classEntry.getTeacherId());
            }
            if(entry.getStudentRespondent() == 1) {
                List<InterestClassStudent> students = classEntry.getCurrentInterestClassStudents();
                for(InterestClassStudent student : students){
                    userIds.add(student.getStudentId());
                    studentsIds.add(student.getStudentId());
                }

            }

        }
        questionedUsers.addAll(userDao.getUserEntryList(studentsIds, Constant.FIELDS));
        if(entry.getTeacherRespondent() == 1){
            questionedUsers.addAll(userDao.getParentEntrys(studentsIds, new BasicDBObject("nm", 1).append("r", 1).append("cid", 1)));
        }
        return questionedUsers;
    }

    private List<UserEntry> getQuestionedUserFromDepartments(QuestionnaireEntry entry, List<ObjectId> ids){
        List<UserEntry> questionedUsers = new ArrayList<UserEntry>();
        List<DepartmentEntry> departmentEntries = departmentDao.getDepartmentsByDepIds(ids);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(DepartmentEntry departmentEntry : departmentEntries){
            if(entry.getTeacherRespondent() == 1 || entry.getHeadmasterRespondent() == 1){
                userIds.addAll(departmentEntry.getMembers());
            }

        }
        questionedUsers.addAll(userDao.getUserEntryList(userIds, new BasicDBObject("nm", 1).append("r", 1).append("cid", 1)));
        return questionedUsers;
    }

    private List<Map<String, Object>> buildData(QuestionnaireEntry entry, List<UserEntry> userEntries, ObjectId schoolId, List<ObjectId> ids){
        Map<ObjectId, ClassEntry> stuClassMap = stuClassMap(schoolId, ids);
        Collections.sort(userEntries, new SortByRole());
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String,List<Object>> respondent = entry.getRespondent();
        Set<String> userIds = respondent == null ? new HashSet<String>() : respondent.keySet();
        for(UserEntry userEntry : userEntries){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", userEntry.getUserName());
            map.put("role", UserRole.getRoleDescription(userEntry.getRole()));
            if(userIds.contains(userEntry.getID().toString())){
                map.put("submit", 1);
            } else {
                map.put("submit", 0);
            }
            Boolean noClassName = false;
            ObjectId connectId = userEntry.getConnectIds().size()>0 ? userEntry.getConnectIds().get(0) :null;
            if(stuClassMap.containsKey(userEntry.getID()) || stuClassMap.containsKey(connectId)){
                ClassEntry classEntry = stuClassMap.get(userEntry.getID());
                if(classEntry == null){
                    classEntry = stuClassMap.get(userEntry.getConnectIds().get(0));
                }
                map.put("cName", classEntry.getName());
            } else if(entry.getIsPlatform() == 1){
                map.put("cName", "");
                noClassName = false;
            }
            else {
                map.put("cName", "");
                noClassName = true;
            }
            Boolean stuOrPar = userEntry.getRole() == 1 || userEntry.getRole() == 4;
            if(!noClassName || !stuOrPar) {
                data.add(map);
            }
        }
        return data;
    }
    
    private Map<ObjectId, ClassEntry> stuClassMap(ObjectId schoolId, List<ObjectId> ids){
        Map<ObjectId, ClassEntry> stuClassMap = new HashMap<ObjectId, ClassEntry>();
        List<ClassEntry> classEntries;
        if(ids.size() > 0){
            classEntries = classDao.getClassEntryByIds(ids, Constant.FIELDS);
        } else {
            classEntries = classDao.findClassInfoBySchoolId(schoolId, Constant.FIELDS);
        }
        for(ClassEntry classEntry : classEntries){
            List<ObjectId> stuIds = classEntry.getStudents();
            if(stuIds != null){
                for(ObjectId stuId : stuIds){
                    stuClassMap.put(stuId, classEntry);
                }
            }
        }
        return stuClassMap;
    }

    private class SortByRole implements Comparator {
        public int compare(Object o1, Object o2) {
            UserEntry u1 = (UserEntry) o1;
            UserEntry u2 = (UserEntry) o2;
            Integer role1 = u1.getRole();
            Integer role2 = u2.getRole();
            return role1.compareTo(role2);
        }
    }

    public void sendLetters(ObjectId id, ObjectId schoolId, HttpServletRequest request){
        int skip = 0;
        int limit = 200;
        while(true){
            List<Map<String, Object>> users = getQuestionedUsers(id, schoolId, skip, limit);
            if(users.size() <= 0){
                break;
            }
            StringBuilder recipient = new StringBuilder();
            for(int i=0; i<users.size(); i++){
                Map<String, Object> user = users.get(i);
                if((Integer)user.get("submit") == 0){
                    recipient.append(user.get("name") + ";");
                }
            }
            QuestionnaireEntry entry = questionnaireDao.findOne(id);
            String endDate = DateTimeUtils.convert(entry.getEndTime(), DateTimeUtils.CHINESE_DATE);
            String message = "您还未填写问卷调查 《" + entry.getName() + "》，请在" + endDate + "前完成填写，谢谢。";
            LetterController letterController = (LetterController)SpringContextUtil.getBean("letterController");
            letterController.addLetter(recipient.toString(), message, request);

            skip += 200;
        }


    }





}
