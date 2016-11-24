package com.pojo.school;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.utils.MongoUtils;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生提交作业
 * @author fourer
 *
 * <pre>
 * {
 *  si:学生ID
 *  ci:班级ID
 *  ti:时间
 *  con:内容
 *  cor:是否已批阅 0：未批阅 1：已批阅
 *  vf:语音作业地址
 *  [
 *   {
 *    id:
 *    nm:
 *    v
 *   }
 *  ]
 *  df:文档作业地址
 *  [
 *   {
 *    id:
 *    nm:
 *    v:
 *   }
 *  ]
 *  ty:提交类型 ；参见HomeWorkSubmitType
 *  hf:老师回复
 *  List<StudentSubmitHomeWork>
 * }
 * </pre>
 *
 */
public class StudentSubmitHomeWork extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1836154058211682307L;
	
	
	public StudentSubmitHomeWork(BasicDBObject baseEntry) {
		super(baseEntry);
	}


    public StudentSubmitHomeWork(ObjectId studentId,ObjectId classId,
                                 long time,String content)
    {
        BasicDBObject dbo =new BasicDBObject()
                .append("si", studentId)
                .append("ci",classId)
                .append("ti", time)
                .append("con", content)
                .append("vf", null)
                .append("df", null)
                .append("cor", 0)
                .append("ty", HomeWorkSubmitType.ONLINE.getType())
                .append("hf",null);

        setBaseEntry(dbo);
    }
	public StudentSubmitHomeWork(ObjectId studentId,ObjectId classId,
			long time, String content, IdNameValuePair voiceFile, IdNameValuePair docFile,
			HomeWorkSubmitType type) {
		super();

        List<IdNameValuePair> voiceList =new ArrayList<IdNameValuePair>();
        if(voiceFile!=null) {
            if (null == voiceFile.getId()) {
                voiceFile.setId(new ObjectId());
            }
            voiceList.add(voiceFile);

        }
        List<IdNameValuePair> docList =new ArrayList<IdNameValuePair>();

        if(docFile!=null) {
            if (null == docFile.getId()) {
                docFile.setId(new ObjectId());
            }
            docList.add(docFile);

        }
        List<StudentSubmitHomeWork> hf = new ArrayList<StudentSubmitHomeWork>();

		BasicDBObject dbo =new BasicDBObject()
            .append("si", studentId).append("ci", classId).append("ti", time)
            .append("con", content)
            .append("cor", 0)
            .append("vf", MongoUtils.convert(MongoUtils.fetchDBObjectList(voiceList)))
            .append("df", MongoUtils.convert(MongoUtils.fetchDBObjectList(docList)))
            .append("ty", type.getType())
            .append("hf", MongoUtils.convert(MongoUtils.fetchDBObjectList(hf)));
		
		setBaseEntry(dbo);
	}



	public ObjectId getStudentId() {
		return getSimpleObjecIDValue("si");
	}
	public void setStudentId(ObjectId studentId) {
		setSimpleValue("si", studentId);
	}
	

    public ObjectId getClassId(){return getSimpleObjecIDValue("ci");}
    public void setClassId(ObjectId classId){setSimpleValue("ci",classId);}

	public long getTime() {
		return getSimpleLongValue("ti");
	}
	public void setTime(long time) {
		setSimpleValue("ti", time);
	}
	public String getContent() {
		return getSimpleStringValue("con");
	}
	public void setContent(String content) {
		setSimpleValue("con", content);
	}
    public int getCorrrect()
    {
    	if(getBaseEntry().containsField("cor"))
    	{
    	  return getSimpleIntegerValue("cor");
    	}
    	return 0;
    }
    public void setCorrect(int correct) {
        setSimpleValue("cor", correct);
    }

    public List<IdNameValuePair> getVoiceFile() {
        List<IdNameValuePair> retList =new ArrayList<IdNameValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("vf");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdNameValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setVoiceFile(List<IdNameValuePair> vf) {
        List<DBObject> list= MongoUtils.fetchDBObjectList(vf);
        setSimpleValue("vf", MongoUtils.convert(list));
    }

    public List<IdNameValuePair> getDocFile() {
        List<IdNameValuePair> retList =new ArrayList<IdNameValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("df");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdNameValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setDocFile(List<IdNameValuePair> df) {
        List<DBObject> list=MongoUtils.fetchDBObjectList(df);
        setSimpleValue("df", MongoUtils.convert(list));
    }


    public List<StudentSubmitHomeWork> getHF(){
        List<StudentSubmitHomeWork> retList = new ArrayList<StudentSubmitHomeWork>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("hf");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new StudentSubmitHomeWork((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setHF(List<StudentSubmitHomeWork> hf){
        List<DBObject> list=MongoUtils.fetchDBObjectList(hf);
        setSimpleValue("hf", MongoUtils.convert(list));
    }

	public HomeWorkSubmitType getType() {
		int type =getSimpleIntegerValue("ty");
		return HomeWorkSubmitType.getHomeWorkSubmitType(type);
	}
	public void setType(HomeWorkSubmitType type) {
		setSimpleValue("ty", type.getType());
	}
	
}
