package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;

/**
 * 更新用户最后登录时间
 *
 * @author fourer
 */
public class LastActiveDateUpdate {

    public static void main(String[] args) throws IOException {


        File f = new File("/home/aaaa.txt");
        File errorFile = new File("/home/aaaa_err.txt");
        errorFile.createNewFile();
        File statFile = new File("/home/aaaa_stat.txt");
        statFile.createNewFile();
        UserDao dao = new UserDao();
        SchoolDao schoolDao = new SchoolDao();
        List<String> strs = null;
        try {
            strs = FileUtils.readLines(f, "utf-8");
        } catch (IOException e) {
            FileUtils.write(errorFile, "文件不存在", true);
            FileUtils.write(errorFile, "\r\n", true);
            return;
        }

        for (String s : strs) {
            FieldValuePair fvp = new FieldValuePair("lad", System.currentTimeMillis());
            if (null != s && ObjectId.isValid(s)) {
                UserEntry e = dao.getUserEntry(new ObjectId(s), new BasicDBObject("_id", 1));
                if (null != e) {
                    dao.update(new ObjectId(s), fvp);
                } else {
                    FileUtils.write(errorFile, s + "不存在", true);
                    FileUtils.write(errorFile, "\r\n", true);
                }
            } else {
                UserEntry e = dao.findByUserName(s);
                if (null == e) {
                    FileUtils.write(errorFile, s + "不存在", true);
                    FileUtils.write(errorFile, "\r\n", true);
                } else {
                    dao.update(e.getID(), fvp);
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }


        int skip = 0;
        int limit = 200;

        while (true) {
            List<SchoolEntry> schoolList = schoolDao.getSchoolEntry(skip, limit);

            if (null == schoolList || schoolList.isEmpty()) {
                break;
            }

            for (SchoolEntry se : schoolList) {
                try {
                    List<UserEntry> list = dao.getUserEntryBySchoolIdList(se.getID(), new BasicDBObject("lad", 1));

                    int total = 0;
                    for (UserEntry e : list) {
                        if (e.getLastActiveDate() > 100L) {
                            total = total + 1;
                        }
                    }


                    FileUtils.write(statFile, se.getID().toString() + "," + se.getName() + "," + total, true);
                    FileUtils.write(statFile, "\r\n", true);
                    list = null;

                    Thread.sleep(1);

                } catch (Exception ex) {
                    FileUtils.write(errorFile, ex.getMessage(), true);
                    FileUtils.write(errorFile, "\r\n", true);
                }
            }
            skip = skip + 200;
        }


    }
}
