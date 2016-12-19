package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.user.service.UserService;
import com.fulaan.user.util.QQLoginUtil;
import com.pojo.app.SessionValue;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.HttpClientUtils;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * create by Jerry 2016/8/17
 * wap 端的 controller
 */
@Controller
@RequestMapping("/wap")
public class WapController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("/page")
    @SessionNeedless
    public String wapPage(String page) {
        return "wap/" + page;
    }

    @RequestMapping("/login")
    @SessionNeedless
    @ResponseBody
    public RespObj loginWap(String name, String password, HttpServletResponse response, HttpServletRequest request) {
        UserEntry user = userService.login(name);
        if (user == null) {
            return RespObj.FAILD("accountError");
        }
        if (!userService.isValidPassword(password, user)) {
            return RespObj.FAILD("密码错误");
        }

        //更新本次活动时间
        userService.updateInterviewTimeValue(user.getID());

        if (!userService.isValidUser(user)) {
            return RespObj.FAILD("该用户已失效");
        }
        String ip = getIP();
        SessionValue value = userService.setCookieValue(ip, user, response, request);
        return RespObj.SUCCESS(value);
    }

    @RequestMapping("/third")
    @SessionNeedless
    public String third(String redirectUrl, @RequestHeader("User-Agent") String userAgent,
                        HttpServletResponse response) {

        if (StringUtils.isNotBlank(redirectUrl)) {
            SessionValue value = new SessionValue();
            value.put("redirectUrl", redirectUrl);
            ObjectId cacheKey = new ObjectId();
            CacheHandler.cacheSessionValue(cacheKey.toString(), value, Constant.SESSION_FIVE_MINUTE);
            Cookie appShareCookie = new Cookie(Constant.APP_SHARE, cacheKey.toString());
            appShareCookie.setMaxAge(Constant.SECONDS_IN_DAY);
            appShareCookie.setPath(Constant.BASE_PATH);
            response.addCookie(appShareCookie);
        }

        if (userAgent.indexOf("MicroMessenger") > 0) { //是微信浏览器
            return "redirect:wechat.do";
        } else {
            return "redirect:qq.do";
        }
    }

    @RequestMapping("/qq")
    @SessionNeedless
    public String qq() {
        String redirect_URI = HttpClientUtils.strURLEncodeUTF8(QQLoginUtil.getValue("redirect_URI").trim());
        String authorizeURL = QQLoginUtil.getValue("authorizeURL").trim();
        String app_ID = QQLoginUtil.getValue("app_ID").trim();
        String url = authorizeURL + "?client_id=" + app_ID + "&redirect_uri=" + redirect_URI + "&response_type=" + "code" + "&state=123456";
        return "redirect:" + url;
    }

    @RequestMapping("/wechat")
    @SessionNeedless
    public String wechat() {
        String urlEncodeRedirectUrl = HttpClientUtils.strURLEncodeUTF8(Constant.WECHAT_REDIRECT_URL);
        String strWeChatConnectUrl = String.format(Constant.WECHAT_THIRD_PART_CONNECT_URL, Constant.WECHAT_THIRD_PART_APPID,
                urlEncodeRedirectUrl, "123456");
        return "redirect:" + strWeChatConnectUrl;
    }

    @RequestMapping("/share")
    @SessionNeedless
    public String shareFPost(String postId, String replyId, Map<String, Object> model) {
        model.put("postId", postId);
        model.put("replyId", replyId);
        return "wap/post";
    }

    @RequestMapping("/download")
    @SessionNeedless
    public String downloadApp() {
        return "redirect:" + Constant.COLLECTION_MALL_MARKET_URL;
    }
}
