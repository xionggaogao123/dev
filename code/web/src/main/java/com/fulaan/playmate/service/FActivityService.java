package com.fulaan.playmate.service;

import com.db.playmate.FActivityDao;
import com.db.user.UserDao;
import com.fulaan.playmate.dto.ActivityDTO;
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
 */
@Service
public class FActivityService {

    private FActivityDao fActivityDao = new FActivityDao();
    private UserDao userDao = new UserDao();

    public void saveActivity(ObjectId uid, double lon, double lat, int acode, String title, String desc, long endTime) {
        ObjectId _id = new ObjectId();
        FActivityEntry fActivityEntry = new FActivityEntry(_id, uid, acode, title, desc, lon, lat, endTime);
        fActivityDao.save(fActivityEntry);
    }

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

    public boolean signActivity(ObjectId acid, ObjectId userId, String signText) {
        if (fActivityDao.isUserSignActivity(acid, userId)) {
            return false;
        }
        FASignEntry faSignEntry = new FASignEntry(new ObjectId(), acid, userId, signText);
        fActivityDao.save(faSignEntry);
        return true;
    }

    public List<Map<String, Object>> get20SignSheets(ObjectId acid) {
        List<FASignEntry> signEntries = fActivityDao.get20SignEntry(acid);
        List<Map<String, Object>> sheets = new ArrayList<Map<String, Object>>();
        for (FASignEntry signEntry : signEntries) {
            ObjectId userId = signEntry.getUserId();
            UserEntry userEntry = userDao.findByObjectId(userId);
            String nickName = StringUtils.isBlank(userEntry.getNickName()) ? userEntry.getUserName() : userEntry.getNickName();
            String userName = userEntry.getUserName();
            String avatar = AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("nickName", nickName);
            map.put("userName", userName);
            map.put("avatar", avatar);
            sheets.add(map);
        }
        return sheets;
    }
}