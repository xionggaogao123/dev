package com.fulaan.forum.service;

import com.db.forum.FCollectionDao;
import com.db.forum.FPostDao;
import com.db.forum.FReplyDao;
import com.db.forum.FSectionDao;
import com.db.user.UserDao;
import com.pojo.forum.*;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangkaidong on 2016/5/30.
 */
@Service
public class FCollectionService {
    FCollectionDao collectionDao = new FCollectionDao();
    FPostDao fPostDao = new FPostDao();
    FSectionDao fSectionDao = new FSectionDao();
    UserDao userDao = new UserDao();
    FReplyDao fReplyDao = new FReplyDao();

    /**
     * 新增
     *
     * @param userId
     * @param postSectionId
     * @param type
     */
    public void addCollection(ObjectId userId, String postSectionId, int type) {
        FCollectionEntry collectionEntry = new FCollectionEntry(userId, new ObjectId(postSectionId), type, System.currentTimeMillis());
        collectionDao.addCollection(collectionEntry);
    }

    /**
     * 获取收藏
     *
     * @param userId
     * @param type
     * @return
     */
    public List<FCollectionDTO> getCollections(ObjectId userId, int type) {

        List<FCollectionEntry> fCollectionEntries = collectionDao.getCollection(userId, type);
        List<FCollectionDTO> collectionDTOList = new ArrayList<FCollectionDTO>();
        if (null != fCollectionEntries) {
            for (FCollectionEntry fCollectionEntry : fCollectionEntries) {
                FCollectionDTO fCollectionDTO = new FCollectionDTO(fCollectionEntry);
                fCollectionDTO.setTime(DateTimeUtils.convert(fCollectionEntry.getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
                if (type == 0) {
                    FPostEntry fPostEntry = fPostDao.getFPostEntry(fCollectionEntry.getPostSectionId());
                    fCollectionDTO.setPostTitle(fPostEntry.getPostTitle());
                    fCollectionDTO.setSectionId(fPostEntry.getPostSectionId().toString());
                    fCollectionDTO.setPersonId(fPostEntry.getPersonId().toString());
                } else if (type == 1) {
                    FSectionEntry fSectionEntry = fSectionDao.getFSection(fCollectionEntry.getPostSectionId());
                    fCollectionDTO.setPostTitle(fSectionEntry.getName());
                }


                collectionDTOList.add(fCollectionDTO);
            }
        }
        return collectionDTOList;
    }

    /**
     * 为app端处理视频
     *
     * @param versionVideo
     * @return
     */
    public List<Map<String, String>> dealVideo(String versionVideo) {

        List<Map<String, String>> returnString = new ArrayList<Map<String, String>>();

        if (StringUtils.isNotBlank(versionVideo)) {
            if (versionVideo.indexOf(",") > -1) {
                String[] str = versionVideo.split(",");
                for (String item : str) {
                    String[] kl = item.split("\\$");
                    Map map = new HashMap();
                    map.put("imageUrl", kl[1]);
                    map.put("videoUrl", kl[0]);
                    returnString.add(map);
                }
            } else {
                String[] kl = versionVideo.split("\\$");
                Map map = new HashMap();
                map.put("imageUrl", kl[1]);
                map.put("videoUrl", kl[0]);
                returnString.add(map);
            }
        }
        return returnString;
    }

    //手机端接口
    public List<FAppCollectPostDTO> getAppCollect(ObjectId userId, int type) {
        List<FCollectionEntry> fCollectionEntries = collectionDao.getCollection(userId, type);
        List<FAppCollectPostDTO> collectionDTOList = new ArrayList<FAppCollectPostDTO>();
        if (null != fCollectionEntries) {
            for (FCollectionEntry fCollectionEntry : fCollectionEntries) {
                if (type == 0) {
                    FPostEntry fPostEntry = fPostDao.getFPostEntry(fCollectionEntry.getPostSectionId());
                    if (fPostEntry != null) {
                        FAppCollectPostDTO fAppCollectPostDTO = new FAppCollectPostDTO();
                        fAppCollectPostDTO.setPersonId(fPostEntry.getPersonId().toString());
                        fAppCollectPostDTO.setCollectedId(fCollectionEntry.getID().toString());
                        fAppCollectPostDTO.setFpostId(fPostEntry.getID().toString());
                        fAppCollectPostDTO.setPostSectionId(fPostEntry.getPostSectionId().toString());
                        fAppCollectPostDTO.setPostTime(DateTimeUtils.convert(fPostEntry.getTime(), DateTimeUtils.DATE_YYYY_MM_DD));
                        fAppCollectPostDTO.setTime(fPostEntry.getTime());
                        fAppCollectPostDTO.setPlainText(fPostEntry.getPlainText());
                        fAppCollectPostDTO.setPostTitle(fPostEntry.getPostTitle());
                        fAppCollectPostDTO.setInSet(fPostEntry.getInSet());

                        if (fPostEntry.getPersonId() != null) {
                            UserEntry dto = userDao.getUserEntry(fPostEntry.getPersonId(), Constant.FIELDS);
                            if (dto != null) {
                                fAppCollectPostDTO.setUserImage(AvatarUtils.getAvatar(dto.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                                if (StringUtils.isNotBlank(dto.getNickName())) {
                                    fAppCollectPostDTO.setNickName(dto.getNickName());
                                } else {
                                    fAppCollectPostDTO.setNickName(dto.getUserName());
                                }
                            } else {
                                fAppCollectPostDTO.setUserImage(null);
                            }
                        } else {
                            fAppCollectPostDTO.setUserImage(null);
                        }
                        int commentCount = fReplyDao.getFReplyEntriesCount(fPostEntry.getID(), null, null, -1, -1);
                        fAppCollectPostDTO.setCommentCount(commentCount);
                        fAppCollectPostDTO.setPraiseCount(fPostEntry.getPraiseCount());
                        fAppCollectPostDTO.setScanCount(fPostEntry.getScanCount());
                        String comment = fPostEntry.getComment();
                        String appImageList = fPostEntry.getAppImageList();
                        String appVideoList = fPostEntry.getAppVideoList();
                        if (StringUtils.isNotBlank(comment)) {
                            //web端上传的
                            fAppCollectPostDTO.setImageList(dealWithImage(comment));
                            //新版本兼容
                            if (StringUtils.isNotBlank(fPostEntry.getVersion())) {
                                fAppCollectPostDTO.setVideoList(dealVideo(fPostEntry.getVersionVideo()));
                            } else {
                                fAppCollectPostDTO.setVideoList(dealWithVideo(comment));
                            }
                        } else {
                            //app端上传的
                            if (StringUtils.isNotBlank(appImageList)) {
                                fAppCollectPostDTO.setImageList(getAppImageList(appImageList));
                            } else {
                                List<String> item = new ArrayList<String>();
                                fAppCollectPostDTO.setImageList(item);
                            }
                            if (StringUtils.isNotBlank(appVideoList)) {
                                fAppCollectPostDTO.setVideoList(getAppVideoList(appVideoList));
                            } else {
                                List<Map<String, String>> map = new ArrayList<Map<String, String>>();
                                fAppCollectPostDTO.setVideoList(map);
                            }
                        }
                        collectionDTOList.add(fAppCollectPostDTO);
                    }
                } else if (type == 1) {
                    FAppCollectPostDTO fAppCollectPostDTO = new FAppCollectPostDTO();
                    FSectionEntry fSectionEntry = fSectionDao.getFSection(fCollectionEntry.getPostSectionId());
                    fAppCollectPostDTO.setPostSectionId(fSectionEntry.getID().toString());
                    fAppCollectPostDTO.setName(fSectionEntry.getName());
                    fAppCollectPostDTO.setSectionName(fSectionEntry.getSectionName());
                    fAppCollectPostDTO.setMemo(fSectionEntry.getMemo());
                    fAppCollectPostDTO.setMemoName(fSectionEntry.getMemoName());
                    fAppCollectPostDTO.setImageAppSrc(fSectionEntry.getImageAppSrc());
                    fAppCollectPostDTO.setImageBigAppSrc(fSectionEntry.getImageBigAppSrc());
                    collectionDTOList.add(fAppCollectPostDTO);
                }
            }
        }
        return collectionDTOList;
    }

    //处理app端上传的图像
    public List<String> getAppImageList(String imageStr) {
        List<String> retList = new ArrayList<String>();
        if (imageStr.indexOf(",") > -1) {
            String[] imageList = imageStr.split(",");
            for (int i = 0; i < imageList.length; i++) {
                retList.add(imageList[i]);
            }
        } else {
            retList.add(imageStr);
        }

        return retList;
    }

    //处理app端上传的视频
    public List<Map<String, String>> getAppVideoList(String videoStr) {
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
        if (videoStr.indexOf(",") > -1) {
            String[] videoList = videoStr.split(",");
            for (String item : videoList) {
                Map<String, String> map = new HashMap<String, String>();
                String[] mItem = item.split("@");
                map.put("imageUrl", mItem[2]);
                map.put("videoUrl", mItem[1]);
                retList.add(map);
            }
        } else {
            Map<String, String> map = new HashMap<String, String>();
            String[] mItem = videoStr.split("@");
            map.put("imageUrl", mItem[2]);
            map.put("videoUrl", mItem[1]);
            retList.add(map);
        }

        return retList;
    }

    /**
     * 为app端处理图像
     *
     * @param plainContext
     * @return
     */
    public List<String> dealWithImage(String plainContext) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
        String regEx_img = "<(img|IMG)(.*?)(/>|></img>|>)";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(plainContext);
        boolean result_img = m_image.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_image.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    String str_src = m_src.group(3);
                    pics.add(str_src);
                }
                result_img = m_image.find();
            }
        }

        List<String> returnString = new ArrayList<String>();
        if (pics.size() > 0) {
            for (int i = 0; i < pics.size(); i++) {
                String item = pics.get(i);
                if (item.contains("http://www.fulaan.com/")) {
                    returnString.add(item);
                } else {
                    if (item.contains("/upload/ueditor")) {
                        String str = item.replaceAll("/upload/ueditor", "http://www.fulaan.com/upload/ueditor");
                        returnString.add(str);
                    } else {
                        returnString.add(item);
                    }
                }

            }
        }
        return returnString;
    }

