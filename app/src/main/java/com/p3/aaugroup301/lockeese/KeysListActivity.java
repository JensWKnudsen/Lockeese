package com.p3.aaugroup301.lockeese;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;



public class KeysListActivity extends AppCompatActivity {

     Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys_list);
        ListView listView;
        listView = findViewById(R.id.customListView);
        ListAdapter keyListAdapter = new ListAdapter(this, getKeyHash());
        listView.setAdapter(keyListAdapter);
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

    public ArrayList<KeysHashes> getKeyHash() {
        ArrayList<KeysHashes> keysHashesArrayList = new ArrayList<>();
        keysHashesArrayList.add(new KeysHashes("0000", "Summer House key (Tom)", "sxdchgj", 1, 345676, "gh"));
        keysHashesArrayList.add(new KeysHashes("0001", "House (Jerry)", "hhgg", 3, 34576, "gkjhh"));
        keysHashesArrayList.add(new KeysHashes("0002", "Office (John)", "kjihvgf", 4, 3676, "ghhg"));
        keysHashesArrayList.add(new KeysHashes("0003", "Garage (Perry)", "jhc", 2, 376, "gjhh"));
        keysHashesArrayList.add(new KeysHashes("0004", "BF place (Perry)", "hgcxcv", 2, 34567, "gjhgh"));
        return keysHashesArrayList;
    }

    public String getRemainingTime(int currentUser) {
        long expirationTime = getKeyHash().get(1).Timer;
        Date expirationDate = new Date(expirationTime);
        Date currentDate = new Date();
        if (currentDate.after(expirationDate)) {
            //delete key
        }
        return expirationDate.toString();
    }

/*
        public long onTicking(int size) {
        //display how much time remains
         long  expirationTime = getKeyHash().get(size).Timer;
           return expirationTime / 1000;

        }

        public void onFinish() {
            //delete key from this user's list
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

