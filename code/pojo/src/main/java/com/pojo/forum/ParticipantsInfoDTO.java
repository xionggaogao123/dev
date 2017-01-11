package com.pojo.forum;

/**
 * Created by admin on 2017/1/11.
 */
public class ParticipantsInfoDTO {

    private String id;
    private String creator;
    private String participator;
    private String name;
    private int age;
    private int sex;
    private String relation;
    private String school;

    public ParticipantsInfoDTO(ParticipantsInfoEntry entry){
        this.id=entry.getID().toString();
        this.creator=entry.getCreator().toString();
        this.participator=entry.getParticipator().toString();
        this.name=entry.getName();
        this.age=entry.getAge();
        this.sex=entry.getSex();
        this.relation=entry.getRelation();
        this.school=entry.getSchool();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getParticipator() {
        return participator;
    }

    public void setParticipator(String participator) {
        this.participator = participator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
