package com.fulaan.meeting.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.meeting.dto.MeetingDTO;
import com.fulaan.meeting.dto.MessageDTO;
import com.fulaan.meeting.dto.VoteDTO;
import com.fulaan.meeting.srevice.MeetingService;
import com.fulaan.repertory.controller.CoursewareController;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.UserGroupInfo;
import com.pojo.lesson.LessonWare;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.props.Resources;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.*;
import java.util.*;

/**
 * Created by wang_xinxin on 2016/8/1.
 */
@Controller
@RequestMapping("/meeting")
public class MeetingController extends BaseController {


    private static final Logger logger = Logger.getLogger(MeetingController.class);

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserService userService;

    private VideoService videoService = new VideoService();

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping("meeting")
    public String homepage(Model model) {
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findTeacherInfoBySchoolId(getSessionValue().getSchoolId());
        model.addAttribute("user", userDetailInfoDTOList);
        return "meeting/meeting";
    }


    @RequestMapping("meetingDetail")
    public String meetingDetail(String meetId,Model model) {
        model.addAttribute("meetId",meetId);
        MeetingDTO meet = meetingService.selMeetingDetail(meetId);
        List<UserDetailInfoDTO> userDetailInfoDTOList = meet.getCheckUserList();
        int check =1;
        if (userDetailInfoDTOList!=null && userDetailInfoDTOList.size()!=0){
            for (UserDetailInfoDTO user : userDetailInfoDTOList) {
                if (user.getId().equals(getUserId().toString())) {
                    check = 2;
                }
            }
        }
        int isshow = 1;
        if (meet.getUserId().equals(getUserId().toString())) {
            isshow = 2;
        }
        model.addAttribute("flag",meet.getUserId().equals(getSessionValue().getId()));
        model.addAttribute("isshow", isshow);
        model.addAttribute("check", check);
        model.addAttribute("roomId", meet.getChatId());
        model.addAttribute("name",meet.getName());
        return "meeting/meetingxq";
    }

    @RequestMapping("meetingLog")
    public String meetingLog(String meetId,Model model) {
        Map map = new HashMap();
        MeetingDTO meetingDTO = meetingService.selMeetingDetail(meetId);
        model.addAttribute("name",meetingDTO.getName());
        model.addAttribute("process",meetingDTO.getProcess());
        model.addAttribute("order",meetingDTO.getOrder());
        String usernames = "";
        if (meetingDTO.getUserlist()!=null && meetingDTO.getUserlist().size()!=0) {
            for (UserDetailInfoDTO user : meetingDTO.getUserlist()) {
                usernames += user.getUserName()+"、";
            }
        }
        model.addAttribute("users",usernames);
        List<MessageDTO> messageDTOs = meetingService.selChatLogList(new ObjectId(meetId));
        model.addAttribute("message",messageDTOs);
        List<VoteDTO> voteDTOList = meetingDTO.getVoteDTOList();
        List<VoteDTO> voteDTOs = new ArrayList<VoteDTO>();
        if (voteDTOList!=null && voteDTOList.size()!=0) {
            for (VoteDTO voteDTO : voteDTOList) {
                map = new HashMap();
                meetingService.selVoteDetail(new ObjectId(voteDTO.getId()), getUserId(), map);
                voteDTOs.add((VoteDTO)map.get("rows"));
            }
        }
        model.addAttribute("voteDTOs",voteDTOs);
        return "meeting/meetingjl";
    }

    /**
     * @param meetingDTO
     * @return
     */
    @RequestMapping("addMeetingInfo")
    @ResponseBody
    public Map AddMeetingInfo(MeetingDTO meetingDTO) {
        logger.info("++++++++++jjj+++++++");
        Map map = new HashMap();
        try {
            meetingService.AddMeetingInfo(meetingDTO, getUserId(), getSchoolId(),getSessionValue().getChatid().toString(),getSessionValue().getUserName(),getSessionValue().getMaxAvatar(),map);
            map.put("flag", true);
        } catch (Exception e) {
            logger.error("会议", e);
            logger.error(e.getMessage());
            map.put("flag", false);
        }
        return map;
    }

