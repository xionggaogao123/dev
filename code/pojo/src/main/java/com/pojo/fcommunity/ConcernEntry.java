package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/11/7.
 * {
 *   uid:userId关注人
 *   cid:concernId被关注人列表
 *   r:remove 0:未删除，1:已删除
 * }
 */
public class ConcernEntry  extends BaseDBObject{

  public ConcernEntry(DBObject dbObject){setBaseEntry((BasicDBObject)dbObject);}

  public  ConcernEntry(ObjectId userId,ObjectId concernId){
    BasicDBObject dbObject=new BasicDBObject()
            .append("uid",userId)
            .append("cid",concernId)
            .append("r",0);
    setBaseEntry(dbObject);
  }

  public ObjectId getUserId(){
    return getSimpleObjecIDValue("uid");
  }

  public ObjectId getConcernId(){
    return getSimpleObjecIDValue("cid");
  }

  public int getRemove(){
    return getSimpleIntegerValue("r");
  }
}
