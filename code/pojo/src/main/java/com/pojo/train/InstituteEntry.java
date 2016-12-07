package com.pojo.train;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.questions.PropertiesObj;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/10/12.
 * {
 *
 * }
 */
public class InstituteEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InstituteEntry(){
        super();
    }

    public InstituteEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public InstituteEntry(String name, String mainPic, String address,  String telephone, String chuangLiShiJian,
                          String shangHuJianJie, String yingYeShiJian, String teSeFuWu, List<PropertiesObj> types,
                          List<PropertiesObj> areas){
        super();

        BasicDBObject baseEntry=new BasicDBObject().append("nm",name)
                .append("mp",mainPic)
                .append("ad",address)
                .append("tp",telephone)
                .append("clsj",chuangLiShiJian)
                .append("shjj",shangHuJianJie)
                .append("yysj",yingYeShiJian)
                .append("tsfw",teSeFuWu)
                .append("tys", MongoUtils.convert(MongoUtils.fetchDBObjectList(types)))
                .append("ars", MongoUtils.convert(MongoUtils.fetchDBObjectList(areas)))
                .append("ir", Constant.ZERO);

        setBaseEntry(baseEntry);
    }

    public String getName(){
        return getSimpleStringValue("nm");

    }

    public void setName(String name){
        setSimpleValue("nm",name);
    }

    public String getMainPic(){
        return getSimpleStringValue("mp");
    }

    public void setMainPic(String mainPic){
        setSimpleValue("mp",mainPic);
    }

    public String getAddress(){
        return getSimpleStringValue("ad");
    }

    public void setAddress(String address){
        setSimpleValue("ad",address);
    }

    public String getTelephone(){
        return getSimpleStringValue("tp");
    }

    public void setTelephone(String telephone){
        setSimpleValue("tp",telephone);
    }

    public String getChuangLiShiJian(){
        return getSimpleStringValue("clsj");
    }

    public void setChuangLiShiJian(String chuangLiShiJian){
        setSimpleValue("clsj",chuangLiShiJian);
    }

    public String getShangHuJianJie(){
        return getSimpleStringValue("shjj");
    }

    public void setShangHuJianJie(String shangHuJianJie){
        setSimpleValue("shjj",shangHuJianJie);
    }

    public String getYingYeShiJian(){
        return getSimpleStringValue("yysj");
    }

    public void setYingYeShiJian(String yingYeShiJian){
        setSimpleValue("yysj",yingYeShiJian);
    }

    public String getTeSeFuWu(){
        return getSimpleStringValue("tsfw");
    }

    public void setTeSeFuWu(String teSeFuWu){
        setSimpleValue("tsfw",teSeFuWu);
    }

    public List<PropertiesObj> getTypes(){
        List<PropertiesObj> resultList =new ArrayList<PropertiesObj>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("tys");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                resultList.add(new PropertiesObj((BasicDBObject)o));
            }
        }
        return resultList;
    }

    public void setTypes(List<PropertiesObj> types){
        List<DBObject> list=MongoUtils.fetchDBObjectList(types);
        setSimpleValue("tys",MongoUtils.convert(list));
    }

    public List<PropertiesObj> getAreas(){
        List<PropertiesObj> resultList =new ArrayList<PropertiesObj>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("ars");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                resultList.add(new PropertiesObj((BasicDBObject)o));
            }
        }
        return resultList;
    }

    public void setAreas(List<PropertiesObj> areas){
        List<DBObject> list=MongoUtils.fetchDBObjectList(areas);
        setSimpleValue("ars",MongoUtils.convert(list));
    }


    public double getScore(){
        if(getBaseEntry().containsField("sc")){
            return getSimpleDoubleValueDef("sc",0);
        }else{
            return 0;
        }
    }

    public void setScore(double score){
        setSimpleValue("sc",score);
    }

}
