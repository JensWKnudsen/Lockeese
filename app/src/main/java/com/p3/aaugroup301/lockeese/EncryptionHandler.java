package com.p3.aaugroup301.lockeese;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**Class KeyGenerator generates Keys on the phone of the user and encrypts it
 *  with AES 256 encryption
  */


public class EncryptionHandler {

    public KeyPair asymmetricKeyGeneration() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public CipherInfo asymmetricEncrypt(byte[] inputText, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher;

        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //ISO10126Padding //PKCS1Padding
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] ciphertext = cipher.doFinal(inputText);

        CipherInfo cipherInfo = new CipherInfo(ciphertext);
        cipherInfo.setPrivateKey(privateKey);

        return cipherInfo;
    }

    public CipherInfo asymmetricEncrypt(byte[] inputText, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher;

        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //ISO10126Padding //PKCS1Padding
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        Log.d("encrypt", "test 1: " + Arrays.toString(inputText));
        byte[] ciphertext = cipher.doFinal(inputText);
        Log.d("encrypt", "test 2");
        CipherInfo cipherInfo = new CipherInfo(ciphertext);
        cipherInfo.setPublicKey(publicKey);

        return cipherInfo;
    }

    public byte[] asymmetricDecrypt(byte[] bytes, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //ISO10126Padding

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] plaintext = cipher.doFinal(bytes);

        return plaintext;
    }

    public byte[] asymmetricDecrypt(byte[] bytes, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //ISO10126Padding

        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] plaintext = cipher.doFinal(bytes);

        return plaintext;
    }











    public SecretKey symmetricKeyGeneration() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator;
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        return keyGenerator.generateKey();
    }

    public CipherInfo symmetricEncrypt(byte[] inputText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher;

        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //ISO10126Padding
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();

        Log.d("encrypt","Length of iv: " + String.valueOf(iv.length));
        Log.d("encrypt","iv: " + iv);


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

    public KeyAgreement DHKeyAgreement(KeyPair appDHKpair) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement appKeyAgree = KeyAgreement.getInstance("DH");
        appKeyAgree.init(appDHKpair.getPrivate());
        return appKeyAgree;
    }
}
