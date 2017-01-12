package com.pojo.forum;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.Platform;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 帖子
 *{
 *     psid : personId 发帖人id
 *     psnm : personName 发帖人昵称
 *     pt : postTitle
 *     pstid : postSectionId 帖子所属版块id
 *     pstnm : postSectionName 帖子所属版块名称
 *     ti : time 发帖时间（悬赏时间）
 *     pstlvlid : postSectionLevelId 帖子所属子版块Id
 *     pstlvlnm : postSectionLevelName 帖子所属子版块名称
 *     cr : cream 精华标签
 *     tp : top 置顶标签
 *     sc : scanCount 浏览数量
 *     cc : commentCount 评论数量
 *     psc  : postSectionCount 回帖数
 *     cm : comment 评论内容
 *     dg : draught 是否草稿
 *     clf : classify 分类标志 1:官方公告 2:精彩活动 3:问题求助 4:功能建议
 *     upt ：updateTime 更新时间(最后回复时间)
 *     upti: updateDateTime 活动排序时间列表
 *     pf : platform 帖子来源
 *     clfc : classifyContent 分类名 1:官方公告 2:精彩活动 3:问题求助 4:功能建议
 *     plt : plainText 发帖纯文本内容
 *     src : imageSrc 获取image第一个元素的src
 *     im : image 发帖人头像
 *     url ： userReplyList 记录回帖人信息Id(点赞人Id)
 *     prc :praiseCount 点赞数
 *     opc ：opposeCount 反对数
 *     opl : opposeList 记录反对人列表(反对人Id)
 *     //活动
 *     acim:activityImage 活动图片
 *     acmm:activityMemo 活动描述
 *     acti:activityTime 活动日期
 *     acs:activityStartTime 活动开始时间
 *     ace:activityEndTIME 活动结束日期
 *     ist:inset设为首页
 *
 *     //app端上传图片以及视频
 *     iml: appImageList 图片
 *     vil: appVideoList 视频
 *     //app端组装内容
 *     acm:appComment app上传组装内容
 *     rei：replyUserId 最后回复人
 *
 *     //投票贴
 *     投票贴内容
 *     voc: voteContent 投票标题
 *     vos: voteSelect 投票选项
 *     ty: type 类型投票贴为1悬赏帖2奖赏贴3
 *     otc:optionCount 最多可选选项
 *
 *     //举报管理
 *     rpt: reported 举报标志1:未处理的举报 2：已处理举报 3:删除举报
 *     rpl: reportedList 举报列表
 *     [
 *         {
 *             ti:time 举报时间
 *             uid:userId 举报人
 *             res:reason 举报理由
 *         }
 *     ]
 *     rpe: reportedExperience 举报经验值
 *     rpc: reportedComment 举报留言
 *     //编辑
 *     bua:backUpComment 未编辑的内容
 *
 *     //帖子删除状态
 *     r: removeStatus 0:未删除状态 1：删除状态
 *
 *     //兼容性
 *     ve：version 表示富文本框上传到七牛上，看是否为空。
 *
 *     //七牛上的路径
 *     vel :versionVideo vurl@vimage
 *
 *     //回帖奖励
 *     rws:rewardScore 每次奖励积分
 *     rwc:rewardCount 奖励次数
 *     rwm:rewardMax 每人最多获取次数
 *
 *     //悬赏帖
 *     ofs:offeredScore 悬赏积分
 *     ofc:offeredCompleted 悬赏状态 0:默认状态 1：已解决
 *
 *     //悬赏最佳答案
 *     sol:solution 最佳答案
 *
 *     //回帖超过100时，标志位代表着只加一次
 *     pfl:postFlag 标志位 0:默认 1:加了
 *
 *}
 *
 */
public class FPostEntry extends BaseDBObject{

    public FPostEntry(){}

    public FPostEntry(ObjectId personId,String personName,String postTitle,ObjectId postSectionId,String postSectionName,
                      long time,String comment){
        this(
              personId,
              personName,
              postTitle,
              postSectionId,   // 帖子所属版块id
              postSectionName, //帖子所属版块名称
              time,
              null,
              null,
              -1,
              -1,
              10,
              20,
              20,
              comment,
              -1,
                1,
               time,
                Platform.PC.toString(),
                "",
                ""
        );
    }
    public FPostEntry(ObjectId personId,String personName,String postTitle,ObjectId postSectionId,String postSectionName,
                      long time,ObjectId postSectionLevelId,String postSectionLevelName,int cream,int top,
                      int scanCount,int commentCount,int  postSectionCount,String comment,
                      int draught,int classify,long updateTime,String platform,String plainText,String imageSrc){
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("psid", personId)
                .append("psnm", personName)
                .append("pt", postTitle)
                .append("pstid", postSectionId)
                .append("pstnm", postSectionName)
                .append("ti", time)
                .append("pstlvlid", postSectionLevelId)
                .append("pstlvlnm", postSectionLevelName)
                .append("cr", cream)
                .append("tp",top)
                .append("sc", scanCount)
                .append("cc", commentCount)
                .append("psc", postSectionCount)
                .append("cm", comment)
                .append("dg", draught)
                .append("clf", classify)
                .append("upt", updateTime)
                .append("pf",platform)
                .append("plt",plainText)
                .append("r",0)
                .append("src",imageSrc);
        setBaseEntry(baseEntry);

    }

