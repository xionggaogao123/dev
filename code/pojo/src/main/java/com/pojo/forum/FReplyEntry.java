package com.pojo.forum;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 回帖
 * {
 *     ptid : postId 帖子id
 *     pt : postTitle 帖子标题
 *     rimg : replyImage 回帖人的头像
 *     rc : replyComment 回帖内容
 *     ti : time 回帖时间
 *     pstid : postSectionId 帖子所属版块id
 *     psid: personId 用户id（回帖人id）
 *     psnm : personName 回帖人呢称
 *     pf : platform 帖子来源
 *     plt : plainText 发帖纯文本内容
 *
 *     rfr: repliesToReply 判断是楼中贴还是回复贴 1：楼中楼 0：回复贴
 *     rpid : replyPostId 跟帖Id //当回复给回复贴时跟踪的回复贴
 *
 *     rl : repliesList 楼中楼跟帖列表
 *
 *     //app端上传图片以及视频
 *     iml: appImageList 图片
 *     vil: appVideoList 视频
 *
 *     //点赞
 *     url :userReplyList 记录回帖人信息Id(点赞人Id)
 *     prc :praiseCount 点赞数
 *     //楼层
 *     fl :floor 楼层
 *
 *     rm : remove 1:删除
 *     //更新时间
 *     upt: updateTime 更新时间
 *     //语音上传
 *     vcl: voiceFile 语音列表
 *
 *     //参赛人员信息Id
 *     pct:participateId
 *
 * }
 */
public class FReplyEntry extends BaseDBObject {

    public FReplyEntry(){}

    public FReplyEntry(DBObject baseEntry){
        super((BasicDBObject)baseEntry);
    }
    public ObjectId getPostId(){
        return getSimpleObjecIDValue("ptid");
    }

    public void setPostId(ObjectId id){
        setSimpleValue("ptid",id);
    }

    public String getPostTitle(){
        if(getBaseEntry().containsField("pt")) {
            return getSimpleStringValue("pt");
        }else{
            return "";
        }
    }

    public void setPostTitle(String postTitle){
        setSimpleValue("pt",postTitle);
    }

    public void setReplyImage(String replyImage){
        setSimpleValue("rimg",replyImage);
    }

    public String getReplyImage(){
        return getSimpleStringValue("rimg");
    }

    public String getReplyComment(){
        return getSimpleStringValue("rc");
    }

    public void setReplyComment(String replyComment){
        setSimpleValue("rc",replyComment);
    }

    public ObjectId getPostSectionId(){
        return getSimpleObjecIDValue("pstid");
    }

    public void setPostSectionId(ObjectId id){
        setSimpleValue("pstid",id);
    }

    public Long getTime(){
        if(getBaseEntry().containsField("ti")) {
            return getSimpleLongValue("ti");
        }else{
            return 0L;
        }
    }

    public void setTime(Long time){
        setSimpleValue("ti", time);
    }

    public String getPlatform(){
        return getSimpleStringValue("pf");
    }

    public void setPlatform(String platform){
        setSimpleValue("pf",platform);
    }

    public String getPersonName(){
        return getSimpleStringValue("psnm");
    }

