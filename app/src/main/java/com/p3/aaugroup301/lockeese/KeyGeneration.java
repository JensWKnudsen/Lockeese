package com.p3.aaugroup301.lockeese;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    private byte[] ciphertext;
    private byte[] iv;

    public CipherInfo symmetricEncrypt(byte[] inputText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        KeyGenerator keyGenerator;
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        SecretKey symmetricPhoneKey = keyGenerator.generateKey();

        Cipher cipher;

        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //ISO10126Padding
        cipher.init(Cipher.ENCRYPT_MODE, symmetricPhoneKey);

        iv = cipher.getIV();

        Log.d("encrypt", "encrypt IV: " + iv);

        ciphertext = cipher.doFinal(inputText);

        CipherInfo cipherInfo = new CipherInfo(symmetricPhoneKey,ciphertext,iv);

        return cipherInfo;
    }

    private byte[] plaintext;

    public byte[] symmetricDecrypt(byte[] inputText, SecretKey symmetricPhoneKey, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        Cipher cipher;
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //ISO10126Padding
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, symmetricPhoneKey, ivParameterSpec);

        plaintext = cipher.doFinal(inputText);

        return plaintext;
    }
}
