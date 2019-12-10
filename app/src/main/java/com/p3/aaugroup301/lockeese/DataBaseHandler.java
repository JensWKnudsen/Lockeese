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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import androidx.annotation.NonNull;

public class DataBaseHandler {

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


    private static ArrayBlockingQueue<String> StringBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<ArrayList<String>> stringArrayBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<ArrayList<ArrayList>> ArrayArrayBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<ArrayList<KeysHashes>> ArrayHashesBlockingQueue = new ArrayBlockingQueue<>(1);
    private static ArrayBlockingQueue<ArrayList<ListOfLocks>> ArrayListOfLocksBlockingQueue = new ArrayBlockingQueue<>(1);


    static String currentUserID;
    static String currentUserName;

    public static String getCurrentUserID() {
        return currentUserID;
    }

    public static void setCurrentUserID(String currentUserID) {
        DataBaseHandler.currentUserID = currentUserID;
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static void setCurrentUserName(String currentUserName) {
        DataBaseHandler.currentUserName = currentUserName;
    }



    //Login
    public String login(String username, String password){
        Log.e("verify user","start");
        StringBlockingQueue.clear();
        Query userWithTheUsername = db.collection(USERS_COLLECTION).whereEqualTo(USERNAME, username).whereEqualTo(PASSWORD,password);

        userWithTheUsername.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                Log.e("verify user","in query");
                if (querySnapshot.isEmpty()){
                    Log.e("verify user", "A user with this password and username does not exist");
                    try {
                        StringBlockingQueue.put("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{
                    String id = querySnapshot.getDocuments().get(0).getId();
                    Log.e("verify user", "A user with this password and username exists. id: " + id);
                    try {
                        StringBlockingQueue.put(id);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            String temp = StringBlockingQueue.take();
            currentUserID = temp;
            currentUserName = username;
            return temp;
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
    public void removeKey(String id,String lockID){
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
        DocumentReference locksUser = db.collection(LOCKS_COLLECTION).document(lockID).collection(USERS_COLLECTION).document(id);

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
    public ArrayList<KeysHashes> getKeyHashes(){
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
                        KeysHashes keysHashes = new KeysHashes(name,id,hash,lockid,accessLevel,timestamp);
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

    public ArrayList<String> getUsers() {
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

    public ArrayList<String> getUsers(String LockID) {
        Log.w("getUsersBylocks", "Checking in new get users method" +LockID);

        Query allUsersLocks = db.collection(LOCKS_COLLECTION).document(LockID).collection(USERSOFLOCK_COLLECTION);
        final ArrayList<String> listOfUsers = new ArrayList<>();
        allUsersLocks.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.w("getUsersBylocks", "print user name " +document.getString("Username") );
                        if( document.getString("Username") != null )
                            listOfUsers.add(document.getString("Username"));
                    }
                } else {
                    Log.w("getUsers", "Error getting documents.", task.getException());
                }
            }
        });

        return listOfUsers;
    }


    public void shareKey(String username, String lockID, String lockName, String accessLevel){
        StringBlockingQueue.clear();
        String userSharedWith;

        Query queryUsername = db.collection(USERS_COLLECTION).whereEqualTo(USERNAME, username);

        queryUsername.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    try {
                        StringBlockingQueue.put(document.getId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.w("getKeys", "Error getting documents.", task.getException());
                }
            }
        });
        try {
            userSharedWith = StringBlockingQueue.take();

            Map<String, Object> lockData = new HashMap<>();
            db.collection(LOCKS_COLLECTION).document(lockID).collection(USERS_COLLECTION).document(userSharedWith).set(lockData);


            Date currentDate = new Date(  );
            String hashKeyInput = userSharedWith + currentDate + lockID;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(hashKeyInput.getBytes(StandardCharsets.UTF_8));

            Map<String, Object> keyData = new HashMap<>();
            keyData.put(NAME_OF_KEY, lockName + " (" + currentUserName + ")");
            keyData.put(LOCKID, lockID);
            keyData.put(ACCESS_LEVEL, accessLevel);
            keyData.put(HASH, encodedHash);
            db.collection(USERS_COLLECTION).document(userSharedWith).collection(KEYS_COLLECTION).add(keyData);

        } catch (InterruptedException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //get locks lockname listOfUsersOnTheLock timeRemaining
    public ArrayList<ListOfLocks> getLocks() {
        ArrayListOfLocksBlockingQueue.clear();
        final ArrayList<ListOfLocks> listOfIDs = new ArrayList<>();

        db.collection(USERS_COLLECTION).document(currentUserID).collection(LOCKS_COLLECTION).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (final DocumentSnapshot documentLocks : queryDocumentSnapshots.getDocuments()) {
                            final ListOfLocks listOfLocks = new ListOfLocks(documentLocks.getString("LockName"), currentUserID, currentUserName, documentLocks.getString("LockID"));
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


}
