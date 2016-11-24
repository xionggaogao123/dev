package com.fulaan_old.forum.service;


import com.db.forum.FInvitationDao;
import com.pojo.forum.FInvitationEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/13.
 */
@Service
public class FInvitationService {

    FInvitationDao fInvitationDao = new FInvitationDao();

    /**
     * @param id
     * @return
     */
    public FInvitationEntry getFInvitation(ObjectId id) {
        return fInvitationDao.getFInvitation(id);
    }

    /**
     * @param id
     */
    public void updateCount(ObjectId id, ObjectId invited) {
        fInvitationDao.updateCount(id, invited);
    }

    public ObjectId saveOrUpdate(ObjectId userId, ObjectId invited, long count) {
        FInvitationEntry fInvitationEntry = new FInvitationEntry();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        objectIdList.add(invited);
        fInvitationEntry.setUserId(userId);
        fInvitationEntry.setCount(count);
        fInvitationEntry.setUserReplyList(objectIdList);
        return fInvitationDao.saveOrUpdate(fInvitationEntry);
    }
}
