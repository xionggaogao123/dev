package com.fulaan.quality.dto;

import com.pojo.Quality.TeacherPlainEntry;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.docflow.IdUserFilePair;
import com.pojo.docflow.IdUserFilePairDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/11/10.
 */
public class TeacherPlainDTO {

    private String id;

    private String plainName;

    private String content;

    private String term;

    private List<IdNameValuePairDTO> lessLessonWareDTOList;

    private String docNames;

    private String docAddress;

    private String schoolId;

    private String teacherId;

    private List<IdUserFilePairDTO> docList;

    private int type;

    public TeacherPlainDTO() {

    }

    public TeacherPlainDTO(TeacherPlainEntry entry) {
        this.content = entry.getContent();
        this.term = entry.getTerm();
        this.plainName = entry.getTeachName();
        this.id = entry.getID().toString();
        List<IdUserFilePairDTO> docs = new ArrayList<IdUserFilePairDTO>();
        if (entry.getDocFile()!=null && entry.getDocFile().size()!=0) {
            for (IdUserFilePair idUserFilePair : entry.getDocFile()) {
                docs.add(new IdUserFilePairDTO(idUserFilePair));
            }
        }
        this.docList = docs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlainName() {
        return plainName;
    }

    public void setPlainName(String plainName) {
        this.plainName = plainName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<IdNameValuePairDTO> getLessLessonWareDTOList() {
        return lessLessonWareDTOList;
    }

    public void setLessLessonWareDTOList(List<IdNameValuePairDTO> lessLessonWareDTOList) {
        this.lessLessonWareDTOList = lessLessonWareDTOList;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDocNames() {
        return docNames;
    }

    public void setDocNames(String docNames) {
        this.docNames = docNames;
    }

    public String getDocAddress() {
        return docAddress;
    }

    public void setDocAddress(String docAddress) {
        this.docAddress = docAddress;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<IdUserFilePairDTO> getDocList() {
        return docList;
    }

    public void setDocList(List<IdUserFilePairDTO> docList) {
        this.docList = docList;
    }

    /**
     * 创建TeacherPlainEntry构造函数
     * @return
     */
    public TeacherPlainEntry buildTeacherPlainEntry() {
        List<IdUserFilePair> idNameValuePairs=new ArrayList<IdUserFilePair>();
        for(IdUserFilePairDTO idValuePairDTO :this.getDocList())
        {
            IdUserFilePair idValuePair=new IdUserFilePair(new ObjectId(idValuePairDTO.getId()),new ObjectId(idValuePairDTO.getUserId()),
                    idValuePairDTO.getName(),idValuePairDTO.getValue());
            idNameValuePairs.add(idValuePair);
        }
        TeacherPlainEntry teacherPlainEntry = new TeacherPlainEntry(this.term,new ObjectId(this.schoolId),new ObjectId(this.teacherId),this.plainName,this.content,idNameValuePairs);
        return teacherPlainEntry;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
