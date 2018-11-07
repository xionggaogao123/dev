package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.LogMessageDao;
import com.db.backstage.RoleJurisdictionSettingDao;
import com.db.backstage.UserLogResultDao;
import com.db.backstage.UserRoleJurisdictionDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.LogMessageDTO;
import com.fulaan.backstage.dto.UserLogResultDTO;
import com.fulaan.backstage.dto.UserRoleJurisdictionDto;
import com.fulaan.user.service.UserService;
import com.pojo.backstage.LogMessageType;
import com.pojo.backstage.RoleJurisdictionSettingEntry;
import com.pojo.backstage.UserLogResultEntry;
import com.pojo.backstage.UserRoleJurisdictionEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/22 16:44
 * @Description:
 */
@Service
public class BackStageAdminManageService {

    @Autowired
    private UserService userService;

    private UserLogResultDao userLogResultDao = new UserLogResultDao();
    private UserDao userDao = new UserDao();
    private LogMessageDao logMessageDao = new LogMessageDao();

    private RoleJurisdictionSettingDao roleJurisdictionSettingDao = new RoleJurisdictionSettingDao();

    private UserRoleJurisdictionDao userRoleJurisdictionDao = new UserRoleJurisdictionDao();


    public String saveAdminJurisdiction(Map map) {
        String msg = "";
        UserEntry userEntry = userDao.getGenerateCodeEntry(map.get("userJiaId").toString());
        if (null == userEntry){
            msg = "用户不存在，请检查用户ID！";
            return msg;
        }
        List<UserLogResultEntry> resultEntries = userLogResultDao.getLogsByUserId(userEntry.getID());
        if (resultEntries.size() > 0){
            msg = "此用户已经是管理员！";
            return msg;
        }
        if ("".equals(map.get("id")) || null == map.get("id")) {//新增管理员权限
//            UserLogResultEntry entry=new UserLogResultEntry(new ObjectId(map.get("userId").toString()),new ObjectId(map.get("roleId").toString()));
            UserLogResultEntry entry=new UserLogResultEntry(userEntry.getID(), new ObjectId(map.get("roleId").toString()));
            userLogResultDao.saveUserLogEntry(entry);
            this.addLogMessage
                    (entry.getID().toString(),
                    "新增了管理员用户"+userEntry.getUserName()+"权限为"+map.get("roleName").toString(),
                    LogMessageType.manageUserRole.getDes(),userEntry.getID().toString()
                    );
            msg = entry.getID().toString();
        }else {//修改管理员权限
            UserLogResultEntry resultEntry=userLogResultDao.getEntryById(new ObjectId(map.get("id").toString()));
            resultEntry.setRoleId(new ObjectId(map.get("roleId").toString()));
            this.addLogMessage
                    (resultEntry.getID().toString(),
                    "修改了"+userEntry.getUserName()+"的权限为"+map.get("roleName").toString(),
                    LogMessageType.manageUserRole.getDes(), userEntry.getID().toString()
                    );
            userLogResultDao.saveUserLogEntry(resultEntry);
            msg = resultEntry.getID().toString();
        }
        return msg;
    }

    //非敏感日志操作
    public void addLogMessage(String contactId,String content,String function,String userId){
        LogMessageDTO dto = new LogMessageDTO();
        dto.setType(1);
        dto.setContactId(contactId);
        dto.setContent(content);
        dto.setFunction(function);
        dto.setUserId(userId);
        logMessageDao.addEntry(dto.buildAddEntry());
    }

