package com.fulaan.mall.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.ebusiness.EGoodsDao;
import com.fulaan.cache.CacheHandler;
import com.pojo.ebusiness.EGoodsEntry;
import com.pojo.ebusiness.EBusinessCartGoods;
import com.pojo.ebusiness.EGoodsEntry.Kind;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.serialize.ListObjectSerializable;

/**
 * 购物车service
 *
 * @author fourer
 */
public class EBusinessCartService {

    private EGoodsDao goodsdao = new EGoodsDao();


    /**
     * 添加商品
     *
     * @param userId
     * @param goodsId
     * @param kinds
     * @param count
     * @return
     * @throws Exception
     */
    public List<EBusinessCartGoods> addGoods(ObjectId userId, ObjectId goodsId, List<ObjectId> kinds, int count, int type) throws Exception {

        EGoodsEntry ge = goodsdao.getEGoodsEntry(goodsId, Constant.FIELDS);
        if (null == ge)
            throw new Exception("Can not find egoods;the id:" + goodsId.toString());

        EBusinessCartGoods ecg = new EBusinessCartGoods(ge, count);
        ecg.setType(type);

        if (null != kinds && kinds.size() > 0) {
            StringBuilder kindInfo = new StringBuilder();
            List<Kind> kindList = ge.getKindList();
            for (Kind k : kindList) {
                kindInfo.append(k.getName()).append(":");
                for (EGoodsEntry.Spec ivp : k.getList()) {
                    if (kinds.contains(ivp.getSpecId())) {
                        kindInfo.append(ivp.getSpecName()).append("	");
                        ecg.setPrice(ecg.getPrice() + ivp.getSpecPrice());
                    }
                }
            }
            ecg.setObjKinds(MongoUtils.convertToStr(kinds));
            ecg.setKind(kindInfo.toString());
        }

        //取出已经存在的
        List<EBusinessCartGoods> existsGoodsList = getGoodsFromCars(userId, -1);

        boolean isContain = false;
        for (EBusinessCartGoods ecgds : existsGoodsList) {
            if (ecgds.getId().equals(goodsId.toString())) {
                if (null == kinds || kinds.size() == 0) {
                    isContain = true;
                } else {
                    if (StringUtils.isNotBlank(ecgds.getObjKinds())) {
                        List<ObjectId> existsKindIds = MongoUtils.convert(ecgds.getObjKinds());

                        if (existsKindIds.size() == kinds.size()) {
                            Set<ObjectId> kindSet = new HashSet<ObjectId>(existsKindIds);
                            kindSet.addAll(kinds);
                            if (kindSet.size() == existsKindIds.size()) {
                                isContain = true;
                            }
                        }
                    }
                }


                if (isContain) {
                    ecgds.setCount(ecgds.getCount() + count);
                    ecgds.setType(1);
                    break;
                }
            }
        }

        if (!isContain) {
            existsGoodsList.add(ecg);
        }
        //放入cache
        cacheCarts(userId, existsGoodsList);
        return existsGoodsList;
    }


