package com.pojo.ebusiness;

/**
 * Created by wangkaidong on 2016/4/12.
 */
public class EGoodsCollection {
    private String id;
    private String goodsId;

    public EGoodsCollection(EGoodsLogEntry entry){
        this.id = entry.getID().toString();
        this.goodsId = entry.getGoodsId().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
