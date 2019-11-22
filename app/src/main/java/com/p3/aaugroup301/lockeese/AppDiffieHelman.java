package com.p3.aaugroup301.lockeese;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

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

    public PublicKey AppStart(PublicKey AsymmetricPublicKeyPi) throws NoSuchAlgorithmException {

        encryptionHandler = new EncryptionHandler();
        AsymmetricKeyPairApp = encryptionHandler.asymmetricKeyGeneration();
        this.AsymmetricPublicKeyPi = AsymmetricPublicKeyPi;
        return AsymmetricKeyPairApp.getPublic();

    }

    public byte[] appDHKeyExchange(byte[] encryptedMessageFromPi) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        byte[] singleDecryptedMessageFromPi = encryptionHandler.asymmetricDecrypt(encryptedMessageFromPi,AsymmetricPublicKeyPi);
        byte[] piDHPubKeyEnc = encryptionHandler.asymmetricDecrypt(singleDecryptedMessageFromPi,AsymmetricKeyPairApp.getPrivate());
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
        byte[] doubleEncryptedDHPublicKeyOfApp = encryptionHandler.asymmetricEncrypt(encryptedDHPublicKeyOfApp,AsymmetricKeyPairApp.getPrivate()).getBytes();

        //This should happen after the return
        createSharedSecretKey(piDHPubKey,appKeyAgree);

        return doubleEncryptedDHPublicKeyOfApp;


    }

    // The app creates its own DH key pair
    public KeyPair createDHKeypair(DHParameterSpec dhParamFromPisPubKey) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Log.d("DH", "PI: Generate DH keypair ...");
        KeyPairGenerator appKpairGen = KeyPairGenerator.getInstance("DH");
        appKpairGen.initialize(dhParamFromPisPubKey);
        return appKpairGen.generateKeyPair();
    }

    // The app creates and initializes its DH KeyAgreement object
    public KeyAgreement DHKeyAgreement(KeyPair appDHKpair) throws NoSuchAlgorithmException, InvalidKeyException {
        Log.d("DH", "App: Initialization ...");
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
        Log.d("DH", "App: Execute PHASE1 ...");
        appKeyAgree.doPhase(piDHPubKey, true);
        byte[] piSharedSecret = appKeyAgree.generateSecret();
        SecretKeySpec appAesKeySpec = new SecretKeySpec(piSharedSecret, 0, 16, "AES");
    }

}