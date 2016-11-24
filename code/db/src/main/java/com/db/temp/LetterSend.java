package com.db.temp;

import com.db.letter.LetterDao;
import com.db.letter.LetterRecordDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.letter.LetterEntry;
import com.pojo.letter.LetterRecordEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/9/1.
 */
public class LetterSend {
    private static Object lock =new Object();
    public static void main(String[] args) throws IOException {
        UserDao userDao=new UserDao();
        LetterDao letterDao =new LetterDao();


        List<UserEntry> list= userDao.getUserList("小助手",new BasicDBObject(Constant.ID,1).append("nm",1).append("si",1));;
        for (UserEntry userEntry : list) {
//            if (!userEntry.getUserName().equals("k6kt小助手1") && !userEntry.getUserName().equals("k6kt小助手2")) {
                List<UserEntry> userEntryList = userDao.getStudentEntryBySchoolId(userEntry.getSchoolID(), new BasicDBObject(Constant.ID, 1).append("chatid", 1));
                List<ObjectId> receiverIds = new ArrayList<ObjectId>();
                ArrayList<String> recipientIds = new ArrayList<String>();
                for (UserEntry user : userEntryList) {
                    receiverIds.add(user.getID());
                    recipientIds.add(user.getChatId());
                }
                LetterEntry letterEntry = new LetterEntry(userEntry.getID(),
                        "将将将！“复兰杯”秋季才艺挑战赛开始啦！再也不用羡慕那些拿奖拿到手软的同学们啦，因为，我们自己就可以拿奖拿到手软哟！本次大赛有：英语演讲、硬笔书法、跆拳道、国画、少儿模特、摄影参赛方式：①登录K6KT快乐课堂【首页】跳转进入“复兰教育社区”【大赛】；②登录www.fulaan.com，进入【大赛】；③下载“复兰教育社区”APP版", receiverIds);
                ObjectId letterId=letterDao.addLetterEntry(letterEntry);
                List<ObjectId> receiveIds= MongoUtils.getFieldObjectIDs(letterEntry.getReceiveList(), "ri");
                handleLetterRecordForSend(letterEntry.getSenderId(),receiveIds,letterId);
            }
//        }
    }

    public static void handleLetterRecordForSend(final ObjectId myId,final Collection<ObjectId> friends,final ObjectId letterId)
    {
        Runnable handler=new Runnable() {
            @Override
            public void run() {
                if(null!=friends && friends.size()>0)
                {
                    for(ObjectId fi:friends)
                    {
                        handleLetterRecord(myId,fi,letterId);
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

    public static void handleLetterRecord(ObjectId myId,ObjectId letterFriendui,ObjectId letterId)
    {
        LetterRecordDao letterRecordDao =new LetterRecordDao();
        synchronized (lock) {
            try
            {
                LetterRecordEntry e=letterRecordDao.getLetterRecordEntry(myId, letterFriendui);
                if(null==e)
                {
                    e=new LetterRecordEntry(myId, letterFriendui,letterId);
                    letterRecordDao.addLetterRecordEntry(e);
                }
                else
                {
                    String field ="lus";
                    if(e.getLetterUserId().equals(myId))
                    {
                        field ="us";
                    }
                    letterRecordDao.update(e.getID(), letterId, field);
                }
            }catch(Exception ex)
            {

            }
        }
    }
}
