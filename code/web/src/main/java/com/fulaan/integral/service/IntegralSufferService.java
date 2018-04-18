package com.fulaan.integral.service;

import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.integral.IntegralRecordDao;
import com.db.integral.IntegralSufferDao;
import com.pojo.integral.IntegralRecordEntry;
import com.pojo.integral.IntegralSufferEntry;
import com.pojo.integral.IntegralType;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-16.
 */
@Service
public class IntegralSufferService {

    private IntegralRecordDao integralRecordDao =  new IntegralRecordDao();

    private IntegralSufferDao integralSufferDao = new IntegralSufferDao();

    private MemberDao memberDao = new MemberDao();

    private GroupDao groupDao = new GroupDao();


    /**
     * 添加积分(逻辑)
     */
    public int addIntegral(ObjectId userId,IntegralType integralType,int role,int type){//   role 1 老师   2 家长   3 不明   4 不限  type(适用规则) : 1 老师   2  家长
        long current = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        List<IntegralRecordEntry> entries =  integralRecordDao.getEntryListByUserId(userId, zero);
        int sort = 0;//排序
        int allScore = 0;//总分
        int score = 0;//此次得分

        //类型计算（得出未获得的模块）
        //1.得出可得分模块
        List<String> stringList = IntegralType.getDesList();
        //2.得出已得分
        List<String> stringList1 = new ArrayList<String>();//已计算模块
        for(IntegralRecordEntry entry : entries){
            if(entry.getSort()>=sort){
                sort=entry.getSort()+1;
            }
            allScore += entry.getScore();

            String module = entry.getModule();
            if(stringList.contains(module) && !stringList1.contains(module)){//初次计算
                stringList1.add(module);
            }
        }
        if(allScore>IntegralType.getSnameAllType()){//总分超限警告  修复
            this.updateEntry(userId,IntegralType.getSnameAllType());
            return score;
        }
        //3.得出未得分模块
        stringList.removeAll(stringList1);
        if(!stringList.contains(integralType.getDes())){//不在未得分模块直接返回
           return score;
        }
        int log = role;
        if(role==3 || role==4){
            List<ObjectId> oids = getMyRoleList(userId);
            if(oids.size()>0){
                role=1;
            }else{
                role=2;
            }
        }
        if(log != 4 && role!=type){//不适用
            return score;
        }
        IntegralRecordEntry integralRecordEntry =  new IntegralRecordEntry(userId,0,integralType.getDes(),zero,current,sort);
        if(role==1){//教师
            if(sort==0){//第一次  加  50 分
                integralRecordEntry.setScore(IntegralType.all.getSname());
            }else if(sort==IntegralType.getBigType()){//最后一次 多加  50分
                integralRecordEntry.setScore(integralType.getSname()+IntegralType.con.getSname());
            }else{//其中
                integralRecordEntry.setScore(integralType.getSname());
            }
            score  = integralRecordEntry.getScore();
            //总分计算
            int bigScore = IntegralType.getSnameAllType();
            //总分超过
            if(allScore>bigScore){
                //修正
                this.updateEntry(userId,bigScore);
                //置零
                score = 0;
            }else if(allScore==bigScore){
                //置零
                score = 0;
            }else{
                //保存
                this.addEntry(integralRecordEntry,userId);

            }
        }else{//家长
            if(sort==0){//第一次  加  50 分
                integralRecordEntry.setScore(IntegralType.all.getEname());
                integralRecordEntry.setSort(Constant.ZERO);
            }else if(sort==IntegralType.getBigType()){//最后一次 多加  50分
                integralRecordEntry.setSort(integralType.getEname() + IntegralType.con.getEname());
            }else{//其中
                integralRecordEntry.setScore(integralType.getEname());
            }
            score  = integralRecordEntry.getScore();
            int bigScore = IntegralType.getEnameAllType();
            if(allScore>bigScore){
                //修正
                this.updateEntry(userId,bigScore);
                //置零
                score = 0;
            }else if(allScore==bigScore){
                //置零
                score = 0;
            }else{
                //保存
                this.addEntry(integralRecordEntry,userId);
            }
        }
        return score;
    }

    /**
     * 保存积分及经验值
     */
    public void addEntry(IntegralRecordEntry integralRecordEntry,ObjectId userId){
        integralRecordDao.addEntry(integralRecordEntry);
        IntegralSufferEntry integralSufferEntry = integralSufferDao.getEntry(userId);
        int score = integralRecordEntry.getScore();
        if(integralSufferEntry!=null){
            integralSufferEntry.setScore(integralSufferEntry.getScore() + score);
            integralSufferEntry.setSuffer(integralSufferEntry.getSuffer()+score);
            if(score==50){
                integralSufferEntry.setSign(integralSufferEntry.getSign() + 1);
            }
            integralSufferDao.updEntry(integralSufferEntry);
        }else{
            IntegralSufferEntry integralSufferEntry1 = new IntegralSufferEntry(userId,score,score,Constant.ONE,Constant.ZERO,Constant.ZERO);
            integralSufferDao.addEntry(integralSufferEntry1);
        }
    }


    /**
     * 修正积分及经验值
     */
    public void updateEntry(ObjectId userId,int bigScore){
        IntegralSufferEntry integralSufferEntry = integralSufferDao.getEntry(userId);
        int oldScore = integralSufferEntry.getOldScore();
        if(integralSufferEntry!=null){
            if(oldScore + bigScore == integralSufferEntry.getScore()){//分数最大值已获得不处理

            }else{//分数不匹配
                //加最大分
                integralSufferEntry.setScore(integralSufferEntry.getOldScore() + bigScore);
                integralSufferEntry.setSuffer(integralSufferEntry.getOldSuffer()+bigScore);
                integralSufferDao.updEntry(integralSufferEntry);
            }
        }else{//记录不存在（多并发异常）
            IntegralSufferEntry integralSufferEntry1 = new IntegralSufferEntry(userId,bigScore,bigScore,Constant.ONE,Constant.ZERO,Constant.ZERO);
            integralSufferDao.addEntry(integralSufferEntry1);
        }
    }

    /**
     * 获得用户的所有具有管理员权限的社区id
     *
     */
    public List<ObjectId> getMyRoleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getManagerGroupIdsByUserId(userId);
        List<ObjectId> clist = new ArrayList<ObjectId>();
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }
}
