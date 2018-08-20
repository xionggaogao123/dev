package com.fulaan.reportCard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.db.business.ModuleTimeDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.IndexPageDao;
import com.db.indexPage.WebHomePageDao;
import com.db.operation.AppNoticeDao;
import com.db.reportCard.*;
import com.db.wrongquestion.ExamTypeDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.dto.VideoDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.fulaan.reportCard.dto.*;
import com.fulaan.user.service.TestTable;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.CollectionUtil;
import com.fulaan.utils.HSSFUtils;
import com.fulaan.wrongquestion.dto.ExamTypeDTO;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
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

    public static void main(String[] args) throws Exception {
        ReportCardNewService reportCardService = new ReportCardNewService();
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
    }
    
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
        //成绩单发送记录
        moduleTimeDao.addEntry(entry.getUserId(),ApplyTypeEn.repordcard.getType(),entry.getCommunityId());
        PictureRunNable.addTongzhi(entry.getCommunityId().toString(), entry.getUserId().toString(), 6);
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
                subjectIds.add(entry.getSubjectId());
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
                    SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(recordEntry.getSubjectId());
                    if (null != subjectClassEntry) {
                        detailDTO.setSubjectName(subjectClassEntry.getName());
                    }
                    if (null != recordEntry.getExamType()) {
                        ExamTypeEntry examTypeEntry = examTypeEntryMap.get(recordEntry.getExamType());
                        if (null != examTypeEntry) {
                            detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
                        }
                    }
                    detailDTO.setScore(recordEntry.getScore());
                    detailDTO.setScoreLevel(recordEntry.getScoreLevel());
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
            Set<ObjectId> userIds = new HashSet<ObjectId>();
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
                        Constant.ZERO
                        ));
                } else {
                    int i = dto.getSubjectIds().split(",").length + 1;
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
                        StringUtils.isNotEmpty(dto.getGroupId()) ? new ObjectId(dto.getGroupId()) : null,
                        StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,
                        dto.getSubjectIds(),
                        StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                        scoreStr.toString(),
                        scoreStr.toString(),
                        0,
                        Constant.ZERO,
                        scoreStrr.toString()
                        ));
                }
                
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
                String[] sa = dto.getSubjectIds().split(",");
                for (int i = 0;i < sa.length; i++) {
                    SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(sa[i]));
                    scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(groupExamDetailId, new ObjectId(sa[i]), sc.getName(), "100", "100", "90", "89", "80", "79", "70", "69", "0",i, Constant.ONE));
                }
                int i = sa.length;
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(groupExamDetailId,  "总分", multiply(100, i), multiply(100, i), multiply(90, i), multiplyJy(90, i), multiply(80, i), multiplyJy(80, i), multiply(70, i), multiply(70, i), "0", 50,Constant.ONE));
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
                            scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(id),  "总分", multiply(100, i), multiply(100, i), multiply(90, i), multiplyJy(90, i), multiply(80, i), multiplyJy(80, i), multiply(70, i), multiply(70, i), "0", 50,Constant.ONE));
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
                
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(sr.getId()),new ObjectId(sr.getGroupExamDetailId()), new ObjectId(sr.getSubjectId()) ,sr.getSubjectName() , sr.getMaxScore(), sr.getScoreOne(), sr.getScoreTwo(), sr.getScoreThree(), sr.getScoreFour(), sr.getScoreFive(), sr.getScoreSix(), sr.getScoreSeven(), sr.getScoreEight(), Integer.valueOf(sr.getSort()), Integer.valueOf(sr.getRepresentNameType())));
            } else {
                scoreRepresentDao.saveScoreRepresent(new ScoreRepresentEntry(new ObjectId(sr.getGroupExamDetailId()), sr.getSubjectName() , sr.getMaxScore(), sr.getScoreOne(), sr.getScoreTwo(), sr.getScoreThree(), sr.getScoreFour(), sr.getScoreFive(), sr.getScoreSix(), sr.getScoreSeven(), sr.getScoreEight(), Integer.valueOf(sr.getSort()), Integer.valueOf(sr.getRepresentNameType()),new ObjectId(sr.getId())));
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
                groupExamUserRecordDao.updateGroupExamUserRecordScoreNew(new ObjectId(dto.getId()),
                        dto.getScoreStr(), dto.getScoreLevelStr(), dto.getRankStr());
            }
            List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamDetailId), -1, -1, -1, 1);
            examScoreDTOs.clear();
            for (GroupExamUserRecordEntry entry : recordEntries) {
                examScoreDTOs.add(new GroupExamUserRecordDTO(entry,1));
            }
            GroupExamDetailEntry detailEntry = groupExamDetailDao.getEntryById(new ObjectId(groupExamDetailId));
           
            examScoreDTOs = this.tran(examScoreDTOs, detailEntry);
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                groupExamUserRecordDao.updateGroupExamUserRecordScoreNew(new ObjectId(dto.getId()),
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
            if(status==2 && isSend!=1){
                redDotService.addThirdList(detailEntry.getID(),detailEntry.getCommunityId(), detailEntry.getUserId(), ApplyTypeEn.repordcard.getType());
                PictureRunNable.addTongzhi(detailEntry.getCommunityId().toString(), detailEntry.getUserId().toString(), 6);
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

    public GroupExamDetailDTO getGroupExamDetail(ObjectId singleId) {
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

    public GroupExamDetailDTO getTeacherGroupExamDetail(ObjectId groupExamDetailId) {
        
        GroupExamDetailDTO detailDTO = new GroupExamDetailDTO();
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(groupExamDetailId);
        if (null != detailEntry) {
            detailDTO = new GroupExamDetailDTO(detailEntry);
            //detailDTO.setSignCount(this.calcuteSignCount(groupExamDetailId));
            detailDTO.setSignCount(this.getMyRoleList4(detailEntry.getCommunityId(), detailEntry.getUserId()));
            SubjectClassEntry subjectClassEntry = subjectClassDao.getEntry(detailEntry.getSubjectId());
            if (null != subjectClassEntry) {
                detailDTO.setSubjectName(subjectClassEntry.getName());
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
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(groupExamDetailId);
                if (null != evaluateEntry) {
                    /*detailDTO.setExcellentPercent(evaluateEntry.getExcellentPercent());
                    detailDTO.setQualifyPercent(evaluateEntry.getQualifyPercent());
                    detailDTO.setUnQualifyPercent(evaluateEntry.getUnQualifyPercent());
                    detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                    detailDTO.setGroupMaxScore(evaluateEntry.getMaxScore());
                    detailDTO.setGroupMinScore(evaluateEntry.getMinScore());*/
                    detailDTO.setExcellentPercentList(Arrays.asList(evaluateEntry.getExcellentPercentStr().split(","))); 
                    detailDTO.setQualifyPercentList(Arrays.asList(evaluateEntry.getQualifyPercentStr().split(",")));
                    detailDTO.setUnQualifyPercentList(Arrays.asList(evaluateEntry.getUnQualifyPercentStr().split(",")));
                    detailDTO.setAvgScoreList(Arrays.asList(evaluateEntry.getAvgScoreStr().split(",")));
                    detailDTO.setGroupMaxScoreList(Arrays.asList(evaluateEntry.getMaxScoreStr().split(",")));
                    detailDTO.setGroupMinScoreList(Arrays.asList(evaluateEntry.getMinScoreStr().split(",")));
                    
                }
            } else {
                RecordLevelEvaluateEntry levelEvaluateEntry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(groupExamDetailId);
                if (null != levelEvaluateEntry) {
                    /*detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                    detailDTO.setbPercent(levelEvaluateEntry.getBpercent());
                    detailDTO.setcPercent(levelEvaluateEntry.getCpercent());
                    detailDTO.setdPercent(levelEvaluateEntry.getDpercent());*/
                    detailDTO.setaPercentList(Arrays.asList(levelEvaluateEntry.getAPercentStr().split(",")));
                    detailDTO.setbPercentList(Arrays.asList(levelEvaluateEntry.getBPercentStr().split(",")));
                    detailDTO.setcPercentList(Arrays.asList(levelEvaluateEntry.getCPercentStr().split(",")));
                    detailDTO.setdPercentList(Arrays.asList(levelEvaluateEntry.getDPercentStr().split(",")));
                }
            }
            groupExamDetailDao.updateSignCount(groupExamDetailId, this.getMyRoleList4(detailEntry.getCommunityId(),detailEntry.getUserId()));
            //参考人数
            
            List<Integer> examCountList = new ArrayList<Integer>();
            //未填写人数
            
            List<Integer> unCompleteCountList = new ArrayList<Integer>();
            
            
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
                    for(GroupExamUserRecordEntry g : recordEntries) {
                        String[] s = g.getScoreStr().split(",");
                        if (!(("-1").equals(s[i])||("-2").equals(s[i])) ) {
                            examCount++;
                        } 
                        if (("-1").equals(s[i])) {
                            unCompleteCount++;
                        }
                    }
                    examCountList.add(examCount);
                    unCompleteCountList.add(unCompleteCount);
                }
            } else {
                for (int i = 0; i<recordEntries.get(0).getScoreStr().split(",").length; i++) {
                    int examCount = 0;
                    int unCompleteCount = 0;
                    for(GroupExamUserRecordEntry g : recordEntries) {
                        String[] s = g.getScoreLevelStr().split(",");
                        if (!(("-1").equals(s[i])||("-2").equals(s[i])) ) {
                            examCount++;
                        } 
                        if (("-1").equals(s[i])) {
                            unCompleteCount++;
                        }
                    }
                    examCountList.add(examCount);
                    unCompleteCountList.add(unCompleteCount);
                }
            }
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
            cell.setCellValue("用户姓名");

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
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("关键字Id");

            cell = row.createCell(1);
            cell.setCellValue("考试Id");

            cell = row.createCell(2);
            cell.setCellValue("用户姓名");

            /*cell = row.createCell(3);
            cell.setCellValue("用户学号");*/

            for (GroupExamUserRecordDTO g : recordDTOs) {
                String[] ss = g.getSubjectId().split(",");
                for (int i =0;i<ss.length;i++) {
                    SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(ss[i]));
                    cell = row.createCell(3+(i*2));
                    cell.setCellValue(sc.getName()+"等第分值");

                    cell = row.createCell(4+(i*2));
                    cell.setCellValue(sc.getName()+"考试分值");
                }
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

                cellItem = rowItem.createCell(0);
                cellItem.setCellValue(recordDTO.getId());

                cellItem = rowItem.createCell(1);
                cellItem.setCellValue(recordDTO.getGroupExamDetailId());

                cellItem = rowItem.createCell(2);
                cellItem.setCellValue(recordDTO.getUserName());

                /*cellItem = rowItem.createCell(3);
                cellItem.setCellValue(recordDTO.getUserNumber());*/
                
                for (GroupExamUserRecordDTO g : recordDTOs) {
                    String[] ss = g.getSubjectId().split(",");
                    for (int i =0;i<ss.length;i++) {
                        if (detailEntry.getRecordScoreType() == Constant.ONE) {
                            cellItem = rowItem.createCell(3+(i*2));
                            cellItem.setCellValue(-1);

                            cellItem = rowItem.createCell(4+(i*2));
                            cellItem.setCellValue("");
                        } else {
                            cellItem = rowItem.createCell(3+(i*2));
                            cellItem.setCellValue("");

                            cellItem = rowItem.createCell(4+(i*2));
                            cellItem.setCellValue(-1);
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


    public void importTemplate(String groupExamId,InputStream inputStream) throws Exception {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
        int rowNum = sheet.getLastRowNum();
        List<GroupExamUserRecordDTO> examScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
        List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamId), -1, -1, -1, 1);
        int i = recordEntries.get(0).getSubjectIds().split(",").length;
        for (int j = 2; j <= rowNum; j++) {
            GroupExamUserRecordDTO item = new GroupExamUserRecordDTO();
            String id = sheet.getRow(j).getCell(0).getStringCellValue();
            String groupExamDetailId = sheet.getRow(j).getCell(1).getStringCellValue();
            item.setGroupExamDetailId(groupExamDetailId);
            item.setId(id);
            StringBuffer sb = new StringBuffer();
            StringBuffer sbb = new StringBuffer();
            for(int k =0;k <= i;k++) {
                HSSFCell cell = sheet.getRow(j).getCell(4+(2*k));
                double score = getValue(cell);
                if (score == -1.0) {
                    sb.append("-1").append(",");
                } else if(score == -2.0) {
                    sb.append("-2").append(",");
                }else {
                    sb.append(String.valueOf(score)).append(",");
                }
                
                //item.setScore(score);
                HSSFCell hssfCell = sheet.getRow(j).getCell(3+(2*k));
                int scoreLevel = (new Double(getValue(hssfCell))).intValue();
                sbb.append(String.valueOf(scoreLevel)).append(",");
                //item.setScoreLevel(scoreLevel);
            }
            item.setScoreStr(sb.toString());
            item.setScoreLevelStr(sbb.toString());
            item.setRank(Constant.ZERO);
            examScoreDTOs.add(item);
        }
        if(examScoreDTOs.size()>0){
            String examGroupId = examScoreDTOs.get(0).getGroupExamDetailId();
            if(!groupExamId.equals(examGroupId)){
                throw  new Exception("上传的不是这次考试的成绩!");
            }
        }
        if (examScoreDTOs.size() > 0) {
            saveRecordExamScore(examScoreDTOs, Constant.ZERO,0);
            increaseVersion(new ObjectId(examScoreDTOs.get(0).getGroupExamDetailId()));
        }
    }

    public double getValue(HSSFCell cell) {
        double cellValue = -1;
        if (cell != null) {
            String vvv = getStringCellValue(cell);
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
    public double getValueByPrint(String levelScore) {
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

    private String getStringCellValue(HSSFCell cell) {
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
}
