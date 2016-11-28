package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/10/26.
 * {
 *   vurl:videoUrl 视频路径
 *   uid:userId 用户Id
 *   iurl:imageUrl 视频封面
 *   ti:time 上传时间
 * }
 */
public class VideoEntry extends BaseDBObject {
  public VideoEntry(DBObject dbObject) {
    setBaseEntry((BasicDBObject) dbObject);
  }

  public VideoEntry(String videoUrl,String imageUrl,long time, ObjectId userId) {
    super();
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("url", videoUrl)
            .append("ti",time)
            .append("iurl",imageUrl)
            .append("uid", userId)
            .append("r", 0);
    setBaseEntry(basicDBObject);
  }

  public VideoEntry(String videoUrl,String imageUrl, ObjectId userId) {
    super();
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("url", videoUrl)
            .append("ti",System.currentTimeMillis())
            .append("iurl",imageUrl)
            .append("uid", userId)
            .append("r", 0);
    setBaseEntry(basicDBObject);
  }

  public VideoEntry(String videoUrl, ObjectId userId) {
    super();
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("url", videoUrl)
            .append("ti",System.currentTimeMillis())
            .append("uid", userId)
            .append("r", 0);
    setBaseEntry(basicDBObject);
  }

  public String getVideoUrl() {
    return getSimpleStringValue("url");
  }

  public void setVideoUrl(String url) {
    setSimpleValue("url", url);
  }

  public String getImageUrl() {
    return getSimpleStringValue("iurl");
  }

  public void setImageUrl(String imageUrl) {
    setSimpleValue("iurl", imageUrl);
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
