package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.app.Platform;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 帖子
 *{
 *     psid : personId 发帖人id
 *     psnm : personName 发帖人昵称
 *     pt : postTitle 帖子标题
 *     pstid : postSectionId 帖子所属版块id
 *     pstnm : postSectionName 帖子所属版块名称
 *     ti : time 发帖时间
 *     pstlvlid : postSectionLevelId 帖子所属子版块Id
 *     pstlvlnm : postSectionLevelName 帖子所属子版块名称
 *     cr : cream 精华标签
 *     tp : top 置顶标签
 *     sc : scanCount 浏览数量
 *     cc : commentCount 评论数量
 *     psimg : postSectionImage 发帖人头像
 *     psc  : postSectionCount 回帖数
 *     cm : comment 评论内容
 *     dg : draught 是否草稿
 *     clf : classify 分类 1:官方公告 2:精彩活动 3:问题求助 4:功能建议
 *     upt ：updateTime 更新时间
 *     pf : platform 帖子来源
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
              null,
              20,
              comment,
              -1,
                1,
               time,
                Platform.PC.toString()
        );
    }
    public FPostEntry(ObjectId personId,String personName,String postTitle,ObjectId postSectionId,String postSectionName,
                      long time,ObjectId postSectionLevelId,String postSectionLevelName,int cream,int top,
                      int scanCount,int commentCount,String postSectionImage,int  postSectionCount,String comment,
                      int draught,int classify,long updateTime,String platform){
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
                .append("psimg", postSectionImage)
                .append("psc", postSectionCount)
                .append("cm", comment)
                .append("dg", draught)
                .append("clf", classify)
                .append("upt", updateTime)
                .append("pf",platform);
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

    public int getTop(){
        return getSimpleIntegerValue("tp");
    }

    public void setTop(int top) {
        setSimpleValue("tp", top);
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
    public String getPostSectionImage(){
        return getSimpleStringValue("psimg");
    }

    public void setPostSectionImage(String postSectionImage){
        setSimpleValue("psimg",postSectionImage);
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

    public String getPlatform(){
        return getSimpleStringValue("pf");
    }

    public void setPlatform(String platform){
        setSimpleValue("pf",platform);
    }

}