    /**
     * 上传文件
     * @param request
     * @return
     */
    @RequestMapping(value = "/addMeetingFile", produces = "text/html; charset=utf-8")
    public @ResponseBody Map addMeetingFile(MultipartRequest request) {
        Map result = new HashMap<String,Object>();
//        String filePath = FileUtil.UPLOADMEETDIR;
        List<String> fileUrls = new ArrayList<String>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String examName =getFileName(file);
                RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
                if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
                {
                    throw new FileUploadException();
                }
                fileUrls.add(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT,examName));
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
     * 上传视频
     * @param
     * @return
     * @throws com.sys.exceptions.IllegalParamException
     * @throws java.io.IOException
     * @throws IllegalStateException
     * @throws com.fulaan.screenshot.EncoderException
     */
    @RequestMapping("/file/upload")
    @ResponseBody
    @SessionNeedless
    public Map<String,Object> uploadVideo(MultipartFile Filedata,String meetId) throws IllegalParamException, IllegalStateException, IOException, EncoderException,FileUploadException
    {
        Map map = new HashMap();
        if (!StringUtils.isEmpty(meetId)) {
            boolean falg = meetingService.checkMeetingTime(meetId);
            if (!falg) {
                map.put("flg",false);
                map.put("mesg","会议已结束，不能上传附件！");
                return map;
            }
        }
        String fileName= FilenameUtils.getName(Filedata.getOriginalFilename());
        if(!ValidationUtils.isRequestVideoName(fileName))
        {
            map.put("flg",false);
            map.put("mesg","视频名字非法");
            return map;
        }
        String dex = fileName.substring(fileName.lastIndexOf(".")+1);
        String url = "";
        //.avi; *.mp4; *.mpg; *.flv; *.wmv; *.mov; *.mkv
        if (dex.equals("avi")||dex.equals("mp4")||dex.equals("mpg")||dex.equals("flv")||dex.equals("wmv")||dex.equals("mov")||dex.equals("mkv")) {
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
            Encoder encoder = new Encoder();
            long videoLength = 60000;//缺省一分钟
            //是否生成了图片
            try
            {
                videoLength = encoder.getInfo(savedFile).getDuration();
            }catch(Exception ex)
            {
                logger.error("", ex);
            }
            if(videoLength==-1){
                videoLength = 60000;//获取不到时间就设为1分钟
            }
            logger.debug("begin upload video iamge");

            //String imageFilePath = null;
            VideoEntry ve =new VideoEntry(fileName, videoLength, VideoSourceType.USER_VIDEO.getType(),videoFilekey);
            ve.setID(new ObjectId());
            logger.debug("begin upload video");

            QiniuFileUtils.uploadVideoFile(ve.getID(),videoFilekey, Filedata.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);

            //删除临时文件
            try
            {
                savedFile.delete();
            }catch(Exception ex)
            {
                logger.error("", ex);
            }
            url = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey());
        } else {
            String examName =getFileName(Filedata);
            RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, Filedata.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
            if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
            {
                throw new FileUploadException();
            }
            url = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT,examName);
        }
        if (!StringUtils.isEmpty(meetId)) {
           meetingService.updateMeetFile(new ObjectId(meetId),new LessonWare(fileName.substring(fileName.lastIndexOf(".")+1),fileName,url));
        }
        map.put("flg",true);
        map.put("name",fileName);
        map.put("vurl",url);
        return map;
    }


    /**
     *
     * @param file
     * @return
     */
    private String getFileName(MultipartFile file){
        return new ObjectId().toString()+Constant.POINT+ file.getOriginalFilename();
    }

    /**
     *
     * @param meetId
     * @return
     */
    @RequestMapping("selMeetingDetail")
    public @ResponseBody Map selMeetingDetail(String meetId) {
        Map map = new HashMap<String,Object>();
        map.put("rows", meetingService.selMeetingDetail(meetId));
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selMeetingModelList")
    public @ResponseBody Map selMeetingModelList() {
        Map map = new HashMap<String,Object>();
        List<MeetingDTO> meetingDTOs = meetingService.selModelList(getSchoolId(),getUserId());
        map.put("rows", meetingDTOs);
        return map;
    }

    /**
     *
     * @param meetId
     * @return
     */
    @RequestMapping("delMeetingInfo")
    public @ResponseBody Map delMeetingInfo(String meetId) {
        Map map = new HashMap();
        try {
            meetingService.delMeetingInfo(new ObjectId(meetId));
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     *
     * @param modelName
     * @param meetId
     * @return
     */
    @RequestMapping("updateMeetModel")
    public @ResponseBody Map updateMeetModel(String modelName,String meetId) {
        Map map = new HashMap();
        try {
            meetingService.updateMeetModel(modelName,new ObjectId(meetId),map);
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     * 我的会务
     * @return
     */
    @RequestMapping("selMyMeetingList")
    public @ResponseBody Map selMyMeetingList(String startTime,String endTime,int type,String keyword,int index) {
        Map map = new HashMap();
        List<MeetingDTO> meetingDTOs = meetingService.selMyMeetingList(startTime,endTime,type,keyword,getUserId(),index);
        map.put("rows", meetingDTOs);
        return map;
    }

    /**
     * 我的存档
     * @return
     */
    @RequestMapping("selMeetLogList")
    public @ResponseBody Map selMeetLogList(String startTime,String endTime,int type,String keyword) {
        Map map = new HashMap();
        List<MeetingDTO> meetingDTOs = meetingService.selMeetLogList(startTime, endTime, keyword, getUserId());
        map.put("rows", meetingDTOs);
        return map;
    }


    /**
     * 签到
     * @param meetId
     * @return
     */
    @RequestMapping("checkInMeeting")
    public @ResponseBody Map checkInMeeting(String meetId) {
        Map map = new HashMap();
        try {
            meetingService.checkInMeeting(getUserId(),new ObjectId(meetId));
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     * 签退
     * @param meetId
     * @return
     */
    @RequestMapping("checkOutMeeting")
    public @ResponseBody Map checkOutMeeting(String meetId) {
        Map map = new HashMap();
        try {
            meetingService.checkOutMeeting(getUserId(), new ObjectId(meetId));
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }


    /**
     *
     * @param meetId
     * @param theme
     * @param answers
     * @return
     */
    @RequestMapping("addVoteInfo")
    public @ResponseBody Map addVoteInfo(String meetId,String theme,String[] answers) {
        Map map = new HashMap();
        try {
            meetingService.addVoteInfo(new ObjectId(meetId),getUserId(), theme, answers);
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     * |
     * @param voteId
     * @return
     */
    @RequestMapping("removeVoteInfo")
    public @ResponseBody Map removeVoteInfo(String voteId) {
        Map map = new HashMap();
        try {
            meetingService.removeVoteInfo(new ObjectId(voteId));
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     *
     * @param voteId
     * @param chooseId
     * @return
     */
    @RequestMapping("selVoteUserList")
    public @ResponseBody Map selVoteUserList(String voteId,String chooseId) {
        Map map = new HashMap();
        meetingService.selVoteUserList(new ObjectId(voteId), chooseId,map);
        return map;
    }

    /**
     *
     * @param voteId
     * @return
     */
    @RequestMapping("selVoteDetail")
    public @ResponseBody Map selVoteDetail(String voteId) {
        Map map = new HashMap();
        meetingService.selVoteDetail(new ObjectId(voteId), getUserId(), map);
        return map;
    }

    /**
     *
     * @param voteId
     * @param chooseId
     * @return
     */
    @RequestMapping("updateVoteInfo")
    public @ResponseBody Map updateVoteInfo(String voteId,String chooseId) {
        Map map = new HashMap();
        try {
            meetingService.updateVoteInfo(voteId,chooseId,getUserId());
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     *
     * @param meetId
     * @param flag
     * @param appUserId
     * @param remark
     * @return
     */
    @RequestMapping("submitShenHe")
    public @ResponseBody Map submitShenHe(String meetId,int flag,String appUserId,String remark) {
        Map map = new HashMap();
        try {
            meetingService.submitShenHe(meetId, flag, getUserId(), appUserId, remark);
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     *
     * @param userId
     * @return
     */
    @RequestMapping("selUserInfo")
    public @ResponseBody Map selUserInfo(String userId) {
        Map map = new HashMap();
        UserEntry userEntry = userService.searchUserId(new ObjectId(userId));
        map.put("name", userEntry.getUserName());
        map.put("phone", userEntry.getMobileNumber());
        map.put("email", userEntry.getEmail());
        return map;
    }

    /**
     *
     * @param meetId
     * @return
     */
    @RequestMapping("endMeetingInfo")
    public @ResponseBody Map endMeetingInfo(String meetId) {
        Map map = new HashMap();
        try {
            meetingService.endMeetingInfo(meetId, getUserId(), getSchoolId());
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping("delMeetLog")
    public @ResponseBody Map delMeetLog(String id) {
        Map map = new HashMap();
        try {
            meetingService.delMeetLog(id);
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     *
     * @param meetId
     * @return
     */
    @RequestMapping("addMessage")
    public @ResponseBody Map addMessage(String meetId,String content) {
        Map map = new HashMap();
        try {
            meetingService.addMessage(meetId,content,getUserId(),getSchoolId());
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     *
     * @param meetId
     * @return
     */
    @RequestMapping("selChatLogList")
    public @ResponseBody Map selChatLogList(String meetId) {
        Map map = new HashMap();
        List<MessageDTO> messageDTOs = meetingService.selChatLogList(new ObjectId(meetId));
        map.put("rows",messageDTOs);
        return map;
    }

    /**
     *
     * @param meetId
     * @return
     */
    @RequestMapping("checkMeetingStatus")
    public @ResponseBody Map checkMeetingStatus(String meetId) {
        Map map = new HashMap();
        map.put("flag",meetingService.checkMeetingStatus(meetId,getUserId()));
        return map;
    }

    /**
     * 获取部门及学科人员
     *
     * @return
     */
    @RequestMapping("/getdepartmentusers")
    @ResponseBody
    public Map<String, List<UserGroupInfo<IdNameValuePairDTO>>> getDepartmentAndSubjectUsers() {
        Map<String, List<UserGroupInfo<IdNameValuePairDTO>>> map = new HashMap<String, List<UserGroupInfo<IdNameValuePairDTO>>>();
        //部门的
        List<UserGroupInfo<IdNameValuePairDTO>> retList = new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = null;

        retMap = userService.getDepartmemtMembersMap2(new ObjectId(getSessionValue().getSchoolId()));
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entryMaps : retMap.entrySet()) {
            retList.add(new UserGroupInfo<IdNameValuePairDTO>(entryMaps.getKey(), new ArrayList<IdNameValuePairDTO>(entryMaps.getValue())));
        }
        map.put("department", retList);
        //学科
        retList = new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();
        retMap = userService.getTeacherMap(new ObjectId(getSessionValue().getSchoolId()), 1);
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entryMaps : retMap.entrySet()) {
            retList.add(new UserGroupInfo<IdNameValuePairDTO>(entryMaps.getKey(), new ArrayList<IdNameValuePairDTO>(entryMaps.getValue())));
        }
        map.put("subject", retList);
        return map;
    }

    /**
     * 添加议题
     * @param meetId
     * @return
     */
    @RequestMapping("addIssue")
    public @ResponseBody Map addIssueList(String meetId,String content) {
        Map map = new HashMap();
        try {
            meetingService.addIssueList(meetId, content);
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     * 删除议题
     * @param issueId
     * @return
     */
    @RequestMapping("delIssue")
    public @ResponseBody Map delIssueInfo(String meetId,String issueId) {
        Map map = new HashMap();
        try {
            meetingService.delIssueInfo(meetId,issueId);
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

}
