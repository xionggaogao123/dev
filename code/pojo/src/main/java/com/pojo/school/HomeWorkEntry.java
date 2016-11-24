package com.pojo.school;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业
 * <pre>
 * collectionName:homeworks
 * </pre>
 * <pre>
 * { 
 *  ti:老师ID
 *  cs:
 *  [
 *   {
 *    id:班级ID
 *     v:班级名字
 *   }
 *  ]
 *  nm:名字
 *  con:内容
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
 *  lut:最后提交时间
 *  sc:提交数量
 *  sul：学生提交作业
 *  [
 *    {
 *     参见：StudentSubmitHomeWork
 *    }
 *  ]
 *
 * sbj:科目ID列表
 * []
 * tm:学期
 * lid：课程ID
 * ver：版本号
 * ty: 类型 0：课前 1：课后 2：其他
 * pg:是否需要批改 0：不需要 1：需要
 * vi:视频数
 * vo:音频数
 * fi:文档数
 * ex:练习数
 * ifc:是否来自云资源
 * cli:班级课时索引
 * [
 *   {
 *    id:班级ID
 *     v:课时索引
 *   }
 *  ]
 * }
 * </pre>
 * @author fourer
 */
public class HomeWorkEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6228145245006759935L;
	
	
	public HomeWorkEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public HomeWorkEntry(ObjectId teacherId, List<IdValuePair> classes,
			String name, String content, IdNameValuePair voiceFile, IdNameValuePair docFile,List<IdValuePair> classLessonIndex
			) {
		
		if(voiceFile!=null&&null==voiceFile.getId())
		{
			voiceFile.setId(new ObjectId());
		}
		if(docFile!=null&&(null==docFile.getId()))
		{
			docFile.setId(new ObjectId());
		}
		List<IdNameValuePair> voiceList =new ArrayList<IdNameValuePair>();
        if(voiceFile!=null){
            voiceList.add(voiceFile);

        }

		List<IdNameValuePair> docList =new ArrayList<IdNameValuePair>();
        if(docFile!=null){
            docList.add(docFile);

        }

        init(teacherId, classes, name, content,voiceList,docList,null,classLessonIndex);
	}
	
	public HomeWorkEntry(ObjectId teacherId, List<IdValuePair> classes,
			String name, String content, List<IdNameValuePair> voiceFile, List<IdNameValuePair> docFile,
			List<StudentSubmitHomeWork> submitList) {
		super();
		
		init(teacherId, classes, name, content, voiceFile, docFile, submitList, new ArrayList<IdValuePair>());
	}

    /**
     * 此方法仅仅应用于查询分页情况
     * @param name
     * @param content
     * @param list
     */
    public HomeWorkEntry(String name, String content, List<StudentSubmitHomeWork> list) {
        super();

        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", name)
                .append("con", content)
                .append("sul", MongoUtils.convert(MongoUtils.fetchDBObjectList(list)));
        setBaseEntry(baseEntry);
    }

	private void init(ObjectId teacherId, List<IdValuePair> classes,
			String name, String content, List<IdNameValuePair> voiceFile,
			List<IdNameValuePair> docFile, List<StudentSubmitHomeWork> submitList,List<IdValuePair> classLessonIndex) {
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ti", teacherId)
		.append("cs", MongoUtils.convert(MongoUtils.fetchDBObjectList(classes)))
		.append("nm", name)
		.append("con", content)
		.append("vf", MongoUtils.convert(MongoUtils.fetchDBObjectList(voiceFile)))
		.append("df", MongoUtils.convert(MongoUtils.fetchDBObjectList(docFile)))
		.append("lut", Constant.DEFAULT_VALUE_LONG)
		.append("sc", Constant.ZERO)
		.append("vi", Constant.ZERO)
		.append("vo", Constant.ZERO)
		.append("fi", Constant.ZERO)
		.append("ex", Constant.ZERO)
		.append("ifc",Constant.ZERO)
		.append("sul", MongoUtils.convert(MongoUtils.fetchDBObjectList(submitList)))
				.append("cli",MongoUtils.convert(MongoUtils.fetchDBObjectList(classLessonIndex)));
		setBaseEntry(baseEntry);
	}
	
	
	public int getSubmitCount() {
		return getSimpleIntegerValue("sc");
	}

	public void setSubmitCount(int submitCount) {
		setSimpleValue("sc", submitCount);
	}

	public long getLastSubmitTime() {
		return getSimpleLongValue("lut");
	}

	public void setLastSubmitTime(long lastSubmitTime) {
		setSimpleValue("lut", lastSubmitTime);
	}
	
	public ObjectId getTeacherId() {
		return getSimpleObjecIDValue("ti");
	}
	public void setTeacherId(ObjectId teacherId) {
		setSimpleValue("ti", teacherId);
	}
	public List<IdValuePair> getClasses() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setClasses(List<IdValuePair> classes) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(classes);
		setSimpleValue("cs", MongoUtils.convert(list));
	}

	public List<IdValuePair> getClassLessonIndex() {
		List<IdValuePair> retList = new ArrayList<IdValuePair>();
		if(getBaseEntry().containsField("cli")) {
			BasicDBList list = (BasicDBList) getSimpleObjectValue("cli");
			if (null != list && !list.isEmpty()) {
				for (Object o : list) {
					retList.add(new IdValuePair((BasicDBObject) o));
				}
			}
		} else {
			retList = getClasses();
			for(IdValuePair idValuePair : retList){
				idValuePair.setValue(0);
			}
		}
		return retList;
	}
	public void setClassLessonIndex(List<IdValuePair> classes) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(classes);
		setSimpleValue("cli", MongoUtils.convert(list));
	}
	
	
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public String getContent() {
		return getSimpleStringValue("con");
	}
	public void setContent(String content) {
		setSimpleValue("con", content);
	}
	
	public int getIsFromCloud(){
		if(getBaseEntry().containsField("ifc")){
			return getSimpleIntegerValue("ifc");
		}
		return 0;
	}

	public void setIsFromCloud(int isFromCloud){
		setSimpleValue("ifc", isFromCloud);
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
		List<DBObject> list=MongoUtils.fetchDBObjectList(vf);
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
	
	
	
	
	public List<StudentSubmitHomeWork> getSubmitList() {


		List<StudentSubmitHomeWork> retList =new ArrayList<StudentSubmitHomeWork>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sul");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new StudentSubmitHomeWork((BasicDBObject)o));
			}
		}
		return retList;
		
	}
	public void setSubmitList(List<StudentSubmitHomeWork> submitList) {
		
		List<DBObject> list =MongoUtils.fetchDBObjectList(submitList);
		setSimpleValue("sul", MongoUtils.convert(list));
	}



	public void setSubjectIdList(List<ObjectId> subjectIdList) {
		setSimpleValue("sbj", MongoUtils.convert(subjectIdList));
	}

	public List<ObjectId> getSubjectList() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sbj");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}

	public void setTerm(String term) {
		setSimpleValue("tm", term);
	}

	public String getTerm() {
		return getSimpleStringValue("tm");
	}

	public void setLessonId(ObjectId lessonId) {
		setSimpleValue("lid", lessonId);
	}

	public ObjectId getLessonId() {
		return getSimpleObjecIDValue("lid");
	}

	public void setVersion(int version) {
		setSimpleValue("ver", version);
	}

	public int getVersion() {
		return getSimpleIntegerValue("ver");
	}

	public void setType(int type) {
		setSimpleValue("ty", type);
	}

	public Integer getType() {
		return getSimpleIntegerValue("ty");
	}

	public void setCorrect(int correct) {
		setSimpleValue("pg", correct);
	}

	public int getCorrect() {
		return getSimpleIntegerValue("pg");
	}

	public void setVideoNum(int flag) {
		setSimpleValue("vi", flag);
	}

	public int getVideoNum() {
		if(getBaseEntry().containsField("vi")){
			return getSimpleIntegerValue("vi");
		}
		return 0;
	}

	public void setVoiceNum(int flag) {
		setSimpleValue("vo", flag);
	}

	public int getVoiceNum() {
		return getSimpleIntegerValue("vo");
	}

	public void setFileNum(int flag) {
		setSimpleValue("fi", flag);
	}

	public int getFileNum() {
		return getSimpleIntegerValue("fi");
	}

	public void setExerciseNum(int flag) {
		setSimpleValue("ex", flag);
	}

	public int getExerciseNum() {
		return getSimpleIntegerValue("ex");
	}


}
