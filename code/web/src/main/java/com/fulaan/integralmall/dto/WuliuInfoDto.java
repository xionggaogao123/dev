package com.fulaan.integralmall.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.integralmall.AddressEntry;
import com.pojo.integralmall.OrderEntry;
import com.pojo.lancustom.MobileReturnEntry;

/**
 * 
 * <简述>物流信息
 * <详细描述>
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class WuliuInfoDto {

    //收货地址
    private String area;
    //物流公司
    private String excompanyNo;
    //运单编号
    private String expressNo;
    //物流信息
    private List<Traces> traces;
    
    private Map<String, String> map = new HashMap<String, String>() {
        {
            put("SF", "顺丰速运");
            put("ZTO", "中通快递");
            put("STO", "申通快递");
            put("YTO", "圆通速递");
            put("YD", "韵达速递");
            put("EMS", "EMS");
            put("HTKY", "百世快递");
            put("HHTT", "天天快递");
        }
    };
    
    public WuliuInfoDto() {
      
    }
    
    public WuliuInfoDto(OrderEntry orderEntry, wuliuDto w, AddressEntry addressEntry) {
        this.area = addressEntry.getArea()+addressEntry.getDetail();
        this.excompanyNo = this.map.get(orderEntry.getExcompanyNo());
        this.expressNo = orderEntry.getExpressNo();
        this.traces=w.getTraces();
    }
    
    public WuliuInfoDto(MobileReturnEntry entry, wuliuDto w) {
        this.area = entry.getAddress();
        this.excompanyNo = this.map.get(entry.getExcompanyNo());
        this.expressNo = entry.getExpressNo();
        this.traces=w.getTraces();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getExcompanyNo() {
        return excompanyNo;
    }

    public void setExcompanyNo(String excompanyNo) {
        this.excompanyNo = excompanyNo;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public List<Traces> getTraces() {
        return traces;
    }

    public void setTraces(List<Traces> traces) {
        this.traces = traces;
    }
    
    
}
