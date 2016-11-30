package com.fulaan.playmate;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.playmate.service.MateService;
import com.fulaan.util.StrUtils;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
@Controller
@RequestMapping("/mate")
public class PlayMateController {

    @Autowired
    private MateService mateService;


    @RequestMapping("/getPlayMates")
    @ResponseBody
    @SessionNeedless
    public RespObj getPlayMates(double lon, double lat, String tags, String hobbys, int aged, int ons,int page,int pageSize) {
        List<String> tagList = StrUtils.splitToList(tags);
        List<String> hobbyList = StrUtils.splitToList(hobbys);
        return RespObj.SUCCESS(mateService.findMates(lon,lat,tagList,hobbyList,aged,ons,page,pageSize));
    }
    
}
