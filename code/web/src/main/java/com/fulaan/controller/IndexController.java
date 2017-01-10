package com.fulaan.controller;

import com.db.fcommunity.BannerDao;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.forum.service.FPostService;
import com.fulaan.forum.service.FSectionService;
import com.pojo.fcommunity.Banner;
import com.pojo.forum.FSectionCountDTO;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by jerry on 2016/9/8.
 * Index索引
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private FSectionService fSectionService;
    @Autowired
    private FPostService fPostService;

    private BannerDao mBannerDao = new BannerDao();

    @RequestMapping("webim/index")
    public String webim() {
        return "/webim/webim";
    }

    @LoginInfo
    @SessionNeedless
    @RequestMapping("/entrance")
    public String entrance() {
        return "/community/index";
    }

    @RequestMapping("/competionStatus")
    @SessionNeedless
    @LoginInfo
    public String competionStatus() {
        return "/forum/competitionStatus";
    }

    @SessionNeedless
    @RequestMapping("/competition")
    @LoginInfo
    public String competition(Map<String, Object> model) {
        model.put("count", fPostService.competitionPostCount());
        return "/forum/competition";
    }

    @SessionNeedless
    @RequestMapping("/integrate")
    @LoginInfo
    public String toIntegratePage() {
        return "/mall/integrate";
    }

    @SessionNeedless
    @RequestMapping("/forum")
    @LoginInfo
    public String forum(HttpServletRequest request, Map<String, Object> map) {
        String uId = request.getParameter("uId");
        if (StringUtils.isNotBlank(uId)) {
            CacheHandler.cache("trackId", uId, Constant.SECONDS_IN_DAY);
        }
        List<FSectionCountDTO> fSectionDTOs = fSectionService.getFSectionList(2);
        Collections.sort(fSectionDTOs);
        map.put("sections", fSectionDTOs);
        return "/forum/index";
    }

    @SessionNeedless
    @RequestMapping("/mall")
    @LoginInfo
    public String mallSection(Map<String, Object> model) {
        model.put("categoryId", getRequest().getParameter("categoryId"));
        model.put("levelCategoryId", getRequest().getParameter("levelCategoryId"));
        List<Banner> banners = mBannerDao.getBanner(2);
        model.put("banners", banners);
        model.put("haha", "sdfdsf");
        return "/mall/jsp_index";
    }

    @SessionNeedless
    @RequestMapping("/communityIndex")
    @LoginInfo
    public String newIndex() {
        return "/community/index";
    }

    @RequestMapping("/communityCreate")
    @LoginInfo
    public String communityCreate() {
        return "/community/communityCreate";
    }

    @RequestMapping("/communityJoin")
    @LoginInfo
    public String communityJoin() {
        return "/community/communityJoin";
    }

}
