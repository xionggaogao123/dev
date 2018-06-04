package com.fulaan.alipay.service;

import com.db.excellentCourses.AccountLogDao;
import com.pojo.excellentCourses.AccountLogEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 2018-05-30.
 */
@Service
public class AppAlipayService  {
    private AccountLogDao accountLogDao = new AccountLogDao();


    /**
     * 充值消费日志
     */
    public void addLog(ObjectId userId,ObjectId contactId,String description){
        AccountLogEntry accountLogEntry = new AccountLogEntry(userId,contactId,description);
        accountLogDao.addEntry(accountLogEntry);
    }

    /**
     * 用户自定义充值（创建订单）
     */
    public Map<String,Object> appPay(){
        Map<String,Object> map = new HashMap<String, Object>();














        return map;
    }

}
