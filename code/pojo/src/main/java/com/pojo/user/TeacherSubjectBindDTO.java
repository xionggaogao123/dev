package com.pojo.user;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/28.
 */
public class TeacherSubjectBindDTO {

    private String id;
    private String userId;
    private String userName;
    private List<String> subjectIds=new ArrayList<String>();
    private List<SubjectDto> subjectId=new ArrayList<SubjectDto>();

    public TeacherSubjectBindDTO(){

    }

    public TeacherSubjectBindEntry buildEntry(){
        List<ObjectId> subjects=new ArrayList<ObjectId>();
        if(subjectIds.size()>0){
            for(String sItem:subjectIds){
                subjects.add(new ObjectId(sItem));
            }
        }
        return new TeacherSubjectBindEntry(new ObjectId(userId),subjects);
    }

    public TeacherSubjectBindDTO(TeacherSubjectBindEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getUserId().toString();
        List<ObjectId> sIds=entry.getSubjectIds();
        for(ObjectId item:sIds){
            subjectIds.add(item.toString());
        }
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<SubjectDto> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<SubjectDto> subjectId) {
        this.subjectId = subjectId;
    }

    static class SubjectDto{
         String subjectId;
         String subjectName;

        public SubjectDto(){

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
    }
}
