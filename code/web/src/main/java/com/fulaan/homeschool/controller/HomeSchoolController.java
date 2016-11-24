package com.fulaan.homeschool.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.homeschool.dto.HomeSchoolDTO;
import com.fulaan.homeschool.service.HomeSchoolService;
import com.fulaan.homeschool.service.ThemeService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.mongodb.BasicDBObject;
import com.pojo.app.Platform;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.microblog.ThemeEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.props.Resources;
import com.sys.utils.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wang_xinxin on 15-2-26.
 */
@Controller
@RequestMapping("/homeschool")
public class HomeSchoolController extends BaseController {

    private static final Logger logger = Logger.getLogger(HomeSchoolController.class);

    @Autowired
    private HomeSchoolService homeSchoolService;

    private VideoService videoService = new VideoService();

    @Autowired
    private UserService userService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private SchoolService schoolService;

    @RequestMapping("homepage")
    public String homepage() {
        return "homepage/homepage";
    }

    /**
     * 添加微博
     * @param homeSchoolDTO
     * @param headers
     * @return
     */
    @RequestMapping("publicBlog")
    public @ResponseBody Map<String ,Object> publicBlog(HomeSchoolDTO homeSchoolDTO,@RequestHeader HttpHeaders headers) {
    	Map<String,Object> model = new HashMap<String,Object>();
        //long role = getSessionValue().getUserRole();
        String userid = getSessionValue().getId();
        String schoolid = getSessionValue().getSchoolId();
        UserDetailInfoDTO userdto = userService.getUserInfoById(userid);
        if (userdto.getJinyan()==1) {
            model.put("flag",true);
            userService.updateJinYanTime(userid);
            return model;
        }
        if (StringUtils.isEmpty(homeSchoolDTO.getBlogcontent())
                && homeSchoolDTO.getFilenameAry()!=null && homeSchoolDTO.getFilenameAry().length!=0 && homeSchoolDTO.getVideoAry()!=null && homeSchoolDTO.getVideoAry().length!=0) {
            model.put("resultCode","1");
        }
        homeSchoolDTO.setUserid(userid);
        int blogtype = 1;
        if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            blogtype = 2;
        } else {
            if (UserRole.isK6KT(getSessionValue().getUserRole()) || getSessionValue().getUserName().contains("k6kt小助手")) {
                blogtype =homeSchoolDTO.getBlogtype();
            }
        }
        homeSchoolDTO.setBlogtype(blogtype);
        String client = headers.getFirst("User-Agent");
        Platform pf = null;
        if (client.contains("iOS")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")){
            pf = Platform.Android;
        } else {
            pf = Platform.PC;
        }
        List<ObjectId> classAry = new ArrayList<ObjectId>();
        if (homeSchoolDTO.getSendtype() != 1) {
            if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
                ObjectId uid = getUserId();
                if (UserRole.isParent(getSessionValue().getUserRole())) {
                    UserDetailInfoDTO user = userService.getUserInfoById(getSessionValue().getId());
                    uid = new ObjectId(user.getConnectIds().get(0));
                }
                ClassEntry classety = classService.getClassEntryByStuId(uid, null);
                if (classety!=null) {
                    if (homeSchoolDTO.getSendtype()==2) {
                        List<ClassInfoDTO> classlist = classService.findClassByGradeId(classety.getGradeId().toString());
                        if (classlist!=null && classlist.size()!=0) {
                            for (ClassInfoDTO classinfo : classlist) {
                                classAry.add(new ObjectId(classinfo.getId()));
                            }
                        }
                    }else if (homeSchoolDTO.getSendtype() == 3) {
                        classAry.add(classety.getID());
                    }
                }

            } else {
                List<ClassInfoDTO> clslist = classService.findClassInfoByTeacherId(new ObjectId(getSessionValue().getId()));
                if (clslist!=null && clslist.size()!=0) {
                    if (homeSchoolDTO.getSendtype()==2) {
                        for (ClassInfoDTO classentry : clslist) {
                            List<ClassInfoDTO> classlist = classService.findClassByGradeId(classentry.getGradeId().toString());
                            if (classlist!=null && classlist.size()!=0) {
                                for (ClassInfoDTO classinfo : classlist) {
                                    classAry.add(new ObjectId(classinfo.getId()));
                                }
                            }
                        }
                    }else if (homeSchoolDTO.getSendtype() == 3) {
                            for (ClassInfoDTO classentry : clslist) {
                                classAry.add(new ObjectId(classentry.getId()));
                            }
                    }
                }

            }
        }
