package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.pojo.app.SessionValue;
import com.sys.constants.Constant;
import com.sys.utils.HttpClientUtils;
import fulaan.social.connect.Auth;
import fulaan.social.factory.AuthFactory;
import fulaan.social.model.AuthType;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Map;

/**
 * create by Jerry 2016/8/17
 * wap 端的 controller
 */
@Controller
@RequestMapping("/wap")
public class WapController extends BaseController {

    private Auth qqAuth = AuthFactory.getQQAuth();
    private Auth wechatAuth = AuthFactory.getAuth(AuthType.WECHAT);

    @RequestMapping("/page")
    @SessionNeedless
    public String wapPage(String page) {
        return "wap/" + page;
}

    @RequestMapping("/qq")
    @SessionNeedless
    public void qq(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.sendRedirect(qqAuth.getAuthUrl());
    }

    @RequestMapping("/wechat")
    @SessionNeedless
    public String wechat() {
        return "redirect:" + wechatAuth.getWapAuthUrl();
    }

    @RequestMapping("/third")
    @SessionNeedless
    public String third(@RequestHeader("User-Agent") String userAgent,
                        HttpServletResponse response) throws Exception{
        if (userAgent.indexOf("MicroMessenger") > 0) { //是微信浏览器
            return "redirect:wechat.do";
        } else {
            return "redirect:qq.do";
        }
    }

    @RequestMapping("/share")
    @SessionNeedless
    public String shareFPost(String postId, String replyId,
                             Map<String, Object> model,HttpServletResponse response) {
        model.put("postId", postId);
        model.put("replyId", replyId);
        //组装cookie
        String redirectUrl="http://www.fulaan.com/wap/share.do?postId="+postId+"&replyId="+replyId;
        SessionValue value = new SessionValue();
        value.put("redirectUrl", redirectUrl);
        ObjectId cacheKey = new ObjectId();
        CacheHandler.cacheSessionValue(cacheKey.toString(), value, Constant.SESSION_TEN_MINUTE);
        Cookie appShareCookie = new Cookie(Constant.APP_SHARE, cacheKey.toString());
        appShareCookie.setMaxAge(Constant.SECONDS_IN_DAY);
        appShareCookie.setPath(Constant.BASE_PATH);
        response.addCookie(appShareCookie);
        return "wap/post";
    }

    @RequestMapping("/download")
    @SessionNeedless
    public String downloadApp() {
        return "redirect:" + Constant.COLLECTION_MALL_MARKET_URL;
    }
}
