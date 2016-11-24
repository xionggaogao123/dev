package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.zouban.dto.SchoolSubjectGroupDTO;
import com.fulaan.zouban.service.SchoolSubjectGroupService;
import com.pojo.school.ClassEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/7/7.
 *
 * 学生选课Controller
 */

@Controller
@RequestMapping("/zouban/student")
public class StudentController extends BaseController {
    @Autowired
    private SchoolSubjectGroupService subjectGroupService;
    @Autowired
    private ClassService classService;


    /**
     * 课表页面
     *
     * @param model
     * @return
     */
    @RequestMapping
    public String indexPage(Model model){
        List<ClassEntry> classEntries = classService.findClassEntryByStuId(getUserId());
        model.addAttribute("curweek", 1);
        model.addAttribute("gid", classEntries.get(0).getGradeId());

        return "zoubannew/student/kebiao";
    }

    /**
     * 选课页面
     * @return
     */
    @RequestMapping("/xuanke")
    public String XuankePage() {
        return "zoubannew/student/lessonselect";
    }


    /**
     * 获取开放的选课组合
     *
     * @param term
     * @param gradeId
     * @return
     */
    @RequestMapping("/groupList")
    @ResponseBody
    public Map<String, Object> getGroupList(String term, String gradeId) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            SchoolSubjectGroupDTO schoolSubjectGroupDTO = subjectGroupService.getSchoolSubjectGroupDTO(getSchoolId(), term, new ObjectId(gradeId));
            List<SchoolSubjectGroupDTO.SubjectGroupDTO> subjectGroupDTOList = schoolSubjectGroupDTO.getSubjectGroupDTOs();

            Iterator<SchoolSubjectGroupDTO.SubjectGroupDTO> iterator = subjectGroupDTOList.iterator();
            while (iterator.hasNext()) {
                SchoolSubjectGroupDTO.SubjectGroupDTO subjectGroupDTO = iterator.next();
                if (!subjectGroupDTO.getIsPublic()) {
                    iterator.remove();
                }
            }

            result.put("groupList", schoolSubjectGroupDTO);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("groupList", null);
        }

        return result;
    }

}
