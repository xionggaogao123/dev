//package com.db.temp;
//
//import com.db.ebusiness.EGoodsCommentDao;
//import com.db.ebusiness.EGoodsDao;
//import com.pojo.app.NameValuePair;
//import com.pojo.ebusiness.EGoodsCommentEntry;
//import com.pojo.ebusiness.EGoodsEntry;
//import com.sys.constants.Constant;
//import org.bson.types.ObjectId;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by fl on 2016/1/26.
// */
//public class EGoodsAddCommentSummary {
//    private EGoodsDao eGoodsDao = new EGoodsDao();
//    private EGoodsCommentDao eGoodsCommentDao = new EGoodsCommentDao();
//
//    private void addCommentSummary(){
//        List<EGoodsEntry> eGoodsEntryList = eGoodsDao.findEGoods(0,50);
//        for(EGoodsEntry eGoodsEntry : eGoodsEntryList){
//            List<EGoodsCommentEntry> commentList = eGoodsCommentDao.getEGoodsCommentEntrys(null, eGoodsEntry.getID(), 0, 0, 50);
//            eGoodsEntry.setCommentSummary(commentSummary(commentList));
//            ObjectId newId = eGoodsDao.addEGoodsEntry(eGoodsEntry);
////            System.out.println(goodsId + "\t" + newId);
//        }
//
//    }
//
//    private List<NameValuePair> commentSummary(List<EGoodsCommentEntry> commentList){
//        List<NameValuePair> commentSummary = new ArrayList<NameValuePair>();
//        NameValuePair pair1 = new NameValuePair("5", 0);
//        NameValuePair pair2 = new NameValuePair("4", 0);
//        NameValuePair pair3 = new NameValuePair("3", 0);
//        NameValuePair pair4 = new NameValuePair("2", 0);
//        NameValuePair pair5 = new NameValuePair("1", 0);
//
//        if(null != commentList){
//            for(EGoodsCommentEntry entry : commentList){
//                int score = entry.getScore();
//                if(score == 5){
//                    pair1.setValue((Integer)pair1.getValue() + 1);
//                }
//                if(score == 4){
//                    pair2.setValue((Integer)pair2.getValue() + 1);
//                }
//                if(score == 3){
//                    pair3.setValue((Integer)pair3.getValue() + 1);
//                }
//                if(score == 2){
//                    pair4.setValue((Integer)pair4.getValue() + 1);
//                }
//                if(score == 1){
//                    pair5.setValue((Integer)pair5.getValue() + 1);
//                }
//            }
//        }
//
//        commentSummary.add(pair1);
//        commentSummary.add(pair2);
//        commentSummary.add(pair3);
//        commentSummary.add(pair4);
//        commentSummary.add(pair5);
//        return commentSummary;
//    }
//    //==============================================================================
//    private void addPopularLevel(){
//        List<EGoodsEntry> eGoodsEntryList = eGoodsDao.findEGoods(0,50);
//        Map<String, Integer> map = new HashMap<String, Integer>();
//        map.put("56a0a1be2dacd9eda6b99b01", 1);
//        map.put("56a0a9612dac932c51550797", 2);
//        map.put("56a0a37e2dacb8f47c3120f0", 3);
//        map.put("56a09b3e2dacb158f48fd16e", 4);
//        map.put("56a0be631fa87018fcb6fee0", 5);
//
//        map.put("56a0be9f1fa87018fcb6fee1", 6);
//        map.put("56a0be151fa87018fcb6fedf", 7);
//        map.put("56a0bd7a1fa87018fcb6fede", 8);
//        map.put("56a0aca12daca6fe9159e46a", 9);
//        map.put("56a0ab302dac66cf7004b546", 10);
//        for(EGoodsEntry eGoodsEntry : eGoodsEntryList){
//            int level = 10000;
//            String id = eGoodsEntry.getID().toString();
//            if(map.containsKey(id)){
//                level = map.get(id);
//            }
//            eGoodsEntry.setPopularLevel(level);
//            eGoodsDao.addEGoodsEntry(eGoodsEntry);
//        }
//    }
//
//    public static void main(String[] args){
//        new EGoodsAddCommentSummary().addCommentSummary();
//    }
//}
