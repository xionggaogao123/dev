package com.fulaan.newVersionBind.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.newVersionGrade.NewVersionGradeDao;
import com.db.newVersionGrade.NewVersionSubjectDao;
import com.db.user.MakeOutUserRelationDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.TeacherSubjectBindDao;
import com.fulaan.cache.CacheHandler;
import com.fulaan.mqtt.MQTTSendMsg;
import com.fulaan.newVersionBind.dto.*;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.pojo.KeyValue;
import com.fulaan.wrongquestion.dto.NewVersionGradeDTO;
import com.fulaan.wrongquestion.service.WrongQuestionService;
import com.pojo.app.SessionValue;
import com.pojo.controlphone.MQTTType;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.newVersionGrade.NewVersionGradeEntry;
import com.pojo.user.*;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by scott on 2017/9/5.
 */
@Service
public class NewVersionBindService {


    private NewVersionBindRelationDao newVersionBindRelationDao=new NewVersionBindRelationDao();

    private NewVersionUserRoleDao newVersionUserRoleDao= new NewVersionUserRoleDao();

    private NewVersionGradeDao newVersionGradeDao = new NewVersionGradeDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private TeacherSubjectBindDao teacherSubjectBindDao= new TeacherSubjectBindDao();

    private NewVersionSubjectDao newVersionSubjectDao= new NewVersionSubjectDao();

    private MakeOutUserRelationDao makeOutUserRelationDao = new MakeOutUserRelationDao();

    private CommunityDao communityDao = new CommunityDao();

    private MemberDao memberDao = new MemberDao();

    @Autowired
    private UserService userService;

    @Autowired
    private WrongQuestionService wrongQuestionService;


    public void saveBindUserDetail(ObjectId bindId,
                                   int sex,
                                   String birthDate,
                                   String avatar,
                                   String nickName,
                                   String personalSignature
                                   )throws Exception{
        NewVersionBindRelationEntry entry = newVersionBindRelationDao.getEntry(bindId);
        if (null != entry) {
            ObjectId userId = entry.getUserId();
            long time=-1L;
            if(StringUtils.isNotEmpty(birthDate)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                time = format.parse(birthDate).getTime();
            }
            userService.updateUserBirthDateAndSex(userId, sex, time, avatar, nickName);
            if(StringUtils.isNotEmpty(personalSignature)) {
                newVersionBindRelationDao.updatePersonalSignature(entry.getID(),personalSignature);
            }
        }
    }


    public void completeBindInfo(ObjectId bindId,
                                 String provinceName,
                                 String regionName,
                                 String regionAreaName,
                                 String schoolName,
                                 int gradeType,
                                 String nickName,
                                 int relation){
        try {
            NewVersionBindRelationEntry entry = newVersionBindRelationDao.getEntry(bindId);
            if (null != entry) {
                ObjectId userId = entry.getUserId();
                userService.updateUserBirthDateAndSex(userId, -1,-1L, "", nickName);
                //绑定年级
                if(gradeType != Constant.NEGATIVE_ONE) {
                    KeyValue keyValue = wrongQuestionService.getCurrTermType();
                    NewVersionGradeEntry gradeEntry = newVersionGradeDao.getEntryByCondition(userId, keyValue.getValue());
                    if (null == gradeEntry) {
                        NewVersionGradeDTO dto = new NewVersionGradeDTO(userId.toString(), keyValue.getValue(), gradeType);
                        wrongQuestionService.addGradeFromUser(dto);
                    } else {
                        newVersionGradeDao.updateNewVersionGrade(userId, keyValue.getValue(), gradeType);
                    }
                }
                newVersionBindRelationDao.supplementNewVersionInfo(bindId, provinceName, regionName, regionAreaName, schoolName,relation);
            }
        }catch (Exception e){
            throw new RuntimeException("保存完善信息失败");
        }
    }

