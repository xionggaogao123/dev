package com.fulaan.reportCard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.db.business.ModuleTimeDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.IndexContentDao;
import com.db.indexPage.IndexPageDao;
import com.db.indexPage.WebHomePageDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.operation.AppNoticeDao;
import com.db.reportCard.*;
import com.db.wrongquestion.ExamTypeDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.count.service.CountService;
import com.fulaan.dto.VideoDTO;
import com.fulaan.extendedcourse.service.ExtendedCourseService;
import com.fulaan.indexpage.dto.IndexContentDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.jiaschool.service.HomeSchoolService;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.fulaan.reportCard.dto.*;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.TestTable;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.HSSFUtils;
import com.fulaan.wrongquestion.dto.ExamTypeDTO;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.GroupEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.IndexContentEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.reportCard.*;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.QiniuFileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by scott on 2017/9/30.
 */
@Service
public class ReportCardNewService {
 

    @Autowired
    private UserService userService;
    @Autowired
    private RedDotService redDotService;

    private GroupExamDetailDao groupExamDetailDao = new GroupExamDetailDao();
    private RecordScoreEvaluateDao recordScoreEvaluateDao = new RecordScoreEvaluateDao();
    private RecordLevelEvaluateDao recordLevelEvaluateDao = new RecordLevelEvaluateDao();
    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();
    private CommunityDao communityDao = new CommunityDao();
    private VirtualUserDao virtualUserDao = new VirtualUserDao();

    private ExamTypeDao examTypeDao = new ExamTypeDao();

    private VirtualAndUserDao virtualAndUserDao = new VirtualAndUserDao();

    private GroupExamUserRecordDao groupExamUserRecordDao = new GroupExamUserRecordDao();

    private GroupExamVersionDao groupExamVersionDao = new GroupExamVersionDao();

    private SubjectClassDao subjectClassDao = new SubjectClassDao();

    private WebHomePageDao webHomePageDao = new WebHomePageDao();

    private AppNoticeDao appNoticeDao = new AppNoticeDao();

    private MemberDao memberDao = new MemberDao();

    private ReportCardSignDao reportCardSignDao = new ReportCardSignDao();

    private VirtualCommunityDao virtualCommunityDao = new VirtualCommunityDao();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private ModuleTimeDao moduleTimeDao=  new ModuleTimeDao();
    
    public ScoreRepresentDao scoreRepresentDao = new ScoreRepresentDao();

    private IndexContentDao indexContentDao = new IndexContentDao();
    
    @Autowired
    private CountService countService;
    
    private MultiGroupExamDetailDao multiGroupExamDetailDao = new MultiGroupExamDetailDao();
    
    private GroupDao groupDao = new GroupDao();
    
    @Autowired
    private ExtendedCourseService extendedCourseService;
    
    @Autowired
    private HomeSchoolService homeSchoolService;
    
    private static final Logger logger = Logger.getLogger(ReportCardNewService.class);
    
 
    
  

    /*public static void main(String[] args) throws Exception {
        ReportCardNewService reportCardService = new ReportCardNewService();*/
        /**
         GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
         detailDTO.setGroupId("59c32cc6670ab23fb82dc4ae");
         detailDTO.setCommunityId("59c32cc5670ab23fb82dc4ac");
         detailDTO.setExamType("59e415a2bf2e7917683d7354");
         detailDTO.setRecordScoreType(2);
         detailDTO.setExamName("单周测试");
         detailDTO.setSubjectId("59b5fc0fbf2e791bb445cdb7");
         detailDTO.setMaxScore(-1);
         detailDTO.setQualifyScore(-1);
         detailDTO.setExcellentScore(-1);
         detailDTO.setExamStrTime("2017-10-20");
         ObjectId userId=new ObjectId("59c32c8c670ab23fb82dc49a");
         reportCardService.saveGroupExamDetail(detailDTO,userId);
         **/
        /**-----------添加学生成绩---------**/
        /**
         List<GroupExamUserRecordDTO> examGroupUserScoreDTOs=new ArrayList<GroupExamUserRecordDTO>();
         GroupExamUserRecordDTO dto1=new GroupExamUserRecordDTO();
         dto1.setId("59e6c86a267564078c9d07be");
         dto1.setScore(-1);
         dto1.setScoreLevel(99);
         dto1.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto1);
         GroupExamUserRecordDTO dto2=new GroupExamUserRecordDTO();
         dto2.setId("59e6c86a267564078c9d07bf");
         dto2.setScore(-1);
         dto2.setScoreLevel(98);
         dto2.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto2);
         GroupExamUserRecordDTO dto3=new GroupExamUserRecordDTO();
         dto3.setId("59e6c86a267564078c9d07c0");
         dto3.setScore(-1);
         dto3.setScoreLevel(90);
         dto3.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto3);
         GroupExamUserRecordDTO dto4=new GroupExamUserRecordDTO();
         dto4.setId("59e6c86a267564078c9d07c1");
         dto4.setScore(-1);
         dto4.setScoreLevel(93);
         dto4.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto4);
         GroupExamUserRecordDTO dto5=new GroupExamUserRecordDTO();
         dto5.setId("59e6c86a267564078c9d07c2");
         dto5.setScore(-1);
         dto5.setScoreLevel(96);
         dto5.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto5);
         int status=2;
         reportCardService.saveRecordExamScore(examGroupUserScoreDTOs,status);
         **/
        /**----------生成版本号--------------**/
        /**
         GroupExamVersionEntry versionEntry=new GroupExamVersionEntry(new ObjectId("59e6c86a267564078c9d07bd"),1);
         GroupExamVersionDao groupExamVersionDao=new GroupExamVersionDao();
         groupExamVersionDao.saveGroupExamVersionEntry(versionEntry);
         **/
   /* }*/
    
    /**
     * 
     *〈简述〉根据成绩单id判断是否存在成绩单
     *〈详细描述〉
     * @author Administrator
     * @param examGroupDetailId
     * @return
     */
    public GroupExamDetailEntry isHaveGroupExam (ObjectId examGroupDetailId) {
        GroupExamDetailEntry  g = null;
        g = groupExamDetailDao.getGroupExamDetailEntry(examGroupDetailId);
        return g;
    }


    /**
     * 签字的功能
     *
     * @param groupExamDetailId
     * @param userId
     */
    public void pushSign(ObjectId groupExamDetailId,
                         ObjectId userId,
                         ObjectId mainUserId
    ) {
        GroupExamUserRecordEntry recordEntry = groupExamUserRecordDao.getUserRecordEntry(groupExamDetailId, userId);
        if (null == recordEntry) {
            GroupExamDetailEntry groupExamDetailEntry = groupExamDetailDao.getEntryById(groupExamDetailId);
            groupExamUserRecordDao.pushSign(groupExamDetailId, userId);
            //增加签字数
            if(reportCardSignDao.getUserRecordEntry(groupExamDetailId, mainUserId)==null){
                groupExamDetailDao.updateSignedCount(groupExamDetailId);
            }
            //groupExamDetailEntry.setSignCount(this.getMyRoleList4(groupExamDetailEntry.getCommunityId(),groupExamDetailEntry.getUserId()));
            //修改总数
            groupExamDetailDao.updateSignCount(groupExamDetailId,this.getMyRoleList4(groupExamDetailEntry.getCommunityId(),groupExamDetailEntry.getUserId()));
            GroupExamUserRecordEntry userRecordEntry=groupExamUserRecordDao.getExamUserRecordEntry(groupExamDetailId,userId);
            if(null!=userRecordEntry) {
                webHomePageDao.updateContactStatus(userRecordEntry.getID(), Constant.THREE, Constant.THREE);
                reportCardSignDao.updateTypeByRecordId(userRecordEntry.getID(),mainUserId);
            }
            IndexContentEntry indexContentEntry = indexContentDao.getEntry(groupExamDetailId);
            if(indexContentEntry!=null){
                List<ObjectId> reList = indexContentEntry.getReaList();
                if(!reList.contains(mainUserId)) {
                    indexContentDao.pushReadList(mainUserId, groupExamDetailId);
                    //红点减一
                    //redDotService.jianRedDot(mainUserId,ApplyTypeEn.repordcard.getType());
                }
            }

        }
    }

    /**
     * 删除成绩单
     *
     * @param id
     */
    public void removeGroupExamDetailEntry(ObjectId id, ObjectId userId) throws Exception {
        GroupExamDetailEntry entry = groupExamDetailDao.getGroupExamDetailEntry(id);
        if (null != entry) {
            if (null != entry.getUserId() && null != userId &&
                    entry.getUserId().toString().equals(userId.toString())) {
                long current=System.currentTimeMillis();
                if(entry.getSubmitTime() >current-24*60*60*1000) {
                    groupExamDetailDao.removeGroupExamDetailEntry(id);
                    groupExamUserRecordDao.updateGroupExamDetailStatus(id, Constant.ONE);
                    webHomePageDao.removeContactList(id);
                    webHomePageDao.removeReportCard(id);
                    //删除首页记录
                    indexPageDao.delEntry(id);
                }else{
                    throw new Exception("已过有效时间!");
                }
            } else {
                throw new Exception("你没有权限删除成绩单！");
            }
        }

    }

    public Map<String,Object> searchReportCardSignList(ObjectId groupExamDetailId){
        Map<String,Object> result = new HashMap<String,Object>();
        List<User> sign = new ArrayList<User>();
        List<User> unSign = new ArrayList<User>();
        List<ReportCardSignEntry> signEntries = reportCardSignDao.getEntries(groupExamDetailId);
        Map<ObjectId,List<Integer>> userTypeMap = new HashMap<ObjectId, List<Integer>>();
        Map<ObjectId,Long> signTime = new HashMap<ObjectId, Long>();
        for(ReportCardSignEntry signEntry:signEntries){
            if(null!=signEntry.getParentId()) {
                signTime.put(signEntry.getParentId(), signEntry.getSignTime());
                if (null != userTypeMap.get(signEntry.getParentId())) {
                    List<Integer> types = userTypeMap.get(signEntry.getParentId());
                    types.add(signEntry.getType());
                    userTypeMap.put(signEntry.getParentId(), types);
                } else {
                    List<Integer> types = new ArrayList<Integer>();
                    types.add(signEntry.getType());
                    userTypeMap.put(signEntry.getParentId(), types);
                }
            }
        }
        Set<ObjectId> signIds = new HashSet<ObjectId>();
        Set<ObjectId> unSignIds = new HashSet<ObjectId>();
        for(Map.Entry<ObjectId,List<Integer>> item:userTypeMap.entrySet()){
            ObjectId mainUserId = item.getKey();
            List<Integer> types = item.getValue();
            boolean flag=true;
            for(int type:types){
                if(type!=Constant.THREE){
                    flag=false;
                }
            }
            if(flag){
                signIds.add(mainUserId);
            }else {
                //unSignIds.add(mainUserId);
            }
        }
        GroupExamDetailEntry groupExamDetailEntry = groupExamDetailDao.getEntryById(groupExamDetailId);
        List<ObjectId> objectIdList =  this.getMyRoleList5(groupExamDetailEntry.getCommunityId(),groupExamDetailEntry.getUserId());
        objectIdList.removeAll(signIds);
        unSignIds.addAll(objectIdList);
        if(signIds.size()>0) {
            setSignValues(signIds,signTime,sign,Constant.ONE,groupExamDetailEntry.getGroupId());
        }
        if(unSignIds.size()>0){
            setSignValues(unSignIds,signTime,unSign,Constant.TWO,groupExamDetailEntry.getGroupId());
        }
        result.put("SignList",sign);
        result.put("SignListNum",sign.size());
        result.put("UnSignList",unSign);
        result.put("UnSignListNum",unSign.size());
        return result;
    }

    public void setSignValues(Set<ObjectId> userIds,Map<ObjectId,Long> signTime,
                              List<User> users,int type,ObjectId groupId){
        Map<ObjectId, UserEntry> signUserEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        objectIdList.addAll(userIds);
        Map<ObjectId,String>  smap = memberDao.getNickNameByUserIds(objectIdList,groupId);
        for(Map.Entry<ObjectId, UserEntry> userItem:signUserEntryMap.entrySet()){
            UserEntry userEntry=userItem.getValue();
            String time=Constant.EMPTY;
            if(type==Constant.ONE){
                time=DateTimeUtils.getLongToStrTimeTwo(signTime.get(userItem.getKey()));
            }
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            String name2 = smap.get(userEntry.getID());
            if(name2!=null){
                name = name2;
            }
            User user=new User(name,
                    name,userEntry.getID().toString(),
                    AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
                    userEntry.getSex(), time);
            users.add(user);
        }
    }


    public void sendGroupExam(ObjectId groupExamDetailId, String showType) {
        groupExamDetailDao.updateGroupExamDetailEntry(groupExamDetailId, Constant.TWO);
        if (showType != null) {
            groupExamDetailDao.updateGroupExamDetailEntryShowType(groupExamDetailId, Integer.valueOf(showType));
        }
        groupExamUserRecordDao.updateGroupExamDetailStatus(groupExamDetailId, Constant.TWO);
        webHomePageDao.updateContactStatus(groupExamDetailId, Constant.FIVE, Constant.TWO);
        webHomePageDao.updateReportCardStatus(groupExamDetailId,Constant.THREE, Constant.TWO);
        GroupExamDetailEntry entry = groupExamDetailDao.getEntryById(groupExamDetailId);
        //添加红点
        redDotService.addThirdList(entry.getID(),entry.getCommunityId(), entry.getUserId(), ApplyTypeEn.repordcard.getType());

        List<GroupExamUserRecordEntry> groupExamUserRecordEntries = groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId);
        List<String>  objectIdList = new ArrayList<String>();
        objectIdList.add(entry.getUserId().toString());
        StringBuffer sb = new StringBuffer();
        for (GroupExamUserRecordEntry entry4 : groupExamUserRecordEntries) {
            objectIdList.add(entry4.getUserId().toString());
            sb.append(entry4.getUserId().toString());
            sb.append("#");
            sb.append(entry4.getID().toString());
            sb.append(",");
        }
        //新首页
        IndexPageDTO dto2 = new IndexPageDTO();
        dto2.setType(CommunityType.reportCard.getType());
        dto2.setUserId(entry.getUserId().toString());
        dto2.setCommunityId(entry.getCommunityId().toString());
        dto2.setContactId(entry.getID().toString());
        dto2.setReceiveIdList(objectIdList);
        IndexPageEntry entry2 = dto2.buildAddEntry();
        indexPageDao.addEntry(entry2);
        String str = entry.getSubjectIds();
        String suid = "59dc8a68bf2e791a140769b4";
        if(str!=null){
            String[] string = str.split(",");
            if(string.length>0){
                suid = string[0];
            }
        }

