package com.db.configManage;

import com.db.configmanage.ConfigManageDao;
import com.pojo.configmanage.ConfigManageEntry;
import com.pojo.configmanage.ConfigManageType;
import org.junit.Test;

import java.util.List;

/**
 * Created by guojing on 2015/5/14.
 */
public class ConfigManageDaoTest
{
    private ConfigManageDao dao =new ConfigManageDao();

    @Test
    public void addConfigManageEntry()
    {
        ConfigManageEntry entry=new ConfigManageEntry(ConfigManageType.CEILINGOFDAILYEXP.getCode(),ConfigManageType.CEILINGOFDAILYEXP.getDesc(),"20");
        dao.addConfigManageEntry(entry);
    }

    @Test
    public void updateConfigManageValue()
    {
        dao.updateConfigManageValue(1, "20");
    }

    @Test
    public void getConfigManageEntry()
    {
        ConfigManageEntry entry=dao.getConfigManageEntry(1);
        System.out.println(entry.toString());
    }

    @Test
    public void getAllConfigManageEntry()
    {
        List<ConfigManageEntry> lis=dao.selAllConfigManageEntry();
        for(ConfigManageEntry entry:lis){
            System.out.println(entry.toString());
        }
    }
}
