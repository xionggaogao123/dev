package com.fulaan.lancustom.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.lancustom.CommonQuestionDao;
import com.fulaan.lancustom.dto.CommonQuestionDto;
import com.pojo.lancustom.CommonQuestionEntry;

import cn.jiguang.commom.utils.StringUtils;

/**
 * 
 * <简述>
 * <详细描述>
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
@Service
public class CommonQuestionService {

    CommonQuestionDao commonQuestionDao = new CommonQuestionDao();
    
    public List<CommonQuestionDto> getCommonQuestion (String pid, String question, int page,int pageSize) {
        List<CommonQuestionEntry> list = commonQuestionDao.getCommonQuestion(pid, question, page, pageSize);
        List<CommonQuestionDto> dtoList = new ArrayList<CommonQuestionDto>();
        for (CommonQuestionEntry c : list) {
            CommonQuestionDto cqd = new CommonQuestionDto(c);
            dtoList.add(cqd);
        }
        return dtoList;
    }
    
    public List<CommonQuestionDto> getCommonQuestion (int type, String question, int page,int pageSize) {
        List<CommonQuestionEntry> list = commonQuestionDao.getCommonQuestion(type, question, page, pageSize);
        List<CommonQuestionDto> dtoList = new ArrayList<CommonQuestionDto>();
        for (CommonQuestionEntry c : list) {
            CommonQuestionDto cqd = new CommonQuestionDto(c);
            dtoList.add(cqd);
        }
        return dtoList;
    }
    
    public long getCommonQuestionCount (String pid, String question) {
        List<CommonQuestionEntry> list = commonQuestionDao.getCommonQuestionCount(pid, question);
        
        return list.size();
    }
    
    public long getCommonQuestionCountByType (int type, String question) {
        List<CommonQuestionEntry> list = commonQuestionDao.getCommonQuestionCountByType(type, question);
        
        return list.size();
    }
    
    public void saveQuestion(CommonQuestionDto dto) throws Exception{
        if (StringUtils.isEmpty(dto.getId())) {
            CommonQuestionEntry entry = dto.buildAddEntry();
            commonQuestionDao.addEntry(entry);
        } else {
            commonQuestionDao.updateQuestion(new ObjectId(dto.getId()), dto.getQuestion(), dto.getAnswer(), dto.getType());
        }
       
    }
    
    /**
     * 
     *〈简述〉删除问题
     *〈详细描述〉
     * @author Administrator
     * @param dto
     */
    public void updateIsr(ObjectId goodId) throws Exception{
        commonQuestionDao.updateIsr(goodId);
        
    }
}