    public FPostEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public String getPostTitle(){
        return getSimpleStringValue("pt");
    }

    public void setPostTitle(String postTitle){
        setSimpleValue("pt",postTitle);
    }

    public ObjectId getPostSectionId(){
        return getSimpleObjecIDValue("pstid");
    }

    public void setPostSectionId(ObjectId id){
        setSimpleValue("pstid",id);
    }

    public String getPostSectionName(){
        return getSimpleStringValue("pstnm");
    }

    public void setPostSectionName(String postSectionName){
        setSimpleValue("pstnm",postSectionName);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPersonId(ObjectId id){
        setSimpleValue("psid",id);
    }

    public String getPersonName(){
        return getSimpleStringValue("psnm");
    }

    public void setPersonName(String personName){
        setSimpleValue("psnm",personName);
    }

    public Long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(Long time){
        setSimpleValue("ti", time);
    }

    public ObjectId getPostSectionLevelId(){
        return getSimpleObjecIDValue("pstlvlid");
    }

    public void setPostSectionLevelId(ObjectId id){
        setSimpleValue("pstlvlid",id);
    }

    public String getPostSectionLevelName(){
        return getSimpleStringValue("pstlvlnm");
    }

    public void setPostSectionLevelName(String postSectionLevelName){
        setSimpleValue("pstlvlnm",postSectionLevelName);
    }

    public int getCream(){
        return getSimpleIntegerValue("cr");
    }

    public void setCream(int cream) {
        setSimpleValue("cr", cream);
    }

    public int getIsTop(){
        return getSimpleIntegerValue("tp");
    }

    public void setIsTop(int isTop) {
        setSimpleValue("tp", isTop);
    }

    public int getScanCount(){
        return getSimpleIntegerValue("sc");
    }

    public void setScanCount(int scanCount) {
        setSimpleValue("sc", scanCount);
    }

    public int getCommentCount(){
        return getSimpleIntegerValue("cc");
    }

    public void setCommentCount(int commentCount) {
        setSimpleValue("cc", commentCount);
    }

    public int getPostSectionCount(){
        return getSimpleIntegerValue("psc");
    }

    public void setPostSectionCount(int postSectionCount) {
        setSimpleValue("psc", postSectionCount);
    }

    public String getComment(){
        return getSimpleStringValue("cm");
    }

    public void setComment(String comment){
        setSimpleValue("cm",comment);
    }

    public int getDraught(){
        return getSimpleIntegerValue("dg");
    }

    public void setDraught(int draught) {
        setSimpleValue("dg", draught);
    }

    public int getClassify(){
        return getSimpleIntegerValue("clf");
    }

    public void setClassify(int classify) {
        setSimpleValue("clf", classify);
    }

    public Long getUpdateTime(){
        return getSimpleLongValue("upt");
    }

    public void setUpdateTime(Long updateTime){
        setSimpleValue("upt", updateTime);
    }

    public Long getUpdateDateTime(){
        if(getBaseEntry().containsField("up")) {
                return getSimpleLongValue("up");
        }else{
            return -1L;
        }
    }

    public int getCate(){
        if(getBaseEntry().containsField("cate")) {
            return getSimpleIntegerValue("cate");
        }else{
            return -1;
        }
    }

    public void setUpdateDateTime(Long updateDateTime){
        setSimpleValue("up", updateDateTime);
    }

    public String getPlatform(){
        return getSimpleStringValue("pf");
    }

    public void setPlatform(String platform){
        setSimpleValue("pf",platform);
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
    public String getImageSrc(){
        if(getBaseEntry().containsField("src")) {
            return getSimpleStringValue("src");
        }else{
            return "";
        }
    }

    public void setImageSrc(String imageSrc){
        setSimpleValue("src",imageSrc);
    }

    public String getImage(){
        if(getBaseEntry().containsField("im")) {
            return getSimpleStringValue("im");
        }else{
            return "";
        }
    }

    public void setImage(String image){
        setSimpleValue("im",image);
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

    public int getOpposeCount(){
        if(getBaseEntry().containsField("opc")) {
            return getSimpleIntegerValue("opc");
        }else{
            return 0;
        }
    }

    public void setOpposeCount(int opposeCount) {
        setSimpleValue("opc", opposeCount);
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

    public List<ObjectId> getOpposeList(){
        List<ObjectId> retList =new ArrayList<ObjectId>();
        if(getBaseEntry().containsField("opl")) {
            BasicDBList list = (BasicDBList) getSimpleObjectValue("opl");
            if (null != list && !list.isEmpty()) {
                for (Object o : list) {
                    retList.add((ObjectId) o);
                }
            }
        }
        return retList;
    }


    public void setOpposeList(List<ObjectId> opposeList){
        setSimpleValue("opl", MongoUtils.convert(opposeList));
    }

    public int getLogReply(){
        if(getBaseEntry().containsField("log")) {
            return getSimpleIntegerValue("log");
        }else{
            return 0;
        }
    }

    public void setLogReply(int praiseCount) {
        setSimpleValue("log", praiseCount);
    }



    public String getActivityImage(){
        if(getBaseEntry().containsField("acim")) {
            return getSimpleStringValue("acim");
        }else{
            return "";
        }
    }

    public void setActivityImage(String activityImage){
        setSimpleValue("acim",activityImage);
    }

    public String getActivityMemo(){
        if(getBaseEntry().containsField("acmm")) {
            return getSimpleStringValue("acmm");
        }else{
            return "";
        }
    }

    public void setActivityMemo(String activityMemo){
        setSimpleValue("acmm",activityMemo);
    }

    public String getActivityStartTime(){
        if(getBaseEntry().containsField("acs")) {
            return getSimpleStringValue("acs");
        }else{
            return "";
        }
    }

    public void setActivityStartTime(String activityStartTime){
        setSimpleValue("acs",activityStartTime);
    }

    public String getActivityEndTime(){
        if(getBaseEntry().containsField("ace")) {
            return getSimpleStringValue("ace");
        }else{
            return "";
        }
    }

    public void setActivityEndTime(String activityEndTime){
        setSimpleValue("ace",activityEndTime);
    }

    public Long getActivityTime(){
        if(getBaseEntry().containsField("acti")) {
            return getSimpleLongValue("acti");
        }else{
            return 0L;
        }
    }

    public void setActivityTime(Long activityTime){
        setSimpleValue("acti",activityTime);
    }

    public int getInSet(){
        if(getBaseEntry().containsField("ist")) {
            return getSimpleIntegerValue("ist");
        }else{
            return -1;
        }
    }

    public void setInSet(int inSet) {
        setSimpleValue("ist", inSet);
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

    public String getAppComment(){
        if(getBaseEntry().containsField("acm")) {
            return getSimpleStringValue("acm");
        }else{
            return "";
        }
    }

    public void setAppComment(String appComment){
        setSimpleValue("acm",appComment);
    }

    public String getReplyUserId(){
        if(getBaseEntry().containsField("rei")) {
            return getSimpleStringValue("rei");
        }else{
            return "";
        }
    }

    public void setReplyUserId(String replyUserId){
        setSimpleValue("rei",replyUserId);
    }

    public String getBackUpComment(){
        if(getBaseEntry().containsField("bua")) {
            return getSimpleStringValue("bua");
        }else{
            return "";
        }
    }

    public void setBackUpComment(String backUpComment){
        setSimpleValue("bua",backUpComment);
    }

    public String getVoteContent(){
        if(getBaseEntry().containsField("voc")) {
            return getSimpleStringValue("voc");
        }else{
            return "";
        }
    }

    public void setVoteContent(String voteContent){
        setSimpleValue("voc",voteContent);
    }

    public String getVoteSelect(){
        if(getBaseEntry().containsField("vos")) {
            return getSimpleStringValue("vos");
        }else{
            return "";
        }
    }

    public void setVoteSelect(String voteSelect){
        setSimpleValue("vos",voteSelect);
    }

    public int getType(){
        if(getBaseEntry().containsField("ty")) {
            return getSimpleIntegerValue("ty");
        }else{
            return 0;
        }
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getReported(){
        if(getBaseEntry().containsField("rpt")) {
            return getSimpleIntegerValue("rpt");
        }else{
            return -1;
        }
    }

    public void setReported(int reported){
        setSimpleValue("rpt",reported);
    }

    public int getRemoveStatus(){
        if(getBaseEntry().containsField("rms")) {
            return getSimpleIntegerValue("rms");
        }else{
            return 0;
        }
    }

    public void setRemoveStatus(int removeStatus){
        setSimpleValue("rms",removeStatus);
    }


    public int getReportedExperience(){
        if(getBaseEntry().containsField("rpe")) {
            return getSimpleIntegerValue("rpe");
        }else{
            return 0;
        }
    }

    public void setReportedExperience(int reportedExperience){setSimpleValue("rpe",reportedExperience);}

    public String getReportedComment(){
        if(getBaseEntry().containsField("rpc")) {
            return getSimpleStringValue("rpc");
        }else{
            return "";
        }
    }

    public void setReportedComment(String reportedComment){setSimpleValue("rpc",reportedComment);}

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

    public void setVersionVideo(String versionVideo){setSimpleValue("vel",versionVideo);}


    public int getOptionCount(){
        if(getBaseEntry().containsField("otc")) {
            return getSimpleIntegerValue("otc");
        }else{
            return 1;
        }
    }

    public void setOptionCount(int optionCount){
        setSimpleValue("otc",optionCount);
    }


    public List<Reported> getReportedList() {
        List<Reported> retList =new ArrayList<Reported>();
        if(getBaseEntry().containsField("rpl")){
            BasicDBList list =(BasicDBList)getSimpleObjectValue("rpl");
            if(null!=list && !list.isEmpty())
            {
                for(Object o:list)
                {
                    retList.add(new Reported((BasicDBObject)o));
                }
            }
            return retList;
        }else{
            return null;
        }
    }
    public void setReportedList(List<Reported> reportedList) {

        List<DBObject> list = MongoUtils.fetchDBObjectList(reportedList);
        setSimpleValue("rpl", MongoUtils.convert(list));
    }

    public Long getOfferedScore(){
        if(getBaseEntry().containsField("ofs")){
            return getSimpleLongValueDef("ofs",0L);
        }else{
            return 0L;
        }
    }

    public void setOfferedScore(Long offeredScore){
        setSimpleValue("ofs",offeredScore);
    }

    public int getOfferedCompleted(){
        if(getBaseEntry().containsField("ofc")){
            return getSimpleIntegerValue("ofc");
        }else{
            return 0;
        }
    }

    public void setOfferedCompleted(int offeredCompleted){
        setSimpleValue("ofc",offeredCompleted);
    }

    public int getRewardScore(){
        if(getBaseEntry().containsField("rws")){
            return getSimpleIntegerValue("rws");
        }else{
            return 0;
        }
    }

    public void setRewardScore(int rewardScore){
        setSimpleValue("rws",rewardScore);
    }

    public int getRewardCount(){
        if(getBaseEntry().containsField("rwc")){
            return getSimpleIntegerValue("rwc");
        }else{
            return 0;
        }
    }

    public void setRewardCount(int rewardCount){
        setSimpleValue("rwc",rewardCount);
    }

    public int getRewardMax(){
        if(getBaseEntry().containsField("rwm")) {
            return getSimpleIntegerValue("rwm");
        }else{
            return 0;
        }
    }

    public void setRewardMax(int rewardMax){
        setSimpleValue("rwm",rewardMax);
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("r",isRemove);
    }

    public ObjectId getSolution(){
        return getSimpleObjecIDValue("sol");
    }

    public void setSolution(ObjectId solution){
        setSimpleValue("sol",solution);
    }

    public int getPostFlag(){
        if(getBaseEntry().containsField("pfl")){
            return getSimpleIntegerValueDef("pfl",0);
        }else{
            return 0;
        }
    }

    public void setPostFlag(int postFlag){
        setSimpleValue("pfl",postFlag);
    }

    public void setCate(int cate){
        setSimpleValue("cate",cate);
    }


    public void setCreamTime(long creamTime){
        setSimpleValue("crti",creamTime);
    }

    public static class Reported extends BaseDBObject{



        public Reported(BasicDBObject baseEntry){
            setBaseEntry(baseEntry);
        }

        public Reported(ObjectId id,ObjectId userId, long time, String  reason){
            BasicDBObject baseEntry = new BasicDBObject()
                    .append("id", id)
                    .append("ti",time)
                    .append("uid", userId)
                    .append("res", reason);
            setBaseEntry(baseEntry);
        }

        public ObjectId getId(){
            return getSimpleObjecIDValue("id");
        }

        public void setId(ObjectId id){
            setSimpleValue("id", id);
        }

        public ObjectId getUserId(){
            return getSimpleObjecIDValue("uid");
        }

        public void setUserId(ObjectId userId){
            setSimpleValue("uid", userId);
        }

        public long getTime(){return getSimpleLongValue("ti");}

        public void setTime(long time){setSimpleValue("ti",time);}

        public String getReason(){return getSimpleStringValue("res");}

        public void setReason(String reason){setSimpleValue("res",reason);}
    }

}
