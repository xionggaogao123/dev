package com.fulaan.playmate.service;

import com.db.playmate.FActivityDao;
import com.db.user.UserDao;
import com.fulaan.base.BaseService;
import com.fulaan.playmate.dto.FActivityDTO;
import com.fulaan.playmate.pojo.MateData;
import com.fulaan.playmate.pojo.User;
import com.fulaan.pojo.PageModel;
import com.fulaan.util.DistanceUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.playmate.FASignEntry;
import com.pojo.playmate.FActivityEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserTag;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FActivityService extends BaseService{

    private FActivityDao fActivityDao = new FActivityDao();
    private UserDao userDao = new UserDao();
    @Autowired
    private FMateTypeService fMateTypeService;

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
    public PageModel<FActivityDTO> getNearActivitys(double lon, double lat, int page, int pageSize) {
        BasicDBObject query = fActivityDao.buildQuery(lon, lat, 10000000);
        int totalCount = fActivityDao.coutByQuery(query);
        int totalPages = totalCount % pageSize == Constant.ZERO ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + Constant.ONE;
        page = page > totalPages ? totalPages : page;
        if(totalPages == Constant.ZERO || page < Constant.ONE) {
            page = Constant.ONE;
        }
        List<MateData> allTags = fMateTypeService.getTags();
        List<FActivityDTO> FActivityDTOS = new ArrayList<FActivityDTO>();
        List<FActivityEntry> activityEntryList = fActivityDao.findByPage(query, page, pageSize);
        for (FActivityEntry entry : activityEntryList) {
            FActivityDTO fActivityDTO = new FActivityDTO(entry);
            fActivityDTO.setSignCount(fActivityDao.countSignUser(entry.getID()));
            ObjectId userId = entry.getUserId();
            UserEntry userEntry = userDao.findByUserId(userId);
            String nickName = userEntry.getNickName();
            String userName = userEntry.getUserName();
            String avatar = AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex());
            List<UserTag> tags = new ArrayList<UserTag>();
            List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
            for (UserEntry.UserTagEntry tagEntry : userTagEntries) {
                UserTag userTag = new UserTag(tagEntry.getCode(), tagEntry.getTag());
                tags.add(userTag);
            }
            fActivityDTO.setUser(userId, nickName, userName, avatar, tags);

            for (MateData mateData : allTags) {
                if (mateData.getCode() == entry.getACode()) {
                    fActivityDTO.setActivityTheme(mateData);
                }
            }
            if (fActivityDTO.getActivityTheme() == null) {
                fActivityDTO.setActivityTheme(new MateData(-1, "不限"));
            }

            fActivityDTO.setSignSheets(getAllSignMembers(entry.getID()));
            BasicDBList locs = entry.getLocations();
            if (locs != null) {
                Double distance = DistanceUtils.distance(lon, lat, (Double) locs.get(0), (Double) locs.get(1));
                fActivityDTO.setDistance(fromDistance(distance.longValue()));
            }
            FActivityDTOS.add(fActivityDTO);
        }
        PageModel<FActivityDTO> pageModel = new PageModel<FActivityDTO>();
        pageModel.setResult(FActivityDTOS);
        pageModel.setTotalCount(totalCount);
        pageModel.setTotalPages(totalPages);
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        return pageModel;
    }

    private User getMateUser(ObjectId userId) {
        UserEntry userEntry = userDao.findByUserId(userId);
        if (userEntry == null) {
            return null;
        }
        String nickName = userEntry.getNickName();
        String userName = userEntry.getUserName();
        String avatar = AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex());
        List<UserTag> tags = new ArrayList<UserTag>();
        List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
        for (UserEntry.UserTagEntry tagEntry : userTagEntries) {
            UserTag userTag = new UserTag(tagEntry.getCode(), tagEntry.getTag());
            tags.add(userTag);
        }
        User user = new User();
        user.setUserId(userId.toString());
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setAvatar(avatar);
        user.setTags(tags);
        return user;
    }

    public FActivityDTO getActivityById(ObjectId acid) {
        FActivityEntry fActivityEntry = fActivityDao.getActivityById(acid);
        if (fActivityEntry == null) {
            return null;
        }
        FActivityDTO fActivityDTO = new FActivityDTO(fActivityEntry);
        List<MateData> allTags = fMateTypeService.getTags();
        for (MateData mateData : allTags) {
            if (mateData.getCode() == fActivityEntry.getACode()) {
                fActivityDTO.setActivityTheme(mateData);
            }
        }
        if (fActivityDTO.getActivityTheme() == null) {
            fActivityDTO.setActivityTheme(new MateData(-1, "不限"));
        }
        fActivityDTO.setSignCount(fActivityDao.countSignUser(acid));
        fActivityDTO.setUser(getMateUser(fActivityEntry.getUserId()));
        return fActivityDTO;
    }

    public List<Map<String, Object>> getAllSignMembers(ObjectId acid) {
        List<FASignEntry> signEntries = fActivityDao.getAllSignMember(acid);
        return getMembers(signEntries);
    }

    private List<Map<String, Object>> getMembers(List<FASignEntry> signEntries) {
        List<Map<String, Object>> sheets = new ArrayList<Map<String, Object>>();
        for (FASignEntry signEntry : signEntries) {
            ObjectId userId = signEntry.getUserId();
            UserEntry userEntry = userDao.findByUserId(userId);
            if (userEntry == null) {
                continue;
            }
            String nickName = StringUtils.isBlank(userEntry.getNickName()) ? userEntry.getUserName() : userEntry.getNickName();
            String userName = userEntry.getUserName();
            String avatar = AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex());
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
            map.put("userId", userId.toString());
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
    public PageModel<FActivityDTO> getPublishedActivity(ObjectId userId, int page, int pageSize) {
        int totalCount = fActivityDao.countPublishActivity(userId);
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if(totalPages == 0 || page < 1) {
            page = 1;
        }
        List<FActivityEntry> activityEntries = fActivityDao.getPublishedActivity(userId, page, pageSize);
        PageModel<FActivityDTO> pageModel = new PageModel<FActivityDTO>();
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalPages(totalPages);
        pageModel.setTotalCount(totalCount);
        pageModel.setResult(getFActivityDtos(activityEntries, userId));
        return pageModel;
    }


    public boolean signActivity(ObjectId acid, ObjectId userId, String signText) {
        if (fActivityDao.isUserSignedActivity(acid, userId)) {
            return false;
        }

        if (fActivityDao.isUserPublishedActivity(acid, userId)) {
            return false;
        }
        FActivityEntry activityEntry = fActivityDao.getActivityById(acid);
        FASignEntry faSignEntry = new FASignEntry(new ObjectId(), acid, userId, signText, activityEntry.getActivityTime());
        fActivityDao.save(faSignEntry);
        return true;
    }

    public boolean isUserSigned(ObjectId acid, ObjectId userId) {
        return fActivityDao.isUserSignedActivity(acid, userId);
    }

    public int countUserSignActivity(ObjectId userId) {
        return fActivityDao.countUserSignActivity(userId);
    }

    public int countPublishActivity(ObjectId userId) {
        return fActivityDao.countPublishActivity(userId);
    }

    /**
     * 获取已经报名的活动
     *
     * @param userId 用户id
     * @param page 页
     * @param pageSize 页码
     * @return
     */
    public PageModel<FActivityDTO> getSignedActivity(ObjectId userId, int page, int pageSize) {
        int totalCount = fActivityDao.countUserSignActivity(userId);
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if(totalPages == 0 || page < 1) {
            page = 1;
        }
        List<FActivityEntry> activityEntries = fActivityDao.getSignedActivity(userId, page, pageSize);
        PageModel<FActivityDTO> pageModel = new PageModel<FActivityDTO>();
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalPages(totalPages);
        pageModel.setTotalCount(totalCount);
        pageModel.setResult(getFActivityDtos(activityEntries, userId));
        return pageModel;
    }

    /**
     * 获取已经参加过的活动
     *
     * @param userId 用户id
     * @param page 页
     * @param pageSize 页码
     * @return
     */
    public Object getAttendedActivity(ObjectId userId, int page, int pageSize) {
        int totalCount = fActivityDao.countUserAttendActivity(userId);
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if(totalPages == 0 || page < 1) {
            page = 1;
        }
        List<FActivityEntry> activityEntries = fActivityDao.getAttendedActivity(userId, page, pageSize);
        PageModel<FActivityDTO> pageModel = new PageModel<FActivityDTO>();
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalPages(totalPages);
        pageModel.setTotalCount(totalCount);
        pageModel.setResult(getFActivityDtos(activityEntries, userId));
        return pageModel;
    }

    private List<FActivityDTO> getFActivityDtos(List<FActivityEntry> entryList, ObjectId userId) {
        List<FActivityDTO> fActivityDTOS = new ArrayList<FActivityDTO>();
        List<MateData> allTags = fMateTypeService.getTags();
        for (FActivityEntry entry : entryList) {
            FActivityDTO fActivityDTO = new FActivityDTO(entry);
            for (MateData mateData : allTags) {
                if (mateData.getCode() == entry.getACode()) {
                    fActivityDTO.setActivityTheme(mateData);
                }
            }
            if (fActivityDTO.getActivityTheme() == null) {
                fActivityDTO.setActivityTheme(new MateData(-1, "不限"));
            }
            if (isUserSigned(entry.getID(), userId)) {
                fActivityDTO.setYouSigned(true);
            }
            fActivityDTOS.add(fActivityDTO);
        }
        return fActivityDTOS;

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
