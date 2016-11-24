package com.pojo.ebusiness;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/3/31.
 *
 * 商品分类简介视频
 *
 * nm: name 名称
 * vi: videoId 视频在video表中的id
 * vu: videoUrl 视频url
 * viu: videoImageUrl 视频图片url
 * img: imageUrl
 * tt: title 标题
 * txt: text 简介文本
 * ct: category 商品分类
 */
public class ECategoryVideoEntry extends BaseDBObject{
    public ECategoryVideoEntry(){}

    public ECategoryVideoEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public ECategoryVideoEntry(String name,String videoId,String videoUrl,String videoImageUrl,String imageUrl,String title,String text,ObjectId category){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("nm",name)
                .append("vi",videoId)
                .append("vu", videoUrl)
                .append("viu",videoImageUrl)
                .append("img",imageUrl)
                .append("tt", title)
                .append("txt",text)
                .append("ct",category);
        setBaseEntry(baseEntry);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm",name);
    }

    public ObjectId getVideoId(){
        return getSimpleObjecIDValue("vi");
    }

    public void setVideoId(ObjectId videoId){
        setSimpleValue("vi",videoId);
    }

    public String getVideoUrl(){
        return getSimpleStringValue("vu");
    }

    public void setVideoUrl(String videoUrl){
        setSimpleValue("vu",videoUrl);
    }

    public String getVideoImageUrl(){
        return  getSimpleStringValue("viu");
    }

    public void setVideoImageUrl(String videoImageUrl){
        setSimpleValue("viu",videoImageUrl);
    }

    public String getImageUrl(){
        return getSimpleStringValue("img");
    }

    public void setImageUrl(String imageUrl){
        setSimpleValue("img",imageUrl);
    }

    public String getTitle(){
        return getSimpleStringValue("tt");
    }

    public void setTitle(String title){
        setSimpleValue("tt",title);
    }

    public String getText(){
        return getSimpleStringValue("txt");
    }

    public void setText(String text){
        setSimpleValue("txt",text);
    }

    public ObjectId getCategory(){
        return getSimpleObjecIDValue("ct");
    }

    public void setCategory(ObjectId category){
        setSimpleValue("ct",category);
    }
}
