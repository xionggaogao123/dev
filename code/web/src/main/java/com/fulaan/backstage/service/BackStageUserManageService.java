package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.RoleJurisdictionSettingDao;
import com.db.backstage.TeacherApproveDao;
import com.db.backstage.UserLogResultDao;
import com.db.controlphone.ControlVersionDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.RemarkDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.UserManageResultDTO;
import com.fulaan.cache.CacheHandler;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.controlphone.dto.ControlVersionDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.jiaschool.dto.SchoolCommunityDTO;
import com.fulaan.service.MemberService;
import com.pojo.app.SessionValue;
import com.pojo.backstage.RoleJurisdictionSettingEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.backstage.UserLogResultEntry;
import com.pojo.controlphone.ControlVersionEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.RemarkEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/30 09:36
 * @Description:
 */
@Service
public class BackStageUserManageService {

    @Autowired
    private MemberService memberService;

    private NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private UserDao userDao = new UserDao();

    //用户 跟 系统角色绑定
    private UserLogResultDao userLogResultDao = new UserLogResultDao();
    //角色相关信息
    private RoleJurisdictionSettingDao roleJurisdictionSettingDao = new RoleJurisdictionSettingDao();

    private ControlVersionDao controlVersionDao = new ControlVersionDao();

    //社群
    private MemberDao memberDao = new MemberDao();

    private CommunityDao communityDao =new CommunityDao();

//    private RemarkDao remarkDao = new RemarkDao();

    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();

