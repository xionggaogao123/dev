package com.pojo.business;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 运营所关表
 * Created by James on 2018/1/15.
 * id                                             id
 * userId           用户id                       uid
 * homeId           家校美id                     hid
 * phone            注册手机                     pho
 * type             1 微信 2qq 3 正式用户        typ
 * openId           qq/微信登陆                  qid
 * communityIdList  社区id集合                   clt
 * communityNumbers 参与的社区号集合             cnm
 * communityRoles   创建的社区号集合             rnm
 * childIdList      孩子id集合                   cht
 * weekNumber       周登陆频率                   wnm
 * monthNumber      月登陆频率                   mnm
 * functionList     常用功能                     fun
 * role             角色（1 孩子  2 家长  3教师）rol
 * subjectIdList    年级列表（）                 sut
 * createTime       创建时间                     ctm
 * phoneType        ios/安卓                     pty
 *      PC(1,"PC"),
        Android(2,"Android"),
        IOS(3,"IOS");
 * storeType        运营商（电信、移动、联通）   sty
 * addressIp        注册ip                       aip
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
            List<String> communityNumbers,
            List<String> communityRoles,
            List<ObjectId> childIdList,
            List<ObjectId> subjectIdList,
            int weekNumber,
            int monthNumber,
            List<String> functionList,
            int role,
            long createTime,
            long onlineTime,
            String storeType,
            String addressIp,
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
                .append("cnm", communityNumbers)
                .append("rnm", communityRoles)
                .append("cht", childIdList)
                .append("sut", subjectIdList)
                .append("wnm", weekNumber)
                .append("mnm", monthNumber)
                .append("fun", functionList)
                .append("rol", role)
                .append("sty",storeType)
                .append("aip",addressIp)
                .append("rty", regionType)
                .append("ctm", createTime)
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
            List<String> communityNumbers,
            List<String> communityRoles,
            List<ObjectId> childIdList,
            List<ObjectId> subjectIdList,
            int weekNumber,
            int monthNumber,
            List<String> functionList,
            int role,
            long createTime,
            long onlineTime,
            String storeType,
            String addressIp,
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
                .append("cnm", communityNumbers)
                .append("rnm", communityRoles)
                .append("cht", childIdList)
                .append("sut", subjectIdList)
                .append("wnm", weekNumber)
                .append("mnm", monthNumber)
                .append("fun", functionList)
                .append("rol", role)
                .append("sty", storeType)
                .append("aip", addressIp)
                .append("rty", regionType)
                .append("ctm", createTime)
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
    public String getPhone(){
        return getSimpleStringValue("pho");
    }
    public void setPhone(String phone){
        setSimpleValue("pho", phone);
    }

    public String getAddressIp(){
        return getSimpleStringValue("aip");
    }
    public void setAddressIp(String addressIp){
        setSimpleValue("aip", addressIp);
    }
    public String getRegionType(){
        return getSimpleStringValue("rty");
    }
    public void setRegionType(String regionType){
        setSimpleValue("rty", regionType);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getWeekNumber(){
        return getSimpleIntegerValue("wnm");
    }

    public void setWeekNumber(int weekNumber){
        setSimpleValue("wnm",weekNumber);
    }

    public int getMonthNumber(){
        return getSimpleIntegerValue("mnm");
    }

    public void setMonthNumber(int monthNumber){
        setSimpleValue("mnm",monthNumber);
    }

    public int getRole(){
        return getSimpleIntegerValue("rol");
    }

    public void setRole(int role){
        setSimpleValue("rol",role);
    }
    public String getStoreType(){
        return getSimpleStringValue("sty");
    }

    public void setStoreType(String storeType){
        setSimpleValue("sty",storeType);
    }
    public long getOnlineTime(){
        return getSimpleLongValue("otm");
    }

    public void setOnlineTime(long onlineTime){
        setSimpleValue("otm",onlineTime);
    }

    public String getOpenId(){
        return getSimpleStringValue("oid");
    }
    public void setOpenId(String openId){
        setSimpleValue("oid", openId);
    }

    public String getHomeId(){
        return getSimpleStringValue("hid");
    }
    public void setHomeId(String homeId){
        setSimpleValue("hid", homeId);
    }

    public void setCommunityIdList(List<ObjectId> communityIdList){
        setSimpleValue("clt", MongoUtils.convert(communityIdList));
    }

    public List<ObjectId> getCommunityIdList(){
        ArrayList<ObjectId> communityIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                communityIdList.add((ObjectId)obj);
            }
        }
        return communityIdList;
    }

    public void setChildIdList(List<ObjectId> childIdList){
        setSimpleValue("cht", MongoUtils.convert(childIdList));
    }

    public List<ObjectId> getChildIdList(){
        ArrayList<ObjectId> childIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("cht");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                childIdList.add((ObjectId)obj);
            }
        }
        return childIdList;
    }

    public void setSubjectIdList(List<ObjectId> subjectIdList){
        setSimpleValue("sut", MongoUtils.convert(subjectIdList));
    }

    public List<ObjectId> getSubjectIdList(){
        ArrayList<ObjectId> subjectIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("sut");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                subjectIdList.add((ObjectId)obj);
            }
        }
        return subjectIdList;
    }
    public void setCommunityNumbers(List<String> communityNumbers){
        setSimpleValue("cnm",communityNumbers);
    }

    public List<String> getCommunityNumbers(){
        @SuppressWarnings("rawtypes")
        List communityNumbers =(List)getSimpleObjectValue("cnm");
        return communityNumbers;
    }

    public void setCommunityRoles(List<String> communityRoles){
        setSimpleValue("rnm",communityRoles);
    }

    public List<String> getCommunityRoles(){
        @SuppressWarnings("rawtypes")
        List communityRoles =(List)getSimpleObjectValue("rnm");
        return communityRoles;
    }

    public void setFunctionList(List<String> functionList){
        setSimpleValue("fun",functionList);
    }

    public List<String> getFunctionList(){
        @SuppressWarnings("rawtypes")
        List functionList =(List)getSimpleObjectValue("fun");
        return functionList;
    }
    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
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
