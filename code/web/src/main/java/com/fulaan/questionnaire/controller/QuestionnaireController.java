package com.fulaan.questionnaire.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.department.service.DepartmentService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.learningcenter.service.ExerciseService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.questionnaire.dto.QuestionnaireDTO;
import com.fulaan.questionnaire.service.QuestionnaireService;
import com.fulaan.user.service.UserService;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.DepartmentEntry;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.HttpFileConvertUtils;
import com.sys.utils.RespObj;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/** 调查问卷
 * Created by fourer on 15-2-26.
 * @author qinbo
 */
@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController extends BaseController {

    private QuestionnaireService questionnaireService = new QuestionnaireService();

    private ClassService classService = new ClassService();
    private UserService userService = new UserService();
    private DepartmentService departmentService = new DepartmentService();
    private ExerciseService exerciseService =new ExerciseService();
    @Autowired
    private ExperienceService experienceService;

    @RequestMapping("/index")
    public String indexPage(){
        return "questionnaire/index";
    }

    //=================新问卷调查=======================
    @RequestMapping("/load")
    public String index(Model model){
        boolean isTeacher = UserRole.isTeacher(getSessionValue().getUserRole());
        boolean isHeadmaster = UserRole.isHeadmaster(getSessionValue().getUserRole());
        if(isTeacher || isHeadmaster){//老师
            model.addAttribute("role", 1);
        } else {
            model.addAttribute("role", 0);
        }
        model.addAttribute("userId", getSessionValue().getId());
        return "questionnaire/list_new";
    }

    @RequestMapping("/add_new")
    public String add_new(){
        return "/questionnaire/add_new";
    }

    @RequestMapping("/review")
    public String review(){
        return "/questionnaire/review_new";
    }

    /** 列出所有调查问卷
     * @param page
     * @param size
     * @param onlyMyself 0:全部 1：仅仅我自己
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String ,Object> listSurvey
        (@RequestParam Integer page,@RequestParam Integer size, @RequestParam(required = false, defaultValue = "0") Integer onlyMyself) throws Exception{

        Map<String,Object> model = new HashMap<String, Object>();

        String schoolId = getSessionValue().getSchoolId();
        List<ClassInfoDTO> classInfoList= userService.getClassDTOList(
                new ObjectId(getSessionValue().getId()), getSessionValue().getUserRole());
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for(ClassInfoDTO classInfoDTO:classInfoList){

            classIds.add(new ObjectId(classInfoDTO.getId()));
        }
        List<DepartmentEntry> departmentEntryList = departmentService.getDepartmentsByUserId(getUserId());
        for(DepartmentEntry departmentEntry:departmentEntryList){
            classIds.add(departmentEntry.getID());
        }
        ObjectId userId = new ObjectId(getSessionValue().getId());
        int role = 0;
        if(UserRole.isTeacher(getSessionValue().getUserRole())){
            role = 1;
        } else if(UserRole.isStudent(getSessionValue().getUserRole())){
            role = 2;
        } else if(UserRole.isParent(getSessionValue().getUserRole())){
            role = 3;
        } else if(UserRole.isHeadmaster(getSessionValue().getUserRole())){
            role = 4;
        }
        List<QuestionnaireEntry> surveyEntries = questionnaireService.findSurveysBySchoolId(schoolId, classIds, page, size, userId, role, onlyMyself);

        List<QuestionnaireDTO> questionnaireDTOList = new ArrayList<QuestionnaireDTO>();

        for(QuestionnaireEntry questionnaireEntry : surveyEntries){
            QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO(questionnaireEntry);
            checkState(questionnaireDTO,getSessionValue().getId());
            int respondentNumber  = questionnaireEntry.getRespondent()==null ? 0 : questionnaireEntry.getRespondent().size();
            int totalNumber = questionnaireEntry.getTotalNumber();
            if(totalNumber == 0){
                totalNumber = questionnaireService.countQuestionedUsers(questionnaireEntry.getID(), new ObjectId(schoolId));
                questionnaireEntry.setTotalNumber(totalNumber);
                questionnaireService.addQuestionnaire(questionnaireEntry);
            }
            questionnaireDTO.setProcess(respondentNumber + "/" + totalNumber);
            questionnaireDTOList.add(questionnaireDTO);
        }

        model.put("content", questionnaireDTOList);

        //分页信息
        int totalCount = questionnaireService.getQuestionnaireCountBySchoolId(schoolId, classIds, userId, role, onlyMyself);

        int totalPages = 0;
        if(totalCount > 0)
        {
            totalPages = (totalCount-1)/size+1;
        }
        boolean first = false;
        if(page==0)
        {
            first = true;
        }
        boolean last = false;
        if(page == totalPages-1)
        {
            last = true;
        }
        model.put("first",first);
        model.put("totalElements",totalCount);
        model.put("last",last);
        model.put("numberOfElements",questionnaireDTOList.size());
        model.put("totalPages",totalPages);
        model.put("number",page);

        model.put("sort",null);
        model.put("size",size);


        return model;
    }

    private void checkState(QuestionnaireDTO questionnaireDTO, String userId){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        if(questionnaireDTO.getPublisher().equals(userId)){
            questionnaireDTO.setState(1);
        } else if(questionnaireDTO.getRespondents()!=null && questionnaireDTO.getRespondents().containsKey(userId)){
            questionnaireDTO.setState(3);
        } else if (questionnaireDTO.getEndDate().before(calendar.getTime())){
            questionnaireDTO.setState(2);
        } else {
            questionnaireDTO.setState(4);
        }
    }


    /**
     * 新建
     *
     * @param
     * @return
     */
    @RequestMapping("/new")
    @ResponseBody
    public QuestionnaireDTO addQuestionnaire(QuestionnaireDTO questionnaireDTO){


        questionnaireDTO.setSchoolId(getSessionValue().getSchoolId());
        questionnaireDTO.setPublisher(getSessionValue().getId());
        questionnaireDTO.setPublishDate(new Date());


        QuestionnaireEntry questionnaireEntry = questionnaireDTO.buildSurveyEntry();

        //add
        ObjectId id=questionnaireService.addQuestionnaire(questionnaireEntry);

        ExpLogType expLogType = ExpLogType.LAUNCH_QUESTIONNAIRE;
        experienceService.updateScore(getUserId().toString(), expLogType, id.toString());
        return questionnaireDTO;
    }

    @RequestMapping("/new1")
    @ResponseBody
    public QuestionnaireDTO addQuestionnaire1(QuestionnaireDTO questionnaireDTO, String classList, String answerSheetList){
        List<String> classIds = new ArrayList<String>();
        List<Integer> answerSheet = new ArrayList<Integer>();
        for(String classId : classList.split(",")) {
            classIds.add(classId);
        }
        for(String answer : answerSheetList.split(",")){
            answerSheet.add(new Integer(answer));
        }

        questionnaireDTO.setClassIds(classIds);
        questionnaireDTO.setAnswerSheet(answerSheet);
        questionnaireDTO.setSchoolId(getSessionValue().getSchoolId());
        questionnaireDTO.setPublisher(getSessionValue().getId());
        questionnaireDTO.setPublishDate(new Date());

//        QuestionnaireEntry questionnaireEntry = questionnaireDTO.buildSurveyEntry();
//
//        //add
//        questionnaireService.addQuestionnaire(questionnaireEntry);



        return questionnaireDTO;
    }

    /**  回答问卷
     * @param params
     */
    @RequestMapping("/answer")
    @ResponseBody
    public QuestionnaireDTO answer(@RequestBody Map<String,Object> params)  throws Exception{
        String surveyId = (String) params.get("id");
        List answers = (List) params.get("answers");

        QuestionnaireEntry questionnaireEntry = questionnaireService.findOne(surveyId);
        Map<String,List<Object>> respondents = questionnaireEntry.getRespondent();
        if(respondents == null){
            respondents = new HashMap<String, List<Object>>();
            questionnaireEntry.setRespondent(respondents);
        }

        ObjectId userId = new ObjectId(getSessionValue().getId());
        List<Object> answerList= new ArrayList<Object>();

        for (int i = 0; i < questionnaireEntry.getAnswerSheet().size(); i++) {
            Integer question = questionnaireEntry.getAnswerSheet().get(i);
            Object answerObj = answers.get(i);
            if (question > 0) {
                //单选题
                answerObj = Integer.parseInt((String) answerObj);
            } else if (question < 0) {
                //多选题
                List<String> multiAnswers = (List<String>) answerObj;
                List<Integer> multiIntAnswers = new ArrayList<Integer>();
                for (String answerStr : multiAnswers) {
                    multiIntAnswers.add(Integer.parseInt(answerStr));
                }
                answerObj = multiIntAnswers;
            }
            answerList.add(answerObj);
        }
        respondents.put(userId.toString(), answerList);
        QuestionnaireDTO questionnaireDTO=new QuestionnaireDTO(questionnaireService.updateRespondents(respondents,surveyId));

        ExpLogType expLogType = ExpLogType.JOIN_QUESTIONNAIRE;
        experienceService.updateScore(getUserId().toString(), expLogType, surveyId);

        return questionnaireDTO;

    }

    /**  浏览调查详情
     * @param id 调查id
     * @return
     * @throws Exception
     */
    @RequestMapping("/view")
    @ResponseBody
    public QuestionnaireDTO view(String id) throws Exception{
        return new QuestionnaireDTO(questionnaireService.findOne(id));
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(String id) throws Exception{

        questionnaireService.removeById(id);

    }


    /** 发送通知
     * @param content
     * @param userIds
     * @return
     */
    @RequestMapping("/notify")
    @ResponseBody
    public Map<String, Object> notify(String content, Integer[] userIds) {
        Map<String, Object> result = new HashMap<String, Object>();
        //todo : use letter service

        return result;
    }


    /**
     * 上传word问卷，todo 文件转换
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest req,MultipartFile file) {
//
        String html = wordtoHTML(req,file);
        return html;

    }

    @RequestMapping("/upload1")
    @ResponseBody
    public String upload1(HttpServletRequest req,MultipartFile file) {
//        Map<String, Object> model = new HashMap<String, Object>();

        ObjectId docId=new ObjectId();
        try
        {
            convertPdfAndSwfFile(req,file, docId);
        }catch(Exception ex) {

        }
        String originalName = file.getOriginalFilename();
        String name = originalName.substring(0, originalName.lastIndexOf("."));
//        model.put("url", docId.toString());
//        model.put("name", originalName.substring(0, originalName.lastIndexOf(".")));
//        return model;
        return "/upload/questionnaire/"+name + '_' +docId.toString() +".pdf";
    }

    private void convertPdfAndSwfFile(HttpServletRequest req,MultipartFile file,ObjectId id) throws Exception
    {
        File wordFile=null;
        File pdfFile=null;
        File swfFile=null;
        String originalName = file.getOriginalFilename();
        String name = originalName.substring(0, originalName.lastIndexOf("."));
        try
        {
            String path=req.getServletContext().getRealPath("/upload/questionnaire");
            wordFile =new File(path, id.toString()+"_"+originalName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), wordFile);
            pdfFile =new File(path, name + '_' +id.toString()+".pdf");
            swfFile =new File(path, name + '_' +id.toString()+".swf");
            HttpFileConvertUtils.word2PdfAndSwf(wordFile, swfFile, pdfFile);
//            QiniuFileUtils.uploadFile(id.toString() + FilenameUtils.getExtension(file.getOriginalFilename()), new FileInputStream(wordFile), QiniuFileUtils.TYPE_DOCUMENT);
//            QiniuFileUtils.uploadFile(id.toString()+".pdf", new FileInputStream(pdfFile), QiniuFileUtils.TYPE_DOCUMENT);
//            QiniuFileUtils.uploadFile(id.toString()+".swf", new FileInputStream(swfFile), QiniuFileUtils.TYPE_DOCUMENT);
        }catch(Exception ex) {
            throw ex;
        }
    }

    private String wordtoHTML(HttpServletRequest req,MultipartFile file)  {

        String parentPath = req.getServletContext().getRealPath("/upload")+"/questionnaire";

        File questionnareparent = new File(parentPath);
        if(!questionnareparent.exists()){
            questionnareparent.mkdirs();
        }

        String keyPath = new ObjectId().toString();
        String urlPath = "/upload/questionnaire/"+keyPath;
        String randomPath = parentPath+"/"+keyPath;
        File randomFile = new File(randomPath);
        if (!randomFile.exists()) {
            randomFile.mkdirs();
        }

        File wordFile =new File(randomPath, file.getOriginalFilename());
        String wordFilePath = randomPath+"/"+file.getOriginalFilename();

        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), wordFile);

            HWPFDocument document = new HWPFDocument(new FileInputStream(wordFile));
            WordToHtmlConverter converter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            //String wordUrl = wordFilePath;
            File parentDir = wordFile.getParentFile();
            //String parentUrl = wordUrl.substring(0, wordUrl.lastIndexOf("/"));
            converter.setPicturesManager(new WordPicManager(urlPath, parentDir.getPath()));
            converter.processDocument(document);
            DOMSource domSource = new DOMSource(converter.getDocument());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            String htmlFileName = file.getOriginalFilename()+".html";
            transformer.transform(domSource, new StreamResult(new File(parentDir, htmlFileName)));
            return urlPath + "/" + htmlFileName;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


        return null;


    }





    /**
     * 统计问卷
     *
     * @param id
     * @return
     */
    @RequestMapping("/stat")
    @ResponseBody
    public QuestionnaireDTO stat(String id, HttpServletResponse response) throws Exception{
        noCache(response);
        QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO(questionnaireService.findOne(id));
        return questionnaireDTO;
    }


    private void noCache(HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache");
    }





    enum OfficeType {
        WORD, EXCEL;
    }
    @RequestMapping("/export")
    @ResponseBody
    public Map<String, Object> export(String id, OfficeType type, String[] chart,HttpServletResponse response) throws Exception{
        QuestionnaireEntry questionnaire = questionnaireService.findOne(id);
        //HttpHeaders headers = new HttpHeaders();
        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStream os = response.getOutputStream();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        String suffix = "";
        if (type == OfficeType.EXCEL) {

            suffix = ".xls";
        }else {

            suffix = ".doc";
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((questionnaire.getName() + suffix).getBytes("utf-8"), "ISO8859-1"));

        try {
            if (type == OfficeType.EXCEL) {
                HSSFWorkbook wb = questionnaireService.getExcel1(id, chart);
                wb.write(os);
            } else {
                XWPFDocument doc = questionnaireService.getWord(id, chart);
                doc.write(os);
            }
            os.close();
            } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String,Object> retMap = new HashMap<String, Object>();

        return retMap;
    }


    /**得到班级和部门,问卷使用
     * @return
     */
    @RequestMapping("/classanddepart")
    @ResponseBody
    public List<ClassInfoDTO> getUserClasses() {
        List<ClassInfoDTO> classDtolist =  userService.getClassDTOList(getUserId(), getSessionValue().getUserRole());
        List<DepartmentEntry> departmentEntryList = departmentService.getDepartmentEntrys(new ObjectId(getSessionValue().getSchoolId()));
        List<ClassInfoDTO> retList = new ArrayList<ClassInfoDTO>();
        retList.addAll(classDtolist);
        for(DepartmentEntry de : departmentEntryList){
            ClassInfoDTO deInfo = new ClassInfoDTO();
            deInfo.setId(de.getID().toString());
            deInfo.setClassName(de.getName());
            retList.add(deInfo);
        }
        return  retList;
    }

    @RequestMapping("updateIsPublic")
    @ResponseBody
    public RespObj updateIsPublic(@ObjectIdType ObjectId qId, int isPublic){
        RespObj respObj = RespObj.FAILD;
        try{
            questionnaireService.updateIsPublic(qId, isPublic);
            respObj = RespObj.SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
        }
        return respObj;
    }

    @RequestMapping("/users")
    public String showUsers(){
        return "/questionnaire/users";
    }

    @RequestMapping("/{questionId}/users")
    @ResponseBody
    public Map<String, Object> getQuestionedUsers(@PathVariable @ObjectIdType ObjectId questionId, int page, int pageSize) throws IllegalParamException {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        List<Map<String, Object>> data = questionnaireService.getQuestionedUsers(questionId, schoolId, page, pageSize);

        QuestionnaireEntry questionnaireEntry = questionnaireService.findOne(questionId.toString());
        int totalNumber = questionnaireEntry.getTotalNumber();
        if(totalNumber == 0){
            totalNumber = questionnaireService.countQuestionedUsers(questionnaireEntry.getID(), schoolId);
            questionnaireEntry.setTotalNumber(totalNumber);
            questionnaireService.addQuestionnaire(questionnaireEntry);
        }

        model.put("userList", data);
        model.put("page", page);
        model.put("pageSize", pageSize);
        model.put("count", totalNumber);
        return model;
    }

    @RequestMapping("/sendLetters")
    @ResponseBody
   public RespObj sendLetters(@ObjectIdType ObjectId id, HttpServletRequest request){
       RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        questionnaireService.sendLetters(id, schoolId, request);
       return respObj;
   }





}