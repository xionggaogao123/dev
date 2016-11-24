package com.fulaan.zouban.service;

import com.db.zouban.CourseConfDao;
import com.db.zouban.TimeTableDao;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.CourseConfDTO;
import com.fulaan.zouban.dto.CourseEventDTO;
import com.fulaan.zouban.dto.TeacherEventDTO;
import com.fulaan.zouban.dto.TimetableConfDTO;
import com.pojo.user.UserEntry;
import com.pojo.zouban.CourseConfEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2015/9/15.
 */
@Service
public class CourseService {
    private CourseConfDao courseConfDao = new CourseConfDao();



    /**
     * 获取课表配置，用于来凤，不需要精确到年级
     *
     * @param schoolId
     * @param term
     * @return
     */
    public CourseConfDTO findCourseConfBySchool(String schoolId, String term) {
        CourseConfEntry courseConfEntry = courseConfDao.findCourseConfBySchool(new ObjectId(schoolId), term);
        if (courseConfEntry == null)//无数据，新建一个
        {
            CourseConfDTO courseConfDTO = new CourseConfDTO();
            courseConfDTO.setSchoolId(schoolId);
            courseConfDTO.setTerm(term);
            courseConfDTO.setGradeId(new ObjectId().toString());

            List<Integer> classDays = new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
            }};
            List<String> classTime = new ArrayList<String>() {{
                add("08:00~08:45");
                add("08:55~09:40");
                add("10:00~10:45");
                add("10:55~11:40");
                add("14:00~14:45");
                add("14:55~15:40");
                add("16:00~16:45");
                add("16:55~17:40");
            }};
            courseConfDTO.setClassDays(classDays);
            courseConfDTO.setClassCount(8);
            courseConfDTO.setClassTime(classTime);

            List<CourseEventDTO> courseEventList = new ArrayList<CourseEventDTO>();
            courseConfDTO.setEvents(courseEventList);

            addCourseConf(courseConfDTO);
            return courseConfDTO;
        }
        CourseConfDTO courseConfDTO = new CourseConfDTO(courseConfEntry);

        return courseConfDTO;
    }



    /**
     * 增加/修改课表配置
     *
     * @param courseConf
     * @return
     */
    public void addCourseConf(CourseConfDTO courseConf) {
        //判断是否已经存在，为避免多个管理员同时添加造成数据重复
        if (courseConfDao.countCourseConf(new ObjectId(courseConf.getSchoolId()), courseConf.getTerm(),
                new ObjectId(courseConf.getGradeId())) > 0) {
            courseConfDao.removeCourseConf(new ObjectId(courseConf.getSchoolId()), courseConf.getTerm(),
                    new ObjectId(courseConf.getGradeId()));
        }
        CourseConfDTO courseConfDTO = addObjectId(courseConf);
        courseConfDao.addCourseConf(courseConfDTO.export());
    }


    /**
     * 给前端发送过来的CourseConfDTO添加id（嵌套部分的id）
     *
     * @param courseConfDTO
     * @return
     */
    public CourseConfDTO addObjectId(CourseConfDTO courseConfDTO) {
        List<CourseEventDTO> courseEventDTOList = courseConfDTO.getEvents();
        if (courseEventDTOList != null && !courseEventDTOList.isEmpty()) {
            for (CourseEventDTO courseEventDTO : courseEventDTOList) {
                courseEventDTO.setId(new ObjectId().toString());
                List<TeacherEventDTO> teacherEventDTOList = courseEventDTO.getPersonEvent();
                if (teacherEventDTOList != null && !teacherEventDTOList.isEmpty()) {
                    for (TeacherEventDTO teacherEventDTO : teacherEventDTOList) {
                        teacherEventDTO.setId(new ObjectId().toString());
                    }
                }
                courseEventDTO.setPersonEvent(teacherEventDTOList);
            }
            courseConfDTO.setEvents(courseEventDTOList);
        }
        return courseConfDTO;
    }


    public CourseConfDTO findCourseConfList(String term, String schoolId) {
        List<CourseConfEntry> courseConfEntryList = courseConfDao.findCourseConfsBySchoolId(term, new ObjectId(schoolId));
        if (courseConfEntryList.size() > 0)
            return new CourseConfDTO(courseConfEntryList.get(0));
        return new CourseConfDTO();
    }

}
