package com.pojo.indexPage;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页加载表
 * Created by James on 2017/9/28.
 * id
 * userId           发送用户               uid
 * type             类型                   typ    1 作业    2 通知    4 系统通知  6 投票    7 活动   8 新集合通知  9 新成绩单
 * communityId      社区id                 cid
 * contactId        联系记录id             tid
 * List<ObjectId>   接受者集合              receiveIdList（社群集合）
 * List<Integer>    接受权限集合            roleList（角色集合）
 */
public class IndexPageEntry extends BaseDBObject {

    public IndexPageEntry(){

    }
    public IndexPageEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public IndexPageEntry(
            int type,
            ObjectId userId,
            ObjectId communityId,
            ObjectId contactId,
            List<ObjectId> receiveIdList,
            List<Integer> roleList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("typ", type)
                .append("uid",userId)
                .append("cid", communityId)
                .append("tid",contactId)
                .append("rlt",receiveIdList)
                .append("olt",roleList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public IndexPageEntry(
            int type,
            ObjectId userId,
            ObjectId communityId,
            ObjectId contactId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("typ", type)
                .append("uid",userId)
                .append("cid", communityId)
                .append("tid",contactId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public IndexPageEntry(
            ObjectId id,
            int type,
            ObjectId userId,
            ObjectId communityId,
            ObjectId contactId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("typ", type)
                .append("uid", userId)
                .append("cid", communityId)
                .append("tid",contactId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public List<Integer> getRoleList(){
        @SuppressWarnings("rawtypes")
        List voteTypeList =(List)getSimpleObjectValue("olt");
        return voteTypeList;
    }

    public void setRoleList(List<Integer> roleList){
        setSimpleValue("olt",roleList);
    }

    public void setReceiveIdList(List<ObjectId> receiveIdList){
        setSimpleValue("rlt", MongoUtils.convert(receiveIdList));
    }

    public List<ObjectId> getReceiveIdList(){
        ArrayList<ObjectId> receiveIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("rlt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                receiveIdList.add((ObjectId)obj);
            }
        }
        return receiveIdList;
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }
    public ObjectId getContactId(){
        return getSimpleObjecIDValue("tid");
    }

    public void setContactId(ObjectId contactId){
        setSimpleValue("tid",contactId);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
