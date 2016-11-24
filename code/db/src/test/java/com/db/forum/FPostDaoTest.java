package com.db.forum;

import com.pojo.forum.FPostEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Created by admin on 2016/6/1.
 */
public class FPostDaoTest {
    private FPostDao fPostDao= new FPostDao();

    @Test
    public void addFPostEntry(){
        String personId="5742738565dc45553ffe3db9";
        FPostEntry fPostEntry =new FPostEntry(new ObjectId(personId),"scott_zl","我发表的帖子2",new ObjectId("574e54c265dc38cda820e5c1"),"晒才艺",System.currentTimeMillis(),"我今天为了测试发表帖子2，非常不错");
        fPostDao.addFPost(fPostEntry);
        System.out.println(fPostEntry.getID());
    }

    @Test
    public void countFPostScanCount(){
        int count=fPostDao.getTotalScanCount(new ObjectId("574e54c265dc38cda820e5c1"),"");
        System.out.println(count);
    }

    @Test
    public void countFPostCommentCount(){
        int count=fPostDao.getTotalCommentCount(new ObjectId("574e54c265dc38cda820e5c1"),"");
        System.out.println(count);
    }

    @Test
    public void countTheme(){
        int count=fPostDao.getThemeCount(new ObjectId("574e54c265dc38cda820e5c1"),"");
        System.out.println(count);
    }

}
