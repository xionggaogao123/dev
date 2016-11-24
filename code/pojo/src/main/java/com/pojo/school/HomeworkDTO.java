package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.*;
import org.bson.types.ObjectId;

import com.sys.utils.DateTimeUtils;

/**
 * 作业DTO
 * @author fourer
 *
 */
public class HomeworkDTO {

    private String id;
	private String title;
	private String content;
	
	private String time;
	private long lastSubmitTime;
	private int submitCount;
	private List<StudentSubmitHomeWorkDTO> list =new ArrayList<HomeworkDTO.StudentSubmitHomeWorkDTO>();
    private List<IdNameValuePairDTO> voiceFile=new ArrayList<IdNameValuePairDTO>();
    private List<IdNameValuePairDTO> docFile=new ArrayList<IdNameValuePairDTO>();

    private String userName;
    private String userAvatar;



	private String teacherId;

	private List<String> classIdList = new ArrayList<String>();
	private List<FieldValuePair> classLessonIndex = new ArrayList<FieldValuePair>();//所有班级的课时索引
	private int type;

	private int lessonIndex;//当前班级下的课时索引



	private String lessonId;
	private String exerciseId;//配置练习的ID
	private int videoNum;//视频数
	private int fileNum;//文档数
	private int exerciseNum;//习题数
	private int voiceNum;
	private int reminder;//提醒数
	private int submitStuNo;
	private int allStuNo;



	private int pg;

	public int getVoiceNum() {
		return voiceNum;
	}

	public void setVoiceNum(int voiceNum) {
		this.voiceNum = voiceNum;
	}

	public HomeworkDTO(){

    }
	
