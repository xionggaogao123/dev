package com.fulaan.user.service;


import com.db.app.RegionDao;
import com.db.fcommunity.LoginLogDao;
import com.db.forum.FLevelDao;
import com.db.forum.FPostDao;
import com.db.school.*;
import com.db.user.UserDao;
import com.fulaan.base.BaseService;
import com.fulaan.cache.CacheHandler;
import com.fulaan.dto.UserDTO;
import com.fulaan.mall.service.EBusinessVoucherService;
import com.fulaan.pojo.FLoginLog;
import com.fulaan.pojo.Validate;
import com.fulaan.user.dao.ThirdLoginDao;
import com.fulaan.user.model.ThirdLoginEntry;
import com.fulaan.user.model.ThirdType;
import com.fulaan.util.check.FastCheck;
import com.fulaan.utils.KeyWordFilterUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.*;
import com.pojo.ebusiness.SortType;
import com.pojo.fcommunity.FLoginLogEntry;
import com.pojo.school.*;
import com.pojo.user.*;
import com.pojo.utils.LoginLog;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yan on 2015/2/27.
 */
@Service
public class UserService extends BaseService {

    private static final Logger logger = Logger.getLogger(UserService.class);

    private UserDao userDao = new UserDao();
    private ThirdLoginDao thirdLoginDao = new ThirdLoginDao();
    private SchoolDao schoolDao = new SchoolDao();
    private ClassDao classDao = new ClassDao();
    private InterestClassDao interestClassDao = new InterestClassDao();
    private RegionDao regionDao = new RegionDao();
    private FLevelDao fLevelDao = new FLevelDao();
    private FPostDao fPostDao = new FPostDao();
    private LoginLogDao loginLogDao = new LoginLogDao();

    @Autowired
    private EBusinessVoucherService voucherService;


    /**
     * 判断是否禁止访问
     *
     * @param userId
     * @return
     */
    public boolean isSilenced(String userId) {
        UserEntry userEntry = findById(new ObjectId(userId));
        int silencedStatus = userEntry.getSilencedStatus();
        if (silencedStatus == 0) {
            return false;
        } else {
            if (silencedStatus == 2) {
                long silencedTime = userEntry.getSilencedTime();
                long nowTime = System.currentTimeMillis();
                return silencedTime == 0 || silencedTime > nowTime;
            } else {
                return false;
            }
        }
    }

    public String filter(String content) {
        return org.apache.commons.lang.StringUtils.replaceEach(content, KeyWordFilterUtil.split_list.toArray(new String[]{}), KeyWordFilterUtil.replace_list.toArray(new String[]{}));
    }

    public String replaceSensitiveWord(String text) {
        //先添加词汇
        if (FastCheck.hash.size() == 0) {
            for (String item : KeyWordFilterUtil.split_list) {
                FastCheck.addWord(item);
            }
        }
        return FastCheck.replaceWith(text, '*');
    }

    public List<String> recordSensitiveWords(String text) {
        List<String> list = new ArrayList<String>();
        for (String item : KeyWordFilterUtil.split_list) {
            if (text.contains(item)) {
                list.add(item);
            }
        }
        return list;
    }


    public List<FLoginLog> getLoginLog(long start, long end) {
        List<FLoginLogEntry> loginLogEntries = loginLogDao.getLoginLog(start, end);
        List<FLoginLog> loginLogs = new ArrayList<FLoginLog>();
        for (FLoginLogEntry logEntry : loginLogEntries) {
            FLoginLog loginLog = new FLoginLog(logEntry);
            loginLogs.add(loginLog);
        }
        return loginLogs;
    }

    public void updateHuanXinTag(ObjectId uid) {
        userDao.updateHuanXin(uid);
    }

    /**
     * 判断是否禁止发言
     *
     * @param userId
     * @return
     */
    public boolean isUnSpeak(String userId) {
        UserEntry userEntry = findById(new ObjectId(userId));
        int silencedStatus = userEntry.getSilencedStatus();
        if (silencedStatus == 0) {
            return false;
        } else {
            if (silencedStatus == 1) {
                long silencedTime = userEntry.getSilencedTime();
                int day = userEntry.getSilencedTen();
                long nowTime = System.currentTimeMillis();
                return silencedTime == 0 || day > (nowTime / 1000 / (24 * 3600) - silencedTime / 1000 / (24 * 3600));
            } else {
                return false;
            }
        }
    }

