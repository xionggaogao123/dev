package com.fulaan.yunying.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.school.SchoolDao;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.department.service.DepartmentService;
import com.fulaan.elect.dto.ElectDTO;
import com.fulaan.elect.service.ElectService;
import com.fulaan.experience.dto.ExperienceLogDTO;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.homeschool.dto.HomeSchoolDTO;
import com.fulaan.homeschool.service.HomeSchoolService;
import com.fulaan.homeschool.service.ThemeService;
import com.fulaan.letter.service.LetterService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.questionnaire.dto.QuestionnaireDTO;
import com.fulaan.questionnaire.service.QuestionnaireService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.ImportExcelUtil;
import com.fulaan.video.service.VideoService;
import com.fulaan.yunying.service.YunYingService;
import com.mongodb.BasicDBObject;
import com.pojo.app.Platform;
import com.pojo.app.SessionValue;
import com.pojo.app.SimpleDTO;
import com.pojo.elect.Candidate;
import com.pojo.elect.ElectEntry;
import com.pojo.emarket.UserBalanceDTO;
import com.pojo.letter.*;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.pojo.salary.SalaryDto;
import com.pojo.school.*;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sql.dao.UserBalanceDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by wang_xinxin on 2016/1/5.
 */

@Controller
@RequestMapping("/yunying")
public class YunYingController extends BaseController {

    private static final Logger logger = Logger.getLogger(YunYingController.class);

    private LetterService letterService = new LetterService();
    @Autowired
    private HomeSchoolService homeSchoolService;

    private DepartmentService departmentService = new DepartmentService();
    @Autowired
    private YunYingService yunYingService;

    private VideoService videoService = new VideoService();

    @Autowired
    private UserService userService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ThemeService themeService;

    private UserBalanceDao userBalanceDao = new UserBalanceDao();
    private SchoolDao schoolDao = new SchoolDao();

    private QuestionnaireService questionnaireService = new QuestionnaireService();

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ElectService electService;

    @RequestMapping("/yunying")
    public String yunying(Map<String, Object> model){
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            List<UserDetailInfoDTO> userEntryList = userService.getK6ktEntryByRoles2();
            model.put("schools",userEntryList);
            url="/yunying/yunying";
        }

