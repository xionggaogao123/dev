package com.fulaan.lancustom.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.lancustom.MonetaryGoodsDao;
import com.fulaan.lancustom.dto.MonetaryGoodsDto;
import com.pojo.lancustom.MonetaryGoodsEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/6 15:35
 * @Description:
 */
@Service
public class MonetaryGoodsService {

    MonetaryGoodsDao dao = new MonetaryGoodsDao();

    public void saveGoodsInfo(MonetaryGoodsDto dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            MonetaryGoodsEntry entry = dto.buildAddEntry();
            dao.addEntry(entry);
        } else {
            dao.updateGoods(new ObjectId(dto.getId()),dto.getAvatar(),dto.getDescription(),dto.getLabel(),dto.getMoney(),dto.getName(),dto.getPic(),dto.getStyle());
        }
    }

    public List<MonetaryGoodsDto> getMonetaryGoodsList(String name, int page, int pageSize) {
        List<MonetaryGoodsEntry> list = dao.getMonetaryGoodsList(name,page,pageSize);
        List<MonetaryGoodsDto> goodsList = new ArrayList<MonetaryGoodsDto>();
        for (MonetaryGoodsEntry g : list) {
            MonetaryGoodsDto gd = new MonetaryGoodsDto(g);
            goodsList.add(gd);
        }

        return goodsList;
    }

    public int getMonetaryGoodsCount(String name) {
        int count = dao.getMonetaryGoodsCount(name);
        return count;
    }

    public void delMonetaryGoods(ObjectId goodId) {
        dao.updateIsr(goodId);
    }

    public MonetaryGoodsDto getMonetaryGoodsDetail(String goodId) {
        MonetaryGoodsEntry goodsEntry = dao.getEntryById(new ObjectId(goodId));
        MonetaryGoodsDto dto = new MonetaryGoodsDto(goodsEntry);
        return dto;
    }
}
