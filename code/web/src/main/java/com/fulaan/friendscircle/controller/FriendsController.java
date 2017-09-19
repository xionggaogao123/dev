package com.fulaan.friendscircle.controller;

import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.school.SchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.activity.FriendApply;
import com.pojo.app.RegionEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;

import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.*;

/**
 * Created by Hao on 15-2-26.
 */
@Api(value="friendcircle",hidden = true)
@Controller
@RequestMapping("/friendcircle")
public class FriendsController extends BaseController {

    private static final Logger logger = Logger.getLogger(FriendsController.class);


    @Resource
    private UserService userService;
    @Resource
    private FriendApplyService friendApplyService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private FriendService friendService;


    public void headInfo(Model model) {
        UserDetailInfoDTO user = userService.getUserInfoById(getUserId().toString());
        long role = user.getRole();
        if (UserRole.isStudentOrParent((int) role)) {//学生，家长
            user = userService.getUserInfoById(user.getId().toString());
            if (UserRole.isParent((int) role)) {
                user = userService.findStuInfoByParentId(user.getId().toString());
            }
            model.addAttribute("userInfo", user);
            model.addAttribute("template", "student");
            model.addAttribute("manager", UserRole.isManager((int) user.getRole()));
        } else {
            model.addAttribute("userInfo", user);
            model.addAttribute("template", "teacher");
            model.addAttribute("manager", UserRole.isManager((int) user.getRole()));
        }
    }

    /*
    *
    * 拒绝邀请
    *
    * */
    @RequestMapping("/reject")
    @ResponseBody
    public Map<String, Object> refuseApply(String applyId) {
        String userId = getUserId().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        FriendApply friendApply = friendApplyService.getFriendApplyDetail(applyId);
        boolean k = userId.equals(friendApply.getRespondent());
        if (!k) {
            map.put("code", Constant.FAILD_CODE);
            map.put("message", "非当前用户申请，非法操作");
            return map;
        }
        if (friendApply.getAccepted() != 0) {
            map.put("code", Constant.FAILD_CODE);
            map.put("message", "已经操作过，不能重复操作");
            return map;
        }
        friendApplyService.refuseApply(applyId);
        map.put("code", Constant.SUCCESS_CODE);
        map.put("message", "操作成功");
        return map;
    }

    /*
    *
    *
    *sponsorId 发起人ID
    * */
    @RequestMapping("accept")
    @ResponseBody
    public Map<String, Object> acceptApply(@RequestParam String sponsorId, String fromDevice) {
        String userId = getUserId().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        List<FriendApply> friendApplyList = friendApplyService.findApplyBySponsorIdAndRespondentId(sponsorId, userId);
        List<ObjectId> applyIds = new ArrayList<ObjectId>();
        if (friendApplyList == null || friendApplyList.isEmpty()) {
            map.put("code", Constant.FAILD_CODE);
            map.put("message", "非当前用户申请，非法操作");
            return map;
        } else {
            for (FriendApply friendApply : friendApplyList) {
                applyIds.add(new ObjectId(friendApply.getId()));
            }
        }
        friendApplyService.updateApplyByIds(applyIds);
        friendApplyService.acceptApply(friendApplyList.get(0).getId());
        map.put("code", Constant.SUCCESS_CODE);
        map.put("message", "操作成功");
        return map;
    }

