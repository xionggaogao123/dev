package com.fulaan.forum.service;

import com.db.forum.FPostDao;
import com.db.forum.FReplyDao;
import com.db.forum.FSectionDao;
import com.pojo.forum.FSectionCountDTO;
import com.pojo.forum.FSectionDTO;
import com.pojo.forum.FSectionEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/5/31.
 */
@Service
public class FSectionService {


    //=====================================商品分类============================================

    private FSectionDao fSectionDao = new FSectionDao();
    private FPostDao fPostDao = new FPostDao();
    private FReplyDao fReplyDao = new FReplyDao();

    /**
     * 增加一个板块
     * @param fSectionDTO
     * @return
     */
    public ObjectId addFSection(FSectionDTO fSectionDTO){
        return fSectionDao.addFSectionEntry(fSectionDTO.exportEntry());
    }

    /**
     * 更新板块
     * */
    public ObjectId updateFSection(FSectionDTO dto){
        return fSectionDao.updateFSectionEntry(dto.exportEntry());
    }

    /**
     * 更新板块排序
     * */
    public void updateFSectionSort(ObjectId selfId,int selfSort,ObjectId anotherId,int anotherSort){
        fSectionDao.updateFSectionEntrySort(selfId, selfSort, anotherId, anotherSort);
    }

    /**
     * 上传首页图片
     * */
    public void updateFSectionImg(ObjectId id,String type,String imgUrl){
        fSectionDao.updateFSectionEntryImg(id, type, imgUrl);
    }


    /**
     * 得到子板块
     * @param parentId
     * @return
     */
    public List<FSectionDTO> getFSectionListByParentId(ObjectId parentId){
        List<FSectionDTO> fSectionDTOArrayList = new ArrayList<FSectionDTO>();
        List<FSectionEntry> fSectionEntryList = fSectionDao.getFSectionListByParentId(parentId);
        for(FSectionEntry fSectionEntry : fSectionEntryList){
            fSectionDTOArrayList.add(new FSectionDTO(fSectionEntry));
        }
        return fSectionDTOArrayList;
    }

    /**
     * 得到某等级所有板块
     * @param level
     * @return
     */
    public List<FSectionDTO> getFSectionListByLevel(int level,ObjectId id,String Name){
        List<FSectionDTO> fSectionDTOArrayList = new ArrayList<FSectionDTO>();
        List<FSectionEntry> fSectionEntries = fSectionDao.getFSectionListByLevel(level, id,Name);
        for(FSectionEntry fSectionEntry : fSectionEntries){
            fSectionDTOArrayList.add(new FSectionDTO(fSectionEntry));
        }
        return fSectionDTOArrayList;
    }

    /**
     * 通过id查询板块
     * */
    public FSectionDTO findFSectionById(ObjectId id)
    {
        FSectionEntry entry=fSectionDao.getFSection(id);
        return new FSectionDTO(entry);
    }

    /**
     * 删除板块及其子板块
     * @param fSectionId
     */
    public void deleteFSection(ObjectId fSectionId){
        fSectionDao.deleteFSectionEntry(fSectionId);
    }

    /**
     *
     */

    /**
     * 通过板块Id查找总浏览数、总评论数、主题数、贴数
     * @param postSectionId
     * @return
     */
    public List<FSectionCountDTO> getFSectionListByLevel(ObjectId postSectionId,String name){
        List<FSectionCountDTO> retList=new ArrayList<FSectionCountDTO>();
        FSectionCountDTO item=new FSectionCountDTO();
        int  totalScanCount= fPostDao.getTotalScanCount(postSectionId,name);
        int  totalCommentCount = fPostDao.getTotalCommentCount(postSectionId,name);
        int  themeCount= fPostDao.getThemeCount(postSectionId,name);
        int  postCount = fReplyDao.getFReplyCount(postSectionId,name);
        item.setTotalScanCount(totalScanCount);
        item.setTotalCommentCount(totalCommentCount);
        item.setThemeCount(themeCount);
        item.setPostCount(postCount);
        retList.add(item);
        return retList;
    }

}
