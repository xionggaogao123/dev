package com.db.temp;

import com.db.ebusiness.EVoucherDao;
import com.pojo.ebusiness.EVoucherEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.util.Date;

/**
 * Created by fl on 2016/3/11.
 */
public class GenerateVoucher {
    private EVoucherDao eVoucherDao = new EVoucherDao();

    private void generate() {
        String num = String.valueOf(System.currentTimeMillis());
        num += RandomUtils.nextInt(Constant.MIN_PASSWORD);
        Date date = DateTimeUtils.stringToDate("2016-04-30 23:59:59", DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        EVoucherEntry eVoucherEntry = new EVoucherEntry(null, num, 5000, date.getTime(), 0);
        eVoucherDao.add(eVoucherEntry);
    }

    public static void main(String[] args) {
        GenerateVoucher generateVoucher = new GenerateVoucher();
        for (int i = 0; i < 10; i++) {
            generateVoucher.generate();
        }
    }
}
