package com.db.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.app.RegionDao;
import com.db.school.SchoolDao;
import com.pojo.app.RegionEntry;
import com.pojo.school.SchoolEntry;

public class SchoolState {

    public static void main(String[] args) {
        RegionDao dao1 = new RegionDao();
        SchoolDao dao = new SchoolDao();

        Map<ObjectId, List<ObjectId>> provinceCityMap = new HashMap<ObjectId, List<ObjectId>>();
        Map<ObjectId, RegionEntry> map = new HashMap<ObjectId, RegionEntry>();
        List<RegionEntry> list = dao1.getAllRegionEntry();
        for (RegionEntry re : list) {
            map.put(re.getID(), re);
        }


        for (RegionEntry re : list) {
            if (re.getLevel() == 2) {
                provinceCityMap.put(re.getID(), new ArrayList<ObjectId>());
            }
        }


        for (RegionEntry re : list) {

            if (re.getLevel() == 3) {
                try {
                    if (null != re.getParentId()) {
                        provinceCityMap.get(re.getParentId()).add(re.getID());
                    }
                } catch (Exception ex) {
                    System.out.println(re);
                }
            }
        }


        for (Map.Entry<ObjectId, List<ObjectId>> entry : provinceCityMap.entrySet()) {
//			List<ObjectId> newList =new ArrayList<ObjectId>(entry.getValue());
//			newList.add(entry.getKey());
//			
//			List<SchoolEntry> slist= dao.getSchoolEntry(newList,0, 1000);
//			
//			RegionEntry re= map.get(entry.getKey());
//			
//			System.out.println(re.getName()+":"+slist.size());
        }
    }
}
