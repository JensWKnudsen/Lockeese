package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    byte[] cipherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyGeneration keyGeneration = new KeyGeneration();
        try {
            String plainText = "test";
            byte[] byteText = plainText.getBytes();

            Log.d("encrypt", "encrypt plain text: " + plainText);
            Log.d("encrypt", "encrypt plain byte text: " + byteText);

            CipherInfo cipherInfo = keyGeneration.symmetricEncrypt(byteText);
            cipherText = cipherInfo.getBytes();

            Log.d("encrypt", "encrypt cipher byte text: " + cipherText);

            String cipherStringText = new String(cipherText);

            Log.d("encrypt", "encrypt cipher text: " + cipherStringText);

            byte[] decryptedText = keyGeneration.symmetricDecrypt(cipherText,cipherInfo.getSecretKey(),cipherInfo.getIv());

            Log.d("encrypt", "decrypt cipher byte text: " + decryptedText);

            String decyptedStringText = new String(decryptedText);

            Log.d("encrypt", "decrypt cipher text: " + decyptedStringText);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


    }
}
