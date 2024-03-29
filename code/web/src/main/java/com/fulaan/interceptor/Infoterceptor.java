package com.fulaan.interceptor;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.communityValidate.service.ValidateInfoService;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.service.CommunitySystemInfoService;
import com.fulaan.service.ConcernService;
import com.fulaan.forum.service.FCollectionService;
import com.fulaan.forum.service.FLevelService;
import com.fulaan.forum.service.FMissionService;
import com.fulaan.user.service.UserService;
import com.fulaan.friendscircle.service.FriendService;
import com.pojo.app.SessionValue;
import com.pojo.user.UserEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fulaan.base.BaseController.SESSION_VALUE;

/**
 * Created by jerry on 2016/9/8.
 * 将登录信息放在controller处理完之后
 * controller的方法注解上有LoginInfo注解
 */
public class Infoterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private FMissionService fMissionService;
    @Autowired
    private FCollectionService fCollectionService;
    @Autowired
    private FLevelService fLevelService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private ConcernService concernService;
    @Autowired
    private CommunitySystemInfoService communitySystemInfoService;
    @Autowired
    private FriendApplyService friendApplyService;
    @Autowired
    private ValidateInfoService validateInfoService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            LoginInfo loginInfo = method.getMethodAnnotation(LoginInfo.class);
            if (loginInfo != null) {
                SessionValue sessionValue = getSessionValue(request);
                if (sessionValue != null) {
                    if (StringUtils.isNotBlank(sessionValue.getId())) {
                        if (userService.isSilenced(sessionValue.getId())) {
                            response.sendRedirect("/forum/forumVisited.do");
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
            throws Exception {
        if(handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            LoginInfo loginInfo = method.getMethodAnnotation(LoginInfo.class);
            if (loginInfo != null) {
                loginInfo(request, modelAndView.getModel());
            }
        }
    }

    private void loginInfo(HttpServletRequest request, Map<String, Object> model) throws IOException {
        SessionValue sessionValue = getSessionValue(request);
        if (null == sessionValue || sessionValue.isEmpty()) {
            model.put("login", false);
            return;
        }

        int systemInfoCount = communitySystemInfoService.findUnReadInfo(new ObjectId(sessionValue.getId()));
        int applyValidateInfoCount = validateInfoService.getValidateInfoCount(new ObjectId(sessionValue.getId()));
        model.put("systemInfoCount", systemInfoCount+applyValidateInfoCount);
        model.put("xitongCount",systemInfoCount);
        model.put("applyComCount",applyValidateInfoCount);
        int friendApplyCount = friendApplyService.countNoResponseReply(sessionValue.getId());
        model.put("friendApplyCount", friendApplyCount);
        model.put("infoCount",systemInfoCount+friendApplyCount+applyValidateInfoCount);
        Map<String, Object> map = fMissionService.findTodayMissionByUserId(sessionValue.getId());
        model.put("signIn", map.get("signIn"));
        model.put("userPermission", sessionValue.getUserRole());
        model.put("userName", sessionValue.getUserName());
        model.put("userId", sessionValue.getId());
        model.put("login", true);
        model.put("k6kt", sessionValue.getK6kt());
        model.put("avatar", sessionValue.getMinAvatar());
        int friendCount = friendService.countFriend(sessionValue.getId());
        int concernCount = concernService.countConcernList(new ObjectId(sessionValue.getId()));
        model.put("friendCount", friendCount);
        model.put("concernCount", concernCount);
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
        UserEntry userEntry = userService.findById(new ObjectId(sessionValue.getId()));
        if (null != userEntry) {
            model.put("forumScore", userEntry.getForumScore());
            model.put("forumExperience", userEntry.getForumExperience());
            long stars = fLevelService.getStars(userEntry.getForumExperience());
            model.put("stars", stars);
            if(StringUtils.isNotBlank(userEntry.getGenerateUserCode())){
                model.put("packageCode",userEntry.getGenerateUserCode());
            }else{
                model.put("packageCode",sessionValue.getId());
            }
            //获取自己的标签信息
            List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
            List<String> tags = new ArrayList<String>();
            for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
                tags.add(userTagEntry.getTag());
            }
            model.put("tags", tags);
        } else {
            model.put("forumScore", 0L);
            model.put("forumExperience", 0L);
        }
    }

    protected SessionValue getSessionValue(HttpServletRequest request) {
        return (SessionValue) request.getAttribute(SESSION_VALUE);
    }
}
