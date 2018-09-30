package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by taoto.chan on 2018年9月20日10:26:00
 * id		主键                         ObjectId
 * imeiNo   IMEI码   String
 * phoneModel   手机型号  String
 * color    颜色  String
 * manufacturer 厂商  String
 * inStorageTime  入库时间    String
 * inStorageYear  入库年    String
 * inStorageMonth  入库月    String
 * storageStatus 入库状态( 参考小兰退货 "serviceVersion":2, 维修(不入库)    "serviceVersion":1, 退货  "serviceVersion":3, 换货     4 回收入库     0 新机入库 5 出库) String
 * comment 备注   String
 * commentType 备注类型   String
 * needRepairComment    故障维修    List<String>
 * <p>
 * 以上手机库存冗余字段
 * <p>
 * address		地址
 * outStorageTime  出库库时间    String
 * outStorageYear  出库年    String
 * outStorageMonth  出库月    String
 * deliveryTime 交付时间
 * excompanyNo 物流公司
 * expressNo	物流单号
 * deliveryMethod	发货方式
 * ================项目信息============
 * projectName	项目名称
 * projectDockPeople		项目对接人
 * schoolName	学校名称
 * accessClass	使用班级
 * accessObj	使用对象
 * contactInfo	联系方式
 * ===============个人信息===============
 * parentName	家长姓名
 * parentMobile	家长电话
 * parentId		家长Id
 * studentName	学生姓名
 * studentMobile	学生电话
 * studentId		学生Id
 * ================维修信息=================
 * recycleComment	回收备注
 * repairRange		维修范围
 * repairCost		维修价格
 * storageRecordStatus 记录出入库状态( 0 新机入库 参考小兰退货 "serviceVersion":1, 退货 "serviceVersion":2, 维修(入库)      "serviceVersion":3, 换货     4 回收入库    5 出库(数据存在物流信息表示发货))
 */
public class InOutStorageEntry extends BaseDBObject {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Calendar calendar = Calendar.getInstance();

