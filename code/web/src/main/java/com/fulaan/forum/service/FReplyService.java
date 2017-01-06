package com.fulaan.forum.service;

import com.db.forum.FPostDao;
import com.db.forum.FReplyDao;
import com.db.user.UserDao;
import com.fulaan.pojo.LikeInfo;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.BasicDBObject;
import com.pojo.ebusiness.SortType;
import com.pojo.forum.*;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/5/31.
 */
@Service
public class FReplyService {

    private FReplyDao fReplyDao = new FReplyDao();
    private FPostDao fPostDao = new FPostDao();
    private UserDao userDao = new UserDao();

    /**
     * 通过昵称返回userId
     */
    public String getUserIdByNickName(String nickName) {
        if (nickName.lastIndexOf('回') > -1) {
            String n2 = nickName.substring(nickName.indexOf('复') + 2);
            n2 = n2.replaceAll(" ", "");
            List<ObjectId> l = userDao.findIdListByNickName("", n2);
            if (null != l && l.size() > 0) {
                return l.get(0).toString();
            } else {
                List<ObjectId> ll = userDao.findIdListByNickName(n2, "");
                if (null != ll && ll.size() > 0) {
                    return ll.get(0).toString();
                } else {
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    /**
     * 获取点赞的数据
     *
     * @param postId
     * @return
     */
    public List<LikeInfo> getLikeInfos(ObjectId postId) {
        List<FReplyEntry> replyEntries = fReplyDao.getReplyList(postId);
        List<LikeInfo> likeInfos = new ArrayList<LikeInfo>();
        for (FReplyEntry entry : replyEntries) {
            UserEntry userEntry = userDao.findByUserId(entry.getPersonId());
            LikeInfo likeInfo = new LikeInfo();
            likeInfo.setReplyId(entry.getID().toString());
            likeInfo.setFloor(entry.getFloor());
            likeInfo.setLikeCount(entry.getPraiseCount());
            likeInfo.setUserId(entry.getPersonId().toString());
            likeInfo.setUserName(entry.getPersonName());
            likeInfo.setReplyContent(entry.getPlainText());
            likeInfo.setUserNickName(userEntry.getNickName());
            likeInfo.setUserEmail(userEntry.getEmail());
            likeInfo.setUserPhone(userEntry.getMobileNumber());

            List<ObjectId> userIdList = entry.getUserReplyList();
            List<LikeInfo.UserInfo> userInfos = new ArrayList<LikeInfo.UserInfo>();
            for (ObjectId uid : userIdList) {
                UserEntry user = userDao.findByUserId(uid);
                if (user != null) {
                    LikeInfo.UserInfo userInfo = new LikeInfo.UserInfo();
                    userInfo.setId(user.getID().toString());
                    userInfo.setName(user.getUserName());
                    userInfo.setNick(user.getNickName());
                    userInfo.setIp(user.getInterviewIP());
                    userInfos.add(userInfo);
                }
            }
            likeInfo.setUserInfoList(userInfos);
            likeInfos.add(likeInfo);
        }
        return likeInfos;
    }


    /**
     * 为app端处理图像
     *
     * @param plainContext
     * @return
     */
    private List<String> dealWithImage(String plainContext) {
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
                if (item.contains("/upload/ueditor")) {
                    returnString.add(item);
                } else {
                    returnString.add(item);
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
    private List<Map<String, String>> dealWithVideo(String plainContext) {
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
                Map map = new HashMap();
                map.put("imageUrl", "");
                map.put("videoUrl", item);
                returnString.add(map);
            }
        }
        return returnString;
    }

    /**
     * 根据回复人查询回复人回复的贴子
     *
     * @param person   回复人
     * @param page     页数
     * @param pageSize
     * @return
     */
    public List<FPostDTO> getFPostListByCondition(int orderType, String person, int page, int pageSize) {
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        List<FPostDTO> retList = new ArrayList<FPostDTO>();
        List<FReplyEntry> fReplyEntryList = fReplyDao.getFReplyByCondition(Constant.FIELDS, sort, personId, -1, (page - 1) * pageSize, pageSize);
        for (FReplyEntry fReplyEntry : fReplyEntryList) {
            ObjectId postId = fReplyEntry.getPostId();
            FPostEntry fPostEntry = fPostDao.getFPostEntry(postId);
            if (null != fPostEntry) {
                retList.add(new FPostDTO(fPostEntry));
            }
        }
        return retList;
    }

    /**
     * 根据回复人查询改回复人回复的贴子
     *
     * @param person 回复人
     * @return
     */
    public int getFPostListByPerson(int orderType, String person) {
        int count = 0;
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        List<FReplyEntry> fReplyEntryList = fReplyDao.getFReplyByPerson(Constant.FIELDS, sort, personId, -1);
        for (FReplyEntry fReplyEntry : fReplyEntryList) {
            ObjectId postId = fReplyEntry.getPostId();
            FPostEntry fPostEntry = fPostDao.getFPostEntry(postId);
            if (null != fPostEntry) {
                count += 1;
            }

        }
        return count;
    }

    public long timeText(ObjectId postId, int flooor) {
        return fReplyDao.timeText(postId, flooor);
    }

    /**
     * 更新点赞
     *
     * @param userReplyId
     * @param replyId
     */
    public void updateBtnZan(ObjectId userReplyId, ObjectId replyId) {
        fReplyDao.updateBtnZan(userReplyId, replyId);
    }

    /**
     * 获取自己的点赞数
     *
     * @param post
     * @param userId
     * @return
     */
    public int getPraiseCount(String post, String userId) {
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        ObjectId personId = userId.equals("") ? null : new ObjectId(userId);
        return fReplyDao.getPraiseCount(postId, personId);
    }

    /**
     * 排行列表
     *
     * @param post
     * @return
     */
    public List<FReplyDTO> getFReplyListRank(String post) {
        ObjectId postId = new ObjectId(post);
        List<FReplyEntry> fReplyEntries = fReplyDao.getFReplyListRank(postId);
        List<FReplyDTO> fReplyDTOList = new ArrayList<FReplyDTO>();
        boolean flag = true;
        for (FReplyEntry fReplyEntry : fReplyEntries) {
            FReplyDTO fReplyDTO = new FReplyDTO(fReplyEntry);
            UserEntry ee = userDao.getUserEntry(new ObjectId(fReplyDTO.getPersonId()), Constant.FIELDS);
            if (null != ee) {
                fReplyDTO.setImageSrc(AvatarUtils.getAvatar(ee.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                if (null != ee.getNickName() && !"".equals(ee.getNickName())) {
                    fReplyDTO.setReplyNickName(ee.getNickName());
                } else {
                    fReplyDTO.setReplyNickName(ee.getUserName());
                }
            }
            for (FReplyDTO item : fReplyDTOList) {
                if (fReplyDTO.getPersonId().equals(item.getPersonId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if (fReplyDTO.getPraiseCount() != 0) {
                    fReplyDTOList.add(fReplyDTO);
                }
            }
            flag = true;

        }
        return fReplyDTOList;
    }

    public List<FReplyEntry> getReplys(ObjectId postId) {
        return fReplyDao.getReplyList(postId);
    }


    /**
     * 处理新版本的视频
     */
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

    /**
     * 分享贴子Id
     *
     * @param Id
     * @return
     */
    public FReplyDTO appShare(ObjectId Id, String client) {
        FReplyEntry fReplyEntry = fReplyDao.getFReplyEntry(Id);
        FReplyDTO fReplyDTO = new FReplyDTO(fReplyEntry);
        long time = fReplyEntry.getTime();
        long nowTime = System.currentTimeMillis();
        long day = (nowTime - time) / (1000 * 60 * 60 * 24);
        fReplyDTO.setTimeText(fReplyEntry.getTime());
        fReplyDTO.setTime(day);
        String appImageList = fReplyDTO.getAppImageList();
        String appVideoList = fReplyDTO.getAppVideoList();

        UserEntry ee = userDao.getUserEntry(new ObjectId(fReplyDTO.getPersonId()), Constant.FIELDS);

        if (ee != null) {
            if (null != ee.getNickName() && !"".equals(ee.getNickName())) {
                fReplyDTO.setReplyNickName(ee.getNickName());
            } else {
                fReplyDTO.setReplyNickName(ee.getUserName());
            }
            fReplyDTO.setImageSrc(AvatarUtils.getAvatar(ee.getAvatar(), AvatarType.MIN_AVATAR.getType()));
        }

        if (!"".equals(fReplyDTO.getReplyComment())) {
            //web端上传的
            String comment = fReplyDTO.getReplyComment().replaceAll("/upload/ueditor", "http://www.fulaan.com/upload/ueditor");
            fReplyDTO.setReplyComment(comment);
            fReplyDTO.setImageList(dealWithImage(comment));
            if (StringUtils.isNotBlank(fReplyDTO.getVersion())) {
                fReplyDTO.setVideoList(dealVideo(fReplyDTO.getVersionVideo()));
            } else {
                fReplyDTO.setVideoList(dealWithVideo(comment));
            }
        } else {
            //app端上传的
            if (StringUtils.isNotBlank(appImageList)) {
                fReplyDTO.setImageList(getAppImageList(appImageList));
            } else {
                List<String> item = new ArrayList<String>();
                fReplyDTO.setImageList(item);
            }
            if (StringUtils.isNotBlank(appVideoList)) {
                fReplyDTO.setVideoList(getAppVideoList(appVideoList));
            } else {
                List<Map<String, String>> map = new ArrayList<Map<String, String>>();
                fReplyDTO.setVideoList(map);
            }
        }
        List<FReplyDTO> f = getFRepliesList(2, "", "", "", 1, fReplyDTO.getfReplyId(), 1, 1, client);
        if (null != f && f.size() > 0) {
            int count = f.get(0).getRepliesList().size();
            fReplyDTO.setReplyToCount(count);
            List<RepliesDTO> repliesDTOList = f.get(0).getRepliesList();
            StringBuffer str = new StringBuffer();
            if (null != repliesDTOList && repliesDTOList.size() > 0) {
                for (RepliesDTO item : repliesDTOList) {
                    if (item.getContent().length() > 30) {
                        str.append(item.getContent().substring(0, 30)).append("...");
                    } else {
                        str.append(item);
                    }
                    String timeText = DateTimeUtils.convert(item.getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
                    item.setTimeText(timeText);
                    String itemPersonId = item.getPersonId();
                    String itemUserId = item.getUserId();
                    String nickName = item.getNickName();
                    StringBuffer str1 = new StringBuffer();
                    String image = "";
                    if (nickName.lastIndexOf('回') > -1) {
                        String n1 = "";
                        String n2 = "";
                        UserEntry e = userDao.getUserEntry(new ObjectId(itemPersonId), Constant.FIELDS);

                        if (e.getNickName() == null || "".equals(e.getNickName())) {
                            n1 = e.getUserName();
                        } else {
                            n1 = e.getNickName();
                        }

                        image = e.getAvatar();
                        UserEntry eItem = userDao.getUserEntry(new ObjectId(itemUserId), Constant.FIELDS);
                        if (null != eItem) {
                            if (StringUtils.isNotBlank(eItem.getNickName())) {
                                n2 = eItem.getNickName();
                            } else {
                                n2 = eItem.getUserName();
                            }
                        }
                        str1.append(n1 + " 回复 @" + n2);
                    } else {
                        UserEntry e = userDao.getUserEntry(new ObjectId(itemPersonId), Constant.FIELDS);

                        image = e.getAvatar();
                        if (e.getNickName() == null || "".equals(e.getNickName())) {
                            str1.append(e.getUserName());
                        } else {
                            str1.append(e.getNickName());
                        }
                    }

                    item.setNickName(str1.toString());

                    item.setImageStr(AvatarUtils.getAvatar(image, AvatarType.MIN_AVATAR.getType()));
                }

                fReplyDTO.setRepliesFlag(1);
            } else {
                fReplyDTO.setRepliesFlag(0);
            }
            fReplyDTO.setRepliesList(repliesDTOList);
        }


        return fReplyDTO;
    }

    private static final Logger logger = Logger.getLogger(FReplyService.class);


    public List<Map<String, String>> getVoiceStr(String voiceFile) {
        List<Map<String, String>> re = new ArrayList<Map<String, String>>();
        if (voiceFile.indexOf(",") > 0) {
            String[] vo = voiceFile.split(",");
            for (int i = 0; i < vo.length; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("url", vo[i]);
                map.put("vId", vo[i].substring(vo[i].lastIndexOf("/") + 1, vo[i].lastIndexOf(".")));
                re.add(map);
            }
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("url", voiceFile);
            map.put("vId", voiceFile.substring(voiceFile.lastIndexOf("/") + 1, voiceFile.lastIndexOf(".")));
            re.add(map);
        }
        return re;
    }

    /**
     * 回帖列表
     *
     * @param orderType   排序方式（时间先后）
     * @param postSection
     * @param post
     * @param page
     * @param pageSize
     * @return
     */
    public List<FReplyDTO> getFRepliesList(int orderType, String postSection, String post, String person, int repliesToReply, String replyPost,
                                           int page, int pageSize, String client) {
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        ObjectId replyPostId = replyPost.equals("") ? null : new ObjectId(replyPost);
        List<FReplyEntry> fReplyEntries = fReplyDao.getFReplyEntries(-1, Constant.FIELDS, sort, postSectionId, postId, personId, replyPostId, repliesToReply,
                (page - 1) * pageSize, pageSize);
        List<FReplyDTO> fReplyDTOList = new ArrayList<FReplyDTO>();

        if (null != fReplyDTOList) {
            for (FReplyEntry fReplyEntry : fReplyEntries) {

                StringBuffer str = new StringBuffer();
                FReplyDTO fReplyDTO = new FReplyDTO(fReplyEntry);

                //=================word=========================
                fReplyDTO.setWord(fReplyEntry.getUserWord());
                fReplyDTO.setPdf(fReplyEntry.getUserPdf());
                fReplyDTO.setWordName(fReplyEntry.getUserWordName());
                logger.info(fReplyEntry.getUserPdf());
                //================word===========================


                long time = fReplyEntry.getTime();
                long nowTime = System.currentTimeMillis();
                long day = (nowTime - time) / (1000 * 60 * 60 * 24);
                fReplyDTO.setTimeText(fReplyEntry.getTime());
                fReplyDTO.setTime(day);

                fReplyDTO.setAppAudioStr(fReplyEntry.getAppAudioStr());
                String appImageList = fReplyDTO.getAppImageList();
                String appVideoList = fReplyDTO.getAppVideoList();
                String appAudioStr = fReplyDTO.getAppAudioStr();
                String voiceFile = fReplyDTO.getVoiceFile();

                if (!StringUtils.isBlank(appAudioStr)) {
                    fReplyDTO.setAudioList(getAppImageList(appAudioStr));
                }

                //处理语音
                if (StringUtils.isNotBlank(voiceFile)) {
                    fReplyDTO.setVoiceList(getVoiceStr(voiceFile));
                }

                UserEntry ee = userDao.getUserEntry(new ObjectId(fReplyDTO.getPersonId()), Constant.FIELDS);

                if (client.contains("iOS") || client.contains("Android")) {
                    //为app端处理图像以及视频
                    //先判断是web端上传的还是app端上传的
                    if (!"".equals(fReplyDTO.getReplyComment())) {
                        //web端上传的
                        String comment = fReplyDTO.getReplyComment().replaceAll("/upload/ueditor", "http://www.fulaan.com/upload/ueditor");
                        fReplyDTO.setReplyComment(comment);
                        List<String> images = dealWithImage(comment);
                        List<String> filterImages = new ArrayList<String>();
                        //过滤baidu 表情
                        for (String image : images) {
                            if (!image.contains("baidu")) {
                                filterImages.add(image);
                            }
                        }


                        fReplyDTO.setImageList(filterImages);

                        if (StringUtils.isNotBlank(fReplyDTO.getVersion())) {
                            fReplyDTO.setVideoList(dealVideo(fReplyDTO.getVersionVideo()));
                        } else {
                            fReplyDTO.setVideoList(dealWithVideo(comment));
                        }
                    } else {
                        //app端上传的
                        if (StringUtils.isNotBlank(appImageList)) {
                            fReplyDTO.setImageList(getAppImageList(appImageList));
                        } else {
                            List<String> item = new ArrayList<String>();
                            fReplyDTO.setImageList(item);
                        }
                        if (StringUtils.isNotBlank(appVideoList)) {
                            fReplyDTO.setVideoList(getAppVideoList(appVideoList));
                        } else {
                            List<Map<String, String>> map = new ArrayList<Map<String, String>>();
                            fReplyDTO.setVideoList(map);
                        }

                    }


                } else {
                    if (!"".equals(fReplyDTO.getReplyComment())) {

                    } else {
                        //app端上传的
                        if (StringUtils.isNotBlank(appImageList)) {
                            fReplyDTO.setImageList(getAppImageList(appImageList));
                        } else {
                            List<String> item = new ArrayList<String>();
                            fReplyDTO.setImageList(item);
                        }
                        if (StringUtils.isNotBlank(appVideoList)) {
                            fReplyDTO.setVideoList(getAppVideoList(appVideoList));
                        } else {
                            List<Map<String, String>> map = new ArrayList<Map<String, String>>();
                            fReplyDTO.setVideoList(map);
                        }
                    }
                }
                if (ee != null) {
                    if (null != ee.getNickName() && !"".equals(ee.getNickName())) {
                        fReplyDTO.setReplyNickName(ee.getNickName());
                    } else {
                        fReplyDTO.setReplyNickName(ee.getUserName());
                    }
                    fReplyDTO.setImageSrc(AvatarUtils.getAvatar(ee.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                }

                //判断是回复给主题还是回复给某个回帖
                List<RepliesDTO> repliesDTOList = fReplyDTO.getRepliesList();

                if (null != repliesDTOList && repliesDTOList.size() > 0) {
                    for (RepliesDTO item : repliesDTOList) {
                        if (item.getContent().length() > 30) {
                            str.append(item.getContent().substring(0, 30)).append("...");
                        } else {
                            str.append(item);
                        }
                        String timeText = DateTimeUtils.convert(item.getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
                        item.setTimeText(timeText);
                        String itemPersonId = item.getPersonId();
                        String itemUserId = item.getUserId();
                        String nickName = item.getNickName();
                        StringBuffer str1 = new StringBuffer();
                        String image = "";
                        if (nickName.lastIndexOf('回') > -1) {

                            String n1 = "";
                            String n2 = "";
                            UserEntry e = userDao.getUserEntry(new ObjectId(itemPersonId), Constant.FIELDS);

                            if (e.getNickName() == null || "".equals(e.getNickName())) {
                                n1 = e.getUserName();
                            } else {
                                n1 = e.getNickName();
                            }

                            image = e.getAvatar();
                            UserEntry eItem = userDao.getUserEntry(new ObjectId(itemUserId), Constant.FIELDS);
                            if (null != eItem) {
                                if (StringUtils.isNotBlank(eItem.getNickName())) {
                                    n2 = eItem.getNickName();
                                } else {
                                    n2 = eItem.getUserName();
                                }
                            }
                            str1.append(n1 + " 回复 @" + n2);
                        } else {
                            UserEntry e = userDao.getUserEntry(new ObjectId(itemPersonId), Constant.FIELDS);

                            if (null != e) {
                                image = e.getAvatar();
                                if (e.getNickName() == null || "".equals(e.getNickName())) {
                                    str1.append(e.getUserName());
                                } else {
                                    str1.append(e.getNickName());
                                }
                            }
                        }
                        item.setNickName(str1.toString());

                        item.setImageStr(AvatarUtils.getAvatar(image, AvatarType.MIN_AVATAR.getType()));
                    }
                    fReplyDTO.setRepliesFlag(1);
                } else {
                    fReplyDTO.setRepliesFlag(0);
                }
                fReplyDTO.setRepliesList(repliesDTOList);
                //判断是否为楼中贴
                fReplyDTOList.add(fReplyDTO);
            }
        }
        return fReplyDTOList;

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
     * 根据postID（发帖Id)来删除回帖
     */
    public void removeFReplyByPostId(ObjectId postId) {
        fReplyDao.removeFReplyByPostId(postId);
    }

    /**
     * 根据postID（发帖Id)来删除回帖
     */
    public void removeFReply(ObjectId id) {
        fReplyDao.removeReplyLogic(id);
    }

    /**
     * 查询回帖数量
     *
     * @param postSection
     * @param post
     * @return
     */
    public int getFRepliesCount(String postSection, String post, String person, String replyPost, int repliesToReply) {
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        ObjectId replyPostId = replyPost.equals("") ? null : new ObjectId(replyPost);
        return fReplyDao.getFReplyEntriesCount(postId, personId, replyPostId, repliesToReply, 1);

    }

    public int getFRepliyCountByPostId(String postId) {
        return getFRepliesCount("", postId, "", "", -1);
    }

    public int getFRepliyCountByPersonId(String personId) {
        return getFRepliesCount("", "", personId, "", -1);
    }

    public int getFloor(int floor, String post) {
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        List<FReplyEntry> fReplyEntries = fReplyDao.getFloor(postId);
        int count = 0;
        if (null != fReplyEntries) {
            for (FReplyEntry fReplyEntry : fReplyEntries) {
                int itemFloor = fReplyEntry.getFloor();
                if (itemFloor >= floor) {
                    count = count + 1;
                } else {
                    break;
                }
            }
        }
        return count;
    }

    public int getMaxFloor(String post) {
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        return fReplyDao.getMaxFloor(postId);
    }

    /**
     * 楼层
     */
    public int getFloor(String postSection, String post, String person, String replyPost, int repliesToReply) {
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        ObjectId replyPostId = replyPost.equals("") ? null : new ObjectId(replyPost);
        return fReplyDao.getFReplyEntriesCount(postId, personId, replyPostId, repliesToReply, -1);

    }

    /**
     * 帖子详情
     *
     * @param Id
     * @return
     */
    public FReplyDTO detail(ObjectId Id) {
        FReplyEntry fReplyEntry = fReplyDao.getFReplyEntry(Id);
        FReplyDTO fReplyDTO = new FReplyDTO(fReplyEntry);
        return fReplyDTO;
    }

    /**
     * entry详情
     * @param Id
     * @return
     */
    public FReplyEntry find(ObjectId Id){
        return fReplyDao.getFReplyEntry(Id);
    }

    /**
     * 保存楼层
     * @param fReplyEntry
     */
    public void saveFReplyEntryForFloor(FReplyEntry fReplyEntry){
        fReplyDao.saveFReplyEntryForFloor(fReplyEntry);
    }

    /**
     * 新增和覆盖
     *
     * @param fReplyDTO
     */
    public ObjectId addFPostEntry(FReplyDTO fReplyDTO) {
        return fReplyDao.addFReply(fReplyDTO.exportEntry());
    }


    /**
     * 下载七牛视频
     */
    public String m3u8ToMp4DownLoad(String fileUrl, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException {
        String filePath = fileUrl.split("#")[0];
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String prefix = fileName.substring(0, fileName.lastIndexOf("."))+".mp4";
        if (".m3u8".equals(suffix)) {
            InputStream in = null;
            InputStream subIn = QiniuFileUtils.downFileByUrl(filePath);
            int i = 0;
            String filePathsStr = "";
            boolean flag = false;
            while ((i = subIn.read()) != -1) {
                String sbuStr = (char) i + "";
                if ("\n".equals(sbuStr) && flag) {
                    filePathsStr += ",";
                    flag = false;
                }
                if ("h".equals(sbuStr) || "/".equals(sbuStr) || flag) {
                    flag = true;
                    filePathsStr += sbuStr;
                }
            }
            String[] filePaths = filePathsStr.split(",");
            if (filePaths[0].contains("http://7sbrbl.com1.z0.glb.clouddn.com")) {
                in = QiniuFileUtils.downFileByUrl(filePaths[0]);
            } else {
                in = QiniuFileUtils.downFileByUrl("http://7sbrbl.com1.z0.glb.clouddn.com/" + filePaths[0]);
            }

            if (filePaths.length > 1) {
                for (int k = 1; k < filePaths.length; k++) {
                    String tempPath = "";
                    if (filePaths[k].contains("http://7sbrbl.com1.z0.glb.clouddn.com")) {
                        tempPath = filePaths[k];
                    } else {
                        tempPath = "http://7sbrbl.com1.z0.glb.clouddn.com/" + filePaths[k];
                    }
                    //tempPath=filePaths[k];
                    InputStream in2 = QiniuFileUtils.downFileByUrl(tempPath);
                    in = new SequenceInputStream(in, in2);
                }
            }

            BufferedInputStream bins = new BufferedInputStream(in);// 放到缓冲流里面
            OutputStream outs = response.getOutputStream();// 获取文件输出IO流
            BufferedOutputStream bouts = new BufferedOutputStream(outs);
            response.setContentType("application/x-download");// 设置response内容的类型
            response.setHeader("Content-disposition", "attachment; filename=" + new String(prefix.getBytes("gb2312"), "iso8859-1"));// 设置头部信息

            int bytesRead = 0;
            byte[] buffer = new byte[2014];
            // 开始向网络传输文件流
            while ((bytesRead = bins.read(buffer, 0, buffer.length)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }
            bouts.flush();// 这里一定要调用flush()方法
            in.close();
            bins.close();
            outs.close();
            bouts.close();

            return "";
        }
        return "";
    }

    /**
     * 删除楼中楼回帖
     *
     * @param rpid  帖子或者回复id
     * @param lolId 楼中楼id
     * @return
     */
    public boolean deleteLol(ObjectId rpid, ObjectId lolId) {
        return fReplyDao.deleteLol(rpid, lolId) > 0;
    }

    //============================================word


}
