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
import com.mongodb.BasicDBObject;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;


public class UserLoginStat {

    public static void main(String[] args) throws IOException {


//		
//		List<String> list7=	FileUtils.readLines(new File("d:\\7月.txt"));
//		
//		Map<ObjectId,Integer> map7=new HashMap<ObjectId, Integer>();
//		
//		for(String s:list7)
//		{
//			if(StringUtils.isNotBlank(s))
//			{
//				String[] arr =s.split(",");
//				map7.put(new ObjectId(arr[0]), Integer.valueOf(arr[2]));
//			}
//		}
//		
//		
//		List<String> list8=	FileUtils.readLines(new File("d:\\8月.txt"));
//
//         Map<ObjectId,Integer> map8=new HashMap<ObjectId, Integer>();
//		
//		for(String s:list8)
//		{
//			if(StringUtils.isNotBlank(s))
//			{
//				String[] arr =s.split(",");
//				map8.put(new ObjectId(arr[0]), Integer.valueOf(arr[2]));
//			}
//		}
//		
//		
//		
//		for(Map.Entry<ObjectId,Integer> entry:map7.entrySet())
//		{
//			if(map8.containsKey(entry.getKey()))
//			{
//				if(map8.get(entry.getKey())<entry.getValue())
//				{
//					System.out.println("数据错误"+entry.getKey());
//				}
//			}
//			else
//			{
//				System.out.println("数据错误"+entry.getKey());
//			}
//		}
//		


        File file = new File("/home/micro160506.txt");
        file.createNewFile();

        Map<ObjectId, Integer> statMap = new HashMap<ObjectId, Integer>();

        int skip = 0;
        int limit = 500;


        UserDao userDao = new UserDao();
        SchoolDao schoolDao = new SchoolDao();

        while (true) {
            System.out.println("skip=" + skip);
            List<UserEntry> list = userDao.getAllEntrys(skip, limit);
            if (null == list || list.size() == 0) {
                break;
            }
            for (UserEntry u : list) {
                if (u.getLastActiveDate() > 0L && null != u.getSchoolID()) {
                    if (!statMap.containsKey(u.getSchoolID())) {
                        statMap.put(u.getSchoolID(), 1);
                    } else {
                        int n = statMap.get(u.getSchoolID());
                        n = n + 1;
                        statMap.put(u.getSchoolID(), n);
                    }
                }
            }
            skip = skip + 500;

            list = null;
        }


        Map<ObjectId, SchoolEntry> schoolmap = schoolDao.getSchoolMap(statMap.keySet(), new BasicDBObject("nm", 1));

        for (Map.Entry<ObjectId, SchoolEntry> entry : schoolmap.entrySet()) {
            FileUtils.write(file, "\r\n", true);
            FileUtils.write(file, entry.getKey().toString() + "," + entry.getValue().getName() + "," + statMap.get(entry.getKey()), true);
        }


        /**
         List<String> list =new ArrayList<String>();

         File f =new File("C:\\Users\\fourer\\Desktop\\学校logo\\totlogin.log");
         f.createNewFile();
         File dir1 =new File("C:\\Users\\fourer\\Desktop\\学校logo\\1");
         File dir2 =new File("C:\\Users\\fourer\\Desktop\\学校logo\\2");


         for(File f1:dir1.listFiles())
         {
         FileUtils.writeLines(f, FileUtils.readLines(f1), true);
         }
         for(File f1:dir2.listFiles())
         {
         FileUtils.writeLines(f, FileUtils.readLines(f1), true);
         }
         **/


        /**
         File f =new File("C:\\Users\\fourer\\Desktop\\学校logo\\totlogin.log");
         List<String> list =FileUtils.readLines(f);

         Map<String,Integer> ccMap =new HashMap<String, Integer>();
         for(String ss:list)
         {
         int a=ss.indexOf("schoolName=");
         int b=ss.indexOf(", userId");

         String cc=	ss.substring(a+11, b);


         if(ccMap.containsKey(cc))
         {
         int ccCount=ccMap.get(cc);
         ccMap.put(cc, ccCount+1);
         }
         else
         {
         ccMap.put(cc, 1);
         }

         }

         int count=0;
         for(Map.Entry<String,Integer> entry:ccMap.entrySet())
         {
         count=count+entry.getValue();
         System.out.println(entry.getKey()+":"+entry.getValue());
         }

         System.out.println(count);
         **/
    }
}
