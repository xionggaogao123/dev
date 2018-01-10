package com.fulaan.moneysign;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Created by scott on 2018/1/10.
 */
public class KeyPairUtil {
    // 采用的双钥加密算法，既可以用DSA，也可以用RSA
    public static final String KEY_ALGORITHM = "DSA";

    /**
     * 从输入流中获取KeyPair对象
     * @param keyPairStream
     * @return
     */
    public static KeyPair loadKeyPair(InputStream keyPairStream) {
        if (keyPairStream == null) {
            System.out.println("指定的输入流=null!因此无法读取KeyPair!");
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(keyPairStream);
            KeyPair keyPair = (KeyPair) ois.readObject();
            ois.close();
            return keyPair;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 将整个KeyPair以对象形式存储在OutputStream流中， 当然也可以将PublicKey和PrivateKey作为两个对象分别存到两个OutputStream流中，
     * 从而私钥公钥分开，看需求而定。
     * @param keyPair 公钥私钥对对象
     * @param out 输出流
     * @return
     */
    public static boolean storeKeyPair(KeyPair keyPair, OutputStream out) {
        if ((keyPair == null) || (out == null)) {
            System.out.println("keyPair=" + keyPair + ", out=" + out);
            return false;
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(keyPair);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 生成KeyPair公钥私钥对
     *
     * @return
     */
    public static KeyPair initKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        return keyPairGen.genKeyPair();

    }

    /**
     * 生成密钥，并存储
     * @param out
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static boolean initAndStoreKeyPair(OutputStream out) throws NoSuchAlgorithmException {
        return storeKeyPair(initKeyPair(), out);
    }
}
