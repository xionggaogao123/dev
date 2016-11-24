package com.db.withdrawCash;

import com.pojo.emarket.WithDrawEntry;
import org.junit.Test;

import java.util.Date;

/**
 * Created by guojing on 2015/6/4.
 */
public class WithdrawCashDaoTest {
    private WithdrawCashDao dao =new WithdrawCashDao();

    @Test
    public void addWithdrawCash()
    {
        Date currTime=new Date();
        Long createTime= currTime.getTime();
        WithDrawEntry entry = new WithDrawEntry(
                "hdfkljdskfjh",
                "ddddd",
                123.21,
                createTime,
                1,
                createTime,
                "siri",
                "123456",
                "中国银行",
                "12345678119",
                1,
                "eee",
                "320925198787895489"
        );
        dao.addWithdrawCash(entry);
        System.out.println(entry.getID().toString());
    }
}
