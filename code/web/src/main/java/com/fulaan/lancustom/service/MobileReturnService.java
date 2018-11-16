package com.fulaan.lancustom.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fulaan.lancustom.dto.PhoneCostDto;
import com.pojo.lancustom.PhoneCostEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.db.lancustom.mobileReturnDao;
import com.fulaan.integralmall.dto.WuliuInfoDto;
import com.fulaan.integralmall.dto.wuliuDto;
import com.fulaan.lancustom.dto.MobileReturnDto;
import com.fulaan.mall.service.EBusinessOrderService;
import com.pojo.lancustom.MobileReturnEntry;

@Service
public class MobileReturnService {

    mobileReturnDao mobileReturnDao = new mobileReturnDao();
    
    @Autowired
    private EBusinessOrderService eBusinessOrderService;
    
    public void saveMobileReturn(MobileReturnDto dto, ObjectId uid) throws Exception{
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.setOrderTimeStr(sdf.format(new Date()));
        MobileReturnEntry entry = dto.buildAddEntry(uid);
        mobileReturnDao.addEntry(entry);
    }
    
    public List<MobileReturnDto> getListAll(int page,int pageSize, ObjectId uid) throws Exception{
        List<MobileReturnEntry> entryList = mobileReturnDao.getList(page, pageSize, uid);
        List<MobileReturnDto> dtoList = new ArrayList<MobileReturnDto>();
        for (MobileReturnEntry entry : entryList) {
            MobileReturnDto dto = new MobileReturnDto(entry);
            dtoList.add(dto);
        }
        return dtoList;
    }
    
    public long getListCount(ObjectId uid) throws Exception{
        
        
        return mobileReturnDao.getList(uid);
    }
    
    public List<MobileReturnDto> getListAll(int page,int pageSize) throws Exception{
        List<MobileReturnEntry> entryList = mobileReturnDao.getListAll(page, pageSize);
        List<MobileReturnDto> dtoList = new ArrayList<MobileReturnDto>();
        for (MobileReturnEntry entry : entryList) {
            MobileReturnDto dto = new MobileReturnDto(entry);
            dtoList.add(dto);
        }
        return dtoList;
    }
    
    public long getListAllCount() throws Exception{
        
       
        return mobileReturnDao.getListAll();
    }
    
    /**
     * 
     *〈简述〉保存订单物流信息
     *〈详细描述〉
     * @author Administrator
     * @param dto
     */
    public void saveOrder(String id, String excompanyNo, String expressNo) throws Exception{
        if (id != null) {
            mobileReturnDao.updateEx(new ObjectId(id), excompanyNo, expressNo);
        } 
        
    }
    
    /**
     * 
     *〈简述〉更新状态
     *〈详细描述〉
     * @author Administrator
     * @param dto
     */
    public void handleState(ObjectId goodId, int state) throws Exception{
        mobileReturnDao.handleState(goodId, state);
        
    }
    
    /**
     * 
     *〈简述〉获得物流信息
     *〈详细描述〉
     * @author Administrator
     * @param orderId
     */
    public WuliuInfoDto wuLiuInfo(ObjectId orderId, ObjectId userId) throws  Exception{
        
        MobileReturnEntry entry = mobileReturnDao.getEntry(orderId);
 
        String s = eBusinessOrderService.getExpressList(entry.getExcompanyNo(), entry.getExpressNo());
        wuliuDto w = JSON.parseObject(s, new TypeReference<com.fulaan.integralmall.dto.wuliuDto>() {});
        WuliuInfoDto wuliuInfoDto = new WuliuInfoDto(entry, w);
        return wuliuInfoDto;
    }

    /**
     * 可维修手机型号列表
     * @return
     */
    public List<String> getAllPhoneModel() {
        List<PhoneCostEntry> entryList = mobileReturnDao.getAllPhoneModel();
        List<String> stringList = new ArrayList<String>();
        if (entryList.size() > 0){
            for (PhoneCostEntry phoneCostEntry : entryList){
                stringList.add(phoneCostEntry.getModel());
            }
        }
        return stringList;
    }

    /**
     *当前手机型号价目表
     * @param phoneModel
     * @return
     */
    public String getCostPicUrlByPhoneModel(String phoneModel) {
        String result = mobileReturnDao.getCostPicUrlByPhoneModel(phoneModel);
        return result;
    }

    /**
     * 手机型号维修价目列表
     * @return
     */
    public List<PhoneCostDto> getPhoneRepairCostList() {
        List<PhoneCostEntry> entryList = mobileReturnDao.getPhoneRepairCostList();
        List<PhoneCostDto> phoneCostDtos = new ArrayList<PhoneCostDto>();
        if (entryList.size() > 0){
            for (PhoneCostEntry phoneCostEntry : entryList){
                phoneCostDtos.add(new PhoneCostDto(phoneCostEntry));
            }
        }
        return phoneCostDtos;
    }
}
