package com.fulaan.train.dto;

import com.pojo.train.RegionEntry;

/**
 * Created by admin on 2016/12/2.
 */
public class RegionDTO {
    private String id;
    private String parentId;
    private String name;
    private int level;
    private int sort;

    public RegionDTO(RegionEntry entry){
        this.id=entry.getID().toString();
        if(null!=entry.getParentId()){
           this.parentId=entry.getParentId().toString();
        }

    }
}
