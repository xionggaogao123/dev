package com.fulaan.indicator.service;

import com.db.indicator.InterestEvaluateDao;
import com.db.school.InterestClassDao;
import com.db.user.UserDao;
import com.fulaan.indicator.dto.*;
import com.fulaan.utils.RestAPIUtil;
import com.mongodb.BasicDBObject;
import com.pojo.indicator.InterestEvaluate;
import com.pojo.indicator.InterestEvaluateEntry;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.InterestClassStudent;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.JsonUtil;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/10/31.
 */
@Service
public class InterestEvaluateService {

    private InterestClassDao interestClassDao =new InterestClassDao();

    private InterestEvaluateDao interestEvaluateDao =new InterestEvaluateDao();

    private UserDao userDao=new UserDao();

    public void addEvaluateList(InterestEvaluateDTO evaluateDto, ObjectId schoolId, ObjectId userId) {
        String resultStr =RestAPIUtil.getITreeSnapshotById(evaluateDto.getSnapshotId());
        Map<String, Indicator> zhiBiaoMap=new HashMap<String, Indicator>();
        for(Indicator indi : evaluateDto.getZhiBiaos()){
            zhiBiaoMap.put(indi.getZhiBiaoId(), indi);
        }
        List<Indicator> zhiBiaos = new ArrayList<Indicator>();
        try{
            JSONObject dataJson = new JSONObject(resultStr);
            JSONObject message = dataJson.getJSONObject("message");
            JSONArray rows = message.getJSONArray("indiList");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    IndicatorDTO dto = (IndicatorDTO) JsonUtil.JSONToObj(info.toString(), IndicatorDTO.class);
                    Indicator indicator = zhiBiaoMap.get(dto.getZid());
                    if(indicator==null){
                        indicator=new Indicator();
                        indicator.setZhiBiaoId(dto.getZid());
                        indicator.setScoreType(1);
                        indicator.setScore("0");
                    }
                    zhiBiaos.add(indicator);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        List<InterestEvaluateEntry> entryList = new ArrayList<InterestEvaluateEntry>();
        String[] commToIds = evaluateDto.getCommonToIdsStr().split(",");
        for(String commToId : commToIds) {
            evaluateDto.setSchoolId(schoolId.toString());
            evaluateDto.setCommonFromId(userId.toString());
            evaluateDto.setCommonToId(commToId);
            evaluateDto.setZhiBiaos(zhiBiaos);
            entryList.add(evaluateDto.buildInterestEvaluateEntry());
        }
        interestEvaluateDao.addInterestEvaluateEntryList(entryList);
    }

    public void updEvaluateList(InterestEvaluateDTO evaluateDto, ObjectId schoolId, ObjectId userId) {
        InterestEvaluateEntry entry= interestEvaluateDao.getStudentEvaluateEntryById(new ObjectId(evaluateDto.getId()));
        Map<String, Indicator> evaluateMap =new HashMap<String, Indicator>();
        if(entry!=null){
            InterestEvaluateDTO reDto = new InterestEvaluateDTO(entry);
            if(null!=reDto.getZhiBiaos() && !reDto.getZhiBiaos().isEmpty())
            {
                for(Indicator ie: reDto.getZhiBiaos())
                {
                    evaluateMap.put(ie.getZhiBiaoId(), ie);
                }
            }
        }
        List<String> zhiBiaoIds = new ArrayList<String>();
        List<Indicator> zhiBiaos = new ArrayList<Indicator>();
        for (Map.Entry<String, Indicator> item : evaluateMap.entrySet()) {
            if(!zhiBiaoIds.contains(item.getKey())){
                zhiBiaos.add(item.getValue());
            }
        }
        zhiBiaos.addAll(evaluateDto.getZhiBiaos());
        evaluateDto.setZhiBiaos(zhiBiaos);
        String[] commToIds = evaluateDto.getCommonToIdsStr().split(",");
        for(String commToId : commToIds) {
            evaluateDto.setSchoolId(schoolId.toString());
            evaluateDto.setCommonFromId(userId.toString());
            evaluateDto.setCommonToId(commToId);
        }
        InterestEvaluateEntry updateEntry = evaluateDto.buildInterestEvaluateEntry();
        interestEvaluateDao.updInterestEvaluateEntryById(new ObjectId(evaluateDto.getId()), updateEntry);
    }

    public List<UserDetailInfoDTO> findInterestClassStuByActivityId(String appliedId, String activityId, int termType, int stuState) {
        List<InterestClassStudent> stuList = new ArrayList<InterestClassStudent>();
        List<InterestEvaluateEntry> list = interestEvaluateDao.findInterestEvaluateListByParam(new ObjectId(appliedId), new ObjectId(activityId), termType);
        List<ObjectId> stuIds = MongoUtils.getFieldObjectIDs(list,"ctid");
        if(stuState==1){
            InterestClassEntry interestClassEntry=interestClassDao.findEntryByClassId(new ObjectId(activityId));
            if(interestClassEntry==null || interestClassEntry.getInterestClassStudents()==null){
                return new ArrayList<UserDetailInfoDTO>();
            }
            if(termType < 0){
                stuList = interestClassEntry.getCurrentInterestClassStudents();
            } else {
                stuList = interestClassEntry.getInterestClassStudentsByTermType(termType);
            }
            stuIds = collectObjectId(stuList, stuIds);
        }

        List<UserDetailInfoDTO> userDTOList=new ArrayList<UserDetailInfoDTO>();
        List<UserEntry> userEntryList=userDao.getUserEntryList(stuIds, Constant.FIELDS);
        for(UserEntry userEntry : userEntryList){
            UserDetailInfoDTO dto=new UserDetailInfoDTO(userEntry);
            userDTOList.add(dto);
        }
        return userDTOList;
    }

    public Map<String, Object> getStudentResultMap(String appliedId, String activityId, int termType, String name, int page, int pageSize) {
        Map<String, Object> map = new HashMap<String,Object>();
        List<ObjectId> stuIds = null;
        List<ObjectId> uids =null;
        if(!"".equals(name)){
            List<InterestEvaluateEntry> list = interestEvaluateDao.findInterestEvaluateListByParam(new ObjectId(appliedId), new ObjectId(activityId), termType, new BasicDBObject("ctid",1));
            stuIds = MongoUtils.getFieldObjectIDs(list,"ctid");
            uids = userDao.findIdListByUserName(stuIds, name);
        }else{
            uids = new ArrayList<ObjectId>();
        }
        int skip = page < 1 ? 0 : ((page - 1) * pageSize);
        List<InterestEvaluateEntry> ieeList = interestEvaluateDao.findInterestEvaluatePageListByParam(new ObjectId(appliedId), new ObjectId(activityId), termType, name, uids, skip, pageSize);
        int count = interestEvaluateDao.findInterestEvaluateCountByParam(new ObjectId(appliedId), new ObjectId(activityId), termType, name, uids);
        stuIds = MongoUtils.getFieldObjectIDs(ieeList,"ctid");
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(stuIds, new BasicDBObject("nm",1));
        List<InterestEvaluateDTO> reList=new ArrayList<InterestEvaluateDTO>();
        UserEntry userEntry = null;
        for(InterestEvaluateEntry entry : ieeList){
            InterestEvaluateDTO dto=new InterestEvaluateDTO(entry);
            userEntry = userMap.get(entry.getCommonToId());
            if(userEntry != null) {
                dto.setCommonToName(userEntry.getUserName());
            }
            reList.add(dto);
        }
        map.put("total", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("rows", reList);
        return map;
    }

    public static List<ObjectId> collectObjectId(List<InterestClassStudent> studentList, List<ObjectId> stuIds){
        List<ObjectId> idList=new ArrayList<ObjectId>();
        for(InterestClassStudent stu:studentList){
            if(!stuIds.contains(stu.getStudentId())) {
                idList.add(stu.getStudentId());
            }
        }
        return  idList;
    }

    public InterestEvaluateDTO getStudentEvaluate(String resultStr, String appliedId, String activityId, String commonToId, int termType, int stuState) {
        InterestEvaluateDTO reDto=null;
        Map<ObjectId, InterestEvaluate> map =new HashMap<ObjectId, InterestEvaluate>();
        if(stuState==2){
            InterestEvaluateEntry entry= interestEvaluateDao.getStudentEvaluateEntry(new ObjectId(appliedId) ,new ObjectId(activityId), new ObjectId(commonToId), termType);
            if(entry!=null){
                if(null!=entry.getZhiBiaos() && !entry.getZhiBiaos().isEmpty())
                {
                    for(InterestEvaluate ie: entry.getZhiBiaos())
                    {
                        map.put(ie.getZhiBiaoId(), ie);
                    }
                }
                reDto = new InterestEvaluateDTO(entry);
            }
        }else{
            reDto = new InterestEvaluateDTO();
        }
        try {
            List<Indicator> indiList = new ArrayList<Indicator>();
            JSONObject dataJson = new JSONObject(resultStr);
            JSONObject message = dataJson.getJSONObject("message");
            JSONArray infos = message.getJSONArray("indiList");
            for (int j = 0; j < infos.length(); j++) {
                JSONObject info = infos.getJSONObject(j);
                String zhiBiaoName = info.getString("name");
                Indicator indi = null;
                String zhiBiaoId = info.getString("zid");
                InterestEvaluate ie = null;
                if(stuState==2){
                    ie = map.get(new ObjectId(zhiBiaoId));
                }
                if(ie!=null){
                    indi = new Indicator(ie);
                }else{
                    indi = new Indicator();
                    indi.setZhiBiaoId(zhiBiaoId);
                    indi.setScoreType(1);
                    indi.setScore("0");
                }
                String zhiBiaoParentId = info.getString("parentId");
                int lvl=info.getInt("level");
                int type=info.getInt("type");
                indi.setZhiBiaoParentId(zhiBiaoParentId);
                indi.setLevel(lvl);
                indi.setType(type);
                indi.setZhiBiaoName(zhiBiaoName);
                indiList.add(indi);
            }
            reDto.setZhiBiaos(indiList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return reDto;
    }

    public InterestEvaluateDTO getStudentEvaluateById(String id) {
        BasicDBObject fields = new BasicDBObject();
        fields.append("aid",1);
        fields.append("tty",1);
        fields.append("cfid",1);
        fields.append("ctid",1);
        fields.append("des",1);
        fields.append("apid",1);
        fields.append("stid",1);
        InterestEvaluateEntry entry= interestEvaluateDao.getStudentEvaluateEntryById(new ObjectId(id), fields);
        UserEntry user = userDao.getUserEntry(entry.getCommonToId(), new BasicDBObject("nm",1));
        InterestClassEntry classEntry=interestClassDao.findEntryByClassId(entry.getActivityId());
        InterestEvaluateDTO reDto = new InterestEvaluateDTO(entry);
        if(user!=null) {
            reDto.setCommonToName(user.getUserName());
        }
        if(classEntry!=null) {
            reDto.setActivityName(classEntry.getClassName());
        }
        return reDto;
    }
}
