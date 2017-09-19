package com.fulaan.newVersionBind.service;

import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.newVersionGrade.NewVersionGradeDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.fulaan.newVersionBind.dto.NewVersionBindRelationDTO;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.pojo.KeyValue;
import com.fulaan.wrongquestion.dto.NewVersionGradeDTO;
import com.fulaan.wrongquestion.service.WrongQuestionService;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.newVersionGrade.NewVersionGradeEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/5.
 */
@Service
public class NewVersionBindService {


    private NewVersionBindRelationDao newVersionBindRelationDao=new NewVersionBindRelationDao();

    private NewVersionUserRoleDao newVersionUserRoleDao= new NewVersionUserRoleDao();

    private NewVersionGradeDao newVersionGradeDao = new NewVersionGradeDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    @Autowired
    private UserService userService;

    @Autowired
    private WrongQuestionService wrongQuestionService;


    public void perfectNewVersionInfo(
            ObjectId bindId,
            int sex,String birthDate,
            String provinceName,
            String regionName,
            String regionAreaName,
            String schoolName,
            String avator,
            int gradeType
    ){
        try {
            NewVersionBindRelationEntry entry = newVersionBindRelationDao.getEntry(bindId);
            if (null != entry) {
                ObjectId userId = entry.getUserId();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBirth = format.parse(birthDate);
                userService.updateUserBirthDateAndSex(userId, sex, dateBirth.getTime(), avator, "");
                //绑定年级
                KeyValue keyValue = wrongQuestionService.getCurrTermType();
                NewVersionGradeEntry gradeEntry = newVersionGradeDao.getEntryByCondition(userId, keyValue.getValue());
                if (null == gradeEntry) {
                    NewVersionGradeDTO dto = new NewVersionGradeDTO(userId.toString(), keyValue.getValue(), gradeType);
                    wrongQuestionService.addGradeFromUser(dto);
                } else {
                    newVersionGradeDao.updateNewVersionGrade(userId, keyValue.getValue(), gradeType);
                }
                newVersionBindRelationDao.perfectNewVersionInfo(bindId, provinceName, regionName, regionAreaName, schoolName);
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
            dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
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

    public List<NewVersionBindRelationDTO> getNewVersionBindDtos (ObjectId mainUserId){
        List<NewVersionBindRelationDTO> dtos = new ArrayList<NewVersionBindRelationDTO>();
        List<NewVersionBindRelationEntry> entries=newVersionBindRelationDao.getEntriesByMainUserId(mainUserId);
        List<ObjectId> userIds= new ArrayList<ObjectId>();
        for(NewVersionBindRelationEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        KeyValue keyValue = wrongQuestionService.getCurrTermType();
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        Map<ObjectId,NewVersionGradeEntry> newVersionGradeEntryMap=newVersionGradeDao.getNewVersionGradeMap(userIds,keyValue.getValue());
        for(NewVersionBindRelationEntry entry:entries){
            NewVersionBindRelationDTO dto=new NewVersionBindRelationDTO(entry);
            UserEntry userEntry=userEntryMap.get(entry.getUserId());
            if(null!=userEntry){
                dto.setMobileNumber(userEntry.getMobileNumber());
                dto.setSex(userEntry.getSex());
                dto.setNickName(userEntry.getNickName());
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String birthDate=format.format(userEntry.getBirthDate());
                dto.setBirthDate(birthDate);
            }
            dto.setGradeType(1);
            NewVersionGradeEntry gradeEntry=newVersionGradeEntryMap.get(entry.getUserId());
            if(null!=gradeEntry){
                dto.setGradeType(gradeEntry.getGradeType());
            }
            dtos.add(dto);
        }
        return dtos;
    }


    public void saveNewVersionBindRelationEntry (
            ObjectId mainUserId,
            ObjectId userId,
            int relation,
            String nickName
    ){
        NewVersionBindRelationEntry entry= newVersionBindRelationDao.getBindEntry(userId);
        if(null==entry){
            try {
                userService.updateUserBirthDateAndSex(userId,-1,-1,"",nickName);
                NewVersionBindRelationEntry relationEntry
                        =new NewVersionBindRelationEntry(mainUserId,
                        userId,
                        relation,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY);
                newVersionBindRelationDao.saveNewVersionBindEntry(relationEntry);
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

    public String saveNewVersionEntry(ObjectId bid,ObjectId userId){
        NewVersionBindRelationEntry entry = new NewVersionBindRelationEntry();
        entry.setMainUserId(userId);
        entry.setUserId(bid);
        newVersionBindRelationDao.saveNewVersionBindEntry(entry);
        return "";
    }

    public void addCommunityBindEntry(String userIds,ObjectId communityId,ObjectId mainUserId){
        String [] uIds=userIds.split(",");
        List<NewVersionCommunityBindEntry> entries=new ArrayList<NewVersionCommunityBindEntry>();
        for(String uId:uIds){
            NewVersionCommunityBindEntry entry=new NewVersionCommunityBindEntry(communityId,mainUserId,new ObjectId(uId));
            entries.add(entry);
        }
        if(entries.size()>0) {
            newVersionCommunityBindDao.saveEntries(entries);
        }
    }


    public void delNewVersionEntry(ObjectId parentId,ObjectId studentId){
        newVersionBindRelationDao.delNewVersionEntry(parentId,studentId);
    }


}
