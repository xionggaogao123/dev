package com.fulaan.entry;

import com.fulaan.pojo.FLoginLog;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by jerry on 2016/9/21.
 * 登录日志 Entry
 * nm-----------名字
 * nk-----------昵称
 * ti-----------登录时间
 * ip-----------登录ip地址
 * pf-----------登录平台
 */
public class FLoginLogEntry extends BaseDBObject {


    public FLoginLogEntry(DBObject dbo){
        super((BasicDBObject) dbo);
    }

    public FLoginLogEntry(FLoginLog loginLog) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("nm", loginLog.getUserName())
                .append("nk", loginLog.getNickName())
                .append("ti", loginLog.getLoginTime())
                .append("ip", loginLog.getIp())
                .append("pf", loginLog.getPf().getName());
        setBaseEntry(dbo);
    }

    public String getUserName(){
        return getSimpleStringValue("nm");
    }

    public String getUserNick(){
        return getSimpleStringValue("nk");
    }

    public long getLoginTime(){
        return getSimpleLongValue("ti");
    }

    public String getLoginIp(){
        return getSimpleStringValue("ip");
    }

    public String getLoginPf(){
        return getSimpleStringValue("pf");
    }



}
