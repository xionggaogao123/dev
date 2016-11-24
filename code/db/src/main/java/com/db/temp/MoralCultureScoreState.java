package com.db.temp;

import com.db.moralculture.MoralCultureScoreDao;
import com.mongodb.DBObject;
import com.pojo.moralculture.MoralCultureScoreEntry;
import com.pojo.moralculture.MoralCultureScoreInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/7/27.
 */
public class MoralCultureScoreState {

	

    public static void main(String[] args) {
    	
        MoralCultureScoreDao dao=new MoralCultureScoreDao();
        List<MoralCultureScoreEntry> list= dao.selAllSchoolMoralCultureScore("55934c15f6f28b7261c19cd4");
        for(MoralCultureScoreEntry entry:list){
            List<MoralCultureScoreInfo> mcsiList=entry.getMcsiList();
            for(MoralCultureScoreInfo item: mcsiList){
                if(!"".equals(item.getProjectScore())) {
                   int projectScore=Integer.parseInt(item.getProjectScore())*10;
                    item.setProjectScore(projectScore+"");
                }
            }
        }

        for(MoralCultureScoreEntry entry:list){
            List<MoralCultureScoreInfo> mcsiList=entry.getMcsiList();
            List<DBObject> list1 =new ArrayList<DBObject>();
            for(MoralCultureScoreInfo item: mcsiList){
                list1.add(item.getBaseEntry());
            }
            dao.updMoralCultureScoreEntry(entry.getUserId(),entry.getGradeId(),entry.getClassId(),entry.getSemesterId(),list1);
        }
    }
}
