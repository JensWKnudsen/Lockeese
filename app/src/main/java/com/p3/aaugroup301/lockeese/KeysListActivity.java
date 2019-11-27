package com.p3.aaugroup301.lockeese;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;



public class KeysListActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys_list);
        ListView listView;
        listView = findViewById(R.id.customListView);
        ListAdapter keyListAdapter = new ListAdapter( this, getKeyHash() );
        listView.setAdapter( keyListAdapter);
        Button nextScreenButton = findViewById( R.id.nextScreenButton );
        nextScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DBHandler.goToLocksActivity() should be called here
            }
        });

    }

    public ArrayList<KeysHashes> getKeyHash() {
        ArrayList<KeysHashes>  keysHashesArrayList = new ArrayList<>(  ) ;
        keysHashesArrayList.add(new KeysHashes("0000", "Summer House key (Tom)","sxdchgj",1,345676, "gh"));
        keysHashesArrayList.add(new KeysHashes("0001", "House (Jerry)","hhgg",3,34576, "gkjhh"));
        keysHashesArrayList.add(new KeysHashes("0002", "Office (John)","kjihvgf",4,3676, "ghhg"));
        keysHashesArrayList.add(new KeysHashes("0003", "Garage (Perry)","jhc",2,376, "gjhh"));
        keysHashesArrayList.add(new KeysHashes("0004", "BF place (Perry)","hgcxcv",2,34567, "gjhgh"));
        return keysHashesArrayList;
    }

    public String getRemainingTime (int currentUser){
        long expirationTime = getKeyHash().get( 1 ).Timer;
        Date expirationDate = new Date(expirationTime);
        Date currentDate = new Date(  );
        if (currentDate.after( expirationDate )){
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

}

