package com.fulaan_old.forum.service;

import com.db.forum.FScoreDao;
import com.db.user.UserDao;
import com.pojo.forum.FScoreDTO;
import com.pojo.forum.FScoreEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/8/16.
 * 积分服务，所有关于积分的都在这里调用
 */
@Service
public class FScoreService {

    private FScoreDao fScoreDao = new FScoreDao();
    private UserDao userDao = new UserDao();

    public ObjectId addFScore(ObjectId uid, int action, int offerScore, int reword) {
        FScoreDTO fScoreDTO = new FScoreDTO();
        fScoreDTO.setTime(System.currentTimeMillis());
        fScoreDTO.setPersonId(uid.toString());
        if (action == 1) { //发帖奖励
            fScoreDTO.setType(1);
            fScoreDTO.setOperation("发帖");
            fScoreDTO.setScoreOrigin("发帖获得积分");
            fScoreDTO.setScore(3);
            userDao.updateForumScoreValue(uid, 3);
        } else if (action == 2) { //发帖多余三个
            fScoreDTO.setType(1);
            fScoreDTO.setOperation("发帖");
            fScoreDTO.setScoreOrigin("每天发帖多发3个以上获得5个积分");
            fScoreDTO.setScore(5);
            userDao.updateForumScoreValue(uid, 5);
        } else if (action == 3) { //悬赏帖子
            fScoreDTO.setType(1);
            fScoreDTO.setOperation("悬赏帖子");
            fScoreDTO.setScoreOrigin("悬赏回帖人的积分奖励");
            fScoreDTO.setScore(offerScore);
            userDao.updateForumScoreValue(uid, offerScore);
        } else if (action == 4) { //奖赏帖子
            fScoreDTO.setType(1);
            fScoreDTO.setOperation("奖赏帖子");
            fScoreDTO.setScoreOrigin("奖励回帖人的积分奖励");
            fScoreDTO.setScore(reword);
            userDao.updateForumScoreValue(uid, reword);
        }
        return fScoreDao.addFScore(fScoreDTO.exportEntry());
    }

    public ObjectId addFScore(ObjectId uid, int action, int offerScore) {
        return addFScore(uid, action, offerScore, 0);
    }

    public ObjectId addFScore(ObjectId uid, int action) {
        return addFScore(uid, action, 0, 0);
    }

    public ObjectId addFScore(FScoreDTO fScoreDTO) {
        return fScoreDao.addFScore(fScoreDTO.exportEntry());
    }

    /**
     * 额外奖赏(就是别人打赏)
     *
     * @param uid
     * @param title
     * @param reword
     * @return
     */
    public ObjectId extraRewordaddScore(ObjectId uid, String title, int reword) {
        userDao.updateForumScoreValue(uid, reword);
        FScoreDTO fScoreDTO = new FScoreDTO();
        fScoreDTO.setTime(System.currentTimeMillis());
        fScoreDTO.setType(1);
        fScoreDTO.setOperation("额外奖赏回帖");
        fScoreDTO.setPersonId(uid.toString());
        fScoreDTO.setScoreOrigin("回复" + title + "额外奖赏积分");
        fScoreDTO.setScore(reword);
        return fScoreDao.addFScore(fScoreDTO.exportEntry());
    }

    /**
     * 回复活动贴
     *
     * @param uid
     * @param title
     * @return
     */
    public ObjectId activityAddScore(ObjectId uid, String title) {
        userDao.updateForumScoreValue(uid, 1);
        FScoreDTO fScoreDTO = new FScoreDTO();
        fScoreDTO.setTime(System.currentTimeMillis());
        fScoreDTO.setType(1);
        fScoreDTO.setOperation("回复活动帖");
        fScoreDTO.setPersonId(uid.toString());
        fScoreDTO.setScoreOrigin("回复" + title + "活动帖奖励积分");
        fScoreDTO.setScore(2);
        return fScoreDao.addFScore(fScoreDTO.exportEntry());
    }

    /**
     * 回复普通贴
     *
     * @param uid
     * @param title
     * @return
     */
    public ObjectId replyForm(ObjectId uid, String title) {
        userDao.updateForumScoreValue(uid, 1);
        FScoreDTO fScoreDTO = new FScoreDTO();
        fScoreDTO.setTime(System.currentTimeMillis());
        fScoreDTO.setType(1);
        fScoreDTO.setOperation("回复普通贴");
        fScoreDTO.setPersonId(uid.toString());
        fScoreDTO.setScoreOrigin("回复" + title + "普通贴奖励积分");
        fScoreDTO.setScore(1);
        return fScoreDao.addFScore(fScoreDTO.exportEntry());
    }

    /**
     * 回帖100时加积分
     *
     * @param uid
     * @return
     */
    public ObjectId hundredFloorRewordAddScore(ObjectId uid) {
        userDao.updateForumScoreValue(uid, 10);
        FScoreDTO fScoreDTO = new FScoreDTO();
        fScoreDTO.setTime(System.currentTimeMillis());
        fScoreDTO.setType(1);
        fScoreDTO.setOperation("楼主获得积分");
        fScoreDTO.setPersonId(uid.toString());
        fScoreDTO.setScoreOrigin("回帖数超过一百时，楼主加积分");
        fScoreDTO.setScore(10);
        return fScoreDao.addFScore(fScoreDTO.exportEntry());
    }

    /**
     * 回复楼中楼奖励积分
     *
     * @param uid
     * @param replyContent
     * @return
     */
    public ObjectId floorInFloorRewordScore(ObjectId uid, String replyContent) {
        userDao.updateForumScoreValue(uid, 1);
        FScoreDTO fScoreDTO = new FScoreDTO();
        fScoreDTO.setTime(System.currentTimeMillis());
        fScoreDTO.setType(1);
        fScoreDTO.setOperation("回复楼中楼");
        fScoreDTO.setPersonId(uid.toString());
        if (replyContent.length() > 10) {
            fScoreDTO.setScoreOrigin("回复" + replyContent.substring(0, 10) + "楼中楼奖励积分");
        } else {
            fScoreDTO.setScoreOrigin("回复" + replyContent + "楼中楼奖励积分");
        }
        fScoreDTO.setScore(1);
        return fScoreDao.addFScore(fScoreDTO.exportEntry());
    }

    /**
     * 积分列表
     *
     * @param uid
     * @return
     */
    public List<FScoreDTO> getFScoreByPersonId(String uid) {
        List<FScoreDTO> fScoreDTOList = new ArrayList<FScoreDTO>();
        ObjectId personId = new ObjectId(uid);
        List<FScoreEntry> fScoreEntries = fScoreDao.getFScore(personId);
        if (null != fScoreEntries) {
            for (FScoreEntry fScoreEntry : fScoreEntries) {
                FScoreDTO fScoreDTO = new FScoreDTO(fScoreEntry);
                long time = fScoreEntry.getTime();
                fScoreDTO.setTimeText(DateTimeUtils.convert(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
                fScoreDTOList.add(fScoreDTO);
            }
        }
        return fScoreDTOList;
    }
}
