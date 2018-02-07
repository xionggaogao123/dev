package com.fulaan.jiaschool.dto;

import com.pojo.jiaschool.SchoolAppEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/2/6.
 */
public class SchoolAppDTO {

    private String id;
    private String schoolId;
    private List<String> appIdList = new ArrayList<String>();

    public SchoolAppDTO(){

    }
    public SchoolAppDTO(SchoolAppEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.schoolId = e.getSchoolId() == null ? "" : e.getSchoolId().toString();
            List<ObjectId> uIdList = e.getAppIdList();
            for(ObjectId uId : uIdList){
                appIdList.add(uId.toString());
            }

        }else{
            new SchoolAppDTO();
        }
    }

    public SchoolAppEntry buildAddEntry(){
        ObjectId sId=null;
        if(this.getSchoolId()!=null&&!"".equals(this.getSchoolId())) {
            sId = new ObjectId(this.getSchoolId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String uId : this.appIdList){
            uIdList.add(new ObjectId(uId));
        }
        SchoolAppEntry openEntry =
                new SchoolAppEntry(
                        sId,
                        uIdList
                );
        return openEntry;

    }
    public SchoolAppEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId sId=null;
        if(this.getSchoolId()!=null&&!"".equals(this.getSchoolId())) {
            sId = new ObjectId(this.getSchoolId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String uId : this.appIdList){
            uIdList.add(new ObjectId(uId));
        }
        SchoolAppEntry openEntry =
                new SchoolAppEntry(
                        Id,
                        sId,
                        uIdList
                );
        return openEntry;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public List<String> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<String> appIdList) {
        this.appIdList = appIdList;
    }
}
