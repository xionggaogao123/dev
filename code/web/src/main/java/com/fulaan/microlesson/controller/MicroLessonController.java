package com.fulaan.microlesson.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.microlesson.dto.MatchAddressDTO;
import com.fulaan.microlesson.dto.MicroMatchDTO;
import com.fulaan.microlesson.dto.ScoreDTO;
import com.fulaan.microlesson.dto.TeacherScoreDTO;
import com.fulaan.microlesson.service.MicroLessonService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.mongodb.BasicDBObject;
import com.pojo.app.SimpleDTO;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.lesson.*;
import com.pojo.microlesson.LessonScoreEntry;
import com.pojo.microlesson.MicroMatchEntry;
import com.pojo.microlesson.ScoreTypeEntry;
import com.pojo.microlesson.TeacherScoreEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by wang_xinxin on 2015/8/19.
 */
@Controller
@RequestMapping("/microlesson")
public class MicroLessonController extends BaseController {

    @Autowired
    private MicroLessonService microLessonService;

    @Autowired
    private EducationBureauService bureauService;

    private LessonService lessonService = new LessonService();

    private VideoService videoService =new VideoService();
    
    private UserService userService =new UserService();
    
    private SchoolService schoolService =new SchoolService();

    /*
   * 跳转微课大赛页面
   * */
    @RequestMapping("/micropage")
    public String micropage(Model model , @RequestParam(required = false,defaultValue = "1")int a) {
        if (getbureauentryid() == null) {
            if(a==10000){
                return "microlesson/micropagec";
            }
            return "microlesson/micropage";
        } else {
            int count = microLessonService.selMicroMatchCount(getbureauentryid());
            if (count==0) {
                if(a==10000){
                    return "microlesson/micropagec";
                }
                return "microlesson/micropage";
            } else {
                if(a==10000){
                    return "microlesson/matchlistc";
                }
                return "microlesson/matchlist";
            }
        }

/*voyage_wu微课大赛java*/

    }

    /**
     * 跳转发起比赛页面
     * @return
     */
    @RequestMapping("micromatchpage")
    public String micromatchpage(@RequestParam(required = false,defaultValue = "1")int a) {
        if(a==10000){
            return "microlesson/micromatchc";
        }
        return "microlesson/micromatch";
    }

    @RequestMapping("editmatch")
    public String editmatch(@RequestParam("matchid") String matchid,Map<String, Object> model,@RequestParam(required = false,defaultValue = "1")int a) {
        model.put("matchid", matchid);
        if(a==10000){
            return "microlesson/editmatchc";
        }
        return "microlesson/editmatch";
    }


    @RequestMapping("editmatchDtail")
    @ResponseBody
    public Map editmatchDtail(@RequestParam("matchid") String matchid) {
        Map<String, Object> model = new HashMap<String, Object>();
        MicroMatchDTO matchdto = microLessonService.matchDetailInfo(matchid,1);
        model.put("match",matchdto);
        return model;
    }



