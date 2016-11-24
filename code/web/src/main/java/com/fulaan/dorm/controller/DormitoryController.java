package com.fulaan.dorm.controller;

import com.fulaan.base.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by wang_xinxin on 2016/10/8.
 */

@Controller
@RequestMapping("/dormitory")
public class DormitoryController extends BaseController {

    private static final Logger logger = Logger.getLogger(DormitoryController.class);

    @RequestMapping("/dormitoryPage")
    public String dormpage(Map<String, Object> model) {
        return "peoplegroup/dormitory";
    }
}
