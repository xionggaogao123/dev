package com.pojo.lesson;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.SimpleDTO;
import com.pojo.video.VideoDTO;
import org.bson.types.ObjectId;

/**
 * 课程具体信息
 * @author fourer
 *
 */
public class LessonDetailDTO extends LessonDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1062238887573654496L;
	
	public LessonDetailDTO(LessonEntry e) {
		super(e);
	}

	//视频
	private List<VideoDTO> videoList =new ArrayList<VideoDTO>();
	//课件
	private List<SimpleDTO> coursewareList =new ArrayList<SimpleDTO>();
	//练习
	private SimpleDTO exercise;

	
	public List<VideoDTO> getVideoList() {
		return videoList;
	}
	public void setVideoList(List<VideoDTO> videoList) {
		this.videoList = videoList;
	}
	public List<SimpleDTO> getCoursewareList() {
		return coursewareList;
	}
	public void setCoursewareList(List<SimpleDTO> coursewareList) {
		this.coursewareList = coursewareList;
	}
	
	
	
	
	public SimpleDTO getExercise() {
		return exercise;
	}
	public void setExercise(SimpleDTO exercise) {
		this.exercise = exercise;
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
	
	public void addDTOToCoursewareList(SimpleDTO dto)
	{
		this.coursewareList.add(dto);
	}

	
}
