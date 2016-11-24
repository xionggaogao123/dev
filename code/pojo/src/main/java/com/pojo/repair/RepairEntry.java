package com.pojo.repair;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * @author cxy
 * 2015-07-18 15:54:35
 * 
 *  报修Entry类
 * collectionName:repair
 *  报修学期 : rtt(repairTermType)
 *  报修人Id	: rdpid(declareRepairPersonId)
 *  报修人名 : repna(declareRepairPersonName)
 *  报修日期 : rd(repairDate)
 *  报修地点 : rpl(repairPlace)
 *  联系电话 : ph(phone)
 *  报修类别 : rt(repairType)
 *  报修部门ID : rdid(repairDepartmentId)
 *  报修部门名   : rdna(repairDepartmentName)
 *  报修描述 : rc(repairContent)
 *  报修进度 : rpr(repairProcedure)
 *  报修结果 : rre(repairResult)
 *  
 *  
 *  
 *  维修人id: rspid(solveRepairPersonId)
 *  维修人na: rspna(solveRepairPersonName)
 *  
 *  
 *  
 *  报修评价 : rev(repariEvaluation)
 *  报修照片 : rpic(repairPicture)
 *  删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 *  所属学校id : scid(schoolId)
 *  所属学校名字：scnm(schoolName)
 *  上报人名:rnm(manager)
 *  是否上报至教育局：ispt(isReport,0未上报,1已上报,)
 *  上报原因：rr(reportReason)
 *  所属教育局的id：eid(educationId)
 *  教育局是否指派：isr(isResolve,0为指派，1已指派)
 *  图片地址：ipath
 */
