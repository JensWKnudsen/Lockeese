package com.p3.aaugroup301.lockeese;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;



public class KeysListActivity extends AppCompatActivity {

    Context context;
    DataBaseHandler dataBaseHandler;
    ListView listView;

  static   public ArrayList<KeysHashes> getListOfKeys() {
        return listOfKeys;
    }

    static public void setListOfKeys(ArrayList<KeysHashes> listOfKeysS) {
        listOfKeys = listOfKeysS;
    }

    static ArrayList<KeysHashes> listOfKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_keys_list );
        listView = findViewById( R.id.customListView );
        dataBaseHandler = new DataBaseHandler();
        Log.e( "SearchLocks", "before GetKeysAsyncTask" );

        Button nextScreenButton = findViewById( R.id.nextScreenButton );
        nextScreenButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent( context, ListOfLocksActivity.class );
                context.startActivity( intent );

            }
        } );

        Button LogOutButton = findViewById( R.id.logOut );
        LogOutButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataBaseHandler.setCurrentUserID( null );
                DataBaseHandler.setCurrentUserName( null );

                Context context = view.getContext();
                Intent intent = new Intent( context, LoginActivity.class );
                context.startActivity( intent );


            }
        } );

        GetKeysAsyncTask getKeysAsyncTask = new GetKeysAsyncTask( context );
        getKeysAsyncTask.execute( (Void) null );
        Log.e( "SearchLocks", "After AsyncTask" );


    }


    public void getRemainingTime(KeysHashes keysHashes) {
        Log.e( "Expiration", "checking key" );
        Date expirationDate = keysHashes.expirationDate.toDate();
        ;
        Date currentDate = new Date();
        Log.e( "Expiration", "current date: " + currentDate );
        Log.e( "Expiration", "expiration date: " + expirationDate );

        if (currentDate.after( expirationDate )) {
            //delete key
            Log.e( "Expiration", "key is expired" );
            dataBaseHandler.removeKey( String.valueOf( keysHashes.keyID ), String.valueOf( keysHashes.LockID ) );
        }

    }


    public class GetKeysAsyncTask extends AsyncTask<Void, Void, String> {

        Context context;
        private ProgressDialog progressDialog;
        ArrayList<KeysHashes> listOfKeys = new ArrayList<>();
        private DataBaseHandler dataBaseHandler = new DataBaseHandler();

        public GetKeysAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog( KeysListActivity.this );
            progressDialog.setTitle( "Loading Your keys" );
            progressDialog.setMessage( "Please wait" );
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e( "SearchLocks", "do in background: " + listOfKeys.toString() );
            // String result = "Something went wrong when searching";

            synchronized (this) {
                try {
                    listOfKeys = dataBaseHandler.getKeyHashes();
                    KeysListActivity.setListOfKeys( listOfKeys );

                    Log.e( "asynctest", "list of keys is size:" + listOfKeys.size() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "Success";
        }


        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing()) {
                progressDialog.setMessage( "Success" );
                progressDialog.dismiss();
            }

            //super.onPostExecute(listOfKeys);
            Log.e( "SearchLocks", "Adapter before called " );
            ListAdapter keyListAdapter = new ListAdapter( KeysListActivity.this, listOfKeys );
            ListView listView1 = findViewById( R.id.customListView );

            listView1.setAdapter( keyListAdapter );


            Log.d( "SearchLocks", "After AsyncTask before the for loop" );
            Log.e( "SearchLocks", "Inside for loop " + listOfKeys.size() );

            for (int i = 0; i <= listOfKeys.size() - 1; i++) {
                Log.e( "SearchLocks", "Inside for loop " + listOfKeys.size() );
                if (listOfKeys.get( i ).getAccessLevel() == 4) {
                    getRemainingTime( listOfKeys.get( i ) );
                }
                Log.e( "SearchLocks", "After AsyncTask after the for loop" );
            }
        }
    }

}


