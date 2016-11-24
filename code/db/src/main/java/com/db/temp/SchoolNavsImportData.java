package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.school.SchoolDao;
import com.db.school.SchoolNavigationDao;
import com.pojo.school.SchoolEntry;
import com.pojo.school.SchoolNavigationEntry;
import com.pojo.school.SchoolNavigationEntry.RoleLink;
import com.pojo.user.UserRole;

/**
 * 学校导航导入数据
 * @author fourer
 *
 */
public class SchoolNavsImportData {
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		File f= new File("D:\\schoolnans1.txt");
		List<String> list =FileUtils.readLines(f);
		
		List<String> deflist=new ArrayList<String>();
		List<String> schoolList=new ArrayList<String>();
		
		for(String s:list)
		{
			String[] arr=s.split(",");
			if(arr.length==7) //定制
			{
				schoolList.add(s);
			}
			else
			{
				deflist.add(s);
			}
		}
		
		defaultNav(deflist);
		schoolNav(schoolList);
	}
	
	
	
	
	

	public static void defaultNav(List<String> list) throws IOException {
		
		SchoolNavigationDao dao =new SchoolNavigationDao();
		Map<String, List<String[]>> map =new HashMap<String, List<String[]>>();
		for(String s:list)
		{
			String[] arr=s.split("\\|");
			String idStr=arr[1];
			if(!map.containsKey(idStr))
			{
				map.put(idStr, new ArrayList<String[]>());
			}
			map.get(idStr).add(arr);
		}
		for(Map.Entry<String, List<String[]>> entry:map.entrySet())
		{
			try
			{
					List<String[]> valueList=entry.getValue();
					List<RoleLink> rolelinkList =new ArrayList<SchoolNavigationEntry.RoleLink>();
					
					for(String[] arr:valueList)
					{
						String link=arr[4];
						String roles=arr[5];
						List<Integer> roleList=getRoleIntFromRoleString(roles);
						RoleLink rl=new RoleLink(roleList,link);
						rolelinkList.add(rl);
					}
					String[] arr=valueList.get(0);
					String ty=arr[0];
					String idStr=arr[1];
					String name=arr[2];
					String sort=arr[3];
					
					
				    int tyInt=getInteger(ty);
				   
				    int sortInt =getInteger(sort);
				    
				    
				    String midStr=arr[7];
				    int moduleInt=getInteger(midStr);
					SchoolNavigationEntry e =new SchoolNavigationEntry(idStr,tyInt,moduleInt, name, "css_name",sortInt , rolelinkList, null,"");
					dao.addSchoolNavigationEntry(e);
			
			}catch(Exception ex)
			{
				System.out.println("处理默认导航出错;Id:"+entry.getKey());
				for(String[] arr:entry.getValue())
				{
					System.out.println(Arrays.asList(arr));
				}
				ex.printStackTrace();
				System.out.println("++++++++++++++++++++++");
			}
		}
		
	}
	
	
	
	
   public static void schoolNav(List<String> list) throws IOException {
	   SchoolDao sDao =new SchoolDao();
		SchoolNavigationDao dao =new SchoolNavigationDao();
		Map<String, List<String[]>> map =new HashMap<String, List<String[]>>();
		for(String s:list)
		{
			String[] arr=s.split("\\|");
			String idStr=arr[1];
			if(!map.containsKey(idStr))
			{
				map.put(idStr, new ArrayList<String[]>());
			}
			map.get(idStr).add(arr);
		}
		
		
		for(Map.Entry<String, List<String[]>> entry:map.entrySet())
		{
			try
			{
				
					List<String[]> valueList=entry.getValue();
					List<RoleLink> rolelinkList =new ArrayList<SchoolNavigationEntry.RoleLink>();
					
					for(String[] arr:valueList)
					{
						String link=arr[4];
						String roles=arr[5];
						List<Integer> roleList=getRoleIntFromRoleString(roles);
						RoleLink rl=new RoleLink(roleList,link);
						rolelinkList.add(rl);
					}
					String[] arr=valueList.get(0);
					String ty=arr[0];
					String idStr=arr[1];
					String name=arr[2];
					String sort=arr[3];
				
					List<ObjectId> sids=new ArrayList<ObjectId>();
					String sidStr="";
					//if(arr.length==7)
					{
						sidStr=arr[6];
						if(StringUtils.isNotBlank(sidStr))
						{
							
							String[] schoolArr=sidStr.split("，");
							for(String schoolName:schoolArr)
							{
								SchoolEntry se=sDao.getSchoolEntry(schoolName, null);
								if(null!=se)
								{
								 sids.add(se.getID());
								}
								else
								{
									throw new Exception("找不到学校："+schoolName);
								}
							}
							
						}
					}
					
					int tyInt=getInteger(ty);
				
					int sortInt =getInteger(sort);
					
					String midStr=arr[7];
					int moduleInt=getInteger(midStr);
					SchoolNavigationEntry e =new SchoolNavigationEntry(idStr,tyInt,moduleInt, name, "css_name", sortInt, rolelinkList, sids,"");
			        dao.addSchoolNavigationEntry(e);
			
			}catch(Exception ex)
			{
				System.out.println("处理学校导航出错;Id:"+entry.getKey());
				for(String[] arr:entry.getValue())
				{
					System.out.println(Arrays.asList(arr));
				}
				ex.printStackTrace();
				System.out.println("++++++++++++++++++++++");
			}
		}
		
	}
	
	
	
	public static Integer getInteger(String ty)
	{
		try
		{
		   return Integer.valueOf(ty);
		}catch(Exception ex)
		{
			 return Double.valueOf(ty).intValue();
		}
	}
	
	public static Integer getModuleFromIdStr(String str) throws Exception
	{
		String[] arr =str.split("_");
		
		if(str.indexOf("version")==0)
		{
		  return Integer.parseInt(arr[1]);
		}
		
		if(str.indexOf("nav_left")==0)
		{
		  return Integer.parseInt(arr[2]);
		}
		
		throw new Exception(str);
	}
	
	
	public static List<Integer> getRoleIntFromRoleString(String str) throws Exception
	{
		List<Integer> list=new ArrayList<Integer>();
		String[] arr =str.split("，");
		
		for(String s:arr)
		{
			if(s.equals("超级管理员"))
			{
				s="系统管理员";
			}
			int role=getIntFromRoleDes(s);
			if(role!=-1)
			{
				list.add(role);
			}
			else
			{
				throw new Exception();
			}
			
		}
		return list;
	}
	
	
	
	
	public static Integer getIntFromRoleDes(String des)
	{
		
		if(des.equals("教育局用户"))
		{
			return UserRole.EDUCATION.getRole();
		}
		for(UserRole r:UserRole.values())
		{
			if(r.getDes().equals(des))
			{
				return r.getRole();
			}
		}
		
		return -1;
	}
}
