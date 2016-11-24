package com.fulaan.pojo;


import com.pojo.fcommunity.AppBannerEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/9/6.
 * App端的Banner
 */
public class AppBanner {

    private String id;
    private String imgUrl;
    private String goodName;
    private String goodId;
    private int status;

    public AppBanner(AppBannerEntry entity) {
        id = entity.getID().toString();
        imgUrl = entity.getImagUrl();
        goodId = entity.getGoodId().toString();
        goodName = entity.getGoodName();
        status = entity.getStatus();
    }

    public static List<AppBanner> getList(List<AppBannerEntry> entities) {
        List<AppBanner> list = new ArrayList<AppBanner>();
        for (AppBannerEntry entity : entities) {
            AppBanner banner = new AppBanner(entity);
            list.add(banner);
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
