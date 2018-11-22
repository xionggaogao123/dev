package com.fulaan.appvote.service;

import com.db.appvote.AppNewVoteDao;
import com.db.appvote.AppVoteDao;
import com.db.appvote.AppVoteOptionDao;
import com.db.backstage.TeacherApproveDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.MineCommunityDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.forum.FVoteDao;
import com.db.indexPage.IndexContentDao;
import com.db.indexPage.IndexPageDao;
import com.db.instantmessage.RedDotDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.appvote.dto.AppNewVoteDTO;
import com.fulaan.appvote.dto.AppVoteOptionDTO;
import com.fulaan.forum.service.FVoteService;
import com.fulaan.indexpage.dto.IndexContentDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.User;
import com.pojo.appvote.AppNewVoteEntry;
import com.pojo.appvote.AppVoteEntry;
import com.pojo.appvote.AppVoteOptionEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.fcommunity.*;
import com.pojo.forum.FVoteDTO;
import com.pojo.indexPage.IndexContentEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.instantmessage.RedDotEntry;
import com.pojo.integral.IntegralType;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.TimeChangeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by James on 2018-10-29.
 */
@Service
public class AppNewVoteService {

    private AppNewVoteDao appNewVoteDao = new AppNewVoteDao();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private CommunityDao communityDao = new CommunityDao();

    private IndexContentDao indexContentDao = new IndexContentDao();

    private MemberDao memberDao = new MemberDao();

    private AppVoteOptionDao appVoteOptionDao = new AppVoteOptionDao();
    @Autowired
    private RedDotService redDotService;
    @Autowired
    private IntegralSufferService integralSufferService;

    private NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();

    private MineCommunityDao mineCommunityDao = new MineCommunityDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private UserDao userDao = new UserDao();

    private AppVoteDao appVoteDao = new AppVoteDao();

    private FVoteDao fVoteDao = new FVoteDao();

    private RedDotDao redDotDao = new RedDotDao();

    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();
    @Autowired
    private FVoteService fVoteService;

    public String saveNewAppVote(AppNewVoteDTO appNewVoteDTO,ObjectId userId){
        AppNewVoteEntry appNewVoteEntry = appNewVoteDTO.buildAddEntry();
        appNewVoteDao.saveAppVote(appNewVoteEntry);
        //添加选项
        long current = System.currentTimeMillis();
        List<String> option = appNewVoteDTO.getOption();
        if(appNewVoteDTO.getType()==1 && option!=null && option.size()>0){
            int index = 10;
            for(String s:option){
                index++;
                current = index +current;
                AppVoteOptionEntry appVoteOptionEntry = new AppVoteOptionEntry(
                        appNewVoteEntry.getID(),
                        s,
                        userId,
                        Constant.ONE,
                        Constant.ONE,
                        new ArrayList<AttachmentEntry>(),
                        new ArrayList<AttachmentEntry>(),
                        new ArrayList<VideoEntry>(),
                        new ArrayList<AttachmentEntry>(),
                        Constant.ZERO,
                        current,
                        new ArrayList<ObjectId>());
                appVoteOptionDao.saveAppVote(appVoteOptionEntry);
            }
        }
        //添加附加信息
        if(appNewVoteDTO.getVoteTypeList().contains(new Integer("2")) ||
                appNewVoteDTO.getApplyTypeList().contains(new Integer("2")) ||
                appNewVoteDTO.getVoteTypeList().contains(new Integer("3")) ||
                appNewVoteDTO.getApplyTypeList().contains(new Integer("3"))){ //投票或报名 有 家长或老师
            //发送通知
            List<String> communityIds3 = appNewVoteDTO.getCommunityList();
            List<ObjectId> communityIds2 = new ArrayList<ObjectId>();
            if(communityIds3!=null){
                for(String st:communityIds3){
                    communityIds2.add(new ObjectId(st));
                }
            }
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(communityIds2);
            List<String> cids = new ArrayList<String>();
            StringBuffer sb2 = new StringBuffer();
            List<ObjectId> groupIds = new ArrayList<ObjectId>();
            List<ObjectId> communityIds = new ArrayList<ObjectId>();
            for(CommunityEntry communityEntry : communityEntries){
                sb2.append(communityEntry.getCommunityName());
                sb2.append("、");
                cids.add(communityEntry.getID().toString());
                groupIds.add(communityEntry.getGroupId());
                communityIds.add(communityEntry.getID());
            }
            if(communityIds.size()>0){
                for(ObjectId communityId : communityIds){
                    PictureRunNable.addTongzhi(communityId.toString(), appNewVoteDTO.getUserId(), 5);
                    cids.add(communityId.toString());
                }
            }
            String sb = sb2.toString().substring(0, sb2.length() - 1);
            //新首页记录
            IndexPageDTO dto1 = new IndexPageDTO();
            dto1.setType(CommunityType.newVote.getType());
            dto1.setUserId(appNewVoteDTO.getUserId());
            dto1.setReceiveIdList(cids);
            dto1.setCommunityId(null);
            dto1.setContactId(appNewVoteEntry.getID().toString());
            //角色列表
            Set<Integer> set = new HashSet<Integer>();
            set.addAll(appNewVoteEntry.getApplyTypeList());
            set.addAll(appNewVoteEntry.getVoteTypeList());
            List<Integer> roleList =  new ArrayList<Integer>();
            roleList.addAll(set);
            dto1.setRoleList(roleList);
            IndexPageEntry entry = dto1.buildAddEntry();
            indexPageDao.addEntry(entry);
            IndexContentDTO indexContentDTO = new IndexContentDTO(
                    appNewVoteDTO.getSubjectName(),
                    "通知-投票",
                    appNewVoteDTO.getTitle(),
                    appNewVoteDTO.getVideoList(),
                    appNewVoteDTO.getImageList(),
                    appNewVoteDTO.getAttachements(),
                    appNewVoteDTO.getVoiceList(),
                    sb.toString(),
                    "");
            Set<ObjectId> members=memberDao.getAllCommunityMemberIds(groupIds);
            IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(appNewVoteDTO.getUserId(),appNewVoteDTO.getSubjectId(), null,null,1);
            indexContentEntry.setReadList(new ArrayList<ObjectId>());
            indexContentEntry.setContactId(appNewVoteEntry.getID());
            indexContentEntry.setContactType(7);
            indexContentEntry.setAllCount(members.size());
            indexContentDao.addEntry(indexContentEntry);
            //添加红点
            redDotService.addOtherEntryList(communityIds, new ObjectId(appNewVoteDTO.getUserId()), ApplyTypeEn.piao.getType(), 1);
        }
        int score = integralSufferService.addIntegral(new ObjectId(appNewVoteDTO.getUserId()), IntegralType.vote,4,1);
        return score+"";
    }

