package com.pojo.ebusiness;

import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
public class EGoodsDayLog {
    private String date;
    private List<EGoodsLogDTO> goodsLogList;

    public EGoodsDayLog(){}

    public EGoodsDayLog(String date, List<EGoodsLogDTO> goodsLogList){
        this.date = date;
        this.goodsLogList = goodsLogList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<EGoodsLogDTO> getGoodsLogList() {
        return goodsLogList;
    }

    public void setGoodsLogList(List<EGoodsLogDTO> goodsDTOList) {
        this.goodsLogList = goodsDTOList;
    }
}
