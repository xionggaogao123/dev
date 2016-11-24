package com.fulaan.forum.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.forum.service.FPostService;
import com.fulaan.forum.service.FReplyService;
import com.fulaan.forum.service.FSectionService;
import com.pojo.app.Platform;
import com.pojo.app.SessionValue;
import com.pojo.forum.FPostDTO;
import com.pojo.forum.FReplyDTO;
import com.pojo.forum.FSectionCountDTO;
import com.pojo.forum.FSectionDTO;
import com.pojo.user.UserRole;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/5/31.
 * 帖子、板块处理中心
 */
@Controller
@RequestMapping("/forum")
public class FPostController extends BaseController {

    @Autowired
    private FPostService fPostService;
    @Autowired
    private FSectionService fSectionService;
    @Autowired
    private FReplyService fReplyService;


    /**
     * 首页
     * @param request
     * @param model
     * @return
     */
    @SessionNeedless
    @RequestMapping("/index")
    public String index(HttpServletRequest request, Map<String, Object> model){
        loginInfo(request, model);
        return "/forum/index";
    }

    /**
     * 板块发帖首页
     * @param request
     * @param model
     * @return
     */
    @SessionNeedless
    @RequestMapping("/postIndex")
    public String postIndex(HttpServletRequest request, Map<String, Object> model){
        loginInfo(request, model);
        model.put("pSectionId", request.getParameter("pSectionId"));
        return "/forum/postIndex";
    }

    /**
     * 回帖页
     * @param request
     * @param model
     * @return
     */
    @SessionNeedless
    @RequestMapping("/postDetail")
    public String postDetail(HttpServletRequest request, Map<String, Object> model){
        loginInfo(request, model);
        model.put("pSectionId", request.getParameter("pSectionId"));
        model.put("postId", request.getParameter("postId"));
        return "/forum/postDetail";
    }

    /**
     * 新增帖子页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/newPost")
    public String newPost(HttpServletRequest request, Map<String, Object> model){
        model.put("pSectionId",request.getParameter("pSectionId"));
        return "/forum/newPost";
    }

    /**
     * 帖子列表
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
                                          @RequestParam(required = false, defaultValue = "-1") int classify,
                                          @RequestParam(required = false, defaultValue = "-1") int  cream,
                                          @RequestParam(required = false, defaultValue = "0") long  gtTime,
                                          int sortType, int page, int pageSize){
        Map<String, Object> model = new HashMap<String, Object>();

        List<FPostDTO> fPostDTOList=fPostService.getFPostsList(sortType,postSection,"",page,cream,classify,gtTime,pageSize);

        int count  = fPostService.getFPostsCount(postSection,"",cream, classify, gtTime);
        model.put("list", fPostDTOList);
        model.put("count", count);
        return model;
    }


    /**
     * 板块列表
     * @param level
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/fSection", method = RequestMethod.GET)
    @ResponseBody
    public List<FSectionDTO> getEGoodsCategoryDTOs(@RequestParam(required = false , defaultValue = "1") int level,
                                                   @RequestParam(required = false , defaultValue = "") String id,
                                                   @RequestParam(required = false , defaultValue = "") String name){
        ObjectId e=null;
        if(!"".equals(id)){
            e=new ObjectId(id);
        }
        return fSectionService.getFSectionListByLevel(level, e,name);
    }

    /**
     *  通过板块Id查找总浏览数、总评论数、主题数、贴数
     */
    @RequestMapping(value = "/fSectionCount", method = RequestMethod.GET)
    @ResponseBody
    public List<FSectionCountDTO> getFSectionListByParentId(@RequestParam(required = false , defaultValue = "") String id,
                                                            @RequestParam(required = false , defaultValue = "") String name){
        ObjectId e=null;
        if(!"".equals(id)){
            e=new ObjectId(id);
        }
        return fSectionService.getFSectionListByLevel(e,name);
    }