    public List<ObjectId> getCommunitys3(ObjectId uid, int page, int pageSize) {
        List<MineCommunityEntry> allMineCommunitys = mineCommunityDao.findAll(uid, page, pageSize);
        List<ObjectId> list = new ArrayList<ObjectId>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (MineCommunityEntry mineCommunityEntry : allMineCommunitys) {
            communityIds.add(mineCommunityEntry.getCommunityId());
        }
        List<CommunityEntry> communityEntries = communityDao.findByNotObjectIds(communityIds);
        for(CommunityEntry communityEntry:communityEntries){
            list.add(communityEntry.getID());
        }
        return list;
    }


    //查询列表   l   学生       2    家长      3 老师
    public Map<String,Object> getVoteList(ObjectId userId,String communityId,String keyword,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得查询者的身份
        int role = 2;//默认家长
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry !=null && teacherApproveEntry.getType()==2){
            role = 3;//默认老师
        }
        List<ObjectId> communityIds =  new ArrayList<ObjectId>();
        if(communityId==null || communityId.equals("")){
            communityIds = this.getCommunitys3(userId, 1, 100);
        }else{
            communityIds.add(new ObjectId(communityId));
        }
        List<AppNewVoteEntry> appNewVoteEntries = appNewVoteDao.getVoteList(userId,keyword,communityIds,page,pageSize,role);
        int count = appNewVoteDao.countVoteList(userId,keyword,communityIds,role);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> cids = new ArrayList<ObjectId>();
        for(AppNewVoteEntry appNewVoteEntry: appNewVoteEntries){
            userIds.add(appNewVoteEntry.getUserId());
            cids.addAll(appNewVoteEntry.getCommunityList());
        }
        Map<ObjectId,CommunityEntry> nap = communityDao.findMapByObjectIds(cids);
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        List<AppNewVoteDTO> dtos = new ArrayList<AppNewVoteDTO>();
        long current = System.currentTimeMillis();
        for(AppNewVoteEntry appNewVoteEntry: appNewVoteEntries){
            AppNewVoteDTO dto = new AppNewVoteDTO(appNewVoteEntry);
            //1.发布人数据
            UserEntry userEntry  = userEntryMap.get(appNewVoteEntry.getUserId());
            if(userEntry!=null){
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                dto.setUserName(name);
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
            }
            //2.获得投票阶段
            if(appNewVoteEntry.getApplyStartTime()< current && appNewVoteEntry.getApplyEndTime()>current){//报名阶段
                dto.setLevel(1);
            }else if(appNewVoteEntry.getVoteStartTime()< current && appNewVoteEntry.getVoteEndTime()>current){//投票阶段
                dto.setLevel(2);
            }else if(appNewVoteEntry.getVoteEndTime()<current){//结束阶段
                dto.setLevel(3);
            }else{//未开始阶段
                dto.setLevel(0);
            }

            //3.判断阶段
            if(appNewVoteEntry.getUserId().equals(userId)){//发布人
                dto.setIsOwner(1);
                dto.setIsApply(0);
                dto.setIsVote(0);
            }else{//非发布人
                dto.setIsOwner(0);
                dto.setIsApply(0);
                dto.setIsVote(0);
                if(appNewVoteEntry.getType()==2 && appNewVoteEntry.getApplyTypeList()!=null && appNewVoteEntry.getApplyTypeList().contains(new Integer(role))) {//可报名
                    if (appNewVoteEntry.getApplyUserList() != null && appNewVoteEntry.getApplyUserList().contains(userId)) {//已报名
                        dto.setIsApply(2);
                    } else {//未报名
                        dto.setIsApply(1);
                    }
                }
                if(appNewVoteEntry.getVoteTypeList()!=null && appNewVoteEntry.getVoteTypeList().contains(new Integer(role))){//可投票
                    if(appNewVoteEntry.getVoteUesrList()!=null && appNewVoteEntry.getVoteUesrList().contains(userId)){//已投票
                        dto.setIsVote(2);
                    }else{//未投票
                        dto.setIsVote(1);
                    }
                }

            }
            List<ObjectId> comIds = appNewVoteEntry.getCommunityList();
            StringBuffer sb = new StringBuffer();
            if(comIds!=null){
                for(ObjectId oid:comIds){
                    CommunityEntry communityEntry = nap.get(oid);
                    if(communityEntry!=null){
                        sb.append(communityEntry.getCommunityName());
                        sb.append("、");
                    }
                }
            }
            String cs = sb.toString();
            if(cs.length()>0){
                cs = cs.substring(0,cs.length()-1);
            }
            dto.setCommunityNames(cs);
            dto.setTimeExpression(TimeChangeUtils.getChangeTime(appNewVoteEntry.getCreateTime()));
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }

