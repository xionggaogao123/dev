package com.fulaan.train.service;

import com.db.train.ItemTypeNameDao;
import com.fulaan.train.dto.ItemTypeDTO;
import com.pojo.train.ItemTypeEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/2.
 */
@Service
public class ItemTypeService {

    private ItemTypeNameDao itemTypeNameDao=new ItemTypeNameDao();

    public List<ItemTypeDTO> getItemTypes(int level, ObjectId parentId){
        List<ItemTypeDTO> dtos=new ArrayList<ItemTypeDTO>();
        List<ItemTypeEntry> entries=itemTypeNameDao.getLevelEntries(level,parentId);
        for(ItemTypeEntry itemTypeEntry:entries){
            dtos.add(new ItemTypeDTO(itemTypeEntry));
        }
        return dtos;
    }
}
