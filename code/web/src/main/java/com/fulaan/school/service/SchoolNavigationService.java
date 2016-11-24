package com.fulaan.school.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.school.SchoolDao;
import com.db.school.SchoolNavigationDao;
import com.google.common.base.Splitter;
import com.google.gson.internal.LinkedTreeMap;
import com.mongodb.BasicDBObject;
import com.pojo.school.SchoolEntry;
import com.pojo.school.SchoolNavigationEntry;
import com.pojo.school.SchoolNavigationModuleDTO;
import com.pojo.school.SchoolNavigationEntry.RoleLink;
import com.pojo.school.SchoolNavigationModuleDTO.SchoolNavigationDTO;
import com.sys.constants.Constant;
import java.util.Comparator;  

/**
 * 学校导航service
 * @author fourer
 *
 */
public class SchoolNavigationService {

	
	
	private SchoolDao schoolDao =new SchoolDao();
	private SchoolNavigationDao schoolNavigationDao =new SchoolNavigationDao();
	
	private static List<Integer> vavList =new ArrayList<Integer>();
	private static List<Integer> citieList =new ArrayList<Integer>();

	static
	{
		vavList.add(1);
		vavList.add(2);
		citieList.add(3);
	}
	
	
	//杨浦小学 22
	//21  "1,2,3,4,5,7,8,9,10,11,14"
	//25  "2,7,8"
	//26 "1,2,3,4,5,7,9,10,11,14,20,23",
	//27  1,2,3,4,5,7,15
	//28  1,5,7,9,10
	//16  
	//22 
	public static void main(String[] args) {
		
		/**
		SchoolDao schoolDao =new SchoolDao();
		SchoolNavigationDao schoolNavigationDao =new SchoolNavigationDao();
		List<SchoolEntry> list=schoolDao.getSchoolEntryListBYNv(20);
		List<ObjectId> sid1 =MongoUtils.getFieldObjectIDs(list, "_id");
		schoolNavigationDao.removeSchools(new ObjectId("5812ebb3f446bc2054a795e2"), sid1);
		schoolNavigationDao.removeSchools(new ObjectId("5812ebb3f446bc2054a795e5"), sid1);
		schoolNavigationDao.removeSchools(new ObjectId("5812ebb3f446bc2054a795e6"), sid1);
		**/
		
		
		
		SchoolNavigationService s =new SchoolNavigationService();
		
		List<Integer> list =new ArrayList<Integer>();
		
//		list.add(1024);
		list.add(64);
		list.add(8);
		list.add(2);
		
		List<SchoolNavigationDTO> list2=s.loadCiTie(new ObjectId("57f8b0bf71f056532ba3abf1"), list);
		for(SchoolNavigationDTO dto:list2)
		{
			System.out.println(dto);
		}

		
	}
	
	
	
	
	
