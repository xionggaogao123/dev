package com.fulaan.jiaschool.dto;

import com.pojo.jiaschool.SchoolCommunityEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/2/2.
 */
public class SchoolCommunityDTO {
    private String id;
    private String communityId;
    private String schoolId;
    public SchoolCommunityDTO(){

    }
    public SchoolCommunityDTO(SchoolCommunityEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.communityId = e.getCommunityId()==null?"":e.getCommunityId().toString();
            this.schoolId = e.getSchoolId() ==null ?"":e.getSchoolId().toString();

        }else{
            new HomeSchoolDTO();
        }
    }

    public SchoolCommunityEntry buildAddEntry(){
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ObjectId sId=null;
        if(this.getSchoolId()!=null&&!"".equals(this.getSchoolId())){
            sId=new ObjectId(this.getSchoolId());
        }
        SchoolCommunityEntry openEntry =
                new SchoolCommunityEntry(
                        cId,
                        sId
                );
        return openEntry;

    }
    public SchoolCommunityEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ObjectId sId=null;
        if(this.getSchoolId()!=null&&!"".equals(this.getSchoolId())){
            sId=new ObjectId(this.getSchoolId());
        }
        SchoolCommunityEntry openEntry =
                new SchoolCommunityEntry(
                        Id,
                        cId,
                        sId
                );
        return openEntry;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
