package com.fulaan.mall.service;

import com.db.ebusiness.EGoodsDao;
import com.db.ebusiness.EGoodsLogDao;
import com.pojo.ebusiness.*;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fl on 2016/1/28.
 */
@Service
public class EGoodsLogService {
    private EGoodsLogDao eGoodsLogDao = new EGoodsLogDao();
    private EGoodsDao eGoodsDao = new EGoodsDao();

    /**
     * 添加一条记录
     *
     * @param eGoodsLogEntry
     * @return
     */
    public ObjectId addEGoodsLog(ObjectId userId, EGoodsLogEntry eGoodsLogEntry) {
        Date time = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdFormat.format(time);
        if (eGoodsLogEntry.getType() == 1 && !eGoodsLogDao.isAddLog(userId, eGoodsLogEntry.getGoodsId(), date)) {//浏览记录每天只添加一次
            return eGoodsLogDao.add(eGoodsLogEntry);
        } else if (eGoodsLogEntry.getType() == 2) {
            return eGoodsLogDao.add(eGoodsLogEntry);
        }
        return eGoodsLogEntry.getID();
    }

    /**
     * 查询浏览记录
     */
    public List<EGoodsDayLog> getDayLogList(ObjectId userId) {
        List<EGoodsHistory> historyList = eGoodsLogDao.getLogs(userId);
        Set<String> dateList = new LinkedHashSet<String>();
        List<EGoodsDayLog> logList = new ArrayList<EGoodsDayLog>();

        for (EGoodsHistory history : historyList) {
            dateList.add(history.getDate());
        }
        for (String date : dateList) {
            EGoodsDayLog log = new EGoodsDayLog();
            log.setDate(date);
            List<EGoodsLogDTO> goodsLogList = new ArrayList<EGoodsLogDTO>();
            for (EGoodsHistory history : historyList) {
                if (date.equals(history.getDate())) {
                    EGoodsEntry goodsEntry = eGoodsDao.getEGoodsEntry(new ObjectId(history.getGoodsId()), Constant.FIELDS);
                    if (goodsEntry != null) {
                        goodsLogList.add(new EGoodsLogDTO(history.getId(), goodsEntry));
                    }
                }
            }
            log.setGoodsLogList(goodsLogList);
            logList.add(log);
        }
        return logList;
    }

    /**
     * 查询收藏记录
     */
    public List<EGoodsLogDTO> getCollectionList(ObjectId userId) {
        List<EGoodsCollection> collectionList = eGoodsLogDao.getCollections(userId);
        List<EGoodsLogDTO> goodsLogList = new ArrayList<EGoodsLogDTO>();
        for (EGoodsCollection collection : collectionList) {
            EGoodsEntry goodsEntry = eGoodsDao.getEGoodsEntry(new ObjectId(collection.getGoodsId()), Constant.FIELDS);
            if (goodsEntry != null) {
                goodsLogList.add(new EGoodsLogDTO(collection.getId(), goodsEntry));
            }
        }
        return goodsLogList;
    }

    /**
     * 判断是否已收藏
     */
    public boolean isCollected(ObjectId userId, ObjectId goodsId) {
        return eGoodsLogDao.isCollected(userId, goodsId);
    }

    /**
     * 删除记录
     */
    public void deleteLog(ObjectId logId) {
        eGoodsLogDao.delete(logId);
    }

}
