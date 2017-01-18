package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.HttpClientUtils;
import fulaan.social.connect.Auth;
import fulaan.social.factory.AuthFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * create by Jerry 2016/8/17
 * wap 端的 controller
 */
@Controller
@RequestMapping("/wap")
public class WapController extends BaseController {


    private Auth qqAuth = AuthFactory.getQQAuth();

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
