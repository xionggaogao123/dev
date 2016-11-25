package com.fulaan.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.user.service.UserService;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.pojo.activity.FriendApply;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/10/24.
 * 好友 Controller
 */
@Controller
@RequestMapping("/friend")
public class FriendController extends BaseController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendApplyService friendApplyService;
    @Autowired
    private UserService userService;

    @RequestMapping("/getFriends")
    @ResponseBody
    public RespObj getFriends() {
        ObjectId uid = getUserId();
        return RespObj.SUCCESS(friendService.getFrinds(uid));
    }

    @RequestMapping("/getParters")
    @ResponseBody
    public RespObj getParters() {
        ObjectId uid = getUserId();
        return RespObj.SUCCESS(friendService.getParters(uid));
    }

    /**
     * 接受好友申请
     *
     * @param applyId
     * @return
     */
    @RequestMapping("/accept")
    @ResponseBody
    public RespObj acceptFriend(@ObjectIdType ObjectId applyId) {
        friendApplyService.acceptApply(applyId.toString());
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 拒绝好友申请
     *
     * @param applyId
     * @return
     */
    @RequestMapping("/refuse")
    @ResponseBody
    public RespObj refuseFriend(@ObjectIdType ObjectId applyId) {
        try {
            friendApplyService.refuseApply(applyId.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("拒绝好友失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 好友申请
     *
     * @param content
     * @param personId
     * @return
     */
    @RequestMapping(value = "/apply")
    @ResponseBody
    public RespObj friendApplyList(@RequestParam(required = false, defaultValue = "") String content, String personId) {
        ObjectId uid = getUserId();
        List<FriendApply> friendApplyList = friendApplyService.findApplyBySponsorIdAndRespondentId(uid.toString(), personId);
        if (friendService.isFriend(personId, uid.toString())) {
            return RespObj.FAILD("已经是好友");
        }
        if (null == friendApplyList || friendApplyList.isEmpty()) {
            friendApplyService.insertApply(content, uid.toString(), personId);
        }
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 获取好友申请列表
     *
     * @return
     */
    @RequestMapping("/newFriends")
    @ResponseBody
    public Map<String, Object> friendList(@RequestParam(required = false, defaultValue = "0") int accpeted) {
        ObjectId uid = getUserId();
        Map<String, Object> model = new HashMap<String, Object>();
        List<FriendApply> friendApplyList = friendApplyService.findFriendApplyListByCondition(uid.toString(), accpeted);
        model.put("list", friendApplyList);
        return model;
    }

    @RequestMapping("/search")
    @ResponseBody
    public RespObj search(String relax) {
        return RespObj.SUCCESS(userService.findByRegular(relax));
    }

    /**
     * 删除好友
     *
     * @param userIds
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public RespObj deleteFriend(@RequestParam String userIds) {
        String userId = getUserId().toString();
    /* 删除好友关系 */
        String[] friendList = userIds.split(",");
        for (String friendId : friendList) {
            friendService.deleteByUserId1AndUserId2(userId, friendId);
        }
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 判断是否是好友
     *
     * @param friendId
     * @return
     */
    @RequestMapping("/isFriend")
    @ResponseBody
    public RespObj isFriend(@ObjectIdType ObjectId friendId) {
        ObjectId uid = getUserId();
        boolean isFriend = friendService.isFriend(uid.toString(), friendId.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFriend", isFriend);
        return RespObj.SUCCESS(map);
    }
}