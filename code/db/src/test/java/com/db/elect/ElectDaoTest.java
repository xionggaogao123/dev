package com.db.elect;

import com.pojo.elect.Candidate;
import com.pojo.elect.ElectEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Created by qinbo on 15/3/2.
 */
public class ElectDaoTest {
    ElectDao dao = new ElectDao();

    @Test
    public void addElectEntry()
    {
        ElectEntry electEntry = new ElectEntry("第一个选举","说明文字",null,new ObjectId("54f16ba6fe5bfcc6c96b4ba3"),
                0,0,0,0,new ObjectId("54f0328afe5b0f608a43329a"),0,0,0,0,0,0,0,0,1,null,0,0);

        ObjectId id = dao.add(electEntry);


    }

    @Test
    public void addCandidate()
    {
        Candidate candidate = new Candidate(
                new ObjectId("54f0328afe5b0f608a43329a"),
                "王明",
                "请投我",
                null,
                null,
                null,
                System.currentTimeMillis(),
                null
                );
        dao.addCandidate(new ObjectId("54f42bfa77c8d56e11d2596e"),candidate);
    }


}
