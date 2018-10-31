package com.pojo.appvote;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-25.
 *       新投票（模仿k6Kt的投票选举2.0）
 *   title                    标题                       tit
 *   content                  内容                       con
 *   schoolId                 学校id                     sid
 *   subjectId                学科id                     bid
 *   createTime               创建时间                   ctm
 *   userId                   创建者                     uid
 *   role                     创建者身份                 rol
 *   userCount                投票人数                   uco
 *   isRemove                 是否删除                   isr
 *   List<VideoEntry> videoList,                        vl//视频
 *   List<AttachmentEntry> attachmentEntries,           ats//文件
 *   List<AttachmentEntry> imageList                    il//图片
 *   List<AttachmentEntry> voiceList                    vt//语音
 *
 *   type                     类型                      typ             1   投票       2    报名(不指定选项投票)
 *   applyTypeList            申请对象                  aty             l   学生       2    家长      3 老师      （针对报名）
 *   applyCount               申请数量                  aco            （针对报名）
 *   applyStartTime           申请开始时间              ast            （针对报名）
 *   applyEndTime             申请结束时间              aet            （针对报名）
 *
 *   communityList<ObjectId>  投票社群集合              clt
 *   voteTypeList             投票对象                  vtl
 *   voteCount                投票数量                  vct
 *   sign                     是否记名                  sig              0   不记名         1 记名
 *   open                     是否公开                  ope              0   不公开         1 公开
 *   voteStartTime            投票开始时间              vst
 *   voteEndTime              投票结束时间              vet
 *
 *
 */
public class AppNewVoteEntry extends BaseDBObject {
    public AppNewVoteEntry(){

    }


    public AppNewVoteEntry(BasicDBObject dbObject){
       setBaseEntry(dbObject);
    }

    public AppNewVoteEntry(
            String title,
            String content,
            ObjectId schoolId,
            ObjectId subjectId,
            ObjectId userId,
            int role,
            int userCount,
            List<AttachmentEntry> imageList,
            List<AttachmentEntry> voiceList,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries,
            int type,
            List<Integer>  applyTypeList,
            int applyCount,
            long applyStartTime,
            long applyEndTime,
            List<ObjectId> communityList,
            List<Integer> voteTypeList,
            int voteCount,
            int sign,
            int open,
            long voteStartTime,
            long voteEndTime
    ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("tit", title)
                .append("con", content)
                .append("sid",schoolId)
                .append("bid", subjectId)
                .append("ctm", System.currentTimeMillis())
                .append("uid", userId)
                .append("rol", role)
                .append("uco",userCount)
                .append("ats", MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("vl", MongoUtils.fetchDBObjectList(videoList))
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("typ", type)
                .append("aty",applyTypeList)
                .append("aco",applyCount)
                .append("ast", applyStartTime)
                .append("aet",applyEndTime)
                .append("clt", communityList)
                .append("vtl",voteTypeList)
                .append("vct",voteCount)
                .append("vul",new ArrayList<ObjectId>())
                .append("aul",new ArrayList<ObjectId>())
                .append("sig",sign)
                .append("ope", open)
                .append("vst", voteStartTime)
                .append("vet", voteEndTime)
                .append("isr", 0);
        setBaseEntry(basicDBObject);

    }
    public String getTitle(){
        return getSimpleStringValue("tit");
    }

    public void setTitle(String title){
        setSimpleValue("tit",title);
    }

    public String getContent(){
        return getSimpleStringValue("con");
    }

    public void setContent(String content){
        setSimpleValue("con",content);
    }


    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }
    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("bid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("bid",subjectId);
    }


    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getRole(){
        return  getSimpleIntegerValue("rol");
    }
    public void setRole(int role){
        setSimpleValue("rol",role);
    }

    public int getUserCount(){
        return getSimpleIntegerValue("uco");
    }

    public void setUserCount(int userCount){
        setSimpleValue("uco",userCount);
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

    public int getType(){
        return getSimpleIntegerValueDef("typ", 0);
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public List<Integer> getVoteTypeList(){
        @SuppressWarnings("rawtypes")
        List voteTypeList =(List)getSimpleObjectValue("vtl");
        return voteTypeList;
    }

    public void setVoteTypeList(List<Integer> voteTypeList){
        setSimpleValue("vtl",voteTypeList);
    }

    public int getVoteCount(){
        return getSimpleIntegerValue("vct");
    }

    public void setVoteCount(int voteCount){
        setSimpleValue("vct",voteCount);
    }

    public int getSign(){
        return getSimpleIntegerValue("sig");
    }

    public void setSign(int sign){
        setSimpleValue("sig",sign);
    }

    public int getOpen(){
        return getSimpleIntegerValue("ope");
    }

    public void setOpen(int open){
        setSimpleValue("ope",open);
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

    public void setCommunityList(List<ObjectId> communityList){
        setSimpleValue("clt", MongoUtils.convert(communityList));
    }

    public List<ObjectId> getCommunityList(){
        ArrayList<ObjectId> communityIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                communityIdList.add((ObjectId)obj);
            }
        }
        return communityIdList;
    }


    public void setVoteUesrList(List<ObjectId> voteUesrList){
        setSimpleValue("vul", MongoUtils.convert(voteUesrList));
    }

    public List<ObjectId> getVoteUesrList(){
        ArrayList<ObjectId> voteUesrList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("vul");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                voteUesrList.add((ObjectId)obj);
            }
        }
        return voteUesrList;
    }

    public void setApplyUserList(List<ObjectId> applyUserList){
        setSimpleValue("aul", MongoUtils.convert(applyUserList));
    }

    public List<ObjectId> getApplyUserList(){
        ArrayList<ObjectId> applyUserList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("aul");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                applyUserList.add((ObjectId)obj);
            }
        }
        return applyUserList;
    }

    public List<Integer> getApplyTypeList(){
        @SuppressWarnings("rawtypes")
        List appList =(List)getSimpleObjectValue("aty");
        return appList;
    }

    public void setApplyTypeList(List<Integer> applyTypeList){
        setSimpleValue("aty",applyTypeList);
    }

    public int getApplyCount(){
        return getSimpleIntegerValue("aco");
    }

    public void setApplyCount(int applyCount){
        setSimpleValue("aco",applyCount);
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


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }





}
