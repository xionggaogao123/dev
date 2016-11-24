package com.pojo.ebusiness;

/**
 * Created by wangkaidong on 2016/4/12.
 */
public class EGoodsLogDTO extends EGoodsDTO{
    private String id;

    public EGoodsLogDTO(String id,EGoodsEntry goodsEntry){
        super(goodsEntry);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
