package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.InOutStorageRecordDao;
import com.db.backstage.StorageManageDao;
import com.fulaan.backstage.dto.InOutStorageRecordDto;
import com.pojo.backstage.InOutStorageEntry;
import com.sys.constants.Constant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/25 10:44
 * @Description:
 */
@Service
public class BackStageDeliveryManageService {

    private InOutStorageRecordDao inOutStorageRecordDao = new InOutStorageRecordDao();

    private StorageManageDao storageManageDao = new StorageManageDao();

    /**
     * 状态下拉列表
     * @return
     */
    public JSONArray getDeliveryOptionList() {
        JSONArray jsonArray = new JSONArray();
        //待发货 没有物流信息
        List<InOutStorageEntry> inOutStorageEntryList1 = inOutStorageRecordDao.getDeliveryOptionList("will");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name","待发货("+inOutStorageEntryList1.size()+")");
        jsonObject1.put("value","will");
        jsonArray.add(jsonObject1);
        //已发货 有物流信息
        List<InOutStorageEntry> inOutStorageEntryList2 = inOutStorageRecordDao.getDeliveryOptionList("have");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name","已发货("+inOutStorageEntryList2.size()+")");
        jsonObject2.put("value","have");
        jsonArray.add(jsonObject2);
        return jsonArray;
    }

    /**
     * 发货管理列表信息
     * @param page
     * @param pageSize
     * @param inputParams
     * @param year
     * @param month
     * @param deliveryFlag
     * @return
     */
    public Map<String,Object> getDeliveryInfoList(int page, int pageSize, String inputParams, String year, String month, String deliveryFlag) {
        Map<String,Object> result = new HashMap<String, Object>();
        result = inOutStorageRecordDao.getDeliveryInfoList(page, pageSize, inputParams, year, month, deliveryFlag);
        List<InOutStorageRecordDto> inOutStorageRecordDtoList = new ArrayList<InOutStorageRecordDto>();
        List<InOutStorageEntry> inOutStorageEntryList = (ArrayList)result.get("entryList");
        for (InOutStorageEntry inOutStorageEntry : inOutStorageEntryList){
            if (inOutStorageEntry != null){
                inOutStorageRecordDtoList.add(new InOutStorageRecordDto(inOutStorageEntry));
            }
        }
        result.put("dtos",inOutStorageRecordDtoList);
        result.remove("entryList");
        return result;
    }

    /**
     * 根据唯一索引更新物流信息
     * @param map
     * @return
     */
    public String updateDeliveryLogisticsInfoById(Map map) {
        return inOutStorageRecordDao.updateDeliveryLogisticsInfoById(map);
    }

    /**
     * 根据唯一索引集合更新物流信息
     * @param map
     * @return
     */
    public String updateDeliveryLogisticsInfoByIds(Map map) {
        return inOutStorageRecordDao.updateDeliveryLogisticsInfoByIds(map);
    }

    /**
     * 发货管理-获取当前型号库存手机颜色
     * @return
     */
    public List<String> getCurrentModelColor(String phoneModel) {
        return storageManageDao.getCurrentModelColor(phoneModel);
    }

    /**
     * 发货管理-配置手机检测可用
     * @return
     */
    public int checkPhoneInfoValid(String imeiNo) {
        return storageManageDao.checkPhoneInfoValid(imeiNo);
    }

    /**
     * 发货管理-单个客户发货
     * parameter
     * imeiNo color excompanyNo expressNo inOutStorageId
     * @return
     */
    public String updateSingleCustomerDelivery(Map map) {
        String result = "";
        //更新库存中的imeiNo为出库状态
        result += storageManageDao.updateStorageInfoByImeiNo(map)+",";
        //更新出入库记录的手机 物流 信息
        result += inOutStorageRecordDao.updateDeliveryLogisticsInfoById(map);
        return result;
    }
}