//        else {
//            List<ClassInfoDTO> classEntryList = classService.findClassInfoBySchoolId(getSessionValue().getSchoolId());
//            if (classEntryList!=null && classEntryList.size()!=0) {
//                for (ClassInfoDTO classinfo : classEntryList) {
//                    classAry.add(new ObjectId(classinfo.getId()));
//                }
//            }
//        }
        MicroBlogEntry microBlogEntry = homeSchoolDTO.buildMicroBlogEntry(pf.toString(),schoolid,classAry);
        ObjectId microBlogId=homeSchoolService.addMicroBlog(microBlogEntry);
        //todo:经验值
        ExpLogType microblog = null;
        if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            microblog = ExpLogType.MICRO_HOME_BLOG;
        } else {
            microblog=ExpLogType.MICROBLOG;
        }
        int userExp=1;
        if ((homeSchoolDTO.getFilenameAry()!=null && homeSchoolDTO.getFilenameAry().length!=0)||(homeSchoolDTO.getVideoAry()!=null && homeSchoolDTO.getVideoAry().length!=0)) {
            userExp=2;
        }
        int exp = experienceService.updateMicroBlogDaily(getUserId().toString(), microblog, userExp, microBlogId.toString(), pf);
        if (exp>0) {
            model.put("scoreMsg", microblog.getDesc());
            model.put("score", exp);
        }
        model.put("flag",false);
        return model;
    }

    /**
     * 删除微博
     *
     * @return
     */
    @RequestMapping("delteMicroBlog")
    public @ResponseBody Map<String,Object> deleteMicroBlogInfo(@RequestParam("id") String id,String blogid) throws Exception{
    	Map<String,Object>  model = new HashMap<String,Object>();
        String userid = getSessionValue().getId();
        if(null==id || !ObjectId.isValid(id))
        {
            logger.error("the param id is error!the user:"+getSessionValue());
        }
        homeSchoolService.deleteMicroBlogInfo(id,new ObjectId(userid),blogid);
        logger.info("删除微校园微家园id:" + id+ "删除者" + getSessionValue().getId());
        model.put("result", true);
        return model;
    }
    
    
    /**
     * 删除微博
     *
     * @return
     */
    @RequestMapping("/multi/delteMicroBlog")
    @ResponseBody
    @UserRoles({UserRole.K6KT_HELPER,UserRole.ADMIN,UserRole.HEADMASTER})
    public  RespObj deleteMultiMicroBlogInfo(final String ids) throws Exception{
    	
    	logger.info("delete blogs;ids="+ids);
    	logger.info("user infos=;"+getSessionValue().getMap());
    	Thread th =new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					List<ObjectId> delteIdsList=new ArrayList<ObjectId>();
			    	List<ObjectId> idsList =MongoUtils.convert(ids);
			    	delteIdsList.addAll(idsList);
			    	final BasicDBObject fields =new BasicDBObject("cli",1);
			    	while(true)
			    	{
			    		List<MicroBlogEntry> eList=	homeSchoolService.getMicroBlogEntryByIds(idsList, fields);
			    		if(eList.isEmpty())
			    		{
			    			break;
			    		}
			    		idsList =new ArrayList<ObjectId>();
			    		for(MicroBlogEntry e:eList)
			    		{
			    			delteIdsList.addAll(e.getCommentList());
			    			idsList.addAll(e.getCommentList());
			    		}
			    	}
			    	homeSchoolService.deleteMicroBlog(delteIdsList);
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
			}
		});
    	
    	th.start();
    	
    	return RespObj.SUCCESS;
    	
    }
    

    /**
     * 回复评论
     * @return
     */
    @RequestMapping("replyComment")
    public @ResponseBody Map<String,Object> replyComment(HomeSchoolDTO homeSchoolDTO) {
    	Map<String,Object> model = new HashMap<String,Object>();
        String userid = getSessionValue().getId();
        String schoolid = getSessionValue().getSchoolId();
        MicroBlogEntry microBlogEntry = homeSchoolDTO.buildReplyMicroBlogEntry(new ObjectId(userid),schoolid);
        int count = homeSchoolService.replyComment(microBlogEntry,homeSchoolDTO.getId());
        model.put("replyCount",count);
        ExpLogType microblog = null;
        if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            microblog = ExpLogType.MICRO_HOME_BLOG_REVIEW;
        } else {
            microblog=ExpLogType.MICRO_BLOG_REVIEW;
        }
        if (experienceService.updateDaily(getUserId().toString(), microblog, homeSchoolDTO.getId())) {
            model.put("scoreMsg", microblog.getDesc());
            model.put("score", microblog.getExp());
        }
       return model;
    }

    /**
     * 上传微博图片
     * @param request
     * @return
     */
    @RequestMapping(value = "/addBlogPic", produces = "text/html; charset=utf-8")
    public @ResponseBody Map addBlogPic(MultipartRequest request) {
        Map result = new HashMap<String,Object>();
        String filePath = FileUtil.UPLOADBLOGDIR;
        List<String> fileUrls = new ArrayList<String>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String examName =getFileName(file);
                RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
                {
                    throw new FileUploadException();
                }
                fileUrls.add(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,examName));
            }
            result.put("result", true);
            result.put("path", fileUrls);
        }  catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            }
        return result;
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        String orgname = file.getOriginalFilename();

        return new ObjectId().toString()+Constant.POINT+ orgname.substring(orgname.lastIndexOf(".") + 1);
    }