    public JSONArray getUserRoleOption() {
        JSONArray jsonArray = new JSONArray();

        List<Integer> roleListInt = null;
        JSONObject jsonObject = null;
        List<NewVersionUserRoleEntry> roleEntries = null;
        //获取老师(teacherapprove 表 typ 为 2通过申请的 role 为0 或者没有role字段的（role为新增字段） 的） 及其 数量
        List<TeacherApproveEntry> teacherApproveEntries = teacherApproveDao.getTeacherByType();
        jsonObject = new JSONObject();
        jsonObject.put("value","老师"+"++"+teacherApproveEntries.size());
        jsonObject.put("name","老师"+"("+teacherApproveEntries.size()+"人)");
        jsonArray.add(jsonObject);

        //获取家长
        // （newversion_userrole 表 nr 为0 ） 及其 数量
        roleListInt = new ArrayList<Integer>();
        roleListInt.add(0);
        roleEntries = newVersionUserRoleDao.getUserByRoleList(roleListInt);
        jsonObject = new JSONObject();
        jsonObject.put("value","家长"+"++"+roleEntries.size());
        jsonObject.put("name","家长"+"("+roleEntries.size()+"人)");
        jsonArray.add(jsonObject);

        //获取学生（newversion_userrole 表 nr 为 1 学生未激活 2 学生激活） 及其 数量
        roleListInt = new ArrayList<Integer>();
        roleListInt.add(1);
        roleListInt.add(2);
        roleEntries = newVersionUserRoleDao.getUserByRoleList(roleListInt);
        jsonObject = new JSONObject();
        jsonObject.put("value","学生"+"++"+roleEntries.size());
        jsonObject.put("name","学生"+"("+roleEntries.size()+"人)");
        jsonArray.add(jsonObject);
        // 员工也会放在这个表里面
        // (teacherapprove 表 typ 为 2通过申请的 role 为1 （role为新增字段） 的） 及其 数量
        List<TeacherApproveEntry> staffApproveEntries = teacherApproveDao.getStaffByType();
        jsonObject = new JSONObject();
        jsonObject.put("value","员工"+"++"+staffApproveEntries.size());
        jsonObject.put("name","员工"+"("+staffApproveEntries.size()+"人)");
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    /**
     * 两种情况 分为input框输入用户信息查询 下拉框选择用户角色查询
     * @param map
     * @return
     */
    public List<UserManageResultDTO> getUserListByRole(Map map) {
        List<UserManageResultDTO> userManageResultDTOS = new ArrayList<UserManageResultDTO>();

        if (null != map.get("userInfo")) {
            /**
             * //input框输入查询 用户名 id 或手机号
             */
            //根据用户信息查询用户Id
            ObjectId userId = null;
            UserEntry userEntry = userDao.findByUserName(map.get("userInfo").toString());
            if (null != userEntry){
                //根据用户名查询到
                userId = userEntry.getID();
            }else {
                //根据用户名查询不到，进而根据用户手机号查询

                userEntry = userDao.findByMobile(map.get("userInfo").toString());
                if (null != userEntry) {
                    userId = userEntry.getID();
                }else {
                    //根据用户名，手机号都查询不到
                    if (!StringUtils.isNumeric(map.get("userInfo").toString())){
                        userEntry = userDao.findByUserId(new ObjectId(map.get("userInfo").toString()));
                    }

                    if (null != userEntry){
                        userId = userEntry.getID();
                    }else {//根据用户名，根据用户Id，手机号都查询不到
                        return userManageResultDTOS;
                    }
                }
            }
            /**
             * 到此处查到 用户信息 再根据用户信息 去查对应的角色信息
             */
            //查看是否为 学生 或者 家长
            UserManageResultDTO userManageResultDTO = null;
            NewVersionUserRoleEntry userRoleEntry = newVersionUserRoleDao.getEntry(userId);
            if (null != userRoleEntry){//用户在学生 或者家长中 有角色
                userManageResultDTO = new UserManageResultDTO();
                userManageResultDTO.setId(userRoleEntry.getID().toString());
                //获取当前用户所有的社群
                List<MemberEntry> entries2 = memberDao.getCommunityListByUid(userId);
                userManageResultDTO.setCommunityCount(entries2.size()+"");
                if (0 == userRoleEntry.getNewRole()){
                    userManageResultDTO.setUserRoleName("家长");
                    //家长，老师 员工 登录 或 未登录
                    String cacheUserKey= CacheHandler.getUserKey(userEntry.getID().toString());
                    if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                        SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                        if (null != sv && !sv.isEmpty()) {
                            userManageResultDTO.setLineStatus("登录");
                        }else {
                            userManageResultDTO.setLineStatus("未登录");
                        }
                    }
                }else {
                    userManageResultDTO.setUserRoleName("学生");
                    //学生 在线与否
                    ControlVersionDTO controlVersionDTO2 = getStudentVersion(userEntry.getID());
                    String ver2 = controlVersionDTO2.getVersion();
                    if(controlVersionDTO2==null){//记录不存在
//                            map.put("status",5);//未在线
                        userManageResultDTO.setLineStatus("离线");
                    }else{
                        if(ver2==null){//版本号不存在
//                                map.put("status",5);//未在线
                            userManageResultDTO.setLineStatus("离线");
                        }else{
                            if(ver2.equals("已退出") || ver2.equals("暂无数据")){
//                                    map.put("status",5);//未在线
                                userManageResultDTO.setLineStatus("离线");
                            }else{
                                //判断mqtt状态
                                if(controlVersionDTO2.getStatus()==0){//离线
//                                        map.put("status",4);//版本号不一致   离线
                                    userManageResultDTO.setLineStatus("离线");
                                }else{
//                                        map.put("status",3);//版本号不一致   在线
                                    userManageResultDTO.setLineStatus("在线");
                                }
                            }
                        }
                    }

                }
                userManageResultDTO.setUserId(userEntry.getID().toString());
                userManageResultDTO.setUserName(userEntry.getUserName());
                userManageResultDTO.setNickName(userEntry.getNickName());
                userManageResultDTO.setTelephone(userEntry.getMobileNumber());
                //家长学生无系统角色
                userManageResultDTO.setSysRoleName("");
//                    userManageResultDTO.setUserRoleName("");
                userManageResultDTOS.add(userManageResultDTO);
            }
            TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
            if (null != teacherApproveEntry){//用户在老师 或者员工中 有角色
                userManageResultDTO = new UserManageResultDTO();
                userManageResultDTO.setId(teacherApproveEntry.getID().toString());
                //获取当前用户所有的社群
                List<MemberEntry> entries2 = memberDao.getCommunityListByUid(userId);
                userManageResultDTO.setCommunityCount(entries2.size()+"");
                //家长，老师 员工 登录 或 未登录
                String cacheUserKey= CacheHandler.getUserKey(userEntry.getID().toString());
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                    SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                    if (null != sv && !sv.isEmpty()) {
                        userManageResultDTO.setLineStatus("登录");
                    }else {
                        userManageResultDTO.setLineStatus("未登录");
                    }
                }
                userManageResultDTO.setUserId(userEntry.getID().toString());
                userManageResultDTO.setUserName(userEntry.getUserName());
                userManageResultDTO.setNickName(userEntry.getNickName());
                userManageResultDTO.setTelephone(userEntry.getMobileNumber());
                //系统角色
                //通过绑定表 获取 userId 对应的 roleId
                UserLogResultEntry userLogResultEntry= userLogResultDao.getEntryByUserId(userEntry.getID());
                if (null != userLogResultEntry ){
                    RoleJurisdictionSettingEntry settingEntry = roleJurisdictionSettingDao.getEntryById(userLogResultEntry.getRoleId());
                    userManageResultDTO.setSysRoleName(settingEntry == null ? "" : settingEntry.getRoleName());
                }else {
                    userManageResultDTO.setSysRoleName("");
                }
                userManageResultDTO.setUserRoleName("大V老师");
                userManageResultDTOS.add(userManageResultDTO);
            }
        }else {
            /**
             * //下拉查询
             */
            UserManageResultDTO userManageResultDTO = null;

            //下拉选中老师 或者 员工
            if ("老师".equals(map.get("roleOption")) || "员工".equals(map.get("roleOption"))){
                List<TeacherApproveEntry> teacherApproveEntries = teacherApproveDao.getUserListByRole(map);
                for (TeacherApproveEntry teacherApproveEntry : teacherApproveEntries){
                    UserEntry userEntry = userDao.findByUserId(teacherApproveEntry.getUserId());
                    userManageResultDTO = new UserManageResultDTO();
                    userManageResultDTO.setId(teacherApproveEntry.getID().toString());
                    //获取当前用户所有的社群
                    List<MemberEntry> entries2 = memberDao.getCommunityListByUid(teacherApproveEntry.getUserId());
                    userManageResultDTO.setCommunityCount(entries2.size()+"");
                    //家长，老师 员工 登录 或 未登录
                    String cacheUserKey= CacheHandler.getUserKey(userEntry.getID().toString());
                    if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                        SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                        if (null != sv && !sv.isEmpty()) {
                            userManageResultDTO.setLineStatus("登录");
                        }else {
                            userManageResultDTO.setLineStatus("未登录");
                        }
                    }
                    userManageResultDTO.setUserId(userEntry.getID().toString());
                    userManageResultDTO.setUserName(userEntry.getUserName());
                    userManageResultDTO.setNickName(userEntry.getNickName());
                    userManageResultDTO.setTelephone(userEntry.getMobileNumber());
                    //系统角色
                    //通过绑定表 获取 userId 对应的 roleId
                    UserLogResultEntry userLogResultEntry= userLogResultDao.getEntryByUserId(userEntry.getID());
                    if (null != userLogResultEntry ){
                        RoleJurisdictionSettingEntry settingEntry = roleJurisdictionSettingDao.getEntryById(userLogResultEntry.getRoleId());
                        userManageResultDTO.setSysRoleName(settingEntry == null ? "" : settingEntry.getRoleName());
                    }else {
                        userManageResultDTO.setSysRoleName("");
                    }
                    userManageResultDTO.setUserRoleName("大V老师");
                    userManageResultDTOS.add(userManageResultDTO);
                }
            }else {
                List<Integer> roleListInt = null;
                List<NewVersionUserRoleEntry> roleEntries = null;
                //下拉选中家长 或者 学生
                if ("家长".equals(map.get("roleOption"))){
                    roleListInt = new ArrayList<Integer>();
                    roleListInt.add(0);
                }else {
                    roleListInt = new ArrayList<Integer>();
                    roleListInt.add(1);
                    roleListInt.add(2);
                }
                map.put("roleListInt",roleListInt);
                roleEntries = newVersionUserRoleDao.getUserByRolePageList(roleListInt,map);
                for (NewVersionUserRoleEntry userRoleEntry : roleEntries){
                    UserEntry userEntry = userDao.findByUserId(userRoleEntry.getUserId());
                    userManageResultDTO = new UserManageResultDTO();
                    userManageResultDTO.setId(userRoleEntry.getID().toString());
                    //获取当前用户所有的社群
                    List<MemberEntry> entries2 = memberDao.getCommunityListByUid(userRoleEntry.getUserId());
                    userManageResultDTO.setCommunityCount(entries2.size()+"");
                    if ("家长".equals(map.get("roleOption"))){
                        userManageResultDTO.setUserRoleName("家长");
                        //家长，老师 员工 登录 或 未登录
                        String cacheUserKey= CacheHandler.getUserKey(userEntry.getID().toString());
                        if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                            SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                            if (null != sv && !sv.isEmpty()) {
                                userManageResultDTO.setLineStatus("登录");
                            }else {
                                userManageResultDTO.setLineStatus("未登录");
                            }
                        }
                    }else {
                        userManageResultDTO.setUserRoleName("学生");
                        //学生 在线与否
                        ControlVersionDTO controlVersionDTO2 = getStudentVersion(userEntry.getID());
                        String ver2 = controlVersionDTO2.getVersion();
                        if(controlVersionDTO2==null){//记录不存在
//                            map.put("status",5);//未在线
                            userManageResultDTO.setLineStatus("离线");
                        }else{
                            if(ver2==null){//版本号不存在
//                                map.put("status",5);//未在线
                                userManageResultDTO.setLineStatus("离线");
                            }else{
                                if(ver2.equals("已退出") || ver2.equals("暂无数据")){
//                                    map.put("status",5);//未在线
                                    userManageResultDTO.setLineStatus("离线");
                                }else{
                                    //判断mqtt状态
                                    if(controlVersionDTO2.getStatus()==0){//离线
//                                        map.put("status",4);//版本号不一致   离线
                                        userManageResultDTO.setLineStatus("离线");
                                    }else{
//                                        map.put("status",3);//版本号不一致   在线
                                        userManageResultDTO.setLineStatus("在线");
                                    }
                                }
                            }
                        }

                    }
                    userManageResultDTO.setUserId(userEntry.getID().toString());
                    userManageResultDTO.setUserName(userEntry.getUserName());
                    userManageResultDTO.setNickName(userEntry.getNickName());
                    userManageResultDTO.setTelephone(userEntry.getMobileNumber());
                    //家长学生无系统角色
                    userManageResultDTO.setSysRoleName("");
//                    userManageResultDTO.setUserRoleName("");
                    userManageResultDTOS.add(userManageResultDTO);
                }
            }

        }
        return userManageResultDTOS;
    }

    //查询学生最新的收到版本
    public ControlVersionDTO getStudentVersion(ObjectId userId){
        ControlVersionEntry entry = controlVersionDao.getEntry(userId, 1);
        if(entry!=null){
            return new ControlVersionDTO(entry);
        }else{
            ControlVersionDTO dto = new ControlVersionDTO();
            dto.setVersion("暂无数据");
            return dto;
        }

    }

    /**
     * 获取用户创建的社群
     * @param map
     * @return
     */
    public Map<String,Object> getUserCreatedCommunity(Map map) {
        List<CommunityDTO> communityDTOS = new ArrayList<CommunityDTO>();
        Map<String,Object> result = communityDao.getUserCreatedCommunity(map);
        List<CommunityEntry> communityEntries = (ArrayList)result.get("communityEntryList");
//        List<CommunityEntry> communityEntries = communityDao.getUserCreatedCommunity(map);

        for (CommunityEntry communityEntry : communityEntries){
            CommunityDTO communityDTO = new CommunityDTO(communityEntry);
            //根据communityId查询关联学校信息
            List<ObjectId> communityIdL  = new ArrayList<ObjectId>();
            communityIdL.add(communityEntry.getID());
            List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(communityIdL);
            if (0 == schoolCommunityEntries.size()){//没关联学校
                communityDTO.setSchoolName("");
            }else {//有关联学校
                HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolCommunityEntries.get(0).getSchoolId());
                communityDTO.setSchoolName(homeSchoolEntry.getName());
            }

            int memberCount = memberService.getMemberCount(communityEntry.getGroupId());
            communityDTO.setMemberCount(memberCount);
            communityDTOS.add(communityDTO);
        }
        result.remove("communityEntryList");
        result.put("communitydtos",communityDTOS);
        return result;
    }

    /**
     * 获取用户加入的社群
     * @param map
     * @return
     */
    public Map<String,Object> getUserJoinCommunity(Map map) {
        List<CommunityDTO> communityDTOS = new ArrayList<CommunityDTO>();
        //获取当前用户所有的社群
        List<MemberEntry> memberEntries = memberDao.getCommunityListByUid(new ObjectId(map.get("userId").toString()));
        List<ObjectId> communityIdList = new ArrayList<ObjectId>();
        for (MemberEntry memberEntry : memberEntries) {
            if (!communityIdList.contains(memberEntry.getCommunityId())) {
                communityIdList.add(memberEntry.getCommunityId());
            }
        }
        Map<String,Object> result = communityDao.getUserJoinCommunityByIdList(communityIdList, map);
        List<CommunityEntry> communityEntries = (ArrayList)result.get("communityEntryList");
//        List<CommunityEntry> communityEntries = communityDao.getUserJoinCommunityByIdList(communityIdList, map);
        for (CommunityEntry communityEntry : communityEntries) {
            CommunityDTO communityDTO = new CommunityDTO(communityEntry);
            //根据communityId查询关联学校信息
            List<ObjectId> communityIdL  = new ArrayList<ObjectId>();
            communityIdL.add(communityEntry.getID());
            List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(communityIdL);
            if (0 == schoolCommunityEntries.size()){//没关联学校
                communityDTO.setSchoolName("");
            }else {//有关联学校
                HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolCommunityEntries.get(0).getSchoolId());
                communityDTO.setSchoolName(homeSchoolEntry.getName());
            }


            List<MemberEntry> entries = memberDao.getAllMembers(communityEntry.getGroupId());
            for (MemberEntry memberEntry : entries){
                if (new ObjectId(map.get("userId").toString()).equals(memberEntry.getUserId())){
                    communityDTO.setCurrentUserRole(new MemberDTO(memberEntry).getRoleStr());
                }
                if (2 == memberEntry.getRole()){
                    communityDTO.setOwerName(memberEntry.getUserName());
                }
            }
            communityDTOS.add(communityDTO);
        }
        result.remove("communityEntryList");
        result.put("communitydtos",communityDTOS);
        return result;
    }

    /**
     * 查询社群成员
     * @param map
     * @return
     */
    public Map<String,Object> getCommunityMember(Map map) {
        Map<String,Object> result = new HashMap<String, Object>();

        ObjectId groupId = new ObjectId(map.get("groupId").toString());
//        ObjectId userId = new ObjectId(map.get("userId").toString());
//        List<MemberEntry> entries = memberDao.getAllMembers(groupId);
        result = memberDao.getAllMembersForPage(map);
        List<MemberEntry> entries = (ArrayList)result.get("memberEntries");
        List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for (MemberEntry entry : entries) {
            userIds.add(entry.getUserId());
        }
//        Map<ObjectId,RemarkEntry> remarkEntryMap=new HashMap<ObjectId, RemarkEntry>();
//        if(null!=userId) {
//            remarkEntryMap=remarkDao.find(userId, userIds);
//        }
        for (MemberEntry entry : entries) {
            MemberDTO memberDTO=new MemberDTO(entry);
//            if(null!=remarkEntryMap){
//                RemarkEntry remarkEntry=remarkEntryMap.get(entry.getUserId());
//                if(null!=remarkEntry){
//                    memberDTO.setNickName(remarkEntry.getRemark());
//                }
//            }
            memberDTOs.add(memberDTO);
        }
        result.remove("memberEntries");
        result.put("memberdtos",memberDTOs);
        return result;
    }

    public String setCommunityRole(Map map) {
        String msg = "";
//        CommunityEntry communityEntry = communityDao.findBySearchId(map.get("searchParam").toString());
        CommunityEntry communityEntry = communityDao.findByObjectId(new ObjectId(map.get("communityId").toString()));
        //社群信息校验
        if (null == communityEntry){
//            communityEntry = communityDao.findByName(map.get("searchParam").toString());
//            if (null == communityEntry){
                return "无该社群，请确认输入的社群信息是否正确！";
//            }
        }

        //用户信息校验
        UserEntry userEntry = userDao.findByUserId(new ObjectId(map.get("userId").toString()));
        if(null == userEntry){
            return "当前用户信息有误！";
        }

//        Boolean flag = memberDao.isCommunityMember(communityEntry.getID(),userEntry.getID());
//        if(true == flag){
//            return "该用户在该社群中！";
//        }
//        MemberEntry memberEntry = new MemberEntry(
//                userEntry.getID(),
//                communityEntry.getGroupId(),
//                communityEntry.getID(),
//                userEntry.getNickName(),
//                userEntry.getAvatar(),
//                Integer.parseInt(map.get("role").toString()),
//                userEntry.getUserName()
//
//        );
//        memberEntry.setRole(0);
        memberDao.updateRoleById(map);
        msg = "操作成功！";
        return msg;
    }

    /**
     * 搜索社群信息
     * searchParam(包括searchId（社群查找Id）communityName)
     * @param map
     * @return
     */
    public CommunityDTO getCommunityInfo(Map map) {
        CommunityDTO communityDTO = null;
        CommunityEntry communityEntry = communityDao.findBySearchId(map.get("searchParam").toString());
        //社群信息校验
        if (null == communityEntry){
            communityEntry = communityDao.findByName(map.get("searchParam").toString());
            if (null != communityEntry){
                communityDTO = new CommunityDTO(communityEntry);
            }
        }else {
            communityDTO = new CommunityDTO(communityEntry);
        }
        return communityDTO;
    }

}
