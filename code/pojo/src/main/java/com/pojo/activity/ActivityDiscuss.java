package com.pojo.activity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动评论表
 * @author Hao
 *
 */
public class ActivityDiscuss extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3743565058079632778L;
	/*
    * id  评论id
    * uid 评论人id
    * con 评论内容
    * imgList --> iml 图片key 集合 可以通过七牛api 获取完整url
    * dt  评论时间
    * rid 父级评论id
    *
    * */
    public ActivityDiscuss(){
        BasicDBObject base = new BasicDBObject("id",null).append("uid",null).append("con","").
                append("iml",null).append("dt",null).append("rid",null);
        setBaseEntry(base);
    }
    public ActivityDiscuss(BasicDBObject basicDBObject){
        super(basicDBObject);
    }

    public ObjectId getId(){
        return getSimpleObjecIDValue("id");
    }
    public void setId(ObjectId objectId){
        setSimpleValue("id",objectId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("uid",userId);
    }
    public String getContent() {
        return getSimpleStringValue("con");
    }
    public void setContent(String content) {
        setSimpleValue("con",content);
    }
    public List<String> getImageList() {
        List<String> objectIdList=new ArrayList<String>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("iml");
        if(basicDBList!=null){
            for(Object object:basicDBList){
                objectIdList.add((String) object);
            }
        }
        return objectIdList;
    }
    public void setImageList(List<String> imageList) {
        setSimpleValue("iml", MongoUtils.convert(imageList));
    }
    public long getDate() {
        return getSimpleLongValue("dt");
    }
    public void setDate(long date) {
        setSimpleValue("dt",date);
    }
    public ObjectId getRepId() {
        return getSimpleObjecIDValue("rid");
    }
    public void setRepId(ObjectId repId) {
        setSimpleValue("rid",repId);
    }
}
