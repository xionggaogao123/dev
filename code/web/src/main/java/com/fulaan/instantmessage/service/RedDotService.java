package com.fulaan.instantmessage.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.instantmessage.RedDotDao;
import com.db.reportCard.GroupExamUserRecordDao;
import com.fulaan.instantmessage.dto.RedDotDTO;
import com.fulaan.instantmessage.dto.RedResultDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.mongodb.DBObject;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.instantmessage.RedDotEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by James on 2017/10/25.
 */
@Service
public class RedDotService {

    private RedDotDao redDotDao = new RedDotDao();

    private CommunityDao communityDao = new CommunityDao();

    private MemberDao memberDao = new MemberDao();
    @Autowired
    private NewVersionBindService newVersionBindService;

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private GroupExamUserRecordDao groupExamUserRecordDao = new GroupExamUserRecordDao();

    /**
     * 批量增加红点记录
     * @param list
     */
    public void addRedDotEntryBatch(List<RedDotDTO> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            RedDotDTO si = list.get(i);
            RedDotEntry obj = si.buildAddEntry();
            dbList.add(obj.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            redDotDao.addBatch(dbList);
        }
    }

    /**
     * 首页加载红点信息
     * @param userId
     * @return
     */
    public Map<String,Object> selectResult(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //作业
        RedDotEntry entry = redDotDao.getOtherEntryByUserId(userId, zero, ApplyTypeEn.operation.getType());
        //通知
        RedDotEntry entry2 = redDotDao.getEntryByUserId(userId, ApplyTypeEn.notice.getType());
        map.put("operation",new RedDotDTO(entry));
        map.put("notice",new RedDotDTO(entry2));
        return map;
    }

    /**
     * 首页加载所有的红点记录
     * @param userId
     * @return
     */
    public Map<String,Object> selectAllResult(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<RedDotEntry> entries = redDotDao.getAllEntry(userId);
        //作业
        RedDotEntry entry = redDotDao.getOtherEntryByUserId(userId, zero, ApplyTypeEn.operation.getType());
        RedResultDTO dtos = new RedResultDTO();
        map.put("operation",new RedDotDTO(entry));
        dtos.setOperation(new RedDotDTO(entry));
        for(RedDotEntry entry1 : entries){
            if(entry1.getType()==ApplyTypeEn.notice.getType()){
                map.put("notice",new RedDotDTO(entry1));
                dtos.setNotice(new RedDotDTO(entry));
            }else if(entry1.getType()==ApplyTypeEn.hot.getType()){
                map.put("hot",new RedDotDTO(entry1));
                dtos.setHot(new RedDotDTO(entry));
            }else if(entry1.getType()==ApplyTypeEn.text.getType()){
                map.put("text",new RedDotDTO(entry1));
                dtos.setText(new RedDotDTO(entry));
            }else if(entry1.getType()==ApplyTypeEn.repordcard.getType()){
                map.put("repordcard",new RedDotDTO(entry1));
                dtos.setRepordcard(new RedDotDTO(entry));
            }else if(entry1.getType()==ApplyTypeEn.study.getType()){
                map.put("study",new RedDotDTO(entry1));
                dtos.setStudy(new RedDotDTO(entry));
            }else if(entry1.getType()==ApplyTypeEn.piao.getType()){
                map.put("piao",new RedDotDTO(entry1));
                dtos.setPiao(new RedDotDTO(entry));
            }else if(entry1.getType()==ApplyTypeEn.happy.getType()){
                map.put("happy",new RedDotDTO(entry1));
                dtos.setHappy(new RedDotDTO(entry));
            }else if(entry1.getType()==ApplyTypeEn.active.getType()){
                map.put("active",new RedDotDTO(entry1));
                dtos.setActive(new RedDotDTO(entry));
            }
        }
        return map;
    }

