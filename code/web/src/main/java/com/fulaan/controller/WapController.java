package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.user.util.QQLoginUtil;
import com.sys.constants.Constant;
import com.sys.utils.HttpClientUtils;
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

    @RequestMapping("/page")
    @SessionNeedless
    public String wapPage(String page) {
        return "wap/" + page;
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
