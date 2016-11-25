package com.fulaan.forum.service;

import com.db.fcommunity.LoginLogDao;
import com.db.forum.FLogDao;
import com.fulaan.pojo.FLoginLog;
import com.pojo.fcommunity.FLoginLogEntry;
import com.pojo.forum.FLogDTO;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2016/7/19.
 */
@Service
public class FLogService {

    private FLogDao fLogDao = new FLogDao();
    private LoginLogDao loginDao = new LoginLogDao();

    /**
     * 新增日志
     *
     * @param fLogDTO
     * @return
     */
    public ObjectId addFLog(FLogDTO fLogDTO) {
        return fLogDao.addFLog(fLogDTO.exportEntry());
    }

    /**
     * 根据板块页面名称（改用户）查询访问量
     *
     * @param actionName
     * @param personId
     * @return
     */
    public int getCountByActionName(String actionName, String personId) {
        ObjectId userId = personId.equals("") ? null : new ObjectId(personId);
        return fLogDao.getCountByActionName(actionName, userId);
    }

    public void loginLog(FLoginLog log) {
        loginDao.save(new FLoginLogEntry(log.getUserName(), log.getNickName(), log.getLoginTime(), log.getIp(), log.getPf()));
    }


}
