package com.fulaan_old.forum.service;

import com.db.forum.FVoteDao;
import com.pojo.forum.FVoteDTO;
import com.pojo.forum.FVoteEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/13.
 */
@Service
public class FVoteService {

    public FVoteDao fVoteDao = new FVoteDao();


    /**
     * 查询某论坛投票贴信息
     *
     * @param voteId
     * @param userId
     * @return
     */
    public FVoteEntry getFVote(String voteId, String userId) {
        ObjectId vote = voteId.equals("") ? null : new ObjectId(voteId);
        ObjectId user = userId.equals("") ? null : new ObjectId(userId);
        return fVoteDao.findFVote(vote, user);
    }


    /**
     * 新增和覆盖
     *
     * @param fVoteDTO
     * @return
     */
    public ObjectId addFVote(FVoteDTO fVoteDTO) {
        return fVoteDao.saveOrUpdate(fVoteDTO.exportEntry());
    }

    /**
     * 批量添加
     */
    public Boolean addFVoteList(List<FVoteEntry> fVoteEntryList) {
        return fVoteDao.addFVoteList(fVoteEntryList);
    }

    /**
     * 获取该帖的投票信息
     *
     * @param voteId
     * @return
     */
    public List<FVoteDTO> getFVoteList(String voteId) {
        ObjectId vote = voteId.equals("") ? null : new ObjectId(voteId);
        List<FVoteEntry> fVoteEntries = fVoteDao.getFVoteList(vote);
        List<FVoteDTO> fVoteDTOList = new ArrayList<FVoteDTO>();
        for (FVoteEntry fVoteEntry : fVoteEntries) {
            fVoteDTOList.add(new FVoteDTO(fVoteEntry));
        }
        return fVoteDTOList;
    }

    /**
     * 获取该帖的投票人数
     *
     * @param voteId
     * @return
     */
    public int getFVoteCount(String voteId) {
        ObjectId vote = voteId.equals("") ? null : new ObjectId(voteId);
        return fVoteDao.getFVoteListCount(vote);
    }
}
