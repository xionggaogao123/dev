package com.db.temp;

import com.db.ebusiness.EGoodsDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.ebusiness.EGoodsEntry;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/2/1.
 */
public class UpdateEGoods {
    private EGoodsDao eGoodsDao = new EGoodsDao();

    private void update() {
        List<EGoodsEntry> eGoodsEntryList = eGoodsDao.getEGoodsEntrys(Constant.FIELDS, new BasicDBObject("_id", 1), 0, -1, -1, -1, null, null, null, 0, null, null, 0, 50);
        if (null != eGoodsEntryList) {
            for (EGoodsEntry eGoodsEntry : eGoodsEntryList) {
                List<EGoodsEntry.Kind> kindList = eGoodsEntry.getKindList();
                if (null != kindList) {
                    for (EGoodsEntry.Kind kind : kindList) {
                        List<EGoodsEntry.Spec> specs = new ArrayList<EGoodsEntry.Spec>();
                        List<IdValuePair> idValuePairs = kind.getIVList();
                        for (IdValuePair pair : idValuePairs) {
                            specs.add(new EGoodsEntry.Spec(pair.getId(), pair.getValue().toString(), 0));
                        }
                        kind.setList(specs);
                    }
                }
                eGoodsDao.addEGoodsEntry(eGoodsEntry);
            }
        }
    }

    public static void main(String[] args) {
        new UpdateEGoods().update();
    }
}