    public Map<String,Object> getAdminJurisdiction(Map map) {
        Map<String,Object> result = new HashMap<String, Object>();
        if (null == map.get("userInfo")) {
            //当未输入用户信息时
            //roleProperty 去 jxm_role_jurisdiction_setting表查找对应的roleId
            // 同一个roleProperty可以对应多个roleId
            List<RoleJurisdictionSettingEntry> settingEntries = roleJurisdictionSettingDao.getEntriesByRoleProperty(map.get("roleProperty").toString());
            List<ObjectId> settingEntryIdList = new ArrayList<ObjectId>();
            for (RoleJurisdictionSettingEntry settingEntryF : settingEntries){
                settingEntryIdList.add(settingEntryF.getID());
            }
            List<UserLogResultDTO> resultDTOs = new ArrayList<UserLogResultDTO>();
            int count = 0 ;
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //roleId 去查找 对应的角色下的用户
                List<UserLogResultEntry> resultEntriesCount = userLogResultDao.getEntryByRoleIdList(settingEntryIdList);//获取数据总量
                count += resultEntriesCount.size();
                //获取分页下的数据
                List<UserLogResultEntry> resultEntries = userLogResultDao.getEntryPageByRoleIdList(
                                settingEntryIdList,
                                Integer.parseInt(map.get("page").toString()),
                                Integer.parseInt(map.get("pageSize").toString())
                        );
                for(UserLogResultEntry entry:resultEntries){
                    UserLogResultDTO resultDTO=new UserLogResultDTO();
                    resultDTO.setId(entry.getID().toString());
                    resultDTO.setUserId(entry.getUserId().toString());
                    //用户基础信息
                    UserEntry userEntry = userDao.findByUserId(entry.getUserId());
                    if(null!=userEntry){
                        resultDTO.setUserName(userEntry.getNickName() != "" ? userEntry.getNickName() : userEntry.getUserName());
                        resultDTO.setNickName(userEntry.getNickName());
                        resultDTO.setRegisterTime(sd.format(new Date(userEntry.getRegisterTime())));
                        resultDTO.setJiaId(userEntry.getGenerateUserCode());
                    }
                    //根据角色和权限绑定 带出角色Id关联的权限信息
                    RoleJurisdictionSettingEntry settingEntry = roleJurisdictionSettingDao.getEntryById(entry.getRoleId());
                    if (null != settingEntry){
                        resultDTO.setRoleName(settingEntry.getRoleName());
                        resultDTO.setRoleId(entry.getRoleId().toString());
                        //根据JurisdictionLevelId为主键
                        // 查找jxm_user_role_jurisdiction表里面的roleJurisdiction集合
                        //UserRoleJurisdictionDto 将查询转换出权限中文RoleJurisdictionName
                        UserRoleJurisdictionEntry jurisdictionEntry = userRoleJurisdictionDao.getEntryById(settingEntry.getJurisdictionLevelId());
                        UserRoleJurisdictionDto userRoleJurisdictionDto = new UserRoleJurisdictionDto(jurisdictionEntry);
                        resultDTO.setUserRoleJurisdictionDto(userRoleJurisdictionDto);
                        resultDTO.setRoleJurisdictionName(userRoleJurisdictionDto.getRoleJurisdictionName());
                        resultDTO.setLevel(userRoleJurisdictionDto.getLevel());
                    }
                    resultDTOs.add(resultDTO);
                }
            result.put("count",count);
            result.put("dto",resultDTOs);
        }else {
            //当输入用户信息userInfo（userId 或 userName）时
            //根据用户信息查询用户Id
            ObjectId userId = null;
            UserEntry userEntry = userDao.findByUserNameOrNickName(map.get("userInfo").toString());
            if (null != userEntry){
                //根据用户名查询到
                userId = userEntry.getID();
            }else {
                //根据用户名查询不到，进而根据用户家校美Id查询
                userEntry = userDao.getGenerateCodeEntry(map.get("userInfo").toString());
                if (null != userEntry) {
                    userId = userEntry.getID();
                }else {
                    //根据用户名，根据用户Id都查询不到
                    result.put("msg","用户名或ID输入错误");
                    return result;
                }
            }
            /**
             * 开始查询
             */
            List<UserLogResultEntry> resultEntries = userLogResultDao.getLogsByUserId(userId);
            List<UserLogResultDTO> resultDTOs = new ArrayList<UserLogResultDTO>();

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(UserLogResultEntry entry:resultEntries){
                UserLogResultDTO resultDTO=new UserLogResultDTO();
                resultDTO.setId(entry.getID().toString());
                resultDTO.setUserId(entry.getUserId().toString());
                //带出用户信息
                if(null!=userEntry){
                    resultDTO.setUserName(userEntry.getNickName() != "" ? userEntry.getNickName() : userEntry.getUserName());
                    resultDTO.setNickName(userEntry.getNickName());
                    resultDTO.setRegisterTime(sd.format(new Date(userEntry.getRegisterTime())));
                    resultDTO.setJiaId(userEntry.getGenerateUserCode());
                }
                //根据角色和权限绑定 带出角色Id关联的权限信息
                RoleJurisdictionSettingEntry settingEntry = roleJurisdictionSettingDao.getEntryById(entry.getRoleId());
                if (null != settingEntry){
                    resultDTO.setRoleName(settingEntry.getRoleName());
                    resultDTO.setRoleId(entry.getRoleId().toString());
                    //根据JurisdictionLevelId为主键
                    // 查找jxm_user_role_jurisdiction表里面的roleJurisdiction集合
                    //UserRoleJurisdictionDto 将查询转换出权限中文RoleJurisdictionName
                    UserRoleJurisdictionEntry jurisdictionEntry = userRoleJurisdictionDao.getEntryById(settingEntry.getJurisdictionLevelId());
                    UserRoleJurisdictionDto userRoleJurisdictionDto = new UserRoleJurisdictionDto(jurisdictionEntry);
                    resultDTO.setUserRoleJurisdictionDto(userRoleJurisdictionDto);
                    resultDTO.setRoleJurisdictionName(userRoleJurisdictionDto.getRoleJurisdictionName());
                    resultDTO.setLevel(userRoleJurisdictionDto.getLevel());
                    resultDTOs.add(resultDTO);//删除对应角色的 不展示
                }
//                resultDTOs.add(resultDTO);
            }
            result.put("count",resultDTOs.size());
            result.put("dto",resultDTOs);
        }
        return result;
    }

