package com.fulaan.base.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.lesson.DirDao;
import com.db.school.LeagueDao;
import com.db.school.TeacherClassSubjectDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.lesson.DirDTO;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.school.LeagueEnrty;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

/**
 * 目录服务
 * @author fourer
 *
 */
@Service
public class DirService {

	private DirDao dirDao =new DirDao();
	private TeacherClassSubjectDao teacherClassLessonDao =new TeacherClassSubjectDao();
	private LeagueDao leagueDao =new LeagueDao();
	/**
	 * 添加DirEntry
	 * @param e
	 * @return
	 */
	public ObjectId addDirEntry(DirEntry e)
	{
		return dirDao.addDirEntry(e);
	}
	
	/**
	 * 根据id和用户ID查询
	 * @param id Id必须
	 * @param userId
	 * @return
	 */
	public DirEntry getDirEntry(ObjectId id,ObjectId userId)
	{
		DirEntry e= dirDao.getDirEntry(id,userId);
		return e;
	}
	
	
	/**
	 * 更新
	 * @param dirId
	 * @param field
	 * @param value
	 */
	 public void update(ObjectId dirId,String field,Object value)
	 {
		 dirDao.update(dirId, field, value);
		 
	 }
	
	/**
	 * 根据用户ID查询
	 * @param userId
	 * @return
	 */
	public List<DirDTO> getDirDTOs(ObjectId userId)
	{
		List<DirDTO> retList =new ArrayList<DirDTO>();
	    List<DirEntry> entryList =dirDao.getDirEntryList(userId,null);
	    for(DirEntry e:entryList)
	    {
	    	retList.add(new DirDTO(e));
	    }
		return retList;
	}
	
	
	/**
	 * 根据owerID和parentId查询
	 * @param owerID
	 * @param parentId
	 * @return
	 */
	public List<DirEntry> getDirEntryList(Collection<ObjectId> owerIDs ,DBObject fields,int dirtype)
	{
		return dirDao.getDirEntryList(owerIDs,fields,dirtype);
	}
	
	/**
	 * 根据ID集合查询
	 * @param iDs
	 * @param fields
	 * @return
	 */
	public List<DirEntry> getDirEntryListByIds(Collection<ObjectId> iDs ,DBObject fields)
	{
		return dirDao.getDirEntryListByIds(iDs,fields);
	}
	
	/**
	 * 查找自己以及子目录ID
	 * @param usertId
	 * @param dirId
	 * @return
	 */
	public Set<ObjectId> getSelfAndChildDirs(ObjectId ownerId,ObjectId dirId)
	{
		Set<ObjectId> idSet =new HashSet<ObjectId>();
		idSet.add(dirId);
		List<DirEntry> entryList =dirDao.getDirEntryList(null, dirId);
		if(!entryList.isEmpty())
		{
			ObjectId	parentId =null;
			entryList =dirDao.getDirEntryList(ownerId,null);
			Map<ObjectId, Set<ObjectId>> map =new HashMap<ObjectId, Set<ObjectId>>();
			for(DirEntry e:entryList)
			{
				parentId =e.getParentId();
				if(null!=parentId)
				{
					if(!map.containsKey(parentId))
					{
						map.put(parentId, new HashSet<ObjectId>());
					}
					map.get(parentId).add(e.getID());
				}
			}
			//2+级目录
			Set<ObjectId> deleteSonSet =map.get(dirId);
		    
			while(true)
			{
				if(deleteSonSet.isEmpty())
				{
					break;
				}
				Set<ObjectId> newDeleteSonSet =new HashSet<ObjectId>(deleteSonSet);
				deleteSonSet.clear();
				for(ObjectId deleteId:newDeleteSonSet) 
				{
					idSet.add(deleteId);
					if(map.containsKey(deleteId))
					{
						deleteSonSet.addAll(map.get(deleteId));
					}
				}
			}
		}
		
		return idSet;
	}
	
	
	/**
	 * 删除目录
	 * @param userId
	 * @param dirId
	 */
	public Set<ObjectId> removeDir(DirEntry dirEntry)
	{
		Set<ObjectId> deleteIds =getSelfAndChildDirs(dirEntry.getOwerId(),dirEntry.getID());
		dirDao.removeDirs(deleteIds);
		return deleteIds;
	}
	

