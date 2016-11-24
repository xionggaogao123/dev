package com.pojo.customized;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/8/23.
 */
public class RecordVideoEntry extends BaseDBObject{

    public RecordVideoEntry(){

    }

    public RecordVideoEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public RecordVideoEntry(String url,String date,String name,String imageUrl,String app,int remove) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("ti", date)
                .append("url", url)
                .append("nm", name)
                .append("im",imageUrl)
                .append("app",app)
                .append("ir",remove);
        setBaseEntry(baseEntry);
    }

    public String getUrl() {
        return getSimpleStringValue("url");
    }

    public void setUrl(String url) {
        setSimpleValue("url", url);
    }

    public String getDate() {
        return getSimpleStringValue("ti");
    }

    public void setDate(String date) {
        setSimpleValue("ti", date);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getImageUrl() {
        return getSimpleStringValue("im");
    }

    public void setImageUrl(String imageUrl) {
        setSimpleValue("im", imageUrl);
    }

    public String getApp() {
        return getSimpleStringValue("app");
    }

    public void setApp(String app) {
        setSimpleValue("app", app);
    }

    public int getRemove() {
        return getSimpleIntegerValue("ir");
    }

    public void setRemove(int remove) {
        setSimpleValue("ir", remove);
    }
}
