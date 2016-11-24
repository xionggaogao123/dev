package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.resources.ResourceDao;

/**
 * 删除资源
 * @author fourer
 *
 */
public class ResourceDelete {

	public static void main(String[] args) throws IOException {
		
		List<String> list =FileUtils.readLines(new File("d:\\problem.txt"), "utf-8");
		ResourceDao rDao =new ResourceDao();
		for(String str:list)
		{
			int index=str.indexOf("=");
			
			if(index>0)
			{
				String idstr=str.substring(index+1);
				if(ObjectId.isValid(idstr))
				{
					rDao.remove(new ObjectId(idstr));
				}
			}
		}
	}
}
