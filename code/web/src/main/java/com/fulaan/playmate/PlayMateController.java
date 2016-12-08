package com.fulaan.playmate;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.controller.BaseController;
import com.fulaan.playmate.pojo.MateData;
import com.fulaan.playmate.service.FActivityService;
import com.fulaan.playmate.service.FMateTypeService;
import com.fulaan.playmate.service.MateService;
import com.fulaan.user.service.UserService;
import com.fulaan.util.StrUtils;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.user.UserTag;
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
    @Autowired
    private FActivityService fActivityService;
    @Autowired
    private FMateTypeService fMateTypeService;


    @SessionNeedless
    @RequestMapping("/friend")
    @LoginInfo
    public String friend(Map<String, Object> model) {

        if (getUserId() == null) {
            model.put("signActivityCount", 0);
            model.put("publishActivityCount", 0);
        } else {
            model.put("signActivityCount", fActivityService.countUserSignActivity(getUserId()));
            model.put("publishActivityCount", fActivityService.countPublishActivity(getUserId()));
        }
        return "/friend/index";
    }

    @SessionNeedless
    @RequestMapping("/sortType")
    @ResponseBody
    public RespObj getSortType() {
        return RespObj.SUCCESS(fMateTypeService.getAllSortTypes());
    }

    @RequestMapping("/getPlayMates")
    @ResponseBody
    @SessionNeedless
    public RespObj getPlayMates(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                @RequestParam(value = "distance", required = false, defaultValue = "-1") int distance,
                                @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
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
        if (distance < 0) {
            distance = 10000000;
        } else {
            distance *= 500;
        }
        List<String> tagList = StrUtils.splitToList(tags);
        return RespObj.SUCCESS(mateService.findMates(getUserId(), lon, lat, tagList, aged, ons, page, pageSize, distance));
    }

    @RequestMapping("/updateMateData")
    @ResponseBody
    public RespObj updateLocation(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                  @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                  @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                  @RequestParam(value = "aged", required = false, defaultValue = "-1") int age,
                                  @RequestParam(value = "ons", required = false, defaultValue = "-1") int ons) {
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

            List<UserTag> userTagList = new ArrayList<UserTag>();
            List<MateData> mateDatas = fMateTypeService.getTags();
            for (int code : tagIntegerList) {
                for (MateData mateData : mateDatas) {
                    if (mateData.getCode() == code) {
                        userTagList.add(new UserTag(mateData.getCode(), mateData.getData()));
                    }
                }
            }
            userService.pushUserTags(getUserId(), userTagList);
        }
        //更新年龄
        if (age != -1) {
            mateService.updateAge(getUserId(), age);
        }

        //更新在线时间段
        if (ons != -1) {
            mateService.updateOns(getUserId(), ons);
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

    @RequestMapping("/getMyOns")
    @ResponseBody
    public RespObj getMyOns() {
        return RespObj.SUCCESS(mateService.getMyOns(getUserId()));
    }

    @RequestMapping("/generateSortData")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj generateSortData() {
        fMateTypeService.generateData();
        return RespObj.SUCCESS;
    }


}
