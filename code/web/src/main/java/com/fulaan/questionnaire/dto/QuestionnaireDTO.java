package com.fulaan.questionnaire.dto;

import com.fulaan.user.service.UserService;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * Created by qinbo on 15/3/13.
 * todo：分成简单和详细的两个
 */
public class QuestionnaireDTO {

    /**
     * 问卷id
     */
    private String id;

    /**
     * 问卷名称
     */
    private String name;

    /**
     *  发起人id
     */
    private String publisher;

    /**
     * 发布者姓名
     */
    private String publishName;


    /**
     * 发起时间
     */
    private Date publishDate;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date endDate;
    private String stringEndDate;


    private String schoolId;
    /**
     * 所属班级ID(空表示全校范围)
     */
    private List<String> classIds;

    /**
     * 家长是否回应
     */
    private Boolean parentRespondent;
    /**
     * 学生是否回应
     */
    private Boolean studentRespondent;
    /**
     * 老师是否回应
     */
    private Boolean teacherRespondent;
    /**
     * 校长是否回应
     */
    private Boolean headmasterRespondent;
    /**
     * 问卷地址
     */
    private String docUrl;


    /**
     * 答题卡设置,>0表示选择题的选项个数，否则为问答题,<0表示多选题
     */
    List<Integer> answerSheet;
    /**
     * 回应结果
     */
    Map<String, List<Object>> respondents;


    /**
     * 可参加人员总共,未参加
     */
    private List<String> userIds;
    /**
     * 对象用户信息，不存入数据库,
     */
    //todo: UserInfoDTO 改成这里实际用的
    private List<UserInfoDTO> users;

    /**
     * 状态 1：自己发布的问卷  2：问卷已结束  3：自己已填写  4：自己未填写
     */
    private Integer state;

    /**
     * 是否对外公开调查结果 0：不公开  1：公开   默认0
     */
    private int isPublic;


    /**
     * 是否全平台  0：不是  1：是  默认0
     */
    private int isPlatform;

    private String process;





    public QuestionnaireDTO(){

    }

    public QuestionnaireDTO(QuestionnaireEntry questionnaireEntry){
        this.id = questionnaireEntry.getID().toString();
        this.name = questionnaireEntry.getName();
        this.publisher = questionnaireEntry.getPublisherId().toString();

        this.publishDate = new Date(questionnaireEntry.getPublishTime());

        this.endDate = new Date(questionnaireEntry.getEndTime());
        this.stringEndDate = DateTimeUtils.convert(questionnaireEntry.getEndTime(),"yyyy/MM/dd");
        ObjectId schoolId = questionnaireEntry.getSchoolId();
        this.schoolId = schoolId==null ? "" : schoolId.toString();
        this.classIds = questionnaireEntry.getClassIds();


        this.parentRespondent = (questionnaireEntry.getParentRespondent()==1);

        this.studentRespondent = (questionnaireEntry.getStudentRespondent() == 1);

        this.teacherRespondent = (questionnaireEntry.getTeacherRespondent() == 1);

        this.headmasterRespondent = (questionnaireEntry.getHeadmasterRespondent() == 1);

        this.docUrl = questionnaireEntry.getDocUrl();

        this.answerSheet = questionnaireEntry.getAnswerSheet();

        this.respondents = null;
        if(questionnaireEntry.getRespondent()!=null)
        {
            Map<String,List<Object>> resMap = questionnaireEntry.getRespondent();
            this.respondents = new HashMap<String, List<Object>>();
            for(String userKey : resMap.keySet()){
                this.respondents.put(userKey,resMap.get(userKey));

            }
        }

        //todo: change null

        UserService userService = new UserService();
        UserEntry userEntry = userService.searchUserId(questionnaireEntry.getPublisherId());
        this.publishName = userEntry==null ? "匿名" : userEntry.getUserName();

        this.userIds = null;
        this.users = null;
        this.isPublic = questionnaireEntry.getIsPublic();
        this.isPlatform = questionnaireEntry.getIsPlatform();
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<String> classIds) {
        this.classIds = classIds;
    }

    public Boolean getParentRespondent() {
        return parentRespondent;
    }

    public void setParentRespondent(Boolean parentRespondent) {
        this.parentRespondent = parentRespondent;
    }

    public Boolean getStudentRespondent() {
        return studentRespondent;
    }

    public void setStudentRespondent(Boolean studentRespondent) {
        this.studentRespondent = studentRespondent;
    }

    public Boolean getTeacherRespondent() {
        return teacherRespondent;
    }

    public void setTeacherRespondent(Boolean teacherRespondent) {
        this.teacherRespondent = teacherRespondent;
    }

    public Boolean getHeadmasterRespondent() {
        return headmasterRespondent;
    }

    public void setHeadmasterRespondent(Boolean headmasterRespondent) {
        this.headmasterRespondent = headmasterRespondent;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public List<Integer> getAnswerSheet() {
        return answerSheet;
    }

    public void setAnswerSheet(List<Integer> answerSheet) {
        this.answerSheet = answerSheet;
    }

    public Map<String, List<Object>> getRespondents() {
        return respondents;
    }

    public void setRespondents(Map<String, List<Object>> respondents) {
        this.respondents = respondents;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<UserInfoDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDTO> users) {
        this.users = users;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getStringEndDate() {
        return stringEndDate;
    }

    public void setStringEndDate(String stringEndDate) {
        this.stringEndDate = stringEndDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }


    public int getIsPlatform() {
        return isPlatform;
    }

    public void setIsPlatform(int isPlatform) {
        this.isPlatform = isPlatform;
    }
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public QuestionnaireEntry buildSurveyEntry(){

        ObjectId pb = null;
        if(this.publisher!=null) {
            pb = new ObjectId(this.publisher);
        }

        ObjectId sid = null;
        if(this.schoolId!=null){
            sid = new ObjectId(this.schoolId);
        }

        long pbt = System.currentTimeMillis();
        if(this.publishDate!=null){
            pbt = this.publishDate.getTime();
        }

        List<ObjectId> cls = null;
        if(this.classIds!=null&& this.classIds.size()>0){
            cls = new ArrayList<ObjectId>();
            for(String clstr : this.classIds){
                cls.add(new ObjectId(clstr));
            }
        }

        int parr = this.getParentRespondent()?1:0;
        int stur = this.getStudentRespondent()?1:0;
        int tear = this.getTeacherRespondent()?1:0;
        int hear = this.getHeadmasterRespondent()?1:0;


        Map<String,List<Object>> res = null;
        if(this.respondents!=null && !this.respondents.isEmpty()){
            res = new HashMap<String, List<Object>>();
            for(String userKey : this.respondents.keySet()){
                res.put(userKey,
                        this.respondents.get(userKey));

            }
        }

        QuestionnaireEntry questionnaireEntry = new QuestionnaireEntry(
                this.getName(),
                pb,
                pbt,
                this.endDate.getTime(),
                sid,
                cls,
                parr,
                stur,
                tear,
                hear,
                this.getDocUrl(),
                this.getAnswerSheet(),
                res,
                isPublic,
                isPlatform

        );

        return questionnaireEntry;
    }



}
