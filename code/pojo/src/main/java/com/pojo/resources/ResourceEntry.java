package com.pojo.resources;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源信息
 * <pre>
 * collectionName:resources
 * </pre>
 * <pre>
 * {
 *  ty:1 视频 2Flash 3Mp3 4word 5ppt 6pdf
 *  nm:名字
 *  ud:更新日期
 *  lng:长度
 *  iurl:图片地址
 *  us:上传状态 0:正在上传 1：上传成功 ,2:上传失败 , 实际对应转码状态，0为未转码，1为转码成功
 *  df：删除标记 0没有删除 1已经删除
 *  tn：临时名字
 *  bk:七牛存储Key
 *  ofs:原文件存储
 *  
 *  pid:七牛persistentId
 *  vn:观看次数
 *  vsty:视频来源类型; 详见VideoSourceType
 *  vt：视频type
 *  
 *  ======资源用====
 *  iig(isIgnore) 0否,1是
 *  ======资源用====
 *  
 *  ui:
 *  si:学校
 *  pc:推送次数
 *  scs：知识点list;放置最后一级知识点
 *  [
 *  ]
 *  psbs：课程list;放置最后一级课程表
 *  [
 *  ]
 *
 *  删除标志位  : ir (isRemoved) 0没有删除 1已经删除
 *  入库标志位  : is (isSaved)  0没有入库 1已经入库
 *  归属类型：ot(ownType) 0:大众所有，1：私人专属
 *  专属教育局ID： edid(educationBureauId)
 *  不可用教育局IDs： neids(noEducationBureauIds)
 *  资源来源 : rf (resourceFrom)
 * }
 * </pre>
 * @author fourer
 */
