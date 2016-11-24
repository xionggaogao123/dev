package com.fulaan.homeschool.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fulaan.utils.CharacterUtil;
import com.fulaan.utils.KeyWordFilterUtil;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.app.Platform;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.microblog.MicroBlogImage;
import com.sys.constants.Constant;
import com.sys.utils.CustomDateSerializer;
import com.sys.utils.HtmlUtils;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/3/12.
 */
public class HomeSchoolDTO {

    /**
     * id
     */
    private String id;

    /**
     * replyid
     */
    private String replyid;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String userimage;

    /**
     * 微博内容
     */
    private String blogcontent;

    /**
     * 学校id
     */
    private String schoolid;

    /**
     * 上传图片
     */
    private String[] filenameAry;

    /**
     * 上传视频
     */
    private String[] videoAry;

    /**
     * 学生 2  老师 1
     */
    private int blogtype;

    /**
     * 是否是主题帖
     */
    private int top;
    /**
     * 修改时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date publishtime;

    /**
     * 用户角色
     */
    private int role;

    /**
     * zan数量
     */
    private int zancount;

    /**
     * 回复数
     */
    private int mreply;

    /**
     * 是否赞过
     */
    private int iszan;

    /**
     * 平台
     */
    private String clienttype;

    /**
     * 回复类型
     */
    private int replytype;

    /**
     * 被回复的username
     */
    private String busername;

    /**
     * 被回复内容
     */
    private String replycontent;

    /**
     * 创建时间
     */
    private String createtime;

    /**
     * 角色描述
     */
    private String roleDescription;

    /**
     * 平台
     */
    private int paltform;
    

    private int isdelete;

    /**
     * 发布全校班级年级
     */
    private int sendtype;

    private String timedes;

    private String schoolname;

    private List<VideoFileDTO> videoFileDTOs;

    public HomeSchoolDTO() {

    }

