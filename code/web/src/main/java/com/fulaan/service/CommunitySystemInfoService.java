package com.fulaan.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.CommunitySystemInfoDao;
import com.fulaan.community.dto.CommunitySystemInfoDTO;
import com.fulaan.user.service.UserService;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.CommunitySystemInfoEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/11/29.
 */
@Service
public class CommunitySystemInfoService {

    private CommunitySystemInfoDao communitySystemInfoDao=new CommunitySystemInfoDao();
    private CommunityDao communityDao=new CommunityDao();
    @Autowired
    private UserService userService;



    /**
     * 保存或更新数据
     * @param userId
     * @param roleStr
     * @param type
     * @param communityId
     */
    public void saveOrupdateEntry(ObjectId userId,ObjectId relationId,String roleStr,int type,ObjectId communityId){
        CommunitySystemInfoEntry entry=new CommunitySystemInfoEntry(roleStr,userId,relationId,type,communityId,System.currentTimeMillis());
        communitySystemInfoDao.saveOrUpdateEntry(entry);
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    public List<CommunitySystemInfoDTO> findInfoByUserIdAndType(ObjectId relationId,int page,int pageSize){
        List<CommunitySystemInfoDTO> dtos=new ArrayList<CommunitySystemInfoDTO>();
        List<ObjectId> ids=new ArrayList<ObjectId>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        List<CommunitySystemInfoEntry> entries=communitySystemInfoDao.findEntriesByUserIdAndType(relationId,page, pageSize);

        for(CommunitySystemInfoEntry entry:entries){
            ids.add(entry.getCommunityId());
            userIds.add(entry.getUserId());
        }

        Map<ObjectId,CommunityEntry> map=communityDao.findMapInfo(ids);
        Map<ObjectId,UserEntry> map1=userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(CommunitySystemInfoEntry entry:entries){
            CommunitySystemInfoDTO communitySystemInfoDTO=new CommunitySystemInfoDTO(entry);
            CommunityEntry communityEntry=map.get(entry.getCommunityId());
            communitySystemInfoDTO.setCommunityName(communityEntry.getCommunityName());
            UserEntry userEntry=map1.get(entry.getUserId());
            communitySystemInfoDTO.setNickName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            dtos.add(communitySystemInfoDTO);
        }
        return dtos;
    }

    public int countEntriesByUserIdAndType(ObjectId relationId){
        return communitySystemInfoDao.countEntriesByUserIdAndType(relationId);
    }


    public void setAllData(ObjectId relationId){
        communitySystemInfoDao.setAllData(relationId);
    }

    public void addBatchData(ObjectId userId,List<ObjectId> ids,String roleStr,int type,ObjectId communityId){
        communitySystemInfoDao.addBatchData(userId, ids, roleStr, type, communityId);
    }

    public int findUnReadInfo(ObjectId relationId){
        return communitySystemInfoDao.findUnReadInfo(relationId);
    }

}
