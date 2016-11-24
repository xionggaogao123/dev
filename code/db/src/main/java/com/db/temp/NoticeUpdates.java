package com.db.temp;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.db.notice.NoticeDao;
import com.pojo.app.IdValuePair;
import com.pojo.notice.NoticeEntry;
import com.pojo.notice.NoticeReadsEntry;
import com.sys.constants.Constant;

public class NoticeUpdates {

    public static void main(String[] args) {
//		
        int skip = 0;
        int limit = 200;

        NoticeDao dao = new NoticeDao();
        while (true) {
            System.out.println("skip=" + skip);
            List<NoticeEntry> list = dao.getNoticeEntry(skip, limit);
            if (list.size() == 0) {
                break;
            }
            for (NoticeEntry e : list) {
                try {
//					 dao.updateTotalUserCount(e.getID(), "tus",e.getUsers().size());
//					 int read=e.getReadUsers().size();
//					 if(e.getReadUsers().size()>e.getUsers().size())
//					 {
//						 read=e.getUsers().size();
//					 }
//			         dao.updateTotalUserCount(e.getID(), "trus",read);


                    List<ObjectId> ids = new ArrayList<ObjectId>();
                    for (IdValuePair ivp : e.getReadUsers()) {
                        ids.add(ivp.getId());
                    }

                    NoticeReadsEntry nre = new NoticeReadsEntry(e.getID(), ids);


                    MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_NOTICE_READ_NAME).save(nre.getBaseEntry());


                } catch (Exception ex) {

                }
            }

            skip = skip + 200;
        }
    }
}
