package com.fulaan.playmate.service;

import com.db.playmate.FMateDao;
import com.db.user.UserDao;
import com.fulaan.playmate.dto.MateDTO;
import com.mongodb.BasicDBList;
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

    public List<MateDTO> findMates(double lon,double lat,List<String> tags,List<String> hobbys,int aged,int ons,int page,int pageSize) {
        List<MateDTO> mateDTOS = new ArrayList<MateDTO>();
        List<FMateEntry> fMateEntries = fMateDao.findByPage(lon,lat,tags,hobbys,aged,ons,page,pageSize);
        for(FMateEntry mateEntry : fMateEntries) {
            BasicDBList dbList = mateEntry.getLocation();
            Double distance = distance(lon,lat,(Double) dbList.get(0),(Double)dbList.get(1));
            MateDTO mateDTO = new MateDTO();
            UserEntry userEntry = userDao.findByObjectId(mateEntry.getUserId());
            mateDTO.setDistance(distance.longValue());
            mateDTO.setUserId(userEntry.getID().toString());
            mateDTO.setNickName(userEntry.getNickName());
            mateDTO.setUserName(userEntry.getUserName());
            mateDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            mateDTO.setTags(MongoUtils.convertToStrList(mateEntry.getTags()));
            mateDTO.setHobbys(MongoUtils.convertToStrList(mateEntry.getHobbys()));
            mateDTOS.add(mateDTO);
        }
        return mateDTOS;
    }

    /**
     * 计算地图上两点之间的距离
     * @param longitude
     * @param latitude
     * @param long2
     * @param lat2
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
        return d;
    }
}
