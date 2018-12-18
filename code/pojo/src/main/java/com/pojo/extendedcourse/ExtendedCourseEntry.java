package com.pojo.extendedcourse;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-12-06.
 *
 * 拓展课 — 课程详情
 *
 *  schoolId                 学校id                        sid
 *  userId                   开课人                        uid
 *  type                     课程类型                      typ      1 抢课（实时显示）    2 火速报名（结束显示）
 *  courseName               课程名称                      cnm
 *  description              课程简介                      des
 *  typeId                   类型id                        tpd
 *  typeName                 课程类型                      tnm
 *  applyStartTime           申请开始时间                  ast
 *  applyEndTime             申请结束时间                  aet
 *  voteStartTime            课程开始时间                  vst
 *  voteEndTime              课程结束时间                  vet
 *  week                     星期                          wek
 *  lessonType               课节（第几节）                lty
 *  teacherName              上课老师名称                  tna
 *  teacherId                老师id                        tid
 *  gradeList                上课年级集合                  glt
 *  userAllNumber            上课总人数                    unm
 *  classUserNumber          每班人数                      cun (为 0  不限制)
 *  roomName                 教室名称                      rnm
 *  courseLabel              课程编号                      cll
 *  courseCount              课程数                        cct
 *  List<VideoEntry> videoList,                        vl//视频
 *  List<AttachmentEntry> attachmentEntries,           ats//文件
 *  List<AttachmentEntry> imageList                    il//图片
 *  List<AttachmentEntry> voiceList                    vt//语音
 *
 *  userApplyList            申请用户集合                  ult
 *  userSelectedList         上课用户集合                  slt
 *
 */
public class ExtendedCourseEntry extends BaseDBObject {

    public ExtendedCourseEntry(){

    }

    public ExtendedCourseEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public ExtendedCourseEntry(
            ObjectId schoolId,
            ObjectId userId,
            int type,
            String courseName,
            String description,
            ObjectId typeId,
            String typeName,
            long applyStartTime,
            long applyEndTime,
            long voteStartTime,
            long voteEndTime,
            int week,
            int lessonType,
            String teacherName,
            ObjectId teacherId,
            List<String> gradeList,
            int userAllNumber,
            int classUserNumber,
            String roomName,
            String courseLabel,
            int courseCount,
            List<ObjectId> userApplyList,
            List<ObjectId> userSelectedList,
            List<AttachmentEntry> imageList,
            List<AttachmentEntry> voiceList,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries
    ){

        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("uid", userId)
                .append("typ", type)
                .append("cnm", courseName)
                .append("des", description)
                .append("tpd", typeId)
                .append("tnm", typeName)
                .append("ast", applyStartTime)
                .append("aet", applyEndTime)
                .append("vst", voteStartTime)
                .append("vet", voteEndTime)
                .append("wek", week)
                .append("lty", lessonType)
                .append("tna", teacherName)
                .append("tid", teacherId)
                .append("glt", gradeList)
                .append("unm", userAllNumber)
                .append("cun", classUserNumber)
                .append("rnm", roomName)
                .append("cll",courseLabel)
                .append("cct",courseCount)
                .append("ult", userApplyList)
                .append("slt", userSelectedList)
                .append("ats", MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("vl", MongoUtils.fetchDBObjectList(videoList))
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);

    }
    public String getCourseLabel(){
        return getSimpleStringValue("cll");
    }

    public void setCourseLabel(int courseLabel){
        setSimpleValue("cll",courseLabel);
    }

    public int getCourseCount(){
        return getSimpleIntegerValueDef("cct",0);
    }

    public void setCourseCount(int courseCount){
        setSimpleValue("cct",courseCount);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public void setUserSelectedList(List<ObjectId> userSelectedList){
        setSimpleValue("slt", MongoUtils.convert(userSelectedList));
    }

    public List<ObjectId> getUserSelectedList(){
        ArrayList<ObjectId> userSelectedList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("slt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                userSelectedList.add((ObjectId)obj);
            }
        }
        return userSelectedList;
    }

    public void setUserApplyList(List<ObjectId> userApplyList){
        setSimpleValue("ult", MongoUtils.convert(userApplyList));
    }

