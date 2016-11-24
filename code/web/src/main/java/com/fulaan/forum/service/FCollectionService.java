package com.fulaan.forum.service;

import com.db.forum.FCollectionDao;
import com.pojo.forum.FCollectionDTO;
import com.pojo.forum.FCollectionEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/30.
 */
@Service
public class FCollectionService {
    FCollectionDao collectionDao = new FCollectionDao();

    /**
     * 新增
     *
     * @param userId
     * @param postSectionId
     * @param type
     * */
    public void addCollection(ObjectId userId,String postSectionId,int type){
        FCollectionEntry collectionEntry = new FCollectionEntry(userId,new ObjectId(postSectionId),type);
        collectionDao.addCollection(collectionEntry);
    }

    /**
     * 获取收藏
     *
     * @param userId
     * @param type
     * @return
     * */
    public List<FCollectionDTO> getCollections(ObjectId userId,int type){
        List<FCollectionDTO> collectionDTOList = new ArrayList<FCollectionDTO>();

        return collectionDTOList;
    }

    /**
     * 判断是否收藏
     *
     * @param userId
     * @param postSectionId
     * @return
     * */
    public boolean isCollected(ObjectId userId,String postSectionId){
        FCollectionEntry entry = collectionDao.getCollection(userId,new ObjectId(postSectionId));
        if(entry != null){
            return true;
        }
        return false;
    }


    /**
     * 取消收藏
     *
     * @param id
     */
    public void remove(String id){
        collectionDao.removeCollection(new ObjectId(id));
    }


}