	/**
	 * 加载页面左面的导航
	 * @param schoolId 学校的ID 
	 * @param roles 用户角色
	 * @return
	 */
	public  List<SchoolNavigationModuleDTO> loadNavs(ObjectId schoolId,List<Integer> roles)
	{
		List<SchoolNavigationModuleDTO> retList =new ArrayList<SchoolNavigationModuleDTO>();
		
		
		SchoolEntry se=schoolDao.getSchoolEntry(schoolId, new BasicDBObject("mods",1));
		String moduleStr=se.getModuleStr();
		List<Integer> moduleIntList =toIntegerArray(moduleStr);
		moduleIntList.add(0);
		List<SchoolNavigationEntry> navEntryList=schoolNavigationDao.getSchoolNavigationEntryList(schoolId,vavList, moduleIntList);
		//处理定制需求
		List<SchoolNavigationEntry> navEntryList2 =new ArrayList<SchoolNavigationEntry>(navEntryList.size());
		for(SchoolNavigationEntry sne:navEntryList)
		{
			if(sne.getSchoolIds().size()>0 && sne.getSchoolIds().indexOf(schoolId)<0 )
			{
				continue;
			}
			navEntryList2.add(sne);
		}
		Map<String,SchoolNavigationEntry> idMap =new HashMap<String, SchoolNavigationEntry>();
		SchoolNavigationEntry thisSne;
		for(SchoolNavigationEntry sne:navEntryList2)
		{
			thisSne=idMap.get(sne.getId());
			if(null==thisSne)
			{
				idMap.put(sne.getId(), sne);
			}
			else
			{
				boolean isAdd=false;
				if(sne.getSchoolIds().size()>0 && sne.getSchoolIds().indexOf(schoolId)>=0 )
				{
					if(sne.getList().size()==0)
					{
						isAdd=true;
					}
					else
					{
						for(RoleLink rl:sne.getList())
						{
							List<Integer> newRoles=new ArrayList<Integer>(roles);
							newRoles.retainAll(rl.getRoles());
							if(newRoles.size()>0)
							{
								isAdd=true;
							}
						}
					}
				}
				
				if(isAdd)
				{
					idMap.put(sne.getId(), sne);
				}
				
			}
		}
		
		List<SchoolNavigationEntry> newNavEntryList =new ArrayList<SchoolNavigationEntry>(idMap.values());
		
		//判断导航权限
		List<SchoolNavigationDTO> userNavDTOList =new ArrayList<SchoolNavigationModuleDTO.SchoolNavigationDTO>(newNavEntryList.size());
		for(SchoolNavigationEntry sne:newNavEntryList)
		{
			for(RoleLink rl:sne.getList())
			{
				List<Integer> newRoles=new ArrayList<Integer>(roles);
				newRoles.retainAll(rl.getRoles());
				if(newRoles.size()>0)
				{
					List<RoleLink> rlList =new ArrayList<SchoolNavigationEntry.RoleLink>();
					rlList.add(rl);
					sne.setList(rlList);
					userNavDTOList.add(new SchoolNavigationDTO(sne));
					break;
				}
			}
		}
        Map<Integer,SchoolNavigationDTO> navBlockMap =new HashMap<Integer, SchoolNavigationDTO>();
		Map<Integer,List<SchoolNavigationDTO>> navMap =new LinkedTreeMap<Integer, List<SchoolNavigationDTO>>();
		for(SchoolNavigationDTO dto:userNavDTOList)
		{
			if(dto.getType()==1)
			{
				navBlockMap.put(dto.getModleId(), dto);
			}
			else
			{
				
				if(!navMap.containsKey(dto.getModleId()))
				{
					navMap.put(dto.getModleId(), new ArrayList<SchoolNavigationDTO>());
				}
				navMap.get(dto.getModleId()).add(dto);
			}
		}
		for(Integer moduleId:moduleIntList)
		{
			SchoolNavigationDTO blockNav=navBlockMap.get(moduleId);
			List<SchoolNavigationDTO> itemList=navMap.get(moduleId);
			
			if(null!=blockNav)
			{
				SchoolNavigationModuleDTO dto =new SchoolNavigationModuleDTO(blockNav,itemList);
				retList.add(dto);
			}
		}
		
      Collections.sort(retList,new Comparator<SchoolNavigationModuleDTO>(){  
          @Override  
          public int compare(SchoolNavigationModuleDTO b1, SchoolNavigationModuleDTO b2) {  
              return b1.getDto().getSort()-b2.getDto().getSort();
          }  
      }); 
		
      for(SchoolNavigationModuleDTO dto:retList)
	  {
    	  if(null!=dto.getList())
    	  {
    	   Collections.sort(dto.getList(),new Comparator<SchoolNavigationDTO>(){  
              @Override  
              public int compare(SchoolNavigationDTO b1, SchoolNavigationDTO b2) {  
                  return b1.getSort()-b2.getSort();
              }  
           }); 
    	  }
	  }
	  return retList;
	}
	
	
	/**
	 * 加载磁贴页面
	 * @param schoolId
	 * @param roles
	 * @return
	 */
	public  List<SchoolNavigationDTO> loadCiTie(ObjectId schoolId,List<Integer> roles)
	{
		List<SchoolNavigationDTO> retList =new ArrayList<SchoolNavigationDTO>();
		SchoolEntry se=schoolDao.getSchoolEntry(schoolId, new BasicDBObject("mods",1));
		String moduleStr=se.getModuleStr();
		List<Integer> moduleIntList =toIntegerArray(moduleStr);
		List<SchoolNavigationEntry> navEntryList=schoolNavigationDao.getSchoolNavigationEntryList(schoolId,citieList, moduleIntList);
		for(SchoolNavigationEntry sne:navEntryList)
		{
			for(RoleLink rl:sne.getList())
			{
				List<Integer> newRoles=new ArrayList<Integer>(roles);
				newRoles.retainAll(rl.getRoles());
				if(newRoles.size()>0)
				{
					List<RoleLink> rlList =new ArrayList<SchoolNavigationEntry.RoleLink>();
					rlList.add(rl);
					sne.setList(rlList);
					retList.add(new SchoolNavigationDTO(sne));
					break;
				}
			}
		}
		
		 Collections.sort(retList,new Comparator<SchoolNavigationDTO>(){  
             @Override  
             public int compare(SchoolNavigationDTO b1, SchoolNavigationDTO b2) {  
                 return b1.getSort()-b2.getSort();
             }  
          }); 
		return retList;
		
	}
	/**
	 * 将1,3,5,6,形式字符串转变成Integer 数组
	 * @param module
	 * @return
	 */
	private List<Integer> toIntegerArray(String module)
	{
		List<Integer> retList =new ArrayList<Integer>();
		List<String> list=Splitter.on(Constant.COMMA).splitToList(module);
		
		for(String s:list)
		{
			if(StringUtils.isNotBlank(s) && StringUtils.isNumeric(s))
			{
				retList.add(Integer.valueOf(s));
			}
		}
		return retList;
	}
}
