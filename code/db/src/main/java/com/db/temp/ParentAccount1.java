package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.MD5Utils;

public class ParentAccount1 {

    public static void main(String[] args) throws IOException, IllegalParamException {

        File logFile = new File("/home/parentAccount0422.log");
        File errorFile = new File("/home/errorAccount0422.log");

        logFile.createNewFile();
        errorFile.createNewFile();

        UserDao userDao = new UserDao();
        SchoolDao schoolDao = new SchoolDao();

        int skip = 0;
        int limit = 500;


        while (true) {
            System.out.println("skip=" + skip);
            List<UserEntry> list = userDao.getAllEntrys(skip, limit);

            if (null == list || list.size() == 0) {
                break;
            }

            for (UserEntry u : list) {
                if (UserRole.isStudent(u.getRole())) {
                    try {
                        handleOneStudent(logFile, userDao, schoolDao, u, errorFile);
                    } catch (Exception ex) {
                        FileUtils.write(errorFile, u.getUserName(), true);
                        ;
                        FileUtils.write(errorFile, ex.getMessage(), true);
                        FileUtils.write(errorFile, "\r\n", true);
                    }
                } else if (UserRole.isParent(u.getRole())) {

                } else {
                    MongoFacroty.getAppDB().getCollection("uuser2").insert(u.getBaseEntry());
                }
            }

            skip = skip + 500;
            list = null;
        }
    }

    private static void handleOneStudent(File logFile, UserDao userDao,
                                         SchoolDao schoolDao, UserEntry u, File errorFile) throws IOException {
        SchoolEntry schoolEntry;
        int remove = u.getIsRemove();


        List<ObjectId> studentConnectids = new ArrayList<ObjectId>();
        schoolEntry = schoolDao.getSchoolEntry(u.getSchoolID(), new BasicDBObject("inp", 1));
        List<ObjectId> sunList = new ArrayList<ObjectId>();
        sunList.add(u.getID());

        //爸爸
        ObjectId fatherId = new ObjectId();
        UserEntry father = new UserEntry(u.getUserName() + "爸爸", MD5Utils.getMD5String(schoolEntry.getInitialPassword()), Constant.ONE, null);
        father.setRole(UserRole.PARENT.getRole());
        father.setSchoolID(schoolEntry.getID());
        father.setID(fatherId);
        father.setConnectIds(sunList);
        father.setAvatar("head-" + "default-head.jpg");
        father.setNickName(u.getUserName() + "爸爸");
        father.setIsRemove(remove);
        MongoFacroty.getAppDB().getCollection("uuser2").insert(father.getBaseEntry());


        //妈妈
        ObjectId matherId = new ObjectId();
        UserEntry mather = new UserEntry(u.getUserName() + "妈妈", MD5Utils.getMD5String(schoolEntry.getInitialPassword()), Constant.ZERO, null);
        mather.setRole(UserRole.PARENT.getRole());
        mather.setSchoolID(schoolEntry.getID());
        mather.setID(matherId);
        mather.setConnectIds(sunList);
        mather.setAvatar("head-" + "default-head.jpg");
        mather.setNickName(u.getUserName() + "妈妈");
        mather.setIsRemove(remove);
        MongoFacroty.getAppDB().getCollection("uuser2").insert(mather.getBaseEntry());


        UserEntry parentEntry = userDao.getUserEntryByName(u.getUserName() + "家长");

        if (null == parentEntry) {
            FileUtils.write(logFile, u.getUserName() + "家长没有找到", true);
            ;
            FileUtils.write(logFile, "\r\n", true);
        } else {
            try {
                studentConnectids.add(parentEntry.getID());
                parentEntry.setConnectIds(sunList);
                parentEntry.setIsRemove(remove);
                MongoFacroty.getAppDB().getCollection("uuser2").insert(parentEntry.getBaseEntry());
            } catch (Exception ex) {
                FileUtils.write(errorFile, u.getUserName(), true);
                ;
                FileUtils.write(errorFile, ex.getMessage(), true);
                FileUtils.write(errorFile, "\r\n", true);
            }
        }

        studentConnectids.add(fatherId);
        studentConnectids.add(matherId);

        u.setConnectIds(studentConnectids);
        MongoFacroty.getAppDB().getCollection("uuser2").insert(u.getBaseEntry());
        studentConnectids = null;
        sunList = null;
    }
}
