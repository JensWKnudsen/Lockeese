package com.p3.aaugroup301.lockeese;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys_list);
        ListView listView;
        listView = findViewById(R.id.customListView);
        ListAdapter keyListAdapter = new ListAdapter(this, dataBaseHandler.getKeyHashes());
        listView.setAdapter(keyListAdapter);
        dataBaseHandler = new DataBaseHandler();
        for(int i=0; i<=dataBaseHandler.getKeyHashes().size(); i++){
            getRemainingTime( dataBaseHandler.getKeyHashes().get( i ) );
        }
        Button nextScreenButton = findViewById(R.id.nextScreenButton);
        nextScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, ListOfLocksActivity.class);
                   context.startActivity(intent);
               // GoToLOLTask goToLOLTask = new GoToLOLTask();

            }
        });

    }


    public void getRemainingTime (KeysHashes keysHashes){
        Timestamp expirationTime =  keysHashes.expirationDate;
        long expirationDate = expirationTime.getSeconds();
        Date expirationDateD = new Date(expirationDate);
        Date currentDate = new Date();
        if (currentDate.after(expirationDateD)) {
            //delete key
            dataBaseHandler.removeKey( String.valueOf(keysHashes.keyID),String.valueOf(keysHashes.LockID) );
        }

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
     * about the user from the database


    public class GoToLOLTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private ProgressDialog progressDialog;
        private ArrayList<ArrayList> listOfLocks = new ArrayList<>();
        DataBaseHandler dataBaseHandler = new DataBaseHandler();

       /** public GoToLOLTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Loading your locks");
            progressDialog.setMessage("Please hang on");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(Void... voids) {
            Log.e("SearchLocks", "do in background: " + listOfLocks.toString());
            String result = "Something went wrong when searching";

            synchronized (this) {
                try {

                    listOfLocks = dataBaseHandler.getLocks();
                    Log.e("SearchLocks", "result from search: " + listOfLocks.toString());
                    result = "success";

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                AlertDialog.Builder lolActivity = new AlertDialog.Builder(context);
                lolActivity.setMessage(result);
                if (result.equals("success"))
                    context.startActivity(new Intent(context, ListOfLocksActivity.class));
            }

        }


    }

    */
}

