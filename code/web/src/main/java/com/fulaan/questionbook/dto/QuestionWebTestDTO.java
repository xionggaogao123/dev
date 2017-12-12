package com.fulaan.questionbook.dto;

import com.pojo.questionbook.QuestionWebSizeEntry;
import com.pojo.questionbook.QuestionWebTestEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/12/12.
 */
public class QuestionWebTestDTO {
    /*ObjectId id,
            String title,
            List<QuestionWebSizeEntry> sizeList,
            int  count*/

    private String id;
    private String title;
    private String userId;
    private List<QuestionWebSizeDTO> sizeList = new ArrayList<QuestionWebSizeDTO>();
    private String createTime;
    private int count;
    public QuestionWebTestDTO(){

    }
    public QuestionWebTestDTO(QuestionWebTestEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.title = e.getTitle();
            this.count = e.getCount();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            List<QuestionWebSizeEntry> attachmentEntries = e.getSizeList();
            if(attachmentEntries != null && attachmentEntries.size()>0){
                for(QuestionWebSizeEntry entry : attachmentEntries){
                    this.sizeList.add(new QuestionWebSizeDTO(entry));
                }
            }

        }else{
            new QuestionTagsDTO();
        }
    }

    public QuestionWebTestEntry buildAddEntry(){
        List<QuestionWebSizeEntry> videoEntries=new ArrayList<QuestionWebSizeEntry>();
        if(sizeList.size()>0){
            for(QuestionWebSizeDTO videoDTO:sizeList){
                videoEntries.add(new QuestionWebSizeEntry(new ObjectId(videoDTO.getQuestionId()),
                        videoDTO.getQuestionHeight(),videoDTO.getAnswerHeight()));
            }
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        QuestionWebTestEntry openEntry =
                new QuestionWebTestEntry(
                        title,
                        uId,
                        videoEntries,
                        count
                );
        return openEntry;

    }
    public QuestionWebTestEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        List<QuestionWebSizeEntry> videoEntries=new ArrayList<QuestionWebSizeEntry>();
        if(sizeList.size()>0){
            for(QuestionWebSizeDTO videoDTO:sizeList){
                videoEntries.add(new QuestionWebSizeEntry(new ObjectId(videoDTO.getQuestionId()),
                        videoDTO.getQuestionHeight(),videoDTO.getAnswerHeight()));
            }
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        QuestionWebTestEntry openEntry =
                new QuestionWebTestEntry(
                        Id,
                        title,
                        uId,
                        videoEntries,
                        count
                );
        return openEntry;

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuestionWebSizeDTO> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<QuestionWebSizeDTO> sizeList) {
        this.sizeList = sizeList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