        SubjectClassEntry subjectClassEntry = subjectClassDao.getEntry(new ObjectId(suid));
        String name = "其他";
        if(subjectClassEntry!=null){
            name = subjectClassEntry.getName();
        }
        CommunityEntry communityEntry = communityDao.findCommunityByObjectId(entry.getGroupId());
        String groupName = "";
        if(communityEntry!=null){
            groupName = communityEntry.getCommunityName();
        }
        IndexContentDTO indexContentDTO = new IndexContentDTO(
                name,
                entry.getExamName(),
                "老师发了一份成绩单，赶快查看吧！",
                new ArrayList<VideoDTO>(),
                new ArrayList<Attachement>(),
                new ArrayList<Attachement>(),
                new ArrayList<Attachement>(),
                groupName,
                sb.toString());
        IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(entry.getUserId().toString(),suid, entry.getGroupId().toString(),entry.getCommunityId().toString(),3);
        indexContentEntry.setReadList(new ArrayList<ObjectId>());
        indexContentEntry.setContactId(entry.getID());
        indexContentEntry.setContactType(8);
        indexContentEntry.setAllCount(entry.getSignCount());
        indexContentDao.addEntry(indexContentEntry);
        //成绩单发送记录
        moduleTimeDao.addEntry(entry.getUserId(),ApplyTypeEn.repordcard.getType(),entry.getCommunityId());
        PictureRunNable.addTongzhi(entry.getCommunityId().toString(), entry.getUserId().toString(), 6,entry.getExamName());
    }

    public GroupExamVersionDTO getExamGroupVersion(ObjectId groupExamDetailId) throws Exception {
        GroupExamVersionEntry entry = groupExamVersionDao.getVersionByGroupExamDetailId(groupExamDetailId);
        if (null != entry) {
            return new GroupExamVersionDTO(entry);
        } else {
            throw new Exception("传入的考试参数有误");
        }
    }
    
    public List<GroupExamUserRecordDTO> tran(List<GroupExamUserRecordDTO> recordExamScoreDTOs, GroupExamDetailEntry detailEntry) {
        int q = recordExamScoreDTOs.get(0).getScoreStr().split(",").length;
        
        for (int i = 0; i< q;i++) {
            for(GroupExamUserRecordDTO g : recordExamScoreDTOs) {
                String[] scoreArr = g.getScoreStr().split(",");
                String[] scoreLevelArr = g.getScoreLevelStr().split(",");
                g.setScore(Double.valueOf(scoreArr[i]));
                g.setScoreLevel(Integer.valueOf(scoreLevelArr[i]));
            }
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScore() > o2.getScore()) {
                            return -1;
                        } else if (o1.getScore() == o2.getScore()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            } else {
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScoreLevel() > o2.getScoreLevel()) {
                            return -1;
                        } else if (o1.getScoreLevel() == o2.getScoreLevel()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            }
            int rank = 1;
            //如果未填写或者缺考排名给-1
            for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
                if (detailEntry.getRecordScoreType() == Constant.ONE) {
                    if (dto.getScore() != -1 && dto.getScore() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        
                        rank++;
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                 
                    }
                } else {
                    if (dto.getScoreLevel() != -1 && dto.getScoreLevel() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        rank++;
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                    }
                }
                
                
            }
        }
             return    recordExamScoreDTOs;
    }


    public List<GroupExamUserRecordDTO> searchRecordStudentScores(ObjectId groupExamDetailId) {
        List<GroupExamUserRecordDTO> recordExamScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(groupExamDetailId);
        if(detailEntry==null) {
            return recordExamScoreDTOs;
        }
   
        final List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId);
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for (GroupExamUserRecordEntry recordEntry : recordEntries) {
            userIds.add(recordEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        Map<ObjectId, NewVersionCommunityBindEntry> bindUserMap = new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        if (userIds.size() > 0) {
            userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
            bindUserMap = newVersionCommunityBindDao.getUserEntryMapByCondition(
                    recordEntries.get(0).getCommunityId(), new ArrayList<ObjectId>(userIds));
        }
        boolean flag = false;
        for (GroupExamUserRecordEntry recordEntry : recordEntries) {
            GroupExamUserRecordDTO userRecordDTO = new GroupExamUserRecordDTO(recordEntry,1);
            NewVersionCommunityBindEntry
                    entry = bindUserMap.get(recordEntry.getUserId());
            if (null == entry) {
                flag = true;
            } else {
                userRecordDTO.setUserNumber(entry.getNumber());
                if (StringUtils.isNotBlank(entry.getThirdName())) {
                    userRecordDTO.setUserName(entry.getThirdName());
                } else {
                    flag = true;
                }
            }
            if (flag) {
                UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                if(null != userEntry){
                    userRecordDTO.setUserName(
                            StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                }
                flag = false;
            }
            VirtualUserEntry virtualUserEntry = virtualUserDao.getIrVirtualUserByUserId(recordEntry.getCommunityId(),recordEntry.getUserId());
            //如果查不到实时的查过版本的
            if (virtualUserEntry == null) {
                virtualUserEntry = virtualUserDao.getVirtualUserByUserId(recordEntry.getUserId());
            }
            
            if(null != virtualUserEntry){
                userRecordDTO.setUserNumber(virtualUserEntry.getUserNumber());
                userRecordDTO.setUserName(virtualUserEntry.getUserName());
            }
       
            recordExamScoreDTOs.add(userRecordDTO);
        }
        
        int q = recordExamScoreDTOs.get(0).getScoreStr().split(",").length;
        
        for (int i = 0; i< q;i++) {
            for(GroupExamUserRecordDTO g : recordExamScoreDTOs) {
                String[] scoreArr = g.getScoreStr().split(",");
                String[] scoreLevelArr = g.getScoreLevelStr().split(",");
                g.setScore(Double.valueOf(scoreArr[i]));
                g.setScoreLevel(Integer.valueOf(scoreLevelArr[i]));
            }
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScore() > o2.getScore()) {
                            return -1;
                        } else if (o1.getScore() == o2.getScore()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            } else {
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScoreLevel() > o2.getScoreLevel()) {
                            return -1;
                        } else if (o1.getScoreLevel() == o2.getScoreLevel()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            }
            int rank = 1;
            int in = 0;
            //如果未填写或者缺考排名给-1
            for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
                if (detailEntry.getRecordScoreType() == Constant.ONE) {
                    if (dto.getScore() != -1 && dto.getScore() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        if (in < recordExamScoreDTOs.size() -1) {
                            if (new BigDecimal(recordExamScoreDTOs.get(in+1).getScore()).compareTo(new BigDecimal(recordExamScoreDTOs.get(in).getScore())) != 0) {
                                rank++;
                            }
                        }
                           
                        
                        
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                 
                    }
                } else {
                    if (dto.getScoreLevel() != -1 && dto.getScoreLevel() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        if (in < recordExamScoreDTOs.size() -1) {
                            if (new BigDecimal(recordExamScoreDTOs.get(in+1).getScoreLevel()).compareTo(new BigDecimal(recordExamScoreDTOs.get(in).getScoreLevel())) != 0) {
                                rank++;
                            }
                        }
                        
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                    }
                }
                in++;
                
            }
            
            for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
                String bc = dto.getBc();
                if (StringUtils.isNotBlank(bc)) {
                  if (!("-1".equals(bc)||("-2".equals(bc)))) {
                      dto.setRankStr(bc);
                  } else {
                      dto.setRankStr("-1");
                  } 
                } 
            }
        }
        
        
        

        
        Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
            @Override
            public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                int result=0;
                if(StringUtils.isNotEmpty(o1.getUserNumber())&&
                        StringUtils.isNotEmpty(o1.getUserName()) && StringUtils.isNotEmpty(o2.getUserNumber())&&
                        StringUtils.isNotEmpty(o2.getUserName())) {
                    result = getCompareResult(o1.getUserNumber(), o2.getUserNumber());
                    if (result == 0) {
                        result = getCompareResult(o1.getUserName(), o2.getUserName());
                    }
                }
                return result;
            }
        });
        
        Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
            @Override
            public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                int result=0;
                if(o1.getSort() < o2.getSort()) {
                    result = -1;
                } else {
                    result = 1;
                }
                return result;
            }
        });
        
        
        return recordExamScoreDTOs;
    }
    

    /**
     * 
     *〈简述〉获取成绩和排行
     *〈详细描述〉
     * @author Administrator
     * @param groupExamDetailId 成绩单详情id
     * @return
     */
    public List<GroupExamUserRecordDTO> getGroupExamUserRecord(ObjectId groupExamDetailId, ObjectId userId) {
        GroupExamDetailEntry oldEntry = groupExamDetailDao.getGroupExamDetailEntry(groupExamDetailId);
        List<GroupExamUserRecordDTO> recordExamScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
       
        final List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId,-1, -1, -1, 1);
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for (GroupExamUserRecordEntry recordEntry : recordEntries) {
            userIds.add(recordEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        Map<ObjectId, NewVersionCommunityBindEntry> bindUserMap = new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        if (userIds.size() > 0) {
            userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
            bindUserMap = newVersionCommunityBindDao.getUserEntryMapByCondition(
                    recordEntries.get(0).getCommunityId(), new ArrayList<ObjectId>(userIds));
        }
        boolean flag = false;
        for (GroupExamUserRecordEntry recordEntry : recordEntries) {
            GroupExamUserRecordDTO userRecordDTO = new GroupExamUserRecordDTO(recordEntry);
            NewVersionCommunityBindEntry
                    entry = bindUserMap.get(recordEntry.getUserId());
            if (null == entry) {
                flag = true;
            } else {
                userRecordDTO.setUserNumber(entry.getNumber());
                if (StringUtils.isNotBlank(entry.getThirdName())) {
                    userRecordDTO.setUserName(entry.getThirdName());
                } else {
                    flag = true;
                }
            }
            if (flag) {
                UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                if(null != userEntry){
                    userRecordDTO.setUserName(
                            StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                }
                flag = false;
            }
            //VirtualUserEntry virtualUserEntry = virtualUserDao.getVirtualUserByUserId(recordEntry.getUserId());VirtualUserEntry virtualUserEntry = virtualUserDao.getIrVirtualUserByUserId(recordEntry.getCommunityId(),recordEntry.getUserId());
            VirtualUserEntry virtualUserEntry = virtualUserDao.getIrVirtualUserByUserId(recordEntry.getCommunityId(),recordEntry.getUserId());
            //如果查不到实时的查过版本的
            if (virtualUserEntry == null) {
                virtualUserEntry = virtualUserDao.getVirtualUserByUserId(recordEntry.getUserId());
            }
            if(null != virtualUserEntry){
                userRecordDTO.setUserNumber(virtualUserEntry.getUserNumber());
                userRecordDTO.setUserName(virtualUserEntry.getUserName());
            }
            recordExamScoreDTOs.add(userRecordDTO);
        }
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(groupExamDetailId);
        //List<GroupExamUserRecordDTO> list = new ArrayList<GroupExamUserRecordDTO>();
        //注释掉部分为同分排名一样
        /*if (detailEntry.getRecordScoreType() == 1) {
          //分数排序
            Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                @Override
                public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                    int result=0;
                    if (o1.getScore() < o2.getScore()) {
                        result = 1;
                    } else {
                        result = -1;
                    }
                    return result;
                }
            });
            
            for (int i = 0; i < recordExamScoreDTOs.size(); i++) {
                if (i == 0) {
                    recordExamScoreDTOs.get(i).setRank(i+1);
                } else {
                    if (recordExamScoreDTOs.get(i).getScore() == recordExamScoreDTOs.get(i-1).getScore()) {
                        recordExamScoreDTOs.get(i).setRank(recordExamScoreDTOs.get(i-1).getRank());
                    } else {
                        recordExamScoreDTOs.get(i).setRank(recordExamScoreDTOs.get(i-1).getRank()+1);
                    }
                }
                list.add(recordExamScoreDTOs.get(i));
            }
        } else {
          //等第排序
            Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                @Override
                public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                    int result=0;
                    if (o1.getScoreLevel() < o2.getScoreLevel()) {
                        result = 1;
                    } else {
                        result = -1;
                    }
                    return result;
                }
            });
            
            for (int i = 0; i < recordExamScoreDTOs.size(); i++) {
                if (i == 0) {
                    recordExamScoreDTOs.get(i).setRank(i+1);
                } else {
                    if (recordExamScoreDTOs.get(i).getScoreLevel() == recordExamScoreDTOs.get(i-1).getScoreLevel()) {
                        recordExamScoreDTOs.get(i).setRank(recordExamScoreDTOs.get(i-1).getRank());
                    } else {
                        recordExamScoreDTOs.get(i).setRank(recordExamScoreDTOs.get(i-1).getRank()+1);
                    }
                }
                list.add(recordExamScoreDTOs.get(i));
            }
        }*/
        
        
        if (detailEntry.getRecordScoreType() == Constant.ONE) {
            Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                @Override
                public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                    if (o1.getScore() > o2.getScore()) {
                        return -1;
                    } else if (o1.getScore() == o2.getScore()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        } else {
            Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                @Override
                public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                    if (o1.getScoreLevel() > o2.getScoreLevel()) {
                        return -1;
                    } else if (o1.getScoreLevel() == o2.getScoreLevel()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
        int rank = 1;
        //如果未填写或者缺考排名给-1
        for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                if (dto.getScore() != -1 && dto.getScore() != -2) {
                    dto.setRank(rank);
                    rank++;
                } else {
                    dto.setRank(-1);
                }
            } else {
                if (dto.getScoreLevel() != -1 && dto.getScoreLevel() != -2) {
                    dto.setRank(rank);
                    rank++;
                } else {
                    dto.setRank(-1);
                }
            }
            
            
        }
        
        /*recordExamScoreDTOs.clear();
        recordExamScoreDTOs.addAll(list);*/
        
        Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
            @Override
            public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                int result=0;
                if(StringUtils.isNotEmpty(o1.getUserNumber())&&
                        StringUtils.isNotEmpty(o1.getUserName()) && StringUtils.isNotEmpty(o2.getUserNumber())&&
                        StringUtils.isNotEmpty(o2.getUserName())) {
                    result = getCompareResult(o1.getUserNumber(), o2.getUserNumber());
                    if (result == 0) {
                        result = getCompareResult(o1.getUserName(), o2.getUserName());
                    }
                }
                return result;
            }
        });

        
      //个人
        if (oldEntry.getShowType() == 0 && !oldEntry.getUserId().equals(userId)) {
            List<GroupExamUserRecordDTO> listt = new ArrayList<GroupExamUserRecordDTO>();
            Map<ObjectId,NewVersionCommunityBindEntry> map = newVersionCommunityBindDao.getCommunityBindMap(oldEntry.getCommunityId(), userId);
            for (ObjectId id : map.keySet()) {
                for (GroupExamUserRecordDTO g : recordExamScoreDTOs) {
                    if (id.equals(new ObjectId(g.getUserId()))) {
                        listt.add(g);
                    }
                }
            }
            recordExamScoreDTOs.clear();
            recordExamScoreDTOs.addAll(listt);
        } 
        
        return recordExamScoreDTOs;
    }

    public int getCompareResult(String itemOne, String itemTwo) {
        int result = 0;
        if (itemOne.length() > itemTwo.length()) {
            result = -1;
        } else if (itemOne.length() < itemTwo.length()) {
            result = 1;
        } else {
            int length = itemOne.length();
            for (int i = 0; i < length; i++) {
                if (itemOne.charAt(i) > itemTwo.charAt(i)) {
                    result = -1;
                } else if (itemOne.charAt(i) < itemTwo.charAt(i)) {
                    result = 1;
                }
            }
        }
        return result;
    }

    public int countReceiveExams(
            String subjectId, String examType, int status,
            ObjectId userId
    ) {
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        //return groupExamUserRecordDao.countStudentReceivedEntries(suId, examTypeId, status, userId);
        List<ObjectId> oids = new ArrayList<ObjectId>();
        oids.add(userId);
        List<ObjectId> users = this.getMyChildList(oids);
        return groupExamUserRecordDao.countNewStudentReceivedEntries(suId, examTypeId, status, users);
    }
    //由绑定孩子列表获得虚拟孩子列表
    public List<ObjectId> getMyChildList(List<ObjectId> objectIds){
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        Set<ObjectId> set = new HashSet<ObjectId>();
        List<ObjectId> objectIdList1 = virtualAndUserDao.getEntryListByCommunityId(objectIds);
        set.addAll(objectIdList1);
        set.addAll(objectIds);
        objectIdList.addAll(set);
        return objectIdList;
    }
    /**
     * 获取我接受的成绩单列表(学生)
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupExamDetailDTO> getReceiveExams(
            String subjectId, String examType, int status,
            ObjectId userId, int page, int pageSize
    ) {
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        /*List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getStudentReceivedEntries(
                suId, examTypeId, status, userId, page, pageSize
        );*/
        List<ObjectId> oids = new ArrayList<ObjectId>();
        oids.add(userId);
        List<ObjectId> users = this.getMyChildList(oids);
        List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getStudentIdListReceivedEntries(
                suId, examTypeId, status, users, page, pageSize
        );
        return getGroupExamDetailDtos(recordEntries);
    }

    public List<GroupExamDetailDTO> getGroupExamDetailDtos(List<GroupExamUserRecordEntry> recordEntries) {
        List<GroupExamDetailDTO> groupExamDetailDTOs = new ArrayList<GroupExamDetailDTO>();
        if (recordEntries.size() > 0) {
            Set<ObjectId> groupExamIds = new HashSet<ObjectId>();
            Set<ObjectId> uIds = new HashSet<ObjectId>();
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            Set<ObjectId> subjectIds = new HashSet<ObjectId>();
            Set<ObjectId> examTypeIds = new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry
                    entry : recordEntries) {
                groupExamIds.add(entry.getGroupExamDetailId());
                uIds.add(entry.getUserId());
                communityIds.add(entry.getCommunityId());
                if (entry.getSubjectId() != null) {
                    subjectIds.add(entry.getSubjectId());
                } else {
                    List<String> l = Arrays.asList(entry.getSubjectIds().split(","));
                    for (String s : l) {
                        if (StringUtils.isNotEmpty(s)) {
                            subjectIds.add(new ObjectId(s));
                        }
                        
                    }
                }
                
                
                if (null != entry.getExamType()) {
                    examTypeIds.add(entry.getExamType());
                }
            }
            Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(uIds, Constant.FIELDS);
            Map<ObjectId, VirtualUserEntry> virtualUserEntryMap = virtualUserDao.getVirtualUserMap(new ArrayList<ObjectId>(uIds));
            Map<ObjectId, GroupExamDetailEntry> examDetailEntryMap = groupExamDetailDao.getGroupExamDetailMap(new ArrayList<ObjectId>(groupExamIds));
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            Map<ObjectId, ExamTypeEntry> examTypeEntryMap = new HashMap<ObjectId, ExamTypeEntry>();
            if (examTypeIds.size() > 0) {
                examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
            }
            List<ObjectId> mainUserIds = new ArrayList<ObjectId>();
            Map<ObjectId, UserEntry> mainUserEntryMap = new HashMap<ObjectId, UserEntry>();
            for (Map.Entry<ObjectId, GroupExamDetailEntry>
                    detailItem : examDetailEntryMap.entrySet()) {
                mainUserIds.add(detailItem.getValue().getUserId());
            }
            if (mainUserIds.size() > 0) {
                mainUserEntryMap = userService.getUserEntryMap(mainUserIds, Constant.FIELDS);
            }
            for (GroupExamUserRecordEntry recordEntry : recordEntries) {
                ObjectId groupExamDetailId = recordEntry.getGroupExamDetailId();
                GroupExamDetailEntry detailEntry = examDetailEntryMap.get(groupExamDetailId);
                if (null != detailEntry) {
                    GroupExamDetailDTO detailDTO = new GroupExamDetailDTO(detailEntry);
                    detailDTO.setShowType(detailEntry.getShowType());
                    UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                    VirtualUserEntry virtualUserEntry = virtualUserEntryMap.get(recordEntry.getUserId());
                    if (null != userEntry) {
                        detailDTO.setChildUserName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                    }else if(null!=virtualUserEntry){
                        detailDTO.setChildUserName(StringUtils.isNotBlank(virtualUserEntry.getUserName())?virtualUserEntry.getUserName():Constant.EMPTY);
                    }
                    detailDTO.setChildUserId(recordEntry.getUserId().toString());
                    CommunityEntry communityEntry = communityEntryMap.get(recordEntry.getCommunityId());
                    if (null != communityEntry) {
                        detailDTO.setGroupName(communityEntry.getCommunityName());
                    }
                    /*SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(recordEntry.getSubjectId());
                    if (null != subjectClassEntry) {
                        detailDTO.setSubjectName(subjectClassEntry.getName());
                    }*/
                    
                    if (recordEntry.getSubjectId() != null) {
                        SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(recordEntry.getSubjectId());
                        if (null != subjectClassEntry) {
                            detailDTO.setSubjectName(subjectClassEntry.getName());
                        }
                    } else {
                        String str = recordEntry.getSubjectIds();
                        String[] sArry = str.split(",");
                        StringBuffer subjectName = new StringBuffer();
                        for (int i = 0;i < sArry.length;i++) {
                            SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(new ObjectId(sArry[i]));
                            if (null != subjectClassEntry) {
                                subjectName.append(subjectClassEntry.getName());
                                if (i < sArry.length-1) {
                                    subjectName.append(",");
                                }
                                
                            }
                            
                        }
                        detailDTO.setSubjectName(subjectName.toString());
                    }
                    
                    
                    String subjectIdStr = recordEntry.getSubjectIds();
                    if (StringUtils.isNotBlank(subjectIdStr)) {
                        List<String> subjectIdList = Arrays.asList(subjectIdStr.split(","));
                        List<String> subjectNameList = new ArrayList<String>();
                        for (String s : subjectIdList) {
                            SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(new ObjectId(s));
                            if (null != subjectClassEntry) {
                                subjectNameList.add(subjectClassEntry.getName());
                            }
                            
                            
                        }
                        if (CollectionUtils.isNotEmpty(subjectNameList)) {
                            detailDTO.setSubjectNameList(subjectNameList);
                        }
                    }
                    
                    if (null != recordEntry.getExamType()) {
                        ExamTypeEntry examTypeEntry = examTypeEntryMap.get(recordEntry.getExamType());
                        if (null != examTypeEntry) {
                            detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
                        }
                    }
 
                    String scoreStr = recordEntry.getScoreStr();
                    String scoreLevelStr = recordEntry.getScoreLevelStr();
                    if (StringUtils.isNotBlank(scoreStr)) {
                        detailDTO.setScoreList(Arrays.asList(scoreStr.split(",")));
                    }
                    if (StringUtils.isNotBlank(scoreLevelStr)) {
                        detailDTO.setScoreLevelList(Arrays.asList(scoreLevelStr.split(",")));
                    }
            
                    try {
                        detailDTO.setScore(recordEntry.getScore());
                        detailDTO.setScoreLevel(recordEntry.getScoreLevel());
                    } catch (Exception e) {
                        // TODO: handle exception
                        logger.error("error", e);
                    }
                        
                   
                    
                    
                    
                     
                    UserEntry mainUserEntry = mainUserEntryMap.get(detailEntry.getUserId());
                    if (null != mainUserEntry) {
                        detailDTO.setUserName(StringUtils.isNotEmpty(mainUserEntry.getNickName())?mainUserEntry.getNickName():mainUserEntry.getUserName());
                    }
                    detailDTO.setStatus(recordEntry.getStatus());
                    detailDTO.setSingleScoreId(recordEntry.getID().toString());
                    groupExamDetailDTOs.add(detailDTO);
                }
            }
        }
        return groupExamDetailDTOs;
    }


    /**
     * 获取我接受的成绩单列表(家长)
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String, Object> getParentReceivedGroupExamDetailDTOs(
            String subjectId, String examType, int status,
            ObjectId userId, int page, int pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getEntriesByMainUserId(userId);
        for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
            userIds.add(bindEntry.getUserId());
        }
        List<GroupExamUserRecordEntry> userRecordEntries = groupExamUserRecordDao.getParentReceivedEntries(
                suId, examTypeId, status, userId, new ArrayList<ObjectId>(userIds), page, pageSize
        );
        int count = groupExamUserRecordDao.countParentReceivedEntries(suId, examTypeId, status, userId, new ArrayList<ObjectId>(userIds));
        List<GroupExamDetailDTO> groupExamDetailDTOs = getGroupExamDetailDtos(userRecordEntries);
        retMap.put("list", groupExamDetailDTOs);
        retMap.put("count", count);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.repordcard.getType());
        return retMap;
    }


    public int countMySendGroupExamDetailDTOs(String subjectId, String examType, int status,
                                              ObjectId userId) {
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        return groupExamDetailDao.countMySendGroupExamDetailEntries(suId, examTypeId, status, userId);
    }

    /**
     * 获取我发出的成绩单列表
     *
     * @param userId
     * @return
     */
    public List<GroupExamDetailDTO> getMySendGroupExamDetailDTOs(
            String subjectId, String examType, int status,
            ObjectId userId, int page, int pageSize) {
        List<GroupExamDetailDTO> groupExamDetailDTOs = new ArrayList<GroupExamDetailDTO>();
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        List<GroupExamDetailEntry> entries = groupExamDetailDao.getMySendGroupExamDetailEntries(
                suId, examTypeId, status, userId,
                page, pageSize);
        Set<ObjectId> communityIds = new HashSet<ObjectId>();
        Set<ObjectId> examTypeIds = new HashSet<ObjectId>();
        Set<ObjectId> subjectIds = new HashSet<ObjectId>();
        for (GroupExamDetailEntry examDetailEntry : entries) {
            communityIds.add(examDetailEntry.getCommunityId());
            if (null != examDetailEntry.getExamType()) {
                examTypeIds.add(examDetailEntry.getExamType());
            }
            subjectIds.add(examDetailEntry.getSubjectId());
        }
        Map<ObjectId, CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));
        Map<ObjectId, ExamTypeEntry> examTypeEntryMap = new HashMap<ObjectId, ExamTypeEntry>();
        if (examTypeIds.size() > 0) {
            examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
        }
        Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
        for (GroupExamDetailEntry examDetailEntry : entries) {
            GroupExamDetailDTO detailDTO = new GroupExamDetailDTO(examDetailEntry);
            detailDTO.setShowType(examDetailEntry.getShowType());
            detailDTO.setUnSignCount(detailDTO.getSignCount() - detailDTO.getSignedCount());
            CommunityEntry communityEntry = communityEntryMap.get(examDetailEntry.getCommunityId());
            if (null != communityEntry) {
                detailDTO.setGroupName(communityEntry.getCommunityName());
            }
            if (null != examDetailEntry.getExamType()) {
                ExamTypeEntry examTypeEntry = examTypeEntryMap.get(examDetailEntry.getExamType());
                if (null != examTypeEntry) {
                    detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
                }
            }
            SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(examDetailEntry.getSubjectId());
            if (null != subjectClassEntry) {
                detailDTO.setSubjectName(subjectClassEntry.getName());
            }
            if (examDetailEntry.getRecordScoreType() == Constant.ONE) {
                RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(examDetailEntry.getID());
                if (null != evaluateEntry) {
                    detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                }
            } else {
                RecordLevelEvaluateEntry levelEvaluateEntry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(examDetailEntry.getID());
                if (null != levelEvaluateEntry) {
                    detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                }
            }
            groupExamDetailDTOs.add(detailDTO);
        }
        return groupExamDetailDTOs;
    }
    
    /**
     * 
     *〈简述〉是否有虚拟学生
     *〈详细描述〉
     * @author Administrator
     * @param groupExamDetailId 成绩单id
     * @return
     */
    public boolean isHaveRecordEntries(String communityId) {
        boolean flag = true;
        List<VirtualUserEntry> virtualUserEntries=virtualUserDao.getAllVirtualUsers(new ObjectId(communityId));
        if (CollectionUtils.isEmpty(virtualUserEntries)) {
            flag = false;
        }
        return flag;
    }

    /**
     * 保存新建的考试
     *
     * @param dto
     * @param userId
     * @throws Exception
     */
    public String saveGroupExamDetail(GroupExamDetailDTO dto, ObjectId userId) throws Exception {

        String id = dto.getId();
        dto.setUserId(userId.toString());
        dto.setSignedCount(Constant.ZERO);
        dto.setSignCount(this.getMyRoleList4(new ObjectId(dto.getCommunityId()),new ObjectId(dto.getUserId())));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long examTime = dateFormat.parse(dto.getExamStrTime()).getTime();
        dto.setExamTime(examTime);
        if (StringUtils.isEmpty(id)) {
            String communityId = dto.getCommunityId();
            Set<ObjectId> userIds = new LinkedHashSet<ObjectId>();
            Map<ObjectId,ObjectId> userMainIds=new HashMap<ObjectId, ObjectId>();
            List<NewVersionCommunityBindEntry> entries
                    = newVersionCommunityBindDao.getStudentIdListByCommunityId(new ObjectId(communityId));
            for (NewVersionCommunityBindEntry bindEntry : entries) {
//                userIds.add(bindEntry.getUserId());
                userMainIds.put(bindEntry.getUserId(),bindEntry.getMainUserId());
            }
            List<VirtualUserEntry> virtualUserEntries=virtualUserDao.getAllVirtualUsers(new ObjectId(communityId));
            for (VirtualUserEntry virtualUserEntry : virtualUserEntries) {
                userIds.add(virtualUserEntry.getUserId());
            }


            ObjectId groupExamDetailId = groupExamDetailDao.saveGroupExamDetailEntry(dto.buildEntry());
            List<GroupExamUserRecordEntry> userRecordEntries = new ArrayList<GroupExamUserRecordEntry>();
            if (dto.getIsFromPt() == 1) {
                List<GroupExamUserRecordEntry> userRecordEntriess = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(dto.getMultiId()), new ObjectId(dto.getCommunityId()));
                
                for (GroupExamUserRecordEntry g : userRecordEntriess) {
                    String[] scoreStrOld = g.getScoreStr().split(",");
                    String[] scoreLevelStrOld = g.getScoreLevelStr().split(",");
                    String scoreStrNew = "";
                    String scoreLevelStrNew = "";
                    String bc = "";
                    String xc = "";
                    String[] ss = dto.getSubjectIds().split(",");
                    List<String> ssList = Arrays.asList(ss);
                    String[] ss1 = g.getSubjectIds().split(",");
                    String[] bcc = g.getBc().split(",");
                    String[] xcc = g.getXc().split(",");
                    for (int i = 0; i<ss1.length; i++) {
                        if (ssList.contains(ss1[i])) {
                            scoreStrNew = scoreStrNew + scoreStrOld[i] + ",";
                            scoreLevelStrNew = scoreLevelStrNew + scoreLevelStrOld[i] + ",";
                            bc = bc + bcc[i] + ",";
                            xc = xc + xcc[i] + ",";
                        }
                    }
                    if (ss.length >1) {
                        scoreStrNew = scoreStrNew + scoreStrOld[ss1.length] + ",";
                        scoreLevelStrNew = scoreLevelStrNew + scoreLevelStrOld[ss1.length] + ",";
                        bc = bc + bcc[ss1.length] + ",";
                        xc = xc + xcc[ss1.length] + ",";
                    }
                    GroupExamUserRecordEntry eee = new GroupExamUserRecordEntry(
                        groupExamDetailId,
                        userId,
                        g.getUserId(),
                        g.getGroupId(),
                        g.getExamType(),
                        dto.getSubjectIds(),
                        g.getCommunityId(),
                        scoreStrNew,
                        scoreLevelStrNew,
                        0,
                        Constant.ZERO,
                        g.getRankStr(),
                        g.getSort(),
                        bc,
                        xc
                        );
                    userRecordEntries.add(eee);
                    //GroupExamUserRecordDTO d = new GroupExamUserRecordDTO(eee);
                    //listGe.add(d);
                }
                //this.tranEvaluate(listGe, groupExamDetailDao.getEntryById(groupExamDetailId), groupExamDetailId.toString());
            } 
            
            int k = 0;
            for (ObjectId uId : userIds) {
                if (StringUtils.isNotEmpty(dto.getSubjectId())) {
                    userRecordEntries.add(new GroupExamUserRecordEntry(
                        groupExamDetailId,
                        userId,
                        uId,
                        StringUtils.isNotEmpty(dto.getGroupId()) ? new ObjectId(dto.getGroupId()) : null,
                        StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,
                        StringUtils.isNotEmpty(dto.getSubjectId()) ? new ObjectId(dto.getSubjectId()) : null,
                        StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                        -2D,
                        -2,
                        0,
                        Constant.ZERO,
                        k
                        ));
                } else {
                    int i;
                    if(dto.getSubjectIds().split(",").length==1) {
                        i = 1;
                    } else {
                        i = dto.getSubjectIds().split(",").length+1;
                    }
                    StringBuffer scoreStr = new StringBuffer();
                    for (int j = 0;j<i;j++) {
                        scoreStr.append("-2,");
                    }
                    
                    StringBuffer scoreStrr = new StringBuffer();
                    for (int j = 0;j<i;j++) {
                        scoreStrr.append("-1,");
                    }
                    if (dto.getIsFromPt() == 1) {
                        
                    } else {
                        userRecordEntries.add(new GroupExamUserRecordEntry(
                            groupExamDetailId,
                            userId,
                            uId,
                            StringUtils.isNotEmpty(dto.getGroupId()) ? new ObjectId(dto.getGroupId()) : null,
                            StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,
                            dto.getSubjectIds(),
                            StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                            scoreStr.toString(),
                            scoreStr.toString(),
                            0,
                            Constant.ZERO,
                            scoreStrr.toString(),
                            k
                            ));
                    }
                    
                }
                k++;
            }
            if (StringUtils.isNotEmpty(dto.getSubjectId())) {
                WebHomePageEntry homePageEntry = new WebHomePageEntry(Constant.FIVE, userId,
                    StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                    groupExamDetailId, StringUtils.isNotEmpty(dto.getSubjectId()) ? new ObjectId(dto.getSubjectId()) : null,
                    null, null, StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,Constant.ZERO
                    );
                    webHomePageDao.saveWebHomeEntry(homePageEntry);
            } else {
                WebHomePageEntry homePageEntry = new WebHomePageEntry(Constant.FIVE, userId,
                    StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                    groupExamDetailId, dto.getSubjectIds(),
                    null, null, StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,Constant.ZERO
                );
                webHomePageDao.saveWebHomeEntry(homePageEntry);
                if (dto.getIsFromPt() == 1) {
                    List<ScoreRepresentEntry> srEntry = scoreRepresentDao.getScoreRepresentAll(new ObjectId(dto.getMultiId()));
                    String[] ss = dto.getSubjectIds().split(",");
                    List<String> sL = Arrays.asList(ss);
                    for (ScoreRepresentEntry e : srEntry) {
                        if (e.getSubjectId() != null) {
                            if (sL.contains(e.getSubjectId().toString())) {
                                ScoreRepresentEntry seNew = new ScoreRepresentEntry(groupExamDetailId, e.getSubjectId(), e.getSubjectName(),e.getMaxScore(), e.getScoreOne(), e.getScoreTwo(), e.getScoreThree(), e.getScoreFour(), e.getScoreFive(), e.getScoreSix(), e.getScoreSeven(), e.getScoreEight(), e.getSort(), e.getRepresentNameType());
                                scoreRepresentDao.saveScoreRepresent(seNew);
                            }
                        }
                        
                        
                        
                    }
                    if (ss.length >1) {
                        ScoreRepresentEntry seNew = new ScoreRepresentEntry(groupExamDetailId, srEntry.get(srEntry.size()-1).getSubjectId(), srEntry.get(srEntry.size()-1).getSubjectName(),srEntry.get(srEntry.size()-1).getMaxScore(), srEntry.get(srEntry.size()-1).getScoreOne(), srEntry.get(srEntry.size()-1).getScoreTwo(), srEntry.get(srEntry.size()-1).getScoreThree(), srEntry.get(srEntry.size()-1).getScoreFour(), srEntry.get(srEntry.size()-1).getScoreFive(), srEntry.get(srEntry.size()-1).getScoreSix(), srEntry.get(srEntry.size()-1).getScoreSeven(), srEntry.get(srEntry.size()-1).getScoreEight(), srEntry.get(srEntry.size()-1).getSort(), srEntry.get(srEntry.size()-1).getRepresentNameType());
                        scoreRepresentDao.saveScoreRepresent(seNew);
                    }
                } else {
                    String[] sa = dto.getSubjectIds().split(",");
                    for (int i = 0;i < sa.length; i++) {
                        SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(sa[i]));
                        scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(groupExamDetailId, new ObjectId(sa[i]), sc.getName(), "100", "100", "90", "89", "80", "79", "60", "59", "0",i, Constant.ONE));
                    }
                    int i = sa.length;
                    if (sa.length>1) {
                        scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(groupExamDetailId,  "总分", multiply(100, i), multiply(100, i), multiply(90, i), multiplyJy(90, i), multiply(80, i), multiplyJy(80, i), multiply(60, i), multiplyJy(60, i), "0", 50,Constant.ONE));
                    }
                }
                
                
            }
            
            for (GroupExamUserRecordEntry userRecordEntry : userRecordEntries) {
                ObjectId recordId = groupExamUserRecordDao.saveGroupExamUserRecord(userRecordEntry);
                WebHomePageEntry pageEntry = new WebHomePageEntry(Constant.THREE, userId,
                        userRecordEntry.getCommunityId(),
                        recordId, userRecordEntry.getSubjectId(),
                        userRecordEntry.getUserId(), groupExamDetailId, userRecordEntry.getExamType(),Constant.ZERO
                );
                webHomePageDao.saveWebHomeEntry(pageEntry);
                ObjectId mainUserId=userMainIds.get(userRecordEntry.getUserId());
                ReportCardSignEntry signEntry =new  ReportCardSignEntry(mainUserId,groupExamDetailId,
                        recordId,Constant.ZERO,System.currentTimeMillis());
                reportCardSignDao.saveEntry(signEntry);
            }
            List<GroupExamUserRecordEntry> list = groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId);
            List<GroupExamUserRecordDTO> listGe = new ArrayList<GroupExamUserRecordDTO>();
            for (GroupExamUserRecordEntry geur : list) {
                listGe.add(new GroupExamUserRecordDTO(geur,1));
            }
            this.tranEvaluate(listGe, groupExamDetailDao.getEntryById(groupExamDetailId), groupExamDetailId.toString());
            //groupExamDetailDao.updateSignCount(groupExamDetailId, userIds.size());
            GroupExamVersionEntry versionEntry = new GroupExamVersionEntry(groupExamDetailId, 1L);
            groupExamVersionDao.saveGroupExamVersionEntry(versionEntry);
            return groupExamDetailId.toString();
        } else {
            GroupExamDetailEntry oldEntry = groupExamDetailDao.getGroupExamDetailEntry(new ObjectId(id));
            if (null != oldEntry) {
                GroupExamDetailEntry entry = dto.buildEntry();
                entry.setID(new ObjectId(id));
                entry.setShowType(oldEntry.getShowType());
                entry.setFsShowType(oldEntry.getFsShowType());
                //entry.setSignCount(this.getMyRoleList4(entry.getCommunityId(),entry.getUserId()));
                entry.setSignedCount(oldEntry.getSignedCount());
                entry.setStatus(oldEntry.getStatus());
                groupExamDetailDao.saveGroupExamDetailEntry(entry);
                groupExamUserRecordDao.updateGroupExamDetailGESC(new ObjectId(id),StringUtils.isNotEmpty(dto.getGroupId()) ? new ObjectId(dto.getGroupId()) : null , StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null, StringUtils.isNotEmpty(dto.getSubjectId()) ? new ObjectId(dto.getSubjectId()) : null, StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null);
                //查询该考试信息
                WebHomePageEntry homePageEntry;
                if (StringUtils.isNotEmpty(dto.getSubjectId())) {
                    homePageEntry = new WebHomePageEntry(Constant.FIVE, userId,
                        StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                        new ObjectId(id), StringUtils.isNotEmpty(dto.getSubjectId()) ? new ObjectId(dto.getSubjectId()) : null,
                        null, null, StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,oldEntry.getStatus()
                );
                } else {
                    homePageEntry = new WebHomePageEntry(Constant.FIVE, userId,
                        StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                        new ObjectId(id), dto.getSubjectIds(),
                        null, null, StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,oldEntry.getStatus()
                );
                }
                
                WebHomePageEntry pageEntry = webHomePageDao.getWebHomePageEntry(new ObjectId(id));
                if (null != pageEntry) {
                    homePageEntry.setID(pageEntry.getID());
                    homePageEntry.setStatus(oldEntry.getStatus());
                }
                webHomePageDao.saveWebHomeEntry(homePageEntry);
                if (StringUtils.isNotEmpty(dto.getSubjectId())) {
                    webHomePageDao.updateWebHomePageEntry(new ObjectId(id), new ObjectId(dto.getSubjectId()));
                    groupExamUserRecordDao.updateGroupExamUserRecord(new ObjectId(id), new ObjectId(dto.getSubjectId()));
                } else {
                    webHomePageDao.updateWebHomePageEntry(new ObjectId(id), dto.getSubjectIds());
                    groupExamUserRecordDao.updateGroupExamUserRecord(new ObjectId(id), dto.getSubjectIds());
                    String[] sa = dto.getSubjectIds().split(",");
                    //增加
                    for (int i = 0;i<sa.length;i++) {
                        int k = scoreRepresentDao.getScoreRepresentCount(new ObjectId(id), new ObjectId(sa[i]));
                        if (k == 0) {
                            int j = scoreRepresentDao.getScoreRepresentTrueCount(new ObjectId(id), new ObjectId(sa[i]));
                            if (j != 0) {
                                scoreRepresentDao.updateIsr(new ObjectId(id), new ObjectId(sa[i]));
                            } else {
                                SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(sa[i]));
                                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(id), new ObjectId(sa[i]), sc.getName(), "100", "100", "90", "89", "80", "79", "60", "59", "0",i ,Constant.ONE));
                            }
                            
                        }
                        
                    }
                    //删除  以及总分更新
                    List<ScoreRepresentEntry> list = scoreRepresentDao.getScoreRepresentWithgidnM(new ObjectId(id));
                    if (CollectionUtils.isNotEmpty(list)) {
                        List<String> l = Arrays.asList(sa);
                        List<String> oList = new ArrayList<String>();
                        for (ScoreRepresentEntry s : list) {
                             oList.add(s.getSubjectId().toString());
                        }
                        for (String s : oList) {
                            if (!l.contains(s)) {
                                scoreRepresentDao.updateIsrOne(new ObjectId(id), new ObjectId(s));
                            }
                        }
                        if (l.size() != list.size()) {
                            scoreRepresentDao.updateScoreRepresentWithgidoM(new ObjectId(id));
                            int i = l.size();
                            scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(id),  "总分", multiply(100, i), multiply(100, i), multiply(90, i), multiplyJy(90, i), multiply(80, i), multiplyJy(80, i), multiply(60, i), multiplyJy(60, i), "0", 50,Constant.ONE));
                        }
                    }
                }
                
            }
            return id;
        }
    }
    
    public String multiply(int num, int n) {
        BigDecimal bNum = new BigDecimal(num);
        BigDecimal bN = new BigDecimal(n);
        BigDecimal mul = bNum.multiply(bN);
        int i = mul.intValue();
        return String.valueOf(i);
    }
    
    public String multiplyJy(int num, int n) {
        BigDecimal bNum = new BigDecimal(num);
        BigDecimal bN = new BigDecimal(n);
        BigDecimal mul = bNum.multiply(bN).subtract(new BigDecimal(1));
        int i = mul.intValue();
        return String.valueOf(i);
    }
    
    /**
     * 
     *〈简述〉通过考试id获得分数段代表
     *〈详细描述〉
     * @author Administrator
     * @param groupExamDetailId
     * @return
     */
    public List<ScoreRepresentDto> getScoreRepresentById(ObjectId groupExamDetailId) {
        List<ScoreRepresentEntry> list = scoreRepresentDao.getScoreRepresentAll(groupExamDetailId);
        List<ScoreRepresentDto> listDto = new ArrayList<ScoreRepresentDto>();
        for (ScoreRepresentEntry e : list) {
            listDto.add(new ScoreRepresentDto(e));
        }
        return listDto;
    }
    
    public void saveScoreRepresent (String s) throws Exception{
        List<ScoreRepresentDto> list = JSON.parseObject(s, new TypeReference<List<ScoreRepresentDto>>() {});
        for (ScoreRepresentDto sr : list) {
            if (StringUtils.isNotEmpty(sr.getSubjectId())) {
                
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(sr.getId()),new ObjectId(sr.getGroupExamDetailId()), new ObjectId(sr.getSubjectId()) ,sr.getSubjectName() , sr.getMaxScore().trim(), sr.getScoreOne().trim(), sr.getScoreTwo().trim(), sr.getScoreThree().trim(), sr.getScoreFour().trim(), sr.getScoreFive().trim(), sr.getScoreSix().trim(), sr.getScoreSeven().trim(), sr.getScoreEight().trim(), Integer.valueOf(sr.getSort()), Integer.valueOf(sr.getRepresentNameType())));
            } else {
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(sr.getGroupExamDetailId()), sr.getSubjectName() , sr.getMaxScore().trim(), sr.getScoreOne().trim(), sr.getScoreTwo().trim(), sr.getScoreThree().trim(), sr.getScoreFour().trim(), sr.getScoreFive().trim(), sr.getScoreSix().trim(), sr.getScoreSeven().trim(), sr.getScoreEight().trim(), Integer.valueOf(sr.getSort()), Integer.valueOf(sr.getRepresentNameType()),new ObjectId(sr.getId())));
            }
            
        }
        
    }
    
    public void saveScoreRepresentByDto (ScoreRepresentListDto list) throws Exception{
        //List<ScoreRepresentDto> list = JSON.parseObject(s, new TypeReference<List<ScoreRepresentDto>>() {});
        for (ScoreRepresentDto sr : list.getList()) {
            if (StringUtils.isNotEmpty(sr.getSubjectId())) {
                
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(sr.getId()),new ObjectId(sr.getGroupExamDetailId()), new ObjectId(sr.getSubjectId()) ,sr.getSubjectName() , sr.getMaxScore().trim(), sr.getScoreOne().trim(), sr.getScoreTwo().trim(), sr.getScoreThree().trim(), sr.getScoreFour().trim(), sr.getScoreFive().trim(), sr.getScoreSix().trim(), sr.getScoreSeven().trim(), sr.getScoreEight().trim(), Integer.valueOf(sr.getSort()), Integer.valueOf(sr.getRepresentNameType())));
            } else {
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(sr.getGroupExamDetailId()), sr.getSubjectName() , sr.getMaxScore().trim(), sr.getScoreOne().trim(), sr.getScoreTwo().trim(), sr.getScoreThree().trim(), sr.getScoreFour().trim(), sr.getScoreFive().trim(), sr.getScoreSix().trim(), sr.getScoreSeven().trim(), sr.getScoreEight().trim(), Integer.valueOf(sr.getSort()), Integer.valueOf(sr.getRepresentNameType()),new ObjectId(sr.getId())));
            }
            
        }
        
    }

    public void updateVersion(ObjectId groupExamDetailId,
                              long version) {
        groupExamVersionDao.updateVersionByGroupExamDetailId(groupExamDetailId, version);
    }

    public void increaseVersion(ObjectId groupExamDetailId) {
        groupExamVersionDao.increaseVersion(groupExamDetailId);
    }
    
    

    /**
     * 保存成绩列表
     *
     * @param examScoreDTOs
     */
    public void saveRecordExamScore(List<GroupExamUserRecordDTO> examScoreDTOs, int status, int isSend) {
        if (examScoreDTOs.size() > 0) {
            String groupExamDetailId = examScoreDTOs.get(0).getGroupExamDetailId();
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                groupExamUserRecordDao.updateGroupExamUserRecordScoreNew(new ObjectId(dto.getUserId()),new ObjectId(dto.getGroupExamDetailId()),
                        dto.getScoreStr(), dto.getScoreLevelStr(), dto.getRankStr());
            }
            List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamDetailId), -1, -1, -1, 1);
            examScoreDTOs.clear();
            List<String>  objectIdList = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            for (GroupExamUserRecordEntry entry : recordEntries) {
                examScoreDTOs.add(new GroupExamUserRecordDTO(entry,1));
                objectIdList.add(entry.getUserId().toString());
                sb.append(entry.getUserId().toString());
                sb.append("#");
                sb.append(entry.getID().toString());
                sb.append(",");
            }
            GroupExamDetailEntry detailEntry = groupExamDetailDao.getEntryById(new ObjectId(groupExamDetailId));
           
            examScoreDTOs = this.tran(examScoreDTOs, detailEntry);
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                groupExamUserRecordDao.updateGroupExamUserRecordScoreNew(new ObjectId(dto.getUserId()),new ObjectId(dto.getGroupExamDetailId()),
                        dto.getScoreStr(), dto.getScoreLevelStr(), dto.getRankStr());
                //webHomePageDao.updateContactStatus(new ObjectId(dto.getId()), Constant.THREE, status);
                
                GroupExamUserRecordEntry e = groupExamUserRecordDao.getGroupExamUserRecordEntry(new ObjectId(dto.getId()));
                if (e.getStatus() != 3) {
                    webHomePageDao.updateContactStatus(new ObjectId(dto.getId()), Constant.THREE, status);
                    groupExamUserRecordDao.updateGroupExamDetailUserRecord(e.getCommunityId(), e.getUserId(), status);
                }
            }
            //数据分析:分为两种
            //一是分值分析 二是等第分析
            groupExamDetailDao.updateGroupExamDetailEntry(new ObjectId(groupExamDetailId), status);
            //更新状态(改为保存改变状态)

            webHomePageDao.updateContactStatus(new ObjectId(groupExamDetailId), Constant.FIVE, status);
            //groupExamUserRecordDao.updateGroupExamDetailStatus(new ObjectId(groupExamDetailId), status);

            //添加红点
            //发送状态下
            IndexPageEntry indexPageEntry = indexPageDao.getEntry(detailEntry.getID());
            groupExamDetailDao.updateGroupExamDetailEntrySubTime(new ObjectId(groupExamDetailId), System.currentTimeMillis());
            if(indexPageEntry==null && status==2){
                redDotService.addThirdList(detailEntry.getID(),detailEntry.getCommunityId(), detailEntry.getUserId(), ApplyTypeEn.repordcard.getType());
                //IndexPageEntry indexPageEntry = indexPageDao.getEntry(detailEntry.getID());
                //新首页
                IndexPageDTO dto2 = new IndexPageDTO();
                dto2.setType(CommunityType.reportCard.getType());
                dto2.setUserId(detailEntry.getUserId().toString());
                dto2.setCommunityId(detailEntry.getCommunityId().toString());
                dto2.setContactId(detailEntry.getID().toString());
                objectIdList.add(detailEntry.getUserId().toString());
                dto2.setReceiveIdList(objectIdList);
                IndexPageEntry entry2 = dto2.buildAddEntry();
                indexPageDao.addEntry(entry2);
                String str = detailEntry.getSubjectIds();
                String suid = "59dc8a68bf2e791a140769b4";
                // List<ObjectId> os = new ArrayList<ObjectId>();
                if(str!=null){
                    String[] string = str.split(",");
                    if(string.length>0){
                        suid = string[0];
                    }
                        /*for(String tr:string){
                            if(ObjectId.isValid(tr)){
                                os.add(new ObjectId(tr));
                            }
                        }*/
                }
                SubjectClassEntry subjectClassEntry = subjectClassDao.getEntry(new ObjectId(suid));
                String name = "其他";
                if(subjectClassEntry!=null){
                    name = subjectClassEntry.getName();
                }
                // String com = subjectClassDao.getBigListByList(os);
                CommunityEntry communityEntry = communityDao.findCommunityByObjectId(detailEntry.getGroupId());
                String groupName = "";
                if(communityEntry!=null){
                    groupName = communityEntry.getCommunityName();
                }
                IndexContentDTO indexContentDTO = new IndexContentDTO(
                        name,
                        detailEntry.getExamName(),
                        "老师发了一份成绩单，赶快查看吧！",
                        new ArrayList<VideoDTO>(),
                        new ArrayList<Attachement>(),
                        new ArrayList<Attachement>(),
                        new ArrayList<Attachement>(),
                        groupName,
                        sb.toString());
                List<ObjectId> members=memberDao.getAllMemberIds(detailEntry.getGroupId());
                //发送者
                IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(detailEntry.getUserId().toString(),suid, detailEntry.getGroupId().toString(),detailEntry.getCommunityId().toString(),3);
                indexContentEntry.setReadList(new ArrayList<ObjectId>());
                indexContentEntry.setContactId(detailEntry.getID());
                indexContentEntry.setContactType(8);
                indexContentEntry.setAllCount(members.size());
                indexContentDao.addEntry(indexContentEntry);
                //接受者
                  /*  IndexContentEntry indexContentEntry2 = indexContentDTO.buildEntry(detailEntry.getUserId().toString(),suid, detailEntry.getGroupId().toString(),detailEntry.getCommunityId().toString(),3);
                    indexContentEntry2.setReadList(new ArrayList<ObjectId>());
                    indexContentEntry2.setContactId(detailEntry.getID());
                    indexContentEntry2.setContactType(8);
                    indexContentEntry2.setAllCount(members.size());
                    indexContentEntry2.setRemove(2);
                    indexContentDao.addEntry(indexContentEntry2);*/
                PictureRunNable.addTongzhi(detailEntry.getCommunityId().toString(), detailEntry.getUserId().toString(), 6,detailEntry.getExamName());
                //成绩单发送记录
                moduleTimeDao.addEntry(detailEntry.getUserId(),ApplyTypeEn.repordcard.getType(),detailEntry.getCommunityId());
            }

            
            this.tranEvaluate(examScoreDTOs, detailEntry, groupExamDetailId);
        }
    }
    
    public void tranEvaluate(List<GroupExamUserRecordDTO> recordExamScoreDTOs, GroupExamDetailEntry detailEntry, String groupExamDetailId) {
        int q = recordExamScoreDTOs.get(0).getScoreStr().split(",").length;
        //excellentPercent, qualifyPercent, unQualifyPercent, avgScore, maxScore, minScore
        StringBuffer excellentPercentStr = new StringBuffer();
        StringBuffer qualifyPercentStr = new StringBuffer();
        StringBuffer unQualifyPercentStr = new StringBuffer();
        StringBuffer avgScoreStr = new StringBuffer();
        StringBuffer maxScoreStr = new StringBuffer();
        StringBuffer minScoreStr = new StringBuffer();
        StringBuffer aPencentStr = new StringBuffer();
        StringBuffer bPencentStr = new StringBuffer();
        StringBuffer cPencentStr = new StringBuffer();
        StringBuffer dPencentStr = new StringBuffer();
        for (int j = 0; j< q;j++) {
            for(GroupExamUserRecordDTO g : recordExamScoreDTOs) {
                String[] scoreArr = g.getScoreStr().split(",");
                String[] scoreLevelArr = g.getScoreLevelStr().split(",");
                g.setScore(Double.valueOf(scoreArr[j]));
                g.setScoreLevel(Integer.valueOf(scoreLevelArr[j]));
            }
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScore() > o2.getScore()) {
                            return -1;
                        } else if (o1.getScore() == o2.getScore()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            } else {
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScoreLevel() > o2.getScoreLevel()) {
                            return -1;
                        } else if (o1.getScoreLevel() == o2.getScoreLevel()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            }
            int rank = 1;
            //如果未填写或者缺考排名给-1
            for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
                if (detailEntry.getRecordScoreType() == Constant.ONE) {
                    if (dto.getScore() != -1 && dto.getScore() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        
                        rank++;
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                 
                    }
                } else {
                    if (dto.getScoreLevel() != -1 && dto.getScoreLevel() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        rank++;
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                    }
                }
                
                
            }
            
            
            
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                List<ScoreRepresentEntry> list = scoreRepresentDao.getScoreRepresentAll(new ObjectId(groupExamDetailId));
                double qualifyScore = Double.valueOf(list.get(j).getScoreSix());
                double excellentScore = Double.valueOf(list.get(j).getScoreTwo());
                //所有有分值的总人数
                int totalCount = 0;
                double totalScore = 0D;
                //合格人数
                int qualifyCount = 0;
                //优秀人数
                int excellentCount = 0;
                //不及格人数
                int unQualifyCount = 0;
                double maxScore = 0;
                if (recordExamScoreDTOs.get(0).getScore() != -1D && recordExamScoreDTOs.get(0).getScore() != -2D) {
                    maxScore = recordExamScoreDTOs.get(0).getScore();
                }
                double minScore = 0;
                /*if (examScoreDTOs.get(examScoreDTOs.size() - 1).getScore() != -1D && examScoreDTOs.get(examScoreDTOs.size() - 1).getScore() != -2D) {
                    minScore = examScoreDTOs.get(examScoreDTOs.size() - 1).getScore();
                }*/
                
                for (int i = 0; i<recordExamScoreDTOs.size(); i++) {
                    if (recordExamScoreDTOs.get(recordExamScoreDTOs.size() - 1 - i).getScore() != -1D && recordExamScoreDTOs.get(recordExamScoreDTOs.size() - 1 - i).getScore() != -2D) {
                        minScore = recordExamScoreDTOs.get(recordExamScoreDTOs.size() - 1 - i).getScore();
                        break;
                    }
                }
                for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
                    double score = dto.getScore();
                    if (score != -1 && score!= -2) {
                        totalCount++;
                        totalScore += score;
                        if (score >= qualifyScore) {
                            qualifyCount++;
                        } else {
                            unQualifyCount++;
                        }
                        if (score >= excellentScore) {
                            excellentCount++;
                        }
                    }
                }
                if (totalCount != 0) {
                    double avgScore = divide(totalScore, (double) totalCount, 1);
                    double excellentPercent = divide(mul((double) excellentCount, 100D), (double) totalCount, 1);
                    double qualifyPercent = divide(mul((double) qualifyCount, 100D), (double) totalCount, 1);
                    double unQualifyPercent = divide(mul((double) unQualifyCount, 100D), (double) totalCount, 1);
                    /*RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(new ObjectId(groupExamDetailId));
                    RecordScoreEvaluateEntry entry = new RecordScoreEvaluateEntry(new ObjectId(groupExamDetailId),
                            excellentPercent, qualifyPercent, unQualifyPercent, avgScore, maxScore, minScore);
                    if (null != evaluateEntry) {
                        entry.setID(evaluateEntry.getID());
                    }
                    recordScoreEvaluateDao.saveRecordScoreEvaluateEntry(entry);*/
                    maxScoreStr.append(String.valueOf(maxScore)).append(",");
                    minScoreStr.append(String.valueOf(minScore)).append(",");
                    avgScoreStr.append(String.valueOf(avgScore)).append(",");
                    excellentPercentStr.append(String.valueOf(excellentPercent)).append(",");
                    qualifyPercentStr.append(String.valueOf(qualifyPercent)).append(",");
                    unQualifyPercentStr.append(String.valueOf(unQualifyPercent)).append(",");
                } else {
                    maxScoreStr.append("").append(",");
                    minScoreStr.append("").append(",");
                    avgScoreStr.append("").append(",");
                    excellentPercentStr.append("").append(",");
                    qualifyPercentStr.append("").append(",");
                    unQualifyPercentStr.append("").append(",");
                }
            } else {
                int totalCount = 0;
                int aCount = 0;
                int bCount = 0;
                int cCount = 0;
                int dCount = 0;
                for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
                    int scoreLevel = dto.getScoreLevel();
                    if (scoreLevel != -1 && scoreLevel != -2) {
                        totalCount++;
                        if (scoreLevel >= RecordLevelEnum.AP.getLevelScore()) {
                            aCount++;
                        } else if (RecordLevelEnum.AP.getLevelScore() >
                                scoreLevel && scoreLevel >= RecordLevelEnum.BP.getLevelScore()) {
                            bCount++;
                        } else if (scoreLevel < RecordLevelEnum.BP.getLevelScore()
                                && scoreLevel >= RecordLevelEnum.CP.getLevelScore()) {
                            cCount++;
                        } else if (scoreLevel < RecordLevelEnum.CP.getLevelScore()
                                && scoreLevel >= RecordLevelEnum.DP.getLevelScore()) {
                            dCount++;
                        }
                    }
                }
                if (totalCount != 0) {
                    double aPercent = divide(mul((double) aCount, 100D), (double) totalCount, 1);
                    double bPercent = divide(mul((double) bCount, 100D), (double) totalCount, 1);
                    double cPercent = divide(mul((double) cCount, 100D), (double) totalCount, 1);
                    double dPercent = divide(mul((double) dCount, 100D), (double) totalCount, 1);
                    
                    aPencentStr.append(String.valueOf(aPercent)).append(",");
                    bPencentStr.append(String.valueOf(bPercent)).append(",");
                    cPencentStr.append(String.valueOf(cPercent)).append(",");
                    dPencentStr.append(String.valueOf(dPercent)).append(",");

                   
                }
            }
            
        }
        
        if (detailEntry.getRecordScoreType() == Constant.ONE) {
            RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(new ObjectId(groupExamDetailId));
            RecordScoreEvaluateEntry entry = new RecordScoreEvaluateEntry(new ObjectId(groupExamDetailId),
                    excellentPercentStr.toString(), qualifyPercentStr.toString(), unQualifyPercentStr.toString(), avgScoreStr.toString(), maxScoreStr.toString(), minScoreStr.toString());
            if (null != evaluateEntry) {
                entry.setID(evaluateEntry.getID());
            }
            recordScoreEvaluateDao.saveRecordScoreEvaluateEntry(entry);
        } else {
            RecordLevelEvaluateEntry entry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(new ObjectId(groupExamDetailId));
            RecordLevelEvaluateEntry evaluateEntry = new RecordLevelEvaluateEntry(
                    new ObjectId(groupExamDetailId), aPencentStr.toString(), bPencentStr.toString(), cPencentStr.toString(), dPencentStr.toString());
            if (null != entry) {
                evaluateEntry.setID(entry.getID());
            }
            recordLevelEvaluateDao.saveRecordLevelEvaluate(evaluateEntry);
        }
             
    }

    public static Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static Double divide(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public GroupExamDetailDTO getGroupExamDetail(ObjectId singleId) throws Exception{
        GroupExamDetailDTO detailDTO = new GroupExamDetailDTO();
        GroupExamUserRecordEntry examUserRecordEntry = groupExamUserRecordDao.getGroupExamUserRecordEntry(singleId);
        if (null != examUserRecordEntry) {
            ObjectId groupExamDetailId = examUserRecordEntry.getGroupExamDetailId();
            detailDTO = getTeacherGroupExamDetail(groupExamDetailId);
            detailDTO.setSingleScoreId(singleId.toString());
            detailDTO.setStatus(examUserRecordEntry.getStatus());
            detailDTO.setChildUserId(examUserRecordEntry.getUserId().toString());
           // NewVersionCommunityBindEntry bindEntry = newVersionCommunityBindDao.getEntry(examUserRecordEntry.getCommunityId(), examUserRecordEntry.getUserId());
            UserEntry userEntry = userService.findById(examUserRecordEntry.getUserId());
            VirtualUserEntry virtualUserEntry = virtualUserDao.getVirtualUserByUserId(examUserRecordEntry.getUserId());
           // if (bindEntry != null && bindEntry.getThirdName() != null && !bindEntry.getThirdName().equals("")) {
            //    detailDTO.setChildUserName(bindEntry.getThirdName());
            //} else {
            if (null != userEntry) {
                detailDTO.setChildUserName(StringUtils.isNotBlank(userEntry.getNickName()) ?
                                userEntry.getNickName() : userEntry.getUserName()
                );
            }else if(null!=virtualUserEntry){
                detailDTO.setChildUserName(virtualUserEntry.getUserName());
            }
           // }
            if (detailDTO.getRecordScoreType() == Constant.ONE) {
                detailDTO.setScore(examUserRecordEntry.getScore());
                detailDTO.setSingleRank(examUserRecordEntry.getRank());
            } else {
                detailDTO.setScoreLevel(examUserRecordEntry.getScoreLevel());
            }
        }
        return detailDTO;
    }

    public GroupExamDetailDTO getTeacherGroupExamDetail(ObjectId groupExamDetailId) throws Exception{
        
        GroupExamDetailDTO detailDTO = new GroupExamDetailDTO();
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(groupExamDetailId);
        if (detailEntry == null) {
            throw new Exception("改成绩单已删除！");
        }
        if (null != detailEntry) {
            detailDTO = new GroupExamDetailDTO(detailEntry);
            //detailDTO.setSignCount(this.calcuteSignCount(groupExamDetailId));
            detailDTO.setSignCount(this.getMyRoleList4(detailEntry.getCommunityId(), detailEntry.getUserId()));
            SubjectClassEntry subjectClassEntry = subjectClassDao.getEntry(detailEntry.getSubjectId());
            if (null != subjectClassEntry) {
                detailDTO.setSubjectName(subjectClassEntry.getName());
            }
            if (StringUtils.isNotBlank(detailEntry.getSubjectIds())) {
                String[] subjectIdArry = detailEntry.getSubjectIds().split(",");
                for (String s : subjectIdArry) {
                    detailDTO.getSubjectNameList().add(subjectClassDao.getEntry(new ObjectId(s)).getName());
                }
                if (subjectIdArry.length >1) {
                    detailDTO.getSubjectNameList().add("总分");
                }
            }
            UserEntry userEntry = userService.findById(detailEntry.getUserId());
            if (null != userEntry) {
                detailDTO.setUserName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
            }
            CommunityEntry communityEntry = communityDao.findByObjectId(detailEntry.getCommunityId());
            if (null != communityEntry) {
                detailDTO.setGroupName(communityEntry.getCommunityName());
            }
            ExamTypeEntry examTypeEntry = examTypeDao.getEntry(detailEntry.getExamType());
            if (null != examTypeEntry) {
                detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
            }
            //常量List，防止移动端空指针
            List<String> clList = new ArrayList<String>();
            for (int i = 0;i<=detailEntry.getSubjectIds().split(",").length;i++) {
                clList.add("0");
            }
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(groupExamDetailId);
                if (null != evaluateEntry) {
                    /*detailDTO.setExcellentPercent(evaluateEntry.getExcellentPercent());
                    detailDTO.setQualifyPercent(evaluateEntry.getQualifyPercent());
                    detailDTO.setUnQualifyPercent(evaluateEntry.getUnQualifyPercent());
                    detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                    detailDTO.setGroupMaxScore(evaluateEntry.getMaxScore());
                    detailDTO.setGroupMinScore(evaluateEntry.getMinScore());*/
                    if (evaluateEntry.getExcellentPercentStr().split(",").length == 0) {
                        detailDTO.setExcellentPercentList(clList);
                    } else {
                        detailDTO.setExcellentPercentList(Arrays.asList(evaluateEntry.getExcellentPercentStr().split(",")));
                    }
                    
                    if (evaluateEntry.getQualifyPercentStr().split(",").length == 0) {
                        detailDTO.setQualifyPercentList(clList);
                    } else {
                        detailDTO.setQualifyPercentList(Arrays.asList(evaluateEntry.getQualifyPercentStr().split(",")));
                    }
                    
                    if (evaluateEntry.getUnQualifyPercentStr().split(",").length == 0) {
                        detailDTO.setUnQualifyPercentList(clList);
                    } else {
                        detailDTO.setUnQualifyPercentList(Arrays.asList(evaluateEntry.getUnQualifyPercentStr().split(",")));
                    }
                     
                    if (evaluateEntry.getAvgScoreStr().split(",").length == 0) {
                        detailDTO.setAvgScoreList(clList);
                    } else {
                        detailDTO.setAvgScoreList(Arrays.asList(evaluateEntry.getAvgScoreStr().split(",")));
                    }
                    
                    if (evaluateEntry.getMaxScoreStr().split(",").length == 0) {
                        detailDTO.setGroupMaxScoreList(clList);
                    } else {
                        detailDTO.setGroupMaxScoreList(Arrays.asList(evaluateEntry.getMaxScoreStr().split(",")));
                    }
                    
                    if (evaluateEntry.getMinScoreStr().split(",").length == 0) {
                        detailDTO.setGroupMinScoreList(clList);
                    } else {
                        detailDTO.setGroupMinScoreList(Arrays.asList(evaluateEntry.getMinScoreStr().split(",")));
                    }
                    
                    
                }
            } else {
                RecordLevelEvaluateEntry levelEvaluateEntry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(groupExamDetailId);
                if (null != levelEvaluateEntry) {
                    /*detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                    detailDTO.setbPercent(levelEvaluateEntry.getBpercent());
                    detailDTO.setcPercent(levelEvaluateEntry.getCpercent());
                    detailDTO.setdPercent(levelEvaluateEntry.getDpercent());*/
                    if (levelEvaluateEntry.getAPercentStr().split(",").length == 0) {
                        detailDTO.setaPercentList(clList);
                    } else {
                        detailDTO.setaPercentList(Arrays.asList(levelEvaluateEntry.getAPercentStr().split(",")));
                    }
                    if (levelEvaluateEntry.getBPercentStr().split(",").length == 0) {
                        detailDTO.setbPercentList(clList);
                    } else {
                        detailDTO.setbPercentList(Arrays.asList(levelEvaluateEntry.getBPercentStr().split(",")));
                    }
                    if (levelEvaluateEntry.getCPercentStr().split(",").length == 0) {
                        detailDTO.setcPercentList(clList);
                    } else {
                        detailDTO.setcPercentList(Arrays.asList(levelEvaluateEntry.getCPercentStr().split(",")));
                    }
                     if (levelEvaluateEntry.getDPercentStr().split(",").length == 0) {
                         detailDTO.setdPercentList(clList);
                     } else {
                         detailDTO.setdPercentList(Arrays.asList(levelEvaluateEntry.getDPercentStr().split(",")));
                     }
                    
                }
            }
            groupExamDetailDao.updateSignCount(groupExamDetailId, this.getMyRoleList4(detailEntry.getCommunityId(),detailEntry.getUserId()));
            //参考人数
            
            List<Integer> examCountList = new ArrayList<Integer>();
            //未填写人数（老）  缺考人数（新）
            
            List<Integer> unCompleteCountList = new ArrayList<Integer>();
            
            //未填写人数
            
            List<Integer> wtxCountList = new ArrayList<Integer>();
            
            
            final List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId);

           /* for(GroupExamUserRecordEntry g : recordEntries) {
                
                int examCount = 0;
                int unCompleteCount = 0;
                if (detailEntry.getRecordScoreType() == Constant.ONE) {
                 
                    String[] s = g.getScoreStr().split(",");
                    for (String ss : s) {
                       
                        if (!("-1").equals(ss) ) {
                            examCount++;
                        } 
                        if (("-2").equals(ss)) {
                            unCompleteCount++;
                        }
                    }
                    examCountList.add(examCount);
                    unCompleteCountList.add(unCompleteCount);
                } else {
                
                    String[] s = g.getScoreLevelStr().split(",");
                    for (String ss : s) {
                      
                        if (!("-1").equals(ss) ) {
                            examCount++;
                        } 
                        if (("-2").equals(ss)) {
                            unCompleteCount++;
                        }
                    }
                    examCountList.add(examCount);
                    unCompleteCountList.add(unCompleteCount);
                }
            }*/
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                for (int i = 0; i<recordEntries.get(0).getScoreStr().split(",").length; i++) {
                    int examCount = 0;
                    int unCompleteCount = 0;
                    int wtxCount = 0;
                    for(GroupExamUserRecordEntry g : recordEntries) {
                        String[] s = g.getScoreStr().split(",");
                        if (!(("-1").equals(s[i])||("-2").equals(s[i])) ) {
                            examCount++;
                        } 
                        if (("-1").equals(s[i])) {
                            unCompleteCount++;
                        }
                        if (("-2").equals(s[i])) {
                            wtxCount++;
                        }
                    }
                    examCountList.add(examCount);
                    unCompleteCountList.add(unCompleteCount);
                    wtxCountList.add(wtxCount);
                }
            } else {
                for (int i = 0; i<recordEntries.get(0).getScoreStr().split(",").length; i++) {
                    int examCount = 0;
                    int unCompleteCount = 0;
                    int wtxCount = 0;
                    for(GroupExamUserRecordEntry g : recordEntries) {
                        String[] s = g.getScoreLevelStr().split(",");
                        if (!(("-1").equals(s[i])||("-2").equals(s[i])) ) {
                            examCount++;
                        } 
                        if (("-1").equals(s[i])) {
                            unCompleteCount++;
                        }
                        if (("-2").equals(s[i])) {
                            wtxCount++;
                        }
                    }
                    examCountList.add(examCount);
                    unCompleteCountList.add(unCompleteCount);
                    wtxCountList.add(wtxCount);
                }
            }
            detailDTO.setWtxCountList(wtxCountList);
            detailDTO.setAllCount(recordEntries.size());
            detailDTO.setExamCountList(examCountList);
            detailDTO.setUnCompleteCountList(unCompleteCountList);
            
            if (CollectionUtils.isNotEmpty(recordEntries)) {
              //各分段
                List<String> maxList = new ArrayList<String>();
                List<String> qualifyList = new ArrayList<String>();
                List<String> excellentList = new ArrayList<String>();
                List<ScoreRepresentDto> list =this.getScoreRepresentById(groupExamDetailId);
                List<String> l = Arrays.asList(recordEntries.get(0).getSubjectIds().split(","));
                for (String s : l) {
                    SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(s));
                    for (ScoreRepresentDto sr : list) {
                        if (sr.getSubjectName().trim().equals(sc.getName().trim())) {
                            maxList.add(sr.getScoreOne());
                            qualifyList.add(sr.getScoreSix());
                            excellentList.add(sr.getScoreTwo());
                            break;
                        }
                    }
                }
                maxList.add(list.get(list.size()-1).getScoreOne());
                qualifyList.add(list.get(list.size()-1).getScoreSix());
                excellentList.add(list.get(list.size()-1).getScoreTwo());
                detailDTO.setMaxScoreList(maxList);
                detailDTO.setQualifyScoreList(qualifyList);
                detailDTO.setExcellentScoreList(excellentList);
            }
            
        }

        return detailDTO;
    }
    
    /**
     * 
     *〈简述〉
     *〈详细描述〉计算参考人数
     * @author Administrator
     * @param groupExamDetailId
     * @return
     */
    public int calcuteSignCount(ObjectId groupExamDetailId) {
        int i = 0;
      
        List<GroupExamUserRecordDTO> groupExamUserRecordDTOList = this.searchRecordStudentScores(groupExamDetailId);
        for (GroupExamUserRecordDTO dto : groupExamUserRecordDTOList) {
            
            if (((!("-1").equals(doubleTrans1(dto.getScore()))) && (!("-2").equals(doubleTrans1(dto.getScore())))) || (dto.getScoreLevel() != -1 && dto.getScoreLevel() != -2)) {
                i++;
            }
        }
        return i;
    }

    /**
     * 返回社群中不是我的用户idString
     */
    public int getMyRoleList4(ObjectId id,ObjectId userId){
        //获得groupId
        ObjectId obj =   communityDao.getGroupIdByCommunityId(id);
        List<MemberEntry> olist = memberDao.getMembers(obj, 1, 1000);
        List<String> clist = new ArrayList<String>();
        if(olist.size()>0){
            for(MemberEntry en : olist){

                clist.add(en.getUserId().toString());

            }
        }
        return clist.size();
    }

    public  List<ObjectId> getMyRoleList5(ObjectId id,ObjectId userId){
        //获得groupId
        ObjectId obj =   communityDao.getGroupIdByCommunityId(id);
        List<MemberEntry> olist = memberDao.getMembers(obj, 1, 1000);
        List<ObjectId> clist = new ArrayList<ObjectId>();
        if(olist.size()>0){
            for(MemberEntry en : olist){

                    clist.add(en.getUserId());

            }
        }
        return clist;
    }
    /**
     * 
     *〈简述〉
     *〈详细描述〉double类型如果小数点后为零显示整数否则保留
     * @author Administrator
     * @param num
     * @return
     */
    public static String doubleTrans1(double num){
        if(num % 1.0 == 0){
                return String.valueOf((long)num);
        }
        return String.valueOf(num);
}


    public List<ExamTypeDTO> getExamTypeDTOs() {
        List<ExamTypeDTO> examTypeDTOs = new ArrayList<ExamTypeDTO>();
        List<ExamTypeEntry> examTypeEntries = examTypeDao.getList();
        for (ExamTypeEntry examTypeEntry : examTypeEntries) {
            examTypeDTOs.add(new ExamTypeDTO(examTypeEntry));
        }
        return examTypeDTOs;
    }

    public Map<String, Object> searchUserList(ObjectId communityId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<VirtualUserDTO> virtualUserDTOs = new ArrayList<VirtualUserDTO>();
        List<VirtualUserEntry> virtualUserEntries = virtualUserDao.getAllVirtualUsers(communityId, page, pageSize);
        for (VirtualUserEntry virtualUserEntry : virtualUserEntries) {
            VirtualUserDTO dto = new VirtualUserDTO(virtualUserEntry);
            virtualUserDTOs.add(dto);
        }
        int count = virtualUserDao.countAllVirtualUsers(communityId);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", count);
        result.put("list", virtualUserDTOs);
        return result;
    }

    public Map<ObjectId, VirtualCommunityEntry> getVirtualCommunityMap(List<ObjectId> communityIds){
        return  virtualCommunityDao.getVirtualMap(communityIds);
    }

    public List<VirtualCommunityUserDTO> getRoleCommunities(ObjectId userId) {
        List<VirtualCommunityUserDTO> virtualCommunityUserDTOs
                = new ArrayList<VirtualCommunityUserDTO>();
        List<ObjectId> groupIds = memberDao.getManagerGroupIdsByUserId(userId);
        List<CommunityEntry> entries = communityDao.getCommunityEntriesByGroupIds(groupIds);
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (CommunityEntry communityEntry : entries) {
            communityIds.add(communityEntry.getID());
        }
        Map<ObjectId, VirtualCommunityEntry> map = virtualCommunityDao.getVirtualMap(communityIds);
        for (CommunityEntry communityEntry : entries) {
            VirtualCommunityUserDTO userDTO = new VirtualCommunityUserDTO();
            userDTO.setCommunityId(communityEntry.getID().toString());
            userDTO.setCommunityName(communityEntry.getCommunityName());
            userDTO.setUserCount(Constant.ZERO);
            userDTO.setFileName(communityEntry.getCommunityName()+"学生名单");
            if (null != map.get(communityEntry.getID())) {
                userDTO.setUserCount(map.get(communityEntry.getID()).getUserCount());
                userDTO.setFileName(map.get(communityEntry.getID()).getFileName());
            }
            virtualCommunityUserDTOs.add(userDTO);
        }
        return virtualCommunityUserDTOs;
    }

    public void removeVirtualUserList(ObjectId communityId) {
        VirtualCommunityEntry virtualCommunityEntry=virtualCommunityDao.findntryByCommunityId(communityId);
        if(null!=virtualCommunityEntry){
            virtualCommunityEntry.setUserCount(Constant.ZERO);
            virtualCommunityDao.saveVirtualCommunity(virtualCommunityEntry);
        }
        virtualUserDao.removeOldData(communityId);
    }

    public void removeItemId(ObjectId itemId,ObjectId communityId) {
        VirtualCommunityEntry virtualCommunityEntry=virtualCommunityDao.findntryByCommunityId(communityId);
        if(null!=virtualCommunityEntry){
            virtualCommunityEntry.setUserCount(virtualCommunityEntry.getUserCount()-1);
            virtualCommunityDao.saveVirtualCommunity(virtualCommunityEntry);
        }
        virtualUserDao.removeItemById(itemId);
    }

    public void editVirtualUserItem(ObjectId itemId,
                                    String userName,
                                    String userNumber) {
//        virtualUserDao.editVirtualUserItem(itemId, userName, userNumber);
        VirtualUserEntry entry = virtualUserDao.findById(itemId);
        if(null!=entry){
            entry.setUserName(userName);
            entry.setUserNumber(userNumber);
            NewVersionCommunityBindEntry bindEntry = newVersionCommunityBindDao
                    .getVirtualBindEntry(entry.getCommunityId(),entry.getUserName(),entry.getUserNumber());
            if(null!=bindEntry){
                entry.setUserId(bindEntry.getUserId());
            }
            virtualUserDao.saveVirualEntry(entry);
        }
    }

    public int importUserTemplate(InputStream inputStream, String communityId,String fileName) throws Exception {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
        int rowNum = sheet.getLastRowNum();
        List<VirtualUserDTO> virtualUserDTOs = new ArrayList<VirtualUserDTO>();
        for (int j = 1; j <= rowNum; j++) {
            String userName =  getCellValue(sheet.getRow(j).getCell(0));
            //String userNumber = getCellValue(sheet.getRow(j).getCell(1));
            if (StringUtils.isNotEmpty(userName)) {
                VirtualUserDTO v = new VirtualUserDTO();
                v.setCommunityId(communityId);
                //v.setUserNumber(userNumber);
                v.setUserName(userName.trim().replace(" ", ""));
                virtualUserDTOs.add(v);
            }
        }
        for (int i=0;i<virtualUserDTOs.size();i++) {
            for (int j=i+1;j<virtualUserDTOs.size();j++) {
                if (virtualUserDTOs.get(i).getUserName().trim().replace(" ", "").equals(virtualUserDTOs.get(j).getUserName().trim().replace(" ", ""))) {
                    throw new Exception("名单中有重复名字："+virtualUserDTOs.get(i).getUserName());
                  
                }
            }
        }
        return dealData(virtualUserDTOs,fileName);
    }

    public int judgeIsExistMatch(ObjectId communityId){
        int flag=1;
        VirtualCommunityEntry communityEntry =virtualCommunityDao.findntryByCommunityId(communityId);
        if(null==communityEntry){
            flag=0;
        }else{
            if(communityEntry.getUserCount()==Constant.ZERO){
                flag=0;
            }
        }
        return flag;
    }
    
    public void judgeIsExistMatchs(String communityId) throws Exception{
        String[] s = communityId.split(",");
        for(String ss : s) {
            int flag=1;
            VirtualCommunityEntry communityEntry =virtualCommunityDao.findntryByCommunityId(new ObjectId(ss));
            if(null==communityEntry){
                flag=0;
            }else{
                if(communityEntry.getUserCount()==Constant.ZERO){
                    flag=0;
                }
            }
            if (flag == 0) {
                throw new Exception(communityDao.getCommunityName(new ObjectId(ss))+"没有上传学生名单");
            }
        }
        
    }

    public void sendUnMatchNotice(String communityId,ObjectId userId)throws Exception{
        List<VirtualUserDTO> userDTOs = matchInputCount(communityId);
        if(userDTOs.size()>0){
            CommunityEntry communityEntry = communityDao.findByObjectId(new ObjectId(communityId));
            UserEntry userEntry = userService.findById(userId);
            TestTable cg = new TestTable();
            ObjectId fileKey = new ObjectId();
            File outFile = File.createTempFile(fileKey.toString(), ".jpg");
            System.out.println(outFile.getAbsolutePath());
            System.out.println(outFile.getPath());
            cg.graphicsGeneration(outFile.getAbsolutePath(),userDTOs.size()+3,1,communityEntry.getCommunityName()+"未匹配学生名单",userDTOs);
            QiniuFileUtils.uploadFile(fileKey.toString() + ".jpg", new FileInputStream(outFile), QiniuFileUtils.TYPE_IMAGE);
            outFile.delete();
            String imagePath =  QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey.toString() + ".jpg");
            List<Attachement> imageList = new ArrayList<Attachement>();
            Attachement item =new Attachement();
            item.setFlnm(imagePath);
            item.setUrl(imagePath);
            item.setUploadUserId(userId.toString());
            imageList.add(item);
            AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                    "59dc8a68bf2e791a140769b4",
                    "其他",
                    communityEntry.getCommunityName()+"未匹配学生名单",
                    "未匹配名单通知列表",
                    communityEntry.getGroupId().toString(),
                    communityEntry.getID().toString(),
                    Constant.THREE,
                    new ArrayList<VideoDTO>(),
                    imageList,
                    new ArrayList<Attachement>(),
                    new ArrayList<Attachement>(),
                    communityEntry.getCommunityName(),
                    userEntry.getUserName());
            appNoticeDTO.setUserId(userId.toString());
            ObjectId appNoticeId=appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            IndexPageDTO dto1 = new IndexPageDTO();
            dto1.setType(CommunityType.appNotice.getType());
            dto1.setUserId(userId.toString());
            dto1.setCommunityId(communityEntry.getID().toString());
            dto1.setContactId(appNoticeId.toString());
            IndexPageEntry entry = dto1.buildAddEntry();
            indexPageDao.addEntry(entry);
            //新首页
            IndexPageDTO dto2 = new IndexPageDTO();
            dto2.setType(CommunityType.allNotice.getType());
            dto2.setUserId(userId.toString());
            dto2.setCommunityId(communityEntry.getID().toString());
            dto2.setContactId(appNoticeId.toString());
            IndexPageEntry entry2 = dto2.buildAddEntry();
            indexPageDao.addEntry(entry2);

            IndexContentDTO indexContentDTO = new IndexContentDTO(
                    "其他",
                    communityEntry.getCommunityName()+"未匹配学生名单",
                    "未匹配名单通知列表",
                    new ArrayList<VideoDTO>(),
                    imageList,
                    new ArrayList<Attachement>(),
                    new ArrayList<Attachement>(),
                    communityEntry.getCommunityName(),
                    userEntry.getUserName());
            List<ObjectId> members=memberDao.getAllMemberIds(communityEntry.getGroupId());
            IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(userId.toString(),"59dc8a68bf2e791a140769b4", communityEntry.getGroupId().toString(),communityEntry.getID().toString(),1);
            indexContentEntry.setReadList(new ArrayList<ObjectId>());
            indexContentEntry.setContactId(appNoticeId);
            indexContentEntry.setContactType(1);
            indexContentEntry.setAllCount(members.size());
            indexContentDao.addEntry(indexContentEntry);

            objectIdList.add(new ObjectId(communityEntry.getID().toString()));
            //1:家长2:学生3:家长，学生
            redDotService.addEntryList(objectIdList,userId, ApplyTypeEn.notice.getType(),Constant.THREE);
            redDotService.addOtherEntryList(objectIdList,userId, ApplyTypeEn.daynotice.getType(),Constant.THREE);
        }
    }

    public List<VirtualUserDTO> matchInputCount(String communityId)throws Exception{
        Map<String, ObjectId> userBindMap = new HashMap<String, ObjectId>();
        List<VirtualUserDTO> dtos = new ArrayList<VirtualUserDTO>();
        List<NewVersionCommunityBindEntry>
                bindEntries = newVersionCommunityBindDao.getStudentIdListByCommunityId(new ObjectId(communityId));
        for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
            if (StringUtils.isNotEmpty(bindEntry.getThirdName())) {
                String key = bindEntry.getThirdName();
                userBindMap.put(key, bindEntry.getUserId());
            }
        }
        List<VirtualUserEntry> virtualUserEntries=virtualUserDao.getAllVirtualUsers(new ObjectId(communityId));
        for (VirtualUserEntry virtualUserEntry : virtualUserEntries) {
            String userName = virtualUserEntry.getUserName();
            if(StringUtils.isNotEmpty(userName)) {
                String key = userName;
                if(null!=userBindMap.get(key)){
                    virtualUserEntry.setUserId(userBindMap.get(key));
                    virtualUserDao.saveVirualEntry(virtualUserEntry);
                }else{
                    dtos.add(new VirtualUserDTO(virtualUserEntry));
                }
            }else{
                dtos.add(new VirtualUserDTO(virtualUserEntry));
            }
        }
        if(virtualUserEntries.size()==Constant.ZERO){
            throw new Exception("请先导入学生名单再进行匹配");
        }
        return dtos;
    }

    public int dealData(List<VirtualUserDTO> virtualUserDTOs,String fileName) {
        int count=0;
        if (virtualUserDTOs.size() > 0) {
            ObjectId communityId = new ObjectId(virtualUserDTOs.get(0).getCommunityId());
            List<NewVersionCommunityBindEntry>
                    bindEntries = newVersionCommunityBindDao.getStudentIdListByCommunityId(communityId);
            Map<String, ObjectId> userIds = new HashMap<String, ObjectId>();
            for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
                if (StringUtils.isNotEmpty(bindEntry.getThirdName())/*&&
                        StringUtils.isNotEmpty(bindEntry.getNumber())*/) {
                    String key= bindEntry.getThirdName()/*+"&"+bindEntry.getNumber()*/;
                    userIds.put(key, bindEntry.getUserId());
                }
            }
            List<VirtualUserEntry> entries = new ArrayList<VirtualUserEntry>();
            for (VirtualUserDTO virtualUserDTO : virtualUserDTOs) {
                String userName = virtualUserDTO.getUserName();
                //String userNumber = virtualUserDTO.getUserNumber();
                if(StringUtils.isNotEmpty(userName)){
                    String key=userName;
                    if (null != userIds.get(key)) {
                        VirtualUserEntry userEntry = new VirtualUserEntry(communityId, virtualUserDTO.getUserNumber(),
                                userIds.get(key), virtualUserDTO.getUserName());
                        entries.add(userEntry);
                    } else {
                        count++;
                        VirtualUserEntry userEntry = new VirtualUserEntry(communityId, virtualUserDTO.getUserNumber(),
                                new ObjectId(), virtualUserDTO.getUserName());
                        entries.add(userEntry);
                    }
                }else{
                    count++;
                    VirtualUserEntry userEntry = new VirtualUserEntry(communityId, virtualUserDTO.getUserNumber(),
                            new ObjectId(), virtualUserDTO.getUserName());
                    entries.add(userEntry);
                }
            }
            if (entries.size() > 0) {
                VirtualCommunityEntry virtualCommunityEntry=virtualCommunityDao.findntryByCommunityId(communityId);
                if(null!=virtualCommunityEntry){
                    virtualCommunityEntry.setUserCount(entries.size());
                    virtualCommunityEntry.setFileName(fileName);
                    virtualCommunityDao.saveVirtualCommunity(virtualCommunityEntry);
                }else{
                    VirtualCommunityEntry entry=new VirtualCommunityEntry(communityId,entries.size(),fileName);
                    virtualCommunityDao.saveVirtualCommunity(entry);
                }
                virtualUserDao.removeOldData(communityId);
                virtualUserDao.saveEntries(entries);
            }
        }
        return count;
    }

    public void importUserControl(InputStream inputStream,String fileName) throws Exception {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
        int rowNum = sheet.getLastRowNum();
        List<VirtualUserDTO> virtualUserDTOs = new ArrayList<VirtualUserDTO>();
        for (int j = 1; j <= rowNum; j++) {
            String communityId = getCellValue(sheet.getRow(j).getCell(0));
            String userName =  getCellValue(sheet.getRow(j).getCell(1));
            //String userNumber =  getCellValue(sheet.getRow(j).getCell(2));
            if (StringUtils.isNotEmpty(communityId) &&
                    StringUtils.isNotEmpty(userName)) {
                VirtualUserDTO v = new VirtualUserDTO();
                v.setCommunityId(communityId);
                v.setUserName(userName);
                //v.setUserNumber(userNumber);
                virtualUserDTOs.add(v);
            }
        }
        
        dealData(virtualUserDTOs,fileName);
    }

    private String getCellValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC: {
                    short format = cell.getCellStyle().getDataFormat();
                    if(format == 14 || format == 31 || format == 57 || format == 58){   //excel中的时间格式
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        cellvalue = sdf.format(date);
                    }
                    // 判断当前的cell是否为Date
                    else if (HSSFDateUtil.isCellDateFormatted(cell)) {  //先注释日期类型的转换，在实际测试中发现HSSFDateUtil.isCellDateFormatted(cell)只识别2014/02/02这种格式。
                        // 如果是Date类型则，取得该Cell的Date值           // 对2014-02-02格式识别不出是日期格式
                        Date date = cell.getDateCellValue();
                        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue= formater.format(date);
                    } else { // 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = NumberToTextConverter.toText(cell.getNumericCellValue());

                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getStringCellValue().replaceAll("'", "''");
                    break;
                case  HSSFCell.CELL_TYPE_BLANK:
                    cellvalue = null;
                    break;
                // 默认的Cell值
                default:{
                    cellvalue = " ";
                }
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    public void exportUserTemplate(HttpServletRequest request,HttpServletResponse response) {
        String sheetName = "学生录入模板";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名");

        /*cell = row.createCell(1);
        cell.setCellValue("用户学号");*/

        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent,response, wb, fileName);
    }
    
    public void exportUserRecord(String examId,HttpServletRequest request,HttpServletResponse response) {
        List<GroupExamUserRecordDTO> examScoreDTOs = this.searchRecordStudentScores(new ObjectId(examId));
        GroupExamDetailEntry g = groupExamDetailDao.getGroupExamDetailEntry(new ObjectId(examId));
        String sheetName = "学生成绩列表";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        if (g.getRecordScoreType() == 1) {
            cell.setCellValue("分数");
        } else {

            cell.setCellValue("等第");
        }
        
        
        
        cell = row.createCell(2);
        cell.setCellValue("排名");
        
        for (int i = 0;i<examScoreDTOs.size();i++) {
            HSSFRow roww = sheet.createRow(i+1);

            HSSFCell celll = roww.createCell(0);
            celll.setCellValue(examScoreDTOs.get(i).getUserName());

            celll = roww.createCell(1);
            if (g.getRecordScoreType() == 1) {
                if (examScoreDTOs.get(i).getScore() == -1d) {
                    celll.setCellValue("缺");
                } else {
                    celll.setCellValue(examScoreDTOs.get(i).getScore());
                }
                
            } else {
                if (examScoreDTOs.get(i).getScoreLevel() == -1) {
                    celll.setCellValue("缺");
                } else {
                    if (examScoreDTOs.get(i).getScoreLevel() == 100) {
                        celll.setCellValue("A+");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 99) {
                        celll.setCellValue("A");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 98) {
                        celll.setCellValue("A-");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 97) {
                        celll.setCellValue("B+");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 96) {
                        celll.setCellValue("B");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 95) {
                        celll.setCellValue("B-");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 94) {
                        celll.setCellValue("C+");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 93) {
                        celll.setCellValue("C");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 92) {
                        celll.setCellValue("C-");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 91) {
                        celll.setCellValue("D+");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 90) {
                        celll.setCellValue("D");
                    } else if (examScoreDTOs.get(i).getScoreLevel() == 89) {
                        celll.setCellValue("D-");
                    } else {
                        celll.setCellValue(examScoreDTOs.get(i).getScoreLevel());
                    }
                    
                }
                
            }
            
            
            celll = roww.createCell(2);
            if (examScoreDTOs.get(i).getRank() == -1) {
                celll.setCellValue("缺");
            } else {
                celll.setCellValue(examScoreDTOs.get(i).getRank());
            }
            
        }

        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent,response, wb, fileName);
    }
    
    

    public void exportUserControl(HttpServletRequest request,ObjectId communityId, HttpServletResponse response) {
        List<VirtualUserEntry> virtualUserEntries = virtualUserDao.getAllVirtualUsers(communityId);
        String sheetName = "学生录入模板";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("社区Id");

        cell = row.createCell(1);
        cell.setCellValue("用户名");

        cell = row.createCell(2);
        cell.setCellValue("用户学号");

        int rowLine = 1;

        HSSFRow rowItem;
        HSSFCell cellItem;

        for (VirtualUserEntry userEntry : virtualUserEntries) {
            rowItem = sheet.createRow(rowLine);

            cellItem = rowItem.createCell(0);
            cellItem.setCellValue(userEntry.getCommunityId().toString());

            cellItem = rowItem.createCell(1);
            cellItem.setCellValue(userEntry.getUserName());

            cellItem = rowItem.createCell(2);
            cellItem.setCellValue(userEntry.getUserNumber());
            rowLine++;
        }
        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent,response, wb, fileName);
    }


    public void exportTemplate(HttpServletRequest request,ObjectId examGroupDetailId, HttpServletResponse response) {
        List<GroupExamUserRecordDTO> recordDTOs = searchRecordStudentScores(examGroupDetailId);
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(examGroupDetailId);
        if (null != detailEntry) {
            String sheetName = detailEntry.getExamName() + "录入模板";
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            HSSFRow rowZero = sheet.createRow(0);
            HSSFCell cellZero = rowZero.createCell(0);
            cellZero.setCellValue("缺（免）考：如考生成绩不计入总分，则填写“缺”；");

            HSSFRow row = sheet.createRow(1);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("关键字Id");

            cell = row.createCell(1);
            cell.setCellValue("考试Id");

            cell = row.createCell(2);
            cell.setCellValue("学生姓名");

            /*cell = row.createCell(3);
            cell.setCellValue("用户学号");*/

            cell = row.createCell(3);
            cell.setCellValue("等第分值");

            cell = row.createCell(4);
            cell.setCellValue("考试分值");

            int rowLine = 2;

            HSSFRow rowItem;
            HSSFCell cellItem;
            for (GroupExamUserRecordDTO recordDTO : recordDTOs) {

                rowItem = sheet.createRow(rowLine);

                cellItem = rowItem.createCell(0);
                cellItem.setCellValue(recordDTO.getId());

                cellItem = rowItem.createCell(1);
                cellItem.setCellValue(recordDTO.getGroupExamDetailId());

                cellItem = rowItem.createCell(2);
                cellItem.setCellValue(recordDTO.getUserName());

                /*cellItem = rowItem.createCell(3);
                cellItem.setCellValue(recordDTO.getUserNumber());*/

                if (detailEntry.getRecordScoreType() == Constant.ONE) {
                    cellItem = rowItem.createCell(3);
                    cellItem.setCellValue(-1);

                    cellItem = rowItem.createCell(4);
                    cellItem.setCellValue("");
                } else {
                    cellItem = rowItem.createCell(3);
                    cellItem.setCellValue("");

                    cellItem = rowItem.createCell(4);
                    cellItem.setCellValue(-1);
                }
                rowLine++;
            }

            String fileName = sheetName + ".xls";
            String userAgent = request.getHeader("USER-AGENT");
            HSSFUtils.exportExcel(userAgent,response, wb, fileName);
        }
    }
    
    public void exportTemplateNew(HttpServletRequest request,ObjectId examGroupDetailId, HttpServletResponse response) {
        List<GroupExamUserRecordDTO> recordDTOs = searchRecordStudentScores(examGroupDetailId);
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(examGroupDetailId);
        if (null != detailEntry) {
            String sheetName = detailEntry.getExamName() + "录入模板";
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            HSSFRow rowZero = sheet.createRow(0);
            HSSFCell cellZero = rowZero.createCell(0);
            cellZero.setCellValue("缺（免）考：如考生成绩不计入总分，则填写“缺”；");

            HSSFRow row = sheet.createRow(1);
            /*HSSFCell cell = row.createCell(0);
            cell.setCellValue("关键字Id");

            cell = row.createCell(1);
            cell.setCellValue("考试Id");*/

            HSSFCell cell = row.createCell(0);
            cell.setCellValue("学生姓名");

            /*cell = row.createCell(3);
            cell.setCellValue("用户学号");*/
            
            String scoreName = "";
            if (detailEntry.getRecordScoreType() == 1) {
                scoreName = "考试分值";
            } else {
                scoreName = "等第分值";
            }

            for (GroupExamUserRecordDTO g : recordDTOs) {
                String[] ss = g.getSubjectId().split(",");
                for (int i =0;i<ss.length;i++) {
                    SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(ss[i]));
                    cell = row.createCell(1+i);
                    cell.setCellValue(sc.getName()+scoreName);
                }
                
            }
            String[] sss = recordDTOs.get(0).getSubjectId().split(",");
            if (sss.length>1) {
                cell = row.createCell(1+sss.length);
                cell.setCellValue("总分"+scoreName);

                
            }
            
            /*cell = row.createCell(3);
            cell.setCellValue("等第分值");

            cell = row.createCell(4);
            cell.setCellValue("考试分值");*/

            int rowLine = 2;

            HSSFRow rowItem;
            HSSFCell cellItem;
            for (GroupExamUserRecordDTO recordDTO : recordDTOs) {

                rowItem = sheet.createRow(rowLine);

                /*cellItem = rowItem.createCell(0);
                cellItem.setCellValue(recordDTO.getId());

                cellItem = rowItem.createCell(1);
                cellItem.setCellValue(recordDTO.getGroupExamDetailId());*/

                cellItem = rowItem.createCell(0);
                cellItem.setCellValue(recordDTO.getUserName());

                /*cellItem = rowItem.createCell(3);
                cellItem.setCellValue(recordDTO.getUserNumber());*/
                
                for (GroupExamUserRecordDTO g : recordDTOs) {
                    String[] ss = g.getSubjectId().split(",");
                    for (int i =0;i<ss.length;i++) {
                        /*if (detailEntry.getRecordScoreType() == Constant.ONE) {
                            cellItem = rowItem.createCell(1+(i*2));
                            cellItem.setCellValue(-1);

                            cellItem = rowItem.createCell(2+(i*2));
                            cellItem.setCellValue("");
                        } else {
                            cellItem = rowItem.createCell(1+(i*2));
                            cellItem.setCellValue("");

                            cellItem = rowItem.createCell(2+(i*2));
                            cellItem.setCellValue(-1);
                        }*/
                        cellItem = rowItem.createCell(1+i);
                        cellItem.setCellValue("");
                    }
                    if (ss.length>1) {
                        /*if (detailEntry.getRecordScoreType() == Constant.ONE) {
                            cellItem = rowItem.createCell(1+((ss.length)*2));
                            cellItem.setCellValue(-1);

                            cellItem = rowItem.createCell(2+((ss.length)*2));
                            cellItem.setCellValue("");
                        } else {
                            cellItem = rowItem.createCell(1+((ss.length)*2));
                            cellItem.setCellValue("");

                            cellItem = rowItem.createCell(2+((ss.length)*2));
                            cellItem.setCellValue(-1);
                        }*/
                        cellItem = rowItem.createCell(1+ss.length);
                        cellItem.setCellValue("");
                        
                    }
                }
                
                rowLine++;
            }

            String fileName = sheetName + ".xls";
            String userAgent = request.getHeader("USER-AGENT");
            HSSFUtils.exportExcel(userAgent,response, wb, fileName);
        }
    }
    
    
    public void exportStuNew(HttpServletRequest request,ObjectId examGroupDetailId, HttpServletResponse response) {
        List<GroupExamUserRecordDTO> recordDTOs = searchRecordStudentScores(examGroupDetailId);
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(examGroupDetailId);
        if (null != detailEntry) {
            String sheetName = detailEntry.getExamName();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            HSSFRow rowZero = sheet.createRow(0);
            HSSFCell cellZero = rowZero.createCell(0);
            cellZero.setCellValue("缺（免）考：如考生成绩不计入总分，则填写“缺”；");

            HSSFRow row = sheet.createRow(1);

            HSSFCell cell = row.createCell(0);
            cell.setCellValue("学生姓名");
            
            String scoreName = "";
            if (detailEntry.getRecordScoreType() == 1) {
                scoreName = "考试分值";
            } else {
                scoreName = "等第分值";
            }
            for (GroupExamUserRecordDTO g : recordDTOs) {
                String[] ss = g.getSubjectId().split(",");
                for (int i =0;i<ss.length;i++) {
                    SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(ss[i]));
                    /*cell = row.createCell(1+(i*2));
                    cell.setCellValue(sc.getName()+"等第分值");

                    cell = row.createCell(2+(i*2));
                    cell.setCellValue(sc.getName()+"考试分值");*/
                    cell = row.createCell(1+i);
                    cell.setCellValue(sc.getName()+scoreName);
                }
                
            }
            String[] sss = recordDTOs.get(0).getSubjectId().split(",");
            if (sss.length>1) {
                /*cell = row.createCell(1+((sss.length)*2));
                cell.setCellValue("总分等第分值");

                cell = row.createCell(2+((sss.length)*2));
                cell.setCellValue("总分考试分值");*/
                cell = row.createCell(1+sss.length);
                cell.setCellValue("总分"+scoreName);
            }
          
            int rowLine = 2;

            HSSFRow rowItem;
            HSSFCell cellItem;
            for (GroupExamUserRecordDTO recordDTO : recordDTOs) {
                
                rowItem = sheet.createRow(rowLine);
                cellItem = rowItem.createCell(0);
                cellItem.setCellValue(recordDTO.getUserName());
                String[] scoreStrArry = recordDTO.getScoreStr().split(",");
                String[] scoreLevelStrArry = recordDTO.getScoreLevelStr().split(",");
                for (GroupExamUserRecordDTO g : recordDTOs) {
                    String[] ss = g.getSubjectId().split(",");
                    for (int i =0;i<ss.length;i++) {
                        if (detailEntry.getRecordScoreType() == Constant.ONE) {
                            /*cellItem = rowItem.createCell(1+(i*2));
                            cellItem.setCellValue(-1);*/

                            cellItem = rowItem.createCell(1+i);
                          
                            cellItem.setCellValue(compareScore(scoreStrArry[i]));
                          
                            //cellItem.setCellType(HSSFCell.CELL_TYPE_BLANK);
                            
                            
                        } else {
                            cellItem = rowItem.createCell(1+i);
                            cellItem.setCellValue(compareScoreLevel(Integer.valueOf(scoreLevelStrArry[i])));

                            /*cellItem = rowItem.createCell(2+(i*2));
                            cellItem.setCellValue(-1);*/
                        }
                    }
                    if (ss.length>1) {
                        if (detailEntry.getRecordScoreType() == Constant.ONE) {
                            /*cellItem = rowItem.createCell(1+((ss.length)*2));
                            cellItem.setCellValue(-1);*/

                            cellItem = rowItem.createCell(1+ss.length);
                      
                            cellItem.setCellValue(compareScore(scoreStrArry[scoreStrArry.length-1]));
                            
                            //cellItem.setCellType(HSSFCell.CELL_TYPE_BLANK);
                            
                            
                        } else {
                            cellItem = rowItem.createCell(1+ss.length);
                            cellItem.setCellValue(compareScoreLevel(Integer.valueOf(scoreLevelStrArry[scoreLevelStrArry.length-1])));

                            /*cellItem = rowItem.createCell(2+((ss.length)*2));
                            cellItem.setCellValue(-1);*/
                        }
                        
                    }
                }
                
                rowLine++;
            }

            String fileName = sheetName + ".xls";
            String userAgent = request.getHeader("USER-AGENT");
            HSSFUtils.exportExcel(userAgent,response, wb, fileName);
        }
    }
    
    public String compareScoreLevel(Integer score) {
        if (score == 100) {
            return "A+";
        } else if (score == 99) {
            return "A";
        } else if (score == 98) {
            return "A-";
        } else if (score == 97) {
            return "B+";
        } else if (score == 96) {
            return "B";
        } else if (score == 95) {
            return "B-";
        } else if (score == 94) {
            return "C+";
        } else if (score == 93) {
            return "C";
        } else if (score == 92) {
            return "C-";
        } else if (score == 91) {
            return "D+";
        } else if (score == 90) {
            return "D";
        } else if (score == 89) {
            return "D-";
        } else if (score == -1) {
            return "缺";
        } else  {
            return "未填写";
        } 
    }
    
    public String compareScore(String score) {
        if ("-2".equals(score)) {
            return "";
        } else if ("-1".equals(score)) {
  
            return "缺";
        } else {
            return score;
        }
        
       
    }
    
    
    public void judgeVirtualUser(String groupExamId, HSSFSheet sheet) throws Exception {
        ObjectId communityId = groupExamDetailDao.getEntryById(new ObjectId(groupExamId)).getCommunityId();
        List<VirtualUserEntry> l =virtualUserDao.getAllVirtualUsers(communityId);
        int rowNum = sheet.getLastRowNum();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbb = new StringBuffer();
        for (VirtualUserEntry user : l) {
            boolean flag = false;
            for (int i =0;i<=rowNum;i++) {
                String userName = getStringCellValue1(sheet.getRow(i).getCell(0));
                if (StringUtils.isNotBlank(userName)) {
                    if (user.getUserName().equals(userName)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                sb.append(user.getUserName()).append("、");
            }
        }
        for (int i =2;i<=rowNum;i++) {
            boolean flag = false;
            String userName = getStringCellValue1(sheet.getRow(i).getCell(0));
            for (VirtualUserEntry user : l) {
                if (StringUtils.isNotBlank(userName)) {
                    if (user.getUserName().equals(userName)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                if (StringUtils.isNotBlank(userName)) {
                    sbb.append(userName).append("、");
                }
                
            }
        }
        if (StringUtils.isNotBlank(sb.toString()) || StringUtils.isNotBlank(sbb.toString())) {
            throw new Exception("<p style='text-align:center;font-weight:bold;margin-top:-20px;'>提醒</p><div style='text-align:left;font-size:14px;margin-top:15px;'><span style='font-weight:bold'>上传的excel中有多余学生：</span></br>"+sbb.toString()+"</br></br><span style='font-weight:bold'>未在上传exel中找到以下学生成绩：</span></br>"+sb.toString()+"</br></br>请在页面直接录入以上学生成绩，</br>或者在Excel里调整以上学生姓名后再上传！</br></br><span style='font-size:12px'>注意：</br>如页面显示的不是新名单，可在左侧“学生管理”里编辑或者导入新的学生名单</span></div>");
        }
    }
    
    public void judgeVirtualUserMulti(ObjectId communityId, HSSFSheet sheet) throws Exception {
       
        List<VirtualUserEntry> l =virtualUserDao.getAllVirtualUsers(communityId);
        int rowNum = sheet.getLastRowNum();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbb = new StringBuffer();
        for (VirtualUserEntry user : l) {
            boolean flag = false;
            for (int i =0;i<=rowNum;i++) {
                String userName = getStringCellValue1(sheet.getRow(i).getCell(0));
                if (StringUtils.isNotBlank(userName)) {
                    if (user.getUserName().equals(userName)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                sb.append(user.getUserName()).append("、");
            }
        }
        for (int i =2;i<=rowNum;i++) {
            boolean flag = false;
            String userName = getStringCellValue1(sheet.getRow(i).getCell(0));
            for (VirtualUserEntry user : l) {
                if (StringUtils.isNotBlank(userName)) {
                    if (user.getUserName().equals(userName)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                if (StringUtils.isNotBlank(userName)) {
                    sbb.append(userName).append("、");
                }
                
            }
        }
        if (StringUtils.isNotBlank(sb.toString()) || StringUtils.isNotBlank(sbb.toString())) {
            throw new Exception("<p style='text-align:center;font-weight:bold;margin-top:-20px;'>提醒</p><div style='text-align:left;font-size:14px;margin-top:15px;'><span style='font-weight:bold'>上传的excel中有多余学生：</span></br>"+sbb.toString()+"</br></br><span style='font-weight:bold'>未在上传exel中找到以下学生成绩：</span></br>"+sb.toString()+"</br></br>请在页面直接录入以上学生成绩，</br>或者在Excel里调整以上学生姓名后再上传！</br></br><span style='font-size:12px'>注意：</br>如页面显示的不是新名单，可在左侧“学生管理”里编辑或者导入新的学生名单</span></div>");
        }
    }


    public void importTemplate(String groupExamId,InputStream inputStream) throws Exception {
        
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        int rowNum = sheet.getLastRowNum();
        List<GroupExamUserRecordDTO> examScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
        List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamId), -1, -1, -1, 1);
        int i; 
        if (recordEntries.get(0).getSubjectIds().split(",").length == 1) {
            i = 1;
        } else {
            i = recordEntries.get(0).getSubjectIds().split(",").length + 1;
        }
        GroupExamDetailEntry groupExamDetailEntry = groupExamDetailDao.getEntryById(new ObjectId(groupExamId));
        for (int j = 2; j <= rowNum; j++) {
            GroupExamUserRecordDTO item = new GroupExamUserRecordDTO();
            //String id = sheet.getRow(j).getCell(0).getStringCellValue();
            //String groupExamDetailId = sheet.getRow(j).getCell(1).getStringCellValue();
            ObjectId communityId = groupExamDetailDao.getEntryById(new ObjectId(groupExamId)).getCommunityId();
            item.setGroupExamDetailId(groupExamId);
            String userName = getStringCellValue1(sheet.getRow(j).getCell(0));
            //List<NewVersionCommunityBindEntry> l = newVersionCommunityBindDao.getBindEntriesNew(communityId, userName);
            List<VirtualUserEntry> l =virtualUserDao.getAllVirtualUsersNew(communityId,userName);
            if (!CollectionUtils.isEmpty(l)) {
                if (StringUtils.isNotBlank(l.get(0).getUserId().toString())) {
                    item.setUserId(l.get(0).getUserId().toString());
                } else {
                    continue;
                }
                
            } else {
                continue;
            }
            
            //item.setId(id);
            StringBuffer sb = new StringBuffer();
            StringBuffer sbb = new StringBuffer();

            for(int k =0;k < i;k++) {
                HSSFCell cell = sheet.getRow(j).getCell(1+k);
                if (groupExamDetailEntry.getRecordScoreType() == 1) {
                    double score = getValue(cell, evaluator);
        
                    if (score == -1.0) {
                        sb.append("-1").append(",");
                    } else if(score == -2.0) {
                  
                        sb.append("-2").append(",");
                   
                        
                    }else {            
                        sb.append(String.valueOf(score)).append(",");
                        
                        
                    }
                    sbb.append("-1").append(",");
                } else {
                    sb.append("-1").append(",");
                    int scoreLevel = (new Double(getValue(cell, evaluator))).intValue();
                    sbb.append(String.valueOf(scoreLevel)).append(",");
                }
                /*HSSFCell cell = sheet.getRow(j).getCell(2+(2*k));
                double score = getValue(cell);
                if (score == -1.0) {
                    sb.append("-1").append(",");
                } else if(score == -2.0) {
                    sb.append("-2").append(",");
                }else {
                    sb.append(String.valueOf(score)).append(",");
                }
                
       
                HSSFCell hssfCell = sheet.getRow(j).getCell(1+(2*k));
                int scoreLevel = (new Double(getValue(hssfCell))).intValue();
                sbb.append(String.valueOf(scoreLevel)).append(",");*/
              
            }
           
            item.setScoreStr(sb.toString());
     
            item.setScoreLevelStr(sbb.toString());
          
            
            
            item.setRank(Constant.ZERO);
            examScoreDTOs.add(item);
        }
        /*if(examScoreDTOs.size()>0){
            String examGroupId = examScoreDTOs.get(0).getGroupExamDetailId();
            if(!groupExamId.equals(examGroupId)){
                throw  new Exception("上传的不是这次考试的成绩!");
            }
        }*/
        if (examScoreDTOs.size() > 0) {
            saveRecordExamScore(examScoreDTOs, Constant.ZERO,0);
            increaseVersion(new ObjectId(groupExamId));
        }
        this.judgeVirtualUser(groupExamId, sheet);
    }

    public double getValue(HSSFCell cell, FormulaEvaluator evaluator) throws Exception{
        double cellValue = -1;
        if (cell != null) {
            String vvv = getStringCellValue(cell, evaluator);
            if (StringUtils.isNotBlank(vvv)) {
                if ("缺".equals(vvv.trim())) {
                    
                } else {
                    cellValue = getValueByPrint(vvv);
                }
                
            } else {
                cellValue = -2;
            }
        }
        return cellValue;
    }

    /**
     * A+:100 A:99 A-:98 B+:97 B:96 B-:95 C+:94 C:93 C-:92 D+:91 D:90 D-:89
     */
    public double getValueByPrint(String levelScore) throws Exception{
        if (StringUtils.isNotBlank(levelScore)) {
            levelScore = levelScore.trim();
        }
        
        double cellValue = -1;
        if (levelScore.equals("A+")) {
            cellValue = 100;
        } else if (levelScore.equals("A")) {
            cellValue = 99;
        } else if (levelScore.equals("A-")) {
            cellValue = 98;
        } else if (levelScore.equals("B+")) {
            cellValue = 97;
        } else if (levelScore.equals("B")) {
            cellValue = 96;
        } else if (levelScore.equals("B-")) {
            cellValue = 95;
        } else if (levelScore.equals("B-")) {
            cellValue = 95;
        } else if (levelScore.equals("C+")) {
            cellValue = 94;
        } else if (levelScore.equals("C")) {
            cellValue = 93;
        } else if (levelScore.equals("C-")) {
            cellValue = 92;
        } else if (levelScore.equals("D+")) {
            cellValue = 91;
        } else if (levelScore.equals("D")) {
            cellValue = 90;
        } else if (levelScore.equals("D-")) {
            cellValue = 89;
        } else{
            cellValue = Double.valueOf(levelScore);
        }
        return cellValue;
    }

    private String getStringCellValue(HSSFCell cell, FormulaEvaluator evaluator) throws Exception{
        if (cell == null) return Constant.EMPTY;
        CellValue cellValue = evaluator.evaluate(cell);
        String strCell;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                strCell = getStringCellValue2(cellValue);
                break;
            default:
                strCell = Constant.EMPTY;
                break;
        }


        return org.apache.commons.lang.StringUtils.isBlank(strCell) ? Constant.EMPTY : strCell;
    }
    
    private String getStringCellValue1(HSSFCell cell) throws Exception{
        if (cell == null) return Constant.EMPTY;
        String strCell;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = Constant.EMPTY;
                break;
        }


        return org.apache.commons.lang.StringUtils.isBlank(strCell) ? Constant.EMPTY : strCell;
    }
    
    public String getStringCellValue2(CellValue cellValue) {
        Object o;
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                o = cellValue.getBooleanValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                o = cellValue.getNumberValue();
                break;
            case Cell.CELL_TYPE_STRING:
                o = cellValue.getStringValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                o = "";
                break;
            case Cell.CELL_TYPE_ERROR:
                o = "";
                break;

            // CELL_TYPE_FORMULA will never happen
            case Cell.CELL_TYPE_FORMULA:
                o = "";
                break;
            default:
                o = "";
                break;
        }    
        return String.valueOf(o);
    }



    public void generateReportCardSign(){
        int page=1;
        int pageSize=200;
        boolean flag=true;
        reportCardSignDao.removeOldData();
        while(flag){
            List<GroupExamUserRecordEntry> entries = groupExamUserRecordDao.getEntriesByStatus(page,pageSize);
            if(entries.size()>0){
                Map<ObjectId,ObjectId> groupCommunityMap=new HashMap<ObjectId, ObjectId>();
                Map<ObjectId,List<GroupExamUserRecordEntry>> recordMap = new HashMap<ObjectId, List<GroupExamUserRecordEntry>>();
                for(GroupExamUserRecordEntry recordEntry:entries){
                     ObjectId groupExamDetailId=recordEntry.getGroupExamDetailId();
                     groupCommunityMap.put(groupExamDetailId,recordEntry.getCommunityId());
                     if(null!=recordMap.get(groupExamDetailId)){
                         List<GroupExamUserRecordEntry> userRecordEntries =recordMap.get(groupExamDetailId);
                         userRecordEntries.add(recordEntry);
                         recordMap.put(groupExamDetailId,userRecordEntries);
                     }else{
                         List<GroupExamUserRecordEntry> userRecordEntries =new ArrayList<GroupExamUserRecordEntry>();
                         userRecordEntries.add(recordEntry);
                         recordMap.put(groupExamDetailId,userRecordEntries);
                     }
                }
                for(Map.Entry<ObjectId,List<GroupExamUserRecordEntry>> item:recordMap.entrySet()){
                    ObjectId groupExamDetailId=item.getKey();
                    ObjectId communityId=groupCommunityMap.get(groupExamDetailId);
                    List<GroupExamUserRecordEntry> userRecordEntries=item.getValue();
                    List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao
                            .getStudentIdListByCommunityId(communityId);
                    Map<ObjectId,ObjectId> bindUserMap = new HashMap<ObjectId, ObjectId>();
                    for(NewVersionCommunityBindEntry bindEntry:bindEntries){
                        bindUserMap.put(bindEntry.getUserId(),bindEntry.getMainUserId());
                    }
                    for(GroupExamUserRecordEntry recordEntry:userRecordEntries){
                        ObjectId userId=recordEntry.getUserId();
                        if(null!=bindUserMap.get(userId)){
                            ObjectId mainUserId=bindUserMap.get(userId);
                            int type=Constant.ZERO;
                            if(recordEntry.getStatus()==Constant.THREE){
                                type=Constant.THREE;
                            }
                            ReportCardSignEntry signEntry = new ReportCardSignEntry(
                                    mainUserId,
                                    groupExamDetailId,
                                    recordEntry.getID(),
                                    type,
                                    recordEntry.getID().getTime()
                                    );
                            reportCardSignDao.saveEntry(signEntry);
                        }
                    }
                }

            }else{
                flag=false;
            }
            page++;
        }
    }


    public void updateShowType(ObjectId groupExamDetailId,int showType) {
        GroupExamDetailEntry e = groupExamDetailDao.getEntryById(groupExamDetailId);
        /*if (e.getShowType()!=1) {*/
            groupExamDetailDao.updateShowType(groupExamDetailId, showType);
       /* }*/
        
    }
    
    public void updateFsShowType(ObjectId groupExamDetailId,int fsShowType) {
        GroupExamDetailEntry e = groupExamDetailDao.getEntryById(groupExamDetailId);
 
        groupExamDetailDao.updateFsShowType(groupExamDetailId, fsShowType);
        
        
    }
    
    
    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();
    /**
     * 
     *〈简述〉根据用户id和年级属性获得社群
     *〈详细描述〉
     * @author Administrator
     * @param userId
     * @param grade
     */
    public List<CommunityDTO> getCommunityByNjAndUserId(String title, ObjectId userId, String grade, String isWithN) {
        List<CommunityDTO> cDto = new ArrayList<CommunityDTO>();
        if (StringUtils.isBlank(isWithN)) {
            CommunityDTO oo = new CommunityDTO();
            oo.setId("");
            oo.setName("全部班级");
            cDto.add(oo);
        }
        
        List<ObjectId> communityIds2 = new ArrayList<ObjectId>();
        Map<String,Object> map = extendedCourseService.getUserRole(userId);
        if((Boolean)map.get("isHeader")) {
            String schoolId = (String)map.get("schoolId");
            List<ObjectId> schoolIdList = new ArrayList<ObjectId>();
            schoolIdList.add(new ObjectId(schoolId));
          //已绑定的社群集合
            communityIds2 =  schoolCommunityDao.getCommunityIdsList(schoolIdList);
        } 
        
        List<ObjectId> idss = memberDao.getGroupIdsList(userId);
        List<ObjectId> communityIds3  = groupDao.getCommunitysIdsgroupIdList(idss);
        communityIds2.addAll(communityIds3);
        
        
        if (CollectionUtils.isNotEmpty(communityIds2)) {
            if (StringUtils.isNotBlank(grade)) {
                communityIds2 = countService.reList(communityIds2, grade);
            }
            List<CommunityEntry> ceList = communityDao.findByObjectIds(title, communityIds2);
            for (CommunityEntry e : ceList) {
                CommunityDTO o = new CommunityDTO(e);
                cDto.add(o);
            }
        }
        
        return cDto;
    }
    
    @Autowired
    private CommunityService communityService;
    
    public Map<String, Object> getMultiReportList(String title, ObjectId userId,String grade, String cId, int page, int pageSize) {    
        //获得用户权限  0：没权限  1：有权限
        int ii = homeSchoolService.getAllRole(userId);
        Map<String, Object> result = new HashMap<String, Object>();
        List<MultiGroupExamDetailDto> med = new ArrayList<MultiGroupExamDetailDto>();
        Integer count = 0;
        
        
        Map<String,Object> map = extendedCourseService.getUserRole(userId);
        List<ObjectId> cIdL = new ArrayList<ObjectId>();
        if((Boolean)map.get("isHeader")) {
            String schoolId = (String)map.get("schoolId");
            List<ObjectId> schoolIdList = new ArrayList<ObjectId>();
            schoolIdList.add(new ObjectId(schoolId));
          //已绑定的社群集合
            cIdL =  schoolCommunityDao.getCommunityIdsList(schoolIdList);
            
            List<ObjectId> cidList = new ArrayList<ObjectId>();
            if (StringUtils.isNotBlank(cId)) {
                cidList.add(new ObjectId(cId));
                cIdL = cidList;
            } else {
                
                cidList = communityService.getCommunitys3(userId, 1, 100);
                cIdL.addAll(cidList);
                if (StringUtils.isNotBlank(grade)) {
                    cIdL = countService.reList(cIdL, grade);
                    
                }
            }
            List<MultiGroupExamDetailEntry> me = multiGroupExamDetailDao.getMappingDatas(title,cIdL, page, pageSize);
            
            for (MultiGroupExamDetailEntry m : me) {
                MultiGroupExamDetailDto o = new MultiGroupExamDetailDto(m);
                String[] ss = m.getSubjectIds().split(",");
                String s = "";
                for (int i = 0; i < ss.length; i++) {
                    if (i != ss.length-1) {
                        s = s + this.subjectMap().get(new ObjectId(ss[i])) + ",";
                    } else {
                        s += this.subjectMap().get(new ObjectId(ss[i]));
                    }
                    
                }
                o.setSubjectName(s);
                if (m.getUserId().equals(userId)) {
                    o.setOwn(true);
                } else {
                    o.setOwn(false);
                }
                med.add(o);
            }
            count = multiGroupExamDetailDao.getMappingDatas(cIdL);
        } else {
            if (ii == 1) {
                
                List<ObjectId> cidList = new ArrayList<ObjectId>();
                if (StringUtils.isNotBlank(cId)) {
                    cidList.add(new ObjectId(cId));
                } else {
                    
                    //cidList = memberDao.getMyCommunityOIdsByUserId(userId);
                    cidList = communityService.getCommunitys3(userId, 1, 100);
                    if (StringUtils.isNotBlank(grade)) {
                        cidList = countService.reList(cidList, grade);
                    }
                }
                List<MultiGroupExamDetailEntry> me = multiGroupExamDetailDao.getMappingDatas(title,cidList, page, pageSize);
                
                for (MultiGroupExamDetailEntry m : me) {
                    MultiGroupExamDetailDto o = new MultiGroupExamDetailDto(m);
                    String[] ss = m.getSubjectIds().split(",");
                    String s = "";
                    for (int i = 0; i < ss.length; i++) {
                        if (i != ss.length-1) {
                            s = s + this.subjectMap().get(new ObjectId(ss[i])) + ",";
                        } else {
                            s += this.subjectMap().get(new ObjectId(ss[i]));
                        }
                        
                    }
                    o.setSubjectName(s);
                    if (m.getUserId().equals(userId)) {
                        o.setOwn(true);
                    } else {
                        o.setOwn(false);
                    }
                    med.add(o);
                }
                count = multiGroupExamDetailDao.getMappingDatas(cidList);
            }
        }
        
        
        
        
        
        result.put("list", med);
        result.put("count", count);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }
    
    /**
     * 
     *〈简述〉学科map
     *〈详细描述〉
     * @author Administrator
     * @return
     */
    public Map<ObjectId, String> subjectMap() {
        Map<ObjectId, String> map = new HashMap<ObjectId, String>();
        List<SubjectClassEntry> list = subjectClassDao.getList();
        for(SubjectClassEntry s : list) {
            map.put(s.getID(), s.getName());
        }
        return map;
    }
    
    /**
     * 
     *〈简述〉保存平台成绩
     *〈详细描述〉
     * @author Administrator
     * @param dto
     * @param userId
     * @return
     * @throws Exception
     */
    public String saveMultiGroupExam(MultiGroupExamDetailDto dto, ObjectId userId) throws Exception{
        String communityName = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long examTime = dateFormat.parse(dto.getExamTime()).getTime();
        List<ObjectId> cidList = new ArrayList<ObjectId>();
        String[] ss = dto.getCommunityId().split(",");
        for (int i =0; i < ss.length ; i++) {
            cidList.add(new ObjectId(ss[i]));
            CommunityEntry cEntry = communityDao.findByObjectId(new ObjectId(ss[i]));
            if (i != (ss.length-1)) {
                communityName = communityName + cEntry.getCommunityName() + ",";
            } else {
                communityName += cEntry.getCommunityName();
            }
            
        }
        
        
        MultiGroupExamDetailEntry entry = new MultiGroupExamDetailEntry(null,cidList , communityName, dto.getRecordScoreType(), userId, dto.getExamName(), dto.getSubjectIds(), examTime, dto.getGrade());
        if (StringUtils.isNotBlank(dto.getId())) {
            entry.setID(new ObjectId(dto.getId()));
        } 
        ObjectId id = multiGroupExamDetailDao.saveGroupExamDetailEntry(entry);
        
        if (StringUtils.isBlank(dto.getId())) {
          //保存分数段代表
            String[] sa = dto.getSubjectIds().split(",");
            for (int i = 0;i < sa.length; i++) {
                SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(sa[i]));
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(id, new ObjectId(sa[i]), sc.getName(), "100", "100", "90", "89", "80", "79", "60", "59", "0",i, Constant.ONE));
            }
            int i = sa.length;
            if (sa.length>1) {
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(id,  "总分", multiply(100, i), multiply(100, i), multiply(90, i), multiplyJy(90, i), multiply(80, i), multiplyJy(80, i), multiply(60, i), multiplyJy(60, i), "0", 50,Constant.ONE));
            }
            
            for (int k =0; k < ss.length ; k++) {
                
                GroupEntry ge = groupDao.getByCommunityId(new ObjectId(ss[k]));
                this.saveGeur(userId, id, dto.getSubjectIds(), ss[k], ge.getID().toString());
            }
        }
        
      
        
        return id.toString();
    }
    
    
    public void saveGeur( ObjectId userId, ObjectId groupExamDetailId, String subjectIds, String communityId, String groupId) {
        Set<ObjectId> userIds = new LinkedHashSet<ObjectId>();
        List<VirtualUserEntry> virtualUserEntries=virtualUserDao.getAllVirtualUsers(new ObjectId(communityId));
        for (VirtualUserEntry virtualUserEntry : virtualUserEntries) {
            userIds.add(virtualUserEntry.getUserId());
        }
        
        List<GroupExamUserRecordEntry> userRecordEntries = new ArrayList<GroupExamUserRecordEntry>();
        int k = 0;
        for (ObjectId uId : userIds) {
            
                int i;
                if(subjectIds.split(",").length==1) {
                    i = 1;
                } else {
                    i = subjectIds.split(",").length+1;
                }
                StringBuffer scoreStr = new StringBuffer();
                for (int j = 0;j<i;j++) {
                    scoreStr.append("-2,");
                }
                
                StringBuffer scoreStrr = new StringBuffer();
                for (int j = 0;j<i;j++) {
                    scoreStrr.append("-1,");
                }
                
                userRecordEntries.add(new GroupExamUserRecordEntry(
                    groupExamDetailId,
                    userId,
                    uId,
                    StringUtils.isNotEmpty(groupId) ? new ObjectId(groupId) : null,
                    null,
                    subjectIds,
                    StringUtils.isNotEmpty(communityId) ? new ObjectId(communityId) : null,
                    scoreStr.toString(),
                    scoreStr.toString(),
                    0,
                    Constant.ZERO,
                    scoreStrr.toString(),
                    k,
                    scoreStr.toString(),
                    scoreStr.toString()
                    ));
                k++;
            }
            
            for (GroupExamUserRecordEntry userRecordEntry : userRecordEntries) {
                groupExamUserRecordDao.saveGroupExamUserRecord(userRecordEntry);
                
            }
        
    }
    
    //删除
    public void delMultiGroupExam(String id) throws Exception{
        MultiGroupExamDetailEntry entry = multiGroupExamDetailDao.getEntry(new ObjectId(id));
        long current=System.currentTimeMillis();
        if(entry.getSubmitTime() <current-24*60*60*1000) {
            throw new Exception("已过有效时间！");
        }
        multiGroupExamDetailDao.updateGroupExamDetailEntry(new ObjectId(id), Constant.ONE);
    }
    
    public List<GroupExamUserRecordDTO> searchRecordStudentScoresNew(ObjectId mulId, ObjectId communityId) {
        List<GroupExamUserRecordDTO> recordExamScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
        MultiGroupExamDetailEntry mentry = multiGroupExamDetailDao.getEntry(mulId);
        if (mentry != null) {
            final List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(mentry.getID(), communityId);
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry recordEntry : recordEntries) {
                userIds.add(recordEntry.getUserId());
            }
            Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
            Map<ObjectId, NewVersionCommunityBindEntry> bindUserMap = new HashMap<ObjectId, NewVersionCommunityBindEntry>();
            if (userIds.size() > 0) {
                userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
                bindUserMap = newVersionCommunityBindDao.getUserEntryMapByCondition(
                        recordEntries.get(0).getCommunityId(), new ArrayList<ObjectId>(userIds));
            }
            boolean flag = false;
            for (GroupExamUserRecordEntry recordEntry : recordEntries) {
                GroupExamUserRecordDTO userRecordDTO = new GroupExamUserRecordDTO(recordEntry,1);
                NewVersionCommunityBindEntry
                        entry = bindUserMap.get(recordEntry.getUserId());
                if (null == entry) {
                    flag = true;
                } else {
                    userRecordDTO.setUserNumber(entry.getNumber());
                    if (StringUtils.isNotBlank(entry.getThirdName())) {
                        userRecordDTO.setUserName(entry.getThirdName());
                    } else {
                        flag = true;
                    }
                }
                if (flag) {
                    UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                    if(null != userEntry){
                        userRecordDTO.setUserName(
                                StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                    }
                    flag = false;
                }
                VirtualUserEntry virtualUserEntry = virtualUserDao.getIrVirtualUserByUserId(recordEntry.getCommunityId(),recordEntry.getUserId());
                //如果查不到实时的查过版本的
                if (virtualUserEntry == null) {
                    virtualUserEntry = virtualUserDao.getVirtualUserByUserId(recordEntry.getUserId());
                }
                
                if(null != virtualUserEntry){
                    userRecordDTO.setUserNumber(virtualUserEntry.getUserNumber());
                    userRecordDTO.setUserName(virtualUserEntry.getUserName());
                }
           
                recordExamScoreDTOs.add(userRecordDTO);
            }
        }
        return recordExamScoreDTOs;
    }
    
    public void exportTemplateMulti(HttpServletRequest request,ObjectId examGroupDetailId, HttpServletResponse response) {
        MultiGroupExamDetailEntry detailEntry = multiGroupExamDetailDao.getEntry(examGroupDetailId);
        //List<GroupExamUserRecordDTO> recordDTOs = searchRecordStudentScoresNew(examGroupDetailId);
        HSSFWorkbook wb = new HSSFWorkbook();
      //合并的单元格样式
        HSSFCellStyle boderStyle = wb.createCellStyle();
        //垂直居中
        boderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        boderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //设置一个边框
        boderStyle.setBorderTop(HSSFBorderFormatting.BORDER_THICK);
   
        if (null != detailEntry) {
            List<ObjectId> cmId = detailEntry.getCommunityId();
            for (ObjectId o : cmId) {
                List<GroupExamUserRecordDTO> recordDTOs = this.searchRecordStudentScoresNew(examGroupDetailId, o);
                CommunityEntry c = communityDao.findByObjectId(o);
                String sheetName = c.getCommunityName();
                
                HSSFSheet sheet = wb.createSheet(sheetName);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
                
                
                HSSFRow rowZero = sheet.createRow(0);
                HSSFCell cellZero = rowZero.createCell(0);
                //cellZero.setCellValue("缺（免）考：如考生成绩不计入总分，则填写“缺”；");
              cellZero.setCellValue(detailEntry.getExamName());
              
              
                HSSFRow row = sheet.createRow(1);
               

                HSSFCell cell = row.createCell(0);
                cell.setCellValue("姓名");

        
                
                String scoreName = "";
                if (detailEntry.getRecordScoreType() == 1) {
                    scoreName = "考试分值";
                } else {
                    scoreName = "等第分值";
                }

                for (GroupExamUserRecordDTO g : recordDTOs) {
                    String[] ss = g.getSubjectId().split(",");
                    for (int i =0;i<ss.length;i++) {
                        SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(ss[i]));
                        cell = row.createCell(1+i*3);
                        cell.setCellValue(sc.getName());
                    }
                    
                }
                String[] sss;
                if (CollectionUtils.isNotEmpty(recordDTOs)) {
                    sss = recordDTOs.get(0).getSubjectId().split(",");
                } else {
                    sss = new String[] {};
                }
                
                if (sss.length>1) {
                    cell = row.createCell(1+(sss.length)*3);
                    cell.setCellValue("总分"+scoreName);

                    
                }
                
           
                HSSFRow row2 = sheet.createRow(2);
                int kk = 1;
                String[] ss;
                if (CollectionUtils.isNotEmpty(recordDTOs)) {
                    ss = recordDTOs.get(0).getSubjectId().split(",");
                } else {
                    ss = new String[] {};
                }
              
                
                
                for (int i =0;i<ss.length;i++) {
                    //SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(ss[i]));
                    cell = row2.createCell(kk);
                    if (detailEntry.getRecordScoreType() == 1) {
                        cell.setCellValue("分数");
                    } else {
                        cell.setCellValue("等第");
                    }
                    
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("班次");
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("校次");
                    kk++;
                }
                    
               
                
                
                if (sss.length>1) {
                    cell = row2.createCell(kk);
                    if (detailEntry.getRecordScoreType() == 1) {
                        cell.setCellValue("分数");
                    } else {
                        cell.setCellValue("等第");
                    }
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("班次");
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("校次");
                    kk++;

                    
                }

                int rowLine = 3;

                HSSFRow rowItem;
                HSSFCell cellItem;
                for (GroupExamUserRecordDTO recordDTO : recordDTOs) {

                    rowItem = sheet.createRow(rowLine);

                

                    cellItem = rowItem.createCell(0);
                    cellItem.setCellValue(recordDTO.getUserName());

                
                    
                    for (GroupExamUserRecordDTO g : recordDTOs) {
                        String[] ssd = g.getSubjectId().split(",");
                        for (int i =0;i<ssd.length;i++) {
                            
                            cellItem = rowItem.createCell(1+i);
                            cellItem.setCellValue("");
                        }
                        if (ss.length>1) {
                            
                            cellItem = rowItem.createCell(1+ssd.length);
                            cellItem.setCellValue("");
                            
                        }
                    }
                    
                    rowLine++;
                    
                }

                CellRangeAddress cra = new CellRangeAddress(1, 2, 0, 0);
                sheet.addMergedRegion(cra);
             
              
                
                for (int i =0;i<ss.length;i++) {
                    CellRangeAddress craa = new CellRangeAddress(1, 1, 1+3*i,3*(i+1) );
                    sheet.addMergedRegion(craa);
                }
                    
              
         
                if (sss.length>1) {
                    CellRangeAddress craaa = new CellRangeAddress(1, 1, 1+3*(sss.length),3*(sss.length+1));
                    sheet.addMergedRegion(craaa);

                    
                }
            }
           }
        String fileName =  "录入模板.xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent,response, wb, fileName);   
    }
    
    
    
    public void exportTemplateMultiWithData(HttpServletRequest request,ObjectId examGroupDetailId, HttpServletResponse response) {
        MultiGroupExamDetailEntry detailEntry = multiGroupExamDetailDao.getEntry(examGroupDetailId);
        //List<GroupExamUserRecordDTO> recordDTOs = searchRecordStudentScoresNew(examGroupDetailId);
        HSSFWorkbook wb = new HSSFWorkbook();
      //合并的单元格样式
        HSSFCellStyle boderStyle = wb.createCellStyle();
        //垂直居中
        boderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        boderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //设置一个边框
        boderStyle.setBorderTop(HSSFBorderFormatting.BORDER_THICK);
   
        if (null != detailEntry) {
            List<ObjectId> cmId = detailEntry.getCommunityId();
            for (ObjectId o : cmId) {
                List<GroupExamUserRecordDTO> recordDTOs = this.searchRecordStudentScoresNew(examGroupDetailId, o);
                CommunityEntry c = communityDao.findByObjectId(o);
                String sheetName = c.getCommunityName();
                
                HSSFSheet sheet = wb.createSheet(sheetName);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
                
                
                HSSFRow rowZero = sheet.createRow(0);
                HSSFCell cellZero = rowZero.createCell(0);
                //cellZero.setCellValue("缺（免）考：如考生成绩不计入总分，则填写“缺”；");
              cellZero.setCellValue(detailEntry.getExamName());
              
              
                HSSFRow row = sheet.createRow(1);
               

                HSSFCell cell = row.createCell(0);
                cell.setCellValue("姓名");

        
                
                String scoreName = "";
                if (detailEntry.getRecordScoreType() == 1) {
                    scoreName = "考试分值";
                } else {
                    scoreName = "等第分值";
                }

                for (GroupExamUserRecordDTO g : recordDTOs) {
                    String[] ss = g.getSubjectId().split(",");
                    for (int i =0;i<ss.length;i++) {
                        SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(ss[i]));
                        cell = row.createCell(1+i*3);
                        cell.setCellValue(sc.getName());
                    }
                    
                }
                String[] sss;
                if (CollectionUtils.isNotEmpty(recordDTOs)) {
                    sss = recordDTOs.get(0).getSubjectId().split(",");
                } else {
                    sss = new String[] {};
                }
                
                if (sss.length>1) {
                    cell = row.createCell(1+(sss.length)*3);
                    cell.setCellValue("总分"+scoreName);

                    
                }
                
           
                HSSFRow row2 = sheet.createRow(2);
                int kk = 1;
                String[] ss;
                if (CollectionUtils.isNotEmpty(recordDTOs)) {
                    ss = recordDTOs.get(0).getSubjectId().split(",");
                } else {
                    ss = new String[] {};
                }
              
                
                
                for (int i =0;i<ss.length;i++) {
                    //SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(ss[i]));
                    cell = row2.createCell(kk);
                    if (detailEntry.getRecordScoreType() == 1) {
                        cell.setCellValue("分数");
                    } else {
                        cell.setCellValue("等第");
                    }
                    
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("班次");
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("校次");
                    kk++;
                }
                    
               
                
                
                if (sss.length>1) {
                    cell = row2.createCell(kk);
                    if (detailEntry.getRecordScoreType() == 1) {
                        cell.setCellValue("分数");
                    } else {
                        cell.setCellValue("等第");
                    }
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("班次");
                    kk++;
                    cell = row2.createCell(kk);
                    cell.setCellValue("校次");
                    kk++;

                    
                }

                int rowLine = 3;

                HSSFRow rowItem;
                HSSFCell cellItem;
                for (GroupExamUserRecordDTO recordDTO : recordDTOs) {

                    rowItem = sheet.createRow(rowLine);

                

                    cellItem = rowItem.createCell(0);
                    cellItem.setCellValue(recordDTO.getUserName());

                
                    
                    for (GroupExamUserRecordDTO g : recordDTOs) {
                        String[] ssd = g.getSubjectId().split(",");
                        String[] scoreStrStr = g.getScoreStr().split(",");
                        String[] scoreLevelStrStr = g.getScoreLevelStr().split(",");
                        String[] bcArray = g.getBc().split(",");
                        String[] xcArray = g.getXc().split(",");
                        for (int i =0;i<ssd.length;i++) {
                            //分数
                            cellItem = rowItem.createCell(1+i);
                            if (detailEntry.getRecordScoreType() == 1) {
                                
                                cellItem.setCellValue(compareScore(scoreStrStr[i]));
                            } else {
                                cellItem.setCellValue(compareScoreLevel(Integer.valueOf(scoreLevelStrStr[i])));
                            }
                            //班次
                            cellItem = rowItem.createCell(2+i);
                            cellItem.setCellValue(compareScore(bcArray[i]));
                            //校次
                            cellItem = rowItem.createCell(3+i);
                            cellItem.setCellValue(compareScore(xcArray[i]));
                        }
                        if (ss.length>1) {
                            
                            cellItem = rowItem.createCell(1+ssd.length);
                            
                            if (detailEntry.getRecordScoreType() == 1) {
                                
                                cellItem.setCellValue(compareScore(scoreStrStr[scoreStrStr.length-1]));
                            } else {
                                cellItem.setCellValue(compareScoreLevel(Integer.valueOf(scoreLevelStrStr[scoreLevelStrStr.length-1])));
                            }
                            
                          //班次
                            cellItem = rowItem.createCell(2+ssd.length);
                            cellItem.setCellValue(compareScore(bcArray[bcArray.length-1]));
                            //校次
                            cellItem = rowItem.createCell(3+ssd.length);
                            cellItem.setCellValue(compareScore(xcArray[xcArray.length-1]));
                            
                        }
                    }
                    
                    rowLine++;
                    
                }

                CellRangeAddress cra = new CellRangeAddress(1, 2, 0, 0);
                sheet.addMergedRegion(cra);
             
              
                
                for (int i =0;i<ss.length;i++) {
                    CellRangeAddress craa = new CellRangeAddress(1, 1, 1+3*i,3*(i+1) );
                    sheet.addMergedRegion(craa);
                }
                    
              
         
                if (sss.length>1) {
                    CellRangeAddress craaa = new CellRangeAddress(1, 1, 1+3*(sss.length),3*(sss.length+1));
                    sheet.addMergedRegion(craaa);

                    
                }
            }
           }
        String fileName =  detailEntry.getExamName() + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent,response, wb, fileName);   
    }
    
    
    
    
