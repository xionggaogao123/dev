package com.pojo.customized;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/8/23.
 */
public class RecordVideoDTO {

    private String id;
    private String url;
    private String date;
    private String name;
    private String imageUrl;
    private String app;
    private int  remove;

    public RecordVideoDTO(){

    }

    public RecordVideoDTO(RecordVideoEntry entry){
        this.id = entry.getID().toString();
        this.url = entry.getUrl();
        this.date = entry.getDate();
        this.name = entry.getName();
        this.imageUrl = entry.getImageUrl();
        this.app= entry.getApp();
        this.remove = entry.getRemove();
    }

    public RecordVideoEntry exportEntry(){
        RecordVideoEntry entry = new RecordVideoEntry();
        if(id != null && !id.equals("")){
            entry.setID(new ObjectId(id));
        }
        entry.setUrl(url);
        entry.setDate(date);
        entry.setName(name);
        entry.setImageUrl(imageUrl);
        entry.setApp(app);
        entry.setRemove(remove);

        return entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getRemove() {
        return remove;
    }

    public void setRemove(int remove) {
        this.remove = remove;
    }
}
