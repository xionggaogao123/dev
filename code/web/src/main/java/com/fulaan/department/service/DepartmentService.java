package com.fulaan.department.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.school.DepartmentDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.NameValuePair;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.DepartmentFile;
import com.pojo.school.DepartmentFileDTO;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.utils.NameShowUtils;

/**
 * 
 * @author cxy
 *
 */
@Service
public class DepartmentService {
	private DepartmentDao departmentDao = new DepartmentDao();
	private UserDao userDao =new UserDao();
	/**
	 * 通过学校ID查询所有部门
	 * @param sid
	 * @return
	 */
	public List<DepartmentEntry> getDepartmentEntrys(ObjectId sid)
	{
		return departmentDao.getDepartmentEntrys(sid);
	}
	/**
	 * 查询该用户所处的所有部门
	 * @param userId
	 * @return
	 */
	public List<DepartmentEntry> getDepartmentsByUserId(ObjectId userId){
		
		return departmentDao.getDepartmentsByUserId(userId);
	}
	/**
	 * 根据部门ID查询详情
	 * @param departmentId
	 * @return
	 */
	public DepartmentEntry getDepartmentEntryById(ObjectId departmentId){
		
		return departmentDao.getDepartmentEntry(departmentId);
	}
	
	
	
	
	
	/**
	 * 得到部门文件
	 * @param depId
	 * @return
	 */
	public List<DepartmentFileDTO> getDepartmentFileDTOs(ObjectId depId)
	{
		List<DepartmentFileDTO> retList =new ArrayList<DepartmentFileDTO>();
		DepartmentEntry e=departmentDao.getDepartmentEntry(depId);
		List<DepartmentFile> files =e.getDepartmentFiles();
		Map<ObjectId, UserEntry> userMap =userDao.getUserEntryMap(MongoUtils.getFieldObjectIDs(files, "ui"), new BasicDBObject("nm",1));
		UserEntry ue=null;
		DepartmentFileDTO dto=null;
		for(DepartmentFile file:files)
		{
			try
			{
			ue=userMap.get(file.getUserId());
			dto=new DepartmentFileDTO(file);
			dto.setUser(ue.getUserName());
			retList.add(dto);
			}catch(Exception ex)
			{
				
			}
		}
		return retList;
	}
	
	
	/**
	 * 得到部门人员
	 * @param depId
	 * @return
	 */
	public List<NameValuePair> getDepartmentUser(ObjectId depId)
	{
		List<NameValuePair> retList =new ArrayList<NameValuePair>();
		DepartmentEntry e=departmentDao.getDepartmentEntry(depId);
		Map<ObjectId, UserEntry> userMap =userDao.getUserEntryMap(e.getMembers(), new BasicDBObject("nm",1));
	
		for(UserEntry u:userMap.values())
		{
			retList.add(new NameValuePair(u.getUserName(), u.getID().toString()));
		}
		return retList;
	}
	
	
	/**
	 * 添加一个部门文件
	 * @param depId
	 * @param file
	 */
	public void addDepartFile(ObjectId depId,DepartmentFile file)
	{
		departmentDao.addDepartmentFile(depId, file);
	}
	
	/**
	 * 删除一个部门文件
	 * @param depId
	 * @param file
	 */
	public void removeDepartFile(ObjectId depId,DepartmentFile file)
	{
		departmentDao.removeDepartmentFile(depId, file);
	}
	
}
