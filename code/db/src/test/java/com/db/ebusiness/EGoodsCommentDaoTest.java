package com.db.ebusiness;

import com.db.user.UserDao;
import com.pojo.app.IdValuePair;
import com.pojo.ebusiness.EGoodsCommentEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fl on 2016/1/15.
 */
public class EGoodsCommentDaoTest {
    private EGoodsCommentDao eGoodsCommentDao = new EGoodsCommentDao();
    private EGoodsDao eGoodsDao = new EGoodsDao();
    private UserDao userDao = new UserDao();

    @Test
    public void addComments(){
//        ObjectId userId = new ObjectId("55934c26f6f28b7261c1baf1");//bob

        List<IdValuePair> imgs = new ArrayList<IdValuePair>();
        IdValuePair idValuePair0 = new IdValuePair(new ObjectId(), "http://7xiclj.com1.z0.glb.clouddn.com/56a1da990cf21fa3b6b552e9.jpg");
        IdValuePair idv1 = new IdValuePair(new ObjectId(),"http://7xiclj.com1.z0.glb.clouddn.com/56a1daa20cf21fa3b6b552eb.jpg");


//        List<ObjectId> userIdList = new ArrayList<ObjectId>();
//        userIdList.add(new ObjectId("55934c26f6f28b7261c1bab2"));
//        userIdList.add(new ObjectId("55934c26f6f28b7261c1bab4"));
//        userIdList.add(new ObjectId("55934c26f6f28b7261c1bab6"));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
//        userIdList.add(new ObjectId(""));
        List<ObjectId> userIdList = userDao.getUserForEBusiness(new ObjectId("55934c14f6f28b7261c19c62"), Constant.FIELDS);




        List<String> contentList = new ArrayList<String>();
        contentList.add("超级赞");
        contentList.add("价格很合理！快递服务也很不错");
        contentList.add("真的很赞，物流也很好");
        contentList.add("查了，是正品！");
//        contentList.add("");
//        contentList.add("");
//        contentList.add("");
//        contentList.add("");
//        contentList.add("");
//        contentList.add("");
//        contentList.add("");
























        int[] scores = new int[]{5,5,4,5,4,5,5,5,4,4,5,5,5,5,4,3,5,5,4,5,4,5,5,5,5,5,5,5,5,5};
        ObjectId goodsId = new ObjectId("56a0ae152dac23106007c961");
        Random random = new Random();
        for(int i=0; i<contentList.size(); i++){
            int score = scores[i];
            int j = random.nextInt(200);
            EGoodsCommentEntry entry = new EGoodsCommentEntry(userIdList.get(j),goodsId,null, contentList.get(i), score, imgs);
            ObjectId commentId = eGoodsCommentDao.addEGoodsCommentEntry(entry);
            System.out.println(commentId.toString());
        }

        eGoodsDao.increaseSellCount(goodsId, 15);

    }
}
