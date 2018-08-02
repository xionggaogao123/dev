package com.fulaan.excellentCourses.dto;

/**
 * Created by James on 2018-07-10.
 * cc 登陆回调对象
 *
 userid	字符串	用户ID
 roomid	字符串	直播间ID
 viewername	字符串	登陆用户名，限制20个字符
 viewertoken	字符串	登录校验码，限制40个字符
 viewercustomua	字符串	可选，用户uatype信息，限制40个字符
 liveid	字符串	观看回放验证时会传递该参数
 recordid	字符串	观看回放验证时会传递该参数
 *
 */
public class CCLoginDTO {
    private String userid;
    private String roomid;
    private String viewername;
    private String viewertoken;
    private String viewercustomua;
    private String liveid;
    private String recordid;

    public CCLoginDTO(){

    }

    public CCLoginDTO(String userid,String roomid,String viewername,String viewertoken,String viewercustomua,String liveid,String recordid){
        this.userid =userid;
        this.roomid =roomid;
        this.viewername =viewername;
        this.viewertoken =viewertoken;
        this.viewercustomua =viewercustomua;
        this.liveid =liveid;
        this.recordid =recordid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getViewername() {
        return viewername;
    }

    public void setViewername(String viewername) {
        this.viewername = viewername;
    }

    public String getViewertoken() {
        return viewertoken;
    }

    public void setViewertoken(String viewertoken) {
        this.viewertoken = viewertoken;
    }

    public String getViewercustomua() {
        return viewercustomua;
    }

    public void setViewercustomua(String viewercustomua) {
        this.viewercustomua = viewercustomua;
    }

    public String getLiveid() {
        return liveid;
    }

    public void setLiveid(String liveid) {
        this.liveid = liveid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }
}
