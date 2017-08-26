package com.fulaan.operation.service;

import com.db.operation.AppCommentDao;
import com.db.operation.AppOperationDao;
import com.db.operation.AppRecordDao;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.AppRecordDTO;
import com.pojo.operation.AppCommentEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.operation.AppRecordEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by James on 2017/8/25.
 */
@Service
public class AppCommentService {

    private AppCommentDao appCommentDao = new AppCommentDao();
    private AppOperationDao appOperationDao = new AppOperationDao();
    private AppRecordDao appRecordDao = new AppRecordDao();
    /**
     * 发布作业
     * @return
     */
    public String addCommentEntry(AppCommentDTO dto,List<String> comList){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        dto.setMonth(month);
        AppCommentEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        en.setDateTime(zero);
        //todo  查找社区
        String id = appCommentDao.addEntry(en);
        return id;
    }

    /**
     * 查询当前老师今天发布的作业
     *
     */
    public List<AppCommentDTO> selectListByTeacherId(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<AppCommentEntry> entries = appCommentDao.getEntryListByUserId(userId,zero);
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                int num = appOperationDao.getEntryCount(en.getID());
                dto.setNumber(num);
                dtos.add(dto);
            }
        }
        return dtos;
    }
    /**
     * 查找当前家长收到的作业
     *
     */
    public List<AppCommentDTO> selectListFromParent(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
       // List<AppCommentEntry> ids = appCommentDao.getEntryListByUserId(userId,zero);

        //todo  查找出当前家长的孩子缩收到的所有作业
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
       /* if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                int num = appOperationDao.getEntryCount(en.getID());
                dto.setNumber(num);
            }
        }*/
        return dtos;
    }

    /**
     * 查询当前作业签到名单
     */
    public List<AppRecordDTO> selectRecordList(ObjectId id,int type){
        List<AppRecordEntry> entries = appRecordDao.getEntryListByParentId(id,type);
        List<AppRecordDTO> dtos = new ArrayList<AppRecordDTO>();
        if(entries.size()>0){
            for(AppRecordEntry en : entries){
                dtos.add(new AppRecordDTO(en));
            }
        }
       return dtos;
    }

    /**
     * 按月查找用户发放作业情况
     */
    public List<String> selectResultList(int month,ObjectId userId){
        List<AppCommentEntry> entries = appCommentDao.selectResultList(userId, month);
        List<String> dtos = new ArrayList<String>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                dtos.add(dto.getDateTime());
            }
        }
        return dtos;
    }
    /**
     * 根据作业id查找当前评论列表
     */
    public List<AppOperationDTO> getOperationList(ObjectId id,ObjectId userId){
        AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppOperationEntry> entries = null;
        if(entry.getAdminId() != null && entry.getAdminId().equals(userId)){
            entries= appOperationDao.getEntryListByParentId(id);
        }else{
            entries= appOperationDao.getEntryListByUserId(userId, id);
        }
        List<AppOperationDTO> dtos = new ArrayList<AppOperationDTO>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry en : entries){
                AppOperationDTO dto = new AppOperationDTO(en);
                dtos.add(dto);
            }
        }
        return dtos;
    }

    /**
     * 发布评论
     * @return
     */
    public String addOperationEntry(AppOperationDTO dto){
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        return id;
    }
}
