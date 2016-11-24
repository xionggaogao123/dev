package com.db.temp;

import java.util.List;


import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import com.sys.exceptions.IllegalParamException;

public class UserNameUpdate {

    public static void main(String[] args) {

        UserDao dao = new UserDao();
        int skip = 0;
        int limit = 200;

        while (true) {
            List<UserEntry> users = dao.getAllEntrys(skip, limit);

            if (null == users || users.isEmpty()) {
                break;
            }
            for (UserEntry ue : users) {
                try {
                    dao.update(ue.getID(), "nm", ue.getUserName().toLowerCase(), false);
                } catch (IllegalParamException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            skip = skip + 200;
        }
    }
}
