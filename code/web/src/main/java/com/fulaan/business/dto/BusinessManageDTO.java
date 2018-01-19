package com.fulaan.business.dto;

import com.pojo.business.BusinessManageEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/1/16.
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
 */
public class BusinessManageDTO {
    private String id;
    private String userId;
    private String avatar;
    private String userName;
    private String homeId;
    private String phone;
    private int type;
    private String openId;
    private List<String>  communityIdList = new ArrayList<String>();
    private List<String> communityNumbers = new ArrayList<String>();
    private List<String> communityRoles = new ArrayList<String>();
    private List<String> childIdList = new ArrayList<String>();
    private List<String> subjectIdList = new ArrayList<String>();
    private int weekNumber;
    private int monthNumber;
    private List<String> functionList = new ArrayList<String>();
    private int role;
    private String createTime;
    private long onlineTime;
    private String storeType;
    private String regionType;
    private int phoneType;

    public BusinessManageDTO(){

    }

    public BusinessManageDTO(BusinessManageEntry e) {
        if (e != null) {
            this.id = e.getID() == null ? "" : e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.homeId = e.getHomeId();
            this.phone = e.getPhone();
            this.type = e.getType();
            this.openId = e.getOpenId();
            List<String> stringList = new ArrayList<String>();
            if(e.getCommunityNumbers() != null){
                for(String str : e.getCommunityNumbers()){
                    stringList.add(str+" ");
                }
            }
            this.communityNumbers = stringList;
            List<String> stringList2 = new ArrayList<String>();
            if(e.getCommunityRoles() != null){
                for(String str : e.getCommunityRoles()){
                    stringList2.add(str+" ");
                }
            }
            this.communityRoles = stringList2;
            this.weekNumber = e.getWeekNumber();
            this.monthNumber = e.getMonthNumber();
            this.functionList = e.getFunctionList();
            List<ObjectId> cmIdList = e.getCommunityIdList();
            for (ObjectId uId : cmIdList) {
                communityIdList.add(uId.toString());
            }
            List<ObjectId> chIdList = e.getChildIdList();
            for (ObjectId uId : chIdList) {
                childIdList.add(uId.toString());
            }
            List<ObjectId> subIdList = e.getSubjectIdList();
            for (ObjectId uId : subIdList) {
                subjectIdList.add(uId.toString());
            }
            this.role = e.getRole();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            this.onlineTime = e.getOnlineTime();
            this.storeType = e.getStoreType();
            this.regionType =e.getRegionType();
            this.phoneType = e.getPhoneType();
        } else {
            new BusinessManageDTO();
        }

    }

    /* ObjectId id,
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
            int phoneType*/
    public BusinessManageEntry buildAddEntry(){
        ObjectId aId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            aId=new ObjectId(this.getUserId());
        }
        long cTm = 0l;
        if(this.getCreateTime() != null && this.getCreateTime() != ""){
            cTm = DateTimeUtils.getStrToLongTime(this.getCreateTime());
        }
        List<ObjectId> cmIdList = new ArrayList<ObjectId>();
        for(String sId : this.communityIdList){
            cmIdList.add(new ObjectId(sId));
        }
        List<ObjectId> chIdList = new ArrayList<ObjectId>();
        for(String sId : this.childIdList){
            chIdList.add(new ObjectId(sId));
        }
        List<ObjectId> sIdList = new ArrayList<ObjectId>();
        for(String sId : this.subjectIdList){
            sIdList.add(new ObjectId(sId));
        }
        BusinessManageEntry openEntry =
                new BusinessManageEntry(
                        aId,
                        this.homeId,
                        this.phone,
                        this.type,
                        this.openId,
                        cmIdList,
                        this.communityNumbers,
                        this.communityRoles,
                        chIdList,
                        sIdList,
                        this.weekNumber,
                        this.monthNumber,
                        this.functionList,
                        this.role,
                        cTm,
                        this.onlineTime,
                        this.storeType,
                        this.regionType,
                        this.phoneType
                );
        return openEntry;

    }
    public BusinessManageEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId aId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            aId=new ObjectId(this.getUserId());
        }
        long cTm = 0l;
        if(this.getCreateTime() != null && this.getCreateTime() != ""){
            cTm = DateTimeUtils.getStrToLongTime(this.getCreateTime());
        }
        List<ObjectId> cmIdList = new ArrayList<ObjectId>();
        for(String sId : this.communityIdList){
            cmIdList.add(new ObjectId(sId));
        }
        List<ObjectId> chIdList = new ArrayList<ObjectId>();
        for(String sId : this.childIdList){
            chIdList.add(new ObjectId(sId));
        }
        List<ObjectId> sIdList = new ArrayList<ObjectId>();
        for(String sId : this.subjectIdList){
            sIdList.add(new ObjectId(sId));
        }
        BusinessManageEntry openEntry =
                new BusinessManageEntry(
                        Id,
                        aId,
                        this.homeId,
                        this.phone,
                        this.type,
                        this.openId,
                        cmIdList,
                        this.communityNumbers,
                        this.communityRoles,
                        chIdList,
                        sIdList,
                        this.weekNumber,
                        this.monthNumber,
                        this.functionList,
                        this.role,
                        cTm,
                        this.onlineTime,
                        this.storeType,
                        this.regionType,
                        this.phoneType
                );
        return openEntry;

    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public List<String> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<String> functionList) {
        this.functionList = functionList;
    }

    public List<String> getCommunityRoles() {
        return communityRoles;
    }

    public void setCommunityRoles(List<String> communityRoles) {
        this.communityRoles = communityRoles;
    }

    public List<String> getCommunityNumbers() {
        return communityNumbers;
    }

    public void setCommunityNumbers(List<String> communityNumbers) {
        this.communityNumbers = communityNumbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public List<String> getCommunityIdList() {
        return communityIdList;
    }

    public void setCommunityIdList(List<String> communityIdList) {
        this.communityIdList = communityIdList;
    }

    public List<String> getChildIdList() {
        return childIdList;
    }

    public void setChildIdList(List<String> childIdList) {
        this.childIdList = childIdList;
    }

    public List<String> getSubjectIdList() {
        return subjectIdList;
    }

    public void setSubjectIdList(List<String> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }
}
