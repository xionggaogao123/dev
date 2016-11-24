package com.fulaan.businessactivity.dto;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.businessactivity.FieryActivityEntry;
import com.sys.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/7/30.
 */
public class FieryActivityDTO {

    private String id;
    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private int checkRole;
    private int takeEffect;
    private String picId;
    private String picFile;
    private String picName;
    private String phonePicId;
    private String phonePicFile;
    private String phonePicName;
    private List<IdNameValuePairDTO> docFile=new ArrayList<IdNameValuePairDTO>();
    private String createTime;
    private String createBy;
    private int activityType;
    private int isFinish; //1:活动已结束,0:活动进行中

    public FieryActivityDTO(){

    }

    public FieryActivityDTO(FieryActivityEntry e)
    {
        this.id=e.getID().toString();
        this.title=e.getName();
        this.content=e.getContent();
        if(e.getBeginTime()>0){
            this.startDate=DateTimeUtils.convert(e.getBeginTime(), DateTimeUtils.DATE_YYYY_MM_DD);
        }else{
            this.startDate="";
        }
        if(e.getEndTime()>0){
            this.endDate=DateTimeUtils.convert(e.getEndTime(), DateTimeUtils.DATE_YYYY_MM_DD);
        }else{
            this.endDate="";
        }

        this.checkRole=e.getCheckRole();

        this.takeEffect=e.getTakeEffect();

        if(e.getPicFile()!=null&&!"".equals(e.getPicFile())){
            this.picId=e.getPicFile().substring(0,e.getPicFile().lastIndexOf("."));
        }
        this.picFile=e.getPicFile();
        this.picName=e.getPicName();

        if(e.getPhonePicFile()!=null&&!"".equals(e.getPhonePicFile())){
            this.phonePicId=e.getPhonePicFile().substring(0, e.getPhonePicFile().lastIndexOf("."));
        }
        this.phonePicFile=e.getPhonePicFile();
        this.phonePicName=e.getPhonePicName();

        for(IdNameValuePair p:e.getDocFile())
        {
            this.docFile.add(new IdNameValuePairDTO(p));
        }
        this.createTime= DateTimeUtils.convert(e.getCreateTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_H);
        this.createBy=e.getCreateBy().toString();
        this.activityType=e.getActivityType();
        this.isFinish=e.getIsFinish();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getCheckRole() {
        return checkRole;
    }

    public void setCheckRole(int checkRole) {
        this.checkRole = checkRole;
    }

    public int getTakeEffect() {
        return takeEffect;
    }

    public void setTakeEffect(int takeEffect) {
        this.takeEffect = takeEffect;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicFile() {
        return picFile;
    }

    public void setPicFile(String picFile) {
        this.picFile = picFile;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPhonePicId() {
        return phonePicId;
    }

    public void setPhonePicId(String phonePicId) {
        this.phonePicId = phonePicId;
    }

    public String getPhonePicFile() {
        return phonePicFile;
    }

    public void setPhonePicFile(String phonePicFile) {
        this.phonePicFile = phonePicFile;
    }

    public String getPhonePicName() {
        return phonePicName;
    }

    public void setPhonePicName(String phonePicName) {
        this.phonePicName = phonePicName;
    }

    public List<IdNameValuePairDTO> getDocFile() {
        return docFile;
    }

    public void setDocFile(List<IdNameValuePairDTO> docFile) {
        this.docFile = docFile;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getAttachCount() {
        return docFile.size();
    }
}
