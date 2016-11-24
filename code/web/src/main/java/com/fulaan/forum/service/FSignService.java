package com.fulaan.forum.service;

import com.db.forum.FSignDao;
import com.pojo.forum.FSignEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangkaidong on 2016/5/31.
 */
@Service
public class FSignService {
    private FSignDao fSignDao = new FSignDao();

    /**
     * 查询是否签到
     *
     * @param userId
     * */
    public boolean isSign(ObjectId userId){
        FSignEntry fSignEntry = fSignDao.findSignByUserId(userId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        if(dateFormat(fSignEntry.getDate()).equals(dateStr)){
            return true;
        } else {
            return false;
        }
    }


    /**
     * 签到
     *
     * @param userId
     * */
    public void sign(ObjectId userId){
        FSignEntry fSignEntry = fSignDao.findSignByUserId(userId);
        FSignEntry update = new FSignEntry(userId);
        if(fSignEntry != null){//更新
            update.setID(fSignEntry.getID());
            String dateStr = dateFormat(fSignEntry.getDate());
            if(dateStr.equals(getBeforeDay(dateStr))){//判断是否连续签到
                update.setCount(fSignEntry.getCount() + 1);
            } else {
                update.setCount(1);
            }
        } else {//新增
            update.setCount(1);
        }

        update.setDate(dateConvert(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        fSignDao.saveOrUpdate(update);

    }


    private String dateFormat(long date){
        Date dt = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(dt);
        return dateStr;
    }

    private long dateConvert(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = null;
        try {
            dt = format.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return dt.getTime();
    }

    private String getBeforeDay(String specifiedDay) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = new Date(dateConvert(specifiedDay));
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = dateFormat(c.getTime().getTime());

        return dayBefore;
    }


}
