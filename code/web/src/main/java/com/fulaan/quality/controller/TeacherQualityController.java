package com.fulaan.quality.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.quality.service.TeacherQualityService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.service.CommonService;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/10/24.
 */

@Controller
@RequestMapping("/teachQuality")
public class TeacherQualityController extends BaseController {

    private static final Logger logger = Logger.getLogger(TeacherQualityController.class);

    @Autowired
    private TeacherQualityService teacherQualityService;

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private CommonService commonService;
    /**
     * 教师质量管理页面
     * @return
     */
    @RequestMapping("/index")
    public String indexPage(Model model){
        List<String> termList = commonService.getTermList();
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        model.addAttribute("termList",termList);
        model.addAttribute("gradeList", gradeList);
        return "/teacherevaluation/comment";
    }

    /**
     * 教师质量检索
     * @return
     */
    @RequestMapping("/selTeacherQualityList")
    @ResponseBody
    public Map selTeacherQualityList(String term,String gradeId,String userName,int page,int pageSize) {
        Map<String,Object> model = new HashMap<String,Object>();
        teacherQualityService.selTeacherQualityList(getSchoolId(),term, gradeId, userName, page, pageSize,model);
//        model.put("rows",teacherQualityDTOs);
//        model.put("total", teacherQualityService.selTeacherQualityCount(getSchoolId(),gradeId,userName));
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/addComment")
    @ResponseBody
    public Map addTeacherComment(String team,String comment,String teacherId,String score,int type) {
        Map map = new HashMap();
        try {
            teacherQualityService.addTeacherComment(team,comment,new ObjectId(teacherId),score,type);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }

        return map;
    }
}
