package com.fulaan.pojo;

import com.pojo.fcommunity.GroupAnnounceEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by moslpc on 2016/11/8.
 */
public class GroupAnnounceDTO {

  private String id;
  private String title;
  private String userId;
  private String time;
  private String groupId;
  private String content;
  private List<String> images;


  public GroupAnnounceDTO(GroupAnnounceEntry entry){
    this.id = entry.getID().toString();
    this.groupId = entry.getGroupId().toString();

    if(StringUtils.isNotBlank(entry.getAnnounce())){
      this.content = entry.getAnnounce();
    }
    if(StringUtils.isBlank(entry.getAnnounce())){
      if(entry.getImages().size() > 1){
        this.content = "[图片]";
      }
    }
    this.title = entry.getTitle();
    this.userId = entry.getUserId().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<String> getImages() {
    return images;
  }

  public void setImages(List<String> images) {
    this.images = images;
  }
}
