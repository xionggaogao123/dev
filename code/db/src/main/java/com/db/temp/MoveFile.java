package com.db.temp;

import com.db.lesson.LessonDao;
import com.db.school.HomeWorkDao;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdNameValuePair;
import com.pojo.lesson.LessonWare;
import com.pojo.school.HomeWorkEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/9/16.
 */
public class MoveFile {
    public static void main(String[] args) {
        //查找所有作业
        HomeWorkDao homeWorkService = new HomeWorkDao();
        LessonDao lessonService = new LessonDao();
        List<HomeWorkEntry> homeWorkEntryList = homeWorkService.findAllHomeworkEntry();
        if (homeWorkEntryList != null && homeWorkEntryList.size() > 0) {
            long size = homeWorkEntryList.size();
            long i = 0;
            for (HomeWorkEntry homeWorkEntry : homeWorkEntryList) {
                i++;
                System.out.println("==============================" + i + "/" + size);
                //把作业的附件迁移到课程的课件中去
                List<IdNameValuePair> docFiles = homeWorkEntry.getDocFile();
                List<LessonWare> lessonWareList = new ArrayList<LessonWare>();
                if (docFiles != null && docFiles.size() > 0) {
                    for (IdNameValuePair idNameValuePair : docFiles) {
                        String fileName = idNameValuePair.getName();
                        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);//得到后缀
                        LessonWare lessonWare = new LessonWare(prefix, fileName, (String) idNameValuePair.getValue());
                        lessonWareList.add(lessonWare);
                    }
                    ObjectId lessonId = homeWorkEntry.getLessonId();
                    List<ObjectId> lessonList = new ArrayList<ObjectId>();
                    lessonList.add(lessonId);
                    List<DBObject> list = MongoUtils.fetchDBObjectList(lessonWareList);
                    FieldValuePair fieldValuePair1 = new FieldValuePair("dcl", MongoUtils.convert(list));
                    FieldValuePair fieldValuePair2 = new FieldValuePair("dc", lessonWareList.size());
                    lessonService.update(lessonList, fieldValuePair1, fieldValuePair2);
                    System.out.println("迁移成功 homeworkId=" + homeWorkEntry.getID().toString());
                } else {
                    System.out.println("没有附件 homeworkId=" + homeWorkEntry.getID().toString());
//                    continue;
                }
            }
        } else {
            System.out.println("没查到作业");
        }
    }
}
