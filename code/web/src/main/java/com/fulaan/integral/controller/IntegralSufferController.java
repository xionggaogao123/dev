package com.fulaan.integral.controller;

import com.fulaan.base.BaseController;
import com.fulaan.integral.service.IntegralSufferService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by James on 2018-04-16.
 */
@Api(value="积分经验值")
@Controller
@RequestMapping(value="/jxmapi/integral")
public class IntegralSufferController extends BaseController {
    @Autowired
    private IntegralSufferService integralSufferService;

}