        return url;
    }

    @RequestMapping("/replyMessage")
    public String replyLetterPage(String replyId,String userid,Model model){
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            model.addAttribute("replyId", replyId);
            model.addAttribute("userid", userid);
            url="/yunying/replyletter";
        }
        return url;
    }

    @RequestMapping("/teacherBlance")
    public String teacherBlance() {
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            url="/yunying/teacherBlance";
        }
        return url;
    }
    @RequestMapping("/theme")
    public String theme() {
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            url="/yunying/theme";
        }
        return url;
    }

    @RequestMapping("/selTheme")
    @ResponseBody
    public Map selTheme() {
        Map map = new HashMap();
        map.put("rows",themeService.getThemeList2());
        return map;
    }

    @RequestMapping("/letter")
    public String letter(Map<String, Object> model) {
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            List<UserDetailInfoDTO> userEntryList = userService.getK6ktEntryByRoles2();
            model.put("user", userEntryList);
            url="/yunying/letter";
        }

        return url;
    }
    @RequestMapping("/withdrawcash")
    public String mangeNews(Integer page, Model model) {
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            url="/yunying/withdrawcash";
        }
        return url;
    }

    @RequestMapping("/votePage")
    public String votePage(Model model)
    {
        int role=getSessionValue().getUserRole();
        List<UserDetailInfoDTO> userEntryList = userService.getK6ktEntryByRoles2();
        model.addAttribute("schools", userEntryList);
        model.addAttribute("role",role);
        model.addAttribute("userId",getUserId().toString());
        return "yunying/vote";
    }

    /** 添加一个选举
     * @param electDTO 传入参数，选举基本信息
     * @param candidateArr 传入参数，表示指定候选人列表
     * @return 包含选举信息和经验值信息的数据
     */
    //todo:review and test!
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/addElect")
    public @ResponseBody Map<String,Object> addElect(ElectDTO electDTO ,String[] candidateArr){
        Map<String,Object> model = new HashMap<String,Object>();


        //todo: use session value
        SessionValue sessionValue = getSessionValue();

        electDTO.setSchoolId(sessionValue.getSchoolId());
        electDTO.setPublisher(sessionValue.getId());

        ElectEntry electEntry = electDTO.buildElectEntry();

        //指定候选人
        List<Candidate> candidates = null;
        if(candidateArr!=null ){
            //
            candidates = new ArrayList<Candidate>();

            for(int i=0;i<candidateArr.length;i++){

                //todo: get user name from service
                //String candidateId = "54f0328afe5b0f608a43329a";
                UserDetailInfoDTO userInfo = userService.getUserInfoById(candidateArr[i]);
                candidates.add(new Candidate(
                                new ObjectId(candidateArr[i]),
                                userInfo.getUserName(),
                                null,//manifest
                                null,//voice
                                null,//pic
                                null,//video
                                System.currentTimeMillis(),//signdate
                                null
                        )
                );
            }
        }

        electEntry.setCandidateList(candidates);

        ObjectId electId=electService.addElect(electEntry);


        electDTO = new ElectDTO(electEntry);
        model.put("elect",electDTO);

        //todo:经验值
        ExpLogType publishElect = ExpLogType.PUBLISH_ELECT;
        if (experienceService.updateScore(getUserId().toString(), publishElect, electId.toString())) {
            model.put("score", publishElect.getExp());
            model.put("scoreMsg", publishElect.getDesc());
        }

        return model;
    }

    //public static final String 	CLOUD_RESOURCE_QINIU_URL= Resources.getProperty("cloud.bucket.url", "http://7xj25c.com1.z0.glb.clouddn.com/");
    /** 列出当前用户可见的所有选举活动，本校的，该用户所在班级含在目标班级的选举。
     *  排序按照先是未截止，再是截止的。然后按照发布时间的倒序排列。
     * @param page 传入的分页信息，第几页，从0开始。缺省0.
     * @param size 传入的分页信息，每页多少个，缺省5.
     * @return 包含选举列表和分页信息的数据
     * @throws Exception
     */
    //todo:review and test!
    @RequestMapping("/elects")
    @ResponseBody
    public Map<String ,Object> listElects( Integer page ,Integer size) throws Exception
    {
        Map<String,Object> model = new HashMap<String,Object>();
        if(size ==null) {
            size = 5;
        }
        if(page ==null) {
            page = 0;
        }

        SessionValue sessionValue = getSessionValue();
        String schoolId = sessionValue.getSchoolId();
        List<ObjectId> classIds = new ArrayList<ObjectId>();

//        List<ClassInfoDTO> classInfoDTOList=userService.getClassDTOList(new ObjectId(sessionValue.getId()),
//                sessionValue.getUserRole());
//        for(ClassInfoDTO classInfoDTO:classInfoDTOList){
//            classIds.add(new ObjectId(classInfoDTO.getId()));
//        }

        List<ElectEntry> electEntries = yunYingService.findElectsBySchoolId(schoolId,classIds,getUserId(),page,size);
        List<ElectDTO> electDTOList = new ArrayList<ElectDTO>();


        for (ElectEntry electEntry : electEntries) {

            try
            {
                ElectDTO electDTO = new ElectDTO(electEntry);
                electDTOList.add(electDTO);
            }catch(Exception ex)
            {
                logger.error("投票选举", ex);
            }

        }

        model.put("content",electDTOList);

        int electCount = yunYingService.getElectCountBySchoolId(schoolId, classIds);


        //产生分页信息
        int totalPages = 0;
        if(electCount > 0)
        {
            totalPages = (electCount-1)/size+1;
        }
        boolean first = false;
        if(page==0)
        {
            first = true;
        }
        boolean last = false;
        if(page == totalPages-1)
        {
            last = true;
        }
        model.put("first",first);
        model.put("totalElements",electCount);
        model.put("last",last);
        model.put("numberOfElements",electEntries.size());
        model.put("totalPages",totalPages);
        model.put("number",page);
        model.put("sort",null);
        model.put("size",size);//todo: 根据页面调试


        return model;
    }

    @RequestMapping("/quesionload")
    public String index(Model model){
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            model.addAttribute("role", 1);
            List<SchoolEntry> schoolEntryList = schoolService.getSchoolEntryListByRegionForEdu();
            List<SchoolDTO> schoolDTOList = new ArrayList<SchoolDTO>();
            if (schoolEntryList!=null && schoolEntryList.size()!=0) {
                for (SchoolEntry schoolEntry : schoolEntryList) {
                    schoolDTOList.add(new SchoolDTO(schoolEntry));
                }
            }
            model.addAttribute("schools", schoolDTOList);
            model.addAttribute("userId", getSessionValue().getId());
            url="/yunying/questionlist";
        }

        return url;
    }

    @RequestMapping("/mainGoodsPage")
    public String mainGoodsPage(Map<String, Object> model){
        String url="";
        int userRole=getSessionValue().getUserRole();
        if(UserRole.isSysManager(userRole)) {
            List<SimpleDTO> list =new ArrayList<SimpleDTO>();
            list.add(SchoolType.PRIMARY.toSimpleDTO());
            list.add(SchoolType.JUNIOR.toSimpleDTO());
            list.add(SchoolType.SENIOR.toSimpleDTO());
            model.put("styStionList", list);
            url="/yunying/emarket";
        }

        return url;
    }
    /**
     * 微博列表
     * @param hottype
     * @param blogtype
     * @param page
     * @param pageSize
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     */
    @RequestMapping("selBlogInfo")
    @ResponseBody
    public Map<String,Object> selBlogInfo(@RequestParam("hottype") int hottype,String schoolid,String startDate,String endDate,Integer formtype,String keyword,@RequestParam("blogtype") int blogtype,@RequestParam(required=false,defaultValue="1") Integer seachtype,String theme,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,String choose1,String choose2,int btype) throws ResultTooManyException {
        Map<String,Object> model = new HashMap<String,Object>();
        String userid = getSessionValue().getId();
        if (pageSize == 0) {
            pageSize = 10;
        }
        int count = yunYingService.selBlogCount(new ObjectId(userid), hottype, blogtype, schoolid, theme, seachtype, startDate, endDate, formtype, keyword, choose1, choose2, btype);
        List<MicroBlogEntry> microBlogList = yunYingService.selBlogInfo(new ObjectId(userid), hottype, blogtype, schoolid, page, pageSize, seachtype, theme, seachtype,startDate,endDate,formtype,keyword,choose1,choose2,btype);
        List<HomeSchoolDTO> homeSchoolDTOList = new ArrayList<HomeSchoolDTO>();


        List<ObjectId> userIds = MongoUtils.getFieldObjectIDs(microBlogList, "ui");
        List<ObjectId> schoolIds = MongoUtils.getFieldObjectIDs(microBlogList, "si");
        Map<ObjectId,UserEntry> userMap=userService.getUserEntryMap2(userIds, new BasicDBObject("nm", 1).append("r", 1).append("avt", 1).append("nnm",1));
        Map<ObjectId, SchoolEntry> schollMap =schoolDao.getSchoolMap(schoolIds, Constant.FIELDS);
        //尽量禁止循环操作数据库
        UserEntry user =null;

        for (MicroBlogEntry microblog : microBlogList) {
            HomeSchoolDTO homeSchoolDTO = new HomeSchoolDTO(microblog);
            user=userMap.get(microblog.getUserId());
            if (user != null) {
                homeSchoolDTO.setUserimage(AvatarUtils.getAvatar(user.getAvatar(), 3));
                homeSchoolDTO.setUsername(NameShowUtils.showName(user.getUserName()));
                homeSchoolDTO.setRole(user.getRole());
                homeSchoolDTO.setSchoolname(microblog.getSchoolID() == null ? "" : schollMap.get(microblog.getSchoolID()).getName());
                homeSchoolDTO.setRoleDescription(UserRole.getRoleDescription(user.getRole()));
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
     * 提现记录
     * @return
     */
    @RequestMapping("selTeacherWithdraw")
    @ResponseBody
    public Map<String,Object> selTeacherWithdraw(String tname,String begintime,String endtime,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        Map<String,Object> model = new HashMap<String,Object>();
        List<String> userids =new ArrayList<String>();
        if (!StringUtils.isEmpty(tname)) {
            UserEntry user = userService.searchUserByUserName(tname);
            userids.add(user.getID().toString());
        }
        int count = yunYingService.withDrawEntryCount(userids,!StringUtils.isEmpty(begintime)?new ObjectId(DateTimeUtils.stringToDate(begintime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A)):null,!StringUtils.isEmpty(endtime)?new ObjectId(DateTimeUtils.stringToDate(endtime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A)):null);
        model.put("rows", yunYingService.withDrawEntryList(userids,!StringUtils.isEmpty(begintime)?new ObjectId(DateTimeUtils.stringToDate(begintime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A)):null,!StringUtils.isEmpty(endtime)?new ObjectId(DateTimeUtils.stringToDate(endtime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A)):null,page,pageSize));
        model.put("total", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }

    /**
     * 查询用户
     * @param username
     * @return
     */
    @RequestMapping("selUserInfo")
    @ResponseBody
    public Map<String,Object> selUserInfo(String schoolId,String username,int jinyan,int page,int pageSize) {
        Map<String,Object> model = new HashMap<String,Object>();
        List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        int count = userService.searchUserCount(schoolId,username,jinyan);
        List<UserEntry> userEntryList = userService.searchUser(schoolId,username,jinyan,page,pageSize);
        UserDetailInfoDTO userDetailInfoDTO = null;
        try {
            if (userEntryList!=null && userEntryList.size()!=0) {
                List<String> userids = new ArrayList<String>();
                Set<ObjectId> schollSet =new HashSet<ObjectId>();
                for (UserEntry userEntry : userEntryList) {
                    userids.add(userEntry.getID().toString());
                    schollSet.add(userEntry.getSchoolID());
                }
                UserBalanceDTO dto = new UserBalanceDTO();
                dto.setUserids(userids);
                Map<String, UserBalanceDTO> userBalanceInfos = userBalanceDao.getUserBalanceInfos(dto);
                Map<ObjectId, SchoolEntry> schollMap =schoolDao.getSchoolMap(schollSet, Constant.FIELDS);
                for (UserEntry user : userEntryList) {
                    userDetailInfoDTO = new UserDetailInfoDTO();
                    userDetailInfoDTO.setBalance(userBalanceInfos.get(user.getID().toString())!=null?userBalanceInfos.get(user.getID().toString()).getBalance():0);
                    userDetailInfoDTO.setMainClassName("");
                    if (UserRole.isStudent(user.getRole())) {
                        userDetailInfoDTO.setMainClassName(classService.findMainClassNameByUserId(user.getID().toString())==null?"":classService.findMainClassNameByUserId(user.getID().toString()));
                    }

                    userDetailInfoDTO.setId(user.getID().toString());
                    userDetailInfoDTO.setExperienceValue(user.getExperiencevalue());
                    userDetailInfoDTO.setUserName(NameShowUtils.showName(user.getUserName()));
                    userDetailInfoDTO.setMobileNumber(user.getMobileNumber());
                    userDetailInfoDTO.setEmail(user.getEmail());
                    userDetailInfoDTO.setSchoolName(schollMap.get(user.getSchoolID())!=null?schollMap.get(user.getSchoolID()).getName():"");
                    userDetailInfoDTO.setJinyan(user.getJinyan());
                    userDetailInfoDTOList.add(userDetailInfoDTO);
                }
            }
        }catch (Exception e) {
           e.printStackTrace();
        }

        model.put("rows",userDetailInfoDTOList);
        model.put("total", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }

    /**
     * 修改经验值
     * @return
     */
    @RequestMapping("modifyExpValue")
    @ResponseBody
    public Map<String,Object> modifyExpValue(String userid,int exp,String content) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            yunYingService.updateExperenceValue(new ObjectId(userid),exp,content);
            ExperienceLogDTO experienceLog = new ExperienceLogDTO();
            experienceLog.setExperience(exp);
            experienceLog.setExperiencename(content);
            experienceLog.setCreatetime(new Date());
            experienceService.addUserExperience(userid,experienceLog);
            model.put("flg",true);
        } catch (Exception e) {
            model.put("flg",false);
        }
        return model;
    }

    /**
     * 修改余额
     * @return
     */
    @RequestMapping("modifyBalance")
    @ResponseBody
    public Map<String,Object> modifyBalance(String userid,int exp,String content) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            yunYingService.modifyBalance(userid, exp,content);
            model.put("flg",true);
        } catch (Exception e) {
            model.put("flg",false);
        }
        return model;
    }

    /**
     * 删除微博
     *
     * @return
     */
    @RequestMapping("delteAllMicroBlog")
    public @ResponseBody Map<String,Object> delteAllMicroBlog(@RequestParam("ids") String ids) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        List<String> list=new ArrayList<String>();
        String[] objArr=ids.split(",");
        for(int i=0;i<objArr.length;i++)
        {
            list.add(objArr[i]);
        }
        yunYingService.delteAllMicroBlog(list);
        model.put("result", true);
        return model;
    }

    /**
     * 取消主题帖
     *
     * @return
     */
    @RequestMapping("canceltop")
    public @ResponseBody Map<String,Object> canceltop(@RequestParam("ids") String ids) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        List<ObjectId> list=new ArrayList<ObjectId>();
        String[] objArr=ids.split(",");
        for(int i=0;i<objArr.length;i++)
        {
            list.add(new ObjectId(objArr[i]));
        }
        yunYingService.canceltop(list);
        model.put("result", true);
        return model;
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
//        String schoolid = getSessionValue().getSchoolId();
        if (StringUtils.isEmpty(homeSchoolDTO.getBlogcontent())
                && homeSchoolDTO.getFilenameAry()!=null && homeSchoolDTO.getFilenameAry().length!=0 && homeSchoolDTO.getVideoAry()!=null && homeSchoolDTO.getVideoAry().length!=0) {
            model.put("resultCode","1");
        }
        homeSchoolDTO.setUserid(userid);
