package com.fulaan.experience.service;

import com.db.microblog.MicroBlogDao;
import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.db.user.UserExperienceLogDao;
import com.db.user.UserSchoolYearExperienceDao;
import com.fulaan.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.experience.dto.ExperienceLogDTO;
import com.fulaan.experience.dto.UserExperienceLogDTO;
import com.mongodb.BasicDBObject;
import com.pojo.app.Platform;
import com.pojo.app.SessionValue;
import com.pojo.school.ClassEntry;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserExperienceLogEntry;
import com.pojo.user.UserExperienceLogEntry.ExperienceLog;
import com.pojo.user.UserSchoolYearExperienceEntry;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.jsoup.helper.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ExperienceService {

    private UserExperienceLogDao userExpLogDao = new UserExperienceLogDao();

    private UserDao userDao = new UserDao();

    private ClassDao classDao = new ClassDao();

    private UserSchoolYearExperienceDao userSchoolYearExperienceDao = new UserSchoolYearExperienceDao();

    private MicroBlogDao microBlogDao = new MicroBlogDao();


    public SessionValue getSessionValue() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (SessionValue) request.getAttribute(BaseController.SESSION_VALUE);
    }

    /**
     * 获取用户经验值
     *
     * @param userId
     * @param pageable
     */
    public Page<ExperienceLogDTO> selUserExperienceInfoList(String userId, Pageable pageable) {
        //取得分页页码
        int page = pageable.getOffset() - pageable.getPageSize();
        //取得分页每页的size
        int pageSize = pageable.getPageSize();
        //取得用户积分日志信息
        UserExperienceLogEntry userExpLogEntry = userExpLogDao.getUserExperienceLogEntry(new ObjectId(userId), page, pageSize);
        UserExperienceLogDTO userExpLogDTO = new UserExperienceLogDTO(userExpLogEntry);
        //取得用户增减积分的明细信息
        List<ExperienceLogDTO> expLogDTOList = userExpLogDTO.getExperienceLogs();
        sortList(expLogDTOList);
        Collections.reverse(expLogDTOList);
        return new PageImpl<ExperienceLogDTO>(expLogDTOList, pageable, userExpLogEntry.getCount());
    }

    /**
     * 对用户经验值集合，按照时间、积分排序
     *
     * @param list
     */
    public void sortList(List<ExperienceLogDTO> list) {
        Collections.sort(list, new Comparator<ExperienceLogDTO>() {
            public int compare(ExperienceLogDTO obj1, ExperienceLogDTO obj2) {
                int flag = obj1.getCreatetime().compareTo(obj2.getCreatetime());
                if (flag == 0) {
                    return obj1.getExperience() - obj2.getExperience();
                } else {
                    return flag;
                }
            }
        });
    }

    /**
     * 给用户添加经验值信息
     *
     * @param userId        用户ID
     * @param experienceLog 积分信息
     * @return
     */
    public void addUserExperience(String userId, ExperienceLogDTO experienceLog) {
        //判断用户是否添加过经验值信息
        if (isExistUserExp(userId)) {
            //在已存在的用户积分的积分信息中添加积分信息
            userExpLogDao.addExperienceLogEntry(new ObjectId(userId), experienceLog.buildExperienceLog());
        } else {
            //添加用户积分信息
            List<ExperienceLog> list = new ArrayList<ExperienceLog>();
            list.add(experienceLog.buildExperienceLog());
            UserExperienceLogEntry userExperienceLogEntry = new UserExperienceLogEntry(new ObjectId(userId), list);
            userExpLogDao.addUserExperienceLog(userExperienceLogEntry);
        }
    }

    /**
     * 判断用户是否添加过经验值信息
     *
     * @param userId 用户ID
     * @return
     */
    public boolean isExistUserExp(String userId) {
        int count = userExpLogDao.userExpCount(new ObjectId(userId));
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 指定分值的加分/减分
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @param relateId   相关ID
     * @return boolean
     */
    public boolean updateScore(String userId, ExpLogType expLogType, String relateId) {
        return update(userId, expLogType, expLogType.getExp(), relateId, false, false, true);
    }

    /**
     * 指定分值的加分/减分
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @param score      积分
     * @param relateId   相关ID
     * @return boolean
     */
    public boolean updateScore(String userId, ExpLogType expLogType, int score, String relateId) {
        return update(userId, expLogType, score, relateId, false, false, false);
    }

    /**
     * 商城积分抵用加减积分
     *
     * @param userId
     * @param expLogType
     * @param exp
     * @param relateId
     * @return
     */
    public boolean updateScoreForMall(String userId, ExpLogType expLogType, int exp, String relateId) {
        //获取用户积分信息
        Boolean flag = false;
        try {
            UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), new BasicDBObject("exp", 1).append(Constant.ID, 1));
            int experience = userEntry.getExperiencevalue();
            experience = experience + exp;
            //修改用户的积分
            userDao.update(new ObjectId(userId), "exp", experience, true);

            SessionValue sv = getSessionValue();
            sv.setExperience(experience);
            String userKey = CacheHandler.getUserKey(sv.getId());
            CacheHandler.cacheSessionValue(userKey, sv, Constant.SECONDS_IN_DAY);


            ExperienceLogDTO expLogs = new ExperienceLogDTO();
            expLogs.setExperience(exp);
            expLogs.setExperiencename(expLogType.getDesc());
            expLogs.setCreatetime(new Date());
            expLogs.setExpLogTypeOrdinal(expLogType.getType());
            expLogs.setRelateId(relateId);
            //将增减的积分信息添加到积分日志表中
            addUserExperience(userId, expLogs);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return flag;
        }
    }

    /**
     * 指定分值的加分/减分
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @return boolean
     */
    public boolean updateScore(String userId, ExpLogType expLogType) {
        return updateScore(userId, expLogType, null);
    }

    /**
     * 检查第一次的加分
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @return
     */
    public boolean updateNoRepeat(String userId, ExpLogType expLogType) {
        return updateNoRepeat(userId, expLogType, null);
    }

    /**
     * 检查第一次的加分
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @param relateId   关联id
     * @return
     */
    public boolean updateNoRepeat(String userId, ExpLogType expLogType, String relateId) {
        return update(userId, expLogType, expLogType.getExp(), relateId, true, false, true);
    }

    /**
     * 检查第一次的加分,且是否当天第一次
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @param relateId   关联id
     * @return
     */
    public boolean updateNoRepeatAndDaily(String userId, ExpLogType expLogType, String relateId) {
        return update(userId, expLogType, expLogType.getExp(), relateId, true, true, true);
    }

    /**
     * 检查是否当天第一次
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @param relateId   关联id
     * @return
     */
    public boolean updateDaily(String userId, ExpLogType expLogType, String relateId) {
        return updateDaily(userId, expLogType, expLogType.getExp(), relateId);
    }

    /**
     * 检查是否当天第一次
     *
     * @param userId     用户ID
     * @param expLogType 加分类型
     * @param relateId   关联id
     * @return
     */
    public boolean updateDaily(String userId, ExpLogType expLogType, int exp, String relateId) {
        return update(userId, expLogType, exp, relateId, false, true, true);
    }


    public int updateMicroBlogDaily(String userId, ExpLogType microblog, int userExp, String relateId, Platform pf) {
        int exp = userExp;
        if (pf.getType() == 2 || pf.getType() == 3) {
            long currTime = DateTimeUtils.getDayMinTime(new Date().getTime());
            //检查是否当天第一次
            Long count = microBlogDao.getPhonePublicMicroBlogCount(new ObjectId(userId), currTime);
            if (count <= 1) {
                String currDate = DateTimeUtils.getCurrDate();
                String startDate = "2015-07-07";
                String endDate = "2015-08-31";
                boolean sBool = DateTimeUtils.compare_date2(startDate, currDate) < 1;
                boolean eBool = DateTimeUtils.compare_date2(currDate, endDate) < 1;
                if (sBool && eBool) {
                    exp = 5;
                }
            }
        }
        boolean result = updateDaily(userId, microblog, exp, relateId);
        if (result) {
            return exp;
        } else {
            return -1;
        }
    }

    /**
     * 增减积分记录到积分日志表中，并修改用户积分
     *
     * @param userId      用户ID
     * @param expLogType  加分类型
     * @param exp         加分值
     * @param relateId    关联id
     * @param checkRepeat 是否重复
     * @param checkDaily  检查是否当天第一次
     * @return
     */
    private boolean update(String userId, ExpLogType expLogType, int exp, String relateId, boolean checkRepeat, boolean checkDaily, boolean updateSession) {
        try {
            if (updateSession) {
                long currTime = 0l;
                //取得当前日期
                currTime = DateTimeUtils.getDayMinTime(new Date().getTime());

                if (!expLogType.equals(ExpLogType.AVATAR) && !expLogType.equals(ExpLogType.PASSWORD)) {
                    //查询日积分上限
                    String ceilingOfDailyExp = Resources.getProperty("ceilingOfDailyExp");
                    //判断积分上限是否是数字类型
                    if (StringUtil.isNumeric(ceilingOfDailyExp)) {
                        int ceilingOfDailyExpInt = Integer.parseInt(ceilingOfDailyExp);
                        //取得当前日增减的积分日志
                        List<ExperienceLog> expList = userExpLogDao.getExperienceLogList(new ObjectId(userId), currTime);
                        int dayTotalExp = 0;
                        for (ExperienceLog expLog : expList) {
                            //计算出今日增减积分总和
                            dayTotalExp += expLog.getExperience();
                        }
                        //判断今日积分总和是否超过日积分上限
                        if (dayTotalExp >= ceilingOfDailyExpInt) {
                            return false;
                        }
                    }
                }

                if (checkDaily) {
                    List<Integer> types = new ArrayList<Integer>();
                    if (expLogType.equals(ExpLogType.MICROBLOG) || expLogType.equals(ExpLogType.MICRO_BLOG_REVIEW)
                            || expLogType.equals(ExpLogType.MICRO_HOME_BLOG) || expLogType.equals(ExpLogType.MICRO_HOME_BLOG_REVIEW)) {//每日5分
                        types.add(ExpLogType.MICROBLOG.getType());
                        types.add(ExpLogType.MICRO_BLOG_REVIEW.getType());
                        types.add(ExpLogType.MICRO_HOME_BLOG.getType());
                        types.add(ExpLogType.MICRO_HOME_BLOG_REVIEW.getType());
                        int dayTotalExp = getDailyUserExpByExpLogTypes(new ObjectId(userId), currTime, types);
                        if (dayTotalExp >= 5) {
                            return false;
                        }
                    }

                    if (expLogType.equals(ExpLogType.CLOUD)) {//每日5分
                        types.add(ExpLogType.CLOUD.getType());
                        int dayTotalExp = getDailyUserExpByExpLogTypes(new ObjectId(userId), currTime, types);
                        if (dayTotalExp >= 5) {
                            return false;
                        }
                    }
                    if (expLogType.equals(ExpLogType.LAUNCH_FRIEND_SCIRCLE_ACTIVITY) || expLogType.equals(ExpLogType.JOIN_FRIEND_SCIRCLE_ACTIVITY)) {//每日3分
                        types.add(ExpLogType.LAUNCH_FRIEND_SCIRCLE_ACTIVITY.getType());
                        types.add(ExpLogType.JOIN_FRIEND_SCIRCLE_ACTIVITY.getType());
                        int dayTotalExp = getDailyUserExpByExpLogTypes(new ObjectId(userId), currTime, types);
                        if (dayTotalExp >= 3) {
                            return false;
                        }
                    }
                }

                //判断积分是否可以重复增减
                if (checkRepeat) {
                    int count = 0;
                    count = countUserExp(userId, relateId, expLogType.getType());
                    if (count > 0) {
                        return false;
                    }
                }
            } else {
                ClassEntry classEntry = classDao.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
                UserSchoolYearExperienceEntry schoolYearExp = userSchoolYearExperienceDao.getOneUserSchoolYearExperience(new ObjectId(userId), new BasicDBObject("sye", 1).append(Constant.ID, 1));
                if (schoolYearExp == null) {
                    schoolYearExp = new UserSchoolYearExperienceEntry(
                            new ObjectId(userId),
                            classEntry.getSchoolId(),
                            classEntry.getGradeId(),
                            classEntry.getID(),
                            exp);
                    userSchoolYearExperienceDao.addUserSchoolYearExperience(schoolYearExp);
                } else {
                    int newSchoolYearExp = schoolYearExp.getSchoolYearExperience() + exp;
                    userSchoolYearExperienceDao.updateUserSchoolYearExperience(
                            new ObjectId(userId),
                            classEntry.getSchoolId(),
                            classEntry.getGradeId(),
                            classEntry.getID(),
                            newSchoolYearExp);
                }

            }

            //获取用户积分信息

            UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), new BasicDBObject("exp", 1).append("lad", 1).append(Constant.ID, 1));
            int experience = userEntry.getExperiencevalue();
            experience = experience + exp;
            //修改用户的积分
            userDao.update(new ObjectId(userId), "exp", experience, true);
            if (updateSession) {
                SessionValue sv = getSessionValue();
                sv.setExperience(experience);
                String userKey = CacheHandler.getUserKey(sv.getId());
                int validityTime = Constant.SECONDS_IN_DAY + (int) (userEntry.getLastActiveDate() - System.currentTimeMillis()) / 1000;
                CacheHandler.cacheSessionValue(userKey, sv, validityTime);
            }

            ExperienceLogDTO expLogs = new ExperienceLogDTO();
            expLogs.setExperience(exp);
            expLogs.setExperiencename(expLogType.getDesc());
            expLogs.setCreatetime(new Date());
            expLogs.setExpLogTypeOrdinal(expLogType.getType());
            expLogs.setRelateId(relateId);
            //将增减的积分信息添加到积分日志表中
            addUserExperience(userId, expLogs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getDailyUserExpByExpLogTypes(ObjectId userId, long currTime, List<Integer> types) {
        List<ExperienceLog> expList = userExpLogDao.getExperienceLogListByTypes(userId, currTime, types);
        int dayTotalExp = 0;
        for (ExperienceLog expLog : expList) {
            //计算出今日增减积分总和
            dayTotalExp += expLog.getExperience();
        }
        return dayTotalExp;
    }

    /**
     * 查询用户已添加的积分数
     *
     * @param userId   用户ID
     * @param relateId 关联id
     * @param type
     * @return
     */
    private int countUserExp(String userId, String relateId, int type) {
        return userExpLogDao.countUserExp(new ObjectId(userId), relateId, type);
    }

}
