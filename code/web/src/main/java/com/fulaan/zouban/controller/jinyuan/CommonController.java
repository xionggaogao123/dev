package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.base.controller.BaseController;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.service.CommonService;
import com.fulaan.zouban.service.ZoubanStateService;
import com.pojo.school.ClassInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by wangkaidong on 2016/7/5.
 */

@Controller
@RequestMapping("/zouban/common")
public class CommonController extends BaseController {
    @Autowired
    private ClassService classService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private CommonService commonService;

    /**
     * 获取学年列表
     */
    @RequestMapping("/getTermList")
    @ResponseBody
    public Map<String, Object> getTermList() {
        List<String> termList = commonService.getTermList();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("termList", termList);
        return result;
    }



    /**
     * 获取年级列表
     * @return
     */
    @RequestMapping("/getGradeList")
    @ResponseBody
    public Map<String, Object> getGradeList() {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("gradeList", gradeList);
        return result;
    }

    /**
     * 获取年级下的班级列表
     * @param gradeId
     * @return
     */
    @RequestMapping("/getClassList")
    @ResponseBody
    public Map<String, Object> findClassByGradeId(String gradeId) {
        List<ClassInfoDTO> classInfoList = new ArrayList<ClassInfoDTO>();
        if (gradeId.equals("-1")) {
            classInfoList.addAll(classService.findClassInfoBySchoolId(getSessionValue().getSchoolId()));
        } else {
            classInfoList.addAll(classService.findClassByGradeId(gradeId));
        }

        List<IdNameDTO> classList = new ArrayList<IdNameDTO>();
        for (ClassInfoDTO classInfoDTO : classInfoList) {
            classList.add(new IdNameDTO(classInfoDTO.getId(), classInfoDTO.getClassName()));
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("classList", classList);
        return result;
    }

    /**
     * 获取走班课学科列表
     * @param term
     * @param gradeId
     * @return
     */
    @RequestMapping("/subjectList")
    @ResponseBody
    public Map<String,Object> getSubjectList(String term, String gradeId) {
        List<IdNameDTO> subjectList = commonService.getSubjectList(term, gradeId, getSchoolId().toString());
        Map<String ,Object> result = new HashMap<String, Object>();
        result.put("subjectList", subjectList);
        return result;
    }




}
