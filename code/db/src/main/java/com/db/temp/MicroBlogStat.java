package com.db.temp;

import com.db.microblog.MicroBlogDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MicroBlogStat {


    public static void main(String[] args) throws IOException {

        MicroBlogDao microBlogDao = new MicroBlogDao();
        UserDao userDao = new UserDao();
        SchoolDao schoolDao = new SchoolDao();

        File file = new File("/home/micro0910a.txt");
        file.createNewFile();
        List<MicroBlogEntry> jiawulist = microBlogDao.getMicroBlogEntryList("手拉手晒友谊");
        List<MicroBlogEntry> yundonglist = microBlogDao.getMicroBlogEntryList("温情陪伴");

        Set<ObjectId> userSet = new HashSet<ObjectId>();
        Set<ObjectId> schollSet = new HashSet<ObjectId>();
        for (MicroBlogEntry me : jiawulist) {
            userSet.add(me.getUserId());
            schollSet.add(me.getSchoolID());
        }
//		for(MicroBlogEntry me:yundonglist)
//		{
//			userSet.add(me.getUserId());
//			schollSet.add(me.getSchoolID());
//		}

        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(userSet, Constant.FIELDS);
        Map<ObjectId, SchoolEntry> schollMap = schoolDao.getSchoolMap(schollSet, Constant.FIELDS);

        // FileUtils.write(file, "", true);
        FileUtils.write(file, "**********手拉手晒友谊begin*********", true);
        FileUtils.write(file, "\r\n", true);
        FileUtils.write(file, "数量：" + String.valueOf(jiawulist.size()), true);
        FileUtils.write(file, "\r\n", true);
//		 FileUtils.write(file, "**********我家的一角begin*********",true);
//		 FileUtils.write(file, "\r\n",true);
//		 FileUtils.write(file, "数量："+ String.valueOf(jiawulist.size()),true);
//		 FileUtils.write(file, "\r\n",true);
//		 

        Set<ObjectId> uset = new HashSet<ObjectId>();
        long min = DateTimeUtils.stringToDate("2015-09-10", DateTimeUtils.DATE_YYYY_MM_DD).getTime();
        long max = DateTimeUtils.stringToDate("2015-09-15", DateTimeUtils.DATE_YYYY_MM_DD).getTime();
        for (MicroBlogEntry me : jiawulist) {
//				String text="用户名：" +userMap.get(me.getUserId()).getUserName()+";";
//				text+="学校名："+schollMap.get(me.getSchoolID()).getName()+";";
//				text+="内容："+me.getContent()+";";
//				text+="赞的数量："+me.getZanCount()+";";
//				
            //FileUtils.write(file, text,true);
            //FileUtils.write(file, "\r\n",true);

            if (me.getID().getTime() >= min && me.getID().getTime() <= max) {
                if (!uset.contains(me.getUserId())) {
                    int n = userMap.get(me.getUserId()).getExperiencevalue() + 30;
                    try {
                        userDao.update(me.getUserId(), "exp", n, false);
                    } catch (IllegalParamException e) {
                    }
                    uset.add(me.getUserId());

                    String text = "用户名：" + userMap.get(me.getUserId()).getUserName() + ";";
                    FileUtils.write(file, text, true);
                    FileUtils.write(file, "\r\n", true);
                }
            }


        }

        FileUtils.write(file, "**********手拉手晒友谊end*********", true);
        FileUtils.write(file, "\r\n", true);


        FileUtils.write(file, "**********温情陪伴begin*********", true);
        FileUtils.write(file, "\r\n", true);
        FileUtils.write(file, "数量：" + String.valueOf(yundonglist.size()), true);
        FileUtils.write(file, "\r\n", true);

        for (MicroBlogEntry me : yundonglist) {
            String text = "用户名：" + userMap.get(me.getUserId()).getUserName() + ";";
            text += "学校名：" + schollMap.get(me.getSchoolID()).getName() + ";";
            text += "内容：" + me.getContent() + ";";
            text += "赞的数量：" + me.getZanCount() + ";";
            FileUtils.write(file, text, true);
            FileUtils.write(file, "\r\n", true);
        }
        FileUtils.write(file, "**********温情陪伴end*********", true);
        FileUtils.write(file, "\r\n", true);


        // FileUtils.write(file, "**********我家的一角end*********",true);
        // FileUtils.write(file, "\r\n",true);


//		 FileUtils.write(file, "**********学习成果begin*********",true);
//		 FileUtils.write(file, "\r\n",true);
//		 FileUtils.write(file, "数量："+ String.valueOf(yundonglist.size()),true);
//		 FileUtils.write(file, "\r\n",true);
//		
//		 for(MicroBlogEntry me:yundonglist)
//		 {
//				String text="用户名：" +userMap.get(me.getUserId()).getUserName()+";";
//				text+="学校名："+schollMap.get(me.getSchoolID()).getName()+";";
//				text+="内容："+me.getContent()+";";
//				text+="赞的数量："+me.getZanCount()+";";
//				FileUtils.write(file, text,true);
//				FileUtils.write(file, "\r\n",true);
//		 }
//		 FileUtils.write(file, "**********学习成果end*********",true);
//		 FileUtils.write(file, "\r\n",true);
//		
    }
}
