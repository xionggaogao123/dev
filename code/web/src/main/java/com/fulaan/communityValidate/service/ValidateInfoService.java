package com.fulaan.communityValidate.service;

import com.db.communityValidate.ValidateInfoDao;
import com.fulaan.dto.MemberDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.pojo.fcommunity.ValidateInfoEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */
@Service
public class ValidateInfoService {
    private ValidateInfoDao validateInfoDao=new ValidateInfoDao();

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
    public void saveValidateInfos(ObjectId userId,ObjectId communityId,String message){
        List<ValidateInfoEntry> entries=new ArrayList<ValidateInfoEntry>();
        ValidateInfoEntry entry=new ValidateInfoEntry(userId,userId,message,communityId,0);
        entries.add(entry);
        ObjectId groupId=communityService.getGroupId(communityId);
        List<MemberDTO> memberDTOs=memberService.getManagers(groupId);
        for(MemberDTO memberDTO:memberDTOs){
            ValidateInfoEntry validateInfoEntry=new ValidateInfoEntry(userId,new ObjectId(memberDTO.getUserId()),
                    message,communityId,1);
            entries.add(validateInfoEntry);
        }
        validateInfoDao.batchSaveInfo(entries);
    }




}
