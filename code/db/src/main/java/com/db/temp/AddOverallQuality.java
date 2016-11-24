package com.db.temp;

import com.db.overallquality.OverallQualityScoreSetDao;
import com.pojo.overallquality.OverallQualityScoreSetEntry;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Created by guojing on 2016/8/4.
 */
public class AddOverallQuality {
    public static void main(String[] args) throws IOException {
        OverallQualityScoreSetDao dao=new OverallQualityScoreSetDao();
        OverallQualityScoreSetEntry entry=new OverallQualityScoreSetEntry(
            new ObjectId("55934c14f6f28b7261c19c5e"),"卫生分值","ws",1);//体锻 卫生 行规
        dao.addOverallQualityScoreSetEntry(entry);
    }
}
