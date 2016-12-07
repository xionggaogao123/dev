package com.fulaan.playmate.service;

import com.db.playmate.FMateTypeDao;
import com.fulaan.playmate.pojo.MateData;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.pojo.playmate.FMateTypeEntry;
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

    public Map<String,List<MateData>> getAllSortTypes() {
        Map<String,List<MateData>> data = new HashMap<String, List<MateData>>();
        data.put("tags",getMateType(1));
        data.put("ages",getMateType(2));
        data.put("distances",getMateType(3));
        data.put("times",getMateType(4));
        return data;
    }

    private List<MateData> getMateType(int type) {
        FMateTypeEntry fMateTypeEntry = fMateTypeDao.getType(type);
        List<MateData> tags = new ArrayList<MateData>();
        if (fMateTypeEntry == null) {
            return tags;
        }

        BasicDBList dbList = fMateTypeEntry.getData();
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

    public void pushType(int type,int code,String data) {
        fMateTypeDao.pushType(type,code,data);
    }


    public static void main(String[] args) {

        FMateTypeService service = new FMateTypeService();
        service.saveType(4);

        String s = "周六08:00~11:00,周六11:00~14:00,周六14:00~17:00,周日08:00~11:00,周日11:00~14:00,周日14:00~17:00";
        String[] list = s.split(",");
        int code = 1;
        for(String s1:list) {
            service.pushType(4,code++,s1);
        }


//        List<MateData> mateDatas = service.getMateType(1);
//        int a = 10;

    }


}
