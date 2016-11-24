package com.fulaan_old.letter.service;


import com.mongodb.DBObject;
import com.pojo.letter.*;
import com.pojo.letter.LetterRecordEntry.LetterRecordState;
import com.sys.exceptions.IllegalParamException;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.db.letter.LetterDao;
import com.db.letter.LetterRecordDao;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 私信service
 *
 * @author fourer
 */

@Service
public class LetterService {

    private static final Logger logger = Logger.getLogger(LetterService.class);

    private LetterDao letterDao = new LetterDao();
    private LetterRecordDao letterRecordDao = new LetterRecordDao();


    private static Object lock = new Object();

    /**
     * 得到未读信件数量
     *
     * @param userId
     * @return
     */
    public int getUnReadLetterCount(ObjectId userId) {
        //return letterDao.countLetters(null, Constant.NEGATIVE_ONE, Constant.NEGATIVE_ONE, userId, LetterState.LETTER_SEDND_SUCCESS.getState());
        return letterDao.getUnReadLetterCount(userId);
    }

    /**
     * 得到map
     *
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, LetterEntry> getLetterEntryMap(Collection<ObjectId> ids, DBObject fields) {
        return letterDao.getLetterEntryMap(ids, fields);
    }


    /**
     * 得到指定信件
     *
     * @param letterId
     * @return
     */
    public LetterEntry getLetterEntry(ObjectId letterId) {
        return letterDao.getLetterEntry(letterId);
    }

    public void sendLetter(LetterEntry le) {
        ObjectId letterId = letterDao.addLetterEntry(le);
        List<ObjectId> receiveIds = MongoUtils.getFieldObjectIDs(le.getReceiveList(), "ri");
        handleLetterRecordForSend(le.getSenderId(), receiveIds, letterId);
    }

    public void readLetter(ObjectId userId, LetterEntry letterEntry) throws Exception {
        List<ReceiveInfo> receiveInfoList = letterEntry.getReceiveList();
        for (ReceiveInfo receiveInfo : receiveInfoList) {
            if (receiveInfo.getReceiverId().equals(userId)
                    && receiveInfo.getState() == LetterState.LETTER_SEDND_SUCCESS.getState()) {

                letterDao.receiverRead(letterEntry.getID(), userId);
                handleLetterRecordForRead(letterEntry.getID(), userId);
            }
        }
    }


