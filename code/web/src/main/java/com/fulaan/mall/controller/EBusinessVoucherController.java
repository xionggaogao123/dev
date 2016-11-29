package com.fulaan.mall.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.controller.BaseController;
import com.fulaan.mall.service.EBusinessVoucherService;
import com.pojo.ebusiness.EVoucherDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/3/7.
 */
@Controller
@RequestMapping("/mall/vouchers")
public class EBusinessVoucherController extends BaseController {

    @Autowired
    private EBusinessVoucherService eBusinessVoucherService;


    /**
     * 优惠券
     *
     * @param state 优惠券状态   unused：未使用    used：已使用    expiration：已过期   默认 unused
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getVouchersByUserId(@RequestParam(required = false, defaultValue = "unused") String state) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<EVoucherDTO> eVoucherDTOs = eBusinessVoucherService.getEVouchersByUserIdAndState(getUserId(), state);
        model.put("vouchers", eVoucherDTOs);
        return model;
    }

    @RequestMapping(value = "/num/{voucherNo}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> RechargeVoucher(@PathVariable("voucherNo") String voucherNo) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eBusinessVoucherService.userRechargeVoucherToMyVoucherAccount(getUserId(), voucherNo);
            model.put("code", 200);
            model.put("msg", "充值成功");
        } catch (Exception e) {
            model.put("code", 500);
            model.put("msg", e.getMessage());
        }
        return model;
    }

    @RequestMapping(value = "/{voucherId}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> changeVoucherState(@PathVariable("voucherId") @ObjectIdType ObjectId voucherId, int state) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eBusinessVoucherService.updateVoucherState(voucherId, state);
            model.put("code", 200);
        } catch (Exception e) {
            model.put("code", 500);
            model.put("msg", e.getMessage());
        }
        return model;
    }
}