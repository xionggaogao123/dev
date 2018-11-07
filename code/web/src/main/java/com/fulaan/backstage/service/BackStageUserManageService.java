package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.RoleJurisdictionSettingDao;
import com.db.backstage.TeacherApproveDao;
import com.db.backstage.UserLogResultDao;
import com.db.controlphone.ControlShareDao;
import com.db.controlphone.ControlVersionDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.fcommunity.RemarkDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.UserManageChildrenDTO;
import com.fulaan.backstage.dto.UserManageParentDTO;
import com.fulaan.backstage.dto.UserManageResultDTO;
import com.fulaan.cache.CacheHandler;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.controlphone.dto.ControlVersionDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.jiaschool.dto.SchoolCommunityDTO;
import com.fulaan.newVersionBind.dto.NewVersionBindRelationDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.backstage.RoleJurisdictionSettingEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.backstage.UserLogResultEntry;
import com.pojo.controlphone.ControlShareEntry;
import com.pojo.controlphone.ControlVersionEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.fcommunity.RemarkEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.RespObj;
import com.sys.utils.StringUtil;
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

    @Autowired
    private CommunityService communityService;

    @Autowired
    private NewVersionBindService newVersionBindService;

    @Autowired
    private UserService userService;

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

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private NewVersionBindRelationDao newVersionBindRelationDao=new NewVersionBindRelationDao();

    private ControlShareDao controlShareDao = new ControlShareDao();

    public JSONArray getUserRoleOption() {
        JSONArray jsonArray = new JSONArray();

        List<Integer> roleListInt = null;
        JSONObject jsonObject = null;
        List<NewVersionUserRoleEntry> roleEntries = null;
        //获取老师(teacherapprove 表 typ 为 2通过申请的 role 为0 或者没有role字段的（role为新增字段） 的） 及其 数量
        List<TeacherApproveEntry> teacherApproveEntries = teacherApproveDao.getTeacherByType();
        jsonObject = new JSONObject();
        jsonObject.put("value","老师");
        jsonObject.put("name","老师"+"("+teacherApproveEntries.size()+"人)");
        jsonArray.add(jsonObject);

        //获取家长
        // （newversion_userrole 表 nr 为0 ） 及其 数量
        roleListInt = new ArrayList<Integer>();
        roleListInt.add(0);
        roleEntries = newVersionUserRoleDao.getUserByRoleList(roleListInt);
        jsonObject = new JSONObject();
        jsonObject.put("value","家长");
        jsonObject.put("name","家长"+"("+(roleEntries.size()-teacherApproveEntries.size())+"人)");
        jsonArray.add(jsonObject);

        //获取学生（newversion_userrole 表 nr 为 1 学生未激活 2 学生激活） 及其 数量
        roleListInt = new ArrayList<Integer>();
        roleListInt.add(1);
        roleListInt.add(2);
        roleEntries = newVersionUserRoleDao.getUserByRoleList(roleListInt);
        jsonObject = new JSONObject();
        jsonObject.put("value","学生");
        jsonObject.put("name","学生"+"("+roleEntries.size()+"人)");
        jsonArray.add(jsonObject);
        // 员工也会放在这个表里面
        // (teacherapprove 表 typ 为 2通过申请的 role 为1 （role为新增字段） 的） 及其 数量
        List<TeacherApproveEntry> staffApproveEntries = teacherApproveDao.getStaffByType();
        jsonObject = new JSONObject();
        jsonObject.put("value","员工");
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
            //根据用户信息（用户Id，用户手机号查询，用户名）查询
            ObjectId userId = null;
            UserEntry userEntry = userDao.findByUserNameOrMobileOrJiaId(map.get("userInfo").toString());
            if (null != userEntry){
                //根据用户名查询到
                userId = userEntry.getID();
            }else {
                return userManageResultDTOS;
            }
            /**
             * 到此处查到 用户信息 再根据用户信息 去查对应的角色信息
             */
            UserManageResultDTO userManageResultDTO = null;
            TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getTeacherEntry(userId);
            if (null != teacherApproveEntry){
                //用户在老师 或者员工中 有角色
                userManageResultDTO = new UserManageResultDTO();
                userManageResultDTO.setId(teacherApproveEntry.getID().toString());
                //超简洁获取用户的社团 listMineCommunityId
                List<ObjectId> listMineCommunityId = communityService.getCommunitys3(userId,-1,0);
                //获取当前用户所有的社群（范围存在于 listMineCommunityId）
                List<MemberEntry> entries2 = memberDao.getCommunityListByUid(userId,listMineCommunityId);
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
                userManageResultDTO.setJiaId(userEntry.getGenerateUserCode());
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
                return userManageResultDTOS;//是老师就不再去看是不是家长
            }

            //查看是否为 学生 或者 家长
            NewVersionUserRoleEntry userRoleEntry = newVersionUserRoleDao.getEntry(userId);
            if (null != userRoleEntry){
                //用户在学生 或者家长中 有角色
                userManageResultDTO = new UserManageResultDTO();
                userManageResultDTO.setId(userRoleEntry.getID().toString());
                //超简洁获取用户的社团 listMineCommunityId
                List<ObjectId> listMineCommunityId = communityService.getCommunitys3(userId,-1,0);
                //获取当前用户所有的社群（范围存在于 listMineCommunityId）
                List<MemberEntry> entries2 = memberDao.getCommunityListByUid(userId,listMineCommunityId);
                userManageResultDTO.setCommunityCount(entries2.size()+"");
                if (0 == userRoleEntry.getNewRole()){
                    userManageResultDTO.setUserRoleName("家长");
                    /**
                     * 带出孩子信息
                     */
                    List<UserManageChildrenDTO> userManageChildrenDTOS = getChildrenByParentId(userRoleEntry == null ? null : userRoleEntry.getUserId());
                    userManageResultDTO.setChildrenDTOList(userManageChildrenDTOS);
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
                    /**
                     * 带出绑定父母的信息
                     */
                    List<UserManageParentDTO> userManageParentDTOList = getParentByChildrenId(userEntry == null ? null : userEntry.getID());
                    userManageResultDTO.setParentDTOList(userManageParentDTOList);
                    //父母加入的社群 作为孩子加入或者即将加入的
                    int count = 0;
                    for (UserManageParentDTO dto : userManageParentDTOList){
                        count += dto.getCommunityCount();
                    }
                    userManageResultDTO.setCommunityCount(count + "");
                    //学生 在线与否
                    ControlVersionDTO controlVersionDTO2 = getStudentVersion(userEntry == null ? null : userEntry.getID());
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
                userManageResultDTO.setJiaId(userEntry.getGenerateUserCode());
                userManageResultDTO.setUserName(userEntry.getUserName());
                userManageResultDTO.setNickName(userEntry.getNickName());
                userManageResultDTO.setTelephone(userEntry.getMobileNumber());
                //家长学生无系统角色
                userManageResultDTO.setSysRoleName("");
//                    userManageResultDTO.setUserRoleName("");
                userManageResultDTOS.add(userManageResultDTO);
            }

        }else {
            /**
             * //下拉查询
             */
            userManageResultDTOS = getRoleOptionData(map);


        }
        return userManageResultDTOS;
    }

    /**
     * 下拉查询
     */
    private List<UserManageResultDTO> getRoleOptionData(Map map) {
        List<UserManageResultDTO> userManageResultDTOS = new ArrayList<UserManageResultDTO>();
        UserManageResultDTO userManageResultDTO = null;
        //下拉选中老师 或者 员工
        if ("老师".equals(map.get("roleOption")) || "员工".equals(map.get("roleOption"))){
            List<TeacherApproveEntry> teacherApproveEntries = teacherApproveDao.getUserListByRole(map);
            List<ObjectId> userIds= new ArrayList<ObjectId>();
            for (TeacherApproveEntry teacherApproveEntry : teacherApproveEntries){
                userIds.add(teacherApproveEntry.getUserId());
            }

            //超简洁获取用户的社团 listMineCommunityId
            // 按userId分组 返回Map<userId,List<communityId>>
            Map<ObjectId,List<ObjectId>> mineUserIdCommunityIdList = communityService.getCommunitys3GroupByUserId(userIds);
            //获取当前用户所有的社群（范围存在于 listMineCommunityId）
            // 按userId分组 返回Map<userId,List<MemberEntry>>
            Map<ObjectId,List<MemberEntry>> userIdMemberEntries = memberDao.getMemberEntriesGroupByuserId(userIds,mineUserIdCommunityIdList);
            //通过绑定表 获取 userId 对应的 roleId
            // 按userId分组 返回Map<userId,List<userLogResultEntryRoleId>>
            Map<ObjectId,List<ObjectId>> userLogResultEntryRoleIdsMap = userLogResultDao.getEntriesGroupByUserId(userIds);
            //sysRoleNameList 按userId分组 返回Map<userId,List<sysRoleName>>
            Map<ObjectId,List<String>> userIdSysRoleNameList = roleJurisdictionSettingDao.getSysRoleNameListGroupByUserId(userIds,userLogResultEntryRoleIdsMap);
            //获取用户基本信息
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
            for (TeacherApproveEntry teacherApproveEntry : teacherApproveEntries){
//                UserEntry userEntry = userDao.findByUserId(teacherApproveEntry.getUserId());
                UserEntry userEntry = userEntryMap.get(teacherApproveEntry.getUserId());
                userManageResultDTO = new UserManageResultDTO();
                userManageResultDTO.setId(teacherApproveEntry.getID().toString());
//                //超简洁获取用户的社团 listMineCommunityId
//                List<ObjectId> listMineCommunityId = communityService.getCommunitys3(teacherApproveEntry.getUserId(),-1,0);
//                //获取当前用户所有的社群（范围存在于 listMineCommunityId）
//                List<MemberEntry> entries2 = memberDao.getCommunityListByUid(teacherApproveEntry.getUserId(),listMineCommunityId);
//                userManageResultDTO.setCommunityCount(entries2.size()+"");
                userManageResultDTO.setCommunityCount(userIdMemberEntries.get(teacherApproveEntry.getUserId()).size()+"");
                //家长，老师 员工 登录 或 未登录
                String cacheUserKey= CacheHandler.getUserKey(userEntry==null?"":userEntry.getID().toString());
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                    SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                    if (null != sv && !sv.isEmpty()) {
                        userManageResultDTO.setLineStatus("登录");
                    }else {
                        userManageResultDTO.setLineStatus("未登录");
                    }
                }
                userManageResultDTO.setUserId(userEntry == null ? "" : userEntry.getID().toString());
                userManageResultDTO.setJiaId(userEntry == null ? "" : userEntry.getGenerateUserCode());
                userManageResultDTO.setUserName(userEntry == null ? "" : userEntry.getUserName());
                userManageResultDTO.setNickName(userEntry == null ? "" : userEntry.getNickName());
                userManageResultDTO.setTelephone(userEntry == null ? "" : userEntry.getMobileNumber());
                //系统角色
                //通过绑定表 获取 userId 对应的 roleId
//                UserLogResultEntry userLogResultEntry= userLogResultDao.getEntryByUserId(userEntry.getID());
//                if (null != userLogResultEntry ){
//                    RoleJurisdictionSettingEntry settingEntry = roleJurisdictionSettingDao.getEntryById(userLogResultEntry.getRoleId());
//                    userManageResultDTO.setSysRoleName(settingEntry == null ? "" : settingEntry.getRoleName());
//                }else {
//                    userManageResultDTO.setSysRoleName("");
//                }
                if (userEntry != null && userIdSysRoleNameList.get(userEntry.getID())!= null && userIdSysRoleNameList.get(userEntry.getID()).size()>0){
                    String roleNameListStr = "";
                    for (String roleName : userIdSysRoleNameList.get(userEntry.getID())){
                        roleNameListStr+=roleName+"   ";
                    }
                    userManageResultDTO.setSysRoleName(roleNameListStr);
                }else {
                    userManageResultDTO.setSysRoleName("");
                }
                userManageResultDTO.setUserRoleName("大V老师");
                userManageResultDTOS.add(userManageResultDTO);
            }
        }else {
            List<Integer> roleListInt = null;
            List<NewVersionUserRoleEntry> roleEntries = null;
            List<ObjectId> teacherUserIds= new ArrayList<ObjectId>();
            //下拉选中家长 或者 学生
            if ("家长".equals(map.get("roleOption"))){
                List<TeacherApproveEntry> teacherApproveEntries = teacherApproveDao.getUserListByTeacherRole();
                for (TeacherApproveEntry teacherApproveEntry : teacherApproveEntries){
                    teacherUserIds.add(teacherApproveEntry.getUserId());
                }
                roleListInt = new ArrayList<Integer>();
                roleListInt.add(0);
            }else {
                roleListInt = new ArrayList<Integer>();
                roleListInt.add(1);
                roleListInt.add(2);
            }
            map.put("roleListInt",roleListInt);
            roleEntries = newVersionUserRoleDao.getUserByRolePageList(roleListInt,map, teacherUserIds);

            List<ObjectId> userIds= new ArrayList<ObjectId>();
            for (NewVersionUserRoleEntry userRoleEntry : roleEntries) {
                userIds.add(userRoleEntry.getUserId());
            }

            Map<ObjectId,List<MemberEntry>> userIdMemberEntries = new HashMap<ObjectId, List<MemberEntry>>();
            if ("家长".equals(map.get("roleOption"))){
                //超简洁获取用户的社团 listMineCommunityId
                // 按userId分组 返回Map<userId,List<communityId>>
                Map<ObjectId,List<ObjectId>> mineUserIdCommunityIdList = communityService.getCommunitys3GroupByUserId(userIds);
                //获取当前用户所有的社群（范围存在于 listMineCommunityId）
                // 按userId分组 返回Map<userId,List<MemberEntry>>
                userIdMemberEntries = memberDao.getMemberEntriesGroupByuserId(userIds,mineUserIdCommunityIdList);
            }
            //获取用户基本信息
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
            for (NewVersionUserRoleEntry userRoleEntry : roleEntries) {
                userManageResultDTO = new UserManageResultDTO();
                UserEntry userEntry = userEntryMap.get(userRoleEntry.getUserId());
                userManageResultDTO.setId(userRoleEntry.getID().toString());
                //超简洁获取用户的社团 listMineCommunityId
//                List<ObjectId> listMineCommunityId = communityService.getCommunitys3(userRoleEntry.getUserId(),-1,0);
                //获取当前用户所有的社群（范围存在于 listMineCommunityId）
//                List<MemberEntry> entries2 = memberDao.getCommunityListByUid(userRoleEntry.getUserId(),listMineCommunityId);
//                userManageResultDTO.setCommunityCount(entries2.size()+"");
//                userManageResultDTO.setCommunityCount(userIdMemberEntries.get(userRoleEntry.getUserId()).size()+"");
                if ("家长".equals(map.get("roleOption"))){
                    userManageResultDTO.setUserRoleName("家长");
                    userManageResultDTO.setCommunityCount(userIdMemberEntries.get(userRoleEntry.getUserId()).size()+"");
                    //家长，老师 员工 登录 或 未登录
                    String cacheUserKey= CacheHandler.getUserKey(userEntry == null ? "" : userEntry.getID().toString());
                    if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                        SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                        if (null != sv && !sv.isEmpty()) {
                            userManageResultDTO.setLineStatus("登录");
                        }else {
                            userManageResultDTO.setLineStatus("未登录");
                        }
                    }
                    /**
                     * 带出孩子信息
                     */
                    List<UserManageChildrenDTO> userManageChildrenDTOS = getChildrenByParentId(userEntry == null ? null : userEntry.getID());
                    userManageResultDTO.setChildrenDTOList(userManageChildrenDTOS);
                }else {
                    userManageResultDTO.setUserRoleName("学生");
                    //学生 在线与否
                    ControlVersionDTO controlVersionDTO2 = getStudentVersion(userEntry == null ? null : userEntry.getID());
                    String ver2 = controlVersionDTO2.getVersion();
                    if(controlVersionDTO2==null){
                        //记录不存在
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
                    /**
                     * 带出绑定父母的信息
                     */
                    List<UserManageParentDTO> userManageParentDTOList = getParentByChildrenId(userEntry == null ? null : userEntry.getID());
                    userManageResultDTO.setParentDTOList(userManageParentDTOList);
                    //父母加入的社群 作为孩子加入或者即将加入的
                    int count = 0;
                    for (UserManageParentDTO dto : userManageParentDTOList){
                        count += dto.getCommunityCount();
                    }
                    userManageResultDTO.setCommunityCount(count + "");
                }
                userManageResultDTO.setUserId(userEntry == null ? "" : userEntry.getID().toString());
                userManageResultDTO.setJiaId(userEntry == null ? "" : userEntry.getGenerateUserCode());
                userManageResultDTO.setUserName(userEntry == null ? "" : userEntry.getUserName());
                userManageResultDTO.setNickName(userEntry == null ? "" : userEntry.getNickName());
                userManageResultDTO.setTelephone(userEntry == null ? "" : userEntry.getMobileNumber());
                //家长学生无系统角色
                userManageResultDTO.setSysRoleName("");
                userManageResultDTOS.add(userManageResultDTO);
            }
        }
        return userManageResultDTOS;
    }

    /**
     *根据孩子Id查找父亲信息
     * @param childrenId
     * @return
     */
    private List<UserManageParentDTO> getParentByChildrenId(ObjectId childrenId) {
        List<UserManageParentDTO> parentDTOList = new ArrayList<UserManageParentDTO>();
        List<NewVersionBindRelationEntry> entries=newVersionBindRelationDao.getEntriesByUserId(childrenId);
        List<ObjectId> userIds= new ArrayList<ObjectId>();
        for(NewVersionBindRelationEntry entry:entries){
            userIds.add(entry.getMainUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(NewVersionBindRelationEntry entry:entries){
            UserEntry userEntry1 = userEntryMap.get(entry.getMainUserId());
            UserManageParentDTO userManageParentDTO = new UserManageParentDTO();
            userManageParentDTO.setUserName(userEntry1.getUserName());
            userManageParentDTO.setUserId(userEntry1.getID().toString());
            userManageParentDTO.setJiaId(userEntry1.getGenerateUserCode());
            userManageParentDTO.setMobilePhone(userEntry1.getMobileNumber());
            userManageParentDTO.setNickName(userEntry1.getNickName());
            userManageParentDTO.setCommunityCount(getUserCommunityCount(entry.getMainUserId()).size());
            userManageParentDTO.setCommunityDTOList(getUserCommunityCount(entry.getMainUserId()));
            parentDTOList.add(userManageParentDTO);
        }
        return parentDTOList;
    }

    /**
     * 获取用户社群
     * @param mainUserId
     * @return
     */
    private List<CommunityDTO> getUserCommunityCount(ObjectId mainUserId) {
        List<CommunityDTO> result = null;
        List<CommunityDTO> communityDTOList = new ArrayList<CommunityDTO>();
        CommunityDTO fulanDto = communityService.getCommunityByName("复兰社区");
        if (null == mainUserId && null != fulanDto) {
            communityDTOList.add(fulanDto);
            return communityDTOList;
        } else {
            if (null != fulanDto) {
                //加入复兰社区
                joinFulaanCommunity(mainUserId, new ObjectId(fulanDto.getId()));
            }
            communityDTOList = communityService.getCommunitys(mainUserId, -1, 10);
            List<CommunityDTO> communityDTOList2 = new ArrayList<CommunityDTO>();
            if (communityDTOList.size() > 0) {
                for (CommunityDTO dto3 : communityDTOList) {
                    //5a7bb6e13d4df96672b6a2bf
                    if (!dto3.getName().equals("复兰社区") && !dto3.getName().equals("复兰大学")) {
                        communityDTOList2.add(dto3);
                    } else {
                        if(!dto3.getName().equals("复兰社区") && mainUserId.toString().equals("5a7bb6e13d4df96672b6a2bf")){
                        communityDTOList2.add(dto3);
                        }
                    }
                }
            }
            result = communityDTOList2;
        }
        return result;
    }

    /**
     * 加入社区但是不加入环信群组---这里只有复兰社区调用
     * 加入复兰社区--- 复兰社区很特殊，特殊对待
     *
     * @param userId
     * @param communityId
     * @return
     */
    private void joinFulaanCommunity(ObjectId userId, ObjectId communityId) {

        ObjectId groupId = communityService.getGroupId(communityId);
        //type=1时，处理的是复兰社区
        if (memberService.isGroupMember(groupId, userId)) {
            return;
        }
        //判断该用户是否曾经加入过该社区
        if (memberService.isBeforeMember(groupId, userId)) {
            memberService.updateMember(groupId, userId, 0);
            communityService.pushToUser(communityId, userId, 3);
            //设置先前该用户所发表的数据
            communityService.setPartIncontentStatus(communityId, userId, 0);
        } else {
            //新人
            communityService.pushToUser(communityId, userId, 3);
            memberService.saveMember(userId, groupId, 0);
        }
    }

    /**
     * 根据父亲Id查找孩子信息
     * @param mainUserId
     * @return
     */
    private List<UserManageChildrenDTO> getChildrenByParentId(ObjectId mainUserId) {
        List<UserManageChildrenDTO> childrenDTOList = new ArrayList<UserManageChildrenDTO>();
//        List<NewVersionBindRelationEntry> entries=newVersionBindRelationDao.getEntriesByMainUserId(mainUserId);
//        List<ObjectId> userIds= new ArrayList<ObjectId>();
//        for(NewVersionBindRelationEntry entry:entries){
//            userIds.add(entry.getUserId());
//        }
//        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
//        for(NewVersionBindRelationEntry entry:entries){
//            UserEntry userEntry1 = userEntryMap.get(entry.getUserId());
//            UserManageChildrenDTO userManageChildrenDTO = new UserManageChildrenDTO();
//            userManageChildrenDTO.setUserName(userEntry1.getUserName());
//            userManageChildrenDTO.setUserId(userEntry1.getID().toString());
//            userManageChildrenDTO.setJiaId(userEntry1.getGenerateUserCode());
//            userManageChildrenDTO.setMobilePhone(userEntry1.getMobileNumber());
//            userManageChildrenDTO.setNickName(userEntry1.getNickName());
//            //是否管控
//            NewVersionBindRelationEntry n = newVersionBindRelationDao.getBindEntry(entry.getUserId());
//            if (n != null){
//                userManageChildrenDTO.setStatus("1");//管控状态
//            }else {
//                userManageChildrenDTO.setStatus("0");
//            }
//            childrenDTOList.add(userManageChildrenDTO);
//        }
        //可管控对象
        List<ObjectId> sonIds = new ArrayList<ObjectId>();
        List<ControlShareEntry> controlShareEntrys = controlShareDao.getAllEntryList(mainUserId);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        objectIdList.add(mainUserId);
        for(ControlShareEntry controlShareEntry : controlShareEntrys){
            objectIdList.add(controlShareEntry.getUserId());
            //分享
            sonIds.add(controlShareEntry.getSonId());
        }
        List<NewVersionBindRelationEntry> entries=newVersionBindRelationDao.getEntriesByMainUserIdList(objectIdList);
        List<ObjectId> userIds= new ArrayList<ObjectId>();
        for(NewVersionBindRelationEntry entry:entries){
            if(entry.getMainUserId().equals(mainUserId)){//管控
                sonIds.add(entry.getUserId());
            }
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(NewVersionBindRelationEntry newVersionBindRelationEntry :entries){
            UserEntry userEntry = userEntryMap.get(newVersionBindRelationEntry.getUserId());
            if(userEntry!=null && sonIds.contains(newVersionBindRelationEntry.getUserId())){
                UserManageChildrenDTO userManageChildrenDTO = new UserManageChildrenDTO();
                String name = org.apache.commons.lang3.StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
//                map.put("nickName",name);
//                map.put("userId",newVersionBindRelationEntry.getUserId().toString());
                userManageChildrenDTO.setUserName(name);
                userManageChildrenDTO.setUserId(userEntry.getID().toString());
                userManageChildrenDTO.setJiaId(userEntry.getGenerateUserCode());
                userManageChildrenDTO.setMobilePhone(userEntry.getMobileNumber());
                userManageChildrenDTO.setNickName(userEntry.getNickName());
                //是否管控
                if(newVersionBindRelationEntry.getMainUserId().equals(mainUserId)){
//                    map.put("isControl",1);
                    userManageChildrenDTO.setStatus("1");//管控状态
                }else{
//                    map.put("isControl",0);
                    userManageChildrenDTO.setStatus("0");
                }
                childrenDTOList.add(userManageChildrenDTO);
            }
        }
        return childrenDTOList;
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
    public Map<String, Object> getUserCreatedCommunity(Map map) {
        List<CommunityDTO> communityDTOS = new ArrayList<CommunityDTO>();
        //超简洁获取用户的社团 listMineCommunityId
        List<ObjectId> listMineCommunityId = communityService.getCommunitys3(new ObjectId(map.get("userId").toString()), -1, 0);
        Map<String, Object> result = communityDao.getUserCreatedCommunity(map, listMineCommunityId);
        List<CommunityEntry> communityEntries = (ArrayList) result.get("communityEntryList");
//        List<CommunityEntry> communityEntries = communityDao.getUserCreatedCommunity(map);

        for (CommunityEntry communityEntry : communityEntries) {
            CommunityDTO communityDTO = new CommunityDTO(communityEntry);
            //根据communityId查询关联学校信息
            List<ObjectId> communityIdL = new ArrayList<ObjectId>();
            communityIdL.add(communityEntry.getID());
            List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(communityIdL);
            if (0 == schoolCommunityEntries.size()) {//没关联学校
                communityDTO.setSchoolName("");
            } else {//有关联学校
                HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolCommunityEntries.get(0).getSchoolId());
                communityDTO.setSchoolName(homeSchoolEntry.getName());
            }
            //获取某个社区下绑定的孩子有哪些（userId在communityId下的孩子）
            List<NewVersionBindRelationDTO> bindDtoList = newVersionBindService.getNewVersionBindDtos(new ObjectId(map.get("userId").toString()), communityEntry.getID());
            communityDTO.setBindRelationDTOList(bindDtoList);

            int memberCount = memberService.getMemberCount(communityEntry.getGroupId());
            communityDTO.setMemberCount(memberCount);
            communityDTOS.add(communityDTO);
        }
        result.remove("communityEntryList");
        result.put("communitydtos", communityDTOS);
        return result;
    }

    /**
     * 获取用户加入的社群
     * @param map
     * @return
     */
    public Map<String,Object> getUserJoinCommunity(Map map) {
        List<CommunityDTO> communityDTOS = new ArrayList<CommunityDTO>();
        //超简洁获取用户的社团 listMineCommunityId
        List<ObjectId> listMineCommunityId = communityService.getCommunitys3(new ObjectId(map.get("userId").toString()),-1,0);
        //获取当前用户所有的社群 （范围存在于 listMineCommunityId）
        List<MemberEntry> memberEntries = memberDao.getCommunityListByUid(new ObjectId(map.get("userId").toString()),listMineCommunityId);
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

            //获取某个社区下绑定的孩子有哪些（userId在communityId下的孩子）
            List<NewVersionBindRelationDTO> bindDtoList = newVersionBindService.getNewVersionBindDtos(new ObjectId(map.get("userId").toString()), communityEntry.getID());
            communityDTO.setBindRelationDTOList(bindDtoList);

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
        Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
//        Map<ObjectId,RemarkEntry> remarkEntryMap=new HashMap<ObjectId, RemarkEntry>();
//        if(null!=userId) {
//            remarkEntryMap=remarkDao.find(userId, userIds);
//        }
        for (MemberEntry entry : entries) {
            MemberDTO memberDTO=new MemberDTO(entry);
            UserEntry userEntry = userEntryMap.get(entry.getUserId());
            memberDTO.setJiaId(userEntry.getGenerateUserCode());
//            if(null!=remarkEntryMap){
//                RemarkEntry remarkEntry=remarkEntryMap.get(entry.getUserId());
//                if(null!=remarkEntry){
//                    memberDTO.setNickName(remarkEntry.getRemark());
//                }
//            }
            //获取某个社区下绑定的孩子有哪些（userId在communityId下的孩子）
            List<NewVersionBindRelationDTO> bindDtoList = newVersionBindService.getNewVersionBindDtos(entry.getUserId(), entry.getCommunityId());
            memberDTO.setBindRelationDTOList(bindDtoList);

            memberDTOs.add(memberDTO);
        }
        result.remove("memberEntries");
        result.put("memberdtos",memberDTOs);
        return result;
    }

    public String setCommunityRole(Map map) {
        String msg = "";
        ObjectId groupId = new ObjectId(map.get("groupId").toString());
        CommunityEntry communityEntry = communityDao.findByObjectId(new ObjectId(map.get("communityId").toString()));
        //社群信息校验
        if (null == communityEntry){
                return "无该社群，请确认输入的社群信息是否正确！";
        }

        //用户信息校验
        UserEntry userEntry = userDao.findByUserId(new ObjectId(map.get("userId").toString()));
        if(null == userEntry){
            return "当前用户信息有误！";
        }
        //判断是否设置社长
        if (!StringUtils.isBlank(map.get("role").toString()) && Integer.parseInt(map.get("role").toString()) == 2){
            //设置社长

            //1 查出社长信息

            MemberEntry memberEntry = memberDao.getHeader(groupId);

            // 2 判断需要设置的是否为新社长
            if (!StringUtils.isBlank(map.get("userId").toString()) && !memberEntry.getUserId().toString().equals(map.get("userId").toString())){
                //是设置新社长
                // 3 设置新社长
                memberDao.updateRoleById(map);
                // 4 老社长设置为社群成员
                ObjectId id = memberEntry.getID();
                int role = 0;
                memberDao.updateRoleById(id, role);
            }else {
                memberDao.updateRoleById(map);
            }
        }else if (!StringUtils.isBlank(map.get("role").toString()) && Integer.parseInt(map.get("role").toString()) == 1){
            //设置副社长
            int countDeputyHead = memberDao.countDeputyHead(groupId);//社区副社长人数
            if (countDeputyHead >= 10){
                return "副社长不能超过十人！";
            }else {
                memberDao.updateRoleById(map);
            }
        }else{
            memberDao.updateRoleById(map);
        }

        msg = "操作成功！";
        return msg;
    }

    /**
     * 搜索社群信息
     * searchParam(包括searchId（社群查找Id）communityName)
     * @param map
     * @return
     */
    public Map<String, Object> getCommunityInfo(Map map) {
        Map<String, Object> result = new HashMap<String, Object>();

        CommunityDTO communityDTO = null;
        CommunityEntry communityEntry = communityDao.findBySearchIdOrName(map.get("searchParam").toString());
        //社群信息校验
        if (null == communityEntry){
            communityDTO = new CommunityDTO();
            result.put("communityDTO",communityDTO);
        }else {
            communityDTO = new CommunityDTO(communityEntry);
            result.put("communityDTO",communityDTO);
            //判断用户是否在要加入的社群
            ObjectId groupId = new ObjectId(communityDTO.getGroupId());
            List<MemberEntry> memberEntries = memberDao.getAllMembersByGroupId(groupId);
            for (MemberEntry memberEntry : memberEntries){
                if (memberEntry.getUserId().toString().equals(map.get("userId").toString()) ){
                    result.put("in","true");
                    return result;
                }
            }
        }
        result.put("in","false");

        return result;
//        return communityDTO;
    }

    /**
     * 解除孩子社群绑定
     * @param communityId
     * @param userIds
     */
    public void relieveChildrenBindRelation(ObjectId communityId, String userIds) {
        String[] uids = userIds.split(",");
        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        for(String uid:uids){
            userIdList.add(new ObjectId(uid));
        }
        newVersionCommunityBindDao.relieveChildrenBindRelation(userIdList,communityId);
    }

    /**
     * 添加孩子社群绑定
     * @param communityId
     * @param checkUserIds
     */
    public void addChildrenBindRelation(ObjectId communityId, String mainUserId, String checkUserIds) {
        ObjectId mainUserIdObj = new ObjectId(mainUserId);
        String[] uids = checkUserIds.split(",");
        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        List<NewVersionCommunityBindEntry> communityBindEntries = new ArrayList<NewVersionCommunityBindEntry>();
        for(String uid:uids){
            //判断是否是绑定状态
            ObjectId uidObj = new ObjectId(uid);
            NewVersionCommunityBindEntry newVersionCommunityBindEntry = newVersionCommunityBindDao.getValidEntry(communityId, mainUserIdObj, uidObj);
            if (newVersionCommunityBindEntry == null){
                //没绑定 添加绑定记录
                communityBindEntries.add(new NewVersionCommunityBindEntry(communityId, mainUserIdObj, uidObj));
//                userIdList.add(uidObj);
            }else{
                if (1 == newVersionCommunityBindEntry.getRemoveStatus()){
                    newVersionCommunityBindEntry.setRemoveStatus(0);
                    newVersionCommunityBindDao.saveEntry(newVersionCommunityBindEntry);//更新
                }
            }
        }
        if (communityBindEntries.size()>0){
            //添加绑定记录到数据库(批量新增)
            newVersionCommunityBindDao.saveEntries(communityBindEntries);
        }
    }
}
