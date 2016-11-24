package com.fulaan.overallquality.service;

import com.db.overallquality.*;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.overallquality.dto.*;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.CommonUtils;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.pojo.KeyValue;
import com.mongodb.BasicDBObject;
import com.pojo.overallquality.*;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by guojing on 2016/8/4.
 */
@Service
public class OverallQualityItemService {

    private static final Logger logger = Logger.getLogger(OverallQualityItemService.class);

    private OverallQualityScoreSetDao overallQualityScoreSetDao = new OverallQualityScoreSetDao();

    private OverallQualityItemDao overallQualityItemDao = new OverallQualityItemDao();

    private ClassOverallQualityDao classOverallQualityDao = new ClassOverallQualityDao();

    private ClassOverallQualityScoreDao classOverallQualityScoreDao = new ClassOverallQualityScoreDao();

    private StudentOverallQualityDao studentOverallQualityDao = new StudentOverallQualityDao();

    private StuOverallQualityScoreDao stuOverallQualityScoreDao = new StuOverallQualityScoreDao();

    private OverallQualityGoodsDao overallQualityGoodsDao = new OverallQualityGoodsDao();

    private StuChangeGoodsDao stuChangeGoodsDao = new StuChangeGoodsDao();

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    public List<OverallQualityScoreDTO> getQualityScoreList(ObjectId schoolId) {
        List<OverallQualityScoreDTO> reList=new ArrayList<OverallQualityScoreDTO>();
        List<OverallQualityScoreSetEntry> list= overallQualityScoreSetDao.getQualityScoreEntryList(schoolId);
        for(OverallQualityScoreSetEntry entry : list){
            OverallQualityScoreDTO dto=new OverallQualityScoreDTO(entry);
            reList.add(dto);
        }
        return reList;
    }

    public ObjectId addOrEditQualityItem(String id, ObjectId schoolId, String itemName, String scoreId) {
        OverallQualityItemEntry entry=null;
        if(id!=null&&!"".equals(id)){
            entry=overallQualityItemDao.getQualityItem(new ObjectId(id));
            entry.setItemName(itemName);
            entry.setScoreSetId(new ObjectId(scoreId));
            overallQualityItemDao.updOverallQualityItemEntry(entry);
        }else {
            entry = new OverallQualityItemEntry(schoolId, itemName, new ObjectId(scoreId));
            overallQualityItemDao.addOverallQualityItemEntry(entry);
        }
        return entry.getID();
    }

    public int getQualityItemCount(String id, ObjectId schoolId, String itemName) {
        int count = overallQualityItemDao.getQualityItemCount(id, schoolId, itemName);
        if(id!=null&&!"".equals(id)&&count>=1){
            count=count-1;
        }
        return count;
    }

    public List<OverallQualityItemDTO> searchQualityItemList(ObjectId schoolId) {
        List<OverallQualityItemDTO> reList=new ArrayList<OverallQualityItemDTO>();
        List<OverallQualityItemEntry> list=overallQualityItemDao.searchQualityItemEntryList(schoolId);
        List<ObjectId> ssids=new ArrayList<ObjectId>();
        for(OverallQualityItemEntry entry : list){
            ssids.add(entry.getScoreSetId());
            OverallQualityItemDTO dto=new OverallQualityItemDTO(entry);
            reList.add(dto);
        }
        Map<ObjectId, OverallQualityScoreSetEntry> map=overallQualityScoreSetDao.getOverallQualityScoreMap(ssids);
        for(OverallQualityItemDTO dto : reList){
            OverallQualityScoreSetEntry scoreSetEntry=map.get(new ObjectId(dto.getScoreSetId()));
            if(scoreSetEntry!=null){
                dto.setScoreName(scoreSetEntry.getScoreName());
            }
        }
        return reList;
    }

    public void delQualityItem(ObjectId id) {
        overallQualityItemDao.delQualityItem(id);
    }

    public OverallQualityItemDTO getQualityItem(ObjectId id) {
        OverallQualityItemEntry entry=overallQualityItemDao.getQualityItem(id);
        OverallQualityItemDTO dto=new OverallQualityItemDTO(entry);
        OverallQualityScoreSetEntry scoreSetEntry=overallQualityScoreSetDao.getOverallQualityScoreById(entry.getScoreSetId());
        if(scoreSetEntry!=null){
            dto.setScoreName(scoreSetEntry.getScoreName());
        }
        return dto;
    }