    /**
     * 上传图片
     * @param request
     * @return
     */
    @RequestMapping(value = "/addMatchPic", produces = "text/html; charset=utf-8")
    public @ResponseBody Map addBlogPic(MultipartRequest request) {
        Map result = new HashMap<String,Object>();
        String filePath = FileUtil.UPLOADMATCHDIR;
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


    /**
     * 评委通讯录
     * @return
     */
    @RequestMapping("microMatchAdress")
    public @ResponseBody Map microMatchAdress() {
        List<MatchAddressDTO> adslist = microLessonService.microMatchAdress(getSessionValue().getId());
        Map map = new HashMap();
        map.put("rows",adslist);
        return map;
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        return new ObjectId().toString()+Constant.POINT+ file.getOriginalFilename();
    }

    /**
     * 添加微课大赛
     * @param micromatch
     * @return
     */
    @RequestMapping("addmicromatch")
    @ResponseBody
    public Map addMicroMatch(MicroMatchDTO micromatch) {
        Map model = new HashMap();
        try {
            String userid = getSessionValue().getId();

            MicroMatchEntry microMatchEntry = micromatch.buildMicroMatchEntry(new ObjectId(userid),getbureauentryid());
            microLessonService.addMicroMatch(microMatchEntry);
            model.put("result",true);
        } catch(Exception e) {
            model.put("result",false);
        }


    return model;
    }

    /**
     * 跟新比赛
     * @param micromatch
     * @return
     */
    @RequestMapping("updateMatch")
    @ResponseBody
    public Map updateMatch(MicroMatchDTO micromatch) {
        Map model = new HashMap();
        try {
            MicroMatchEntry microMatchEntry = micromatch.buildUpdateMicroMatchEntry();
            microLessonService.updatematch(microMatchEntry,micromatch);
            model.put("result",true);
        } catch(Exception e) {
            model.put("result",false);
        }

    return model;
    }

    /**
     * 查询微课大赛列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("matchlist")
    @ResponseBody
    public Map<String,Object> selMicroMatch(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        Map<String,Object> model = new HashMap<String,Object>();

        int count = microLessonService.selMicroMatchCount(getbureauentryid());
        List<MicroMatchDTO> matchList = microLessonService.selMicroMatch(getbureauentryid(),page,pageSize);
        model.put("rows",matchList);
        model.put("total", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }

    private ObjectId getbureauentryid() {
        String userid = getSessionValue().getId();
        EducationBureauEntry bureauEntry = null;
        if (UserRole.isEducation(getSessionValue().getUserRole())) {
            bureauEntry = bureauService.selEducationByUserId(userid);
        } else {
            List<EducationBureauEntry> bureauEntrylist = bureauService.selEducationBySchoolId(getSessionValue().getSchoolId());
            if (bureauEntrylist != null && bureauEntrylist.size()!=0) {
                bureauEntry = bureauEntrylist.get(0);
            }
        }
        if (bureauEntry==null) {
            return null;
        } else {
            return bureauEntry.getID();
        }

    }

    /**
     * 查询微课大赛列表
     * @param typeid
     * @param matchid
     * @return
     */
    @RequestMapping("lessonlist")
    @ResponseBody
    public Map<String,Object> selMicroLesson(@RequestParam("typeid") String typeid,@RequestParam("matchid") String matchid) {
        Map<String,Object> model = new HashMap<String,Object>();
        List<LessonEntry> lessonlist = microLessonService.selMicroLesson(typeid,matchid);
        List<LessonDTO> lessons = new ArrayList<LessonDTO>();
       
        
        
        
        
        List<ObjectId> userIds =MongoUtils.getFieldObjectIDs(lessonlist, "ui");
        
        
        Map<ObjectId, UserEntry> userMap=userService.getUserEntryMap(userIds, new BasicDBObject("nm",1).append("si", 1));
        
        List<ObjectId> schoolIds =MongoUtils.getFieldObjectIDs(userMap.values(), "si");
        

        Map<ObjectId, SchoolEntry> schoolMap=  schoolService.getSchoolMap(schoolIds, new BasicDBObject("nm",1));

        
        
        
        
        
        
        
        
        MicroLessonDTO les = null;
        UserEntry ue=null;
        SchoolEntry se=null;
        for(LessonEntry e:lessonlist)
        {
            les = new MicroLessonDTO(e);
            if (StringUtils.isEmpty(e.getImgUrl())) {
                les.setImgUrl("/img/matchlesson.jpg");
            }
            les.setIsdelete(false);
            if (UserRole.isEducation(getSessionValue().getUserRole())) {
                les.setIsdelete(true);
            } else {
                if (e.getUserId().toString().equals(getSessionValue().getId())) {
                    les.setIsdelete(true);
                }
            }
            
            ue=userMap.get(e.getUserId());
            
            if(null!=ue)
            {
            	les.setUserName(ue.getUserName().replaceAll("[(0-9)]", "").replaceAll("kkt小助手", "k6kt小助手"));
            	se=schoolMap.get(ue.getSchoolID());
            	if(null!=se)
            	{
            		les.setSchoolName(se.getName());
            	}
            }
            
            lessons.add(les);
        }
        model.put("rows",lessons);
        return model;
    }


    /**
     * 比赛详细
     * @param matchid
     * @return
     */
    @RequestMapping("matchDetailInfo")
    public String matchDetailInfo(@RequestParam("matchid") String matchid,Model model,@RequestParam(required = false,defaultValue = "1")int a) {
        MicroMatchDTO match = microLessonService.matchDetailInfo(matchid,2);
        model.addAttribute("rows",match);
        if(a==10000){
            return "microlesson/matchdetailc";
        }
    return "microlesson/matchdetail";
    }

    /**
     * 新建课程
     * @param typeid
     * @param name
     * @return
     * @throws com.sys.exceptions.IllegalParamException
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/create")
    @ResponseBody
    public RespObj createLesson(String typeid,String name,String matchid) throws IllegalParamException
    {
        if(!ValidationUtils.isRequestLessonName(name))
        {
            return RespObj.FAILD;
        }
        LessonEntry e =new LessonEntry(name, name, LessonType.BACKUP_LESSON, getUserId(),new ObjectId(matchid),null,Constant.ZERO);
        ObjectId id=lessonService.addLessonEntry(e);
        microLessonService.updateMicroMatch(matchid, typeid, id,name);
        RespObj obj =new RespObj(Constant.SUCCESS_CODE, id.toString());
        return obj;
    }

    /**
     * 编辑课程
     * @param lessonId
     * @param model
     * @return
     * @throws com.sys.exceptions.PermissionUnallowedException
     * @throws IllegalParamException
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("edit")
    public String editLesson(@ObjectIdType ObjectId lessonId, Map<String, Object> model,@RequestParam(required = false,defaultValue = "1")int a) throws PermissionUnallowedException, IllegalParamException {
        LessonEntry e =lessonService.getLessonEntry(lessonId);
        model.put("matchid",e.getDirId().toString());
        if (a==10000){
            return "microlesson/matchlessonc";
        }
        return "microlesson/matchlesson";
    }

    /**
     * 删除比赛
     * @param matchid
     * @return
     */
    @RequestMapping("deletematch")
    @ResponseBody
    public Map deletematch(@RequestParam("matchid") String matchid) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            microLessonService.deleteMatch(new ObjectId(matchid));
            model.put("result",true);
        } catch(Exception e) {
            model.put("result",false);
        }
        return model;
    }

    /**
     * 删除比赛微课
     * @param lessonid
     * @param matchid
     * @return
     */
    @RequestMapping("dellesson")
    @ResponseBody
    public Map deletelesson(@RequestParam("lessonid") String lessonid,@RequestParam("matchid") String matchid) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            microLessonService.deletelesson(lessonid, matchid);
            model.put("result",true);
        } catch(Exception e) {
            model.put("result",false);
        }
        return model;
    }

    @RequestMapping("addScoreInfo")
    @ResponseBody
    public Map addScoreInfo(TeacherScoreDTO teacherdto) {
        Map<String,Object> model = new HashMap<String, Object>();
        try {
            TeacherScoreEntry score = microLessonService.selTeacherScore(getSessionValue().getId(), new ObjectId(teacherdto.getLessonid()));
            if (score!=null) {
                model.put("result",false);
                model.put("mesg","已打分，不可以重复打分！");
            } else {
                TeacherScoreEntry teacherScore = teacherdto.buildTeacherScore(new ObjectId(getSessionValue().getId()),getSessionValue().getUserName(),model);
                microLessonService.addScoreInfo(teacherScore);
                model.put("result",true);
                model.put("score",teacherScore.getScore());
            }
        } catch (Exception e) {
            model.put("result",false);
            model.put("mesg","打分失败！");
        }

        return model;
    }

    /**
     * 查询比赛打分列表
     * @param matchid
     * @return
     */
    @RequestMapping("getlessonScoreList")
    public String getlessonScoreList(@RequestParam("matchid") String matchid,Model model,@RequestParam(required = false,defaultValue = "1")int a) {
        microLessonService.getlessonScoreList(matchid, model);
        if(a==10000){
            return "microlesson/matchresultc";
        }
        return "microlesson/matchresult";
    }

    @RequestMapping("view")
    public String viewLesson(@ObjectIdType ObjectId lessonId, Map<String, Object> model,@RequestParam(required = false,defaultValue = "1")int a) throws Exception {
        String userid = getSessionValue().getId();
        LessonEntry e=lessonService.getLessonEntry(lessonId);
        if(null==e)
        {
            throw new IllegalParamException();
        }
        MicroMatchDTO match = microLessonService.matchDetailInfo(e.getDirId().toString(),2);
        model.put("matchname",match.getMatchname());
        model.put("lessonname",e.getName());
        model.put("lessonid",e.getID().toString());
        model.put("matchid",e.getDirId().toString());
        ScoreDTO scoreDTO = new ScoreDTO();
        List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>();
        List<ScoreTypeEntry> scoreTypeEntryList = match.getScoretypes();
        model.put("stype",0);
        if (scoreTypeEntryList!=null && scoreTypeEntryList.size()!=0) {
            for (ScoreTypeEntry scoreTypeEntry : scoreTypeEntryList) {
                scoreDTO = new ScoreDTO();
                scoreDTO.setName(scoreTypeEntry.getName());
                List<Integer> scorelist = new ArrayList<Integer>();
                for(int i=0;i<=scoreTypeEntry.getScore();i++) {
                    scorelist.add(i);
                }
                scoreDTO.setScorelist(scorelist);
                scoreDTOs.add(scoreDTO);
            }
            model.put("stype",1);
        }
//        else {
//            scoreDTO = new ScoreDTO();
//            scoreDTO.setName("总分");
//            List<Integer> scorelist = new ArrayList<Integer>();
//            for(int i=0;i<=100;i++) {
//                scorelist.add(i);
//            }
//            scoreDTO.setScorelist(scorelist);
//            scoreDTOs.add(scoreDTO);
//        }
        model.put("scoreTypeList",scoreDTOs);
        List<Integer> scores = new ArrayList<Integer>();
        for (int i=0;i<=100;i++ ) {
            scores.add(i);
        }
        model.put("scores",scores);
        LessonDetailDTO lessonDetail = buildLessonDTO(e);
        BeanInfo info = Introspector.getBeanInfo(lessonDetail.getClass());
        for (PropertyDescriptor pd: info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null) {
                model.put(pd.getName(), reader.invoke(lessonDetail));
            }
        }
        LessonScoreEntry scorety = microLessonService.getlessonscore(lessonId,e.getDirId());

            if (UserRole.isEducation(getSessionValue().getUserRole())) {
                model.put("isdelete",1);
                if (scorety != null) {
                    model.put("allscore",scorety.getAllScore());
                    model.put("avg",scorety.getAverage());
                    model.put("sort",scorety.getSort());
                    microLessonService.selmatchscore(lessonId,e.getDirId(),model);
                } else {
                    model.put("type",4);
                }

            } else {
                MicroMatchEntry matchentry = microLessonService.selMicroMatchByUserid(userid, lessonId);
                if ((e.getUserId().toString()).equals(getSessionValue().getId())) {
                    model.put("isdelete",1);
                } else {
                    model.put("isdelete",2);
                }

                if (match.getEdtime()<new Date().getTime()) {
                    model.put("type",3);
                } else {
                    if (matchentry != null) {
//                        if ((e.getUserId().toString()).equals(getSessionValue().getId())) {
//                            model.put("type",3);
//                        } else {
                            model.put("type",2);
                            TeacherScoreEntry score = microLessonService.selTeacherScore(userid, lessonId);
                            if (score!=null) {
                                model.put("rate",1);
                                model.put("score",score.getScore());
                                List<ScoreDTO> scoreDTOs2 = new ArrayList<ScoreDTO>();

                                List<ScoreTypeEntry> scoreTypeEntryList1 = score.getScoreTypeList();
                                int sc = score.getScore();
                                if (scoreTypeEntryList1!=null && scoreTypeEntryList1.size()!=0) {
                                    sc = 0;
                                    for (ScoreTypeEntry scoreTypeEntry:scoreTypeEntryList1) {
                                        scoreDTO = new ScoreDTO();
                                        scoreDTO.setName(scoreTypeEntry.getName());
                                        scoreDTO.setScore(String.valueOf(scoreTypeEntry.getScore()));
                                        scoreDTOs2.add(scoreDTO);
                                        sc += scoreTypeEntry.getScore();
                                    }
                                }
                                scoreDTO = new ScoreDTO();
                                scoreDTO.setName("总分");
                                scoreDTO.setScore(String.valueOf(sc));
                                scoreDTOs2.add(scoreDTO);
                                model.put("scorelist2",scoreDTOs2);
                                model.put("comment",score.getComment());
                            } else {
                                model.put("rate",2);
                            }
//                        }

                    } else {
                        model.put("type",3);

                    }
                }

            }
        if(a==10000){
            return "microlesson/lessondetailc";
        }

        return "microlesson/lessondetail";
    }

    private LessonDetailDTO buildLessonDTO(LessonEntry e) {
        LessonDetailDTO dto =new LessonDetailDTO(e);
        List<ObjectId> videoList =e.getVideoIds();
        SimpleDTO simpleDTO=null;
        VideoDTO VideoDTO =null;
        //视频
        if(!videoList.isEmpty())
        {
            Map<ObjectId, VideoEntry> maps=videoService.getVideoEntryMap(videoList, Constant.FIELDS);
            if(!maps.isEmpty())
            {
                for(Map.Entry<ObjectId, VideoEntry> entry:maps.entrySet())
                {
                    VideoEntry ve = entry.getValue();

                    VideoDTO =new VideoDTO(ve); //todo video location
                    if(ve.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
                        VideoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, entry.getValue().getBucketkey()));
                    }else if(ve.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType())
                    {
                        VideoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, entry.getValue().getBucketkey()));
                    }else if(ve.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType())
                    {
                        VideoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, entry.getValue().getBucketkey()));
                    }
                    dto.addDTOToVidesList(VideoDTO);
                }
            }
        }
        //课件
        List<LessonWare> lessonWareList =e.getLessonWareList();
        if(!lessonWareList.isEmpty())
        {
            for(LessonWare ware:lessonWareList)
            {
                String path = "";
                simpleDTO =new SimpleDTO(ware);
                if(ware.getPath().contains("upload")){//兼容老的数据
                    path = ware.getPath();
                    simpleDTO.setValue(ware.getPath());
                }else {
                    path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, ware.getPath());
                    simpleDTO.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, ware.getPath()));
                }
                simpleDTO.setType1(0);
                if (path.contains("ppt")||path.contains("pptx")) {
                    simpleDTO.setType1(1);
                    simpleDTO.setValue1("/static_new/images/ppt.png");
                } else if (path.contains("doc")||path.contains("docx")) {
                    simpleDTO.setType1(1);
                    simpleDTO.setValue1("/static_new/images/doc.png");
                } else if (path.contains("pdf")) {
                    simpleDTO.setValue1("/static_new/images/pdf.png");
                } else if (path.contains("avi")) {
                    simpleDTO.setValue1("/static_new/images/avi.png");
                } else if (path.contains("jpg")) {
                    simpleDTO.setValue1("/static_new/images/jpg.png");
                } else if (path.contains("xls")||path.contains("xlsx")) {
                    simpleDTO.setType1(1);
                    simpleDTO.setValue1("/static_new/images/xls.png");
                } else if (path.contains("mp3")) {
                    simpleDTO.setValue1("/static_new/images/mp3.png");
                } else {
                    simpleDTO.setValue1("/static_new/images/no.png");
                }
                dto.addDTOToCoursewareList(simpleDTO);
            }
        }

        return dto;
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadAll")
    public void downloadAll(@RequestParam("matchid") String matchid, HttpServletRequest request, HttpServletResponse response) {
        try {
            microLessonService.downAllFile(matchid,request,response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalParamException e) {
            e.printStackTrace();
        }
    }

    /**
     * m3u8 ת mp4
     * @param filePath
     * @return
     */
    @RequestMapping("/m3u8DownLoad")
    @ResponseBody
    public Map m3u8DownLoad(String filePath, HttpServletRequest request) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            List<String> urls=new ArrayList<String>();
            urls.add("http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5784d4e10cf28165097d5a3f.mp4.m3u8#来凤实验小学-李书杭");

            urls.add("http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5788993db0fbeb224e077aeb.mp4.m3u8#来凤实验小学-田祥瑞(1)");
            urls.add("http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5788998fb0fbeb224e077aee.mp4.m3u8#来凤实验小学-田祥瑞(2)");
            urls.add("http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/578cdc05b0fbeb224e084fd2.mp4.m3u8#杨浦小学-车宜轩");
            for(String url:urls) {
                String path = microLessonService.m3u8DownLoad(url, request);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalParamException e) {
            e.printStackTrace();
        }
        return model;
    }

}