public class RepairEntry extends BaseDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2460585943922873390L;
	public RepairEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public RepairEntry(String repairTermType,ObjectId declareRepairPersonId,String declareRepairPersonName, long repairDate, String repairPlace,
			String phone, String repairType, ObjectId repairDepartmentId,String repairDepartmentName,String repairContent, String repairProcedure,
			ObjectId schoolId,String schoolName,ObjectId educationId,String reportReason,String manager,Integer isReport,Integer isResolve,String imagePath) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("rtt", repairTermType)
		.append("rdpid", declareRepairPersonId)
		.append("repna", declareRepairPersonName)
		.append("rd", repairDate)
		.append("rpl", repairPlace)
		.append("ph", phone)
		.append("rt", repairType)
		.append("rdid", repairDepartmentId)
		.append("rdna", repairDepartmentName)
		.append("rc", repairContent)
		.append("rpr", repairProcedure)
		.append("ir", Constant.ZERO)
		.append("ispt",Constant.ZERO )
		.append("scid", schoolId)
		.append("scnm", schoolName)
		.append("eid", educationId)
		.append("rr", "")
		.append("rnm", "")
		.append("isr", Constant.ZERO)
		.append("ipath", imagePath)
		;
		
		
		setBaseEntry(baseEntry);
		
	}
	
	
	
	
	
	public String getRepairTermType() {
		return getSimpleStringValue("rtt");
	}
	public void setRepairTermType(String repairTermType) {
		setSimpleValue("rtt", repairTermType);
	}
	
	public ObjectId getDeclareRepairPersonId() {
		return getSimpleObjecIDValue("rdpid");
	}
	public void setDeclareRepairPersonId(ObjectId declareRepairPersonId) {
		setSimpleValue("rdpid", declareRepairPersonId);
	}
	
	public String getDeclareRepairPersonName() {
		return getSimpleStringValue("repna");
	}
	public void setDeclareRepairPersonName(String declareRepairPersonName) {
		setSimpleValue("repna", declareRepairPersonName);
	}
	
	public long getRepairTime() {
		return getSimpleLongValue("rd"); 
	}
	public void setRepairTime(long repairDate) {
		setSimpleValue("rd", repairDate);
	}
	
	public String getRepairPlace() {
		return getSimpleStringValue("rpl");
	}
	public void setRepairPlace(String repairPlace) {
		setSimpleValue("rpl", repairPlace);
	}
	
	
	
	
	
	
	
	public String getImagePath() {
		return getSimpleStringValue("ipath");
	}
	public void setImagePath(String ipath) {
		setSimpleValue("ipath", ipath);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getPhone() {
		return getSimpleStringValue("ph");
	}
	public void setPhone(String phone) {
		setSimpleValue("ph", phone);
	}
	
	public String getRepairType() {
		return getSimpleStringValue("rt");
	}
	public void setRepairType(String repairType) {
		setSimpleValue("rt", repairType);
	}
	
	public ObjectId getRepairDepartmentId() {
		return getSimpleObjecIDValue("rdid");
	}
	public void setRepairDepartmentId(ObjectId repairDepartmentId) {
		setSimpleValue("rdid", repairDepartmentId);
	}
	
	public String getRepairDepartmentName() {
		return getSimpleStringValue("rdna");
	}
	public void setRepairDepartmentName(String repairDepartmentName) {
		setSimpleValue("rdna", repairDepartmentName);
	}
	
	public String getRepairContent() {
		return getSimpleStringValue("rc");
	}
	public void setRepairContent(String repairContent) {
		setSimpleValue("rc", repairContent);
	}
	
	public String getRepairProcedure() {
		return getSimpleStringValue("rpr");
	}
	public void setRepairProcedure(String repairProcedure) {
		setSimpleValue("rpr", repairProcedure);
	}
	
	public String getRepairResult() {
		return getSimpleStringValue("rre");
	}
	public void setRepairResult(String repairResult) {
		setSimpleValue("rre", repairResult);
	}
	
	public ObjectId getSolveRepairPersonId() {
		return getSimpleObjecIDValue("rspid");
	}
	public void setSolveRepairPersonId(ObjectId solveRepairPersonId) {
		setSimpleValue("rspid", solveRepairPersonId);
	}
	public ObjectId getEducationId() {
		return getSimpleObjecIDValue("eid");
	}
	public void setEducationId(ObjectId educationId) {
		setSimpleValue("eid", educationId);
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("scid", schoolId);
	}
	
	public String getSolveRepairPersonName() {
		return getSimpleStringValue("rspna");
	}
	public void setSolveRepairPersonName(String solveRepairPersonName) {
		setSimpleValue("rspna", solveRepairPersonName);
	}
	public String getSchoolName() {
		return getSimpleStringValue("scnm");
	}
	public void setSchoolName(String schoolName) {
		setSimpleValue("scnm", schoolName);
	}
	
	public String getRepariEvaluation() {
		return getSimpleStringValue("rev");
	}
	public void setRepariEvaluation(String repariEvaluation) {
		setSimpleValue("rev", repariEvaluation);
	}
	
	public String getRepairPicture() {
		return getSimpleStringValue("rpic");
	}
	public void setRepairPicture(String repairPicture) {
		setSimpleValue("rpic", repairPicture);
	}
	//默认未删除
	public int getIsRemove() {
		if(getBaseEntry().containsField("ir"))
		{
		  return getSimpleIntegerValue("ir");
		}
		return Constant.ZERO;
	}
	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}
	//默认未上报
	public int getIsReport() {
		if(getBaseEntry().containsField("ispt"))
		{
			return getSimpleIntegerValue("ispt");
		}
		return Constant.ZERO;
	}
	public void setIsReport(int isReport) {
		setSimpleValue("ir", isReport);
	}
	//默认未指派
	public int getIsResolve() {
		if(getBaseEntry().containsField("isr"))
		{
			return getSimpleIntegerValue("isr");
		}
		return Constant.ZERO;
	}
	public void setIsResolve(int isResolve) {
		setSimpleValue("isr", isResolve);
	}
	public String getManager() {
		return getSimpleStringValue("rnm");
	}
	public void setManager(String manager) {
		setSimpleValue("rnm", manager);
	}
	public String getReportReason() {
		return getSimpleStringValue("rr");
	}
	public void setReportReason(String reportReason) {
		setSimpleValue("rr", reportReason);
	}
	public String getReportContent() {
		return getSimpleStringValue("rpc");
	}
	public void setReportContent(String reportContent) {
		setSimpleValue("rpc", reportContent);
	}
}