    /**
     * 用户购物车
     *
     * @param userId
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<EBusinessCartGoods> getGoodsFromCars(ObjectId userId, int type) throws ClassNotFoundException, IOException {
        List<EBusinessCartGoods> retList = new ArrayList<EBusinessCartGoods>();
        String key = CacheHandler.getKeyString(CacheHandler.CACHE_ESHOPPING_CARS, userId.toString());
        ListObjectSerializable<EBusinessCartGoods> ser = new ListObjectSerializable<EBusinessCartGoods>();
        byte[] byteArr = CacheHandler.getBytesValue(key);
        if (null != byteArr) {
            List<EBusinessCartGoods> list = ser.deserialize(byteArr);
            if (null != list && list.size() > 0) {
                if (type == -1) {
                    retList.addAll(list);
                } else {
                    for (EBusinessCartGoods ecg : list) {
                        if (ecg.getType() == type) {
                            retList.add(ecg);
                        }
                    }
                }
            }
        }
        return retList;
    }


    /**
     * 从购物车中选中商品
     *
     * @param userId
     * @param selectEbcIds 选中商品的ebcId
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void selectGoodsFromCars(ObjectId userId, List<ObjectId> selectEbcIds) throws ClassNotFoundException, IOException {
        List<EBusinessCartGoods> retList = new ArrayList<EBusinessCartGoods>();
        String key = CacheHandler.getKeyString(CacheHandler.CACHE_ESHOPPING_CARS, userId.toString());
        ListObjectSerializable<EBusinessCartGoods> ser = new ListObjectSerializable<EBusinessCartGoods>();
        byte[] byteArr = CacheHandler.getBytesValue(key);
        if (null != byteArr) {
            List<EBusinessCartGoods> list = ser.deserialize(byteArr);
            if (null != list && list.size() > 0) {
                for (EBusinessCartGoods ecg : list) {
                    if (selectEbcIds.contains(new ObjectId(ecg.getEbcId()))) {
                        ecg.setType(Constant.ONE);
                    } else {
                        ecg.setType(Constant.ZERO);
                    }
                    retList.add(ecg);
                }
            }
        }
        cacheCarts(userId, retList);
    }

    /**
     * 从购物车中删除已经购买的商品
     *
     * @param userId
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void deleteBuyedGoodsFromCars(ObjectId userId) throws ClassNotFoundException, IOException {
        List<EBusinessCartGoods> retList = new ArrayList<EBusinessCartGoods>();
        String key = CacheHandler.getKeyString(CacheHandler.CACHE_ESHOPPING_CARS, userId.toString());
        ListObjectSerializable<EBusinessCartGoods> ser = new ListObjectSerializable<EBusinessCartGoods>();
        byte[] byteArr = CacheHandler.getBytesValue(key);
        if (null != byteArr) {
            List<EBusinessCartGoods> list = ser.deserialize(byteArr);
            if (null != list && list.size() > 0) {
                for (EBusinessCartGoods ecg : list) {
                    if (ecg.getType() == 0) {
                        retList.add(ecg);
                    }
                }
            }
        }
        cacheCarts(userId, retList);
    }

    /**
     * 删除购物车商品
     *
     * @param userId
     * @param ebcId
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public List<EBusinessCartGoods> deleteEGoods(ObjectId userId, ObjectId ebcId) throws ClassNotFoundException, IOException {
        List<EBusinessCartGoods> existsGoodsList = getGoodsFromCars(userId, -1);
        List<EBusinessCartGoods> newGoodsList = new ArrayList<EBusinessCartGoods>();
        if (null != existsGoodsList && existsGoodsList.size() > 0) {
            for (EBusinessCartGoods ecg : existsGoodsList) {
                if (!ecg.getEbcId().toString().equals(ebcId.toString())) {
                    newGoodsList.add(ecg);
                }
            }
            cacheCarts(userId, newGoodsList);
        }
        return newGoodsList;
    }

    /**
     * 批量删除购物车商品
     */
    public List<EBusinessCartGoods> deleteEGoods(ObjectId userId, String ebcIds) throws ClassNotFoundException, IOException {
        List<EBusinessCartGoods> existsGoodsList = getGoodsFromCars(userId, -1);
        List<EBusinessCartGoods> newGoodsList = new ArrayList<EBusinessCartGoods>();
        if (null != existsGoodsList && existsGoodsList.size() > 0) {
            for (EBusinessCartGoods ecg : existsGoodsList) {
                if (!ebcIds.contains(ecg.getEbcId().toString())) {
                    newGoodsList.add(ecg);
                }
            }
            cacheCarts(userId, newGoodsList);
        }
        return newGoodsList;
    }


    /**
     * 修改数量
     *
     * @param userId
     * @param ebcId
     * @param count
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void updateCount(ObjectId userId, ObjectId ebcId, int count) throws ClassNotFoundException, IOException {
        List<EBusinessCartGoods> existsGoodsList = getGoodsFromCars(userId, -1);
        if (null != existsGoodsList && existsGoodsList.size() > 0) {
            for (EBusinessCartGoods ecg : existsGoodsList) {
                if (ecg.getEbcId().toString().equals(ebcId.toString())) {
                    ecg.setCount(ecg.getCount() + count);
                    cacheCarts(userId, existsGoodsList);
                    break;
                }
            }
        }
    }


    public void cacheCarts(ObjectId userId,
                           List<EBusinessCartGoods> existsGoodsList) throws IOException {
        ListObjectSerializable<EBusinessCartGoods> ser = new ListObjectSerializable<EBusinessCartGoods>();
        byte[] bytes = ser.serialize(existsGoodsList);
        String key = CacheHandler.getKeyString(CacheHandler.CACHE_ESHOPPING_CARS, userId.toString());
        CacheHandler.cache(key, bytes, Integer.MAX_VALUE);
    }


}
