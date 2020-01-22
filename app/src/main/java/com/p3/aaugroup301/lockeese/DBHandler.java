package com.p3.aaugroup301.lockeese;


import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

/**
 * DBHandler handles all communication with the database
 * Final strings are used as references to database collections and fields
 * The information about the logged in user is stored here
 */
public class DBHandler {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();//creates an object/reference of the database "db"
    private static final String USERS_COLLECTION = "Users"; //Name of the user collection in the database
    private static final String LOCKS_COLLECTION = "Locks";
    private static final String USERSOFLOCK_COLLECTION = "UsersOfLock";
    private static final String EXPIRATION = "Expiration";
    private static final String NAME_OF_KEY = "Name";
    private static final String KEYS_COLLECTION = "Keys";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String HASH = "Hash";
    private static final String LOCKID = "LockID";
    private static final String ACCESS_LEVEL = "Access Level";
    private static final String USERID = "UserID";
    private static final String KEY = "Key";
    private static final String LOCKPUBLICKEY = "LockPublicKey";
    private static final String PRIVATEKEY = "PrivateKey";
    private static final String PUBLICEKEY = "PublicKey";
    private static final String USERSPUBLICEKEY = "UserPublicKey";

    private static ArrayBlockingQueue<ArrayList<String>> stringArrayBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<ArrayList<KeysHashes>> ArrayHashesBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<ArrayList<ListOfLocks>> ArrayListOfLocksBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<String> UserExistsBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<String> SharekeyBlockingQueue = new ArrayBlockingQueue<>(1);


    private static String currentUserID;
    private static String currentUserName;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    public static String getCurrentUserID() {
        return currentUserID;
    }

