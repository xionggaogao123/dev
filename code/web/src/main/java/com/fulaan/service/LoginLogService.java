package com.fulaan.service;

import com.fulaan.dao.LoginLogDao;
import com.fulaan.pojo.FLoginLog;
import com.pojo.fcommunity.FLoginLogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/10/20.
 */
@Service
public class LoginLogService {

    @Autowired private LoginLogDao loginLogDao;

    public List<FLoginLog> getLoginLog(long start,long end){
        List<FLoginLogEntry> loginLogEntries = loginLogDao.getLoginLog(start,end);
        List<FLoginLog> loginLogs = new ArrayList<FLoginLog>();
        for(FLoginLogEntry logEntry:loginLogEntries){
            FLoginLog loginLog = new FLoginLog(logEntry);
            loginLogs.add(loginLog);
        }
        return loginLogs;
    }
}
