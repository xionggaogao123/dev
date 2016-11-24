package com.db.zouban;

import com.pojo.zouban.CourseConfEntry;
import com.pojo.zouban.CourseEvent;
import com.pojo.zouban.TeacherEvent;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CourseConfDaoTest {

    private CourseConfDao courseConfDao;

    @Before
    public void setUp() throws Exception {
        courseConfDao = new CourseConfDao();
    }

    @Test
    public void testFindCourseConf() throws Exception {
        CourseConfEntry courseConfEntry = courseConfDao.findCourseConfByGradeId( "2015-2016学年第一学期", new ObjectId("55fa2dc77f965702b7541fbb"));
        System.out.println(courseConfEntry);
    }


    @Test
    public void testDeleteCourseConf() throws Exception {
        courseConfDao.removeCourseConf(new ObjectId("55fa2dc77f965702b7541fba"), "2015-2016学年第一学期",
                new ObjectId("55fa2dc77f965702b7541fbb"));
    }

    @Test
    public void testCountCourseConf() throws Exception {
        int count = courseConfDao.countCourseConf(new ObjectId("55fa2dc77f965702b7541fba"), "2015-2016学年第一学期",
                new ObjectId("55fa2dc77f965702b7541fbb"));
        System.out.println(count);
    }
}