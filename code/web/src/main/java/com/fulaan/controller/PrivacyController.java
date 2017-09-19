package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jerry on 2016/9/22.
 * 关于我们
 */
@Api(value="关于我们",hidden = true)
@Controller
public class PrivacyController extends BaseController {

    @RequestMapping("/aboutus/k6kt")
    @SessionNeedless
    public String aboutUs() {
        return "/privacy/aboutUs";
    }

    @RequestMapping("/service/k6kt")
    @SessionNeedless
    public String service() {
        return "/privacy/service";
    }

    @RequestMapping("/privacy/k6kt")
    @SessionNeedless
    public String privacy() {
        return "/privacy/privacy";
    }

    @RequestMapping("/contactus/k6kt")
    @SessionNeedless
    public String contactus() {
        return "/privacy/contact";
    }
}
