package com.fulaan.forum.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.forum.service.*;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.activity.FriendApply;
import com.pojo.app.SessionValue;
import com.pojo.forum.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserForumDTO;
import com.pojo.user.UserRole;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangkaidong on 2016/5/31.
 * <p/>
 * 论坛个人中心Controller
 */
@Api(value="论坛个人中心Controller",hidden = true)
@Controller
@RequestMapping("/jxmapi/forum/userCenter")
public class DefaultUserCenterController extends BaseController {

    private static final Logger logger = Logger.getLogger(DefaultUserCenterController.class);

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;
    @Autowired
    private FCollectionService fCollectionService;
    @Autowired
    private FMissionService fMissionService;
    @Resource
    private FriendApplyService friendApplyService;
    @Resource
    private FriendService friendService;
    @Autowired
    private FInformationService fInformationService;
    @Autowired
    private FLevelService fLevelService;
    @Autowired
    private FVoteService fVoteService;
    @Autowired
    private FPostService fPostService;
    @Autowired
    private FReplyService fReplyService;
    @Autowired
    private FScoreService fScoreService;

    private void loginInfo(Map<String, Object> model) {
        SessionValue sessionValue = getSessionValue();
        model.put("userName", sessionValue.getUserName());
        model.put("userId", sessionValue.getId());
        model.put("login", true);
        model.put("avatar", sessionValue.getMinAvatar());
    }
    @ApiOperation(value = "userPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/user")
    @LoginInfo
    public String userPage(Map<String, Object> model) {
        loginInfo(model);
        List<FScoreDTO> fScoreDTOs = fScoreService.getFScoreByPersonId(getSessionValue().getId());
        model.put("scores", fScoreDTOs);
        UserEntry user = userService.findById(getUserId());
        model.put("formScore", user.getForumScore());
        model.put("formExpermence", user.getForumExperience());
        return "/forum/user";
    }
    @ApiOperation(value = "manageCenter", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/manageCenter")
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public String manageCenter(Map<String, Object> model) {
        loginInfo(model);
        model.put("Item", 0);
        return "/forum/manageCenter";
    }

    /**
     * 举报信息查找
     *
     * @param sortType
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "举报信息查找", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/reported")
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public Map<String, Object> reportedList(int reported, int sortType, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<FPostDTO> fPostDTOList = fPostService.getFPostByReported(reported, sortType, page, pageSize);
        int count = fPostService.getFPostByReported(reported);
        model.put("list", fPostDTOList);
        model.put("count", count);
        return model;
    }

    /**
     * 用户管理数据查询
     *
     * @param userId
     * @param sortType
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户管理数据查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/getForumUser")
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public Map<String, Object> getForumUser(String userId, int sortType, int page, int pageSize) {
        List<ObjectId> userList = getObjectIdList(userId);
        Map<String, Object> model = new HashMap<String, Object>();
        List<UserForumDTO> userForumDTOList = userService.findForumInfoByIds(userList, sortType, page, pageSize);
        int count = userService.getForumInfoCount(userList);
        model.put("list", userForumDTOList);
        model.put("count", count);
        return model;
    }

    /**
     * 后台批量逻辑处理帖子数据
     *
     * @param postId
     * @return
     */
    @ApiOperation(value = "后台批量逻辑处理帖子数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/FPostLogic")
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj FPostLogic(String postId) {
        try {
            List<ObjectId> postList = getObjectIdList(postId);
            fPostService.FPostLogic(postList);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("批量逻辑处理帖子数据失败！");
        }
        return RespObj.SUCCESS;
    }

    private List<ObjectId> getObjectIdList(String silptString) {
        List<ObjectId> userList = new ArrayList<ObjectId>();
        if (silptString.contains(",")) {
            String[] users = silptString.split(",");
            for (String item : users) {
                userList.add(new ObjectId(item));
            }
        } else {
            userList.add(new ObjectId(silptString));
        }
        return userList;
    }

    /**
     * 后台批量逻辑删除用户数据
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "后台批量逻辑删除用户数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeUserLogic")
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj removeUserLogic(String userId) {
        try {
            List<ObjectId> userList = getObjectIdList(userId);
            userService.removeUserLogic(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("批量逻辑删除用户数据失败！");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 后台彻底删除数据
     */
    @ApiOperation(value = "后台彻底删除数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removePostData")
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj removePostData(String postId) {
        try {
            List<ObjectId> postList = getObjectIdList(postId);
            fPostService.recoverFPostLogic(postList, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("删除失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 批量恢复帖子数据
     *
     * @param postId
     * @return
     */
    @ApiOperation(value = "批量恢复帖子数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/recoverFPostLogic")
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj recoverFPostLogic(String postId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<ObjectId> postList = getObjectIdList(postId);
            fPostService.recoverFPostLogic(postList, 0);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("批量逻辑处理帖子数据失败！");
        }
        return respObj;
    }

    /**
     * 根据星星数获取最小经验值
     *
     * @param stars
     * @return
     */
    @ApiOperation(value = "根据星星数获取最小经验值", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/getMinLevel", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getMinLevel(String stars) {
        Map<String, Object> model = new HashMap<String, Object>();
        long minLevel = fLevelService.getMinLevel(Long.parseLong(stars));
        model.put("minLevel", minLevel);
        return model;
    }

    /**
     * 更新用户经验值
     *
     * @param userId
     * @param exp
     * @return
     */
    @ApiOperation(value = "更新用户经验值", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/updateForumExp", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateForumExp(String userId, String exp) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            if ("".equals(exp)) {
                exp = "0";
            }
            userService.setForumExperience(new ObjectId(userId), Long.parseLong(exp));
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新经验值失败！");
        }
        return respObj;
    }

    /**
     * 更新用户的禁言状态
     *
     * @param userId
     * @param together
     * @return
     */
    @ApiOperation(value = "更新用户的禁言状态", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/updateForumSilenced", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj updateForumSilenced(String userId, String together) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            UserEntry userEntry = userService.findById(new ObjectId(userId));
            if (together.contains(",")) {
                String[] item = together.split(",");
                userEntry.setSilencedStatus(Integer.parseInt(item[0]));
                userEntry.setSilencedReason(item[1]);
                userEntry.setSilencedTen(Integer.parseInt(item[2]));
                if (Integer.parseInt(item[2]) == 0) {
                    userEntry.setSilencedTime(0L);
                } else {
                    long time = 0L;
                    if ("30".equals(item[2])) {
                        time = 24 * 60 * 60 * 1000 * 30L;
                    } else if ("180".equals(item[2])) {
                        time = 24 * 60 * 60 * 1000 * 180L;
                    } else {
                        time = 24 * 60 * 60 * 1000 * Integer.parseInt(item[2]);
                    }
                    long banTime = System.currentTimeMillis() + time;
                    userEntry.setSilencedTime(banTime);
                }
            } else {
                userEntry.setSilencedStatus(Integer.parseInt(together));
                userEntry.setSilencedReason("");
                userEntry.setSilencedTen(-1);
                userEntry.setSilencedTime(0L);
            }
            userService.addUser(userEntry);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新用户禁言状态失败！");
        }
        return respObj;
    }

    /**
     * 批量处理删除举报信息
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "批量处理删除举报信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/removeFReported", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj removeFReported(String params) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<ReportedDTO> reportedDTOList = new ArrayList<ReportedDTO>();
            if (params.contains("$")) {
                String[] fParams = params.split("\\$");
                for (String param : fParams) {
                    FPostDTO fPostDTO = fPostService.detail(new ObjectId(param));
                    fPostDTO.setReportedDTOList(reportedDTOList);
                    fPostDTO.setReported(3);
                    fPostService.addFPostEntry(fPostDTO);
                }
            } else {
                FPostDTO fPostDTO = fPostService.detail(new ObjectId(params));
                fPostDTO.setReported(3);
                fPostDTO.setReportedDTOList(reportedDTOList);
                fPostService.addFPostEntry(fPostDTO);
            }
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("删除举报信息失败！");
        }
        return respObj;
    }


    /**
     * 批量处理举报信息
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "批量处理举报信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/addFReported", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj addFReported(String params) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            if (params.contains("$")) {
                String[] fParams = params.split("\\$");
                for (String param : fParams) {
                    String[] myItem = param.split(",");
                    FPostDTO fPostDTO = fPostService.detail(new ObjectId(myItem[0]));
                    if (!"".equals(myItem[1])) {
                        fPostDTO.setReportedExperience(Integer.parseInt(myItem[1]));
                        userService.updateForumExperience(new ObjectId(fPostDTO.getReportedDTOList().get(0).getUserId()), Long.parseLong(myItem[1]));
                    }
                    String str = myItem[2];
                    fPostDTO.setReportedComment(myItem[2]);
                    fPostDTO.setUpdateTime(System.currentTimeMillis());
                    fPostDTO.setReported(2);
                    fPostDTO.getReportedDTOList().get(0).setTime(System.currentTimeMillis());
                    fPostService.addFPostEntry(fPostDTO);
                    if ("未留言".equals(myItem[2])) {
                        str = "";
                    }
                    //发送系统消息
                    fInformationService.addFInformation(new ObjectId("568dd46a0cf2316dfff62d81"), fPostDTO.getReportedDTOList().get(0).getUserId(), 1, str, 0);

                }
            } else {
                String[] myItem = params.split(",");
                FPostDTO fPostDTO = fPostService.detail(new ObjectId(myItem[0]));
                if (!"".equals(myItem[1])) {
                    fPostDTO.setReportedExperience(Integer.parseInt(myItem[1]));
                    userService.updateForumExperience(new ObjectId(fPostDTO.getReportedDTOList().get(0).getUserId()), Long.parseLong(myItem[1]));
                }
                String str = myItem[2];
                fPostDTO.setReportedComment(myItem[2]);
                fPostDTO.setReported(2);
                fPostDTO.setUpdateTime(System.currentTimeMillis());
                fPostDTO.getReportedDTOList().get(0).setTime(System.currentTimeMillis());
                fPostService.addFPostEntry(fPostDTO);
                //发送系统消息
                fInformationService.addFInformation(new ObjectId("568dd46a0cf2316dfff62d81"), fPostDTO.getReportedDTOList().get(0).getUserId(), 1, str, 0);
            }
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("处理举报信息失败！");
        }
        return respObj;
    }

    /**
     * 获取用户个人信息
     */
    @ApiOperation(value = "获取用户个人信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserInfo() {
        UserDetailInfoDTO user = userService.getUserInfoById(getUserId().toString());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("name", user.getUserName());
        result.put("nickName", user.getNickName());
        result.put("sex", user.getSex());
        result.put("phone",getProtectedMobile(user.getMobileNumber()));
        Date birthdate = user.getBirthDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(birthdate);
        String[] dateArr = date.split("-");
        int year = Integer.valueOf(dateArr[0]);
        int month = Integer.valueOf(dateArr[1]);
        int day = Integer.valueOf(dateArr[2]);

        result.put("year", year);
        result.put("month", month);
        result.put("day", day);
        result.put("avatar",user.getImgUrl());

        result.put("email",user.getEmail());
        return result;
    }

    /**
     * 修改个人基本信息
     */
    @ApiOperation(value = "修改个人基本信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateUserInfo(String nickName, int sex, int year, int month, int day) {
        try {
            String date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = format.parse(date);
            userService.updateNickNameAndSexById(getUserId().toString(), nickName, sex);
            userService.update(getUserId(), "bir", birthday.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD(e.getMessage());
        }
        return RespObj.SUCCESS("编辑成功！");
    }

    /**
     * 更新昵称
     *
     * @param nickName
     * @return
     */
    @ApiOperation(value = "更新昵称", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/updateUserNickName")
    @ResponseBody
    public RespObj updateUserNickName(String nickName,Map<String, Object> model) {
        try {
            userService.updateNickNameById(getUserId().toString(), nickName);
            SessionValue sv = getSessionValue();
            sv.setRealName(nickName);
            String userKey = CacheHandler.getUserKey(sv.getId());
            CacheHandler.cacheSessionValue(userKey,
                    sv, Constant.SECONDS_IN_DAY);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD(e.getMessage());
        }
        return RespObj.SUCCESS("编辑成功！");
    }

    /**
     * 获取该用户的任务情况
     *
     * @return
     */
    @ApiOperation(value = "获取该用户的任务情况", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/getMission")
    @ResponseBody
    public Map<String, Object> getMission() {
        ObjectId userId = getUserId();
        return fMissionService.findTodayMissionByUserId(userId.toString());
    }

    /**
     * 获取论坛等级列表
     *
     * @return
     */
    @ApiOperation(value = "获取论坛等级列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = List.class)})
    @RequestMapping(value = "/getFLevel", method = RequestMethod.POST)
    @ResponseBody
    public List<FLevelDTO> getFLevel() {
        return fLevelService.getFLevelInf();
    }

    /**
     * 添加论坛等级信息
     */
    @ApiOperation(value = "添加论坛等级信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/removeFLevelById", method = RequestMethod.POST)
    @ResponseBody
    public RespObj removeFLevelById(String id) {
        try {
            fLevelService.removeFLevel(id);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("更新失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 通过Id获取论坛等级
     *
     * @return
     */
    @ApiOperation(value = "通过Id获取论坛等级", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = FLevelDTO.class)})
    @RequestMapping(value = "/getFLevelById")
    @ResponseBody
    public FLevelDTO getFLevelById(String id) {
        return fLevelService.getFLevelById(id);
    }

    /**
     * 添加论坛等级信息
     */
    @ApiOperation(value = "添加论坛等级信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/addFLevel", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFLevel(@RequestParam(required = false, defaultValue = "") String id,
                             String level, String startLevel, String endLevel, String stars) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            FLevelDTO fLevelDTO = new FLevelDTO();
            if (StringUtils.isNotBlank(id)) {
                fLevelDTO.setId(id);
            }
            fLevelDTO.setLevel(level);
            fLevelDTO.setStartLevel(Long.parseLong(startLevel));
            fLevelDTO.setEndLevel(Long.parseLong(endLevel));
            fLevelDTO.setStars(Integer.parseInt(stars));
            fLevelService.addFLevel(fLevelDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新失败");
        }
        return respObj;
    }

    /**
     * 添加论坛投票贴信息
     */
    @ApiOperation(value = "添加论坛投票贴信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/addFVote", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFVote(String number, String voteId) {
//        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        RespObj respObj = null;
        try {
            SessionValue sv = getSessionValue();
            String userId = "";
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
            }
            FVoteEntry entry=fVoteService.getFVote(voteId,userId);
            if(null!=entry){
                respObj = RespObj.FAILD;
                respObj.setErrorMessage("已经投过票了");
               return  respObj;
            }else {
                List<FVoteEntry> fVoteEntryList = new ArrayList<FVoteEntry>();
                if (number.contains(",")) {
                    String[] nu = number.split(",");
                    for (String myItem : nu) {
                        FVoteEntry fVoteEntry = new FVoteEntry();
                        fVoteEntry.setNumber(Integer.parseInt(myItem));
                        fVoteEntry.setUserId(new ObjectId(userId));
                        fVoteEntry.setVoteId(new ObjectId(voteId));
                        fVoteEntryList.add(fVoteEntry);
                    }
                    fVoteService.addFVoteList(fVoteEntryList);
                } else {
                    FVoteDTO fVoteDTO = new FVoteDTO();
                    fVoteDTO.setNumber(Integer.parseInt(number));
                    fVoteDTO.setUserId(userId);
                    fVoteDTO.setVoteId(voteId);
                    fVoteService.addFVote(fVoteDTO);
                }
                respObj = RespObj.SUCCESS;
                respObj.setMessage("成功");
                return  respObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("更新失败");
            return  respObj;
        }
    }

    /**
     * @param content
     * @param personId
     * @return
     */
    @ApiOperation(value = "sendInf", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/sendInf", method = RequestMethod.POST)
    @ResponseBody
    public RespObj sendInf(String content, String personId) {
        try {
            SessionValue sv = getSessionValue();
            String userId = "";
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
            }
            fInformationService.addFInformation(new ObjectId(userId), personId, 0, content, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("更新失败");
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
    @RequestMapping(value = "/applyFriend", method = RequestMethod.POST)
    @ResponseBody
    public RespObj friendApplyList(String content, String personId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            SessionValue sv = getSessionValue();
            String userId = "";
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
            }
            List<FriendApply> friendApplyList = friendApplyService.findApplyBySponsorIdAndRespondentId(userId, personId);
            boolean k = friendService.isFriend(personId, userId);
            if (k) {
                respObj.setMessage("已经是好友了");
            } else {
                if (null == friendApplyList || friendApplyList.isEmpty()) {
                    friendApplyService.insertApply(content, userId, personId);
                    respObj.setCode(Constant.SUCCESS_CODE);
                } else {
                    respObj.setMessage("已经申请好友了！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新失败");
        }
        return respObj;
    }

    /**
     * 获取好友申请列表
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "获取好友申请列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/friendList")
    @ResponseBody
    public Map<String, Object> friendList(String personId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<FriendApply> friendApplyList = friendApplyService.findFriendApplyListByCondition(personId);
        model.put("list", friendApplyList);
        return model;
    }

    /**
     * 获取发送消息列表
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "获取发送消息列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/fMessageList")
    @ResponseBody
    public Map<String, Object> fMessageList(String personId) {

        SessionValue sv = getSessionValue();
        String userId = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
        }

        Map<String, Object> model = new HashMap<String, Object>();
        List<FInformationDTO> fInformationDTOList = fInformationService.getInformationFirst(userId, new ObjectId(personId), 0);
        model.put("list", fInformationDTOList);
        return model;
    }

    /**
     * 获取系统消息列表
     *
     * @return
     */
    @ApiOperation(value = "获取系统消息列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/fSystemList")
    @ResponseBody
    public Map<String, Object> fSystemList() {
        ObjectId uid = getUserId();
        Map<String, Object> model = new HashMap<String, Object>();
        List<FInformationDTO> fInformationDTOList = fInformationService.getSystemInf(uid);
        model.put("list", fInformationDTOList);
        return model;
    }


    /**
     * 删除消息
     *
     * @param messageId
     * @return
     */
    @ApiOperation(value = "删除消息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeMessage")
    @ResponseBody
    public RespObj removeMessage(String messageId) {
        RespObj respObj = RespObj.FAILD;
        try {
            fInformationService.removeFInformation(new ObjectId(messageId));
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("删除消息失败");
        }
        return respObj;
    }

    /**
     * 全部删除
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "全部删除", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeInf")
    @ResponseBody
    public RespObj removeMessageByPerson(String personId) {
        try {
            SessionValue sv = getSessionValue();
            String userId = sv.getId();
            fInformationService.remove(new ObjectId(personId), new ObjectId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("删除消息失败");
        }

        return RespObj.SUCCESS;
    }


    /**
     * 获取发送消息详细列表
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "获取发送消息详细列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/fMessagesList")
    @ResponseBody
    public Map<String, Object> fMessagesList(String personId, int type) {
        Map<String, Object> model = new HashMap<String, Object>();
        SessionValue sv = getSessionValue();
        String userId = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
        }

        List<FInformationDTO> fInformationDTOList = fInformationService.getInformation(type, new ObjectId(personId), new ObjectId(userId), 0);
        fInformationService.updateScan(new ObjectId(personId), new ObjectId(userId), 0);
        model.put("personId", personId);
        model.put("list", fInformationDTOList);
        model.put("count", fInformationDTOList.size());
        return model;
    }

    /**
     * 获取好友申请数
     *
     * @return
     */
    @ApiOperation(value = "获取好友申请数", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/countApplyFriend", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> countApplyFriend() {
        Map<String, Object> model = new HashMap<String, Object>();
        SessionValue sv = getSessionValue();
        String userId = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
        }
        int applyCount = friendApplyService.countNoResponseReply(userId);

        model.put("applyCount", applyCount);
        return model;
    }


    /**
     * 获取消息提醒数
     *
     * @return
     */
    @ApiOperation(value = "获取消息提醒数", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/countMessage", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> countMessage() {
        Map<String, Object> model = new HashMap<String, Object>();
        SessionValue sv = getSessionValue();
        String userId = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
        }
        int messageCount = fInformationService.getCount(new ObjectId(userId), 0, 0);
        model.put("messageCount", messageCount);
        return model;
    }

    /**
     * 获取系统消息未浏览数
     *
     * @return
     */
    @ApiOperation(value = "获取系统消息未浏览数", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/countSystem", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> countSystem() {
        Map<String, Object> model = new HashMap<String, Object>();
        SessionValue sv = getSessionValue();
        String userId = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
        }
        int systemCount = fInformationService.getCount(new ObjectId(userId), 1, 0);

        model.put("systemCount", systemCount);
        return model;
    }

    /**
     * 签到
     *
     * @return
     */
    @ApiOperation(value = "签到", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    @ResponseBody
    public RespObj signIn() {
        try {
            SessionValue sv = getSessionValue();
            String userId = sv.getId();
            ;
            boolean isSign = fMissionService.isSign(userId);
            if (isSign) {
                return RespObj.FAILD("已经签到了");
            } else {
                fMissionService.sign(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("更新失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * \
     * 抽奖
     *
     * @param count
     * @return
     */
    @ApiOperation(value = "抽奖", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/welfare", method = RequestMethod.POST)
    @ResponseBody
    public RespObj welfare(int count) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            SessionValue sv = getSessionValue();
            String userId = "";
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
            }
            boolean isWelfare = fMissionService.isWelfare(userId);
            if (isWelfare) {
                respObj.setMessage("已经抽奖了");
            } else {
                fMissionService.welfare(userId);
                //抽奖得到的经验值
                userService.updateForumScore(new ObjectId(userId), count);
                FScoreDTO fScoreDTO = new FScoreDTO();
                fScoreDTO.setTime(System.currentTimeMillis());
                fScoreDTO.setType(2);
                fScoreDTO.setOperation("抽奖");
                fScoreDTO.setPersonId(userId);
                fScoreDTO.setScoreOrigin("中奖");
                fScoreDTO.setScore(count);
                fScoreService.addFScore(fScoreDTO);
                userService.updateForumExperience(new ObjectId(userId), count);
                respObj.setCode(Constant.SUCCESS_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新失败");
        }

        return respObj;
    }

    /**
     * 修改个人基本信息
     *
     * @param nickName
     * @return
     */
    @ApiOperation(value = "修改个人基本信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成")})
    @RequestMapping("/existUserInfo")
    @ResponseBody
    public boolean existUserInfo(String nickName) {
        SessionValue sv = getSessionValue();
        return userService.existUserInfo("", sv.getId(), nickName);
    }

    /**
     * 收藏页
     */
    @ApiOperation(value = "收藏页", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/collection")
    public String collectionPage(Map<String, Object> model) {
        loginInfo(model);
        return "/forum/forumCollect";
    }

    /**
     * 消息页
     */
    @ApiOperation(value = "消息页", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/message")
    public String messagePage(Map<String, Object> model) {
        loginInfo(model);
        return "/forum/message";
    }

    /**
     * 查询收藏
     */
    @ApiOperation(value = "查询收藏", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/getCollections")
    @ResponseBody
    public Map<String, Object> getCollections(int type) {
        List<FCollectionDTO> collectionDTOList = fCollectionService.getCollections(getUserId(), type);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", collectionDTOList);
        return result;
    }

    /**
     * 查询收藏
     */
    @ApiOperation(value = "查询收藏", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/getAppCollect")
    @ResponseBody
    public Map<String, Object> getAppCollect(int type) {
        List<FAppCollectPostDTO> collectionDTOList = fCollectionService.getAppCollect(getUserId(), type);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", collectionDTOList);
        return result;
    }


    /**
     * 取消收藏
     *
     * @param collectionId
     * @return
     */
    @ApiOperation(value = "取消收藏", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
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

    /**
     * 接受好友申请
     *
     * @param applyId
     * @return
     */
    @ApiOperation(value = "接受好友申请", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/acceptFriend")
    @ResponseBody
    public RespObj acceptFriend(String applyId) {
        try {
            friendApplyService.acceptApply(applyId);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("添加好友失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 拒绝好友申请
     *
     * @param applyId
     * @return
     */
    @ApiOperation(value = "拒绝好友申请", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/refuseFriend")
    @ResponseBody
    public RespObj refuseFriend(String applyId) {
        try {
            friendApplyService.refuseApply(applyId);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("拒绝好友失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 收藏帖子或板块
     */
    @ApiOperation(value = "收藏帖子或板块", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/addCollection", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addCollection(String postSectionId, int type) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            SessionValue sv = getSessionValue();
            String userId = "";
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
            }
            boolean isCollected = fCollectionService.isCollected(new ObjectId(userId), postSectionId);
            if (isCollected) {
                respObj.setMessage("已收藏");
            } else {
                fCollectionService.addCollection(new ObjectId(userId), postSectionId, type);
            }
            int count = fCollectionService.getCollectionCount(postSectionId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(count);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("取消收藏失败");
        }

        return respObj;
    }

    /**
     * 获取改帖子/板块收藏的人数
     *
     * @return
     */
    @ApiOperation(value = "获取改帖子/板块收藏的人数", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/getCountByCondition", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCountByCondition(String postSection) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("collectionCount", fCollectionService.getCollectionCount(postSection));
        return model;
    }

    /**
     * 返回给ueditor的内容
     */
    public static class ReturnVideo {
        private String original;
        private String size;
        private String state;
        private String title;
        private String type;
        private String url;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    //得到文件名
    private String getFileName(MultipartFile file) {
        String orgname = file.getOriginalFilename();

        return new ObjectId().toString() + Constant.POINT + orgname.substring(orgname.lastIndexOf(".") + 1);
    }

    /**
     * 上传图片到七牛
     */
    @ApiOperation(value = "上传图片到七牛", httpMethod = "POST", produces = "application/json")
    @RequestMapping("/uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IllegalParamException, FileUploadException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("upfile");
        ReturnVideo returnVideo = new ReturnVideo();
        String fileName = FilenameUtils.getName(file.getOriginalFilename());
        returnVideo.setOriginal(fileName);
        returnVideo.setTitle(fileName);
        returnVideo.setSize(String.valueOf(file.getSize()));
        returnVideo.setType(fileName.substring(fileName.lastIndexOf(".")));
        String examName = getFileName(file);
        RespObj upladTestPaper = QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
        if (upladTestPaper.getCode() != Constant.SUCCESS_CODE) {
            throw new FileUploadException();
        }
        String uploadFileUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, examName);

        returnVideo.setState("SUCCESS");
        returnVideo.setUrl(uploadFileUrl);
        String result = JSON.toJSONString(returnVideo);//这边就是为了返回给UEditor做的格式转换
        response.getWriter().write(result);
    }

    /**
     * 上传图片到七牛
     */
    @ApiOperation(value = "上传图片到七牛", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/uploadImage")
    public String uploadImage(HttpServletRequest request, HttpServletResponse response) throws IllegalParamException, FileUploadException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("upfile");
        ReturnVideo returnVideo = new ReturnVideo();
        String fileName = FilenameUtils.getName(file.getOriginalFilename());
        returnVideo.setOriginal(fileName);
        returnVideo.setTitle(fileName);
        returnVideo.setSize(String.valueOf(file.getSize()));
        returnVideo.setType(fileName.substring(fileName.lastIndexOf(".")));
        String examName = getFileName(file);
        RespObj upladTestPaper = QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
        if (upladTestPaper.getCode() != Constant.SUCCESS_CODE) {
            throw new FileUploadException();
        }
        returnVideo.setState("SUCCESS");
        returnVideo.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, examName));
        String result = JSON.toJSONString(returnVideo);//这边就是为了返回给UEditor做的格式转换
        response.getWriter().write(result);
        return null;
    }

    /**
     * 上传视频到七牛
     */
    @ApiOperation(value = "上传视频到七牛", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/uploadVideo")
    @SessionNeedless
    public String uploadVideo(HttpServletRequest request, HttpServletResponse response) throws IllegalParamException, IllegalStateException, IOException, EncoderException {
        response.setContentType("text/html;charset=UTF-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("upfile");

        Map map = new HashMap();
        ReturnVideo returnVideo = new ReturnVideo();
        String fileName = FilenameUtils.getName(file.getOriginalFilename());
        returnVideo.setOriginal(fileName);
        returnVideo.setTitle(fileName);
        returnVideo.setSize(String.valueOf(file.getSize()));

        //视频filekey
        String videoFilekey = new ObjectId().toString() + "." + FilenameUtils.getExtension(fileName);

        File savedFile = File.createTempFile(new ObjectId().toString(), "." + FilenameUtils.getExtension(fileName));
        OutputStream stream = new FileOutputStream(savedFile);
        stream.write(file.getBytes());
        stream.flush();
        stream.close();

        returnVideo.setType(fileName.substring(fileName.lastIndexOf(".")));

        String coverImage = new ObjectId().toString() + ".jpg";
        Encoder encoder = new Encoder();
        File screenShotFile = File.createTempFile("coverImage", ".jpg");
        long videoLength = 60000;//缺省一分钟
        //是否生成了图片
        boolean isCreateImage = false;
        try {
            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            videoLength = encoder.getInfo(savedFile).getDuration();
            isCreateImage = true;
        } catch (Exception ex) {
        }
        if (videoLength == -1) {
            videoLength = 60000;//获取不到时间就设为1分钟
        }
        //上传图片
        if (isCreateImage && screenShotFile.exists()) {
            QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);
        }


        VideoEntry ve = new VideoEntry(fileName, videoLength, VideoSourceType.USER_VIDEO.getType(), videoFilekey);
        ve.setID(new ObjectId());

        QiniuFileUtils.uploadVideoFile(ve.getID(), videoFilekey, file.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
        if (isCreateImage && screenShotFile.exists()) {
            ve.setImgUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage));
        }

        ObjectId videoId = videoService.addVideoEntry(ve);
        //删除临时文件
        try {
            savedFile.delete();
            screenShotFile.delete();
        } catch (Exception ex) {
            logger.error("", ex);
        }
        returnVideo.setState("SUCCESS");
        returnVideo.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()) + "$" + QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage));
        String result = JSON.toJSONString(returnVideo);//这边就是为了返回给UEditor做的格式转换
        response.getWriter().write(result);
        return null;
    }


    /**
     * 七牛视频下载
     */
    @ApiOperation(value = "七牛视频下载", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/m3u8ToMp4DownLoad")
    @ResponseBody
    public Map m3u8ToMp4DownLoad(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException {
        Map<String, Object> model = new HashMap<String, Object>();
        fReplyService.m3u8ToMp4DownLoad(filePath, request, response);
        return model;
    }

}
