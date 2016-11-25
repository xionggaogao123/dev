package com.fulaan.forum.service;

import com.db.forum.FRecordDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.ebusiness.SortType;
import com.pojo.forum.FRecordDTO;
import com.pojo.forum.FRecordEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/21.
 */
@Service
public class FRecordService {

    private FRecordDao fRecordDao = new FRecordDao();
    private UserDao userDao = new UserDao();

    /**
     * 记录列表
     *
     * @return
     */
    public List<FRecordDTO> getFRepliesList(String person, int orderType) {
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        List<FRecordEntry> fRecordEntries = fRecordDao.getFRecordEntries(Constant.FIELDS, sort, personId);
        List<FRecordDTO> fRecordDTOList = new ArrayList<FRecordDTO>();
        if (null != fRecordDTOList) {
            for (FRecordEntry fRecordEntry : fRecordEntries) {
                FRecordDTO fRecordDTO = new FRecordDTO(fRecordEntry);
                String userId = fRecordDTO.getPersonId();
                long time = fRecordDTO.getTime();
                String timeText = DateTimeUtils.convert(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
                fRecordDTO.setTimeText(timeText);
                String postTitle = fRecordDTO.getPostTitle();
                String nickName = "";
                String content = "";
                UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);
                if (null != userEntry) {
                    fRecordDTO.setImageSrc(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                    if (null != userEntry.getNickName() && !"".equals(userEntry.getNickName())) {
                        nickName = userEntry.getNickName();
                    } else {
                        nickName = userEntry.getUserName();
                    }
                }
                //1删除2回复
                if (fRecordDTO.getLogRecord() == 1) {
                    content = "您的主题  " + postTitle + "  被  " + nickName + "  删除";
                    fRecordDTO.setContent(content);
                } else if (fRecordDTO.getLogRecord() == 2) {
                    content = nickName + " " + "回复了您的帖子  " + postTitle + "  ";
                    fRecordDTO.setContent(content);
                }
                fRecordDTOList.add(fRecordDTO);
            }
        }
        return fRecordDTOList;
    }


    /**
     * @param fRecordDTO
     * @return
     */
    public ObjectId addFRecordEntry(FRecordDTO fRecordDTO) {
        return fRecordDao.addFRecord(fRecordDTO.exportEntry());
    }

    /**
     * 查询记录数量
     *
     * @return
     */
    public int getFRecordCount(String person) {
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        return fRecordDao.getFRecordEntriesCount(personId);

    }

    /**
     * 更新删除浏览记录
     */
    public void updateDelete() {
        fRecordDao.updateDelete();
    }

    /**
     * 更新回复浏览记录
     */
    public void updateReply(ObjectId id) {
        fRecordDao.updateReply(id);
    }

    /**
     * 帖子详情
     *
     * @param Id
     * @return
     */
    public FRecordDTO detail(ObjectId Id) {
        FRecordEntry fRecordEntry = fRecordDao.getFRecordEntry(Id);
        FRecordDTO fRecordDTO = new FRecordDTO(fRecordEntry);
        return fRecordDTO;
    }
}
