package com.fulaan.myschool.service;

import com.db.news.NewsColumnDao;
import com.db.news.NewsDao;
import com.db.school.SchoolDao;
import com.pojo.news.News;
import com.pojo.news.NewsColumn;
import com.pojo.news.NewsColumnEntry;
import com.pojo.news.NewsEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2015/3/16.
 */
@Service
public class NewsService {

    private NewsDao newsDao=new NewsDao();
    private NewsColumnDao newsColumnDao=new NewsColumnDao();
    private SchoolDao schoolDao=new SchoolDao();

    //============================================内容管理部分=============================================
    public void addNews(News news) {
        newsDao.addNews(news.exportEntry());
    }

    public List<News> newsList(int skip, int pageSize,String columnId,String title,String schoolId,String educationId) {
        List<NewsEntry> newsEntryList=newsDao.newsList(skip,pageSize,columnId,title,schoolId,educationId);

        List<News> newsList=new ArrayList<News>();
        List<ObjectId> columnIds=new ArrayList<ObjectId>();
        for(NewsEntry newsEntry:newsEntryList){
            columnIds.add(newsEntry.getColumn());
        }
        List<NewsColumnEntry> newsColumnEntryList=newsColumnDao.getColumnByIds(columnIds);
        for(NewsEntry newsEntry:newsEntryList)
        {
            News news=new News(newsEntry);
            news.setColumn(getNameById(newsColumnEntryList,newsEntry.getColumn()));
            newsList.add(news);
        }
        return newsList;
    }

    public String getNameById(List<NewsColumnEntry> list,ObjectId id)
    {
        for(NewsColumnEntry newsColumnEntry : list)
        {
            if(id.equals(newsColumnEntry.getID()))
                return newsColumnEntry.getColumnName();
        }
        return "";
    }
    public void updateNews(News news) {
        newsDao.updateNews(new ObjectId(news.getId()), news.getTitle(), new ObjectId(news.getColumn()),
                news.getPinned(), news.getThumb(), news.getDigest(), news.getContent());
    }

    public void deleteNewsById(String objectId) {
        newsDao.deleteById(new ObjectId(objectId));
    }

    public void deleteManyNews(List<String> list)
    {
        List<ObjectId> objList=new ArrayList<ObjectId>();
        for(int i=0;i<list.size();i++)
        {
            objList.add(new ObjectId(list.get(i)));
        }
        newsDao.deleteMany(objList);
    }
    public News findOneNewsById(String newsId) {
        NewsEntry newsEntry=newsDao.findNewsById(new ObjectId(newsId));
        if(newsEntry!=null){
            return new News(newsEntry);
        }
        return null;
    }
    public int countPage(String schoolId,String educationId,String columnId,String title) {
        int total=newsDao.countNews(schoolId,educationId,columnId,title);
        return total;
    }
    /*public int countPage(String schoolId,int i) {
        int total=newsDao.countNews(new ObjectId(schoolId));
        return total%i==0?total/i:total/i+1;
    }*/

    /**
     * 根据栏目名获取置顶的条目
     * @param type
     * @return
     */
    public List<NewsEntry> getNoticeAndNews(String schoolName,String type)
    {
        //先根据学校名获取学校Id
        ObjectId schoolId=schoolDao.getSchoolIdByName(schoolName);
        if(schoolId==null)
        {
            return new ArrayList<NewsEntry>();
        }
        //先根据栏目名type获取栏目id
        ObjectId objectId=newsColumnDao.getColumnIdByName(schoolId,type);
        if(objectId==null)//没有该栏目
        {
            return new ArrayList<NewsEntry>();
        }
        else//再根据栏目id获取对应数据
        {
            return newsDao.getNewsListByColumnId(objectId);
        }

    }

