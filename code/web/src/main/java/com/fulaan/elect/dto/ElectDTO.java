package com.fulaan.elect.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fulaan.learningcenter.service.InteractLessonService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.video.service.VideoService;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.InterestClassDTO;
import com.pojo.school.InterestClassEntry;
import com.pojo.user.SimpleUserInfo;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.elect.Candidate;
import com.pojo.elect.ElectEntry;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.sys.constants.Constant;
import com.sys.utils.CustomDateSerializer;
import com.sys.utils.DayDateSerializer;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * Created by qinbo on 15/3/2.
 */
public class ElectDTO {



    private String id;

    /**
     * 标题
     */
    private String name;

    /**
     * 说明
     */
    private String description;


    /**
     * 班级ID，空表示全校
     */
    private List<String> classIds;

    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyyMMdd")
    @JsonSerialize(using = DayDateSerializer.class)
    private Date startDate;
    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyyMMdd")
    @JsonSerialize(using = DayDateSerializer.class)
    private Date endDate;

    /**
     * 发布人id
     */
    private String publisher;

    /**
     * 发布时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date publishDate;

    /**
     * 家长参选资格
     */
    private Boolean parentEligible;

    /**
     * 学生参选资格
     */
    private Boolean studentEligible;

    /**
     * 老师参选资格
     */
    private Boolean teacherEligible;
    /**
     * 校领导参选资格
     */
    private Boolean leaderEligible;
    /**
     * 家长投票权
     */
    private Boolean parentVotable;

    /**
     * 学生投票权
     */
    private Boolean studentVotable;

    /**
     * 老师投票权
     */
    private Boolean teacherVotable;
    /**
     * 校领导投票权
     */
    private Boolean leaderVotable;
    /**
     * 每人票数
     */
    private Integer ballotCount;

    public Boolean getLeaderEligible() {
        return leaderEligible;
    }

    public void setLeaderEligible(Boolean leaderEligible) {
        this.leaderEligible = leaderEligible;
    }

    public Boolean getLeaderVotable() {
        return leaderVotable;
    }

    public void setLeaderVotable(Boolean leaderVotable) {
        this.leaderVotable = leaderVotable;
    }

    /**
     * 修改时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date modifyTime;

    /**
     * 候选人列表
     */
    private List<CandidateDTO> candidates=new ArrayList<CandidateDTO>();


    private Boolean voting;
    private Boolean notify;


    public int getPublish() {
        return publish;
    }

    public void setPublish(int publish) {
        this.publish = publish;
    }

    private int publish;//是否发布选举结果，默认发布，0发布  1不发布
    /**
     * classids 对应的班级信息
     */
    private Map<String,ClassInfoDTO> classes;

    /**
     * 发布者信息
     */
    private UserDetailInfoDTO publishUser;

    private int manageType;



    public ElectDTO(){

    }