	public HomeworkDTO(HomeWorkEntry e)
	{
		this.title=e.getName();
		this.content=e.getContent();
        this.id = e.getID().toString();
		try{
			this.type = e.getType();
		} catch (NullPointerException ne) {

		}
		this.lessonId = e.getLessonId().toString();
		for(IdNameValuePair p:e.getVoiceFile())
		{
			this.voiceFile.add(new IdNameValuePairDTO(p));
		}
		
		for(IdNameValuePair p:e.getDocFile())
		{
			this.docFile.add(new IdNameValuePairDTO(p));
		}
		this.teacherId=e.getTeacherId().toString();
		this.time=DateTimeUtils.convert(e.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
		this.lastSubmitTime=e.getLastSubmitTime();
		this.submitCount=e.getSubmitCount();
		this.pg = e.getCorrect();
		this.videoNum = e.getVideoNum();
		List<StudentSubmitHomeWork> submitHomeWorkList = e.getSubmitList();
		for(StudentSubmitHomeWork sshw:submitHomeWorkList)
		{
			try
			{
			list.add(new StudentSubmitHomeWorkDTO(sshw));
			}catch(Exception ex){}
		}
		if (e.getClasses() != null) {
			for (IdValuePair p : e.getClasses()) {
				this.classIdList.add(p.getId().toString());
			}
		}
		if(e.getClassLessonIndex() != null){
			for(IdValuePair p : e.getClassLessonIndex()){
				this.classLessonIndex.add(new FieldValuePair(p.getId().toString(), p.getValue()));
			}
		}



	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
	public List<IdNameValuePairDTO> getVoiceFile() {
		return voiceFile;
	}


	public void setVoiceFile(List<IdNameValuePairDTO> voiceFile) {
		this.voiceFile = voiceFile;
	}


	public List<IdNameValuePairDTO> getDocFile() {
		return docFile;
	}


	public void setDocFile(List<IdNameValuePairDTO> docFile) {
		this.docFile = docFile;
	}


	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public long getLastSubmitTime() {
		return lastSubmitTime;
	}
	public void setLastSubmitTime(long lastSubmitTime) {
		this.lastSubmitTime = lastSubmitTime;
	}
	public int getSubmitCount() {
		return submitCount;
	}
	public void setSubmitCount(int submitCount) {
		this.submitCount = submitCount;
	}
	
	public List<StudentSubmitHomeWorkDTO> getList() {
		return list;
	}

	public void setList(List<StudentSubmitHomeWorkDTO> list) {
		this.list = list;
	}



	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public List<String> getClassIdList() {
		return classIdList;
	}

	public void setClassIdList(List<String> classIdList) {
		this.classIdList = classIdList;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	public String getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}
	public int getVideoNum() {
		return videoNum;
	}

	public void setVideoNum(int videoNum) {
		this.videoNum = videoNum;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public int getExerciseNum() {
		return exerciseNum;
	}

	public void setExerciseNum(Integer exerciseNum) {
		if(exerciseNum == null){
			this.exerciseNum = 0;
		} else {
			this.exerciseNum = exerciseNum;
		}
	}
	public int getPg() {
		return pg;
	}

	public void setPg(int pg) {
		this.pg = pg;
	}

	public int getReminder() {
		return reminder;
	}

	public void setReminder(int reminder) {
		this.reminder = reminder;
	}

	public List<FieldValuePair> getClassLessonIndex() {
		return classLessonIndex;
	}

	public void setClassLessonIndex(List<FieldValuePair> classLessonIndex) {
		this.classLessonIndex = classLessonIndex;
	}

	public int getLessonIndex() {
		return lessonIndex;
	}

	public void setLessonIndex(int lessonIndex) {
		this.lessonIndex = lessonIndex;
	}

	public int getSubmitStuNo() {
		return submitStuNo;
	}

	public void setSubmitStuNo(int submitStuNo) {
		this.submitStuNo = submitStuNo;
	}

	public int getAllStuNo() {
		return allStuNo;
	}

	public void setAllStuNo(int allStuNo) {
		this.allStuNo = allStuNo;
	}

	/**
	 * 学生提交作业
	 * @author fourer
	 *
	 */
	public static class StudentSubmitHomeWorkDTO
	{
		private String userId;
		private String userName;
		private String avatar;
		private String content;
        private int isview;



		private int correct;



        private ObjectId classId;

        private List<IdNameValuePairDTO> voiceFile=new ArrayList<IdNameValuePairDTO>();
        private List<IdNameValuePairDTO> docFile=new ArrayList<IdNameValuePairDTO>();
		private List<StudentSubmitHomeWorkDTO> hf = new ArrayList<StudentSubmitHomeWorkDTO>();

		private long time;
		
		public StudentSubmitHomeWorkDTO(StudentSubmitHomeWork e)
		{
            this.classId = e.getClassId();
			this.userId=e.getStudentId().toString();
			this.content=e.getContent();
			this.correct = e.getCorrrect();
			for(StudentSubmitHomeWork studentSubmitHomeWork : e.getHF()){
				this.hf.add(new StudentSubmitHomeWorkDTO(studentSubmitHomeWork));
			}
            for(IdNameValuePair p:e.getVoiceFile())
            {
                this.voiceFile.add(new IdNameValuePairDTO(p));
            }

            for(IdNameValuePair p:e.getDocFile())
            {
                this.docFile.add(new IdNameValuePairDTO(p));
            }
			this.time=e.getTime();
		}

        public ObjectId getClassId() {
            return classId;
        }

        public void setClassId(ObjectId classId) {
            this.classId = classId;
        }
		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

        public List<IdNameValuePairDTO> getVoiceFile() {
            return voiceFile;
        }


        public void setVoiceFile(List<IdNameValuePairDTO> voiceFile) {
            this.voiceFile = voiceFile;
        }


        public List<IdNameValuePairDTO> getDocFile() {
            return docFile;
        }


        public void setDocFile(List<IdNameValuePairDTO> docFile) {
            this.docFile = docFile;
        }

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

        public int getIsview() {
            return isview;
        }

        public void setIsview(int isview) {
            this.isview = isview;
        }
		public int getCorrect() {
			return correct;
		}

		public void setCorrect(int correct) {
			this.correct = correct;
		}
		public List<StudentSubmitHomeWorkDTO> getHf() {
			return hf;
		}

		public void setHf(List<StudentSubmitHomeWorkDTO> hf) {
			this.hf = hf;
		}
    }
	
}