//        int blogtype = 1;
//        if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
//            blogtype = 2;
//        } else {
//            if (UserRole.isK6KT(getSessionValue().getUserRole()) || getSessionValue().getUserName().contains("k6kt小助手")) {
        int blogtype =homeSchoolDTO.getBlogtype();
//            }
//        }
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
                ClassEntry classety = classService.getClassEntryByStuId(new ObjectId(getSessionValue().getId()), null);
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
//        if (StringUtils.isEmpty(homeSchoolDTO.getSchoolid())) {
//            List<SchoolEntry> schoolEntryList = schoolService.getSchoolEntryListByRegionForEdu();
//            for (SchoolEntry schoolEntry : schoolEntryList) {
//                MicroBlogEntry microBlogEntry = homeSchoolDTO.buildMicroBlogEntry(pf.toString(),schoolEntry.getID(),classAry);
//                homeSchoolService.addMicroBlog(microBlogEntry);
//            }
//        } else {
            MicroBlogEntry microBlogEntry = homeSchoolDTO.buildMicroBlogEntry(pf.toString(),homeSchoolDTO.getSchoolid(),classAry);
            homeSchoolService.addMicroBlog(microBlogEntry);
//        }

//        ObjectId microBlogId=homeSchoolService.addMicroBlog(microBlogEntry);
        //todo:经验值
