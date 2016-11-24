package com.pojo.docflow;

import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/8/17.
 */
public class DocFlowDTO {
    private String id;
    private String term;
    private String title;
    private String userId;
    private String userName;
    private String departmentId;
    private String departmentName;
    private String time;
    private List<String> publishList;
    private List<String> publishNames;
    private List<String> unreadList;
    private Boolean ifCheck;
    private List<DocCheckDTO> checkDTOList;
    private String content;
    private List<IdUserFilePairDTO> docList;
    private Integer state;//
    private String checkId;//最新审阅状态Id
    private String schoolId;

    public DocFlowDTO()
    {
        super();
    }
    public DocFlowDTO(DocFlowEntry doc) {
        this.id = doc.getID().toString();
        this.term = doc.getTerm();
        this.title = doc.getTitle();
        this.userId = doc.getUserId().toString();
        this.departmentId = doc.getDepartmentId().toString();
        this.publishList = new ArrayList<String>();
        for(ObjectId oid :doc.getPublishList())
        {
            this.publishList.add(oid.toString());
        }
        this.unreadList = new ArrayList<String>();
        for(ObjectId oid :doc.getUnreadList())
        {
            this.unreadList.add(oid.toString());
        }
        this.ifCheck = doc.getIfCheck() == 1 ? true : false;
        this.checkDTOList=new ArrayList<DocCheckDTO>();
        for (DocCheckEntry d : doc.getDocCheckList()) {
            this.checkDTOList.add(new DocCheckDTO(d));
        }
        this.content = doc.getContent();
        this.docList=new ArrayList<IdUserFilePairDTO>();
        for (IdUserFilePair p : doc.getDocFile()) {
            this.docList.add(new IdUserFilePairDTO(p));
        }
        if(doc.getSchoolId()!=null)
            this.schoolId=doc.getSchoolId().toString();
    }

    public DocFlowEntry export() {
        DocFlowEntry docFlowEntry = new DocFlowEntry();
        docFlowEntry.setTerm(this.getTerm());
        docFlowEntry.setTitle(this.getTitle());
        docFlowEntry.setUserId(new ObjectId(this.getUserId()));
        docFlowEntry.setDepartmentId(new ObjectId(this.getDepartmentId()));

        List<ObjectId> publishList=new ArrayList<ObjectId>();
        for(String strId : this.getPublishList())
        {
            publishList.add(new ObjectId(strId));
        }
        docFlowEntry.setPublishList(publishList);

        List<ObjectId> unReadList=new ArrayList<ObjectId>();
        for(String strId : this.getUnreadList())
        {
            unReadList.add(new ObjectId(strId));
        }
        docFlowEntry.setUnreadList(unReadList);

        docFlowEntry.setIfCheck(this.getIfCheck() ? 1 : 0);
        List<DocCheckEntry> docCheckEntries=new ArrayList<DocCheckEntry>();
        for(DocCheckDTO docCheckDTO : this.checkDTOList)
        {
            docCheckEntries.add(docCheckDTO.export());
        }
        docFlowEntry.setDocCheckList(docCheckEntries);
        docFlowEntry.setContent(this.getContent());
        List<IdUserFilePair> idNameValuePairs=new ArrayList<IdUserFilePair>();
        for(IdUserFilePairDTO idValuePairDTO :this.getDocList())
        {
            IdUserFilePair idValuePair=new IdUserFilePair(new ObjectId(idValuePairDTO.getId()),new ObjectId(idValuePairDTO.getUserId()),
                    idValuePairDTO.getName(),idValuePairDTO.getValue());
            idNameValuePairs.add(idValuePair);
        }
        docFlowEntry.setDocFile(idNameValuePairs);
        docFlowEntry.setState(this.getState());
        docFlowEntry.setCheckId(new ObjectId(this.getCheckId()));
        if(this.getSchoolId()!=null)
            docFlowEntry.setSchoolId(new ObjectId(this.getSchoolId()));
        return docFlowEntry;
    }
    public DocFlowEntry exportSimple() {
        DocFlowEntry docFlowEntry = new DocFlowEntry();
        docFlowEntry.setID(new ObjectId(this.getId()));
        docFlowEntry.setTitle(this.getTitle());
        if(!this.getUserId().equals("")&&!this.getUserId().equals(null))
            docFlowEntry.setUserId(new ObjectId(this.getUserId()));

        List<ObjectId> publishList=new ArrayList<ObjectId>();
        for(String strId : this.getPublishList())
        {
            publishList.add(new ObjectId(strId));
        }
        docFlowEntry.setPublishList(publishList);

        List<ObjectId> unReadList=new ArrayList<ObjectId>();
        for(String strId : this.getUnreadList())
        {
            unReadList.add(new ObjectId(strId));
        }
        docFlowEntry.setUnreadList(unReadList);

        docFlowEntry.setContent(this.getContent());
        List<IdUserFilePair> idNameValuePairs=new ArrayList<IdUserFilePair>();
        for(IdUserFilePairDTO idValuePairDTO :this.getDocList())
        {
            IdUserFilePair idValuePair=new IdUserFilePair(new ObjectId(idValuePairDTO.getId()),new ObjectId(idValuePairDTO.getUserId()),
                    idValuePairDTO.getName(),idValuePairDTO.getValue());
            idNameValuePairs.add(idValuePair);
        }
        docFlowEntry.setDocFile(idNameValuePairs);
        docFlowEntry.setState(this.getState());
        if(this.getSchoolId()!=null)
            docFlowEntry.setSchoolId(new ObjectId(this.getSchoolId()));
        return docFlowEntry;
    }

    public List<String> getPublishNames() {
        return publishNames;
    }

    public void setPublishNames(List<String> publishNames) {
        this.publishNames = publishNames;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<String> getPublishList() {
        return publishList;
    }

    public void setPublishList(List<String> publishList) {
        this.publishList = publishList;
    }

    public List<String> getUnreadList() {
        return unreadList;
    }

    public void setUnreadList(List<String> unreadList) {
        this.unreadList = unreadList;
    }

    public Boolean getIfCheck() {
        return ifCheck;
    }

    public void setIfCheck(Boolean ifCheck) {
        this.ifCheck = ifCheck;
    }

    public List<DocCheckDTO> getCheckDTOList() {
        return checkDTOList;
    }

    public void setCheckDTOList(List<DocCheckDTO> checkDTOList) {
        this.checkDTOList = checkDTOList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<IdUserFilePairDTO> getDocList() {
        return docList;
    }

    public void setDocList(List<IdUserFilePairDTO> docList) {
        this.docList = docList;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
