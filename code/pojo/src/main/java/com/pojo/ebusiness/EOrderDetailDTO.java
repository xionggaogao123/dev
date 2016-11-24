package com.pojo.ebusiness;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class EOrderDetailDTO {
    private List<EOrderDetailGoods> goodsList = new ArrayList<EOrderDetailGoods>();
    private int expressPrice;//运费
    private int exp;//积分
    private int voff;//抵用券
    private int totalPrice;//订单总价
    private String name;//收货人
    private String address;//收货地址
    private String tel;//电话


    public List<EOrderDetailGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<EOrderDetailGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(int expressPrice) {
        this.expressPrice = expressPrice;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getVoff() {
        return voff;
    }

    public void setVoff(int voff) {
        this.voff = voff;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public static class EOrderDetailGoods{
        private String id;
        private String goodsName;
        private String kind;
        private String img;
        private int count;
        private int price;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getPriceStr(){
            return String.valueOf(Double.valueOf(String.valueOf(price)) / 100.0);
        }

        public String getSumPriceStr(){
            return String.valueOf(Double.valueOf(String.valueOf(price * count)) / 100.0);
        }
    }

}
