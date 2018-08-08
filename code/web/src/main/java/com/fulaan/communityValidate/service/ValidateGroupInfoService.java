package com.fulaan.communityValidate.service;

import com.db.communityValidate.ValidateGroupInfoDao;
import com.db.fcommunity.GroupDao;
import com.fulaan.communityValidate.dto.ValidateGroupInfoDTO;
import com.mongodb.DBObject;
import com.pojo.fcommunity.GroupEntry;
import com.pojo.fcommunity.ValidateGroupInfoEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-08-07.
 */
@Service
public class ValidateGroupInfoService {
    private ValidateGroupInfoDao validateGroupInfoDao = new ValidateGroupInfoDao();

    private GroupDao groupDao = new GroupDao();

    public boolean saveValidateInfos(ObjectId userId,ObjectId contactId,int type,String message,List<ObjectId> ids){
        List<ValidateGroupInfoEntry> entries = new ArrayList<ValidateGroupInfoEntry>();
        for(ObjectId id :ids){
            ValidateGroupInfoEntry validateGroupInfoEntry = new ValidateGroupInfoEntry(userId,id,contactId,type, Constant.ZERO,0,Constant.ZERO,message);
            entries.add(validateGroupInfoEntry);
        }
        this.addClassEntryBatch(entries);
        return true;
    }

    public Map<String,Object> getValidateList(ObjectId userId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ValidateGroupInfoDTO> validateGroupInfoDTOs = new ArrayList<ValidateGroupInfoDTO>();
        List<ValidateGroupInfoEntry> entries = validateGroupInfoDao.getMyBuyEntryListById(userId, page, pageSize, Constant.ZERO);
        int count =  validateGroupInfoDao.countMyBuyEntryListById(userId,Constant.ZERO);
        for(ValidateGroupInfoEntry entry :entries){
            ValidateGroupInfoDTO dto = new ValidateGroupInfoDTO(entry);
            validateGroupInfoDTOs.add(dto);
        }
        map.put("list",validateGroupInfoDTOs);
        map.put("count",count);
        return map;
    }

    public String agreeOrRefuse(ObjectId id,int type){
        ValidateGroupInfoEntry validateGroupInfoEntry =  validateGroupInfoDao.getEntry(id);
        if(validateGroupInfoEntry!=null && validateGroupInfoEntry.getContactId()!=null){
            GroupEntry groupEntry =  groupDao.findByObjectId(validateGroupInfoEntry.getContactId());
            long current = System.currentTimeMillis();
            //修改申请状态
            validateGroupInfoDao.updEntry(id,type,Constant.ONE,current);
            if(groupEntry!=null){
                return groupEntry.getEmChatId();
            }
        }
        return "";

    }


    /**
     * 批量增加课时
     * @param list
     */
    public void addClassEntryBatch(List<ValidateGroupInfoEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ValidateGroupInfoEntry si = list.get(i);
            dbList.add(si.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            validateGroupInfoDao.addBatch(dbList);
        }
    }
}
