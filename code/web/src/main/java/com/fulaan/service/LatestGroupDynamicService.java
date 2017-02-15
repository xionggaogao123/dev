package com.fulaan.service;

import com.db.fcommunity.LatestGroupDynamicDao;
import com.pojo.fcommunity.LatestGroupDynamicEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by admin on 2017/2/15.
 */
@Service
public class LatestGroupDynamicService {
    private LatestGroupDynamicDao latestGroupDynamicDao =new LatestGroupDynamicDao();

    public void saveLatestInfo(ObjectId communityId,ObjectId communityDetailId,String message,
                               ObjectId userId,int type){
        LatestGroupDynamicEntry  entry=new LatestGroupDynamicEntry(communityId,communityDetailId,userId,
                new ArrayList<ObjectId>(),type,message);
        latestGroupDynamicDao.saveItem(entry);
    }

    public LatestGroupDynamicEntry getLatestInfo(ObjectId communityId){
        LatestGroupDynamicEntry entry=latestGroupDynamicDao.getLatestInfo(communityId);
        return entry;
    }

    public void pushRead(ObjectId id,ObjectId userId){
        latestGroupDynamicDao.pushRead(id,userId);
    }
}
