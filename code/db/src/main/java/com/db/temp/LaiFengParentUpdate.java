package com.db.temp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;


public class LaiFengParentUpdate {

    public static void main(String[] args) {

        UserDao dao = new UserDao();

        Map<String, List<ObjectId>> nameIdMap = new HashMap<String, List<ObjectId>>();

        List<UserEntry> userList = dao.getUserEntryBySchoolId(new ObjectId("56dea9e60cf29b1476e63e35"), Arrays.asList(4), new BasicDBObject("nm", 1), 0, 20000);

        for (UserEntry ue : userList) {
            if (!nameIdMap.containsKey(ue.getUserName())) {
                nameIdMap.put(ue.getUserName(), new ArrayList<ObjectId>());
            }
            nameIdMap.get(ue.getUserName()).add(ue.getID());
        }


        for (Map.Entry<String, List<ObjectId>> entry : nameIdMap.entrySet()) {


            if (entry.getValue().size() == 1) {
                System.out.println("single parent:" + entry.getKey());
            } else if (entry.getValue().size() == 2) {
                UserEntry u1 = dao.getUserEntry(entry.getValue().get(0), Constant.FIELDS);

                UserEntry u2 = dao.getUserEntry(entry.getValue().get(1), Constant.FIELDS);

                if (u1.getIsRemove() == 0 && u2.getIsRemove() == 0) {
                    ObjectId smallerId = u1.getID();

                    if (u2.getID().compareTo(u1.getID()) < 0) {
                        smallerId = u2.getID();
                    }
                    dao.logicRemoveUser(smallerId);
                }
            } else {
                System.out.println("multi parents:" + entry.getKey());
            }
        }


    }
}
