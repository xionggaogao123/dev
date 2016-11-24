package com.pojo.elect;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by qinbo on 15/6/11.
 */
public class OldCandidate extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4870445146517065882L;

	public OldCandidate(BasicDBObject dbo){setBaseEntry(dbo);}




    public Integer getId() {
        return getSimpleIntegerValue("_id");
    }

    public String getName() {
        return getSimpleStringValue("name");
    }



    public String getManifesto() {
        return getSimpleStringValue("manifesto");
    }



    public String getVoiceUrl() {
        return getSimpleStringValue("voiceUrl");
    }



    public List<String> getPicUrls() {
        List<String> picUrls = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("candidates");
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


    public Integer getVideoId() {
        return getSimpleIntegerValue("videoId");
    }


    public Object getSignTime() {
        return getSimpleObjectValue("signTime");
    }

    public List<Integer> getBallots() {
        List<Integer> ballots = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("ballots");
        if(null!=list && !list.isEmpty())
        {
            ballots =new ArrayList<Integer>();
            for(Object o:list)
            {
                ballots.add((int)Double.parseDouble(o.toString()));
            }
        }
        return ballots;
    }

}
