package com.fulaan.homework;


import com.fulaan.examresult.controller.ExamResultController;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.homework.controller.HomeWorkController;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fl on 2015/8/17.
 */
public class HomeworkControllerTest {
    ObjectId teacherId = new ObjectId("55934c26f6f28b7261c1baf1");
    private HomeWorkController homeWorkController = new HomeWorkController();
    private ExamResultController examResultController = new ExamResultController();

    @Test
    public void getTCS() {
        Map<String, Object> model = new HashMap<String, Object>();
        examResultController.getExamSelection();
//        homeWorkController.teacher(teacherId, model);
//        for (Map.Entry<String, Object> entry : model.entrySet()) {
//            Map<String, Object> map = (Map<String, Object>)entry.getValue();
//            System.out.println("####################################################");
//            for (Map.Entry<String, Object> en : map.entrySet()) {
//                System.out.println("key= " + en.getKey() + " and value= " + en.getValue());
//            }
//        }
    }

    /**
     * 班级课程转成新作业
     */
    @Test
    public void testClassLessonConvertHW(){
//        ObjectId lessonId = new ObjectId("54562c6ff6f28b7261ccac33");
//        homeWorkController.classLessonConvertHW(lessonId);
       // homeWorkController.classLessonConvertHW();
    }

    /**
     * 老作业转成新作业
     */
    @Test
    public void testConvert(){
//        ObjectId homeworkId = new ObjectId("5487e3c8f6f28b7261d12e99");
        homeWorkController.convert();
    }


    /**
     * 更新多媒体字段
     */
    @Test
    public void testUpdateCon(){
        homeWorkController.updateCon();
    }

    @Test
    public void ss(){
        long time = new ObjectId("5487e3c8f6f28b7261d12e99").getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        System.out.println(year + "====" + month);
    }

}
