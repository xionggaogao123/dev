package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.pojo.app.SessionValue;
import com.sys.constants.Constant;
import com.sys.utils.HttpClientUtils;
import fulaan.social.connect.Auth;
import fulaan.social.factory.AuthFactory;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * create by Jerry 2016/8/17
 * wap 端的 controller
 */
@Controller
@RequestMapping("/wap")
public class WapController extends BaseController {

    private Auth qqAuth = AuthFactory.getQQAuth();
    private Auth wechatAuth = AuthFactory.getWechatAuth();

    @RequestMapping("/page")
    @SessionNeedless
    public String wapPage(String page) {
        return "wap/" + page;
}

    @RequestMapping("/qq")
    @SessionNeedless
    public String qq() {
        return "redirect:" + qqAuth.getAuthUrl();
    }

    @RequestMapping("/wechat")
    @SessionNeedless
    public String wechat() {
        return "redirect:" + wechatAuth.getAuthUrl();
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
