package com.fulaan.review.service;

import com.db.factory.MongoFacroty;
import com.db.review.ReviewDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.PageDTO;
import com.pojo.review.ReviewEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 评课议课逻辑处理
 * Created by Caocui on 2015/8/26.
 */
@Service
public class ReviewService {
    private ReviewDao reviewDao = new ReviewDao();

    /**
     * 分页查询评课议课数据
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageDTO<ReviewEntry> findReviewEntries(int pageNo, int pageSize,ObjectId edId) {
        return findReviewEntries(null, null, pageNo, pageSize,edId);
    }

    /**
     * 根据章节信息查询评课议课
     *
     * @param queryId
     * @param columName
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageDTO<ReviewEntry> findReviewEntries(String queryId, String columName, int pageNo, int pageSize,ObjectId edId) {
        PageDTO<ReviewEntry> pageDTO = new PageDTO<ReviewEntry>();
        pageDTO.setCount(reviewDao.count(queryId, columName,edId));
        if (pageDTO.getCount() > 0) {
            int pageNum = (pageDTO.getCount() + pageSize - 1) / pageSize;
            pageNo = pageNo > pageNum ? pageNum : pageNo;
            pageDTO.setList(reviewDao.findReviewEntry(queryId, columName, pageNo, pageSize,edId));
        } else {
            pageDTO.setList(Collections.EMPTY_LIST);
        }
        return pageDTO;
    }

    /**
     * 添加评课议课记录
     *
     * @param reviewEntry
     * @return
     */
    public ObjectId addReviewCourse(ReviewEntry reviewEntry) {
        return reviewDao.add(reviewEntry);
    }

    /**
     * 根据ID获取对应的对象息
     *
     * @param id
     * @return
     */
    public ReviewEntry getReviewEntry(String id) {
        return reviewDao.getReviewEntry(new ObjectId(id));
    }
    
    /**
	 * 查询所有的列表信息
	 * @param resourceDictionaryId
	 * @param columName
	 * @return
	 */
	public List<ReviewEntry> getAllReviewEntries(ObjectId edId){
		return reviewDao.getAllReviewEntries(edId);
	}

	/**
	 * 根据传入的数据字典ID和字段名查询集体备课List
	 * @param resourceDictionaryId
	 * @param columName
	 * @return
	 */
	public List<ReviewEntry> getReviewEntriesByResourceDictionaryId(String resourceDictionaryId,String columName,ObjectId edId){
		return reviewDao.getReviewEntriesByResourceDictionaryId(resourceDictionaryId, columName,edId);
	}
	
	/**
     * 给某一个评课议课去除一个课件
     * @param userId
     * @return
     */
    public void delFileForReview(ObjectId reviewId,ObjectId fileId)
    {
    	reviewDao.delFileForReview(reviewId, fileId);
    }
}
