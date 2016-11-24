package com.fulaan.forum.service;

import com.db.forum.FPostDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.SortType;
import com.pojo.forum.FPostDTO;
import com.pojo.forum.FPostEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/31.
 */
@Service
public class FPostService {
    private FPostDao fPostDao=new FPostDao();


    /**
     * 帖子列表
     * @param orderType 排序方式（发帖时间/最新发表）
     * @param postSection
     * @param person
     * @param page
     * @param cream  精华
     * @param classify  分类
     * @param gtTime  发帖时间范围
     * @param pageSize
     * @return
     */
    public List<FPostDTO> getFPostsList(int orderType,String postSection,String person,
                                        int page,int cream,int classify, long gtTime,int pageSize){
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        List<FPostEntry> fPostEntries=fPostDao.getFPostEntries(Constant.FIELDS,sort,cream,postSectionId,personId,classify,gtTime,
        (page-1)*pageSize, pageSize);
        List<FPostDTO> fPostDTOList = new ArrayList<FPostDTO>();
        if(null != fPostEntries){
            for(FPostEntry fPostEntry : fPostEntries){
                fPostDTOList.add(new FPostDTO(fPostEntry));
            }
        }
        return fPostDTOList;

    }

    /**
     * 查询帖子数量
     * @param postSection
     * @param person
     * @param cream
     * @param classify
     * @param gtTime
     * @return
     */
    public int getFPostsCount(String postSection,String person,
                                        int cream,int classify, long gtTime){
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        ObjectId personId = person.equals("") ? null : new ObjectId(person);
        return fPostDao.getFPostEntriesCount(cream,postSectionId,personId,classify,gtTime);

    }

    /**
     * 帖子详情
     * @param Id
     * @return
     */
    public FPostDTO detail(ObjectId Id){
        FPostEntry  fPostEntry = fPostDao.getFPostEntry(Id);
        FPostDTO fPostDTO =  new FPostDTO(fPostEntry);
        return fPostDTO;
    }

    /**
     * 新增和覆盖
     * @param fPostDTO
     */
    public ObjectId addFPostEntry(FPostDTO fPostDTO){
        fPostDTO.setTime(System.currentTimeMillis());
        fPostDTO.setUpdateTime(System.currentTimeMillis());
        return fPostDao.addFPost(fPostDTO.exportEntry());
    }


    /**
     * 获取某版块的主题数
     * @param postSection
     * @return
     */
    public int getThemeCount(String postSection){
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        return   fPostDao.getThemeCount(postSectionId,"");
    }

    /**
     * 获取某板块的总浏览量
     */
    public int getTotalScanCount(String postSection){
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        return   fPostDao.getTotalScanCount(postSectionId,"");
    }

    /**
     * 获取某板块的总评论数
     */
    public int getTotalCommentCount(String postSection){
        ObjectId postSectionId = postSection.equals("") ? null : new ObjectId(postSection);
        return   fPostDao.getTotalCommentCount(postSectionId,"");
    }
}
