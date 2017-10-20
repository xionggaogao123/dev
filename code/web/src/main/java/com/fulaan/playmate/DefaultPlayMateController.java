package com.fulaan.playmate;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.BaseController;
import com.fulaan.playmate.pojo.MateData;
import com.fulaan.playmate.service.FActivityService;
import com.fulaan.playmate.service.FMateTypeService;
import com.fulaan.playmate.service.MateService;
import com.fulaan.user.service.UserService;
import com.fulaan.util.StrUtils;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.user.UserTag;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by moslpc on 2016/11/30.
 */
@Api(value="",hidden = true)
@Controller
@RequestMapping("/jxmapi/mate")
public class DefaultPlayMateController extends BaseController {

    @Autowired
    private MateService mateService;
    @Autowired
    private UserService userService;
    @Autowired
    private FActivityService fActivityService;
    @Autowired
    private FMateTypeService fMateTypeService;

    @ApiOperation(value = "friend", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
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

    @ApiOperation(value = "getSortType", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @SessionNeedless
    @RequestMapping("/sortType")
    @ResponseBody
    public RespObj getSortType() {
        return RespObj.SUCCESS(fMateTypeService.getAllSortTypes());
    }

    @ApiOperation(value = "getPlayMates", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getPlayMates")
    @ResponseBody
    @SessionNeedless
    public RespObj getPlayMates(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                @RequestParam(value = "distance", required = false, defaultValue = "-1") int distance,
                                @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                @RequestParam(value = "aged", required = false, defaultValue = "-1") int aged,
                                @RequestParam(value = "ons", required = false, defaultValue = "-1") String ons,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        if (lon == 0 && lat == 0) {
            if (getUserId() != null) {
                ObjectId userId = getUserId();
                List<Double> locations = mateService.getCoordinates(userId);
                if (locations != null && locations.size() == 2) {
                    lon = locations.get(0);
                    lat = locations.get(1);
                }
            }
        }
        if (distance < 0) {
            distance = 10000000;
        } else if (distance == 1){
            distance = 500;
        } else if(distance == 2) {
            distance = 1000;
        } else if(distance == 3) {
            distance = 2000;
        } else if(distance == 4) {
            distance = 5000;
        }

        List<Integer> onsList = new ArrayList<Integer>();
        if (StringUtils.isNotBlank(ons) && !"-1".equals(ons)) {
            String[] tempList = ons.split(",");
            for (String s : tempList) {
                try {
                    onsList.add(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
            mateService.updateOns(getUserId(), onsList);
        }

        List<String> tagList = StrUtils.splitToList(tags);
        return RespObj.SUCCESS(mateService.findMates(getUserId(), lon, lat, tagList, aged, onsList, page, pageSize, distance));
    }

    @ApiOperation(value = "updateLocation", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateMateData")
    @ResponseBody
    public RespObj updateLocation(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                  @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                  @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                  @RequestParam(value = "aged", required = false, defaultValue = "-1") int age,
                                  @RequestParam(value = "ons", required = false, defaultValue = "") String ons) {
        ObjectId userId = getUserId();
        if (lon != 0 && lat != 0) {
            mateService.updateLocation(userId, lon, lat);
        }
        if (StringUtils.isNotBlank(tags)) {
            updateTag(userId, tags);
        }
        //更新年龄
        if (age != -1) {
            mateService.updateAge(getUserId(), age);
        }
        //更新在线时间段
        updateTime(userId, ons);
        return RespObj.SUCCESS("成功");
    }

    /**
     * 获取我的标签
     *
     * @return
     */
    @ApiOperation(value = "获取我的标签", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getMyTags")
    @ResponseBody
    public RespObj getMyTags() {
        ObjectId userId = getUserId();
        UserEntry userEntry = userService.findById(userId);
        List<Map<String, Object>> tags = getTagsFromUserEntry(userEntry);
        return RespObj.SUCCESS(tags);
    }

    @ApiOperation(value = "getMyOns", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getMyOns")
    @ResponseBody
    public RespObj getMyOns() {
        return RespObj.SUCCESS(mateService.getMyOns(getUserId()));
    }

    /**
     * 生成排序数据
     * 没后台管理好烦 =_=
     *
     * @return
     */
    @ApiOperation(value = "生成排序数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/generateSortData")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj generateSortData() {
        fMateTypeService.generateData();
        return RespObj.SUCCESS;
    }

    /**
     * 创建索引
     * =_= 创建 地理位置索引
     *
     * @return 200
     */
    @ApiOperation(value = "创建索引", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/create2dsphereIndex")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj create2dsphereIndex() {
        fMateTypeService.create2dsphereIndex();
        return RespObj.SUCCESS;
    }

    @ApiOperation(value = "clearHeap", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/clearHeap")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj clearHeap() {
        fMateTypeService.clearHeap();
        return RespObj.SUCCESS;
    }

    @ApiOperation(value = "getUserMateData", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getUserMateData")
    @ResponseBody
    @SessionNeedless
    public RespObj getUserMateData(@RequestParam(value = "userId", defaultValue = "", required = false) String userId) {

        ObjectId personId;
        if (StringUtils.isNotBlank(userId)) {
            personId = new ObjectId(userId);
        } else {
            personId = getUserId();
        }

        if (personId == null) {
            return RespObj.FAILD;
        }
        List<MateData> mateDatas = mateService.getMyOns(personId);
        UserEntry userEntry = userService.findById(personId);
        List<Map<String, Object>> tags = getTagsFromUserEntry(userEntry);
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> freeTime = new ArrayList<Map<String, Object>>();
        for (MateData data : mateDatas) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("code", data.getCode());
            map1.put("time", data.getData());
            freeTime.add(map1);
        }
        map.put("times", freeTime);
        map.put("tags", tags);
        return RespObj.SUCCESS(map);
    }

    @ApiOperation(value = "updateUserAge", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateUserAge")
    @ResponseBody
    public RespObj updateUserAge(int year, int month, int day) {
        try {
            long birthdayTimeStamp = DateTimeUtils.dateTime(year,month,day);
            userService.update(getUserId(), "bir", birthdayTimeStamp);
            mateService.updateAged(getUserId(), birthdayTimeStamp);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("修改失败");
        }
        return RespObj.SUCCESS("修改成功");
    }

    @ApiOperation(value = "updateUserTag", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateUserTag")
    @ResponseBody
    public RespObj updateUserTag(String tags) {
        ObjectId userId = getUserId();
        updateTag(userId, tags);
        return RespObj.SUCCESS("修改成功");
    }

    @ApiOperation(value = "updateUserTime", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateUserTime")
    @ResponseBody
    public RespObj updateUserTime(String times) {
        updateTime(getUserId(), times);
        return RespObj.SUCCESS("修改成功");
    }

    @ApiOperation(value = "updateUserTagAndTime", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateUserTagAndTime")
    @ResponseBody
    public RespObj updateUserTagAndTime(@RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                        @RequestParam(value = "times", required = false, defaultValue = "") String times) {
        ObjectId userId = getUserId();
        updateTag(userId, tags);
        updateTime(userId, times);
        return RespObj.SUCCESS("修改成功");
    }

    private void updateTag(ObjectId userId, String tags) {
        if (StringUtils.isNotBlank(tags)) {
            List<String> tagList = StrUtils.splitToList(tags);
            List<Integer> tagIntegerList = new ArrayList<Integer>();
            for (String tag : tagList) {
                tagIntegerList.add(Integer.parseInt(tag));
            }
            mateService.updateTags(userId, tagIntegerList);
            List<UserTag> userTagList = new ArrayList<UserTag>();
            List<MateData> mate = fMateTypeService.getTags();
            for (int code : tagIntegerList) {
                for (MateData mateData : mate) {
                    if (mateData.getCode() == code) {
                        userTagList.add(new UserTag(mateData.getCode(), mateData.getData()));
                    }
                }
            }
            userService.pushUserTags(userId, userTagList);
        } else {
            mateService.updateTags(userId, new ArrayList<Integer>());
            userService.pushUserTags(userId, new ArrayList<UserTag>());
        }

    }

    private void updateTime(ObjectId userId, String times) {
        //更新在线时间段
        if (StringUtils.isNotBlank(times)) {
            List<Integer> onsList = new ArrayList<Integer>();
            String[] tempList = times.split(",");
            for (String s : tempList) {
                try {
                    onsList.add(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            onsList = new ArrayList<Integer>(new HashSet<Integer>(onsList));
            mateService.updateOns(userId, onsList);
        } else {
            mateService.updateOns(userId, new ArrayList<Integer>());
        }
    }

    private List<Map<String, Object>> getTagsFromUserEntry(UserEntry userEntry) {
        List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
        List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
        for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", userTagEntry.getCode());
            map.put("tag", userTagEntry.getTag());
            tags.add(map);
        }
        return tags;
    }


}
