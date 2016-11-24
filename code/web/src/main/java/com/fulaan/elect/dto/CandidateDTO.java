package com.fulaan.elect.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Grade;
import com.pojo.user.SimpleUserInfo;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserInfoDTO;
import com.pojo.elect.Candidate;
import com.pojo.user.UserRole;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.sys.utils.CustomDateSerializer;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qinbo on 15/3/3.
 */
public class CandidateDTO {

    /**
     * 候选人user id
     */
    private String id;

    /**
     * 候选人姓名
     */
    private String name;

    /**
     * 竞选宣言
     */
    private String manifesto;

    /**
     * 录音链接
     */
    private String voiceUrl;

    /**
     * 图片链接
     */
    private List<String> picUrls;

    /**
     * 视频ID
     */
    private String videoId;

    /**
     * 报名时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date signTime;

    /**
     * 选票
     */
    private List<String> ballots;


    /**
     * 候选人的班级信息
     */
    private ClassInfoDTO classInfo;

    /**
     * 候选人的相关信息
     */
    private SimpleUserInfo user;


    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }

    private VideoDTO video;

    public CandidateDTO(){

    }

    public CandidateDTO(Candidate candidate) {
        this.id = candidate.getUserId().toString();
        this.name = candidate.getName();
        this.manifesto = candidate.getManifesto();
        this.voiceUrl = candidate.getVoiceUrl();
        this.picUrls = candidate.getPicUrls();
        /*if (candidate.getVideoId() != null) {
            this.videoId = candidate.getVideoId().toString();

            //video ,url, imageUrl,id
            VideoService videoService = new VideoService();
            VideoEntry videoEntry = videoService.getVideoEntryById(new ObjectId(this.videoId));
            this.video = new VideoDTO(videoEntry);
            this.video.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, videoEntry.getBucketkey()));

        }*/
        this.signTime = new Date(candidate.getSignTime());

        List<ObjectId> ballotIds = candidate.getBallots();
        if (ballotIds != null && !ballotIds.isEmpty()) {
            List<String> ballotList = new ArrayList<String>();
            for (ObjectId bId : ballotIds) {
                ballotList.add(bId.toString());
            }
            this.ballots = ballotList;
        }

       ClassService classService = new ClassService();
        SchoolService schoolService = new SchoolService();
        UserService userService=new UserService();
        //todo: get user class info from service

        this.classInfo = new ClassInfoDTO();
        //this.classInfo.setClassName(classService.findMainClassNameByUserId(this.id));
        this.classInfo.setGradeName("");
        this.classInfo.setClassName("");

        UserDetailInfoDTO userDetailInfoDTO=userService.getUserInfoById(this.id);
        if(UserRole.isStudent(userDetailInfoDTO.getRole()))
        {
            this.classInfo.setClassName(classService.findMainClassNameByUserId(this.id));
            Grade grade=schoolService.getStudentGrade(new ObjectId(this.id));
            if(grade!=null)
                this.classInfo.setGradeName(grade.getName());
        }
        else if(UserRole.isParent(userDetailInfoDTO.getRole()))
        {
            UserDetailInfoDTO dto=userService.findStuInfoByParentId(this.id);
            if(dto!=null) {
                String studentId = dto.getId();
                String className=classService.findMainClassNameByUserId(studentId);
                if(className!=null)
                    this.classInfo.setClassName(className);
                Grade grade = schoolService.getStudentGrade(new ObjectId(studentId));
                if (grade != null)
                    this.classInfo.setGradeName(grade.getName());
            }
        }
        //todo: get user class info from service
         /*UserService userService = new UserService();
        UserDetailInfoDTO userDetailInfoDTO=userService.getUserInfoById(this.id);
        SimpleUserInfo userInfo = new SimpleUserInfo();
        userInfo.setId(userDetailInfoDTO.getId());
        userInfo.setExperienceValue(userDetailInfoDTO.getExperienceValue());
        userInfo.setImgUrl(userDetailInfoDTO.getImgUrl());
        userInfo.setName(userDetailInfoDTO.getUserName());
        userInfo.setNickName(userDetailInfoDTO.getNickName());
        userInfo.setSex(userDetailInfoDTO.getSex());
        this.user = userInfo;*/





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

    public String getManifesto() {
        return manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public List<String> getBallots() {
        return ballots;
    }

    public void setBallots(List<String> ballots) {
        this.ballots = ballots;
    }


    public SimpleUserInfo getUser() {
        return user;
    }

    public void setUser(SimpleUserInfo user) {
        this.user = user;
    }

    public ClassInfoDTO getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfoDTO classInfo) {
        this.classInfo = classInfo;
    }


    /**
     * 从当前dto 生成candidate
     *
     * @return
     */
    public Candidate buildCandidate() {

        ObjectId _videoId = null;
        if (this.videoId != null) {
            _videoId = new ObjectId(this.videoId);
        }



        List<ObjectId> _ballots = null;
        if (this.ballots != null && !this.ballots.isEmpty()) {
            _ballots = new ArrayList<ObjectId>();
            for (String ballotId:this.ballots) {
                _ballots.add(new ObjectId(ballotId));
            }
        }

        Candidate candidate = new Candidate(
                new ObjectId(this.getId()),
                this.getName(),
                this.getManifesto(),
                this.getVoiceUrl(),
                picUrls, //picurls
                _videoId,
                this.getSignTime().getTime(),
                _ballots
        );
        candidate.setPicUrls(this.picUrls);

        return candidate;
    }

}
