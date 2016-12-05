package com.fulaan.train;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by admin on 2016/12/5.
 */
@Controller
@RequestMapping("/train")
public class TrainController extends BaseController{

    @RequestMapping("/trainList")
    @SessionNeedless
    @LoginInfo
    public String trainList(Map<String,Object> model){
        return"/train/trainList";
    }


    @RequestMapping("/trainDetail")
    @SessionNeedless
    @LoginInfo
    public String trainDetail(Map<String,Object> model){
        return "/train/trainDetail";
    }


}
