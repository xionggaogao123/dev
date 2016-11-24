package com.db.temp;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.db.school.HomeWorkDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.HomeWorkEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fl on 2016/1/13.
 */
public class CountHomeworkInfo extends BaseDao{
    private UserDao userDao = new UserDao();
    private SchoolDao schoolDao = new SchoolDao();

    private void count(){
        Date date1 = DateTimeUtils.stringToDate("2015-12-01 00:00:00", DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        ObjectId begin = new ObjectId(date1);
        Date date2 = DateTimeUtils.stringToDate("2016-01-01 00:00:00", DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        ObjectId end = new ObjectId(date2);
        List<HomeWorkEntry> homeWorkEntryList = findHomeWorkEntrysB(begin, end);
        for(HomeWorkEntry entry : homeWorkEntryList){
            ObjectId teacherId = entry.getTeacherId();
            UserEntry userEntry=userDao.getUserEntry(teacherId, Constant.FIELDS);
            ObjectId schoolId = userEntry.getSchoolID();
            SchoolEntry schoolEntry=schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
            long time = entry.getID().getTime();
            String date = DateTimeUtils.convert(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
            System.out.println(schoolEntry.getName()+"\t" + userEntry.getUserName()+"\t"+entry.getName()+"\t"+ date +"\t" +entry.getSubmitList().size());
        }
//        System.out.println(homeWorkEntryList.size());

    }

    public List<HomeWorkEntry> findHomeWorkEntrysB(ObjectId begin, ObjectId end)
    {
        List<HomeWorkEntry> retList = new ArrayList<HomeWorkEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject("$gte", begin).append("$lt", end));
        List<DBObject> list = find(MongoFacroty.getAppDB(),Constant.COLLECTION_HOMEWORK_NAME, query,Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            HomeWorkEntry e;
            for (DBObject dbo : list)
            {
                e = new HomeWorkEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }

    public static void main(String[] args){
        new CountHomeworkInfo().count();
    }
}
