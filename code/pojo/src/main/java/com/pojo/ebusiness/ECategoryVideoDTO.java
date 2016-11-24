package com.pojo.ebusiness;

import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/3/31.
 */
public class ECategoryVideoDTO {
    private String id;
    private String name;
    private String videoId;
    private String videoUrl;
    private String videoImageUrl;
    private String imageUrl;
    private String title;
    private String text;
    private String category;

    public ECategoryVideoDTO(){}

    public ECategoryVideoDTO(String id,String name,String videoId,String videoUrl,String videoImageUrl,String imageUrl,String title,String text,String category){
        this.id = id;
        this.name = name;
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.videoImageUrl = videoImageUrl;
        this.imageUrl = imageUrl;
        this.title = title;
        this.text = text;
        this.category = category;
    }

    public ECategoryVideoDTO(ECategoryVideoEntry entry){
        this.id = entry.getID().toString();
        this.name = entry.getName();
        this.videoId = entry.getVideoId() == null ? "" : entry.getVideoId().toString();
        this.videoUrl = entry.getVideoUrl();
        this.videoImageUrl = entry.getVideoImageUrl();
        this.imageUrl = entry.getImageUrl();
        this.title = entry.getTitle();
        this.text = entry.getText();
        this.category = entry.getCategory().toString();
    }

    public ECategoryVideoEntry exportEntry(){
        ECategoryVideoEntry entry = new ECategoryVideoEntry();
        if(!id.equals("")){
            entry.setID(new ObjectId(id));
        }
        entry.setName(name);
        if(!videoId.equals("")){
            entry.setVideoId(new ObjectId(videoId));
        }
        entry.setVideoUrl(videoUrl);
        entry.setVideoImageUrl(videoImageUrl);
        entry.setImageUrl(imageUrl);
        entry.setTitle(title);
        entry.setText(text);
        if(!category.equals("")){
            entry.setCategory(new ObjectId(category));
        }

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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
