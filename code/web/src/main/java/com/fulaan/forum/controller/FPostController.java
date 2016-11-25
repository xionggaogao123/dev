package com.fulaan.forum.controller;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.cache.CacheHandler;
import com.fulaan.controller.BaseController;
import com.fulaan.service.ConcernService;
import com.fulaan.user.service.UserService;
import com.fulaan.forum.service.*;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.app.FileUploadDTO;
import com.pojo.app.Platform;
import com.pojo.app.SessionValue;
import com.pojo.fcommunity.ConcernEntry;
import com.pojo.forum.*;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.props.Resources;
import com.sys.utils.AvatarUtils;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/5/31.
 * 帖子、板块处理中心
 */
@Controller
@RequestMapping("/forum")
public class FPostController extends BaseController {

    private static final Logger logger = Logger.getLogger(FPostController.class);

    @Autowired
    private VideoService videoService;
    @Autowired
    private FPostService fPostService;
    @Autowired
    private FSectionService fSectionService;
    @Autowired
    private FReplyService fReplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private FRecordService fRecordService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private FMissionService fMissionService;
    @Autowired
    private FCollectionService fCollectionService;
    @Autowired
    private FLevelService fLevelService;
    @Autowired
    private FVoteService fVoteService;
    @Autowired
    private FLogService fLogService;
    @Autowired
    private FScoreService fScoreService;
    @Autowired
    private FInformationService fInformationService;
    @Autowired
    private ConcernService concernService;

