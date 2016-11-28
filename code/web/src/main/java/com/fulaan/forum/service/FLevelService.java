package com.fulaan.forum.service;

import com.db.forum.FLevelDao;
import com.pojo.forum.FLevelDTO;
import com.pojo.forum.FLevelEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/11.
 */
@Service
public class FLevelService {

    private FLevelDao fLevelDao = new FLevelDao();

    /**
     * 查询所有的论坛等级信息
     */
    public List<FLevelDTO> getFLevelInf() {
        List<FLevelEntry> fLevelEntryList = fLevelDao.findFLevel();
        List<FLevelDTO> fLevelDTOList = new ArrayList<FLevelDTO>();
        for (FLevelEntry item : fLevelEntryList) {
            fLevelDTOList.add(new FLevelDTO(item));
        }
        return fLevelDTOList;
    }

    /**
     * 查询某论坛等级信息
     *
     * @param levelId
     * @return
     */
    public FLevelDTO getFLevelById(String levelId) {
        ObjectId id = levelId.equals("") ? null : new ObjectId(levelId);
        return new FLevelDTO(fLevelDao.findLevelById(id));
    }

    /**
     * 删除某论坛等级信息
     *
     * @param levelId
     */
    public void removeFLevel(String levelId) {
        ObjectId id = levelId.equals("") ? null : new ObjectId(levelId);
        fLevelDao.remove(id);
    }

    /**
     * 新增和覆盖
     *
     * @param fLevelDTO
     * @return
     */
    public ObjectId addFLevel(FLevelDTO fLevelDTO) {
        return fLevelDao.saveOrUpdate(fLevelDTO.exportEntry());
    }


    /**
     * 根据经验值获取他的等级星星数
     *
     * @param levels
     * @return
     */
    public long getStars(long levels) {
        return fLevelDao.getStars(levels);
    }

    public long getMinLevel(long stars) {
        return fLevelDao.getMinLevel(stars);
    }

}
