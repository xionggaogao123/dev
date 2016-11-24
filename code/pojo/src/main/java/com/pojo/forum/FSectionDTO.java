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
    private String memoName;

    //app端上传图片
    private String imageAppSrc;
    private String imageBigAppSrc;

    //设置优化数据
    private long totalScanCount;
    private long totalCommentCount;
    private long themeCount;
    private long postCount;


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
        this.imageAppSrc=fSectionEntry.getImageAppSrc();
        this.imageBigAppSrc=fSectionEntry.getImageBigAppSrc();
        this.sectionName=fSectionEntry.getSectionName();
        this.memo=fSectionEntry.getMemo();
        this.memoName=fSectionEntry.getMemoName();
        this.totalCommentCount=fSectionEntry.getTotalCommentCount();
        this.totalScanCount=fSectionEntry.getTotalCommentCount();
        this.themeCount=fSectionEntry.getThemeCount();
        this.postCount=fSectionEntry.getPostCount();
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
        fSectionEntry.setImageAppSrc(imageAppSrc);
        fSectionEntry.setImageBigAppSrc(imageBigAppSrc);
        fSectionEntry.setMemoName(memoName);
        fSectionEntry.setTotalCommentCount(totalCommentCount);
        fSectionEntry.setTotalScanCount(totalScanCount);
        fSectionEntry.setThemeCount(themeCount);
        fSectionEntry.setPostCount(postCount);
        if(null!=parentId&&!parentId.equals("")) {
            fSectionEntry.setParentId(new ObjectId(parentId));
        }
        return fSectionEntry;
    }

    public static FSectionEntry exportEntry(String name,String sectionName,String introduction,String memo) {
        FSectionEntry fSectionEntry=new FSectionEntry();
        fSectionEntry.setID(new ObjectId());
        fSectionEntry.setName(name);
        fSectionEntry.setSectionName(sectionName);
        fSectionEntry.setIntroduction(introduction);
        fSectionEntry.setMemo(memo);
        fSectionEntry.setCount(0);
        fSectionEntry.setTotalCount(0);
        fSectionEntry.setLevel(0);
        fSectionEntry.setSort(0);
        fSectionEntry.setMemoName("");
        fSectionEntry.setTotalCommentCount(Long.parseLong("0"));
        fSectionEntry.setTotalScanCount(Long.parseLong("0"));
        fSectionEntry.setThemeCount(Long.parseLong("0"));
        fSectionEntry.setPostCount(Long.parseLong("0"));
        fSectionEntry.setParentId(new ObjectId());
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

    public String getMemoName() {
        return memoName;
    }

    public void setMemoName(String memoName) {
        this.memoName = memoName;
    }

    public String getImageAppSrc() {
        return imageAppSrc;
    }

    public void setImageAppSrc(String imageAppSrc) {
        this.imageAppSrc = imageAppSrc;
    }

    public String getImageBigAppSrc() {
        return imageBigAppSrc;
    }

    public void setImageBigAppSrc(String imageBigAppSrc) {
        this.imageBigAppSrc = imageBigAppSrc;
    }

    public long getTotalScanCount() {
        return totalScanCount;
    }

    public void setTotalScanCount(long totalScanCount) {
        this.totalScanCount = totalScanCount;
    }

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }

    public long getThemeCount() {
        return themeCount;
    }

    public void setThemeCount(long themeCount) {
        this.themeCount = themeCount;
    }

    public long getTotalCommentCount() {
        return totalCommentCount;
    }

    public void setTotalCommentCount(long totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }
}
