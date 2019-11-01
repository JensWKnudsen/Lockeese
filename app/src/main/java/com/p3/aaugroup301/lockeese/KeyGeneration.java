package com.p3.aaugroup301.lockeese;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**Class KeyGenerator generates Keys on the phone of the user and encrypts it
 *  with AES 256 encryption
  */


public class KeyGeneration {

    public KeyPair asymmetricKeyGeneration() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public CipherInfo asymmetricEncrypt(byte[] inputText, KeyPair keyPair) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Cipher cipher;

        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //ISO10126Padding
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] ciphertext = cipher.doFinal(inputText);

        CipherInfo cipherInfo = new CipherInfo(ciphertext);
        cipherInfo.setPrivateKey(privateKey);
        cipherInfo.setPublicKey(publicKey);

        return cipherInfo;
    }

    public byte[] asymmetricDecrypt(CipherInfo cipherInfo) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //ISO10126Padding

        cipher.init(Cipher.DECRYPT_MODE, cipherInfo.getPrivateKey());

        byte[] plaintext = cipher.doFinal(cipherInfo.getBytes());

        return plaintext;
    }











    public SecretKey symmetricKeyGeneration() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator;
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        return keyGenerator.generateKey();
    }

    public CipherInfo symmetricEncrypt(byte[] inputText,SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher;

        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //ISO10126Padding
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();

        byte[] ciphertext = cipher.doFinal(inputText);

        CipherInfo cipherInfo = new CipherInfo(ciphertext);
        cipherInfo.setSecretKey(secretKey);
        cipherInfo.setIv(iv);

        return cipherInfo;
    }

    public byte[] symmetricDecrypt(byte[] inputText, byte[] iv, SecretKey symmetricPhoneKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        Cipher cipher;
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //ISO10126Padding
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, symmetricPhoneKey, ivParameterSpec);

        byte[] plaintext = cipher.doFinal(inputText);

        return plaintext;
    }
}
