package com.fulaan_old.video.service;

import com.db.video.VideoDao;
import com.mongodb.DBObject;
import com.pojo.video.VideoEntry;
import com.sys.exceptions.IllegalParamException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * 视频服务
 *
 * @author fourer
 */
@Service
public class VideoService {

    private VideoDao videoDao = new VideoDao();

    public ObjectId addVideoEntry(VideoEntry e) {
        return videoDao.addVideoEntry(e);
    }

    /**
     * @param id
     * @return
     */
    public VideoEntry getVideoEntryById(ObjectId id) {
        return videoDao.getVideoEntryById(id);
    }


    public VideoEntry getVideoEntryByPersistentId(String perId) {
        VideoEntry ve = videoDao.getVideoEntryByPersistentId(perId);
        return ve;
    }

    /**
     * 根据ID集合查询
     *
     * @param col
     * @param fields
     * @return
     */
    public Map<ObjectId, VideoEntry> getVideoEntryMap(Collection<ObjectId> col, DBObject fields) {
        return videoDao.getVideoEntryMap(col, fields);
    }

    public void updateVideoUpdateStatus(ObjectId videoId, int uploadStatus) {
        try {
            videoDao.update(videoId, "us", uploadStatus);
        } catch (IllegalParamException il) {

        }
    }

    public void updatePersistentId(ObjectId videoId, String persistentId) {
        try {
            videoDao.update(videoId, "pid", persistentId);
        } catch (IllegalParamException il) {

        }
    }
}
