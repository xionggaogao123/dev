package com.fulaan.mall.service;

import com.db.ebusiness.ECategoryVideoDao;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.ebusiness.ECategoryVideoDTO;
import com.pojo.ebusiness.ECategoryVideoEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/3/31.
 * <p>
 * 商品分类视频
 */
@Service
public class ECategoryVideoService {
    private ECategoryVideoDao eCategoryVideoDao = new ECategoryVideoDao();
    private VideoService videoService = new VideoService();

    /**
     * 通过videoid获取videoUrl
     */
    private String getVideoUrl(ObjectId videoId) {
        String fileKey = videoService.getVideoEntryById(videoId).getBucketkey();
        String qiniuPath = QiniuFileUtils.getPath(5, fileKey);
        return qiniuPath;
    }

    /**
     * 新增
     */
    public String addCategoryVideo(ECategoryVideoDTO dto) {
        if (!StringUtils.isEmpty(dto.getVideoId())) {
            dto.setVideoUrl(getVideoUrl(new ObjectId(dto.getVideoId())));
        }

        eCategoryVideoDao.addCategoryVideo(dto.exportEntry());
        return dto.getId();
    }

    /**
     * 更新
     */
    public String updateVideo(ECategoryVideoDTO dto) {
        if (!StringUtils.isEmpty(dto.getVideoId())) {
            dto.setVideoUrl(getVideoUrl(new ObjectId(dto.getVideoId())));
        }
        eCategoryVideoDao.updateCategoryVideo(dto.exportEntry());
        return dto.getId();
    }

    /**
     * 删除
     */
    public void deleteVideo(String id) {
        eCategoryVideoDao.deleteCategroyVideo(new ObjectId(id));
    }


    /**
     * 分页查询
     */
    public List<ECategoryVideoDTO> getVideoList(String categoryId, int skip, int limit) {
        List<ECategoryVideoDTO> rtList = new ArrayList<ECategoryVideoDTO>();
        ObjectId cid = null;
        if (!categoryId.equals("")) {
            cid = new ObjectId(categoryId);
        }
        List<ECategoryVideoEntry> results = eCategoryVideoDao.getCategoryVideoList(cid, skip, limit);
        for (ECategoryVideoEntry entry : results) {
            rtList.add(new ECategoryVideoDTO(entry));
        }
        return rtList;
    }

    /**
     * 查询（不分页）
     */
    public List<ECategoryVideoDTO> getVideoList(String categoryId) {
        List<ECategoryVideoDTO> rtList = new ArrayList<ECategoryVideoDTO>();
        ObjectId cid = null;
        if (!categoryId.equals("")) {
            cid = new ObjectId(categoryId);
        }
        List<ECategoryVideoEntry> results = eCategoryVideoDao.getCategoryVideoList(cid);
        for (ECategoryVideoEntry entry : results) {
            rtList.add(new ECategoryVideoDTO(entry));
        }
        return rtList;
    }

    /**
     * 查询总数
     */
    public int getCount(String categoryId) {
        ObjectId cid = null;
        if (!categoryId.equals("")) {
            cid = new ObjectId(categoryId);
        }
        int count = eCategoryVideoDao.getCount(cid);
        return count;
    }

    /**
     * 根据id查询
     */
    public ECategoryVideoDTO getvideoById(String videoId) {
        ECategoryVideoEntry entry = eCategoryVideoDao.getCategoryVideoById(new ObjectId(videoId));
        ECategoryVideoDTO dto = new ECategoryVideoDTO(entry);
        return dto;
    }

}
