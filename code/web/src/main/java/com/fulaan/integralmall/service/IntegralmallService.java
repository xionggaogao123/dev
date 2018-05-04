package com.fulaan.integralmall.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.db.integral.IntegralRecordDao;
import com.db.integralmall.AddressDao;
import com.db.integralmall.GoodsDao;
import com.db.integralmall.OrderDao;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.integralmall.dto.AddressDto;
import com.fulaan.integralmall.dto.GoodsDto;
import com.fulaan.integralmall.dto.IntegralmallHomeDto;
import com.fulaan.integralmall.dto.OrderDto;
import com.fulaan.integralmall.dto.WuliuInfoDto;
import com.fulaan.integralmall.dto.wuliuDto;
import com.fulaan.mall.service.EBusinessOrderService;
import com.fulaan.util.DateUtils;
import com.pojo.integral.IntegralRecordEntry;
import com.pojo.integral.IntegralSufferEntry;
import com.pojo.integralmall.AddressEntry;
import com.pojo.integralmall.GoodsEntry;
import com.pojo.integralmall.OrderEntry;
import com.sys.constants.Constant;

@Service
public class IntegralmallService {


    private GoodsDao goodsDao = new GoodsDao();
    

    private OrderDao orderDao = new OrderDao();
    

    private AddressDao addressDao = new AddressDao();
    
    @Autowired
    private IntegralSufferService integralSufferService;
    
    @Autowired
    private EBusinessOrderService eBusinessOrderService;
    

    private IntegralRecordDao integralRecordDao = new IntegralRecordDao();
    
    /**
     * 
     *〈简述〉积分商城首页数据
     *〈详细描述〉
     * @author Administrator
     * @param page
     * @param pageSize
     * @return
     */
    public IntegralmallHomeDto getIntegralmallHome(int page,int pageSize,ObjectId userId) {
        IntegralSufferEntry integralSufferEntry = integralSufferService.getEntry(userId);
        IntegralmallHomeDto dto = new IntegralmallHomeDto();
        List<GoodsEntry> list= goodsDao.getGoodsList(page, pageSize);
        List<GoodsDto> goodsList = new ArrayList<GoodsDto>();
        for (GoodsEntry g : list) {
            GoodsDto gd = new GoodsDto(g);
            goodsList.add(gd);
        }
        dto.setGoodsList(goodsList);
        dto.setScore(integralSufferEntry.getScore());
        return dto;
    }
    

    /**
     * 
     *〈简述〉兑换记录
     *〈详细描述〉
     * @author Administrator
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    public List<GoodsDto> getExchangeRecord(int page,int pageSize,ObjectId userId) {
        List<GoodsDto> goodL = new ArrayList<GoodsDto>();
        List<OrderEntry> list = orderDao.getOrderList(page, pageSize, userId);
        for (OrderEntry o : list) {
            GoodsEntry g = goodsDao.getEntry(o.getGid());
            GoodsDto go = new GoodsDto(g, o);
            goodL.add(go);
        }
        return goodL;
    }
    
    /**
     * 
     *〈简述〉订单申述提交
     *〈详细描述〉
     * @author Administrator
     * @param orderId
     * @param stateReason
     */
    public void stateSave(ObjectId orderId,String stateReason) throws Exception{
        orderDao.updateOrderState(orderId, stateReason);
    }
    
    /**
     * 
     *〈简述〉
     *〈详细描述〉
     * @author Administrator
     * @param goodId
     * @return
     */
    public GoodsDto getGoodById(ObjectId goodId) throws Exception{
        GoodsEntry entry = goodsDao.getEntry(goodId);
        GoodsDto goodsDto = new GoodsDto(entry);
        return goodsDto;
    }
    
    /**
     * 订单确认
     *〈简述〉
     *〈详细描述〉
     * @author Administrator
     * @param goodId
     */
    public OrderDto orderConfirm(ObjectId goodId, ObjectId userId) throws  Exception{
        IntegralSufferEntry integralSufferEntry = integralSufferService.getEntry(userId);
        GoodsEntry entry = goodsDao.getEntry(goodId);
        goodsDao.updateGoodsTimes(goodId, entry.getTimes()+1);
        AddressEntry addressEntry = addressDao.getEntry(userId);
        OrderDto orderDto;
        if (addressEntry != null) {
            orderDto = new OrderDto(entry, addressEntry, integralSufferEntry.getScore());
        } else {
            orderDto = new OrderDto(entry, integralSufferEntry.getScore());
        }
        
        return orderDto;
    }
    
    /**
     * 
     *〈简述〉保存地址
     *〈详细描述〉
     * @author Administrator
     * @param goodId
     * @param userId
     * @return
     * @throws Exception
     */
    public void saveAddress(AddressDto addressDto, ObjectId userId) throws  Exception{
        addressDao.updateAddressState(userId);
        AddressEntry entry = new AddressEntry(addressDto.getName(), addressDto.getTelphone(), addressDto.getArea(), addressDto.getDetail(), userId);
        addressDao.addEntry(entry);
    }
    
    /**
     * 
     *〈简述〉保存订单
     *〈详细描述〉
     * @author Administrator
     * @param goodNum
     * @param goodId
     */
    public void saveOrder(int goodNum, ObjectId goodId, ObjectId addressId,ObjectId userId,Integer costScore) throws  Exception{
        IntegralSufferEntry integralSufferEntry = integralSufferService.getEntry(userId);
        if (integralSufferEntry.getScore() < costScore) {
            throw  new Exception("积分不够!");
        } else {
            //增加积分记录
            IntegralRecordEntry entry = new IntegralRecordEntry(userId, costScore, "divCost", new Date().getTime(), new Date().getTime(), 0);
            integralRecordDao.addEntry(entry);
            //扣积分
            integralSufferService.divCostEntry(userId, costScore);
            //新建订单
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setAid(addressId);
            orderEntry.setGid(goodId);
            orderEntry.setGoodNum(goodNum);
            orderEntry.setUid(userId);
            orderEntry.setIsRemove(Constant.ZERO);
            Date now = new  Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            orderEntry.setOrderTimeStr(f.format(now));
            orderDao.addEntry(orderEntry);
        }
        
    }
    
    /**
     * 
     *〈简述〉获得物流信息
     *〈详细描述〉
     * @author Administrator
     * @param orderId
     */
    public WuliuInfoDto wuLiuInfo(ObjectId orderId, ObjectId userId) throws  Exception{
        
        OrderEntry orderEntry = orderDao.getEntry(orderId);
        AddressEntry addressEntry = addressDao.getEntryById(orderEntry.getAid());
        String s = eBusinessOrderService.getExpressList(orderEntry.getExcompanyNo(), orderEntry.getExpressNo());
        wuliuDto w = JSON.parseObject(s, new TypeReference<wuliuDto>() {});
        WuliuInfoDto wuliuInfoDto = new WuliuInfoDto(orderEntry, w, addressEntry);
        return wuliuInfoDto;
    }
}