    /*
    * 好友申请
    *
    * */
    @RequestMapping("applys")
    public String friendApplyList(Integer page, Model model) {
        headInfo(model);
        if (page == null) page = 0;
        String userId = getUserId().toString();
        int count = friendApplyService.countNoResponseReply(userId);
        if (page <= 0) page = 0;
        else {
            page = page - 1;
        }
        List<FriendApply> friendApplyList = friendApplyService.findFriendApplyList(userId, page * 10, 10);
        List<String> userIds = new ArrayList<String>();
        for (FriendApply friendApply : friendApplyList) {
            userIds.add(friendApply.getUserid());
        }
        Map<String, UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
        if (!userIds.isEmpty()) {
            List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByUserIds(userIds);
            if (userInfoDTO4WBList != null) {
                for (UserDetailInfoDTO userInfoDTO4WB : userInfoDTO4WBList) {
                    String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB.getId().toString());
                    String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB.getId().toString());
                    userInfoDTO4WB.setCityName(cityName);
                    userInfoDTO4WB.setSchoolName(schoolName);
                    map.put(userInfoDTO4WB.getId().toString(), userInfoDTO4WB);
                }
            }
        }
        for (FriendApply friendApply : friendApplyList) {
            UserDetailInfoDTO userInfoDTO4WB = map.get(friendApply.getUserid());
            if (userInfoDTO4WB == null) continue;

            friendApply.setUserName(userInfoDTO4WB.getUserName());
            friendApply.setUserRole(userInfoDTO4WB.getRole());
            friendApply.setImage(userInfoDTO4WB.getImgUrl());
            friendApply.setSchoolName(userInfoDTO4WB.getSchoolName());
            friendApply.setCityName(userInfoDTO4WB.getCityName());
            friendApply.setMainClassName(userInfoDTO4WB.getMainClassName());
        }
        int pageCount = count % 4 == 0 ? count / 4 : count / 4 + 1;
        model.addAttribute("total", count);
        model.addAttribute("bannerType", "userCenter");
        model.addAttribute("menuIndex", 3);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("page", page + 1);
        model.addAttribute("rows", friendApplyList);
        model.addAttribute("pageSize", 4);
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        model.addAttribute("currentUser", info);
        return "activity/friend-application";
    }

    @RequestMapping("getFriendApplys")
    @ResponseBody
    public Map<String, Object> getFriendApplyList(Integer page) {
        if (page == null) page = 0;
        String userId = getUserId().toString();
        int count = friendApplyService.countNoResponseReply(userId);
        if (page <= 0) page = 0;
        else {
            page = page - 1;
        }
        List<FriendApply> friendApplyList = friendApplyService.findFriendApplyList(userId, page * 10, 10);
        List<String> userIds = new ArrayList<String>();
        for (FriendApply friendApply : friendApplyList) {
            userIds.add(friendApply.getUserid());
        }
        Map<String, UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
        if (!userIds.isEmpty()) {
            List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByUserIds(userIds);
            if (userInfoDTO4WBList != null) {
                for (UserDetailInfoDTO userInfoDTO4WB : userInfoDTO4WBList) {
                    String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB.getId().toString());
                    String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB.getId().toString());
                    userInfoDTO4WB.setCityName(cityName);
                    userInfoDTO4WB.setSchoolName(schoolName);
                    map.put(userInfoDTO4WB.getId().toString(), userInfoDTO4WB);
                }
            }
        }
        for (FriendApply friendApply : friendApplyList) {
            UserDetailInfoDTO userInfoDTO4WB = map.get(friendApply.getUserid());
            if (userInfoDTO4WB == null) continue;

            friendApply.setUserName(userInfoDTO4WB.getUserName());
            friendApply.setUserRole(userInfoDTO4WB.getRole());
            friendApply.setImage(userInfoDTO4WB.getImgUrl());
            friendApply.setSchoolName(userInfoDTO4WB.getSchoolName());
            friendApply.setCityName(userInfoDTO4WB.getCityName());
            friendApply.setMainClassName(userInfoDTO4WB.getMainClassName());
        }
        int pageCount = count % 4 == 0 ? count / 4 : count / 4 + 1;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("total", count);
        model.put("bannerType", "userCenter");
        model.put("menuIndex", 3);
        model.put("pageCount", pageCount);
        model.put("page", page + 1);
        model.put("rows", friendApplyList);
        model.put("pageSize", 4);
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        model.put("currentUser", info);
        return model;
    }

    /*
    *
    * 移动端 收到的好友申请
    *
    * */
    @RequestMapping("friendApply")
    @ResponseBody
    public Map<String, Object> applies(@RequestParam Integer page, @RequestParam Integer pageSize) {
        if (page == null) page = 0;
        if (pageSize == null) pageSize = 4;
        String userId = getUserId().toString();
        int count = friendApplyService.countNoResponseReply(userId);
        if (page <= 0) page = 0;
        else {
            page = page - 1;
        }
        List<FriendApply> friendApplyList = friendApplyService.findFriendApplyList(userId, page * pageSize, pageSize);
        List<String> userIds = new ArrayList<String>();
        for (FriendApply friendApply : friendApplyList) {
            userIds.add(friendApply.getUserid());
        }
        Map<String, UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
        if (!userIds.isEmpty()) {
            List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByUserIds(userIds);
            if (userInfoDTO4WBList != null) {
                for (UserDetailInfoDTO userInfoDTO4WB : userInfoDTO4WBList) {
                    String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB.getId().toString());
                    String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB.getId().toString());
//                    String mainClassName = classService.findMainClassNameByUserId(userInfoDTO4WB.getId().toString());
                    userInfoDTO4WB.setCityName(cityName);
                    userInfoDTO4WB.setSchoolName(schoolName);
//                    userInfoDTO4WB.setMainClassName(mainClassName);
                    map.put(userInfoDTO4WB.getId().toString(), userInfoDTO4WB);
                }
            }
        }
        for (FriendApply friendApply : friendApplyList) {
            UserDetailInfoDTO userInfoDTO4WB = map.get(friendApply.getUserid());
            if (userInfoDTO4WB == null) continue;
            friendApply.setUserName(userInfoDTO4WB.getUserName());
            friendApply.setUserRole(userInfoDTO4WB.getRole());
            friendApply.setImage(userInfoDTO4WB.getImgUrl());
            friendApply.setSchoolName(userInfoDTO4WB.getSchoolName());
            friendApply.setCityName(userInfoDTO4WB.getCityName());
            friendApply.setMainClassName(userInfoDTO4WB.getMainClassName());
        }
        int pageCount = count % 4 == 0 ? count / 4 : count / 4 + 1;
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("total", count);
        returnMap.put("pageCount", pageCount);
        returnMap.put("page", page + 1);
        returnMap.put("rows", friendApplyList);
        returnMap.put("pageSize", pageSize);
        return returnMap;
    }

    /*
    * 好友申请数量
    *
    * */
    @RequestMapping("/friendApplyCount")
    @ResponseBody
    public Map<String, Object> friendApplyCountMap() {
        String userId = getUserId().toString();
        int total = friendApplyService.countNoResponseReply(userId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        return map;
    }

    /*
    * 好友列表
    *
    * */
    @RequestMapping("/friendlist")
    public String friends(Integer page, Model model) {
        if (page == null) page = 1;
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        List<UserDetailInfoDTO> userInfoList = null;
        long role = userInfoDTO4WB.getRole();
        userInfoList = friendService.selectAllFriend(userInfoDTO4WB.getId().toString());
        if (page < 1) page = 1;
        int total = userInfoList.size();
        if ((page - 1) * 12 > (userInfoList.size() - 1)) page = 1; //截取起始点超过 list大小重置page
        int begin = (page - 1) * 12;
        int end = page * 12;
        if (page * 12 > userInfoList.size()) end = userInfoList.size(); //截取结束点超过size 截取起始点到最后
        userInfoList = userInfoList.subList(begin, end);
        int pageCount = 0;
        if (total % 12 == 0) pageCount = total / 12;
        else {
            pageCount = total / 12 + 1;
        }
        for (UserDetailInfoDTO userInfoDTO4WB1 : userInfoList) {
            String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB1.getId().toString());
            String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB1.getId().toString());
            userInfoDTO4WB1.setCityName(cityName);
            userInfoDTO4WB1.setSchoolName(schoolName);
        }
        model.addAttribute("userInfos", userInfoList);
        model.addAttribute("page", page);
        model.addAttribute("bannerType", "userCenter");
        model.addAttribute("menuIndex", 3);
        model.addAttribute("total", total);
        model.addAttribute("role", role);
        model.addAttribute("pageCount", pageCount);
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        model.addAttribute("currentUser", info);

        return "activity/friend-list";
    }

    /**
     * 异步获取好友列表
     *
     * @param page
     * @return
     */
    @RequestMapping("/ajaxGetFriendlist")
    @ResponseBody
    public Map<String, Object> getfriends(Integer page) {
        if (page == null) page = 1;
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        List<UserDetailInfoDTO> userInfoList = null;
        long role = userInfoDTO4WB.getRole();
        userInfoList = friendService.selectAllFriend(userInfoDTO4WB.getId().toString());
        if (page < 1) page = 1;
        int total = userInfoList.size();
        if ((page - 1) * 12 > (userInfoList.size() - 1)) page = 1; //截取起始点超过 list大小重置page
        int begin = (page - 1) * 12;
        int end = page * 12;
        if (page * 12 > userInfoList.size()) end = userInfoList.size(); //截取结束点超过size 截取起始点到最后
        userInfoList = userInfoList.subList(begin, end);
        int pageCount = 0;
        if (total % 12 == 0) pageCount = total / 12;
        else {
            pageCount = total / 12 + 1;
        }
        for (UserDetailInfoDTO userInfoDTO4WB1 : userInfoList) {
            String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB1.getId().toString());
            String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB1.getId().toString());
            userInfoDTO4WB1.setCityName(cityName);
            userInfoDTO4WB1.setSchoolName(schoolName);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("userInfos", userInfoList);
        model.put("page", page);
        model.put("bannerType", "userCenter");
        model.put("menuIndex", 3);
        model.put("total", total);
        model.put("role", role);
        model.put("pageCount", pageCount);
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        model.put("currentUser", info);

        return model;
    }

    @RequestMapping("/getfriendlistgroupbyrole")
    @ResponseBody
    public Map<String, Object> getFriendsListByRole(Integer roleid, Integer page) {
        if (page == null) page = 1;
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        List<UserDetailInfoDTO> userInfoList = null;
        int role = roleid;
        if (role == UserRole.TEACHER.getRole()) {//老师
            userInfoList = friendService.selectFriendIsTeacher(getUserId().toString());
        } else if (role == UserRole.PARENT.getRole()) {//家长
            userInfoList = friendService.selectFriendIsParent(getUserId().toString());
        } else if (role == UserRole.STUDENT.getRole()) {//学生
            userInfoList = friendService.selectFriendIsStudent(getUserId().toString());
        } else if (role == UserRole.HEADMASTER.getRole()) {//校长
            userInfoList = friendService.selectFriendIsLeader(getUserId().toString());
        } else {
            userInfoList = friendService.findMyFriends(getUserId().toString());
        }

        if (page < 1) page = 1;
        int total = userInfoList.size();
        if ((page - 1) * 12 > (userInfoList.size() - 1)) page = 1; //截取起始点超过 list大小重置page
        int begin = (page - 1) * 12;
        int end = page * 12;
        if (page * 12 > userInfoList.size()) end = userInfoList.size(); //截取结束点超过size 截取起始点到最后
        userInfoList = userInfoList.subList(begin, end);
        int pageCount = 0;
        if (total % 12 == 0) pageCount = total / 12;
        else {
            pageCount = total / 12 + 1;
        }
        for (UserDetailInfoDTO userInfoDTO4WB1 : userInfoList) {
            String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB1.getId().toString());
            String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB1.getId().toString());
            userInfoDTO4WB1.setCityName(cityName);
            userInfoDTO4WB1.setSchoolName(schoolName);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("userInfos", userInfoList);
        model.put("page", page);
        model.put("bannerType", "userCenter");
        model.put("menuIndex", 3);
        model.put("total", total);
        model.put("role", role);
        model.put("pageCount", pageCount);
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        model.put("currentUser", info);

        return model;
    }

    @RequestMapping("/getFriendList")
    public String goToPage(@RequestParam int type, Model model) {
        model.addAttribute("type", type);
        return "activity/friendList";
    }

    @RequestMapping("/friendSearch")
    public String goToPage2(@RequestParam String keyWords, Model model) {
        model.addAttribute("keyWords", keyWords);
        return "activity/friendSearch";
    }

    /**
     * 按照角色获取
     *
     * @return
     */
    @RequestMapping("/friendlistgroupbyrole")
    public String friendsListByRole(Integer roleid, Integer page, Model model) {
        if (page == null) page = 1;
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        List<UserDetailInfoDTO> userInfoList = null;
        int role = roleid;
        if (role == UserRole.TEACHER.getRole()) {//老师
            userInfoList = friendService.selectFriendIsTeacher(getUserId().toString());
        } else if (role == UserRole.PARENT.getRole()) {//家长
            userInfoList = friendService.selectFriendIsParent(getUserId().toString());
        } else if (role == UserRole.STUDENT.getRole()) {//学生
            userInfoList = friendService.selectFriendIsStudent(getUserId().toString());
        } else if (role == UserRole.HEADMASTER.getRole()) {//校长
            userInfoList = friendService.selectFriendIsLeader(getUserId().toString());
        } else {
            userInfoList = friendService.findMyFriends(getUserId().toString());
        }

        if (page < 1) page = 1;
        int total = userInfoList.size();
        if ((page - 1) * 12 > (userInfoList.size() - 1)) page = 1; //截取起始点超过 list大小重置page
        int begin = (page - 1) * 12;
        int end = page * 12;
        if (page * 12 > userInfoList.size()) end = userInfoList.size(); //截取结束点超过size 截取起始点到最后
        userInfoList = userInfoList.subList(begin, end);
        int pageCount = 0;
        if (total % 12 == 0) pageCount = total / 12;
        else {
            pageCount = total / 12 + 1;
        }
        for (UserDetailInfoDTO userInfoDTO4WB1 : userInfoList) {
            String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB1.getId().toString());
            String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB1.getId().toString());
            userInfoDTO4WB1.setCityName(cityName);
            userInfoDTO4WB1.setSchoolName(schoolName);
        }
        model.addAttribute("userInfos", userInfoList);
        model.addAttribute("page", page);
        model.addAttribute("bannerType", "userCenter");
        model.addAttribute("menuIndex", 3);
        model.addAttribute("total", total);
        model.addAttribute("role", userInfoDTO4WB.getRole());
        model.addAttribute("pageCount", pageCount);
        return "activity/friend-list";
    }

    /*
    *
    * 好友搜索
    * 学校类型 1全部学校  2本校  3本年级
    * 角色类型 -1全部 1老师 4家长 0学生 对应数据库
    * */
    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> search(@RequestParam String keyWord,
                                      @RequestParam Integer schoolType,
                                      @RequestParam Integer roleType,
                                      @RequestParam Integer page) {
        String userId = getUserId().toString();
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isBlank(keyWord) || keyWord.length() < 2) {
            logger.debug("关键字少于两个字符，无效字符" + keyWord);
            return map;
        }
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        List<ObjectId> allUserIds = new ArrayList<ObjectId>();
        if (schoolType == 3) {
            gradeIds = friendService.getGradeIdByUser(userId);
            allUserIds = friendService.getAllUserIds(gradeIds, roleType);
        }


        List<UserDetailInfoDTO> userInfoList = friendService.searchUsers(keyWord, schoolType, roleType, getSessionValue().getSchoolId(), allUserIds, page, 24);
        int total = friendService.countAllFriends(keyWord, schoolType, roleType, getSessionValue().getSchoolId(), allUserIds);

        List<String> userIdList = new ArrayList<String>();
        for (UserDetailInfoDTO user : userInfoList) {
            userIdList.add(user.getId());
        }

        Map<String, List> schoolAndRegion = schoolService.findCityNameByUserIds(userIdList);
        List<SchoolEntry> schoolEntryList = new ArrayList<SchoolEntry>(schoolAndRegion.get("school"));
        List<RegionEntry> regionEntryList = new ArrayList<RegionEntry>(schoolAndRegion.get("region"));


        List<String> friendIds = friendService.findFiendIds(userId);
        List<FriendApply> friendApplyList = friendApplyService.findFriendApplyList(userId);

        Set<String> friendIdSet = new HashSet<String>();
        Set<String> respondentIdSet = new HashSet<String>();

        for (String friendId : friendIds) {
            friendIdSet.add(friendId);
        }
        friendIdSet.add(userId);// 将自己也加进去
        for (FriendApply friendApply : friendApplyList) {
            respondentIdSet.add(friendApply.getRespondent());
        }

        //注入地区和学校属性
        for (UserDetailInfoDTO userInfoDTO4WB1 : userInfoList) {
           /* String cityName = schoolService.findCityNameByUserId(userInfoDTO4WB1.getId().toString());
            String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO4WB1.getId().toString());*/
            Map<String, String> map2 = getNameById(schoolEntryList, regionEntryList, new ObjectId(userInfoDTO4WB1.getSchoolID()));
            userInfoDTO4WB1.setCityName(map2.get("region"));
            userInfoDTO4WB1.setSchoolName(map2.get("school"));
            boolean k = friendIdSet.contains(userInfoDTO4WB1.getId());
            boolean i = respondentIdSet.contains(userInfoDTO4WB1.getId());
            if (k) {
                userInfoDTO4WB1.setRelation(1);
            }
            if (i) {
                userInfoDTO4WB1.setRelation(2);
            }
            if (!i && !k) {
                userInfoDTO4WB1.setRelation(3);
            }
        }
        map.put("res", userInfoList);
        map.put("total", total);
        map.put("pageSize", 24);
        map.put("page", page);
        return map;
    }

    public Map<String, String> getNameById(List<SchoolEntry> schoolEntryList, List<RegionEntry> regionEntryList, Object schoolId) {
        Map<String, String> map = new HashMap<String, String>();
        for (SchoolEntry schoolEntry : schoolEntryList) {
            if (schoolEntry.getID().equals(schoolId)) {
                map.put("school", schoolEntry.getName());
                for (RegionEntry regionEntry : regionEntryList) {
                    if (regionEntry.getID().equals(schoolEntry.getRegionId())) {
                        map.put("region", regionEntry.getName());
                        return map;
                    }
                }
            }
        }
        return map;
    }

    /*
    * 查询好友并分组
    * */
    @RequestMapping("/findMyFriend")
    @ResponseBody
    public Map<String, Object> findMyFriend() {
        String userId = getUserId().toString();
        List<UserDetailInfoDTO> userInfoList = friendService.findMyFriends(userId);
        List<UserDetailInfoDTO> student = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> teacher = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> leader = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> parent = new ArrayList<UserDetailInfoDTO>();
        for (UserDetailInfoDTO userInfo : userInfoList) {
            int role = userInfo.getRole();
            if (UserRole.isStudent(role)) {
                student.add(userInfo);
            } else if (UserRole.isTeacher(role)) {
                teacher.add(userInfo);
            } else if (UserRole.isHeadmaster(role) || UserRole.isK6KT(role)) {
                leader.add(userInfo);
            } else if (UserRole.isParent(role)) {
                parent.add(userInfo);
            }
        }
        Map map = new HashMap();
        map.put("student", student);
        map.put("teacher", teacher);
        map.put("leader", leader);
        map.put("parent", parent);
        return map;
    }

    /*
    *
    * 添加好友申请
    * */
    @RequestMapping("/addFriendApply")
    @ResponseBody
    public boolean addFriendApply(@RequestParam String friendId, String message) {
        String userId = getUserId().toString();
        logger.debug("user" + userId + " add friendApply:" + friendId + ";message:" + message);
        FriendApply friendApply = new FriendApply();
        friendApply.setUserid(userId);
        friendApply.setRespondent(friendId);
        friendApply.setApplydate(new Date());
        friendApply.setAccepted(0);
        friendApply.setContent(message);
        friendService.addFriendApply(friendApply);
        return true;
    }


    /*
    *
    * 删除好友关系关系
    *
    * */
    @RequestMapping("/delete")
    @ResponseBody
    public boolean deleteFriend(@RequestParam String friendId) {
        String userId = getUserId().toString();
        //删除好友关系
        int k = friendService.deleteByUserId1AndUserId2(userId, friendId);
        return k == 1;
    }


    /*
    *
    * 推荐好友
    *
    * */
    @RequestMapping("/recommendedFriends")
    @ResponseBody
    public Map<String, Object> findRecommendedFriends(@RequestParam Integer page, @RequestParam Integer pageSize) {
        String userId = getUserId().toString();
        //根据当前userId 推荐好友  页面上有换一换按钮 需注意
        if (page == null || page <= 1) page = 1;
        if (pageSize == null || pageSize < 0) pageSize = 5;
        int begin = (page - 1) * pageSize;
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        //推荐学校的学生
        List<UserDetailInfoDTO> userInfos = friendService.recommendFriendBySchool(userInfoDTO4WB.getSchoolID(), userId, begin, pageSize);
        if (userInfos == null || userInfos.isEmpty()) {
            userInfos = friendService.recommendFriendBySchool(userInfoDTO4WB.getSchoolID(), userId, 0, pageSize);
        }
        if (null != userInfos && userInfos.size() > 0) {

            String cityName = schoolService.findCityNameByUserId(userInfos.get(0).getId().toString());
            String schoolName = schoolService.findSchoolNameByUserId(userInfos.get(0).getId().toString());

            //注入学校名称和城市名称
            for (UserDetailInfoDTO userInfoDTO4WB1 : userInfos) {
                userInfoDTO4WB1.setCityName(cityName);
                userInfoDTO4WB1.setSchoolName(schoolName);
            }
        }


        int total = 0;
        String key = CacheHandler.getKeyString(CacheHandler.SCHOOL_USER_COUNT, userInfoDTO4WB.getSchoolID(), userId);

        String countStr = CacheHandler.getStringValue(key);

        if (StringUtils.isNotBlank(countStr)) {
            total = Integer.parseInt(countStr);
        } else {
            total = friendService.recommendFriendBySchoolCount(userInfoDTO4WB.getSchoolID(), userId);
            CacheHandler.cache(key, String.valueOf(total), Constant.SESSION_FIVE_MINUTE);//5分钟
        }


        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("total", total);
        map1.put("rows", userInfos);
        map1.put("pageSize", pageSize);
        map1.put("page", page);
        return map1;
    }

    /**
     * 得到我的全部好友
     */
    @RequestMapping("/listAll")
    @ResponseBody
    public List<UserDetailInfoDTO> findMyFriends() {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        logger.debug(userInfoDTO4WB.getId() + " get my all friends!");
        return friendService.findMyFriends(userInfoDTO4WB.getId().toString());
    }


}
