package com.db.temp;

import com.db.smartcard.KaoQinTimeSetDao;
import com.pojo.smartcard.KaoQinTimeSetEntry;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2016/6/21.
 */
public class KaoQinState {

    public static void main(String[] args) {
        KaoQinTimeSetDao kaoQinTimeSetDao=new KaoQinTimeSetDao();
        String lateTime="08:00:00";

        String middleTime="11:00:00";

        String punctualTime="17:00:00";

        KaoQinTimeSetEntry entry=new KaoQinTimeSetEntry(
                new ObjectId("55934c14f6f28b7261c19c62"),
                lateTime,
                middleTime,
                punctualTime
        );
        kaoQinTimeSetDao.addKaoQinTimeSetEntry(entry);
    }
}