    public Map<String,Object> getStudentVoteList(ObjectId userId,String communityId,String keyword,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得查询者的身份
        int role = 1;//默认学生
        List<ObjectId> communityIds =  new ArrayList<ObjectId>();
        NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(userId);
        if(communityId==null || communityId.equals("")){
            List<ObjectId> oids = newVersionCommunityBindDao.getEntries(newVersionBindRelationEntry.getMainUserId(),userId);
            communityIds.addAll(oids);
        }else{
            communityIds.add(new ObjectId(communityId));
        }
        List<AppNewVoteEntry> appNewVoteEntries = appNewVoteDao.getVoteList(userId,keyword,communityIds,page,pageSize,role);
        int count = appNewVoteDao.countVoteList(userId,keyword,communityIds,role);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> cids = new ArrayList<ObjectId>();
        for(AppNewVoteEntry appNewVoteEntry: appNewVoteEntries){
            userIds.add(appNewVoteEntry.getUserId());
            cids.addAll(appNewVoteEntry.getCommunityList());
        }
        Map<ObjectId,CommunityEntry> nap = communityDao.findMapByObjectIds(cids);
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        List<AppNewVoteDTO> dtos = new ArrayList<AppNewVoteDTO>();
        long current = System.currentTimeMillis();
        for(AppNewVoteEntry appNewVoteEntry: appNewVoteEntries){
            AppNewVoteDTO dto = new AppNewVoteDTO(appNewVoteEntry);
            //1.发布人数据
            UserEntry userEntry  = userEntryMap.get(appNewVoteEntry.getUserId());
            if(userEntry!=null){
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                dto.setUserName(name);
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
            }
            //2.获得投票阶段
            if(appNewVoteEntry.getApplyStartTime()< current && appNewVoteEntry.getApplyEndTime()>current){//报名阶段
                dto.setLevel(1);
            }else if(appNewVoteEntry.getVoteStartTime()< current && appNewVoteEntry.getVoteEndTime()>current){//投票阶段
                dto.setLevel(2);
            }else if(appNewVoteEntry.getVoteEndTime()<current){//结束阶段
                dto.setLevel(3);
            }else{//未开始阶段
                dto.setLevel(0);
            }

            //3.判断阶段
            if(appNewVoteEntry.getUserId().equals(userId)){//发布人
                dto.setIsOwner(1);
                dto.setIsApply(0);
                dto.setIsVote(0);
            }else{//非发布人
                dto.setIsOwner(0);
                dto.setIsApply(0);
                dto.setIsVote(0);
                if(appNewVoteEntry.getType()==2 && appNewVoteEntry.getApplyTypeList()!=null && appNewVoteEntry.getApplyTypeList().contains(new Integer(role))) {//可报名
                    if (appNewVoteEntry.getApplyUserList() != null && appNewVoteEntry.getApplyUserList().contains(userId)) {//已报名
                        dto.setIsApply(2);
                    } else {//未报名
                        dto.setIsApply(1);
                    }
                }
                if(appNewVoteEntry.getVoteTypeList()!=null && appNewVoteEntry.getVoteTypeList().contains(new Integer(role))){//可投票
                    if(appNewVoteEntry.getVoteUesrList()!=null && appNewVoteEntry.getVoteUesrList().contains(userId)){//已投票
                        dto.setIsVote(2);
                    }else{//未投票
                        dto.setIsVote(1);
                    }
                }

            }
            List<ObjectId> comIds = appNewVoteEntry.getCommunityList();
            StringBuffer sb = new StringBuffer();
            if(comIds!=null){
                for(ObjectId oid:comIds){
                    CommunityEntry communityEntry = nap.get(oid);
                    if(communityEntry!=null){
                        sb.append(communityEntry.getCommunityName());
                        sb.append("、");
                    }
                }
            }
            String cs = sb.toString();
            if(cs.length()>0){
                cs = cs.substring(0,cs.length()-1);
            }
            dto.setCommunityNames(cs);
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }


    //查询投票详情
    public Map<String,Object> getOneVote(ObjectId userId,ObjectId id) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        AppNewVoteDTO appNewVoteDTO = new AppNewVoteDTO(appNewVoteEntry);
        //1. 发布人数据
        UserEntry userEntry = userDao.findByUserId(appNewVoteEntry.getUserId());
        if(userEntry!=null){
            appNewVoteDTO.setUserName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            appNewVoteDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
        }
        //获得查询者的身份
        int role = 2;//默认家长
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry !=null && teacherApproveEntry.getType()==2){
            role = 3;//默认老师
        }else{
            NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
            if(newVersionUserRoleEntry!=null && newVersionUserRoleEntry.getNewRole()==2){
                role = 1;//默认学生
            }
        }
        //2. 获得投票阶段
        long current = System.currentTimeMillis();
        if(appNewVoteEntry.getApplyStartTime()< current && appNewVoteEntry.getApplyEndTime()>current){//报名阶段
            appNewVoteDTO.setLevel(1);
        }else if(appNewVoteEntry.getVoteStartTime()< current && appNewVoteEntry.getVoteEndTime()>current){//投票阶段
            appNewVoteDTO.setLevel(2);
        }else if(appNewVoteEntry.getVoteEndTime()<current){//结束阶段
            appNewVoteDTO.setLevel(3);
        }else{//未开始阶段
            appNewVoteDTO.setLevel(0);
        }
        //3. 判断阶段
        if(appNewVoteEntry.getUserId().equals(userId)){//发布人
            appNewVoteDTO.setIsOwner(1);
            appNewVoteDTO.setIsApply(0);
            appNewVoteDTO.setIsVote(0);
        }else{//非发布人
            appNewVoteDTO.setIsOwner(0);
            appNewVoteDTO.setIsApply(0);
            appNewVoteDTO.setIsVote(0);
            if(appNewVoteEntry.getType()==2 && appNewVoteEntry.getApplyTypeList()!=null && appNewVoteEntry.getApplyTypeList().contains(new Integer(role))) {//可报名
                if (appNewVoteEntry.getApplyUserList() != null && appNewVoteEntry.getApplyUserList().contains(userId)) {//已报名
                    appNewVoteDTO.setIsApply(2);
                } else {//未报名
                    appNewVoteDTO.setIsApply(1);
                }
            }


            if(appNewVoteEntry.getVoteTypeList()!=null && appNewVoteEntry.getVoteTypeList().contains(new Integer(role))){//可投票

                if(appNewVoteEntry.getVoteUesrList()!=null){
                    appNewVoteDTO.setUserCount(appNewVoteEntry.getVoteUesrList().size());
                    if(appNewVoteEntry.getVoteUesrList().contains(userId)){//已投票
                        appNewVoteDTO.setIsVote(2);
                    }else{//未投票
                        appNewVoteDTO.setIsVote(1);
                    }
                }else{
                    appNewVoteDTO.setUserCount(0);
                    appNewVoteDTO.setIsVote(1);
                }
            }

        }
        List<ObjectId> objectIdList = appNewVoteEntry.getCommunityList();
        StringBuffer sb = new StringBuffer();
        if(objectIdList!=null){
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(objectIdList);
            for(CommunityEntry communityEntry:communityEntries){
                sb.append(communityEntry.getCommunityName());
                sb.append("、");
            }
        }
        String cs = sb.toString();
        if(cs.length()>0){
            cs = cs.substring(0,cs.length()-1);
            appNewVoteDTO.setCommunityNames(cs);
        }else{
            appNewVoteDTO.setCommunityNames("");
        }

