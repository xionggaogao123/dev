package com.pojo.ebusiness;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by wangkaidong on 2016/4/8.
 *
 *
 * 商品年级分类 1-12
 * nm: name
 * st: sort
 */
public class EGradeCategoryEntry extends BaseDBObject{
    public EGradeCategoryEntry(){}

    public EGradeCategoryEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm",name);
    }

    public int getSort(){
        return getSimpleIntegerValue("st");
    }

    public void setSort(int sort){
        setSimpleValue("st",sort);
    }


}
