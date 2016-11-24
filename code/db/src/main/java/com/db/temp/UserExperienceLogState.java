package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.db.user.UserExperienceLogDao;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserExperienceLogEntry;
import com.pojo.user.UserExperienceLogEntry.ExperienceLog;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

/**
 * 统计用户日志
 *
 * @author fourer
 */
public class UserExperienceLogState {


    public static void main(String[] args) throws IOException {

        final File file = new File("/home/update0824.txt");
        file.createNewFile();


        UserExperienceLogDao dao = new UserExperienceLogDao();
        UserDao userDao = new UserDao();
        SchoolDao schoolDao = new SchoolDao();


        long min = DateTimeUtils.stringToDate("2015-07-06", DateTimeUtils.DATE_YYYY_MM_DD).getTime();
        long max = DateTimeUtils.stringToDate("2015-08-21", DateTimeUtils.DATE_YYYY_MM_DD).getTime();

        int skip = 0;
        int limit = 200;


        Map<ObjectId, Integer> uMap = new HashMap<ObjectId, Integer>();
        while (true) {
            List<UserExperienceLogEntry> list = dao.getAllUserExperienceLogEntry(skip, limit);

            if (null == list || list.size() == 0) {
                break;
            }
            for (UserExperienceLogEntry e : list) {
                if (e.getCount() >= 50) {
                    int count = 0;
                    for (ExperienceLog log : e.getList()) {
                        if (log.getTime() >= min && log.getTime() <= max) {
                            count += log.getExperience();
                        }
                    }

                    if (count >= 50) {
                        uMap.put(e.getUserId(), count);
                    }
                }
            }
            skip = skip + 200;
        }

        UserEntry e = null;
        SchoolEntry sch = null;

        for (Map.Entry<ObjectId, Integer> entry : uMap.entrySet()) {
            try {
                e = userDao.getUserEntry(entry.getKey(), Constant.FIELDS);
                sch = schoolDao.getSchoolEntry(e.getSchoolID(), Constant.FIELDS);
                FileUtils.write(file, "用户名：" + e.getUserName() + ";学校名字：" + sch.getName() + ";积分：" + entry.getValue(), true);
                FileUtils.write(file, "\r\n", true);
            } catch (Exception ex) {

            }
        }

    }
}
