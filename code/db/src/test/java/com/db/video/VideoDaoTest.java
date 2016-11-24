package com.db.video;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

public class VideoDaoTest {

	private VideoDao dao =new VideoDao();
	
	@Test
	public void addVideoEntry()
	{
		VideoEntry e =new VideoEntry("测试视频", 888l, VideoSourceType.VIDEO_CLOUD_CLASS.getType(),"");
		
		dao.addVideoEntry(e);
		System.out.println(e.getID());
	}
	
	@Test
	public void update()
	{
		try {
			dao.update(new ObjectId("54f57b39fe5b1dd8e1a25029"), "cn", 500);
		} catch (IllegalParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void getVideoEntryById()
	{
		VideoEntry e=dao.getVideoEntryById(new ObjectId("54f57b39fe5b1dd8e1a25029"));
		System.out.println(e);
		System.out.println(e.getBaseEntry().keySet().size());
	}
	
	@Test
	public void getVideoEntryMap()
	{
		List<ObjectId> list =new ArrayList<ObjectId>();
		list.add(new ObjectId("54f57b39fe5b1dd8e1a25029"));
		list.add(new ObjectId("54f57c3efe5ba878d22bda22"));
		
		System.out.println(dao.getVideoEntryMap(list, Constant.FIELDS));
	}
}
