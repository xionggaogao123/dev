package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.db.cloudlesson.CloudLessonDao;
import com.db.resources.ResourceDao;
import com.pojo.cloudlesson.CloudLessonEntry;
import com.pojo.resources.ResourceEntry;
import com.sys.exceptions.IllegalParamException;

public class ResourcesUpdates {

    public static void main(String[] args) throws IOException {


        File file = new File("/home/micro0111.txt");

        ResourceDao resourceDao = new ResourceDao();

        CloudLessonDao cloudLessonDao = new CloudLessonDao();

        int skip = 0;
        int limit = 200;

        while (true) {
            System.out.println("skip=" + skip);
            List<ResourceEntry> list = resourceDao.getResourceEntry(skip, limit);

            if (null == list || list.size() == 0) {
                break;
            }

            for (ResourceEntry e : list) {
                if (StringUtils.isBlank(e.getImgUrl())) {

//					{
//						CloudLessonEntry ce=cloudLessonDao.getCloudLessonEntryByVideoid(e.getName());
//						if(null!=ce && StringUtils.isNotBlank(ce.getImageUrl()))
//						{
//							try {
//								resourceDao.update(e.getID(), "iurl", ce.getImageUrl());
//								FileUtils.write(file, "\r\n",true);
//								FileUtils.write(file, e.getID().toString()+","+e.getName(),true);
//								System.out.println(e.getID().toString());
//							} catch (IllegalParamException e1) {
//								
//							}
//						}
                    //}
                }
            }


            skip = skip + 200;
        }


    }
}
