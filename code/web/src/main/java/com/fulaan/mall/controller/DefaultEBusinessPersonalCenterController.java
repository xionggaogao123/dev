package com.fulaan.mall.controller;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.mall.service.EGoodsLogService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.ebusiness.EGoodsDayLog;
import com.pojo.ebusiness.EGoodsLogDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/4/12.
 */
@Api(value="",hidden = true)
@Controller
@RequestMapping("/jxmapi/mall/userCenter")
public class DefaultEBusinessPersonalCenterController extends BaseController {

    @Autowired
    private EGoodsLogService eGoodsLogService;
    @Autowired
    private UserService userService;


    private void loginInfo(HttpServletRequest request, Map<String, Object> model) {
        SessionValue sessionValue = (SessionValue) request.getAttribute(BaseController.SESSION_VALUE);
        if (null == sessionValue || sessionValue.isEmpty()) {
            model.put("userName", "");
            model.put("userId", "");
            model.put("login", false);
            model.put("k6kt", -1);
        } else {
            model.put("userName", sessionValue.getUserName());
            model.put("userId", sessionValue.getId());
            model.put("login", true);
            model.put("k6kt", sessionValue.getK6kt());
            model.put("avatar", sessionValue.getMinAvatar());
        }
    }


    /**
     * 抵用券
     */
    @ApiOperation(value = "抵用券", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/voucher")
    public String voucherIndexPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        model.put("menuItem", 2);
        return "/mall/voucher";
    }

    /**
     * 收藏记录
     */
    @ApiOperation(value = "收藏记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/collection")
    public String collectionPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        model.put("menuItem", 3);
        return "/mall/collection";
    }

    /**
     * 浏览记录
     */
    @ApiOperation(value = "浏览记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/history")
    public String historyPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        model.put("menuItem", 4);
        return "/mall/history";
    }

    /**
     * 个人中心
     */
    @ApiOperation(value = "个人中心", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/user")
    @LoginInfo
    public String userPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
//        model.put("menuItem", 5);
        model.put("menuItem", 6);
        return "/mall/addressmanage";
//        return "/mall/user";
    }

    /**
     * 地址管理
     */
    @ApiOperation(value = "地址管理", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/address")
    public String addressPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        model.put("menuItem", 6);
        return "/mall/addressmanage";
    }

    /**
     * 获取浏览记录
     */
    @ApiOperation(value = "获取浏览记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/historyList")
    @ResponseBody
    public Map<String, Object> getHistoryList() {
        List<EGoodsDayLog> historyList = eGoodsLogService.getDayLogList(getUserId());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("historyList", historyList);
        return result;
    }

    /**
     * 获取收藏记录
     */
    @ApiOperation(value = "获取收藏记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/collectionList")
    @ResponseBody
    public Map<String, Object> getGoodsLogList() {
        List<EGoodsLogDTO> collectionList = eGoodsLogService.getCollectionList(getUserId());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("collectionList", collectionList);
        return result;
    }

    /**
     * 删除浏览/收藏记录
     */
    @ApiOperation(value = "删除浏览/收藏记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteLog/{logId}")
    @ResponseBody
    public RespObj deleteCollection(@PathVariable("logId") @ApiParam(name = "logId", required = true, value = "logId") @ObjectIdType ObjectId logId) {
        RespObj respObj = RespObj.FAILD;
        try {
            eGoodsLogService.deleteLog(logId);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("删除失败！");
        }
        return respObj;
    }

    /**
     * 获取用户个人信息
     */
    @ApiOperation(value = "获取用户个人信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserInfo() {
        UserDetailInfoDTO user = userService.getUserInfoById(getUserId().toString());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("name", user.getUserName());
        result.put("nickName", user.getNickName());
        result.put("sex", user.getSex());
        result.put("phone", user.getMobileNumber());

        Date birthdate = user.getBirthDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(birthdate);
        String[] dateArr = date.split("-");
        int year = Integer.valueOf(dateArr[0]);
        int month = Integer.valueOf(dateArr[1]);
        int day = Integer.valueOf(dateArr[2]);

        result.put("year", year);
        result.put("month", month);
        result.put("day", day);
        return result;
    }

    /**
     * 修改个人基本信息
     */
    @ApiOperation(value = "修改个人基本信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public String updateUserInfo(String nickName, int sex, int year, int month, int day) {
        try {
            String date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = format.parse(date);
            userService.updateNickNameAndSexById(getUserId().toString(), nickName, sex);
            userService.update(getUserId(), "bir", birthday.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/mall/userCenter/user.do";
    }

    /**
     * 修改密码
     *
     * @param password
     * @return
     */
    @ApiOperation(value = "修改密码", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/modifyPassword")
    @ResponseBody
    public RespObj modifyPassword(String password) throws Exception {
        RespObj respObj = RespObj.FAILD;
        try {
            userService.updatePassword(getSessionValue().getId(), MD5Utils.getMD5(password));
            respObj = RespObj.SUCCESS;
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("修改失败！");
        }

        return respObj;
    }
    @ApiOperation(value = "ValidateUser", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/validateUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ValidateUser(String userName, String cacheKeyId, String code, String phoneNumber,
                                            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", 500);
        Map<String, String> map = new HashMap<String, String>();


        UserEntry ue = userService.findByUserName(userName);

        if (null == ue) {
            model.put("message", "用户名错误");
            return model;
        }

        if (!ValidationUtils.isMobile(phoneNumber)) {
            model.put("message", "手机非法");
            return model;
        }

        UserEntry mobileEntry = userService.findByMobile(phoneNumber);

        if (null != mobileEntry && !mobileEntry.getUserName().toLowerCase().equals(ue.getUserName())) {
            model.put("message", "此手机已经被占用");
            return model;
        }
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
            model.put("message", "验证码失效或者为空，请重新获取");
            return model;
        }
        String[] cache = value.split(",");
        if (!cache[1].equals(phoneNumber)) {
            model.put("message", "验证失败：手机号码与验证码不匹配");
            return model;
        }

        if (cache[0].equals(code)) {
            model.put("message", "身份验证成功");
            model.put("code", 200);
        } else {
            model.put("message", "身份验证失败");
        }
        return model;
    }

}
