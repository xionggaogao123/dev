package com.fulaan.extendedcourse.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import com.pojo.extendedcourse.ExtendedCourseEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-12-07.
 */
public class ExtendedCourseDTO {
    private String id;
    private String schoolId;
    private int type;   //1 抢课（实时显示）    2 火速报名（结束显示）
    private String userId;
    private String courseName;
    private String description;
    private String typeId;
    private String typeName;
    private String applyStartTime;
    private String applyEndTime;
    private String voteStartTime;
    private String voteEndTime;
    private int week;
    private int lessonType;
    private String teacherName;
    private String teacherId;
    private List<String> gradeList = new ArrayList<String>();
    private int userAllNumber;
    private int classUserNumber;
    private String roomName;
    private String createTime;
    private String gradeName;
    private String courseLabel;
    private int courseCount;
    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();           //提交
    private List<Attachement> imageList=new ArrayList<Attachement>();     //提交
    private List<Attachement> attachements=new ArrayList<Attachement>();  //提交
    private List<Attachement> voiceList=new ArrayList<Attachement>();     //提交

    private int status; //0  全部   1  报名中   2  学习中   3 已学完
    private int stage;//   1 报名未开始   2 报名进行中  3 火速报名进行中  4 报名已抢光  5 已报名  6 报名已结束 （针对报名中） 7 已入选已结束
    private int role;  // 1 老师  2 家长  3 学生
    private static final String[] gradeString = new String[]{"","一年级","二年级","三年级","四年级","五年级","六年级","初一","初二","初三","高一","高二","高三"};
    public  String getGrade(List<String> gradeList){
        String name = "";
        for(String str:gradeList){
            int index = Integer.parseInt(str);
            name = name + gradeString[index]+"、";
        }
        String string  = name.substring(0,name.length()-1);
        return string;
    }