    public InOutStorageEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject) dbObject);
    }

    public InOutStorageEntry() {
    }


    //新增构造
    public InOutStorageEntry(String imeiNo,
                             String phoneModel,
                             String color,
                             String manufacturer,
                             String inStorageTime,
                             String inStorageYear,
                             String inStorageMonth,
                             String storageStatus,
                             String comment,
                             String projectName,
                             String projectDockPeople,
                             String schoolName,
                             String accessClass,
                             String accessObj,
                             String contactInfo,
                             String address,
                             String deliveryTime,
                             String deliveryMethod,
                             String excompanyNo,
                             String expressNo,
                             String parentName,
                             String parentMobile,
                             String parentId,
                             String studentName,
                             String studentMobile,
                             String studentId,
                             String recycleComment,
                             String repairRange,
                             String repairCost,
                             String storageRecordStatus,
                             String commentType,
                             List<String> needRepairComment
    ) {
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("imeiNo", imeiNo)
                .append("phoneModel", phoneModel)
                .append("color", color)
                .append("manufacturer", manufacturer)
                .append("inStorageTime", inStorageTime)
                .append("inStorageYear", inStorageYear)
                .append("inStorageMonth", inStorageMonth)
                .append("storageStatus", storageStatus)
                .append("comment", comment)
                .append("projectName", projectName)
                .append("projectDockPeople", projectDockPeople)
                .append("schoolName", schoolName)
                .append("accessClass", accessClass)
                .append("accessObj", accessObj)
                .append("contactInfo", contactInfo)
                .append("address", address)
                .append("outStorageTime", dateFormat.format(new Date()))
                .append("outStorageYear", calendar.get(Calendar.YEAR)+"")
                .append("outStorageMonth", (calendar.get(Calendar.MONTH) + 1)+"")
                .append("deliveryTime", deliveryTime)
                .append("deliveryMethod", deliveryMethod)
                .append("excompanyNo", excompanyNo)
                .append("expressNo", expressNo)
                .append("parentName", parentName)
                .append("parentMobile", parentMobile)
                .append("parentId", parentId)
                .append("studentName", studentName)
                .append("studentMobile", studentMobile)
                .append("studentId", studentId)
                .append("recycleComment", recycleComment)
                .append("repairRange", repairRange)
                .append("repairCost", repairCost)
                .append("storageRecordStatus", storageRecordStatus)
                .append("commentType", commentType)
                .append("needRepairComment", needRepairComment)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

//    //更新构造
//    public InOutStorageEntry(ObjectId id,
//                             String projectName,
//                             String projectDockPeople,
//                             String schoolName,
//                             String accessClass,
//                             String accessObj,
//                             String contactInfo,
//                             String address
//
//    ) {
//        BasicDBObject basicDBObject = new BasicDBObject()
//                .append(Constant.ID, id)
//                .append("projectName", projectName)
//                .append("projectDockPeople", projectDockPeople)
//                .append("schoolName", schoolName)
//                .append("accessClass", accessClass)
//                .append("accessObj", accessObj)
//                .append("contactInfo", contactInfo)
//                .append("address", address)
//                .append("isr", Constant.ZERO);
//        setBaseEntry(basicDBObject);
//    }


    public String getImeiNo() {
        return getSimpleStringValue("imeiNo");
    }

    public String getPhoneModel() {
        return getSimpleStringValue("phoneModel");
    }

    public String getColor() {
        return getSimpleStringValue("color");
    }

    public String getManufacturer() {
        return getSimpleStringValue("manufacturer");
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

    public String getStorageStatus() {
        return getSimpleStringValue("storageStatus");
    }

    public String getComment() {
        return getSimpleStringValue("comment");
    }

    public String getProjectName() {
        return getSimpleStringValue("projectName");
    }

    public String getProjectDockPeople() {
        return getSimpleStringValue("projectDockPeople");
    }

    public String getSchoolName() {
        return getSimpleStringValue("schoolName");
    }

    public String getAccessClass() {
        return getSimpleStringValue("accessClass");
    }

    public String getAccessObj() {
        return getSimpleStringValue("accessObj");
    }

    public String getContactInfo() {
        return getSimpleStringValue("contactInfo");
    }

    public String getAddress() {
        return getSimpleStringValue("address");
    }

    public String getDeliveryTime() {
        return getSimpleStringValue("deliveryTime");
    }

    public String getDeliveryMethod() {
        return getSimpleStringValue("deliveryMethod");
    }

    public String getExcompanyNo() {
        return getSimpleStringValue("excompanyNo");
    }

    public String getExpressNo() {
        return getSimpleStringValue("expressNo");
    }

    public String getParentName() {
        return getSimpleStringValue("parentName");
    }

    public String getParentMobile() {
        return getSimpleStringValue("parentMobile");
    }

    public String getParentId() {
        return getSimpleStringValue("parentId");
    }

    public String getStudentName() {
        return getSimpleStringValue("studentName");
    }

    public String getStudentMobile() {
        return getSimpleStringValue("studentMobile");
    }

    public String getStudentId() {
        return getSimpleStringValue("studentId");
    }

    public String getRecycleComment() {
        return getSimpleStringValue("recycleComment");
    }


    public String getRepairRange() {
        return getSimpleStringValue("repairRange");
    }

    public String getRepairCost() {
        return getSimpleStringValue("repairCost");
    }

    public String getStorageRecordStatus() {
        return getSimpleStringValue("storageRecordStatus");
    }

    public String getOutStorageTime() {
        return getSimpleStringValue("outStorageTime");
    }

    public String getOutStorageYear() {
        return getSimpleStringValue("outStorageYear");
    }

    public String getOutStorageMonth() {
        return getSimpleStringValue("outStorageMonth");
    }

}