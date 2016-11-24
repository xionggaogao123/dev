package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/11/8.
 *
 * grid : 群组id
 * grti : group title
 * gran : 群组公告
 * uid  : 发布人用户id
 * grim : 群组图片
 * r:   : 是否删除
 */
public class GroupAnnounceEntry extends BaseDBObject {

  public GroupAnnounceEntry(DBObject dbo){
    setBaseEntry((BasicDBObject)dbo);
  }

  public GroupAnnounceEntry(ObjectId _id,ObjectId groupId,ObjectId userId,String title,String announce,List<String> images){
    BasicDBObject dbo = new BasicDBObject()
            .append("_id",_id)
            .append("grid", groupId)
            .append("grti",title)
            .append("gran", announce)
            .append("grim", images)
            .append("uid",userId)
            .append("r", 0);
    setBaseEntry(dbo);
  }

  public GroupAnnounceEntry(ObjectId _id,ObjectId groupId,ObjectId userId,String title,String announce){
    BasicDBObject dbo = new BasicDBObject()
            .append("_id",_id)
            .append("grid", groupId)
            .append("grti",title)
            .append("gran", announce)
            .append("uid",userId)
            .append("r", 0);
    setBaseEntry(dbo);
  }

  public ObjectId getGroupId(){
    return getSimpleObjecIDValue("grid");
  }

  public String getAnnounce(){
    return getSimpleStringValue("gran");
  }

  public List<String> getImages(){
    if(getBaseEntry().containsKey("grim")){
      return (List<String>)getSimpleObjectValue("grim");
    }
    return new ArrayList<String>();
  }

  public ObjectId getUserId(){
    return getSimpleObjecIDValue("uid");
  }

  public String getTitle(){
    return getSimpleStringValue("grti");
  }

}
