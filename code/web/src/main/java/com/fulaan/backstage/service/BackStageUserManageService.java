package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.RoleJurisdictionSettingDao;
import com.db.backstage.TeacherApproveDao;
import com.db.backstage.UserLogResultDao;
import com.db.controlphone.ControlVersionDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.UserManageResultDTO;
import com.fulaan.cache.CacheHandler;
import com.fulaan.controlphone.dto.ControlVersionDTO;
import com.fulaan.controlphone.service.ControlPhoneService;
import com.pojo.app.SessionValue;
import com.pojo.backstage.RoleJurisdictionSettingEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.backstage.UserLogResultEntry;
import com.pojo.controlphone.ControlVersionEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/30 09:36
 * @Description:
 */
@Service
public class BackStageUserManageService {

    private NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private UserDao userDao = new UserDao();

    //用户 跟 系统角色绑定
    private UserLogResultDao userLogResultDao = new UserLogResultDao();
    //角色相关信息
    private RoleJurisdictionSettingDao roleJurisdictionSettingDao = new RoleJurisdictionSettingDao();

    private ControlVersionDao controlVersionDao = new ControlVersionDao();

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
                userManageResultDTO.setCommunityCount("");
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
                userManageResultDTO.setCommunityCount("");
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
                    userManageResultDTO.setCommunityCount("");
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
                    userManageResultDTO.setCommunityCount("");
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
}
