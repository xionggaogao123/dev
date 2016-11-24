package com.fulaan.homeschool.service;

import com.db.microblog.MicroBlogDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.DBObject;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;

import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wang_xinxin on 2015/3/17.
 */

@Service
public class HomeSchoolService {

    private static final Logger logger= Logger.getLogger(HomeSchoolService.class);

    private MicroBlogDao microBlogDao = new MicroBlogDao();

    private UserDao userdao = new UserDao();

    SchoolDao schoolDao =new SchoolDao();

    /**
     * 发表微博
     * @param microBlogEntry
     */
    public ObjectId addMicroBlog(MicroBlogEntry microBlogEntry) {
        return microBlogDao.addMicroBlog(microBlogEntry);
    }

    /**
     * 删除自己的微博
     * @param id
     * @param userid
     * @throws IllegalParamException
     */
    public void deleteMicroBlogInfo(String id, ObjectId userid,String blogid) throws IllegalParamException{
        if(!ObjectId.isValid(id))
        {
            throw new IllegalParamException("the id ["+id+" ] is valid!!");
        }
        microBlogDao.deleteMicroBlog(new ObjectId(id), userid);
        if (!StringUtils.isEmpty(blogid)) {
            microBlogDao.deleteComment(new ObjectId(blogid), new ObjectId(id));
        }
    }
    
    
    /**
     * 根据ID集合查询
     * @param ids
     * @param fields
     * @return
     */
    public List<MicroBlogEntry> getMicroBlogEntryByIds(List<ObjectId> ids,DBObject fields) 
    {
    	return microBlogDao.getMicroBlogEntryByIds(ids, fields);
    }
    
    
    /**
     * 删除多个评论
     * @param ids
     */
    public void deleteMicroBlog(Collection<ObjectId> ids)
    {
    	microBlogDao.deleteMicroBlog(ids);
    }
    

    /**
     * 查询微博
     * @param userid
     * @param hottype
     * @param blogtype
     * @param page
     * @param pageSize
     * @return
     * @throws ResultTooManyException
     */
    public List<MicroBlogEntry> selFriendBlogInfo(ObjectId userid,int hottype, int blogtype, String schoolid, int page, int pageSize,int seachtype,List<ObjectId> classAry,String theme,int seachType) throws ResultTooManyException {
        List<MicroBlogEntry> bloglist = new ArrayList<MicroBlogEntry>();
        if (hottype==3) {
            bloglist = microBlogDao.getMicroBlogEntryList(userid, DeleteState.NORMAL, null,null,0,hottype, null,schoolid,page < 1 ? 0 : ((page - 1) * pageSize),pageSize,seachtype, classAry,theme,seachType);
        } else {
            bloglist = microBlogDao.getMicroBlogEntryList(null, DeleteState.NORMAL, null,null,blogtype,hottype, null,schoolid,page < 1 ? 0 : ((page - 1) * pageSize),pageSize,seachtype, classAry,theme,seachType);
        }
        return bloglist;
    }

    /**
     * 回复评论
     * @param microBlogEntry
     * @param replyid
     */
    public int replyComment(MicroBlogEntry microBlogEntry,String replyid) {
       ObjectId id =  microBlogDao.addMicroBlog(microBlogEntry);
        microBlogDao.addComment(new ObjectId(replyid),id);
        MicroBlogEntry entry = selOneBlogInfo(replyid);
        return entry.getCommentList().size();
    }

    /**
     * 查询单条微博
     * @param blogid
     * @return
     */
    public MicroBlogEntry selOneBlogInfo(String blogid) {
        return microBlogDao.getOneBlogInfo(new ObjectId(blogid),null);
    }