    public void setPersonName(String personName){
        setSimpleValue("psnm",personName);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPersonId(ObjectId id){
        setSimpleValue("psid",id);
    }

    public String getPlainText(){
        if(getBaseEntry().containsField("plt")) {
            return getSimpleStringValue("plt");
        }else{
            return "";
        }
    }

    public void setPlainText(String plainText){
        setSimpleValue("plt",plainText);
    }

    public int  getRepliesToReply(){
        if(getBaseEntry().containsField("rfr")) {
            return getSimpleIntegerValue("rfr");
        }else{
            return -1;
        }
    }

    public void setRepliesToReply(int repliesToReply){
        setSimpleValue("rfr", repliesToReply);
    }

    public ObjectId getReplyPostId(){
        if(getBaseEntry().containsField("rpid")) {
            return getSimpleObjecIDValue("rpid");
        }else{
            return null;
        }
    }

    public void setReplyPostId(ObjectId replyPostId){
        setSimpleValue("rpid",replyPostId);
    }


    public ObjectId getParticipateId(){
        if(getBaseEntry().containsField("pct")){
            return getSimpleObjecIDValue("pct");
        }else{
            return null;
        }
    }

    public void setParticipateId(ObjectId participateId){
        setSimpleValue("pct",participateId);
    }



    public int  getRepliesFlag(){
        if(getBaseEntry().containsField("rlf")) {
            return getSimpleIntegerValue("rlf");
        }else{
            return -1;
        }
    }

    public void setRepliesFlag(int repliesFlag){
        setSimpleValue("rlf", repliesFlag);
    }


    public List<Replies> getRepliesList() {
        List<Replies> retList =new ArrayList<Replies>();
        if(getBaseEntry().containsField("rl")){
            BasicDBList list =(BasicDBList)getSimpleObjectValue("rl");
            if(null!=list && !list.isEmpty())
            {
                for(Object o:list)
                {
                    retList.add(new Replies((BasicDBObject)o));
                }
            }
            return retList;
        }else{
            return null;
        }
    }
    public void setRepliesList(List<Replies> repliesList) {

        List<DBObject> list = MongoUtils.fetchDBObjectList(repliesList);
        setSimpleValue("rl", MongoUtils.convert(list));
    }

    public String getAppImageList(){
        if(getBaseEntry().containsField("iml")) {
            return getSimpleStringValue("iml");
        }else{
            return "";
        }
    }

    public void setAppImageList(String appImageList){
        setSimpleValue("iml",appImageList);
    }

    public String getVoiceFile(){
        if(getBaseEntry().containsField("vcl")) {
            return getSimpleStringValue("vcl");
        }else{
            return "";
        }
    }

    public void setVoiceFile(String voiceFile){
        setSimpleValue("vcl",voiceFile);
    }

    public String getAppVideoList(){
        if(getBaseEntry().containsField("vil")) {
            return getSimpleStringValue("vil");
        }else{
            return "";
        }
    }

    public void setAppVideoList(String appVideoList){
        setSimpleValue("vil",appVideoList);
    }

    public String getAppAudioStr(){
        if(getBaseEntry().containsField("aud")) {
            return getSimpleStringValue("aud");
        }else{
            return "";
        }
    }

    public void setAppAudioStr(String audioStr){
        setSimpleValue("aud",audioStr);
    }


    public List<ObjectId> getUserReplyList(){
        List<ObjectId> retList =new ArrayList<ObjectId>();
        if(getBaseEntry().containsField("url")) {
            BasicDBList list = (BasicDBList) getSimpleObjectValue("url");
            if (null != list && !list.isEmpty()) {
                for (Object o : list) {
                    retList.add((ObjectId) o);
                }
            }
        }
        return retList;
    }


    public void setUserReplyList(List<ObjectId> userReplyList){
        setSimpleValue("url", MongoUtils.convert(userReplyList));
    }

    public int getPraiseCount(){
        if(getBaseEntry().containsField("prc")) {
            return getSimpleIntegerValue("prc");
        }else{
            return 0;
        }
    }

    public void setPraiseCount(int praiseCount) {
        setSimpleValue("prc", praiseCount);
    }

    public int getFloor(){
        if(getBaseEntry().containsField("fl")) {
            return getSimpleIntegerValue("fl");
        }else{
            return 0;
        }
    }

    public void setFloor(int floor) {
        setSimpleValue("fl", floor);
    }

    public int getRemove(){
        if(getBaseEntry().containsField("rm")) {
            return getSimpleIntegerValue("rm");
        }else{
            return 0;
        }
    }

    public void setRemove(int remove) {
        setSimpleValue("rm", remove);
    }

    public Long getUpdateTime(){
        if(getBaseEntry().containsField("upt")) {
            return getSimpleLongValue("upt");
        }else{
            return 0L;
        }
    }

    public void setUpdateTime(Long updateTime){
        setSimpleValue("upt", updateTime);
    }


    public String getVersion(){
        if(getBaseEntry().containsField("ve")) {
            return getSimpleStringValue("ve");
        }else{
            return "";
        }
    }

    public void setVersion(String version){setSimpleValue("ve",version);}

    public String getVersionVideo(){
        if(getBaseEntry().containsField("vel")) {
            return getSimpleStringValue("vel");
        }else{
            return "";
        }
    }


    //=======================================================================word

    public void setUserWord(String word){
        if(word == null)return;
        setSimpleValue("word",word);
    }

    public void setUserPdf(String pdf){
        if(pdf == null)return;
        setSimpleValue("pdf",pdf);
    }

    public void setWordName(String name){
        if(name == null)return;
        setSimpleValue("wdname",name);
    }

    public String getUserPdf(){
        if(getBaseEntry().containsField("pdf")) {
            return getSimpleStringValue("pdf");
        }else{
            return "";
        }
    }

    public String getUserWord(){
        if(getBaseEntry().containsField("word")) {
            return getSimpleStringValue("word");
        }else{
            return "";
        }
    }

    public String getUserWordName(){
        if(getBaseEntry().containsField("wdname")) {
            return getSimpleStringValue("wdname");
        }else{
            return "";
        }
    }



    //============================================================================word

    public void setVersionVideo(String versionVideo){setSimpleValue("vel",versionVideo);}


    public static class Replies extends BaseDBObject
    {
        private static final long serialVersionUID = -397602579611173241L;

        /**
         *
         */

        public Replies(BasicDBObject baseEntry){
            setBaseEntry(baseEntry);
        }

        public Replies(ObjectId id,String nickName, String content,long time,String imageStr,ObjectId personId,ObjectId userId){
            BasicDBObject baseEntry = new BasicDBObject()
                    .append("id",id)
                    .append("nnm", nickName)
                    .append("ct", content)
                    .append("ti",time)
                    .append("ims",imageStr)
                    .append("pid",personId)
                    .append("uid",userId);
            setBaseEntry(baseEntry);
        }

        public ObjectId getId(){
            return getSimpleObjecIDValue("id");
        }

        public void setId(ObjectId id){
            setSimpleValue("id", id);
        }

        public String getNickName(){
            return getSimpleStringValue("nnm");
        }

        public void setNickName(String nickName){
            setSimpleValue("nnm", nickName);
        }

        public String getContent(){
            return getSimpleStringValue("ct");
        }

        public void setContent(String content){
            setSimpleValue("ct", content);
        }

        public Long getTime(){
            return getSimpleLongValue("ti");
        }

        public void setTime(Long time){
            setSimpleValue("ti", time);
        }

        public String getImageStr(){
            return getSimpleStringValue("ims");
        }

        public void setImageStr(String imageStr){
            setSimpleValue("ims", imageStr);
        }

        public ObjectId getPersonId(){
                return getSimpleObjecIDValue("pid");
        }
        public void setPersonId(ObjectId personId){
            setSimpleValue("pid", personId);
        }

        public ObjectId getUserId(){
            return getSimpleObjecIDValue("uid");
        }

        public ObjectId getRpid(){
            return getSimpleObjecIDValue("id");
        }
        public void setUserId(ObjectId userId){
            setSimpleValue("uid", userId);
        }
    }
}
