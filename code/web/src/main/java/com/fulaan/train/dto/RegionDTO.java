package com.fulaan.train.dto;

import com.pojo.train.RegionEntry;
import com.sys.constants.Constant;

/**
 * Created by admin on 2016/12/2.
 */
public class RegionDTO {
    private String id;
    private String parentId;
    private String name;
    private int level;
    private int sort;

    public RegionDTO(){}

    public RegionDTO(RegionEntry entry){
        this.id=entry.getID().toString();
        if(null!=entry.getParentId()){
           this.parentId=entry.getParentId().toString();
        }else{
           this.parentId= Constant.EMPTY;
        }
        this.name=entry.getName();
        this.level=entry.getLevel();
        this.sort=entry.getSort();
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
}
