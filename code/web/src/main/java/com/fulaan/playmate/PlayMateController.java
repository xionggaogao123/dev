package com.fulaan.playmate;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.playmate.service.MateService;
import com.fulaan.user.service.UserService;
import com.fulaan.util.StrUtils;
import com.pojo.user.UserEntry;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moslpc on 2016/11/30.
 */
@Controller
@RequestMapping("/mate")
public class PlayMateController extends BaseController {

    @Autowired
    private MateService mateService;
    @Autowired
    private UserService userService;

    @RequestMapping("/getPlayMates")
    @ResponseBody
    @SessionNeedless
    public RespObj getPlayMates(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                @RequestParam(value = "distance", required = false, defaultValue = "10000000") int distance,
                                @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                @RequestParam(value = "hobbys", required = false, defaultValue = "") String hobbys,
                                @RequestParam(value = "aged", required = false, defaultValue = "-1") int aged,
                                @RequestParam(value = "ons", required = false, defaultValue = "-1") int ons,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        if (lon == 0 && lat == 0) {
            ObjectId userId = getUserId();
            List<Double> locs = mateService.getCoordinates(userId);
            if (locs != null && locs.size() == 2) {
                lon = locs.get(0);
                lat = locs.get(1);
            }
        }
        if(distance < 0) {
            distance = 10000000;
        }
        List<String> tagList = StrUtils.splitToList(tags);
        List<String> hobbyList = StrUtils.splitToList(hobbys);
        return RespObj.SUCCESS(mateService.findMates(getUserId(),lon, lat, tagList, hobbyList, aged, ons, page, pageSize, distance));
    }

    @RequestMapping("/updateMateData")
    @ResponseBody
    public RespObj updateLocation(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                  @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                  @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                  @RequestParam(value = "hobbys", required = false, defaultValue = "") String hobbys,
                                  @RequestParam(value = "age",required = false,defaultValue = "-1") int age) {
        ObjectId userId = getUserId();
        if (lon != 0 && lat != 0) {
            mateService.updateLocation(userId, lon, lat);
        }
        if (StringUtils.isNotBlank(tags)) {
            List<String> tagList = StrUtils.splitToList(tags);
            List<Integer> tagIntegerList = new ArrayList<Integer>();
            for (String tag : tagList) {
                tagIntegerList.add(Integer.parseInt(tag));
            }
            mateService.updateTags(userId, tagIntegerList);
        }
        if (StringUtils.isNotBlank(hobbys)) {
            List<String> hobbyList = StrUtils.splitToList(hobbys);
            mateService.updateHobbys(userId, hobbyList);
        }
        return RespObj.SUCCESS("成功");
    }

    /**
     * 获取我的标签
     *
     * @return
     */
    @RequestMapping("/getMyTags")
    @ResponseBody
    public RespObj getMyTags() {
        List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
        ObjectId userId = getUserId();
        UserEntry userEntry = userService.find(userId);
        List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
        for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", userTagEntry.getCode());
            map.put("tag", userTagEntry.getTag());
            tags.add(map);
        }
        return RespObj.SUCCESS(tags);
    }


}
