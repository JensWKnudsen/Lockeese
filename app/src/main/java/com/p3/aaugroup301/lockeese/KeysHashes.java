package com.p3.aaugroup301.lockeese;

import com.google.firebase.Timestamp;

public class KeysHashes {
    String KeyName;
    String KeyID;
    String KeyHash;
    String LockID;
    int AccessLevel;
    Timestamp Timer;

    public KeysHashes(String keyName, String keyID, String keyHash, String lockID, int accessLevel, Timestamp timer) {
        this.KeyName = keyName;
        this.KeyID = keyID;
        this.KeyHash = keyHash;
        this.LockID = lockID;
        this.AccessLevel = accessLevel;
        this.Timer = timer;
    }

    public String getKeyName() {
        return KeyName;
    }

    public void setKeyName(String keyName) {
        KeyName = keyName;
    }

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String keyID) {
        KeyID = keyID;
    }

    public String getKeyHash() {
        return KeyHash;
    }

    public void setKeyHash(String keyHash) {
        KeyHash = keyHash;
    }

    public String getLockID() {
        return LockID;
    }

    public void setLockID(String lockID) {
        LockID = lockID;
    }

    public int getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        AccessLevel = accessLevel;
    }

    public Timestamp getTimer() {
        return Timer;
    }

    public void setTimer(Timestamp timer) {
        Timer = timer;
    }


}
