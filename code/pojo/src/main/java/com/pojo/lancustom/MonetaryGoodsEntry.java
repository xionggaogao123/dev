package com.pojo.lancustom;

import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/6 15:53
 * @Description: 商品Entry
 */
public class MonetaryGoodsEntry extends BaseDBObject {
    public MonetaryGoodsEntry() {

    }

    public MonetaryGoodsEntry(DBObject object) {
        setBaseEntry((BasicDBObject)object);
    }

    public MonetaryGoodsEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MonetaryGoodsEntry(String avatar,
                              String pic,
                              String label,
                              String style,
                              String name,
                              Double money,
                              String description) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("avatar", avatar)
                .append("pic", pic)
                .append("label", label)
                .append("style", style)
                .append("name", name)
                .append("money", money)
                .append("desc", description)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public String getAvatar(){
        return getSimpleStringValue("avatar");
    }

    public void setAvatar(String avatar){
        setSimpleValue("avatar",avatar);
    }

    public String getPic(){
        return getSimpleStringValue("pic");
    }

    public void setPic(String pic){
        setSimpleValue("pic",pic);
    }

    public String getLabel(){
        return getSimpleStringValue("label");
    }

    public void setLabel(String label){
        setSimpleValue("label",label);
    }


    public String getStyle(){
        return getSimpleStringValue("style");
    }

    public void setStyle(String style){
        setSimpleValue("style",style);
    }

    public String getName(){
        return getSimpleStringValue("name");
    }

    public void setName(String name){
        setSimpleValue("name",name);
    }

    public void setMoney(Double money){
        setSimpleValue("money",money);
    }

    public Double getMoney(){
        return Double.parseDouble(getSimpleStringValue("money"));
    }

    public String getDescription(){
        return getSimpleStringValue("desc");
    }

    public void setDescription(String desc){
        setSimpleValue("desc",desc);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
