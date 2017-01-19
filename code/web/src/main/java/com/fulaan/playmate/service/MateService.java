package com.fulaan.playmate.service;

import com.db.activity.FriendDao;
import com.db.playmate.FMateDao;
import com.db.user.UserDao;
import com.fulaan.base.BaseService;
import com.fulaan.playmate.dto.FMateDTO;
import com.fulaan.playmate.pojo.MateData;
import com.fulaan.pojo.PageModel;
import com.fulaan.pojo.User;
import com.fulaan.util.DateUtils;
import com.fulaan.util.DistanceUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.activity.FriendEntry;
import com.pojo.playmate.FMateEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserTag;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
@Service
public class MateService extends BaseService {

    @Autowired
    private FMateTypeService fMateTypeService;

    private FMateDao fMateDao = new FMateDao();
    private UserDao userDao = new UserDao();
    private FriendDao friendDao = new FriendDao();

    private List<MateData> datas;

    public PageModel<FMateDTO> findMates(ObjectId userId, double lng, double lat, List<String> tagList, int ageGroup, List<Integer> timeList, int page, int pageSize, int maxDistance) {
        List<FMateDTO> result = new ArrayList<FMateDTO>();
        PageModel<FMateDTO> pageModel = new PageModel<FMateDTO>();
        List<Integer> tagIntegers = getTagsList(tagList);
        BasicDBObject query = fMateDao.buildQuery(userId, lng, lat, tagIntegers, ageGroup, timeList, maxDistance);
        System.out.println(query.toString());
        int count = fMateDao.countByPage(query);
        int totalPages = count % pageSize == 0 ? count / pageSize : (int) Math.ceil(count / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if(totalPages == 0 || page < 1) {
            page = 1;
        }
        List<FMateEntry> fMateEntries = fMateDao.findByPage(query, page, pageSize);
        FriendEntry myFriendEnty = null;
        if (userId != null) {
            myFriendEnty = friendDao.get(userId);
        }
        for (FMateEntry mateEntry : fMateEntries) {
            FMateDTO fMateDTO = getfMateDTO(lng, lat, mateEntry);
            List<User> users = getCommonFriends(myFriendEnty, mateEntry);
            fMateDTO.setCommonFriends(users);
            result.add(fMateDTO);
        }
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalCount(count);
        pageModel.setTotalPages(totalPages);
        pageModel.setResult(result);
        return pageModel;
    }

    private List<Integer> getTagsList(List<String> tags) {
        List<Integer> tagIntegers = new ArrayList<Integer>();
        for (String tagStr : tags) {
            try {
                tagIntegers.add(Integer.parseInt(tagStr));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return tagIntegers;
    }

    private List<User> getCommonFriends(FriendEntry myFriendEnty, FMateEntry mateEntry) {
        List<User> users = new ArrayList<User>();
        //寻找共同好友
        FriendEntry userFriendEntry = friendDao.get(mateEntry.getUserId());
        if (myFriendEnty != null && userFriendEntry != null) {
            List<ObjectId> myFriendList = myFriendEnty.getFriendIds();
            List<ObjectId> userFriendList = userFriendEntry.getFriendIds();
            myFriendList.retainAll(userFriendList);

            for (ObjectId uid : myFriendList) {
                UserEntry entry = userDao.findByUserId(uid);
                if (entry != null) {
                    User user = new User();
                    if (entry.getAvatar() != null) {
                        user.setAvator(AvatarUtils.getAvatar(entry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                    }
                    user.setSex(entry.getSex());
                    user.setId(entry.getID().toString());
                    user.setNickName(StringUtils.isBlank(entry.getNickName()) ? entry.getUserName() : entry.getNickName());
                    user.setUserName(entry.getUserName());
                    users.add(user);
                }
            }
        }
        if (users.size() > 4) {
            users = users.subList(0, 4);
        }
        return users;
    }

    private FMateDTO getfMateDTO(double lon, double lat, FMateEntry mateEntry) {
        BasicDBList dbList = mateEntry.getLocation();
        String distance = "未知";
        if (lon != 0 && lat != 0 && dbList != null) {
            Double distanceDouble = DistanceUtils.distance(lon, lat, (Double) dbList.get(0), (Double) dbList.get(1));
            distance = fromDistance(distanceDouble.longValue());
        }
        FMateDTO fMateDTO = new FMateDTO();
        UserEntry userEntry = userDao.findByUserId(mateEntry.getUserId());
        fMateDTO.setDistance(distance);
        if(userEntry != null) {
            fMateDTO.setUserId(userEntry.getID().toString());
            fMateDTO.setNickName(userEntry.getNickName());
            fMateDTO.setUserName(userEntry.getUserName());
            fMateDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
            List<UserTag> tagList = new ArrayList<UserTag>();
            for (UserEntry.UserTagEntry tagEntry : userTagEntries) {
                UserTag userTag = new UserTag(tagEntry.getCode(), tagEntry.getTag());
                tagList.add(userTag);
            }
            fMateDTO.setTags(tagList);
        }

        List<MateData> onsList = getMateDatas(mateEntry);
        fMateDTO.setOns(onsList);
        return fMateDTO;
    }

    private List<MateData> getMateDatas(FMateEntry mateEntry) {
        List<MateData> onsList = new ArrayList<MateData>();
        for (MateData mateData : getDatas()) {
            BasicDBList basicDBList = mateEntry.getOns();
            if (basicDBList != null) {
                for (Object o : basicDBList) {
                    if (((Integer) o) == mateData.getCode()) {
                        onsList.add(mateData);
                    }
                }
            }
        }
        return onsList;
    }

    public List<Double> getCoordinates(ObjectId userId) {
        FMateEntry mateEntry = fMateDao.getMateEntryByUserId(userId);
        List<Double> locs = new ArrayList<Double>();
        if (mateEntry == null) return locs;
        BasicDBList dbList = mateEntry.getLocation();
        if (dbList != null) {
            locs.add((Double) dbList.get(0));
            locs.add((Double) dbList.get(1));
        }
        return locs;
    }


    public void saveMateEntry(ObjectId userId) {
        FMateEntry mateEntry = new FMateEntry(new ObjectId(), userId);
        fMateDao.save(mateEntry);
    }

    @Async
    public void updateLocation(ObjectId userId, double lon, double lat) {
        fMateDao.upateUserLocation(userId, lon, lat);
    }

    public List<MateData> getMyOns(ObjectId userId) {
        FMateEntry fMateEntry = fMateDao.getMateEntryByUserId(userId);
        if (fMateEntry == null) {
            return new ArrayList<MateData>();
        }
        BasicDBList dbList = fMateEntry.getOns();
        List<MateData> onsList = new ArrayList<MateData>();
        for (MateData mateData : getDatas()) {
            for (Object o : dbList) {
                if (((Integer) o) == mateData.getCode()) {
                    onsList.add(mateData);
                }
            }
        }
        return onsList;
    }

    public void updateTags(ObjectId userId, List<Integer> tags) {
        fMateDao.updateUserTags(userId, tags);
    }

    public void pushUserTag(ObjectId userId, int tag) {
        fMateDao.pushUserTag(userId, tag);
    }

    public void pullUserTag(ObjectId userId, int tag) {
        fMateDao.pullUserTag(userId, tag);
    }

    public boolean isMateRecoreExist(ObjectId userId) {
        return fMateDao.isExist(userId);
    }

    public void updateAged(ObjectId userId, long age) {
        try {
            int aged = DateUtils.getAgeFromTimeStamp(age);
            updateAge(userId, aged);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void updateAge(ObjectId userId, int age) {
        int aged = -1;
        if (age >= 3 && age <= 5) {
            aged = 1;
        } else if (age > 5 && age <= 8) {
            aged = 2;
        } else if (age > 8 && age <= 11) {
            aged = 3;
        } else if (age > 11 && age <= 15) {
            aged = 4;
        } else if (age > 15 && age <= 18) {
            aged = 5;
        } else if (age > 18) {
            aged = 6;
        }
        fMateDao.updateAged(userId, aged);
    }

    public void updateOns(ObjectId userId, List<Integer> onsList) {
        fMateDao.updateUserOns(userId, onsList);
    }

    private List<MateData> getDatas() {
        if (datas == null) {
            datas = fMateTypeService.getOns();
        }
        return datas;
    }
}
