package com.db.temp;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.school.HomeWorkEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by qiangm on 2015/10/9.
 */
public class StatisticsMicroBlog extends BaseDao {

    private UserDao userDao = new UserDao();
    private SchoolDao schoolDao = new SchoolDao();

    public static void main(String[] args) {
        new StatisticsMicroBlog().statistics();
    }

    public void statistics() {
        Map<String, Object> stats1 = statisticsMicroBlog1();
        String s1 = "10.16-11.30期间  老师用手机客户端发布微校园状态" + stats1.get("all") + "次\r\n,具体如下:";
        List<ObjectId> teacherIdList = new ArrayList<ObjectId>();
        List<ObjectId> schoolIdList = new ArrayList<ObjectId>();
        for (MicroBlogEntry m : (List<MicroBlogEntry>) stats1.get("detail")) {
            teacherIdList.add(m.getUserId());
            schoolIdList.add(m.getSchoolID());
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(teacherIdList, Constant.FIELDS);
        List<SchoolEntry> schoolEntryList = schoolDao.getSchoolEntryList(schoolIdList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (MicroBlogEntry m : (List<MicroBlogEntry>) stats1.get("detail")) {
            for (UserEntry u : userEntryList) {
                if (u.getID().equals(m.getUserId())) {
                    s1 += "\r\n" + u.getUserName() + "(";
                    break;
                }
            }
            for (SchoolEntry s : schoolEntryList) {
                if (s.getID().equals(m.getSchoolID())) {
                    s1 += s.getName() + ")";
                    break;
                }
            }
            s1 += "[发布内容]:" + m.getContent() + "[发表时间]：" + sdf.format(new Date(m.getID().getTime()));
        }

        Map<String, Object> s2 = new StatisticsMicroBlog().statistics2();
        s1 += "\r\n\r\n\r\n10.16-11.30期间，教师发布作业:" + s2.get("publish") + "条;学生提交" + s2.get("submit") + "次\r\n具体如下：";
        List<HomeWorkEntry> homeWorkEntryList = (List<HomeWorkEntry>) s2.get("detail");
        List<ObjectId> teacherIdList2 = new ArrayList<ObjectId>();
        List<ObjectId> schoolIdList2 = new ArrayList<ObjectId>();
        for (HomeWorkEntry h : homeWorkEntryList) {
            teacherIdList2.add(h.getTeacherId());
        }


        List<UserEntry> userEntryList2 = userDao.getUserEntryList(teacherIdList2, Constant.FIELDS);
        for (UserEntry userEntry : userEntryList2) {
            schoolIdList2.add(userEntry.getSchoolID());
        }
        List<SchoolEntry> schoolEntryList2 = schoolDao.getSchoolEntryList(schoolIdList2);
        for (HomeWorkEntry h : homeWorkEntryList) {
            UserEntry teacher = null;
            for (UserEntry u : userEntryList2) {
                if (u.getID().equals(h.getTeacherId())) {
                    teacher = u;
                    s1 += "\r\n" + u.getUserName();
                    break;
                }
            }
            if (teacher != null) {
                for (SchoolEntry s : schoolEntryList2) {
                    if (s.getID().equals(teacher.getSchoolID())) {
                        s1 += s.getName() + "[发布作业]：";
                        break;
                    }
                }
            }
            s1 += h.getContent();
            s1 += "[发布时间]" + sdf.format(new Date(h.getID().getTime()));
            s1 += "\r[学生提交" + h.getSubmitCount() + "次]";
        }
        File file = new File("/home/10.16-11.30统计.txt");
        //File file =new File("C:\\统计.txt");
        try {
            file.createNewFile();
            String text = "统计内容：" + s1;
            FileUtils.write(file, text, false);
            FileUtils.write(file, "\r\n", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 7.10-9.30期间  老师用手机客户端发布微校园状态的统计
     *
     * @return
     */
    public Map<String, Object> statisticsMicroBlog1() {
        String dt1 = "2015/10/16 00:00:00";
        String dt2 = "2015/12/01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt_from = null;
        Date dt_end = null;
        try {
            dt_from = sdf.parse(dt1);
            dt_end = sdf.parse(dt2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //继续转换得到秒数的long型
        long startTime = dt_from.getTime();
        long endTime = dt_end.getTime();
        BasicDBObject query = new BasicDBObject();
        query.append("bt", 1);//教师发布
        query.append("pbt", new BasicDBObject(Constant.MONGO_GTE, startTime).append(Constant.MONGO_LT, endTime));
        query.append("pf", new BasicDBObject(Constant.MONGO_GT, 1));
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, Constant.FIELDS);
        List<MicroBlogEntry> blogEntryList = new ArrayList<MicroBlogEntry>();
        for (DBObject d : list) {
            blogEntryList.add(new MicroBlogEntry((BasicDBObject) d));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("all", count);
        map.put("detail", blogEntryList);
        return map;
    }

    /**
     * 7.10-9.30期间 老师布置作业 学生的回复情况统计
     *
     * @return
     */
    public Map<String, Object> statistics2() {
        String dt1 = "2015/10/16 00:00:00";
        String dt2 = "2015/12/01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt_from = null;
        Date dt_end = null;
        try {
            dt_from = sdf.parse(dt1);
            dt_end = sdf.parse(dt2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //继续转换得到秒数的long型
        long startTime = dt_from.getTime() / 1000;
        long endTime = dt_end.getTime() / 1000;
        int s1_int = (int) startTime;
        int s2_int = (int) endTime;
        String s1 = Integer.toHexString(s1_int) + "f6f28b7261d125f6";//根据时间构造ObjectId
        String s2 = Integer.toHexString(s2_int) + "f6f28b7261d125f6";

        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_GTE, new ObjectId(s1)).append(Constant.MONGO_LT, new ObjectId(s2)));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOMEWORK_NAME, query, Constant.FIELDS);
        int submitCount = 0;
        int publishCount = list.size();
        List<HomeWorkEntry> homeWorkEntryList = new ArrayList<HomeWorkEntry>();
        for (DBObject d : list) {
            HomeWorkEntry h = new HomeWorkEntry((BasicDBObject) d);
            submitCount += h.getSubmitCount();
            homeWorkEntryList.add(h);
            //submitCount+=new HomeWorkEntry((BasicDBObject)d).getSubmitList().size();
        }
        //System.out.println("7.10-9.30期间，教师发布作业:"+publishCount+"条;学生提交"+submitCount+"次");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("publish", publishCount);
        map.put("submit", submitCount);
        map.put("detail", homeWorkEntryList);
        return map;
    }
}
