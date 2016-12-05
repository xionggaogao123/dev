package com.fulaan.playmate.service;

import com.db.activity.FriendDao;
import com.db.playmate.FMateDao;
import com.db.user.UserDao;
import com.fulaan.playmate.dto.MateDTO;
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
import com.pojo.utils.MongoUtils;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
@Service
public class MateService {

    private FMateDao fMateDao = new FMateDao();
    private UserDao userDao = new UserDao();
    private FriendDao friendDao = new FriendDao();

    public PageModel<MateDTO> findMates(ObjectId userId, double lon, double lat, List<String> tags, List<String> hobbys, int aged, int ons, int page, int pageSize, int maxDistance) {
        List<MateDTO> mateDTOS = new ArrayList<MateDTO>();
        PageModel<MateDTO> pageModel = new PageModel<MateDTO>();

        List<Integer> tagIntegers = new ArrayList<Integer>();
        for (String tagStr : tags) {
            tagIntegers.add(Integer.parseInt(tagStr));
        }
        BasicDBObject query = fMateDao.buildQuery(lon, lat, tagIntegers, hobbys, aged, ons, maxDistance);
        System.out.println(query.toString());
        int count = fMateDao.countByPage(query);
        int totalPages = count % pageSize == 0 ? count / pageSize : (int) Math.ceil(count / pageSize) + 1;
        if (page > totalPages) {
            page = totalPages;
        }
        if (page <= 0) {
            page = 1;
        }
        List<FMateEntry> fMateEntries = fMateDao.findByPage(query, page, pageSize);
        FriendEntry myFriendEnty = null;
        if (userId != null) {
            myFriendEnty = friendDao.get(userId);
        }
        for (FMateEntry mateEntry : fMateEntries) {
            BasicDBList dbList = mateEntry.getLocation();

            if(mateEntry.getUserId().equals(userId)) continue;
            String distance = "未知";
            if (lon != 0 && lat != 0) {
                Double distanceDouble = DistanceUtils.distance(lon, lat, (Double) dbList.get(0), (Double) dbList.get(1));
                distance = String.valueOf(distanceDouble.longValue());
            }
            MateDTO mateDTO = new MateDTO();
            UserEntry userEntry = userDao.findByObjectId(mateEntry.getUserId());
            mateDTO.setDistance(distance);
            mateDTO.setUserId(userEntry.getID().toString());
            mateDTO.setNickName(userEntry.getNickName());
            mateDTO.setUserName(userEntry.getUserName());
            mateDTO.setTimePeriod(mateEntry.getOns());
            mateDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            List<UserTag> tagList = new ArrayList<UserTag>();
            List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
            for (UserEntry.UserTagEntry tagEntry : userTagEntries) {
                UserTag userTag = new UserTag(tagEntry.getCode(), tagEntry.getTag());
                tagList.add(userTag);
            }
            mateDTO.setTags(tagList);
            mateDTOS.add(mateDTO);

            List<User> users = new ArrayList<User>();
            //寻找共同好友
            FriendEntry userFriendEntry = friendDao.get(mateEntry.getUserId());
            if (myFriendEnty != null && userFriendEntry != null) {
                List<ObjectId> myFriendList = myFriendEnty.getFriendIds();
                List<ObjectId> userFriendList = userFriendEntry.getFriendIds();
                myFriendList.retainAll(userFriendList);

                for (ObjectId uid : myFriendList) {
                    UserEntry entry = userDao.findByObjectId(uid);
                    if(entry != null){
                        User user = new User();
                        if(entry.getAvatar() != null) {
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
            mateDTO.setCommonFriends(users);
        }
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalCount(count);
        pageModel.setTotalPages(totalPages);
        pageModel.setResult(mateDTOS);
        return pageModel;
    }

    public List<Double> getCoordinates(ObjectId userId) {
        FMateEntry mateEntry = fMateDao.getCoordinates(userId);
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

    public void updateLocation(ObjectId userId, double lon, double lat) {
        fMateDao.upateUserLocation(userId, lon, lat);
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

    public void updateAged(ObjectId userId,long age){
        try {
            age = DateUtils.getAgeFromTimeStamp(age);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        int aged = -1;
        if(age >= 3 && age <= 5){
            aged = 1;
        } else if(age > 5 && age <= 8){
            aged = 2;
        } else if(age > 8 && age <= 11) {
            aged = 3;
        } else if(age > 11 && age <= 15) {
            aged = 4;
        } else if(age > 15 && age <= 18) {
            aged = 5;
        } else if(age > 18){
            aged = 6;
        }
        fMateDao.updateAged(userId,aged);
    }

    public void updateHobbys(ObjectId userId, List<String> hobbys) {
        fMateDao.updateUserHobbys(userId, hobbys);
    }


}
