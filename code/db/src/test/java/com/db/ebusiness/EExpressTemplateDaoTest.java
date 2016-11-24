package com.db.ebusiness;

import com.pojo.ebusiness.EExpressTemplateEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/10.
 */
public class EExpressTemplateDaoTest {
    private EExpressTemplateDao dao = new EExpressTemplateDao();

    @Test
    public void addEExpTempEntryTest(){
        EExpressTemplateEntry entry = new EExpressTemplateEntry();
        List<EExpressTemplateEntry.ExpTempDetail> details = new ArrayList<EExpressTemplateEntry.ExpTempDetail>();
        details.add(new EExpressTemplateEntry.ExpTempDetail("SH,JS,ZJ","上海、江苏、浙江",10,5));
        details.add(new EExpressTemplateEntry.ExpTempDetail("HK,AM,TW","香港、澳门、台湾",30,10));
        details.add(new EExpressTemplateEntry.ExpTempDetail("BJ,AH,JX","北京、安徽、江西",10,5));
        entry.setName("模板test1");
        entry.setDetails(details);
        ObjectId id = dao.add(entry);
        System.out.println(id);
    }

    @Test
    public void getEntryListTest(){
        List<EExpressTemplateEntry> result = dao.getEntryList(1,20);
        System.out.println(result.size());
    }

    @Test
    public void getEntryByIdTest(){
        ObjectId id = new ObjectId("56e0d9c27c1f6ee2642e7032");
        EExpressTemplateEntry entry = dao.getEntryById(id);
        System.out.println(entry.getName());
    }

    @Test
    public void updateTest(){
        EExpressTemplateEntry updateValue = new EExpressTemplateEntry();

        List<EExpressTemplateEntry.ExpTempDetail> details = new ArrayList<EExpressTemplateEntry.ExpTempDetail>();
        details.add(new EExpressTemplateEntry.ExpTempDetail("SH,JS,ZJ","上海、江苏、浙江",10,5));
        details.add(new EExpressTemplateEntry.ExpTempDetail("HK,AM,TW","香港、澳门、台湾",30,10));
        details.add(new EExpressTemplateEntry.ExpTempDetail("BJ,AH,JX", "北京、安徽、江西", 10, 5));
        updateValue.setID(new ObjectId("56e0d9c27c1f6ee2642e7032"));
        updateValue.setName("TempTest1");
        updateValue.setDetails(details);

        dao.update(updateValue);
    }

    @Test
    public void deleteTest(){
        dao.delete(new ObjectId("56e0de307c1f560d354f10f3"));
    }
}
