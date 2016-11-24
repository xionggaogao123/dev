package com.fulaan.service;

import org.springframework.stereotype.Service;

/**
 * Created by jerry on 2016/9/19.
 * 距离服务
 */
@Service
public class LocationService {

//    private static final Logger logger = Logger.getLogger(LocationService.class);
//
//    @Autowired
//    private LocationDao locationDao;
//    private UserDao userDao = new UserDao();
//
//    /**
//     * 根据经纬度获取用户
//     * 排序由近到远
//     *
//     * @param lat 经度
//     * @param lon 纬度
//     * @return List
//     */
//    public List<UserDTO> getUsers(double lat, double lon) {
//        Page page = new Page(1, 100); //取100个数据
//        PageModel<LocationEntry> pageModel = locationDao.locationEntries(page);
//        List<LocationEntry> list = pageModel.getResult();
//        List<UserLoc> userLocs = new ArrayList<UserLoc>();
//        for (LocationEntry location : list) {
//            UserLoc userLoc = new UserLoc();
//            userLoc.setUid(location.getUserId());
//            userLoc.setLat(location.getLat());
//            userLoc.setLon(location.getLon());
//            userLoc.setDistance(LocationUtil.distance(lon, lat, location.getLon(), location.getLat()));
//            userLoc.setTime(location.getTime());
//            userLocs.add(userLoc);
//        }
//        LocationUtil.sortByDistance(userLocs);
//        List<UserDTO> userDtos = new ArrayList<UserDTO>();
//        for (UserLoc loc : userLocs) {
//            UserEntry user = userDao.find(loc.getUid());
//            UserDTO dto = new UserDTO(user);
//            dto.setDistance((long) Math.floor(loc.getDistance()));
//            dto.setLogin(loc.getTime());
//            userDtos.add(dto);
//        }
//        return userDtos;
//    }
//
//    /**
//     * 获取指定的用户附近的好友
//     *
//     * @param uid 用户id
//     * @return List
//     */
//    public List<UserDTO> getUsers(ObjectId uid) {
//        Location location = getLocaton(uid);
//        return getUsers(location.getLat(), location.getLon());
//    }
//
//    private Location getLocaton(ObjectId uid) {
//        return locationDao.getLocation(uid);
//    }
}
