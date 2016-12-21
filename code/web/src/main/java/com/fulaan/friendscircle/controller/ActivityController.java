package com.fulaan.friendscircle.controller;

import com.fulaan.base.BaseController;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.friendscircle.service.ActivityService;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.letter.service.LetterService;
import com.fulaan.school.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.KeyWordFilterUtil;
import com.pojo.activity.*;
import com.pojo.activity.enums.ActIvtStatus;
import com.pojo.activity.enums.ActStatus;
import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActVisibility;
import com.pojo.letter.*;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import com.sys.utils.HtmlUtils;
import com.sys.utils.ImageUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yan on 2015/2/26.
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController {

    private static final Logger logger = Logger.getLogger(ActivityController.class);
    @Resource
    private UserService userService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private FriendService friendService;
    @Resource
    private ActivityService activityService;
    @Resource
    private FriendApplyService friendApplyService;
    @Resource
    private LetterService letterService;
    @Resource
    private ActivityService actService;
    @Resource
    private ExperienceService experienceService;


    //默认活动图片
    public static final String ACTIVITY_DEFAULT_IMAGE = "/images/activityDef1.jpg";
    //玩伴计划图片位置
    public final static String UPLOAD_BUDDY_PIC = "/upload/buddy/pic";


    public void headInfo(Model model) {
        UserDetailInfoDTO user = userService.getUserInfoById(getUserId().toString());
        int role = user.getRole();
        if (UserRole.isStudent(role) || UserRole.isParent(role)) {//学生，家长
            user = userService.getUserInfoById(user.getId().toString());
            if (UserRole.isParent(role)) {
                user = userService.findStuInfoByParentId(user.getId().toString());
            }
            model.addAttribute("currentUser", user);
            model.addAttribute("template", "student");
            model.addAttribute("manager", UserRole.isManager((int) user.getRole()));
        } else {
            model.addAttribute("currentUser", user);
            model.addAttribute("template", "teacher");
            model.addAttribute("manager", UserRole.isManager((int) user.getRole()));
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping("/activityMain")
    public String activityMainPage(Model model, HttpServletRequest request) {
        UserDetailInfoDTO userInfo = userService.getUserInfoById(getUserId().toString());
        if (userInfo == null) {
            logger.error("用户id不存在，非法用户");
        }
        if (userInfo.getRole() != 0) {
            userInfo = userService.findUserInfoHasCityName(userInfo.getId());
            if (userInfo == null)
                logger.debug("找不到" + userInfo.getUserName() + "的居住地区");
        } else {
            userInfo = userService.selectUserInfoHasClassName(userInfo.getId().toString());
            if (userInfo == null)
                logger.debug("找不到" + userInfo.getUserName() + "的班级或居住地区");
        }
        int friendCount = friendService.countFriend(userInfo.getId().toString());
        int friendApplyCount = friendApplyService.countNoResponseReply(userInfo.getId().toString());
        model.addAttribute("currentUser", userInfo);
        model.addAttribute("wbUserInfo", userInfo);
        model.addAttribute("defaultImage", ACTIVITY_DEFAULT_IMAGE);
        model.addAttribute("friendApplyCount", friendApplyCount);
        model.addAttribute("wbUserInfo", userInfo);
        model.addAttribute("friendCount", friendCount);

        int hiddenType = 0;
        String type = request.getParameter("type");
        if (StringUtils.isNotBlank(type)) {
            hiddenType = 1;
        }
        model.addAttribute("hiddenType", hiddenType);
        return "activity/activityMain";
    }

    /*
    *
    * 适用移动端
    * 活动邀请数量
    *
    * */
    @RequestMapping("/invitationCount")
    @ResponseBody
    public Map<String, Object> actInvitationCount() {
        int invitationCount = actService.selectInvitationCount(getUserId().toString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("invitationCount", invitationCount);
        return map;
    }

    /*
    抽取热门活动
    */
    @RequestMapping("/selectHotActivity")
    @ResponseBody
    public List<Activity> selectHotActivity(@RequestParam Integer page, Integer pageSize) {
        String geoId = userService.findGeoIdByUserId(getUserId().toString());
        List<Activity> activityList = activityService.selectHotActivity(geoId, page, pageSize);
        for (Activity activity : activityList) {
            if (activity.getCoverImage() == null || "".equals(activity.getCoverImage().trim())) {
                activity.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
            }
        }
        activityMemberCount(activityList);
        return activityList;
    }

    /*
    * 动态
    *
    * */
    @RequestMapping("/actTrackList")
    @ResponseBody
    public Map<String, Object> actTrackList(Integer page, Integer pageSize) {
        String userId = getUserId().toString();
        if (page == null || page.equals(0)) page = 1;
        if (pageSize == null || pageSize.equals(0)) pageSize = 10;
        List<ActTrack> actTrackList = actService.getActTrackList(userId, page, pageSize);
        int total = actService.findActTrackCount(userId);
        int pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        for (ActTrack actTrack : actTrackList) {
            if (actTrack.getActivity() != null) {
                if (actTrack.getActivity().getCoverImage() == null ||
                        "".equals(actTrack.getActivity().getCoverImage().trim())) {
                    actTrack.getActivity().setCoverImage(ACTIVITY_DEFAULT_IMAGE);
                }
            }

        }

        if (null != actTrackList && actTrackList.size() > 0) {
            Map<String, ActTrack> activityMap = new HashMap<String, ActTrack>();

            for (ActTrack actTrack : actTrackList) {
                if (null != actTrack.getActivity()) {
                    activityMap.put(actTrack.getActivity().getId(), actTrack);
                }
            }
            List<Activity> countList = null;
            if (!activityMap.keySet().isEmpty()) {
                countList = activityService.selectCount(new ArrayList<String>(activityMap.keySet()));
            } else {
                ArrayList<String> k = new ArrayList<String>();
                k.add(new ObjectId().toString());
                countList = activityService.selectCount(k);
            }

            Map<String, Integer> memberCountMap = new HashMap<String, Integer>();
            for (Activity activityCount : countList) {
                memberCountMap.put(activityCount.getId(), activityCount.getAttendCount());
            }

            for (ActTrack actTrack : actTrackList) {
                if (null != actTrack.getActivity()) {
                    actTrack.getActivity().setMemberCount(memberCountMap.get(actTrack.getActivity().getId()));
                }
            }


        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", actTrackList);
        map.put("total", total);
        map.put("pageCount", pageCount);
        return map;
    }

    /**
     * 好友活动---------------已修改
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/friendsActivity")
    @ResponseBody
    public Map<String, Object> friendsActivity(int page, int pageSize) {
        if (page < 1)
            page = 1;
        if (pageSize < 10)
            pageSize = 10;
        String userId = getUserId().toString();
        List<Activity> activityList = friendService.findFriendsActivities(userId, page, pageSize);
        for (Activity activity : activityList) {
            if (activity.getCoverImage() == null || "".equals(activity.getCoverImage().trim())) {
                activity.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
            }
            //activity.setOrganizerImageUrl();
        }
        activityMemberCount(activityList);
        int total = friendService.findFriendsActivityCount(userId);
        return activity2Map(activityList, page, pageSize, total);
    }

    //设置活动实际参加人数
    private void activityMemberCount(List<Activity> activityList) {
        List<String> actIds = new ArrayList<String>();
        for (Activity activity : activityList) {
            actIds.add(activity.getId());
        }
        List<Activity> activityCountList = new ArrayList<Activity>();
        if (!actIds.isEmpty()) activityCountList = activityService.selectCount(actIds);
        else activityCountList = new ArrayList<Activity>();
        //activityCountList = new ArrayList<Activity>();
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for (Activity activityCount : activityCountList) {
            countMap.put(activityCount.getId(), activityCount.getAttendCount());
        }
        for (Activity activity : activityList) {
            activity.setMemberCount(countMap.get(activity.getId()));
        }
    }

    /**
     * 推荐活动---------------------已修改
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/recommendActivity")
    @ResponseBody
    public Map<String, Object> recommendActivity(int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 10) pageSize = 10;

        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        List<Activity> activityList = actService.recommendActivityOnlySchool(userInfo.getSchoolID(), userId, page, pageSize);
        //获取好友id
        List<String> friendIds = friendService.findFiendIds(userInfo.getId().toString());
        HashMap<String, Object> map = new HashMap<String, Object>(friendIds.size());
        for (String friendId : friendIds) {
            map.put(friendId, "");
        }
        String organizerId;
        for (Activity activity : activityList) {
            organizerId = activity.getOrganizer();
            if (map.get(organizerId) == null && !organizerId.equals(userId)) {
                //不是好友
                activity.setIsFriend(false);
            } else {
                activity.setIsFriend(true);
            }
        }
        for (Activity activity : activityList) {
            if (activity.getCoverImage() == null || "".equals(activity.getCoverImage().trim())) {
                activity.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
            }
        }
        activityMemberCount(activityList);
        int total = actService.recommendActivityOnlySchoolCount(userInfo.getSchoolID(), userInfo.getId().toString());
        return activity2Map(activityList, page, pageSize, total);
    }

    /*
    * 我发布的活动   适用移动端
    *
    * */
    @RequestMapping("/myOrganizedActivity")
    @ResponseBody
    public Map<String, Object> myOrganizedActivity(Integer page, Integer pageSize) {
        String userId = getUserId().toString();
        List<Activity> activityList = actService.myOrganizedActivity(userId, page, pageSize);
        for (Activity activity : activityList) {
            if (activity.getCoverImage() == null || "".equals(activity.getCoverImage().trim())) {
                activity.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
            }
        }
        activityMemberCount(activityList);
        int total = actService.myOrganizedActivityCount(userId);
        return activity2Map(activityList, page, pageSize, total);
    }

    /**
     * 我参加的活动，----------------------------已修改
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/myAttendActivity")
    @ResponseBody
    public Map<String, Object> myAttendActivity(int page, int pageSize) {
        String userId = getUserId().toString();

        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 5;

        List<Activity> activityList = actService.myAttendActivity(userId, page, pageSize);
        for (Activity activity : activityList) {
            if (activity.getCoverImage() == null || "".equals(activity.getCoverImage().trim())) {
                activity.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
            }
        }
        activityMemberCount(activityList);
        int total = actService.myAttendActivityCount(userId);
        return activity2Map(activityList, page, pageSize, total);
    }

    private Map<String, Object> activity2Map(List<Activity> activityList, Integer page, Integer pageSize, Integer total) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", activityList);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    /*
    *
    * 活动细节    移动端
    *
    *
    * */
    @RequestMapping("/activityDetail")
    @ResponseBody
    public Activity activityDetail(@RequestParam String activityId) {
        return actService.getAct(activityId);
    }

    /*
    * 参加某个活动的人 移动端
    *
    * */
    @RequestMapping("/usersInActivity")
    @ResponseBody
    public Map<String, Object> usersInActivity(@RequestParam String activityId,
                                               int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        String userId = getUserId().toString();
        List<UserDetailInfoDTO> userInfoList = actService.usersInActivity(activityId, page, pageSize);

        List<String> ids = friendService.findFiendIds(userId);
        int total = actService.userInActivityCount(activityId);

        for (UserDetailInfoDTO userInfoDTO : userInfoList) {
            if (userInfoDTO.getRole() == 0) {
                //只有学生才有主班级
//                String mainClassName = classService.findMainClassNameByUserId(userInfoDTO.getId().toString());
//                userInfoDTO.setMainClassName(mainClassName);
            }
            String schoolName = schoolService.findSchoolNameByUserId(userInfoDTO.getId().toString());
            String schoolCityName = schoolService.findCityNameByUserId(userInfoDTO.getId().toString());

            userInfoDTO.setSchoolName(schoolName);
            userInfoDTO.setCityName(schoolCityName);
            if (ids.contains(userInfoDTO.getId())) {
                userInfoDTO.setIsFriend(true);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", userInfoList);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }


    /*
    *
    * 我的好友  移动端
    *
    * */
    @RequestMapping("myFriends")
    @ResponseBody
    public Map<String, Object> myFriends() {
        String userId = getUserId().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        List<UserDetailInfoDTO> userInfoLis = friendService.findMyFriends(userId);
        map.put("rows", userInfoLis);
        return map;
    }

    /**
     * 邀请一个朋友 移动端
     */
    @RequestMapping("/inviteOneFriend")
    @ResponseBody
    public Map<String, String> inviteOneFriend(@RequestParam String activityId,
                                               @RequestParam String friendId,
                                               String msg) {
        ActInvitation actInvitation = actService.getInvitation(activityId, friendId);
        Map<String, String> map = new HashMap<String, String>();
        if (actInvitation != null) {
            map.put("message", "已经邀请过了");
            map.put("code", "500");
            return map;
        }
        ActInvitation invite = new ActInvitation();
        invite.setActId(activityId);
        invite.setGuestId(friendId);
        invite.setMsg(msg);
        invite.setStatus(ActIvtStatus.INVITE);
        actService.add(invite);
        map.put("message", "邀请已发出，请静候回音");
        map.put("code", "200");
        return map;
    }


    /*
    * 关于某个活动的 输入的userId的被邀记录  移动端
    *
    * */
    @RequestMapping("/actInvitation")
    @ResponseBody
    public ActInvitation invitation4UserId(@RequestParam String userId, @RequestParam String activityId) {
        ActInvitation actInvitation = actService.getInvitation(activityId, userId.toString());
        if (actInvitation == null) {
            actInvitation = new ActInvitation();
            Activity activity = actService.selectActAttend(activityId, userId);
            if (activity != null) {
                actInvitation.setStatus(ActIvtStatus.ATTENDED);
            } else {
                actInvitation.setStatus(ActIvtStatus.NOINVITE);
            }
            return actInvitation;
        }
        return actInvitation;
    }


    /**
     * 无邀请   参加活动 移动端
     */
    @RequestMapping("/joinActivity")
    @ResponseBody
    public Boolean attendAct(@RequestParam String activityId,
                             @RequestParam String userId,
                             @RequestParam String fromDevice) {

        ActAttend attend = new ActAttend();
        attend.setActivityId(activityId);
        attend.setUserId(userId);
        Activity actAttend = actService.selectActAttend(activityId, userId);
        if (actAttend != null) {//已经参加过了
            return false;
        }

        /**
         * 判断是否可以参加此次活动
         */
        Activity act = actService.getAct(activityId);
        if (act.getVisible().equals(ActVisibility.INVITE_FRIEND)) {
            ActInvitation actInvitation = actService.getInvitation(activityId, userId);
            if (null == actInvitation) {
                return false;
            }
        }
        if (act.getVisible().equals(ActVisibility.FRIEND)) {
            boolean isFriend = friendService.isFriend(act.getOrganizer(), userId);
            if (!isFriend) {
                return false;
            }
        }
        List<ActAttend> attendList = actService.getAttendDetails(activityId);
        if (null != attendList && attendList.size() >= act.getMemberCount()) {
            return false;
        }


        if ("1".equals(fromDevice)) {
            actService.add(attend, ActTrackDevice.FromAndroid);
        } else if ("2".equals(fromDevice)) {
            actService.add(attend, ActTrackDevice.FromIOS);
        } else {
            actService.add(attend, ActTrackDevice.FromPC);
        }

        ExpLogType expLogType = ExpLogType.JOIN_FRIEND_SCIRCLE_ACTIVITY;
        experienceService.updateNoRepeatAndDaily(getUserId().toString(), expLogType, activityId);

        return true;
    }


    /*
    *
    * 移动端
    *
    *  name : 活动名称,
     *            eventStartDate : 开始时间,
     *            eventEndDate : 结束时间,
     *            location : 地点,
     *            description : 说明,
     *            visible : 可见范围,
     *            memberCount : 人数,
     *            coverImage : 封面
    * */
    @RequestMapping("/promoteActivity")
    @ResponseBody
    public String newActivity4Mobile(String eventStartDate,
                                     String eventEndDate,
                                     String location,
                                     String description,
                                     String visible,
                                     String memberCount,
                                     String coverImage,
                                     String name,
                                     String fromDevice //1.android   2.apple 0.pc
    ) {
        String userId = getUserId().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Activity activity = new Activity();
        try {
            activity.setEventEndDate(simpleDateFormat.parse(eventEndDate));
            activity.setEventStartDate(simpleDateFormat.parse(eventStartDate));
            activity.setCreateDate(new Date());
            activity.setLocation(location);
            activity.setDescription(HtmlUtils.delScriptTag(description));
            activity.setCoverImage(coverImage);
            activity.setName(name);

            //ios已经提交审核，不好修改，增加另一个逻辑
            activity.setVisible(ActVisibility.PUBLIC);

            if ("0".equals(visible)) {
                activity.setVisible(ActVisibility.INVITE_FRIEND);
            } else if ("1".equals(visible)) {
                activity.setVisible(ActVisibility.FRIEND);
            } else if ("2".equals(visible)) {
                activity.setVisible(ActVisibility.PUBLIC);
            } else if ("公开".equals(visible)) {
                activity.setVisible(ActVisibility.PUBLIC);
            } else if ("仅好友可见".equals(visible)) {
                activity.setVisible(ActVisibility.FRIEND);
            }
            activity.setMemberCount(Integer.parseInt(memberCount));
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("日期转换格式异常");
            return "";
        }
        logger.debug(activity);
        activity.setStatus(ActStatus.ACTIVE);
        activity.setOrganizer(userId);
        //添加活动
        String actId = null;
        if ("1".equals(fromDevice)) {
            actId = actService.add(activity, ActTrackDevice.FromAndroid);
        } else {
            actId = actService.add(activity, ActTrackDevice.FromIOS);
        }
        //记录经验值
        ExpLogType expLogType = ExpLogType.LAUNCH_FRIEND_SCIRCLE_ACTIVITY;
        experienceService.updateNoRepeatAndDaily(getUserId().toString(), expLogType, actId.toString());
        return actId;
    }

    /**
     * 发起活动接口
     * contentType application/json
     * data {
     * act :
     * {
     * name : 活动名称,
     * eventStartDate : 开始时间,
     * eventEndDate : 结束时间,
     * location : 地点,
     * description : 说明,
     * visible : 可见范围,
     * memberCount : 人数,
     * coverImage : 封面
     * },
     * invitList : [
     * {
     * guestId : 被邀请人,
     * msg : 消息
     * },{
     * guestId : 被邀请人,
     * msg : 消息
     * },...
     * ]
     * }
     *
     * @param activityView
     * @return
     */
    @RequestMapping("/promote")
    @ResponseBody
    public ActivityView newActivity(@RequestBody ActivityView activityView, HttpServletRequest request) {
        logger.debug(activityView);
        String userId = getUserId().toString();
        Activity act = activityView.getAct();
        act.setStatus(ActStatus.ACTIVE);
        act.setOrganizer(userId);
        act.setCreateDate(new Date());
        if (StringUtils.isBlank(act.getCoverImage())) {
            int k = (int) (Math.random() * 6);
            act.setCoverImage("/images/activityDef" + k + ".jpg");
        }
        String actId = actService.add(act, ActTrackDevice.FromPC);
        String guestIds = activityView.getGuestIds();
        String message = activityView.getMessage();
        ActInvitation actInvitation = null;

        //if (StringUtils.isBlank(message)) {
        String url1 = request.getRequestURL().toString();
        String url2 = request.getRequestURI();
        String url = url1.replaceAll(url2, "") + "/activity/activityView.do?actId=" + actId;

        message += "来参加<a href='" + url + "'>" + activityView.getAct().getName() + "</a>吧";
        //}


        if (StringUtils.isNotBlank(guestIds)) {
            String[] guestIdArr = guestIds.split(Constant.COMMA);
            Set<String> hashSet = new HashSet<String>(); //去重
            for (String guestIdStr : guestIdArr) {
                try {
                    if (StringUtils.isNotBlank(guestIdStr) && !hashSet.contains(guestIdStr)) {
                        actInvitation = new ActInvitation();
                        actInvitation.setActId(actId);
                        actInvitation.setGuestId(guestIdStr);
                        actInvitation.setMsg(message);
                        actInvitation.setStatus(ActIvtStatus.INVITE);
                        actService.add(actInvitation);
                        hashSet.add(guestIdStr);
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            Activity activity = actService.getAct(actId);
            sendPrivateLetter(userId, guestIds, message, activity);
        }
        /**
         * 发起人首先参加活动
         */
        ActAttend attend = new ActAttend();
        attend.setActivityId(actId);
        attend.setUserId(userId);
        actService.add(attend, ActTrackDevice.FromPC);
        //记录经验值
        ExpLogType expLogType = ExpLogType.LAUNCH_FRIEND_SCIRCLE_ACTIVITY;
        experienceService.updateNoRepeatAndDaily(getUserId().toString(), expLogType, actId.toString());
        activityView.getAct().setId(actId);
        return activityView;
    }

    /**
     * 上传图片接口
     * enctype multipart/form-data
     * method post
     *
     * @param file
     * @return 链接地址
     */
    @RequestMapping("/uploadPic")
    @ResponseBody
    public String uploadPic(MultipartFile file, HttpSession session) {
        String path = UPLOAD_BUDDY_PIC + "/" + System.currentTimeMillis();
        String originalName = file.getOriginalFilename();
        originalName = originalName.substring(originalName.lastIndexOf("."), originalName.length());
        String fileName = System.currentTimeMillis() + "" + System.nanoTime() + originalName;
        String filePath = path + "/" + fileName;
        String imgTest = filePath.toLowerCase();
        if (!imgTest.endsWith("gif") && !imgTest.endsWith("jpg") &&
                !imgTest.endsWith("jpeg") && !imgTest.endsWith("png") && !imgTest.endsWith("bmp")) {
            logger.error(" upload illegal file :" + file.getOriginalFilename());
            return Constant.FAILD_CODE;
        }
        File newFile = new File(mkDir(session.getServletContext().getRealPath(path)), fileName);
        if (!file.isEmpty()) {
            try {
                file.transferTo(newFile);
                ImageUtils.scale2(newFile, newFile.getPath(), 320, 500, true);
                return filePath;
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return Constant.FAILD_CODE;
    }

    private static File mkDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 增加一个活动讨论,可带有图片 web端使用
     *
     * @param actId   活动ID
     * @param content 评论内容
     * @param file    图片
     * @return
     */
    @RequestMapping("/addwebdiscuss")
    @ResponseBody
    public Map<String, Object> addWebDiscuss(@RequestParam String actId, String content, MultipartFile file, HttpSession session) {
        String userId = getUserId().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        content = HtmlUtils.delScriptTag(content);
        if (StringUtils.isBlank(content) || content.length() > 500) {
            map.put("code", Constant.FAILD_CODE);
            map.put("message", "评论内容为空或者内容太长(不超过500字)");
            logger.error(map);
            //return Constant.FAILD;
            return map;
        }
        String filePath = "";
        if (null != file && !StringUtils.isBlank(file.getOriginalFilename())) {
            String path = UPLOAD_BUDDY_PIC + "/" + System.currentTimeMillis();

            String originalName = file.getOriginalFilename();
            originalName = originalName.substring(originalName.lastIndexOf("."), originalName.length());
            String fileName = System.nanoTime() + originalName;

            filePath = path + "/" + fileName;
            String imgTest = filePath.toLowerCase();
            if (!imgTest.endsWith("gif") && !imgTest.endsWith("jpg") &&
                    !imgTest.endsWith("jpeg") && !imgTest.endsWith("png") && !imgTest.endsWith("bmp")) {
                map.put("code", Constant.FAILD_CODE);
                map.put("message", "请上传合法照片");
                logger.error(map);
                //return Constant.FAILD;
                return map;
            }
            File newFile = new File(mkDir(session.getServletContext().getRealPath(path)), fileName);
            if (!file.isEmpty()) {
                try {
                    file.transferTo(newFile);
                    BufferedImage bi = ImageIO.read(newFile);
                    ImageIO.write(bi, "JPEG", new File(newFile.getPath()));
                    //ImageUtils.scale2(newFile, newFile.getPath(), 108, 151, true);
                } catch (IOException e) {
                    map.put("code", Constant.FAILD_CODE);
                    map.put("message", "服务器太忙了，稍后再试下吧");
                    logger.debug(map);
                    //return Constant.FAILD;
                    return map;
                }
            }
        }

        ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO();
        activityDiscussDTO.setUserId(userId);
        activityDiscussDTO.setContent(content);
        activityDiscussDTO.setImage(filePath);
        //activityDiscussDTO.setRepId("");
        activityDiscussDTO.setTime(new Date());

        actService.insertActDiscuss(actId, activityDiscussDTO);
        map.put("code", Constant.SUCCESS_CODE);
        List<ActivityDiscussDTO> activityDiscussDTOs = getDiscussList(actId);

        map.put("disList", activityDiscussDTOs);
        map.put("disCount", activityDiscussDTOs.size());
        logger.info(map);

        //String retStr = Constant.SUCCESS + Constant.COMMA + activityDiscussDTO.getId() + Constant.COMMA + filePath;
        return map;
    }

    /**
     * 增加一个活动讨论,可带有图片 移动端使用
     *
     * @param actId   活动ID
     * @param content 评论内容
     * @param file    图片
     * @return
     */
    @RequestMapping("/adddiscuss")
    @ResponseBody
    public String addDiscuss(@RequestParam String actId, String content, MultipartFile file, HttpSession session) {
        String userId = getUserId().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        content = HtmlUtils.delScriptTag(content);
        if (StringUtils.isBlank(content) || content.length() > 500) {
            map.put("code", Constant.FAILD_CODE);
            map.put("message", "评论内容为空或者内容太长(不超过500字)");
            logger.error(map);
            return Constant.FAILD;
        }
        String filePath = "";
        if (null != file && !StringUtils.isBlank(file.getOriginalFilename())) {
            String path = UPLOAD_BUDDY_PIC + "/" + System.currentTimeMillis();

            String originalName = file.getOriginalFilename();
            originalName = originalName.substring(originalName.lastIndexOf("."), originalName.length());
            String fileName = System.nanoTime() + originalName;

            filePath = path + "/" + fileName;
            String imgTest = filePath.toLowerCase();
            if (!imgTest.endsWith("gif") && !imgTest.endsWith("jpg") &&
                    !imgTest.endsWith("jpeg") && !imgTest.endsWith("png") && !imgTest.endsWith("bmp")) {
                map.put("code", Constant.FAILD_CODE);
                map.put("message", "请上传合法照片");
                logger.error(map);
                return Constant.FAILD;
            }
            File newFile = new File(mkDir(session.getServletContext().getRealPath(path)), fileName);
            if (!file.isEmpty()) {
                try {
                    file.transferTo(newFile);
                    ImageUtils.scale2(newFile, newFile.getPath(), 108, 151, true);
                } catch (IOException e) {
                    map.put("code", Constant.FAILD_CODE);
                    map.put("message", "服务器太忙了，稍后再试下吧");
                    logger.debug(map);
                    return Constant.FAILD;
                }
            }
        }

        ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO();
        activityDiscussDTO.setUserId(userId);
        activityDiscussDTO.setContent(content);
        activityDiscussDTO.setImage(filePath);
        //activityDiscussDTO.setRepId("");
        activityDiscussDTO.setTime(new Date());

        actService.insertActDiscuss(actId, activityDiscussDTO);
        map.put("code", Constant.SUCCESS_CODE);
        map.put("message", filePath);
        logger.info(map);
        String retStr = Constant.SUCCESS + Constant.COMMA + activityDiscussDTO.getId() + Constant.COMMA + filePath;
        return retStr;
    }

    public List<ActivityDiscussDTO> getDiscussList(String actId) {
        List<ActAttend> attendList = actService.getAttendDetails(actId);
        /**
         * 参加的人id set
         */
        Set<String> attendUserIdSet = new HashSet<String>();
        /**
         * 讨论人id set
         */
        Set<String> disUserIdSet = new HashSet<String>();
        for (ActAttend att : attendList) {
            attendUserIdSet.add(att.getUserId());
        }
        List<ActivityDiscussDTO> discussList = actService.selectActDises(actId);
        if (null != discussList) {
            for (ActivityDiscussDTO dis : discussList) {
                dis.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(dis.getContent(), "*", 2));
                disUserIdSet.add(dis.getUserId());
            }
        }
        List<String> uids = new ArrayList<String>();
        uids.addAll(attendUserIdSet);
        uids.addAll(disUserIdSet);

        /**
         * 已经参加该活动的人
         */
        //List<UserDetailInfoDTO> addendUserList = new ArrayList<UserDetailInfoDTO>();
        /**
         * 活动讨论
         */
        Map<String, ActivityDiscussDTO> discussDTOMap = new HashMap<String, ActivityDiscussDTO>();

        if (uids.size() > 0) {
            List<UserDetailInfoDTO> userInfos = userService.findUserInfoByUserIds(uids);
            Map<String, UserDetailInfoDTO> userInfoMap = new HashMap<String, UserDetailInfoDTO>();

            for (UserDetailInfoDTO userInfo : userInfos) {
                userInfoMap.put(userInfo.getId().toString(), userInfo);
                /*if (attendUserIdSet.contains(userInfo.getId())) {
                    addendUserList.add(userInfo);
                }*/
            }
            if (discussList.size() > 0) {
                Collections.sort(discussList, new Comparator<ActivityDiscussDTO>() {
                    @Override
                    public int compare(ActivityDiscussDTO o1, ActivityDiscussDTO o2) {
                        long t1 = o1.getTime().getTime();
                        long t2 = o2.getTime().getTime();
                        return (int) (t2 - t1);
                    }
                });
                ActivityDiscussDTO dis;
                UserDetailInfoDTO u;
                String repId;
                List<ActivityDiscussDTO> subDiscussList = new ArrayList<ActivityDiscussDTO>();
                for (int i = 0; i < discussList.size(); i++) {
                    dis = discussList.get(i);
                    u = userInfoMap.get(dis.getUserId());
                    if ("".equals(dis.getRepId()) || dis.getRepId() == null) {//表明不是一个子评论
                        discussDTOMap.put(dis.getId(), new ActivityDiscussDTO(u, dis));
                    } else { //表明是一个子回复
                        subDiscussList.add(dis);


                    }
                }
                ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO();
                for (int i = subDiscussList.size() - 1; i >= 0; i--) {
                    activityDiscussDTO = subDiscussList.get(i);
                    u = userInfoMap.get(activityDiscussDTO.getUserId());
                    repId = activityDiscussDTO.getRepId();
                    if (u == null) u = new UserDetailInfoDTO();
                    ActivityDiscussDTO discussDTO = discussDTOMap.get(repId);
                    if (discussDTO != null) {
                        discussDTO.addSubDiscuss(new ActivityDiscussDTO(u, activityDiscussDTO));
                    }
                }
            }
        }
        List<ActivityDiscussDTO> disList = new ArrayList<ActivityDiscussDTO>(discussDTOMap.values());
        Collections.sort(disList, new Comparator<ActivityDiscussDTO>() {
            @Override
            public int compare(ActivityDiscussDTO arg0, ActivityDiscussDTO arg1) {
                return new ObjectId(arg1.getId()).compareTo(new ObjectId(arg0.getId()));
            }
        });
        return disList;
    }

    /**
     * 由于移动端无图片时报错，所以增加此接口，单独适配没有上传图片的情况
     * 增加一个活动讨论,可带有图片
     *
     * @param actId   活动ID
     * @param content 评论内容
     * @return
     */
    @RequestMapping("/mobile/adddiscuss")
    @ResponseBody
    public String adddiscuss(@RequestParam String actId,
                             String content) {
        String userId = getUserId().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(content) || content.length() > 500) {
            String message = "评论内容为空或者内容太长（不超过500字）";
            map.put("code", Constant.FAILD_CODE);
            map.put("message", message);
            return Constant.FAILD;
        }
        String filePath = "";
        ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO();
        activityDiscussDTO.setUserId(userId);
        activityDiscussDTO.setContent(content);
        activityDiscussDTO.setImage(filePath);
//        activityDiscuss.setRepId("");
        activityDiscussDTO.setTime(new Date());
        actService.insertActDiscuss(actId, activityDiscussDTO);
        map.put("code", Constant.SUCCESS_CODE);
        map.put("message", filePath);
        logger.info(map);
        String retStr = Constant.SUCCESS + Constant.COMMA + activityDiscussDTO.getId() + Constant.COMMA + filePath;
        return retStr;
    }


    /**
     * 增加一个子评论
     *
     * @param actId
     * @param content
     * @param repId   回复的ID
     * @return
     */
    @RequestMapping("/addSubdiscuss")
    @ResponseBody
    public String addSubdiscuss(@RequestParam String actId,
                                String content,
                                String repId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = getUserId().toString();
        logger.debug("actId=" + actId + ";content=" + content + ";repId=" + repId);
        if (StringUtils.isBlank(content) || content.length() > 500) {
            map.put("code", Constant.FAILD_CODE);
            logger.error(map);
            return Constant.FAILD;
        }
        ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO();
        activityDiscussDTO.setUserId(userId);
        activityDiscussDTO.setContent(content);
        activityDiscussDTO.setImage("");
        activityDiscussDTO.setRepId(repId);
        activityDiscussDTO.setTime(new Date());
        actService.insertActDiscuss(actId, activityDiscussDTO);
        return Constant.SUCCESS_CODE;
    }

    /**
     * 增加一个子评论
     *
     * @param actId
     * @param content
     * @param repId   回复的ID
     * @return
     */
    @RequestMapping("/addWebSubdiscuss")
    @ResponseBody
    public Map<String, Object> addWebSubdiscuss(@RequestParam String actId,
                                                String content,
                                                String repId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = getUserId().toString();
        logger.debug("actId=" + actId + ";content=" + content + ";repId=" + repId);
        if (StringUtils.isBlank(content) || content.length() > 500) {
            map.put("code", Constant.FAILD_CODE);
            logger.error(map);
            //return Constant.FAILD;
            return map;
        }
        ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO();
        activityDiscussDTO.setUserId(userId);
        activityDiscussDTO.setContent(content);
        activityDiscussDTO.setImage("");
        activityDiscussDTO.setRepId(repId);
        activityDiscussDTO.setTime(new Date());
        actService.insertActDiscuss(actId, activityDiscussDTO);
        List<ActivityDiscussDTO> activityDiscussDTOs = getDiscussList(actId);
        map.put("code", Constant.SUCCESS_CODE);
        map.put("disList", activityDiscussDTOs);
        map.put("disCount", activityDiscussDTOs.size());
        return map;
    }

    /**
     * 邀请朋友
     *
     * @param actId
     * @param guestIds
     * @param msg
     * @return
     */
    @RequestMapping("/invite")
    @ResponseBody
    public boolean invite(String actId,
                          String guestIds,
                          String msg) {

        logger.debug("actId=" + actId + ";guestIds=" + guestIds + ";msg=" + msg);
        String userId = getUserId().toString();
        if (StringUtils.isNotBlank(guestIds) && StringUtils.isNotBlank(msg)) {
            String[] guestIdArr = guestIds.split(Constant.COMMA);
            Set<String> hashSet = new HashSet<String>(); //去重
            ActInvitation actInvitation = null;
            for (String guestIdStr : guestIdArr) {
                try {
                    if (StringUtils.isNotBlank(guestIdStr) && !hashSet.contains(guestIdStr)) {
                        actInvitation = new ActInvitation();
                        actInvitation.setActId(actId);
                        actInvitation.setGuestId(guestIdStr);
                        actInvitation.setMsg(msg);
                        actInvitation.setStatus(ActIvtStatus.INVITE);
                        actService.add(actInvitation);
                        hashSet.add(guestIdStr);
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
        }
        try {
            Activity activity = actService.getAct(actId);
            sendPrivateLetter(userId, guestIds, msg, activity);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return true;
    }

    /**
     * 查看活动页面
     *
     * @param actId
     * @param model
     * @return
     */
    @RequestMapping("/view")
    public String viewAct(String actId, Map<String, Object> model, HttpSession session) {
        String userId = getUserId().toString();
        Activity act = actService.getAct(actId);

        if (StringUtils.isBlank(act.getCoverImage())) {
            act.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
        }

        model.put("activity", act);
        List<ActAttend> attendList = actService.getAttendDetails(actId);
        model.put("attendCount", attendList.size());
        /**
         * 参加的人id set
         */
        Set<String> attendUserIdSet = new HashSet<String>();
        /**
         * 讨论人id set
         */
        Set<String> disUserIdSet = new HashSet<String>();
        for (ActAttend att : attendList) {
            attendUserIdSet.add(att.getUserId());
        }

        UserDetailInfoDTO organizerInfo = userService.getUserInfoById(act.getOrganizer());
        model.put("organInfo", organizerInfo);

        /**
         * type=0:此活动已经取消
         * type=1:此活动已经结束
         * type=2:本人发起,活动还没结结束
         * type=3:本人已经参加该活动
         * type=4:本人么有受邀请
         * type=5:本人收到邀请，没有做出回答
         * type=6:本人拒绝邀请
         */
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        //model.put("currentUser", info);//原来为
        //model.put("userInfo", info);
        model.put("currentUser", info);
        model.put("schoolID", info.getSchoolID());
        model.put("headmaster", session.getAttribute("headmaster")); //是不是代课
        model.put("ismanage", UserRole.isManager((int) info.getRole()));//是不是管理员

        model.put("role", info.getRole());
        if (act.getStatus().equals(ActStatus.CANCEL)) {
            model.put("type", 0);
        } else if (act.getEventEndDate().getTime() <= System.currentTimeMillis()) {
            model.put("type", 1);
            if (!act.getStatus().equals(ActStatus.CLOSE)) {
                actService.updateActivityStatus(actId, ActStatus.CLOSE);
            }
        } else if (act.getStatus().equals(ActStatus.CLOSE)) {
            model.put("type", 1);
        } else if (act.getOrganizer().equals(info.getId())) {
            model.put("type", 2);
        } else {
            boolean attended = false;
            for (ActAttend actAttend : attendList) {
                if (actAttend.getUserId().equals(info.getId())) {
                    attended = true;
                    break;
                }
            }
            if (attended) {
                model.put("type", 3);
            } else {
                ActInvitation actInvitation = actService.getInvitation(actId, info.getId().toString());

                if (null == actInvitation) {//本人么有受邀请
                    model.put("type", 4);
                } else {
                    model.put("initId", actInvitation.getId()); //
                    if (actInvitation.getStatus().equals(ActIvtStatus.INVITE)) {//接受邀请，没有做出回答
                        model.put("type", 5);
                    } else if (actInvitation.getStatus().equals(ActIvtStatus.HESITATE)) {
                        model.put("type", 5);
                    }
                    if (actInvitation.getStatus().equals(ActIvtStatus.REJECT)) {
                        model.put("type", 6);
                    }
                }
            }
        }
        List<ActivityDiscussDTO> discussList = actService.selectActDises(actId);
        if (null != discussList) {
            for (ActivityDiscussDTO dis : discussList) {
                dis.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(dis.getContent(), "*", 2));
                disUserIdSet.add(dis.getUserId());
            }
        }
        List<String> uids = new ArrayList<String>();
        uids.addAll(attendUserIdSet);
        uids.addAll(disUserIdSet);

        /**
         * 已经参加该活动的人
         */
        List<UserDetailInfoDTO> addendUserList = new ArrayList<UserDetailInfoDTO>();
        /**
         * 活动讨论
         */
        Map<String, ActivityDiscussDTO> discussDTOMap = new HashMap<String, ActivityDiscussDTO>();

        if (uids.size() > 0) {
            List<UserDetailInfoDTO> userInfos = userService.findUserInfoByUserIds(uids);
            Map<String, UserDetailInfoDTO> userInfoMap = new HashMap<String, UserDetailInfoDTO>();

            for (UserDetailInfoDTO userInfo : userInfos) {
                userInfoMap.put(userInfo.getId().toString(), userInfo);
                if (attendUserIdSet.contains(userInfo.getId())) {
                    addendUserList.add(userInfo);
                }
            }
            if (discussList.size() > 0) {
                Collections.sort(discussList, new Comparator<ActivityDiscussDTO>() {
                    @Override
                    public int compare(ActivityDiscussDTO o1, ActivityDiscussDTO o2) {
                        long t1 = o1.getTime().getTime();
                        long t2 = o2.getTime().getTime();
                        return (int) (t2 - t1);
                    }
                });
                ActivityDiscussDTO dis;
                UserDetailInfoDTO u;
                String repId;
                for (int i = 0; i < discussList.size(); i++) {
                    dis = discussList.get(i);
                    u = userInfoMap.get(dis.getUserId());
                    if ("".equals(dis.getRepId()) || dis.getRepId() == null) {//表明不是一个子评论
                        discussDTOMap.put(dis.getId(), new ActivityDiscussDTO(u, dis));
                    } else { //表明是一个子回复
                        repId = dis.getRepId();
                        if (u == null) u = new UserDetailInfoDTO();
                        ActivityDiscussDTO discussDTO = discussDTOMap.get(repId);
                        if (discussDTO != null) {
                            discussDTO.addSubDiscuss(new ActivityDiscussDTO(u, dis));
                        }

                    }
                }
            }
        }
        model.put("users", addendUserList);
        List<ActivityDiscussDTO> disList = new ArrayList<ActivityDiscussDTO>(discussDTOMap.values());
        Collections.sort(disList, new Comparator<ActivityDiscussDTO>() {
            @Override
            public int compare(ActivityDiscussDTO arg0, ActivityDiscussDTO arg1) {
                return new ObjectId(arg1.getId()).compareTo(new ObjectId(arg0.getId()));
            }
        });
        model.put("disList", disList);
        model.put("disCount", disList.size());
        return "activity/view";
    }

    /**
     * 跳转到活动详情页面
     *
     * @return
     */
    @RequestMapping("/activityView")
    public String activityView(@RequestParam String actId, Model model) {
        model.addAttribute("actId", actId);
        String userId = getUserId().toString();
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        ActInvitation actInvitation = actService.getInvitation(actId, info.getId().toString());
        if (null != actInvitation) {
            model.addAttribute("inviteId", actInvitation.getId()); //
        }
        return "activity/activityView";
    }

    /**
     * 异步获取活动内容
     *
     * @param actId
     * @return
     */
    @RequestMapping("/ajaxActivityView")
    @ResponseBody
    public Map<String, Object> getActivityViewById(@RequestParam String actId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = getUserId().toString();
        Activity act = actService.getAct(actId);

        if (StringUtils.isBlank(act.getCoverImage())) {
            act.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
        }

        map.put("activity", act);
        List<ActAttend> attendList = actService.getAttendDetails(actId);
        map.put("attendCount", attendList.size());
        /**
         * 参加的人id set
         */
        Set<String> attendUserIdSet = new HashSet<String>();
        /**
         * 讨论人id set
         */
        Set<String> disUserIdSet = new HashSet<String>();
        for (ActAttend att : attendList) {
            attendUserIdSet.add(att.getUserId());
        }

        UserDetailInfoDTO organizerInfo = userService.getUserInfoById(act.getOrganizer());
        map.put("organInfo", organizerInfo);

        /**
         * type=0:此活动已经取消
         * type=1:此活动已经结束
         * type=2:本人发起,活动还没结结束
         * type=3:本人已经参加该活动
         * type=4:本人么有受邀请
         * type=5:本人收到邀请，没有做出回答
         * type=6:本人拒绝邀请
         */
        UserDetailInfoDTO info = userService.getUserInfoById(userId);
        map.put("currentUser", info);
        map.put("schoolID", info.getSchoolID());
        map.put("headmaster", getSessionValue().get("headmaster")/* session.getAttribute("headmaster")*/); //是不是代课
        map.put("ismanage", UserRole.isManager((int) info.getRole()));//是不是管理员

        map.put("role", info.getRole());
        if (act.getStatus().equals(ActStatus.CANCEL)) {
            map.put("type", 0);
        } else if (act.getEventEndDate().getTime() <= System.currentTimeMillis()) {
            map.put("type", 1);
            if (!act.getStatus().equals(ActStatus.CLOSE)) {
                actService.updateActivityStatus(actId, ActStatus.CLOSE);
            }
        } else if (act.getStatus().equals(ActStatus.CLOSE)) {
            map.put("type", 1);
        } else if (act.getOrganizer().equals(info.getId())) {
            map.put("type", 2);
        } else {
            boolean attended = false;
            for (ActAttend actAttend : attendList) {
                if (actAttend.getUserId().equals(info.getId())) {
                    attended = true;
                    break;
                }
            }
            if (attended) {
                map.put("type", 3);
            } else {
                ActInvitation actInvitation = actService.getInvitation(actId, info.getId().toString());

                if (null == actInvitation) {//本人么有受邀请
                    map.put("type", 4);
                } else {
                    map.put("initId", actInvitation.getId()); //
                    if (actInvitation.getStatus().equals(ActIvtStatus.INVITE)) {//接受邀请，没有做出回答
                        map.put("type", 5);
                    } else if (actInvitation.getStatus().equals(ActIvtStatus.HESITATE)) {
                        map.put("type", 5);
                    }
                    if (actInvitation.getStatus().equals(ActIvtStatus.REJECT)) {
                        map.put("type", 6);
                    }
                }
            }
        }
        List<ActivityDiscussDTO> discussList = actService.selectActDises(actId);
        if (null != discussList) {
            for (ActivityDiscussDTO dis : discussList) {
                dis.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(dis.getContent(), "*", 2));

                String qiniuPath = Resources.getProperty("doc.domain");
                if (StringUtils.isNotBlank(qiniuPath) && StringUtils.isNotBlank(dis.getImage())) {
                    dis.setImage(qiniuPath + dis.getImage());
                }
                disUserIdSet.add(dis.getUserId());
            }
        }
        List<String> uids = new ArrayList<String>();
        uids.addAll(attendUserIdSet);
        uids.addAll(disUserIdSet);

        /**
         * 已经参加该活动的人
         */
        List<UserDetailInfoDTO> addendUserList = new ArrayList<UserDetailInfoDTO>();
        /**
         * 活动讨论
         */
        Map<String, ActivityDiscussDTO> discussDTOMap = new HashMap<String, ActivityDiscussDTO>();

        if (uids.size() > 0) {
            List<UserDetailInfoDTO> userInfos = userService.findUserInfoByUserIds(uids);
            Map<String, UserDetailInfoDTO> userInfoMap = new HashMap<String, UserDetailInfoDTO>();


            for (UserDetailInfoDTO userInfo : userInfos) {
                userInfoMap.put(userInfo.getId().toString(), userInfo);
                if (attendUserIdSet.contains(userInfo.getId())) {
                    addendUserList.add(userInfo);
                }
            }
            if (discussList.size() > 0) {
                Collections.sort(discussList, new Comparator<ActivityDiscussDTO>() {
                    @Override
                    public int compare(ActivityDiscussDTO o1, ActivityDiscussDTO o2) {
                        long t1 = o1.getTime().getTime();
                        long t2 = o2.getTime().getTime();
                        return t2 == t1 ? 0 : t2 > t1 ? 1 : -1;
//                        return (int) (t2 - t1);  //会报错  java.lang.IllegalArgumentException: Comparison method violates its general contract!
                    }
                });
                ActivityDiscussDTO dis;
                UserDetailInfoDTO u;
                String repId;
                List<ActivityDiscussDTO> subDiscussList = new ArrayList<ActivityDiscussDTO>();
                for (int i = 0; i < discussList.size(); i++) {
                    dis = discussList.get(i);
                    u = userInfoMap.get(dis.getUserId());
                    if (u == null) {
                        continue;
                    }
                    if ("".equals(dis.getRepId()) || dis.getRepId() == null) {//表明不是一个子评论
                        discussDTOMap.put(dis.getId(), new ActivityDiscussDTO(u, dis));
                    } else { //表明是一个子回复
                        subDiscussList.add(dis);
                    }
                }
                ActivityDiscussDTO activityDiscussDTO = new ActivityDiscussDTO();
                for (int i = subDiscussList.size() - 1; i >= 0; i--) {
                    activityDiscussDTO = subDiscussList.get(i);
                    u = userInfoMap.get(activityDiscussDTO.getUserId());
                    repId = activityDiscussDTO.getRepId();
                    if (u == null) u = new UserDetailInfoDTO();
                    ActivityDiscussDTO discussDTO = discussDTOMap.get(repId);
                    if (discussDTO != null) {
                        discussDTO.addSubDiscuss(new ActivityDiscussDTO(u, activityDiscussDTO));
                    }
                }
            }
        }
        map.put("users", addendUserList);
        List<ActivityDiscussDTO> disList = new ArrayList<ActivityDiscussDTO>(discussDTOMap.values());
        Collections.sort(disList, new Comparator<ActivityDiscussDTO>() {
            @Override
            public int compare(ActivityDiscussDTO arg0, ActivityDiscussDTO arg1) {
                return new ObjectId(arg1.getId()).compareTo(new ObjectId(arg0.getId()));
            }
        });
        //List<ActivityDiscussDTO> disList=getDiscussList(actId);
        map.put("disList", disList);
        map.put("disCount", disList.size());
        return map;
    }


    /**
     * 取消活动
     *
     * @param actId 活动ID
     * @return
     */
    @RequestMapping("/cancel")
    @ResponseBody
    public Map cancelAct(@RequestParam String actId) {
        String userId = getUserId().toString();
        UserDetailInfoDTO user = userService.getUserInfoById(userId);
        logger.info("user:" + user.getId() + " cancel the activity:" + actId);
        Map<String, Object> map = new HashMap<String, Object>();

        Activity act = actService.getAct(actId);
        if (null == act) {
            String message = "此活动不存在";
            map.put("code", Constant.FAILD_CODE);
            map.put("message", message);
            return map;
        }

        if (!user.getId().equals(act.getOrganizer())) {
            String message = "您无权取消该活动";
            map.put("code", Constant.FAILD_CODE);
            map.put("message", message);
            return map;
        }
        if (!act.getStatus().equals(ActStatus.ACTIVE)) {
            String message = "此活动已经取消过了";
            map.put("code", Constant.FAILD_CODE);
            map.put("message", message);
            return map;
        }
        actService.updateActivityStatus(actId, ActStatus.CANCEL);
        map.put("code", Constant.SUCCESS_CODE);
        String message = "活动取消成功";
        map.put("message", message);

        //发送活动取消私信
        sendPrivateLetter(user.getId().toString(), act);
        //发送消息
        return map;
    }


    /**
     * 活动取消，发送私信
     *
     * @param userId
     * @param act
     */
    private void sendPrivateLetter(String userId, Activity act) {
        List<String> list = actService.findAttendIds(act.getId());
        LetterDTO letterDTO = null;


        letterDTO = new LetterDTO();
        letterDTO.setSenderId(userId);
        letterDTO.setContent(MessageFormat.format("{0}活动已经取消了！", act.getName()));
        letterDTO.setLetterState(LetterState.LETTER_SEDND_SUCCESS);
        letterDTO.setLetterType(LetterType.ACTIVITY_CANCEL);
        letterDTO.setExtraData(act);

        for (String att : list) {
            if (userId.equals(att)) {
                continue;
            }
            letterDTO.getReceiveInfoList().add(new ReceiveInfo(new ObjectId(att), LetterState.LETTER_SEDND_SUCCESS.getState(), null));

        }
        logger.debug(letterDTO);
        try {
            //todo 信件
            //letterService.sendLetter(letterDTO);
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    /**
     * 活动邀请，发送私信,应用在活动详情中
     *
     * @param userid
     * @param guestIds
     */
    private void sendPrivateLetter(String userid, String guestIds, String message, Activity activity) {
        if (StringUtils.isBlank(guestIds)) {
            return;
        }
        String[] guestIdArr = guestIds.split(Constant.COMMA);
        LetterDTO letterDTO = null;
        List<ObjectId> receiverIds = new ArrayList<ObjectId>();

        letterDTO = new LetterDTO();
        letterDTO.setSenderId(userid);
        letterDTO.setContent(message);
        letterDTO.setLetterState(LetterState.LETTER_SEDND_SUCCESS);
        letterDTO.setLetterType(LetterType.ACTIVITY_INVITE);
        //letterDTO.setExtraData(activity.exportActivity());
        letterDTO.setExtraData(activity.getId());


        for (String guest : guestIdArr) {
            if (StringUtils.isNotBlank(guest)) {
                letterDTO.getReceiveInfoList().add(new ReceiveInfo(new ObjectId(guest), LetterState.LETTER_SEDND_SUCCESS.getState(), null));
                receiverIds.add(new ObjectId(guest));
            }
        }
        logger.debug(letterDTO);
        try {
            //todo
            LetterEntry letterEntry = new LetterEntry(new ObjectId(userid),
                    message, receiverIds);
            letterService.sendLetter(letterEntry);
            //letterService.sendLetter(letterDTO);
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    /**
     * 参加活动
     *
     * @param actId 活动ID
     * @return
     */
    @RequestMapping("/attend")
    @ResponseBody
    public Map<String, Object> attendActivity(@RequestParam String actId, @RequestParam String fromDevice) {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        logger.info("user:" + userInfo.getId() + " attend the activity:" + actId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.FAILD_CODE);
        Activity act = actService.getAct(actId);
        if (act.getVisible().equals(ActVisibility.INVITE_FRIEND)) {
            ActInvitation actInvitation = actService.getInvitation(actId, userInfo.getId().toString());
            if (null == actInvitation) {
                String message = "抱歉，你没有受邀请，不能参加该活动";
                map.put("message", message);
                return map;
            }
        }
        if (act.getVisible().equals(ActVisibility.FRIEND)) {
            boolean isFriend = friendService.isFriend(act.getOrganizer(), userInfo.getId().toString());
            if (!isFriend) {
                String message = "抱歉，你与活动发起者不是朋友，不能参加该活动";
                map.put("message", message);
                return map;
            }
        }

        List<ActAttend> attendList = actService.getAttendDetails(actId);
        if (null != attendList && attendList.size() >= act.getMemberCount()) {
            String message = "抱歉，已达到活动最大人数了";
            map.put("message", message);
            return map;
        }


        Activity activity = actService.selectActAttend(actId, userId);
        if (null == activity) {
            ActAttend attend = new ActAttend();
            attend.setActivityId(actId);
            attend.setUserId(userInfo.getId().toString());
            if ("0".equals(fromDevice)) {
                actService.add(attend, ActTrackDevice.FromPC);
            } else if ("1".equals(fromDevice)) {
                actService.add(attend, ActTrackDevice.FromAndroid);
            } else {
                actService.add(attend, ActTrackDevice.FromIOS);
            }
        }
        ExpLogType expLogType = ExpLogType.JOIN_FRIEND_SCIRCLE_ACTIVITY;
        if (experienceService.updateNoRepeatAndDaily(getUserId().toString(), expLogType, actId.toString())) {
            map.put("scoreMsg", expLogType.getDesc());
            map.put("score", expLogType.getExp());
        }
        map.put("code", 200);
        return map;
    }


    /**
     * 退出活动
     *
     * @param actId
     * @return
     */
    @RequestMapping("/quit")
    @ResponseBody
    public Map<String, Object> quitAct(@RequestParam String actId) {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        logger.info("user quit activity;userId:" + userInfo.getId() + ";actid:" + actId);
        actService.quitAct(actId, userInfo.getId().toString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 200);
        return map;
    }


    /**
     * 接受邀请
     *
     * @param id 邀请ID
     * @return
     */
    @RequestMapping("/accept")
    @ResponseBody
    public Map<String, Object> acceptInvite(String id, @RequestParam String fromDevice) {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        logger.info("user:" + userInfo.getId() + " accept the invite:" + id);
        Map<String, Object> map = isInviteCanEditor(id, userInfo.getId().toString());
        if (map.get(Constant.FAILD_CODE) != null) {
            return map;
        }
        ActInvitation invite = (ActInvitation) map.get(Constant.SUCCESS_CODE);
        actService.updateInvitationStatus(id, ActIvtStatus.ACCEPT);
        ActAttend attend = new ActAttend();

        attend.setUserId(invite.getGuestId());
        attend.setActivityId(invite.getActId());
        try {
            if ("0".equals(fromDevice)) {
                actService.add(attend, ActTrackDevice.FromPC);
            } else if ("1".equals(fromDevice)) {
                actService.add(attend, ActTrackDevice.FromAndroid);
            } else {
                actService.add(attend, ActTrackDevice.FromIOS);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        ExpLogType expLogType = ExpLogType.JOIN_FRIEND_SCIRCLE_ACTIVITY;
        if (experienceService.updateNoRepeatAndDaily(userId, expLogType, id)) {
            map.put("scoreMsg", expLogType.getDesc());
            map.put("score", expLogType.getExp());
        }
        return map;
    }

    /**
     * 拒绝邀请
     *
     * @param id
     * @return
     */
    @RequestMapping("/reject")
    @ResponseBody
    public Map<String, Object> rejectInvite(@RequestParam String id) {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        logger.info("user:" + userInfo.getId() + " reject the invite:" + id);
        Map<String, Object> map = isInviteCanEditor(id, userInfo.getId().toString());
        if (map.get(Constant.FAILD_CODE) != null) {
            return map;
        }
        actService.updateInvitationStatus(id, ActIvtStatus.REJECT);
        return map;
    }

    /**
     * 犹豫
     *
     * @param id
     * @return
     */
    @RequestMapping("/hesitate")
    @ResponseBody
    public Map<String, Object> hesitateInvite(@RequestParam String id) {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        logger.info("user:" + userInfo.getId() + " hesitate the invite:" + id);
        Map<String, Object> map = isInviteCanEditor(id, userInfo.getId().toString());
        if (map.get(Constant.FAILD_CODE) != null) {
            return map;
        }
        actService.updateInvitationStatus(id, ActIvtStatus.HESITATE);
        return map;
    }

    /**
     * 返回一个活动邀请是不是能被编辑
     *
     * @param inviteId
     * @param userId
     * @return
     */
    private Map<String, Object> isInviteCanEditor(String inviteId, String userId) {
        ActInvitation invite = actService.selectInvitationById(inviteId);
        if (null == invite) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put(Constant.FAILD_CODE, "无法找到该邀请");
            return obj;
        }
        if (!invite.getGuestId().equals(userId)) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put(Constant.FAILD_CODE, "无权处理该邀请");
            return obj;
        }
        if (!invite.getStatus().equals(ActIvtStatus.INVITE) && !invite.getStatus().equals(ActIvtStatus.HESITATE)) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put(Constant.FAILD_CODE, "该邀请已经被处理,不能重复处理");
            return obj;
        }
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put(Constant.SUCCESS_CODE, invite);
        return obj;
    }


    /*
    * 动态  适用移动端
    *
    * */
    @RequestMapping("/recentActTracks")
    @ResponseBody
    public Map<String, Object> recentActTracks(@RequestParam Integer page, @RequestParam Integer pageSize) {
        String userId = getUserId().toString();
        List<ActTrack> actTrackList = actService.getActTrackList(userId, page, pageSize);
        for (ActTrack actTrack : actTrackList) {
            if (actTrack.getActivity() != null) {
                if (actTrack.getActivity().getCoverImage() == null ||
                        "".equals(actTrack.getActivity().getCoverImage().trim())) {
                    actTrack.getActivity().setCoverImage(ACTIVITY_DEFAULT_IMAGE);
                }
            }
        }
        int count = actService.findActTrackCount(userId);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("rows", actTrackList);
        jsonMap.put("total", count);
        jsonMap.put("page", page);
        jsonMap.put("pageSize", pageSize);
        return jsonMap;
    }

    /**
     * 最热活动
     */
    @RequestMapping("/hot")
    @ResponseBody
    public List<Activity> findHotActivity(int topN) {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        String geoId = userService.findGeoIdByUserId(userInfo.getId());
        List<Activity> list = actService.getHotActivity(geoId, (topN - 1) * 5, 5);
        if (null == list || list.size() == 0) {
            list = actService.getHotActivity(geoId, 0, 5);
        }

        for (Activity act : list) {
            if (StringUtils.isBlank(act.getCoverImage())) {
                act.setCoverImage(ACTIVITY_DEFAULT_IMAGE);
            }
        }
        activityMemberCount(list);
        return list;
    }

    @RequestMapping("/deleteInvitation")
    @ResponseBody
    public Map<String, Object> deleteActInvitation(@RequestParam String actInvitationId) {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId);
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "";
        String code = "";
        if (userInfo != null) {
            ActInvitation actInvitation = activityService.selectInvitationById(actInvitationId);
            if (actInvitation != null) {
                if (actInvitation.getGuestId().equals(userInfo.getId())) {
                    int k = activityService.deleteActInvitationById(actInvitationId);
                    if (k > 0) {
                        code = "200";
                        msg = "删除活动邀请成功";
                    }
                }
            } else {
                code = "500";
                msg = "当前邀请不存在";
            }

        } else {
            code = "500";
            msg = "用户状态非法";
        }

        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    /*
    *
    * 获取某个活动的所有评论  仅移动端
    *
    * */
    @RequestMapping("/activityDiscuss")
    @ResponseBody
    public Map<String, Object> findActivityDiscuss(@RequestParam String activityId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        Integer total = null;
        Map<String, Object> returnMap = activityService.findActDiscuss(activityId, page, pageSize);
        List<ActivityDiscussDTO> activityDiscussDTOList = (List<ActivityDiscussDTO>) returnMap.get("discuss");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", returnMap.get("total"));
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("rows", activityDiscussDTOList);
        return map;
    }

    /*
    *
    * 获取活动的图片
    *
    * */
    @RequestMapping("/activityPicture")
    @ResponseBody
    public Map<String, Object> findActivityPic(@RequestParam String activityId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        Map<String, Object> returnMap = activityService.findActDiscussPicture(activityId, page, pageSize);
        List<ActivityDiscussDTO> activityDiscussDTOList = (List<ActivityDiscussDTO>) returnMap.get("discuss");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", returnMap.get("total"));
        map.put("rows", activityDiscussDTOList);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    @RequestMapping("/friendSearch")
    public String friendSearch(Model model) {
        headInfo(model);
        model.addAttribute("bannerType", "userCenter");
        model.addAttribute("menuIndex", 3);
        return "activity/friend-search";
    }

    /**
     * 删除好友圈回复，repid可以是评论，也可以是子评论
     *
     * @param actId
     * @param repId
     */
    @RequestMapping("/deleteReply")
    @ResponseBody
    public Map<String, Object> deleteReply(@RequestParam String actId, @RequestParam String repId, @RequestParam boolean hasPic) {
        activityService.deleteActivity(new ObjectId(actId), new ObjectId(repId), hasPic);
        List<ActivityDiscussDTO> activityDiscussDTOs = getDiscussList(actId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.SUCCESS_CODE);
        map.put("disList", activityDiscussDTOs);
        map.put("disCount", activityDiscussDTOs.size());
        return map;
    }
}
