package com.pojo.groups;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组
 * <pre>
 * collectionName:groups
 * </pre>
 * <pre>
 * {
 *  rd:房间id
 *  ui:创建者userid
 *  gn:群名称
 *  ct:创建时间
 *  st:状态；详见GroupState
 *  guli[]:群成员参见GroupsUser
 * }
 * </pre>
 * @author wang_xinxin
 */
public class GroupsEntry extends BaseDBObject {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7115463627339851612L;

    public GroupsEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public GroupsEntry(String roomid,String userid,String groupname,List<GroupsUser> groupsUserList) {
        this(roomid,userid,groupname,System.currentTimeMillis(), DeleteState.NORMAL.getState(),groupsUserList);
    }

    public GroupsEntry(String roomid, String userid, String groupname, long createtime,int status, List<GroupsUser> groupsUserList) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("rd",roomid)
                .append("ui",userid)
                .append("gn",groupname)
                .append("ct",createtime)
                .append("st",status)
                .append("guli",MongoUtils.convert(MongoUtils.fetchDBObjectList(groupsUserList)));
        setBaseEntry(baseEntry);
    }
    public String getUserid() {
        return getSimpleStringValue("ui");
    }
    public void setUserid(String userid) {
        setSimpleValue("ui", userid);
    }
    public String getRoomid() {
        return getSimpleStringValue("rd");
    }
    public void setRoomid(String roomid) {
        setSimpleValue("rd", roomid);
    }
    public String getGroupname() {return getSimpleStringValue("gn");}
    public void setGroupname(String groupname) {setSimpleValue("gn",groupname);}
    public long getCreatetime() {return getSimpleLongValue("ct");}
    public void setCreatetime(long createtime) {setSimpleValue("ct",createtime);}

    public List<GroupsUser> getGroupsUserList() {
        List<GroupsUser> retList =new ArrayList<GroupsUser>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("guli");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new GroupsUser((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setGroupsUserList(List<GroupsUser> groupsUserList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(groupsUserList);
        setSimpleValue("guli", MongoUtils.convert(list));
        
    }


}
