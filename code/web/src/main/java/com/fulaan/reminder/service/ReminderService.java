package com.fulaan.reminder.service;

import com.db.reminder.ReminderDao;
import com.pojo.reminder.ReminderEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fl on 2015/9/25.
 */
@Service
public class ReminderService {
    private ReminderDao reminderDao = new ReminderDao();

    public void addReminder(ReminderEntry reminderEntry){
        ReminderEntry entry = reminderDao.findReminderEntry(reminderEntry.getSenderId(),reminderEntry.getHomeworkId());
        if(entry != null) {
            reminderDao.update(entry.getID(), reminderEntry);
        } else {
            reminderDao.addAReminder(reminderEntry);
        }

    }

    public int findHWReminderCount(ObjectId receiverId, ObjectId classId, ObjectId subjectId, ObjectId homeworkId){
        return reminderDao.findHWReminderCount(receiverId, classId, subjectId, homeworkId);
    }

    public void deleteReceiver(ObjectId receiverId, ObjectId classId, ObjectId subjectId, ObjectId homeworkId){
        List<ReminderEntry> reminderEntries = reminderDao.findReminderEntryList(receiverId, classId, subjectId, homeworkId);
        if(reminderEntries!=null && reminderEntries.size()>0){
            for(ReminderEntry reminderEntry : reminderEntries){
                List<ObjectId> receiverList = reminderEntry.getReceiverList();
                if(receiverList!=null && receiverList.size()>0){
                    receiverList.remove(receiverId);
                    if(receiverList.size()>0){//还有接收者
                        reminderDao.updateReceiverList(reminderEntry.getID(), receiverList);
                    } else {
                        reminderDao.removeReminder(reminderEntry.getID());
                    }
                }
            }
        }

    }

    public void removeReminder(ObjectId classId, ObjectId subjectId, ObjectId homeworkId){
        reminderDao.removeReminder(classId, subjectId, homeworkId);
    }
}
