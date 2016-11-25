package com.fulaan.forum.service;

import com.db.forum.FMissionDao;
import com.db.forum.FScoreDao;
import com.db.user.UserDao;
import com.pojo.forum.FMissionEntry;
import com.pojo.forum.FScoreDTO;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/7/5.
 */
@Service
public class FMissionService {

    private FMissionDao fMissionDao = new FMissionDao();

    private UserDao userDao = new UserDao();

    private FScoreDao fScoreDao = new FScoreDao();

    /**
     * 查询今天的任务情况
     *
     * @param person
     * @return
     */
    public Map<String, Object> findTodayMissionByUserId(String person) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        String date = DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD);
        FMissionEntry fMissionEntry = fMissionDao.findTodayMissionByUserId(personId, date);
        if (null != fMissionEntry) {
            if (fMissionEntry.getPost() == 1) {
                map.put("post", true);
            } else {
                map.put("post", false);
            }
            if (fMissionEntry.getWelfare() == 1) {
                map.put("welfare", true);
            } else {
                map.put("welfare", false);
            }
            if (fMissionEntry.getSignIn() == 1) {
                map.put("signIn", true);
            } else {
                map.put("signIn", false);
            }
            map.put("count", fMissionEntry.getCount());
            return map;
        } else {
            map.put("count", 0);
            map.put("post", false);
            map.put("welfare", false);
            map.put("signIn", false);
            return map;
        }
    }

    /**
     * 判斷是否簽到了，为了避免出现两次签到获得经验值/积分的情况
     *
     * @param person
     * @return
     */
    public boolean isSign(String person) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Date dNow = new Date();
        String nowDate = sdf.format(dNow);
        FMissionEntry fMissionEntry = fMissionDao.findTodayMissionByUserId(new ObjectId(person), nowDate);
        if (null == fMissionEntry) {
            return false;
        } else {
            if (fMissionEntry.getSignIn() == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 判斷是否抽奖了，为了避免出现连续多次抽奖获得经验值/积分的情况
     *
     * @param person
     * @return
     */
    public boolean isWelfare(String person) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Date dNow = new Date();
        String nowDate = sdf.format(dNow);
        FMissionEntry fMissionEntry = fMissionDao.findTodayMissionByUserId(new ObjectId(person), nowDate);
        if (null == fMissionEntry) {
            return false;
        } else {
            if (fMissionEntry.getWelfare() == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 该用户前两天是否抽过奖
     *
     * @param person
     * @return
     */
    public boolean getThree(String person) {
        ObjectId personId = person.equals("") ? null : new ObjectId(person);

        //标志位
        boolean flag = false;
        //第一步，查询昨天抽奖过没
        String bef = getDate(-1);
        FMissionEntry fMissionEntry = fMissionDao.findTodayMissionByUserId(personId, bef);
        if (null != fMissionEntry) {
            if (fMissionEntry.getWelfare() == 1) {
                flag = true;
                return flag;
            }
        }

        //第二步，查询前天抽奖过没
        String bef1 = getDate(-2);
        FMissionEntry fMissionEntry1 = fMissionDao.findTodayMissionByUserId(personId, bef1);
        if (null != fMissionEntry1) {
            if (fMissionEntry1.getWelfare() == 1) {
                flag = true;
                return flag;
            }
        }

        return flag;

    }

    /**
     * 获取这三天
     *
     * @param arg0
     * @return
     */
    public String getDate(int arg0) {
        Date dNow = new Date();
        Date dBefore = new Date();

        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, arg0);  //设置为前一天
        dBefore = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String retStr = sdf.format(dBefore);
        return retStr;
    }

    /**
     * 签到
     *
     * @param person
     */
    public void sign(String person) {
        ObjectId personId = person.equals("") ? null : new ObjectId(person);

        Date dNow = new Date();
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        dBefore = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);
        String nowDate = sdf.format(dNow);

        FMissionEntry update = new FMissionEntry();
        FMissionEntry fMissionEntry = fMissionDao.findTodayMissionByUserId(personId, defaultStartDate);
        if (null != fMissionEntry) {
            if (fMissionEntry.getPost() == 1) {
                int sCount = fMissionEntry.getCount() + 1;
                update.setCount(fMissionEntry.getCount() + 1);
                if (sCount == 2) {
                    userDao.updateForumExperience(new ObjectId(person), 2);
                    userDao.updateForumScoreValue(new ObjectId(person), 2);
                    FScoreDTO fScoreDTO = new FScoreDTO();
                    fScoreDTO.setTime(System.currentTimeMillis());
                    fScoreDTO.setType(2);
                    fScoreDTO.setOperation("签到");
                    fScoreDTO.setPersonId(person);
                    fScoreDTO.setScoreOrigin("连续签到两天");
                    fScoreDTO.setScore(2);
                    fScoreDao.addFScore(fScoreDTO.exportEntry());
                } else if (sCount == 3) {
                    userDao.updateForumExperience(new ObjectId(person), 4);
                    userDao.updateForumScoreValue(new ObjectId(person), 4);
                    FScoreDTO fScoreDTO = new FScoreDTO();
                    fScoreDTO.setTime(System.currentTimeMillis());
                    fScoreDTO.setType(2);
                    fScoreDTO.setOperation("签到");
                    fScoreDTO.setPersonId(person);
                    fScoreDTO.setScoreOrigin("连续签到三天");
                    fScoreDTO.setScore(4);
                    fScoreDao.addFScore(fScoreDTO.exportEntry());
                } else if (sCount == 4) {
                    userDao.updateForumExperience(new ObjectId(person), 8);
                    userDao.updateForumScoreValue(new ObjectId(person), 8);
                    FScoreDTO fScoreDTO = new FScoreDTO();
                    fScoreDTO.setTime(System.currentTimeMillis());
                    fScoreDTO.setType(2);
                    fScoreDTO.setOperation("签到");
                    fScoreDTO.setPersonId(person);
                    fScoreDTO.setScoreOrigin("连续签到四天");
                    fScoreDTO.setScore(8);
                    fScoreDao.addFScore(fScoreDTO.exportEntry());
                } else if (sCount == 5) {
                    userDao.updateForumExperience(new ObjectId(person), 12);
                    userDao.updateForumScoreValue(new ObjectId(person), 12);
                    FScoreDTO fScoreDTO = new FScoreDTO();
                    fScoreDTO.setTime(System.currentTimeMillis());
                    fScoreDTO.setType(2);
                    fScoreDTO.setOperation("签到");
                    fScoreDTO.setPersonId(person);
                    fScoreDTO.setScoreOrigin("连续签到五天");
                    fScoreDTO.setScore(12);
                    fScoreDao.addFScore(fScoreDTO.exportEntry());
                } else if (6 <= sCount && sCount <= 15) {
                    userDao.updateForumExperience(new ObjectId(person), 18);
                    userDao.updateForumScoreValue(new ObjectId(person), 18);
                    FScoreDTO fScoreDTO = new FScoreDTO();
                    fScoreDTO.setTime(System.currentTimeMillis());
                    fScoreDTO.setType(2);
                    fScoreDTO.setOperation("签到");
                    fScoreDTO.setPersonId(person);
                    fScoreDTO.setScoreOrigin("连续签到六天到十五天");
                    fScoreDTO.setScore(18);
                    fScoreDao.addFScore(fScoreDTO.exportEntry());
                } else if (16 <= sCount && sCount <= 30) {
                    userDao.updateForumExperience(new ObjectId(person), 25);
                    userDao.updateForumScoreValue(new ObjectId(person), 25);
                    FScoreDTO fScoreDTO = new FScoreDTO();
                    fScoreDTO.setTime(System.currentTimeMillis());
                    fScoreDTO.setType(2);
                    fScoreDTO.setOperation("签到");
                    fScoreDTO.setPersonId(person);
                    fScoreDTO.setScoreOrigin("连续签到十六天到一个月");
                    fScoreDTO.setScore(25);
                    fScoreDao.addFScore(fScoreDTO.exportEntry());
                } else if (sCount > 30) {
                    userDao.updateForumExperience(new ObjectId(person), 50);
                    userDao.updateForumScoreValue(new ObjectId(person), 50);
                    FScoreDTO fScoreDTO = new FScoreDTO();
                    fScoreDTO.setTime(System.currentTimeMillis());
                    fScoreDTO.setType(2);
                    fScoreDTO.setOperation("签到");
                    fScoreDTO.setPersonId(person);
                    fScoreDTO.setScoreOrigin("连续签到一个月以上");
                    fScoreDTO.setScore(50);
                    fScoreDao.addFScore(fScoreDTO.exportEntry());
                }
            } else {
                update.setCount(1);
                userDao.updateForumExperience(new ObjectId(person), 1);
                userDao.updateForumScoreValue(new ObjectId(person), 1);
                FScoreDTO fScoreDTO = new FScoreDTO();
                fScoreDTO.setTime(System.currentTimeMillis());
                fScoreDTO.setType(2);
                fScoreDTO.setOperation("签到");
                fScoreDTO.setPersonId(person);
                fScoreDTO.setScoreOrigin("签到第一天");
                fScoreDTO.setScore(1);
                fScoreDao.addFScore(fScoreDTO.exportEntry());
            }
        } else {
            update.setCount(1);
            userDao.updateForumExperience(new ObjectId(person), 1);
            userDao.updateForumScoreValue(new ObjectId(person), 1);
            FScoreDTO fScoreDTO = new FScoreDTO();
            fScoreDTO.setTime(System.currentTimeMillis());
            fScoreDTO.setType(2);
            fScoreDTO.setOperation("签到");
            fScoreDTO.setPersonId(person);
            fScoreDTO.setScoreOrigin("签到第一天");
            fScoreDTO.setScore(1);
            fScoreDao.addFScore(fScoreDTO.exportEntry());
        }
        FMissionEntry fMissionEntry1 = fMissionDao.findTodayMissionByUserId(personId, nowDate);
        if (fMissionEntry1 != null) {//更新
            update.setID(fMissionEntry1.getID());
            update.setPost(fMissionEntry1.getPost());
            update.setWelfare(fMissionEntry1.getWelfare());
            update.setSignIn(1);
            update.setPersonId(fMissionEntry1.getPersonId());
            update.setTime(fMissionEntry1.getTime());
        } else {
            update.setPost(0);
            update.setSignIn(1);
            update.setWelfare(0);
            update.setPersonId(personId);
            update.setTime(nowDate);
        }

        fMissionDao.saveOrUpdate(update);
    }

    /**
     * 福利
     *
     * @param person
     */
    public void welfare(String person) {
        ObjectId personId = person.equals("") ? null : new ObjectId(person);

        Date dNow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String nowDate = sdf.format(dNow);

        FMissionEntry update = new FMissionEntry();
        update.setCount(1);
        FMissionEntry fMissionEntry1 = fMissionDao.findTodayMissionByUserId(personId, nowDate);
        if (fMissionEntry1 != null) {//更新
            update.setID(fMissionEntry1.getID());
            update.setPost(fMissionEntry1.getPost());
            update.setWelfare(1);
            update.setSignIn(fMissionEntry1.getSignIn());
            update.setPersonId(fMissionEntry1.getPersonId());
            update.setTime(fMissionEntry1.getTime());
        } else {
            update.setPost(0);
            update.setSignIn(0);
            update.setWelfare(1);
            update.setPersonId(personId);
            update.setTime(nowDate);
        }

        fMissionDao.saveOrUpdate(update);
    }

    /**
     * 发帖/回帖任务
     *
     * @param person
     */
    public void post(String person) {
        ObjectId personId = person.equals("") ? null : new ObjectId(person);

        Date dNow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String nowDate = sdf.format(dNow);

        FMissionEntry update = new FMissionEntry();
        update.setCount(1);
        FMissionEntry fMissionEntry1 = fMissionDao.findTodayMissionByUserId(personId, nowDate);
        if (fMissionEntry1 != null) {//更新
            update.setID(fMissionEntry1.getID());
            update.setPost(1);
            update.setWelfare(fMissionEntry1.getWelfare());
            update.setSignIn(fMissionEntry1.getSignIn());
            update.setPersonId(fMissionEntry1.getPersonId());
            update.setTime(fMissionEntry1.getTime());
        } else {
            update.setPost(1);
            update.setSignIn(0);
            update.setWelfare(0);
            update.setPersonId(personId);
            update.setTime(nowDate);
        }

        fMissionDao.saveOrUpdate(update);
    }

}