    /**
     * 赞
     * @param blogid
     * @param userid
     * @return
     */
    public Map<String,Object> isBlogZan(String blogid, ObjectId userid) {
        Map<String,Object> result = new HashMap<String, Object>();
        boolean iszan = microBlogDao.isHaveZan(new ObjectId(blogid),userid);
        if (iszan) {
            result.put("flag",false);
            return result;
        } else {
            microBlogDao.addZan(new ObjectId(blogid),userid);
            MicroBlogEntry microBlogEntry = microBlogDao.getOneBlogInfo(new ObjectId(blogid),null);
            result.put("flag",true);
            result.put("zanCount",microBlogEntry.getZanCount());
            return result;
        }
    }

    /**
     * 好友微博数
     * @param userid
     * @param hottype
     * @param blogtype
     * @return
     */
    public int selFriendBlogCount(ObjectId userid, int hottype, int blogtype,String schoolid,int seachtype,List<ObjectId> classAry,String theme,int seachType) {
        return microBlogDao.getFriendBlogCount(userid,hottype,blogtype,schoolid,seachtype,classAry,theme,seachType);
    }

    /**
     * 获取回复评论数量
     * @param blogid
     * @param type
     * @param id
     * @return
     */
    public int getFriendReplyCount(String blogid, int type, String id) {
        return microBlogDao.getFriendReplyCount(blogid,type,new ObjectId(id));
    }

