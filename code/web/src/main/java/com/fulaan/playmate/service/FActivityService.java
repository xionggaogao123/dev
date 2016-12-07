package com.fulaan.playmate.service;

import com.db.playmate.FActivityDao;
import com.db.user.UserDao;
import com.fulaan.playmate.dto.ActivityDTO;
import com.fulaan.playmate.pojo.User;
import com.fulaan.pojo.PageModel;
import com.fulaan.util.DistanceUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.playmate.FASignEntry;
import com.pojo.playmate.FActivityEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserTag;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moslpc on 2016/12/2.
 * 活动 Service
 */
@Service
public class FActivityService {

    private FActivityDao fActivityDao = new FActivityDao();
    private UserDao userDao = new UserDao();

    /**
     * 保存活动
     *
     * @param uid          发布人userId
     * @param lon          经度
     * @param lat          纬度
     * @param acode        活动主题code
     * @param title        标题
     * @param desc         描述
     * @param activityTime 活动时间
     */
    public void saveActivity(ObjectId uid, double lon, double lat, int acode, String title, String desc, long activityTime) {
        ObjectId _id = new ObjectId();
        FActivityEntry fActivityEntry = new FActivityEntry(_id, uid, acode, title, desc, lon, lat, activityTime);
        FASignEntry faSignEntry = new FASignEntry(new ObjectId(), _id, uid, "", activityTime);
        fActivityDao.save(faSignEntry);
        fActivityDao.save(fActivityEntry);
    }

