package com.fulaan.mall.service;

import com.db.ebusiness.EGoodsCategoryDao;
import com.db.ebusiness.EGradeCategoryDao;
import com.pojo.ebusiness.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/3/3.
 */
@Service
public class EGoodsCategoryService {

    //=====================================商品分类============================================

    private EGoodsCategoryDao eGoodsCategoryDao = new EGoodsCategoryDao();

    /**
     * 增加一个商品分类
     *
     * @param eGoodsCategoryDTO
     * @return
     */
    public ObjectId addEGoodsCategory(EGoodsCategoryDTO eGoodsCategoryDTO) {
        return eGoodsCategoryDao.addEGoodsCategory(eGoodsCategoryDTO.exportEntry());
    }

    /**
     * 更新分类
     */
    public ObjectId updateEGoodsCategory(EGoodsCategoryDTO dto) {
        return eGoodsCategoryDao.updateEGoodsCategory(dto.exportEntry());
    }

    /**
     * 更新分类排序
     */
    public void updateEGoodsCategorySort(ObjectId selfId, int selfSort, ObjectId anotherId, int anotherSort) {
        eGoodsCategoryDao.updateEGoodsCategorySort(selfId, selfSort, anotherId, anotherSort);
    }

    /**
     * 上传首页图片
     */
    public void updateEGoodsCategoryImg(ObjectId id, String type, String imgUrl) {
        eGoodsCategoryDao.updateEGoodsCatetoryImg(id, type, imgUrl);
    }


    /**
     * 得到子分类
     *
     * @param parentId
     * @return
     */
    public List<EGoodsCategoryDTO> getEGoodsCategoryListByParentId(ObjectId parentId) {
        List<EGoodsCategoryDTO> eGoodsCategoryDTOList = new ArrayList<EGoodsCategoryDTO>();
        List<EGoodsCategoryEntry> eGoodsCategoryEntryList = eGoodsCategoryDao.getEGoodsCategoryListByParentId(parentId);
        for (EGoodsCategoryEntry eGoodsCategoryEntry : eGoodsCategoryEntryList) {
            eGoodsCategoryDTOList.add(new EGoodsCategoryDTO(eGoodsCategoryEntry));
        }
        return eGoodsCategoryDTOList;
    }

    /**
     * 得到某等级所有分类
     *
     * @param level
     * @return
     */
    public List<EGoodsCategoryDTO> getEGoodsCategoryListByLevel(int level, ObjectId parentId) {
        List<EGoodsCategoryDTO> eGoodsCategoryDTOList = new ArrayList<EGoodsCategoryDTO>();
        List<EGoodsCategoryEntry> eGoodsCategoryEntryList = eGoodsCategoryDao.getEGoodsCategoryListByLevel(level, parentId);
        for (EGoodsCategoryEntry eGoodsCategoryEntry : eGoodsCategoryEntryList) {
            eGoodsCategoryDTOList.add(new EGoodsCategoryDTO(eGoodsCategoryEntry));
        }
        return eGoodsCategoryDTOList;
    }

    /**
     * 通过id查询分类
     */
    public EGoodsCategoryDTO findCategoryById(ObjectId id) {
        EGoodsCategoryEntry entry = eGoodsCategoryDao.getEGoodsCategory(id);
        return new EGoodsCategoryDTO(entry);
    }

    /**
     * 删除分类及其子分类
     *
     * @param eGoodsCategoryId
     */
    public void deleteEGoodsCategory(ObjectId eGoodsCategoryId) {
        eGoodsCategoryDao.deleteEGoodsCategory(eGoodsCategoryId);
    }

    //========================================年级分类============================================

    private EGradeCategoryDao eGradeCategoryDao = new EGradeCategoryDao();

    /**
     * 新增
     */
    public ObjectId addEGradeCategory(EGradeCategoryDTO dto) {
        return eGradeCategoryDao.add(dto.exportEntry());
    }

    /**
     * 得到所有分类
     */
    public List<EGradeCategoryDTO> getEGradeCategoryList() {
        List<EGradeCategoryEntry> entryList = new ArrayList<EGradeCategoryEntry>();
        try {
            entryList = eGradeCategoryDao.getGradeCategoryList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EGradeCategoryDTO> dtoList = new ArrayList<EGradeCategoryDTO>();
        if (entryList.size() > 0) {
            for (EGradeCategoryEntry entry : entryList) {
                dtoList.add(new EGradeCategoryDTO(entry));
            }
        }
        return dtoList;
    }

    /**
     * 删除分类
     */
    public void deleteEGradeCategory(ObjectId id) {
        eGradeCategoryDao.delete(id);
    }
}
