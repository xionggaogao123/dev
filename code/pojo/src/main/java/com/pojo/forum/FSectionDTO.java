package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/5/31.
 */
public class FSectionDTO {

    private String fSectionId;
    private String name;
    private String sectionName;
    private String introduction;
    private int    count;
    private int    totalCount;
    private int    level;
    private int    sort;
    private String parentId;
    private String image;
    private String memo;


    public FSectionDTO(){}

    public FSectionDTO(FSectionEntry fSectionEntry){
        this.fSectionId=fSectionEntry.getID().toString();
        this.name=fSectionEntry.getName();
        this.introduction=fSectionEntry.getIntroduction();
        this.count=fSectionEntry.getCount();
        this.totalCount=fSectionEntry.getTotalCount();
        this.level=fSectionEntry.getLevel();
        if(null!=fSectionEntry.getParentId()){
            this.parentId=fSectionEntry.getParentId().toString();
        }else{
            this.parentId="";
        }
        this.sort=fSectionEntry.getSort();
        this.image=fSectionEntry.getImage();
        this.sectionName=fSectionEntry.getSectionName();
        this.memo=fSectionEntry.getMemo();
    }

    public FSectionEntry exportEntry() {
        FSectionEntry fSectionEntry=new FSectionEntry();
        if(!fSectionId.equals("")){
            fSectionEntry.setID(new ObjectId(fSectionId));
        }
        fSectionEntry.setName(name);
        fSectionEntry.setSectionName(sectionName);
        fSectionEntry.setIntroduction(introduction);
        fSectionEntry.setMemo(memo);
        fSectionEntry.setCount(count);
        fSectionEntry.setTotalCount(totalCount);
        fSectionEntry.setLevel(level);
        fSectionEntry.setSort(sort);
        fSectionEntry.setImage(image);
        if(null!=parentId|!parentId.equals("")) {
            fSectionEntry.setParentId(new ObjectId(parentId));
        }
        return fSectionEntry;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getfSectionId() {
        return fSectionId;
    }

    public void setfSectionId(String fSectionId) {
        this.fSectionId = fSectionId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
