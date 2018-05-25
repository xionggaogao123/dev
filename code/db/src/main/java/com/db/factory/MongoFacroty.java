package com.db.factory;

import com.mongodb.*;
import com.sys.props.Resources;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/14.
 */
public class MongoFacroty {
    private final static Logger logger = Logger.getLogger(MongoFacroty.class);

    /**
     * 资源数据库，用于资源集中管理
     */


    private MongoFacroty() {
    }

    private final static String APP = "k6kt_new";

    private final static String cloud = "cloud";

    private final static String res = "res";

    private final static String jxm = "jxmDB";

    private static Mongo mongo = null;

    static {
        try {
           // MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(500).threadsAllowedToBlockForConnectionMultiplier(500).connectTimeout(1000 * 60 * 10).build();
            MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(500).threadsAllowedToBlockForConnectionMultiplier(500).connectTimeout(1000 * 10).socketTimeout(1000*5).build();
            List<ServerAddress> serverAddressList = new ArrayList<ServerAddress>();
            String[] mongodbhosts = Resources.getProperty("mongo3.db.host").split(",");
            for (String dbhost : mongodbhosts) {
                ServerAddress maddress = new ServerAddress((dbhost), Resources.getIntProperty("mongo3.db.port"));
                serverAddressList.add(maddress);
            }

            List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();

            if (StringUtils.isNotBlank(Resources.getProperty("mongo3.db.auth"))) {
                String[] auths = Resources.getProperty("mongo3.db.auth").split("\\|");
                for (String auth : auths) {
                    String[] userInfoArr = auth.split(",");
                    mongoCredentialList.add(MongoCredential.createScramSha1Credential(userInfoArr[0], userInfoArr[2], userInfoArr[1].toCharArray()));
                }
            }

            mongo = new MongoClient(serverAddressList, mongoCredentialList, options);
        } catch (Exception ex) {
            logger.error("create appMongo error!the host= " + Resources.getProperty("mongo3.db.host") + ",port ="
                    + Resources.getIntProperty("mongo.db.port"));
        }
    }

    /**
     * 应用数据库--获取k6kt数据库链接
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static DB getAppDB() {
        DB db = mongo.getDB(APP);
        return db;
    }


    /**
     * 资源数据库--获取云平台数据库链接
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static DB getResDB() {
        DB db = mongo.getDB(res);
        return db;
    }

    /**
     * 资源数据库--资源数据库
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static DB getCloudAppDB() {
        DB db = mongo.getDB(cloud);
        return db;
    }
}