    /**
     * 获取距离由近到远的活动排序 - 按距离排序
     *
     * @param lon      经度
     * @param lat      纬度
     * @param page     页
     * @param pageSize 每页个数
     * @return PageModel
     */
    public PageModel<ActivityDTO> getNearActivitys(double lon, double lat, int page, int pageSize) {
        BasicDBObject query = fActivityDao.buildQuery(lon, lat, 10000000);
        int count = fActivityDao.coutByQuery(query);
        int totalPages = count % pageSize == 0 ? count / pageSize : (int) Math.ceil(count / pageSize) + 1;
        if (page > totalPages) {
            page = totalPages;
        }
        if (page <= 0) {
            page = 1;
        }
        List<ActivityDTO> activityDTOS = new ArrayList<ActivityDTO>();
        List<FActivityEntry> activityEntryList = fActivityDao.findByPage(query, page, pageSize);
        for (FActivityEntry entry : activityEntryList) {
            ActivityDTO activityDTO = new ActivityDTO(entry);
            activityDTO.setSignCount(fActivityDao.countSignUser(entry.getID()));
            ObjectId userId = entry.getUserId();
            UserEntry userEntry = userDao.findByObjectId(userId);
            String nickName = userEntry.getNickName();
            String userName = userEntry.getUserName();
            String avatar = AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType());
            List<UserTag> tags = new ArrayList<UserTag>();
            List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
            for (UserEntry.UserTagEntry tagEntry : userTagEntries) {
                UserTag userTag = new UserTag(tagEntry.getCode(), tagEntry.getTag());
                tags.add(userTag);
            }
            activityDTO.setUser(userId, nickName, userName, avatar, tags);
            BasicDBList locs = entry.getLocations();
            if (locs != null) {
                Double distance = DistanceUtils.distance(lon, lat, (Double) locs.get(0), (Double) locs.get(1));
                activityDTO.setDistance(String.valueOf(distance.longValue()));
            }
            activityDTO.setSignSheets(get20SignSheets(entry.getID()));
            activityDTOS.add(activityDTO);
        }
        PageModel<ActivityDTO> pageModel = new PageModel<ActivityDTO>();
        pageModel.setResult(activityDTOS);
        pageModel.setTotalCount(count);
        pageModel.setTotalPages(totalPages);
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        return pageModel;
    }

    public User getMateUser(ObjectId userId) {
        UserEntry userEntry = userDao.findByObjectId(userId);
        String nickName = userEntry.getNickName();
        String userName = userEntry.getUserName();
        String avatar = AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType());
        List<UserTag> tags = new ArrayList<UserTag>();
        List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
        for (UserEntry.UserTagEntry tagEntry : userTagEntries) {
            UserTag userTag = new UserTag(tagEntry.getCode(), tagEntry.getTag());
            tags.add(userTag);
        }
        User user = new User();
        user.userId = userId.toString();
        user.nickName = nickName;
        user.userName = userName;
        user.avatar = avatar;
        user.tags = tags;
        return user;
    }

    public ActivityDTO getActivityById(ObjectId acid) {
        FActivityEntry fActivityEntry = fActivityDao.getActivityById(acid);
        if (fActivityEntry == null) {
            return null;
        }
        ActivityDTO activityDTO = new ActivityDTO(fActivityEntry);
        activityDTO.setSignCount(fActivityDao.countSignUser(acid));
        activityDTO.setUser(getMateUser(fActivityEntry.getUserId()));
        return activityDTO;
    }

    public List<Map<String, Object>> getAllSignMembers(ObjectId acid) {
        List<FASignEntry> signEntries = fActivityDao.getAllSignMember(acid);
        return getMembers(signEntries);
    }

    private List<Map<String, Object>> getMembers(List<FASignEntry> signEntries) {
        List<Map<String, Object>> sheets = new ArrayList<Map<String, Object>>();
        for (FASignEntry signEntry : signEntries) {
            ObjectId userId = signEntry.getUserId();
            UserEntry userEntry = userDao.findByObjectId(userId);
            String nickName = StringUtils.isBlank(userEntry.getNickName()) ? userEntry.getUserName() : userEntry.getNickName();
            String userName = userEntry.getUserName();
            String avatar = AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType());
            Map<String, Object> map = new HashMap<String, Object>();

            List<UserTag> userTagList = new ArrayList<UserTag>();
            List<UserEntry.UserTagEntry> tagEntries = userEntry.getUserTag();
            for (UserEntry.UserTagEntry tagEntry : tagEntries) {
                userTagList.add(new UserTag(tagEntry.getCode(), tagEntry.getTag()));
            }
            map.put("tags", userTagList);
            map.put("nickName", nickName);
            map.put("userName", userName);
            map.put("avatar", avatar);
            sheets.add(map);
        }
        return sheets;
    }

    /**
     * 获取某人发布的活动 - 按时间先后顺序
     *
     * @param userId   用户id
     * @param page     页
     * @param pageSize 每页个数
     * @return PageModel
     */
    public PageModel<ActivityDTO> getPublishedActivity(ObjectId userId, int page, int pageSize) {
        int count = fActivityDao.countPublishActivity(userId);
        int totalPages = count % pageSize == 0 ? count / pageSize : (int) Math.ceil(count / pageSize) + 1;
        if (page > totalPages) {
            page = totalPages;
        }
        if (page <= 0) {
            page = 1;
        }
        List<FActivityEntry> activityEntries = fActivityDao.getPublishedActivity(userId, page, pageSize);
        List<ActivityDTO> activityDTOS = new ArrayList<ActivityDTO>();
        PageModel<ActivityDTO> pageModel = new PageModel<ActivityDTO>();
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalPages(totalPages);
        pageModel.setTotalCount(count);
        for (FActivityEntry activityEntry : activityEntries) {
            activityDTOS.add(new ActivityDTO(activityEntry));
        }
        pageModel.setResult(activityDTOS);
        return pageModel;
    }

    public boolean signActivity(ObjectId acid, ObjectId userId, String signText) {
        if (fActivityDao.isUserSignActivity(acid, userId)) {
            return false;
        }
        FActivityEntry activityEntry = fActivityDao.getActivityById(acid);
        FASignEntry faSignEntry = new FASignEntry(new ObjectId(), acid, userId, signText, activityEntry.getActivityTime());
        fActivityDao.save(faSignEntry);
        return true;
    }

    public boolean isUserSigned(ObjectId acid, ObjectId userId) {
        return fActivityDao.isUserSignActivity(acid, userId);
    }

    public int countUserSignActivity(ObjectId userId) {
        return fActivityDao.countUserSignActivity(userId);
    }

    public List<Map<String, Object>> get20SignSheets(ObjectId acid) {
        List<FASignEntry> signEntries = fActivityDao.get20SignEntry(acid);
        return getMembers(signEntries);
    }

    public int countPublishActivity(ObjectId userId) {
        return fActivityDao.countPublishActivity(userId);
    }

    /**
     * 获取已经报名的活动
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PageModel<ActivityDTO> getSignedActivity(ObjectId userId, int page, int pageSize) {
        int count = fActivityDao.countUserSignActivity(userId);
        int totalPages = count % pageSize == 0 ? count / pageSize : (int) Math.ceil(count / pageSize) + 1;
        if (page > totalPages) {
            page = totalPages;
        }
        if (page <= 0) {
            page = 1;
        }
        List<FActivityEntry> activityEntries = fActivityDao.getSignedActivity(userId, page, pageSize);
        List<ActivityDTO> activityDTOS = new ArrayList<ActivityDTO>();
        PageModel<ActivityDTO> pageModel = new PageModel<ActivityDTO>();
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalPages(totalPages);
        pageModel.setTotalCount(count);
        for (FActivityEntry activityEntry : activityEntries) {
            activityDTOS.add(new ActivityDTO(activityEntry));
        }
        pageModel.setResult(activityDTOS);
        return pageModel;
    }

    /**
     * 获取已经参加过的活动
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Object getAttendedActivity(ObjectId userId, int page, int pageSize) {
        int count = fActivityDao.countUserAttendActivity(userId);
        int totalPages = count % pageSize == 0 ? count / pageSize : (int) Math.ceil(count / pageSize) + 1;
        if (page > totalPages) {
            page = totalPages;
        }
        if (page <= 0) {
            page = 1;
        }
        List<FActivityEntry> activityEntries = fActivityDao.getAttendedActivity(userId, page, pageSize);
        List<ActivityDTO> activityDTOS = new ArrayList<ActivityDTO>();
        PageModel<ActivityDTO> pageModel = new PageModel<ActivityDTO>();
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalPages(totalPages);
        pageModel.setTotalCount(count);
        for (FActivityEntry activityEntry : activityEntries) {
            activityDTOS.add(new ActivityDTO(activityEntry));
        }
        pageModel.setResult(activityDTOS);
        return pageModel;
    }

    public FActivityEntry getActivityEntryById(ObjectId acid) {
        return fActivityDao.getActivityById(acid);
    }

    /**
     * 取消报名活动
     *
     * @param acid   活动id
     * @param userId 用户 id
     */
    public void cancelSignActivity(ObjectId acid, ObjectId userId) {
        fActivityDao.cancelSignActivity(acid, userId);
    }

    /**
     * 删除发布的活动
     *
     * @param acid   活动id
     * @param userId 用户id
     */
    public void cancelPublishActivity(ObjectId acid, ObjectId userId) {
        fActivityDao.cancelPublishActivity(acid, userId);
    }

}