//
//    public static String getOriginalName(MultipartFile file) {
//        String filename = file.getOriginalFilename();
//        if (filename.indexOf("\\") >= 0) {
//            filename = filename.substring(filename.lastIndexOf("\\") + 1);
//        }
//        return filename;
//    }

    /**
     * 微博列表
     * @param hottype
     * @param blogtype
     * @param page
     * @param pageSize
     * @return
     * @throws ResultTooManyException
     */
    @RequestMapping("selFriendBlogInfo")
    @ResponseBody
    public Map<String,Object> selFriendBlogInfo(@RequestParam("hottype") int hottype,@RequestParam("blogtype") int blogtype,@RequestParam(required=false,defaultValue="1") Integer seachtype,String theme,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) throws ResultTooManyException {
        if (blogtype == 1) {
            userService.updSchoolHomeDate(getUserId());
        } else if (blogtype == 2) {
            userService.updFamilyHomeDate(getUserId());
        }
    	Map<String,Object> model = new HashMap<String,Object>();
    	String schoolid = getSessionValue().getSchoolId();
        String userid = getSessionValue().getId();
        if (pageSize == 0) {
            pageSize = 10;
        }
        List<ObjectId> classAry = new ArrayList<ObjectId>();
        List<ObjectId> gradelist = new ArrayList<ObjectId>();
        if (seachtype != 1) {
            if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
                ObjectId uid = getUserId();
                if (UserRole.isParent(getSessionValue().getUserRole())) {
                    UserDetailInfoDTO user = userService.getUserInfoById(getSessionValue().getId());
                    uid = new ObjectId(user.getConnectIds().get(0));
                }
                ClassEntry classety = classService.getClassEntryByStuId(uid, null);
                if (classety != null) {
                    if (seachtype == 2) {
                        gradelist = new ArrayList<ObjectId>();
                        gradelist.add(classety.getGradeId());
                        List<ClassInfoDTO> classlist = classService.findClassByGradeIdList(gradelist);
                        if (classlist != null && classlist.size() != 0) {
                            for (ClassInfoDTO classinfo : classlist) {
                                classAry.add(new ObjectId(classinfo.getId()));
                            }
                        }
                    } else if (seachtype == 3) {
                        classAry.add(classety.getID());
                    }
                }
            } else {
                List<ClassInfoDTO> clslist = classService.findClassInfoByTeacherId(new ObjectId(getSessionValue().getId()));
                if (clslist != null && clslist.size() != 0) {
                    if (seachtype == 2) {
                        for (ClassInfoDTO classentry : clslist) {
                            gradelist = new ArrayList<ObjectId>();
                            gradelist.add(new ObjectId(classentry.getGradeId()));
                        }
                        List<ClassInfoDTO> classlist = classService.findClassByGradeIdList(gradelist);
                        if (classlist != null && classlist.size() != 0) {
                            for (ClassInfoDTO classinfo : classlist) {
                                classAry.add(new ObjectId(classinfo.getId()));
                            }
                        }
                    } else if (seachtype == 3) {
                        for (ClassInfoDTO classentry : clslist) {
                            classAry.add(new ObjectId(classentry.getId()));
                        }
                    }
                }
            }
        }
            int count = homeSchoolService.selFriendBlogCount(new ObjectId(userid), hottype, blogtype,schoolid,seachtype,classAry,theme,seachtype);
            List<MicroBlogEntry> microBlogList = homeSchoolService.selFriendBlogInfo(new ObjectId(userid), hottype, blogtype,schoolid, page, pageSize,seachtype,classAry,theme,seachtype);
            List<HomeSchoolDTO> homeSchoolDTOList = new ArrayList<HomeSchoolDTO>();

            
            List<ObjectId> userIds =MongoUtils.getFieldObjectIDs(microBlogList, "ui");
            Map<ObjectId,UserEntry> userMap=userService.getUserEntryMap2(userIds, new BasicDBObject("nm", 1).append("r", 1).append("avt", 1).append("posdec",1).append("nnm",1));
            //尽量禁止循环操作数据库
            UserEntry user =null;

            for (MicroBlogEntry microblog : microBlogList) {
                HomeSchoolDTO homeSchoolDTO = new HomeSchoolDTO(microblog);
                user=userMap.get(microblog.getUserId());
                if (user != null) {
                    homeSchoolDTO.setUserimage(AvatarUtils.getAvatar(user.getAvatar(), 3));
                    homeSchoolDTO.setUsername(NameShowUtils.showName(user.getUserName()));
                    homeSchoolDTO.setRole(user.getRole());
                    String desc = UserRole.getRoleDescription(user.getRole());
                    if (UserRole.isStudentOrParent(user.getRole())) {
                        desc = UserRole.getRoleDescription(user.getRole());
                    } else {
                        if (!StringUtils.isEmpty(user.getPostionDec())) {
                            desc = user.getPostionDec();
                        }
                    }
                    homeSchoolDTO.setRoleDescription(desc);
                }
                List<ObjectId> zanlist = microblog.getZanList();
                homeSchoolDTO.setIszan(0);
                if (zanlist != null && zanlist.size() != 0) {
                    for (ObjectId id : zanlist) {
                        if (id!=null) {
                            if (getSessionValue().getId().equals(id.toString())) {
                                homeSchoolDTO.setIszan(1);
                            }
                        }
                    }
                }
                homeSchoolDTO.setMreply(microblog.getCommentList().size());
                homeSchoolDTO.setZancount(microblog.getZanCount());
                if (UserRole.isHeadmaster(getSessionValue().getUserRole())|| (getSessionValue().getId().toString().equals(microblog.getUserId().toString()))) {
                    homeSchoolDTO.setIsdelete(1);
                }
                homeSchoolDTO.setCreatetime((DateTimeUtils.convert(microblog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS)));
                homeSchoolDTO.setTimedes(IntervalUtil.getInterval(DateTimeUtils.convert(microblog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H)));
                homeSchoolDTOList.add(homeSchoolDTO);
            }

            model.put("rows",homeSchoolDTOList);
            model.put("total", count);
            model.put("page", page);
            model.put("pageSize", pageSize);
    return model;
    }

    /**
     * 查询单条微博
     * @param blogid
     * @return
     */
    @RequestMapping("selOneBlogInfo")
    @ResponseBody
    public Map<String,Object> selOneBlogInfo(@RequestParam("blogid") String blogid) {
    	Map<String,Object> model = new HashMap<String,Object>();
    	MicroBlogEntry microBlogEntry = homeSchoolService.selOneBlogInfo(blogid);
        HomeSchoolDTO homeSchoolDTO = new HomeSchoolDTO(microBlogEntry);
        Map<Integer, String> pfMap=Platform.getPlatformMap();
        UserEntry user = userService.searchUserId(microBlogEntry.getUserId());
        if (user!=null) {
            homeSchoolDTO.setUserimage(AvatarUtils.getAvatar(user.getAvatar(), 3));
            homeSchoolDTO.setUsername(NameShowUtils.showName(user.getUserName()));
            homeSchoolDTO.setRole(user.getRole());
            homeSchoolDTO.setRoleDescription(UserRole.getRoleDescription(user.getRole()));
        }
        homeSchoolDTO.setMreply(microBlogEntry.getCommentList().size());
        homeSchoolDTO.setZancount(microBlogEntry.getZanCount());
        homeSchoolDTO.setClienttype(pfMap.get(microBlogEntry.getPlatformType()));
        model.put("data",homeSchoolDTO);
        return model;
    }
