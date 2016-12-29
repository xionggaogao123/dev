package com.fulaan.communityValidate.service;

import com.db.communityValidate.ValidateInfoDao;
import com.db.fcommunity.CommunityDao;
import com.fulaan.communityValidate.dto.ValidateInfoDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.UserService;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.ValidateInfoEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by admin on 2016/12/26.
 */
@Service
public class ValidateInfoService {
    private ValidateInfoDao validateInfoDao=new ValidateInfoDao();

    private CommunityDao communityDao=new CommunityDao();

    @Autowired
    private CommunityService communityService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

    /**
     * 批量添加数据
     * @param userId
     * @param communityId
     * @param message
     */
    public boolean saveValidateInfos(ObjectId userId,ObjectId communityId,String message,int way){
        //先校验是否申请过，若申请过的话返回已经申请过了。
        ValidateInfoEntry entry1=validateInfoDao.getIsApplyEntry(userId,communityId);
        if(null!=entry1){
            return false;
        }else {
            ObjectId reviewKeyId = new ObjectId();
            List<ValidateInfoEntry> entries = new ArrayList<ValidateInfoEntry>();
            ValidateInfoEntry entry = new ValidateInfoEntry(userId, message, communityId, 0, way, reviewKeyId);
            entries.add(entry);
            ObjectId groupId = communityService.getGroupId(communityId);
            List<MemberDTO> memberDTOs = memberService.getManagers(groupId);
            for (MemberDTO memberDTO : memberDTOs) {
                ValidateInfoEntry validateInfoEntry = new ValidateInfoEntry(userId, new ObjectId(memberDTO.getUserId()),
                        message, communityId, 1, way, reviewKeyId);
                entries.add(validateInfoEntry);
            }
            batchSaveInfo(entries);
            return true;
        }
    }

    public void batchSaveInfo(List<ValidateInfoEntry> entries){
        validateInfoDao.batchSaveInfo(entries);
    }


    /**
     * 查询未处理的数据（相当于已读和未读）
     */
    public int getValidateInfoCount(ObjectId userId){
        //分两种情况
        int applyCount=validateInfoDao.getApplyCount(userId,0);
        int approvedCount=validateInfoDao.getApprovedCount(userId,1);
        return applyCount+approvedCount;
    }


    /**
     * 分页查找数据
     * @param reviewedId
     * @param page
     * @param pageSize
     * @return
     */
    public List<ValidateInfoDTO> getValidateInfos(ObjectId reviewedId,int page,int pageSize){
        List<ValidateInfoDTO> dtos=new ArrayList<ValidateInfoDTO>();
        List<ValidateInfoEntry> entries=validateInfoDao.getValidateInfos(reviewedId,page,pageSize);
        List<ObjectId> cmIds=new ArrayList<ObjectId>();

        Set<ObjectId> ids=new HashSet<ObjectId>();
        for(ValidateInfoEntry entry:entries){
            cmIds.add(entry.getCommunityId());
            ids.add(entry.getUserId());
        }
        ids.add(reviewedId);
        List<ObjectId> userIds=new ArrayList<ObjectId>(ids);
        Map<ObjectId,UserEntry> userMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId,CommunityEntry> map=communityDao.findMapInfo(cmIds);
        for(ValidateInfoEntry entry:entries){
            ValidateInfoDTO dto=new ValidateInfoDTO(entry);
            CommunityEntry communityEntry=map.get(new ObjectId(dto.getCommunityId()));
            if(null!=communityEntry){
                dto.setCommunityName(communityEntry.getCommunityName());
            }

            UserEntry userEntry=userMap.get(entry.getUserId());
            if(null!=userEntry){
                dto.setUserName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            }

            if (StringUtils.isNotBlank(dto.getApprovedId())) {
                UserEntry user = userService.findById(entry.getApprovedId());
                if (null != user) {
                    dto.setReviewName(StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName());
                }
            }

            if(dto.getType()==1) {
                dto.setOwner(0);
                if (StringUtils.isNotBlank(dto.getApprovedId())) {
                    if (dto.getApprovedId().equals(dto.getReviewedId())) {
                        dto.setOwner(1);
                    }
                }
            }
            dtos.add(dto);
        }
        return dtos;
    }

    public int countValidateInfos(ObjectId reviewedId){
        return validateInfoDao.countValidateInfos(reviewedId);
    }

    /**
     * 同意或者拒绝用户申请时获取所有的管理员信息
     * @param userId
     * @param communityId
     * @param reviewKeyId
     * @return
     */
    public List<ValidateInfoEntry> getEntries(ObjectId userId,ObjectId communityId,ObjectId reviewKeyId){
        return validateInfoDao.getEntries(userId, communityId, reviewKeyId);
    }

    /**
     * 更新权限状态
     * @param communityId
     * @param authority
     */
    public void updateAuthority(ObjectId reviewedId,ObjectId communityId,int authority){
        validateInfoDao.updateAuthority(reviewedId, communityId, authority);
    }


    /**
     * 获取验证消息
     * @param userId
     * @param reviewKeyId
     * @param communityId
     * @return
     */
    public ValidateInfoEntry getEntry(ObjectId userId,ObjectId reviewKeyId,ObjectId communityId){
        return validateInfoDao.getEntry(userId, reviewKeyId, communityId);
    }

    public ValidateInfoEntry getApplyEntry(ObjectId userId,ObjectId reviewKeyId,ObjectId communityId){
        return validateInfoDao.getApplyEntry(userId, communityId,reviewKeyId);
    }

    public void saveOrUpdate(ValidateInfoEntry entry){
        validateInfoDao.saveOrUpdate(entry);
    }

}
