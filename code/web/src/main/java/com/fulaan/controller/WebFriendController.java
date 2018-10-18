package com.fulaan.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.communityValidate.service.ValidateInfoService;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.activity.FriendApply;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
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
@Api(value="好友 Controller")
@Controller
@RequestMapping("/web/friend")
public class WebFriendController extends BaseController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendApplyService friendApplyService;
    @Autowired
    private UserService userService;
    
    private ValidateInfoService validateInfoService = new ValidateInfoService();

    @ApiOperation(value = "getFriends", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getFriends")
    @ResponseBody
    public RespObj getFriends() {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        ObjectId uid = getUserId();
       // long start = System.currentTimeMillis();
        List<User> userList = friendService.getFrinds(uid);
       // System.out.println(userList.size());
       // long end = System.currentTimeMillis();
      //  System.out.print(end-start);
        respObj.setMessage(userList);
        return respObj;
    }



    @ApiOperation(value = "getChangeFriends", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getChangeFriends")
    @ResponseBody
    public RespObj getChangeFriends() {
        ObjectId uid = getUserId();
        return RespObj.SUCCESS(friendService.getFrinds(uid));
    }

    @ApiOperation(value = "getPartners", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getParters")
    @ResponseBody
    public RespObj getPartners() {
        ObjectId uid = getUserId();
        return RespObj.SUCCESS(friendService.getParters(uid));
    }

    /**
     * 接受好友申请
     *
     * @param applyId
     * @return
     */
    @ApiOperation(value = "接受好友申请", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/accept")
    @ResponseBody
    public RespObj acceptFriend(@ApiParam(name = "applyId", required = true, value = "ObjectId") @ObjectIdType ObjectId applyId) {
        friendApplyService.acceptApply(applyId.toString());
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 拒绝好友申请
     *
     * @param applyId
     * @return
     */
    @ApiOperation(value = "拒绝好友申请", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/refuse")
    @ResponseBody
    public RespObj refuseFriend(@ApiParam(name = "applyId", required = true, value = "ObjectId") @ObjectIdType ObjectId applyId) {
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
    @ApiOperation(value = "好友申请", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
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
            friendApplyService.updFriStatus(new ObjectId(personId),"0");
        }
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 获取好友申请列表
     *
     * @return
     */
    @ApiOperation(value = "获取好友申请列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/newFriends")
    @ResponseBody
    public Map<String, Object> friendList(@RequestParam(required = false, defaultValue = "0") int accpeted) {
        ObjectId uid = getUserId();
        Map<String, Object> model = new HashMap<String, Object>();
        List<FriendApply> friendApplyList = friendApplyService.findFriendApplyListByCondition(uid.toString(), accpeted);
        model.put("list", friendApplyList);
        return model;
    }
    
    @ApiOperation(value = "search", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
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
    @ApiOperation(value = "删除好友", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
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
    @ApiOperation(value = "判断是否是好友", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/isFriend")
    @ResponseBody
    public RespObj isFriend(@ApiParam(name = "friendId", required = true, value = "ObjectId") @ObjectIdType  ObjectId friendId) {
        ObjectId uid = getUserId();
        boolean isFriend = friendService.isFriend(uid.toString(), friendId.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFriend", isFriend);
        return RespObj.SUCCESS(map);
    }
    
    /**
     * 群组和好友待处理个数（添加好友或者申请加入群组用户未处理的个数）
     *
     * @return
     */
    @ApiOperation(value = "群组和好友待处理个数", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/getGrFrNum")
    @ResponseBody
    public Map<String, Object> getGrFrNum() {
    	Map<String, Object> model = new HashMap<String, Object>();
    	try {
    		 	ObjectId uid = getUserId();
    	        //查找好友tap键点击状态
    	        String status = friendApplyService.getfriendStatus(uid);
    	        //查找好友待处理个数
    	        List<FriendApply> friendApplyList = friendApplyService.getFriNum(uid);
    	        //查找群组申请加入待处理的个数
    	        int count = validateInfoService.countVals(uid);
    	        model.put("friendNum", friendApplyList.size() + "");
    	        model.put("groupNum", count + "");
    	        model.put("status", status);
    	        model.put("code", "200");
		} catch (Exception e) {
			model.put("code", "500");
		}
        return model;
    }
    
    /**
     * 更新好友点击tab键状态
     *
     * @return
     */
    @ApiOperation(value = "更新好友点击tab键状态", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/updFriStatus")
    @ResponseBody
    public Map updFriStatus(@RequestParam(defaultValue = "0", required = false) String status) {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId uid = getUserId();
        try {
        	friendApplyService.updFriStatus(uid,status);
            model.put("code", "200");
		} catch (Exception e) {
			model.put("code", "500");
		}
        return model;
    }
    
    /**
     * 获取好友申请列表(包括待处理和同意`拒绝 的)
     *
     * @return
     */
    @ApiOperation(value = "获取好友申请列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/newAllFriends")
    @ResponseBody
    public Map<String, Object> newAllFriends(@ApiParam(name="page",required = false,value = "页码")@RequestParam(defaultValue = "1", required = false) int page,
            @ApiParam(name="pageSize",required = false,value = "页数")@RequestParam(defaultValue = "5", required = false) int pageSize) {
        
    	ObjectId uid = getUserId();
        Map<String, Object> model = new HashMap<String, Object>();
        try {
        	  List<FriendApply> friendApplyList = friendApplyService.newAllFriends(uid.toString(),page,pageSize);
              model.put("list", friendApplyList);
              model.put("code", "200");
		} catch (Exception e) {
			 model.put("code", "500");
		}
        return model;
    }
   
}