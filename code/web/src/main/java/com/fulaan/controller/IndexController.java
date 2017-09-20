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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/9/8.
 * Index索引
 */
@Api(value="Index索引")
@Controller
public class IndexController extends BaseController {

    @Autowired
    private FSectionService fSectionService;
    @Autowired
    private FPostService fPostService;

    private BannerDao mBannerDao = new BannerDao();
    @ApiOperation(value = "webim", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("webim/index")
    public String webim() {
        return "/webim/webim";
    }
    @ApiOperation(value = "webim", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @LoginInfo
    @SessionNeedless
    @RequestMapping("/entrance")

    public String entrance() {
        return "/community/index";
    }
    @ApiOperation(value = "competionStatus", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/competionStatus")
    @SessionNeedless
    @LoginInfo
    public String competionStatus() {
        return "/forum/competitionStatus";
    }

    @ApiOperation(value = "competition", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @SessionNeedless
    @RequestMapping("/competition")
    @LoginInfo
    public String competition(Map<String, Object> model) {
        model.put("count", fPostService.competitionPostCount());
        return "/forum/competition";
    }

    @ApiOperation(value = "toIntegratePage", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @SessionNeedless
    @RequestMapping("/integrate")
    @LoginInfo
    public String toIntegratePage() {
        return "/mall/integrate";
    }

    @ApiOperation(value = "forum", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
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

    @ApiOperation(value = "mallSection", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
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

    @ApiOperation(value = "newIndex", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @SessionNeedless
    @RequestMapping("/communityIndex")
    @LoginInfo
    public String newIndex() {
        return "/community/index";
    }

    @ApiOperation(value = "communityCreate", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/communityCreate")
    @LoginInfo
    public String communityCreate() {
        return "/community/communityCreate";
    }

    @ApiOperation(value = "communityJoin", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/communityJoin")
    @LoginInfo
    public String communityJoin() {
        return "/community/communityJoin";
    }

}