    /**
     * 返回某个用户的所用最近信件列表（即跟其他用户的最近信件）
     *
     * @param userId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<LetterRecordEntry> getLetterRecordList(String userId, int page, int size) throws Exception {
        if (!ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        List<LetterRecordEntry> letterRecordEntryList = letterRecordDao.getList(new ObjectId(userId), page * size, size);


        return letterRecordEntryList;
    }

    /**
     * 返回某个用户最新信件列表的数量 （不超100000）
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public int countLatestLetterList(String userId) throws Exception {
        if (!ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }

        return letterRecordDao.countList(new ObjectId(userId));
    }


    /**
     * 得到两个用户的所有信件的列表
     *
     * @param userId
     * @param peerUserId
     * @return
     */
    public List<LetterEntry> getLetterListByPeerUserId(String userId, String peerUserId, int page, int size)
            throws Exception {
        if (!ObjectId.isValid(peerUserId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }

        return letterDao.getLetterEntryListByPeerUserId(new ObjectId(userId),
                new ObjectId(peerUserId), page * size, size);
    }

    /**
     * 得到两个用户的所有信件的列表的个数
     *
     * @param userId
     * @param peerUserId
     * @return
     */
    public int countLetterListByPeerUserId(String userId, String peerUserId)
            throws Exception {
        if (!ObjectId.isValid(peerUserId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }

        return letterDao.countLetterEntryListByPeerUserId(new ObjectId(userId),
                new ObjectId(peerUserId));
    }

    /**
     * 接收者删除信件
     *
     * @param userId
     * @param letterId
     * @throws Exception
     */
    public void deleteLetter(String userId, String pairId, String letterId) throws Exception {
        if (!ObjectId.isValid(userId)) {
            throw new IllegalParamException("the user id [" + userId + " ] is valid!!");
        }
        if (!ObjectId.isValid(letterId)) {
            throw new IllegalParamException("the letter id [" + letterId + " ] is valid!!");
        }
        //根据发送者或接收者标记该邮件为删除

        LetterEntry letterEntry = letterDao.getLetterEntry(new ObjectId(letterId));

        //如果是sender
        if (letterEntry.getSenderId().toString().equals(userId)) {
            letterDao.senderDelete(new ObjectId(userId),
                    new ObjectId(letterId));
        } else {
            letterDao.receiverDelete(new ObjectId(userId),
                    new ObjectId(letterId));
        }
        handleLetterRecordForRemove(new ObjectId(letterId), new ObjectId(userId));


    }

    public void deleteAllReply(String userId, String pairId) throws Exception {
        if (!ObjectId.isValid(pairId)) {
            throw new IllegalParamException("the user id [" + pairId + " ] is valid!!");
        }

        //根据发送者或接收者标记该邮件为删除

        List<LetterEntry> letterEntryList = letterDao.getLetterEntryListByPeerUserId(new ObjectId(userId),
                new ObjectId(pairId), 0, 999999);

        for (LetterEntry letterEntry : letterEntryList) {
            //如果是sender
            if (letterEntry.getSenderId().toString().equals(userId)) {
                letterDao.senderDelete(new ObjectId(userId),
                        letterEntry.getID());
            } else {
                letterDao.receiverDelete(new ObjectId(userId),
                        letterEntry.getID());
            }
        }


        ObjectId theUserId = new ObjectId(userId);

        LetterRecordEntry letterRecordEntry = letterRecordDao.getLetterRecordEntry(theUserId,
                new ObjectId(pairId));


        if (letterRecordEntry != null) {
            if (letterRecordEntry.getUserId().equals(theUserId)) {

                letterRecordEntry.setUserState(new LetterRecordState(null, 0));
                letterRecordEntry.setUserId(null);
            } else if (letterRecordEntry.getLetterUserId().equals(theUserId)) {
                letterRecordEntry.setLetterUserState(new LetterRecordState(null, 0));
                letterRecordEntry.setLetterUserId(null);
            }
            letterRecordDao.removeLetterRecordEntry(letterRecordEntry.getID());
            letterRecordDao.addLetterRecordEntry(letterRecordEntry);

        }

    }


    /**
     * 看信更新信件记录
     *
     * @param letterId
     * @param myId
     * @throws IllegalParamException
     */
    public void handleLetterRecordForRead(final ObjectId letterId, final ObjectId myId) throws IllegalParamException {
        final LetterEntry le = letterDao.getLetterEntry(letterId);
        if (null == le)
            throw new IllegalParamException();

        if (!le.getSenderId().equals(myId)) //我是收信人
        {
            LetterRecordEntry letterRecordEntry = letterRecordDao.getLetterRecordEntry(le.getSenderId(), myId);
            String field = "us";
            if (myId.equals(letterRecordEntry.getLetterUserId()))
                field = "lus";
            letterRecordDao.readLetter(letterRecordEntry.getID(), field);
        }

    }


    /**
     * 删除一个信件更新信件记录
     *
     * @param letterId
     * @param myId
     * @throws IllegalParamException
     */
    public void handleLetterRecordForRemove(final ObjectId letterId, final ObjectId myId) throws IllegalParamException {
        final LetterEntry le = letterDao.getLetterEntry(letterId);
        if (null == le)
            throw new IllegalParamException();

        Runnable handler = new Runnable() {
            @Override
            public void run() {

                ObjectId sender = le.getSenderId();
                if (myId.equals(sender)) //我是发信者
                {
                    List<ObjectId> receiveIds = MongoUtils.getFieldObjectIDs(le.getReceiveList(), "ri");
                    for (ObjectId reveiveId : receiveIds) {
                        LetterRecordEntry letterRecordEntry = letterRecordDao.getLetterRecordEntry(myId, reveiveId);
                        if (null == letterRecordEntry) {
                            logger.error("can not find letterRecord; the user=" + myId + " , " + reveiveId);
                            continue;
                        }
                        handleRemove(letterRecordEntry, myId, reveiveId, letterId);
                    }
                } else //我是收信者
                {
                    LetterRecordEntry letterRecordEntry = letterRecordDao.getLetterRecordEntry(sender, myId);
                    handleRemove(letterRecordEntry, myId, sender, letterId);
                }
            }
        };
        Thread t = new Thread(handler);
        t.start();
    }


    /**
     * 更新信件记录
     *
     * @param recerdId       信件记录ID
     * @param myId           我的ID
     * @param letterFriendId
     * @param letterId
     */
    public void handleRemove(LetterRecordEntry record, ObjectId myId, ObjectId letterFriendId, ObjectId letterId) {
        String field = "us";
        if (myId.equals(record.getLetterUserId()))
            field = "lus";
        LetterRecordState state = record.getState(field);

        boolean isUpdate = state.getId().equals(letterId);
        ObjectId recentId = null;
        if (isUpdate) //需要更新最新信件
        {
            List<LetterEntry> ls = letterDao.getLetterEntryListByPeerUserId(myId, letterFriendId, Constant.ZERO, Constant.TWO);
            if (ls.size() == Constant.TWO) {
                recentId = ls.get(Constant.ONE).getID();
            }
        }
        letterRecordDao.removeLetter(record.getID(), letterId, recentId, field, isUpdate);
    }


    /**
     * 更新信件记录,发信 时调用；
     *
     * @param ui
     * @param friends
     * @param letterId
     */
    public void handleLetterRecordForSend(final ObjectId myId, final Collection<ObjectId> friends, final ObjectId letterId) {
        Runnable handler = new Runnable() {
            @Override
            public void run() {
                if (null != friends && friends.size() > 0) {
                    for (ObjectId fi : friends) {
                        handleLetterRecord(myId, fi, letterId);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        };
        Thread t = new Thread(handler);
        t.start();


    }


    public void handleLetterRecord(ObjectId myId, ObjectId letterFriendui, ObjectId letterId) {
        synchronized (lock) {
            try {
                LetterRecordEntry e = letterRecordDao.getLetterRecordEntry(myId, letterFriendui);
                if (null == e) {
                    e = new LetterRecordEntry(myId, letterFriendui, letterId);
                    letterRecordDao.addLetterRecordEntry(e);
                } else {
                    String field = "lus";
                    if (e.getLetterUserId().equals(myId)) {
                        field = "us";
                    }
                    letterRecordDao.update(e.getID(), letterId, field);
                }
            } catch (Exception ex) {

            }
        }
    }

}
