package com.fulaan.service;

import com.db.fcommunity.FeedbackDao;
import com.fulaan.community.dto.FeedbackDTO;
import com.fulaan.user.service.UserService;
import com.pojo.fcommunity.FeedbackEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by scott on 2017/3/21.
 */
@Service
public class FeedbackService {

    FeedbackDao feedbackDao=new FeedbackDao();

    @Autowired
    private UserService userService;

    /**
     * 添加留言功能
     * @param userId
     * @param content
     */
    public void saveFeedBack(ObjectId userId,String content){
        FeedbackEntry entry=new FeedbackEntry(userId,content);
        feedbackDao.addFeedbackEntry(entry);
    }

    /**
     * 删除留言反馈信息
     * @param id
     */
    public void removeFeedBack(ObjectId id){
        feedbackDao.removeFeedbackInfo(id);
    }


    /**
     * 查询留言反馈列表
     * @param page
     * @param pageSize
     * @return
     */
    public List<FeedbackDTO> getFeedbackDtos(int page,int pageSize,ObjectId userId){
        List<FeedbackDTO> feedbackDTOs=new ArrayList<FeedbackDTO>();
        List<FeedbackEntry> entries=feedbackDao.getEntries(page, pageSize);
        Set<ObjectId> userIds=new HashSet<ObjectId>();
        for(FeedbackEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        userIds.add(userId);
        Map<ObjectId,UserEntry> map=userService.getUserEntryMap(userIds, Constant.FIELDS);
        UserEntry userEntry=map.get(userId);
        for(FeedbackEntry entry:entries){
            FeedbackDTO feedbackDTO=new FeedbackDTO(entry);
            feedbackDTO.setUserPermission(0);
            UserEntry user=map.get(entry.getUserId());
            if(null!=user){
                feedbackDTO.setUserName(user.getUserName());
            }
            if(null!=userEntry){
                if(userEntry.getRole()>100){
                    feedbackDTO.setUserPermission(1);
                }
            }
            feedbackDTOs.add(feedbackDTO);
        }
        return feedbackDTOs;
    }

    public int countFeedBack(){
        return feedbackDao.countEntries();
    }


}