public void importTemplateMulti(String groupExamId,InputStream inputStream) throws Exception {
        
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        MultiGroupExamDetailEntry groupExamDetailEntry = multiGroupExamDetailDao.getEntry(new ObjectId(groupExamId));
        for(int kk = 0; kk < groupExamDetailEntry.getCommunityId().size(); kk++) {
            HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(kk));
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            int rowNum = sheet.getLastRowNum();
            List<GroupExamUserRecordDTO> examScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
            List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamId), groupExamDetailEntry.getCommunityId().get(kk));
            int i; 
            if (recordEntries.get(0).getSubjectIds().split(",").length == 1) {
                i = 1;
            } else {
                i = recordEntries.get(0).getSubjectIds().split(",").length + 1;
            }
         
            for (int j = 3; j <= rowNum; j++) {
                GroupExamUserRecordDTO item = new GroupExamUserRecordDTO();
                //String id = sheet.getRow(j).getCell(0).getStringCellValue();
                //String groupExamDetailId = sheet.getRow(j).getCell(1).getStringCellValue();
                //ObjectId communityId = groupExamDetailDao.getEntryById(new ObjectId(groupExamId)).getCommunityId();
                item.setGroupExamDetailId(groupExamId);
                String userName = getStringCellValue1(sheet.getRow(j).getCell(0));
                //List<NewVersionCommunityBindEntry> l = newVersionCommunityBindDao.getBindEntriesNew(communityId, userName);
                List<VirtualUserEntry> l =virtualUserDao.getAllVirtualUsersNew(groupExamDetailEntry.getCommunityId().get(kk),userName);
                if (!CollectionUtils.isEmpty(l)) {
                    if (StringUtils.isNotBlank(l.get(0).getUserId().toString())) {
                        item.setUserId(l.get(0).getUserId().toString());
                    } else {
                        continue;
                    }
                    
                } else {
                    continue;
                }
                
                //item.setId(id);
                StringBuffer sb = new StringBuffer();
                StringBuffer sbb = new StringBuffer();
                
                StringBuffer bc = new StringBuffer();
                StringBuffer xc = new StringBuffer();

                for(int k =0;k < i;k++) {
                    HSSFCell cell = sheet.getRow(j).getCell(1+k*3);
                    HSSFCell cell1 = sheet.getRow(j).getCell(2+k*3);
                    HSSFCell cell2 = sheet.getRow(j).getCell(3+k*3);
                    if (groupExamDetailEntry.getRecordScoreType() == 1) {
                        double score = getValue(cell, evaluator);
                        
                        if (score == -1.0) {
                            sb.append("-1").append(",");
                        } else if(score == -2.0) {
                      
                            sb.append("-2").append(",");
                       
                            
                        } else {            
                            sb.append(String.valueOf(score)).append(",");
                            
                            
                        }
                        sbb.append("-1").append(",");
                    } else {
                        sb.append("-1").append(",");
                        int scoreLevel = (new Double(getValue(cell, evaluator))).intValue();
                        sbb.append(String.valueOf(scoreLevel)).append(",");
                    }
                    String bcc = getStringCellValue(cell1, evaluator);
                    if (StringUtils.isBlank(bcc)) {
                        bc.append("-2").append(",");
                    } else if ((!org.apache.commons.lang.math.NumberUtils.isNumber(bcc)) && !"缺".equals(bcc.trim()) && StringUtils.isNotBlank(bcc)) {
                        throw new Exception("班次存在非正整数，请检查");
                    } else if ("缺".equals(bcc.trim())) {
                        bc.append("-1").append(",");
                    } else {
                        bc.append(bcc).append(",");
                    }
                    String xcc = getStringCellValue(cell2, evaluator);
                    if (StringUtils.isBlank(xcc)) {
                        xc.append("-2").append(",");
                    } else if ((!org.apache.commons.lang.math.NumberUtils.isNumber(xcc)) && !"缺".equals(xcc.trim()) && StringUtils.isNotBlank(xcc)) {
                        throw new Exception("校次存在非正整数，请检查");
                    } else if ("缺".equals(xcc.trim())){
                        xc.append("-1").append(",");
                    } else {
                        xc.append(xcc).append(",");
                    }
                  
                }
                item.setBc(bc.toString());
                item.setXc(xc.toString());
                
                item.setScoreStr(sb.toString());
         
                item.setScoreLevelStr(sbb.toString());
              
                
                
                item.setRank(Constant.ZERO);
                examScoreDTOs.add(item);
            }
      
            if (examScoreDTOs.size() > 0) {
                saveRecordExamScoreMulti(examScoreDTOs);
                //increaseVersion(new ObjectId(groupExamId));
            }
            this.judgeVirtualUserMulti(groupExamDetailEntry.getCommunityId().get(kk), sheet);
        }
        
        
        
    }

