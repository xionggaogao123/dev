package com.fulaan.instantmessage.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.instantmessage.RedDotDao;
import com.fulaan.instantmessage.dto.RedDotDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.mongodb.DBObject;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.instantmessage.RedDotEntry;
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
     * 添加记录
     */
    public void addEntryList(List<ObjectId> commuityIds,int type){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        for(ObjectId obid : commuityIds){
            List<ObjectId> bid = new ArrayList<ObjectId>();
            //所有学生
            List<ObjectId> sids = newVersionCommunityBindDao.getStudentListByCommunityId(obid);
            bid.add(obid);
            //所有家长
            List<ObjectId> list = communityDao.getListGroupIds(bid);
            List<ObjectId> li = memberDao.getMembersByList(list);
            Set<ObjectId> se = new HashSet<ObjectId>();
            List<ObjectId> uids = new ArrayList<ObjectId>();
            se.addAll(li);
            uids.addAll(se);
            //
            uids.addAll(sids);
            List<RedDotDTO> redDotDTOs = new ArrayList<RedDotDTO>();
            if(ApplyTypeEn.getProTypeEname(type).equals("other")){//作业类型
                List<ObjectId> unid =  redDotDao.getOtherRedDotEntryByList(uids, zero, type);
                uids.removeAll(unid);
                if(uids.size()>0){
                    for(ObjectId oid : uids) {
                        RedDotDTO dto = new RedDotDTO();
                        dto.setType(type);
                        dto.setDateTime(zero);
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


    public void cleanResult(ObjectId userId,int type,long dataTime){
        if(ApplyTypeEn.getProTypeEname(type).equals("other")) {//作业类型
            redDotDao.cleanEntry1(userId,dataTime,type);
        }else if(ApplyTypeEn.getProTypeEname(type).equals("same")){//通知类型
            redDotDao.cleanEntry2(userId, type);
        }else{

        }
    }



}
