package com.pojo.ebusiness;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 运费模板
 * Created by Wangkaidong on 2016/3/9.
 * nm : 模板名称
 * dts : 运费详情
 */
public class EExpressTemplateEntry extends BaseDBObject{

    public EExpressTemplateEntry(){}

    public EExpressTemplateEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public EExpressTemplateEntry(String name, List<ExpTempDetail> details){
        List<DBObject> list = MongoUtils.fetchDBObjectList(details);
        BasicDBObject baseEntry = new BasicDBObject()
                .append("nm", name)
                .append("dts",MongoUtils.convert(list));
        setBaseEntry(baseEntry);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm",name);
    }

    public List<ExpTempDetail> getDetails() {
        List<ExpTempDetail> list = new ArrayList<ExpTempDetail>();
        BasicDBList dbList = (BasicDBList)getSimpleObjectValue("dts");
        if(null!=dbList && !dbList.isEmpty())
        {
            for(Object o:dbList)
            {
                list.add( new ExpTempDetail((BasicDBObject)o)   );
            }
        }

        return list;
    }

    public void setDetails(List<ExpTempDetail> details) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(details);
        setSimpleValue("dts", MongoUtils.convert(list));
    }

    /**
     * 运费详情
     * zn : 省份编号
     * znm : 省份名称
     * fp : 首件费用
     * aop : 续件每件费用
     */
    public static class ExpTempDetail extends BaseDBObject{

        public ExpTempDetail(){}

        public ExpTempDetail(BasicDBObject baseEntry){
            super(baseEntry);
        }

        public  ExpTempDetail(String zoneNo,String zoneName,double firstPrice,double addOnePrice){
            BasicDBObject baseEntry = new BasicDBObject()
                    .append("zn",zoneNo)
                    .append("znm",zoneName)
                    .append("fp", firstPrice)
                    .append("aop",addOnePrice);
            setBaseEntry(baseEntry);
        }

        public String getZoneNo() {
           return getSimpleStringValue("zn");
        }

        public void setZoneNo(String zoneNo) {
            setSimpleValue("zn",zoneNo);
        }

        public String getZoneName() {
            return getSimpleStringValue("znm");
        }

        public void setZoneName(String zoneName) {
            setSimpleValue("znm",zoneName);
        }

        public double getFirstPrice() {
            return getSimpleDoubleValue("fp");
        }

        public void setFirstPrice(double firstPrice) {
            setSimpleValue("fp",firstPrice);
        }

        public double getAddOnePrice() {
            return getSimpleDoubleValue("aop");
        }

        public void setAddOnePrice(double addOnePrice) {
            setSimpleValue("aop",addOnePrice);
        }
    }
}
