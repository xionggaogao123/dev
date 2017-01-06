package com.fulaan.forum.service;

import com.db.forum.FInformationDao;
import com.db.forum.FPostDao;
import com.db.forum.FReplyDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.ebusiness.SortType;
import com.pojo.forum.FPostDTO;
import com.pojo.forum.FPostEntry;
import com.pojo.forum.ReportedDTO;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/5/31.
 */
@Service
public class FPostService {
    private FPostDao fPostDao = new FPostDao();
    private UserDao userDao = new UserDao();
    private FReplyDao fReplyDao = new FReplyDao();
    private FInformationDao fInformationDao = new FInformationDao();

    private static final String classifyOne = "官方公告";
    private static final String classifyTwo = "精彩活动";
    private static final String classifyThree = "问题求助";
    private static final String classifyFour = "功能建议";


    /**
     * 获取举报内容
     *
     * @param reported
     * @param orderType
     * @param page
     * @param pageSize
     * @return
     */
    public List<FPostDTO> getFPostByReported(int reported, int orderType, int page, int pageSize) {
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        List<FPostEntry> fPostEntries = fPostDao.getFPostEntries("", "", reported, Constant.FIELDS, "", sort, -1, -1, null, null, -1, 0,
                (page - 1) * pageSize, pageSize, 0);
        List<FPostDTO> fPostDTOList = new ArrayList<FPostDTO>();
        if (null != fPostEntries) {
            for (FPostEntry fPostEntry : fPostEntries) {
                FPostDTO fPostDTO = new FPostDTO(fPostEntry);
                //举报时间
                fPostDTO.setTimeText(DateTimeUtils.convert(fPostEntry.getReportedList().get(0).getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
                ObjectId rId = fPostEntry.getReportedList().get(0).getUserId();
                UserEntry userEntry = userDao.getUserEntry(rId, Constant.FIELDS);

                //举报人
                if (StringUtils.isNotBlank(userEntry.getNickName())) {
                    fPostDTO.setReportedUserName(userEntry.getNickName());
                } else {
                    fPostDTO.setReportedUserName(userEntry.getUserName());
                }

                List<ReportedDTO> reportedList = fPostDTO.getReportedDTOList();
                List<ReportedDTO> reportedDTOs = new ArrayList<ReportedDTO>();
                for (ReportedDTO reportedDTO : reportedList) {
                    ReportedDTO item = new ReportedDTO();
                    UserEntry u = userDao.getUserEntry(new ObjectId(reportedDTO.getUserId()), Constant.FIELDS);
                    if (StringUtils.isNotBlank(u.getNickName())) {
                        item.setNickName(u.getNickName());
                    } else {
                        item.setNickName(u.getUserName());
                    }
                    item.setReason(reportedDTO.getReason());
                    reportedDTOs.add(item);
                }
                fPostDTO.setReportedDTOList(reportedDTOs);
                if (reported == 1) {
                    fPostDTO.setReportedTip("不奖惩");
                }
                fPostDTOList.add(fPostDTO);
            }
        }
        return fPostDTOList;
    }

    /**
     * 查询举报数量
     *
     * @param reported
     * @return
     */
    public int getFPostByReported(int reported) {
        return fPostDao.getFPostEntriesCount("", "", reported, "", -1, -1, null, null, -1, 0, 0);

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
     * 帖子列表
     *
     * @param orderType   排序方式（发帖时间/最新发表）
     * @param postSection
     * @param person
     * @param page
     * @param cream       精华
     * @param classify    分类
     * @param gtTime      发帖时间范围
     * @param pageSize
     * @return
     */
    public List<FPostDTO> getFPostsList(int app, int remove, String startTime, String endTime, String regular, int orderType, int zan, int top, String person, String postSection,
                                        int page, int cream, int classify, long gtTime, int pageSize) {
        BasicDBObject sort = new BasicDBObject();
        if (app == 1) {
            sort.append("tp", top);
        }
        if (zan == 2) {
            sort.append("prc", -1);
        }
        if (orderType == 2) {
            sort.append("_id", -1);
        } else if (orderType == 9) {
            sort.append("upt", -1);
        }
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        List<FPostEntry> fPostEntries = fPostDao.getFPostEntries(startTime, endTime, -1, Constant.FIELDS, regular, sort, cream, -1, postSectionId, personId, classify, gtTime,
                (page - 1) * pageSize, pageSize, remove);
        List<FPostDTO> fPostDTOList = new ArrayList<FPostDTO>();
        if (null != fPostEntries) {
            for (FPostEntry fPostEntry : fPostEntries) {
                FPostDTO fPostDTO = new FPostDTO(fPostEntry);
                String comment = fPostDTO.getComment();
                String appImageList = fPostDTO.getAppImageList();
                String appVideoList = fPostDTO.getAppVideoList();
                //为app端处理图像以及视频
                //先判断是web端上传的还是app端上传的
                if (StringUtils.isNotBlank(comment)) {
                    //web端上传的
                    fPostDTO.setImageList(dealWithImage(comment));
                    //新版本兼容
                    if (StringUtils.isNotBlank(fPostDTO.getVersion())) {
                        fPostDTO.setVideoList(dealVideo(fPostDTO.getVersionVideo()));
                    } else {
                        fPostDTO.setVideoList(dealWithVideo(comment));
                    }
                } else {
                    //app端上传的
                    if (StringUtils.isNotBlank(appImageList)) {
                        fPostDTO.setImageList(getAppImageList(appImageList));
                    } else {
                        List<String> item = new ArrayList<String>();
                        fPostDTO.setImageList(item);
                    }
                    if (StringUtils.isNotBlank(appVideoList)) {
                        fPostDTO.setVideoList(getAppVideoList(appVideoList));
                    } else {
                        List<Map<String, String>> map = new ArrayList<Map<String, String>>();
                        fPostDTO.setVideoList(map);
                    }
                }

                if (StringUtils.isNotBlank(regular)) {
                    StringBuffer str = new StringBuffer();
                    String postTitle = fPostDTO.getPostTitle();
                    int index = postTitle.indexOf(regular);
                    str.append(postTitle.substring(0, index));
                    str.append("<span style=\"color:red\">" + regular + "</span>");
                    str.append(postTitle.substring(index + regular.length()));
                    fPostDTO.setPostTitle(str.toString());
                }
                if (fPostDTO.getPersonId() != null && !"".equals(fPostDTO.getPersonId())) {
                    UserEntry dto = userDao.getUserEntry(new ObjectId(fPostDTO.getPersonId()), Constant.FIELDS);
                    if (dto != null) {
                        fPostDTO.setImage(AvatarUtils.getAvatar(dto.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                        if (dto.getNickName() != null && !"".equals(dto.getNickName())) {
                            fPostDTO.setPersonName(dto.getNickName());
                        }
                    } else {
                        fPostDTO.setImage(null);
                    }


                } else {
                    fPostDTO.setImage(null);
                }
                if (fPostEntry.getCream() == 1) {
                    fPostDTO.setCreamText("精1");
                } else {
                    fPostDTO.setCreamText("");
                }
                if (fPostEntry.getIsTop() == 1) {
                    fPostDTO.setTopText("置顶");
                } else {
                    fPostDTO.setTopText("");
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (dateLarge(fPostEntry.getTime())) {
                    Date date = new Date(fPostEntry.getTime());
                    String time = sdf.format(date);
                    fPostDTO.setTimeText(time);
                } else {
                    String str = DateTimeUtils.convert(fPostEntry.getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
                    fPostDTO.setTimeText("今天：" + str.split(" ")[1]);
                }

                int classifyV = fPostEntry.getClassify();
                if (classifyV == 1) {
                    fPostDTO.setClassifyContent(classifyOne);
                } else if (classifyV == 2) {
                    fPostDTO.setClassifyContent(classifyTwo);
                } else if (classifyV == 3) {
                    fPostDTO.setClassifyContent(classifyThree);
                } else if (classifyV == 4) {
                    fPostDTO.setClassifyContent(classifyFour);
                } else {
                    fPostDTO.setClassifyContent("");
                }
                String replyUserId = fPostDTO.getReplyUserId();
                if (StringUtils.isNotBlank(replyUserId)) {
                    UserEntry userEntry = userDao.getUserEntry(new ObjectId(replyUserId), Constant.FIELDS);
                    if (userEntry != null) {
                        if (StringUtils.isNotBlank(userEntry.getNickName())) {
                            fPostDTO.setReplyUserName(userEntry.getNickName());
                        } else {
                            fPostDTO.setReplyUserName(userEntry.getUserName());
                        }
                    }
                }
                long replyTime = fPostDTO.getUpdateTime();
                long nowTime = System.currentTimeMillis();
                long day = (nowTime - replyTime) / (1000 * 60 * 60 * 24);
                fPostDTO.setReplyTime(day);
                int commentCount = fReplyDao.getFReplyEntriesCount(new ObjectId(fPostDTO.getFpostId()), null, null, -1, 1);
                fPostDTO.setCommentCount(commentCount);
                fPostDTO.setMerge(fPostDTO.getFpostId() + "," + fPostDTO.getPostSectionName());
                fPostDTOList.add(fPostDTO);
            }
        }
        return fPostDTOList;

    }


    public boolean dateLarge(long time) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long gtTime = cal.getTimeInMillis();
        if (time < gtTime) {
            return true;
        } else {
            return false;
        }

    }

    //处理app端上传的图像
    public List<String> getAppImageList(String imageStr) {
        List<String> retList = new ArrayList<String>();
        if (StringUtils.isNotBlank(imageStr)) {
            if (imageStr.contains(",")) {
                String[] imageList = imageStr.split(",");
                for (String image : imageList) {
                    if (!image.contains("baidu")) {
                        retList.add(image);
                    }
                }
            } else {
                if (!imageStr.contains("baidu")) {
                    retList.add(imageStr);
                }
            }
        }
        return retList;
    }

    //处理app端上传的视频
    public List<Map<String, String>> getAppVideoList(String videoStr) {
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
        if (StringUtils.isNotBlank(videoStr)) {
            if (videoStr.contains(",")) {
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
     * @param regular
     * @param orderType
     * @param postSection
     * @param person
     * @param inSet
     * @param page
     * @param cream
     * @param classify
     * @param gtTime
     * @param pageSize
     * @return
     */
    public List<FPostDTO> getFPostsListByActivity(int remove, String regular, int orderType, String postSection, String person, int inSet,
                                                  int page, int cream, int classify, long gtTime, int pageSize) {
        BasicDBObject sort = new BasicDBObject();
        sort.append("up", -1);
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        List<FPostEntry> fPostEntries = fPostDao.getFPostEntries("", "", -1, Constant.FIELDS, regular, sort, cream, inSet, postSectionId, personId, classify, gtTime,
                (page - 1) * pageSize, pageSize, remove);
        List<FPostDTO> fPostDTOList = new ArrayList<FPostDTO>();
        if (null != fPostEntries) {
            for (FPostEntry fPostEntry : fPostEntries) {
                fPostEntry.setComment(convertTime(fPostEntry.getTime()));
                FPostDTO fPostDTO = new FPostDTO(fPostEntry);
                String demo = fPostDTO.getActivityMemo();
                if (demo.contains("】")) {
                    fPostDTO.setMainTitle(demo.substring(demo.indexOf("】") + 1, demo.length()));
                    fPostDTO.setTitle(demo.substring(demo.indexOf("【"), demo.indexOf("】") + 1));
                }
                if (fPostDTO.getPlainText().contains("活动时间")) {
                    fPostDTO.setPartContent(fPostDTO.getPlainText().substring(0, fPostDTO.getPlainText().indexOf("活动时间")));
                }
                fPostDTOList.add(fPostDTO);
            }
        }
        return fPostDTOList;
    }

    /**
     * 获取已下线的活动
     *
     * @return List
     */
    public List<FPostDTO> getFPostsListByOffActivity() {
        List<FPostEntry> fPostEntries = fPostDao.getFPostOffAcEntries();
        List<FPostDTO> fPostDTOList = new ArrayList<FPostDTO>();
        if (null != fPostEntries) {
            for (FPostEntry fPostEntry : fPostEntries) {
                fPostEntry.setComment(convertTime(fPostEntry.getTime()));
                fPostDTOList.add(new FPostDTO(fPostEntry));
            }
        }
        return fPostDTOList;
    }

    public List<FPostDTO> getFPostsListByActivityAll() {
        List<FPostEntry> fPostEntries = fPostDao.getFPostAcEntriesAll();
        List<FPostDTO> fPostDTOList = new ArrayList<FPostDTO>();
        if (null != fPostEntries) {
            for (FPostEntry fPostEntry : fPostEntries) {
                fPostEntry.setComment(convertTime(fPostEntry.getTime()));
                fPostDTOList.add(new FPostDTO(fPostEntry));
            }
        }
        return fPostDTOList;
    }

    /**
     * 获取大赛帖子总数
     *
     * @param
     * @return
     */

    public int competitionPostCount() {
        return fPostDao.getCompetionCount();
    }

    private String convertTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.DATE_YYYY_MM_DD);
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 查询热帖
     *
     * @param orderType
     * @param postSection
     * @param person
     * @param page
     * @param cream
     * @param classify
     * @param gtTime
     * @param pageSize
     * @return
     */
    public List<FPostDTO> getFPostsListByCondition(String regular, int orderType, String postSection, String person, int inSet,
                                                   int page, int cream, int classify, long gtTime, int pageSize) {
        BasicDBObject sort = new BasicDBObject();
        sort.append("cc", -1);
        sort.append("_id", -1);
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        List<FPostEntry> fPostEntries = fPostDao.getFPostEntries("", "", -1, Constant.FIELDS, regular, sort, cream, inSet, postSectionId, personId, classify, gtTime,
                (page - 1) * pageSize, pageSize, 0);
        List<FPostDTO> fPostDTOList = new ArrayList<FPostDTO>();
        if (null != fPostEntries) {
            for (FPostEntry fPostEntry : fPostEntries) {
                FPostDTO fPostDTO = new FPostDTO(fPostEntry);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(fPostEntry.getTime());
                String time = sdf.format(date);
                //先判断是web端上传的还是app端上传的
                fPostDTO.setComment(time);
                if (StringUtils.isNotBlank(fPostEntry.getComment())) {
                    //web端上传的
                    fPostDTO.setImageList(dealWithImage(fPostEntry.getComment()));
                    fPostDTO.setVideoList(dealWithVideo(fPostEntry.getComment()));
                } else {
                    //app端上传的
                    if (StringUtils.isNotBlank(fPostEntry.getAppImageList())) {
                        fPostDTO.setImageList(getAppImageList(fPostEntry.getAppImageList()));
                    } else {
                        List<String> item = new ArrayList<String>();
                        fPostDTO.setImageList(item);
                    }
                    if (StringUtils.isNotBlank(fPostEntry.getAppVideoList())) {
                        fPostDTO.setVideoList(getAppVideoList(fPostEntry.getAppVideoList()));
                    } else {
                        List<Map<String, String>> map = new ArrayList<Map<String, String>>();
                        fPostDTO.setVideoList(map);
                    }
                }
                fPostDTOList.add(fPostDTO);
            }
        }
        return fPostDTOList;

    }

    /**
     * 查询帖子数量
     *
     * @param startTime
     * @param endTime
     * @param regular
     * @param postSection
     * @param person
     * @param cream
     * @param classify
     * @param gtTime
     * @return
     */
    public int getFPostsCount(String startTime, String endTime, String regular, String postSection, String person,
                              int cream, int classify, long gtTime, int remove) {
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        return fPostDao.getFPostEntriesCount(startTime, endTime, -1, regular, cream, -1, postSectionId, personId, classify, gtTime, remove);

    }

    /**
     * 帖子详情
     *
     * @param Id
     * @return
     */
    public FPostDTO detail(ObjectId Id) {
        FPostEntry fPostEntry = fPostDao.getFPostEntry(Id);
        if (fPostEntry == null) {
            return null;
        }
        return new FPostDTO(fPostEntry);
    }

    /**
     * 新增和覆盖
     *
     * @param fPostDTO
     */
    public ObjectId addFPostEntry(FPostDTO fPostDTO) {
        return fPostDao.addFPost(fPostDTO.exportEntry());
    }

    /**
     * 根据个人获取帖子数
     *
     * @param postSection
     * @return
     */
    public int getPostCountByCondition(String postSection, String person) {
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        return fPostDao.getPostCountByCondition(postSectionId, personId);
    }

    /**
     * 根据个人获取帖子数
     *
     * @return
     */
    public int getPostCountByCondition(String person) {
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        return fPostDao.getPostCountByCondition(personId);
    }

    /**
     * 获取某一帖子的点赞数
     */
    public int getPostCountByPostId(String post) {
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        FPostDTO fPostDTO = new FPostDTO(fPostDao.getFPostEntry(postId));
        List<String> rl = fPostDTO.getUserReplyList();
        int count = rl.size();
        return count;
    }

    /**
     * 逻辑删除
     *
     * @param postId
     */
    public void deleteFPost(ObjectId postId) {
        fPostDao.deletePost(postId);
    }

    /**
     * 增加每个帖子的浏览量
     *
     * @param postId
     */
    public void updateScanCount(ObjectId postId) {
        fPostDao.updateScanCount(postId);
    }

    /**
     * 增加每个帖子的评论量
     *
     * @param postId
     */
    public void updateCommentCount(ObjectId postId) {
        fPostDao.updateCommentCount(postId);
    }

    /**
     * 减少每个帖子的评论量
     *
     * @param postId
     */
    public void updateDecCommentCount(ObjectId postId) {
        fPostDao.updateDecCommentCount(postId);
    }

    /**
     * 更新精华
     *
     * @param postId
     */
    public void updateCream(ObjectId postId, int cream) {
        fPostDao.updateCream(postId, cream);
    }

    /**
     * 更新置顶
     *
     * @param postId
     */
    public void updateTop(ObjectId postId, int top) {
        fPostDao.updateTop(postId, top);
    }

    /**
     * 更新点赞
     *
     * @param uid
     * @param flag
     * @param postId
     */
    public void updateBtnZan(ObjectId uid, ObjectId psid, boolean flag, ObjectId postId) {
        if (flag) {
            fInformationDao.saveZanInformation(uid, psid, postId);
        } else {
            fInformationDao.deleteZanInformation(uid, psid, postId);
        }
        fPostDao.updateBtnZan(uid, flag, postId);
    }

    /**
     * 更新点赞
     *
     * @param userReplyId
     * @param flag
     * @param postId
     */
    public void updateOppose(ObjectId userReplyId, boolean flag, ObjectId postId) {
        fPostDao.updateOppose(userReplyId, flag, postId);
    }


    /**
     * 更新最后回复人以及最后回复时间
     *
     * @param postId
     * @param replyUserId
     */
    public void updateReplyInf(ObjectId postId, ObjectId replyUserId) {
        fPostDao.updateReplyInf(postId, replyUserId);
    }

    /**
     * 更新举报
     */
    public void updateReported(String postId, FPostEntry.Reported e, int reported, String reportedComment, int reportedExperience) {
        ObjectId id = postId.equals("") ? null : new ObjectId(postId);
        fPostDao.updateReportedList(e, id, reported, reportedComment, reportedExperience);
    }

    /**
     * 后台批量逻辑删除用户数据
     */
    public void FPostLogic(List<ObjectId> ids) {
        fPostDao.removeFPostLogic(ids);
    }

    public void recoverFPostLogic(List<ObjectId> ids, int remove) {
        fPostDao.recoverFPostLogic(ids, remove);
    }

    public void updateRewardCountValue(ObjectId userId) {
        fPostDao.updateRewardCountValue(userId);
    }
}