    /**
     * 首页加载所有的红点记录
     * @param userId
     * @return
     */
    public RedResultDTO selectAllResultDTO(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<RedDotEntry> entries = redDotDao.getAllEntry(userId);
        //作业
        RedDotEntry entry = redDotDao.getOtherEntryByUserId(userId, zero, ApplyTypeEn.operation.getType());
        RedResultDTO dtos = new RedResultDTO();
        if(entry != null){
            dtos.setOperation(new RedDotDTO(entry));
        }
        for(RedDotEntry entry1 : entries){
            if(entry1.getType()==ApplyTypeEn.notice.getType()){
                dtos.setNotice(new RedDotDTO(entry1));
            }else if(entry1.getType()==ApplyTypeEn.hot.getType()){
                dtos.setHot(new RedDotDTO(entry1));
            }else if(entry1.getType()==ApplyTypeEn.text.getType()){
                dtos.setText(new RedDotDTO(entry1));
            }else if(entry1.getType()==ApplyTypeEn.repordcard.getType()){
                dtos.setRepordcard(new RedDotDTO(entry1));
            }else if(entry1.getType()==ApplyTypeEn.study.getType()){
                dtos.setStudy(new RedDotDTO(entry1));
            }else if(entry1.getType()==ApplyTypeEn.piao.getType()){
                dtos.setPiao(new RedDotDTO(entry1));
            }else if(entry1.getType()==ApplyTypeEn.happy.getType()){
                dtos.setHappy(new RedDotDTO(entry1));
            }else if(entry1.getType()==ApplyTypeEn.active.getType()){
                dtos.setActive(new RedDotDTO(entry1));
            }
        }
        return dtos;
    }
    /**
     * 首页加载所有的红点记录
     * @param userId
     * @return
     */
    public Map<String,Object> selectDayNotice(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<RedDotEntry> entries = redDotDao.getAllEntry(userId);
        //作业
        RedDotEntry entry = redDotDao.getOtherEntryByUserId(userId, zero, ApplyTypeEn.operation.getType());
        map.put("dayNotice",new RedDotDTO(entry));
        for(RedDotEntry entry1 : entries){
            if(entry1.getType()==ApplyTypeEn.daynotice.getType()){
                map.put("dayNotice",new RedDotDTO(entry1));
            }
        }
        return map;
    }
    /**
     * 学生端加载红点信息
     * @param userId
     * @return
     */
    public Map<String,Object> selectStudentResult(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(userId);
        //作业
        RedDotEntry entry = redDotDao.getOtherEntryByUserId(userId, zero, ApplyTypeEn.operation.getType());
        //通知
        RedDotEntry entry2 = redDotDao.getEntryByUserId(userId, ApplyTypeEn.notice.getType());
        map.put("operation",new RedDotDTO(entry));
        map.put("notice",new RedDotDTO(entry2));
        return map;
    }
    /**
     * 添加红点成绩单(third)
     *
     */
    public void addThirdList(ObjectId id,ObjectId communityId,ObjectId userId,int type){
        List<ObjectId> sids = groupExamUserRecordDao.getStudentReceivedEntries(id);
        List<ObjectId> pids = newVersionCommunityBindDao.getAllStudentBindEntries(communityId,sids);
        Set<ObjectId> set = new HashSet<ObjectId>();
        set.addAll(sids);
        set.addAll(pids);
        set.remove(userId);
        List<ObjectId> uids = new ArrayList<ObjectId>();
        uids.addAll(set);
        List<RedDotDTO> redDotDTOs = new ArrayList<RedDotDTO>();
        if(ApplyTypeEn.getProTypeEname(type).equals("third")){//通知类型
            List<ObjectId> unid =  redDotDao.getRedDotEntryByList(uids, type);
            uids.removeAll(unid);
            if(uids.size()>0){
                for(ObjectId oid : uids) {
                    RedDotDTO dto = new RedDotDTO();
                    dto.setType(type);
                    dto.setNewNumber(1);
                    dto.setUserId(oid.toString());
                    redDotDTOs.add(dto);
                }
            }
            redDotDao.updateEntry2(unid, type);
        }else{

        }
        this.addRedDotEntryBatch(redDotDTOs);

    }

