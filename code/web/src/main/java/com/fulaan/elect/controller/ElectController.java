package com.fulaan.elect.controller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.elect.dto.CandidateDTO;
import com.fulaan.elect.dto.ElectDTO;
import com.fulaan.elect.service.ElectService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.app.SessionValue;
import com.pojo.elect.Candidate;
import com.pojo.elect.ElectEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.video.VideoEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.*;


/**
 * Created by fourer on 15-2-26.
 * Modified by autman on 15-2-28.
 * 投票选举
 * @author qinbo
 */
@Controller
@RequestMapping("/elect")
public class ElectController extends BaseController {


    private static final Logger logger = Logger.getLogger(ElectController.class);

    @Autowired
    private ElectService electService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExperienceService experienceService;
    private VideoService videoService =new VideoService();

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

        List<ClassInfoDTO> classInfoDTOList=userService.getClassDTOList(new ObjectId(sessionValue.getId()),
                sessionValue.getUserRole());
        for(ClassInfoDTO classInfoDTO:classInfoDTOList){
            classIds.add(new ObjectId(classInfoDTO.getId()));
        }

        List<ElectEntry> electEntries = electService.findElectsBySchoolId(schoolId,classIds,getUserId(),page,size);
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

        int electCount = electService.getElectCountBySchoolId(schoolId, classIds);


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

    /** 现在页面没有入口 autman 2015.3.11
     * todo: 未完成,页面未使用
     * @param electDTO 传入的参数
     * @return 修改后的选举信息
     * @throws Exception
     */
    @RequestMapping("/update")
    @ResponseBody
    public ElectEntry updateElect(ElectDTO electDTO) throws Exception{


        SessionValue sessionValue = getSessionValue();
        electDTO.setSchoolId(sessionValue.getSchoolId());
        electDTO.setPublisher(sessionValue.getId());
        return electService.updateElectBasicInfo(electDTO);

    }

    /**删除选举
     * @param id 选举的objectid
     * @return 经验值信息
     */
    @RequestMapping("/delete")
    public @ResponseBody Map<String,Object> deleteElect(String id) throws Exception{

        Map<String,Object> result = new HashMap<String, Object>();
    	if(null==id || !ObjectId.isValid(id))
    	{
          logger.error("the param id is error!the user:"+getSessionValue());
    	}
    	electService.removeElectById(id);
        //todo : 发私信,往相关用户发私信, 经验值

        /*ExpLogType deleteElect = ExpLogType.DELETE_ELECT;
        if (experienceService.updateScore(getUserId().toString(), deleteElect, id)) {
            result.put("score", deleteElect.getExp());
            result.put("scoreMsg", deleteElect.getDesc());
        }*/

        return result;

    }

