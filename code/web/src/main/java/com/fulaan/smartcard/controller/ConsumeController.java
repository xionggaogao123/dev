package com.fulaan.smartcard.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/9/9.
 */

@Controller
@RequestMapping("/consume")
public class ConsumeController extends BaseController {

    @Autowired
    private SchoolService schoolService;

    @RequestMapping("/consumepage")
    public String consumePage(Model model) {
        List<GradeView> gradeViewList = schoolService.findGradeList(getSessionValue().getSchoolId());
        model.addAttribute("gradelist",gradeViewList);
        return "peoplegroup/consume";
    }
}
