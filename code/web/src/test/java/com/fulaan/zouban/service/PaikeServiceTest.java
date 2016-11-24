package com.fulaan.zouban.service;

import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.CourseTeacherRoom;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class PaikeServiceTest {

    private PaikeService paikeService=new PaikeService();
    @Autowired
    private SchoolService schoolService;
    @Test
    public void testAutoSortResult() throws Exception {
        //autoSortResult(String courseConfId, List<CourseTeacherRoom> needArrangeCourse,
        //String term, String classId, String schoolId
        String courseConfId="5628b59ba0247d32a9576452";
        String term="2015-2016年度第一学期";
        String classId="5593519cf6f28b7261c9ca3c";
        String schoolId="55934c14f6f28b7261c19c62";
        List<CourseTeacherRoom> courseTeacherRoomList=new ArrayList<CourseTeacherRoom>();
        //paikeService.autoArrangeCourse(courseConfId,courseTeacherRoomList,term,classId,schoolId);
    }
    @Test
    public void test()
    {
        //List<xzbDTO> list=paikeService.groupXZBPoint("2015-2016学年","55934c15f6f28b7261c19c86","55935197f6f28b7261c9bc5e");
        //System.out.println(list);
        //paikeService.xzbSortClass("2015-2016学年","564d88407f969426892411dc","564d88407f969426892411df");
        //paikeService.xzbSortClass("2015-2016学年","55934c15f6f28b7261c19c86","55935197f6f28b7261c9bc5e");
    }
    @Test
    public void testPhysicalClass()
    {
        //paikeService.physicalZoubanSort("2015-2016学年","564d88407f969426892411dc","564d88407f969426892411df",3,true);
        //paikeService.physicalZoubanSort("2015-2016学年","55934c15f6f28b7261c19c86","55935197f6f28b7261c9bc5e",3,true);
    }
}