    /**查看选举详情
     * @param electId
     * @return
     */
    @RequestMapping("/viewElect")
    @ResponseBody
    public ElectDTO viewElect(String electId) throws Exception{

        ElectEntry electEntry=electService.findOne(electId,getUserId());
        ElectDTO electDTO=new ElectDTO(electEntry);
        List<Candidate> candidates=electEntry.getCandidates();
        List<ObjectId> videoIdList=new ArrayList<ObjectId>();
        if(candidates!=null && !candidates.isEmpty()) {
            for (Candidate candidate : candidates) {
                if (candidate.getVideoId() != null)
                    videoIdList.add(candidate.getVideoId());
            }
        }
        Map<ObjectId, VideoEntry> maps=videoService.getVideoEntryMap(videoIdList, Constant.FIELDS);
        if(!maps.isEmpty())
        {
            for(Map.Entry<ObjectId, VideoEntry> entry:maps.entrySet()) {
                List<CandidateDTO> candidateDTOs=electDTO.getCandidates();
                for (CandidateDTO candidateDTO:candidateDTOs)
                {
                    if(candidateDTO.getVideo()!=null&&candidateDTO.getVideo().getId().equals(entry.getKey().toString()))
                    {
                        candidateDTO.getVideo().setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, entry.getValue().getBucketkey()));
                    }
                }
            }
        }
        return electDTO;

    }


    /** 当前用户参加竞选
     * @param candidateDTO 竞选者详情，图片，录音，视频等等
     * @param electId 选举Id
     * @return 更行后的选举信息，以及经验值
     * @throws Exception
     */
    @RequestMapping("/runForElect")
    public @ResponseBody Map<String, Object> runForElect(CandidateDTO candidateDTO,String electId) throws Exception{

        Map<String, Object> result = new HashMap<String, Object>();
        //todo: set session value
        candidateDTO.setSignTime(new Date());

        SessionValue sessionValue = getSessionValue();
        candidateDTO.setId(sessionValue.getId());
        candidateDTO.setName(sessionValue.getUserName());

        Candidate candidate = candidateDTO.buildCandidate();

        //todo:add candidate to elect candidate list,检查此candidate是否已在竞选列表中
        electService.addCandidateToElect(candidate,electId,getUserId());

        ElectDTO electDTO = new ElectDTO(electService.findOne(electId, getUserId()));
        result.put("elect",electDTO);

        //todo:经验值
        ExpLogType runForElect = ExpLogType.RUN_FOR_ELECT;
        if (experienceService.updateNoRepeat(getUserId().toString(), runForElect, electId)) {
            result.put("score", runForElect.getExp());
            result.put("scoreMsg", runForElect.getDesc());
        }
        return  result;
    }

    /** 更新候选人信息
     * @param candidateDTO
     * @param electId
     * @return
     * @throws Exception
     */
    //todo:test and review
    @RequestMapping("/updateCandidate")
    @ResponseBody
    public Map<String,Object> updateCandidate(CandidateDTO candidateDTO,String electId) throws Exception{

        Map<String,Object> result = new HashMap<String, Object>();

        candidateDTO.setSignTime(new Date());

        ElectEntry electEntry = electService.updateCandidate(candidateDTO.buildCandidate(),
                electId,getUserId());

        result.put("elect",electEntry);

        return result;

    }


    /** 当前用户退出某个选举
     * @param electId 选举id
     * @return
     * @throws Exception
     */
    @RequestMapping("/abstain")
    public @ResponseBody Map<String,Object> abstainElect(String electId) throws Exception{

        Map<String,Object> result = new HashMap<String, Object>();
        //todo: get userid from session


        SessionValue sessionValue = getSessionValue();
        String userId = sessionValue.getId();

        ElectEntry electEntry = electService.removeCandidate(electId,userId );


        result.put("elect",new ElectDTO(electEntry));
        //todo: 需要返回被退出的候选人？


        //todo:经验值
        /*ExpLogType abstainFromElect = ExpLogType.ABSTAIN_FROM_ELECT;
        if (experienceService.updateScore(userId, abstainFromElect, electId)) {
            result.put("score", abstainFromElect.getExp());
            result.put("scoreMsg", abstainFromElect.getDesc());
        }*/
        //todo 给所有投票人减去1经验值
        //ExpLogType voterQuit = ExpLogType.VOTER_QUIT;
        /*List<Candidate> candidateList=electEntry.getCandidates();
        for (Candidate candidate:candidateList)
        {
            if (candidate.getUserId().equals(new ObjectId(userId)))
            {
                List<ObjectId> ballots=candidate.getBallots();
                if(ballots!=null && !ballots.isEmpty()) {
                    for (ObjectId ballotUserId : ballots) {
                        experienceService.updateScore(ballotUserId.toString(), voterQuit, electId);
                        if (ballotUserId.equals(userId)) {
                            int nowExp = Integer.parseInt(result.get("score").toString()) - 1;
                            result.put("score", nowExp);
                        }
                    }
                }
                break;
            }
        }*/
        return result;

    }



    @RequestMapping("/upload")
    @ResponseBody
    public List<String>  uploadFile(MultipartRequest request) throws Exception{

        List<String> fileUrls = new ArrayList<String>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            String fileKey = "elect-"+new ObjectId().toString()+file.getOriginalFilename();
            
            
            String extName =FilenameUtils.getExtension(file.getOriginalFilename());
            String path="";
            if(extName.equalsIgnoreCase("amr"))
            {
            	String saveFileKey=new ObjectId().toString()+".mp3";
            	com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, file.getInputStream());
            	path =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
            }
            else
            {
            	  QiniuFileUtils.uploadFile(fileKey,file.getInputStream(),QiniuFileUtils.TYPE_DOCUMENT);
                  path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                  
            }
            
            fileUrls.add(path);

        }

        return fileUrls;




    }

    /** 向候选人投票
     * 要检查此人是否有多余票数
     *
     * @param electId
     * @param candidateId
     * @return
     * @throws Exception
     */
    @RequestMapping("/vote")
    public @ResponseBody Map<String,Object> voteForElect(String electId,String candidateId) throws Exception{
        //

        Map<String,Object> result = new HashMap<String, Object>();
        ElectEntry electEntry = electService.findOne(electId,getUserId());
        //todo :get from session

        SessionValue sessionValue = getSessionValue();
        ObjectId userId =new ObjectId(sessionValue.getId());

        List<Candidate> candidateList = electEntry.getCandidates();

        //检查是否还有多余票可以投
        int votedCount = 0;
        for(Candidate ca : candidateList)
        {
            if(ca.getBallots()!=null){
                for(ObjectId ballotId:ca.getBallots()){
                    if(ballotId.equals(userId)){
                        votedCount++;
                    }
                }
            }

        }
        //如果还可以投票
        if(votedCount<electEntry.getBallotCount()){
            Candidate candidate = null;
            for(Candidate ca : candidateList)
            {
                if(ca.getUserId().equals(new ObjectId(candidateId)))
                {
                    candidate = ca;
                    break;
                }
            }
            if(candidate!=null){
                List<ObjectId> ballotList = candidate.getBallots();
                //如果为空，创建一个
                if(ballotList==null){
                    ballotList = new ArrayList<ObjectId>();
                }

                if(!ballotList.contains(userId)){

                    ballotList.add(userId);
                    //更新ballot
                    candidate.setBallots(ballotList);

                    electEntry = electService.updateCandidateList(candidateList, electId,getUserId());

                    //todo:经验值
                    ExpLogType voteForElect = ExpLogType.VOTE_FOR_ELECT;
                    if (experienceService.updateScore(getUserId().toString(), voteForElect, electId)) {
                        result.put("score", voteForElect.getExp());
                        result.put("scoreMsg", voteForElect.getDesc());
                    }

                    result.put("success", true);
                }
                else{
                    result.put("msg", "已经投过该候选人");
                }


            }
            else
            {
                throw new IllegalParamException("竞选人ID不存在");
            }

        }
        else{
            result.put("msg", "已投满" + electEntry.getBallotCount() + "票");
        }
        result.put("elect", new ElectDTO(electEntry));
        return result;
    }

    ///=========================重构开始==============================================================================
    @RequestMapping("/votePage")
    public String votePage(Model model)
    {
        int role=getSessionValue().getUserRole();
        model.addAttribute("role",role);
        model.addAttribute("userId",getUserId().toString());
        return "vote/vote";
    }

    /**
     * 根据用户名以及角色返回班级列表
     * @return
     */
    @RequestMapping("/getClassList")
    @ResponseBody
    public List<ClassInfoDTO> getClassList()
    {
        return userService.getClassDTOList(getUserId(),getSessionValue().getUserRole());
    }

}
