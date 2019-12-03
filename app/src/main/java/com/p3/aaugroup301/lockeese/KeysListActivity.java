package com.p3.aaugroup301.lockeese;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.google.firebase.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;



public class KeysListActivity extends AppCompatActivity {

     Context context;
     DataBaseHandler dataBaseHandler;
     ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys_list);
        listView = findViewById(R.id.customListView);
        dataBaseHandler = new DataBaseHandler();
        Log.e("SearchLocks", "before GetKeysAsyncTask");

        Button nextScreenButton = findViewById(R.id.nextScreenButton);
        nextScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, ListOfLocksActivity.class);
                   context.startActivity(intent);

            }
        });
        GetKeysAsyncTask getKeysAsyncTask = new GetKeysAsyncTask(context);
        getKeysAsyncTask.execute((Void) null);
        Log.e("SearchLocks", "After AsyncTask" );
    }

    public void getRemainingTime (KeysHashes keysHashes){
        Timestamp expirationTime =  keysHashes.expirationDate;
        long expirationDate = expirationTime.getSeconds();
        Date expirationDateD = new Date(expirationDate);
        Date currentDate = new Date();
//        if (currentDate.after(expirationDateD)) {
//            //delete key
//            dataBaseHandler.removeKey( String.valueOf(keysHashes.keyID),String.valueOf(keysHashes.LockID) );
//        }

    }





/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_more, menu);
        return true;
    }

   // @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Rename:
                //pop up for renaming key
                return true;
            case R.id.Delete:
                // pop up for deleting key
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    */

    /**
     * Represents an asynchronous changing activity task used to fetch the data
     * about the keys that the user has from the database
*/

    public class GetKeysAsyncTask extends AsyncTask<Void, Void, String> {

        Context context;
        private ProgressDialog progressDialog;
        ArrayList<KeysHashes> listOfKeys = new ArrayList<>();
        DataBaseHandler dataBaseHandler = new DataBaseHandler();

       public GetKeysAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(KeysListActivity.this);
            progressDialog.setTitle("Loading Your keys");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("SearchLocks", "do in background: " + listOfKeys.toString());
           // String result = "Something went wrong when searching";

            synchronized (this) {
                try {
                    listOfKeys = dataBaseHandler.getKeyHashes();
                    Log.e("asynctest", "list of keys is size:" + listOfKeys.size());
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

            //super.onPostExecute(listOfKeys);
            Log.e("SearchLocks", "Adapter before called " );
            ListAdapter keyListAdapter = new ListAdapter(KeysListActivity.this,listOfKeys );
            ListView listView1 = findViewById(R.id.customListView);

            listView1.setAdapter(keyListAdapter);

            Log.d("SearchLocks", "After AsyncTask before the for loop" );
            Log.e("SearchLocks", "Inside for loop " + listOfKeys.size() );

            for(int i=0; i<=listOfKeys.size()-1; i++){
                Log.e("SearchLocks", "Inside for loop " + listOfKeys.size() );
                getRemainingTime( listOfKeys.get(i) );
                Log.e("SearchLocks", "After AsyncTask after the for loop" );
            }
        }


    }
}


