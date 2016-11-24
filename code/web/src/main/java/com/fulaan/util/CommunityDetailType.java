package com.fulaan.util;

/**
 * Created by admin on 2016/10/26.
 * ANNOUNCEMENT:
 * ACTIVITY:
 * SHARA:
 * HOMEWORK:
 * MATERIALS:
 */
public enum CommunityDetailType {

  ANNOUNCEMENT(1, "ANNOUNCEMENT"),
  ACTIVITY(2, "ACTIVITY"),
  SHARE(3,"SHARE"),
  MEANS(4,"MEANS"),
  HOMEWORK(5,"HOMEWORK"),
  MATERIALS(6,"MATERIALS"),
  ;

  CommunityDetailType(int type, String decs) {
    this.type = type;
    this.decs = decs;
  }
  private int type;
  private String decs;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getDecs() {
    return decs;
  }

  public void setDecs(String decs) {
    this.decs = decs;
  }

}
