package com.db.dormitory;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.dormitory.*;
import com.pojo.emarket.WithDrawEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wang_xinxin on 2016/9/12.
 */
public class DormitoryDao extends BaseDao {

    /**
     * 添加宿舍区信息
     *
     * @param e
     * @return
     */
    public ObjectId addDormitoryEntry(DormitoryEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_OA_DORM, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除一个宿舍区（逻辑删除）
     *
     * @param id
     * @author
     */
    public void deleteDormitoryEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OA_DORM, query, updateValue);
    }

    /**
     * 根据ID跟新一个宿舍区
     *
     * @param id
     * @param dormitoryName
     * @author
     */
    public void updateDormitoryEntry(ObjectId id, String dormitoryName,String remark) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("rmk",remark).append("dnm", dormitoryName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OA_DORM, query, updateValue);
    }

    /**
     * 查询宿舍区
     * @param id
     * @return
     */
    public DormitoryEntry selDormitoryEntry(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id).append("ir",0);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_OA_DORM, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new DormitoryEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 楼层信息
     * @param schoolId
     * @return
     */
    public List<DormitoryEntry> selDormitoryEntryList(ObjectId schoolId) {
        List<DormitoryEntry> retList =new ArrayList<DormitoryEntry>();
        BasicDBObject query =new BasicDBObject("ir",0).append("si",schoolId);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_OA_DORM,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new DormitoryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 添加楼层信息
     *
     * @param e
     * @return
     */
    public ObjectId addLoopEntry(LoopEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_LOOP, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据ID跟新一个楼层信息
     *
     * @param id
     * @param loopName
     * @author
     */
    public void updateLoopEntry(ObjectId id, String loopName,String remark) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("rmk",remark).append("lnm", loopName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_LOOP, query, updateValue);
    }

    /**
     * 删除一个楼层信息（逻辑删除）
     *
     * @param id
     * @author
     */
    public void deleteLoopEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_LOOP, query, updateValue);
    }

    /**
     * 楼层信息
     * @param dormId
     * @return
     */
    public List<LoopEntry> selLoopEntryList(ObjectId dormId) {
        List<LoopEntry> retList =new ArrayList<LoopEntry>();
        BasicDBObject query =new BasicDBObject("ir",0).append("did",dormId);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_LOOP,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new LoopEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 查询楼层信息
     * @param id
     * @return
     */
    public LoopEntry selLoopEntry(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id).append("ir",0);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_LOOP, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new LoopEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 添加房间信息
     *
     * @param e
     * @return
     */
    public ObjectId addRoomEntry(RoomEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除一个房间信息（逻辑删除）
     *
     * @param id
     * @author
     */
    public void deleteRoomEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM, query, updateValue);
    }

    /**
     * 根据ID跟新一个房间信息
     *
     * @param id
     * @param roomName
     * @author
     */
    public void updateRoomEntry(ObjectId id, String roomName,int bedNum,int roomType,String remark,List<BasicDBObject> idNameValuePairlist) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("rmk",remark).append("rnm", roomName).append("bum",bedNum).append("rtp",roomType).append("beds", idNameValuePairlist));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM, query, updateValue);
    }

    /**
     * 房间信息
     * @param loopId
     * @return
     */
    public List<RoomEntry> selRoomEntryList(ObjectId loopId,int sex) {
        List<RoomEntry> retList =new ArrayList<RoomEntry>();
        BasicDBObject query =new BasicDBObject("ir",0).append("lid",loopId);
        if (sex!=2) {
            query.append("rtp",sex);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new RoomEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 查询房间信息
     * @param id
     * @return
     */
    public RoomEntry selRoomEntry(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id).append("ir",0);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new RoomEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 通过性别查询房间
     * @param loopIds
     * @param sex
     * @return
     */
    public List<RoomEntry> selRoomEntryListBySex(List<ObjectId> loopIds, int sex) {
        List<RoomEntry> retList =new ArrayList<RoomEntry>();
        BasicDBObject query =new BasicDBObject("ir",0).append("lid",new BasicDBObject(Constant.MONGO_IN,loopIds));
        if(sex!=2) {
            query.append("rtp",sex);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new RoomEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 通过区域Ids获取楼层
     * @param dormIds
     * @return
     */
    public List<LoopEntry> selLoopInfoListByDormIds(List<ObjectId> dormIds) {
        List<LoopEntry> retList =new ArrayList<LoopEntry>();
        BasicDBObject query =new BasicDBObject("ir",0).append("did",new BasicDBObject(Constant.MONGO_IN,dormIds));
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_LOOP,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new LoopEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 人员入住
     * @param roomId
     * @param roomUserEntry
     */
    public void addRoomUserInfo(ObjectId roomId,int cnt, RoomUserEntry roomUserEntry) {
        DBObject query = new BasicDBObject(Constant.ID, roomId).append("beds.bnum",roomUserEntry.getBedNum());
        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_SET, new BasicDBObject("ocynum", cnt).append("beds.$.unm", roomUserEntry.getUserName()).append("beds.$.ui", roomUserEntry.getUserId()).append("beds.$.dt", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM, query, update);
    }

    /**
     * 迁出表
     * @param e
     * @return
     */
    public ObjectId addMoveLogEntry(MoveLogEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_MOVE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 跟新宿舍人员
     * @param roomId
     * @param pair
     */
    public void updateRoomUser(ObjectId roomId, RoomUserEntry pair,int cnt) {
        DBObject query = new BasicDBObject(Constant.ID, roomId).append("beds.bnum",pair.getBedNum());
        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_SET, new BasicDBObject("ocynum", cnt).append("beds.$.unm", pair.getUserName()).append("beds.$.ui", pair.getUserId()).append("beds.$.dt", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM, query, update);
    }

    /**
     *获取迁出列表
     * @param roomIds
     * @param userName
     * @param page
     * @param pageSize
     * @return
     */
    public List<MoveLogEntry> selMoveUserList(List<ObjectId> roomIds, String userName, int page, int pageSize) {
        List<MoveLogEntry> retList =new ArrayList<MoveLogEntry>();
        BasicDBObject query =new BasicDBObject("rid",new BasicDBObject(Constant.MONGO_IN,roomIds));
        if (!StringUtils.isEmpty(userName)) {
            query.append("unm",MongoUtils.buildRegex(userName));
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_MOVE, query, null, new BasicDBObject("_id",-1),page,pageSize);
        for(DBObject dbo:list)
        {
            retList.add(new MoveLogEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 获取迁出数量
     * @param roomIds
     * @param userName
     * @return
     */
    public int selMoveUserCount(List<ObjectId> roomIds, String userName) {
        BasicDBObject query =new BasicDBObject("rid",new BasicDBObject(Constant.MONGO_IN,roomIds));
        if (!StringUtils.isEmpty(userName)) {
            query.append("unm",MongoUtils.buildRegex(userName));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_MOVE, query);
    }

    /**
     * 房间人员查询
     * @param roomIds
     * @param userName
     * @return
     */
    public List<RoomEntry> selRoomUserCount(List<ObjectId> roomIds, String userName) {
        List<RoomEntry> retList =new ArrayList<RoomEntry>();
        if (StringUtils.isEmpty(userName)) {
            BasicDBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,roomIds));
            if (!StringUtils.isEmpty(userName)) {
                query.append("beds.unm",MongoUtils.buildRegex(userName));
            }
            List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_ROOM, query, null, new BasicDBObject("_id",-1));
            for(DBObject dbo:list)
            {
                retList.add(new RoomEntry((BasicDBObject)dbo));
            }
        } else {
            DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,roomIds)));
            DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("rnm",1).append("bum", 1).append("beds",1).append("lid",1));
            DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$beds");
            DBObject matchDBO1=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("beds.unm",MongoUtils.buildRegex(userName)));
            List<RoomUserEntry> ruList =new ArrayList<RoomUserEntry>();
            Map<BasicDBObject,RoomUserEntry> map = new HashMap<BasicDBObject,RoomUserEntry>();
            Map<ObjectId,RoomEntry> roomMap = new HashMap<ObjectId, RoomEntry>();
            AggregationOutput output;
            try {
                output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_ROOM,matchDBO,projectDBO,unbindDBO,matchDBO1);
                Iterator<DBObject> iter=output.results().iterator();
                BasicDBObject roomEntry;
                BasicDBObject bedInfos;
                while(iter.hasNext()) {
                    roomEntry=(BasicDBObject)iter.next();
                    bedInfos=(BasicDBObject)roomEntry.get("beds");
                    ruList =new ArrayList<RoomUserEntry>();
                    ruList.add(new RoomUserEntry(bedInfos));
                    retList.add(new RoomEntry((String)roomEntry.get("rnm"),(Integer)roomEntry.get("bum"),new ObjectId(roomEntry.get("lid").toString()),1,"",ruList));
                }


            } catch (Exception e) {
            }

        }
        return retList;
    }
}
