package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/11.
 */
public class FLevelDTO {
    private String id;
    private String level;
    private long startLevel;
    private long endLevel;
    private long stars;

    public FLevelDTO(){}

    public FLevelDTO(FLevelEntry fLevelEntry){
        this.id=fLevelEntry.getID().toString();
        this.level=fLevelEntry.getLevel();
        this.startLevel=fLevelEntry.getStartLevel();
        this.endLevel=fLevelEntry.getEndLevel();
        this.stars=fLevelEntry.getStars();
    }

    public FLevelEntry exportEntry(){
        FLevelEntry fLevelEntry=new FLevelEntry();
        if(id != null && !id.equals("")){
            fLevelEntry.setID(new ObjectId(id));
        }
        fLevelEntry.setLevel(level);
        fLevelEntry.setStartLevel(startLevel);
        fLevelEntry.setEndLevel(endLevel);
        fLevelEntry.setStars(stars);
        return fLevelEntry;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(long startLevel) {
        this.startLevel = startLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getEndLevel() {
        return endLevel;
    }

    public void setEndLevel(long endLevel) {
        this.endLevel = endLevel;
    }

    public long getStars() {
        return stars;
    }

    public void setStars(long stars) {
        this.stars = stars;
    }
}
