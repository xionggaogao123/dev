package com.pojo.wrongquestion;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2017/3/23.
 */
public class NameConvertResNameEntry extends BaseDBObject {
    private static final long serialVersionUID = 4971398085429536346L;

    public NameConvertResNameEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public NameConvertResNameEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public NameConvertResNameEntry(String resName, List<String> nameList) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("rnm", resName)//题库学科名称
                .append("nms", nameList)//学科名称集合
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public String getResName() {
        return getSimpleStringValue("rnm");
    }

    public void setResName(String resName) {
        setSimpleValue("rnm",resName);
    }

    public List<String> getNameList() {
        List<String> retList =new ArrayList<String>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("nms");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((String)o));
            }
        }
        return retList;
    }

    public void setNameList(List<String> nameList) {
        setSimpleValue("nms", nameList);
    }

}