    /**
     * 获取非置顶条目，分页
     * @param page
     * @param pageSize
     * @param columnName
     * @param pinnedList
     * @return
     */
    public List<NewsEntry> getOtherNews(int page,int pageSize,String columnName,List<News> pinnedList,String schoolName)
    {
        //先根据学校名获取学校Id
        ObjectId schoolId=schoolDao.getSchoolIdByName(schoolName);
        if(schoolId==null)
        {
            return new ArrayList<NewsEntry>();
        }
        ObjectId objectId=newsColumnDao.getColumnIdByName(schoolId,columnName);
        if(objectId==null)//没有该栏目
        {
            return new ArrayList<NewsEntry>();
        }
        else//再根据栏目id获取对应数据
        {
            List<ObjectId> objectIdList=new ArrayList<ObjectId>();
            for(int i=0;i<pinnedList.size();i++)
            {
                objectIdList.add(new ObjectId(pinnedList.get(i).getId()));
            }
            return newsDao.getOtherNewsListByColumnId(page,pageSize,objectId,objectIdList);
        }
    }

    /**
     * 阅读量+1
     * @param newsId
     */
    public void updateReadCount(String newsId)
    {
        newsDao.updateReadCount(new ObjectId(newsId));
    }
    public int getOtherNewsCount(String columnName,List<News> pinnedList,String schoolName)
    {
        //先根据学校名获取学校Id
        ObjectId schoolId=schoolDao.getSchoolIdByName(schoolName);
        ObjectId objectId=newsColumnDao.getColumnIdByName(schoolId,columnName);
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        for(int i=0;i<pinnedList.size();i++)
        {
            objectIdList.add(new ObjectId(pinnedList.get(i).getId()));
        }
        return newsDao.getOtherNewsCount(objectId,objectIdList);
    }

    /**
     * 查找上一条下一条新闻或公告
     * @param newsId
     * @param columnId
     * @param type
     * @return
     */
    public News getSwitchContent(String newsId,String columnId,int type)
    {
        //先判断该新闻是否在推荐新闻中

        List<NewsEntry> newsEntryList=newsDao.getNewsListByColumnId(new ObjectId((columnId)));
        int index=-1;
        for(int i=0;i<newsEntryList.size();i++)
        {
            if(newsEntryList.get(i).getID().equals(new ObjectId(newsId)))
            {
                //已经找到当前新闻
                if(type==0 && i==0) {
                    //从其他新闻里选择最后一条
                    List<ObjectId> objectIdList=new ArrayList<ObjectId>();
                    for(int j=0;j<newsEntryList.size();j++)
                    {
                        objectIdList.add(newsEntryList.get(j).getID());
                    }
                    //System.out.println("推荐第一条，取上一个");
                    return new News(newsDao.getLastFromOthers(new ObjectId(columnId),objectIdList));//没有上一条了
                }
                else if(type==0) {
                    //System.out.println("推荐第"+i+"条，取上一个");
                    newsDao.updateReadCount(newsEntryList.get(i - 1).getID());
                    return new News(newsEntryList.get(i - 1));
                }
                else if(type==1&&i!=newsEntryList.size()-1)
                {
                   // System.out.println("推荐第"+i+"条，取下一个");
                    newsDao.updateReadCount(newsEntryList.get(i + 1).getID());
                    return new News(newsEntryList.get(i+1));
                }
                else if(type==1&&i==newsEntryList.size()-1)
                {
                    //System.out.println("推荐最后一条，取下一个");
                    index=4;
                }
            }
        }
        if(index==4)//当前新闻是推荐新闻中的最后一个,而且是下一条,取其他新闻第一个
        {
            List<ObjectId> objectIdList=new ArrayList<ObjectId>();
            for(int j=0;j<newsEntryList.size();j++)
            {
                objectIdList.add(newsEntryList.get(j).getID());
            }
            NewsEntry newsEntry=newsDao.getFirstNews(new ObjectId(columnId),objectIdList);
            //System.out.println("推荐最后一条，取其他新闻第一个一个");
            if(newsEntry!=null) {
                newsDao.updateReadCount(newsEntry.getID());
                return new News(newsEntry);
            }
            return new News();
        }
        else//从其他新闻中查找
        {
            //先找到该新闻，再根据上一条还是下一条选择数据
            /*List<ObjectId> objectIdList=new ArrayList<ObjectId>();
            for(int i=0;i<newsEntryList.size();i++)
            {
                objectIdList.add(newsEntryList.get(i).getID());
            }*/
            NewsEntry newsEntry=newsDao.getOtherNewsNotice(new ObjectId(newsId),new ObjectId(columnId),type,newsEntryList);
            if(newsEntry!=null) {
                newsDao.updateReadCount(newsEntry.getID());
                return new News(newsEntry);
            }
            return new News();
        }
        //根据type查找数据
    }
    //===============================================栏目管理部分=============================================

