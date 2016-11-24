package com.db.temp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.repertory.CoursewareDao;
import com.db.resources.CloudResourceDao;
import com.db.resources.ResourceDao;
import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.IdValuePair;
import com.pojo.repertory.CoursewareEntry;
import com.pojo.repertory.PropertyObject;
import com.pojo.repertory.RepertoryProperty;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;


public class ResourceToYunResource {


    public static void main(String[] args) {

        ResourceDao resourceDao = new ResourceDao();
        CoursewareDao coursewareDao = new CoursewareDao();
        ResourceDictionaryDao resourceDictionaryDao = new ResourceDictionaryDao();
        CloudResourceDao cloudResourceDao = new CloudResourceDao();

        //目录
        ResourceDictionaryEntry defaultmulu = resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf("101102101101106"), 6);

        ResourceDictionaryEntry defaultzhishidian = resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf("101102101101"), 8);

        int skip = 0;
        int limit = 200;
        long count = resourceDao.count();
        System.out.println("count=" + count);

        int error = 0;
        while (true) {
            System.out.println("skip=" + skip);
            List<ResourceEntry> list = resourceDao.getResourceEntry(skip, limit);

            if (null == list || list.isEmpty()) {
                break;
            }
            for (ResourceEntry ue : list) {
                try {
                    List<PropertyObject> versionList = new ArrayList<PropertyObject>();
                    List<PropertyObject> knowledgeList = new ArrayList<PropertyObject>();

                    ObjectId kwId = null;
                    ObjectId banbenId = null;

                    if (ue.getPsbsList().size() != 0) {
                        banbenId = ue.getPsbsList().get(0);
                    }


                    if (ue.getScList().size() != 0) {
                        kwId = ue.getScList().get(0);
                    }


                    if (null == kwId) {
                        kwId = defaultzhishidian.getID();
                    }


                    if (null == banbenId) {
                        banbenId = defaultmulu.getID();
                    }


                    if (null == kwId || null == banbenId) {
                        continue;
                    }

                    //知识点
                    ResourceDictionaryEntry kwRe = resourceDictionaryDao.getResourceDictionaryEntry(kwId);

                    if (null != kwRe && kwRe.getParentInfos().size() != 0) {
                        for (IdValuePair idv : kwRe.getParentInfos()) {
                            knowledgeList.add(new PropertyObject(idv.getId().toString(), idv.getValue().toString()));
                        }
                        knowledgeList.add(new PropertyObject(kwRe.getID().toString(), kwRe.getName().toString()));
                    }


                    //章节
                    ResourceDictionaryEntry psbRe = resourceDictionaryDao.getResourceDictionaryEntry(banbenId);

                    if (null != psbRe && psbRe.getParentInfos().size() != 0) {
                        for (IdValuePair idv : psbRe.getParentInfos()) {
                            versionList.add(new PropertyObject(idv.getId().toString(), idv.getValue().toString()));
                        }
                        versionList.add(new PropertyObject(psbRe.getID().toString(), psbRe.getName().toString()));
                    }

                    RepertoryProperty pp = new RepertoryProperty(versionList, knowledgeList);

                    List<RepertoryProperty> aaalist = new ArrayList<RepertoryProperty>();

                    aaalist.add(pp);

                    ObjectId uid = new ObjectId("55934c26f6f28b7261c1baae");
                    String userName = "siri";
                    long timestamp = System.currentTimeMillis();

                    String image = ue.getImgUrl();

                    if (StringUtils.isBlank(image)) {
                        try {
                            String bk = ue.getBucketkey().toLowerCase();
                            if (bk.endsWith("doc") || bk.endsWith("docx")) {
                                image = "http://www.k6kt.com/images/resource/word.png";
                            } else if (bk.endsWith("ppt") || bk.endsWith("pptx")) {
                                image = "http://www.k6kt.com/images/resource/ppt.png";
                            } else {
                                image = "http://www.k6kt.com/images/resource/pdf.png";
                            }
                        } catch (Exception ex) {

                        }
                    }

                    ObjectId fileId = ue.getID();
                    String rf = "资源板块前端";

                    CoursewareEntry cwe = new CoursewareEntry(uid, userName, timestamp, image, fileId, aaalist, 1, new ObjectId("56188a854a5d271b38eba95f"), rf);

                    ResourceEntry cer = cloudResourceDao.getResourceEntryById(ue.getID());
                    if (null == cer) {
                        coursewareDao.addCourseware(cwe);
                        cloudResourceDao.addResource(ue);
                    }

                } catch (Exception ex) {
                    error = error + 1;
                }
            }
            skip = skip + 200;
        }


        System.out.println("error=" + error);
    }


}
