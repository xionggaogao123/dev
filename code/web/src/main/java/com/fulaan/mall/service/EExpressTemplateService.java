package com.fulaan.mall.service;

import com.db.ebusiness.EExpressTemplateDao;
import com.pojo.ebusiness.EExpressTemplateDTO;
import com.pojo.ebusiness.EExpressTemplateEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 运费模板Service
 * Created by Wangkaidong on 2016/3/9.
 */

@Service
public class EExpressTemplateService {
    private EExpressTemplateDao expTempDao = new EExpressTemplateDao();

    /**
     * 获取运费模板列表(分页)
     */
    public List<EExpressTemplateDTO> getExpTempList(int page, int pageSize) {
        List<EExpressTemplateDTO> exList = new ArrayList<EExpressTemplateDTO>();

        for (EExpressTemplateEntry entry : expTempDao.getEntryList(page, pageSize)) {
            exList.add(new EExpressTemplateDTO(entry));
        }

        return exList;
    }


    /**
     * 查询模板总数
     */
    public int getTotal() {
        return expTempDao.getTotal();
    }

    /**
     * 根据ID查询模板
     */
    public EExpressTemplateDTO getTemplateById(String id) {
        EExpressTemplateEntry entry = expTempDao.getEntryById(new ObjectId(id));
        EExpressTemplateDTO dto = new EExpressTemplateDTO(entry);
        return dto;
    }

    /**
     * 新增或更新
     */
    public ObjectId saveOrUpdate(EExpressTemplateDTO expTempDTO) {
        if (null == expTempDTO.getId() || expTempDTO.getId().equals("")) {
            return expTempDao.add(expTempDTO.exportEntry());
        } else {
            return expTempDao.update(expTempDTO.exportEntry());
        }

    }

    /**
     * 删除
     */
    public void remove(ObjectId id) {
        expTempDao.delete(id);
    }

    /**
     * 获取运费模板列表(不分页)
     */
    public List<EExpressTemplateDTO> getExpTempList() {
        List<EExpressTemplateDTO> exList = new ArrayList<EExpressTemplateDTO>();

        for (EExpressTemplateEntry entry : expTempDao.getEntryList()) {
            exList.add(new EExpressTemplateDTO(entry));
        }

        return exList;
    }
}
