package com.p3.aaugroup301.lockeese;

import javax.crypto.SecretKey;

public class CipherInfo {

    private SecretKey secretKey;
    private byte[] bytes;
    private byte[] iv;

    public CipherInfo(SecretKey secretKey,byte[] bytes,byte[] iv){
        this.secretKey = secretKey;
        this.bytes = bytes;
        this.iv = iv;
    }

    public SecretKey getSecretKey(){
        return secretKey;
    }
    public byte[] getBytes(){
        return bytes;
    }
    public byte[] getIv(){
        return iv;
    }

}