    /**
     * 获取回复评论数量
     * @param blogid
     * @param type
     * @param id
     * @return
     */
    public int getFriendReplyCount(String blogid, int type, String id,String startTime,String endTime) {
        long btime = 0;
        long etime = 0;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(startTime)) {
            btime = DateTimeUtils.stringToDate(startTime,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(endTime)) {
            etime = DateTimeUtils.stringToDate(endTime,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        return microBlogDao.getFriendReplyCount(blogid,type,new ObjectId(id),btime,etime);
    }

    /**
     * 获取回复评论
     * @param blogid
     * @param type
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public List<MicroBlogEntry> getFriendReplyInfo(String blogid, int type, String id, int page, int pageSize) {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        if ( type==1 ) {
            retList = microBlogDao.getFriendReplyInfo(blogid,type,new ObjectId(id),page < 1 ? 0 : ((page - 1) * pageSize),pageSize);
            microBlogDao.updateViewBlog(new ObjectId(id));
        } else if ( type==2 ) {
            MicroBlogEntry microblog = microBlogDao.getOneBlogInfo(new ObjectId(blogid), null);
            if (microblog!=null) {
                List<ObjectId> objectlist =	microblog.getCommentList();
                Collections.reverse(objectlist);
                if (objectlist!=null && objectlist.size()!=0) {
                    if (page < 1) page = 1;
                    if ((page - 1) * pageSize > (objectlist.size() - 1)) page = 1; //截取起始点超过 list大小重置page
                    int begin = (page - 1) * pageSize;
                    int end = page * pageSize;
                    if (page * pageSize > objectlist.size()) end = objectlist.size(); //截取结束点超过size 截取起始点到最后
                    objectlist = objectlist.subList(begin, end);
                    for (ObjectId blog : objectlist) {
                        MicroBlogEntry entry =  microBlogDao.getOneBlogInfo(blog, null);
                        if (entry!=null) {
                            retList.add(microBlogDao.getOneBlogInfo(blog,null));
                        }
                    }
                }
            }
        }
        return retList;
    }

    /**
     * 获取回复评论
     * @param blogid
     * @param type
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public List<MicroBlogEntry> getFriendReplyInfo(String blogid, int type, String id,String startTime,String endTime, int page, int pageSize) {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        if ( type==1 ) {
            long btime = 0;
            long etime = 0;
            if (org.apache.commons.lang.StringUtils.isNotEmpty(startTime)) {
                btime = DateTimeUtils.stringToDate(startTime,
                        DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(endTime)) {
                etime = DateTimeUtils.stringToDate(endTime,
                        DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
            }
            retList = microBlogDao.getFriendReplyInfo(blogid,type,new ObjectId(id),btime,etime,page < 1 ? 0 : ((page - 1) * pageSize),pageSize);
            microBlogDao.updateViewBlog(new ObjectId(id));
        } else if ( type==2 ) {
            MicroBlogEntry microblog = microBlogDao.getOneBlogInfo(new ObjectId(blogid), null);
            if (microblog!=null) {
                List<ObjectId> objectlist =	microblog.getCommentList();
                Collections.reverse(objectlist);
                if (objectlist!=null && objectlist.size()!=0) {
                    if (page < 1) page = 1;
                    if ((page - 1) * pageSize > (objectlist.size() - 1)) page = 1; //截取起始点超过 list大小重置page
                    int begin = (page - 1) * pageSize;
                    int end = page * pageSize;
                    if (page * pageSize > objectlist.size()) end = objectlist.size(); //截取结束点超过size 截取起始点到最后
                    objectlist = objectlist.subList(begin, end);
                    for (ObjectId blog : objectlist) {
                        MicroBlogEntry entry =  microBlogDao.getOneBlogInfo(blog, null);
                        if (entry!=null) {
                            retList.add(microBlogDao.getOneBlogInfo(blog,null));
                        }
                    }
                }
            }
        }
        return retList;
    }

    public int getNoticeCount(String userid,String schoolid) {
        return microBlogDao.getNoticeCount(new ObjectId(userid),new ObjectId(schoolid));
    }

    /**
     * 微博
     * @param userid
     * @return
     */
    public Map getMicroblogCount(String userid,String schoolid) {
        Map map = new HashMap();
        UserEntry user = userdao.getUserEntry(new ObjectId(userid), null);
        int schoolcount = microBlogDao.getSchoolMicroblogCount(user.getSchoolHomeDate(),schoolid);
        int homecount = microBlogDao.getFamilyMicroblogCount(user.getFamilyHomeDate(),schoolid);
        map.put("schoolcount",schoolcount);
        map.put("homecount",homecount);
        return map;
    }

    /**
     * 话题微博数
     * @param theme
     * @return
     */
    public int getThemeCount(String theme,String schoolid) {
        return microBlogDao.getThemeCount(theme,schoolid);
    }

    /**
     * 获取老师12月份微校园
     * @return
     */
    public void getTeacherBlogList(HttpServletResponse response) {
        List<MicroBlogEntry> bloglist = microBlogDao.getTeacherBlogList();
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("老师微校园列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("老师姓名");
        cell = row.createCell(1);
        cell.setCellValue("内容");
        cell = row.createCell(2);
        cell.setCellValue("学校");
        cell = row.createCell(3);
        cell.setCellValue("发布时间");
        Set<ObjectId> userSet =new HashSet<ObjectId>();
        Set<ObjectId> schollSet =new HashSet<ObjectId>();
        for(MicroBlogEntry me:bloglist)
        {
            userSet.add(me.getUserId());
            schollSet.add(me.getSchoolID());
        }
        Map<ObjectId, UserEntry> userMap =userdao.getUserEntryMap2(userSet, Constant.FIELDS);
        Map<ObjectId, SchoolEntry> schollMap =schoolDao.getSchoolMap(schollSet, Constant.FIELDS);
        int page=0;
        for(int i=0; i<bloglist.size(); i++) {
            MicroBlogEntry blog = bloglist.get(i);
            page++;
            row = sheet.createRow(page);

            cell = row.createCell(0);
            cell.setCellValue(userMap.get(blog.getUserId()).getUserName());
            cell = row.createCell(1);
            cell.setCellValue(blog.getContent());
            cell = row.createCell(2);
            cell.setCellValue(schollMap.get(blog.getSchoolID()).getName());
            cell = row.createCell(3);
            cell.setCellValue((DateTimeUtils.convert(blog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS)));
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("微校园.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateZhuTi() {

        microBlogDao.updateZhuTi(getDateBefore(new Date(),7));

    }
    public static long getDateBefore(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return now.getTime().getTime();
    }
}
