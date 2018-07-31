package com.pojo.lancustom;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

public class MobileReturnEntry extends BaseDBObject {

    /**
     * @Fields serialVersionUID : TODO(目的和意义)
     */
    private static final long serialVersionUID = 1L;

    public MobileReturnEntry() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public MobileReturnEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }

    public MobileReturnEntry(BasicDBObject baseEntry) {
        super(baseEntry);
        // TODO Auto-generated constructor stub
    }
    
    
    public MobileReturnEntry(ObjectId userId,
                             String mobileVersion,
                             int serviceVersion,
                             String problem,
                             String aliPayNum,
                             String name,
                             String schoole,
                             String classRoom,
                             String contactNum,
                             
                             String address,
                             String excompanyNo,
                             String expressNo) {
        
        BasicDBObject dbObject = new BasicDBObject()
            .append("uid", userId)
            .append("mobileVersion", mobileVersion)
            .append("serviceVersion", serviceVersion)
            .append("problem", problem)
            .append("aliPayNum", aliPayNum)
            .append("name", name)
            .append("schoole", schoole)
            .append("classRoom", classRoom)
            .append("contactNum", contactNum)
            .append("address", address)
            .append("status", Constant.ZERO)
            .append("excompanyNo", excompanyNo)
            .append("expressNo", expressNo)
            .append("sta", Constant.ZERO)
            .append("isr", Constant.ZERO);
        
        setBaseEntry(dbObject);
        
    }
    
    public ObjectId getUid(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUid(ObjectId uid){
        setSimpleValue("uid",uid);
    }
    
    public int getServiceVersion(){
        return getSimpleIntegerValue("serviceVersion");
    }

    public void setServiceVersion(int serviceVersion){
        setSimpleValue("serviceVersion",serviceVersion);
    }
    
    public int getSta(){
        return getSimpleIntegerValue("sta");
    }

    public void setSta(int sta){
        setSimpleValue("sta",sta);
    }
    
    public String getMobileVersion() {
        return getSimpleStringValue("mobileVersion");
    }
    
    public void setMobileVersion(String mobileVersion) {
        setSimpleValue("mobileVersion",mobileVersion);
    }
    
    public String getProblem() {
        return getSimpleStringValue("problem");
    }
    
    public void setProblem(String problem) {
        setSimpleValue("problem",problem);
    }
    
    public String getAliPayNum() {
        return getSimpleStringValue("aliPayNum");
    }
    
    public void setAliPayNum(String aliPayNum) {
        setSimpleValue("aliPayNum",aliPayNum);
    }
    
    public String getName() {
        return getSimpleStringValue("name");
    }
    
    public void setName(String name) {
        setSimpleValue("name",name);
    }
    
    public String getSchoole() {
        return getSimpleStringValue("Schoole");
    }
    
    public void setSchoole(String schoole) {
        setSimpleValue("schoole",schoole);
    }
    
    public String getClassRoom() {
        return getSimpleStringValue("classRoom");
    }
    
    public void setClassRoom(String classRoom) {
        setSimpleValue("classRoom",classRoom);
    }
    
    public String getAddress() {
        return getSimpleStringValue("address");
    }
    
    public void setAddress(String address) {
        setSimpleValue("address", address);
    }
    
    public String getContactNum() {
        return getSimpleStringValue("contactNum");
    }
    
    public void setContactNum(String contactNum) {
        setSimpleValue("contactNum",contactNum);
    }
    
    public String getExcompanyNo() {
        return getSimpleStringValue("excompanyNo");
    }
    
    public void setExcompanyNo(String excompanyNo) {
        setSimpleValue("excompanyNo",excompanyNo);
    }
    
    public String getExpressNo() {
        return getSimpleStringValue("expressNo");
    }
    
    public void setExpressNo(String expressNo) {
        setSimpleValue("expressNo",expressNo);
    }
}
