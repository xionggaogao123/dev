package com.db.temp;

import com.db.reminder.ReminderDao;
import com.db.school.HomeWorkDao;
import com.pojo.reminder.ReminderEntry;
import com.pojo.school.HomeWorkEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/11/24.
 */
public class DeleteReminder {
    private ReminderDao reminderDao = new ReminderDao();
    private HomeWorkDao homeWorkDao = new HomeWorkDao();

    private void deleteReminder() {
//        List<ReminderEntry> reminderEntryList = reminderDao.findAllReminder();
        List<ReminderEntry> reminderEntryList = new ArrayList<ReminderEntry>();
        for (ReminderEntry reminderEntry : reminderEntryList) {
            ObjectId homeworkId = reminderEntry.getHomeworkId();
            HomeWorkEntry homeWorkEntry = homeWorkDao.findHomeWorkEntry(homeworkId);
            if (homeWorkEntry == null) {
                System.out.println("删除homeworkId=" + homeworkId.toString() + "  的提醒");
                reminderDao.removeReminder(reminderEntry.getID());
            }
        }
        System.out.println(reminderEntryList.size());
    }

    public static void main(String[] args) {
        new DeleteReminder().deleteReminder();
    }
}
