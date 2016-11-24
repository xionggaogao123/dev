package com.pojo.activity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * id
 * ui
 * [
 * u2,t
 * u3,
 * ]
 * u2
 * Created by yan on 2015/3/3.
 *
 *
 * 更改日志:2016/10/24
 * fid   friend
 * ptd   partner
 */
public class FriendEntry extends BaseDBObject {

  private static final long serialVersionUID = 1232296937168011907L;

  /*
    *
    * uid  用户id
    * fid  好友id 数组
    * idf  认证好友 === 1
    * */
  public FriendEntry(BasicDBObject baseDBObject) {
    super(baseDBObject);
  }

  /**
   * 普通好友
   *
   * @param uid
   * @param fid
   */
  public FriendEntry(ObjectId uid, ObjectId fid) {
    super();
    BasicDBObject dbo = new BasicDBObject()
            .append("uid", uid)
            .append(Constant.MONGO_PUSH, new BasicDBObject("fid", fid));
    setBaseEntry(dbo);
  }

  public FriendEntry(){

  }

  /**
   * 伙伴
   *
   * @param uid
   * @param fid
   */
  public static FriendEntry exportIdenFriend(ObjectId uid, ObjectId fid) {
    BasicDBObject dbo = new BasicDBObject()
            .append("uid", uid)
            .append(Constant.MONGO_PUSH, new BasicDBObject("ptd", fid));
    return new FriendEntry(dbo);
  }


  public ObjectId getUserId() {
    return getSimpleObjecIDValue("uid");
  }

  public List<ObjectId> getFriendIds() {
    List<ObjectId> objectIdList = new ArrayList<ObjectId>();
    BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("fid");
    if (basicDBList != null) {
      for (Object object : basicDBList) {
        objectIdList.add((ObjectId) object);
      }
    }
    return objectIdList;
  }

  public List<ObjectId> getPartnerIds() {
    List<ObjectId> objectIdList = new ArrayList<ObjectId>();
    BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("ptd");
    if (basicDBList != null) {
      for (Object object : basicDBList) {
        objectIdList.add((ObjectId) object);
      }
    }
    return objectIdList;
  }

  public void setUserId(ObjectId uid){

  }

  public void setFriendIds(List<ObjectId> ids){

  }
}
