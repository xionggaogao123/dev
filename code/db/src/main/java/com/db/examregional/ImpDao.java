package com.db.examregional;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.examregional.EducationSubject;
import com.sys.constants.Constant;

public class ImpDao extends BaseDao {
    /**
     * 新增科目
     */
    public void save(EducationBureauEntry edu) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, edu.getBaseEntry());
    }
}
