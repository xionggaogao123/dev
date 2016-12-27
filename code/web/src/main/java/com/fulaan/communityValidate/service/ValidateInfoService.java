package com.fulaan.communityValidate.service;

import com.db.communityValidate.ValidateInfoDao;
import com.db.fcommunity.CommunityDao;
import com.fulaan.communityValidate.dto.ValidateInfoDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.ValidateInfoEntry;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 批量添加数据
     * @param userId
     * @param communityId
     * @param message
     */
    public void saveValidateInfos(ObjectId userId,ObjectId communityId,String message,int way){
        ObjectId reviewKeyId=new ObjectId();
        List<ValidateInfoEntry> entries=new ArrayList<ValidateInfoEntry>();
        ValidateInfoEntry entry=new ValidateInfoEntry(userId,message,communityId,0,way,reviewKeyId);
        entries.add(entry);
        ObjectId groupId=communityService.getGroupId(communityId);
        List<MemberDTO> memberDTOs=memberService.getManagers(groupId);
        for(MemberDTO memberDTO:memberDTOs){
            ValidateInfoEntry validateInfoEntry=new ValidateInfoEntry(userId,new ObjectId(memberDTO.getUserId()),
                    message,communityId,1,reviewKeyId);
            entries.add(validateInfoEntry);
        }
        validateInfoDao.batchSaveInfo(entries);
    }


    /**
     * 查询未处理的数据（相当于已读和未读）
     */
    public int getValidateInfoCount(ObjectId userId){
        //分两种情况
        //1.申请人(type=0)
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
        for(ValidateInfoEntry entry:entries){
            cmIds.add(entry.getCommunityId());
        }
        Map<ObjectId,CommunityEntry> map=communityDao.findMapInfo(cmIds);
        for(ValidateInfoEntry entry:entries){
            ValidateInfoDTO dto=new ValidateInfoDTO(entry);
            CommunityEntry communityEntry=map.get(new ObjectId(dto.getCommunityId()));
            if(null!=communityEntry){
                dto.setCommunityName(communityEntry.getCommunityName());
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






}
