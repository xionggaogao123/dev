package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import com.sys.exceptions.IllegalParamException;

public class UserScoreUpdate {

    public static void main(String[] args) throws IOException, IllegalParamException {
        UserDao dao = new UserDao();

        List<String> list = FileUtils.readLines(new File("/home/stuts1.txt"));
        System.out.println("共有学生：" + list.size());
        for (String name : list) {
            UserEntry ue = dao.searchUserByUserName(name);
            if (null != ue) {
                try {
                    System.out.println(ue.getUserName() + "" + ue.getExperiencevalue() + " ");
                    int n = ue.getExperiencevalue() + 20;
                    dao.update(ue.getID(), "exp", n, false);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
