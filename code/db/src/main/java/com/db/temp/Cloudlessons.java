package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.db.cloudlesson.CloudLessonDao;
import com.db.video.VideoDao;
import com.google.common.base.Joiner;
import com.pojo.cloudlesson.CloudLessonEntry;
import com.pojo.school.SubjectType;
import com.pojo.video.VideoEntry;

public class Cloudlessons {

    public static void main(String[] args) throws IOException {


        /**
         final File file = new File("/home/cloudlesson1027.txt");
         file.createNewFile();



         CloudLessonDao dao =new CloudLessonDao();
         VideoDao videoDao =new VideoDao();

         int skip=0;
         int limit=200;

         while(true)
         {
         System.out.println("limit="+limit);
         List<CloudLessonEntry> CloudLessonEntryList=dao.getCloudLessonEntry(skip, limit);

         if(null==CloudLessonEntryList || CloudLessonEntryList.isEmpty())
         {
         break;
         }
         for(CloudLessonEntry se:CloudLessonEntryList)
         {
         VideoEntry ve= videoDao.getVideoEntryById(se.getVideoIds().get(0));

         try
         {
         String s="";
         if(null!=ve)
         {
         s+=ve.getID().toString()+",";
         s+=ve.getName()+",";


         try
         {
         List<Integer> list= se.getCloudClassGradeTypes();

         s+= Joiner.on("|").join(list)+",";
         }catch(Exception ex1)
         {

         }


         try
         {
         s+=SubjectType.getSubjectType(se.getSubject()).getName();
         }catch(Exception ex2)
         {

         }

         FileUtils.write(file, s, true);
         FileUtils.write(file, "\r\n", true);
         }
         }catch(Exception ex)
         {

         }



         }
         skip=skip+200;
         }
         **/
    }
}
