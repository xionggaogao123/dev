package com.pojo.forum;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/18.
 *
 * 日志表
 * {
 *     ti : time 访问时间
 *     psid : personId 访问Id
 *     an:actionName 行为
 *     {
 *        商城 mall
 *         login 登录页 /user/login.do
 *         head 首页   /mall/entrance.do
 *         mallHead 商城首页 /mall/mallSection.do
 *         mall 商城  /mall/index.do
 *         {
 *            mallA 文具/乐器/棋类
 *            mallB STEM创客/益智玩具
 *            mallC 教辅教材/小说文学
 *            mallD 名人传记/成功励志
 *            mallE 智能硬件/健康防护
 *         }
 *         productDetail 商品详情 /mall/detail.do
 *         cart 购物车   /mall/cart/load.do
 *         order 下单  /mall/order/address.do
 *       论坛 forum
 *         forumIndex 论坛首页 /forum/index.do
 *         postIndex 板块页  /forum/postIndex.do
 *         {
 *             postIndexA 晒才艺
 *             postIndexB STEM创客空间
 *             postIndexC 读书/学霸
 *             postIndexD 演讲口才
 *             postIndexE 安全健康
 *         }
 *         newPost 新增帖子页  /forum/newPost.do
 *         postDetail 帖子详情  /forum/postDetail.do
 *         postSearch 搜索页   /forum/postSearch.do
 *         task 任务页  /forum/task.do
 *     }
 *     kid : keyId 关键Id
 *     ph: path 路径
 * }
 */
public class FLogEntry extends BaseDBObject {
    public FLogEntry(){}

    public FLogEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public Long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(Long time){
        setSimpleValue("ti", time);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPersonId(ObjectId id){
        setSimpleValue("psid",id);
    }

    public String getActionName(){ return getSimpleStringValue("an");}

    public void setActionName(String actionName){setSimpleValue("an",actionName);}

    public ObjectId getKeyId(){
        return getSimpleObjecIDValue("kid");
    }

    public void setKeyId(ObjectId keyId){setSimpleValue("kid",keyId);}

    public String getPath(){
        return  getSimpleStringValue("ph");
    }

    public void setPath(String path){
        setSimpleValue("ph",path);
    }
}
