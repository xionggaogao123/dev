package com.pojo.lancustom;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 手机维修价目entry
 * model 手机型号
 * costUrl 手机维修价目表
 */
public class PhoneCostEntry extends BaseDBObject {

    /**
     * @Fields serialVersionUID : TODO(目的和意义)
     */
    private static final long serialVersionUID = 1L;

    public PhoneCostEntry() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PhoneCostEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }

    public PhoneCostEntry(BasicDBObject baseEntry) {
        super(baseEntry);
        // TODO Auto-generated constructor stub
    }


//    public PhoneCostEntry(ObjectId userId,
//                          String mobileVersion,
//                          int serviceVersion,
//                          String problem,
//                          String aliPayNum,
//                          String name,
//                          String schoole,
//                          String classRoom,
//                          String contactNum,
//                          String orderTimeStr,
//                          String address,
//                          String excompanyNo,
//                          String expressNo) {
//
//        BasicDBObject dbObject = new BasicDBObject()
//            .append("uid", userId)
//            .append("mobileVersion", mobileVersion)
//            .append("serviceVersion", serviceVersion)
//            .append("problem", problem)
//            .append("aliPayNum", aliPayNum)
//            .append("name", name)
//            .append("schoole", schoole)
//            .append("classRoom", classRoom)
//            .append("contactNum", contactNum)
//            .append("address", address)
//            .append("status", Constant.ZERO)
//            .append("excompanyNo", excompanyNo)
//            .append("expressNo", expressNo)
//            .append("orderTimeStr", orderTimeStr)
//            .append("sta", Constant.ZERO)
//            .append("isr", Constant.ZERO);
//
//        setBaseEntry(dbObject);
//
//    }
    
    public String getModel(){
        return getSimpleStringValue("model");
    }

    public void setModel(String model){
        setSimpleValue("model",model);
    }
    
    public String getCostUrl(){
        return getSimpleStringValue("costUrl");
    }

    public void setCostUrl(String costUrl){
        setSimpleValue("costUrl",costUrl);
    }

}
