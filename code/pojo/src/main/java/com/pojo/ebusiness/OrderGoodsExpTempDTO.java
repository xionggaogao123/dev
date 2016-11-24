package com.pojo.ebusiness;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/3/16.
 */
public class OrderGoodsExpTempDTO {
    private String address;
    private List<OrderGoodsExpTempDTO.OrderGoodsExpTemp> goodsList = new ArrayList<OrderGoodsExpTempDTO.OrderGoodsExpTemp>();


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderGoodsExpTempDTO.OrderGoodsExpTemp> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoodsExpTempDTO.OrderGoodsExpTemp> goodsList) {
        this.goodsList = goodsList;
    }

    public static class OrderGoodsExpTemp{
        private String goodsId;
        private int count;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
