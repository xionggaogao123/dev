package com.fulaan.lancustom.service;

import cn.jiguang.commom.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.db.lancustom.MonetaryAddrDao;
import com.db.lancustom.MonetaryGoodsDao;
import com.db.lancustom.MonetaryOrdersDao;
import com.db.user.UserDao;
import com.fulaan.integralmall.dto.WuliuInfoDto;
import com.fulaan.integralmall.dto.wuliuDto;
import com.fulaan.lancustom.dto.MonetaryOrdersDto;
import com.fulaan.mall.service.EBusinessOrderService;
import com.mongodb.BasicDBObject;
import com.pojo.lancustom.MonetaryAddrEntry;
import com.pojo.lancustom.MonetaryGoodsEntry;
import com.pojo.lancustom.MonetaryOrdersEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.apache.poi.ss.formula.functions.Now;
import org.bson.types.ObjectId;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 15:58
 * @Description:
 */
@Service
public class MonetaryOrdersService {


    @Autowired
    private EBusinessOrderService eBusinessOrderService;

    private MonetaryGoodsDao goodsDao = new MonetaryGoodsDao();

    private MonetaryAddrDao addrDao = new MonetaryAddrDao();

    private MonetaryOrdersDao ordersDao = new MonetaryOrdersDao();

    private UserDao userDao = new UserDao();

    /**
     * 保存订单
     * @param map
     */
    public String saveOrder(MonetaryOrdersDto ordersDto) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date now = new Date();
        String message = "";