    public void supplementNewVersionInfo(
            ObjectId bindId,
            int sex,String birthDate,
            String provinceName,
            String regionName,
            String regionAreaName,
            String schoolName,
            String avator,
            int gradeType,
            String nickName,
            int relation
    ){
        try {
            NewVersionBindRelationEntry entry = newVersionBindRelationDao.getEntry(bindId);
            if (null != entry) {
                ObjectId userId = entry.getUserId();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBirth = format.parse(birthDate);
                userService.updateUserBirthDateAndSex(userId, sex, dateBirth.getTime(), avator, nickName);
                //绑定年级
                KeyValue keyValue = wrongQuestionService.getCurrTermType();
                NewVersionGradeEntry gradeEntry = newVersionGradeDao.getEntryByCondition(userId, keyValue.getValue());
                if (null == gradeEntry) {
                    NewVersionGradeDTO dto = new NewVersionGradeDTO(userId.toString(), keyValue.getValue(), gradeType);
                    wrongQuestionService.addGradeFromUser(dto);
                } else {
                    newVersionGradeDao.updateNewVersionGrade(userId, keyValue.getValue(), gradeType);
                }
                newVersionBindRelationDao.supplementNewVersionInfo(bindId, provinceName, regionName, regionAreaName, schoolName,relation);
            }
        }catch (Exception e){
            throw new RuntimeException("保存完善信息失败");
        }
    }


    public NewVersionBindRelationDTO getNewVersionBindStudent(ObjectId userId){
        NewVersionBindRelationEntry entry=newVersionBindRelationDao.getBindEntry(userId);
        NewVersionBindRelationDTO dto=new NewVersionBindRelationDTO(entry);
        UserEntry userEntry=userService.findById(userId);
        KeyValue keyValue = wrongQuestionService.getCurrTermType();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        userIds.add(userId);
        Map<ObjectId,NewVersionGradeEntry> newVersionGradeEntryMap=newVersionGradeDao.getNewVersionGradeMap(userIds,keyValue.getValue());
        if(null!=userEntry){
            dto.setSex(userEntry.getSex());
            dto.setNickName(userEntry.getNickName());
            if(userEntry.getInterviewTime() !=0l){
                dto.setLoadTime(DateTimeUtils.getLongToStrTimeTwo(userEntry.getInterviewTime()));
            }else{
                dto.setLoadTime(DateTimeUtils.getLongToStrTimeTwo(new Date().getTime()));
            }
            dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String birthDate=format.format(userEntry.getBirthDate());
            dto.setBirthDate(birthDate);
        }
        dto.setGradeType(1);
        NewVersionGradeEntry gradeEntry=newVersionGradeEntryMap.get(entry.getUserId());
        if(null!=gradeEntry){
            dto.setGradeType(gradeEntry.getGradeType());
        }
       return dto;
    }

    public void saveTeacherSubjectBind(TeacherSubjectBindDTO bindDTO,ObjectId userId){
        TeacherSubjectBindEntry entry=teacherSubjectBindDao.getEntry(userId);
        if(null!=entry){
            teacherSubjectBindDao.removeEntryByTeacherId(userId);
        }
        bindDTO.setUserId(userId.toString());
        teacherSubjectBindDao.saveTeacherSubjectEntry(bindDTO.buildEntry());
    }