    public HomeSchoolDTO(MicroBlogEntry microBlogEntry) {
        this.id = microBlogEntry.getID().toString();
        this.userid = microBlogEntry.getUserId().toString();
//        this.blogcontent = microBlogEntry.getContent();
        this.blogcontent = KeyWordFilterUtil.getReplaceStrTxtKeyWords(microBlogEntry.getContent(), "*", 2);
        int num = 0;
        if (microBlogEntry.getImageList()!=null && microBlogEntry.getImageList().size()!=0) {
            List<String> files = new ArrayList<String>();
            for (MicroBlogImage image : microBlogEntry.getImageList()) {
                if (!StringUtils.isEmpty(image.getPath())) {
                    files.add(image.getPath().toString());
//                    filelist[num] = image.getPath().toString();
//                    num++;
                }

            }
            String[] filelist = new String[files.size()];
            for (String file : files) {
                filelist[num] = file;
                num++;
            }
            this.filenameAry = filelist;
        }
        List<VideoFileDTO> videoFileDTOList = new ArrayList<VideoFileDTO>();
        if (microBlogEntry.getVideoIds()!=null && microBlogEntry.getVideoIds().size()!=0) {
            String[] vids = new String[microBlogEntry.getVideoIds().size()];
            VideoFileDTO videoFileDTO = null;
            for (IdNameValuePair idv:microBlogEntry.getVideoIds()) {
                videoFileDTO = new VideoFileDTO();
                videoFileDTO.setImageUrl(idv.getValue().toString());
                videoFileDTO.setVideoUrl(idv.getName());
                videoFileDTOList.add(videoFileDTO);
            }
            this.videoFileDTOs = videoFileDTOList;
        }
        this.blogtype = microBlogEntry.getBlogtype();
        this.top  = microBlogEntry.getTop();
        this.publishtime = new Date(microBlogEntry.getPublishTime());
        try
        {
         this.paltform=microBlogEntry.getPlatformType();
         this.clienttype=Platform.getPlatform(this.paltform).toString();
        }catch(Exception ex)
        {
        	
        }
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getBlogcontent() {
        return blogcontent;
    }

    public void setBlogcontent(String blogcontent) {
    	blogcontent=HtmlUtils.delScriptTag(blogcontent);
        this.blogcontent = blogcontent;
    }

    public String[] getFilenameAry() {
        return filenameAry;
    }

    public void setFilenameAry(String[] filenameAry) {
        this.filenameAry = filenameAry;
    }

    public String[] getVideoAry() {
        return videoAry;
    }

    public void setVideoAry(String[] videoAry) {
        this.videoAry = videoAry;
    }

    public int getBlogtype() {
        return blogtype;
    }

    public void setBlogtype(int blogtype) {
        this.blogtype = blogtype;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getZancount() {
        return zancount;
    }

    public void setZancount(int zancount) {
        this.zancount = zancount;
    }

    public int getMreply() {
        return mreply;
    }

    public void setMreply(int mreply) {
        this.mreply = mreply;
    }

    public int getIszan() {
        return iszan;
    }

    public void setIszan(int iszan) {
        this.iszan = iszan;
    }



    public int getReplytype() {
        return replytype;
    }

    public void setReplytype(int replytype) {
        this.replytype = replytype;
    }

    public String getBusername() {
        return busername;
    }

    public void setBusername(String busername) {
        this.busername = busername;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
    }
    
    

    public int getPaltform() {
		return paltform;
	}

	public void setPaltform(int paltform) {
		this.paltform = paltform;
	}
	
	

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

    public int getSendtype() {
        return sendtype;
    }

    public void setSendtype(int sendtype) {
        this.sendtype = sendtype;
    }

	public MicroBlogEntry buildMicroBlogEntry(String client,String schoolid,List<ObjectId> classAry) {
        Platform pl = null;
        if (client.toLowerCase().contains("ios")) {
            pl = Platform.IOS;
        } else if (client.toLowerCase().contains("android")){
            pl = Platform.Android;
        } else {
            pl = Platform.PC;
        }

        List<MicroBlogImage> _filenames = null;
        if(this.getFilenameAry()!=null ){
            _filenames = new ArrayList<MicroBlogImage>();
            for(int i=0;i<this.getFilenameAry().length;i++){
                if (!StringUtils.isEmpty(this.getFilenameAry()[i])) {
                    _filenames.add(new MicroBlogImage(this.getFilenameAry()[i]));
                }

            }
        }
        List<IdNameValuePair> videoids = null;
        if (this.getVideoAry()!=null) {
            videoids = new ArrayList<IdNameValuePair>();
            for (String vid : this.getVideoAry()) {
                if (!StringUtils.isEmpty(vid)) {
                    String[] vtmp = vid.split("@");
                    videoids.add( new IdNameValuePair(new ObjectId(vtmp[0]),vtmp[1],vtmp[2]));
                }
            }
        }
        ObjectId micid = null;
        if (this.id!=null) {
            micid = new ObjectId(this.id);
        }
        int type = 1;
        if (this.sendtype!=0) {
            type = this.sendtype;
        }
        String content = this.blogcontent;
        content = content.replaceAll("&lt;br&gt;", "<br>");
        MicroBlogEntry microBlogEntry = new MicroBlogEntry(
                new ObjectId(this.userid),
                Constant.ONE,
                content,
                pl,
                this.top,
                this.blogtype,
                micid,
                Constant.ZERO,
                StringUtils.isEmpty(schoolid)?null:new ObjectId(schoolid),
                _filenames,
                null,
                classAry,
                videoids,
                type,
                StringUtils.isEmpty(schoolid)?1:0
        );
        return microBlogEntry;

    }

    public MicroBlogEntry buildReplyMicroBlogEntry(ObjectId userid,String schoolid) {
        int type = 1;
        if (this.sendtype!=0) {
            type = this.sendtype;
        }
        MicroBlogEntry microBlogEntry = new MicroBlogEntry(
                userid,
                this.replytype,
                this.blogcontent,
                Platform.PC,
                this.top,
                this.blogtype,
                new ObjectId(this.id),
                Constant.ZERO,
                StringUtils.isEmpty(schoolid)?null:new ObjectId(schoolid),
                null,
                new IdValuePair(new ObjectId(this.userid),this.replyid),
                new ArrayList<ObjectId>(),
                null,
                type,
                StringUtils.isEmpty(schoolid)?1:0
        );
        return microBlogEntry;
    }

    public List<VideoFileDTO> getVideoFileDTOs() {
        return videoFileDTOs;
    }

    public void setVideoFileDTOs(List<VideoFileDTO> videoFileDTOs) {
        this.videoFileDTOs = videoFileDTOs;
    }

    public String getTimedes() {
        return timedes;
    }

    public void setTimedes(String timedes) {
        this.timedes = timedes;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
}
