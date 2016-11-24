package com.fulaan.interceptor;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.entry.ConcernEntry;
import com.fulaan.service.CommunityService;
import com.fulaan.service.ConcernService;
import com.fulaan_old.forum.service.FCollectionService;
import com.fulaan_old.forum.service.FLevelService;
import com.fulaan_old.forum.service.FMissionService;
import com.fulaan.user.service.UserService;
import com.fulaan_old.friendscircle.service.FriendService;
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

import static com.fulaan.controller.BaseController.SESSION_VALUE;

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
  private CommunityService communityService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HandlerMethod method = (org.springframework.web.method.HandlerMethod) handler;
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

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response,
                         Object handler, ModelAndView modelAndView)
          throws Exception {
    HandlerMethod method = (org.springframework.web.method.HandlerMethod) handler;
    LoginInfo loginInfo = method.getMethodAnnotation(LoginInfo.class);
    if (loginInfo != null) {
      loginInfo(request, modelAndView.getModel());
    }
  }

  private void loginInfo(HttpServletRequest request, Map<String, Object> model) throws IOException {
    SessionValue sessionValue = getSessionValue(request);
    if (null == sessionValue || sessionValue.isEmpty()) {
      model.put("login", false);
      return;
    }

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
    UserEntry userEntry = userService.find(new ObjectId(sessionValue.getId()));
    if (null != userEntry) {
      model.put("forumScore", userEntry.getForumScore());
      model.put("forumExperience", userEntry.getForumExperience());
      long stars = fLevelService.getStars(userEntry.getForumExperience());
      model.put("stars", stars);
    } else {
      model.put("forumScore", 0L);
      model.put("forumExperience", 0L);
    }

    //获取自己的标签信息
    List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
    List<String> tags = new ArrayList<String>();
    for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
      tags.add(userTagEntry.getTag());
    }
    model.put("tags", tags);
  }

  protected SessionValue getSessionValue(HttpServletRequest request) {
    return (SessionValue) request.getAttribute(SESSION_VALUE);
  }
}
