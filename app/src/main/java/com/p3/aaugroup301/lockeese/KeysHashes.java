package com.p3.aaugroup301.lockeese;

import com.google.firebase.Timestamp;

public class KeysHashes {
    String keyName;
    String keyID;
    String keyHash;
    String LockID;
    int accessLevel;
    Timestamp expirationDate;

    public KeysHashes(String keyName, String keyID, String keyHash, String lockID, int accessLevel, Timestamp expirationDate) {
        this.keyName = keyName;
        this.keyID = keyID;
        this.keyHash = keyHash;
        this.LockID = lockID;
        this.accessLevel = accessLevel;
        this.expirationDate = expirationDate;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public String getKeyHash() {
        return keyHash;
    }

    public void setKeyHash(String keyHash) {
        this.keyHash = keyHash;
    }

    public String getLockID() {
        return LockID;
    }

    public void setLockID(String lockID) {
        LockID = lockID;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Timestamp getTimer() {
        return expirationDate;
    }

    public void setTimer(Timestamp timer) {
        expirationDate = timer;
    }


}