    public List<UserLoginStatus> searchLoginChildren(ObjectId mainUserId){
        List<UserLoginStatus> list = new ArrayList<UserLoginStatus>();
        List<NewVersionBindRelationEntry> entries=newVersionBindRelationDao.getEntriesByMainUserId(mainUserId);
        List<ObjectId> userIds= new ArrayList<ObjectId>();
        for(NewVersionBindRelationEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(NewVersionBindRelationEntry entry:entries){
            UserEntry userEntry=userEntryMap.get(entry.getUserId());
            if(null!=userEntry){
                UserLoginStatus userLoginStatus=new UserLoginStatus();
                userLoginStatus.setAvatar(AvatarUtils.getAvatar2(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                userLoginStatus.setUserName(userEntry.getUserName());
                userLoginStatus.setNickName(userEntry.getNickName());
                userLoginStatus.setLogin(false);
                String cacheUserKey= CacheHandler.getUserKey(userEntry.getID().toString());
                if(StringUtils.isNotEmpty(cacheUserKey)){
                    SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                    if (null != sv && !sv.isEmpty()) {
                        userLoginStatus.setLogin(true);
                    }
                }
                list.add(userLoginStatus);
            }
        }
        return list;
    }


    public List<NewVersionBindRelationDTO> getNewVersionBindDtos (ObjectId mainUserId,ObjectId communityId){
        List<NewVersionBindRelationDTO> dtos = new ArrayList<NewVersionBindRelationDTO>();
        List<NewVersionBindRelationEntry> entries=newVersionBindRelationDao.getEntriesByMainUserId(mainUserId);
        List<ObjectId> userIds= new ArrayList<ObjectId>();
        for(NewVersionBindRelationEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,NewVersionCommunityBindEntry> map=new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        if(null!=communityId){
            map=newVersionCommunityBindDao.getCommunityBindMap(communityId,mainUserId);
        }
        KeyValue keyValue = wrongQuestionService.getCurrTermType();
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        Map<ObjectId,NewVersionGradeEntry> newVersionGradeEntryMap=newVersionGradeDao.getNewVersionGradeMap(userIds,keyValue.getValue());
        for(NewVersionBindRelationEntry entry:entries){
            NewVersionBindRelationDTO dto=new NewVersionBindRelationDTO(entry);
            dto.setIsBindCommunity(0);
            UserEntry userEntry=userEntryMap.get(entry.getUserId());
            if(null!=userEntry){
                dto.setMobileNumber(userEntry.getMobileNumber());
                dto.setSex(userEntry.getSex());
                dto.setNickName(userEntry.getNickName());
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String birthDate=format.format(userEntry.getBirthDate());
                dto.setBirthDate(birthDate);
                NewVersionCommunityBindEntry bindEntry=map.get(userEntry.getID());
                if(null!=bindEntry){
                    dto.setIsBindCommunity(1);
                    dto.setStudentNumber(bindEntry.getNumber());
                    dto.setThirdName(bindEntry.getThirdName());
                }
            }
            dto.setGradeType(0);
            NewVersionGradeEntry gradeEntry=newVersionGradeEntryMap.get(entry.getUserId());
            if(null!=gradeEntry){
                dto.setGradeType(gradeEntry.getGradeType());
            }
            dto.setLogin(false);
            String cacheUserKey= CacheHandler.getUserKey(userEntry.getID().toString());
            if(StringUtils.isNotEmpty(cacheUserKey)){
                SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                if (null != sv && !sv.isEmpty()) {
                    dto.setLogin(true);
                }
            }
            dtos.add(dto);
        }
        return dtos;
    }


    public void saveNewVersionBindRelationEntry (
            ObjectId mainUserId,
            ObjectId userId,
            int relation,
            String nickName,
            String avatar
    ){
        NewVersionBindRelationEntry entry= newVersionBindRelationDao.getBindEntry(userId);
        if(null==entry){
            try {
                userService.updateUserBirthDateAndSex(userId,-1,-1,avatar,nickName);
//                NewVersionBindRelationEntry bindRelationEntry=newVersionBindRelationDao.getEntryByUserId(userId);
//                NewVersionBindRelationEntry relationEntry
//                        =new NewVersionBindRelationEntry(mainUserId,
//                        userId,
//                        relation,
//                        Constant.EMPTY,
//                        Constant.EMPTY,
//                        Constant.EMPTY,
//                        Constant.EMPTY,
//                        Constant.EMPTY);
//                if(null!=bindRelationEntry){
//                    relationEntry.setID(bindRelationEntry.getID());
//                }
//                newVersionBindRelationDao.saveNewVersionBindEntry(relationEntry);
                NewVersionBindRelationEntry oldEntry=newVersionBindRelationDao.getEntryByUserId(userId);
                NewVersionBindRelationEntry relationEntry
                        =new NewVersionBindRelationEntry(mainUserId,
                        userId,
                        relation,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY);
                if(null!=oldEntry) {
                    NewVersionBindRelationEntry bindRelationEntry = newVersionBindRelationDao.getBindRelationEntry(mainUserId, userId);
                    if (null != bindRelationEntry) {
                        bindRelationEntry.setRelation(relation);
                        bindRelationEntry.setRemove(Constant.ZERO);
                        newVersionBindRelationDao.saveNewVersionBindEntry(bindRelationEntry);
                    } else {
                        relationEntry.setRelation(relation);
                        relationEntry.setProvinceName(oldEntry.getProvinceName());
                        relationEntry.setRegionName(oldEntry.getRegionName());
                        relationEntry.setRegionAreaName(oldEntry.getRegionAreaName());
                        relationEntry.setSchoolName(oldEntry.getSchoolName());
                        relationEntry.setPersonalSignature(oldEntry.getPersonalSignature());
                        newVersionBindRelationDao.saveNewVersionBindEntry(relationEntry);
                    }
                }else{
                    newVersionBindRelationDao.saveNewVersionBindEntry(relationEntry);
                }
                NewVersionUserRoleEntry userRoleEntry=newVersionUserRoleDao.getEntry(userId);
                userRoleEntry.setNewRole(Constant.TWO);
                newVersionUserRoleDao.saveEntry(userRoleEntry);
                //发送环信消息
//                List<String> tags = new ArrayList<String>();
//                tags.add(userId.toString());
//                Audience audience = Audience.alias(tags);
//                JPushUtils jPushUtils=new JPushUtils();
//                UserEntry userEntry=userService.findById(userId);
//                Map<String,String> extras = new HashMap<String, String>();
//                extras.put("type","1");
//                jPushUtils.pushRestAndroid(audience, "你的账号已激活", userEntry.getUserName(), "您有新的通知", extras);
//                jPushUtils.pushRestIos(audience, "你的账号已激活", extras);
//                jPushUtils.pushRestWinPhone(audience, "你的账号已激活");
            }catch (Exception e){
                e.printStackTrace();
                throw  new RuntimeException("传入的生日数据有误!");
            }
        }else {
            throw  new RuntimeException("已经绑定了!");
        }
    }

    public void saveNewVersionSubject(String subjectIds,ObjectId userId){
        NewVersionSubjectDTO dto =new NewVersionSubjectDTO();
        String[] sIds=subjectIds.split(",");
        List<String> suIds= new ArrayList<String>();
        for(String sId:sIds){
            suIds.add(sId);
        }
        dto.setSubjectId(suIds);
        dto.setUserId(userId.toString());
        newVersionSubjectDao.removeOldSubjectData(userId);
        newVersionSubjectDao.saveNewVersionSubjectEntry(dto.buildAddEntry());
    }

    public void updateNumber(ObjectId communityId,
                             ObjectId mainUserId,
                             ObjectId userId,
                             String studentNumber){
        newVersionCommunityBindDao.updateNumber(communityId,mainUserId,userId, studentNumber);
    }

    public void updateThirdName(ObjectId communityId,
                                ObjectId mainUserId,
                                ObjectId userId,
                                String thirdName){
        newVersionCommunityBindDao.updateThirdName(communityId,mainUserId,userId, thirdName);
    }

    public String saveNewVersionEntry(ObjectId bid,ObjectId userId){
        NewVersionBindRelationEntry entry = new NewVersionBindRelationEntry();
        entry.setMainUserId(userId);
        entry.setUserId(bid);
        newVersionBindRelationDao.saveNewVersionBindEntry(entry);
        return "";
    }

    public void addCommunityBindEntry(String userIds,ObjectId communityId,ObjectId mainUserId){
        //先删除绑定关系
        newVersionCommunityBindDao.removeNewVersionCommunity(communityId,mainUserId);
        if(StringUtils.isNotBlank(userIds)) {
            String[] uIds = userIds.split(",");
            for (String uId : uIds) {
                NewVersionCommunityBindEntry bindEntry = newVersionCommunityBindDao.getEntry(communityId, mainUserId, new ObjectId(uId));
                if (null != bindEntry) {
                    if (bindEntry.getRemoveStatus() == Constant.ONE) {
                        newVersionCommunityBindDao.updateEntryStatus(bindEntry.getID());
                    }
                } else {
                    NewVersionCommunityBindEntry entry = new NewVersionCommunityBindEntry(communityId, mainUserId, new ObjectId(uId));
                    newVersionCommunityBindDao.saveEntry(entry);
                }
                long current = System.currentTimeMillis();
                //向学生端推送消息
                try {
                    MQTTSendMsg.sendMessage(MQTTType.phone.getEname(), uId,current);
                }catch (Exception e){

                }
            }
        }
    }


    public void delNewVersionEntry(ObjectId parentId,ObjectId studentId){
        newVersionBindRelationDao.delNewVersionEntry(parentId,studentId);
        //删除对应的社区绑定关系
        newVersionCommunityBindDao.removeNewVersionCommunityBindRelation(parentId, studentId);
        //孩子设置为初始状态
        newVersionUserRoleDao.updateNewRole(studentId);
        long current = System.currentTimeMillis();
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.login.getEname(), studentId.toString(), current);
        }catch (Exception e){

        }
    }


    /**
     * 建立关联关系
     * @param parentId
     * @param studentId
     */
    public void establishBindRelation(ObjectId parentId,ObjectId studentId){
        NewVersionBindRelationEntry oldEntry=newVersionBindRelationDao.getEntryByUserId(studentId);
        if(null!=oldEntry) {
            NewVersionBindRelationEntry bindRelationEntry = newVersionBindRelationDao.getBindRelationEntry(parentId, studentId);
            if (null != bindRelationEntry) {
                bindRelationEntry.setRemove(Constant.ZERO);
                newVersionBindRelationDao.saveNewVersionBindEntry(bindRelationEntry);
            } else {
                NewVersionBindRelationEntry newEntry=new NewVersionBindRelationEntry(
                        parentId,
                        studentId,
                        oldEntry.getRelation(),
                        oldEntry.getProvinceName(),
                        oldEntry.getRegionName(),
                        oldEntry.getRegionAreaName(),
                        oldEntry.getSchoolName(),
                        oldEntry.getPersonalSignature()
                );
                newVersionBindRelationDao.saveNewVersionBindEntry(newEntry);
            }
            NewVersionUserRoleEntry userRoleEntry = newVersionUserRoleDao.getEntry(studentId);
            userRoleEntry.setNewRole(Constant.TWO);
            newVersionUserRoleDao.saveEntry(userRoleEntry);
            long current = System.currentTimeMillis();
            //向学生端推送消息
            try {
                MQTTSendMsg.sendMessage(MQTTType.phone.getEname(), studentId.toString(), current);
            }catch (Exception e){

            }
        }
    }

    /**
     * 在该社群移交权限
     * @param parentId
     * @param studentId
     * @param communityId
     */
    public void transferCommunityBind(ObjectId parentId,ObjectId studentId,ObjectId communityId){
        NewVersionCommunityBindEntry bindEntry = newVersionCommunityBindDao.getEntry(communityId, parentId, studentId);
        if (null != bindEntry) {
            if (bindEntry.getRemoveStatus() == Constant.ONE) {
                newVersionCommunityBindDao.updateEntryStatus(bindEntry.getID());
            }
        } else {
            NewVersionCommunityBindEntry entry = new NewVersionCommunityBindEntry(communityId, parentId, studentId);
            newVersionCommunityBindDao.saveEntry(entry);
        }
        long current = System.currentTimeMillis();
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.phone.getEname(), studentId.toString(),current);
        }catch (Exception e){

        }
    }

