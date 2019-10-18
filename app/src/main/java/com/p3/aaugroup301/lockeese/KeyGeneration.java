package com.p3.aaugroup301.lockeese;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**Class KeyGenerator generates Keys on the phone of the user and encrypts it
 *  with AES 256 encryption
  */


public class KeyGeneration {
    private byte[] generateKey;
    KeyGenerator keyGenerator;

    {
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    SecretKey phoneKey = keyGenerator.generateKey();

    Cipher cipher;

    {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //ISO10126Padding
            cipher.init(Cipher.ENCRYPT_MODE, phoneKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
   private byte[] ciphertext;
   private byte[] iv = cipher.getIV();

    {
        try {
            ciphertext = cipher.doFinal(generateKey);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
