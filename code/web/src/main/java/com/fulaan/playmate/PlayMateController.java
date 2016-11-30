package com.fulaan.playmate;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.playmate.service.MateService;
import com.fulaan.util.StrUtils;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
@Controller
@RequestMapping("/mate")
public class PlayMateController extends BaseController{

    @Autowired
    private MateService mateService;


    @RequestMapping("/getPlayMates")
    @ResponseBody
    @SessionNeedless
    public RespObj getPlayMates(double lon, double lat, String tags, String hobbys, int aged, int ons, int page, int pageSize) {
        List<String> tagList = StrUtils.splitToList(tags);
        List<String> hobbyList = StrUtils.splitToList(hobbys);
        return RespObj.SUCCESS(mateService.findMates(lon, lat, tagList, hobbyList, aged, ons, page, pageSize));
    }

    @RequestMapping("/updateMateData")
    @ResponseBody
    public RespObj updateLocation(@RequestParam(value = "lon",required = false,defaultValue = "0") double lon,
                                  @RequestParam(value = "lat",required = false,defaultValue = "0") double lat,
                                  @RequestParam(value = "tags",required = false,defaultValue = "")String tags,
                                  @RequestParam(value = "hobbys",required = false,defaultValue = "")String hobbys){
        ObjectId userId = getUserId();
        if(lon != 0 && lat != 0) {
            mateService.updateLocation(userId,lon,lat);
        }
        if(StringUtils.isNotBlank(tags)) {
            List<String> tagList = StrUtils.splitToList(tags);
            mateService.updateTags(userId,tagList);
        }
        if(StringUtils.isNotBlank(hobbys)) {
            List<String> hobbyList = StrUtils.splitToList(hobbys);
            mateService.updateHobbys(userId,hobbyList);
        }
        return RespObj.SUCCESS("成功");
    }

}
