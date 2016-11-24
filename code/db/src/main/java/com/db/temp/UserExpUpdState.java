package com.db.temp;

import com.db.factory.MongoFacroty;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class UserExpUpdState {

    public static void main(String[] args) {

        UserDao dao = new UserDao();

        int skip = 0;
        int limit = 500;

        for (int i = 0; i < 10000; i++) {
            skip = i * 500;
            System.out.println("skip=" + skip);
            List<DBObject> exps = getUserExps(skip, limit);

            if (exps == null || exps.size() == 0) {
                break;
            }

            List<ObjectId> userIds = new ArrayList<ObjectId>();

            Map<ObjectId, Integer> map = new HashMap<ObjectId, Integer>();

            for (DBObject o : exps) {
                ObjectId id = (ObjectId) o.get("_id");
                int exp = (Integer) o.get("exp");
                map.put(id, exp);
                userIds.add(id);
            }
            System.out.println("########################@@@@@@@@@@@@@@@@11");
            Map<ObjectId, UserEntry> oldUsers = getUserOldExps(userIds, new BasicDBObject("_id", 1).append("exp", 1));
            List<UserEntry> newUsers = dao.getUserEntryList(userIds, new BasicDBObject("_id", 1).append("exp", 1));

            //处理没有导入的父母
            for (UserEntry entry : newUsers) {
                UserEntry userEntry = oldUsers.get(entry.getID());
                Integer exp1 = map.get(entry.getID());
                Integer exp3 = entry.getExperiencevalue();//==2*exp2
                //Integer exp2=entry.getExperiencevalue();

                System.out.println("########################@@@@@@@@@@@@@@@@2222222");
                if (exp1 != null && exp3 != null) {
                    int exp = exp1 - userEntry.getExperiencevalue();
                    if (exp != 0) {
                        int exp2 = (exp3 - exp) / 2;
                        System.out.println("########################@@@@@@@@@@@@@@@@3333");
                        dao.updateExperenceValue(entry.getID(), -exp2);
                    }
                }
            }
        }
    }

    public static List<DBObject> getUserExps(int skip, int limit) {
        BasicDBObject query = new BasicDBObject("exp", new BasicDBObject(Constant.MONGO_GT, 0));
        List<DBObject> list = MongoFacroty.getAppDB().getCollection("uuser").find(query, new BasicDBObject("_id", 1).append("exp", 1)).sort(Constant.MONGO_SORTBY_DESC).skip(skip).limit(limit).toArray();
        System.out.println("########################@@@@@@@@@@@@@@@@44444");
        return list;
    }

    public static Map<ObjectId, UserEntry> getUserOldExps(Collection<ObjectId> ids, DBObject fields) {
        Map<ObjectId, UserEntry> map = new HashMap<ObjectId, UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("ir", Constant.ZERO);
        List<DBObject> list = MongoFacroty.getAppDB().getCollection("users").find(query, fields).sort(Constant.MONGO_SORTBY_DESC).toArray();
        System.out.println("########################@@@@@@@@@@@@@@@@5555");
        if (null != list && !list.isEmpty()) {
            UserEntry userEntry;
            for (DBObject dbo : list) {
                userEntry = new UserEntry((BasicDBObject) dbo);
                map.put(userEntry.getID(), userEntry);
            }
        }
        return map;
    }

}
