package com.fulaan.appvote.dto;

import com.fulaan.pojo.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by scott on 2017/11/7.
 */
public class VoteResult {
    private String voteItemStr;
    private int voteItemCount;
    private String voteItemPercent;
    private int hasVoted;

    private List<String> userIds=new ArrayList<String>();
    private List<User> voteUsers=new ArrayList<User>();

    public VoteResult(){

    }

    public String getVoteItemStr() {
        return voteItemStr;
    }

    public void setVoteItemStr(String voteItemStr) {
        this.voteItemStr = voteItemStr;
    }

    public int getVoteItemCount() {
        return voteItemCount;
    }

    public void setVoteItemCount(int voteItemCount) {
        this.voteItemCount = voteItemCount;
    }

    public String getVoteItemPercent() {
        return voteItemPercent;
    }

    public void setVoteItemPercent(String voteItemPercent) {
        this.voteItemPercent = voteItemPercent;
    }

    public int getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(int hasVoted) {
        this.hasVoted = hasVoted;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<User> getVoteUsers() {
        return voteUsers;
    }

    public void setVoteUsers(List<User> voteUsers) {
        this.voteUsers = voteUsers;
    }
}