//    Request URL:http://localhost:8080/homeschool/mobile/selOneBlogInfo?blogid=%5677a6a8132f2628fd283e4f%22


    /**
     *  h5页面查询单条微博
     * @param blogid
     * @return
     */
    @SessionNeedless
    @RequestMapping("moblie/selOneBlogInfo")
     public String selOneBlogInfoApp(@RequestParam("blogid") String blogid,  @RequestParam("userid") String userid,Map<String,Object> model) {
         MicroBlogEntry microBlogEntry = homeSchoolService.selOneBlogInfo(blogid);
        HomeSchoolDTO homeSchoolDTO = new HomeSchoolDTO(microBlogEntry);
        Map<Integer, String> pfMap=Platform.getPlatformMap();
        UserEntry user = userService.searchUserId(microBlogEntry.getUserId());
        if (user!=null) {
            homeSchoolDTO.setUserimage(AvatarUtils.getAvatar(user.getAvatar(), 3));
            homeSchoolDTO.setUsername(NameShowUtils.showName(user.getUserName()));
            homeSchoolDTO.setRole(user.getRole());
            homeSchoolDTO.setRoleDescription(UserRole.getRoleDescription(user.getRole()));
        }

        homeSchoolDTO.setMreply(microBlogEntry.getCommentList().size());
        homeSchoolDTO.setZancount(microBlogEntry.getZanCount());
        homeSchoolDTO.setClienttype(pfMap.get(microBlogEntry.getPlatformType()));
        Date publishtime = homeSchoolDTO.getPublishtime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(publishtime);
        model.put("data",homeSchoolDTO);
        model.put("userid",userid);
        model.put("publictime",dateString);

        return "homeschool/blogshare";

    }


    @RequestMapping("isBlogZan")
    @ResponseBody
    public Map<String,Object> isBlogZan(@RequestParam("blogid") String blogid) {
    	Map<String,Object> model = new HashMap<String,Object>();
        ObjectId userid = getUserId();
        model = homeSchoolService.isBlogZan(blogid,userid);
        ExpLogType microblog = null;
        if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            microblog = ExpLogType.MICRO_HOME_BLOG_REVIEW;
        } else {
            microblog=ExpLogType.MICRO_BLOG_REVIEW;
        }
        if (experienceService.updateDaily(getUserId().toString(), microblog, blogid)) {
            model.put("scoreMsg", microblog.getDesc());
            model.put("score", microblog.getExp());
        }
        return model;
    }

    /**
     * 获取微博回复的评论 type 1 我的评论 2 单条评论
     * @param blogid
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("friendReplyInfo")
    @ResponseBody
    public Map<String,Object> getFriendReplyInfo(String blogid,@RequestParam("type") int type,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        if (page==0) {
            page = 1;
        }
        if (pageSize==0) {
            pageSize = 10;
        }
        Map<String,Object> model = new HashMap<String, Object>();
        int count = homeSchoolService.getFriendReplyCount(blogid,type,getSessionValue().getId());
        List<MicroBlogEntry> retList = homeSchoolService.getFriendReplyInfo(blogid,type,getSessionValue().getId(),page,pageSize);
        List<HomeSchoolDTO> homeSchoolDTOList = new ArrayList<HomeSchoolDTO>();
        Map<Integer, String> pfMap=Platform.getPlatformMap();
        for (MicroBlogEntry microblog : retList) {
            HomeSchoolDTO homeSchoolDTO = new HomeSchoolDTO(microblog);
            UserEntry user = userService.searchUserId(microblog.getUserId());
            if (user!=null) {
                homeSchoolDTO.setUserimage(AvatarUtils.getAvatar(user.getAvatar(), 3));
                homeSchoolDTO.setUsername(NameShowUtils.showName(user.getUserName()));
                homeSchoolDTO.setRole(user.getRole());
                homeSchoolDTO.setRoleDescription(UserRole.getRoleDescription(user.getRole()));
                homeSchoolDTO.setSchoolname(user.getSchoolID()==null?"":schoolService.getSchoolEntry(user.getSchoolID(),null).getName());
            }
            homeSchoolDTO.setMreply(microblog.getCommentList().size());
            homeSchoolDTO.setZancount(microblog.getZanCount());
            List<ObjectId> zanlist = microblog.getZanList();
            homeSchoolDTO.setIszan(0);
            if (UserRole.isHeadmaster(getSessionValue().getUserRole())|| (getSessionValue().getId().toString().equals(microblog.getUserId().toString())) || UserRole.isK6ktHelper(getSessionValue().getUserRole())) {
                homeSchoolDTO.setIsdelete(1);
            }
            homeSchoolDTO.setReplytype(microblog.getType());
            homeSchoolDTO.setCreatetime((DateTimeUtils.convert(microblog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS)));
            homeSchoolDTO.setTimedes(IntervalUtil.getInterval(DateTimeUtils.convert(microblog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H)));
            homeSchoolDTO.setClienttype(pfMap.get(microblog.getPlatformType()));
            MicroBlogEntry entry = null;
            if (microblog.getType()==3) {
                entry = homeSchoolService.selOneBlogInfo(microblog.getAtInfo().getValue().toString());
                homeSchoolDTO.setBusername(userService.searchUserId(microblog.getAtInfo().getId()).getUserName());
            } else {
                entry = homeSchoolService.selOneBlogInfo(microblog.getMainid().toString());
            }
            if (entry!=null) {
                homeSchoolDTO.setReplycontent(entry.getContent());
            }
            homeSchoolDTO.setReplyid(microblog.getMainid().toString());
            if (zanlist!=null && zanlist.size()!=0) {
                for (ObjectId id : zanlist) {
                    if (id!=null) {
                        if (getSessionValue().getId().equals(id.toString())) {
                            homeSchoolDTO.setIszan(1);
                        }
                    }

                }
            }
            homeSchoolDTOList.add(homeSchoolDTO);
        }
        model.put("rows",homeSchoolDTOList);
        model.put("total", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }



    /**
     * h5页面获取微博回复的评论 type 1 我的评论 2 单条评论
     * @param blogid
      * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("moblie/friendReplyInfo")
    @SessionNeedless
    @ResponseBody
    public Map<String,Object> getFriendReplyInfoApp(@RequestParam("blogid") String blogid,@RequestParam("userId") String userid,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        if (page==0) {
            page = 1;
        }
        if (pageSize==0) {
            pageSize = 10;
        }
        Map<String,Object> model = new HashMap<String, Object>();
        int count = homeSchoolService.getFriendReplyCount(blogid,2,userid);
        UserDetailInfoDTO userInfoById = userService.getUserInfoById(userid);

        List<MicroBlogEntry> retList = homeSchoolService.getFriendReplyInfo(blogid,2,userInfoById.getId(),page,pageSize);
        List<HomeSchoolDTO> homeSchoolDTOList = new ArrayList<HomeSchoolDTO>();
        Map<Integer, String> pfMap=Platform.getPlatformMap();
        for (MicroBlogEntry microblog : retList) {
            HomeSchoolDTO homeSchoolDTO = new HomeSchoolDTO(microblog);
            UserEntry user = userService.searchUserId(microblog.getUserId());
            if (user!=null) {
                homeSchoolDTO.setUserimage(AvatarUtils.getAvatar(user.getAvatar(), 3));
                homeSchoolDTO.setUsername(NameShowUtils.showName(user.getUserName()));
                homeSchoolDTO.setRole(user.getRole());
                homeSchoolDTO.setRoleDescription(UserRole.getRoleDescription(user.getRole()));
            }
            homeSchoolDTO.setMreply(microblog.getCommentList().size());
            homeSchoolDTO.setZancount(microblog.getZanCount());
            List<ObjectId> zanlist = microblog.getZanList();
            homeSchoolDTO.setIszan(0);
            if (UserRole.isHeadmaster(userInfoById.getRole())|| userid.equals(microblog.getUserId().toString()) || UserRole.isK6ktHelper(userInfoById.getRole())) {
                homeSchoolDTO.setIsdelete(1);
            }
            homeSchoolDTO.setReplytype(microblog.getType());
            homeSchoolDTO.setCreatetime((DateTimeUtils.convert(microblog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS)));
            homeSchoolDTO.setTimedes(IntervalUtil.getInterval(DateTimeUtils.convert(microblog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H)));
            homeSchoolDTO.setClienttype(pfMap.get(microblog.getPlatformType()));
            MicroBlogEntry entry = null;
            if (microblog.getType()==3) {
                entry = homeSchoolService.selOneBlogInfo(microblog.getAtInfo().getValue().toString());
                homeSchoolDTO.setBusername(userService.searchUserId(microblog.getAtInfo().getId()).getUserName());
            } else {
                entry = homeSchoolService.selOneBlogInfo(microblog.getMainid().toString());
            }
            if (entry!=null) {
                homeSchoolDTO.setReplycontent(entry.getContent());
            }
            homeSchoolDTO.setReplyid(microblog.getMainid().toString());
            if (zanlist!=null && zanlist.size()!=0) {
                for (ObjectId id : zanlist) {
                    if (id!=null) {
                        if (userid.equals(id.toString())) {
                            homeSchoolDTO.setIszan(1);
                        }
                    }

                }
            }
            homeSchoolDTOList.add(homeSchoolDTO);
        }
        model.put("rows",homeSchoolDTOList);
        model.put("total", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }


    @RequestMapping("/currentUser")
    public @ResponseBody Map<String,Object> getCurrentUserInfo() {
        Map<String,Object> model = new HashMap<String, Object>();
        model.put("id",getSessionValue().getId());
        return model;
    }

    /**
     *
     * @return
     */
    @RequestMapping("getNoticeCount")
    public @ResponseBody Map<String,Object> getNoticeCount() {
        Map<String,Object> model = new HashMap<String, Object>();
        int count = homeSchoolService.getNoticeCount(getSessionValue().getId(),getSessionValue().getSchoolId());
        if (count!=0) {
            model.put("blogType", 1);
            model.put("blogCount", count);
        } else {
            model.put("blogType", 0);
        }
        return model;
    }

    /**
     *
     * @return
     */
    @RequestMapping("getMicoblog")
    public @ResponseBody Map<String,Object> getMicroblogCount() {
        Map map = new HashMap();
        map = homeSchoolService.getMicroblogCount(getSessionValue().getId(),getSessionValue().getSchoolId());
        return map;
    }

    /**
     * 获取话题列表
     * @return
     */
    @RequestMapping("getThemes")
    public @ResponseBody Map<String,Object> getThemeList() {
        Map map = new HashMap();
        map.put("rows",themeService.getThemeList(getSessionValue().getSchoolId()));
        return map;
    }

    /**
     * 上传视频
     * @param
     * @return
     * @throws IllegalParamException
     * @throws IOException
     * @throws IllegalStateException
     * @throws EncoderException
     */
    @RequestMapping("/video/upload")
    @ResponseBody
    @SessionNeedless
    public Map<String,Object> uploadVideo(MultipartFile Filedata) throws IllegalParamException, IllegalStateException, IOException, EncoderException
    {
        Map map = new HashMap();
        String fileName= FilenameUtils.getName(Filedata.getOriginalFilename());
        if(!ValidationUtils.isRequestVideoName(fileName))
        {
//            RespObj obj =new RespObj(Constant.FAILD_CODE, "视频名字非法");
            map.put("flg",false);
            map.put("mesg","视频名字非法");
            return map;
        }

        //视频filekey
        String videoFilekey =new ObjectId().toString()+Constant.POINT+FilenameUtils.getExtension(fileName);
        String bathPath= Resources.getProperty("upload.file");
        File dir =new File(bathPath);
        if(!dir.exists())
        {
            dir.mkdir();
        }

        File savedFile = new File(bathPath, videoFilekey);
        OutputStream stream =new FileOutputStream(savedFile);
        stream.write(Filedata.getBytes());
        stream.flush();
        stream.close();



        String coverImage = new ObjectId().toString() + ".jpg";
        Encoder encoder = new Encoder();
        File screenShotFile = new File(bathPath, coverImage);
        long videoLength = 60000;//缺省一分钟
        //是否生成了图片
        boolean isCreateImage=false;
        try
        {
            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            videoLength = encoder.getInfo(savedFile).getDuration();
            isCreateImage=true;
        }catch(Exception ex)
        {
            logger.error("", ex);
        }
        if(videoLength==-1){
            videoLength = 60000;//获取不到时间就设为1分钟
        }

        logger.debug("begin upload video iamge");

        //String imageFilePath = null;
        //上传图片
        if(isCreateImage&&screenShotFile.exists())
        {
            RespObj obj=QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);
            if(!obj.getCode().equals(Constant.SUCCESS_CODE))
            {
                QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_VIDEO, videoFilekey);
//                obj =new RespObj(Constant.FAILD_CODE, "视频图片上传失败");
                map.put("flg",false);
                map.put("mesg","视频图片上传失败");
                return map;
            }
        }
        VideoEntry ve =new VideoEntry(fileName, videoLength, VideoSourceType.USER_VIDEO.getType(),videoFilekey);
        ve.setID(new ObjectId());
        logger.debug("begin upload video");

        QiniuFileUtils.uploadVideoFile(ve.getID(),videoFilekey, Filedata.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
        if(isCreateImage&&screenShotFile.exists())
        {
            ve.setImgUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,coverImage));
        }
        ObjectId videoId=videoService.addVideoEntry(ve);
        //删除临时文件
        try
        {
            savedFile.delete();
            screenShotFile.delete();
        }catch(Exception ex)
        {
            logger.error("", ex);
        }
        map.put("flg",true);
        map.put("vimage",QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage));
        map.put("vid",videoId.toString());
        map.put("vurl",QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()));
        return map;
    }

    /**
     * 获取话题列表
     * @return
     */
    @RequestMapping("addTheme")
    public @ResponseBody Map<String,Object> addTheme(String theme) {
        Map map = new HashMap();
        ThemeEntry themeEntry = new ThemeEntry("#"+theme+"#");
        themeService.addTheme(themeEntry);
        map.put("rows",true);
        return map;
    }

    /**
     * 获取话题列表
     * @return
     */
    @RequestMapping("deleteTheme")
    public @ResponseBody Map<String,Object> removeTheme(String id) {
        Map map = new HashMap();
        themeService.deleteTheme(new ObjectId(id));
        map.put("rows",true);
        return map;
    }

    /**
     * k6kt小助手导出微校园
     * @param response
     * @return
     */
    @RequestMapping("/exportExcel")
    @ResponseBody
    public boolean exportTeacherBlogExcel(HttpServletResponse response) {
        homeSchoolService.getTeacherBlogList(response);
        return true;

    }

    @RequestMapping("upzhuti")
    @ResponseBody
    public Map<String,Object> updateZhuTi() {
        Map map = new HashMap();
        try {
            homeSchoolService.updateZhuTi();
            map.put("flg",true);
        } catch (Exception e) {
            map.put("flg",false);
        }
        return map;
    }

}
