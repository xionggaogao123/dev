package com.pojo.backstage;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import javafx.beans.binding.When;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by taoto.chan on 2018年9月18日09:53:59
 * id		主键                         ObjectId
 * imeiNo   IMEI码   String
 * phoneModel   手机型号  String
 * color    颜色  String
 * manufacturer 厂商  String
 * inStorageTime  入库时间    String
 * inStorageYear  入库年    String
 * inStorageMonth  入库月    String
// * outStorageTime  出库库时间    String
// * outStorageYear  出库年    String
// * outStorageMonth  出库月    String
 * storageStatus 入库状态( 参考小兰退货 "serviceVersion":2, 维修(不入库)    "serviceVersion":1, 退货  "serviceVersion":3, 换货     4 回收入库     0 新机入库 5 出库) String
 * comment 备注   String
 * useStatus	是否可用状态(0不可用 1可用)    String
 * commentType 备注类型   String
 * needRepairComment    故障维修    List<String>
 */
public class StorageManageEntry extends BaseDBObject {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Calendar calendar = Calendar.getInstance();

    public StorageManageEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject) dbObject);
    }

    public StorageManageEntry() {
    }


    //新增构造
    public StorageManageEntry(String imeiNo,
                              String phoneModel,
                              String color,
                              String manufacturer,
                              String storageStatus,
                              String comment,
                              String useStatus,
                              String commentType,
                              List<String> needRepairComment
    ) {
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("imeiNo", imeiNo)
                .append("phoneModel", phoneModel)
                .append("color", color)
                .append("manufacturer", manufacturer)
                .append("inStorageTime", dateFormat.format(new Date()))
                .append("inStorageYear", calendar.get(Calendar.YEAR)+"")
                .append("inStorageMonth", (calendar.get(Calendar.MONTH)+1)+"")
//                .append("outStorageTime", "")
//                .append("outStorageYear", "")
//                .append("outStorageMonth", "")
                .append("storageStatus", storageStatus)
                .append("comment", comment)
                .append("useStatus", useStatus)
                .append("commentType", commentType)
                .append("needRepairComment", needRepairComment)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }


    public String getImeiNo() {
        return getSimpleStringValue("imeiNo");
    }

    public void setImeiNo(String imeiNo) {
        setSimpleValue("imeiNo", imeiNo);
    }


    public String getPhoneModel() {
        return getSimpleStringValue("phoneModel");
    }

    public void setPhoneModel(String phoneModel) {
        setSimpleValue("phoneModel", phoneModel);
    }


    public String getColor() {
        return getSimpleStringValue("color");
    }

    public void setColor(String color) {
        setSimpleValue("color", color);
    }

    public String getManufacturer() {
        return getSimpleStringValue("manufacturer");
    }

    public void setManufacturer(String manufacturer) {
        setSimpleValue("manufacturer", manufacturer);
    }

    public String getInStorageTime() {
        return getSimpleStringValue("inStorageTime");
    }

    public String getInStorageYear() {
        return getSimpleStringValue("inStorageYear");
    }

    public String getInStorageMonth() {
        return getSimpleStringValue("inStorageMonth");
    }

    public void setInStorageTime(String inStorageTime) {
        setSimpleValue("inStorageTime", inStorageTime);
    }

    public String getStorageStatus() {
        return getSimpleStringValue("storageStatus");
    }

    public void setStorageStatus(String storageStatus) {
        setSimpleValue("storageStatus", storageStatus);
    }

    public String getComment() {
        return getSimpleStringValue("comment");
    }

    public void setComment(String comment) {
        setSimpleValue("comment", comment);
    }

    public String getUseStatus() {
        return getSimpleStringValue("useStatus");
    }

    public void setUseStatus(String useStatus) {
        setSimpleValue("useStatus", useStatus);
    }

    public String getCommentType() {
        return getSimpleStringValue("commentType");
    }

    public void setCommentType(String commentType) {
        setSimpleValue("commentType", commentType);
    }

    public List<String> getNeedRepairComment() {
        return (ArrayList)getSimpleObjectValue("needRepairComment");
    }

    public void setNeedRepairComment(List<String> needRepairComment) {
        setSimpleValue("needRepairComment", needRepairComment);
    }

}
