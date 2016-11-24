package com.pojo.ebusiness;

import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/3/3.
 */
public class EGoodsCategoryDTO {
    private String id;
    private String parentId;
    private String name;
    private int level;
    private int sort;
    private String image;
    private String mobileImage;
    private String mobileCategoryImg;

    public EGoodsCategoryDTO(){}

    public EGoodsCategoryDTO(String id, String parentId, String name, int level, int sort){
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.level = level;
        this.sort = sort;
    }

    public EGoodsCategoryDTO(String id, String parentId, String name, int level, int sort, String image,
                             String mobileImage,String mobileCategoryImg) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.level = level;
        this.sort = sort;
        this.image = image;
        this.mobileImage = mobileImage;
        this.mobileCategoryImg=mobileCategoryImg;
    }

    public EGoodsCategoryDTO(EGoodsCategoryEntry entry){
        this.id = entry.getID().toString();
        this.parentId = entry.getParentId() == null ? "" : entry.getParentId().toString();
        this.name = entry.getName();
        this.level = entry.getLevel();
        this.sort = entry.getSort();
        this.image = entry.getImage() == null ? "" : entry.getImage();
        this.mobileImage = entry.getMobileImage() == null ? "" : entry.getMobileImage();
        this.mobileCategoryImg=entry.getMobileCategoryImage() == null ? "" : entry.getMobileCategoryImage();
    }

    public EGoodsCategoryEntry exportEntry(){
        EGoodsCategoryEntry eGoodsCategoryEntry = new EGoodsCategoryEntry();
        if(!id.equals("")){
            eGoodsCategoryEntry.setID(new ObjectId(id));
        }
        if(!parentId.equals("")){
            eGoodsCategoryEntry.setParentId(new ObjectId(parentId));
        }
        eGoodsCategoryEntry.setName(name);
        eGoodsCategoryEntry.setLevel(level);
        eGoodsCategoryEntry.setSort(sort);
        eGoodsCategoryEntry.setImage(image);
        eGoodsCategoryEntry.setMobileImage(mobileImage);
        eGoodsCategoryEntry.setMobileCategoryImage(mobileCategoryImg);
        return eGoodsCategoryEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public String getMobileCategoryImg() {
        return mobileCategoryImg;
    }

    public void setMobileCategoryImg(String mobileCategoryImg) {
        this.mobileCategoryImg = mobileCategoryImg;
    }
}
