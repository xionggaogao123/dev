package com.fulaan.lancustom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.DBObject;
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
            commonQuestionDao.updateQuestion(new ObjectId(dto.getId()), dto.getQuestion(), dto.getAnswer(), dto.getType(), dto.getTypee());
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

    /**
     * 新版常见问题
     * @param cDto
     * @return
     */
    public void saveQuestionType(CommonQuestionDto cDto) {
        CommonQuestionEntry entry = cDto.buildAddTypeEntry();
        commonQuestionDao.addEntry(entry);
    }

    /**
     * 新版常见问题数据修复
     * @return
     */
    public String fixNewCommonQuestion() {
        try {
            //查找所有的数据
            List<CommonQuestionEntry> allList = commonQuestionDao.getAllCommonQuestion();
//            //查找是类型的数据
//            List<CommonQuestionEntry> allTypeList = commonQuestionDao.getAllTypeCommonQuestion();
            //新版修复删除
            List <ObjectId> ids = new ArrayList<ObjectId>();
            for (CommonQuestionEntry entry : allList){
                ids.add(entry.getID());
            }
            commonQuestionDao.updateRemoveAllIsr(ids);
            //筛选需要留下来的类型（去重）
            List<CommonQuestionEntry> stayTypeList = new ArrayList<CommonQuestionEntry>();
            List<Integer> typeList = new ArrayList<Integer>();
            for (CommonQuestionEntry entry : allList){
                if (!typeList.contains(entry.getType())){
                    stayTypeList.add(entry);
                    typeList.add(entry.getType());
                }
            }
            //插入所有类型
            List<DBObject> dbObjects = new ArrayList<DBObject>();
            for (CommonQuestionEntry entry : stayTypeList){
                CommonQuestionEntry entryNew = new CommonQuestionEntry();
                entryNew.setAnswer("");
                entryNew.setClasses(entry.getClasses());
                entryNew.setIsRemove(0);
//                entryNew.setPid(null);
                entryNew.setQuestion("");
                entryNew.setType(entry.getType());
                commonQuestionDao.addEntry(entryNew);
//                dbObjects.add(entryNew.getBaseEntry());
            }
//            commonQuestionDao.insertAll(dbObjects);
            //重新获取类型数据
            List<CommonQuestionEntry> allNewTypeList = commonQuestionDao.getAllTypeCommonQuestion();
            //根据type给所有类型答案分组
            List<DBObject> dbObjectsQA = new ArrayList<DBObject>();
            //去重相同的问题答案
            List<Map<String,String>> qaContainList = new ArrayList<Map<String, String>>();
            for (CommonQuestionEntry entry : allList){
                /*去重相同的问题答案start*/
                Map<String,String> qaMap = new HashMap<String, String>();
                qaMap.put(entry.getQuestion(),entry.getAnswer());
                if (!qaContainList.contains(qaMap)){
                    qaContainList.add(qaMap);
                }else {
                    continue;//出现过的问题和答案，直接跳过下面的代码
                }
                /*去重相同的问题答案end*/
                for (CommonQuestionEntry entryP : allNewTypeList){
                    if (entry.getType() == entryP.getType()){//属于分类
                        CommonQuestionEntry entryNew = new CommonQuestionEntry();
                        entryNew.setAnswer(entry.getAnswer());
                        entryNew.setClasses(entry.getClasses() != "" ? entry.getClasses() : "");
                        entryNew.setIsRemove(0);
                        entryNew.setPid(entryP.getID());
                        entryNew.setQuestion(entry.getQuestion());
                        entryNew.setType(entry.getType());
                        dbObjectsQA.add(entryNew.getBaseEntry());
                    }
                }
            }
            //插入所有问题答案
            commonQuestionDao.insertAll(dbObjectsQA);
            return "修复成功";
        }catch (Exception e){
            e.printStackTrace();
            return "修复失败："+e.getMessage();
        }

    }
}
