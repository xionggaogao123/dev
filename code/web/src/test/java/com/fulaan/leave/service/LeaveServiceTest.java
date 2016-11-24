package com.fulaan.leave.service;

import com.fulaan.leave.dto.LeaveDTO;
import com.fulaan.leave.dto.TeacherInfo;
import com.fulaan.zouban.dto.CourseConfDTO;
import com.pojo.app.IdNameValuePairDTO;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LeaveServiceTest {
    private LeaveService leaveService=new LeaveService();

    @Test
    public void testAddTeacherLeave() throws Exception {
        leaveService.addTeacherLeave("55934c14f6f28b7261c19c62","55934c26f6f28b7261c1baae","世界那麼大，我想出去看看","我要請假！！！","2016-3-2","2016-11-11",21);
    }

    @Test
    public void testFindTeacherLeaveList() throws Exception {
        List<LeaveDTO> leaveDTOList=leaveService.findMyLeaveList("55934c26f6f28b7261c1baae", 1, 10);
        for (LeaveDTO leaveDTO:leaveDTOList)
        {
            System.out.println(leaveDTO.getId());
        }
    }

    @Test
    public void testFindLeaveById() throws Exception {
        LeaveDTO leaveDTO=leaveService.findLeaveById("56d6896f7448d15bf2e512e5");
        System.out.println(leaveDTO.getId());
    }
    @Test
    public void testConvert()
    {
        System.out.println(converLongToDate(leaveService.converDayToDate(0).get("dt1")));
        System.out.println(converLongToDate(leaveService.converDayToDate(0).get("dt2")));

        System.out.println(converLongToDate(leaveService.converDayToDate(1).get("dt1")));
        System.out.println(converLongToDate(leaveService.converDayToDate(1).get("dt2")));

        System.out.println(converLongToDate(leaveService.converDayToDate(2).get("dt1")));
        System.out.println(converLongToDate(leaveService.converDayToDate(2).get("dt2")));

        System.out.println(converLongToDate(leaveService.converDayToDate(3).get("dt1")));
        System.out.println(converLongToDate(leaveService.converDayToDate(3).get("dt2")));

        System.out.println(converLongToDate(leaveService.converDayToDate(4).get("dt1")));
        System.out.println(converLongToDate(leaveService.converDayToDate(4).get("dt2")));
    }
    public String converLongToDate(long timeStart)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeStart);
        return  sdf.format(date);
    }

    @Test
    public void testcalClass()
    {
        Map<String,Object> map=leaveService.calClass("55934c14f6f28b7261c19c62","2016-03-07","2016-04-11","55934c26f6f28b7261c1baae");
        System.out.println(map);
    }

    @Test
    public void testcalClass1()
    {
       CourseConfDTO map=leaveService.getCourseConf("55934c14f6f28b7261c19c62");
        System.out.println(map);
    }

    @Test
    public void testgetAvailableTeacherList()
    {
        List<TeacherInfo> list=leaveService.getAvailableTeacherList("55934c14f6f28b7261c19c62", 1, 1, 1, "语文");
        System.out.println(list);
    }
}