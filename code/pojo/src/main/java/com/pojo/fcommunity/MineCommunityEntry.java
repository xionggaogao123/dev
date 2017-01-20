package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by moslpc on 2016/11/17.
 * 我的社区 Entry
 * uid  --- 用户id
 * cmid --- 社区id
 * prio --- 优先级  3-复兰社区  2-自己创建的社区   1-加入的社区
 *
 */
public class MineCommunityEntry extends BaseDBObject {

  public MineCommunityEntry(DBObject dbo){
    setBaseEntry((BasicDBObject)dbo);
  }

  public MineCommunityEntry(ObjectId userId,ObjectId communityId,int priority,int customSort){
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("uid", userId)
            .append("cmid",communityId)
            .append("prio",priority)
            .append("cust",customSort)
            .append("tp",0);
    setBaseEntry(basicDBObject);
  }

  public ObjectId getCommunityId(){
    return getSimpleObjecIDValue("cmid");
  }

  public ObjectId getUserId(){
    return getSimpleObjecIDValue("uid");
  }

  public int getPriority(){
    return getSimpleIntegerValue("prio");
  }

  public int getTop(){
      if(getBaseEntry().containsField("tp")){
          return getSimpleIntegerValueDef("tp",0);
      }else{
          return 0;
      }
  }

  public int getCustomSort(){
      if(getBaseEntry().containsField("cust")){
          return getSimpleIntegerValueDef("cust",0);
      }else{
          return 0;
      }
  }

  public void setCustomSort(int customSort){
      setSimpleValue("cust",customSort);
  }

  public void setTop(int top){
      setSimpleValue("tp",top);
  }

}