    /**
     * 增加栏目
     * @param newsColumn
     */
    public int addNewsColumn(NewsColumn newsColumn)
    {
        int result=newsColumnDao.checkIfHave(newsColumn.getSchoolId(),newsColumn.getEducationId(),newsColumn.getColumnName(),newsColumn.getColumnDir());
        if(result==0)//可添加
        {
            newsColumnDao.addNewsColumn(newsColumn.exportEntry());
            return 0;
        }
        return result;
    }

    /**
     * 删除栏目
     * @param newsColumnId
     */
    public void deleteNewsColumn(String newsColumnId)
    {
        newsColumnDao.deleteColumn(new ObjectId(newsColumnId));
    }

    /**
     * 判断是否可以删除该栏目，即是否已经有新闻在该栏目下,可以删除返回true
     * @param newsColumnId
     * @return
     */
    public boolean checkIfCanDelete(String newsColumnId)
    {
        int total=newsDao.countNewsByColumn(newsColumnId);
        return total==0?true:false;
    }
    /**
     * 修改栏目
     * @param newsColumn
     */
    public int updateNewsColumn(NewsColumn newsColumn)
    {
        int result=newsColumnDao.checkIfHave(newsColumn.getSchoolId(),newsColumn.getEducationId(),newsColumn.getId(),newsColumn.getColumnName(),newsColumn.getColumnDir());
        if(result==0) {
            newsColumnDao.updateColumn(new ObjectId((newsColumn.getId())), newsColumn.getColumnName(),newsColumn.getColumnDir());
            return 0;
        }
        return result;
    }
    /**
     * 获取栏目列表
     * @param skip
     * @param pageSize
     * @return
     */
    public List<NewsColumn> getNewsColumnList(int skip,int pageSize,String schoolId,String educationId)
    {
        List<NewsColumnEntry> newsColumnEntryList=newsColumnDao.getNewsColumnList(skip,pageSize,schoolId, educationId);
        List<NewsColumn> newsColumnList=new ArrayList<NewsColumn>();
        for(NewsColumnEntry newsColumnEntry:newsColumnEntryList)
        {
            newsColumnList.add(new NewsColumn((newsColumnEntry)));
        }
        return newsColumnList;
    }

    /**
     * 获取全部栏目列表

     * @return
     */
    public List<NewsColumn> getAllNewsColumnList(String schoolId,String educationId)
    {
        List<NewsColumnEntry> newsColumnEntryList=newsColumnDao.getNewsColumnList(schoolId, educationId);
        List<NewsColumn> newsColumnList=new ArrayList<NewsColumn>();
        for(NewsColumnEntry newsColumnEntry:newsColumnEntryList)
        {
            newsColumnList.add(new NewsColumn((newsColumnEntry)));
        }
        return newsColumnList;
    }

    /**
     * 获取栏目总数
     * @param schoolId
     * @return
     */
    public int countColumnPage(String schoolId,String educationId)
    {
        int total=newsColumnDao.countNewsColumn(schoolId, educationId);
        return total;
    }

    /**
     * 获取某一栏目信息
     * @param newsColumnId
     * @return
     */
    public NewsColumn findOneNewsColumnById(String newsColumnId) {
        NewsColumnEntry newsColumnEntry=newsColumnDao.findNewsColumnById(new ObjectId(newsColumnId));
        if(newsColumnEntry!=null){
            return new NewsColumn(newsColumnEntry);
        }
        return null;
    }

    /**
     * 批量删除多个栏目
     * @param list
     */
    public void deleteManyNewsColumn(List<String> list)
    {
        List<ObjectId> objList=new ArrayList<ObjectId>();
        for(int i=0;i<list.size();i++)
        {
            objList.add(new ObjectId(list.get(i)));
        }
        newsColumnDao.deleteManyColumn(objList);
    }
}
