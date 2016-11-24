package com.fulaan.zouban.controller.zhuzhou;

import com.fulaan.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wangkaidong on 2016/9/14.
 */

@Controller
@RequestMapping("/zouban/zhuzhou")
public class ZhuzhouController extends BaseController{


    @RequestMapping("/index")
    public String index() {
        return "zhuzhou/zzindex";
    }


    @RequestMapping("/subjectConf")
    public String subjectConf() {
        return "zhuzhou/subjectConf";
    }


    @RequestMapping("/xuanke")
    public String xuanke() {
        return "zhuzhou/xuanke";
    }

    @RequestMapping("/timetable")
    public String timetable() {
        return "zhuzhou/timetable";
    }
}