    public ExtendedCourseDTO(){

    }
    public ExtendedCourseDTO(ExtendedCourseEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
            this.userId =e.getUserId()==null?"":e.getUserId().toString();
            this.courseName = e.getCourseName();
            this.description =e.getDescription();
            this.typeId = e.getTypeId()==null?"":e.getTypeId().toString();
            this.typeName = e.getTypeName();
            this.type = e.getType();
            this.week = e.getWeek();
            this.lessonType = e.getLessonType();
            this.teacherName = e.getTeacherName();
            this.teacherId =e.getTeacherId()==null?"":e.getTeacherId().toString();
            this.gradeList = e.getGradeList();
            this.gradeName = getGrade(e.getGradeList());
            this.userAllNumber = e.getUserAllNumber();
            this.classUserNumber = e.getClassUserNumber();
            this.roomName = e.getRoomName();
            List<AttachmentEntry> attachmentEntries = e.getImageList();
            if(attachmentEntries != null && attachmentEntries.size()>0){
                for(AttachmentEntry entry : attachmentEntries){
                    this.imageList.add(new Attachement(entry));
                }
            }
            List<AttachmentEntry> attachmentEntries2 = e.getAttachmentEntries();
            if(attachmentEntries2 != null && attachmentEntries2.size()>0){
                for(AttachmentEntry entry2 : attachmentEntries2){
                    this.attachements.add(new Attachement(entry2));
                }
            }
            List<AttachmentEntry> attachmentEntries3 = e.getVoiceList();
            if(attachmentEntries3 != null && attachmentEntries3.size()>0){
                for(AttachmentEntry entry3 : attachmentEntries3){
                    this.voiceList.add(new Attachement(entry3));
                }
            }
            List<VideoEntry> videoEntries = e.getVideoList();
            if(videoEntries != null && videoEntries.size()>0) {
                for (VideoEntry entry3 : videoEntries) {
                    this.videoList.add(new VideoDTO(entry3));
                }
            }
            if(e.getApplyStartTime()!=0l){
                this.applyStartTime = DateTimeUtils.getLongToStrTimeTwo(e.getApplyStartTime()).substring(0,10);
            }else{
                this.applyStartTime = "";
            }
            if(e.getApplyEndTime()!=0l){
                this.applyEndTime = DateTimeUtils.getLongToStrTimeTwo(e.getApplyEndTime()).substring(0,10);
            }else{
                this.applyEndTime = "";
            }
            if(e.getVoteStartTime()!=0l){
                this.voteStartTime = DateTimeUtils.getLongToStrTimeTwo(e.getVoteStartTime()).substring(0,10);
            }else{
                this.voteStartTime = "";
            }
            if(e.getVoteEndTime()!=0l){
                this.voteEndTime = DateTimeUtils.getLongToStrTimeTwo(e.getVoteEndTime()).substring(0,10);
            }else{
                this.voteEndTime = "";
            }
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }

        }else{
            new ExtendedCourseDTO();
        }
    }

    public ExtendedCourseDTO(ExtendedCourseEntry e,int status){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
            this.userId =e.getUserId()==null?"":e.getUserId().toString();
            this.courseName = e.getCourseName();
            this.description =e.getDescription();
            this.typeId = e.getTypeId()==null?"":e.getTypeId().toString();
            this.typeName = e.getTypeName();
            this.type = e.getType();
            this.week = e.getWeek();
            this.lessonType = e.getLessonType();
            this.teacherName = e.getTeacherName();
            this.teacherId =e.getTeacherId()==null?"":e.getTeacherId().toString();
            this.gradeList = e.getGradeList();
            this.gradeName = getGrade(e.getGradeList());
            this.userAllNumber = e.getUserAllNumber();
            this.classUserNumber = e.getClassUserNumber();
            this.roomName = e.getRoomName();
            this.courseCount = e.getCourseCount();
            this.courseLabel = e.getCourseLabel();
            List<AttachmentEntry> attachmentEntries = e.getImageList();
            if(attachmentEntries != null && attachmentEntries.size()>0){
                for(AttachmentEntry entry : attachmentEntries){
                    this.imageList.add(new Attachement(entry));
                }
            }
            List<AttachmentEntry> attachmentEntries2 = e.getAttachmentEntries();
            if(attachmentEntries2 != null && attachmentEntries2.size()>0){
                for(AttachmentEntry entry2 : attachmentEntries2){
                    this.attachements.add(new Attachement(entry2));
                }
            }
            List<AttachmentEntry> attachmentEntries3 = e.getVoiceList();
            if(attachmentEntries3 != null && attachmentEntries3.size()>0){
                for(AttachmentEntry entry3 : attachmentEntries3){
                    this.voiceList.add(new Attachement(entry3));
                }
            }
            List<VideoEntry> videoEntries = e.getVideoList();
            if(videoEntries != null && videoEntries.size()>0) {
                for (VideoEntry entry3 : videoEntries) {
                    this.videoList.add(new VideoDTO(entry3));
                }
            }
            if(e.getApplyStartTime()!=0l){
                this.applyStartTime = dateToStrLong(new Date(e.getApplyStartTime()), "yyyy/MM/dd-HH:mm");
            }else{
                this.applyStartTime = "";
            }
            if(e.getApplyEndTime()!=0l){
                this.applyEndTime = dateToStrLong(new Date(e.getApplyEndTime()), "yyyy/MM/dd-HH:mm");
            }else{
                this.applyEndTime = "";
            }
            if(e.getVoteStartTime()!=0l){
                this.voteStartTime = DateTimeUtils.getLongToStrTimeTwo(e.getVoteStartTime()).substring(0, 10);
            }else{
                this.voteStartTime = "";
            }
            if(e.getVoteEndTime()!=0l){
                this.voteEndTime = DateTimeUtils.getLongToStrTimeTwo(e.getVoteEndTime()).substring(0,10);
            }else{
                this.voteEndTime = "";
            }
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }

        }else{
            new ExtendedCourseDTO();
        }
    }

    public static String dateToStrLong(Date dateDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public ExtendedCourseEntry addEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId sId=null;
        if(this.getSchoolId()!=null&&!"".equals(this.getSchoolId())){
            sId=new ObjectId(this.getSchoolId());
        }
        ObjectId tpd=null;
        if(this.getTypeId()!=null&&!"".equals(this.getTypeId())){
            tpd=new ObjectId(this.getTypeId());
        }

        ObjectId tId=null;
        if(this.getTeacherId()!=null&&!"".equals(this.getTeacherId())){
            tId=new ObjectId(this.getTeacherId());
        }
        long ast = 0l;
        if(this.getApplyStartTime() != null && this.getApplyStartTime() != ""){
            ast = DateTimeUtils.getStrToLongTime(this.getApplyStartTime(),"yyyy-MM-dd HH:mm");
        }
        long aet = 0l;
        if(this.getApplyEndTime() != null && this.getApplyEndTime() != ""){
            aet = DateTimeUtils.getStrToLongTime(this.getApplyEndTime(),"yyyy-MM-dd HH:mm");
        }
        long vst = 0l;
        if(this.getVoteStartTime() != null && this.getVoteStartTime() != ""){
            vst = DateTimeUtils.getStrToLongTime(this.getVoteStartTime(),"yyyy-MM-dd HH:mm");
        }
        long vet = 0l;
        if(this.getVoteEndTime() != null && this.getVoteEndTime() != ""){
            vet = DateTimeUtils.getStrToLongTime(this.getVoteEndTime(),"yyyy-MM-dd HH:mm");
        }
        List<VideoEntry> videoEntries=new ArrayList<VideoEntry>();
        if(videoList.size()>0){
            for(VideoDTO videoDTO:videoList){
                videoEntries.add(new VideoEntry(videoDTO.getVideoUrl(),
                        videoDTO.getImageUrl(),System.currentTimeMillis(), uId));
            }
        }
        List<AttachmentEntry> imageEntries=new ArrayList<AttachmentEntry>();
        if(imageList.size()>0){
            for(Attachement image:imageList){
                imageEntries.add(new AttachmentEntry(image.getUrl(), image.getFlnm(),
                        System.currentTimeMillis(),
                        uId));
            }
        }
        List<AttachmentEntry> attachmentEntries=new ArrayList<AttachmentEntry>();
        if(attachements.size()>0){
            for(Attachement attachement:attachements){
                attachmentEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        uId));
            }
        }
        List<AttachmentEntry> voiceEntries=new ArrayList<AttachmentEntry>();
        if(voiceList.size()>0){
            for(Attachement attachement:voiceList){
                voiceEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        uId));
            }
        }
        ExtendedCourseEntry openEntry =
                new ExtendedCourseEntry(
                        sId,
                        uId,
                        this.type,
                        this.courseName,
                        this.description,
                        tpd,
                        this.typeName,
                        ast,
                        aet,
                        vst,
                        vet,
                        this.week,
                        this.lessonType,
                        this.teacherName,
                        tId,
                        this.gradeList,
                        this.userAllNumber,
                        this.classUserNumber,
                        this.roomName,
                        this.courseLabel,
                        this.courseCount,
                        new ArrayList<ObjectId>(),
                        new ArrayList<ObjectId>(),
                        imageEntries,
                        voiceEntries,
                        videoEntries,
                        attachmentEntries);
        return openEntry;
    }

    public String getCourseLabel() {
        return courseLabel;
    }

    public void setCourseLabel(String courseLabel) {
        this.courseLabel = courseLabel;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getApplyStartTime() {
        return applyStartTime;
    }

    public void setApplyStartTime(String applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public String getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(String applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getVoteStartTime() {
        return voteStartTime;
    }

    public void setVoteStartTime(String voteStartTime) {
        this.voteStartTime = voteStartTime;
    }

    public String getVoteEndTime() {
        return voteEndTime;
    }

    public void setVoteEndTime(String voteEndTime) {
        this.voteEndTime = voteEndTime;
    }



    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getLessonType() {
        return lessonType;
    }

    public void setLessonType(int lessonType) {
        this.lessonType = lessonType;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<String> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<String> gradeList) {
        this.gradeList = gradeList;
    }

    public int getUserAllNumber() {
        return userAllNumber;
    }

    public void setUserAllNumber(int userAllNumber) {
        this.userAllNumber = userAllNumber;
    }

    public int getClassUserNumber() {
        return classUserNumber;
    }

    public void setClassUserNumber(int classUserNumber) {
        this.classUserNumber = classUserNumber;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<VideoDTO> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoDTO> videoList) {
        this.videoList = videoList;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }

    public List<Attachement> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<Attachement> voiceList) {
        this.voiceList = voiceList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
