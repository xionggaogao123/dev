package com.fulaan.moneysign;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by scott on 2018/1/10.
 */
public class StringHelper {
    /**
     * BASE64Encoder 加密
     * @param data 要加密的数据
     * @return 加密后的字符串
     */
    public static String encryptBASE64(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(data);
        return encode;
    }

    /**
     * BASE64Decoder 解密
     * @param data 要解密的字符串
     * @return 解密后的byte[]
     * @throws Exception
     */
    public static byte[] decryptBASE64(String data) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buffer = decoder.decodeBuffer(data);
        return buffer;
    }
}
