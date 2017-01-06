package com.fulaan.playmate.service;

import com.db.factory.MongoFacroty;
import com.db.playmate.FMateTypeDao;
import com.fulaan.playmate.pojo.MateData;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.pojo.playmate.FMateTypeEntry;
import com.sys.constants.Constant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moslpc on 2016/12/7.
 */
@Service
public class FMateTypeService {

    private FMateTypeDao fMateTypeDao = new FMateTypeDao();

    public Map<String, List<MateData>> getAllSortTypes() {
        Map<String, List<MateData>> data = new HashMap<String, List<MateData>>();
        data.put("tags", getMateType(1));
        data.put("ages", getMateType(2));
        data.put("distances", getMateType(3));
        data.put("times", getMateType(4));
        return data;
    }

    private List<MateData> getMateType(int type) {
        FMateTypeEntry fMateTypeEntry = fMateTypeDao.getType(type);
        if (fMateTypeEntry == null) {
            return new ArrayList<MateData>();
        }

        BasicDBList dbList = fMateTypeEntry.getData();
        return getMateData(dbList);
    }

    public List<MateData> getTags() {
        FMateTypeEntry fMateTypeEntry = fMateTypeDao.getType(1);
        if (fMateTypeEntry == null) {
            return new ArrayList<MateData>();
        }
        BasicDBList dbList = fMateTypeEntry.getData();
        return getMateData(dbList);
    }

    public List<MateData> getOns() {
        FMateTypeEntry fMateTypeEntry = fMateTypeDao.getType(4);
        if (fMateTypeEntry == null) {
            return new ArrayList<MateData>();
        }
        BasicDBList dbList = fMateTypeEntry.getData();
        return getMateData(dbList);
    }

    private List<MateData> getMateData(BasicDBList dbList) {
        List<MateData> tags = new ArrayList<MateData>();
        if (dbList == null) return tags;
        for (Object o : dbList) {
            int code = (Integer) ((DBObject) o).get("co");
            String data = (String) ((DBObject) o).get("da");
            MateData userTag = new MateData(code, data);
            tags.add(userTag);
        }
        return tags;
    }

    public void saveType(int type) {
        FMateTypeEntry mateTypeEntry = new FMateTypeEntry(type);
        fMateTypeDao.save(mateTypeEntry);
    }

    public void pushType(int type, int code, String data) {
        fMateTypeDao.pushType(type, code, data);
    }


    public void generateData() {

        FMateTypeService service = new FMateTypeService();
        int code;
        String[] list;

        service.saveType(1);
        String tags = "旅行,英语,阅读,足球,篮球,羽毛球,网球,乒乓球,轮滑,跆拳道,跳绳,歌唱,表演,舞蹈,美术,钢琴,古筝,二胡,小提琴,笛子,架子鼓,围棋,跳棋,象棋,桥牌,演讲,航模,航海,机器人";
        list = tags.split(",");
        code = 100;
        for (String s1 : list) {
            service.pushType(1, code++, s1);
        }


        service.saveType(2);
        String ages = "3-5岁,5-8岁,8-11岁,11-15岁,15-18岁,18岁以上";
        list = ages.split(",");
        code = 1;
        for (String s1 : list) {
            service.pushType(2, code++, s1);
        }

        service.saveType(3);
        String distance = "≤0.5km,≤1km,≤2km,≤5km";
        list = distance.split(",");
        code = 1;
        for (String s1 : list) {
            service.pushType(3, code++, s1);
        }

        service.saveType(4);
        String s = "周六08:00~11:00,周六11:00~14:00,周六14:00~17:00,周日08:00~11:00,周日11:00~14:00,周日14:00~17:00";
        list = s.split(",");
        code = 1;
        for (String s1 : list) {
            service.pushType(4, code++, s1);
        }

    }


    public void create2dsphereIndex() {
        fMateTypeDao.create2dsphereIndex(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE);
        fMateTypeDao.create2dsphereIndex(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY);
    }

    public void clearHeap() {
//        fMateTypeDao.dropCollection(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_MATE_SEEKMATE);
//        fMateTypeDao.dropCollection(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_ACTIVITY);
//        fMateTypeDao.dropCollection(MongoFacroty.getAppDB(),Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET);
        fMateTypeDao.dropCollection(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_F_MATETYPE);
    }
}