    /**
     * 搜索界面
     *
     * @param request
     * @param model
     * @return
     */
    @SessionNeedless
    @RequestMapping("/postSearch")
    @LoginInfo
    public String postSearch(HttpServletRequest request, Map<String, Object> model) {
        try {
            String regular = request.getParameter("regular");
            model.put("regular", URLDecoder.decode(regular, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/forum/postSearch";
    }

    /**
     * 等级界面
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/forumLevel")
    @LoginInfo
    public String forumLevel() {
        return "/forum/forumLevel";
    }

    /**
     * 板块发帖首页
     *
     * @param request
     * @param model
     * @return
     */
    @SessionNeedless
    @RequestMapping("/postIndex")
    @LoginInfo
    public String postIndex(HttpServletRequest request, Map<String, Object> model) {
        model.put("pSectionId", request.getParameter("pSectionId"));
        if (StringUtils.isNotBlank(request.getParameter("pSectionId"))) {
            FSectionDTO fSectionDTO = fSectionService.findFSectionById(new ObjectId(request.getParameter("pSectionId")));
            model.put("pSectionName", fSectionDTO.getName());
        }
        List<FSectionCountDTO> sectionDTOs = fSectionService.getFSectionList();
        model.put("sections", sectionDTOs);
        return "/forum/postIndex";
    }

    @SessionNeedless
    @RequestMapping("/competitionIndex")
    @LoginInfo
    public String competitionIndex(Map<String, Object> model) {
        List<FSectionCountDTO> sectionDTOs = fSectionService.getFSectionList();
        model.put("sections", sectionDTOs);
        model.put("count", fPostService.competitionPostCount());
        return "forum/competitionIndex";
    }

    @RequestMapping("/sol")
    @ResponseBody
    public RespObj sol(@RequestParam String sol, @RequestParam String postId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(postId));
            fPostDTO.setOfferedCompleted(1);
            fPostDTO.setSolution(sol);
            fPostService.addFPostEntry(fPostDTO);
            //给提供最佳答案的顾客加积分
            FReplyDTO dto = fReplyService.detail(new ObjectId(sol));
            userService.updateForumScore(new ObjectId(dto.getPersonId()), fPostDTO.getOfferedScore());
            FScoreDTO fScoreDTO = new FScoreDTO();
            fScoreDTO.setTime(System.currentTimeMillis());
            fScoreDTO.setType(1);
            fScoreDTO.setOperation("最佳答案");
            fScoreDTO.setPersonId(dto.getPersonId());
            fScoreDTO.setScoreOrigin("回复的答案为最佳答案！");
            fScoreDTO.setScore(fPostDTO.getOfferedScore().intValue());
            fScoreService.addFScore(fScoreDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            respObj.setMessage("更新悬赏已解决出错！");
        }
        return respObj;
    }

    //楼层定位
    @SessionNeedless
    @RequestMapping("/floorPosition")
    public String floorPosition(HttpServletRequest request) {
        int floor = Integer.parseInt(request.getParameter("floor"));
        int orFloor = floor;
        String postId = request.getParameter("postId");
        FPostDTO fPostDTO = fPostService.detail(new ObjectId(postId));
        int inSet = fPostDTO.getInSet();
        double cp;
        if (inSet == 1) {
            //查询未删除楼层总数
            int count = fReplyService.getFRepliesCount("", postId, "", "", -1);
            if (count <= 8) {
                cp = 1;
            } else {
                if (count > floor) {
                    int temp = count - floor;
                    if (temp <= 8) {
                        cp = 1;
                    } else {
                        cp = Math.ceil(temp / 8) + 1;
                    }
                } else {
                    cp = 1;
                }
            }
        } else {
            int count = fReplyService.getFRepliesCount("", postId, "", "", -1);
            if (count <= 8) {
                cp = 1;
            } else {
                if (count > floor) {
                    int temp = count - floor;
                    if (temp <= 8) {
                        cp = Math.ceil(count / 8) + 1;
                    } else {
                        cp = Math.ceil(count / 8) - Math.ceil(temp / 8) + 1;
                    }
                } else {
                    cp = Math.ceil(count / 8) + 1;
                }
            }
        }
        if (floor > 8) {
            floor = floor % 8;
        }
        String url = "/forum/postDetail.do?pSectionId=" + fPostDTO.getPostSectionId() + "&postId=" + fPostDTO.getFpostId() + "&personId=" + fPostDTO.getPersonId() + "&page=" + (int) cp + "&floor=" + orFloor;
        return "redirect:" + url;
    }

    //人物定位
    @SessionNeedless
    @RequestMapping("/redirectFloor")
    public String redirectFloor(HttpServletRequest request, HttpServletResponse response) {
        String replyId = request.getParameter("Id");
        FReplyDTO fReplyDTO = fReplyService.detail(new ObjectId(replyId));
        int floor = fReplyDTO.getFloor();
        String postId = fReplyDTO.getPostId();
        //查询未删除楼层总数
        int count = fReplyService.getFRepliesCount("", postId, "", "", -1);
        //查询最大的楼层
        int maxFloor = fReplyService.getMaxFloor(postId);
        double cp = 0;
        if (count > 8) {
            if (floor == 0) {
                cp = 1;
            } else {
                //根据楼层数来计算它处于未删除的那个楼层
                int t = fReplyService.getFloor(floor, postId);
                if (maxFloor >= floor) {
                    if (t <= 8) {
                        cp = 1;
                    } else {
                        if (t % 8 == 0) {
                            cp = Math.ceil(t / 8);
                        } else {
                            cp = Math.ceil(t / 8) + 1;
                        }
                    }
                } else {
                    cp = 1;
                }
            }
        } else {
            cp = 1;
        }
        try {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(fReplyDTO.getPostId()));
            String url = "/forum/postDetail.do?timeText=" + fReplyDTO.getTime() + "&pSectionId=" + fPostDTO.getPostSectionId() + "&postId=" + fPostDTO.getFpostId() + "&personId=" + fPostDTO.getPersonId() + "&page=" + (int) cp;
            response.sendRedirect(url);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/mall/entrance";
    }

    @RequestMapping("/rewordPost")
    @ResponseBody
    public RespObj rewordPost(String postId, @RequestParam(value = "score", defaultValue = "0") long score) {
        if (score <= 0) {
            return RespObj.FAILD("参数有误");
        }
        if (StringUtils.isBlank(postId) || !ObjectId.isValid(postId)) {
            return RespObj.FAILD("参数有误");
        }
        ObjectId uid = getUserId();
        if (score > userService.score(uid)) {
            return RespObj.FAILD("积分值不够");
        }
        FPostDTO fPostDTO = fPostService.detail(new ObjectId(postId));
        ObjectId pid = new ObjectId(fPostDTO.getPersonId());
        userService.addScore(pid, score);
        userService.minusScore(uid, score);
        return RespObj.SUCCESS(postId);
    }

    @SessionNeedless
    @RequestMapping("/postDetail")
    @LoginInfo
    public String postDetail(HttpServletRequest request, Map<String, Object> model) {
        SessionValue sv = getSessionValue();
        model.put("pSectionId", request.getParameter("pSectionId"));
        model.put("postId", request.getParameter("postId"));
        model.put("page", request.getParameter("page"));
        model.put("personId", request.getParameter("personId"));
        model.put("timeText", request.getParameter("timeText"));
        model.put("sortType", request.getParameter("sortType"));
        if (StringUtils.isNotBlank(request.getParameter("postId"))) {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(request.getParameter("postId")));
            model.put("postTitle", fPostDTO.getPostTitle());
            model.put("InSet", fPostDTO.getInSet());
            if (null != sv && !sv.isEmpty()) {
                int type = fPostDTO.getType();
                if (type == 3) {
                    //该奖赏贴总共奖赏次数
                    int rewardCount = fPostDTO.getRewardCount();
                    int rewardMax = fPostDTO.getRewardMax();
                    int rewardScore = fPostDTO.getRewardScore();
                    model.put("rewardScore", rewardScore);
                    //查询该奖赏贴一共回了多少贴
                    if (0 < rewardCount) {
                        //查询该人奖赏次数
                        int userReward = fReplyService.getFRepliesCount("", request.getParameter("postId"), sv.getId(), "", -1);
                        //判断是否大于每人最多奖赏次数
                        if (userReward < rewardMax) {
                            //设置是否奖赏
                            model.put("reward", 1);
                        }
                    }
                    //奖赏次数已结束
                } else if (type == 2) {
                    int offerCompleted = fPostDTO.getOfferedCompleted();
                    //悬赏帖未解决时
                    if (offerCompleted == 0) {
                        long deadTime = new ObjectId(fPostDTO.getFpostId()).getTime() + 5 * 60 * 1000L;
                        long nowTime = System.currentTimeMillis();
                        if (deadTime > nowTime) {
                            model.put("dead", 1);
                        }
                    } else if (offerCompleted == 1) {
                        model.put("sol", 1);
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(request.getParameter("floor"))) {
            int floor = Integer.parseInt(request.getParameter("floor"));
            model.put("floor", (floor + 1) * 333 + 171);
        }
        FLogDTO fLogDTO = new FLogDTO();
        fLogDTO.setActionName("postDetail");
        if (null != sv && !sv.isEmpty()) {
            fLogDTO.setPersonId(sv.getId());
        }
        if (StringUtils.isNotBlank(request.getParameter("postId"))) {
            fLogDTO.setKeyId(request.getParameter("postId"));
        }
        fLogDTO.setPath("/forum/postDetail.do");
        fLogDTO.setTime(System.currentTimeMillis());
        fLogService.addFLog(fLogDTO);
        if (getSessionValue() != null) {
            model.put("formScore", userService.find(new ObjectId(getUserId().toString())).getForumScore());
        }
        return "/forum/postDetail";
    }

    @RequestMapping("/newPost")
    @LoginInfo
    public String newPost(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        SessionValue sv = getSessionValue();
        if (userService.isUnSpeak(sv.getId())) {
            try {
                response.sendRedirect("/forum/forumSilenced.do");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        model.put("pSectionId", request.getParameter("pSectionId"));
        String postId = request.getParameter("postId");
        if (StringUtils.isNotBlank(postId)) {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(postId));
            model.put("comment", HtmlUtils.htmlEscapeDecimal(fPostDTO.getBackUpComment()));
            model.put("pSectionId", fPostDTO.getPostSectionId());
            model.put("classify", fPostDTO.getClassify());
            model.put("postTitle", fPostDTO.getPostTitle());
            model.put("postId", fPostDTO.getFpostId());
        }
        //获取总积分
        UserEntry userEntry = userService.find(new ObjectId(sv.getId()));
        long forumScore = userEntry.getForumScore();
        model.put("forumScore", forumScore);
        FLogDTO fLogDTO = new FLogDTO();
        fLogDTO.setActionName("newPost");
        if (!sv.isEmpty()) {
            fLogDTO.setPersonId(sv.getId());
        }
        if (StringUtils.isNotBlank(request.getParameter("pSectionId"))) {
            fLogDTO.setKeyId(request.getParameter("pSectionId"));
        }
        fLogDTO.setPath("/forum/newPost.do");
        fLogDTO.setTime(System.currentTimeMillis());
        fLogService.addFLog(fLogDTO);

        ObjectId userId = getUserId();
        return "/forum/newPost";
    }

    @RequestMapping("/task")
    public String task(Map<String, Object> model) {
        SessionValue sv = getSessionValue();
        String userId = "";
        String avatar = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
            avatar = sv.getMinAvatar();
        }
        Map<String, Object> map = fMissionService.findTodayMissionByUserId(userId);
        model.put("signIn", map.get("signIn"));
        model.put("post", map.get("post"));
        model.put("welfare", map.get("welfare"));
        //抽奖分两种情况（签到后才能抽奖）
        //1.判断当前连续签到的次数是否超过30天
        //2.每三天抽一次奖
        if ("0".equals(map.get("count"))) {
            //未签到
            model.put("welfare", "welfare");
        } else {
            //判断是否签到了
            if ("true".equals(map.get("signIn").toString())) {
                //判断今天能否抽奖
                int count = Integer.parseInt(map.get("count").toString());
                //测试用（判断是否连续签到超过4天）
                if (count >= 30) {
                    //能天天抽奖
                } else {
                    //判断这三天内是否抽奖过
                    boolean it = fMissionService.getThree(userId);
                    //三天抽奖过，所以不能抽奖
                    if (it) {
                        model.put("welfare", "welC");
                    }
                }
            } else {
                model.put("welfare", "welfare");
            }
        }

        if (null != avatar && !"http://7xiclj.com1.z0.glb.clouddn.com/".equals(avatar)) {
            model.put("imf", true);
        } else {
            model.put("imf", false);
        }
        return "/forum/forumTask";
    }

    @RequestMapping("/personal")
    @LoginInfo
    @SessionNeedless
    public String personal(HttpServletRequest request, Map<String, Object> model) {
        String personId = request.getParameter("personId");
        model.put("personId", personId);
        UserEntry userEntry = userService.find(new ObjectId(personId));
        int sex = userEntry.getSex();
        if (1 == sex) {
            model.put("sex", "男");
        } else if (0 == sex) {
            model.put("sex", "女");
        }
        if (null != userEntry.getNickName() && !"".equals(userEntry.getNickName())) {
            model.put("personName", userEntry.getNickName());
        } else {
            model.put("personName", userEntry.getUserName());
        }
        model.put("imageSrc", AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));

        model.put("uid", userEntry.getID());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        model.put("birthDay", format.format(userEntry.getBirthDate()));
        model.put("forumScore", userEntry.getForumScore());
        //在线时间
        model.put("statisticTime", userEntry.getStatisticTime());
        //注册时间
        model.put("registerTime", format1.format(userEntry.getRegisterTime()));
        //最后访问时间
        model.put("quitTime", format1.format(userEntry.getQuitTime()));
        //注册IP
        model.put("registerIp", userEntry.getRegisterIP());
        //上次访问Ip
        model.put("interviewIP", userEntry.getInterviewIP());
        //上次活动时间
        model.put("interviewTime", format1.format(userEntry.getInterviewTime()));
        //上次发表时间
        model.put("interviewPostTime", format1.format(userEntry.getInterviewPostTime()));
        //好友数
        model.put("friendCount", friendService.countFriend(personId));
        //回帖数
        model.put("replyCount", fReplyService.getFRepliesCount("", "", personId, "", -1));
        //发帖数（主题数）
        model.put("postCount", fPostService.getFPostsCount("", "", "", "", personId, -1, -1, 0, 0));
        //关注标志
        ConcernEntry concernEntry = concernService.getConcernData(getUserId(), new ObjectId(personId), 0);
        if (null != concernEntry) {
            model.put("concernFlag", 1);
        } else {
            model.put("concernFlag", 0);
        }
        if (model.get("userId") != null) {
            String userId = model.get("userId").toString();
            if (userId.equals(personId)) {
                model.put("friendLog", true);
            } else {
                model.put("friendLog", false);
            }
        }
        return "/forum/personalCenter";
    }

    @RequestMapping("/forumNotice")
    @LoginInfo
    public String forumNotice(Map<String, Object> model) {
        model.put("menuItem", 1);
        fRecordService.updateDelete();
        return "/forum/forumNotice";
    }

    @RequestMapping("/applyFriend")
    @LoginInfo
    public String applyFriend(Map<String, Object> model) {
        model.put("menuItem", 3);
        return "/forum/forumNotice";
    }

    @RequestMapping("/myMessage")
    @LoginInfo
    public String myMessage(Map<String, Object> model) {
        model.put("menuItem", 0);
        return "/forum/forumNotice";
    }

    @RequestMapping("/floorTimeText")
    @SessionNeedless
    @ResponseBody
    public RespObj floorTimeText(@ObjectIdType ObjectId postId, int floor) {
        return RespObj.SUCCESS(fReplyService.timeText(postId, floor));
    }

    @RequestMapping("/mySystem")
    @LoginInfo
    public String mySystem(Map<String, Object> model) {
        model.put("menuItem", 2);
        SessionValue sv = getSessionValue();
        String userId = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
        }
        fInformationService.updateScan(null, new ObjectId(userId), 1);
        return "/forum/forumNotice";
    }


    /**
     * 已删除帖子列表
     *
     * @param postSection
     * @param person
     * @param classify
     * @param cream
     * @param gtTime
     * @param top
     * @param regular
     * @param startTime
     * @param endTime
     * @param sortType
     * @param page
     * @param zan
     * @param pageSize
     * @return
     */
    @SessionNeedless
    @RequestMapping("/fSubPosts")
    @ResponseBody
    public Map<String, Object> fSubPostsList(@RequestParam(required = false, defaultValue = "") String postSection,
                                             @RequestParam(required = false, defaultValue = "") String person,
                                             @RequestParam(required = false, defaultValue = "-1") int classify,
                                             @RequestParam(required = false, defaultValue = "-1") int cream,
                                             @RequestParam(required = false, defaultValue = "0") long gtTime,
                                             @RequestParam(required = false, defaultValue = "-1") int top,
                                             @RequestParam(required = false, defaultValue = "") String regular,
                                             @RequestParam(required = false, defaultValue = "") String startTime,
                                             @RequestParam(required = false, defaultValue = "") String endTime,
                                             int sortType, int page, int zan, int pageSize) {
        String reg = "";
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            reg = URLDecoder.decode(URLDecoder.decode(regular, "utf-8"), "UTF-8");
            List<FPostDTO> fPostDTOs = fPostService.getFPostsList(1, 1, startTime, endTime, reg, sortType, zan, top, person, postSection, page, cream, classify, gtTime, pageSize);
            int sCount = fPostService.getFPostsCount(startTime, endTime, reg, postSection, person, cream, classify, gtTime, 1);
            model.put("subList", fPostDTOs);
            model.put("subCount", sCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 帖子列表
     *
     * @param postSection
     * @param classify
     * @param cream
     * @param gtTime
     * @param sortType
     * @param page
     * @param pageSize
     * @return
     */
    @SessionNeedless
    @RequestMapping("/fPosts")
    @ResponseBody
    public Map<String, Object> fPostsList(@RequestParam(required = false, defaultValue = "") String postSection,
                                          @RequestParam(required = false, defaultValue = "") String person,
                                          @RequestParam(required = false, defaultValue = "-1") int classify,
                                          @RequestParam(required = false, defaultValue = "-1") int cream,
                                          @RequestParam(required = false, defaultValue = "0") long gtTime,
                                          @RequestParam(required = false, defaultValue = "-1") int top,
                                          @RequestParam(required = false, defaultValue = "") String regular,
                                          @RequestParam(required = false, defaultValue = "") String startTime,
                                          @RequestParam(required = false, defaultValue = "") String endTime,
                                          int sortType, int page, int zan, int pageSize) {
        String reg = "";
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            reg = URLDecoder.decode(URLDecoder.decode(regular, "utf-8"), "UTF-8");
            List<FPostDTO> fPostDTOList = fPostService.getFPostsList(1, 0, startTime, endTime, reg, sortType, zan, top, person, postSection, page, cream, classify, gtTime, pageSize);
            int count = fPostService.getFPostsCount(startTime, endTime, reg, postSection, person, cream, classify, gtTime, 0);
            model.put("list", fPostDTOList);
            model.put("count", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    /**
     * 帖子app列表
     *
     * @param postSection
     * @param classify
     * @param cream
     * @param gtTime
     * @param sortType
     * @param page
     * @param pageSize
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getAppPostList")
    @ResponseBody
    public Map<String, Object> getAppPostList(@RequestParam(required = false, defaultValue = "") String postSection,
                                              @RequestParam(required = false, defaultValue = "") String person,
                                              @RequestParam(required = false, defaultValue = "-1") int classify,
                                              @RequestParam(required = false, defaultValue = "-1") int cream,
                                              @RequestParam(required = false, defaultValue = "0") long gtTime,
                                              @RequestParam(required = false, defaultValue = "-1") int top,
                                              @RequestParam(required = false, defaultValue = "") String regular,
                                              @RequestParam(required = false, defaultValue = "") String startTime,
                                              @RequestParam(required = false, defaultValue = "") String endTime,
                                              int sortType, int page, int zan, int pageSize) {
        String reg = "";
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            reg = URLDecoder.decode(URLDecoder.decode(regular, "utf-8"), "UTF-8");
            List<FPostDTO> fPostDTOList = fPostService.getFPostsList(-1, 0, startTime, endTime, reg, sortType, zan, top, person, postSection, page, cream, classify, gtTime, pageSize);
            int count = fPostService.getFPostsCount(startTime, endTime, reg, postSection, person, cream, classify, gtTime, 0);
            model.put("list", fPostDTOList);
            model.put("count", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    /**
     * 根据回复人查询回复人回复的贴子
     *
     * @param person
     * @param page
     * @param pageSize
     * @return
     */
    @SessionNeedless
    @RequestMapping("/fPostsByReplyPerson")
    @ResponseBody
    public Map<String, Object> fPostsByReplyPerson(@RequestParam(required = false, defaultValue = "") String person,
                                                   int sortType, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        if (pageSize > 15) {
            pageSize = 15;
        }
        List<FPostDTO> fPostDTOList = fReplyService.getFPostListByCondition(sortType, person, page, pageSize);

        int count = fReplyService.getFPostListByPerson(sortType, person);
        model.put("list", fPostDTOList);
        model.put("count", count);
        return model;
    }

    /**
     * 获取热帖
     */
    @SessionNeedless
    @RequestMapping("/fPostsByCondition")
    @ResponseBody
    public Map<String, Object> fPostByCondition(@RequestParam(required = false, defaultValue = "") String postSection,
                                                @RequestParam(required = false, defaultValue = "-1") int classify,
                                                @RequestParam(required = false, defaultValue = "") String regular,
                                                @RequestParam(required = false, defaultValue = "-1") int cream,
                                                @RequestParam(required = false, defaultValue = "0") long gtTime,
                                                int sortType, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();

        List<FPostDTO> fPostDTOList = fPostService.getFPostsListByCondition(regular, sortType, postSection, "", -1, page, cream, classify, gtTime, pageSize);

        model.put("list", fPostDTOList);
        return model;
    }

    /**
     * 获取精彩活动帖子
     */
    @SessionNeedless
    @RequestMapping("/fPostsActivity")
    @ResponseBody
    public Map<String, Object> fPostsActivity(@RequestParam(required = false, defaultValue = "") String postSection,
                                              @RequestParam(required = false, defaultValue = "") String regular,
                                              @RequestParam(required = false, defaultValue = "-1") int classify,
                                              @RequestParam(required = false, defaultValue = "-1") int cream,
                                              @RequestParam(required = false, defaultValue = "0") long gtTime,
                                              int inSet,
                                              int sortType, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<FPostDTO> fPostDTOList = fPostService.getFPostsListByActivity(0, regular, sortType, postSection, "", inSet, page, cream, classify, gtTime, pageSize);
        model.put("list", fPostDTOList);
        if (fPostDTOList.size() >= 4) {
            model.put("subList", fPostDTOList.subList(0, 4));
        } else {
            model.put("subList", fPostDTOList);
        }
        return model;
    }

    /**
     * 获取下线活动
     */
    @SessionNeedless
    @RequestMapping("/fPostsOffActivity")
    @ResponseBody
    public Map<String, Object> fPostsOffActivity() {
        Map<String, Object> model = new HashMap<String, Object>();
        List<FPostDTO> fPostDTOList = fPostService.getFPostsListByOffActivity();
        model.put("list", fPostDTOList);
        return model;
    }

    @SessionNeedless
    @RequestMapping("/fPostActivityAll")
    @ResponseBody
    public Map<String, Object> fPostsActivityAll() {
        Map<String, Object> model = new HashMap<String, Object>();
        List<FPostDTO> fPostDTOList = fPostService.getFPostsListByActivityAll();
        model.put("list", fPostDTOList);
        return model;
    }

    /**
     * 获取记录列表
     */
    @RequestMapping("/fRecordList")
    @ResponseBody
    public Map<String, Object> fPostByCondition(String personId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<FRecordDTO> fRecordDTOList = fRecordService.getFRepliesList(personId, 2);
        model.put("list", fRecordDTOList);
        return model;
    }


    /**
     * 获取记录数据
     *
     * @return
     */
    @RequestMapping(value = "/fTip", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> fTip() {
        Map<String, Object> model = new HashMap<String, Object>();
        SessionValue sv = getSessionValue();
        String userId = "";
        if (null != sv && !sv.isEmpty()) {
            userId = sv.getId();
        }
        int recordCount = fRecordService.getFRecordCount(userId);

        model.put("recordCount", recordCount);
        return model;
    }

    /**
     * 更新回复浏览记录
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/updateScan", method = RequestMethod.POST)
    @ResponseBody
    public FRecordDTO updateScan(String fRecordId) {
        FRecordDTO respObj = new FRecordDTO();
        try {
            respObj = fRecordService.detail(new ObjectId(fRecordId));
            fRecordService.updateReply(new ObjectId(fRecordId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 板块列表
     *
     * @param level
     * @param id
     * @param name
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/fSection", method = RequestMethod.GET)
    @ResponseBody
    public List<FSectionDTO> getFSectionDTOs(@RequestParam(required = false, defaultValue = "1") int level,
                                             @RequestParam(required = false, defaultValue = "") String id,
                                             @RequestParam(required = false, defaultValue = "") String name) {
        ObjectId e = null;
        if (ObjectId.isValid(id)) {
            e = new ObjectId(id);
        }
        return fSectionService.getFSectionListByLevel(level, e, name);
    }

    @SessionNeedless
    @RequestMapping(value = "/fSectionData", method = RequestMethod.GET)
    @ResponseBody
    public List<FSectionCountDTO> getFSectionListById() {
        return fSectionService.getFSectionList();
    }

    /**
     * 通过板块Id查找总浏览数、总评论数、主题数、贴数
     */
    @SessionNeedless
    @RequestMapping(value = "/fSectionCount", method = RequestMethod.GET)
    @ResponseBody
    public List<FSectionCountDTO> getFSectionListById(@RequestParam(required = false, defaultValue = "") String id,
                                                      @RequestParam(required = false, defaultValue = "") String name) {
        ObjectId e = null;
        if (!"".equals(id)) {
            e = new ObjectId(id);
        }
        return fSectionService.getFSectionListByLevel(e);
    }


    /**
     * 根据板块Id获取版块信息
     *
     * @param id
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/fSectionDetail", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getFSectionInf(@RequestParam(required = false, defaultValue = "") String id) {
        ObjectId e = new ObjectId(id);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        Map<String, Object> model = new HashMap<String, Object>();
        FSectionDTO Item = fSectionService.findFSectionById(e);
        model.put("sectionName", Item.getName());
        model.put("count", Item.getCount());
        model.put("theme", Item.getTotalCount());
        model.put("memoName", Item.getMemoName());
        model.put("image", Item.getImage());
        model.put("fSectionId", Item.getfSectionId());
        retList.add(model);
        return retList;
    }

    /**
     * 楼中楼发帖
     */
    @SessionNeedless
    @RequestMapping(value = "/forumReplyToReply", method = RequestMethod.GET)
    @ResponseBody
    public List forumReplyToReply(String Id, int postFlagId) {
        ObjectId e = null;
        if (!"".equals(Id)) {
            e = new ObjectId(Id);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (postFlagId == 0) {
            FReplyDTO fReplyDTO = fReplyService.detail(e);
            model.put("nickName", fReplyDTO.getPersonName());
            Date date = new Date(fReplyDTO.getTime());
            model.put("postTime", sdf.format(date));
            model.put("context", fReplyDTO.getPlainText());
        } else if (postFlagId == 1) {
            FPostDTO fPostDTO = fPostService.detail(e);
            model.put("nickName", fPostDTO.getPersonName());
            Date date = new Date(fPostDTO.getTime());
            model.put("postTime", sdf.format(date));
            model.put("context", fPostDTO.getPlainText());
        }
        model.put("postId", Id);
        model.put("postFlagId", postFlagId);

        List retList = new ArrayList();
        retList.add(model);
        return retList;
    }


    /**
     * 查找字符串中该子串循环替换
     */
    public String replaceLooper(String version, String versionVideo) {
        StringBuffer returnString = new StringBuffer();
        if (version.indexOf("class=\"content-DV\"") > -1) {
            if (versionVideo.indexOf(",") > -1) {
                String[] items = versionVideo.split(",");
                List<String> myItem = new ArrayList<String>();
                for (String item : items) {
                    myItem.add(item.split("\\$")[0]);
                }
                String endStr = "";
                for (String op : myItem) {
                    String str = "class=\"content-DV\"";
                    Pattern p = Pattern.compile(str);
                    Matcher m = p.matcher(version);
                    String replaceStr = "class=\"content-DV\"" + "ondblclick=\"download('" + op + "')\"";
                    String tmp = m.replaceFirst(replaceStr);
                    returnString.append(tmp.substring(0, tmp.indexOf("class=\"content-DV\"") + 18));
                    version = tmp.substring(tmp.indexOf("class=\"content-DV\"") + 18);
                    endStr = version;
                }
                returnString.append(endStr);
                return returnString.toString();
            } else {
                String replaceStr = "class=\"content-DV\"" + "ondblclick=\"download('" + versionVideo.split("\\$")[0] + "')\"";
                return version.replace("class=\"content-DV\"", replaceStr);
            }
        } else {
            return version;
        }

    }

    @SessionNeedless
    @RequestMapping(value = "/getReplyId", method = RequestMethod.GET)
    @ResponseBody
    public List<FReplyDTO> getReplyDataById(String replyId, @RequestHeader HttpHeaders headers) {
        List<FReplyDTO> fReplyDTOList = new ArrayList<FReplyDTO>();
        FReplyDTO fReplyDTO = fReplyService.appShare(new ObjectId(replyId), headers.getFirst("User-Agent"));
        fReplyDTO.setIsZan(0);
        SessionValue sv = getSessionValue();
        if (null != sv && !sv.isEmpty()) {
            String userId = sv.getId();
            boolean flag = false;
            List<String> userList = fReplyDTO.getUserReplyList();
            for (String item : userList) {
                if (userId.equals(item)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                fReplyDTO.setIsZan(1);
            }
        }
        fReplyDTOList.add(fReplyDTO);
        return fReplyDTOList;
    }

    /**
     * 通过发帖的Id获取发帖信息
     */
    @SessionNeedless
    @RequestMapping(value = "/fPostDetail", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getFPostInf(String postId,
                                                 @RequestHeader(value = "User-Agent") String client) {
        List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
        if (StringUtils.isBlank(postId) || !ObjectId.isValid(postId)) {
            return responseList;
        }
        ObjectId postObjectId = new ObjectId(postId);
        Map<String, Object> model = new HashMap<String, Object>();
        FPostDTO Item = fPostService.detail(postObjectId);
        if (null == Item) {
            return responseList;
        }
        fPostService.updateScanCount(postObjectId);
        //更新总浏览量
        fSectionService.updateTotalScanCount(new ObjectId(Item.getPostSectionId()));
        int zanCount = fPostService.getPostCountByPostId(postId);
        model.put("zan", zanCount);
        String appImageList = Item.getAppImageList();
        String appVideoList = Item.getAppVideoList();
        String comment = Item.getComment();
        String appComment = Item.getAppComment();

        SessionValue sv = getSessionValue();
        String version = Item.getVersion();
        String versionVideo = Item.getVersionVideo();
        int type = Item.getType();
        if (type == 2) {
            model.put("type", 1);
            //判断是否已解决
            int offerCompleted = Item.getOfferedCompleted();
            if (offerCompleted == 1) {
                model.put("offerCompleted", offerCompleted);
                String solution = Item.getSolution();
                FReplyDTO fReplyDTO = fReplyService.detail(new ObjectId(solution));
                model.put("solution", ParseUserLog.getNoHTMLString(fReplyDTO.getReplyComment(), 200));
                model.put("solutionId", fReplyDTO.getfReplyId());
                //回帖人昵称
                String pId = fReplyDTO.getPersonId();
                UserEntry userEntry = userService.find(new ObjectId(pId));
                if (null != userEntry) {
                    if (StringUtils.isNotBlank(userEntry.getNickName())) {
                        model.put("solNickName", userEntry.getNickName());
                    } else {
                        model.put("solNickName", userEntry.getUserName());
                    }
                    model.put("personImage", AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                } else {
                    model.put("solNickName", "改用户已被删除！");
                    model.put("personImage", "");
                }
            }
        }
        model.put("version", Item.getVersion());
        //对version进行改造
        if (StringUtils.isNotBlank(version)) {
            if (null != sv && !sv.isEmpty()) {
                //改造version，让视频能下载
                String result = replaceLooper(version, versionVideo);
                model.put("version", result);
            }
        }
        int inSet = Item.getInSet();
        if (inSet == 1) {
            model.put("InSet", 1);
            List<FReplyDTO> fReplyDTOList = fReplyService.getFReplyListRank(postId);
            model.put("fReplyDTOList", fReplyDTOList);
            if (null == fReplyDTOList || fReplyDTOList.size() == 0) {
                model.put("fReplyDTOCount", 1);
            }
            if (null != sv && !sv.isEmpty()) {
                model.put("IsLogin", 1);
                int praiseCount = fReplyService.getPraiseCount(postId, sv.getId());
                model.put("praiseCount", praiseCount);
            } else {
                model.put("IsLogin", 0);
            }
        } else {
            model.put("InSet", 0);
        }

        //投票贴显示
        String voteContent = Item.getVoteContent();
        String voteSelect = Item.getVoteSelect();
        int voteOptionCount = Item.getOptionCount();
        model.put("voteOptionCount", voteOptionCount);
        //判断是否为投票贴
        if (StringUtils.isNotBlank(voteContent)) {
            model.put("voteContent", voteContent);
            List<String> str = new ArrayList<String>();
            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            if (voteSelect.contains(",")) {
                String[] o = voteSelect.split(",");
                for (String item : o) {
                    str.add(item);
                }
                model.put("voteOptions", str);
            } else {
                str.add(voteSelect);
                model.put("voteOptions", str);
            }
            List<FVoteDTO> fVoteEntryList = fVoteService.getFVoteList(postId);
            int totalCount = fVoteEntryList.size();
            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(0);
            if (fVoteEntryList.size() > 0) {
                for (int i = 0; i < str.size(); i++) {
                    int j = i + 1;
                    int count = 0;
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (FVoteDTO fVoteDTO : fVoteEntryList) {
                        int number = fVoteDTO.getNumber();
                        if (j == number) {
                            count++;
                        }
                    }
                    double pItem = (double) count / (double) totalCount;
                    map.put("voteItemStr", str.get(i));
                    map.put("voteItemCount", count);
                    map.put("voteItemPercent", nt.format(pItem));
                    mapList.add(map);
                }
            }
            model.put("voteMapList", mapList);
            //该帖投票人数
            int voteCount = fVoteService.getFVoteCount(postId);
            model.put("voteCount", voteCount);
            String userId;
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
                //当登录时分为两种模式，投票了和未投票
                //判断是否投票过
                FVoteEntry fVoteEntry = fVoteService.getFVote(postId, userId);
                if (null != fVoteEntry) {
                    //已经投票了
                    model.put("voteType", "voteType");
                } else {
                    //未投票(显示第一种模式)
                    model.put("voteType", "Type");
                }
            } else {
                //当不登录时显示第二种模式
                model.put("voteType", "voteType");
            }
        }
        //标志位
        model.put("comment", comment);
        if (StringUtils.isNotBlank(comment)) {
            model.put("plainText", ParseUserLog.getNoHTMLString(comment, 300));
        } else {
            model.put("plainText", ParseUserLog.getNoHTMLString(appComment, 300));
        }

        if (getPlatform().isMobile()) {
            //web端上传的
            if (null != comment && !"".equals(comment)) {
                String strL = comment.replaceAll("/upload/ueditor", "http://www.fulaan.com/upload/ueditor");
                String strLL = strL.replaceAll("http://www.fulaan.comhttp://www.fulaan.com/", "http://www.fulaan.com/");
                model.put("content", strLL);
                //手机端分享的内容
                List<String> images = fPostService.dealWithImage(comment);
                List<String> imageList = new ArrayList<String>();
                for (String image : images) {
                    if (!image.contains("baidu")) {
                        imageList.add(image);
                    }
                }
                model.put("imageList", imageList);
                if (StringUtils.isNotBlank(version)) {
                    model.put("videoList", fPostService.dealVideo(versionVideo));
                } else {
                    model.put("videoList", fPostService.dealWithVideo(comment));
                }

            } else {
                //app端上传的app端显示
                if (null != appComment && !"".equals(appComment)) {
                    model.put("content", appComment);
                }
                //app端上传的web端显示
                model.put("plainText", Item.getPlainText());
                model.put("imageList", fPostService.getAppImageList(appImageList));
                model.put("videoList", fPostService.getAppVideoList(appVideoList));
            }
        } else {
            //web端处理app端上传的数据
            if (null != comment && !"".equals(comment)) {
                //web端上传的数据
                model.put("content", Item.getComment());
            } else {
                //app端上传的数据
                model.put("plainText", Item.getPlainText());
                model.put("imageList", fPostService.getAppImageList(appImageList));
                model.put("videoList", fPostService.getAppVideoList(appVideoList));
            }
        }
        List<RepliesDTO> fll = new ArrayList<RepliesDTO>();
        List<FReplyDTO> fl = fReplyService.getFRepliesList(2, "", "", "", 1, postId, 1, 6, client);
        if (null != fl && fl.size() > 0) {
            fll = fl.get(0).getRepliesList();
            if (null != fll && fll.size() > 0) {
                model.put("fl", fll);
            } else {
                model.put("fl", fll);
            }
        } else {
            model.put("fl", fll);
        }

        model.put("postTitle", Item.getPostTitle());
        model.put("postId", postId);
        long time = Item.getTime();
        long nowTime = System.currentTimeMillis();
        long day = (nowTime - time) / (1000 * 60 * 60 * 24);

        int collectionCount = fCollectionService.getCollectionCount(postId);
        model.put("time", day);
        model.put("collectionCount", collectionCount);
        model.put("personId", Item.getPersonId());
        model.put("scanCount", Item.getScanCount());

        model.put("cream", Item.getCream());
        model.put("top", Item.getIsTop());
        //获取某人发的帖子个数
        int themeCounts = fPostService.getPostCountByCondition(Item.getPersonId());
        //某人的回复数
        int replyCounts = fReplyService.getFRepliyCountByPersonId(Item.getPersonId());
        model.put("tc", themeCounts);
        model.put("rc", replyCounts);
        UserEntry dto = userService.searchUserId(new ObjectId(Item.getPersonId()));
        if (null != dto) {
            if (dto.getNickName() != null && !"".equals(dto.getNickName())) {
                model.put("personName", dto.getNickName());
            } else {
                model.put("personName", dto.getUserName());
            }
            model.put("avt", AvatarUtils.getAvatar(dto.getAvatar(), AvatarType.MAX_AVATAR.getType()));
            if (dto.getK6KT() == 1) {
                model.put("userRole", "管理员");
            } else {
                model.put("userRole", "一般用户");
            }
            model.put("score", dto.getForumScore());
        }
        int replyCount = fReplyService.getFRepliyCountByPostId(postId);
        model.put("replyCount", replyCount);
        responseList.add(model);
        return responseList;
    }

    /**
     * 获取楼中楼中列表
     *
     * @param replyId
     * @param headers
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getReply", method = RequestMethod.GET)
    @ResponseBody
    public FReplyDTO getReply(String replyId, @RequestHeader HttpHeaders headers) {
        String client = headers.getFirst("User-Agent");
        List<FReplyDTO> fl = fReplyService.getFRepliesList(2, "", "", "", 1, replyId, 1, 1, client);
        return fl.get(0);
    }

    /**
     * 根据personId获取该论坛用户的详细信息
     *
     * @param personId
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/fUserDetail", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getUserInfByPersonId(String postSectionId, String personId) {
        List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
        if (StringUtils.isBlank(postSectionId) || StringUtils.isBlank(personId)) {
            return responseList;
        } else if (!ObjectId.isValid(postSectionId) || !ObjectId.isValid(personId)) {
            return responseList;
        }
        Map<String, Object> model = getFPostById(postSectionId, personId);
        UserEntry dto = userService.searchUserId(new ObjectId(personId));
        model.put("personId", personId);
        if (null != dto) {
            if (dto.getNickName() != null && !"".equals(dto.getNickName())) {
                model.put("personName", dto.getNickName());
            } else {
                model.put("personName", dto.getUserName());
            }
            model.put("avt", AvatarUtils.getAvatar(dto.getAvatar(), AvatarType.MAX_AVATAR.getType()));
            if (dto.getK6KT() == 1) {
                model.put("userRole", "管理员");
            } else {
                model.put("userRole", "一般用户");
            }
            model.put("score", dto.getForumScore());
            long stars = fLevelService.getStars(dto.getForumExperience());
            model.put("stars", stars);
        }
        responseList.add(model);
        return responseList;
    }

    /**
     * 通过classify分类查询某板块的数量
     */
    @SessionNeedless
    @RequestMapping(value = "/fSectionCountById", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getPostCountBySectionClassify(@ObjectIdType ObjectId id) {
        Map<String, Object> model = fSectionService.getClassify(id);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        retList.add(model);
        return retList;
    }

    /**
     * 通过发帖人Id查询发帖人的基本信息（主题数/贴子数）
     */
    private Map<String, Object> getFPostById(String postSection,
                                             String person) {
        Map<String, Object> model = new HashMap<String, Object>();
        int themeCount = fPostService.getPostCountByCondition(postSection, person);
        int replyCount = fReplyService.getFRepliesCount(postSection, "", person, "", -1);
        model.put("tc", themeCount);
        model.put("rc", replyCount);
        return model;
    }

    /**
     * 通过回帖人Id查询发帖人的基本信息（主题数/贴子数）
     */
    @RequestMapping("/btnZan")
    @ResponseBody
    public RespObj getFlag(String userReplyId, String id) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(id));
            List<String> rl = fPostDTO.getUserReplyList();
            respObj.setCode(Constant.SUCCESS_CODE);
            if (rl.contains(userReplyId)) {
                respObj.setMessage(true);
            } else {
                respObj.setMessage(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("查询失败");
        }
        return respObj;
    }

    /**
     * 更新回帖点赞
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/updateReplyBtnZan")
    @ResponseBody
    public RespObj updateReplyBtnZan(String replyId, String userReply) {
        FReplyDTO fReplyDTO = fReplyService.detail(new ObjectId(replyId));
        List<String> rl = fReplyDTO.getUserReplyList();
        if (rl.contains(userReply)) {
            return RespObj.FAILD("已经点赞过了");
        }
        UserEntry userEntry = userService.find(new ObjectId(userReply));
        if (null != userEntry) {
            //获取登录的人的Id，只有登录的人才能点赞成功
            String userId = getUserId().toString();
            if (userId.equals(userEntry.getID().toString())) {
                fReplyService.updateBtnZan(new ObjectId(userReply), new ObjectId(replyId));
            }
        }
        return RespObj.SUCCESS(fReplyDTO.getPraiseCount() + 1);
    }

    /**
     * 更新点赞
     *
     * @param post      帖子id
     * @param flag      true--点赞---false--取消点赞
     * @param userReply 用户id
     * @return RespObj
     */
    @RequestMapping(value = "/updateBtnZan", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateBtnZan(String post, boolean flag, String userReply) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        FPostDTO fPostDTO = fPostService.detail(new ObjectId(post));
        List<String> rl = fPostDTO.getUserReplyList();
        if (flag) { //点赞
            if (rl.contains(userReply)) {
                respObj.setMessage("已经点过赞了,请刷新页面");
                return respObj;
            }
            UserEntry userEntry = userService.find(new ObjectId(userReply));
            if (null != userEntry) {
                fPostService.updateBtnZan(new ObjectId(userReply), new ObjectId(fPostDTO.getPersonId()), flag, new ObjectId(post));
            }
            respObj.setMessage(fPostDTO.getPraiseCount() + 1);

        } else { //取消点赞
            if (!rl.contains(userReply)) {
                respObj.setMessage("已经取消点赞了,请刷新页面");
                return respObj;
            }
            UserEntry userEntry = userService.find(new ObjectId(userReply));
            if (null != userEntry) {
                fPostService.updateBtnZan(new ObjectId(userReply), new ObjectId(fPostDTO.getPersonId()), flag, new ObjectId(post));
            }
            respObj.setMessage(fPostDTO.getPraiseCount() - 1);
        }
        respObj.setCode(Constant.SUCCESS_CODE);
        return respObj;
    }


    /**
     * 通过回帖人Id查询发帖人的基本信息（主题数/贴子数）
     */
    @RequestMapping("/oppose")
    @ResponseBody
    public RespObj oppose(String userReplyId, String id) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        boolean flag = false;
        try {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(id));
            List<String> rl = fPostDTO.getOpposeList();
            if (null != rl && rl.size() > 0) {
                for (String item : rl) {
                    if (userReplyId.equals(item)) {
                        flag = true;
                        break;
                    }
                }
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            if (flag) {
                respObj.setMessage(true);
            } else {
                respObj.setMessage(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("查询失败");
        }
        return respObj;
    }


    /**
     * 更新点赞
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/updateOppose", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateOppose(String post, boolean flag, String userReply) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            boolean io = false;
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(post));
            List<String> rl = fPostDTO.getOpposeList();
            if (null != rl && rl.size() > 0) {
                for (String item : rl) {
                    if (userReply.equals(item)) {
                        io = true;
                        break;
                    }
                }
            }
            if (flag) {
                if (io) {
                    respObj.setMessage("已经点过赞了,请刷新页面");
                    return respObj;
                }
            } else {
                if (!io) {
                    respObj.setMessage("已经取消赞了,请刷新页面");
                    return respObj;
                }
            }
            fPostService.updateBtnZan(new ObjectId(userReply), new ObjectId(fPostDTO.getPersonId()), flag, new ObjectId(post));
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新点赞失败");
        }
        return respObj;
    }

    /**
     * 上传视频
     *
     * @param
     * @return
     * @throws IllegalParamException
     * @throws IOException
     * @throws IllegalStateException
     * @throws EncoderException
     */
    @RequestMapping("/video/uploadVideo")
    @ResponseBody
    @SessionNeedless
    public Map<String, Object> uploadVideo(@RequestParam("Filedata") MultipartFile Filedata) throws IllegalParamException, IllegalStateException, IOException, EncoderException {

        Map map = new HashMap();
        String fileName = FilenameUtils.getName(Filedata.getOriginalFilename());

        String videoFilekey = new ObjectId().toString();

        File savedFile = File.createTempFile(videoFilekey, FilenameUtils.getExtension(fileName));
        OutputStream stream = new FileOutputStream(savedFile);
        stream.write(Filedata.getBytes());
        stream.flush();
        stream.close();

        String coverImage = new ObjectId().toString();
        Encoder encoder = new Encoder();
        File screenShotFile = File.createTempFile("coverImage", ".jpg");

        //是否生成了图片
        boolean isCreateImage = false;
        try {
            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            isCreateImage = true;
        } catch (Exception ex) {
        }

        //开始上传

        //上传图片
        String imgUrl = null;
        if (isCreateImage && screenShotFile.exists()) {
            QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);

            imgUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage);
        }

        VideoEntry ve = new VideoEntry(fileName, Filedata.getSize(), VideoSourceType.USER_VIDEO.getType(), videoFilekey);
        ve.setVideoSourceType(VideoSourceType.VOTE_VIDEO.getType());
        ve.setID(new ObjectId());
        QiniuFileUtils.uploadVideoFile(ve.getID(), videoFilekey, Filedata.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
        String url = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, videoFilekey);

        if (isCreateImage && screenShotFile.exists()) {
            ve.setImgUrl(imgUrl);
        }

        ObjectId videoId = videoService.addVideoEntry(ve);

        //删除临时文件
        try {
            savedFile.delete();
            screenShotFile.delete();
        } catch (Exception ex) {
            logger.error("", ex);
        }

        map.put("flg", true);
        map.put("vimage", QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage));
        map.put("vid", videoId.toString());
        map.put("vurl", QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()));
        return map;
    }


    //得到文件名
    private String getFileName(MultipartFile file) {
        String orgname = file.getOriginalFilename();
        return new ObjectId().toString() + Constant.POINT + orgname.substring(orgname.lastIndexOf(".") + 1);
    }

    /**
     * 上传帖子图片
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadImage", produces = "text/html; charset=utf-8")
    public
    @ResponseBody
    Map addBlogPic(MultipartRequest request) {
        Map result = new HashMap<String, Object>();
        String filePath = FileUtil.UPLOADBLOGDIR;
        List<String> fileUrls = new ArrayList<String>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String examName = getFileName(file);
                RespObj upladTestPaper = QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                if (upladTestPaper.getCode() != Constant.SUCCESS_CODE) {
                    throw new FileUploadException();
                }
                fileUrls.add(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, examName));
            }
            result.put("result", true);
            result.put("path", fileUrls);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }

    @RequestMapping("/uploadVoice")
    @ResponseBody
    public RespObj uploadVoice(MultipartRequest request, HttpServletRequest req) {
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            try {
                ObjectId id = new ObjectId();
                String fileKey = "postVideo-" + id.toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
                String bathPath = Resources.getProperty("uploads.file");
                File parentFile = new File(bathPath);
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                File destFile = new File(parentFile, file.getOriginalFilename());
                file.transferTo(destFile);
                InputStream inputStream = new FileInputStream(destFile);
                String extName = FilenameUtils.getExtension(file.getOriginalFilename());

                FileUploadDTO dto;
                if (extName.equalsIgnoreCase("amr")) {
                    String saveFileKey = new ObjectId().toString() + ".mp3";
                    com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
                    String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
                    dto = new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
                } else {
                    QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);
                    String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                    dto = new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
                }

                fileInfos.add(dto);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("", ex);
            }
        }
        return RespObj.SUCCESS(fileInfos);
    }

    /**
     * 查询回帖列表
     *
     * @param postSection      板块Id
     * @param post             该主题贴Id
     * @param person           personId（可以是楼主的，也可以是回帖人的）
     * @param sortType（2，时间排序）
     * @param page
     * @param pageSize
     * @return
     */
    @SessionNeedless
    @RequestMapping("/fReply")
    @ResponseBody
    public Map<String, Object> fReplyList(@RequestParam(required = false, defaultValue = "") String postSection,
                                          @RequestParam(required = false, defaultValue = "") String post,
                                          @RequestParam(required = false, defaultValue = "") String person,
                                          Integer sortType, Integer page, Integer pageSize,
                                          @RequestHeader("User-Agent") String client) {
        Map<String, Object> model = new HashMap<String, Object>();
        if (StringUtils.isBlank(post) || !ObjectId.isValid(post)) {
            return model;
        }
        try {
            SessionValue sv = getSessionValue();
            List<FReplyDTO> fReplyDTOs = new ArrayList<FReplyDTO>();
            ObjectId postId = new ObjectId(post);
            FPostDTO fPostDTO = fPostService.detail(postId);
            if (fPostDTO.getInSet() == 1 && sortType != 11) {
                sortType = 2;
            }
            List<FReplyDTO> fReplyDTOList = fReplyService.getFRepliesList(sortType, postSection, post, person, -1, "", page, pageSize, client);
            for (FReplyDTO item : fReplyDTOList) {
                List<FReplyDTO> f = fReplyService.getFRepliesList(sortType, postSection, "", person, 1, item.getfReplyId(), 1, 1, client);
                if (null != f && f.size() > 0) {
                    item.setRepliesFlag(1);
                    item.setRepliesList(f.get(0).getRepliesList());
                }
            }
            String userId = "";
            boolean flag = false;
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
                //能下載回帖視頻.(app/web端上傳的)
                for (FReplyDTO myItem : fReplyDTOList) {
                    String version = myItem.getVersion();
                    String versionVideo = myItem.getVersionVideo();
                    if (StringUtils.isNotBlank(version)) {
                        if (null != sv && !sv.isEmpty()) {
                            //改造version，让视频能下载
                            String result = replaceLooper(version, versionVideo);
                            myItem.setVersion(result);
                        }
                    }
                }
                //点赞与否
                for (FReplyDTO myItem : fReplyDTOList) {
                    List<String> userReplyList = myItem.getUserReplyList();
                    for (String u : userReplyList) {
                        if (userId.equals(u)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        myItem.setIsZan(1);
                    } else {
                        myItem.setIsZan(0);
                    }
                    fReplyDTOs.add(myItem);
                    flag = false;
                }
            } else {
                fReplyDTOs.addAll(fReplyDTOList);
            }
            int count = fReplyService.getFRepliesCount(postSection, post, person, "", -1);
            model.put("list", fReplyDTOs);
            model.put("count", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    /**
     * 组装image
     *
     * @param imageList
     * @return
     */
    public String getImageStr(String[] imageList) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < imageList.length; i++) {
            String item = "<p><img src=" + imageList[i] + " width=\"300\"  title=\"head-0.7220739887561649.jpg\" alt=\"head-0.7220739887561649.jpg\"></p><br/> ";
            str.append(item);
        }
        return str.toString();
    }

    /**
     * 组装video
     *
     * @param videoList
     * @return
     */
    public String getVideoStr(String[] videoList) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < videoList.length; i++) {
            String[] items = videoList[i].split("@");
            String item = "<p><video src=" + items[1] + " width=\"400\" height=\"360\" controls>\n" +
                    "        <source src=\"movie.mp4\" type=\"video/mp4\" />\n" +
                    "        <source src=\"movie.ogg\"  type=\"video/ogg\"/>\n" +
                    "</video></p><br/>";
            str.append(item);
        }
        return str.toString();
    }


    /**
     * 兼容语音上传
     */
    @RequestMapping(value = "/addFReplyVoice", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFReplyVoice(
            String comment, String plainText, String personId, String postSectionId, String postId,
            int postFlagId,
            String postReplyId,
            String nickName, //回復列表呢稱
            String content, //回復列表內容
            String imageStr,
            String videoStr,
            String audioStr,
            String voiceFile,
            @RequestHeader HttpHeaders headers
    ) {
        return addFReplyItem(comment, plainText, personId, postSectionId, postId,
                postFlagId,
                postReplyId,
                nickName, //回復列表呢稱
                content, //回復列表內容
                imageStr,
                videoStr,
                audioStr,
                voiceFile,
                headers);
    }

    private RespObj addFReplyItem(String comment, String plainText, String personId, String postSectionId, String postId,
                                  int postFlagId,
                                  String postReplyId,
                                  String nickName, //回復列表呢稱
                                  String content, //回復列表內容
                                  String imageStr,
                                  String videoStr,
                                  String audioStr,
                                  String voiceFile, HttpHeaders headers) {

        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        SessionValue sv = getSessionValue();
        if (userService.isUnSpeak(sv.getId())) {
            return RespObj.FAILD("sss");
        }

        comment = userService.filter(comment);
        plainText = userService.filter(plainText);
        FReplyDTO fReplyDTO = new FReplyDTO();
        fReplyDTO.setPostSectionId(postSectionId);
        fReplyDTO.setPlainText(plainText);
        fReplyDTO.setVoiceFile(voiceFile);
        String client = headers.getFirst("User-Agent");
        Platform pf = getPlatform();
        if ((pf == Platform.Android || pf == Platform.IOS) && postFlagId == 1) {
            if (StringUtils.isNotBlank(comment)) {
                String replyComment = dealData(comment);
                fReplyDTO.setReplyComment(replyComment);
                String version = replaceStr(comment);
                fReplyDTO.setVersion(version);
                List<String> urls = getVideoSrc(comment);
                StringBuilder versionVideo = new StringBuilder("");
                for (String item : urls) {
                    if (StringUtils.isNotBlank(item)) {
                        versionVideo.append(item + ",");
                    }
                }
                fReplyDTO.setVersionVideo(versionVideo.toString());
            } else {
                fReplyDTO.setAppImageList(imageStr);
                fReplyDTO.setAppVideoList(videoStr);
                fReplyDTO.setAppAudioStr(audioStr);
                fReplyDTO.setReplyComment(comment);
            }

        } else {
            String replyComment = dealData(comment);
            fReplyDTO.setReplyComment(replyComment);
            String version = replaceStr(comment);

            fReplyDTO.setVersion(version);
            List<String> urls = getVideoSrc(comment);
            String versionVideo = "";
            for (String item : urls) {
                if (!"".equals(versionVideo)) {
                    versionVideo = versionVideo + "," + item;
                } else {
                    versionVideo = item;
                }
            }
            fReplyDTO.setVersionVideo(versionVideo);
        }
        //回帖量
        int count = 0;
        String userId = "";
        String personName = "";
        String image = "";
        if (!sv.isEmpty()) {
            userId = sv.getId();
            personName = sv.getUserName();
            image = sv.getMinAvatar();
        }
        //该标志是为了说明是否为楼中贴 1不是 0是
        FRecordDTO fRecordDTO = new FRecordDTO();
        long time = System.currentTimeMillis();
        fRecordDTO.setPersonId(userId);
        fRecordDTO.setTime(time);
        if (postFlagId == 1) {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(postId));
            //判断是否为奖赏贴
            int type = fPostDTO.getType();
            if (type == 3) {
                //该奖赏贴总共奖赏次数
                int rewardCount = fPostDTO.getRewardCount();
                int rewardMax = fPostDTO.getRewardMax();
                int rewardScore = fPostDTO.getRewardScore();
                if (0 < rewardCount) {
                    //查询该人奖赏次数
                    int userReward = fReplyService.getFRepliesCount("", postId, userId, "", -1);
                    //判断是否大于每人最多奖赏次数
                    if (userReward < rewardMax) {
                        fPostService.updateRewardCountValue(new ObjectId(postId));
                        //额外奖赏积分
                        fScoreService.extraRewordaddScore(getUserId(), fPostDTO.getPostTitle(), rewardScore);
                    }
                }
            }
            int floor = fReplyService.getFloor("", postId, "", "", -1);
            String postTitle = fPostDTO.getPostTitle();
            fRecordDTO.setPostId(postId);
            fRecordDTO.setPostTitle(postTitle);
            fRecordDTO.setUserId(fPostDTO.getPersonId());
            fRecordDTO.setPostSectionId(fPostDTO.getPostSectionId());
            fRecordDTO.setLogRecord(2);
            fRecordDTO.setLogScan(0);

            fReplyDTO.setPostId(postId);
            count = fReplyService.getFRepliesCount(postSectionId, postId, personId, "", -1);

            fRecordService.addFRecordEntry(fRecordDTO);
            fReplyDTO.setPersonName(personName);
            fReplyDTO.setPersonId(userId);
            fReplyDTO.setReplyImage(image);
            fReplyDTO.setPlatform(pf.toString());
            fReplyDTO.setTime(System.currentTimeMillis());
            fReplyDTO.setUpdateTime(System.currentTimeMillis());
            int f = floor + 1;
            fReplyDTO.setFloor(f);

            ObjectId FReplyId = fReplyService.addFPostEntry(fReplyDTO);
            //保存回复消息
            logger.info("====执行======");
            fInformationService.saveReplyInfo(getUserId(), new ObjectId(fPostDTO.getPersonId()), new ObjectId(fPostDTO.getFpostId()), FReplyId);

            respObj.setMessage(FReplyId.toString());

            //更新最后回复人以及最后回复时间
            fPostService.updateReplyInf(new ObjectId(postId), new ObjectId(userId));

            //判断回复的是否为活动贴
            if (fPostDTO.getInSet() == 1) {
                //回帖加一分
                fScoreService.activityAddScore(getUserId(), fPostDTO.getPostTitle());
                //回帖加一点经验值
                userService.updateForumExperience(new ObjectId(userId), 2);
            } else {
                //回普通贴加一分
                fScoreService.replyForm(getUserId(), fPostDTO.getPostTitle());
                //回帖加一点经验值
                userService.updateForumExperience(new ObjectId(userId), 1);
            }

        } else {
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(postReplyId));
            String postTitle = fPostDTO.getPostTitle();
            fRecordDTO.setPostTitle(postTitle);
            fRecordDTO.setPostSectionId(fPostDTO.getPostSectionId());
            fRecordDTO.setLogRecord(2);
            fRecordDTO.setPostId(postReplyId);
            if (!postId.equals(postReplyId)) {
                if (nickName.lastIndexOf('回') > -1) {
                    String recordUserId = fReplyService.getUserIdByNickName(nickName);
                    fRecordDTO.setUserId(recordUserId);
                } else {
                    FReplyDTO fReplyDTO1 = fReplyService.detail(new ObjectId(postId));
                    fRecordDTO.setUserId(fReplyDTO1.getPersonId());
                }
            } else {
                fRecordDTO.setUserId(fPostDTO.getPersonId());
            }

            fRecordService.addFRecordEntry(fRecordDTO);

            fReplyDTO.setReplyPostId(postId);
            //当是楼中贴时，值为1
            fReplyDTO.setRepliesToReply(1);
            fReplyDTO.setPersonName(personName);
            fReplyDTO.setPersonId(userId);
            fReplyDTO.setReplyImage(image);
            fReplyDTO.setPlatform(pf.toString());
            fReplyDTO.setTime(System.currentTimeMillis());
            fReplyDTO.setUpdateTime(System.currentTimeMillis());
            List<RepliesDTO> repliesDTOList = new ArrayList<RepliesDTO>();
            RepliesDTO replies = new RepliesDTO();
            replies.setNickName(nickName);
            String replyUserId = fReplyService.getUserIdByNickName(nickName);
            if (StringUtils.isNotBlank(replyUserId)) {
                replies.setUserId(replyUserId);
            }
            replies.setContent(content);
            replies.setImageStr(sv.getMinAvatar());
            replies.setPersonId(sv.getId());
            replies.setTime(System.currentTimeMillis());
            repliesDTOList.add(replies);

            fReplyDTO.setRepliesList(repliesDTOList);
            int tCount = fReplyService.getFRepliesCount("", "", "", postId, 1);
            if (tCount > 0) {
                List<FReplyDTO> fl = fReplyService.getFRepliesList(2, "", "", "", 1, postId, 1, 1, client);
                //向内嵌文档中加入数据
                List<RepliesDTO> f = fl.get(0).getRepliesList();
                f.add(replies);
                fl.get(0).setRepliesList(f);
                fReplyService.removeFReply(new ObjectId(fl.get(0).getfReplyId()));
                ObjectId replyId = fReplyService.addFPostEntry(fl.get(0));
                //保存回复消息
            } else {
                ObjectId FReplyId = fReplyService.addFPostEntry(fReplyDTO);
                // 保存回复消息
            }
            //回帖加一分
            fScoreService.floorInFloorRewordScore(getUserId(), fReplyDTO.getPlainText());
            //回帖加一点经验值
            userService.updateForumExperience(new ObjectId(userId), 1);
        }
        try {
            //更新总回帖量
            fSectionService.updateReplyAndPost(new ObjectId(postSectionId));
            fSectionService.updateTotalComment(new ObjectId(postSectionId));
            if (postFlagId == 1) {
                fPostService.updateCommentCount(new ObjectId(postId));
            } else if (postFlagId == 0) {
                fPostService.updateCommentCount(new ObjectId(postReplyId));
            }
            userService.updatePostCount(new ObjectId(userId));
            //完成发帖任务
            fMissionService.post(userId);
            //当回帖数超过一百时，楼主加积分
            FPostDTO fPostDTO;
            if (postFlagId == 1) {
                fPostDTO = fPostService.detail(new ObjectId(postId));
            } else {
                fPostDTO = fPostService.detail(new ObjectId(postReplyId));
            }
            if (count >= 100 && fPostDTO.getPostFlag() == 0) {
                //回帖数超过一百时，楼主加积分
                fScoreService.hundredFloorRewordAddScore(getUserId());
                fPostDTO.setPostFlag(1);
                fPostService.addFPostEntry(fPostDTO);
                userService.updateForumExperience(new ObjectId(personId), 10);
            }
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("添加失败");
        }
        return respObj;
    }

    /**
     * 添加回帖
     *
     * @param comment       富文本框的内容
     * @param plainText     纯文本
     * @param personId      主题帖的发帖人（楼主）信息
     * @param postSectionId 该主题贴的板块Id
     * @param postId        跟帖Id（）
     * @param postFlagId    1:不是楼中楼0：是楼中楼
     * @param postReplyId   不是楼中楼时和postId一样,楼中楼时还是帖子主题Id
     * @param nickName      为了区别楼中楼中的回复信息
     * @param content       楼中楼回复时恢复的内容
     * @param imageStr      app端上传的图片路径，以，分隔开
     * @param videoStr      app端上传的视频路径，以，分隔开
     * @param headers
     * @return
     */
    @RequestMapping(value = "/addFReply", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFReply(String comment, String plainText, String personId, String postSectionId, String postId,
                             int postFlagId,
                             String postReplyId,
                             String nickName, //回復列表呢稱
                             String content, //回復列表內容
                             String imageStr,
                             String videoStr,
                             String audioStr,
                             @RequestHeader HttpHeaders headers) {

        return addFReplyItem(comment, plainText, personId, postSectionId, postId,
                postFlagId,
                postReplyId,
                nickName, //回復列表呢稱
                content, //回復列表內容
                imageStr,
                videoStr,
                audioStr,
                "",
                headers);

    }


    /**
     * 更新板块（移动板块）
     *
     * @param postId     帖子id 用@分隔
     * @param fSectionId 板块id
     * @return RespObj
     */
    @RequestMapping(value = "/updateFPost", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj updateFPost(String postId, String fSectionId) {
        String[] postIds = postId.split("@");
        for (String id : postIds) {
            if (StringUtils.isNotBlank(id)) {
                try {
                    FPostDTO fPostDTO = fPostService.detail(new ObjectId(id));
                    FSectionDTO Item = fSectionService.findFSectionById(new ObjectId(fSectionId));
                    fPostDTO.setPostSectionName(Item.getName());
                    fPostDTO.setPostSectionId(fSectionId);
                    fPostService.addFPostEntry(fPostDTO);
                } catch (Exception e) {
                    return RespObj.FAILD("移动板块失败！");
                }
            }
        }
        return RespObj.SUCCESS;
    }

    /**
     * 添加举报
     *
     * @param postId
     * @param reason
     * @return
     */
    @RequestMapping(value = "/addReported", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addReported(String postId, String reason) {
        try {

            ReportedDTO reportedDTO = new ReportedDTO();
            SessionValue sv = getSessionValue();
            String userId = "";
            if (null != sv && !sv.isEmpty()) {
                userId = sv.getId();
            }
            reportedDTO.setUserId(userId);
            reportedDTO.setTime(System.currentTimeMillis());
            reportedDTO.setReason(reason);
            FPostEntry.Reported reported = reportedDTO.exportEntry();
            FPostDTO fPostDTO = fPostService.detail(new ObjectId(postId));

            //判断是否为已处理的举报
            if (fPostDTO.getReported() == 2) {
                fPostDTO.setReportedDTOList(new ArrayList<ReportedDTO>());
            }
            String reportedComment = "";
            int reportedExperience = 0;
            //清空掉
            fPostService.addFPostEntry(fPostDTO);
            fPostService.updateReported(postId, reported, 1, reportedComment, reportedExperience);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.SUCCESS("举报失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 获取活跃排行榜
     */
    @SessionNeedless
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    @ResponseBody
    public List<UserInfoDTO> getFSectionDTOs(@RequestParam(required = false, defaultValue = "5") int pageSzie,
                                             @RequestParam(required = false, defaultValue = "1") String flag) {
        return userService.getUserListByPostCount(pageSzie, flag);
    }

    /**
     * 添加悬赏帖子
     *
     * @param classify      0:全部 1:官方公告 2:精彩活动 3:问题求助 4:功能建议
     * @param postId        区分是编辑还是新增
     * @param comment       富文本框中的内容
     * @param plainText     纯文本
     * @param draught       1：默认不是草稿
     * @param postSectionId 板块Id
     * @param postTitle     主题内容
     * @param imageStr      app端上传的图片路径，以，分隔开
     * @param videoStr      app端上传的视频路径，以，分隔开
     * @param request
     * @return
     */
    @RequestMapping(value = "/addOfferedPost", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addOfferedPost(int classify, String postId, String comment, String plainText, int draught, String postSectionId, String postTitle,
                                  String imageStr,
                                  String videoStr,
                                  long offeredScore,
                                  HttpServletRequest request) {
        return addFVote(classify, postId, comment, plainText, draught, postSectionId, postTitle,
                imageStr,
                videoStr,
                2,
                0,
                0,
                0,
                offeredScore,
                0,
                "",
                "",
                1,
                request);

    }


    /**
     * 添加投票贴
     *
     * @param classify
     * @param postId
     * @param comment
     * @param plainText
     * @param draught
     * @param postSectionId
     * @param postTitle
     * @param imageStr
     * @param videoStr
     * @param voteContent
     * @param voteSelect
     * @param request
     * @return
     */
    @RequestMapping(value = "/addVote", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addVote(int classify, String postId, String comment, String plainText, int draught, String postSectionId, String postTitle,
                           String imageStr,
                           String videoStr,
                           String voteContent,
                           String voteSelect,
                           int optionCount,
                           HttpServletRequest request) {
        return addFVote(classify, postId, comment, plainText, draught, postSectionId, postTitle,
                imageStr,
                videoStr,
                1,
                0,
                0,
                0,
                0L,
                0,
                voteContent,
                voteSelect,
                optionCount,
                request);
    }

    /**
     * 添加回帖奖励帖子
     *
     * @param classify      0:全部 1:官方公告 2:精彩活动 3:问题求助 4:功能建议
     * @param postId        区分是编辑还是新增
     * @param comment       富文本框中的内容
     * @param plainText     纯文本
     * @param draught       1：默认不是草稿
     * @param postSectionId 板块Id
     * @param postTitle     主题内容
     * @param imageStr      app端上传的图片路径，以，分隔开
     * @param videoStr      app端上传的视频路径，以，分隔开
     * @param type
     * @param request
     * @return
     */
    @RequestMapping(value = "/addFRewardPost", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFRewardPost(int classify, String postId, String comment, String plainText, int draught, String postSectionId, String postTitle,
                                  String imageStr,
                                  String videoStr,
                                  int type,
                                  int rewardScore,
                                  int rewardCount,
                                  int rewardMax,
                                  HttpServletRequest request) {
        return addFVote(classify, postId, comment, plainText, draught, postSectionId, postTitle,
                imageStr,
                videoStr,
                type,
                rewardScore,
                rewardCount,
                rewardMax,
                0L,
                0,
                "",
                "",
                1,
                request);

    }

    /**
     * 添加帖子
     *
     * @param classify      0:全部 1:官方公告 2:精彩活动 3:问题求助 4:功能建议
     * @param postId        区分是编辑还是新增
     * @param comment       富文本框中的内容
     * @param plainText     纯文本
     * @param draught       1：默认不是草稿
     * @param postSectionId 板块Id
     * @param postTitle     主题内容
     * @param imageStr      app端上传的图片路径，以，分隔开
     * @param videoStr      app端上传的视频路径，以，分隔开
     * @param request
     * @return
     */
    @RequestMapping(value = "/addFPost", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFPost(int classify, String postId, String comment, String plainText, int draught, String postSectionId, String postTitle,
                            String imageStr,
                            String videoStr,
                            HttpServletRequest request) {
        return addFVote(classify, postId, comment, plainText, draught, postSectionId, postTitle,
                imageStr,
                videoStr,
                0,
                0,
                0,
                0,
                0L,
                0,
                "",
                "",
                1,
                request);

    }

    private RespObj addFVote(int classify, String postId, String comment, String plainText, int draught, String postSectionId, String postTitle,
                             String imageStr,
                             String videoStr,
                             int type,
                             int rewardScore,
                             int rewardCount,
                             int rewardMax,
                             long offeredScore,
                             int offeredCompleted,
                             String voteContent,
                             String voteSelect,
                             int optionCount,
                             HttpServletRequest request) {


        if (userService.isSilenced(getUserId().toString())) {
            return RespObj.FAILD("您已被禁言!");
        }

        if (userService.isUnSpeak(getUserId().toString())) {
            return RespObj.FAILD("您已被禁言!");
        }

        String url = "http://" + request.getServerName() + ":" + request.getServerPort();
        String imageSrc = dealWith(comment, url);
        FPostDTO fPostDTO = new FPostDTO();
        fPostDTO.setPostFlag(0);
        fPostDTO.setIsTop(0);
        fPostDTO.setCream(0);
        fPostDTO.setClassify(classify);
        fPostDTO.setDraught(draught);
        fPostDTO.setOptionCount(optionCount);
        fPostDTO.setType(type);
        fPostDTO.setRewardScore(rewardScore);
        fPostDTO.setRewardCount(rewardCount);
        fPostDTO.setRewardMax(rewardMax);

        fPostDTO.setOfferedScore(offeredScore);
        fPostDTO.setOfferedCompleted(offeredCompleted);

        comment = userService.filter(comment);

        fPostDTO.setBackUpComment(comment);
        String content = dealData(comment);

        String version = replaceStr(comment);
        fPostDTO.setVersion(version);
        List<String> urls = getVideoSrc(comment);
        String versionVideo = "";
        for (String item : urls) {
            if (!"".equals(versionVideo)) {
                versionVideo = versionVideo + "," + item;
            } else {
                versionVideo = item;
            }
        }
        fPostDTO.setVersionVideo(versionVideo);
        fPostDTO.setImageSrc(imageSrc);
        fPostDTO.setPlainText(plainText);
        fPostDTO.setPostSectionId(postSectionId);
        fPostDTO.setPostTitle(postTitle);
        fPostDTO.setRemoveStatus(0);
        if (StringUtils.isNotBlank(voteContent)) {
            fPostDTO.setVoteContent(voteContent);
        }

        if (StringUtils.isNotBlank(voteSelect)) {
            fPostDTO.setVoteSelect(voteSelect);
        }
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        Platform pf = getPlatform();
        String commentStr = "";
        String appImageFirst = "";

        if (pf.isMobile()) {
            if (StringUtils.isNotBlank(comment)) {
                fPostDTO.setComment(content);
            } else {
                String imgStr = "";
                String vidStr = "";
                fPostDTO.setAppImageList(imageStr);
                fPostDTO.setAppVideoList(videoStr);
                if (StringUtils.isNotBlank(imageStr)) {
                    if (imageStr.contains(",")) {
                        String[] imageList = imageStr.split(",");
                        imgStr = getImageStr(imageList);
                        fPostDTO.setImageSrc(imageList[0]);
                        appImageFirst = imageList[0];
                    } else {
                        String[] image = new String[1];
                        image[0] = imageStr;
                        imgStr = getImageStr(image);
                        fPostDTO.setImageSrc(imageStr);
                        appImageFirst = imageStr;
                    }
                }
                if (StringUtils.isNotBlank(videoStr)) {
                    if (videoStr.contains(",")) {
                        String[] videoList = videoStr.split(",");
                        vidStr = getVideoStr(videoList);
                    } else {
                        String[] video = new String[1];
                        video[0] = videoStr;
                        vidStr = getVideoStr(video);
                    }
                }
                commentStr = vidStr + "<p>" + plainText + "</p><br/>" + imgStr + "<br/>";
                fPostDTO.setAppComment(commentStr);
                fPostDTO.setComment(comment);
            }

        } else {
            fPostDTO.setComment(content);
        }
        try {
            SessionValue sv = getSessionValue();
            String userId = sv.getId();
            String userName = sv.getUserName();
            String image = sv.getMinAvatar();
            if (type == 3) {
                int rewardTotal = 0 - rewardScore * rewardCount;
                fScoreService.addFScore(getUserId(), 4, 0, rewardTotal);
            }
            if (type == 2) {
                long re = 0 - offeredScore;
                fScoreService.addFScore(getUserId(), 3, (int) re);
            }
            FSectionDTO Item = fSectionService.findFSectionById(new ObjectId(postSectionId));
            //更新发帖量
            fSectionService.updateThemeAndPost(new ObjectId(postSectionId));
            fSectionService.updateThemeCount(new ObjectId(postSectionId));

            fPostDTO.setPostSectionName(Item.getName());
            fPostDTO.setPersonId(userId);
            fPostDTO.setPersonName(userName);
            fPostDTO.setPlatform(pf.toString());
            fPostDTO.setImage(image);
            if (StringUtils.isNotBlank(postId)) {
                FPostDTO fPostDTO1 = fPostService.detail(new ObjectId(postId));
                fPostDTO1.setAppImageList(imageStr);
                fPostDTO1.setAppVideoList(videoStr);
                fPostDTO1.setImageSrc(appImageFirst);
                fPostDTO1.setPostTitle(postTitle);
                fPostDTO1.setClassify(classify);
                fPostDTO1.setComment(content);
                fPostDTO1.setImageSrc(imageSrc);
                fPostDTO1.setBackUpComment(comment);
                fPostDTO1.setPlainText(plainText);
                fPostDTO1.setVersion(version);
                fPostDTO1.setVersionVideo(versionVideo);
                fPostDTO1.setAppComment(commentStr);
                fPostService.addFPostEntry(fPostDTO1);
                respObj = RespObj.SUCCESS;
                respObj.setMessage(postId);
                return respObj;
            }
            fPostDTO.setTime(System.currentTimeMillis());
            fPostDTO.setUpdateTime(System.currentTimeMillis());
            fPostDTO.setUpdateDateTime(System.currentTimeMillis());
            ObjectId FPostId = fPostService.addFPostEntry(fPostDTO);
            userService.updateForum(new ObjectId(userId), 3, 3);
            //奖励发帖积分
            fScoreService.addFScore(getUserId(), 1);
            //完成发帖任务
            fMissionService.post(userId);
            //每日发帖3个及以上经验值多加5点
            Date dNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            String nowDate = sdf.format(dNow);
            Map<String, String> map = new HashMap<String, String>();
            String key = sv.getId() + nowDate;
            Map<String, String> mapL = CacheHandler.getMapValue(key);
            if (null != mapL && !mapL.isEmpty()) {
                int count = Integer.parseInt(mapL.get("count"));
                count = count + 1;
                if (count >= 3 && Boolean.valueOf(mapL.get("fsl"))) {
                    //每天发帖多发3个以上获得5个积分
                    fScoreService.addFScore(getUserId(), 2);
                    map.put("fsl", String.valueOf(false));
                    map.put("count", String.valueOf(count));
                    CacheHandler.cache(key, map, Constant.SECONDS_IN_DAY);
                } else {
                    if (Boolean.valueOf(mapL.get("fsl"))) {
                        map.put("count", String.valueOf(count));
                        CacheHandler.cache(key, map, Constant.SECONDS_IN_DAY);
                    }
                }
            } else {
                map.put("count", String.valueOf(1));
                map.put("fsl", String.valueOf(true));
                CacheHandler.cache(key, map, Constant.SECONDS_IN_DAY);
            }
            String FPostMessage = FPostId.toString();
            respObj = RespObj.SUCCESS;
            respObj.setMessage(FPostMessage);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("添加失败");
        }
        return respObj;
    }

    /**
     * 删除帖子
     *
     * @param post
     * @return
     */
    @RequestMapping(value = "/deletePost", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteFPost(String post) {
        if (StringUtils.isBlank(post) || !ObjectId.isValid(post)) {
            return RespObj.FAILD("参数不正确");
        }
        try {
            SessionValue sv = getSessionValue();
            String userId = sv.getId();
            ObjectId postId = new ObjectId(post);
            FRecordDTO fRecordDTO = new FRecordDTO();
            FPostDTO fPostDTO = fPostService.detail(postId);
            String postTitle = fPostDTO.getPostTitle();
            long time = System.currentTimeMillis();
            fRecordDTO.setPersonId(userId);
            fRecordDTO.setPostId(post);
            fRecordDTO.setTime(time);
            fRecordDTO.setPostTitle(postTitle);
            fRecordDTO.setLogRecord(1);
            fRecordDTO.setLogScan(0);
            fRecordDTO.setUserId(fPostDTO.getPersonId());
            fRecordDTO.setPostSectionId(fPostDTO.getPostSectionId());
            fRecordService.addFRecordEntry(fRecordDTO);
            fPostService.deleteFPost(postId);
            fReplyService.removeFReplyByPostId(postId);
        } catch (Exception e) {
            return RespObj.FAILD("删除失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 删除评论回帖
     *
     * @param reply 回复ID
     * @param post  帖子ID
     * @return RespObj
     */
    @RequestMapping(value = "/deleteReply", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public RespObj deleteReply(String reply, String post) {
        if (StringUtils.isBlank(reply) || StringUtils.isBlank(post)) {
            return RespObj.FAILD("参数为空");
        }
        if (!ObjectId.isValid(reply) || !ObjectId.isValid(post)) {
            return RespObj.FAILD("ID类型不正确");
        }
        try {
            ObjectId id = new ObjectId(reply);
            fReplyService.removeFReply(id);
            ObjectId postId = new ObjectId(post);
            fPostService.updateDecCommentCount(postId);
        } catch (Exception e) {
            return RespObj.FAILD("出错了");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 更新精华帖子
     *
     * @param post
     * @param personId
     * @param cream
     * @return
     */
    @RequestMapping(value = "/updatePostCream", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateFPostCream(String post, String personId, int cream) {
        try {
            fPostService.updateCream(new ObjectId(post), cream);
            //楼主精华帖子加积分
            if (cream == 1) {
                userService.updateForumScore(new ObjectId(personId), 15);
                userService.updateForumExperience(new ObjectId(personId), 15);
                FScoreDTO fScoreDTO = new FScoreDTO();
                fScoreDTO.setTime(System.currentTimeMillis());
                fScoreDTO.setType(2);
                fScoreDTO.setOperation("版主设置精华");
                fScoreDTO.setPersonId(personId);
                fScoreDTO.setScoreOrigin("版主设置为精华帖子！");
                fScoreDTO.setScore(15);
                fScoreService.addFScore(fScoreDTO);
            } else if (cream == 0) {
                userService.updateForumScore(new ObjectId(personId), -15);
                userService.updateForumExperience(new ObjectId(personId), -15);
                FScoreDTO fScoreDTO = new FScoreDTO();
                fScoreDTO.setTime(System.currentTimeMillis());
                fScoreDTO.setType(2);
                fScoreDTO.setOperation("版主取消精华");
                fScoreDTO.setPersonId(personId);
                fScoreDTO.setScoreOrigin("版主取消该精华帖子！");
                fScoreDTO.setScore(-15);
                fScoreService.addFScore(fScoreDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RespObj.FAILD("更新精华失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * 更新置顶帖子
     *
     * @param post
     * @param top
     * @return RespObj
     */
    @RequestMapping(value = "/updatePostTop", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateFPostTop(@ObjectIdType ObjectId post, int top) {
        try {
            fPostService.updateTop(post, top);
        } catch (Exception e) {
            return RespObj.FAILD("更新精华失败");
        }
        return RespObj.SUCCESS;
    }

    /**
     * loginInfo
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SessionNeedless
    @RequestMapping("/loginInfo")
    @ResponseBody
    public Map<String, Object> getLoginInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        loginInfo(request, response, model);
        return model;
    }

    @RequestMapping("/forumVisited")
    public String forumVisited() {
        return "/forum/forumUnvisit";
    }

    @RequestMapping("/forumSilenced")
    public String forumSilenced() {
        return "/forum/forumSilenced";
    }

    @RequestMapping(value = "/sectionId", method = RequestMethod.GET)
    @ResponseBody
    public FSectionDTO getValidate(String pId) {
        return fSectionService.getValidate(new ObjectId(pId.trim()));
    }

    @RequestMapping(value = "/addFSections", method = RequestMethod.POST)
    @ResponseBody
    public RespObj getValidate(String pId, String memo) {
        FSectionDTO fSectionDTO = fSectionService.findFSectionById(new ObjectId(pId));
        fSectionDTO.setMemo(memo);
        fSectionService.addFSection(fSectionDTO);
        return RespObj.SUCCESS;
    }

    /**
     * 获取视频列表
     *
     * @param plainContext 纯文本
     * @return List<String>
     */
    private List<String> getVideoSrc(String plainContext) {
        String video = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
        String video_regex = "<video.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (video_regex, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(plainContext);
        while (m_image.find()) {
            video = video + "," + m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(video);//匹配src
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        List<String> videoUrl = new ArrayList<String>();
        for (int i = 0; i < pics.size(); i++) {
            String item = pics.get(i).toLowerCase();

            if (item.lastIndexOf(".mp4") > -1 || item.lastIndexOf(".wmv") > -1 || item.lastIndexOf(".wav") > -1 ||
                    item.lastIndexOf(".flv") > -1 || item.lastIndexOf(".swf") > -1 || item.lastIndexOf(".mkv") > -1 ||
                    item.lastIndexOf(".avi") > -1 || item.lastIndexOf(".rm") > -1 || item.lastIndexOf(".rmvb") > -1 ||
                    item.lastIndexOf(".mpeg") > -1 || item.lastIndexOf(".mpg") > -1 || item.lastIndexOf(".ogg") > -1 ||
                    item.lastIndexOf(".ogv") > -1 || item.lastIndexOf(".mov") > -1 || item.lastIndexOf(".webm") > -1 ||
                    item.lastIndexOf(".mid") > -1) {
                videoUrl.add(item);
            }
            if (item.indexOf(".mp3") > -1 || item.indexOf(".cd") > -1
                    || item.indexOf(".wma") > -1 || item.indexOf(".mp3pro") > -1 || item.indexOf(".real") > -1
                    || item.indexOf(".ape") > -1 || item.indexOf(".module") > -1 || item.indexOf(".midi") > -1 || item.indexOf(".vqf") > -1) {
                if (item.indexOf("$") > -1) {
                    StringBuffer str = new StringBuffer();
                    String[] temp = item.split("\\$");
                    str.append(temp[0] + "$");
                    str.append("http://www.fulaan.com/static/images/forum/musicll.jpg");
                    videoUrl.add(str.toString());
                }
            }
        }
        return videoUrl;
    }

    /**
     * 新版本替换内容
     *
     * @param comment
     * @return
     */
    private String replaceStr(String comment) {
        List<String> urls = getVideoSrc(comment);
        String str = "<div class=\"content-DV\"" + ">" +
                "<img class=\"content-img content-Im videoshow2\" vurl=\"/img/eBusiness/stem.mp4\"" +
                "src=\"/img/eBusiness/stem.jpg\"" + ">" +
                "<img src=\"/static/images/play.png\" class=\"video-play-btn\"" +
                "onclick=\"tryPlayYCourse('/img/eBusiness/stem.mp4')\"" + "></div>";
        String regex = "<\\s*video\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(comment);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            sb.append(matcher.replaceAll(str) + "\n");
        }
        String sbStr = sb.toString();
        if (!"".equals(sbStr)) {
            String destStr = "";
            String ss[] = sbStr.split("</div>");
            for (int i = 0; i < ss.length - 1; i++) {
                String url = urls.get(i);
                String ssl[] = url.split("\\$");
                if (ssl[0].indexOf(".mp3") > -1 || ssl[0].indexOf(".cd") > -1
                        || ssl[0].indexOf(".wma") > -1 || ssl[0].indexOf(".mp3pro") > -1 || ssl[0].indexOf(".real") > -1
                        || ssl[0].indexOf(".ape") > -1 || ssl[0].indexOf(".module") > -1 || ssl[0].indexOf(".midi") > -1 || ssl[0].indexOf(".vqf") > -1) {
                    destStr = destStr + ss[i].replace("/img/eBusiness/stem.mp4", ssl[0]).replace("/img/eBusiness/stem.jpg", "/static/images/forum/musicll.jpg") + "</div>";
                } else {
                    destStr = destStr + ss[i].replace("/img/eBusiness/stem.mp4", ssl[0]).replace("/img/eBusiness/stem.jpg", ssl[1]) + "</div>";
                }
            }
            destStr = destStr + ss[ss.length - 1];
            return destStr;
        } else {
            return comment;
        }
    }

    /**
     * 替换内容
     *
     * @param comment 评论
     * @return String
     */
    private String dealData(String comment) {

        List<String> urls = getVideoSrc(comment);
        String str = "<video src=\"/img/eBusiness/stem.mp4\" width=\"400\" height=\"360\" controls>\n" +
                "        <source src=\"movie.mp4\" type=\"video/mp4\" />\n" +
                "        <source src=\"movie.ogg\"  type=\"video/ogg\"/>\n";
        String regex = "<\\s*video\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(comment);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            sb.append(matcher.replaceAll(str) + "\n");
        }
        String sbStr = sb.toString();
        if (!"".equals(sbStr)) {
            String destStr = "";
            String ss[] = sbStr.split("</video>");
            for (int i = 0; i < ss.length - 1; i++) {
                String url = urls.get(i);
                String ssl[] = url.split("\\$");
                destStr = destStr + ss[i].replace("/img/eBusiness/stem.mp4", ssl[0]) + "</video>";
            }
            destStr = destStr + ss[ss.length - 1];
            return destStr;
        } else {
            return comment;
        }
    }

    /**
     * 获取首页图片
     *
     * @param plainContext
     * @param url
     * @return
     */
    public String dealWith(String plainContext, String url) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";//图片链接地址
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(plainContext);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);//匹配src
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        if (pics.size() > 0) {
            return pics.get(0);
        } else {
            return url + "/static" + "/images" + "/forum" + "/default-post.jpg";
        }
    }

    private void loginInfo(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws Exception {
        SessionValue sessionValue = getSessionValue();
        if (null == sessionValue || sessionValue.isEmpty()) {
            model.put("userName", "");
            model.put("userId", "");
            model.put("login", false);
            model.put("k6kt", -1);
            model.put("signIn", false);
            return;
        }

        if (userService.isSilenced(sessionValue.getId())) {
            response.sendRedirect("/forum/forumVisited.do");
            return;
        }

        Map<String, Object> map = fMissionService.findTodayMissionByUserId(sessionValue.getId());
        model.put("signIn", map.get("signIn"));
        model.put("userPermission", sessionValue.getUserRole());
        model.put("userName", sessionValue.getUserName());
        if ("".equals(sessionValue.getRealName())) {
            model.put("nickName", sessionValue.getUserName());
        } else {
            model.put("nickName", sessionValue.getRealName());
        }
        String temp = request.getParameter("pSectionId");
        if (null != temp && !"".equals(temp)) {
            boolean collect = fCollectionService.isCollected(new ObjectId(sessionValue.getId()), temp);
            model.put("collect", collect);
        }

        UserEntry userEntry = userService.find(new ObjectId(sessionValue.getId()));
        model.put("forumScore", userEntry.getForumScore());
        model.put("forumExperience", userEntry.getForumExperience());
        long stars = fLevelService.getStars(userEntry.getForumExperience());
        model.put("stars", stars);
        model.put("userId", sessionValue.getId());
        model.put("login", true);
        model.put("k6kt", sessionValue.getK6kt());
        model.put("avatar", sessionValue.getMinAvatar());
    }
}
