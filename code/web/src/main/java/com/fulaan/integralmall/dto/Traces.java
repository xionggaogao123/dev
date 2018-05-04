package com.fulaan.integralmall.dto;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 
 * <简述>物流信息
 * <详细描述>
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class Traces {

   

    //详情
    @JSONField(name="AcceptStation")
   private String acceptStation;
    //时间
    @JSONField(name="AcceptTime")
   private String acceptTime;
    public String getAcceptStation() {
        return acceptStation;
    }
    public void setAcceptStation(String acceptStation) {
        this.acceptStation = acceptStation;
    }
    public String getAcceptTime() {
        return acceptTime;
    }
    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }
   
   
    
    
}
