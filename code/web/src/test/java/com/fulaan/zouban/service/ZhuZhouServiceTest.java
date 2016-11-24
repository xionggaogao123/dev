package com.fulaan.zouban.service;

import com.db.zouban.XuanKeConfDao;
import com.fulaan.zouban.dto.ClassFenDuanDTO;
import com.fulaan.zouban.dto.ZouBanCourseDTO;
import com.pojo.zouban.PointEntry;
import com.pojo.zouban.XuankeConfEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ZhuZhouServiceTest {

    private ZhuZhouService zhuZhouService=new ZhuZhouService();
    private XuanKeConfDao xuanKeConfDao=new XuanKeConfDao();
    @Test
    public void testFindZoubanCourseList() throws Exception {
        String schoolId="56e68ec80cf2a5c70a1cc14c";//格致中学
        String gradeId="56e691b70cf2a5c70a1ccd3c";
        List<ZouBanCourseDTO> zouBanCourseDTOs=zhuZhouService.findZoubanCourseList("2015-2016学年",gradeId,schoolId);
        System.out.println(zouBanCourseDTOs);
    }

    @Test
    public void testAddInterestCourse() throws Exception {
        ZouBanCourseDTO zouBanCourseDTO=new ZouBanCourseDTO();
        zouBanCourseDTO.setSubjectName("欢乐课堂");
        zouBanCourseDTO.setSubjectId("56e691b80cf2a5c70a1ccd79");
        zouBanCourseDTO.setClassRoomId("56ea648c0cf28c73c3379630");
        zouBanCourseDTO.setGradeId("56e691b70cf2a5c70a1ccd3c");
        zouBanCourseDTO.setTeacherId("56e691b70cf2a5c70a1ccd57");
        zouBanCourseDTO.setTeacherName("周全_gz1");
        zouBanCourseDTO.setCourseName("啦啦啦");
        zouBanCourseDTO.setTerm("2015-2016学年");
        zouBanCourseDTO.setLessonCount(1);
        zouBanCourseDTO.setClassIdList(new ArrayList<ObjectId>());
        zouBanCourseDTO.setMax(30);
        List<PointEntry> pointEntryList=new ArrayList<PointEntry>();
        pointEntryList.add(new PointEntry(4,5));
        zouBanCourseDTO.setPointEntrylist(pointEntryList);
        String schoolId="56e68ec80cf2a5c70a1cc14c";//格致中学
        zhuZhouService.addInterestCourse(zouBanCourseDTO);
    }

    @Test
    public void testUpdateInterestCourse() throws Exception {

    }

    @Test
    public void testDeleteInterestCourse() throws Exception {

    }

    @Test
    public void addFenDuanTest()
    {
        XuankeConfEntry xuanke = xuanKeConfDao.findXuanKeConf("2015-2016学年", new ObjectId("5719db8f0cf2899955c86d2e"));
        ObjectId xuankeId=xuanke.getID();
        List<String> classIds=new ArrayList<String>();
        classIds.add("5719db8f0cf2899955c86d38");
        ClassFenDuanDTO classFenDuanDTO=new ClassFenDuanDTO(
                new ObjectId().toString(),xuankeId.toString(),0,"第一单元",classIds,classIds
        );
        zhuZhouService.addFenDuan(classFenDuanDTO);
    }
    @Test
    public void testFindFenDuans()
    {
        List<ClassFenDuanDTO> classFenDuanDTOs= zhuZhouService.findFenDuanDTOS("2015-2016学年","5719db8f0cf2899955c86d2e");
        System.out.println(classFenDuanDTOs);
    }
}