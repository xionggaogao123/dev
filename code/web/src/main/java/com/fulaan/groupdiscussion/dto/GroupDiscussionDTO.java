package com.fulaan.groupdiscussion.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pojo.groups.GroupsEntry;
import com.pojo.groups.GroupsUser;
import com.sys.utils.CustomDateSerializer;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/3/24.
 */
public class GroupDiscussionDTO {

    /**
     * id
     */
    private String id;

    /**
     * 房间号
     */
    private String roomid;

    /**
     * 群名称
     */
    private String groupname;

    /**
     * 群主
     */
    private String maingroup;


    /**
     * 群成员
     */
    private String[] useridAry;

    /**
     * 群记录
     */
    private List<GroupChatDTO> groupchatList;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createDate;

    /**
     * 删除flg
     */
    private Integer delflg;

    /**
     * 没有观看数量
     */
    private int notviewcount;

    /**
     * 是否有更多消息
     */
    private boolean flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getMaingroup() {
        return maingroup;
    }

    public void setMaingroup(String maingroup) {
        this.maingroup = maingroup;
    }

    public String[] getUseridAry() {
        return useridAry;
    }

    public void setUseridAry(String[] useridAry) {
        this.useridAry = useridAry;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<GroupChatDTO> getGroupchatList() {
        return groupchatList;
    }

    public void setGroupchatList(List<GroupChatDTO> groupchatList) {
        this.groupchatList = groupchatList;
    }

    public Integer getDelflg() {
        return delflg;
    }

    public void setDelflg(Integer delflg) {
        this.delflg = delflg;
    }

    public int getNotviewcount() {
        return notviewcount;
    }

    public void setNotviewcount(int notviewcount) {
        this.notviewcount = notviewcount;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public GroupsEntry buildGroupsEntry() {
        List<GroupsUser> groupsUser = new ArrayList<GroupsUser>();
        if (useridAry!=null && useridAry.length!=0) {
            for (int i=0;i<useridAry.length ;i++) {
                groupsUser.add(new GroupsUser(useridAry[i],0,System.currentTimeMillis()));
            }
        }
        groupsUser.add(new GroupsUser(this.maingroup,0,System.currentTimeMillis()));
        GroupsEntry groupsEntry = new GroupsEntry(
                this.roomid,
                this.maingroup,
                this.groupname,
                groupsUser
        );
        return groupsEntry;
    }
}