    /**
     * 为app端处理视频
     *
     * @param plainContext
     * @return
     */
    public List<Map<String, String>> dealWithVideo(String plainContext) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();

        String regEx_img = "<(video|VIDEO)(.*?)(/>|></img>|>)";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(plainContext);
        boolean result_img = m_image.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_image.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    String str_src = m_src.group(3);
                    pics.add(str_src);
                }
                result_img = m_image.find();
            }
        }
        List<Map<String, String>> returnString = new ArrayList<Map<String, String>>();
        if (pics.size() > 0) {
            for (int i = 0; i < pics.size(); i++) {
                String item = pics.get(i);
                if (item.contains("/upload/ueditor")) {
                    String str = item.replaceAll("/upload/ueditor", "http://www.fulaan.com/upload/ueditor");
                    Map map = new HashMap();

                    map.put("imageUrl", "");
                    map.put("videoUrl", str);
                    returnString.add(map);
                } else {
                    Map map = new HashMap();
                    map.put("imageUrl", "");
                    map.put("videoUrl", item);
                    returnString.add(map);
                }
            }
        }
        return returnString;
    }

    /**
     * 查询该帖子/板块收藏的人数
     *
     * @param postSection
     * @return
     */
    public int getCollectionCount(String postSection) {
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        return collectionDao.getCollectionCount(postSectionId);
    }

    /**
     * 判断是否收藏
     *
     * @param userId
     * @param postSectionId
     * @return
     */
    public boolean isCollected(ObjectId userId, String postSectionId) {
        FCollectionEntry entry = collectionDao.getCollection(userId, new ObjectId(postSectionId));
        if (entry != null) {
            return true;
        }
        return false;
    }


    /**
     * 取消收藏
     *
     * @param id
     */
    public void remove(String id) {
        collectionDao.removeCollection(new ObjectId(id));
    }


}
