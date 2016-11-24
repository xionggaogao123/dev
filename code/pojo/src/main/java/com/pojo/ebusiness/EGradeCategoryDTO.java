package com.pojo.ebusiness;

import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/4/8.
 */
public class EGradeCategoryDTO {
    private String id;
    private String name;
    private int sort;

    public EGradeCategoryDTO(){}

    public EGradeCategoryDTO(EGradeCategoryEntry entry){
        this.id = entry.getID().toString();
        this.name = entry.getName();
        this.sort = entry.getSort();
    }

    public EGradeCategoryEntry exportEntry(){
        EGradeCategoryEntry entry = new EGradeCategoryEntry();
        if(id != null && !id.equals("")){
            entry.setID(new ObjectId(id));
        }
        entry.setName(name);
        entry.setSort(sort);
        return entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
