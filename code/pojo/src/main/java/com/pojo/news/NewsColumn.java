package com.pojo.news;

import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/7/2.
 */
public class NewsColumn {
    private String id;
    private String columnName;
    private String columnDir;
    private String schoolId;
    private String educationId;

    public String getEducationId() {
        return educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public NewsColumn()
    {

    }

    public NewsColumn(NewsColumnEntry newsColumnEntry)
    {
        this.setColumnName(newsColumnEntry.getColumnName());
        this.setColumnDir(newsColumnEntry.getColumnDir());
        this.setSchoolId(newsColumnEntry.getSchoolId()==null?"":newsColumnEntry.getSchoolId().toString());
        this.setEducationId(newsColumnEntry.getEducationId()==null?"":newsColumnEntry.getEducationId().toString());
        this.setId(newsColumnEntry.getID().toString());
    }

    public NewsColumnEntry exportEntry()
    {
        NewsColumnEntry newsColumnEntry=new NewsColumnEntry();
        newsColumnEntry.setColumnName(this.getColumnName());
        newsColumnEntry.setColumnDir(this.getColumnDir());
        if(this.getSchoolId()!=null&&!"".equals(this.getSchoolId())) {
            newsColumnEntry.setSchoolId(new ObjectId(this.getSchoolId()));
        }else{
            newsColumnEntry.setSchoolId(null);
        }
        if(this.getEducationId()!=null&&!"".equals(this.getEducationId())) {
            newsColumnEntry.setEducationId(new ObjectId(this.getEducationId()));
        }else{
            newsColumnEntry.setEducationId(null);
        }
        newsColumnEntry.setID(new ObjectId(this.getId()));
        return newsColumnEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getColumnDir() {
        return columnDir;
    }

    public void setColumnDir(String columnDir) {
        this.columnDir = columnDir;
    }
}
