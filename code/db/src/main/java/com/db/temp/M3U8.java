package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.db.cloudlesson.CloudLessonDao;
import com.db.resources.ResourceDao;
import com.db.video.VideoDao;
import com.pojo.resources.ResourceEntry;
import com.pojo.video.VideoEntry;

public class M3U8 {

	public static void main(String[] args) throws IOException {
        File file =new File("/home/micro0324.txt");
        file.createNewFile();
	    VideoDao dao =new VideoDao();
		
		int skip=0;
		int limit=200;
		
		while(true)
		{
			System.out.println("skip="+skip);
			List<VideoEntry> list=dao.getVideoEntrys(skip, limit);
			
			if(null==list || list.size()==0)
			{
				break;
			}
			
			for(VideoEntry e:list)
			{
				if(StringUtils.isNotBlank(e.getBucketkey()))
				{
					String ss="http://7sbnym.com1.z0.glb.clouddn.com/m3u8/"+e.getBucketkey()+".m3u8";
					FileUtils.write(file, ss,true);
					FileUtils.write(file, "\r\n",true);

				}
			}
			
			
			skip=skip+200;
		}
		
		
	}
}
