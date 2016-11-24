package com.db.temp;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.db.user.UserDao;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

public class ParentAccount2 {

    public static void main(String[] args) {

        UserDao dao = new UserDao();


        int skip = 0;
        int limit = 500;

        for (int i = 0; i < 1000; i++) {

            skip = i * 500;
            System.out.println("+++++++++++++skip=" + skip);
            List<DBObject> oldParents = getOldParents(skip, limit);
            System.out.println("1");

            if (oldParents == null || oldParents.size() == 0) {
                break;
            }

            List<ObjectId> oldParentIds = getIds(oldParents);
            System.out.println("2");
            List<UserEntry> oldParentUsers = dao.getUserEntryList(oldParentIds, new BasicDBObject("_id", 1));

            System.out.println("3");

            if (oldParentUsers.size() == oldParentIds.size()) {
                continue;
            }


            {
                for (UserEntry ue : oldParentUsers) {
                    oldParentIds.remove(ue.getID());
                }
            }

            //处理没有导入的父母
            for (ObjectId oldParentId : oldParentIds) {
                try {
                    handleOneOldParent(dao, oldParentId);
                } catch (Exception ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }


        }
    }


    private static void handleOneOldParent(UserDao dao, ObjectId oldParentId) throws IllegalParamException {

        DBObject oldParent = getParentsDetail(oldParentId);

        ObjectId sunId = (ObjectId) oldParent.get("cid");
        UserEntry sunEntry = dao.getUserEntry(sunId, Constant.FIELDS);

        if (null != sunEntry && null != oldParent) {
            {
                //更新家长
                BasicDBList list = new BasicDBList();
                list.add(sunId);
                oldParent.put("cid", list);
                UserEntry oldParentEntry = new UserEntry(oldParent);
                dao.addUserEntry(oldParentEntry);

                //更新学生
                BasicDBList parentBDlist = new BasicDBList();
                parentBDlist.add(oldParentId);
                List<ObjectId> parentlist = sunEntry.getConnectIds();

                if (parentlist.size() == 2) {
                    parentBDlist.addAll(parentlist);
                }

                if (parentlist.size() == 3) {
                    parentlist.remove(0);
                    parentBDlist.addAll(parentlist);
                }

                dao.update(sunEntry.getID(), "cid", parentBDlist, false);
            }

        }
    }


    public static List<DBObject> getOldParents(int skip, int limit) {
        BasicDBObject query = new BasicDBObject("r", 4).append("ir", new BasicDBObject(Constant.MONGO_NE, 1));
        List<DBObject> l = MongoFacroty.getAppDB().getCollection("users").find(query, new BasicDBObject("_id", 1).append("cid", 1)).sort(Constant.MONGO_SORTBY_DESC).skip(skip).limit(limit).toArray();
        return l;
    }


    public static DBObject getParentsDetail(ObjectId oldParentId) {
        BasicDBObject query = new BasicDBObject("_id", oldParentId);
        DBObject l = MongoFacroty.getAppDB().getCollection("users").findOne(query, Constant.FIELDS);
        return l;
    }


    public static List<ObjectId> getIds(List<DBObject> list) {
        List<ObjectId> retList = new ArrayList<ObjectId>();

        for (DBObject o : list) {
            retList.add((ObjectId) o.get("_id"));
        }

        return retList;
    }
}
