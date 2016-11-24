package com.fulaan.customizedpage.service;

import com.db.customized.RecordVideoDao;
import com.pojo.customized.RecordVideoDTO;
import com.pojo.customized.RecordVideoEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/8/23.
 */
@Service
public class RecordVideoService {

    private RecordVideoDao recordVideoDao = new RecordVideoDao();

    public ObjectId saveOrUpdate(RecordVideoDTO recordVideoDTO){
        return  recordVideoDao.add(recordVideoDTO.exportEntry());
    }

    public List<RecordVideoDTO> getRecordVideoList(String date,String app,int page,int pageSize){
        List<RecordVideoEntry> recordVideoEntries=recordVideoDao.getRecordVideoList(date,app,(page-1)*pageSize,pageSize);
        List<RecordVideoDTO> recordVideoDTOs=new ArrayList<RecordVideoDTO>();
        if(null!=recordVideoEntries){
            for(RecordVideoEntry recordVideoEntry:recordVideoEntries){
                RecordVideoDTO recordVideoDTO=new RecordVideoDTO(recordVideoEntry);
                recordVideoDTOs.add(recordVideoDTO);
            }
        }
        return recordVideoDTOs;
    }

    public void removeVideo(ObjectId Id){
        recordVideoDao.logicRemove(Id);
    }
    public int  countRecordVideoList(String date,String app){
        return recordVideoDao.countRecordVideoList(date,app);
    }
}
