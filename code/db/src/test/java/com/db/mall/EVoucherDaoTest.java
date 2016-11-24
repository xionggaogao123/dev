package com.db.mall;

import com.db.ebusiness.EVoucherDao;
import org.junit.Test;

/**
 * Created by fl on 2016/3/16.
 */
public class EVoucherDaoTest {

    private EVoucherDao eVoucherDao = new EVoucherDao();

    @Test
    public void testCheckVoucherExpiration(){
        eVoucherDao.checkVoucherExpiration();
    }

}