        if (StringUtils.isEmpty(ordersDto.getOrderId())) {
            MonetaryGoodsEntry goodsEntry = goodsDao.getEntryById(new ObjectId(ordersDto.getGoodId()));
            //生成订单号
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            String orderNoTemp = "" + c.get(Calendar.YEAR) + (c.get(Calendar.MONTH) + 1) + c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + (int) Math.random() * 900;
//            //获取addressId 目前设计 一个用户只有一个
            MonetaryAddrEntry addrEntry = addrDao.getEntryByUid(new ObjectId(ordersDto.getUserId()));
            MonetaryOrdersEntry ordersEntry = new MonetaryOrdersEntry(new ObjectId(ordersDto.getUserId()), new ObjectId(ordersDto.getGoodId()), addrEntry==null?null:addrEntry.getID(),
                    ordersDto.getGoodNum(), ordersDto.getStyle(), Double.parseDouble((goodsEntry.getMoney()*ordersDto.getGoodNum())+""), orderNoTemp, "", "", f.format(now), "0", "", "","","0","","0","");
            message = ordersDao.addEntry(ordersEntry);
        } else {
//            ordersDao.updateOrder(map);
        }
        return message;
    }

    public MonetaryOrdersDto orderConfirm(ObjectId goodId, ObjectId userId) {
        MonetaryGoodsEntry goodsEntry = goodsDao.getEntryById(goodId);
        MonetaryAddrEntry addrEntry = addrDao.getEntryByUid(userId);

        MonetaryOrdersDto ordersDto;
        if (addrEntry != null) {
            ordersDto = new MonetaryOrdersDto(goodsEntry,addrEntry);
        }else {
            ordersDto = new MonetaryOrdersDto(goodsEntry);
        }

        return ordersDto;
    }

    public List<MonetaryOrdersDto> getMonetaryPersonalOrderList(Map map) {
        List<MonetaryOrdersEntry> ordersEntries = ordersDao.getMonetaryOrderList(map);
        List<MonetaryOrdersDto> dtoList = new ArrayList<MonetaryOrdersDto>();
        for (MonetaryOrdersEntry ordersEntry : ordersEntries) {
            MonetaryOrdersDto ordersDto = new MonetaryOrdersDto(ordersEntry);
            dtoList.add(ordersDto);
        }
        return dtoList;
    }

    /**
     * 获取物流信息
     * @param orderId
     * @param userId
     * @return
     */
    public WuliuInfoDto wuLiuInfo(ObjectId orderId, ObjectId userId) {
        MonetaryOrdersEntry orderEntry = ordersDao.getEntryById(orderId);
        MonetaryAddrEntry addressEntry = addrDao.getEntryById(orderEntry.getAid());
        String s = eBusinessOrderService.getExpressList(orderEntry.getExcompanyNo(), orderEntry.getExpressNo());
        wuliuDto w = JSON.parseObject(s, new TypeReference<wuliuDto>() {});
        WuliuInfoDto wuliuInfoDto = new WuliuInfoDto(orderEntry, w, addressEntry);
        return wuliuInfoDto;
    }

    public void updatePayOrder(String orderId, String tradeNo, String payOrderTimeStr, String payMethod) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderId",orderId);
        map.put("tradeNo",tradeNo);
        map.put("payOrderTimeStr",payOrderTimeStr);
        map.put("payMethod",payMethod);
        ordersDao.updateOrder(map);
    }

    public List<MonetaryOrdersDto> getOrderList(String orderNo, int page, int pageSize) {
        List<MonetaryOrdersDto> orderL = new ArrayList<MonetaryOrdersDto>();
        List<MonetaryOrdersEntry> list = ordersDao.getOrderList(orderNo,page, pageSize);
        for (MonetaryOrdersEntry ordersEntry : list) {
            MonetaryGoodsEntry goodsEntry = goodsDao.getEntryById(ordersEntry.getGid());
            UserEntry userEntry = userDao.getUserEntry(ordersEntry.getUid(), new BasicDBObject());
            if (goodsEntry == null) {
                goodsEntry = new MonetaryGoodsEntry();
            }
            MonetaryAddrEntry addrEntry = addrDao.getEntryById(ordersEntry.getAid());
            if (addrEntry == null) {
                addrEntry = new MonetaryAddrEntry();
            }
            MonetaryOrdersDto d = new MonetaryOrdersDto(goodsEntry, addrEntry, ordersEntry, userEntry);
            orderL.add(d);
        }
        return orderL;
    }

    public Object getOrderListAll(String orderNo) {
        List<MonetaryOrdersEntry> list = ordersDao.getOrderListAll(orderNo);
        return list.size();
    }

    public void saveOrderLogisticsInfo(String orderId, String excompanyNo, String expressNo) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderId",orderId);
        map.put("excompanyNo",excompanyNo);
        map.put("expressNo",expressNo);
        if (orderId != null) {
            ordersDao.updateOrder(map);
        }
    }

    public void delOrder(ObjectId orderId) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderId",orderId);
        map.put("isr",Constant.ONE);
        if (orderId != null) {
            ordersDao.updateOrder(map);
        }
    }

    public void handleState(ObjectId orderId, int isAcceptance) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderId",orderId);
        map.put("isAcceptance",isAcceptance);
        if (orderId != null) {
            ordersDao.updateOrder(map);
        }
    }

    public MonetaryOrdersDto getMonetaryPersonalOrderDetail(Map<String,Object> pMap) {
        MonetaryOrdersEntry ordersEntry = ordersDao.getEntryById(new ObjectId(pMap.get("orderId").toString()));
//        MonetaryGoodsEntry goodsEntry = goodsDao.getEntryById(ordersEntry.getGid());
//        UserEntry userEntry = userDao.getUserEntry(ordersEntry.getUid(), new BasicDBObject());
        MonetaryAddrEntry addrEntry = addrDao.getEntryByUid(new ObjectId(pMap.get("userId").toString()));
        MonetaryOrdersDto ordersDto = new MonetaryOrdersDto(addrEntry, ordersEntry);
        return ordersDto;
    }

    public int getMonetaryPersonalOrderListCount(Map<String,Object> pMap) {
        return ordersDao.getMonetaryPersonalOrderListCount(pMap);
    }
}