    /**
     * 通过发帖人Id查询发帖人的基本信息（主题数/贴子数）
     */
     public Map<String,Object> getFPostById(@RequestParam(required = false , defaultValue = "") String postSection,
                                            @RequestParam(required = false , defaultValue = "") String post,
                                            @RequestParam(required = false , defaultValue = "") String person){
         Map<String,Object> model = new HashMap<String, Object>();
         int themeCount=fPostService.getFPostsCount(postSection,person, -1,-1, 0);
         int replyCount=fReplyService.getFRepliesCount(postSection,post,person);
         model.put("tc",themeCount);
         model.put("rc",replyCount);
         return model;
     }


    /**
     * 查询回帖列表
     * @param postSection
     * @param post
     * @param sortType
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
                                          int sortType, int page, int pageSize){
        Map<String, Object> model = new HashMap<String, Object>();

        List<FReplyDTO> fReplyDTOList=fReplyService.getFRepliesList(sortType, postSection, post, person,page, pageSize);

        int count  = fReplyService.getFRepliesCount(postSection, post,person);
        model.put("list", fReplyDTOList);
        model.put("count", count);
        return model;
    }

    /**
     * 添加回帖
     * @param fReplyDTO
     * @return
     */
    @RequestMapping(value="/addFReply", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFReply(@RequestBody FReplyDTO fReplyDTO,@RequestHeader HttpHeaders headers){
        RespObj respObj = RespObj.FAILD;
        String client = headers.getFirst("User-Agent");
        Platform pf = null;
        if (client.contains("iOS")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")){
            pf = Platform.Android;
        } else {
            pf = Platform.PC;
        }
        try {
            SessionValue sv =getSessionValue();
            String userId="";
            String personName="";
            String image="";
            if(null!=sv && !sv.isEmpty())
            {
                userId=sv.getId();
                personName=sv.getUserName();
                image=sv.getAvatar();
            }
            fReplyDTO.setPersonName(personName);
            fReplyDTO.setPersonId(userId);
            fReplyDTO.setReplyImage(image);
            fReplyDTO.setPlatform(pf.toString());
            ObjectId FReplyId = fReplyService.addFPostEntry(fReplyDTO);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("添加失败");
        }

        return respObj;
    }

    /**
     * 添加帖子
     * @param fPostDTO
     * @return
     */
    @RequestMapping(value="/addFPost", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addFPost(@RequestBody FPostDTO fPostDTO,@RequestHeader HttpHeaders headers){
        RespObj respObj = RespObj.FAILD;
        String client = headers.getFirst("User-Agent");
        Platform pf = null;
        if (client.contains("iOS")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")){
            pf = Platform.Android;
        } else {
            pf = Platform.PC;
        }
        try {
            SessionValue sv =getSessionValue();
            String userId="";
            String userName="";
            String image="";
            if(null!=sv && !sv.isEmpty())
            {
                userId=sv.getId();
                userName=sv.getUserName();
                image=sv.getAvatar();
            }
            fPostDTO.setPersonId(userId);
            fPostDTO.setPersonName(userName);
            fPostDTO.setImage(image);
            fPostDTO.setPlatform(pf.toString());
            ObjectId FPostId = fPostService.addFPostEntry(fPostDTO);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("添加失败");
        }

        return respObj;
    }


    @SessionNeedless
    @RequestMapping("/loginInfo")
    @ResponseBody
    public Map<String, Object> getLoginInfo(HttpServletRequest request){
        Map<String, Object> model = new HashMap<String, Object>();
        loginInfo(request, model);
        return model;
    }

    private void loginInfo(HttpServletRequest request, Map<String, Object> model){
        SessionValue sessionValue = (SessionValue)request.getAttribute(BaseController.SESSION_VALUE);
        if( null==sessionValue || sessionValue.isEmpty()){
            model.put("userName", "");
            model.put("userId", "");
            model.put("login", false);
            model.put("k6kt", -1);
        } else {
            model.put("userName",sessionValue.getUserName());
            model.put("userId", sessionValue.getId());
            model.put("login", true);
            model.put("k6kt", sessionValue.getK6kt());
            model.put("avatar", sessionValue.getMinAvatar());
            model.put("isStudent", UserRole.isStudent(sessionValue.getUserRole()));
        }
    }



}
