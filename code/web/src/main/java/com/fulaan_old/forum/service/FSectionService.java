package com.fulaan_old.forum.service;

import com.db.forum.FPostDao;
import com.db.forum.FReplyDao;
import com.db.forum.FSectionDao;
import com.pojo.forum.FSectionCountDTO;
import com.pojo.forum.FSectionDTO;
import com.pojo.forum.FSectionEntry;
import com.pojo.forum.Classify;
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

    private FSectionDao fSectionDao = new FSectionDao();
    private FPostDao fPostDao = new FPostDao();
    private FReplyDao fReplyDao = new FReplyDao();
    private FSectionDao mFSectionDao = new FSectionDao();

    /**
     * 增加一个板块
     *
     * @param fSectionDTO
     * @return
     */
    public ObjectId addFSection(FSectionDTO fSectionDTO) {
        return fSectionDao.addFSectionEntry(fSectionDTO.exportEntry());
    }

    /**
     * 更新板块
     */
    public ObjectId updateFSection(FSectionDTO dto) {
        return fSectionDao.updateFSectionEntry(dto.exportEntry());
    }

    /**
     * 更新板块排序
     */
    public void updateFSectionSort(ObjectId selfId, int selfSort, ObjectId anotherId, int anotherSort) {
        fSectionDao.updateFSectionEntrySort(selfId, selfSort, anotherId, anotherSort);
    }

    /**
     * 上传首页图片
     */
    public void updateFSectionImg(ObjectId id, String type, String imgUrl) {
        fSectionDao.updateFSectionEntryImg(id, type, imgUrl);
    }

    /**
     * 得到某等级所有板块
     *
     * @param level
     * @return
     */
    public List<FSectionDTO> getFSectionListByLevel(int level, ObjectId id, String Name) {
        List<FSectionDTO> fSectionDTOArrayList = new ArrayList<FSectionDTO>();
        List<FSectionEntry> fSectionEntries = fSectionDao.getFSectionListByLevel(level, id, Name);
        for (FSectionEntry fSectionEntry : fSectionEntries) {
            fSectionDTOArrayList.add(new FSectionDTO(fSectionEntry));
        }
        return fSectionDTOArrayList;
    }

    /**
     * 通过id查询板块
     */
    public FSectionDTO findFSectionById(ObjectId id) {
        FSectionEntry entry = fSectionDao.getFSection(id);
        int themeCount = fPostDao.getPostCount(id);
        int count = fPostDao.getTodayPostCount(id);
        entry.setCount(count);
        entry.setTotalCount(themeCount);
        return new FSectionDTO(entry);
    }

    /**
     * 删除板块及其子板块
     *
     * @param fSectionId
     */
    public void deleteFSection(ObjectId fSectionId) {
        fSectionDao.deleteFSectionEntry(fSectionId);
    }

    /**
     * 更新总浏览量
     */
    public void updateTotalScanCount(ObjectId id) {
        fSectionDao.updateTotalScanCount(id);
    }

    /**
     * 更新回帖量和总发帖量
     *
     * @param id
     */
    public void updateReplyAndPost(ObjectId id) {
        fSectionDao.updateReplyAndPost(id);
    }

    public void updateTotalComment(ObjectId id) {
        fSectionDao.updateTotalComment(id);
    }

    /**
     * 更新发帖量和总发帖量
     *
     * @param id
     */
    public void updateThemeAndPost(ObjectId id) {
        fSectionDao.updateThemeAndPost(id);
    }

    public void updateThemeCount(ObjectId id) {
        fSectionDao.updateThemeCount(id);
    }

    /**
     * 通过板块Id查找所有数据
     */
    public List<FSectionCountDTO> getFSectionList() {
        List<FSectionCountDTO> retList = new ArrayList<FSectionCountDTO>();
        List<FSectionEntry> ll = fSectionDao.getFSection();
        for (FSectionEntry item : ll) {
            FSectionCountDTO fSectionCountDTO = new FSectionCountDTO();
            fSectionCountDTO.setMemo(item.getMemo());
            fSectionCountDTO.setfSectionId(item.getID().toString());
            fSectionCountDTO.setMemoName(item.getMemoName());
            fSectionCountDTO.setName(item.getName());
            fSectionCountDTO.setSectionName(item.getSectionName());
            fSectionCountDTO.setImageAppSrc(item.getImageAppSrc());
            fSectionCountDTO.setImageBigAppSrc(item.getImageBigAppSrc());
            fSectionCountDTO.setTotalScanCount(item.getTotalScanCount());
            fSectionCountDTO.setTotalCommentCount(item.getTotalCommentCount());
            fSectionCountDTO.setThemeCount(item.getThemeCount());
            fSectionCountDTO.setPostCount(item.getPostCount());
            fSectionCountDTO.setSort(item.getSort());
            retList.add(fSectionCountDTO);
        }
        return retList;
    }

    /**
     * 通过板块Id查找总浏览数、总评论数、主题数、贴数
     *
     * @param postSectionId
     * @return
     */
    public List<FSectionCountDTO> getFSectionListByLevel(ObjectId postSectionId) {
        List<FSectionCountDTO> retList = new ArrayList<FSectionCountDTO>();
        FSectionCountDTO item = new FSectionCountDTO();
        FSectionEntry fSectionEntry = fSectionDao.getFSection(postSectionId);
        item.setTotalScanCount(fSectionEntry.getTotalScanCount());
        item.setTotalCommentCount(fSectionEntry.getTotalCommentCount());
        item.setThemeCount(fSectionEntry.getThemeCount());
        item.setPostCount(fSectionEntry.getPostCount());
        retList.add(item);
        return retList;
    }

    public FSectionDTO getValidate(ObjectId postSectionId) {

        FSectionEntry fSectionEntry = fSectionDao.getFSection(postSectionId);
        FSectionDTO item = new FSectionDTO(fSectionEntry);
        int totalScanCount = fPostDao.getTotalScanCount(postSectionId);
        int themeCount = fPostDao.getPostCount(postSectionId);
        int postCount = fReplyDao.getFReplyCount(postSectionId, "");
        int count = themeCount + postCount;
        item.setTotalScanCount(totalScanCount);
        item.setTotalCommentCount(themeCount);
        item.setThemeCount(postCount);
        item.setPostCount(count);
        return item;
    }

    /**
     * 通过板块Id查找各个分类的帖子数
     *
     * @param sectionId
     * @return Map
     */
    public Map<String, Object> getClassify(ObjectId sectionId) {
        Map<String, Object> model = new HashMap<String, Object>();
        int officalCount = fPostDao.sectionPostCount(sectionId, Classify.OFFICIAL);
        int activityCount = fPostDao.sectionPostCount(sectionId, Classify.ACTIVITY);
        int helpCount = fPostDao.sectionPostCount(sectionId, Classify.HELP);
        int adviceCount = fPostDao.sectionPostCount(sectionId, Classify.ADVICE);
        int chatCount = fPostDao.sectionPostCount(sectionId, Classify.CHAT);
        int originalCount = fPostDao.sectionPostCount(sectionId, Classify.ORIGINAL);
        int otherCount = fPostDao.sectionPostCount(sectionId, Classify.OTHER);
        model.put("classifyOne", officalCount);
        model.put("classifyTwo", activityCount);
        model.put("classifyThree", chatCount);
        model.put("classifyFour", originalCount);
        model.put("classifyFive", helpCount + adviceCount + otherCount);
        return model;
    }
}
