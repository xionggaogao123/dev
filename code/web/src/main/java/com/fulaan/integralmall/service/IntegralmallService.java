package com.fulaan.integralmall.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.db.integral.IntegralRecordDao;
import com.db.integralmall.AddressDao;
import com.db.integralmall.GoodsDao;
import com.db.integralmall.OrderDao;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.integralmall.dto.AddressDto;
import com.fulaan.integralmall.dto.GoodsDto;
import com.fulaan.integralmall.dto.IntegralmallHomeDto;
import com.fulaan.integralmall.dto.OrderDto;
import com.fulaan.integralmall.dto.WuliuInfoDto;
import com.fulaan.integralmall.dto.wuliuDto;
import com.fulaan.mall.service.EBusinessOrderService;
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
     *〈简述〉获得所有商品数量
     *〈详细描述〉
     * @author Administrator
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    public int getIntegralmallHomeNum() {
        
        return goodsDao.getGoodsListAll().size();
    }
    
    /**
     * 
     *〈简述〉商品列表
     *〈详细描述〉
     * @author Administrator
     * @param page
     * @param pageSize
     * @return
     */
    public List<GoodsDto> getGoodsList(int page,int pageSize) {
        List<GoodsEntry> list= goodsDao.getGoodsList(page, pageSize);
        List<GoodsDto> goodsList = new ArrayList<GoodsDto>();
        for (GoodsEntry g : list) {
            GoodsDto gd = new GoodsDto(g);
            goodsList.add(gd);
        }
        
        return goodsList;
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
        List<OrderEntry> list = orderDao.getOrderListByUserId(page, pageSize, userId);
        for (OrderEntry o : list) {
            GoodsEntry g = goodsDao.getEntryById(o.getGid());
            GoodsDto go = new GoodsDto(g, o);
            goodL.add(go);
        }
        return goodL;
    }
    
    
    /**
     * 
     *〈简述〉订单列表
     *〈详细描述〉
     * @author Administrator
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    public List<OrderDto> getOrderList(int page,int pageSize) {
        List<OrderDto> orderL = new ArrayList<OrderDto>();
        List<OrderEntry> list = orderDao.getOrderList(page, pageSize);
        for (OrderEntry o : list) {
            GoodsEntry g = goodsDao.getEntryById(o.getGid());
            if (g == null) {
                g = new GoodsEntry();
            }
            AddressEntry a = addressDao.getEntryById(o.getAid());
            if (a == null) {
                a = new AddressEntry();
            }
            OrderDto d = new OrderDto(g, a, o);
            orderL.add(d);
        }
        return orderL;
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
        GoodsEntry entry = goodsDao.getEntryById(goodId);
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
        GoodsEntry entry = goodsDao.getEntryById(goodId);
        
        AddressEntry addressEntry = addressDao.getEntry(userId);
        OrderDto orderDto;
        if (addressEntry != null) {
            orderDto = new OrderDto(entry, addressEntry, integralSufferEntry==null?0:integralSufferEntry.getScore());
        } else {
            orderDto = new OrderDto(entry, integralSufferEntry==null?0:integralSufferEntry.getScore());
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
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号 
        Matcher m = p.matcher(addressDto.getTelphone());
        if (m.matches()) {
            addressDao.updateAddressState(userId);
            AddressEntry entry = new AddressEntry(addressDto.getName(), addressDto.getTelphone(), addressDto.getArea(), addressDto.getDetail(), userId);
            addressDao.addEntry(entry);
        } else {
            throw new Exception("请输入正确的手机号码!");
        }
        
    }
    
    /**
     * 
     *〈简述〉编辑地址
     *〈详细描述〉
     * @author Administrator
     * @param goodId
     * @param userId
     * @return
     * @throws Exception
     */
    public AddressDto editAddress(ObjectId userId) throws  Exception{
        AddressDto addressDto = null;
        AddressEntry entry = addressDao.getEntry(userId);
        if (entry != null) {
            addressDto = new AddressDto(entry);
        }
        return addressDto;
        
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
            GoodsEntry gentry = goodsDao.getEntryById(goodId);
            goodsDao.updateGoodsTimes(goodId, gentry.getTimes()+1);
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
    
    /**
     * 
     *〈简述〉保存商品
     *〈详细描述〉
     * @author Administrator
     * @param dto
     */
    public void saveGoods(GoodsDto dto) throws Exception{
        if (StringUtils.isBlank(dto.getId())) {
            GoodsEntry entry = new GoodsEntry(dto.getAvatar(), dto.getPic(),dto.getLabel(), dto.getName(), dto.getCost(), dto.getTimes() == null ?0 :dto.getTimes(), dto.getDescription());
            goodsDao.addEntry(entry);
        } else {
            goodsDao.updateGood(new ObjectId(dto.getId()), dto.getAvatar(),dto.getPic(), dto.getLabel(), dto.getName(), dto.getCost(), dto.getDescription());
        }
        
    }
    
    
    /**
     * 
     *〈简述〉删除商品
     *〈详细描述〉
     * @author Administrator
     * @param dto
     */
    public void delGoods(ObjectId goodId) throws Exception{
        goodsDao.updateIsr(goodId);
        
    }
    
    /**
     * 
     *〈简述〉保存订单物流信息
     *〈详细描述〉
     * @author Administrator
     * @param dto
     */
    public void saveOrder(OrderDto dto) throws Exception{
        if (dto.getId() != null) {
            orderDao.updateEx(new ObjectId(dto.getId()), dto.getExcompanyNo(), dto.getExpressNo());
        } 
        
    }
}
