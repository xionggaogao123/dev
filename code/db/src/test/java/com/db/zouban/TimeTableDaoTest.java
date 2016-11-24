package com.db.zouban;

import com.pojo.zouban.CourseItem;
import com.pojo.zouban.TimeTableEntry;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TimeTableDaoTest {

    private TimeTableDao timeTableDao;

    @Before
    public void setUp() throws Exception {
        timeTableDao = new TimeTableDao();
    }

    @Test
    public void testFindTimeTable() throws Exception {
        TimeTableEntry timeTableEntry=timeTableDao.findTimeTable("2015-2016学年第一学期", new ObjectId("55fb9f1674d67d3724e6bab4"), 0,0);
        System.out.println(timeTableEntry);
    }

    @Test
    public void testAddTimeTable() throws Exception {
        List<CourseItem> courseItemList = new ArrayList<CourseItem>();
        CourseItem courseItem = new CourseItem(new ObjectId(), 1, 1, new ArrayList<ObjectId>() {{
            add(new ObjectId());
        }},0);
        courseItemList.add(courseItem);
        TimeTableEntry timeTableEntry = new TimeTableEntry("2015-2016学年第一学期", new ObjectId(),new ObjectId(),new ObjectId(), courseItemList, 1,0,0);
        timeTableDao.addTimeTable(timeTableEntry);
    }

    @Test
    public void testDeleteTimeTable() throws Exception {
        //timeTableDao.deleteTimeTable("2015-2016学年第一学期", new ObjectId("55fb9f1674d67d3724e6bab4"), 1);
    }

    @Test
    public void testCountTimeTable() throws Exception {
        System.out.println(String.valueOf(1));
        System.out.println(String.valueOf(true));
        //System.out.println(timeTableDao.countTimeTable("2015-2016学年第一学期", new ObjectId("55fb9f1674d67d3724e6bab4"), 1));
    }
}