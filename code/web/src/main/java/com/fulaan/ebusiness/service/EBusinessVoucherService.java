package com.fulaan.ebusiness.service;

import com.db.ebusiness.EVoucherDao;
import com.pojo.ebusiness.EVoucherEntry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/8/26.
 */
@Service
public class EBusinessVoucherService {

    private EVoucherDao eVoucherDao=new EVoucherDao();
    /**
     * 用户活动领取生成优惠券
     */
    public Boolean addActivityEVoucher(List<EVoucherEntry> eVoucherEntries){
        return eVoucherDao.add(eVoucherEntries);
    }
}
