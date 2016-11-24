package com.pojo.ebusiness;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**用户查看商品日志类
 * Created by fl on 2016/1/28.
 * ip:用户ip
 * nm:用户名（未登录为空）
 * uid:用户id（未登录为空）
 * sn:学校名称（未登录为空）
 * rl:角色（未登录为0）
 * gn:商品名称
 * gid:商品id
 * pr：价格（单位分）
 * ty:类型 1:浏览商品 2:收藏商品
 * ili:是否登录 0：未登录， 1：已登陆
 */
public class EGoodsLogEntry extends BaseDBObject {

    public EGoodsLogEntry(){}

    public EGoodsLogEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public String getIp(){
        return getSimpleStringValue("ip");
    }

    public void setIp(String ip){
        setSimpleValue("ip", ip);
    }

    public String getUserName(){
        return getSimpleStringValue("nm");
    }

    public void setUserName(String userName){
        setSimpleValue("nm", userName);
    }

    public ObjectId getUserId(){
        if(getBaseEntry().containsField("uid")){
            return getSimpleObjecIDValue("uid");
        }
        return null;
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getSchoolName(){
        return getSimpleStringValue("sn");
    }

    public void setSchoolName(String schoolName){
        setSimpleValue("sn", schoolName);
    }

    public int getRole(){
        return getSimpleIntegerValue("rl");
    }

    public void setRole(int role){
        setSimpleValue("rl", role);
    }

    public String getGoodsName(){
        return getSimpleStringValue("gn");
    }

    public void setGoodsName(String goodsName){
        setSimpleValue("gn", goodsName);
    }

    public ObjectId getGoodsId(){
        if(getBaseEntry().containsField("gid")){
            return getSimpleObjecIDValue("gid");
        }
        return null;
    }

    public void setGoodsId(ObjectId goodsId){
        setSimpleValue("gid",goodsId);
    }

    public int getPrice(){
        return getSimpleIntegerValue("pr");
    }

    public void setPrice(int price){
        setSimpleValue("pr", price);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty", type);
    }

    public int getIsLogin(){
        return getSimpleIntegerValue("ili");
    }

    public void setIsLogin(int isLogin){
        setSimpleValue("ili", isLogin);
    }

}
