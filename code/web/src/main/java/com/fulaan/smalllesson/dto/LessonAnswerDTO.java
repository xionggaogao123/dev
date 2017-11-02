package com.fulaan.smalllesson.dto;

import com.pojo.smalllesson.LessonAnswerEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/27.
 */
public class LessonAnswerDTO {

    private String id;
    private String lessonId;
    private String userId;
    private String userName;
    private int number;
    private long time;
    private int isTrue;
    private String answer;
    private List<LessonAnswerDTO> list = new ArrayList<LessonAnswerDTO>();

    public LessonAnswerDTO(){

    }
    public LessonAnswerDTO(LessonAnswerEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.lessonId = e.getLessonId() == null ? "" : e.getLessonId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.userName = e.getUserName();
            this.number = e.getNumber();
            this.time = e.getTime();
            this.isTrue = e.getIsTrue();
            this.answer = e.getAnswer();
        }else{
            new LessonAnswerDTO();
        }
    }

    public LessonAnswerEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId lId=null;
        if(this.getLessonId()!=null&&!"".equals(this.getLessonId())){
            lId=new ObjectId(this.getLessonId());
        }
        LessonAnswerEntry openEntry =
                new LessonAnswerEntry(
                        lId,
                        uId,
                        this.userName,
                        this.number,
                        this.time,
                        this.isTrue,
                        this.answer
                );
        return openEntry;

    }
    public LessonAnswerEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId lId=null;
        if(this.getLessonId()!=null&&!"".equals(this.getLessonId())){
            lId=new ObjectId(this.getLessonId());
        }
        LessonAnswerEntry openEntry =
                new LessonAnswerEntry(
                        Id,
                        lId,
                        uId,
                        this.userName,
                        this.number,
                        this.time,
                        this.isTrue,
                        this.answer
                );
        return openEntry;

    }


    public List<LessonAnswerDTO> getList() {
        return list;
    }

    public void setList(List<LessonAnswerDTO> list) {
        this.list = list;
    }

    public int getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(int isTrue) {
        this.isTrue = isTrue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
