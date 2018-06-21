package com.pojo.excellentCourses;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-27.
 * 用户行为表
 *
 * id                        id                 id
 * userId                    用户id             uid
 * account                   账户余额吧         acc 美豆
 * collectList <ObjectId>    收藏列表           clt
 * browseList<ObjectId>      浏览列表           blt
 * sonOpenList<ObjectId>     关闭孩子权限       slt
 *
 */
public class UserBehaviorEntry extends BaseDBObject {


    public UserBehaviorEntry(){

    }

    public UserBehaviorEntry(BasicDBObject object){
        super(object);
    }


    //添加构造
    public UserBehaviorEntry(
            ObjectId userId,
            int account,
            List<ObjectId> collectList,
            List<ObjectId> browseList,
            List<ObjectId> sonOpenList

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("acc", account)
                .append("clt", collectList)
                .append("blt", browseList)
                .append("slt", sonOpenList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public UserBehaviorEntry(
            ObjectId id,
            ObjectId userId,
            int account,
            List<ObjectId> collectList,
            List<ObjectId> browseList,
            List<ObjectId> sonOpenList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("acc",account)
                .append("clt", collectList)
                .append("blt", browseList)
                .append("slt", sonOpenList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public void setCollectList(List<ObjectId> collectList){
        setSimpleValue("clt", MongoUtils.convert(collectList));
    }

    public List<ObjectId> getCollectList(){
        ArrayList<ObjectId> collectList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                collectList.add((ObjectId)obj);
            }
        }
        return collectList;
    }

    public void setSonOpenList(List<ObjectId> sonOpenList){
        setSimpleValue("slt", MongoUtils.convert(sonOpenList));
    }

    public List<ObjectId> getSonOpenList(){
        ArrayList<ObjectId> sonOpenList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("slt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                sonOpenList.add((ObjectId)obj);
            }
        }
        return sonOpenList;
    }

    public void setBrowseList(List<ObjectId> browseList){
        setSimpleValue("blt", MongoUtils.convert(browseList));
    }

    public List<ObjectId> getBrowseList(){
        ArrayList<ObjectId> browseList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("blt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                browseList.add((ObjectId)obj);
            }
        }
        return browseList;
    }
    public int getAccount(){
        return getSimpleIntegerValueDef("acc",0);
    }

    public void setAccount(int account){
        setSimpleValue("acc",account);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