    public List<ObjectId> getUserApplyList(){
        ArrayList<ObjectId> userApplyList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("ult");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                userApplyList.add((ObjectId)obj);
            }
        }
        return userApplyList;
    }

    public void setRoomName(String roomName){
        setSimpleValue("rnm",roomName);
    }
    public String getRoomName(){
        return getSimpleStringValue("rnm");
    }


    public void setUserAllNumber(int userAllNumber){
        setSimpleValue("unm",userAllNumber);
    }
    public int getUserAllNumber(){
        return getSimpleIntegerValue("unm");
    }

    public void setClassUserNumber(int classUserNumber){
        setSimpleValue("cun",classUserNumber);
    }
    public int getClassUserNumber(){
        return getSimpleIntegerValue("cun");
    }


    public List<String> getGradeList(){
        @SuppressWarnings("rawtypes")
        List voteTypeList =(List)getSimpleObjectValue("glt");
        return voteTypeList;
    }

    public void setGradeList(List<String> gradeList){
        setSimpleValue("glt",gradeList);
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public ObjectId getTeacherId(){
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId){
        setSimpleValue("tid",teacherId);
    }

    public void setTypeId(ObjectId typeId){
        setSimpleValue("tpd",typeId);
    }

    public ObjectId getTypeId(){
        return getSimpleObjecIDValue("tpd");
    }

    public void setCourseName(String courseName){
        setSimpleValue("cnm",courseName);
    }

    public String getCourseName(){
        return getSimpleStringValue("cnm");
    }

    public String getTypeName(){
        return getSimpleStringValue("tnm");
    }

    public void setTypeName(String typeName){
        setSimpleValue("tnm",typeName);
    }

    public void setDescription(String description){
        setSimpleValue("des",description);
    }



    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setTeacherName(String teacherName){
        setSimpleValue("tna",teacherName);
    }

    public String getTeacherName(){
        return getSimpleStringValue("tna");
    }


    public void setVoiceList(List<AttachmentEntry> voiceList){
        setSimpleValue("vt",MongoUtils.fetchDBObjectList(voiceList));
    }

    public List<AttachmentEntry> getVoiceList() {
        BasicDBList list = getDbList("vt");
        List<AttachmentEntry> voiceList = new ArrayList<AttachmentEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            voiceList.add(new AttachmentEntry(dbObject));
        }
        return voiceList;
    }
    public void setAttachmentEntries(List<AttachmentEntry> attachmentEntries){
        setSimpleValue("ats",MongoUtils.fetchDBObjectList(attachmentEntries));
    }

    public List<AttachmentEntry> getAttachmentEntries() {
        BasicDBList list = getDbList("ats");
        List<AttachmentEntry> attachmentEntries = new ArrayList<AttachmentEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            attachmentEntries.add(new AttachmentEntry(dbObject));
        }
        return attachmentEntries;
    }


    public void setImageList(List<AttachmentEntry> imageList){
        setSimpleValue("il",MongoUtils.fetchDBObjectList(imageList));
    }

    public List<AttachmentEntry> getImageList() {
        BasicDBList list = getDbList("il");
        List<AttachmentEntry> imageEntries = new ArrayList<AttachmentEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            imageEntries.add(new AttachmentEntry(dbObject));
        }
        return imageEntries;
    }

    public void setVideoList(List<VideoEntry> videoList){
        setSimpleValue("vl",MongoUtils.fetchDBObjectList(videoList));
    }

    public List<VideoEntry> getVideoList() {
        BasicDBList list = getDbList("vl");
        List<VideoEntry> videoEntries = new ArrayList<VideoEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            videoEntries.add(new VideoEntry(dbObject));
        }
        return videoEntries;
    }

    public long getVoteStartTime(){
        return getSimpleLongValue("vst");
    }
    public void setVoteStartTime(long voteStartTime){
        setSimpleValue("vst",voteStartTime);
    }

    public long getVoteEndTime(){
        return getSimpleLongValue("vet");
    }
    public void setVoteEndTime(long voteEndTime){
        setSimpleValue("vet",voteEndTime);
    }



    public long getApplyStartTime(){
        return getSimpleLongValue("ast");
    }
    public void setApplyStartTime(long applyStartTime){
        setSimpleValue("ast",applyStartTime);
    }

    public long getApplyEndTime(){
        return getSimpleLongValue("aet");
    }
    public void setApplyEndTime(long applyEndTime){
        setSimpleValue("aet",applyEndTime);
    }

    public void setWeek(int week){
        setSimpleValue("wek",week);
    }

    public int getWeek(){
        return getSimpleIntegerValue("wek");
    }
    public void setLessonType(int lessonType){
        setSimpleValue("lty",lessonType);
    }

    public int getLessonType(){
        return getSimpleIntegerValue("lty");
    }



    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
