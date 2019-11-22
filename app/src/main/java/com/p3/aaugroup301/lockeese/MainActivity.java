package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    byte[] cipherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EncryptionHandler keyGeneration = new EncryptionHandler();
        try {
            String plainText = "Success!!!";
            byte[] byteText = plainText.getBytes();

            Log.d("encrypt", "encrypt plain text: " + plainText);
            Log.d("encrypt", "encrypt plain byte text: " + byteText);

            SecretKey secretKey = keyGeneration.symmetricKeyGeneration();
            CipherInfo cipherInfo = keyGeneration.symmetricEncrypt(byteText,secretKey);
            cipherText = cipherInfo.getBytes();

            Log.d("encrypt", "encrypt cipher byte text: " + cipherText);

            String cipherStringText = new String(cipherText);

            Log.d("encrypt", "encrypt cipher text: " + cipherStringText);

            byte[] decryptedText = keyGeneration.symmetricDecrypt(cipherText,cipherInfo.getIv(),secretKey);

            Log.d("encrypt", "decrypt cipher byte text: " + decryptedText);

            String decyptedStringText = new String(decryptedText);

            Log.d("encrypt", "decrypted text: " + decyptedStringText);


            KeyPair keyPair = keyGeneration.asymmetricKeyGeneration();
            CipherInfo asymmetricCipherInfo = keyGeneration.asymmetricEncrypt(byteText,keyPair);
            byte[] asymmetricCipherText = asymmetricCipherInfo.getBytes();

            Log.d("encrypt", "asymmetricEncrypt cipher text: " + asymmetricCipherText);

            byte[] asymmetricDecryptedBytes = keyGeneration.asymmetricDecrypt(asymmetricCipherInfo);

            Log.d("encrypt", "asymmetricDecrypt cipher byte text: " + asymmetricDecryptedBytes);

            String asymmetricDecyptedStringText = new String(asymmetricDecryptedBytes);

            Log.d("encrypt", "asymmetricDecrypt cipher text: " + asymmetricDecyptedStringText);






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
