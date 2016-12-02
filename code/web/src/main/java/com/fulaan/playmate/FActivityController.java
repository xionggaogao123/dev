package com.fulaan.playmate;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.playmate.service.FActivityService;
import com.fulaan.util.DateUtils;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

/**
 * Created by moslpc on 2016/11/30.
 */
@Controller
@RequestMapping("/activity")
public class FActivityController extends BaseController {

    @Autowired
    private FActivityService fActivityService;

    @RequestMapping("/publish")
    @ResponseBody
    public RespObj publishActivity(double lon, double lat, int acode, String title,
                                   @RequestParam(value = "desc", required = false, defaultValue = "") String des,
                                   String endTime) {
        long timeStamp;
        try {
            timeStamp = DateUtils.strToTimeStamp(endTime + ":00") * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return RespObj.FAILD("失败");
        }
        fActivityService.saveActivity(getUserId(), lon, lat, acode, title, des, timeStamp);
        return RespObj.SUCCESS;
    }

    @RequestMapping("/nearActivitys")
    @ResponseBody
    @SessionNeedless
    public RespObj nearActivitys(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                 @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "page", required = false, defaultValue = "10") int pageSize) {
        return RespObj.SUCCESS(fActivityService.getNearActivitys(lon, lat, page, pageSize));
    }

    @RequestMapping("sign")
    @ResponseBody
    @SessionNeedless
    public RespObj signActivity(@RequestParam(value = "acid") @ObjectIdType ObjectId acid,
                                @RequestParam(value = "signText", required = false, defaultValue = "") String signText) {
        return RespObj.SUCCESS(fActivityService.signActivity(acid,getUserId(),signText));
    }
}
