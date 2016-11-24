package com.db.microblog;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.microblog.ThemeEntry;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/12/14.
 */
public class ThemeDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addTheme(ThemeEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_THEME_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteTheme(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject value = new BasicDBObject("deflg", DeleteState.DELETED.getState());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_THEME_NAME, query, updateValue);
    }

    /**
     * 跟新话题
     *
     * @param id
     * @param themedec
     */
    public void updateTheme(ObjectId id, String themedec) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject value = new BasicDBObject("themedec", themedec);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_THEME_NAME, query, updateValue);
    }

    /**
     * 获取话题列表
     *
     * @return
     */
    public List<ThemeEntry> getThemeList() {
        List<ThemeEntry> themeEntryList = new ArrayList<ThemeEntry>();
        BasicDBObject query = new BasicDBObject("deflg", DeleteState.NORMAL.getState());
        List<DBObject> list = new ArrayList<DBObject>();
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_THEME_NAME, query, null);
        for (DBObject dbo : list) {
            themeEntryList.add(new ThemeEntry((BasicDBObject) dbo));
        }
        return themeEntryList;
    }
}
