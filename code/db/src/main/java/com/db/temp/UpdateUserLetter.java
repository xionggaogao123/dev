package com.db.temp;


import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.user.UserEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/10/20.
 */
public class UpdateUserLetter {

    private static UserDao userDao = new UserDao();

    private void update(){
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdListByLimit(new ObjectId("57f8b0bf71f056532ba3abf1"), new BasicDBObject("nm", 1));
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry user : userEntryList) {
                if (!StringUtils.isEmpty(user.getUserName())) {
                    userDao.update(user.getID(),new FieldValuePair("lt",PinYin2Abbreviation.cn2py(user.getUserName())));
                }
            }
            if (userEntryList.size()>=200) {
                update();
            }
        }

    }

    public static void main(String[] args){
        new UpdateUserLetter().update();
    }
}