    public void searchClassWeeklyQuality(ObjectId schoolId, String gradeId, String classId, String operate, String refDate, boolean suspend, Map<String, Object> model) {
        DateTimeUtils time=new DateTimeUtils();
        int opt=0;
        if("prev".equals(operate)){
            opt=-7;
        }else if("curr".equals(operate)){
            refDate=time.getCurrDate();
            opt=0;
        }else if("next".equals(operate)){
            opt=7;
        }
        if(suspend){
            opt=0;
        }
        try {
            String prevDayStr ="";
            if(opt==0){
                prevDayStr=refDate;
            }else {
                Date prevDay = time.getPrevDay(time.stringToDate(refDate, time.DATE_YYYY_MM_DD), opt);
                prevDayStr = time.getDateToStrTime(prevDay);
            }
            model.put("refDate", prevDayStr);
            Long[] dates = time.getWeekMinAndMaxTime(prevDayStr);
            long dateStart = dates[0];
            long dateEnd = dates[1];
            List<String> titleDates = new ArrayList<String>();
            String dateStartStr = time.getLongToStrTime(dateStart);
            String dateEndStr = time.getLongToStrTime(dateEnd);
            List<String> dateAreas = getUseTimeArea(dateStartStr, dateEndStr);
            for(String str: dateAreas){
                Date date = time.stringToDate(str, time.DATE_YYYY_MM_DD);
                String dateStr = time.getLongToStrTime(date.getTime(), time.CHINESE_DATE_MM_DD);
                titleDates.add(dateStr);
            }
            model.put("titleDates", titleDates);

            List<ClassOverallQualityEntry> list = classOverallQualityDao.searchClassOverallQualityEntryList(new ObjectId(gradeId), new ObjectId(classId), dateStart, dateEnd);
            Map<String, OverallQualityInfo> coqMap =null;
            Map<ObjectId, Map<String, OverallQualityInfo>> oqMap = new HashMap<ObjectId, Map<String, OverallQualityInfo>>();
            Map<String, ObjectId> coqIdMap =new HashMap<String, ObjectId>();
            for(ClassOverallQualityEntry entry:list){
                String createDate = time.getLongToStrTime(entry.getCreateDate());
                coqIdMap.put(createDate, entry.getID());
                if(entry.getOqiList()!=null){
                    for(OverallQualityInfo info:entry.getOqiList()){
                        coqMap=oqMap.get(info.getItemId());
                        if(coqMap==null){
                            coqMap = new HashMap<String, OverallQualityInfo>();
                        }
                        coqMap.put(createDate, info);
                        oqMap.put(info.getItemId(),coqMap);
                    }
                }
            }

            String currDate=time.getCurrDate();
            List<OverallQualityItemEntry> oqiList = overallQualityItemDao.searchQualityItemEntryList(schoolId);
            List<ClassOverallQualityDTO> resultList=new ArrayList<ClassOverallQualityDTO>();
            OverallQualityInfo oqi=null;
            for (OverallQualityItemEntry entry : oqiList) {
                ObjectId itemId = entry.getID();
                String name = entry.getItemName();
                ClassOverallQualityDTO dto=new ClassOverallQualityDTO();
                dto.setItemId(itemId.toString());
                dto.setItemName(name);
                dto.setGradeId(gradeId);
                dto.setClassId(classId);
                List<ClassOverallQualityInfo> coqiList=new ArrayList<ClassOverallQualityInfo>();
                for(String str: dateAreas) {
                    ObjectId coqId = coqIdMap.get(str);
                    ClassOverallQualityInfo info=new ClassOverallQualityInfo();
                    info.setFlagDate(str);
                    if(coqId!=null) {
                        info.setId(coqId.toString());
                    }else{
                        info.setId("");
                    }
                    coqMap=oqMap.get(itemId);
                    if(DateTimeUtils.compare_date2(str,currDate)<1) {
                        if (coqMap != null) {
                            oqi = coqMap.get(str);
                            if (oqi != null) {
                                if(oqi.getScore()==0){
                                    info.setFlag("1");
                                }else {
                                    info.setFlag("0");
                                }
                            }else{
                                info.setFlag("1");
                            }
                        }else{
                            info.setFlag("1");
                        }
                    }else{
                        info.setFlag("2");
                    }
                    coqiList.add(info);
                }
                dto.setCoqiList(coqiList);
                resultList.add(dto);
            }
            model.put("resultList", resultList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 产生一个date1到date2的时间list
     * @param date1
     * @param date2
     * @return
     */
    private List<String> getUseTimeArea(String date1,String date2){
        List<String> list2 = new ArrayList<String>();
        while (DateTimeUtils.compare_date2(date1,date2)<1&&list2.size()<5) {
            list2.add(date1);
            date1 = DateTimeUtils.getSpecifiedDayAfter(date1);
        }
        return list2;
    }


    public void editClassWeeklyQuality(String id, ObjectId schoolId, String itemId, String gradeId, String classId, String flagDate, String flag) {
        DateTimeUtils time=new DateTimeUtils();
        OverallQualityItemEntry overallQualityItem = overallQualityItemDao.getQualityItem(new ObjectId(itemId));
        OverallQualityScoreSetEntry scoreSetEntry=overallQualityScoreSetDao.getOverallQualityScoreById(overallQualityItem.getScoreSetId());
        long longDate = time.getStrToLongTime(flagDate,time.DATE_YYYY_MM_DD);
        List<OverallQualityInfo> coqiList=new ArrayList<OverallQualityInfo>();
        ClassOverallQualityEntry entry =null;
        OverallQualityInfo overallQualityInfo=null;
        if("".equals(id)){
            overallQualityInfo=new OverallQualityInfo(overallQualityItem.getID(), scoreSetEntry.getScoreNum());
            coqiList.add(overallQualityInfo);
            entry=new ClassOverallQualityEntry(schoolId, new ObjectId(gradeId), new ObjectId(classId), longDate, coqiList);
            classOverallQualityDao.addClassOverallQualityEntry(entry);
        }else{
            entry = classOverallQualityDao.searchClassOverallQualityEntry(new ObjectId(id));
            if(entry.getOqiList()!=null){
                coqiList=entry.getOqiList();
                for(OverallQualityInfo info: coqiList){
                    if(itemId.equals(info.getItemId().toString())){
                        if("1".equals(flag)) {
                            info.setScore(scoreSetEntry.getScoreNum());
                        }else{
                            info.setScore(0);
                        }
                        overallQualityInfo=info;
                        break;
                    }
                }
                if(overallQualityInfo==null){
                    overallQualityInfo=new OverallQualityInfo(overallQualityItem.getID(), scoreSetEntry.getScoreNum());
                    coqiList.add(overallQualityInfo);
                }
            }else{
                coqiList=new ArrayList<OverallQualityInfo>();
                overallQualityInfo=new OverallQualityInfo(overallQualityItem.getID(), scoreSetEntry.getScoreNum());
                coqiList.add(overallQualityInfo);
            }
            entry.setOqiList(coqiList);
            classOverallQualityDao.updateClassOverallQualityEntry(entry);
        }

        ClassOverallQualityScoreEntry scoreEntry=classOverallQualityScoreDao.searchClassOverallQualityScoreEntryByClassId(new ObjectId(classId));
        List<ClassOverallQualityScore> coqsList=new ArrayList<ClassOverallQualityScore>();
        ClassOverallQualityScore scoreInfo = null;
        if(scoreEntry!=null){
            int scoreInt=0;
            coqsList=scoreEntry.getCoqsList();
            for(ClassOverallQualityScore score: coqsList){
                if(scoreSetEntry.getID().equals(score.getScoreId())){
                    if("1".equals(flag)) {
                        scoreInt+=scoreSetEntry.getScoreNum();
                    }else{
                        scoreInt-=scoreSetEntry.getScoreNum();
                    }
                    score.setScore(score.getScore()+scoreInt);
                    scoreInfo=score;
                    break;
                }
            }
            if(scoreInfo==null){
                scoreInt=10 + scoreSetEntry.getScoreNum();
                scoreInfo = new ClassOverallQualityScore(scoreSetEntry.getID(), scoreSetEntry.getType(), scoreInt);
                coqsList.add(scoreInfo);
            }
            scoreEntry.setCoqsList(coqsList);
            scoreEntry.setTotalScore(scoreEntry.getTotalScore()+scoreInt);
            classOverallQualityScoreDao.updateClassOverallQualityScoreEntry(scoreEntry);
        }else{
            List<OverallQualityScoreSetEntry> scoreSetEntrys=overallQualityScoreSetDao.getQualityScoreEntryList(schoolId);
            int scoreInt=0;
            for(OverallQualityScoreSetEntry entry1: scoreSetEntrys){
                int baseScore=10;
                if(entry1.getID().equals(scoreSetEntry.getID())){
                    baseScore+=scoreSetEntry.getScoreNum();
                }
                scoreInt+=baseScore;
                scoreInfo = new ClassOverallQualityScore(entry1.getID(), entry1.getType(), baseScore);
                coqsList.add(scoreInfo);
            }
            scoreEntry=new ClassOverallQualityScoreEntry(schoolId, new ObjectId(gradeId), new ObjectId(classId), scoreInt, 1, coqsList);
            classOverallQualityScoreDao.addClassOverallQualityScoreEntry(scoreEntry);
        }

    }

    public void searchStuWeeklyQuality(ObjectId userId, String operate, String refDate, boolean suspend, Map<String, Object> model) {
        DateTimeUtils time=new DateTimeUtils();
        int opt=0;
        if("prev".equals(operate)){
            opt=-7;
        }else if("curr".equals(operate)){
            refDate=time.getCurrDate();
            opt=0;
        }else if("next".equals(operate)){
            opt=7;
        }
        if(suspend){
            opt=0;
        }
        try {
            String prevDayStr ="";
            if(opt==0){
                prevDayStr=refDate;
            }else {
                Date prevDay = time.getPrevDay(time.stringToDate(refDate, time.DATE_YYYY_MM_DD), opt);
                prevDayStr = time.getDateToStrTime(prevDay);
            }
            model.put("refDate", prevDayStr);
            Long[] dates = time.getWeekMinAndMaxTime(prevDayStr);
            long dateStart = dates[0];
            long dateEnd = dates[1];
            List<String> titleDates = new ArrayList<String>();
            String dateStartStr = time.getLongToStrTime(dateStart);
            String dateEndStr = time.getLongToStrTime(dateEnd);
            List<String> dateAreas = getUseTimeArea(dateStartStr, dateEndStr);
            for(String str: dateAreas){
                Date date = time.stringToDate(str, time.DATE_YYYY_MM_DD);
                String dateStr = time.getLongToStrTime(date.getTime(), time.CHINESE_DATE_MM_DD);
                titleDates.add(dateStr);
            }
            model.put("titleDates", titleDates);

            List<StuOverallQualityEntry> list =studentOverallQualityDao.searchStudentOverallQualityEntryList(userId, dateStart, dateEnd);
            Map<String, StuOverallQualityInfo> coqMap =null;
            Map<Integer, Map<String, StuOverallQualityInfo>> oqMap = new HashMap<Integer, Map<String, StuOverallQualityInfo>>();
            Map<String, ObjectId> coqIdMap =new HashMap<String, ObjectId>();
            for(StuOverallQualityEntry entry:list){
                String createDate = time.getLongToStrTime(entry.getCreateDate());
                coqIdMap.put(createDate, entry.getID());
                if(entry.getSoqsList()!=null){
                    for(StuOverallQualityInfo info:entry.getSoqsList()){
                        coqMap=oqMap.get(info.getType());
                        if(coqMap==null){
                            coqMap = new HashMap<String, StuOverallQualityInfo>();
                        }
                        coqMap.put(createDate, info);
                        oqMap.put(info.getType(),coqMap);
                    }
                }
            }

            String currDate=time.getCurrDate();
            List<KeyValue> typeList= CommonUtils.enumMapToList(ImageCurrencyType.getImageCurrencyTypeMap());

            List<StuOverallQualityDTO> resultList=new ArrayList<StuOverallQualityDTO>();

            StuOverallQualityInfo soqi=null;
            for (KeyValue value : typeList) {
                int key = value.getKey();
                String name = value.getValue();
                StuOverallQualityDTO dto=new StuOverallQualityDTO();
                dto.setCurrencyType(key);
                dto.setCurrencyName(name);
                List<ClassOverallQualityInfo> coqiList=new ArrayList<ClassOverallQualityInfo>();
                for(String str: dateAreas) {
                    ObjectId coqId = coqIdMap.get(str);
                    ClassOverallQualityInfo info=new ClassOverallQualityInfo();
                    info.setFlagDate(str);
                    if(coqId!=null) {
                        info.setId(coqId.toString());
                    }else{
                        info.setId("");
                    }
                    coqMap=oqMap.get(key);
                    if(DateTimeUtils.compare_date2(str,currDate)<1) {
                        if (coqMap != null) {
                            soqi = coqMap.get(str);
                            if (soqi != null) {
                                if(soqi.getScore()==0){
                                    info.setFlag("1");
                                }else {
                                    info.setFlag("0");
                                }
                            }else{
                                info.setFlag("1");
                            }
                        }else{
                            info.setFlag("1");
                        }
                    }else{
                        info.setFlag("2");
                    }
                    coqiList.add(info);
                }
                dto.setCoqiList(coqiList);
                resultList.add(dto);
            }
            model.put("resultList", resultList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editStuWeeklyQuality(String id, ObjectId schoolId, String stuId, int currencyType, String gradeId, String classId, String flagDate, String flag) {
        DateTimeUtils time=new DateTimeUtils();
        ImageCurrencyType imageCurrency=ImageCurrencyType.getImageCurrencyType(currencyType);
        long longDate = time.getStrToLongTime(flagDate,time.DATE_YYYY_MM_DD);
        List<StuOverallQualityInfo> coqiList=new ArrayList<StuOverallQualityInfo>();
        StuOverallQualityEntry entry =null;
        StuOverallQualityInfo stuOverallQualityInfo=null;
        if("".equals(id)){
            stuOverallQualityInfo=new StuOverallQualityInfo(imageCurrency.getState(), 1);
            coqiList.add(stuOverallQualityInfo);
            entry=new StuOverallQualityEntry(schoolId, new ObjectId(gradeId), new ObjectId(classId), new ObjectId(stuId), longDate, coqiList);
            studentOverallQualityDao.addStudentOverallQualityEntry(entry);
        }else{
            entry = studentOverallQualityDao.searchStudentOverallQualityEntry(new ObjectId(id));
            if(entry.getSoqsList()!=null){
                coqiList=entry.getSoqsList();
                for(StuOverallQualityInfo info: coqiList){
                    if(imageCurrency.getState()==info.getType()){
                        if("1".equals(flag)) {
                            info.setScore(1);
                        }else{
                            info.setScore(0);
                        }
                        stuOverallQualityInfo=info;
                        break;
                    }
                }
                if(stuOverallQualityInfo==null){
                    stuOverallQualityInfo=new StuOverallQualityInfo(imageCurrency.getState(), 1);
                    coqiList.add(stuOverallQualityInfo);
                }
            }else{
                coqiList=new ArrayList<StuOverallQualityInfo>();
                stuOverallQualityInfo=new StuOverallQualityInfo(imageCurrency.getState(), 1);
                coqiList.add(stuOverallQualityInfo);
            }
            entry.setSoqsList(coqiList);
            studentOverallQualityDao.updateStudentOverallQualityEntry(entry);
        }

        StuOverallQualityScoreEntry scoreEntry=stuOverallQualityScoreDao.searchStuOverallQualityScoreEntryByUserId(new ObjectId(stuId));
        List<StuOverallQualityScore> coqsList=new ArrayList<StuOverallQualityScore>();
        StuOverallQualityScore scoreInfo = null;
        if(scoreEntry!=null){
            coqsList=scoreEntry.getSoqsList();
            for(StuOverallQualityScore score: coqsList){
                if(imageCurrency.getState()==score.getType()){
                    if("1".equals(flag)) {
                        score.setScore(score.getScore()+1);
                    }else{
                        score.setScore(score.getScore()-1);
                    }
                    scoreInfo=score;
                    break;
                }
            }
            if(scoreInfo==null){
                scoreInfo = new StuOverallQualityScore(imageCurrency.getState(), 1);
                coqsList.add(scoreInfo);
            }
            scoreEntry.setSoqsList(coqsList);
            if("1".equals(flag)) {
                scoreEntry.setCurrencyTotal(scoreEntry.getCurrencyTotal()+1);
                scoreEntry.setHideCurrTotal(scoreEntry.getHideCurrTotal()+1);
            }else{
                scoreEntry.setCurrencyTotal(scoreEntry.getCurrencyTotal()-1);
                scoreEntry.setHideCurrTotal(scoreEntry.getHideCurrTotal()-1);
            }
            stuOverallQualityScoreDao.updateStuOverallQualityScoreEntry(scoreEntry);
        }else{
            scoreInfo = new StuOverallQualityScore(imageCurrency.getState(), 1);
            coqsList.add(scoreInfo);
            scoreEntry=new StuOverallQualityScoreEntry(schoolId, new ObjectId(gradeId), new ObjectId(classId), new ObjectId(stuId), 1, 1, coqsList);
            stuOverallQualityScoreDao.addStuOverallQualityScoreEntry(scoreEntry);
        }
    }

    public StuOverallQualityScoreDTO getStuOverallQualityScore(ObjectId userId) {
        StuOverallQualityScoreDTO dto=new StuOverallQualityScoreDTO();
        StuOverallQualityScoreEntry scoreEntry=stuOverallQualityScoreDao.searchStuOverallQualityScoreEntryByUserId(userId);
        if(scoreEntry!=null){
            dto.setTotalCount(scoreEntry.getCurrencyTotal());
            dto.setHideTotalCount(scoreEntry.getHideCurrTotal());
            for(StuOverallQualityScore score: scoreEntry.getSoqsList()){
                if(score.getType()==1){
                    dto.setNlxxCount(score.getScore());
                }
                if(score.getType()==2){
                    dto.setRqcyCount(score.getScore());
                }
                if(score.getType()==3){
                    dto.setGwzzCount(score.getScore());
                }
                if(score.getType()==4){
                    dto.setZjmlCount(score.getScore());
                }
                if(score.getType()==5){
                    dto.setYgkaCount(score.getScore());
                }
            }
        }
        return dto;
    }

    public void addOrEditGoods(String id, ObjectId schoolId, String goodsName, String picPath, String qnKey, int goodsPrice, int goodsStock, ObjectId userId) {
        OverallQualityGoodsEntry entry = null;
        if(null!=id&&!"".equals(id)){
            entry = overallQualityGoodsDao.searchOverallQualityGoodsEntry(new ObjectId(id));
            entry.setGoodsName(goodsName);
            if(!entry.getQnKey().equals(qnKey)) {
                try {
                    QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_IMAGE, entry.getQnKey());
                }catch (IllegalParamException e){
                    e.printStackTrace();
                }
                entry.setPicPath(picPath);
                entry.setQnKey(qnKey);
            }
            entry.setGoodsPrice(goodsPrice);
            entry.setGoodsStock(goodsStock);
            entry.setUpdateUserId(userId);
            entry.setUpdateDate(new Date().getTime());
            overallQualityGoodsDao.updateOverallQualityGoodsEntry(entry);
        }else{
            entry = new OverallQualityGoodsEntry(goodsName, picPath, qnKey, goodsPrice, goodsStock, schoolId, userId, new Date().getTime());
            overallQualityGoodsDao.addOverallQualityGoodsEntry(entry);
        }
    }

    public List<OverallQualityGoodsDTO> searchOverallQualityGoodsList(ObjectId schoolId, ObjectId userId, int userRole) {
        List<OverallQualityGoodsDTO> reList=new ArrayList<OverallQualityGoodsDTO>();
        int stuScoreTotal=0;
        boolean isStu=false;
        if(UserRole.isStudentOrParent(userRole)){
            StuOverallQualityScoreEntry scoreEntry=stuOverallQualityScoreDao.searchStuOverallQualityScoreEntryByUserId(userId);
            stuScoreTotal=scoreEntry.getCurrencyTotal();
            isStu=true;
        }
        List<OverallQualityGoodsEntry> list = overallQualityGoodsDao.searchOverallQualityGoodsEntryList(schoolId);
        for(OverallQualityGoodsEntry e: list){
            OverallQualityGoodsDTO dto=new OverallQualityGoodsDTO(e);
            dto.setIsStu(isStu);
            if(isStu==true){
                if(stuScoreTotal>=e.getGoodsPrice()&&e.getGoodsStock()>0){
                    dto.setIsChange(true);
                }else{
                    dto.setIsChange(false);
                }
            }
            reList.add(dto);
        }
        return reList;
    }

    public void delOverallQualityGoods(ObjectId id) {
        OverallQualityGoodsEntry entry = overallQualityGoodsDao.searchOverallQualityGoodsEntry(id);
        try {
            QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_IMAGE, entry.getQnKey());
        }catch (IllegalParamException e){
            e.printStackTrace();
        }
        overallQualityGoodsDao.deleteOverallQualityGoodsEntry(id);
    }

    public OverallQualityGoodsDTO getOverallQualityGoods(ObjectId id) {
        OverallQualityGoodsEntry entry=overallQualityGoodsDao.searchOverallQualityGoodsEntry(id);
        OverallQualityGoodsDTO dto=new OverallQualityGoodsDTO(entry);
        return dto;
    }

    public void studentChangeGoods(ObjectId id, ObjectId userId, Map<String, Object> model) {
        String mgs="已提交,等待老师审核!";
        StuOverallQualityScoreEntry scoreEntry=stuOverallQualityScoreDao.searchStuOverallQualityScoreEntryByUserId(userId);
        int stuScoreTotal=scoreEntry.getCurrencyTotal();
        OverallQualityGoodsEntry entry=overallQualityGoodsDao.searchOverallQualityGoodsEntry(id);
        if(entry.getGoodsStock()<=0){
            mgs=entry.getGoodsName()+"暂无库存!";
            model.put("result", mgs);
            model.put("flag", "false");
        }else if(stuScoreTotal<entry.getGoodsPrice()){
            mgs="可用形象币不足!";
            model.put("result", mgs);
            model.put("flag", "false");
        }else {
            int currencyTotal=scoreEntry.getCurrencyTotal() - entry.getGoodsPrice();
            scoreEntry.setCurrencyTotal(currencyTotal);
            stuOverallQualityScoreDao.updateStuOverallQualityScoreEntry(scoreEntry);
            StuChangeGoodsEntry scgEntry = new StuChangeGoodsEntry(scoreEntry.getSchoolId(),
                                                                    scoreEntry.getGradeId(),
                                                                    scoreEntry.getClassId(),
                                                                    userId,
                                                                    entry.getID(),
                                                                    userId,
                                                                    new Date().getTime(),
                                                                    ChangeState.AUDIT
                                                                    );
            stuChangeGoodsDao.addStuChangeGoodsEntry(scgEntry);
            model.put("result", mgs);
            model.put("flag", "true");
            model.put("totalCount", currencyTotal);
        }

    }

    public List<StuChangeGoodsDTO> searchStuChangeGoodsList(ObjectId gradeId, ObjectId classId, ChangeState state) {
        List<StuChangeGoodsDTO> reList = new ArrayList<StuChangeGoodsDTO>();
        List<StuChangeGoodsEntry> list = stuChangeGoodsDao.searchStuChangeGoodsEntryByClassId(classId, state);
        List<ObjectId> uids=new ArrayList<ObjectId>();
        List<ObjectId> gids=new ArrayList<ObjectId>();
        for(StuChangeGoodsEntry e:list){
            uids.add(e.getUserId());
            gids.add(e.getGoodsId());
        }
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(uids, new BasicDBObject("nm",1));
        Map<ObjectId, OverallQualityGoodsEntry> goodsMap = overallQualityGoodsDao.getOverallQualityGoodsEntryMap(gids, new BasicDBObject("gn",1));
        StuChangeGoodsDTO dto=null;
        UserEntry userEntry=null;
        OverallQualityGoodsEntry goodsEntry=null;
        for(StuChangeGoodsEntry e:list){
            dto=new StuChangeGoodsDTO(e);
            userEntry=userMap.get(e.getUserId());
            goodsEntry=goodsMap.get(e.getGoodsId());
            if(userEntry!=null){
                dto.setUserName(userEntry.getUserName());
            }
            if(goodsEntry!=null){
                dto.setGoodsName(goodsEntry.getGoodsName());
            }
            reList.add(dto);
        }
        return reList;
    }

    public void batchHandleStuChangeGoods(List<String> cids, ObjectId userId, String refuseCon, String operate, Map<String,Object>  model) {
        ChangeState state=null;
        List<ObjectId> ids=new ArrayList<ObjectId>();
        for(String id:cids){
            ids.add(new ObjectId(id));
        }
        if("refuse".equals(operate)) {
            state=ChangeState.REFUSE;
        }else if("sure".equals(operate)) {
            state=ChangeState.PASS;
        }
        List<StuChangeGoodsEntry> list = stuChangeGoodsDao.searchStuChangeGoodsEntryByIds(ids);
        List<ObjectId> uids=new ArrayList<ObjectId>();
        List<ObjectId> gids=new ArrayList<ObjectId>();
        Map<ObjectId, Integer> buyCountMap = new HashMap<ObjectId, Integer>();
        for(StuChangeGoodsEntry e:list){
            uids.add(e.getUserId());
            gids.add(e.getGoodsId());
            Integer buyCount = buyCountMap.get(e.getGoodsId());
            if(buyCount==null||buyCount==0){
                buyCount=0;
            }
            buyCountMap.put(e.getGoodsId(),buyCount+1);
        }
        Map<ObjectId, OverallQualityGoodsEntry> goodsMap = overallQualityGoodsDao.getOverallQualityGoodsEntryMap(gids, new BasicDBObject("gn",1).append("gp",1).append("gs",1));
        Map<ObjectId, StuOverallQualityScoreEntry> scoreMap=stuOverallQualityScoreDao.searchStuOverallQualityScoreEntryMap(uids);
        OverallQualityGoodsEntry goodsEntry=null;
        StuOverallQualityScoreEntry scoreEntry=null;
        List<OverallQualityGoodsEntry> goodsList = new ArrayList<OverallQualityGoodsEntry>();
        boolean flag=true;
        String goodsName="";
        List<ObjectId> goodsIds=new ArrayList<ObjectId>();
        for(StuChangeGoodsEntry e:list){
            goodsEntry=goodsMap.get(e.getGoodsId());
            if(goodsEntry!=null){
                if("refuse".equals(operate)) {
                    scoreEntry=scoreMap.get(e.getUserId());
                    scoreEntry.setCurrencyTotal(scoreEntry.getCurrencyTotal()+goodsEntry.getGoodsPrice());
                    stuOverallQualityScoreDao.updateStuOverallQualityScoreEntry(scoreEntry);
                }else if("sure".equals(operate)) {
                    int buyCount = buyCountMap.get(e.getGoodsId());
                    if(buyCount>goodsEntry.getGoodsStock()){
                        goodsName = goodsEntry.getGoodsName();
                        flag=false;
                        break;
                    }else{
                        if(!goodsIds.contains(e.getGoodsId())) {
                            goodsIds.add(e.getGoodsId());
                            goodsEntry.setGoodsStock(goodsEntry.getGoodsStock() - buyCount);
                            goodsEntry.setSoldCount(buyCount);
                            goodsList.add(goodsEntry);
                        }
                    }
                }
            }
        }
        if(flag) {
            if(goodsList.size()>0){
                for(OverallQualityGoodsEntry entry:goodsList){
                    overallQualityGoodsDao.updateOverallQualityGoodsEntry(entry);
                }
            }
            stuChangeGoodsDao.batchHandleStuChangeGoods(ids, userId, refuseCon, state.getState(), new Date().getTime());
            model.put("result", "true");
        }else{
            model.put("result", "false");
            model.put("mgs", goodsName+"库存不足!");
        }
    }

    public List<StuChangeGoodsDTO> changeGoodsHistory(ObjectId schoolId, ObjectId userId, int userRole) {
        List<StuChangeGoodsDTO> reList = new ArrayList<StuChangeGoodsDTO>();
        List<StuChangeGoodsEntry> list = stuChangeGoodsDao.searchStuChangeGoodsEntryByParam(schoolId, userId, userRole);
        List<ObjectId> uids=new ArrayList<ObjectId>();
        List<ObjectId> gids=new ArrayList<ObjectId>();
        for(StuChangeGoodsEntry e:list){
            uids.add(e.getUserId());
            gids.add(e.getGoodsId());
        }
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(uids, new BasicDBObject("nm",1));
        Map<ObjectId, OverallQualityGoodsEntry> goodsMap = overallQualityGoodsDao.getOverallQualityGoodsEntryMap(gids, new BasicDBObject("gn",1).append("ph",1));
        StuChangeGoodsDTO dto=null;
        UserEntry userEntry=null;
        OverallQualityGoodsEntry goodsEntry=null;
        for(StuChangeGoodsEntry e:list){
            dto=new StuChangeGoodsDTO(e);
            if(UserRole.isStudentOrParent(userRole)){
                dto.setIsStu(true);
            }else{
                dto.setIsStu(false);
                userEntry=userMap.get(e.getUserId());
                if(userEntry!=null){
                    dto.setUserName(userEntry.getUserName());
                }
            }
            goodsEntry=goodsMap.get(e.getGoodsId());
            if(goodsEntry!=null){
                dto.setGoodsName(goodsEntry.getGoodsName());
                dto.setPicPath(goodsEntry.getPicPath());
            }
            reList.add(dto);
        }
        return reList;
    }

    public void searchClassBuLuoInfo(ObjectId schoolId, String gradeId, String classId, Map<String, Object> model) {
        if(!"".equals(classId)){
            ClassOverallQualityScoreEntry entry=classOverallQualityScoreDao.searchClassOverallQualityScoreEntryByClassId(new ObjectId(classId));
            if(entry==null){
                List<OverallQualityScoreSetEntry> scoreSetEntrys=overallQualityScoreSetDao.getQualityScoreEntryList(schoolId);
                List<ClassOverallQualityScore> coqsList=new ArrayList<ClassOverallQualityScore>();
                ClassOverallQualityScore scoreInfo = null;
                int scoreInt=0;
                for(OverallQualityScoreSetEntry entry1: scoreSetEntrys){
                    int baseScore=10;
                    scoreInt+=baseScore;
                    scoreInfo = new ClassOverallQualityScore(entry1.getID(), entry1.getType(), baseScore);
                    coqsList.add(scoreInfo);
                }
                entry=new ClassOverallQualityScoreEntry(schoolId, new ObjectId(gradeId), new ObjectId(classId), scoreInt, 1, coqsList);
                classOverallQualityScoreDao.addClassOverallQualityScoreEntry(entry);
            }
            ClassOverallQualityScoreDTO dto = new ClassOverallQualityScoreDTO(entry);
            model.put("dto",dto);

            Map<ObjectId, ClassEntry> classMap = classService.getClassEntryMapByGradeId(new ObjectId(gradeId), new BasicDBObject("nm",1));
            List<ClassOverallQualityScoreEntry> list=classOverallQualityScoreDao.searchClassOverallQualityScoreEntryByGradeId(new ObjectId(gradeId),0,10);
            List<ClassEntry> classList =new ArrayList<ClassEntry>();
            if(list.size()<10&&classMap.size()>list.size()) {
                List<ObjectId> cids = new ArrayList<ObjectId>();
                for (ClassOverallQualityScoreEntry e : list) {
                    cids.add(e.getClassId());
                }
                classList = classService.getClassEntryByParam(new ObjectId(gradeId), cids, new BasicDBObject("nm", 1));
            }
            List<ClassOverallQualityScoreDTO> dtoList = new ArrayList<ClassOverallQualityScoreDTO>();
            ClassOverallQualityScoreDTO temp = null;
            ClassEntry classEntry = null;
            int number=1;
            int i=0;
            for(ClassOverallQualityScoreEntry item: list){
                i++;
                temp = new ClassOverallQualityScoreDTO(item);
                classEntry = classMap.get(item.getClassId());
                temp.setClassName(classEntry.getName());
                temp.setNumber(i);
                if(classId.equals(temp.getClassId())){
                    number=i;
                    temp.setOurClass(true);
                }else{
                    temp.setOurClass(false);
                }
                if(dtoList.size()<10) {
                    dtoList.add(temp);
                }
            }
            if(dtoList.size()<10&&classMap.size()>list.size()){
                for(ClassEntry ce : classList){
                    i++;
                    temp = new ClassOverallQualityScoreDTO();
                    temp.setClassId(ce.getID().toString());
                    temp.setClassName(ce.getName());
                    temp.setNumber(i);
                    if(classId.equals(temp.getClassId())){
                        number=i;
                        temp.setOurClass(true);
                    }else{
                        temp.setOurClass(false);
                    }
                    if(dtoList.size()<10) {
                        dtoList.add(temp);
                    }
                }
            }
            model.put("number",number);
            model.put("dtoList",dtoList);
        }
    }

    public void searchClassBuLuoInfoByParams(ObjectId schoolId, String gradeId, String classId, Map<String, Object> model) {
        ClassOverallQualityScoreEntry entry=classOverallQualityScoreDao.searchClassOverallQualityScoreEntryByClassId(new ObjectId(classId));
        if(entry==null){
            List<OverallQualityScoreSetEntry> scoreSetEntrys=overallQualityScoreSetDao.getQualityScoreEntryList(schoolId);
            List<ClassOverallQualityScore> coqsList=new ArrayList<ClassOverallQualityScore>();
            ClassOverallQualityScore scoreInfo = null;
            int scoreInt=0;
            for(OverallQualityScoreSetEntry entry1: scoreSetEntrys){
                int baseScore=10;
                scoreInt+=baseScore;
                scoreInfo = new ClassOverallQualityScore(entry1.getID(), entry1.getType(), baseScore);
                coqsList.add(scoreInfo);
            }
            entry=new ClassOverallQualityScoreEntry(schoolId, new ObjectId(gradeId), new ObjectId(classId), scoreInt, 1, coqsList);
            classOverallQualityScoreDao.addClassOverallQualityScoreEntry(entry);
        }
        ClassOverallQualityScoreDTO dto = new ClassOverallQualityScoreDTO(entry);
        model.put("dto", dto);
        for (ClassOverallQualityScoreInfo info : dto.getCoqsiList()) {
            PropType type = PropType.getPropType(info.getType());
            model.put(info.getType()+"Score", info.getScore());
            if(info.getScore()>=type.getState()){
                model.put(info.getType(), true);
            }else{
                model.put(info.getType(), false);
            }
        }
    }

    public void propChange(String gradeId, String classId, String type, Map<String, Object> model) {
        ClassOverallQualityScoreEntry entry=classOverallQualityScoreDao.searchClassOverallQualityScoreEntryByClassId(new ObjectId(classId));
        String msg="兑换成功!";
        boolean flag=true;
        if("3t1".equals(type)){
            if(entry.getVillagerCount()>=3){
                entry.setVillagerCount(entry.getVillagerCount()-3);
                entry.setCastleCount(entry.getCastleCount()+1);
            }else {
                flag=false;
                msg="村民数量不足,不能兑换城堡!";
            }
        }else if("2t3".equals(type)){
            if(entry.getSoldiersCount()>=2){
                entry.setSoldiersCount(entry.getSoldiersCount()-2);
                entry.setVillagerCount(entry.getVillagerCount()+3);
            }else {
                flag=false;
                msg="士兵数量不足,不能兑换村民!";
            }
        }else if("1t2".equals(type)){
            if(entry.getCastleCount()>=1){
                entry.setCastleCount(entry.getCastleCount()-1);
                entry.setSoldiersCount(entry.getSoldiersCount()+2);
            }else {
                flag=false;
                msg="城堡数量不足,不能兑换士兵!";
            }
        }else{
            if(entry.getCoqsList()!=null){
                for(ClassOverallQualityScore item: entry.getCoqsList()){
                    if(type.equals(item.getType())){
                        PropType propType = PropType.getPropType(type);
                        if(item.getScore()>=propType.getState()){
                            item.setScore(item.getScore()-propType.getState());
                            if("ws".equals(type)){
                                entry.setCastleCount(entry.getCastleCount()+1);
                            }
                            if("hg".equals(type)){
                                entry.setVillagerCount(entry.getVillagerCount()+1);
                            }
                            if("td".equals(type)){
                                entry.setSoldiersCount(entry.getSoldiersCount()+1);
                            }
                        }else{
                            flag=false;
                            String zmsg="";
                            if("ws".equals(type)){
                                zmsg="卫生分";
                            }
                            if("hg".equals(type)){
                                zmsg="行规分";
                            }
                            if("td".equals(type)){
                                zmsg="体锻分";
                            }
                            msg=zmsg+"不足,不能兑换"+propType.getDes()+"!";
                        }
                    }
                }
            }
        }
        if(flag){
            classOverallQualityScoreDao.updateClassOverallQualityScoreEntry(entry);
        }
        model.put("flag",flag);
        model.put("msg",msg);
    }
}
