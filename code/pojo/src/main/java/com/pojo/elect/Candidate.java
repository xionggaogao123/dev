package com.pojo.elect;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinbo on 15/2/28.
 * 候选人的相关信息
 *<pre>
 * {
 *  ud:竞选人userid
 *  nm:姓名
 *  mf:竞选宣言
 *  vo:录音连接voiceurl
 *  pics[]:图片连接picurls
 *  vid:视频id,int
 *  tm:报名时间,long
 *  ba[]:选票，数组值为收到某个人的投票，此人的objectID?
 *
 * }
 * </pre>
 *
 * @author qinbo
 */
public class Candidate extends BaseDBObject {

    private static final long serialVersionUID = 7933557018492747487L;

    public Candidate(BasicDBObject dbo){setBaseEntry(dbo);}


    /**
     * 构造器
     *
     */
    public Candidate(ObjectId userId,String name,String manifest,String voiceUrl,List<String> picUrls,
                     ObjectId videoId, long time, List<ObjectId> ballots)
    {
        super();

        BasicDBList pics = null;
        if(picUrls!=null&& picUrls.size()>0)
        {
            pics = new BasicDBList();
            for(String pu : picUrls)
            {
                pics.add(pu);
            }
        }

        BasicDBList ba = null;
        if(ballots!=null && ballots.size()>0)
        {
            ba= new BasicDBList();
            for(ObjectId bl : ballots)
            {
                ba.add(bl);
            }
        }

        BasicDBObject dbo =new BasicDBObject()
                .append("ud",userId)
                .append("nm", name)
                .append("mf", manifest)
                .append("vo", voiceUrl)
                .append("pics", pics)
                .append("vid", videoId)
                .append("tm", time)
                .append("ba", ba)
                ;
        setBaseEntry(dbo);
    }


    public ObjectId getUserId()
    {
        return getSimpleObjecIDValue("ud");
    }
    public void SetUserId(ObjectId userId){
        setSimpleValue("ud",userId);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }


    public String getManifesto() {
        return getSimpleStringValue("mf");
    }
    public void setManifesto(String manifesto) {
        setSimpleValue("mf", manifesto);
    }

    public String getVoiceUrl() {return getSimpleStringValue("vo");}
    public void setVoiceUrl(String voiceUrl){setSimpleValue("vo",voiceUrl);}

    public List<String> getPicUrls() {
        List<String> picUrls = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("pics");
        if(null!=list && !list.isEmpty())
        {
            picUrls =new ArrayList<String>();
            for(Object o:list)
            {
                picUrls.add(o.toString());
            }
        }
        return picUrls;
    }
    public void setPicUrls(List<String> videoUrls) {
        BasicDBList picUrlList = null;
        if(videoUrls!=null && videoUrls.size()>0) {
            picUrlList = new BasicDBList();
            for (String pi : videoUrls) {
                picUrlList.add(pi);
            }
        }
        setSimpleValue("pics", picUrlList);
    }

    public ObjectId getVideoId()
    {
        return getSimpleObjecIDValue("vid");
    }
    public void setVideoId(ObjectId videoId){
        setSimpleValue("vid",videoId);
    }

    public long getSignTime(){
        return getSimpleLongValue("tm");
    }
    public void setSignTime(long signTime){
        setSimpleValue("tm",signTime);
    }

    public List<ObjectId> getBallots() {
        List<ObjectId> ballots = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("ba");
        if(null!=list && !list.isEmpty())
        {
            ballots =new ArrayList<ObjectId>();
            for(Object o:list)
            {
                ballots.add((ObjectId) o);
            }
        }
        return ballots;
    }
    public void setBallots(List<ObjectId> ballots) {
        BasicDBList ballotList = null;
        if(ballots!=null&& ballots.size()>0)
        {
            ballotList = new BasicDBList();
            for(ObjectId ba : ballots)
            {
                ballotList.add(ba);
            }
        }
        setSimpleValue("ba", ballotList);
    }

}
