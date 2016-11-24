package com.pojo.ebusiness;

/**
 * Created by wangkaidong on 2016/4/18.
 *
 *
 * 立即购买商品
 */
public class EBusinessGoods {
    private String id; //商品ID
    private String image;
    private String name;
    private String kind;//规格
    private String objKinds;//规格id
    private int experienceOff;
    private int voucherOff;
    private int price;
    private int count;


    public EBusinessGoods(){}

    public EBusinessGoods(EGoodsEntry entry, int count){
        this.id = entry.getID().toString();
        this.image = entry.getSuggestImage();
        this.name = entry.getName().replaceAll("[\r\n]"," ");
        this.experienceOff = entry.getExperienceOff();
        this.voucherOff = entry.getVoucherOff();
        this.price = entry.getDiscountPrice();
        this.count = count;
    }

    public EBusinessCartGoods toCartGoods(){
        EBusinessCartGoods cartGoods = new EBusinessCartGoods();
        cartGoods.setId(id);
        cartGoods.setImage(image);
        cartGoods.setName(name);
        cartGoods.setKind(kind);
        cartGoods.setObjKinds(objKinds);
        cartGoods.setPrice(price);
        cartGoods.setCount(count);
        return cartGoods;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getObjKinds() {
        return objKinds;
    }

    public void setObjKinds(String objKinds) {
        this.objKinds = objKinds;
    }

    public int getExperienceOff() {
        return experienceOff;
    }

    public void setExperienceOff(int experienceOff) {
        this.experienceOff = experienceOff;
    }

    public int getVoucherOff() {
        return voucherOff;
    }

    public void setVoucherOff(int voucherOff) {
        this.voucherOff = voucherOff;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSumPrice() {
        return price * count;
    }

    public String getPriceStr1(){
        if(this.price<=0){
            return "0.00";
        }
        Double d=Double.valueOf(String.valueOf(this.price))/100D;
        return d.toString();
    }

    public String getPriceStr() {
        if(this.price<=0){
            return "0.00";
        }
        Double d=Double.valueOf(String.valueOf(this.price*this.count))/100D;
        return d.toString();
    }
}
