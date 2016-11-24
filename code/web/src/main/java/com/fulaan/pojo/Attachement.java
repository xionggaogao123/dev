package com.fulaan.pojo;

import com.fulaan.entry.AttachmentEntry;
import com.fulaan.util.DateUtils;

/**
 * Created by jerry on 2016/10/25.
 * 附件信息 pojo
 */
public class Attachement {

  private String url;
  private String flnm;
  private String uploadUserId;
  private String time;

  public Attachement(AttachmentEntry entry){
    this.url = entry.getUrl();
    this.flnm = entry.getFileName();
    this.uploadUserId = entry.getUserId().toString();
    this.time = DateUtils.timeStampToStr(entry.getTime() / 1000);
  }

  public Attachement(){

  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFlnm() {
    return flnm;
  }

  public void setFlnm(String flnm) {
    this.flnm = flnm;
  }

  public String getUploadUserId() {
    return uploadUserId;
  }

  public void setUploadUserId(String uploadUserId) {
    this.uploadUserId = uploadUserId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
