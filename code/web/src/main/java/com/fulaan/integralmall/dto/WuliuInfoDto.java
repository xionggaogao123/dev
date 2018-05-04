package com.fulaan.integralmall.dto;

import java.util.List;

import com.pojo.integralmall.AddressEntry;
import com.pojo.integralmall.OrderEntry;

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
    
    public WuliuInfoDto(OrderEntry orderEntry, wuliuDto w, AddressEntry addressEntry) {
        this.area = addressEntry.getArea()+addressEntry.getDetail();
        this.excompanyNo = orderEntry.getExcompanyNo();
        this.expressNo = orderEntry.getExpressNo();
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
