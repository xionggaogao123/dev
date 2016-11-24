package com.fulaan.dangjian.service;

import com.db.dangjian.PartyResourceDao;
import com.db.dangjian.PartyUserDao;
import com.db.teachermanage.ResumeDao;
import com.db.user.UserDao;
import com.fulaan.base.service.DirService;
import com.mongodb.BasicDBObject;
import com.pojo.dangjian.PartyResourceDTO;
import com.pojo.dangjian.PartyResourceEntry;
import com.pojo.dangjian.PartyUser;
import com.pojo.dangjian.PartyUserDTO;
import com.pojo.lesson.DirType;
import com.pojo.teachermanage.ResumeEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fl on 2016/3/23.
 */
@Service
public class PartyResourceService {
    private PartyResourceDao partyResourceDao = new PartyResourceDao();
    private UserDao userDao = new UserDao();
    private ResumeDao resumeDao = new ResumeDao();
    private PartyUserDao partyUserDao = new PartyUserDao();

    public ObjectId addPartyResource(PartyResourceDTO partyResourceDTO){
        return partyResourceDao.addPartyResource(partyResourceDTO.exportEntry());
    }

    public void deletePartyResource(ObjectId id) throws Exception{
        PartyResourceEntry entry = partyResourceDao.getPartyResourceEntry(id);
        entry.setState(1);//标记为删除
        partyResourceDao.addPartyResource(entry);
    }

    public void editPartyResource(PartyResourceDTO partyResourceDTO) throws Exception{
        PartyResourceEntry entry = partyResourceDao.getPartyResourceEntry(new ObjectId(partyResourceDTO.getId()));
        partyResourceDTO.setUserId(entry.getUserId().toString());
        partyResourceDTO.setTerm(entry.getTerm());
        partyResourceDao.addPartyResource(partyResourceDTO.exportEntry());
    }

    public List<PartyResourceDTO> getResources(List<ObjectId> directoryIds, String term, int page, int pageSize, String userId, Boolean isAll){
        List<PartyResourceDTO> partyResourceDTOs = new ArrayList<PartyResourceDTO>();
        ObjectId uId = isAll ? null : new ObjectId(userId);
        List<PartyResourceEntry> partyResourceEntries = partyResourceDao.getPartyResourceEntrys(directoryIds, term, uId,  (page-1)*pageSize, pageSize);
        for(PartyResourceEntry entry : partyResourceEntries){
            PartyResourceDTO dto = new PartyResourceDTO(entry);
            if(dto.getUserId().equals(userId)){
                dto.setIsMine(1);
            } else {
                dto.setIsMine(0);
            }
            partyResourceDTOs.add(dto);
        }
        return partyResourceDTOs;
    }

    public int countResources(List<ObjectId> directoryIds, String term, ObjectId userId){
        return partyResourceDao.countResources(directoryIds, term, userId);
    }

    public PartyResourceDTO getResourceById(ObjectId id) throws Exception{
        PartyResourceEntry partyResourceEntry = partyResourceDao.getPartyResourceEntry(id);
        return new PartyResourceDTO(partyResourceEntry);
    }

    public ResumeEntry getResumeEntry(ObjectId userId){
        ResumeEntry resumeEntry = resumeDao.getResumeList(userId, Constant.FIELDS);
        return resumeEntry;
    }

    /**
     * 拿到一个用户在党建里的权限信息
     * @param userId
     * @param schoolId
     * @return
     */
    public PartyUserDTO getPartyUserDTO(ObjectId userId, ObjectId schoolId){
        PartyUser partyUser = partyUserDao.getPartyUser(userId);
        if(partyUser == null){
            partyUser = new PartyUser(userId, schoolId, 0, 0, 0);
            partyUserDao.addPartyUser(partyUser);
        }
        return new PartyUserDTO(partyUser);
    }

    /**
     * 一个学校老师、校长、管理员的总数
     * @param schoolId
     * @return
     */
    public int countTeacher_Manager_HeaderMaster(ObjectId schoolId){
        List<Integer> roles = getTeacher_Manager_HeaderMasterRoles();
        return userDao.countUserEntryBySchoolId(schoolId, roles);
    }

    /**
     * 一个学校老师、校长、管理员的党建功能权限
     * @param schoolId
     * @param page
     * @param pageSize
     * @return
     */
    public List<PartyUserDTO> getPartyUserDTOs(ObjectId schoolId, int page, int pageSize){
        List<PartyUserDTO> partyUserDTOs = new ArrayList<PartyUserDTO>();
        List<Integer> roles = getTeacher_Manager_HeaderMasterRoles();
        List<UserEntry> userEntries = userDao.getUserEntryBySchoolId(schoolId, roles, new BasicDBObject("nm", 1), (page-1)*pageSize, pageSize);
        List<ObjectId> userIds = MongoUtils.getFieldObjectIDs(userEntries);
        List<PartyUser> partyUsers = partyUserDao.getPartyUsers(userIds);
        Map<ObjectId, PartyUser> partyUserMap = new HashMap<ObjectId, PartyUser>();
        for(PartyUser partyUser : partyUsers){
            partyUserMap.put(partyUser.getUserId(), partyUser);
        }

        for(UserEntry userEntry : userEntries){
            PartyUser partyUser = partyUserMap.get(userEntry.getID());
            if(partyUser == null){
                partyUser = new PartyUser(userEntry.getID(), schoolId, 0, 0, 0);
                partyUserDao.addPartyUser(partyUser);
            }
            PartyUserDTO partyUserDTO = new PartyUserDTO(partyUser);
            partyUserDTO.setUserName(userEntry.getUserName());
            partyUserDTOs.add(partyUserDTO);
        }
        return partyUserDTOs;
    }

    private List<Integer> getTeacher_Manager_HeaderMasterRoles(){
        List<Integer> roles = new ArrayList<Integer>();
        roles.add(UserRole.TEACHER.getRole());
        roles.add(UserRole.TEACHER.getRole()|UserRole.HEADMASTER.getRole());
        roles.add(UserRole.TEACHER.getRole()|UserRole.ADMIN.getRole());
        roles.add(UserRole.ADMIN.getRole()|UserRole.HEADMASTER.getRole());
        roles.add(UserRole.ADMIN.getRole()|UserRole.HEADMASTER.getRole()|UserRole.TEACHER.getRole());
        return roles;
    }

    public void updatePartyUser(int isPartyMember, int isCenterMember, int isPartySecretary, ObjectId id){
        partyUserDao.updatePartyUser(isPartyMember, isCenterMember, isPartySecretary, id);
    }


}
