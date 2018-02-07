package com.fulaan.wrongquestion.service;

import com.db.resources.ResourceDictionaryDao;
import com.db.wrongquestion.NameConvertResNameDao;
import com.fulaan.wrongquestion.dto.ResourcesDictionaryDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.utils.MongoUtils;
import com.pojo.wrongquestion.NameConvertResNameEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ResourceDictionaryService {
	private ResourceDictionaryDao resourceDictionaryDao = new ResourceDictionaryDao();

	private NameConvertResNameDao nameConvertResNameDao = new NameConvertResNameDao();

	/**
	 * 增加
	 * @param e
	 * @return
	 */
	public ObjectId addResourceDictionaryEntry(ResourceDictionaryEntry e)
	{
		return resourceDictionaryDao.addResourceDictionaryEntry(e);
	}

	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public ResourceDictionaryEntry getResourceDictionaryEntry(ObjectId id)
	{
		return resourceDictionaryDao.getResourceDictionaryEntry(id);
	}
	/**
	 * @param type 类型
	 * @param name 代码
	 * @param pid 级别
	 */
	public ResourceDictionaryEntry getResourceDictionaryEntry(int type, String name, ObjectId pid)
	{
		if(type==2){
			String tempName = getConvertResName(name);
			if(!"".equals(tempName)){
				name = tempName;
			}
		}
		return resourceDictionaryDao.getResourceDictionaryEntry(type, name,pid);
	}

	/**
	 *  名称转换
	 * @param name
	 * @return
	 */
	public String getConvertResName(String name){
		NameConvertResNameEntry entry = nameConvertResNameDao.getNameConvertResNameEntry(name);
		if(entry!=null){
			return entry.getResName();
		}else{
			return "";
		}
	}

	/**
	 * 根据类型查询
	 * @param type
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(int type)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrys(type);
	}

	/**
	 * 根据父节点查询子节点
	 * @param parentId
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId, int type)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrys(parentId, type);
	}

	/**
	 * 根据父节点集合查询子节点
	 * @param parentIds
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrysByParents(Collection<ObjectId> parentIds, int type)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrysByParents(parentIds,type,null);
	}

	/**
	 * 根据科目查询题目类型
	 * @param subject
	 * @return
	 */
	public List<ResourceDictionaryEntry> getItemTypeBySubject(List<ObjectId> subject)
	{
		 return resourceDictionaryDao.getItemTypeBySubject(subject);
	}

	/**
	 * 根据ID集合查询
	 * @param ids
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(Collection<ObjectId> ids)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrys(ids);
	}

	public List<ObjectId> getResourceDictionaryEntryIds(String typeId, int typeInt) {
		List<ObjectId> verIds=new ArrayList<ObjectId>();
		if(null!=typeId&&!"".equals(typeId)) {
			List<ResourceDictionaryEntry> list = getItemTypeBySubject(new ObjectId(typeId), typeInt, new BasicDBObject("ty", 1));
			if (list != null && list.size() > 0) {
				for (ResourceDictionaryEntry entry : list) {
					verIds.add(entry.getID());
				}
			}
		}
		return verIds;
	}

	/**
	 * 根据父节点查询子节点
	 * @param subject
	 * @return
	 */
	public List<ResourceDictionaryEntry> getItemTypeBySubject(ObjectId subject, int typeInt, DBObject fields) {
		List<ResourceDictionaryEntry> list = resourceDictionaryDao.getItemTypeBySubject(subject, typeInt, fields);
		return list;
	}

	public List<ResourcesDictionaryDTO> getKnowledgePointList(String type, String subjectName) {
		List<ResourcesDictionaryDTO> list = new ArrayList<ResourcesDictionaryDTO>();
		ResourceDictionaryEntry schoolEntry = getResourceDictionaryEntry(type);
		if(schoolEntry!=null){
			ResourceDictionaryEntry subjectEntry = getResourceDictionaryEntry(2, subjectName, schoolEntry.getID());
			if(subjectEntry!=null){
				List<Integer> typeList = new ArrayList<Integer>();
				typeList.add(7);
				typeList.add(8);
				typeList.add(9);
				List<ResourceDictionaryEntry> rdList = resourceDictionaryDao.getResourceDictionaryEntrys(subjectEntry.getID(), typeList);
				for(ResourceDictionaryEntry entry : rdList){
					ResourcesDictionaryDTO dto = new ResourcesDictionaryDTO(entry);
					list.add(dto);
				}
			}
		}
		return list;
	}


	public List<ResourcesDictionaryDTO> getChapterPointList(String type, String gradeName, String subjectName) {
		List<ResourcesDictionaryDTO> list = new ArrayList<ResourcesDictionaryDTO>();
		ResourceDictionaryEntry schoolEntry = getResourceDictionaryEntry(type);
		if(schoolEntry!=null){
			ResourceDictionaryEntry subjectEntry = getResourceDictionaryEntry(2, subjectName, schoolEntry.getID());
			if(subjectEntry!=null){
				List<Integer> typeList = new ArrayList<Integer>();
				typeList.add(4);
				typeList.add(5);
				typeList.add(6);
				List<ResourceDictionaryEntry> gradeEntrys = getResourceDictionaryEntryList(4, gradeName, subjectEntry.getID());
				List<ObjectId> gradeIds = MongoUtils.getFieldObjectIDs(gradeEntrys);
				List<ResourceDictionaryEntry> rdList = resourceDictionaryDao.getResourceDictionaryEntrys(subjectEntry.getID(), gradeIds, typeList);
				for(ResourceDictionaryEntry entry : rdList){
					ResourcesDictionaryDTO dto = new ResourcesDictionaryDTO(entry);
					list.add(dto);
				}
			}
		}
		return list;
	}

	private List<ResourceDictionaryEntry> getResourceDictionaryEntryList(int type, String gradeName, ObjectId pid) {
		List<ResourceDictionaryEntry> list = resourceDictionaryDao.getResourceDictionaryEntryList(type, gradeName, pid);
		return list;
	}

	/**
	 *
	 * @param type 类型
	 */
	public ResourceDictionaryEntry getResourceDictionaryEntry(String type)
	{
		int typeInt = 1;
		long sort = 0;
		if(type.equals("primary")){
			sort = 1;
		}else if(type.equals("junior")){
			sort = 2;
		}else if(type.equals("senior")){
			sort = 3;
		}
		ResourceDictionaryEntry schoolEntry = resourceDictionaryDao.getResourceDictionaryEntry(typeInt, sort);
		return schoolEntry;
	}

	public static void main(String[] args) {

		long startTime = DateTimeUtils.getStrToLongTime("2017-12-05 00:00:00");
		long endTime = DateTimeUtils.getStrToLongTime("2018-02-07 00:00:00");
		String str = "";
		int count = 0;
		while(startTime < endTime){
			String str2 = DateTimeUtils.getLongToStrTimeTwo(startTime).substring(0,11);
			str = str + "'"+str2+"',";
			startTime = startTime +24*60*60*1000;
			count ++;
		}
		System.out.print(str + count);
	}
}
