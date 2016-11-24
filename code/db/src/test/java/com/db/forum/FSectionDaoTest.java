package com.db.forum;

import com.pojo.forum.FSectionEntry;
import org.junit.Test;

/**
 * Created by admin on 2016/6/1.
 */
public class FSectionDaoTest {
    private FSectionDao fSectionDao = new FSectionDao();

    @Test
    public void addFSectionEntry(){
        FSectionEntry fSectionEntry =new FSectionEntry("安全健康","儿童安全自我保护、食品安全、运动安全...","funny","安全健康是革命的本钱！");
        fSectionDao.addFSectionEntry(fSectionEntry);
        System.out.println(fSectionEntry.getID());
    }
}
