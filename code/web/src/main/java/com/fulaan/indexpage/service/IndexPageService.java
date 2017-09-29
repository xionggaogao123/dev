package com.fulaan.indexpage.service;

import com.db.operation.AppCommentDao;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/28.
 */
@Service
public class IndexPageService {
    private AppCommentDao appCommentDao = new AppCommentDao();




    public List<Map<String,Object>> getIndexList(ObjectId userId,int page,int pageSize){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();








        return list;
    }

}