    /**
     * 在这个社区移交权限
     * @param parentId
     * @param studentIds
     */
    public void relieveBindRelation(ObjectId parentId,List<String> studentIds){
        for(String studentId:studentIds){
            delNewVersionEntry(parentId,new ObjectId(studentId));
        }
    }





    public List<ObjectId> getCommunityIdsByUserId(ObjectId userId){
        List<NewVersionCommunityBindEntry> entries=newVersionCommunityBindDao.getAllStudentBindEntries(userId);
        List<ObjectId> communityIds=new ArrayList<ObjectId>();
        for(NewVersionCommunityBindEntry bindEntry:entries){
            communityIds.add(bindEntry.getCommunityId());
        }
        return communityIds;
    }

    /**
     *
     * @param communityId
     * @return
     */
    public List<String> getStudentIdListByCommunityId(ObjectId communityId){
        List<NewVersionCommunityBindEntry> entries=newVersionCommunityBindDao.getStudentIdListByCommunityId(communityId);
        List<String> studentIds=new ArrayList<String>();
        for(NewVersionCommunityBindEntry bindEntry:entries){
            studentIds.add(bindEntry.getUserId().toString());
        }
        return studentIds;
    }


    public void makeOutRelation(ObjectId parentId,String userkey)throws Exception{
        MakeOutUserRelationEntry relationEntry=makeOutUserRelationDao.getRelationEntry(parentId, userkey);
        if(null!=relationEntry){
            throw new Exception("该手机号已存在");
        }else{
            MakeOutUserRelationEntry entry=new MakeOutUserRelationEntry(parentId, userkey);
            makeOutUserRelationDao.saveEntry(entry);
        }
    }


