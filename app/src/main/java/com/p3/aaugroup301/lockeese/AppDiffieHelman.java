package com.p3.aaugroup301.lockeese;

import android.util.Log;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AppDiffieHelman {

    KeyPair AsymmetricKeyPairApp;
    EncryptionHandler encryptionHandler;
    PublicKey AsymmetricPublicKeyPi;
    SecretKeySpec appAesKeySpec;

    public PublicKey AppStart(PublicKey AsymmetricPublicKeyPi) throws NoSuchAlgorithmException {

        encryptionHandler = new EncryptionHandler();
        AsymmetricKeyPairApp = encryptionHandler.asymmetricKeyGeneration();
        this.AsymmetricPublicKeyPi = AsymmetricPublicKeyPi;
        return AsymmetricKeyPairApp.getPublic();

    }

    public byte[] appDHKeyExchange(byte[] encryptedMessageFromPi) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        byte[] piDHPubKeyEnc = encryptionHandler.asymmetricDecrypt(encryptedMessageFromPi,AsymmetricKeyPairApp.getPrivate());
        // after the double encryption the message becomes the same as before it was sent

        /*
         * The app has received Pi's public key
         * in encoded format.
         * It instantiates a DH public key from the encoded key material.
         */
        KeyFactory appKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(piDHPubKeyEnc);

        PublicKey piDHPubKey = appKeyFac.generatePublic(x509KeySpec);

        /*
         * The App gets the DH parameters associated with Pi's public key.
         * It must use the same parameters when it generates its own key
         * pair.
         */
        DHParameterSpec dhParamFromPisPubKey = ((DHPublicKey)piDHPubKey).getParams();

        KeyPair appDHKeyPair = createDHKeypair(dhParamFromPisPubKey);
        KeyAgreement appKeyAgree = DHKeyAgreement(appDHKeyPair);

        byte[] appDHPubKeyEnc = appDHKeyPair.getPublic().getEncoded();

        byte[] encryptedDHPublicKeyOfApp = encryptionHandler.asymmetricEncrypt(appDHPubKeyEnc,AsymmetricPublicKeyPi).getBytes();

        //This should happen after the return
        createSharedSecretKey(piDHPubKey,appKeyAgree);

        return encryptedDHPublicKeyOfApp;


    }

    // The app creates its own DH key pair
    public KeyPair createDHKeypair(DHParameterSpec dhParamFromPisPubKey) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator appKpairGen = KeyPairGenerator.getInstance("DH");
        appKpairGen.initialize(dhParamFromPisPubKey);
        return appKpairGen.generateKeyPair();
    }

    // The app creates and initializes its DH KeyAgreement object
    public KeyAgreement DHKeyAgreement(KeyPair appDHKpair) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement appKeyAgree = KeyAgreement.getInstance("DH");
        appKeyAgree.init(appDHKpair.getPrivate());
        return appKeyAgree;
    }

    public void createSharedSecretKey(PublicKey piDHPubKey, KeyAgreement appKeyAgree) throws InvalidKeyException {
        /*
         * The App uses pi's public key for the first (and only) phase
         * of his version of the DH
         * protocol.
         */
        appKeyAgree.doPhase(piDHPubKey, true);
        byte[] piSharedSecret = appKeyAgree.generateSecret();
        appAesKeySpec = new SecretKeySpec(piSharedSecret, 0, 16, "AES");
    }

    public byte[] readRequestMessage(byte[] fullMessage) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        byte[] ivLentgh = Arrays.copyOfRange(fullMessage,0,8);

        byte[] iv = Arrays.copyOfRange(fullMessage,8, 8 + ByteBuffer.wrap(ivLentgh).getInt());

        byte[] message = Arrays.copyOfRange(fullMessage,8 + ByteBuffer.wrap(ivLentgh).getInt() ,fullMessage.length);

        EncryptionHandler encryptionHandler = new EncryptionHandler();
        byte[] decryptedMessage = encryptionHandler.symmetricDecrypt(message,iv,appAesKeySpec);

        String stringOfDecryptedMessage = new String(decryptedMessage);
        if (stringOfDecryptedMessage.equals("request for key")){

            //hash of door key
            byte[] keyMessage = "Door key, yeah!".getBytes();

            CipherInfo requestMessageCipherInfo = encryptionHandler.symmetricEncrypt(keyMessage,appAesKeySpec);

            int lengthOfIV = requestMessageCipherInfo.getIv().length;

            byte[] lengthOfIVInBytes = ByteBuffer.allocate(8).putInt(lengthOfIV).array();

            byte[] messageToPi = new byte[8 + requestMessageCipherInfo.getIv().length + requestMessageCipherInfo.getBytes().length];

            System.arraycopy(lengthOfIVInBytes, 0, messageToPi, 0, 8);

            System.arraycopy(requestMessageCipherInfo.getIv(), 0, messageToPi, 8, requestMessageCipherInfo.getIv().length);

            System.arraycopy(requestMessageCipherInfo.getBytes(), 0, messageToPi, 8 + requestMessageCipherInfo.getIv().length, requestMessageCipherInfo.getBytes().length);

            return messageToPi;

        }

        return "Error".getBytes();

    }

}