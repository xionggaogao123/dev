package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by jerry on 2016/11/1.
 * 成员Entry
 * <p/>
 * MemberEntry
 * {
 * uid: 用户id
 * grid: 群组id
 * rl:角色
 * st：设置：免打扰 0:关闭  1：开启
 * nk：群里的昵称
 * nm: 用户名
 * av：头像
 * }
 * <p/>
 */
public class MemberEntry extends BaseDBObject {

  public MemberEntry(DBObject dbo){
    setBaseEntry((BasicDBObject)dbo);
  }
  /**
   * MemberEntry
   *
   * @param userId
   * @param nickName
   * @param avator
   * @param role
   */
  public MemberEntry(ObjectId userId,ObjectId groupId,String nickName, String avator,int role,String userName) {
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("uid", userId)
            .append("grid",groupId)
            .append("rl", role)
            .append("st", 0)
            .append("nk", nickName)
            .append("nm",userName)
            .append("av",avator)
            .append("r",0);
    setBaseEntry(basicDBObject);
  }

  public ObjectId getUserId() {
    return getSimpleObjecIDValue("uid");
  }

  public int getRole() {
    return getSimpleIntegerValue("rl");
  }

  public int getStatus() {
    return getSimpleIntegerValue("st");
  }

  public String getNickName() {
    return getSimpleStringValue("nk");
  }

  public String getAvator(){
    return getSimpleStringValue("av");
  }

  public ObjectId getGroupId(){
    return getSimpleObjecIDValue("grid");
  }

  public int getRemove(){return getSimpleIntegerValueDef("r",0);}

  public String getUserName(){
    return getSimpleStringValue("nm");
  }


}
