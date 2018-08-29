package com.fulaan.backstage.service;

import com.db.backstage.RoleJurisdictionSettingDao;
import com.db.backstage.UserRoleJurisdictionDao;
import com.fulaan.backstage.dto.RoleJurisdictionSettingDto;
import com.pojo.backstage.RoleJurisdictionSettingEntry;
import com.pojo.backstage.UserRoleJurisdictionEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/23 15:49
 * @Description:
 */
@Service
public class BackStageRoleManageService {

    RoleJurisdictionSettingDao settingDao = new RoleJurisdictionSettingDao();

    UserRoleJurisdictionDao roleJurisdictionDao = new UserRoleJurisdictionDao();

    public String saveRoleJurisdiction(Map map) {
        String msg = "";
        if ("".equals(map.get("id")) || null == map.get("id")){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            RoleJurisdictionSettingEntry settingEntry = new RoleJurisdictionSettingEntry(
                    map.get("roleName").toString(),
                    new ObjectId(map.get("jurisdictionLevelId").toString()),
                    map.get("roleProperty").toString(),
                    new ObjectId(map.get("userId").toString()),
                    f.format(now),
                    new ObjectId(map.get("userId").toString()),
                    f.format(now)
            );
            msg = settingDao.addEntry(settingEntry);
        }else {
            msg = settingDao.updateSetting(map);
        }
        return msg;
    }

    /**
     * 获取角色 和 对应 权限的 信息
     * @param map
     * @return
     */
    public List<RoleJurisdictionSettingDto> getRoleJurisdictionList(Map map) {
        List<RoleJurisdictionSettingEntry>  settingEntries = settingDao.getRoleJurisdictionList(map);
        List<RoleJurisdictionSettingDto> settingDtos = null;
        if ( settingEntries != null){
            settingDtos = new ArrayList<RoleJurisdictionSettingDto>();
            for (RoleJurisdictionSettingEntry entry : settingEntries){
                UserRoleJurisdictionEntry jurisdictionEntry= roleJurisdictionDao.getEntryById(entry.getJurisdictionLevelId());
                settingDtos.add(new RoleJurisdictionSettingDto(entry,jurisdictionEntry));
            }
        }
        return settingDtos;
    }
}
