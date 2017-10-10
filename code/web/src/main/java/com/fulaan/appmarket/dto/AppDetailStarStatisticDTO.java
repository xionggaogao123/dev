package com.fulaan.appmarket.dto;

import com.pojo.appmarket.AppDetailStarStatisticEntry;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailStarStatisticDTO {

    private String id;
    private String appDetailId;
    private int star;
    private int count;
    private double percent;

    public AppDetailStarStatisticDTO(){

    }

    public AppDetailStarStatisticDTO(int star,
                                     int count){
        this.star=star;
        this.count=count;
        this.percent=0;
    }

    public AppDetailStarStatisticDTO(AppDetailStarStatisticEntry entry){
        this.id=entry.getID().toString();
        this.appDetailId=entry.getAppDetailId().toString();
        this.star=entry.getStar();
        this.count=entry.getCount();
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppDetailId() {
        return appDetailId;
    }

    public void setAppDetailId(String appDetailId) {
        this.appDetailId = appDetailId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
