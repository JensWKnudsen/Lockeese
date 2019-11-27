package com.p3.aaugroup301.lockeese;

public class KeysHashes {
    String KeyName;
    String UsernameOfGranter;
    int    AccessLevel;
    long    Timer;

    public KeysHashes(String keyName, String usernameOfGranter, int accessLevel, long timer) {
        KeyName = keyName;
        UsernameOfGranter = usernameOfGranter;
        AccessLevel = accessLevel;
        Timer = timer;
    }
}
