package com.fulaan.controlphone.dto;

import com.pojo.excellentCourses.CoursesBusinessEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-09-18.
 */
public class CoursesBusinessDTO {

    private String id;

    private String contactId;

    private String classNumber;

    private String sellName;

    private String sellId;

    private String province;

    private String city;

    private String assistantName;

    private String assistantId;

    private int type;


    public CoursesBusinessDTO(){

    }

    public CoursesBusinessDTO(CoursesBusinessEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.contactId = e.getContactId() == null ? "" : e.getContactId().toString();
            this.classNumber = e.getClassNumber();
            this.sellId = e.getSellId() == null ? "" : e.getSellId().toString();
            this.sellName = e.getSellName();
            this.province = e.getProvince();
            this.city =e.getCity();
            this.assistantName= e.getAssistantName();
            this.assistantId= e.getAssistantId() == null ? "" : e.getAssistantId().toString();
        }else{
            new ControlVersionDTO();
        }
    }
    public CoursesBusinessEntry buildAddEntry(){
        ObjectId cId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            cId=new ObjectId(this.getContactId());
        }
        ObjectId sId=null;
        if(this.getSellId()!=null&&!"".equals(this.getSellId())){
            sId=new ObjectId(this.getSellId());
        }
        ObjectId aId=null;
        if(this.getAssistantId()!=null&&!"".equals(this.getAssistantId())){
            aId=new ObjectId(this.getAssistantId());
        }
        CoursesBusinessEntry openEntry =
                new CoursesBusinessEntry(
                        cId,
                        this.classNumber,
                        this.sellName,
                        sId,
                        this.province,
                        this.city,
                        this.assistantName,
                        aId
                );
        return openEntry;

    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSellName() {
        return sellName;
    }

    public void setSellName(String sellName) {
        this.sellName = sellName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(String assistantId) {
        this.assistantId = assistantId;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
