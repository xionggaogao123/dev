package com.fulaan.excellentCourses.dto;

import com.pojo.excellentCourses.ExcellentCoursesEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-26.
 */
public class ExcellentCoursesDTO {
    private String id;
    private String userId;
    private String subjectId;
    private String subjectName;
    private String userName;
    private String cover;
    private String title;
    private String target;
    private int allClassCount;
    private List<String> communityIdList = new ArrayList<String>();
    private String startTime;
    private String endTime;
    private int oldPrice;
    private int newPrice;
    private int studentNumbet;
    private String createTime;
    private int status;
    private int type;
    private int isBuy; //0  未购买  1 已购买部分     2  全部购买
    private int isCollect;  // 0 未收藏    1 已收藏

    public ExcellentCoursesDTO(){

    }

    public ExcellentCoursesDTO(ExcellentCoursesEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.userName = e.getUserName();
            this.subjectId=e.getSubjectId()==null?"":e.getSubjectId().toString();
            this.subjectName = e.getSubjectName();
            this.cover = e.getCover();
            this.title = e.getTitle();
            this.target = e.getTarget();
            this.allClassCount = e.getAllClassCount();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            if(e.getStartTime()!=0l){
                this.startTime = DateTimeUtils.getLongToStrTimeTwo(e.getStartTime()).substring(0,11);
            }else{
                this.startTime = "";
            }

            if(e.getEndTime()!=0l){
                this.endTime = DateTimeUtils.getLongToStrTimeTwo(e.getEndTime()).substring(0,11);
            }else{
                this.endTime = "";
            }
            this.oldPrice = e.getOldPrice();
            this.newPrice = e.getNewPrice();
            this.studentNumbet = e.getStudentNumber();
            this.status = e.getStatus();
            this.type = e.getType();
            List<ObjectId> cmIdList = e.getCommunityIdList();
            for (ObjectId uId : cmIdList) {
                communityIdList.add(uId.toString());
            }

        }else{
            new ExcellentCoursesDTO();
        }
    }

    public ExcellentCoursesEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId bId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            bId=new ObjectId(this.getSubjectId());
        }
        long eTm = 0l;
        if(this.getEndTime() != null && this.getEndTime() != ""){
            eTm = DateTimeUtils.getStrToLongTime(this.getEndTime());
        }
        long sTm = 0l;
        if(this.getStartTime() != null && this.getStartTime() != ""){
            sTm = DateTimeUtils.getStrToLongTime(this.getStartTime());
        }
        List<ObjectId> cmIdList = new ArrayList<ObjectId>();
        for(String sId : this.communityIdList){
            cmIdList.add(new ObjectId(sId));
        }
        ExcellentCoursesEntry openEntry =
                new ExcellentCoursesEntry(
                        uId,
                        bId,
                        this.subjectName,
                        this.userName,
                        this.cover,
                        this.title,
                        this.target,
                        this.allClassCount,
                        cmIdList,
                        sTm,
                        eTm,
                        this.oldPrice,
                        this.newPrice,
                        this.studentNumbet,
                        this.status,
                        this.type
                );
        return openEntry;

    }
    public ExcellentCoursesEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId bId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            bId=new ObjectId(this.getSubjectId());
        }
        long eTm = 0l;
        if(this.getEndTime() != null && this.getEndTime() != ""){
            eTm = DateTimeUtils.getStrToLongTime(this.getEndTime());
        }
        long sTm = 0l;
        if(this.getStartTime() != null && this.getStartTime() != ""){
            sTm = DateTimeUtils.getStrToLongTime(this.getStartTime());
        }
        List<ObjectId> cmIdList = new ArrayList<ObjectId>();
        for(String sId : this.communityIdList){
            cmIdList.add(new ObjectId(sId));
        }
        ExcellentCoursesEntry openEntry =
                new ExcellentCoursesEntry(
                        Id,
                        uId,
                        bId,
                        this.subjectName,
                        this.userName,
                        this.cover,
                        this.title,
                        this.target,
                        this.allClassCount,
                        cmIdList,
                        sTm,
                        eTm,
                        this.oldPrice,
                        this.newPrice,
                        this.studentNumbet,
                        this.status,
                        this.type
                );
        return openEntry;

    }


    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCover() {
        return cover;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getAllClassCount() {
        return allClassCount;
    }

    public void setAllClassCount(int allClassCount) {
        this.allClassCount = allClassCount;
    }

    public List<String> getCommunityIdList() {
        return communityIdList;
    }

    public void setCommunityIdList(List<String> communityIdList) {
        this.communityIdList = communityIdList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public int getStudentNumbet() {
        return studentNumbet;
    }

    public void setStudentNumbet(int studentNumbet) {
        this.studentNumbet = studentNumbet;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(int isBuy) {
        this.isBuy = isBuy;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }
}
