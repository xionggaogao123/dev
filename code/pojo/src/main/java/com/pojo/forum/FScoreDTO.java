package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/8/16.
 */
public class FScoreDTO {
    private String id;
    private long time;
    private String scoreOrigin;
    private String personId;
    private int score;
    private String operation;
    private int type;
    private String timeText;

    public FScoreDTO(){}

    public FScoreDTO(FScoreEntry fScoreEntry){
        this.id=fScoreEntry.getID().toString();
        this.time=fScoreEntry.getTime();
        this.scoreOrigin=fScoreEntry.getScoreOrigin();
        this.personId=fScoreEntry.getPersonId().toString();
        this.score=fScoreEntry.getScore();
        this.operation=fScoreEntry.getOperation();
        this.type=fScoreEntry.getType();
    }

    public FScoreEntry exportEntry() {
        FScoreEntry fScoreEntry=new FScoreEntry();
        if(null!=id&&!id.equals("")) {
            fScoreEntry.setID(new ObjectId(id));
        }
        if(null!=personId&&!personId.equals("")) {
            fScoreEntry.setPersonId(new ObjectId(personId));
        }
        fScoreEntry.setTime(time);
        fScoreEntry.setScoreOrigin(scoreOrigin);
        fScoreEntry.setScore(score);
        fScoreEntry.setOperation(operation);
        fScoreEntry.setType(type);
        return  fScoreEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getScoreOrigin() {
        return scoreOrigin;
    }

    public void setScoreOrigin(String scoreOrigin) {
        this.scoreOrigin = scoreOrigin;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }
}
