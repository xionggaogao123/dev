package com.fulaan.playmate.service;

import com.db.playmate.FMateDao;
import com.db.user.UserDao;
import com.fulaan.playmate.dto.MateDTO;
import com.fulaan.pojo.PageModel;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.playmate.FMateEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.utils.AvatarUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
@Service
public class MateService {

    private FMateDao fMateDao = new FMateDao();
    private UserDao userDao = new UserDao();

    public PageModel<MateDTO> findMates(double lon, double lat, List<String> tags, List<String> hobbys, int aged, int ons, int page, int pageSize, int maxDistance) {
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
        for (FMateEntry mateEntry : fMateEntries) {
            BasicDBList dbList = mateEntry.getLocation();
            String distance = "未知";
            if (lon != 0 && lat != 0) {
                Double distanceDouble = distance(lon, lat, (Double) dbList.get(0), (Double) dbList.get(1));
                distance = String.valueOf(distanceDouble.longValue());
            }
            MateDTO mateDTO = new MateDTO();
            UserEntry userEntry = userDao.findByObjectId(mateEntry.getUserId());
            mateDTO.setDistance(distance);
            mateDTO.setUserId(userEntry.getID().toString());
            mateDTO.setNickName(userEntry.getNickName());
            mateDTO.setUserName(userEntry.getUserName());
            mateDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            mateDTO.setTags(MongoUtils.convertToStrList(mateEntry.getTags()));
            mateDTO.setHobbys(MongoUtils.convertToStrList(mateEntry.getHobbys()));
            mateDTOS.add(mateDTO);
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

    public void updateHobbys(ObjectId userId, List<String> hobbys) {
        fMateDao.updateUserHobbys(userId, hobbys);
    }

    /**
     * 计算地图上两点之间的距离(单位千米)
     *
     * @return
     */
    public Double distance(double longitude, double latitude, double long2,
                           double lat2) {
        double a, b, R;
        R = 6371; // 地球半径
        latitude = latitude * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = latitude - lat2;
        b = (longitude - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(
                Math.sqrt(sa2 * sa2 + Math.cos(latitude)
                        * Math.cos(lat2) * sb2 * sb2));
        return d * 1000;
    }
}
