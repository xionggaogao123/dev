package com.fulaan.moneysign;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * Created by scott on 2018/1/10.
 */
public class TestSignUtils {

    public static void main(String[] args){
        // 1.生成证书
//        File file = new File("D:\\cakey\\ca.key");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            KeyPairUtil.initAndStoreKeyPair(fileOutputStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 2.生成数字签名
//        DataSecurity dataSecurity = new DataSecurity();
//        String sign = dataSecurity.sign("大家好");
//        System.out.println("sign:" + sign);

        //3.验证数字签名
        DataSecurity security = new DataSecurity();
        boolean result = security.verifySign("大家好", "MCwCFCKBLhTXcDFs5e0C5RftKb0KV8RGAhRRFfl9XYDUc/MeUgIabvYfLWk8aA==");
        System.out.println("result：" + result);
    }

}
