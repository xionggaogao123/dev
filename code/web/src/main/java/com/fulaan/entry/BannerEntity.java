package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by jerry on 2016/9/7.
 * ss: 1,未上线  2,上线中
 */
public class BannerEntity extends BaseDBObject {


    public static class Builder {
        private String name = "";
        private String targetId = "";
        private String targetUrl = "";
        private String subTitle = "";
        private String imageUrl = "";
        private String createTime = "";
        private int status = 1;

        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setTargetId(String targetId) {
            this.targetId = targetId;
            return this;
        }

        public Builder setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        public Builder setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setCreateTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public BannerEntity build() {
            return new BannerEntity(this);
        }
    }

    private BannerEntity(Builder builder) {
        super();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("il", builder.imageUrl);
        dbo.append("td", builder.targetId);
        dbo.append("ss", builder.status);
        dbo.append("nm", builder.name);
        dbo.append("ct", builder.createTime);
        dbo.append("st", builder.subTitle);
        dbo.append("tg", builder.targetUrl);
        setBaseEntry(dbo);
    }

    public BannerEntity(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public String getTargetId() {
        return getSimpleStringValue("td");
    }

    public int getStatus() {
        return getSimpleIntegerValue("ss");
    }

    public String getCreateTime() {
        return getSimpleStringValue("ct");
    }

    public String getTargetUrl() {
        return getSimpleStringValue("tg");
    }

    public String getSubTitle() {
        return getSimpleStringValue("st");
    }

    public String getImageUrl() {
        return getSimpleStringValue("il");
    }
}
