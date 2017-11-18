package com.fulaan.indexpage.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.WebHomePageDao;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by scott on 2017/11/17.
 */
@Service
public class WebHomePageService {

    private WebHomePageDao webHomePageDao =new WebHomePageDao();

    private MemberDao memberDao = new MemberDao();

    private CommunityDao communityDao = new CommunityDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();



    public Map<String,Long> setTime(int mode, String sTime, String eTime)throws Exception{
        Map<String,Long> retMap=new HashMap<String, Long>();
        long startTime=0L;
        long endTime=0L;
        if(mode==0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startTime = calendar.getTime().getTime();
            endTime = System.currentTimeMillis();
        }else if(mode==1){
            endTime = System.currentTimeMillis();
            startTime = endTime - (60L*60L*1000L*24L*7L);
        }else{
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isNotEmpty(sTime)){
                startTime=format.parse(sTime).getTime();
            }
            if(StringUtils.isNotEmpty(eTime)){
                endTime=format.parse(eTime).getTime();
            }
            if(startTime>endTime){
                throw new Exception("传入的时间结束时间不能小于开始时间");
            }
        }
        retMap.put("startTime",startTime);
        retMap.put("endTime",endTime);
        return retMap;
    }
    /**
     *
     * @param userId
     * @param type
     * @param subjectId
     * @param mode 0:今天 1:一周内 2:时间参数
     * @param status
     * @param sTime
     * @param eTime
     * @param page
     * @param pageSize
     * @throws Exception
     */
    public  void getMyReceivedEntries(ObjectId userId,
                                      int type,
                                      String subjectId,
                                      int mode,
                                      int status,
                                      String sTime,
                                      String eTime,
                                      String communityId,
                                      int page,
                                      int pageSize)throws Exception{

        Map<String,Long> retMap=setTime(mode,sTime,eTime);
        long startTime=retMap.get("startTime");
        long endTime=retMap.get("endTime");
        List<ObjectId> communityIds=new ArrayList<ObjectId>();
        if(ObjectId.isValid(communityId)) {
            List<ObjectId> groupIds = memberDao.getGroupIdsByUserId(userId);
            List<CommunityEntry> communityEntries=communityDao.getCommunityEntriesByGroupIds(groupIds);
            for(CommunityEntry communityEntry:communityEntries){
                communityIds.add(communityEntry.getID());
            }
        }else{
            communityIds.add(new ObjectId(communityId));
        }
        List<ObjectId> receiveIds=new ArrayList<ObjectId>();
        if(type== Constant.NEGATIVE_ONE||type==Constant.THREE){
            List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao.getEntriesByMainUserId(userId);
            for(NewVersionCommunityBindEntry bindEntry:bindEntries){
                receiveIds.add(bindEntry.getUserId());
            }
        }
        ObjectId sId=ObjectId.isValid(subjectId)?new ObjectId(subjectId):null;

        List<WebHomePageEntry> entries=webHomePageDao.getMyReceivedHomePageEntries(communityIds, receiveIds, type, sId, startTime, endTime, status, userId, page,pageSize
        );
    }


    /**
     *
     * @param userId
     * @param type
     * @param subjectId
     * @param mode
     * @param status
     * @param sTime
     * @param eTime
     * @param communityId
     * @param page
     * @param pageSize
     * @throws Exception
     */
    public  void getMySendHomePageEntries(ObjectId userId,
                                      int type,
                                      String subjectId,
                                      int mode,
                                      int status,
                                      String sTime,
                                      String eTime,
                                      String communityId,
                                      int page,
                                      int pageSize)throws Exception{
        ObjectId cId=ObjectId.isValid(communityId)?new ObjectId(communityId):null;
        Map<String,Long> retMap=setTime(mode,sTime,eTime);
        long startTime=retMap.get("startTime");
        long endTime=retMap.get("endTime");
        ObjectId sId=ObjectId.isValid(subjectId)?new ObjectId(subjectId):null;
        List<WebHomePageEntry> entries=webHomePageDao.getMySendHomePageEntries(cId, type, sId, startTime, endTime, status, userId, page,pageSize
        );

    }



}
