package com.pojo.groups;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 群组聊天
 * <pre>
 * {
 *  rd;房间id
 *  gui:聊天者CHATid
 *  cont:聊天内容
 *  ct:发送时间
 * }
 * </pre>
 * @author wang_xinxin
 */
public class GroupsChatEntry extends BaseDBObject {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7513155528617308356L;

    public GroupsChatEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public GroupsChatEntry(String roomid, String userid, String chatContent) {
        this(roomid,userid,chatContent,System.currentTimeMillis());
    }

    public GroupsChatEntry(String roomid, String userid, String chatContent, long createtime) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("rd",roomid)
                .append("gui", userid)
                .append("cont",chatContent)
                .append("ct",createtime);
        setBaseEntry(baseEntry);
    }

    public String getRoomid() {
        return getSimpleStringValue("rd");
    }
    public void setRoomid(String roomid) {
        setSimpleValue("rd",roomid);
    }

    public String getUserid() {
        return getSimpleStringValue("gui");
    }
    public void setUserid(String userid) {
        setSimpleValue("gui",userid);
    }
    public String getChatContent() {
        return getSimpleStringValue("cont");
    }
    public void setChatContent(String chatContent) {
        setSimpleValue("cont", chatContent);
    }
    public long getCreatetime() {
        return getSimpleLongValue("ct");
    }
    public void setCreatetime(long createtime) {
        setSimpleValue("ct",createtime);
    }
}