public class ResourceEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1563852238596483294L;
	
	
	public ResourceEntry(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}
	
	
	public ResourceEntry(int type, String name, long length, String imgUrl, String bucketkey, ObjectId userId, ObjectId schoolId, List<ObjectId> scList, List<ObjectId> psbList, int isRemoved, int isSaved, int ownType, ObjectId eduId, List<ObjectId> noEdIds, String resourceFrom)
	{
		this(
				type,
				name,
				System.currentTimeMillis(),//updateDate
				length,
				imgUrl,
				Constant.TWO,//updateState
				Constant.ZERO,//deleteFlag
				Constant.EMPTY,//temporaryName
				bucketkey,
				Constant.EMPTY,//persistentId
				Constant.ZERO,//viewNumber
				-1,//videoSourceType
				-1,//videoType
				userId,
				schoolId,
				Constant.ZERO,//pushCount
				scList,
				psbList,
				isRemoved,
				isSaved,
				ownType,
				eduId,
				noEdIds,
				resourceFrom
			);
	}
	
	
	
	public ResourceEntry(int type, String name, long updateDate, long length, String imgUrl, int updateState, int deleteFlag, String temporaryName, String bucketkey,
						 String persistentId,
						 int viewNumber,
						 int videoSourceType,
						 int videoType,

						 ObjectId userId,
						 ObjectId schoolId,
						 int pushCount,
						 List<ObjectId> scList,
						 List<ObjectId> psbList,
						 int isRemoved,
						 int isSaved,
						 int ownType,
						 ObjectId eduId,
						 List<ObjectId> noEdIds,
						 String resourceFrom
			) {
		super();
		

		BasicDBObject baseEntry =new BasicDBObject()
		.append("ty", type)
		.append("nm", name)
		.append("ud", updateDate)
		.append("lng", length)
		.append("iurl", imgUrl)
		.append("vn", viewNumber)
		.append("us", updateState)
		.append("vt", videoType)
		.append("df", deleteFlag)
		.append("tn", temporaryName)
		.append("bk", bucketkey)
		.append("pid", persistentId)
		.append("vsty", videoSourceType)//详见VideoSourceType
		.append("ui", userId)
		.append("si", schoolId)
		.append("pc", pushCount)
		.append("scs", MongoUtils.convert(scList))
		.append("psbs", MongoUtils.convert(psbList))
		.append("iig", Constant.ZERO)

		.append("ir", isRemoved)
		.append("is", isSaved)

		.append("ot", ownType)
		.append("edid", eduId)

		.append("neids", noEdIds)

		.append("rf", resourceFrom);
		setBaseEntry(baseEntry);;
	}
	
	
	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("si");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("si", schoolId);
	}
	public int getPushCount() {
		return getSimpleIntegerValue("pc");
	}
	public void setPushCount(int pushCount) {
		setSimpleValue("pc", pushCount);
	}
	public List<ObjectId> getScList() {
        List<ObjectId> scList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("scs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				scList.add( (ObjectId)o);
			}
		}
		return scList; 
	}
	public void setScList(List<ObjectId> scList) {
		setSimpleValue("scs",  MongoUtils.convert(scList));
	}
	public List<ObjectId> getPsbsList() {
		List<ObjectId> psbsList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("psbs");
		if(null!=list && !list.isEmpty())
		{
				for(Object o:list)
				{
					psbsList.add( (ObjectId)o);
				}
		}
		return psbsList;
	}
	public void setPsbsList(List<ObjectId> psbsList) {
		setSimpleValue("psbs",  MongoUtils.convert(psbsList));
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public long getUpdateDate() {
		return getSimpleLongValue("ud");
	}
	public void setUpdateDate(long updateDate) {
		setSimpleValue("ud", updateDate);
	}
	public int getThumbUp() {
		return getSimpleIntegerValue("thu");
	}
	public void setThumbUp(int thumbUp) {
		setSimpleValue("thu", thumbUp);
	}
	public String getThumbDown() {
		return getSimpleStringValue("thd");
	}
	public void setThumbDown(String thumbDown) {
		setSimpleValue("thd", thumbDown);
	}
	
	public long getLength() {
		return getSimpleLongValue("lng");
	}
	public void setLength(long length) {
		setSimpleValue("lng", length);
	}
	public String getImgUrl() {
		return getSimpleStringValue("iurl");
	}
	public void setImgUrl(String imgUrl) {
		setSimpleValue("iurl", imgUrl);
	}
	public int getViewNumber() {
		return getSimpleIntegerValue("vn");
	}
	public void setViewNumber(int viewNumber) {
		setSimpleValue("vn", viewNumber);
	}
	public int getUpdateState() {
		return getSimpleIntegerValue("us");
	}
	public void setUpdateState(int updateState) {
		setSimpleValue("us", updateState);
	}
	

	public int getVideoType() {
		return getSimpleIntegerValue("vt");
	}
	public void setVideoType(int videoType) {
		setSimpleValue("vt", videoType);
	}
	
	
	public int getDeleteFlag() {
		return getSimpleIntegerValue("df");
	}
	public void setDeleteFlag(int deleteFlag) {
		setSimpleValue("df", deleteFlag);
	}
	public String getTemporaryName() {
		return getSimpleStringValue("tn");
	}
	public void setTemporaryName(String temporaryName) {
		setSimpleValue("tn", temporaryName);
	}

	public String getBucketkey() {
		return getSimpleStringValue("bk");
	}
	public void setBucketkey(String bucketkey) {
		setSimpleValue("bk", bucketkey);
	}
	public String getPersistentId() {
		return getSimpleStringValue("pid");
	}
	public void setPersistentId(String persistentId) {
		setSimpleValue("pid", persistentId);
	}
	public int getVideoSourceType() {
		return getSimpleIntegerValue("vsty");
	}
	public void setVideoSourceType(int videoSourceType) {
		setSimpleValue("vsty", videoSourceType);
	}

	public String getOriginalFileStore() {
		return getSimpleStringValue("ofs");
	}
	public void setOriginalFileStore(String OriginalFileId) {
		setSimpleValue("ofs", OriginalFileId);
	}
	//==================资源用=================
	public int getIsIgnore() {
		return getSimpleIntegerValueDef("iig",0);
	}
	public void setIsIgnore(int isIgnore) {
		setSimpleValue("iig", isIgnore);
	}

	public int getIsRemoved() {
		return getSimpleIntegerValue("ir");
	}
	public void setIsRemoved(int isRemoved) {
		setSimpleValue("ir", isRemoved);
	}

	public int getIsSaved() {
		return getSimpleIntegerValueDef("is",1);
	}
	public void setIsSaved(int isSaved) {
		setSimpleValue("is", isSaved);
	}

	public int getOwnType() {
		return getSimpleIntegerValue("ot");
	}
	public void setOwnType(int ownType) {
		setSimpleValue("ot", ownType);
	}

	public ObjectId getEduId() {
		return getSimpleObjecIDValue("edid");
	}
	public void setEduId(ObjectId eduId) {
		setSimpleValue("edid", eduId);
	}

	public List<ObjectId> getNoEdIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("neids");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setNoEdIds(List<ObjectId> noEdIds) {
		setSimpleValue("neids", MongoUtils.convert(noEdIds));
	}

	public String getResourceFrom() {
		return getSimpleStringValue("rf");
	}
	public void setResourceFrom(String resourceFrom) {
		setSimpleValue("rf", resourceFrom);
	}
}
