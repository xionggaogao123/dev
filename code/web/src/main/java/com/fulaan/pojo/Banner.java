package com.fulaan.pojo;

import com.pojo.fcommunity.BannerEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/9/7.
 * PC端的Banner
 */
public class Banner implements Serializable {

    private String id = "";
    private String name = "";
    private String targetId = "";
    private String targetUrl = "";
    private String subTitle = "";
    private String imageUrl = "";
    private String createTime = "";
    private int status = 1;

    public Banner(BannerEntity entity) {
        this.id = entity.getID().toString();
        this.name = entity.getName();
        this.targetId = entity.getTargetId();
        this.targetUrl = entity.getTargetUrl();
        this.subTitle = entity.getSubTitle();
        this.imageUrl = entity.getImageUrl();
        this.createTime = entity.getCreateTime();
        this.status = entity.getStatus();
    }

    public Banner() {
    }

    public static List<Banner> appBannerReverse(List<AppBanner> appBanners) {
        List<Banner> banners = new ArrayList<Banner>();
        for (AppBanner appBanner : appBanners) {
            Banner banner = new Banner();
            banner.setId(appBanner.getId());
            banner.setName(appBanner.getGoodName());
            banner.setTargetId(appBanner.getGoodId());
            banner.setImageUrl(appBanner.getImgUrl());
            banner.setStatus(appBanner.getStatus());
            banners.add(banner);
        }
        return banners;
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
