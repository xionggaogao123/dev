package com.fulaan.integral.service;

import com.db.integral.IntegralRecordDao;
import com.db.integral.IntegralSufferDao;
import com.pojo.integral.IntegralRecordEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by James on 2018-04-16.
 */
@Service
public class IntegralSufferService {

    private IntegralRecordDao integralRecordDao =  new IntegralRecordDao();

    private IntegralSufferDao integralSufferDao = new IntegralSufferDao();


    /**
     * 添加积分(逻辑)
     */
    public boolean addIntegral(ObjectId userId){
        long current = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        List<IntegralRecordEntry> entries =  integralRecordDao.getEntryListByUserId(userId, zero);
        if(entries.size()==0){//第一次

        }

        return false;
    }
}
