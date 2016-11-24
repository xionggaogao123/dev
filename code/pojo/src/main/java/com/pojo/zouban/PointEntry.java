package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by qiangm on 2016/4/20.
 * X---->星期几
 * y---->第几节课
 */
public class PointEntry extends BaseDBObject{
    public PointEntry()
    {
        super();
    }
    public PointEntry(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public PointEntry(int x,int y)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("x",x)
                .append("y",y);
        setBaseEntry(basicDBObject);
    }
    public void setX(int x)
    {
        setSimpleValue("x",x);
    }
    public int getX()
    {
        return getSimpleIntegerValue("x");
    }

    public void setY(int y)
    {
        setSimpleValue("y",y);
    }
    public int getY()
    {
        return getSimpleIntegerValue("y");
    }
}