    public JSONArray getRolePropertyInfo() {
        JSONArray jsonArray = new JSONArray();
        Map<String,Object> map = new HashMap<String, Object>();
        //获取 jxm_role_jurisdiction_setting 数据
        List<RoleJurisdictionSettingEntry> settingEntries = roleJurisdictionSettingDao.getRoleJurisdictionList(map);
        //获取RoleProperty 并去重
        List<String> rolePropertyList = new ArrayList<String>();
        for (RoleJurisdictionSettingEntry settingEntry : settingEntries){
            if (!rolePropertyList.contains(settingEntry.getRoleProperty())){
                rolePropertyList.add(settingEntry.getRoleProperty());
            }
        }
        //获取roleProperty 对应的 roleId 集合
        Map<String,List> propertyRoleIdMap = new HashMap<String, List>();
        for (String roleProperty: rolePropertyList){
            List<String> roleIdList = new ArrayList<String>();
            for (RoleJurisdictionSettingEntry settingEntry : settingEntries) {
                if (roleProperty.equals(settingEntry.getRoleProperty())){
                    roleIdList.add(settingEntry.getID().toString());
                }
            }
            propertyRoleIdMap.put(roleProperty,roleIdList);
        }
        //根据roleId 获取 roleProperty 对应的人员count
        for (String roleProperty: rolePropertyList) {
            JSONObject jsonObject = new JSONObject();
            int count = 0;
            List<String> roleIdList = propertyRoleIdMap.get(roleProperty);
            for (String roleId : roleIdList){
                List<UserLogResultEntry> resultEntries = userLogResultDao.getEntryByRoleId(new ObjectId(roleId));
                count += resultEntries.size();
            }
            jsonObject.put("value",roleProperty);
            jsonObject.put("name",roleProperty+"管理员"+"("+count+"人)");
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    public String delAdminJurisdiction(Map map) {
        String result = "";
        result = userLogResultDao.delAdminJurisdiction(new ObjectId(map.get("id").toString()));
        return result;
    }
}
