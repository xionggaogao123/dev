package com.fulaan.forum.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.forum.service.FCollectionService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.forum.FCollectionDTO;
import com.pojo.user.UserInfoDTO;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/5/31.
 * <p/>
 * 论坛个人中心Controller
 */
@Controller
@RequestMapping("/forum/userCenter")
public class UserCenterController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private FCollectionService fCollectionService;


    private void loginInfo(HttpServletRequest request, Map<String, Object> model) {
        SessionValue sessionValue = (SessionValue) request.getAttribute(BaseController.SESSION_VALUE);
        model.put("userName", sessionValue.getUserName());
        model.put("userId", sessionValue.getId());
        model.put("login", true);
        model.put("avatar", sessionValue.getMinAvatar());
    }

    /**
     * 个人信息页
     */

    @RequestMapping("/userInfo")
    public String userInfo(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        return "/forum/userinfo";
    }

    /**
     * 收藏页
     */
    @RequestMapping("/collection")
    public String collectionPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        return "/forum/collection";
    }

    /**
     * 消息页
     */
    @RequestMapping("/message")
    public String messagePage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        return "/forum/message";
    }


    /**
     * 查询个人信息
     */
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public Map<String, Object> getUserInfo() {
        UserInfoDTO userInfoDTO = userService.getUserInfoById(getUserId().toString());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userInfo", userInfoDTO);
        return result;
    }

    /**
     * 修改个人信息
     */
    @RequestMapping("/updateUserInfo")
    @ResponseBody
    public RespObj updateUserInfo(UserInfoDTO userInfoDTO) {
        RespObj respObj = RespObj.FAILD;
        try {
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新失败");
        }

        return respObj;
    }

    /**
     * 查询收藏
     */
    @RequestMapping("/getCollections")
    @ResponseBody
    public Map<String, Object> getCollections(int type) {
        List<FCollectionDTO> collectionDTOList = fCollectionService.getCollections(getUserId(), type);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("collections", collectionDTOList);
        return result;
    }

    /**
     * 取消收藏
     *
     * @param collectionId
     * @return
     */
    @RequestMapping("/removeCollection")
    @ResponseBody
    public RespObj cancelCollection(String collectionId) {
        RespObj respObj = RespObj.FAILD;
        try {
            fCollectionService.remove(collectionId);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("取消收藏失败");
        }

        return respObj;
    }


}
