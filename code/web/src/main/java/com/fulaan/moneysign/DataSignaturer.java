package com.fulaan.moneysign;

import java.security.*;

/**
 * Created by scott on 2018/1/10.
 */
public class DataSignaturer {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public DataSignaturer(PublicKey publicKey, PrivateKey privateKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    /**
     * 进行数字签名
     * @param data
     * @return
     */
    public byte[] sign(byte[] data) {
        if (this.privateKey == null) {
            System.out.println("privateKey is null");
            return null;
        }
        Signature signer = null;
        try {
            signer = Signature.getInstance(this.privateKey.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        try {
            signer.initSign(this.privateKey);
        } catch (InvalidKeyException e) {
            System.out.println(e.getMessage());
        }
        try {
            signer.update(data);
            return signer.sign();
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 验证数字签名
     * @param data
     * @param signature
     * @return
     */
    public boolean verifySign(byte[] data, byte[] signature) {
        if (this.publicKey == null) {
            System.out.println("publicKey is null");
            return false;
        }
        Signature signer = null;
        try {
            signer = Signature.getInstance(this.publicKey.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            signer.initVerify(this.publicKey);
        } catch (InvalidKeyException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            signer.update(data);
            return signer.verify(signature);
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
