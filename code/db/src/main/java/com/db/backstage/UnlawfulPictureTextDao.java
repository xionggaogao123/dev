package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.UnlawfulPictureTextEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/20.
 */
public class UnlawfulPictureTextDao extends BaseDao {
    //添加
    public String addEntry(UnlawfulPictureTextEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PICTURE_TEXT, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //单查询
    public UnlawfulPictureTextEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PICTURE_TEXT, query, Constant.FIELDS);
        if (obj != null) {
            return new UnlawfulPictureTextEntry((BasicDBObject) obj);
        }
        return null;
    }
    //通过该记录
    public void passContentEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isc",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PICTURE_TEXT, query,updateValue);
    }
    //删除该记录
    public void deleteContentEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isc",Constant.TWO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PICTURE_TEXT, query,updateValue);
    }
    public List<UnlawfulPictureTextEntry> selectContentList(int isCheck,String id,int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isc", isCheck);
        if(id != null && !id.equals("")){
            query.append(Constant.ID,new ObjectId(id));
        }
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_PICTURE_TEXT, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<UnlawfulPictureTextEntry> retList =new ArrayList<UnlawfulPictureTextEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new UnlawfulPictureTextEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getNumber(int isCheck,String id) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isc", isCheck);
        if(id != null && !id.equals("")){
            query.append(Constant.ID,id);
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_PICTURE_TEXT,
                        query);
        return count;
    }
 /*   public List<UnlawfulPictureTextEntry> getEntryList(){


        return null;
    }*/

}
