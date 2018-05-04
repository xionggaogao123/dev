package com.fulaan.integralmall.dto;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class wuliuDto {

    @JSONField(name="LogisticCode") 
       private String logisticCode;
    @JSONField(name="ShipperCode") 
       private String shipperCode;
    @JSONField(name="Traces")
       private List<Traces> traces;
    @JSONField(name="State")
       private String state;
    @JSONField(name="EBusinessID")
       private String eBusinessID;
    @JSONField(name="Success")
       private boolean success;
    public String getLogisticCode() {
        return logisticCode;
    }
    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }
    public String getShipperCode() {
        return shipperCode;
    }
    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }
    public List<Traces> getTraces() {
        return traces;
    }
    public void setTraces(List<Traces> traces) {
        this.traces = traces;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String geteBusinessID() {
        return eBusinessID;
    }
    public void seteBusinessID(String eBusinessID) {
        this.eBusinessID = eBusinessID;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
       
       
   
    
    
    
}