    public Validate validatePhoneNumber(String phoneNumber, String code, String cacheKeyId) {
        Validate validate = new Validate();
        validate.setOk(false);
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
            validate.setMessage("验证码失效，请重新获取");
        }
        String[] cache = value.split(",");
        if (!cache[1].equals(phoneNumber)) {
            validate.setMessage("注册失败：手机号码与验证码不匹配");
        }

        if (cache[0].equals(code)) {
            validate.setOk(true);
        } else {
            validate.setMessage("短信验证码输入错误");
        }
        return validate;
    }


    /**
     * 通过用户id获取用户所在学校的注册地址id
     *
     * @param id
     * @return
     */
    public String findGeoIdByUserId(String id) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        ObjectId regionId = schoolEntry.getRegionId();
        return regionId.toString();
    }

    /**
     * 通过用户id获取用户信息
     *
     * @param id
     * @return
     */
    public UserDetailInfoDTO findUserInfoHasCityName(String id) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        ObjectId regionId = schoolEntry.getRegionId();
        RegionEntry regionEntry = regionDao.getRegionById(regionId);
        UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
        userInfoDTO.setCityName(regionEntry.getName());
        return userInfoDTO;
    }

    /*
    * 主键查询
    *
    * */
    public UserDetailInfoDTO getUserInfoById(String id) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        return userEntry == null ? null : new UserDetailInfoDTO(userEntry);
    }

    public UserDetailInfoDTO getUserInfoByUserName(String userName) {
        UserEntry userEntry = userDao.findByUserName(userName);
        return userEntry == null ? null : new UserDetailInfoDTO(userEntry);
    }

    /**
     * 查询用户信息
     *
     * @param uids
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByUserIds(List<String> uids) {
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (String sid : uids) {
            objectIdList.add(new ObjectId(sid));
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBs = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBs.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBs;
    }


    /**
     * 后台管理数据查询
     *
     * @param uids
     * @return
     */
    public int getForumInfoCount(List<ObjectId> uids) {
        return userDao.getForumInfoCount(uids);
    }


    /**
     * 后台批量逻辑删除用户数据
     */
    public void removeUserLogic(List<ObjectId> uids) {
        userDao.removeUserLogic(uids);
    }

    /**
     * 后台管理查询数据
     *
     * @param uids
     * @return
     */
    public List<UserForumDTO> findForumInfoByIds(List<ObjectId> uids, int orderType, int page, int pageSize) {
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        List<UserEntry> userEntryList = userDao.getUserEntryListByPage(uids, sort, (page - 1) * pageSize, pageSize, Constant.FIELDS);
        List<UserForumDTO> userForumDTOList = new ArrayList<UserForumDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserForumDTO userForumDTO = new UserForumDTO();
            if (StringUtils.isNotBlank(userEntry.getNickName())) {
                userForumDTO.setNickName(userEntry.getNickName());
            } else {
                userForumDTO.setNickName(userEntry.getUserName());
            }
            userForumDTO.setUserId(userEntry.getID().toString());
            userForumDTO.setSilencedStatus(userEntry.getSilencedStatus());
            userForumDTO.setSilencedTen(userEntry.getSilencedTen());
            userForumDTO.setSilencedReason(userEntry.getSilencedReason());
            userForumDTO.setExperience(userEntry.getForumExperience());
            long stars = fLevelDao.getStars(userEntry.getForumExperience());
            userForumDTO.setLevel("Lv." + stars);
            int postCount = fPostDao.getFPostEntriesCount("", "", -1, null, -1, -1, null, userEntry.getID(), -1, 0, 0);
            userForumDTO.setPostCount(postCount);
            userForumDTO.setTogether(userEntry.getID().toString() + ',' + stars + ',' + userEntry.getForumExperience());
            userForumDTO.setBan(userEntry.getID().toString() + ',' + userForumDTO.getNickName() + ',' + userEntry.getSilencedStatus() + ',' +
                    userEntry.getSilencedTen() + ',' + userEntry.getSilencedReason());
            userForumDTOList.add(userForumDTO);
        }
        return userForumDTOList;
    }

    /**
     * 查询用户信息
     *
     * @param uids
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByIds(List<ObjectId> uids) {
        List<UserEntry> userEntryList = userDao.getUserEntryList(uids, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBs = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBs.add(userInfoDTO4WB);
        }
        sortList(userInfoDTO4WBs);
        return userInfoDTO4WBs;
    }

    /**
     * 对list进行排序
     *
     * @param list
     */
    private void sortList(List<UserDetailInfoDTO> list) {
        Collections.sort(list, new Comparator<UserDetailInfoDTO>() {
            public int compare(UserDetailInfoDTO obj1, UserDetailInfoDTO obj2) {
                return Collator.getInstance(Locale.CHINESE).compare(obj1.getUserName(), obj2.getUserName());
            }
        });
    }


    /*
    *
    * 通过父ID 查询孩子信息
    *
    * */
    public UserDetailInfoDTO findStuInfoByParentId(String id) {
        UserEntry e = userDao.getUserEntryByParentId(new ObjectId(id));
        return e == null ? null : new UserDetailInfoDTO(e);
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    public UserDetailInfoDTO selectUserInfoHasClassName(String userId) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);
        UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
        //查找 地区名称
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(), Constant.FIELDS);
        ObjectId regionId = schoolEntry.getRegionId();
        RegionEntry regionEntry = regionDao.getRegionById(regionId);
        userInfoDTO4WB.setCityName(regionEntry.getName());
        //查找班级名称
        ClassEntry classEntry = classDao.getClassEntryByStuId(userEntry.getID(), new BasicDBObject("nm", 1));
        userInfoDTO4WB.setMainClassName(classEntry.getName());
        return userInfoDTO4WB;
    }

    /**
     * 赠送优惠券
     *
     * @param userId
     */
    public void giveVoucher(ObjectId userId) {
        String from = "2016-04-25";
        String to = "2016-06-01";

        try {
            Date fromDate = DateTimeUtils.stringToDate(from, DateTimeUtils.DATE_YYYY_MM_DD);
            Date toDate = DateTimeUtils.stringToDate(to, DateTimeUtils.DATE_YYYY_MM_DD);
            Date now = new Date();

            if (now.compareTo(fromDate) >= 0 && now.compareTo(toDate) <= 0) {
                //注册成功，赠送1张优惠券
                giveVouchers(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserEntry findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    /**
     * 根据用户邮箱
     *
     * @param email
     * @return
     */
    public UserEntry findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public UserEntry findById(ObjectId id) {
        return userDao.getUserEntry(id, Constant.FIELDS);
    }

    public UserEntry findByGenerateCode(String generateCode) {
        return userDao.getGenerateCodeEntry(generateCode);
    }

    /**
     * 根据用户ID查询，返回用户的map
     *
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap(Collection<ObjectId> ids, DBObject fields) {
        return userDao.getUserEntryMap(ids, fields);
    }

    /**
     * 根据用户名查询，返回用户的id
     *
     * @return
     */
    public List<ObjectId> getUserIdsByName(Collection<String> userNames) {
        return userDao.getUserIdsByUserName(userNames);
    }

    /**
     * 添加用户
     */
    public ObjectId addUser(UserEntry e) {
        return userDao.addUserEntry(e);
    }

    /**
     * 保存第三方登录的数据
     *
     * @param thirdLoginEntry
     * @return
     */
    public ObjectId saveThirdEntry(ThirdLoginEntry thirdLoginEntry) {
        return thirdLoginDao.addThidLoginEntry(thirdLoginEntry);
    }

    /**
     * 直接创建用户
     *
     * @param nickName
     * @param sex
     * @return
     */
    public UserEntry createUser(final String nickName, int sex) {

        UserEntry userEntry = findByUserName(nickName);
        String userName = nickName;
        if (userEntry == null) {
            userEntry = new UserEntry(userName, nickName, "*", "", sex);
        } else {
            String uuidName = new ObjectId().toString();
            userName = nickName + uuidName;
            userEntry = new UserEntry(userName, nickName, "*", "", sex);
        }

        addUser(userEntry);
        userEntry = findByUserName(userName);
        return userEntry;
    }

    /**
     * 得到一个学校的老师
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        return userDao.getTeacherEntryBySchoolId(schoolId, fields);
    }

    /**
     * 搜寻第三方登录用户
     *
     * @param openId  用户的OpenId
     * @param unionId 用户的unionId-仅微信
     * @param type    类型-1：微信 2：QQ
     * @return UserEntry
     */
    public UserEntry searchThirdEntry(String openId, String unionId, ThirdType type) {
        UserEntry userEntry = null;
        if (type.getCode() == ThirdType.WECHAT.getCode() && unionId != null) { //微信
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("unionid", unionId);
            map.put("type", ThirdType.WECHAT.getCode());
            logger.info(map + "--");
            userEntry = getThirdEntryByMap(map);
        } else if (type.getCode() == ThirdType.QQ.getCode() && openId != null) { //QQ
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("oid", openId);
            map.put("type", ThirdType.QQ.getCode());
            logger.info(map + "--");
            userEntry = getThirdEntryByMap(map);
        }
        return userEntry;
    }

    /**
     * 根据Map查询用户实体
     *
     * @param map 键值对
     * @return UserEntry
     */
    public UserEntry getThirdEntryByMap(Map<String, Object> map) {
        return thirdLoginDao.getEntryByMap(map);
    }

    /**
     * 根据Map查询第三方登录文档
     *
     * @param map 键值对
     * @return ThirdLoginEntry
     */
    public ThirdLoginEntry searchThirdLoginEntry(Map<String, Object> map) {
        return thirdLoginDao.getThirdLoginEntryByMap(map);
    }

    public String getRedirectUrl(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {


            logger.info("++++++++++++++" + cookie.getName());

            if (cookie.getName() != null && cookie.getName().equals(Constant.APP_SHARE)) {
                String cacheVale = cookie.getValue();
                SessionValue cacheSessionValue = CacheHandler.getSessionValue(cacheVale);
                String redirectUrl = cacheSessionValue.get("redirectUrl");
                logger.info(redirectUrl + "redirect====");
                if (redirectUrl != null) {
                    return "redirect:" + redirectUrl;
                }
            }
        }
        return null;
    }

    public void updateNickNameAndSexById(String studenrid, String stnickname, int sex) {
        userDao.updateNickNameAndSexById(new ObjectId(studenrid), stnickname, sex);
    }

    public void updateNickNameById(String userId, String stnickname) {
        userDao.updateNickNameById(new ObjectId(userId), stnickname);
    }


    public List<UserDetailInfoDTO> findUserBySchoolId2(String schoolId) {
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(new ObjectId(schoolId), new BasicDBObject(Constant.ID, 1).append("chatid", 1).append("avt", 1).append("nnm", 1).append("nm", 1).append("r", 1));
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry, 1);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }

    /**
     * 根据学校id查询学校全部人员信息
     *
     * @param schoolID
     * @return
     */
    public List<UserDetailInfoDTO> getUserInfoBySchoolid(ObjectId schoolID) {
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolId(schoolID, Constant.FIELDS);
        List<UserDetailInfoDTO> userlist = new ArrayList<UserDetailInfoDTO>();
        if (userEntryList != null && userEntryList.size() != 0) {
            for (UserEntry user : userEntryList) {
                if (UserRole.isHeadmaster(user.getRole())) {
                    userlist.add(new UserDetailInfoDTO(user));
                }
            }
        }
        return userlist;
    }

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatar
     * @throws Exception
     */
    public void updateAvatar(String userId, String avatar) throws Exception {
        if (null == userId || !ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        FieldValuePair fvp = new FieldValuePair("avt", avatar);
        userDao.update(new ObjectId(userId), fvp);
    }

    /**
     * 取得用户密码
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public String getUserPassword(String userId) throws Exception {
        if (null == userId || !ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        return userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS).getPassword();

    }

    /**
     * 修改用户密码
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public void updatePassword(String userId, String password) throws Exception {
        if (null == userId || !ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        FieldValuePair fvp = new FieldValuePair("pw", password);
        userDao.update(new ObjectId(userId), fvp);
    }

    /**
     * 得到用户所在的班级列表
     *
     * @param userId
     * @return
     */
    public List<ClassInfoDTO> getClassDTOList(ObjectId userId, int userRole) {
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        if (UserRole.isStudent(userRole) || UserRole.isParent(userRole)) {

            if (UserRole.isParent(userRole)) {
                UserDetailInfoDTO stuinfo = findStuInfoByParentId(userId.toString());
                if (stuinfo != null) {
                    userId = new ObjectId(stuinfo.getId());
                } else {
                    userId = null;
                }
            }

            if (userId != null) {
                List<ClassEntry> classEntryList = classDao.getClassEntryListByStudentId(userId,
                        Constant.FIELDS);
                for (ClassEntry classEntry : classEntryList) {

                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassName(classEntry.getName());
                    classInfoDTO.setId(classEntry.getID().toString());
                    classInfoDTOList.add(classInfoDTO);
                }

                //兴趣班
                List<InterestClassEntry> interestClassEntryList = interestClassDao.findClassInfoByStuId(userId);
                for (InterestClassEntry classEntry : interestClassEntryList) {

                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassName(classEntry.getClassName());
                    classInfoDTO.setId(classEntry.getID().toString());
                    classInfoDTOList.add(classInfoDTO);
                }
            }


        } else if (UserRole.isHeadmaster(userRole)) {
            String schoolId = getUserInfoById(userId.toString()).getSchoolID();
            List<ClassEntry> classEntryList = classDao.findClassInfoBySchoolId(new ObjectId(schoolId), Constant.FIELDS);
            //todo : 兴趣班先不加
            for (ClassEntry classEntry : classEntryList) {

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }


            //兴趣班
            List<InterestClassEntry> interestClassEntryList = interestClassDao.findClassBySchoolId(new ObjectId(schoolId), -1, null, null);
            for (InterestClassEntry classEntry : interestClassEntryList) {

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getClassName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }
        } else if (UserRole.isTeacher(userRole)) {
            List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(userId, Constant.FIELDS);
            //todo : 兴趣班先不加
            for (ClassEntry classEntry : classEntryList) {

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }


            //兴趣班
            List<InterestClassEntry> interestClassEntryList = interestClassDao.findClassInfoByTeacherId(userId, -1);
            for (InterestClassEntry classEntry : interestClassEntryList) {

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getClassName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }


        }
        return classInfoDTOList;
    }

    /**
     * 更新微校园时间
     *
     * @param id
     */
    public void updateSchoolHomeDate(ObjectId id) {
        userDao.updSchoolHomeDate(id);
    }

    /**
     * 更新微家园时间
     *
     * @param id
     */
    public void updateFamilyHomeDate(ObjectId id) {
        userDao.updFamilyHomeDate(id);
    }

    /**
     * 更新最后登录时间
     *
     * @param ui
     * @throws IllegalParamException
     */
    public void updateLastActiveDate(ObjectId ui) throws IllegalParamException {
        userDao.update(ui, "lad", System.currentTimeMillis(), true);
    }


    /**
     * 更新用户字段
     *
     * @param userId
     * @param field
     * @param value
     * @throws IllegalParamException
     */
    public void update(ObjectId userId, String field, Object value) throws IllegalParamException {
        userDao.update(userId, field, value, false);
    }

    /*
   * 检查用户名(昵称名)是否重复
   *
   * */
    public boolean existUserInfo(String userName, String userId, String nickName) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);
        boolean k = userDao.existUserInfo(userName, nickName);
        if (k) {
            if (nickName.equals(userEntry.getNickName())) {
                return false;
            }
        }
        return k;
    }

    /**
     * 按发帖总数排序查询用户
     *
     * @return
     */
    public List<UserInfoDTO> getUserListByPostCount(int pageSize, String flag) {
        List<UserEntry> userEntryList = userDao.getUserListByPostCount(pageSize, flag);
        List<UserInfoDTO> dtolist = new ArrayList<UserInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserInfoDTO userInfoDTO = new UserInfoDTO(userEntry);
            if (null == userInfoDTO.getNickName() || "".equals(userInfoDTO.getNickName())) {
                userInfoDTO.setNickName(userInfoDTO.getName());
            }
            if (null != userInfoDTO.getAvt() && !"".equals(userInfoDTO.getAvt())) {
                userInfoDTO.setAvt(AvatarUtils.getAvatar(userInfoDTO.getAvt(), AvatarType.MIN_AVATAR.getType()));
            } else {
                userInfoDTO.setAvt(null);
            }
            dtolist.add(userInfoDTO);
        }
        return dtolist;
    }

    /**
     * k6kt增加经验值
     *
     * @param name
     */
    public void updateExp(String name) {
        UserEntry user = userDao.getUserEntryByName(name);
        userDao.updateExperenceValue(user.getID());
    }


    /**
     * 增加帖子总数
     *
     * @param userId
     */
    public void updatePostCount(ObjectId userId) {
        userDao.updatePostCountValue(userId);
    }

    /**
     * 更新论坛积分
     *
     * @param userId
     * @param score
     */
    public void updateForumScore(ObjectId userId, long score) {
        userDao.updateForumScoreValue(userId, score);
    }

    /**
     * 更新论坛积分
     *
     * @param userId
     * @param score
     */
    public void updateForum(ObjectId userId, long score, long forumExperience) {
        userDao.updateForum(userId, score, forumExperience);
    }

    /**
     * 更新论坛经验值
     *
     * @param userId
     * @param exp
     */
    public void updateForumExperience(ObjectId userId, long exp) {
        userDao.updateForumExperience(userId, exp);
    }

    /**
     * 设置论坛积分
     *
     * @param userId
     * @param exp
     */
    public void setForumExperience(ObjectId userId, long exp) {
        userDao.setForumExperience(userId, exp);
    }

    /**
     * 更新上次访问ip
     *
     * @param userId
     * @param interviewIP
     */
    public void updateInterviewIPValue(ObjectId userId, String interviewIP) {
        userDao.updateInterviewIPValue(userId, interviewIP);
    }

    /**
     * 更新上次活动时间
     *
     * @param userId
     */
    public void updateInterviewTimeValue(ObjectId userId) {
        userDao.updateInterviewTimeValue(userId);
    }

    /**
     * 更新上次退出时间
     *
     * @param userId
     */
    public void updateQuitTimeValue(ObjectId userId) {
        userDao.updateQuitTimeValue(userId);
    }

    /**
     * 更新统计在线时间
     *
     * @param userId
     */
    public void updateStatisticTimeValue(ObjectId userId) {
        UserEntry userEntry = userDao.getUserEntry(userId, Constant.FIELDS);
        long nowTime = System.currentTimeMillis();
        long time = (nowTime - userEntry.getInterviewTime()) / (1000 * 60 * 60);
        userDao.updateStatisticTimeValue(userId, time);
    }

    public ObjectId randomAnId() {
        List<ObjectId> userIdList = userDao.getUserForEBusiness(new ObjectId("55934c14f6f28b7261c19c62"), Constant.FIELDS);
        Random random = new Random();
        int j = random.nextInt(400);
        return userIdList.get(j);
    }


    /**
     * 新注册商城用户赠送购物券
     */
    private void giveVouchers(ObjectId userId) {
        voucherService.addEVoucher(userId, 1000);
        voucherService.addEVoucher(userId, 1000);
    }

    /**
     * 学生爸爸
     */
    public String getFather(ObjectId stuId) {
        UserEntry student = userDao.getUserEntry(stuId, Constant.FIELDS);
        List<ObjectId> parents = student.getConnectIds();
        if (parents.size() == 2) {//新数据
            return parents.get(0).toString();
        }
        return parents.get(1).toString();
    }

    /**
     * 学生妈妈
     */
    public String getMother(ObjectId stuId) {
        UserEntry student = userDao.getUserEntry(stuId, Constant.FIELDS);
        List<ObjectId> parents = student.getConnectIds();
        if (parents.size() == 2) {//新数据
            return parents.get(1).toString();
        }
        return parents.get(2).toString();
    }

    public UserEntry findByUserName(String name) {
        return userDao.findByUserName(name);
    }


    public UserEntry findByPersonalID(String personalId) {
        return userDao.findByPersonalID(personalId);
    }


    public UserDTO findByRegular(String regular) {
        UserEntry entry;
        entry = userDao.findByUserName(regular);
        if (entry != null) {
            return new UserDTO(entry);
        }
        entry = userDao.findByMobile(regular);
        if (entry != null) {
            return new UserDTO(entry);
        }
        entry = userDao.findByEmail(regular);
        if (entry != null) {
            return new UserDTO(entry);
        }
        return null;
    }


    public boolean updateUserRole(ObjectId uid, UserRole role) {
        return userDao.updateUserPermission(uid, role);
    }

    public UserEntry getUserEntryByAccount(String account) {

        Pattern emailPattern = Pattern.compile("^.+@.+\\..+$");
        Pattern phonePattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        Pattern personalIdPattern = Pattern.compile("^[\\d]{10}");
        Matcher emailMatcher = emailPattern.matcher(account);
        Matcher phoneMatcher = phonePattern.matcher(account);
        Matcher personalIdMatcher = personalIdPattern.matcher(account);
        UserEntry user;

        user = userDao.findByUserName(account);
        if (user != null) {
            return user;
        } else if (emailMatcher.matches()) {
            user = userDao.findByEmail(account);
        } else if (phoneMatcher.matches()) {
            user = userDao.findByMobile(account);
        } else if (personalIdMatcher.matches()) {
            user = userDao.findByPersonalID(account);
        }
        return user;
    }

    public UserEntry getUserEntry(String name) {
        UserEntry e = findByUserName(name);

        if (null == e && ValidationUtils.isRequestModile(name)) {
            e = findByMobile(name);
        }
        if (null == e && ValidationUtils.isEmail(name)) {
            e = findByEmail(name);
        }

        return e;
    }

    /**
     * 密码验证
     *
     * @param password
     * @param user
     * @return
     */
    public boolean isValidPassword(String password, UserEntry user) {

        if (!ValidationUtils.isRequestPassword(password) || (!user.getPassword().equalsIgnoreCase(MD5Utils.getMD5String(password)) && !user.getPassword().equalsIgnoreCase(password))) {

            if (!ValidationUtils.isRequestPassword(password)) {
                return false;
            }
            if (!user.getPassword().equalsIgnoreCase(MD5Utils.getMD5String(password)) && !user.getPassword().equalsIgnoreCase(password)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否是有效用户
     *
     * @param user
     * @return
     */
    public boolean isValidUser(UserEntry user) {

        if (Constant.ONE == user.getUserType()) //有效时间用户
        {
            long validBeginTime = user.getValidBeginTime();
            long validTime = user.getValidTime();
            if (0L == validBeginTime) //第一次登陆
            {
                try {
                    update(user.getID(), "vabt", System.currentTimeMillis());
                } catch (IllegalParamException e1) {
                    e1.printStackTrace();
                }
            } else {
                if (System.currentTimeMillis() > validBeginTime + validTime * 1000) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addScore(ObjectId uid, long score) {
        userDao.updateScoreValue(uid, score);
    }

    public void minusScore(ObjectId uid, long score) {
        userDao.updateScoreValue(uid, -score);
    }

    public long score(ObjectId uid) {
        UserEntry entry = findById(uid);
        return entry.getForumScore();
    }

    public boolean isOpenIdBindQQ(String openId) {
        return thirdLoginDao.isOpenIdBindQQ(openId);
    }

    public boolean isUnionIdBindWechat(String unionId) {
        return thirdLoginDao.isUnionIdBindWechat(unionId);
    }

    /**
     * 获取SessionValue
     *
     * @param e
     * @param response
     * @param request
     * @return
     */
    public SessionValue setCookieValue(String ip, UserEntry e, HttpServletResponse response, HttpServletRequest request) {

        SessionValue value = getSessionValue(e);
        //保存generateCode
        value.setPackageCode(e.getGenerateUserCode());
        //放入缓存
        ObjectId cacheUserKey = new ObjectId();
        String ipKey = CacheHandler.getKeyString(CacheHandler.CACHE_USER_KEY_IP, cacheUserKey.toString());
        CacheHandler.cache(ipKey, ip, Constant.SECONDS_IN_DAY);
        //ck_key
        CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
        //s_key
        CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
        //处理cookie
        Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY, cacheUserKey.toString());
        userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
        userKeycookie.setPath(Constant.BASE_PATH);
        response.addCookie(userKeycookie);
        try {
            Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY, URLEncoder.encode(e.getUserName(), Constant.UTF_8));
            nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
            nameCookie.setPath(Constant.BASE_PATH);
            response.addCookie(nameCookie);
        } catch (UnsupportedEncodingException e1) {
            logger.error("", e1);
        }
        loginLog(request, e);
        return value;
    }

    private void loginLog(HttpServletRequest request, UserEntry e) {
        String ip = getIP(request);
        String client = request.getHeader("User-Agent");
        Platform pf;
        if (client.contains("iOS")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")) {
            pf = Platform.Android;
        } else {
            pf = Platform.PC;
        }
        try {
            LoginLog loginLog = new LoginLog();
            loginLog.setIpAddr(ip + e.getUserName());
            loginLog.setPlatform(pf.getName());
            loginLog.setUserId(e.getID().toString());
            loginLog.setUserName(e.getUserName());
            logger.info(loginLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SessionValue getSessionValue(UserEntry e) {
        //处理SessionValue
        SessionValue value = new SessionValue();
        value.setId(e.getID().toString());
        value.setUserName(e.getUserName());
        value.setRealName(e.getNickName());
        value.setAvatar(e.getAvatar());
        value.setK6kt(e.getK6KT());
        return value;
    }

    public void addUserPhone(ObjectId uid, String phone) {
        userDao.updateUserMobile(uid, phone);
    }

    public void updateUserEmailStatusById(ObjectId id) {
        userDao.updateUserEmailStatusById(id);
    }

    public List<UserEntry> getUserByList(List<ObjectId> userIds) {
        return userDao.getUserEntryList(userIds, Constant.FIELDS);
    }

    public List<UserEntry> getUserList(String field, String regularName, int page, int pageSize) {
        return userDao.getEntriesByUserName(field, regularName, page, pageSize);
    }

    public int countUserList(String field, String userName) {
        return userDao.countEntriesByUserName(field, userName);
    }


    public void pushUserTag(ObjectId userId, int code, String tag) {
        if (userDao.tagIsExist(userId, code)) {
            return;
        }
        UserEntry.UserTagEntry userTagEntry = new UserEntry.UserTagEntry(code, tag);
        userDao.pushUserTag(userId, userTagEntry);
    }

    public void pushUserTags(ObjectId userId, List<UserTag> tags) {
        List<UserEntry.UserTagEntry> tagEntries = new ArrayList<UserEntry.UserTagEntry>();
        for (UserTag tag : tags) {
            tagEntries.add(new UserEntry.UserTagEntry(tag.getCode(), tag.getTag()));
        }
        userDao.pushUserTags(userId, tagEntries);
    }

    public void pullUserTag(ObjectId userId, int code) {
        userDao.pullUserTag(userId, code);
    }


    public List<UserEntry> getInfoByName(String field, String userName) {
        return userDao.findEntryByName(field, userName);
    }

    public UserEntry getUserInfoEntry(String regular) {
        return userDao.getJudgeByRegular(regular);
    }

    public Object checkUserNameExist(String userName) {
        return userDao.findByUserName(userName) != null;
    }

    public void resetPassword(ObjectId userId, String password) {
        userDao.resetPwd(userId, password);
    }

    public void updateSexById(ObjectId userId, int sex) {
        userDao.updateSexById(userId, sex);
    }

    public void updateUserEmail(ObjectId userId, String email) {
        userDao.updateEmailById(userId, email);
    }

    public boolean isBindQQ(ObjectId userId) {
        return thirdLoginDao.isBindQQ(userId);
    }

    public boolean isBindWechat(ObjectId userId) {
        return thirdLoginDao.isBindWechat(userId);
    }

    public void updateUserPhone(ObjectId userId, String mobile) {
        userDao.updateUserMobile(userId, mobile);
    }

    public void clearUserPhone(String phone) {
        userDao.clearUserMobile(phone);
    }

    public void removeThirdBind(ObjectId userId, ThirdType thirdType) {
        thirdLoginDao.removeThirdBind(userId, thirdType);
    }

    public void addEntry(UserEntry userEntry) {
        userDao.addEntry(userEntry);
    }
}
