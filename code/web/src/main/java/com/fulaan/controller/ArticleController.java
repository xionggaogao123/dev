package com.fulaan.controller;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.cache.CacheHandler;
import com.pojo.app.SessionValue;
import com.sys.constants.Constant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jerry on 2016/9/8.
 * 文章Controller
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private static Map<String, String> titles = new HashMap<String, String>();

    static {
        titles.put("article01", "孩子迷恋智能手机怎么办？");
        titles.put("article02", "别用成才的标准 埋没孩子才智");
        titles.put("article03", "小升初衔接 教你三招快速适应初中生活");
        titles.put("article04", "父母的11种行为会严重磨灭孩子的自信");
        titles.put("article05", "7大秘笈 让孩子愉快又高效地完成家庭作业");
    }

    /**
     * 文章页
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/article")
    @LoginInfo
    public String article(Map<String, Object> map, int id) {

        SessionValue sessionValue = CacheHandler.getSessionValue("article0" + id);
        try {
            String countStr = sessionValue.get("article0" + id);
            int count = Integer.parseInt(countStr);
            sessionValue.put("article0" + id, String.valueOf(++count));
            CacheHandler.cacheSessionValue("article0" + id, sessionValue, Constant.SECONDS_IN_DAY);
        } catch (NumberFormatException e) {
            sessionValue = new SessionValue("article0" + id, String.valueOf(1));
            CacheHandler.cacheSessionValue("article0" + id, sessionValue, Constant.SECONDS_IN_DAY);
        }
        String path = getRequest().getServletContext().getRealPath("/");
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/article/article0" + id + ".html"), "UTF-8"));
            String s;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                if (s.startsWith("h")) {
                    s = s.replace("h", "");
                    result.append("<h2><span style=\"font-weight:bold;\">" + s + "</span></h2><br/>");
                } else if (s.startsWith("*")) {
                    s = s.replace("*", "");
                    result.append("<p style=\"text-indent:2em;\">" + s + "</p><br/>");
                } else {
                    result.append("<p>" + s + "</p><br/>");
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("image", "/static/images/article/article0" + id + ".jpg");
        map.put("title", titles.get("article0" + id));
        map.put("article", result.toString());
        return "/article/article";
    }
}
