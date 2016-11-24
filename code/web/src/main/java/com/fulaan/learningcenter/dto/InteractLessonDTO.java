package com.fulaan.learningcenter.dto;

import com.pojo.video.VideoDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/11/26.
 */
public class InteractLessonDTO {

    private String ilid;

    private String classId;

    private String imgurl;

    private String name;

    private String date;

    private int lock;

    //视频
    private List<VideoDTO> videoList =new ArrayList<VideoDTO>();

    public String getIlid() {
        return ilid;
    }

    public void setIlid(String ilid) {
        this.ilid = ilid;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VideoDTO> getVideoList() {
        return videoList;
    }
    public void setVideoList(List<VideoDTO> videoList) {
        this.videoList = videoList;
    }

    public void addDTOToVidesList(VideoDTO dto)
    {
        //排序
        ObjectId dtoId = new ObjectId(dto.getId().toString());
        int index = 0;
        for(VideoDTO videoDTO:videoList){
            ObjectId currentId = new ObjectId(videoDTO.getId().toString());
            //如果遍历到一个大得，就插在前面
            if(currentId.getTime()>dtoId.getTime()){
                videoList.add(index,dto);
                return;
            }
            index++;
        }
        //没有大得，就加在最后
        this.videoList.add(dto);
    }
}
