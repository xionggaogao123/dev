package com.db.questionnaire;

import com.pojo.questionnaire.QuestionnaireEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinbo on 15/4/21.
 */
public class QuestionnaireDaoTest {

    QuestionnaireDao dao = new QuestionnaireDao();

    @Test
    public void addQuestionnaire()
    {
        for(int i=0;i<30;i++) {
            List<Integer> ans = new ArrayList<Integer>();
            ans.add(4);
            ans.add(0);
            ans.add(-4);
            QuestionnaireEntry questionnaireEntry = new QuestionnaireEntry(
                    "测试调查选举3",
                    new ObjectId("552e05fdf6f27442d6b52fca"),
                    System.currentTimeMillis(),
                    System.currentTimeMillis() + 1000 * 60 * 60 * 12 * 20,
                    new ObjectId("552e05f8f6f27442d6b5143c"),
                    null,
                    1,
                    1,
                    1,
                    1,
                    "docurl_null",
                    ans,
                    null,
                    0,
                    0

            );

            dao.add(questionnaireEntry);

        }
    }
}
