package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 运营所关表
 * Created by James on 2018/1/15.
 * id                                             id
 * userId           用户id                       uid
 * homeId           家校美id                     hid
 * phone            注册手机                     pho
 * type            1 正式用户 2 QQ 3微信         typ
 * openId           qq/微信登陆                  qid
 * communityIdList  社区id集合                   clt
 * childIdList      孩子id集合                   cht
 * role             角色（1 孩子  2 家长  3教师）rol
 * subjectIdList    年级列表（）                 sut
 * createTime       创建时间                     ctm
 * phoneType        ios/安卓                     pty
 * storeType        运营商（电信、移动、联通）   sty
 * regionType       地区                         rty
 * onlineTime       总在线时间                   otm
 *
 */
public class BusinessManageEntry extends BaseDBObject {
    public BusinessManageEntry(){

    }

    public BusinessManageEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public BusinessManageEntry(
            ObjectId userId,
            String homeId,
            String phone,
            int type,
            String openId,
            List<ObjectId> communityIdList,
            List<ObjectId> childIdList,
            List<ObjectId> subjectIdList,
            int role,
            long createTime,
            int onlineTime,
            int storeType,
            String regionType,
            int phoneType
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("hid", homeId)
                .append("pho", phone)
                .append("typ", type)
                .append("oid", openId)
                .append("clt", communityIdList)
                .append("cht", childIdList)
                .append("sut", subjectIdList)
                .append("rol", role)
                .append("sty",storeType)
                .append("rty",regionType)
                .append("ltm", createTime)
                .append("otm",onlineTime)
                .append("pty",phoneType)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public BusinessManageEntry(
            ObjectId id,
            ObjectId userId,
            String homeId,
            String phone,
            int type,
            String openId,
            List<ObjectId> communityIdList,
            List<ObjectId> childIdList,
            List<ObjectId> subjectIdList,
            int role,
            long createTime,
            int onlineTime,
            int storeType,
            String regionType,
            int phoneType
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("hid", homeId)
                .append("pho", phone)
                .append("typ", type)
                .append("oid", openId)
                .append("clt", communityIdList)
                .append("cht", childIdList)
                .append("sut", subjectIdList)
                .append("rol", role)
                .append("sty", storeType)
                .append("rty",regionType)
                .append("ltm", createTime)
                .append("otm",onlineTime)
                .append("pty",phoneType)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public long getLoginTime(){
        return getSimpleLongValue("ltm");
    }

    public void setLoginTime(String loginTime){
        setSimpleValue("ltm",loginTime);
    }

    public int getDuration(){
        return getSimpleIntegerValue("dti");
    }

    public void setDuration(int duration){
        setSimpleValue("dti",duration);
    }
    public int getPhoneType(){
        return getSimpleIntegerValue("pty");
    }

    public void setPhoneType(int phoneType){
        setSimpleValue("pty",phoneType);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
