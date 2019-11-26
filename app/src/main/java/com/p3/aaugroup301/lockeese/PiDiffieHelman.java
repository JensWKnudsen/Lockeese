package com.p3.aaugroup301.lockeese;

import android.util.Log;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PiDiffieHelman {

    KeyPair AsymmetricKeyPairPi;

    public void piStart() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        AppDiffieHelman appDiffieHelman = new AppDiffieHelman();


        EncryptionHandler encryptionHandler = new EncryptionHandler();
        AsymmetricKeyPairPi = encryptionHandler.asymmetricKeyGeneration();
        PublicKey AsymmetricPublicKeyApp = appDiffieHelman.AppStart(AsymmetricKeyPairPi.getPublic());

        KeyPair PiDHKpair = createDHKeypair();
        KeyAgreement piKeyAgree = DHKeyAgreement(PiDHKpair);
        // Pi encodes its public key, and sends it over to the app.
        byte[] piDHPubKeyEnc = PiDHKpair.getPublic().getEncoded();

        byte[] encryptedDHPublicKeyOfPi = encryptionHandler.asymmetricEncrypt(piDHPubKeyEnc,AsymmetricPublicKeyApp).getBytes();

        byte[] EncryptedDHPublicKeyOfApp = appDiffieHelman.appDHKeyExchange(encryptedDHPublicKeyOfPi);

        byte[] AppDHPubKeyEnc = encryptionHandler.asymmetricDecrypt(EncryptedDHPublicKeyOfApp,AsymmetricKeyPairPi.getPrivate());
        // after the double encryption the message becomes the same as before it was sent



        /*
         * Pi uses App's public key for the first (and only) phase
         * of its version of the DH
         * protocol.
         * Before it can do so, it has to instantiate a DH public key
         * from the app's encoded key material.
         */
        KeyFactory piKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(AppDHPubKeyEnc);
        PublicKey AppDHPubKey = piKeyFac.generatePublic(x509KeySpec);
        piKeyAgree.doPhase(AppDHPubKey, true);

        byte[] piSharedSecret = piKeyAgree.generateSecret();
        SecretKeySpec piAesKeySpec = new SecretKeySpec(piSharedSecret, 0, 16, "AES");

        byte[] requestMessage = "request for key".getBytes();

        CipherInfo requestMessageCipherInfo = encryptionHandler.symmetricEncrypt(requestMessage,piAesKeySpec);

        int lengthOfIV = requestMessageCipherInfo.getIv().length;

        byte[] lengthOfIVInBytes = ByteBuffer.allocate(8).putInt(lengthOfIV).array();

        byte[] messageToApp = new byte[8 + requestMessageCipherInfo.getIv().length + requestMessageCipherInfo.getBytes().length];

        System.arraycopy(lengthOfIVInBytes, 0, messageToApp, 0, 8);

        System.arraycopy(requestMessageCipherInfo.getIv(), 0, messageToApp, 8, requestMessageCipherInfo.getIv().length);

        System.arraycopy(requestMessageCipherInfo.getBytes(), 0, messageToApp, 8 + requestMessageCipherInfo.getIv().length, requestMessageCipherInfo.getBytes().length);


        // message is sent and response is received
        byte[] doorKeyMessage = appDiffieHelman.readRequestMessage(messageToApp);

        byte[] ivLength = Arrays.copyOfRange(doorKeyMessage,0,8);

        byte[] iv = Arrays.copyOfRange(doorKeyMessage,8, 8 + ByteBuffer.wrap(ivLength).getInt());

        byte[] message = Arrays.copyOfRange(doorKeyMessage,8 + ByteBuffer.wrap(ivLength).getInt() ,doorKeyMessage.length);

        byte[] decryptedMessage = encryptionHandler.symmetricDecrypt(message,iv,piAesKeySpec);


        String stringOfDecryptedMessage = new String(decryptedMessage);
        Log.d("encrypt", "door key text: " + stringOfDecryptedMessage);


    }

    // Diffi helman part
    /*
     * Pi creates its own DH key pair with 2048-bit key size
     */
    public KeyPair createDHKeypair() throws NoSuchAlgorithmException {
        KeyPairGenerator PiKpairGen = KeyPairGenerator.getInstance("DH");
        PiKpairGen.initialize(512);
        return PiKpairGen.generateKeyPair();
    }

    // Pi creates and initializes its DH KeyAgreement object
    public KeyAgreement DHKeyAgreement(KeyPair PiDHKpair) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement PiKeyAgree = KeyAgreement.getInstance("DH");
        PiKeyAgree.init(PiDHKpair.getPrivate());
        return PiKeyAgree;
    }

}
