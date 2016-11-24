package com.db.temp;

import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sys.constants.Constant;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.omg.CORBA.INTERNAL;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by admin on 2016/9/1.
 */
public class FPostTerm {

    public static void main(String[] args) throws IOException{

//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id", new ObjectId("57b260a7de04cb7cb30d6fbf")),
//                        new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("prc", 1028)));
//
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("579acab4de04cb783d017a27")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",1008)));
//
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("57b2bf60de04cb7cb30da297")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",873)));
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("57af33403d4df92a03184565")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",1136)));
//
//
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("57af44dc3d4df92a031846d4")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",987)));
//
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("57afce4dde04cb5082f91157")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",873)));
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("57b3344ede04cb7cb30dd6ad")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",764)));
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("579e98883d4df937a1b27498")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",725)));
//
//
//
//        MongoFacroty.getAppDB().getCollection("'freply'")
//                .update(new BasicDBObject("_id",new ObjectId("579e9a2bde04cb4774f8ccdd")),
//                        new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prc",656)));


        Map<String,Integer> map=new HashMap<String,Integer>();
//        String item1="577dfd600cf271ce42b240f3";
//        String item2="577e14560cf271ce42b24222";
//        String item3="57bec6743d4df95eda9e6284";
//        String item4="577f47b30cf23ce04771a5b6";
//        String item5="577f46570cf271ce42b24a42";
//        String item6="57bef9523d4df95eda9e9741";
        String item7="579c6a8bde04cb4774f8ba80";
        String item8="57bacdcede04cb16ae6ad95e";
//        map.put(item1,332);
//        map.put(item2,356);
//        map.put(item3,305);
//        map.put(item4,586);
//        map.put(item5,349);
//        map.put(item6,328);
        map.put(item7,425);
        map.put(item8,605);
//        String item="5787538d0cf214bbce6809b8";
//        map.put(item,20);



        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            List<DBObject> f = getOpen(new ObjectId(entry.getKey()));
            List<ObjectId> ff = getIds(f);
            System.out.println("添加前"+ff.size());

            File file =new File("/home/test/term.txt");
//            if(!file.exists()){
//                file.mkdir();
//            }
            file.createNewFile();

            try{
                FileUtils.write(file, "\r\n",true);
                FileUtils.write(file, entry.getKey()+":",true);
                FileUtils.write(file, "\r\n",true);
                for(ObjectId id:ff){
                    FileUtils.write(file, id.toString(),true);
                    FileUtils.write(file, "\r\n",true);
                }
                FileUtils.write(file, "\r\n",true);
            }catch (Exception e){
                e.printStackTrace();
            }

//            System.out.println(entry.getValue() - ff.size());
//            for (int i = 0; i < entry.getValue() - ff.size(); i++) {
//                ff.add(new ObjectId());
//            }
            List<ObjectId> kl =new ArrayList<ObjectId>();
//            for(int i=0;i<ff.size();i++){
//                kl.add(ff.get(i));
//            }
//            System.out.println("添加后"+entry.getKey()+"::::"+kl.size());
            for(int i=0;i<entry.getValue();i++){
                kl.add(new ObjectId());
            }
            System.out.println("添加后"+entry.getKey()+"::::"+kl.size());
            setData(new ObjectId(entry.getKey()), kl,entry.getValue());


            List<DBObject> fl = getOpen(new ObjectId(entry.getKey()));
            List<ObjectId> ffl = getIds(fl);
            System.out.println("处理完后："+ffl.size());
        }


    }


    public static void setData(ObjectId id ,List<ObjectId> ids,int prc)
    {

        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        MongoFacroty.getAppDB().getCollection("fpost").update(query, new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("url", ids)));
        MongoFacroty.getAppDB().getCollection("fpost").update(query,new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("prc", prc)));
    }

    public static List<DBObject> getOpen(ObjectId id)
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        List<DBObject> l=MongoFacroty.getAppDB().getCollection("fpost").find(query).sort(Constant.MONGO_SORTBY_DESC).skip(0).limit(1).toArray();
        return l;
    }




    public static List<ObjectId> getIds(List<DBObject> list)
    {
        DBObject object=list.get(0);
        return (List<ObjectId>)object.get("url");



    }
}
