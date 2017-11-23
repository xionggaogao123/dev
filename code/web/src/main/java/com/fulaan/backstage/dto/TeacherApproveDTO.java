package com.fulaan.backstage.dto;

import com.pojo.backstage.TeacherApproveEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/23.
 */
public class TeacherApproveDTO {
    private String id;
    private String userId;
    private String name;
    private String approveId;
    private String loadTime;
    private String applyTime;
    private int type;
    private String url;
    private List<String> communityList = new ArrayList<String>();

    public TeacherApproveDTO(){

    }
    public TeacherApproveDTO(TeacherApproveEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.name = e.getName();
            this.approveId = e.getApproveId() == null ? "" : e.getApproveId().toString();
            if(e.getApplyTime()!=0l){
                this.applyTime = DateTimeUtils.getLongToStrTimeTwo(e.getApplyTime());
            }else{
                this.applyTime = "";
            }
           if(e.getLoadTime()!=0l){
                this.loadTime = DateTimeUtils.getLongToStrTimeTwo(e.getLoadTime());
            }else{
                this.loadTime = "";
            }
            this.type = e.getType();


        }else{
            new TeacherApproveDTO();
        }
    }

    public TeacherApproveEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId aId=null;
        if(this.getApproveId()!=null&&!"".equals(this.getApproveId())){
            aId=new ObjectId(this.getApproveId());
        }
        long lTm = 0l;
        if(this.getLoadTime() != null && this.getLoadTime() != ""){
            lTm = DateTimeUtils.getStrToLongTime(this.getLoadTime());
        }
        TeacherApproveEntry openEntry =
                new TeacherApproveEntry(
                        uId,
                        this.name,
                        aId,
                        lTm,
                        this.type
                );
        return openEntry;

    }
    public TeacherApproveEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId aId=null;
        if(this.getApproveId()!=null&&!"".equals(this.getApproveId())){
            aId=new ObjectId(this.getApproveId());
        }
        long lTm = 0l;
        if(this.getLoadTime() != null && this.getLoadTime() != ""){
            lTm = DateTimeUtils.getStrToLongTime(this.getLoadTime());
        }
        TeacherApproveEntry openEntry =
                new TeacherApproveEntry(
                        Id,
                        uId,
                        this.name,
                        aId,
                        lTm,
                        this.type
                );
        return openEntry;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(List<String> communityList) {
        this.communityList = communityList;
    }
}
