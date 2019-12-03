package com.p3.aaugroup301.lockeese;

public class ListOfLocks {
    String LockName;
    String UserIDInSpinner;


    public ListOfLocks(String lockName, String userIDInSpinner) {
        this.LockName = lockName;
        this.UserIDInSpinner = userIDInSpinner;
    }

    public String getLockName() {
        return LockName;
    }

    public String getUserIDInSpinner() {
        return UserIDInSpinner;
    }
}
