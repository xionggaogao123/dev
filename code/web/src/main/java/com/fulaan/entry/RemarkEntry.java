package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/11/1.
 * 备注名列表
 * {
 *    suid:startUserId 修改备注人
 *    euid:endUserId 被修改备注人
 *    rm:remark 备注名
 * }
 */
public class RemarkEntry extends BaseDBObject {
  public RemarkEntry(DBObject dbo) {
    setBaseEntry((BasicDBObject)dbo);
  }

  public RemarkEntry(ObjectId startUserId, ObjectId endUserId, String remark) {
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("suid", startUserId)
            .append("euid",endUserId)
            .append("rm",remark);
    setBaseEntry(basicDBObject);
  }

  public ObjectId getStartUserId(){
    return getSimpleObjecIDValue("suid");
  }

  public ObjectId getEndUserId(){
    return getSimpleObjecIDValue("euid");
  }

  public String getRemark(){
    return getSimpleStringValue("rm");
  }

}
