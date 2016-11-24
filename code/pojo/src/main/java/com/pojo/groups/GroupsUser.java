package com.pojo.groups;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 群成员，依附GroupsEntry
 * <pre>
 * {
 *  id:标识ID
 *  gui:用户chatid
 *  ir:是否打开过
 *  ct:打开时间
 * }
 * </pre>
 * @author wang_xinxin
 */
public class GroupsUser extends BaseDBObject{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 72204307021234926L;

    public GroupsUser(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public GroupsUser(String userid,int isread,long updatetime) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("gui",userid)
                .append("ir",isread)
                .append("ct",updatetime);
        setBaseEntry(baseEntry);
    }

    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }
    public void setId(ObjectId id) {
        setSimpleValue("id", id);
    }
    public String getUserid() {
        return getSimpleStringValue("gui");
    }
    public void setUserid(String userid) {
        setSimpleValue("gui",userid);
    }
    public int getIsread() {
        return getSimpleIntegerValue("ir");
    }
    public void setIsread(int isread) {
        setSimpleValue("ir",isread);
    }
    public long getUpdatetime() {
        return getSimpleLongValue("ct");
    }
    public void setUpdatetime(long updatetime) {
        setSimpleValue("ct",updatetime);
    }
}