    public List<MakeOutUserRealationDTO> getMakeOutList(ObjectId userId){
        List<MakeOutUserRealationDTO> realationDTOs = new ArrayList<MakeOutUserRealationDTO>();
        List<MakeOutUserRelationEntry> entries = makeOutUserRelationDao.getEntries(userId);
        for(MakeOutUserRelationEntry relationEntry:entries){
            realationDTOs.add(new MakeOutUserRealationDTO(relationEntry));
        }
        return realationDTOs;
    }


    public void removeByItemId(ObjectId makeOutId){
        makeOutUserRelationDao.removeItemById(makeOutId);
    }


    public Map<String,Object> transferBindRelation(ObjectId parentId,TransferUserRelationDTO relationDTO){
        Map<String,Object> result=new HashMap<String,Object>();
        String communityId=relationDTO.getCommunityId();
        List<String> userIds=relationDTO.getUserIds();
        int failed=0;
        int success=0;
        if(userIds.size()>0){
            Map<ObjectId,NewVersionCommunityBindEntry> bindEntryMap=newVersionCommunityBindDao
                    .getCommunityBindMap(new ObjectId(communityId), parentId);
            ObjectId groupId=communityDao.getGroupId(new ObjectId(communityId));
            if(null!=groupId) {
                List<MemberEntry> memberEntries = memberDao.getAllMembers(groupId);
                List<ObjectId> mainUserIds = new ArrayList<ObjectId>();
                for(MemberEntry memberEntry:memberEntries){
                    mainUserIds.add(memberEntry.getUserId());
                }
                Set<ObjectId> children=new HashSet<ObjectId>();
                for(String userId:userIds){
                    children.add(new ObjectId(userId));
                }
                Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(children,Constant.FIELDS);
                Map<String,ObjectId> userKeyMap = new HashMap<String, ObjectId>();
                for(Map.Entry<ObjectId,UserEntry> item:userEntryMap.entrySet()){
                    UserEntry userEntry=item.getValue();
                    userKeyMap.put(userEntry.getUserName(),userEntry.getID());
                }
                Map<String,ObjectId> mainUserKeyMap=new HashMap<String, ObjectId>();
                List<MakeOutUserRelationEntry> relationEntries=makeOutUserRelationDao.getRelationEntriesByParentIds(mainUserIds);
                for(MakeOutUserRelationEntry relationEntry:relationEntries){
                    mainUserKeyMap.put(relationEntry.getUserKey(),relationEntry.getParentId());
                }

                for(Map.Entry<String,ObjectId> item:userKeyMap.entrySet()){
                    String userKey=item.getKey();
                    if(null!=mainUserKeyMap.get(userKey)){
                        ObjectId userId=item.getValue();
                        ObjectId mainUserId=mainUserKeyMap.get(userKey);
                        //解除绑定关系
                        delNewVersionEntry(parentId,userId);
                        //建立绑定关系
                        establishBindRelation(mainUserId,userId);

                        //删除记录列表
                        makeOutUserRelationDao.removeByCondition(mainUserId,userKey);
                        //判断该孩子是否在这个群中，若在的话移交该群的关联关系
                        if(null!=bindEntryMap.get(userId)){
                            transferCommunityBind(mainUserId,userId,new ObjectId(communityId));
                        }
                        success++;
                    }else{
                        failed++;
                    }
                }
            }
        }
        result.put("success",success);
        result.put("failed",failed);
        return result;
    }



}
