package com.fulaan.project.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.comment.service.CommentService;
import com.fulaan.project.service.ProjectService;
import com.fulaan.project.service.SubProjectService;
import com.fulaan.resources.service.CloudResourceService;
import com.fulaan.user.service.UserService;
import com.pojo.comment.CommentEntry;
import com.pojo.project.SubProjectEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.user.UserEntry;
import com.sys.utils.AvatarUtils;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/subProject")
public class SubProjectController extends BaseController{
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SubProjectService subProjectService;
	@Autowired
	UserService userService;
	@Autowired
	private CloudResourceService resourceService;
	@Autowired
	private CommentService commentService;
	
	private SimpleDateFormat publishTimeSDF = new SimpleDateFormat("yyyy-MM-dd");
	

	/**
	 * 新增页面跳转
	 */ 
	@SessionNeedless
	@RequestMapping("/addPage")
	@ResponseBody
	public ModelAndView addPage(@RequestParam(value="parentId", defaultValue = "")String parentId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("parentId",parentId);
		mv.setViewName("project/subProjectAdd");
		return mv;
	}
	
	/**
	 * 详情页面跳转
	 */ 
	@SessionNeedless
	@RequestMapping("/detailPage")
	@ResponseBody
	public ModelAndView listPage(@RequestParam(value="id", defaultValue = "")String id) {
		ModelAndView mv = new ModelAndView();
		
		SubProjectEntry e = subProjectService.getSubProjectEntryById(new ObjectId(id));
		
		List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
		for(ObjectId fileId : e.getCoursewareList()){
			ResourceEntry re = resourceService.getResourceEntryById(fileId);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("fileName", re.getName());
			m.put("id", re.getID().toString());
			m.put("type", re.getType());
			fileList.add(m);
		}
		List<Map<String,Object>> commentList = uploadCommentList(id);
		
		
		mv.addObject("coverImage","".equals(e.getProjectCover())?"":resourceService.getResourceEntryById(new ObjectId(e.getProjectCover())).getImgUrl());
		mv.addObject("title",e.getSubProjectName());
		mv.addObject("content",e.getSubProjectContent());
		mv.addObject("userName",e.getPublishName());
		mv.addObject("fileList", JSON.toJSONString(fileList));
		mv.addObject("commentList", JSON.toJSONString(commentList));
		mv.addObject("publishTime", publishTimeSDF.format(new Date(e.getPublishTime())));
		mv.addObject("id",id);
		mv.setViewName("project/subProjectDetail");
		return mv;
	}
	
	private List<Map<String,Object>> uploadCommentList(String targetId){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<CommentEntry> rootCommentList = commentService.queryCommentsByTargetId(new ObjectId(targetId));
		for(CommentEntry rce : rootCommentList){
			Map<String,Object> rootCommentMap = new HashMap<String,Object>();
			List<Map<String,Object>> subCommentList = new ArrayList<Map<String,Object>>();
			List<CommentEntry> subCommentEntryList = commentService.queryCommentsByParentId(rce.getID().toString());
			for(CommentEntry sce : subCommentEntryList){
				Map<String,Object> subCommentMap = new HashMap<String,Object>();
				subCommentMap.put("id", sce.getID().toString());
				subCommentMap.put("content", sce.getCommentContent());
				subCommentMap.put("commentUserId", sce.getCommentUserId()==null?"":sce.getCommentUserId().toString());
				subCommentMap.put("commentUserName", sce.getCommentUserName());
				subCommentMap.put("replyUserId", sce.getReplyUserId()==null?"":sce.getReplyUserId().toString());
				subCommentMap.put("replyUserName", sce.getReplyUserName());
				subCommentMap.put("parentId", sce.getParentId());
				subCommentMap.put("timeStamp", publishTimeSDF.format(new Date(sce.getTimestamp())));
				subCommentList.add(subCommentMap);
			}
			rootCommentMap.put("id", rce.getID().toString());
			rootCommentMap.put("content", rce.getCommentContent());
			rootCommentMap.put("commentUserId", rce.getCommentUserId()==null?"":rce.getCommentUserId().toString());
			rootCommentMap.put("commentUserName", rce.getCommentUserName());
			rootCommentMap.put("replyUserId", rce.getReplyUserId()==null?"":rce.getReplyUserId().toString());
			rootCommentMap.put("replyUserName", rce.getReplyUserName());
			rootCommentMap.put("parentId", rce.getParentId());
			rootCommentMap.put("timeStamp", publishTimeSDF.format(new Date(rce.getTimestamp())));
			rootCommentMap.put("headImage", AvatarUtils.getAvatar(userService.searchUserId(rce.getCommentUserId()).getAvatar(),1));
			
			rootCommentMap.put("subCommentList", subCommentList);
			list.add(rootCommentMap);
		}
		return list;
	}
	
	/**
	 * 新增一个集体备课信息
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addSubProject")
	@ResponseBody
	public String addSubProject(@RequestParam(value="projectName", defaultValue = "")String projectName,
								 @RequestParam(value="content", defaultValue = "")String content,
								 @RequestParam(value="parentId", defaultValue = "")String parentId,
								 @RequestParam(value="cover", defaultValue = "")String cover,
								 @RequestParam(value="fileListStr", defaultValue = "")String fileListStr){
		if("".equals(parentId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ObjectId userId = new ObjectId(getSessionValue().getId());
		UserEntry ue = userService.searchUserId(userId);
		String userName = ue.getUserName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//上传的附件列表
		List<ObjectId> fileObjectIdList = new ArrayList<ObjectId>();
		//TODO 这里将所有上传的附件ObjectId放入List中
		if(!("".equals(fileListStr))){
			for(String fileId : fileListStr.split(",")){
				fileObjectIdList.add(new ObjectId(fileId));
			}
		}
		subProjectService.addSubProjectEntry(new SubProjectEntry(userId, userName, projectName, System.currentTimeMillis(), 
					content, fileObjectIdList,  new ObjectId(parentId),cover));
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 新增评论
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addComment")
	@ResponseBody
	public String addComment(@RequestParam(value="targetId", defaultValue = "")String targetId,
						    		@RequestParam(value = "content" , defaultValue="")String content,
						    		@RequestParam(value = "parentId" , defaultValue="0")String parentId){
		
		if("".equals(targetId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		
		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue().getId()));
		SubProjectEntry e = subProjectService.getSubProjectEntryById(new ObjectId(targetId));
		if("0".equals(parentId)){
			commentService.addCommentEntry(new CommentEntry(ue.getID(), ue.getUserName(), e.getPublishId(), e.getPublishName(),
					System.currentTimeMillis(), new ObjectId(targetId), content, parentId));
		}else{
			CommentEntry parentCe = commentService.getCommentEntry(new ObjectId(parentId));
			commentService.addCommentEntry(new CommentEntry(ue.getID(), ue.getUserName(), parentCe.getCommentUserId(), parentCe.getCommentUserName(),
					System.currentTimeMillis(), new ObjectId(targetId), content, parentId));
		}
		
		
		List<Map<String,Object>> resultList = uploadCommentList(targetId);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("datas",resultList);
		return JSON.toJSONString(resultMap);
	}
}
