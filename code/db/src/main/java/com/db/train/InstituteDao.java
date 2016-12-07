package com.db.train;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.db.school.CampusDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.train.InstituteEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/12/2.
 */
public class InstituteDao extends BaseDao {

    public void saveOrUpdate(InstituteEntry entry){
      save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_INSTITUTE,entry.getBaseEntry());
    }

    /**
     * 根据培训分类信息和位置查找信息
     * @param type
     * @param area
     * @param page
     * @param pageSize
     * @return
     */
    public List<InstituteEntry> findInstituteEntries(String regular,List<String> regionIds,List<String> itemTypeIds,String type,String area,int page,int pageSize,int sortType){
        List<InstituteEntry> entries=new ArrayList<InstituteEntry>();
        BasicDBObject query=getQueryCondition(regular,regionIds,itemTypeIds,type, area);
        BasicDBObject orderBy=new BasicDBObject();
        if(sortType==1){
            orderBy.append("sc",-1);
        }
        orderBy.append(Constant.ID,-1);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_INSTITUTE,query,Constant.FIELDS,orderBy,(page-1)*pageSize,pageSize);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbo:dbObjects){
                entries.add(new InstituteEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }

    private BasicDBObject getQueryCondition(String regular,List<String> regionIds,List<String> itemTypeIds,String type,String area){
        BasicDBObject query=new BasicDBObject();
        if(StringUtils.isNotBlank(type)){
            query.append("tys.id",type);
        }
        if(StringUtils.isNotBlank(area)){
            query.append("ars.id",area);
        }
        if(regionIds.size()!=0){
            query.append("ars.id",new BasicDBObject(Constant.MONGO_IN,regionIds));
        }
        if(itemTypeIds.size()!=0){
            query.append("tys.id",new BasicDBObject(Constant.MONGO_IN,itemTypeIds));
        }

        if(StringUtils.isNotBlank(regular)){
            Pattern pattern = Pattern.compile("^.*" + regular + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("nm", new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        return query;
    }


    public int countInstituteEntries(String regular,List<String> regionIds,List<String> itemTypeIds,String type,String area){
        BasicDBObject query=getQueryCondition(regular,regionIds,itemTypeIds,type, area);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_INSTITUTE,query);
    }

    /**
     * 根据Id查找信息
     * @param id
     * @return
     */
    public InstituteEntry findById(ObjectId id){
        BasicDBObject query=new BasicDBObject().append(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_INSTITUTE,query);
        if(null!=dbObject){
            return new InstituteEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }

}
