package com.pojo.news;

import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yan on 2015/3/16.
 */
public class News {

    private String id;
    private String title;
    private String column;
    private int pinned;
    private String thumb;
    private String digest;
    private String content;
    private String userId;
    private int readCount;
    private String schoolId;
    private String strDate;
    private String educationId;

    private static  final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");



    public News(){}

    public News(NewsEntry newsEntry){
        this.id=newsEntry.getID().toString();
        this.title=newsEntry.getTitle();
        this.column=newsEntry.getColumn().toString();
        this.pinned=newsEntry.getPinned();
        this.thumb=newsEntry.getThumb();
        this.digest=newsEntry.getDigest();
        this.content=newsEntry.getContent();
        this.userId=newsEntry.getUserId().toString();
        this.readCount=newsEntry.getReadCount();
        this.schoolId=newsEntry.getSchoolId()==null?"":newsEntry.getSchoolId().toString();
        this.educationId=newsEntry.getEducationId()==null?"":newsEntry.getEducationId().toString();
        long time=newsEntry.getID().getTime();
        Date date=new Date(time);
        this.strDate=simpleDateFormat.format(date);

    }


    public NewsEntry exportEntry(){
        NewsEntry newsEntry=new NewsEntry();
        newsEntry.setID(new ObjectId(this.id));
        newsEntry.setTitle(title);
        newsEntry.setColumn(new ObjectId(this.column));
        newsEntry.setPinned(this.pinned);
        newsEntry.setThumb(this.thumb);
        newsEntry.setDigest(this.digest);
        newsEntry.setContent(content);
        newsEntry.setReadCount(this.readCount);
        newsEntry.setUserId(new ObjectId(userId));
        if(schoolId!=null&&!"".equals(schoolId)){
            newsEntry.setSchoolId(new ObjectId(schoolId));
        }else{
            newsEntry.setSchoolId(null);
        }
        if(educationId!=null && !"".equals(educationId)){
            newsEntry.setEducationId(new ObjectId(educationId));
        }else{
            newsEntry.setEducationId(null);
        }
        return newsEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getPinned() {
        return pinned;
    }

    public void setPinned(int pinned) {
        this.pinned = pinned;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getEducationId() {
        return educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }
}