    public ElectDTO(ElectEntry elect)
    {

        this.id = elect.getID().toString();
        this.name = elect.getName();
        this.description = elect.getDescription();

        this.classIds = elect.getClassIds();

        this.schoolId = elect.getSchoolId().toString();

        this.startDate = new Date(elect.getStartTime());

        this.endDate = new Date(elect.getEndTime());

        this.publisher = elect.getPublisherId().toString();

        this.publishDate = new Date(elect.getPublishTime());

        this.parentEligible = (elect.getParentEligible()==1);

        this.studentEligible = (elect.getStudentEligible() == 1);

        this.teacherEligible = (elect.getTeacherEligible() == 1);

        this.leaderEligible = (elect.getLeaderEligible() == 1);

        this.parentVotable = (elect.getParentVotable() ==  1);

        this.teacherVotable = (elect.getTeacherVotable() == 1);

        this.studentVotable = (elect.getStudentVotable() == 1);

        this.leaderVotable = (elect.getLeaderVotable() ==1);

        this.ballotCount = elect.getBallotCount();

        this.modifyTime = new Date(elect.getModifyTime());

        this.publish = elect.getPublish();

        this.manageType = (elect.getClassIds()==null || elect.getClassIds().size()==0)?1:0;


        VideoService videoService=new VideoService();
        ClassService classService = new ClassService();
        UserService userService = new UserService();
        SchoolService schoolService = new SchoolService();
        // candidate
        if(elect.getCandidates()!=null && !elect.getCandidates().isEmpty())
        {
            this.candidates = new ArrayList<CandidateDTO>();
            List<ObjectId> videoIds=new ArrayList<ObjectId>();
            List<ObjectId> userIds=new ArrayList<ObjectId>();
            for(Candidate candidate: elect.getCandidates())
            {
                if(candidate.getVideoId()!=null)
                    videoIds.add(candidate.getVideoId());
                userIds.add(candidate.getUserId());
            }
            Map<ObjectId,VideoEntry> map=videoService.getVideoEntryMap(videoIds,Constant.FIELDS);
            Map<ObjectId,UserEntry> userMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
            for(Candidate candidate: elect.getCandidates())
            {
                CandidateDTO candidateDTO=new CandidateDTO(candidate);
                if(candidate.getVideoId()!=null &&!candidate.getVideoId().equals("")) {
                    VideoEntry videoEntry=map.get(candidate.getVideoId());
                    if(videoEntry!=null) {
                        VideoDTO videoDTO = new VideoDTO(videoEntry);
                        candidateDTO.setVideoId(videoEntry.getID().toString());
                        candidateDTO.setVideo(videoDTO);
                    }
                }
                UserEntry userEntry=userMap.get(candidate.getUserId());
                if(userEntry!=null) {
                    SimpleUserInfo userInfo = new SimpleUserInfo(userEntry);
                    userInfo.setSchoolName(schoolService.findSchoolNameByUserId(userEntry.getID().toString()));
                    candidateDTO.setUser(userInfo);
                    this.candidates.add(candidateDTO);
                }

            }
        }


        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        if(currentDate.getTimeInMillis()-100000 <=elect.getEndTime())
        {
            setVoting(true);
        }
        else
        {
            setVoting(false);
        }


        //set classes,publisher

        //ClassService classService = new ClassService();
        InterestClassService interestClassService=new InterestClassService();
        if(classIds!=null)
        {
            this.classes = new HashMap<String, ClassInfoDTO>();
            List<InterestClassEntry> interestClassDTOList=interestClassService.getInterestClassEntryBySchoolId(new ObjectId(this.schoolId));
            List<ClassInfoDTO> classInfoDTOList=classService.findClassInfoBySchoolId(this.schoolId);
            for(String classId :classIds)
            {
                boolean have=false;
                for (ClassInfoDTO classInfoDTO:classInfoDTOList)
                {
                    if(classInfoDTO.getId().equals(classId))
                    {
                        this.classes.put(classId, classInfoDTO);
                        have=true;
                        break;
                    }
                }
                if(!have) {
                    for (InterestClassEntry interestClassDTO : interestClassDTOList) {
                        if (interestClassDTO.getID().toString().equals(classId)) {
                            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                            classInfoDTO.setClassName(interestClassDTO.getClassName());
                            this.classes.put(classId, classInfoDTO);
                            break;
                        }
                    }
                }
                //todo: use cache
               /* ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
                if(classEntry==null)
                {
                    InterestClassDTO interestClassDTO =  interestClassService.findInterestClassByClassId(classId);
                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassName(interestClassDTO.getClassName());
                    this.classes.put(classId, classInfoDTO);
                }
                else
                {
                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassName(classEntry.getName());
                    this.classes.put(classId, classInfoDTO);
                }*/

            }
        }

        //todo: get user info from service

        //UserService userService = new UserService();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(this.publisher);
        this.publishUser = userInfo;


        //todo:notify 现在没用

        


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<String> classIds) {
        this.classIds = classIds;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Boolean getParentEligible() {
        return parentEligible;
    }

    public void setParentEligible(Boolean parentEligible) {
        this.parentEligible = parentEligible;
    }

    public Boolean getStudentEligible() {
        return studentEligible;
    }

    public void setStudentEligible(Boolean studentEligible) {
        this.studentEligible = studentEligible;
    }

    public Boolean getTeacherEligible() {
        return teacherEligible;
    }

    public void setTeacherEligible(Boolean teacherEligible) {
        this.teacherEligible = teacherEligible;
    }

    public Boolean getParentVotable() {
        return parentVotable;
    }

    public void setParentVotable(Boolean parentVotable) {
        this.parentVotable = parentVotable;
    }

    public Boolean getStudentVotable() {
        return studentVotable;
    }

    public void setStudentVotable(Boolean studentVotable) {
        this.studentVotable = studentVotable;
    }

    public Boolean getTeacherVotable() {
        return teacherVotable;
    }

    public void setTeacherVotable(Boolean teacherVotable) {
        this.teacherVotable = teacherVotable;
    }

    public Integer getBallotCount() {
        return ballotCount;
    }

    public void setBallotCount(Integer ballotCount) {
        this.ballotCount = ballotCount;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<CandidateDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateDTO> candidates) {
        this.candidates = candidates;
    }

    public Boolean getVoting() {
        return voting;
    }

    public void setVoting(Boolean voting) {
        this.voting = voting;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }


    public Map<String, ClassInfoDTO> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, ClassInfoDTO> classes) {
        this.classes = classes;
    }

    public UserDetailInfoDTO getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(UserDetailInfoDTO publishUser) {
        this.publishUser = publishUser;
    }

    public int getManageType() {
        return manageType;
    }

    public void setManageType(int manageType) {
        this.manageType = manageType;
    }

    /** 从当前传入的DTO产生Entry
     * @return
     */
    public ElectEntry buildElectEntry(){

        int _parentEligible = this.getParentEligible()?1:0;
        int _studentEligible = this.getStudentEligible()?1:0;
        int _teacherEligible = this.getTeacherEligible()?1:0;
        int _leaderEligible =0;
        if(this.getLeaderEligible()!=null)
            _leaderEligible = this.getLeaderEligible()?1:0;

        int _parentVotable = this.getParentVotable()?1:0;
        int _studentVotable = this.getStudentVotable()?1:0;
        int _teacherVotable = this.getTeacherVotable()?1:0;
        int _leaderVotable = 0;
        if(this.getLeaderVotable()!=null)
            _leaderVotable = this.getLeaderVotable()?1:0;


        List<ObjectId> _classIds = null;
        if(this.getClassIds()!=null ){
            _classIds = new ArrayList<ObjectId>();
            for(int i=0;i<this.getClassIds().size();i++){
                if (!StringUtils.isEmpty(this.getClassIds().get(i))) {
                    _classIds.add(new ObjectId(this.getClassIds().get(i)));
                }
            }
        }



        //todo: candidate

        ElectEntry electEntry = new ElectEntry(
                this.getName(),
                this.getDescription(),
                _classIds,
                new ObjectId(this.getSchoolId()), // school id
                this.getStartDate().getTime(),
                this.getEndDate().getTime(),
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                new ObjectId(this.getPublisher()),
                _parentEligible,_studentEligible,_teacherEligible,_leaderEligible,
                _parentVotable,_studentVotable,_teacherVotable,_leaderVotable,
                this.getBallotCount(),
                null,
                this.getPublish(),
                _classIds.size()!=0?0:1
        );


        return electEntry;
    }


}
