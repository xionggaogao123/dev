package com.fulaan.groupgather.controller;

import com.fulaan.base.BaseController;
import com.fulaan.groupgather.pojo.User;
import com.fulaan.groupgather.serviceImpl.UserServiceImpl;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Console;

/**
 * Created by scott on 2017/12/22.
 */
@Controller
@RequestMapping("/groupgather")
public class GroupGatherController extends BaseController{

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/saveUser")
    @ResponseBody
    public RespObj saveUser(String userName){
        User user = new User();
        user.setUserId(new ObjectId().toString());
        user.setUserName(userName);
        userService.saveUser(user);
        return new RespObj(Constant.SUCCESS_CODE,"保存数据成功");
    }

    @RequestMapping("/findByUserId")
    @ResponseBody
    public RespObj findByUserId(String userId){
        User user=userService.findUserByUserId(userId);
        return new RespObj(Constant.SUCCESS_CODE,user);
    }

}