        //4. 组装选项
        List<AppVoteOptionEntry> appVoteOptionEntries = appVoteOptionDao.getOneVoteList(id);
        List<AppVoteOptionDTO> selectOption = new ArrayList<AppVoteOptionDTO>();
       // List<AppVoteOptionDTO> unSelectOption = new ArrayList<AppVoteOptionDTO>();
        int allCount = 0;
        for(AppVoteOptionEntry appVoteOptionEntry: appVoteOptionEntries){
            AppVoteOptionDTO appVoteOptionDTO = new AppVoteOptionDTO(appVoteOptionEntry);
            if(appVoteOptionEntry.getUserIdList()!=null && appVoteOptionEntry.getUserIdList().contains(userId)){
                appVoteOptionDTO.setIsSelect(1);
            }else{
                appVoteOptionDTO.setIsSelect(0);
            }
//            if(appVoteOptionEntry.getSelect()==1){//可选项
            selectOption.add(appVoteOptionDTO);
            allCount += appVoteOptionDTO.getCount();
            /*}else{//待选项
                unSelectOption.add(appVoteOptionDTO);
            }*/
        }
        for(AppVoteOptionDTO appVoteOptionDTO:selectOption){
            appVoteOptionDTO.setPercent(Double.parseDouble(division(appVoteOptionDTO.getCount(), allCount)));
        }
        map.put("dto",appNewVoteDTO);
        map.put("optionList",selectOption);
        //map.put("unSelectOption",unSelectOption);
        return map;
    }

    //查询投票详情
    public Map<String,Object> getWebOneVote(ObjectId userId,ObjectId id) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        AppNewVoteDTO appNewVoteDTO = new AppNewVoteDTO(appNewVoteEntry);
        //1. 发布人数据
        UserEntry userEntry = userDao.findByUserId(appNewVoteEntry.getUserId());
        if(userEntry!=null){
            appNewVoteDTO.setUserName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            appNewVoteDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
        }
        //获得查询者的身份
        int role = 2;//默认家长
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry !=null && teacherApproveEntry.getType()==2){
            role = 3;//默认老师
        }else{
            NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
            if(newVersionUserRoleEntry!=null && newVersionUserRoleEntry.getNewRole()==2){
                role = 1;//默认学生
            }
        }
        //2. 获得投票阶段
        long current = System.currentTimeMillis();
        if(appNewVoteEntry.getApplyStartTime()< current && appNewVoteEntry.getApplyEndTime()>current){//报名阶段
            appNewVoteDTO.setLevel(1);
        }else if(appNewVoteEntry.getVoteStartTime()< current && appNewVoteEntry.getVoteEndTime()>current){//投票阶段
            appNewVoteDTO.setLevel(2);
        }else if(appNewVoteEntry.getVoteEndTime()<current){//结束阶段
            appNewVoteDTO.setLevel(3);
        }else{//未开始阶段
            appNewVoteDTO.setLevel(0);
        }
        //3. 判断阶段
        if(appNewVoteEntry.getUserId().equals(userId)){//发布人
            appNewVoteDTO.setIsOwner(1);
            appNewVoteDTO.setIsApply(0);
            appNewVoteDTO.setIsVote(0);
        }else{//非发布人
            appNewVoteDTO.setIsOwner(0);
            appNewVoteDTO.setIsApply(0);
            appNewVoteDTO.setIsVote(0);
            if(appNewVoteEntry.getType()==2 && appNewVoteEntry.getApplyTypeList()!=null && appNewVoteEntry.getApplyTypeList().contains(new Integer(role))) {//可报名
                if (appNewVoteEntry.getApplyUserList() != null && appNewVoteEntry.getApplyUserList().contains(userId)) {//已报名
                    appNewVoteDTO.setIsApply(2);
                } else {//未报名
                    appNewVoteDTO.setIsApply(1);
                }
            }


            if(appNewVoteEntry.getVoteTypeList()!=null && appNewVoteEntry.getVoteTypeList().contains(new Integer(role))){//可投票

                if(appNewVoteEntry.getVoteUesrList()!=null){
                    appNewVoteDTO.setUserCount(appNewVoteEntry.getVoteUesrList().size());
                    if(appNewVoteEntry.getVoteUesrList().contains(userId)){//已投票
                        appNewVoteDTO.setIsVote(2);
                    }else{//未投票
                        appNewVoteDTO.setIsVote(1);
                    }
                }else{
                    appNewVoteDTO.setUserCount(0);
                    appNewVoteDTO.setIsVote(1);
                }
            }

        }
        List<ObjectId> objectIdList = appNewVoteEntry.getCommunityList();
        StringBuffer sb = new StringBuffer();
        if(objectIdList!=null){
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(objectIdList);
            for(CommunityEntry communityEntry:communityEntries){
                sb.append(communityEntry.getCommunityName());
                sb.append("、");
            }
        }
        String cs = sb.toString();
        if(cs.length()>0){
            cs = cs.substring(0,cs.length()-1);
            appNewVoteDTO.setCommunityNames(cs);
        }else{
            appNewVoteDTO.setCommunityNames("");
        }

        //4. 组装选项
        List<AppVoteOptionEntry> appVoteOptionEntries = appVoteOptionDao.getOneVoteList(id);
        List<AppVoteOptionDTO> selectOption = new ArrayList<AppVoteOptionDTO>();
        // List<AppVoteOptionDTO> unSelectOption = new ArrayList<AppVoteOptionDTO>();
        int allCount = 0;
        List<ObjectId> uids = new ArrayList<ObjectId>();
        if(appNewVoteEntry.getVoteUesrList()!=null){
            uids.addAll(appNewVoteEntry.getVoteUesrList());
        }
        Map<ObjectId, UserEntry> map2 = userDao.getUserEntryMap(uids, Constant.FIELDS);
        for(AppVoteOptionEntry appVoteOptionEntry: appVoteOptionEntries){
            AppVoteOptionDTO appVoteOptionDTO = new AppVoteOptionDTO(appVoteOptionEntry);
            if(appVoteOptionEntry.getUserIdList()!=null){
                if(appVoteOptionEntry.getUserIdList().contains(userId)){
                    appVoteOptionDTO.setIsSelect(1);
                }else{
                    appVoteOptionDTO.setIsSelect(0);
                }
            }else{
                appVoteOptionDTO.setIsSelect(0);
            }
            StringBuffer sb2 = new StringBuffer();
            if(appVoteOptionEntry.getUserIdList()!=null){
                for(ObjectId oid : appVoteOptionEntry.getUserIdList()){
                    UserEntry userEntry1 = map2.get(oid);
                    if(userEntry1!=null){
                        sb2.append(StringUtils.isNotBlank(userEntry1.getNickName())?userEntry1.getNickName():userEntry1.getUserName());
                        sb2.append("、#");
                    }
                }
            }
            appVoteOptionDTO.setUserNames(sb2.toString());
//            if(appVoteOptionEntry.getSelect()==1){//可选项
            selectOption.add(appVoteOptionDTO);
            allCount += appVoteOptionDTO.getCount();
            /*}else{//待选项
                unSelectOption.add(appVoteOptionDTO);
            }*/
        }
        for(AppVoteOptionDTO appVoteOptionDTO:selectOption){
            appVoteOptionDTO.setPercent(Double.parseDouble(division(appVoteOptionDTO.getCount(), allCount)));
        }
        map.put("dto",appNewVoteDTO);
        map.put("optionList",selectOption);
        //map.put("unSelectOption",unSelectOption);
        return map;
    }

    //整数相除 保留一位小数
    public static String division(int a ,int b){
        if(b==0){
            return "0";
        }
        String result = "";
        float num =(float)a/b;

        DecimalFormat df = new DecimalFormat("0.0");

        result = df.format(num);

        return result;

    }


    public void voteMyOption(ObjectId userId,ObjectId id,String optionIds)throws Exception{
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        //获得查询者的身份
        int role = 2;//默认家长
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry !=null && teacherApproveEntry.getType()==2){
            role = 3;//默认老师
        }else{
            NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
            if(newVersionUserRoleEntry!=null && newVersionUserRoleEntry.getNewRole()==2){
                role = 1;//默认学生
            }
        }
        if(appNewVoteEntry.getVoteTypeList()==null || !appNewVoteEntry.getVoteTypeList().contains(new Integer(role))){
            throw new Exception("无投票权限");
        }
        long current = System.currentTimeMillis();
        if(appNewVoteEntry.getVoteStartTime()>current){
            throw new Exception("投票未开始");
        }
        if(appNewVoteEntry.getVoteEndTime()<current){
            throw new Exception("投票已结束");
        }
        List<ObjectId> appVoteUserList = new ArrayList<ObjectId>();
        if(appNewVoteEntry.getVoteUesrList()!=null){
            appVoteUserList = appNewVoteEntry.getVoteUesrList();
        }
        if(appVoteUserList.contains(userId)){//已投
            throw new Exception("已经投过票的用户不能再次投票");
        }

        List<AppVoteOptionEntry> appVoteOptionEntries = appVoteOptionDao.getOneVoteList(id);
        boolean fale = false;
        for(AppVoteOptionEntry appVoteOptionEntry:appVoteOptionEntries){
            if(optionIds.contains(appVoteOptionEntry.getID().toString())){//所选项
                List<ObjectId> objectIdList = appVoteOptionEntry.getUserIdList();
                if(objectIdList!=null){
                    if(!objectIdList.contains(userId)){
                        objectIdList.add(userId);
                        appVoteOptionEntry.setUserIdList(objectIdList);
                        appVoteOptionEntry.setCount(objectIdList.size());
                    }
                }else{
                    List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
                    objectIdList1.add(userId);
                    appVoteOptionEntry.setUserIdList(objectIdList1);
                    appVoteOptionEntry.setCount(objectIdList1.size());
                }
                //保存投票信息
                appVoteOptionDao.saveAppVote(appVoteOptionEntry);
                fale = true;
            }
        }
        if(fale){//选项投递成功
            appVoteUserList.add(userId);
            appNewVoteEntry.setVoteUesrList(appVoteUserList);
            appNewVoteEntry.setUserCount(appVoteUserList.size());
            appNewVoteDao.saveAppVote(appNewVoteEntry);
        }
    }


    public void applyMyOption(ObjectId userId,ObjectId id,String description) throws Exception{
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        if(appNewVoteEntry.getType()!=2){
            throw new Exception("该投票不允许自主报名");
        }
        //获得查询者的身份
        int role = 2;//默认家长
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry !=null && teacherApproveEntry.getType()==2){
            role = 3;//默认老师
        }else{
            NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
            if(newVersionUserRoleEntry!=null && newVersionUserRoleEntry.getNewRole()==2){
                role = 1;//默认学生
            }
        }
        if(appNewVoteEntry.getApplyTypeList()==null || !appNewVoteEntry.getApplyTypeList().contains(new Integer(role))){
            throw new Exception("无报名权限");
        }
        if(appNewVoteEntry.getType()==2){
            long current = System.currentTimeMillis();
            if(appNewVoteEntry.getApplyStartTime()>current){
                throw new Exception("报名未开始");
            }
            if(appNewVoteEntry.getApplyEndTime()<current){
                throw new Exception("报名已结束");
            }
        }
        List<ObjectId> applyUserList = new ArrayList<ObjectId>();
        if(appNewVoteEntry.getApplyUserList()!=null){
            applyUserList = appNewVoteEntry.getApplyUserList();
        }
        if(applyUserList.contains(userId)){//已报名
            throw new Exception("已经报过名的用户不能再次报名");
        }
        //查询已成为选项的数量
        int optionCount = appVoteOptionDao.countSelectOneVoteList(id);
        long current = System.currentTimeMillis();
        if(optionCount< appNewVoteEntry.getApplyCount()) {//申请数量未满,成为可选项
            AppVoteOptionEntry appVoteOptionEntry = new AppVoteOptionEntry(
                    appNewVoteEntry.getID(),
                    description,
                    userId,
                    Constant.TWO,
                    Constant.ONE,//已选择
                    new ArrayList<AttachmentEntry>(),
                    new ArrayList<AttachmentEntry>(),
                    new ArrayList<VideoEntry>(),
                    new ArrayList<AttachmentEntry>(),
                    Constant.ZERO,
                    current,
                    new ArrayList<ObjectId>());
            appVoteOptionDao.saveAppVote(appVoteOptionEntry);
        }else{//已满，成为待选项
            AppVoteOptionEntry appVoteOptionEntry = new AppVoteOptionEntry(
                    appNewVoteEntry.getID(),
                    description,
                    userId,
                    Constant.TWO,//
                    Constant.ZERO,//未选择
                    new ArrayList<AttachmentEntry>(),
                    new ArrayList<AttachmentEntry>(),
                    new ArrayList<VideoEntry>(),
                    new ArrayList<AttachmentEntry>(),
                    Constant.ZERO,
                    current,
                    new ArrayList<ObjectId>());
            appVoteOptionDao.saveAppVote(appVoteOptionEntry);
        }
        //添加申请名单中
        applyUserList.add(userId);
        appNewVoteEntry.setApplyUserList(applyUserList);
        appNewVoteDao.saveAppVote(appNewVoteEntry);

    }

    public void deleteMyOption(ObjectId id,ObjectId optionId,ObjectId userId)throws Exception{
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        long current = System.currentTimeMillis();
        if(appNewVoteEntry.getVoteStartTime()<current){//投票已开始
            throw new Exception("投票已开始无法撤回");
        }
        AppVoteOptionEntry appVoteOptionEntry =  appVoteOptionDao.getOneEntry(optionId);
        if(appVoteOptionEntry!=null){
            if(appVoteOptionEntry.getSelect()==1){//选项已被选中
                //将最近的一个选项设为可选
                AppVoteOptionEntry appVoteOptionEntry1 = appVoteOptionDao.getNowOneOption(id);
                if(appVoteOptionEntry1!=null){
                    appVoteOptionEntry1.setSelect(1);
                    appVoteOptionDao.saveAppVote(appVoteOptionEntry1);
                }
            }
            //删除选项
            appVoteOptionDao.delEntry(optionId);
        }

        //去除申请名单中
        List<ObjectId> objectIdList = appNewVoteEntry.getApplyUserList();
        if(objectIdList!=null){
            objectIdList.remove(userId);
        }else{
            objectIdList = new ArrayList<ObjectId>();
        }
        appNewVoteEntry.setApplyUserList(objectIdList);
        appNewVoteDao.saveAppVote(appNewVoteEntry);
    }

    public AppVoteOptionDTO selectMyOption(ObjectId userId,ObjectId id)throws Exception{
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        AppVoteOptionEntry appVoteOptionEntry = appVoteOptionDao.getEntry(id, userId);
        if(appVoteOptionEntry!=null){
            return new AppVoteOptionDTO(appVoteOptionEntry);
        }else{
            return null;
        }
    }

    public Map<String,Object> selectOptionList(ObjectId userId,ObjectId id)throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        AppNewVoteDTO appNewVoteDTO = new AppNewVoteDTO(appNewVoteEntry);
        //1. 发布人数据
        UserEntry userEntry = userDao.findByUserId(appNewVoteEntry.getUserId());
        if(userEntry!=null){
            appNewVoteDTO.setUserName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            appNewVoteDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
        }
        //获得查询者的身份
        int role = 2;//默认家长
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry !=null && teacherApproveEntry.getType()==2){
            role = 3;//默认老师
        }else{
            NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
            if(newVersionUserRoleEntry!=null && newVersionUserRoleEntry.getNewRole()==2){
                role = 1;//默认学生
            }
        }
        //2. 获得投票阶段
        long current = System.currentTimeMillis();
        if(appNewVoteEntry.getApplyStartTime()< current && appNewVoteEntry.getApplyEndTime()>current){//报名阶段
            appNewVoteDTO.setLevel(1);
        }else if(appNewVoteEntry.getVoteStartTime()< current && appNewVoteEntry.getVoteEndTime()>current){//投票阶段
            appNewVoteDTO.setLevel(2);
        }else if(appNewVoteEntry.getVoteEndTime()<current){//结束阶段
            appNewVoteDTO.setLevel(3);
        }else{//未开始阶段
            appNewVoteDTO.setLevel(0);
        }
        //3. 判断阶段
        if(appNewVoteEntry.getUserId().equals(userId)){//发布人
            appNewVoteDTO.setIsOwner(1);
            appNewVoteDTO.setIsApply(0);
            appNewVoteDTO.setIsVote(0);
        }else{//非发布人
            appNewVoteDTO.setIsOwner(0);
            appNewVoteDTO.setIsApply(0);
            appNewVoteDTO.setIsVote(0);
            if(appNewVoteEntry.getType()==2 && appNewVoteEntry.getApplyTypeList()!=null && appNewVoteEntry.getApplyTypeList().contains(new Integer(role))) {//可报名
                if (appNewVoteEntry.getApplyUserList() != null && appNewVoteEntry.getApplyUserList().contains(userId)) {//已报名
                    appNewVoteDTO.setIsApply(2);
                } else {//未报名
                    appNewVoteDTO.setIsApply(1);
                }
            }
            if(appNewVoteEntry.getVoteTypeList()!=null && appNewVoteEntry.getVoteTypeList().contains(new Integer(role))){//可投票
                if(appNewVoteEntry.getVoteUesrList()!=null && appNewVoteEntry.getVoteUesrList().contains(userId)){//已投票
                    appNewVoteDTO.setIsVote(2);
                }else{//未投票
                    appNewVoteDTO.setIsVote(1);
                }
            }

        }
        //4. 组装选项
        List<AppVoteOptionEntry> appVoteOptionEntries = appVoteOptionDao.getAllOneVoteList(id);
        List<AppVoteOptionDTO> selectOption = new ArrayList<AppVoteOptionDTO>();
         List<AppVoteOptionDTO> unSelectOption = new ArrayList<AppVoteOptionDTO>();
        for(AppVoteOptionEntry appVoteOptionEntry: appVoteOptionEntries){
            AppVoteOptionDTO appVoteOptionDTO = new AppVoteOptionDTO(appVoteOptionEntry);
            if(appVoteOptionEntry.getUserIdList()!=null && appVoteOptionEntry.getUserIdList().contains(userId)){
                appVoteOptionDTO.setIsSelect(1);
            }else{
                appVoteOptionDTO.setIsSelect(0);
            }
            if(appVoteOptionEntry.getSelect()==1){//可选项
            selectOption.add(appVoteOptionDTO);
            }else{//待选项
                unSelectOption.add(appVoteOptionDTO);
            }
        }
        map.put("dto",appNewVoteDTO);
        map.put("count",appNewVoteDTO.getApplyCount());
        map.put("selectOption",selectOption);
        map.put("unSelectOption",unSelectOption);
        return map;
    }

    public void updateOption(ObjectId id,String selectOptionIds) throws Exception{
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        long current = System.currentTimeMillis();
        if(appNewVoteEntry.getVoteStartTime()<current){//投票已开始
            throw new Exception("投票已开始无法修改");
        }
        List<AppVoteOptionEntry> appVoteOptionEntries = appVoteOptionDao.getAllOneVoteList(id);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> objectIdList2 = new ArrayList<ObjectId>();
        for(AppVoteOptionEntry appVoteOptionEntry : appVoteOptionEntries){
            if(selectOptionIds.contains(appVoteOptionEntry.getID().toString())) {
                objectIdList1.add(appVoteOptionEntry.getID());
            }else{
                objectIdList2.add(appVoteOptionEntry.getID());
            }
        }
        //改为已选择
        current  = appNewVoteEntry.getCreateTime();
        int index = 10;
        if(selectOptionIds!=null){
            String[] strings = selectOptionIds.split(",");
            for(String string:strings){
                index++;
                current = current+ index;
                if(ObjectId.isValid(string)){
                    appVoteOptionDao.updateOneEntry(id, new ObjectId(string), Constant.ONE,current);
                }
            }
        }


        //改为未选择
        appVoteOptionDao.updateEntry(id,objectIdList2,Constant.ZERO);

    }


    public List<Map<String,Object>> selectUserList(ObjectId id)throws Exception{
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
        if(appNewVoteEntry==null){
            throw new Exception("投票已被删除或不存在");
        }
        List<AppVoteOptionEntry> appVoteOptionEntries = appVoteOptionDao.getSelectOneVoteList(id);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(AppVoteOptionEntry appVoteOptionEntry:appVoteOptionEntries){
            if(appVoteOptionEntry.getUserIdList()!=null){
                userIds.addAll(appVoteOptionEntry.getUserIdList());
            }
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        List<Map<String,Object>>  mapList = new ArrayList<Map<String,Object>>();
        int index = 0;
        for(AppVoteOptionEntry appVoteOptionEntry:appVoteOptionEntries) {
            index++;
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("order", index);
            map2.put("description", appVoteOptionEntry.getDescription());
            List<User> userList = new ArrayList<User>();
            List<ObjectId> objectIdList = appVoteOptionEntry.getUserIdList();
            if (objectIdList != null) {
                for (ObjectId oid : objectIdList) {
                    UserEntry userEntry = userEntryMap.get(oid);
                    if (userEntry != null) {
                        String name = StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                        User user = new User(name,
                                name,
                                userEntry.getID().toString(),
                                AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()),
                                userEntry.getSex(),
                                "");
                        userList.add(user);
                    }
                }
            }
            map2.put("list", userList);
            map2.put("count", userList.size());
            mapList.add(map2);
        }
        return mapList;
    }

    public void deleteVote(ObjectId id,ObjectId userId) throws  Exception{
        AppNewVoteEntry appNewVoteEntry = appNewVoteDao.getEntry(id);
//        long current = System.currentTimeMillis();
//        if(current>appNewVoteEntry.getApplyStartTime()+24*60*60*1000){
//            throw new Exception("发布超过24小时的投票无法撤回！");
//        }
        if(appNewVoteEntry !=null && userId.equals(appNewVoteEntry.getUserId())){
            appNewVoteDao.delEntry(id);
            appVoteOptionDao.delAllEntry(id);
            IndexContentEntry indexContentEntry = indexContentDao.getEntry(id);
            //删除首页记录
            indexPageDao.delEntry(id);
            if(indexContentEntry!=null){
                //获取已签到人数
                List<ObjectId> objectIdList = indexContentEntry.getReaList();
                //消除多余红点
                List<ObjectId> communityIds = appNewVoteEntry.getCommunityList();
                if(communityIds!=null){
                    List<ObjectId> objectIdList1 = communityDao.getGroupIdsByCommunityIds(communityIds);
                    if(objectIdList==null){
                        objectIdList = new ArrayList<ObjectId>();
                    }
                    for(ObjectId cid :objectIdList1){
                        this.updateUser(objectIdList,cid);
                    }
                }

            }

        }
    }

    //消除多余红点
    public void updateUser(final List<ObjectId> objectIds,final ObjectId groupId){
        new Thread(){
            public void run() {
                MemberDao memberDao1 = new MemberDao();
                List<ObjectId> members=memberDao1.getExtraMemberIds(groupId, objectIds);
                for(ObjectId objectId:members){
                    RedDotEntry redDotEntry = redDotDao.getEntryByUserId(objectId,ApplyTypeEn.notice.getType());
                    if(redDotEntry!=null && redDotEntry.getNewNumber()>0){
                        redDotDao.updateEntry(redDotEntry.getID(),redDotEntry.getNewNumber()-1);
                    }
                }
            }
        }.start();
    }

    //处理旧数据
    public void traFraVote(){
        int page = 1;
        int pageSize = 10;
        boolean flage = true;
        while(flage){
            List<AppVoteEntry> appVoteEntries = appVoteDao.getPageList(page, pageSize);
            if(appVoteEntries.size()<10){
                flage = false;
            }
            //flage = false;
            long current = System.currentTimeMillis();
            for(AppVoteEntry appVoteEntry:appVoteEntries){
                //获得投票信息
                List<FVoteDTO> fVoteEntryList = fVoteService.getFVoteList(appVoteEntry.getID().toString());
                //组装对象
                List<Integer> voteTypeList = new ArrayList<Integer>();
                if(appVoteEntry.getVisiblePermission()==1){//家长
                    voteTypeList.add(2);
                    voteTypeList.add(3);
                }else if(appVoteEntry.getVisiblePermission()==2){//学生
                    voteTypeList.add(1);
                }else{//所有人
                    voteTypeList.add(1);
                    voteTypeList.add(2);
                    voteTypeList.add(3);
                }
                List<ObjectId> communityIds = new ArrayList<ObjectId>();
                communityIds.add(appVoteEntry.getCommunityId());
                int sign = appVoteEntry.getVoteType()==0?1:0;
                Map<Integer,List<ObjectId>> map = new HashMap<Integer, List<ObjectId>>();
                Set<ObjectId> set = new HashSet<ObjectId>();
                for(FVoteDTO fVoteDTO:fVoteEntryList){
                    List<ObjectId> objectIdList = map.get(fVoteDTO.getNumber());
                    if(objectIdList==null){
                        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
                        objectIdList1.add(new ObjectId(fVoteDTO.getUserId()));
                        map.put(fVoteDTO.getNumber(),objectIdList1);
                    }else{
                        objectIdList.add(new ObjectId(fVoteDTO.getUserId()));
                        map.put(fVoteDTO.getNumber(),objectIdList);
                    }
                    set.add(new ObjectId(fVoteDTO.getUserId()));

                }
                List<ObjectId> userList = new ArrayList<ObjectId>();
                userList.addAll(set);
                AppNewVoteEntry appNewVoteEntry = new AppNewVoteEntry(
                        appVoteEntry.getTitle(),
                        appVoteEntry.getContent(),
                        null,
                        appVoteEntry.getSubjectId(),
                        appVoteEntry.getUserId(),
                        Constant.ZERO,
                        set.size(),
                        appVoteEntry.getImageList(),
                        new ArrayList<AttachmentEntry>(),
                        new ArrayList<VideoEntry>(),
                        new ArrayList<AttachmentEntry>(),
                        Constant.ONE,
                        new ArrayList<Integer>(),
                        Constant.ZERO,
                        Constant.ZERO,
                        Constant.ZERO,
                        communityIds,
                        voteTypeList,
                        appVoteEntry.getVoteMaxCount(),
                        sign,
                        Constant.ONE,
                        appVoteEntry.getSubmitTime(),
                        appVoteEntry.getVoteDeadTime());
                appNewVoteEntry.setCreateTime(appVoteEntry.getSubmitTime());
                appNewVoteEntry.setID(appVoteEntry.getID());
                appNewVoteDao.saveAppVote(appNewVoteEntry);
                ObjectId id = appNewVoteEntry.getID();
                appNewVoteEntry.setVoteUesrList(userList);
                appNewVoteEntry.setApplyUserList(new ArrayList<ObjectId>());
                appNewVoteDao.saveAppVote(appNewVoteEntry);
                List<String> stringList = appVoteEntry.getVoteContent();
                if(stringList!=null){
                    int index = 10;
                    int i = 0;
                    appVoteOptionDao.delAllEntry(id);
                    for(String s:stringList){
                        index++;
                        i++;
                        current = index +current;
                        List<ObjectId> uids = map.get(i);
                        if(uids==null){
                            uids = new ArrayList<ObjectId>();
                        }
                        AppVoteOptionEntry appVoteOptionEntry = new AppVoteOptionEntry(
                                id,
                                s,
                                appVoteEntry.getUserId(),
                                Constant.ONE,
                                Constant.ONE,
                                new ArrayList<AttachmentEntry>(),
                                new ArrayList<AttachmentEntry>(),
                                new ArrayList<VideoEntry>(),
                                new ArrayList<AttachmentEntry>(),
                                uids.size(),
                                current,
                                uids);
                        appVoteOptionDao.saveAppVote(appVoteOptionEntry);
                    }
                }
            }
            page++;
        }
    }
}
