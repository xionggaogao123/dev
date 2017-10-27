package com.fulaan.appmarket.service;

import com.db.appmarket.AppDetailCommentDao;
import com.db.appmarket.AppDetailDao;
import com.db.appmarket.AppDetailStarStatisticDao;
import com.fulaan.appmarket.dto.AppDetailCommentDTO;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.appmarket.dto.AppDetailStarStatisticDTO;
import com.fulaan.user.service.UserService;
import com.pojo.appmarket.AppDetailCommentEntry;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.appmarket.AppDetailStarStatisticEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by scott on 2017/10/10.
 */
@Service
public class AppMarketService {

    private AppDetailDao appDetailDao=new AppDetailDao();

    private AppDetailCommentDao appDetailCommentDao=new AppDetailCommentDao();

    private AppDetailStarStatisticDao appDetailStarStatisticDao=new AppDetailStarStatisticDao();

    @Autowired
    private UserService userService;


    public void saveAppDetail(AppDetailDTO appDetailDTO,ObjectId userId){
        appDetailDao.saveAppDetailEntry(appDetailDTO.buildEntry(userId));
    }


    public AppDetailDTO getApp(ObjectId appId){
        AppDetailEntry appDetailEntry=appDetailDao.findEntryById(appId);
        if(null!=appDetailEntry){
            return new AppDetailDTO(appDetailEntry);
        }else{
            return null;
        }
    }

    public List<AppDetailDTO> getAppByCondition(String regular){
        List<AppDetailDTO> detailDTOs=new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> appDetailEntries=appDetailDao.getAppByCondition(regular);
        for(AppDetailEntry entry:appDetailEntries){
            detailDTOs.add(new AppDetailDTO(entry));
        }
        return detailDTOs;
    }

    public List<AppDetailDTO> getAllAppDetails(){
        List<AppDetailDTO> detailDTOs=new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> appDetailEntries=appDetailDao.getEntries();
        for(AppDetailEntry entry:appDetailEntries){
            detailDTOs.add(new AppDetailDTO(entry));
        }
        return detailDTOs;
    }

    public Map<String,Object> getCommentList(ObjectId appDetailId,int page,int pageSize){
        Map<String,Object> retMap=new HashMap<String,Object>();
        List<AppDetailCommentDTO> commentDTOs=new ArrayList<AppDetailCommentDTO>();
        List<AppDetailCommentEntry> commentEntries=appDetailCommentDao.getAppEntries(appDetailId, page, pageSize);
        Set<ObjectId> userIds=new HashSet<ObjectId>();
        for(AppDetailCommentEntry commentEntry:commentEntries){
            userIds.add(commentEntry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(AppDetailCommentEntry commentEntry:commentEntries){
            AppDetailCommentDTO commentDTO=new AppDetailCommentDTO(commentEntry);
            UserEntry userEntry=userEntryMap.get(commentEntry.getUserId());
            if(null!=userEntry){
                commentDTO.setUserName(userEntry.getUserName());
            }
            commentDTOs.add(commentDTO);
        }
        int count=appDetailCommentDao.countAppEntries(appDetailId);
        List<AppDetailStarStatisticDTO> initDatas=generateInitData();
        Map<Integer,Integer> map=appDetailStarStatisticDao.getMapEntry(appDetailId);
        for(AppDetailStarStatisticDTO dto:initDatas){
            int star=dto.getStar();
            if(null!=map.get(star)){
                dto.setCount(map.get(star));
            }
        }
        int total=0;
        int totalStar=0;
        double avgStar=0D;
        for(AppDetailStarStatisticDTO dto:initDatas){
            total+=dto.getCount();
            totalStar+=dto.getStar()*dto.getCount();
        }
        if(total!=0){
            avgStar=divide((double)totalStar,(double)total,1);
            for(AppDetailStarStatisticDTO dto:initDatas){
                double p=divide(mul((double)dto.getCount(),100D),(double)total,1);
                dto.setPercent(p);
            }
        }
        Collections.sort(initDatas, new Comparator<AppDetailStarStatisticDTO>() {
            @Override
            public int compare(AppDetailStarStatisticDTO o1, AppDetailStarStatisticDTO o2) {
                return o2.getStar()-o1.getStar();
            }
        });
        retMap.put("avgStar",avgStar);
        retMap.put("stars",initDatas);
        retMap.put("list",commentDTOs);
        retMap.put("count",count);
        retMap.put("page",page);
        retMap.put("pageSize",pageSize);
        return retMap;
    }

    public static Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static Double divide(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public List<AppDetailStarStatisticDTO> generateInitData(){
        List<AppDetailStarStatisticDTO>
                dtos=new ArrayList<AppDetailStarStatisticDTO>();
        dtos.add(new AppDetailStarStatisticDTO(Constant.ONE,0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.TWO,0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.THREE,0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.FOUR,0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.FIVE,0));
        return dtos;
    }

    public void saveAppDetailComment(AppDetailCommentDTO commentDTO, ObjectId userId){
        commentDTO.setUserId(userId.toString());
        appDetailCommentDao.saveAppDetailComment(commentDTO.buildEntry());
        AppDetailStarStatisticEntry
                entry=appDetailStarStatisticDao.getEntryByStarAndDetailId(
                        new ObjectId(commentDTO.getAppDetailId()),commentDTO.getStar()
        );
        if(null!=entry){
            appDetailStarStatisticDao.updateAppDetailStar(new ObjectId(commentDTO.getAppDetailId()),commentDTO.getStar(),
                    Constant.ONE);
        }else{
            AppDetailStarStatisticEntry starStatisticEntry=new AppDetailStarStatisticEntry(
                    new ObjectId(commentDTO.getAppDetailId()),commentDTO.getStar(),
                    Constant.ONE
            );
            appDetailStarStatisticDao.saveAppDetailStarStatisticEntry(starStatisticEntry);
        }
    }
}