//        ExpLogType microblog = ExpLogType.MICROBLOG;
//        int exp = experienceService.updateMicroBlogDaily(getUserId().toString(), microblog, microBlogId.toString(), pf);
//        if (exp>0) {
//            model.put("scoreMsg", microblog.getDesc());
//            model.put("score", exp);
//        }
        return model;
    }


    /**
     * k6kt小助手导出微校园
     * @param response
     * @return
     */
    @RequestMapping("/exportExcel")
    @ResponseBody
    public boolean exportTeacherBlogExcel(@RequestParam("hottype") int hottype,String schoolid,String startDate,String endDate,Integer formtype,String keyword,@RequestParam("blogtype") int blogtype,@RequestParam(required=false,defaultValue="1") Integer seachtype,String theme,HttpServletResponse response,String choose1,String choose2,int btype) throws ResultTooManyException {
        String userid = getSessionValue().getId();
        yunYingService.exportTeacherBlogExcel(new ObjectId(userid), hottype, blogtype, schoolid, theme, seachtype,startDate,endDate,formtype,keyword,response,choose1,choose2,btype);
        return true;

    }

    /**
     * k6kt小助手导出
     * @param response
     * @return
     */
    @RequestMapping("/exportWithdraw")
    @ResponseBody
    public boolean exportWithdraw(String begintime,String endtime,String tname,HttpServletResponse response) throws ResultTooManyException {
        String userid = getSessionValue().getId();
        yunYingService.exportWithdraw(begintime, endtime, tname, response);
        return true;

    }

    /**
     * 获取列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> listLetters(String userId,Integer page, Integer size) throws Exception {
        //ObjectId uid=getUserId();
        //String cacheKey =CacheHandler.getKeyString(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));
        //CacheHandler.cache(cacheKey, "0", Constant.SECONDS_IN_HOUR);

        Map<String, Object> result = new HashMap<String, Object>();
        if (size == null) {
            size = 10;
        }
        List<ObjectId> userList = new ArrayList<ObjectId>();
        if (userId.equals("0")) {
            List<UserDetailInfoDTO> userEntryList = userService.getK6ktEntryByRoles2();
            if (userEntryList!=null && userEntryList.size()!=0) {
                for (UserDetailInfoDTO userDetailInfoDTO : userEntryList) {
                    userList.add(new ObjectId(userDetailInfoDTO.getId()));
                }
            }
        } else {
            userList.add(new ObjectId(userId));
        }
//        String userid = getSessionValue().getId();
        // 传进来的page是从1开始的
        List<LetterRecordEntry> letterRecordEntryList = letterService.getLetterRecordList(userList, page - 1, size);


        //所有相关的信件的id
        Set<ObjectId> letterids = new HashSet<ObjectId>();
        //与当前用户关联的信件的所有用户id
        Set<ObjectId> userIdSet = new HashSet<ObjectId>();


        for (LetterRecordEntry e : letterRecordEntryList) {
            //如果当前用户是user
            if (e.getUserId().equals(getUserId())) {
                userIdSet.add(e.getLetterUserId()); //添加对方id到待查id
                letterids.add(e.getUserState().getId());//获取user的letterid
            } else {
                userIdSet.add(e.getUserId());//添加id到待查id
                letterids.add(e.getLetterUserState().getId());//获取对方的letterid
            }
        }
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap2(userIdSet, Constant.FIELDS);
        Map<ObjectId, LetterEntry> letterEntryMap = letterService.getLetterEntryMap(letterids, Constant.FIELDS);

        List<LetterDTO> letterDTOList = new ArrayList<LetterDTO>();

        for (LetterRecordEntry e : letterRecordEntryList) {

            UserEntry userEntry;
            ObjectId ui;
            LetterRecordEntry.LetterRecordState letterRecordState;
            LetterEntry letterEntry;

            //如果是user
            if ((e.getUserId()).equals(getUserId())) {
                ui = e.getLetterUserId();
                letterRecordState = e.getUserState();
            } else {
                ui = e.getUserId();
                letterRecordState = e.getLetterUserState();
            }
            userEntry = userMap.get(ui);
            if (null == userEntry) {
                logger.error("Can not find user;the id=" + ui);
                continue;
            }
            letterEntry = letterEntryMap.get(letterRecordState.getId());
            if (letterEntry != null) {
                LetterDTO letterDTO = new LetterDTO();
                letterDTO.setUserid(e.getLetterUserId().toString());
                letterDTO.setLetterId(letterRecordState.getId().toString());
                UserDetailInfoDTO userdto = userService.getUserInfoById(ui.toString());
                letterDTO.setSenderchatid(userdto.getChatid());
                letterDTO.setSenderId(ui.toString());
                UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(userEntry);
                letterDTO.setSenderName(userDetailInfoDTO.getUserName());
                if(UserRole.isStudent(userDetailInfoDTO.getRole())) {
                    letterDTO.setRoleName(UserRole.STUDENT.getDes());
                } else if (UserRole.isParent(userDetailInfoDTO.getRole())) {
                    letterDTO.setRoleName(UserRole.PARENT.getDes());
                } else if (UserRole.isHeadmaster(userDetailInfoDTO.getRole())) {
                    letterDTO.setRoleName(UserRole.HEADMASTER.getDes());
                } else {
                    letterDTO.setRoleName(UserRole.TEACHER.getDes());
                }
                letterDTO.setUserImage(userDetailInfoDTO.getImgUrl());
                letterDTO.setUnread(letterRecordState.getUnRead());

                letterDTO.setSendingTime(new Date(letterEntry.getID().getTime()));
                letterDTO.setLetterType(LetterType.getLetterType(letterEntry.getType()));
                letterDTO.setContent(letterEntry.getContent());
                letterDTOList.add(letterDTO);
            }

        }

        result.put("rows", letterDTOList);
        //total ,page, pageSize
        result.put("total", letterService.countLatestLetterList(userList));
        result.put("page", page);
        result.put("pageSize", 10);


        return result;
    }

    /**
     * 得到与某个好友的所有对话
     *
     * @param replyId 好友的id
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @RequestMapping("/getreplyletters")
    @ResponseBody
    public Map<String, Object> getReplyLetters(String userid,String replyId,
                                               Integer page, Integer size) throws Exception {
        if (page == null) {
            page = 1;
        }
        page = page - 1;
        size = 10;
        Map<String, Object> result = new HashMap<String, Object>();


        List<LetterEntry> letterEntryList = letterService.getLetterListByPeerUserId(
                userid, replyId, page, size
        );

        List<LetterDTO> letterDTOList = new ArrayList<LetterDTO>();
        UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(replyId.toString());

//        SessionValue userSessionValue = getSessionValue();

        UserEntry userEntry = userService.searchUserId(new ObjectId(userid));

        //更新未读邮件为已读
        ObjectId uid=userEntry.getID();
        CacheHandler.deleteKey(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));

        for (LetterEntry letterEntry : letterEntryList) {
            LetterDTO letterDTO = new LetterDTO();
            letterDTO.setLetterId(letterEntry.getID().toString());
            letterDTO.setSenderId(letterEntry.getSenderId().toString());
            UserDetailInfoDTO userdto = userService.getUserInfoById(letterEntry.getSenderId().toString());
            letterDTO.setSenderchatid(userdto.getChatid());
            letterDTO.setContent(letterEntry.getContent());
            letterDTO.setSendingTime(new Date(letterEntry.getID().getTime()));
            letterDTO.setLetterState(LetterState.LETTER_READED);
            letterDTO.setLetterType(LetterType.COMMON_LETTER);
            if (letterEntry.getSenderId().toString().equals(userEntry.getID().toString())) {

                letterDTO.setSenderName(userEntry.getUserName());
                letterDTO.setRecipient(replyId);
                letterDTO.setRecipientName(userDetailInfoDTO.getUserName());
                if(UserRole.isStudent(userEntry.getRole())) {
                    letterDTO.setRoleName(UserRole.STUDENT.getDes());
                } else if (UserRole.isParent(userEntry.getRole())) {
                    letterDTO.setRoleName(UserRole.PARENT.getDes());
                } else if (UserRole.isHeadmaster(userEntry.getRole())) {
                    letterDTO.setRoleName(UserRole.HEADMASTER.getDes());
                } else {
                    letterDTO.setRoleName(UserRole.TEACHER.getDes());
                }
                letterDTO.setUserImage(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));

            } else {

                letterService.readLetter(getUserId(), letterEntry);

                letterDTO.setSenderName(userDetailInfoDTO.getUserName());
                if(UserRole.isStudent(userDetailInfoDTO.getRole())) {
                    letterDTO.setRoleName(UserRole.STUDENT.getDes());
                } else if (UserRole.isParent(userDetailInfoDTO.getRole())) {
                    letterDTO.setRoleName(UserRole.PARENT.getDes());
                } else if (UserRole.isHeadmaster(userDetailInfoDTO.getRole())) {
                    letterDTO.setRoleName(UserRole.HEADMASTER.getDes());
                } else {
                    letterDTO.setRoleName(UserRole.TEACHER.getDes());
                }
                letterDTO.setRecipient(userEntry.getID().toString());
                letterDTO.setRecipientName(userEntry.getUserName());
                letterDTO.setUserImage(userDetailInfoDTO.getImgUrl());

            }
            letterDTOList.add(letterDTO);


        }

        int totalLetter = letterService.countLetterListByPeerUserId(userEntry.getID().toString(),
                replyId);
        result.put("total", totalLetter);
        result.put("page", page + 1);
        result.put("pageSize", size);
        result.put("rows", letterDTOList);
        result.put("recipient", userDetailInfoDTO.getUserName());


        return result;
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
    public Map<String,Object> getFriendReplyInfo(String blogid,@RequestParam("type") int type,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,String startDate2,String endDate2) {
        if (page==0) {
            page = 1;
        }
        if (pageSize==0) {
            pageSize = 10;
        }
        Map<String,Object> model = new HashMap<String, Object>();
        int count = homeSchoolService.getFriendReplyCount(blogid,type,getSessionValue().getId(),startDate2,endDate2);
        List<MicroBlogEntry> retList = homeSchoolService.getFriendReplyInfo(blogid,type,getSessionValue().getId(),startDate2,endDate2,page,pageSize);
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
     * 回复私信
     *
     * @param recipient
     * @param message
     * @return
     */
    @RequestMapping("/reply")
    @ResponseBody
    public Map<String, Object> replyLetter(String userid,String[] recipient, String message) {

        Map<String, Object> result = new HashMap<String, Object>();


        List<ObjectId> receiverIds = new ArrayList<ObjectId>();

        for (String aRecipient : recipient) {

            receiverIds.add(new ObjectId(aRecipient));
        }

        LetterEntry letterEntry = new LetterEntry(new ObjectId(userid),
                message, receiverIds);
        letterService.sendLetter(letterEntry);

        return result;
    }

    /**
     * 接收者删除
     *
     * @param letterId
     * @return
     */
    @RequestMapping("/deletereply")
    @ResponseBody
    public Map<String, Object> deletereply(String userid,String letterId, String pairId) throws Exception {



//        ObjectId uid=getUserId();
        CacheHandler.deleteKey(CacheHandler.CACHE_USER_LETTER_COUNT, userid);


        //letterService.d
        letterService.deleteLetter(userid, pairId, letterId);


        Map<String, Object> result = new HashMap<String, Object>();

        result.put("status", "ok");

        return result;
    }

    /**
     * 删除与一个用户的所有对话
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteallreply")
    @ResponseBody
    public Map<String, Object> deleteAllreply(String userid,String replyId) throws Exception {

        CacheHandler.deleteKey(CacheHandler.CACHE_USER_LETTER_COUNT, userid);

        letterService.deleteAllReply(userid, replyId);


        Map<String, Object> result = new HashMap<String, Object>();

        result.put("status", "ok");

        return result;
    }

    /**
     * 新建
     *
     * @param
     * @return
     */
    @RequestMapping("/new")
    @ResponseBody
    public QuestionnaireDTO addQuestionnaire(QuestionnaireDTO questionnaireDTO){

        questionnaireDTO.setPublisher(getSessionValue().getId());
        questionnaireDTO.setPublishDate(new Date());


        QuestionnaireEntry questionnaireEntry = questionnaireDTO.buildSurveyEntry();
//        List<SchoolEntry> schoolEntryList = schoolService.getSchoolEntryListByRegionForEdu();
//        if (schoolEntryList!=null && schoolEntryList.size()!=0) {
//            for (SchoolEntry schoolEntry : schoolEntryList) {
//                //add
//                questionnaireEntry.setSchoolId(schoolEntry.getID());
//                questionnaireEntry.setID(new ObjectId());
//                questionnaireService.addQuestionnaire(questionnaireEntry);
//            }
//        }
        questionnaireEntry.setIsPlatform(1);
        questionnaireService.addQuestionnaire(questionnaireEntry);
        return questionnaireDTO;
    }

    /** 列出所有调查问卷
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/question/list")
    @ResponseBody
    public Map<String ,Object> listSurvey(@RequestParam Integer page,@RequestParam Integer size) throws Exception{

        Map<String,Object> model = new HashMap<String, Object>();
        List<QuestionnaireEntry> surveyEntries = yunYingService.findPlatformSurveys(page, size);

        List<QuestionnaireDTO> questionnaireDTOList = new ArrayList<QuestionnaireDTO>();

        for(QuestionnaireEntry questionnaireEntry : surveyEntries){
            QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO(questionnaireEntry);
            checkState(questionnaireDTO,getSessionValue().getId());
            questionnaireDTOList.add(questionnaireDTO);
        }

        model.put("content", questionnaireDTOList);

        //分页信息
        int totalCount = yunYingService.getPlatFormQuestionnaireCount();

        int totalPages = 0;
        if(totalCount > 0)
        {
            totalPages = (totalCount-1)/size+1;
        }
        boolean first = false;
        if(page==0)
        {
            first = true;
        }
        boolean last = false;
        if(page == totalPages-1)
        {
            last = true;
        }
        model.put("first",first);
        model.put("totalElements",totalCount);
        model.put("last",last);
        model.put("numberOfElements",questionnaireDTOList.size());
        model.put("totalPages",totalPages);
        model.put("number",page);

        model.put("sort",null);
        model.put("size",size);
        return model;
    }

    private void checkState(QuestionnaireDTO questionnaireDTO, String userId){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        if(questionnaireDTO.getPublisher().equals(userId)){
            questionnaireDTO.setState(1);
        } else if(questionnaireDTO.getRespondents()!=null && questionnaireDTO.getRespondents().containsKey(userId)){
            questionnaireDTO.setState(3);
        } else if (questionnaireDTO.getEndDate().before(calendar.getTime())){
            questionnaireDTO.setState(2);
        } else {
            questionnaireDTO.setState(4);
        }
    }

    @RequestMapping("jinyan")
    @ResponseBody
    public Map<String ,Object> isJinyan(String userid) {
        Map<String,Object> model = new HashMap<String, Object>();
        int jinyan = yunYingService.isJinyan(userid);
        model.put("jinyan",jinyan);
        return model;
    }

    @RequestMapping("updateBeiZhu")
    @ResponseBody
    public Map<String ,Object> updateBeiZhu(String id,String beiZhu) {
        Map<String,Object> model = new HashMap<String, Object>();
        try{
        yunYingService.updateBeiZhu(id,beiZhu);
        model.put("flg",true);
        } catch (Exception e) {
            model.put("flg",false);
        }
        return model;
    }

    @RequestMapping("updateStatus")
    @ResponseBody
    public Map<String ,Object> updateStatus(String id,int status) {
        Map<String,Object> model = new HashMap<String, Object>();
        try{
            yunYingService.updateStatus(id, status);
            model.put("flg",true);
        } catch (Exception e) {
            model.put("flg",false);
        }
        return model;
    }

    //  学生积分统计页面
    @RequestMapping("/balanceList")
    public String balanceList(String userId,Map<String, Object> model) {
        //判断studentId是否是null或""
        if(userId==null||"".equals(userId)){
            userId=getUserId().toString();
        }
        model.put("studentId",userId);
        return "yunying/balanceHistory";
    }

    /**
     * 学生经验值Log的取得
     *
     * @return
     */
    @RequestMapping("/getBalanceInfo")//原名“getStuScoreInfo”
    public @ResponseBody
    Page<ExperienceLogDTO> getBalanceInfo(@RequestParam("userid") String usreid,Pageable pageable) {
        //分页查询用户积分日志
        return yunYingService.getBalanceInfo(usreid, pageable);
    }
    /**
     * 删除微博
     *
     * @return
     */
    @RequestMapping("shouCangMicroblog")
    public @ResponseBody Map<String,Object> shouCangMicroblog(@RequestParam("id") String id) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        if(null==id || !ObjectId.isValid(id))
        {
            logger.error("the param id is error!the user:"+getSessionValue());
        }
        yunYingService.shouCangMicroblog(id);
        model.put("result", true);
        return model;
    }


    @RequestMapping("/uploadExp")
    public void importSalary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        response.setContentType("text/html;charset=UTF-8");
        final ImportExcelUtil salaryImport = new ImportExcelUtil(0, 0, new ImportExcelUtil.IConvertRow() {
            @Override
            public Object convert(List<String> rowData, List<String> titles) throws ImportException {
                if (StringUtils.isEmpty(rowData.get(0))) {
                    throw new ImportException("模板文件第一列中存在空白内容，导入失败");
                }
                SalaryDto salaryDto = new SalaryDto();
                salaryDto.setUserName(rowData.get(0));
                salaryDto.setSs(Double.parseDouble(rowData.get(1)));
                salaryDto.setRemark(rowData.get(2));
                return salaryDto;
            }
        }, new ImportExcelUtil.ISaveData() {
            @Override
            public void save(List data) throws ImportException {
                for (SalaryDto dto : (List<SalaryDto>) data) {
                    yunYingService.updateUserExp(dto);
                }
            }
        });
        try {
            salaryImport.importData(request, getSessionValue().getId(), "file1");
            respObj.setCode(Constant.SUCCESS_CODE);
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        } catch (IllegalParamException e) {
            respObj.setMessage(e.getMessage());
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        } catch (ImportException e) {
            respObj.setMessage(e.getMessage());
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        } catch (Exception e) {
            respObj.setMessage("导入文件处理错误");
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        }

    }
}
