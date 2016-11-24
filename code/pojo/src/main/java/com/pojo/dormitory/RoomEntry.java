package com.pojo.dormitory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.app.SimpleDTO;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/9/12.
 */
public class RoomEntry extends BaseDBObject {

    private static final long serialVersionUID = -8809886016811613432L;

    public RoomEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public RoomEntry(String roomName,int bedNum,ObjectId loopId,int roomType,String remark,List<RoomUserEntry> simpleDTOs) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("rnm", roomName)
                .append("bum", bedNum)
                .append("ocynum",0)
                .append("beds", MongoUtils.convert(MongoUtils.fetchDBObjectList(simpleDTOs)))
                .append("lid", loopId)
                .append("rtp",roomType)
                .append("rmk", remark)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }
    public ObjectId getLoopId() {
        return getSimpleObjecIDValue("lid");
    }
    public void setLoopId(ObjectId loopId) {
        setSimpleValue("lid", loopId);
    }
    public String getRemark() {
        return getSimpleStringValue("rmk");
    }
    public void setRemark(String remark) {
        setSimpleValue("rmk",remark);
    }
    public String getRoomName() {
        return getSimpleStringValue("rnm");
    }
    public void setRoomName(String roomName) {
        setSimpleValue("rnm",roomName);
    }
    public int getBedNum() {
        return getSimpleIntegerValue("bum");
    }
    public void setBedNum(int bedNum) {
        setSimpleValue("bum",bedNum);
    }
    public int getRoomType() {
        return getSimpleIntegerValue("rtp");
    }
    public void setRoomType(int roomType) {
        setSimpleValue("rtp",roomType);
    }
    public List<RoomUserEntry> getSimpleDTOs() {
        List<RoomUserEntry> retList =new ArrayList<RoomUserEntry>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("beds");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new RoomUserEntry((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setSimpleDTOs(List<RoomUserEntry> simpleDTOs) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(simpleDTOs);
        setSimpleValue("beds",  MongoUtils.convert(list));
    }

    public int getOccupancyNum() {
        return getSimpleIntegerValue("ocynum");
    }
    public void setOccupancyNum(int occupancyNum) {
        setSimpleValue("ocynum",occupancyNum);
    }
}
