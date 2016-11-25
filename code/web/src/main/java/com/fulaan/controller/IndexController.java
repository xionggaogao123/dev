package com.fulaan.controller;

import com.db.fcommunity.BannerDao;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.cache.CacheHandler;
import com.fulaan.pojo.Article;
import com.fulaan.forum.service.FPostService;
import com.fulaan.forum.service.FSectionService;
import com.pojo.app.SessionValue;
import com.pojo.fcommunity.Banner;
import com.pojo.forum.FSectionCountDTO;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(IndexController.class);
    private BannerDao mBannerDao = new BannerDao();
    @Autowired
    private FSectionService fSectionService;
    @Autowired
    private FPostService fPostService;

    private static Map<String, String> titles = new HashMap<String, String>();

    static {
        titles.put("article01", "孩子迷恋智能手机怎么办？");
        titles.put("article02", "别用成才的标准 埋没孩子才智");
        titles.put("article03", "小升初衔接 教你三招快速适应初中生活");
        titles.put("article04", "父母的11种行为会严重磨灭孩子的自信");
        titles.put("article05", "7大秘笈 让孩子愉快又高效地完成家庭作业");
    }

    @RequestMapping("webim/index")
    public String index() {
        return "/webim/webim";
    }


    @LoginInfo
    @SessionNeedless
    @RequestMapping("/entrance")
    public String index(HttpServletRequest request, Map<String, Object> model) {
        return "/community/index";
    }

    @RequestMapping("/competionStatus")
    @SessionNeedless
    @LoginInfo
    public String competionStatus() {
        return "/forum/competitionStatus";
    }


    @LoginInfo
    @SessionNeedless
    @RequestMapping("/forumIndex")
    public String forumIndex(HttpServletRequest request, Map<String, Object> model) {
        model.put("categoryId", request.getParameter("categoryId"));
        List<Article> articles = new ArrayList<Article>();
        for (int i = 1; i < 6; i++) {
            Article article = new Article();
            article.setTitle(titles.get("article0" + i));
            article.setImageUrl("/static/images/article/article0" + i + ".jpg");
            article.setTime("2016-9-9");
            article.setTarget("/article/article?id=" + i);
            articles.add(article);
            SessionValue sessionValue = CacheHandler.getSessionValue("article0" + i);
            String countStr = sessionValue.get("article0" + i);
            try {
                int count = Integer.parseInt(countStr);
                article.setCount(count);
            } catch (NumberFormatException e) {
                article.setCount(0);
            }
        }
        model.put("articles", articles);
        return "index";
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
        List<FSectionCountDTO> fSectionDTOs = fSectionService.getFSectionList();
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
        return "/mall/jsp_index";
    }

    @SessionNeedless
    @RequestMapping("/communityIndex")
    @LoginInfo
    public String newIndex(Map<String, Object> model) {
        return "/community/index";
    }


    @RequestMapping("/communityCreate")
    @LoginInfo
    public String communityCreate(Map<String, Object> model) {
        return "/community/communityCreate";
    }

    @RequestMapping("/communityJoin")
    @LoginInfo
    public String communityJoin(Map<String, Object> model) {
        return "/community/communityJoin";
    }

    @SessionNeedless
    @RequestMapping("/preview/pdf")
    public String previewPdf() {
        return "forum/previewPdf";
    }

    @SessionNeedless
    @RequestMapping("/preview/images")
    public String images() {
        return "forum/previewWord";
    }

}