    /**
     * 添加记录//1:家长2:学生3:家长，学生
     *
     * //  4其他类
     */
    public void addEntryList(List<ObjectId> commuityIds,ObjectId userId,int type,int lei){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String dateTime = "";
        if(zero!=0l){
            dateTime = DateTimeUtils.getLongToStrTimeTwo(zero);
        }else{
            dateTime = "";
        }
        for(ObjectId obid : commuityIds){
            List<ObjectId> bid = new ArrayList<ObjectId>();
            List<ObjectId> uids = new ArrayList<ObjectId>();
            //所有学生
            List<ObjectId> sids = newVersionCommunityBindDao.getStudentListByCommunityId(obid);
            if(lei ==2 || lei == 3 || lei==4){
                uids.addAll(sids);
            }
            bid.add(obid);
            //所有家长
           // List<ObjectId> li = getMyRoleList4(obid,userId);
            List<ObjectId> list = communityDao.getListGroupIds(bid);
           // List<ObjectId> li = memberDao.getMembersByList(list);
          /*  if(lei==4){
                Set<ObjectId> se = new HashSet<ObjectId>();
                se.addAll(li);
                uids.addAll(se);
            }*/
            if(lei==1 || lei == 3 || lei==4){
                List<ObjectId> li2 = memberDao.getMembersByList2(list,userId);
                Set<ObjectId> se = new HashSet<ObjectId>();
                se.addAll(li2);
                uids.addAll(se);
            }
            List<RedDotDTO> redDotDTOs = new ArrayList<RedDotDTO>();
            if(ApplyTypeEn.getProTypeEname(type).equals("other")){//作业类型
                List<ObjectId> unid =  redDotDao.getOtherRedDotEntryByList(uids, zero, type);
                uids.removeAll(unid);
                if(uids.size()>0){
                    for(ObjectId oid : uids) {
                        RedDotDTO dto = new RedDotDTO();
                        dto.setType(type);
                        dto.setDateTime(dateTime);
                        dto.setNewNumber(1);
                        dto.setUserId(oid.toString());
                        redDotDTOs.add(dto);
                    }
                }
                redDotDao.updateEntry1(unid, zero, type);
            }else if(ApplyTypeEn.getProTypeEname(type).equals("same")){//通知类型
                List<ObjectId> unid =  redDotDao.getRedDotEntryByList(uids, type);
                uids.removeAll(unid);
                if(uids.size()>0){
                    for(ObjectId oid : uids) {
                        RedDotDTO dto = new RedDotDTO();
                        dto.setType(type);
                        dto.setNewNumber(1);
                        dto.setUserId(oid.toString());
                        redDotDTOs.add(dto);
                    }
                }
                redDotDao.updateEntry2(unid,type);
            }else{

            }
            this.addRedDotEntryBatch(redDotDTOs);

        }
    }

