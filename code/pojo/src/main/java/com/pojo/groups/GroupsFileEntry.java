package com.pojo.groups;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 群组文件，
 * <pre>
 * {
 *  rd:房间id
 *  path:路径
 *  uui:上传者userid
 *  fm:上传文件名
 *  ut:上传时间
 *  num:下载数量
 *  fs:文件大小
 *  st:状态
 * }
 * </pre>
 * @author wang_xinxin
 */
public class GroupsFileEntry extends BaseDBObject {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3061353103088745323L;

    public GroupsFileEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public GroupsFileEntry(String roomid, String filepath, String uploadUserid, String filename, int count, int filesize,int state) {
        this(roomid,filepath,uploadUserid,filename,System.currentTimeMillis(),count,filesize,state);
    }

    public GroupsFileEntry(String roomid, String filepath, String uploadUserid, String filename, long uploadtime, int count, int filesize,int state) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("rd",roomid)
                .append("path", filepath)
                .append("uui",uploadUserid)
                .append("fm",filename)
                .append("ut",uploadtime)
                .append("num",count)
                .append("fs", filesize)
                .append("st",state);
        setBaseEntry(baseEntry);

    }

    public String getRoomid() {
        return getSimpleStringValue("rd");
    }
    public void setRoomid(String roomid) {
        setSimpleValue("rd", roomid);
    }
    public String getFilepath() {
        return getSimpleStringValue("path");
    }
    public void setFilepath(String filepath) {
        setSimpleValue("path",filepath);
    }
    public String getUploadUserid() {
        return getSimpleStringValue("uui");
    }
    public void setUploadUserid(String uploadUserid) {
        setSimpleValue("uui",uploadUserid);
    }
    public String getFilename() {
        return getSimpleStringValue("fm");
    }
    public void setFilename(String filename) {
        setSimpleValue("fm",filename);
    }
    public long getUploadtime() {
        return getSimpleLongValue("ut");
    }
    public void setUploadtime(long uploadtime) {
        setSimpleValue("ut",uploadtime);
    }
    public int getCount() {
        return getSimpleIntegerValue("num");
    }
    public void setCount(int count) {
        setSimpleValue("num",count);
    }
    public int getFilesize() {
        return getSimpleIntegerValue("fs");
    }
    public void setFilesize(int filesize) {
        setSimpleValue("fs",filesize);
    }
    public int getState() {
        return getSimpleIntegerValue("st");
    }
    public void setState(int state) {
        setSimpleValue("st", state);
    }
}
