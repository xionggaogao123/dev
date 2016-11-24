package com.fulaan.mall.service;

import com.db.ebusiness.EExpressTemplateDao;
import com.db.ebusiness.EGoodsDao;

import com.mongodb.BasicDBObject;
import com.pojo.ebusiness.EExpressTemplateEntry;
import com.pojo.ebusiness.EGoodsDTO;
import com.pojo.ebusiness.EGoodsEntry;
import com.pojo.ebusiness.SortType;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fl on 2016/1/13.
 */
@Service
public class EGoodsService {
    private EGoodsDao eGoodsDao = new EGoodsDao();
    private EExpressTemplateDao expressTemplateDao = new EExpressTemplateDao();

    /**
     * 商品列表
     *
     * @param orderType 排序方式
     * @param page
     * @param pageSize
     */
    public List<EGoodsDTO> getEGoodsList(int state, int activity, int groupPurchase, int maxOut, int orderType, String goodsCategory, String levelGoodsCategory, String gradeCategory, int bookCategory, int[] priceArr, String regular, int page, int pageSize) {
        BasicDBObject sort = SortType.getSortType(orderType).getSort();
        ObjectId goodsCategoryId = goodsCategory.equals("") ? null : new ObjectId(goodsCategory);
        ObjectId levelGoodsCategoryId = levelGoodsCategory.equals("") ? null : new ObjectId(levelGoodsCategory);
        Set<ObjectId> gradeCategorySet = new HashSet<ObjectId>();
        if (!"".equals(gradeCategory)) {
            String[] gradeArr = gradeCategory.split(",");
            for (String grade : gradeArr) {
                ObjectId gradeCategoryId = new ObjectId(grade);
                gradeCategorySet.add(gradeCategoryId);
            }

        }
        List<EGoodsEntry> eGoodsEntryList = eGoodsDao.getEGoodsEntrys(Constant.FIELDS, sort, state, activity, groupPurchase, maxOut, goodsCategoryId, levelGoodsCategoryId, gradeCategorySet, bookCategory, priceArr, regular, (page - 1) * pageSize, pageSize);
        List<EGoodsDTO> eGoodsDTOList = new ArrayList<EGoodsDTO>();
        if (null != eGoodsEntryList) {
            for (EGoodsEntry eGoodsEntry : eGoodsEntryList) {
                eGoodsDTOList.add(new EGoodsDTO(eGoodsEntry));
            }
        }
        return eGoodsDTOList;
    }

    /**
     * 商品个数
     *
     * @return
     */
    public int getEGoodsCount(int state, int activity, int groupPurchase, int maxOut, String goodsCategory, String levelGoodsCategory, String gradeCategory, int bookCategory, int[] priceArr, String regular) {
        ObjectId goodsCategoryId = goodsCategory.equals("") ? null : new ObjectId(goodsCategory);
        ObjectId levelGoodsCategoryId = levelGoodsCategory.equals("") ? null : new ObjectId(levelGoodsCategory);
        Set<ObjectId> gradeCategorySet = new HashSet<ObjectId>();
        if (!"".equals(gradeCategory)) {
            String[] gradeArr = gradeCategory.split(",");
            for (String grade : gradeArr) {
                ObjectId gradeCategoryId = new ObjectId(grade);
                gradeCategorySet.add(gradeCategoryId);
            }
        }
        return eGoodsDao.getEGoodsEntrysCount(state, activity, groupPurchase, maxOut, goodsCategoryId, levelGoodsCategoryId, gradeCategorySet, bookCategory, priceArr, regular);
    }

    /**
     * 商品详情
     *
     * @param goodId
     * @return
     */
    public EGoodsDTO detail(ObjectId goodId) {
        EGoodsEntry eGoodsEntry = eGoodsDao.getEGoodsEntry(goodId, Constant.FIELDS);
        EGoodsDTO eGoodsDTO = new EGoodsDTO(eGoodsEntry);
        return eGoodsDTO;
    }


    /**
     * 得到商品EGoodsEntry
     *
     * @return
     */
    public EGoodsEntry getEGoodsEntry(ObjectId goodId) {
        EGoodsEntry eGoodsEntry = eGoodsDao.getEGoodsEntry(goodId, Constant.FIELDS);
        return eGoodsEntry;
    }

    /**
     * 新增和覆盖
     *
     * @param eGoodsDTO
     */
    public ObjectId addEGoodsEntry(EGoodsDTO eGoodsDTO) {
        return eGoodsDao.addEGoodsEntry(eGoodsDTO.exportEntry());
    }

    /**
     * 物理删除
     *
     * @param goodsId
     */
    public void deleteEGoods(ObjectId goodsId) {
        eGoodsDao.deleteEGoods(goodsId);
    }


    /**
     * 根据地区计算运费
     */
    public Map<String, Double> getExpressPrice(ObjectId goodsId, String province) {
        Map<String, Double> ep = new HashMap<String, Double>();
        ObjectId exTempId = getEGoodsEntry(goodsId).getExpTempId();
        if (exTempId == null) {
            ep.put("firstPrice", 0.0);
            ep.put("addOnePrice", 0.0);
            return ep;
        } else {
            EExpressTemplateEntry templateEntry = expressTemplateDao.getEntryById(exTempId);
            List<EExpressTemplateEntry.ExpTempDetail> details = templateEntry.getDetails();
            for (EExpressTemplateEntry.ExpTempDetail detail : details) {
                if (detail.getZoneName().contains(province)) {
                    ep.put("firstPrice", detail.getFirstPrice());
                    ep.put("addOnePrice", detail.getAddOnePrice());
                    return ep;
                }
            }
            ep.put("firstPrice", 0.0);
            ep.put("addOnePrice", 0.0);
            return ep;
        }
    }


    /**
     * 增加人气
     */
    public void updatePopular(ObjectId goodsId) {
        EGoodsEntry entry = eGoodsDao.getEGoodsEntry(goodsId, Constant.FIELDS);
        entry.setPopularLevel(entry.getPopularLevel() + 1);
        addEGoodsEntry(new EGoodsDTO(entry));
    }
}