	/**
	 * 判断是否有权限
	 * @param dirId
	 * @param ui
	 * @param schoolId
	 * @return
	 * @throws IllegalParamException 
	 */
	public boolean isHavePermission(ObjectId dirId,ObjectId ui,ObjectId schoolId) throws IllegalParamException {
		DirEntry dirEntry = getDirEntry(dirId, null);
		if (null == dirEntry) {
			throw new IllegalArgumentException();
		}
		
		return isPermission(dirEntry,ui,schoolId);
	}

	/**
	 * 判断是否有权限
	 * @param dirId
	 * @param ui
	 * @param schoolId
	 * @return
	 * @throws IllegalParamException 
	 */
	public boolean isPermission(DirEntry dirEntry,ObjectId ui,ObjectId schoolId) throws IllegalParamException {
		
		if(null==dirEntry)
			throw new IllegalParamException();
		if (ui.equals(dirEntry.getOwerId())) {
			return true;
		}

		List<TeacherClassSubjectEntry> list = teacherClassLessonDao.getTeacherClassSubjectEntryList(ui,null, null);
				
		for (TeacherClassSubjectEntry e : list) {
			//todo
			if(null!=dirEntry.getOwerId())
			{
				if (e.getClassInfo().getId().equals(dirEntry.getOwerId())) {
					return true;
				}
			}
		}
		//todo
		if(null!=dirEntry.getOwerId())
		{
			if (dirEntry.getOwerId().equals(schoolId)) {
				return true;
			}
		}

		List<LeagueEnrty> LeagueEnrtyList=leagueDao.getLeagueEnrtys(schoolId, new BasicDBObject(Constant.ID,1));
		List<ObjectId> leagusIds =MongoUtils.getFieldObjectIDs(LeagueEnrtyList, Constant.ID);
		List<DirEntry> dirs=dirDao.getDirEntryList(leagusIds,new BasicDBObject(Constant.ID,1), DirType.UNION_RESOURCE.getType());
		List<ObjectId> dirIds =MongoUtils.getFieldObjectIDs(dirs, Constant.ID);
		
		if(Collections.binarySearch(dirIds, dirEntry.getID())>=0)
		{
			return true;
		}
		
		return false;
		
		
		//return true;
	}


	/**
	 * 得到和我相关的全部dir的ID
	 * @param ui
	 * @param schoolId
	 * @return
	 */
	public Set<ObjectId> getDirIds(ObjectId ui,ObjectId schoolId)
	{
		List<ObjectId> ownerList =new ArrayList<ObjectId>();
		//我的
		ownerList.add(ui);
		//我的班级的
		List<TeacherClassSubjectEntry> list = teacherClassLessonDao.getTeacherClassSubjectEntryList(ui,null, null);
		ownerList.addAll(MongoUtils.getFieldObjectIDs(list,Constant.ID));
		//校本资源
		ownerList.add(schoolId);
		//联盟
		List<LeagueEnrty> LeagueEnrtyList=leagueDao.getLeagueEnrtys(schoolId, new BasicDBObject(Constant.ID,1));
		ownerList.addAll(MongoUtils.getFieldObjectIDs(LeagueEnrtyList,Constant.ID));
		
		
		
		List<DirEntry> dirs=dirDao.getDirEntryList(ownerList,new BasicDBObject(Constant.ID,1),-1);
		List<ObjectId> idList =MongoUtils.getFieldObjectIDs(dirs,Constant.ID);
		
		return new HashSet<ObjectId>(idList);
	}
	
	
	
	
	/**
	 * 移动dir
	 * @param id
	 * @param parentId
	 * @param order
	 */
	 public void move(ObjectId id,ObjectId parentId,int order)
	 {
		 dirDao.move(id, parentId, order);
	 }
}
