package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 版块
 * {
 *     nm : nm 板块名称
 *     itd : introduction 板块简介（内容摘要）
 *     ct : count 每日发帖数
 *     tct : totalCount 总发帖数
 *     lvl : level 等级
 *     st : sort 排序
 *     pid : parentId 父板块
 *     img ：image 板块图片
 *     snm : sectionName 版主
 *     mm : memo 描述
 *     ias: imageAppSrc 手机首页图片上传路径
 *     ibs: imageBigAppSrc 手机首页图片上传大图路径
 *
 *     tsc:totalScanCount 总浏览量
 *     tcc:totalCommentCount 总评论量（回帖量）
 *     tc:themeCount 发帖量
 *     pc:postCount 总发帖量（发帖量+回帖量）
 * }
 */
public class FSectionEntry extends BaseDBObject {

    public FSectionEntry(){
        super();
    }

    public FSectionEntry(String Name,String introduction,String sectionName,String memo){
        this(Name, introduction,0,0,1,1,null,null,sectionName,memo,"");
    }

    public FSectionEntry(String Name,String introduction,int count,int totalCount,int level,int sort,ObjectId parentid,String image,String sectionName,
                         String memo,String memoName){

        super();

        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", Name)
                .append("itd", introduction)
                .append("ct", count)
                .append("tct", totalCount)
                .append("lvl", level)
                .append("st", sort)
                .append("pid", parentid)
                .append("img", image)
                .append("snm", sectionName)
                .append("mm",memo)
                .append("mmn",memoName);
        setBaseEntry(baseEntry);
    }

    public FSectionEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getIntroduction(){
        return getSimpleStringValue("itd");
    }

    public void setIntroduction(String introduction) {
        setSimpleValue("itd", introduction);
    }

    public int getCount(){
        return getSimpleIntegerValue("ct");
    }

    public void setCount(int count) {
        setSimpleValue("ct", count);
    }

    public int getTotalCount(){
        return getSimpleIntegerValue("tct");
    }

    public void setTotalCount(int totalCount) {
        setSimpleValue("tct", totalCount);
    }

    public int getLevel(){
        return getSimpleIntegerValue("lvl");
    }

    public void setLevel(int level){
        setSimpleValue("lvl", level);
    }

    public ObjectId getParentId(){
            return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid", parentId);
    }

    public int getSort(){
        return getSimpleIntegerValue("st");
    }

    public void setSort(int sort){
        setSimpleValue("st", sort);
    }

    public String getImage(){
        if(this.getBaseEntry().containsField("img")){
            return getSimpleStringValue("img");
        }
        return "";
    }

    public void setImage(String image) {
        setSimpleValue("img", image);
    }

    public String getSectionName() {
        return getSimpleStringValue("snm");
    }

    public void setSectionName(String sectionName) {
        setSimpleValue("snm", sectionName);
    }

    public String getMemo(){
        return getSimpleStringValue("mm");
    }

    public void setMemo(String memo) {
        setSimpleValue("mm", memo);
    }

    public String getMemoName(){
        if(getBaseEntry().containsField("mmn")) {
            return getSimpleStringValue("mmn");
        }else{
            return "";
        }
    }

    public void setMemoName(String memoName) {
        setSimpleValue("mmn", memoName);
    }

    public String getImageAppSrc(){
        if(this.getBaseEntry().containsField("ias")){
            return getSimpleStringValue("ias");
        }
        return "";
    }

    public void setImageAppSrc(String imageAppSrc) {
        setSimpleValue("ias", imageAppSrc);
    }

    public String getImageBigAppSrc(){
        if(this.getBaseEntry().containsField("ibs")){
            return getSimpleStringValue("ibs");
        }
        return "";
    }

    public void setImageBigAppSrc(String imageBigAppSrc) {
        setSimpleValue("ibs", imageBigAppSrc);
    }

    public Long getTotalScanCount(){
        if(this.getBaseEntry().containsField("tsc")){
            return getSimpleLongValue("tsc");
        }
        return 0L;
    }

    public void setTotalScanCount(Long totalScanCount) {
        setSimpleValue("tsc", totalScanCount);
    }

    public Long getTotalCommentCount(){
        if(this.getBaseEntry().containsField("tcc")){
            return getSimpleLongValue("tcc");
        }
        return 0L;
    }

    public void setTotalCommentCount(Long totalCommentCount) {
        setSimpleValue("tcc", totalCommentCount);
    }

    public Long getThemeCount(){
        if(this.getBaseEntry().containsField("tc")){
            return getSimpleLongValue("tc");
        }
        return 0L;
    }

    public void setThemeCount(Long themeCount) {
        setSimpleValue("tc", themeCount);
    }

    public Long getPostCount(){
        if(this.getBaseEntry().containsField("pc")){
            return getSimpleLongValue("pc");
        }
        return 0L;
    }

    public void setPostCount(Long postCount) {
        setSimpleValue("pc", postCount);
    }


}
