package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/13.
 */
public class FVoteDTO {


    private String id;
    private String VoteId;
    private String userId;
    private int    number;

    public FVoteDTO(){}

    public FVoteDTO(FVoteEntry fVoteEntry){
        this.id=fVoteEntry.getID().toString();
        this.VoteId=fVoteEntry.getVoteId().toString();
        this.userId=fVoteEntry.getUserId().toString();
        this.number=fVoteEntry.getNumber();
    }

    public FVoteEntry exportEntry(){
        FVoteEntry fVoteEntry=new FVoteEntry();
        if(id != null && !id.equals("")){
            fVoteEntry.setID(new ObjectId(id));
        }
        if(VoteId != null && !VoteId.equals("")){
            fVoteEntry.setVoteId(new ObjectId(VoteId));
        }
        if(userId != null && !userId.equals("")){
            fVoteEntry.setUserId(new ObjectId(userId));
        }
        fVoteEntry.setNumber(number);
        return fVoteEntry;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVoteId() {
        return VoteId;
    }

    public void setVoteId(String voteId) {
        VoteId = voteId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
