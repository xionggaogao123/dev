package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.TeacherApproveEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/23.
 */
public class TeacherApproveDao extends BaseDao {
    //添加
    public String addEntry(TeacherApproveEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //删除作业
    public void updateEntry(ObjectId id,int type){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query,updateValue);
    }
    public List<TeacherApproveEntry> selectContentList(String seacherId,int type,int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if(type!= 0){
            query.append("typ",type);
        }
        if(seacherId != null && !seacherId.equals("")){
            query.append("uid",new ObjectId(seacherId));
        }
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<TeacherApproveEntry> retList =new ArrayList<TeacherApproveEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new TeacherApproveEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getNumber(String seacherId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if(type!= 0){
            query.append("typ",type);
        }
        if(seacherId != null && !seacherId.equals("")){
            query.append("uid",new ObjectId(seacherId));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TEACHER_APPROVE,
                        query);
        return count;
    }
}
