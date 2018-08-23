package com.fulaan.excellentCourses.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-26.
 */
public class HourResultDTO {
    private List<HourClassDTO> dtos =new ArrayList<HourClassDTO>();
    private String parentId;
    private int count;
    private int type;
    private double price;

    public List<HourClassDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<HourClassDTO> dtos) {
        this.dtos = dtos;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
