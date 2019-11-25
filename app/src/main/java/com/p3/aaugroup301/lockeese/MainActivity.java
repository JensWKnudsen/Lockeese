package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    byte[] cipherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        EncryptionHandler keyGeneration = new EncryptionHandler();
        try {
            PiDiffieHelman piDiffieHelman = new PiDiffieHelman();
            Log.d("encrypt", "Start");
            piDiffieHelman.piStart();

            /*
            String plainText = "request for key";
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
            CipherInfo asymmetricCipherInfo = keyGeneration.asymmetricEncrypt(byteText,keyPair.getPublic());
            byte[] asymmetricCipherText = asymmetricCipherInfo.getBytes();

            Log.d("encrypt", "asymmetricEncrypt cipher text: " + asymmetricCipherText);

            byte[] asymmetricDecryptedBytes = keyGeneration.asymmetricDecrypt(asymmetricCipherInfo.getBytes(),keyPair.getPrivate());

            Log.d("encrypt", "asymmetricDecrypt cipher byte text: " + asymmetricDecryptedBytes);

            String asymmetricDecyptedStringText = new String(asymmetricDecryptedBytes);

            Log.d("encrypt", "asymmetricDecrypt cipher text: " + asymmetricDecyptedStringText);


            Log.d("encrypt", "test" + Arrays.toString(cipherInfo.getIv()));

            int lengthOfIV = cipherInfo.getIv().length;



            byte[] lengthOfIVInBytes = ByteBuffer.allocate(8).putInt(lengthOfIV).array();

            Log.d("encrypt", "symmetric encrypted final message iv length: " +  Arrays.toString(lengthOfIVInBytes));

            byte[] messageToApp = new byte[8 + cipherInfo.getIv().length + cipherInfo.getBytes().length];

            System.arraycopy(lengthOfIVInBytes, 0, messageToApp, 0, 8);

            System.arraycopy(cipherInfo.getIv(), 0, messageToApp, 8, cipherInfo.getIv().length);

            Log.d("encrypt", "symmetric encrypted final message iv: " + Arrays.toString(cipherInfo.getIv()));

            System.arraycopy(cipherInfo.getBytes(), 0, messageToApp, 8 + cipherInfo.getIv().length, cipherInfo.getBytes().length);

            Log.d("encrypt", "symmetric encrypted final message byte text: " + Arrays.toString(cipherInfo.getBytes()));

            Log.d("encrypt", "symmetric encrypted final message: " + Arrays.toString(messageToApp));


            byte[] ivLentgh = Arrays.copyOfRange(messageToApp,0,8);
            Log.d("encrypt", "recived iv length: " + Arrays.toString(ivLentgh));

            byte[] iv = Arrays.copyOfRange(messageToApp,8, 8 + ByteBuffer.wrap(ivLentgh).getInt());
            Log.d("encrypt", "recived iv: " + Arrays.toString(iv));

            byte[] message = Arrays.copyOfRange(messageToApp,8 + ByteBuffer.wrap(ivLentgh).getInt() ,messageToApp.length);
            Log.d("encrypt", "recived message: " + Arrays.toString(message));
            */

        /*
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
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        */

    }
}
