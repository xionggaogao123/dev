package com.db.news;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.news.NewsEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by yan on 2015/3/16.
 */
public class NewsDao extends BaseDao {
	
    public void addNews(NewsEntry newsEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_NAME,newsEntry.getBaseEntry());
    }

    public List<NewsEntry> newsList(int skip, int pageSize,String columnId,String title,String schoolId,String educationId) {
        BasicDBObject query=new BasicDBObject();
        if(schoolId!=null&&!"".equals(schoolId)){
            query.append("sid",new ObjectId(schoolId));
        }
        if(educationId!=null&&!"".equals(educationId)){
            query.append("eid",new ObjectId(educationId));
        }

        if(!columnId.equals("-1"))//根据组名获取
        {
            query.append("col", new ObjectId(columnId));
        }
        if(!title.equals("")) {
            Pattern john = Pattern.compile("^.*"+title+".*$");
            query.append("ti",john);
        }
        BasicDBObject sort=new BasicDBObject();
        sort.append("pin",-1);
        sort.append("_id",-1);

        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS,sort,skip,pageSize);

        List<NewsEntry> newsEntryList=new ArrayList<NewsEntry>();
        for(DBObject dbObject:dbObjects){
            newsEntryList.add(new NewsEntry((BasicDBObject)dbObject));
        }
        return newsEntryList;
    }


    public void updateNews(ObjectId objectId,String title,ObjectId column,int pinned,String thumb,String digest,String content)
    {
        BasicDBObject query=new BasicDBObject(Constant.ID,objectId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ti",title).
                        append("col",column).
                        append("pin",pinned).
                        append("thu",thumb).
                        append("dig",digest).
                        append("con", content));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,updateValue);
    }
    /*public void updateNews(ObjectId objectId, String title, String content) {
        BasicDBObject query=new BasicDBObject(Constant.ID,objectId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ti",title).append("con", content));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,updateValue);
    }*/

    public void deleteById(ObjectId objectId) {
        BasicDBObject query=new BasicDBObject(Constant.ID,objectId);
        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query);
    }

    public void deleteMany(List<ObjectId> list)
    {
        BasicDBObject query=new BasicDBObject();
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,list));
        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query);
    }

    public NewsEntry findNewsById(ObjectId objectId) {
        BasicDBObject query=new BasicDBObject(Constant.ID,objectId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS);
        if(dbObject!=null){
            return  new NewsEntry((BasicDBObject)dbObject);
        }
        return null;
    }

    /*public int countNews(ObjectId schoolId) {
        BasicDBObject query=new BasicDBObject("sid",schoolId);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query);
    }*/
    public int countNews(String schoolId,String educationId,String columnId,String title)
    {
        BasicDBObject query=new BasicDBObject();
        if(schoolId!=null&&!"".equals(schoolId)){
            query.append("sid",new ObjectId(schoolId));
        }
        if(educationId!=null&&!"".equals(educationId)){
            query.append("eid",new ObjectId(educationId));
        }
        if(!columnId.equals("-1"))//根据组名获取
        {
            query.append("col", new ObjectId(columnId));
        }
        if(!"".equals(title)) {
            Pattern john = Pattern.compile("^.*"+title+".*$");
            query.append("ti",john);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_NAME, query);
    }

    /**
     * 计算某一栏目下新闻个数
     * @param columnId
     * @return
     */
    public int countNewsByColumn(String columnId)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("col",new ObjectId(columnId));
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query);
    }

    /**
     * 根据栏目ID获取新闻列表，最多获取5条
     * @param columnId
     * @return
     */
    public List<NewsEntry> getNewsListByColumnId(ObjectId columnId)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("col",columnId);
        query.append("pin",1);
        BasicDBObject sort=new BasicDBObject();
        sort.append("_id",-1);
        int skip=0;
        int pageSize=5;
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS,sort,skip,pageSize);

        List<NewsEntry> newsEntryList=new ArrayList<NewsEntry>();
        for(DBObject dbObject:dbObjects){
            newsEntryList.add(new NewsEntry((BasicDBObject)dbObject));
        }
        return newsEntryList;
    }

    /**
     * 获取非置顶条目
     * @param page
     * @param pageSize
     * @param columnId
     * @param objectIds
     * @return
     */
    public List<NewsEntry> getOtherNewsListByColumnId(int page,int pageSize,ObjectId columnId,List<ObjectId> objectIds)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("col",columnId);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIds));
        BasicDBObject sort=new BasicDBObject();
        sort.append("_id",-1);
        int skip=(page-1)*pageSize;
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS,sort,skip,pageSize);

        List<NewsEntry> newsEntryList=new ArrayList<NewsEntry>();
        for(DBObject dbObject:dbObjects){
            newsEntryList.add(new NewsEntry((BasicDBObject)dbObject));
        }
        return newsEntryList;
    }

    /**
     * 获取非置顶条目数目
     * @param columnId
     * @param objectIds
     * @return
     */
    public int getOtherNewsCount(ObjectId columnId,List<ObjectId> objectIds)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("col",columnId);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIds));
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME, query);
    }

    /**
     * 获取非推荐条目第一条数据
     * @param objectIds
     * @return
     */
    public NewsEntry getFirstNews(ObjectId columnId,List<ObjectId> objectIds)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("col",columnId);
        BasicDBObject sort=new BasicDBObject();
        sort.append(Constant.ID,-1);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIds));
        List<DBObject> dbObject= find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_NAME, query, Constant.FIELDS, sort);
        if(dbObject.size()>0)
            return new NewsEntry((BasicDBObject)dbObject.get(0));
        return null;
        //return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME, query);
    }

    /**
     * 从其他里面获取最后一条
     * @param columnId
     * @param recomandList
     * @return
     */
    public NewsEntry getLastFromOthers(ObjectId columnId,List<ObjectId> recomandList)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("col",columnId);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,recomandList));
        BasicDBObject sort=new BasicDBObject();
        sort.append(Constant.ID,1);
        List<DBObject> basicDBObject=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS,sort,0,1);
        System.out.println(basicDBObject);
        if(basicDBObject.size()==0)
            return new NewsEntry();
        else
        {
            return new NewsEntry((BasicDBObject)basicDBObject.get(0));
        }
    }
    /**
     * 获取非推荐栏目的上一条下一条数据
     * @param newsId
     * @param columnId
     * @param type
     * @param _newsEntryList
     * @return
     */
    public NewsEntry getOtherNewsNotice(ObjectId newsId,ObjectId columnId,int type,List<NewsEntry> _newsEntryList)
    {
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        for(int i=0;i<_newsEntryList.size();i++)
        {
            objectIdList.add(_newsEntryList.get(i).getID());
        }
        BasicDBObject query=new BasicDBObject();
        query.append("col",columnId);
        //query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIdList));
        BasicDBObject sort=new BasicDBObject();
        List<DBObject> dbObjects=new ArrayList<DBObject>();
        if(type==1) {
            sort.append("_id", -1);


            BasicDBObject[] array2={ new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIdList)),
                    new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LT, newsId)) };
            query.put(Constant.MONGO_AND, array2);

            dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS,sort,0,1);
        }
        else if(type==0)//上一页
        {
            sort.append("_id", 1);

            BasicDBObject[] array2={ new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIdList)),
                    new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_GT, newsId)) };

            query.put(Constant.MONGO_AND, array2);
            //query.append("_id",new BasicDBObject(Constant.MONGO_AND,array));
            dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS,sort,0,1);
        }

        List<NewsEntry> newsEntryList=new ArrayList<NewsEntry>();
        for(DBObject dbObject:dbObjects){
            newsEntryList.add(new NewsEntry((BasicDBObject)dbObject));
        }
        if(newsEntryList.size()==0&&objectIdList.size()==0) {
            return new NewsEntry();
        }
        else if(newsEntryList.size()==0)
        {
            //取推荐新闻中的
            if(type==1)//下一条
            {
                //System.out.println("其他新闻中未找到，下一条，取推荐新闻第一个");
                return _newsEntryList.get(0);
            }
            else
            {
                //System.out.println("其他新闻中未找到，上一条，取推荐新闻最后一个");
                return _newsEntryList.get(_newsEntryList.size()-1);
            }
        }
        //System.out.println("其他新闻，正常取");
        return newsEntryList.get(0);
    }
    //阅读量+1
    public void updateReadCount(ObjectId newsId)
    {
        BasicDBObject query=new BasicDBObject();
        query.append(Constant.ID,newsId);
        DBObject obj=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,Constant.FIELDS);
        NewsEntry newsEntry=new NewsEntry((BasicDBObject)obj);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("rc",newsEntry.getReadCount()+1));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_NAME,query,updateValue);
    }
}
