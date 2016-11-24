package com.fulaan.letter;

import com.fulaan.letter.service.LetterService;
import com.pojo.letter.*;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinbo on 15/4/25.
 */
public class LetterTest {
    private LetterService letterService = new LetterService();

    @Test
    public void sendLetterTest1(){

        List<ObjectId> receiverIds = new ArrayList<ObjectId>();
        receiverIds.add(new ObjectId("553b23d77f72b0ab98594e90"));
        receiverIds.add(new ObjectId("553b23d77f72b0ab98594e80"));

        LetterEntry letterEntry = new LetterEntry(new ObjectId("553b23d77f72b0ab98594e51"),
                "私信测试1",
                receiverIds
                );
        letterEntry.setSenderId(new ObjectId("553b23d77f72b0ab98594e51"));



        letterService.sendLetter(letterEntry);

    }

}
