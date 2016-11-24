package com.pojo.configmanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by guojing on 2015/4/9.
 */
public class ConfigManageEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = 7105040754235610727L;

    public ConfigManageEntry(BasicDBObject dbo){setBaseEntry(dbo);}
    /**
     * 构造器
     *
     */
    public ConfigManageEntry(int code, String name,
                             String value)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("cd", code)
                .append("nm", name)
                .append("vl", value)
                ;
        setBaseEntry(dbo);
    }


    public int getCode() {
        return getSimpleIntegerValue("cd");
    }

    public void setCode(int code) {
        setSimpleValue("cd",code);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm",name);
    }

    public String getValue() {
        return getSimpleStringValue("vl");
    }

    public void setValue(String value) {
        setSimpleValue("vl",value);
    }
}
