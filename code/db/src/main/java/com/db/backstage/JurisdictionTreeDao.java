package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.JurisdictionTreeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/24 14:48
 * @Description:    获取分类（ 系统设置 学校设置 运营管理 订单管理）权限的树形结构数据
 */
public class JurisdictionTreeDao extends BaseDao {

    public List<JurisdictionTreeEntry> getJurisdictionTree(Map map) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", 0);
        if (map.get("class") != null) {
            query.append("class", map.get("class"));
        }
        List<JurisdictionTreeEntry> entries = new ArrayList<JurisdictionTreeEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JURISDICTION_TREE, query,
                Constant.FIELDS);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new JurisdictionTreeEntry(dbObject));
            }
        }
        return entries;
    }

    public JurisdictionTreeEntry getEntryById(ObjectId objectId) {
        BasicDBObject query=new BasicDBObject()
                .append("_id",objectId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JURISDICTION_TREE,query,
                Constant.FIELDS);
        if(null!=dbObject){
            return new JurisdictionTreeEntry(dbObject);
        }else{
            return null;
        }
    }
}