    public static void setCurrentUserID(String currentUserID) {
        DBHandler.currentUserID = currentUserID;
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static void setCurrentUserName(String currentUserName) {
        DBHandler.currentUserName = currentUserName;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(PrivateKey privateKey) {
        DBHandler.privateKey = privateKey;
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static void setPublicKey(PublicKey publicKey) {
        DBHandler.publicKey = publicKey;
    }

    //Login
    String login(String username, String password){
        Log.e("verify user","start");
        stringArrayBlockingQueue.clear();
        Query userWithTheUsername = db.collection(USERS_COLLECTION).whereEqualTo(USERNAME, username).whereEqualTo(PASSWORD,password);

        userWithTheUsername.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                Log.e("verify user","in query");
                if (querySnapshot.isEmpty()){
                    Log.e("verify user", "A user with this password and username does not exist");
                    try {
                        ArrayList<String> array= new ArrayList<>();
                        stringArrayBlockingQueue.put(array);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{
                    ArrayList<String> array = new ArrayList<>();
                    String id = querySnapshot.getDocuments().get(0).getId();
                    String privateKey = querySnapshot.getDocuments().get(0).getString(PRIVATEKEY);
                    String publicKey = querySnapshot.getDocuments().get(0).getString(PUBLICEKEY);
                    array.add(id);
                    array.add(privateKey);
                    array.add(publicKey);
                    Log.e("verify user", "A user with this password and username exists. id: " + id);
                    try {

                        stringArrayBlockingQueue.put(array);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            ArrayList<String> temp = stringArrayBlockingQueue.take();
            currentUserID = temp.get(0);
            currentUserName = username;

            try {
                KeyFactory KeyFac = KeyFactory.getInstance("RSA");
                byte[] decodedPrivateKey = decodeHexString(temp.get(1));
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
                privateKey = KeyFac.generatePrivate(pkcs8EncodedKeySpec);

                byte[] decodedPublicKey = decodeHexString(temp.get(2));
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decodedPublicKey);
                PublicKey AsymmetricPubKey = KeyFac.generatePublic(x509KeySpec);
                publicKey = AsymmetricPubKey;

            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }


            return currentUserID;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        }

    }

    // change name of key
    public void changeNameOfKey(String name,String keyID){
        CollectionReference usersKeys = db.collection(USERS_COLLECTION).document(currentUserID).collection(KEYS_COLLECTION);

        Map<String, Object> data = new HashMap<>();
        data.put(NAME_OF_KEY, name);
        usersKeys.document(keyID).set(data);


    }


    // remove key
    void removeKey(String id, String lockID){
        DocumentReference usersKeys = db.collection(USERS_COLLECTION).document(currentUserID).collection(KEYS_COLLECTION).document(id);

        usersKeys.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        DocumentReference locksUser = db.collection(LOCKS_COLLECTION).document(lockID).collection(USERSOFLOCK_COLLECTION).document(id);

        locksUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }



    // get keys     time
    ArrayList<KeysHashes> getKeyHashes(){
        ArrayHashesBlockingQueue.clear();
        Query userWithTheUsername = db.collection(USERS_COLLECTION).document(currentUserID).collection(KEYS_COLLECTION);
        ArrayList<KeysHashes> listOfKeys = new ArrayList<>();
        userWithTheUsername.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<KeysHashes> listOfKeys = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {


                        String name = document.getString(NAME_OF_KEY);
                        String id = document.getId();
                        String hash = document.getString(HASH);
                        String lockid = document.getString(LOCKID);
                        Integer accessLevel = document.getLong(ACCESS_LEVEL).intValue();
                        Timestamp timestamp = document.getTimestamp(EXPIRATION);
                        String publicKey = document.getString(LOCKPUBLICKEY);
                        Log.e("PublicKey","Key is: " + publicKey);
                        Log.e("PublicKey","Key is: " + id);
                        Log.e("PublicKey","Key is: " + timestamp.toString());


                        KeyFactory KeyFac = null;
                        PublicKey AsymmetricPubKey = null;
                        try {
                            byte[] decodedPublicKey = decodeHexString(publicKey);
                            KeyFac = KeyFactory.getInstance("RSA");
                            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decodedPublicKey);
                            AsymmetricPubKey = KeyFac.generatePublic(x509KeySpec);
                        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                            e.printStackTrace();
                        }


                        KeysHashes keysHashes = new KeysHashes(name,id,hash,lockid,accessLevel,timestamp,AsymmetricPubKey);
                        listOfKeys.add(keysHashes);

                    }
                    try {
                        ArrayHashesBlockingQueue.put(listOfKeys);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w("getKeys", "Error getting documents.", task.getException());
                }
            }
        });
        try {
            listOfKeys = ArrayHashesBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return listOfKeys;

    }

    ArrayList<String> getUsers() {
        Query allUsers = db.collection(USERS_COLLECTION);
        final ArrayList<String> listOfUsers = new ArrayList<>();
        allUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.w("getUSers", document.getString("Username"));
                        listOfUsers.add(document.getString("Username"));
                    }
                } else {
                    Log.w("getUsers", "Error getting documents.", task.getException());
                }
            }
        });

        return listOfUsers;
    }

    ArrayList<String> userExists(final String inputUsername) {

        ArrayList<String> listOfUsers = new ArrayList<>();
        UserExistsBlockingQueue.clear();
        db.collection(USERS_COLLECTION)
                .whereEqualTo("Username", inputUsername)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.w("checkUserExists", "print user name " + document.getString("Username") + "=" + inputUsername);
                                //tOfUsers.add(document.getString("Username"));
                                try {
                                    UserExistsBlockingQueue.put(document.getId());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        try {
            listOfUsers.add(UserExistsBlockingQueue.take());
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        return listOfUsers;

    }


    ArrayList<String> getUsers(String LockID) {
        Log.w("getUsersBylocks", "Checking in new get users method" +LockID);

        Query allUsersLocks = db.collection(LOCKS_COLLECTION).document(LockID).collection(USERSOFLOCK_COLLECTION);
        final ArrayList<String> listOfUsers = new ArrayList<>();
        allUsersLocks.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.w("getUsersBylocks", "print user name " +document.getString("Username") +":" + document.getId());
                        if( document.getString("Username") != null ) {
                            listOfUsers.add(document.getString("Username"));
                        }
                    }
                } else {
                    Log.w("getUsers", "Error getting documents.", task.getException());
                }
            }
        });

        return listOfUsers;
    }


    void shareKey(String username, String lockID, String lockName, String accessLevel){
        SharekeyBlockingQueue.clear();
        String userSharedWith;

        Query queryUsername = db.collection(USERS_COLLECTION).whereEqualTo(USERNAME, username);
        Log.w("shareKey", "print user name " +username );


        queryUsername.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    Log.w("shareKey", "print document.getId " +document.getId() );
                    try {
                        SharekeyBlockingQueue.put(document.getId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.w("getKeys", "Error getting documents.", task.getException());
                }
            }
        });
        try {
            userSharedWith = SharekeyBlockingQueue.take();

            Date currentDate = new Date(  );
            String hashKeyInput = userSharedWith + currentDate + lockID;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(hashKeyInput.getBytes(StandardCharsets.UTF_8));
            //String encodedString = Base64.getEncoder().encodeToString(encodedHash);
            String encodedString = encodeHexString(encodedHash);
Log.e ("GOD; HELP US!", "Before Map");
            Map<String, Object> lockData = new HashMap<>();
            lockData.put(ACCESS_LEVEL, Integer.parseInt(accessLevel));
            lockData.put(EXPIRATION,Timestamp.now());
            lockData.put(KEY,encodedString);
            lockData.put(USERID, userSharedWith);
            lockData.put(USERSPUBLICEKEY, getUserPublicKey(userSharedWith));
            lockData.put(USERNAME, username);
            db.collection(LOCKS_COLLECTION).document(lockID).collection(USERSOFLOCK_COLLECTION).document(userSharedWith).set(lockData);
            Log.e ("GOD; HELP US!", "After Map");




            Map<String, Object> keyData = new HashMap<>();
            keyData.put(NAME_OF_KEY, lockName + " (" + currentUserName + ")");
            keyData.put(LOCKID, lockID);
            keyData.put(LOCKPUBLICKEY, getLockPublicKey(lockID));
            keyData.put(ACCESS_LEVEL, Integer.parseInt(accessLevel));
            keyData.put(EXPIRATION, Timestamp.now());
            keyData.put(HASH, encodedString);
            db.collection(USERS_COLLECTION).document(userSharedWith).collection(KEYS_COLLECTION).add(keyData);

        } catch (InterruptedException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    String getLockPublicKey(String lockID){
        Log.e ("GOD; HELP US!", "getLockPublcKey methof Map");
        String lockPublicKey ="";
        UserExistsBlockingQueue.clear();
        db.collection(LOCKS_COLLECTION)
                .whereEqualTo(LOCKID, lockID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //tOfUsers.add(document.getString("Username"));
                                try {
                                    UserExistsBlockingQueue.put(document.getString(PUBLICEKEY));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        try {
            lockPublicKey = UserExistsBlockingQueue.take();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        return lockPublicKey;
    }

    String getUserPublicKey(String userID){
        Log.e ("GOD; HELP US!", "inside getUserPublicKey Map");
        String usersPublicKey ="";
        ArrayList<String> listOfUsers = new ArrayList<>();
        UserExistsBlockingQueue.clear();
        db.collection(USERS_COLLECTION)
                .whereEqualTo(USERID, userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //tOfUsers.add(document.getString("Username"));
                                try {
                                    Log.e ("GOD; HELP US!", "inside getUserPublicKey  INSIDE TRY ");
                                    UserExistsBlockingQueue.put(document.getString(PUBLICEKEY));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        try {
            usersPublicKey = UserExistsBlockingQueue.take();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        return usersPublicKey;
    }

    //get locks lockname listOfUsersOnTheLock timeRemaining
    ArrayList<ListOfLocks> getLocks() {
        ArrayListOfLocksBlockingQueue.clear();
        final ArrayList<ListOfLocks> listOfIDs = new ArrayList<>();

        db.collection(USERS_COLLECTION).document(currentUserID).collection(LOCKS_COLLECTION).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (final DocumentSnapshot documentLocks : queryDocumentSnapshots.getDocuments()) {
                            final ListOfLocks listOfLocks = new ListOfLocks(documentLocks.getString("Lockname"), currentUserID, currentUserName, documentLocks.getString("LockID"));
                            listOfIDs.add(listOfLocks);
                        }
                        try {
                            ArrayListOfLocksBlockingQueue.put(listOfIDs);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        //Temp Initialization
        ArrayList<ListOfLocks> listOfLocksArrayToReturn = new ArrayList<>();
        try {
            listOfLocksArrayToReturn = ArrayListOfLocksBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listOfLocksArrayToReturn;
    }
    //delete user from lock (username, lockID)



    String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }


    private byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }


    private byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

}


