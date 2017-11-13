package com.fulaan.smalllesson.dto;

import com.pojo.smalllesson.LessonUserResultEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/27.
 */
public class LessonUserResultDTO {
    private String id;
    private String lessonId;
    private String userId;
    private String userName;
    private int score;
    private int parming;

    public LessonUserResultDTO(){

    }
    public LessonUserResultDTO(LessonUserResultEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.lessonId = e.getLessonId() == null ? "" : e.getLessonId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.userName = e.getUserName();
            this.score = e.getScore();
        }else{
            new LessonUserResultDTO();
        }
    }

    public LessonUserResultEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId lId=null;
        if(this.getLessonId()!=null&&!"".equals(this.getLessonId())){
            lId=new ObjectId(this.getLessonId());
        }

        LessonUserResultEntry openEntry =
                new LessonUserResultEntry(
                        lId,
                        uId,
                        this.userName,
                        this.score
                );
        return openEntry;

    }
    public LessonUserResultEntry updateEntry(){
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

        LessonUserResultEntry openEntry =
                new LessonUserResultEntry(
                        Id,
                        lId,
                        uId,
                        this.userName,
                        this.score
                );
        return openEntry;

    }

    public int getParming() {
        return parming;
    }

    public void setParming(int parming) {
        this.parming = parming;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
