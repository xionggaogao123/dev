package com.fulaan.groupcollectmoney.service;

import com.db.groupcollectmoney.AplipayUserDao;
import com.pojo.groupcollectmoney.AplipayUserEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by scott on 2018/1/17.
 */
@Service
public class GroupCollectMoneyService {

      private AplipayUserDao aplipayUserDao = new AplipayUserDao();

      public void saveApliPayUser(String rsaPrivateUserId, ObjectId userId){
          AplipayUserEntry entry = new AplipayUserEntry(userId,rsaPrivateUserId);
          aplipayUserDao.saveAplipayUserEntry(entry);
      }







}
