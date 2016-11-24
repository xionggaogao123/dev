package com.fulaan.review.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.comment.service.ComplexCommentService;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.resources.service.CloudResourceService;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.review.service.ReviewService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.PageDTO;
import com.pojo.comment.ComplexCommentEntry;
import com.pojo.comment.StarLevel;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.review.ReviewEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;

/**
 * 评课议课功能
 * Created by Caocui on 2015/8/26.
 */
@Controller
@RequestMapping("/reviewcourse")
public class ReviewController extends BaseController {

    /**
     * 评课议课列表页面
     */
    private static final String PAGE_REVIEW_LIST = "reviewcourse/reviewList";
    /**
     * 回复页面
     */
    private static final String PAGE_DETAIL_LIST = "reviewcourse/reviewDetail";

    /**
     * 新增页面
     */
    private static final String PAGE_RELEASE_COURSE = "reviewcourse/new";

    @Autowired
    private SchoolService schoolService = new SchoolService();

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ResourceDictionaryService resourceDictionaryService;

    @Autowired
    private CloudResourceService resourceService;

    @Autowired
    private ComplexCommentService complexCommentService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private EducationBureauService educationBureauService;

    /**
     * 议课列表页面
     *
     * @return ModelAndView
     */
    @SessionNeedless
    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PAGE_REVIEW_LIST);
        return modelAndView;
    }

    /**
     * 新增议课页面
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public ModelAndView add() { 
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PAGE_RELEASE_COURSE);
        return modelAndView;
    }

    /**
     * 获取评课议课列表
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public RespObj query(@RequestParam(value = "queryId", defaultValue = "") String queryId,
                         @RequestParam(value = "columName", defaultValue = "") String columName,
                         @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    	int role=getSessionValue().getUserRole();
		String schoolId=getSessionValue().getSchoolId();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(), role, schoolId);
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法查询!";
			return respObj;
		}
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        PageDTO<ReviewEntry> pageDTO;
        if (StringUtils.isEmpty(queryId) || StringUtils.isEmpty(columName)) {
            pageDTO = reviewService.findReviewEntries(pageNo, pageSize,ebe.getID());
        } else {
            pageDTO = reviewService.findReviewEntries(queryId, columName, pageNo, pageSize,ebe.getID());
        }

        List<Map<String, Object>> fomartList = fomartList(pageDTO.getList());

        Map<String, Object> pageMap = new HashMap<String, Object>();
        int realNum = (pageDTO.getCount() + pageSize - 1) / pageSize;
        pageMap.put("total", pageDTO.getCount());
        pageMap.put("pageNo", pageNo > realNum ? realNum : pageNo);
        Map<String, Object> resultMap = new HashMap<String, Object>(Constant.TWO);
        resultMap.put("datas", fomartList);
        resultMap.put("pagejson", pageMap);
        respObj.setMessage(resultMap);
        return respObj;
    }

    /**
     * 组装前台要用的list
     *
     * @param srcList
     * @return
     */
    private List<Map<String, Object>> fomartList(List<ReviewEntry> srcList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> m;
        for (ReviewEntry e : srcList) {
            m = new HashMap<String, Object>();
            m.put("id", e.getID().toString());
            m.put("name", e.getReviewName());
            m.put("userName", e.getPublishUserName());
            m.put("schoolName", e.getSchoolName());
            m.put("subject", resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(e.getEducationSubject())).getName());
            m.put("version", resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(e.getTextbookVersion())).getName());
            m.put("cover", e.getReviewCover());
            m.put("publishTime", DateTimeUtils.convert(e.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
            list.add(m);
        }
        return list;
    }

    /**
     * 发布评课议课
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseBody
    public RespObj release(ReviewCourseView view) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        int role=getSessionValue().getUserRole();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId educationBureauId = ebe.getID();
        view.setUserName(getSessionValue().getUserName());
        view.setUserId(getSessionValue().getId());
        view.setSchoolId(getSessionValue().getSchoolId());
        view.setSchoolName(schoolService.findSchoolNameByUserId(view.getUserId()));
        ObjectId rId = new ObjectId(view.getCover());
        view.setCover(resourceService.getResourceEntryById(rId).getImgUrl());
        view.setEducationId(educationBureauId);
        reviewService.addReviewCourse(view.getEntry());
        resourceService.removeById(rId);
        return respObj;
    }


    /**
     * 议课回复页面
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView detail(String id) {
        ModelAndView modelAndView = new ModelAndView();
        String userId = getSessionValue().getId();
        ReviewEntry reviewEntry = reviewService.getReviewEntry(id);
        boolean isMine = reviewEntry.getPublicUserId().toString().equals(userId);

        //加载备选课件
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>(reviewEntry.getCoursewareList().size());
        for (ObjectId fileId : reviewEntry.getCoursewareList()) {
            ResourceEntry re = resourceService.getResourceEntryById(fileId);
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("id", re.getID().toString());
            m.put("fileName", re.getName());
            m.put("type", re.getType());
            fileList.add(m);
        }

        List<Map<String, Object>> commentList = findCommentForReview(new ObjectId(id));
        modelAndView.addObject("id", id);
        modelAndView.addObject("isMine", isMine);
        modelAndView.addObject("headImage", AvatarUtils.getAvatar(userService.searchUserId(reviewEntry.getPublicUserId()).getAvatar(), Constant.TWO));
        modelAndView.addObject("schoolName", reviewEntry.getSchoolName());
        modelAndView.addObject("publishUserName", reviewEntry.getPublishUserName());
        modelAndView.addObject("publishTime", DateTimeUtils.convert(reviewEntry.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
        modelAndView.addObject("fileList", JSON.toJSONString(fileList));
        ResourceEntry entry = resourceService.getResourceEntryById(reviewEntry.getClassRecord());
        modelAndView.addObject("video", entry == null ? "" : QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, entry.getBucketkey()));
        modelAndView.addObject("commentList", JSON.toJSONString(commentList));
        modelAndView.addObject("commentNum", commentList.size());
        modelAndView.setViewName(PAGE_DETAIL_LIST);
        return modelAndView;
    }


    /**
     * 组装
     *
     * @param targetId
     * @return
     */
    private List<Map<String, Object>> findCommentForReview(ObjectId targetId) {
        List<ComplexCommentEntry> rootCommentList = complexCommentService.queryComplexCommentsByTargetId(targetId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(rootCommentList.size());
        //评课议课的评论信息
        Map<String, Object> rootCommentMap;
        //评论的回复
        List<Map<String, Object>> subCommentList;
        List<ComplexCommentEntry> subCommentEntryList;
        Map<String, Object> subCommentMap;
        for (ComplexCommentEntry rce : rootCommentList) {
            rootCommentMap = new HashMap<String, Object>();
            subCommentEntryList = complexCommentService.queryComplexCommentsByParentId(rce.getID().toString());
            subCommentList = new ArrayList<Map<String, Object>>(subCommentEntryList.size());
            for (ComplexCommentEntry sce : subCommentEntryList) {
                subCommentMap = new HashMap<String, Object>();
                subCommentMap.put("id", sce.getID().toString());
                subCommentMap.put("advantage", sce.getAdvantage());
                subCommentMap.put("weakPoint", sce.getWeakPoint());
                subCommentMap.put("commentUserId", sce.getCommentUserId() == null ? Constant.EMPTY : sce.getCommentUserId().toString());
                subCommentMap.put("commentUserName", sce.getCommentUserName());
                subCommentMap.put("replyUserId", sce.getReplyUserId() == null ? Constant.EMPTY : sce.getReplyUserId().toString());
                subCommentMap.put("replyUserName", sce.getReplyUserName());
                subCommentMap.put("parentId", sce.getParentId());
                subCommentMap.put("starLevelList", sce.getStarLevelList());
                subCommentMap.put("timeStamp", DateTimeUtils.convert(sce.getTimestamp(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                subCommentList.add(subCommentMap);
            }
            rootCommentMap.put("id", rce.getID().toString());
            rootCommentMap.put("advantage", rce.getAdvantage());
            rootCommentMap.put("weakPoint", rce.getWeakPoint());
            rootCommentMap.put("commentUserId", rce.getCommentUserId() == null ? Constant.EMPTY : rce.getCommentUserId().toString());
            rootCommentMap.put("commentUserName", rce.getCommentUserName());
            rootCommentMap.put("replyUserId", rce.getReplyUserId() == null ? Constant.EMPTY : rce.getReplyUserId().toString());
            rootCommentMap.put("replyUserName", rce.getReplyUserName());
            rootCommentMap.put("parentId", rce.getParentId());
            rootCommentMap.put("starLevelList", rce.getStarLevelList());
            rootCommentMap.put("timeStamp", DateTimeUtils.convert(rce.getTimestamp(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
            rootCommentMap.put("headImage", AvatarUtils.getAvatar(userService.searchUserId(rce.getCommentUserId()).getAvatar(), 1));
            rootCommentMap.put("subCommentList", subCommentList);
            list.add(rootCommentMap);
        }
        return list;
    }

    /**
     * 新增复杂评论
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/comment")
    @ResponseBody
    public RespObj comment(@RequestParam(value = "targetId", defaultValue = "") String targetId,
                           @RequestParam(value = "starNums", defaultValue = "") String starNums,
                           @RequestParam(value = "starNames", defaultValue = "") String starNames,
                           @RequestParam(value = "advantage", defaultValue = "") String advantage,
                           @RequestParam(value = "weakPoint", defaultValue = "") String weakPoint,
                           @RequestParam(value = "parentId", defaultValue = "0") String parentId) {
        if (StringUtils.isEmpty(targetId)) {
            return RespObj.FAILD;
        }
        List<StarLevel> starLevelList = new ArrayList<StarLevel>();
        if (!(StringUtils.isEmpty(starNames))) {
            String[] starNameArr = starNames.split(Constant.COMMA);
            String[] starNumArr = starNums.split(Constant.COMMA);
            for (int i = 0; (i < starNameArr.length && i < starNumArr.length); i++) {
                StarLevel sl = new StarLevel(starNameArr[i], starNumArr[i]);
                starLevelList.add(sl);
            }
        }
        UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue().getId()));
        ReviewEntry e = reviewService.getReviewEntry(targetId);
        complexCommentService.addComplexCommentEntry(
                new ComplexCommentEntry(ue.getID(), ue.getUserName(), e.getPublicUserId(),
                        e.getPublishUserName(), System.currentTimeMillis(),
                        new ObjectId(targetId), advantage, weakPoint, starLevelList, parentId));
        List<Map<String, Object>> resultList = findCommentForReview(new ObjectId(targetId));
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage(resultList);
        return respObj;
    }

}
