package com.pojo.ebusiness;

import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/2/1.
 */
public class SpecDTO {
    private String id;
    private String name;
    private int price;

    public SpecDTO(){}
    public SpecDTO(String id, String name, int price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public SpecDTO(EGoodsEntry.Spec spec){
        this(spec.getSpecId().toString(), spec.getSpecName(), spec.getSpecPrice());
    }

    public String getId() {
        if(id.equals("")){
            return new ObjectId().toString();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