/**
 * 
 *〈简述〉
 *〈详细描述〉
 * @author Administrator
 * @param examScoreDTOs
 */
    public void saveRecordExamScoreMulti(List<GroupExamUserRecordDTO> examScoreDTOs) {
        if (examScoreDTOs.size() > 0) {
            String groupExamDetailId = examScoreDTOs.get(0).getGroupExamDetailId();
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                groupExamUserRecordDao.updateGroupExamUserRecordScoreMulti(new ObjectId(dto.getUserId()),new ObjectId(dto.getGroupExamDetailId()),
                        dto.getScoreStr(), dto.getScoreLevelStr(), dto.getRankStr(),dto.getBc(),dto.getXc());
            }
            List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamDetailId), -1, -1, -1, 1);
            examScoreDTOs.clear();

            for (GroupExamUserRecordEntry entry : recordEntries) {
                examScoreDTOs.add(new GroupExamUserRecordDTO(entry,1));
                
            }
            MultiGroupExamDetailEntry detailEntry = multiGroupExamDetailDao.getEntry(new ObjectId(groupExamDetailId));
           
            examScoreDTOs = this.tranMulti(examScoreDTOs, detailEntry.getRecordScoreType());
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                groupExamUserRecordDao.updateGroupExamUserRecordScoreMulti(new ObjectId(dto.getUserId()),new ObjectId(dto.getGroupExamDetailId()),
                        dto.getScoreStr(), dto.getScoreLevelStr(), dto.getRankStr(),dto.getBc(),dto.getXc());
                
            }
        }
    }
    
    public List<GroupExamUserRecordDTO> tranMulti(List<GroupExamUserRecordDTO> recordExamScoreDTOs, int recordScoreType) {
        int q = recordExamScoreDTOs.get(0).getScoreStr().split(",").length;
        
        for (int i = 0; i< q;i++) {
            for(GroupExamUserRecordDTO g : recordExamScoreDTOs) {
                String[] scoreArr = g.getScoreStr().split(",");
                String[] scoreLevelArr = g.getScoreLevelStr().split(",");
                g.setScore(Double.valueOf(scoreArr[i]));
                g.setScoreLevel(Integer.valueOf(scoreLevelArr[i]));
            }
            if (recordScoreType == Constant.ONE) {
                
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScore() > o2.getScore()) {
                            return -1;
                        } else if (o1.getScore() == o2.getScore()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            } else {
                Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScoreLevel() > o2.getScoreLevel()) {
                            return -1;
                        } else if (o1.getScoreLevel() == o2.getScoreLevel()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            }
            int rank = 1;
            //如果未填写或者缺考排名给-1
            for (GroupExamUserRecordDTO dto : recordExamScoreDTOs) {
                if (recordScoreType == Constant.ONE) {
                    if (dto.getScore() != -1 && dto.getScore() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        
                        rank++;
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                 
                    }
                } else {
                    if (dto.getScoreLevel() != -1 && dto.getScoreLevel() != -2) {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr(String.valueOf(rank));
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append(String.valueOf(rank));
                            dto.setRankStr(s.toString());
                        }
                        rank++;
                    } else {
                        if (StringUtils.isEmpty(dto.getRankStr())) {
                            dto.setRankStr("-1");
                        } else {
                            StringBuffer s = new StringBuffer();
                            s.append(dto.getRankStr()).append(",").append("-1");
                            dto.setRankStr(s.toString());
                        }
                    }
                }
                
                
            }
        }
             return    recordExamScoreDTOs;
    }
    
    public MultiAllDto getMultiInfo(String multiId) {
        MultiAllDto multiAllDto = new MultiAllDto();
        MultiGroupExamDetailEntry entry = multiGroupExamDetailDao.getEntry(new ObjectId(multiId));
        multiAllDto.setExamName(entry.getExamName());
        
        String[] ss = entry.getSubjectIds().split(",");
        String s = "";
        for (int i = 0; i < ss.length; i++) {
            if (i != ss.length-1) {
                s = s + this.subjectMap().get(new ObjectId(ss[i])) + ",";
            } else {
                s += this.subjectMap().get(new ObjectId(ss[i]));
            }
            multiAllDto.getSubject().add(this.subjectMap().get(new ObjectId(ss[i])));
        }
        if (ss.length > 1) {
            multiAllDto.getSubject().add("总分");
        }

        multiAllDto.setMultiId(multiId);
        multiAllDto.setSubjectNames(s);
        multiAllDto.setCommunityNames(entry.getCName());
        multiAllDto.setExamTime(DateTimeUtils.convert(entry.getExamTime(),
            DateTimeUtils.DATE_YYYY_MM_DD));
        multiAllDto.setRecordScoreType(entry.getRecordScoreType());
        List<ScoreRepresentEntry> srList = scoreRepresentDao.getScoreRepresentAll(new ObjectId(multiId));
        if (CollectionUtils.isNotEmpty(srList)) {
            multiAllDto.setRepresentNameType(srList.get(0).getRepresentNameType());
        }
        List<ObjectId> cmId = entry.getCommunityId();
        Map<Integer, List<MultiPartDto>> map = new HashMap<Integer, List<MultiPartDto>>();
        int k = 0;
        for (ObjectId cid : cmId) {
            List<MultiPartDto> multiPartDtoList = new ArrayList<MultiPartDto>();
            
            List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(entry.getID(), cid);
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry recordEntry : recordEntries) {
                userIds.add(recordEntry.getUserId());
            }
            Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
            Map<ObjectId, NewVersionCommunityBindEntry> bindUserMap = new HashMap<ObjectId, NewVersionCommunityBindEntry>();
            if (userIds.size() > 0) {
                userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
                bindUserMap = newVersionCommunityBindDao.getUserEntryMapByCondition(
                        recordEntries.get(0).getCommunityId(), new ArrayList<ObjectId>(userIds));
            }
            boolean flag = false;
            for (GroupExamUserRecordEntry recordEntry : recordEntries) {
                GroupExamUserRecordDTO userRecordDTO = new GroupExamUserRecordDTO(recordEntry,1);
                NewVersionCommunityBindEntry
                        entry1 = bindUserMap.get(recordEntry.getUserId());
                if (null == entry1) {
                    flag = true;
                } else {
                    userRecordDTO.setUserNumber(entry1.getNumber());
                    if (StringUtils.isNotBlank(entry1.getThirdName())) {
                        userRecordDTO.setUserName(entry1.getThirdName());
                    } else {
                        flag = true;
                    }
                }
                if (flag) {
                    UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                    if(null != userEntry){
                        userRecordDTO.setUserName(
                                StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                    }
                    flag = false;
                }
                VirtualUserEntry virtualUserEntry = virtualUserDao.getIrVirtualUserByUserId(recordEntry.getCommunityId(),recordEntry.getUserId());
                //如果查不到实时的查过版本的
                if (virtualUserEntry == null) {
                    virtualUserEntry = virtualUserDao.getVirtualUserByUserId(recordEntry.getUserId());
                }
                
                if(null != virtualUserEntry){
                    userRecordDTO.setUserNumber(virtualUserEntry.getUserNumber());
                    userRecordDTO.setUserName(virtualUserEntry.getUserName());
                }
                
                MultiPartDto multiPartDto = new MultiPartDto();
                multiPartDto.setUserName(userRecordDTO.getUserName());
                multiPartDto.setUserId(recordEntry.getUserId().toString());
                String scoreStr = recordEntry.getScoreStr();
                String scoreLevelStr = recordEntry.getScoreLevelStr();
                String bc = recordEntry.getBc();
                String xc = recordEntry.getXc();
                String[] scoreStrArry = scoreStr.split(",");
                String[] scoreLevelStrArry = scoreLevelStr.split(",");
                String[] bcArry = new String[scoreStrArry.length];
                String[] xcArry = new String[scoreStrArry.length];
                if (StringUtils.isNotBlank(bc)) {
                    bcArry = bc.split(",");
                 
                }
                if (StringUtils.isNotBlank(xc)) {
                    xcArry = xc.split(",");
                }
                List<MultiDto> multiDtoList = new ArrayList<MultiDto>();
                for (int i = 0; i < scoreStrArry.length; i++) {
                    MultiDto multiDto = new MultiDto();
                    if ("-1".equals(scoreStrArry[i])) {
                        multiDto.setScore("缺");
                    } else if ("-2".equals(scoreStrArry[i])) {
                        multiDto.setScore("");
                    } else {
                        multiDto.setScore(scoreStrArry[i]);
                    }
                    
                    multiDto.setScoreLevel(scoreLevelStrArry[i]);
                    if ("-1".equals(bcArry[i])|| "-2".equals(bcArry[i]) || StringUtils.isBlank(bcArry[i])) {
                        multiDto.setBc("");
                    } else{
                        multiDto.setBc(String.valueOf(new BigDecimal(bcArry[i]).intValue()));
                    }
                    
                    if ("-1".equals(xcArry[i])||"-2".equals(xcArry[i]) || StringUtils.isBlank(xcArry[i])) {
                        multiDto.setXc("");
                    } else {
                        multiDto.setXc(String.valueOf(new BigDecimal(xcArry[i]).intValue()));
                    }
                    
                    multiDtoList.add(multiDto);
                }
                multiPartDto.setMultiDtoList(multiDtoList);
                multiPartDtoList.add(multiPartDto);
                
            }
            map.put(k, multiPartDtoList);
            k++;
            multiAllDto.setMultiPartDtoList(map);
        }
        return multiAllDto;
    }
    
    public void saveMultiInfo(MultiAllDto multiAllDto) throws Exception{
        MultiGroupExamDetailEntry entry = multiGroupExamDetailDao.getEntry(new ObjectId(multiAllDto.getMultiId()));
        List<ScoreRepresentDto> list = this.getScoreRepresentById(new ObjectId(multiAllDto.getMultiId()));
        Map<Integer, List<MultiPartDto>> mp = multiAllDto.getMultiPartDtoList();
        List<ObjectId> cmId = entry.getCommunityId();
        for (int i = 0; i<cmId.size(); i++) {
            List<MultiPartDto> mpd = mp.get(i);
            for (int j =0;j<mpd.size();j++) {
                List<MultiDto> md = mpd.get(j).getMultiDtoList();
                String userId = mpd.get(j).getUserId();
                StringBuffer scoreStr = new StringBuffer();
                
                StringBuffer scoreLevelStr = new StringBuffer();
                StringBuffer bc = new StringBuffer();
                StringBuffer xc = new StringBuffer();
                int ii = 0;
                for (MultiDto m : md) {
                    if((!org.apache.commons.lang.math.NumberUtils.isNumber(m.getScore())) && !"缺".equals(m.getScore().trim()) && StringUtils.isNotBlank(m.getScore())) {
                        throw new Exception("第"+(i+1)+"页"+mpd.get(j).getUserName()+"的第"+(ii+1)+"门分数不是数字");
                    } else if (!"缺".equals(m.getScore().trim()) && StringUtils.isNotBlank(m.getScore()) && new BigDecimal(m.getScore()).compareTo(new BigDecimal(list.get(ii).getMaxScore())) > 0) {
                        throw new Exception("第"+(i+1)+"页"+mpd.get(j).getUserName()+"的第"+(ii+1)+"门分数超最大值");
                    }
                    if (m.getScore().contains("-")) {
                        throw new Exception("第"+(i+1)+"页"+mpd.get(j).getUserName()+"的第"+(ii+1)+"门分数不能为负数");
                    }
                    if ("缺".equals(m.getScore().trim())) {
                        scoreStr.append("-1").append(",");
                    } else if (StringUtils.isBlank(m.getScore())) {
                        scoreStr.append("-2").append(",");
                    } else {
                        scoreStr.append(m.getScore()).append(",");
                    }
                    
                    scoreLevelStr.append(m.getScoreLevel()).append(",");
                    Pattern pattern = Pattern.compile("^[1-9]\\d*$");
                    if (StringUtils.isNotBlank(m.getBc())) {
                        if((!org.apache.commons.lang.math.NumberUtils.isNumber(m.getBc())) && (!"缺".equals(m.getBc().trim()))) {
                            throw new Exception("第"+(i+1)+"页"+mpd.get(j).getUserName()+"的第"+(ii+1)+"门班次必须是正整数");
                        }
                        if ((!pattern.matcher(m.getBc()).matches()) && (!"缺".equals(m.getBc().trim()))) {
                            throw new Exception("第"+(i+1)+"页"+mpd.get(j).getUserName()+"的第"+(ii+1)+"门班次必须是正整数");
                        }
                    }
                    if (StringUtils.isNotBlank(m.getXc())) { 
                        if((!org.apache.commons.lang.math.NumberUtils.isNumber(m.getXc())) && (!"缺".equals(m.getXc().trim()))) {
                            throw new Exception("第"+(i+1)+"页"+mpd.get(j).getUserName()+"的第"+(ii+1)+"门校次必须是正整数");
                        } 
                        if ((!pattern.matcher(m.getXc()).matches()) && (!"缺".equals(m.getXc().trim()))) {
                            throw new Exception("第"+(i+1)+"页"+mpd.get(j).getUserName()+"的第"+(ii+1)+"门校次必须是正整数");
                        }
                    }
                    
                    if ("缺".equals(m.getBc().trim())) {
                        bc.append("-1").append(",");
                    } else if (StringUtils.isBlank(m.getBc())) {
                        bc.append("-1").append(",");
                    } else {
                        bc.append(m.getBc()).append(",");
                    }
                    
                    if ("缺".equals(m.getXc().trim())) {
                        xc.append("-1").append(",");
                    } else if (StringUtils.isBlank(m.getXc())) {
                        xc.append("-1").append(",");
                    } else {
                        xc.append(m.getXc()).append(",");
                    }
                    
                    
                    ii++;
                }
                groupExamUserRecordDao.updateGroupExamUserRecordScoreMultii(new ObjectId(userId), new ObjectId(multiAllDto.getMultiId()), scoreStr.toString(), scoreLevelStr.toString(), bc.toString(), xc.toString());
            }
        }
    }
    
  
    
    public Integer getMultiInfoSize(String multiId) {
        MultiGroupExamDetailEntry entry = multiGroupExamDetailDao.getEntry(new ObjectId(multiId));
        
        List<ObjectId> cmId = entry.getCommunityId();
        return cmId.size();
    }
    
    //生成班级成绩单
    public void createGroupExam(String multiId, String subjectId, String communityId, ObjectId userId) throws Exception{
        MultiGroupExamDetailEntry entry = multiGroupExamDetailDao.getEntry(new ObjectId(multiId));
        List<ObjectId> cmIdList = entry.getCommunityId();
        Map<ObjectId, ObjectId> map = new HashMap<ObjectId, ObjectId>();
        for (ObjectId cmId : cmIdList) {
            GroupEntry ge = groupDao.getByCommunityId(cmId);
            List<ObjectId> list = memberService.getMoreGroupMembers(ge.getID());
            for (ObjectId uid : list) {
                if (memberService.judgeNewPersonPermission(uid) == 5 && uid.equals(userId)) {
                    map.put(cmId, uid);
                }
            }
        }
        for (ObjectId cId : map.keySet()) {
            if (StringUtils.isNotBlank(communityId)) {
                String[] ar = communityId.split(",");
                List<String> list = Arrays.asList(ar);
                if (CollectionUtils.isNotEmpty(list)) {
                    if (list.contains(cId.toString())) {
                        ObjectId userIdd = map.get(cId);
                        //创建成绩基本信息
                        ExamGroupNewDto examGroupDTO = new ExamGroupNewDto();
                        examGroupDTO.setCommunityId(cId.toString());
                        examGroupDTO.setExamName(entry.getExamName());
                        examGroupDTO.setGroupId(groupDao.getByCommunityId(cId).getID().toString());
                        examGroupDTO.setRecordScoreType(entry.getRecordScoreType());
                        examGroupDTO.setSubjectIds(subjectId);
                        examGroupDTO.setExamStrTime(DateTimeUtils.convert(entry.getExamTime(),
                        DateTimeUtils.DATE_YYYY_MM_DD));
                        examGroupDTO.setIsFromPt(1);
                        examGroupDTO.setMultiId(entry.getID().toString());
                        GroupExamDetailDTO groupExamDetailDTO = examGroupDTO.buildDTO();
                        this.saveGroupExamDetail(groupExamDetailDTO, userIdd);
                    }
                }
                
                
            }
            
        }
        
        multiGroupExamDetailDao.updateGroupExamDetailEntry1(new ObjectId(multiId), 1);
    }
    
    public List<SubjectClassDTO> getSubjectList(String multiId) {
        List<SubjectClassDTO> list = new ArrayList<SubjectClassDTO>();
        MultiGroupExamDetailEntry m = multiGroupExamDetailDao.getEntry(new ObjectId(multiId));
        String ids = m.getSubjectIds();
        String[] idArray = ids.split(",");
        for (String s : idArray) {
            SubjectClassEntry entry = subjectClassDao.getEntry(new ObjectId(s));
            SubjectClassDTO subjectClassDto = new SubjectClassDTO(entry);
            list.add(subjectClassDto);
        }
        return list;
    }
    
    public List<CommunityDTO> getCommunityList(String multiId, ObjectId userId) {
        List<CommunityDTO> list = new ArrayList<CommunityDTO>();
        MultiGroupExamDetailEntry entry = multiGroupExamDetailDao.getEntry(new ObjectId(multiId));
        List<ObjectId> cmIdList = entry.getCommunityId();
        Map<ObjectId, ObjectId> map = new HashMap<ObjectId, ObjectId>();
        for (ObjectId cmId : cmIdList) {
            GroupEntry ge = groupDao.getByCommunityId(cmId);
            List<ObjectId> listt = memberService.getMoreGroupMembers(ge.getID());
            for (ObjectId uid : listt) {
                if (memberService.judgeNewPersonPermission(uid) == 5 && uid.equals(userId)) {
                    map.put(cmId, uid);
                }
            }
        }
        for(ObjectId id : map.keySet()) {
            CommunityDTO cd = new CommunityDTO(communityDao.getCommunity(id));
            list.add(cd);
        }
        return list;
    }
    
  
    @Autowired
    private MemberService memberService;
    
}
