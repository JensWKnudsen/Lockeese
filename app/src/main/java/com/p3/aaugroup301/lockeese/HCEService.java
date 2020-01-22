package com.p3.aaugroup301.lockeese;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.util.ArrayUtils;

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
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * HCE service constantly runs in the background and responds to NFC apdu commands
 */
//sending LockKey to NFC reader
public class HCEService extends HostApduService {

    String AID = "A0000002471001";
    PublicKey piAsymmetricPubKey;
    EncryptionHandler encryptionHandler;
    SecretKeySpec appAesKeySpec;
    KeysHashes usedKey;
    DBHandler DBHandler;
    byte[] part1;
    byte[] part2;


    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {

        encryptionHandler = new EncryptionHandler();
        DBHandler = new DBHandler();

        Log.e("NFC","bytes[] has length: " + bytes.length);
        String AID = new String(Arrays.copyOfRange(bytes,0,12));
        Log.e("NFC","AID: " + AID);
        Log.e("NFC","0. byte is " +new String(Arrays.copyOfRange(bytes,0,1)) );
        Log.e("NFC","1. byte is " +new String(Arrays.copyOfRange(bytes,1,2)) );
        Log.e("NFC","2. byte is " +new String(Arrays.copyOfRange(bytes,2,3)) );
        Log.e("NFC","3. byte is " +new String(Arrays.copyOfRange(bytes,3,4)) );
        Log.e("NFC","4. byte is " +new String(Arrays.copyOfRange(bytes,4,5)) );
        Log.e("NFC","5. byte is " +new String(Arrays.copyOfRange(bytes,5,6)) );
        Log.e("NFC","6. byte is " +new String(Arrays.copyOfRange(bytes,6,7)) );
        Log.e("NFC","7. byte is " +new String(Arrays.copyOfRange(bytes,7,8)) );
        Log.e("NFC","8. byte is " +new String(Arrays.copyOfRange(bytes,8,9)) );
        Log.e("NFC","9. byte is " +new String(Arrays.copyOfRange(bytes,9,10)) );
        Log.e("NFC","10. byte is " +new String(Arrays.copyOfRange(bytes,10,11)) );
        Log.e("NFC","11. byte is " +new String(Arrays.copyOfRange(bytes,11,12)) );
        Log.e("NFC","12. byte is " +new String(Arrays.copyOfRange(bytes,12,13)) );

        String stage = new String(Arrays.copyOfRange(bytes,12,13));
        Log.e("NFC","stage is: " + stage);
        String messagePart = new String(Arrays.copyOfRange(bytes,13,bytes.length));
        Log.e("NFC","extra info is: " + messagePart);
      byte[] msgPartBytesArray = Arrays.copyOfRange(bytes,13,bytes.length);
      Log.e("NFC", "size of the array :" + msgPartBytesArray.length);




        switch(stage)
        {
            case"0" :
                Log.e("NFC", "case 0: ");
                part1 = Arrays.copyOfRange(bytes,13,bytes.length);

            return part1;

            case "1" :
                Log.e("NFC", "case 1: ");
                part2 = Arrays.copyOfRange(bytes,13,bytes.length);

               // byte[] finalArrayPublicKey = new byte[part1.length + part2.length];
               byte[] finalArrayPublicKey = ArrayUtils.concatByteArrays(part1,part2);
                Log.e("NFC", "size of the part1 array after concat is : " + part1.length);
                Log.e("NFC", "size of the part2 array after concat is : " + part2.length);

               Log.e("NFC", "size of the final array after concat is : " + finalArrayPublicKey.length);

                Log.e("NFC", "before the try: ");
                try {
                    Log.e("NFC", "inside the try: ");
                    KeyFactory KeyFac = KeyFactory.getInstance("RSA");
                    Log.e("NFC", "AFTER KeyFactory.getInstance: ");
                    //X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Arrays.copyOfRange(bytes,13,bytes.length));
                   // X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(msgPartBytesArray);
                    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(finalArrayPublicKey);
                    Log.e("NFC", "after KeySpec " + x509KeySpec);
                    piAsymmetricPubKey = KeyFac.generatePublic(x509KeySpec);

                    //Log.e("NFC", "if the key is not null: " + piAsymmetricPubKey);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }

                ArrayList<KeysHashes> keys = KeysListActivity.getListOfKeys();
                for(KeysHashes key: keys){
                    if (Arrays.equals(key.getPublicKey().getEncoded(),
                            piAsymmetricPubKey.getEncoded())){
                        usedKey = key;
                        return DBHandler.getPublicKey().getEncoded();
                    }
                }


                return "stage 1 failed".getBytes();


            case "2" :
                byte[] encryptedDHPublicKeyOfApp = new byte[0];

                try {
                    Log.e("Decrypting","Message recived: " + DBHandler.encodeHexString(Arrays.copyOfRange(bytes,13,bytes.length)));

                    byte[] piDHPubKeyEnc = encryptionHandler.asymmetricDecrypt(Arrays.copyOfRange(bytes,13,bytes.length), DBHandler.getPrivateKey());
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

                    encryptedDHPublicKeyOfApp = encryptionHandler.asymmetricEncrypt(appDHPubKeyEnc,piAsymmetricPubKey).getBytes();


                    //This should happen after the return
                    createSharedSecretKey(piDHPubKey,appKeyAgree);
                    Log.e("Stage 2","Got to the end");

                } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                return encryptedDHPublicKeyOfApp;


            case "3" :
                try {
                    byte[] ivLentgh = Arrays.copyOfRange(Arrays.copyOfRange(bytes,13,bytes.length),0,8);

                    byte[] iv = Arrays.copyOfRange(Arrays.copyOfRange(bytes,13,bytes.length),8, 8 + ByteBuffer.wrap(ivLentgh).getInt());

                    byte[] message = Arrays.copyOfRange(Arrays.copyOfRange(bytes,13,bytes.length),8 + ByteBuffer.wrap(ivLentgh).getInt() ,Arrays.copyOfRange(bytes,13,bytes.length).length);

                    EncryptionHandler encryptionHandler = new EncryptionHandler();
                    byte[] decryptedMessage = encryptionHandler.symmetricDecrypt(message,iv,appAesKeySpec);

                    String stringOfDecryptedMessage = new String(decryptedMessage);
                    if (stringOfDecryptedMessage.equals("request for key")){

                        //hash of door key
                        byte[] keyMessage = usedKey.keyHash.getBytes();

                        CipherInfo requestMessageCipherInfo = encryptionHandler.symmetricEncrypt(keyMessage,appAesKeySpec);

                        int lengthOfIV = requestMessageCipherInfo.getIv().length;

                        byte[] lengthOfIVInBytes = ByteBuffer.allocate(8).putInt(lengthOfIV).array();

                        byte[] messageToPi = new byte[8 + requestMessageCipherInfo.getIv().length + requestMessageCipherInfo.getBytes().length];

                        System.arraycopy(lengthOfIVInBytes, 0, messageToPi, 0, 8);

                        System.arraycopy(requestMessageCipherInfo.getIv(), 0, messageToPi, 8, requestMessageCipherInfo.getIv().length);

                        System.arraycopy(requestMessageCipherInfo.getBytes(), 0, messageToPi, 8 + requestMessageCipherInfo.getIv().length, requestMessageCipherInfo.getBytes().length);

                        return messageToPi;
                    }
                } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

        }


        return "Hello Reader".getBytes();

    }

    @Override
    public void onDeactivated(int i) {


    }

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
}
