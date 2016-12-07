package com.fulaan.train.service;

import com.db.train.CriticismDao;
import com.fulaan.train.dto.CriticismDTO;
import com.fulaan.user.service.UserService;
import com.pojo.train.CriticismEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/6.
 */
@Service
public class CriticismService {

    private CriticismDao criticismDao=new CriticismDao();

    @Autowired
    private UserService userService;

    public void saveOrUpdate(String comment, ObjectId userId,ObjectId instituteId,int score){
        CriticismEntry entry=new CriticismEntry(instituteId, score, userId, comment);
        criticismDao.saveOrUpdate(entry);
    }

    public void saveEntry(CriticismEntry entry){
        criticismDao.saveOrUpdate(entry);
    }

    public int countCriticisms(ObjectId instituteId){
       return criticismDao.countCriticismEntries(instituteId);
    }

    public List<CriticismDTO> getCriticismDTOs(ObjectId instituteId,int page,int pageSize){
        List<CriticismDTO> dtos=new ArrayList<CriticismDTO>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        List<CriticismEntry> entries=criticismDao.getCriticismEntries(instituteId, page, pageSize);
        for(CriticismEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(CriticismEntry entry:entries){
            CriticismDTO criticismDTO=new CriticismDTO(entry);
            UserEntry userEntry=userEntryMap.get(entry.getUserId());
            criticismDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),
                    AvatarType.MIN_AVATAR.getType()));
            criticismDTO.setNickName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            int tip=1;
            int score=criticismDTO.getScore();
            List<Integer> tips=new ArrayList<Integer>();
            List<Integer> unTips=new ArrayList<Integer>();
            for(int i=0;i<score;i++){
                tips.add(tip);
            }
            criticismDTO.setScoreList(tips);
            for(int i=0;i<5-score;i++){
                unTips.add(tip);
            }
            criticismDTO.setUnScoreList(unTips);
            dtos.add(criticismDTO);
        }
        return dtos;
    }

    public CriticismEntry getEntry(ObjectId instituteId,ObjectId userId){
        return  criticismDao.getEntry(instituteId, userId);
    }
}
