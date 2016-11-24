package com.fulaan.forum.service;

import com.db.forum.FReplyDao;
import com.mongodb.BasicDBObject;
import com.pojo.ebusiness.SortType;
import com.pojo.forum.FReplyDTO;
import com.pojo.forum.FReplyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/31.
 */
@Service
public class FReplyService {

    private FReplyDao fReplyDao = new FReplyDao();

    /**
     * 回帖列表
     *
     * @param orderType   排序方式（时间先后）
     * @param postSection
     * @param post
     * @param page
     * @param pageSize
     * @return
     */
    public List<FReplyDTO> getFRepliesList(int orderType, String postSection, String post,String person,
                                         int page, int pageSize) {
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        ObjectId personId = person.equals("") ? null : new ObjectId(post);
        List<FReplyEntry> fReplyEntries = fReplyDao.getFReplyEntries(Constant.FIELDS, sort, postSectionId, postId,personId,
                (page - 1) * pageSize, pageSize);
        List<FReplyDTO> fReplyDTOList = new ArrayList<FReplyDTO>();
        if (null != fReplyDTOList) {
            for (FReplyEntry fReplyEntry : fReplyEntries) {
                fReplyDTOList.add(new FReplyDTO(fReplyEntry));
            }
        }
        return fReplyDTOList;

    }

    /**
     * 查询回帖数量
     *
     * @param postSection
     * @param post
     * @return
     */
    public int getFRepliesCount(String postSection, String post,String person) {
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId postId = post.equals("") ? null : new ObjectId(post);
        ObjectId personId = person.equals("") ? null : new ObjectId(post);
        return fReplyDao.getFReplyEntriesCount(postSectionId, postId,personId);

    }

    /**
     * 帖子详情
     *
     * @param Id
     * @return
     */
    public FReplyDTO detail(ObjectId Id) {
        FReplyEntry fReplyEntry = fReplyDao.getFReplyEntry(Id);
        FReplyDTO fReplyDTO = new FReplyDTO(fReplyEntry);
        return fReplyDTO;
    }

    /**
     * 新增和覆盖
     *
     * @param fReplyDTO
     */
    public ObjectId addFPostEntry(FReplyDTO fReplyDTO) {
        return fReplyDao.addFReply(fReplyDTO.exportEntry());
    }

}
