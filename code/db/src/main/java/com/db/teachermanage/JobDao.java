package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.EducationEntry;
import com.pojo.teachermanage.JobEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class JobDao extends BaseDao {

    /**
     * 增加工作信息
     *
     * @param e
     * @return
     */
    public ObjectId addJobEntry(JobEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JOB, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除工作信息
     * @param userId
     */
    public void removeJobEntry(ObjectId userId) {
        DBObject query =new BasicDBObject("ui",userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JOB, query);
    }

    /**
     * 获取工作信息
     * @param userId
     * @param fields
     * @return
     */
    public List<JobEntry> getJobList(ObjectId userId,DBObject fields) {
        BasicDBObject query =new BasicDBObject("ui",userId);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JOB, query, fields);
        List<JobEntry> jobEntryList=new ArrayList<JobEntry>();
        for(DBObject dbObject:list){
            JobEntry jobEntry=new JobEntry((BasicDBObject)dbObject);
            jobEntryList.add(jobEntry);
        }
        return jobEntryList;
    }
}
