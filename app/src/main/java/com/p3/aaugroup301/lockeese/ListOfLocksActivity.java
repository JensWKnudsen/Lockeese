package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.ArrayLinkedVariables;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListOfLocksActivity extends AppCompatActivity {

    List<ListOfLocks> listOfLocksList;
    ListView listViewLOL;
    LockListViewHolder lockListViewHolder = new LockListViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_locks);
        Button buttonGoToKeysActivity = findViewById(R.id.buttonGoToKeysActivity);

        listOfLocksList = new ArrayList<>();
        listViewLOL = findViewById(R.id.dynamicListView);

        Log.e("SearchLocks", "before GetKeysAsyncTask");

        GetListOfLocksAsyncTask getListOfLocksAsyncTask = new GetListOfLocksAsyncTask(ListOfLocksActivity.this);
        getListOfLocksAsyncTask.execute((Void) null);
        Log.e("SearchLocks", "After AsyncTask" );

        buttonGoToKeysActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Intent intent = new Intent(view.getContext(), KeysListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


        /**
         * Represents an asynchronous changing activity task used to fetch the data
         * about the locks and shared keys that the user has from the database
         */

         class GetListOfLocksAsyncTask extends AsyncTask<Void, Void, String> {


            Context context;
            private ProgressDialog progressDialog;
            ArrayList<ListOfLocks> listOflocksTheUserHas = new ArrayList<>();
            private DataBaseHandler dataBaseHandler = new DataBaseHandler();

            public GetListOfLocksAsyncTask(Context context) {
                this.context = context;
            }

            @Override
            protected void onPreExecute() {

                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Loading Your locks");
                progressDialog.setMessage("Please wait");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                Log.e("SearchLocks", "do in background: " + listOflocksTheUserHas.toString());
                // String result = "Something went wrong when searching";

                synchronized (this) {
                    try {
                        listOflocksTheUserHas = dataBaseHandler.getLocks();
                        Log.e("asynctest", "list of locks is size:" + listOflocksTheUserHas.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return "Success";
            }

            @Override
            protected void onPostExecute(String result) {
                if (progressDialog.isShowing()) {
                    progressDialog.setMessage("Success");
                    progressDialog.dismiss();
                }

                LOLAdapter lockListAdapter = new LOLAdapter(ListOfLocksActivity.this, listOflocksTheUserHas);
                ListView listView1 = findViewById(R.id.dynamicListView);
                listView1.setAdapter(lockListAdapter);
                Log.e("SearchLocks", " OnPostExecute --> Inside for loop " + listOflocksTheUserHas.get(0).usersOfLock );

            }
        }
}