    public void addOtherEntryList(List<ObjectId> commuityIds,ObjectId userId,int type,int lei){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String dateTime = "";
        if(zero!=0l){
            dateTime = DateTimeUtils.getLongToStrTimeTwo(zero);
        }else{
            dateTime = "";
        }
        for(ObjectId obid : commuityIds){
            List<ObjectId> bid = new ArrayList<ObjectId>();
            List<ObjectId> uids = new ArrayList<ObjectId>();
            //所有学生
            List<ObjectId> sids = newVersionCommunityBindDao.getStudentListByCommunityId(obid);
            if(lei ==2 || lei == 3 || lei==4){
                uids.addAll(sids);
            }
            bid.add(obid);
            //所有家长
            List<ObjectId> list = communityDao.getListGroupIds(bid);
            List<ObjectId> li = memberDao.getMembersByList(list);
            if(lei==4){
                Set<ObjectId> se = new HashSet<ObjectId>();
                se.addAll(li);
                uids.addAll(se);
            }
            if(lei==1 || lei == 3){
                List<ObjectId> li2 = memberDao.getMembersByList2(list,userId);
                Set<ObjectId> se = new HashSet<ObjectId>();
                se.addAll(li2);
                uids.addAll(se);
            }
            List<RedDotDTO> redDotDTOs = new ArrayList<RedDotDTO>();
            if(type==2){//活动报名
                type=9;
            }else if(type==3){//火热分享
                type=3;
            }else if(type==4){//参考资料
                type=4;
            }else if(type==5){//
                type=10;
            }else if(type==6){//学习用品
                type=6;
            }else if(type==7){//投票
                type=7;
            }else{

            }
            if(ApplyTypeEn.getProTypeEname(type).equals("other")){//作业类型
                List<ObjectId> unid =  redDotDao.getOtherRedDotEntryByList(uids, zero, type);
                uids.removeAll(unid);
                if(uids.size()>0){
                    for(ObjectId oid : uids) {
                        RedDotDTO dto = new RedDotDTO();
                        dto.setType(type);
                        dto.setDateTime(dateTime);
                        dto.setNewNumber(1);
                        dto.setUserId(oid.toString());
                        redDotDTOs.add(dto);
                    }
                }
                redDotDao.updateEntry1(unid, zero, type);
            }else if(ApplyTypeEn.getProTypeEname(type).equals("same")){//通知类型
                List<ObjectId> unid =  redDotDao.getRedDotEntryByList(uids, type);
                uids.removeAll(unid);
                if(uids.size()>0){
                    for(ObjectId oid : uids) {
                        RedDotDTO dto = new RedDotDTO();
                        dto.setType(type);
                        dto.setNewNumber(1);
                        dto.setUserId(oid.toString());
                        redDotDTOs.add(dto);
                    }
                }
                redDotDao.updateEntry2(unid,type);
            }else{

            }
            this.addRedDotEntryBatch(redDotDTOs);

        }
    }
    /**
     *根据社区id返回社区中不具有管理员权限的人
     *
     */
    public List<String> getMyRoleList2(ObjectId id){
        //获得groupId
        ObjectId obj =   communityDao.getGroupIdByCommunityId(id);
        List<MemberEntry> olist = memberDao.getMembers(obj, 1, 1000);
        List<String> clist = new ArrayList<String>();
        if(olist.size()>0){
            for(MemberEntry en : olist){
                if(en.getRole()==0){
                    clist.add(en.getUserId().toString());
                }
            }
        }
        return clist;
    }
    //返回不是我的用户idString
    public List<ObjectId> getMyRoleList4(ObjectId id,ObjectId userId){
        //获得groupId
        ObjectId obj =   communityDao.getGroupIdByCommunityId(id);
        List<MemberEntry> olist = memberDao.getMembers(obj, 1, 1000);
        List<ObjectId> clist = new ArrayList<ObjectId>();
        if(olist.size()>0){
            for(MemberEntry en : olist){
                if(!en.getUserId().toString().equals(userId.toString())){
                    clist.add(en.getUserId());
                }
            }
        }
        return clist;
    }

    public void cleanResult(ObjectId userId,int type,long dataTime){
        if(ApplyTypeEn.getProTypeEname(type).equals("other")) {//作业类型
            redDotDao.cleanEntry1(userId,dataTime,type);
        }else if(ApplyTypeEn.getProTypeEname(type).equals("same")){//通知类型
            redDotDao.cleanEntry2(userId, type);
        }else{

        }
    }

    public void cleanOtherResult(ObjectId userId,int type){
        if(type==2){//活动报名
            type=9;
        }else if(type==3){//火热分享
            type=3;
        }else if(type==4){//参考资料
            type=4;
        }else if(type==5){//
            type=10;
        }else if(type==6){//学习用品
            type=6;
        }else if(type==7){//投票
            type=7;
        }else{

        }
        redDotDao.cleanEntry2(userId, type);
    }
    public void cleanThirdResult(ObjectId userId,int type){
        redDotDao.cleanEntry2(userId, type);
    }

}
