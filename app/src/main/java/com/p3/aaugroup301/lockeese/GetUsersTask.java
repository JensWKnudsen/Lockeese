package com.p3.aaugroup301.lockeese;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class GetUsersTask extends AsyncTask<Void, Void, ArrayList<String>> {

    ProgressDialog progressDialog;
    private Context context;
    private DataBaseHandler dbh;


    GetUsersTask(){
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> result = new ArrayList<>();

        synchronized (this) {
            try {
                //Call the database
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    protected void onPostExecute( ArrayList<String> result) {
        //onPostExecute .. try to fill the drop down
        dbh.getUsers();
    }
    }
