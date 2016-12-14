package com.fulaan.playmate;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.playmate.dto.FActivityDTO;
import com.fulaan.playmate.service.FActivityService;
import com.fulaan.pojo.PageModel;
import com.fulaan.util.DateUtils;
import com.pojo.playmate.FActivityEntry;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

/**
 * Created by moslpc on 2016/11/30.
 * 找活动
 */
@Controller
@RequestMapping("/factivity")
public class FActivityController extends BaseController {

    @Autowired
    private FActivityService fActivityService;

    /**
     * 发布活动
     *
     * @param lon
     * @param lat
     * @param acode
     * @param title
     * @param des
     * @param endTime
     * @return
     */
    @RequestMapping("/publish")
    @ResponseBody
    public RespObj publishActivity(double lon, double lat, int acode, String title,
                                   @RequestParam(value = "desc", required = false, defaultValue = "") String des,
                                   String endTime) {
        long timeStamp;
        try {
            timeStamp = DateUtils.strToTimeStamp(endTime + ":00") * 1000;
            if (timeStamp < System.currentTimeMillis()) {
                return RespObj.FAILD("发布时间不得小于当前时间");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return RespObj.FAILD("失败");
        }
        fActivityService.saveActivity(getUserId(), lon, lat, acode, title, des, timeStamp);
        return RespObj.SUCCESS;
    }

    /**
     * 附近的活动
     *
     * @param lon 经度
     * @param lat 纬度
     * @param page 页
     * @param pageSize 每页个数
     * @return RespObj
     */
    @RequestMapping("/nearActivitys")
    @ResponseBody
    @SessionNeedless
    public RespObj nearActivity(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageModel<FActivityDTO> pageModel = fActivityService.getNearActivitys(lon, lat, page, pageSize);
        for(FActivityDTO fActivityDTO : pageModel.getResult()) {
            if(getUserId() != null) {
                if(fActivityDTO.getUserId().equals(getUserId())) {
                    fActivityDTO.setYouSigned(true);
                } else {
                    fActivityDTO.setYouSigned(fActivityService.isUserSigned(getUserId(),new ObjectId(fActivityDTO.getAcid())));
                }
            }
        }
        return RespObj.SUCCESS(pageModel);
    }

    /**
     * 报名某个活动
     *
     * @param acid
     * @param signText
     * @return
     */
    @RequestMapping("/sign")
    @ResponseBody
    public RespObj signActivity(@RequestParam(value = "acid") @ObjectIdType ObjectId acid,
                                @RequestParam(value = "signText", required = false, defaultValue = "") String signText) {
        return RespObj.SUCCESS(fActivityService.signActivity(acid, getUserId(), signText));
    }

    /**
     * 活动详情
     *
     * @param acid
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    @SessionNeedless
    public RespObj detailActivity(@RequestParam(value = "acid") @ObjectIdType ObjectId acid) {
        FActivityDTO fActivityDTO = fActivityService.getActivityById(acid);
        if (fActivityDTO == null) {
            return RespObj.FAILD("活动不存在");
        }
        fActivityDTO.setSignSheets(fActivityService.getAllSignMembers(acid));
        if(getUserId() != null) {
            if(fActivityDTO.getUserId().equals(getUserId())) {
                fActivityDTO.setYouSigned(true);
            } else {
                fActivityDTO.setYouSigned(fActivityService.isUserSigned(acid,getUserId()));
            }
        }
        return RespObj.SUCCESS(fActivityDTO);
    }

    /**
     * 获取自己或别人发布的活动
     *
     * @param personId 别人的id
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/published")
    @ResponseBody
    @SessionNeedless
    public RespObj published(@RequestParam(value = "personId", required = false, defaultValue = "") String personId,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        if (StringUtils.isNotBlank(personId)) {
            PageModel<FActivityDTO> pageModel = fActivityService.getPublishedActivity(new ObjectId(personId), page, pageSize);
            for(FActivityDTO fActivityDTO : pageModel.getResult()) {
                fActivityDTO.setYouSigned(fActivityService.isUserSigned(new ObjectId(personId),new ObjectId(fActivityDTO.getAcid())));
            }
            return RespObj.SUCCESS(pageModel);
        }
        if(getUserId() != null) {
            return RespObj.SUCCESS(fActivityService.getPublishedActivity(getUserId(), page, pageSize));
        }
        return RespObj.FAILD;
    }

    /**
     * 获取自己参加的活动
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/signed")
    @ResponseBody
    public RespObj signed(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return RespObj.SUCCESS(fActivityService.getSignedActivity(getUserId(), page, pageSize));
    }

    /**
     * 获取已经过期的活动
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/attended")
    @ResponseBody
    public RespObj attended(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return RespObj.SUCCESS(fActivityService.getAttendedActivity(getUserId(), page, pageSize));
    }

    /**
     * 取消报名某活动
     *
     * @param acid
     * @return
     */
    @RequestMapping("/cancelSign")
    @ResponseBody
    public RespObj cancel(@ObjectIdType ObjectId acid) {
        if (!fActivityService.isUserSigned(acid, getUserId())) {
            return RespObj.FAILD("您未参加此活动");
        }
        FActivityEntry fActivityEntry = fActivityService.getActivityEntryById(acid);
        if (fActivityEntry.getUserId().equals(getUserId())) {
            return RespObj.FAILD("不能取消报名自己发布的活动");
        }
        fActivityService.cancelSignActivity(acid, getUserId());
        return RespObj.SUCCESS;
    }

    /**
     * 取消发布某活动
     *
     * @param acid
     * @return
     */
    @RequestMapping("/cancelPublish")
    @ResponseBody
    public RespObj cancelPublish(@ObjectIdType ObjectId acid) {
        fActivityService.cancelPublishActivity(acid, getUserId());
        return RespObj.SUCCESS;
    }


}
