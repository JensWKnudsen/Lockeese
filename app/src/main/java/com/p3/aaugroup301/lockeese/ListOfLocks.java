package com.p3.aaugroup301.lockeese;

import java.util.ArrayList;

public class ListOfLocks {


    String lockName;
    String lockId;
    String userIDInSpinner;
    String userName;
    ArrayList usersOfLock;

    public ArrayList getUsersOfLock() {
        return usersOfLock;
    }

    public void setUsersOfLock(ArrayList usersOfLock) {
        this.usersOfLock = usersOfLock;
    }



    public ListOfLocks(String lockName, String userIDInSpinner, String userName, String lockId) {
        this.lockName = lockName;
        this.userIDInSpinner = userIDInSpinner;
        this.userName = userName;
        this.lockId = lockId;
    }


    public String getLockName() {
        return lockName;
    }

    public String getUserIDInSpinner() {
        return userIDInSpinner;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setLockName(String lockName) {
        lockName = lockName;
    }

    public void setUserIDInSpinner(String userIDInSpinner) {
        userIDInSpinner = userIDInSpinner;
    }
}