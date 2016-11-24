package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/10/24.
 * 附件
 * {
 * url:url 附件路径
 * flnm:fileName 文件名
 * ti:time 上传时间
 * uid:userId 上传人Id
 * r:remove 0：未删除 1：已删除
 * <p/>
 * }
 */
public class AttachmentEntry extends BaseDBObject {

  public AttachmentEntry(DBObject dbObject) {
    setBaseEntry((BasicDBObject) dbObject);
  }

  public AttachmentEntry(String url, String fileName, long time, ObjectId userId) {
    super();
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("url", url)
            .append("flnm", fileName)
            .append("ti", time)
            .append("uid", userId)
            .append("r", 0);
    setBaseEntry(basicDBObject);
  }

  public AttachmentEntry(String url, String fileName, ObjectId userId) {
    super();
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("url", url)
            .append("flnm", fileName)
            .append("ti", System.currentTimeMillis())
            .append("uid", userId)
            .append("r", 0);
    setBaseEntry(basicDBObject);
  }

  public String getUrl() {
    return getSimpleStringValue("url");
  }

  public void setUrl(String url) {
    setSimpleValue("url", url);
  }

  public String getFileName() {
    return getSimpleStringValue("flnm");
  }

  public void setFileName(String fileName) {
    setSimpleValue("flnm", fileName);
  }

  public Long getTime() {
    return getSimpleLongValue("ti");
  }

  public void setTime(Long time) {
    setSimpleValue("ti", time);
  }

  public ObjectId getUserId() {
    return getSimpleObjecIDValue("uid");
  }

  public void setUserId(ObjectId userId) {
    setSimpleValue("uid", userId);
  }

  public int getRemove() {
    return getSimpleIntegerValue("r");
  }

  public void setRemove(int remove) {
    setSimpleValue("r", remove);
  }

}
