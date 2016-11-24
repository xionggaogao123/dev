package com.db.temp;

import java.util.List;

import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;

public class StudentDeleteUtils {

    public static void main(String[] args) {

        int skip = 0;
        int limit = 1000;

        UserDao userDao = new UserDao();

        ClassDao classDao = new ClassDao();

        while (true) {
            System.out.println("skip=" + skip);
            List<UserEntry> list = userDao.getAllEntrys(skip, limit);
            if (null == list || list.size() == 0) {
                break;
            }
            for (UserEntry u : list) {
                if (u.getRole() == UserRole.STUDENT.getRole() && u.getIsRemove() == 1) {
                    classDao.deleteStu(u.getID());
                }
            }
            skip = skip + 1000;

            list = null;
        }
    }
}
