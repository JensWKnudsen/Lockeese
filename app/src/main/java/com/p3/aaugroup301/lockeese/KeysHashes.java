package com.p3.aaugroup301.lockeese;

public class KeysHashes {
    String KeyName;
    String KeyID;
    String KeyHash;
    String LockID;
    int    AccessLevel;
    long    Timer;



    public KeysHashes(String keyID, String keyName, String keyHash, int accessLevel, long timer, String lockID ) {
        KeyName = keyName;
        KeyID = keyID;
        KeyHash = keyHash;
        LockID = lockID;
        AccessLevel = accessLevel;
        Timer = timer;
    }
}
