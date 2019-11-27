package com.p3.aaugroup301.lockeese;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
        keysHashesArrayList.add(new KeysHashes("Home key", "Tom",2,134567));
        keysHashesArrayList.add(new KeysHashes("Office key", "Jerry",3,500000l));
        keysHashesArrayList.add(new KeysHashes("Summer House key", "John",4,59));
        keysHashesArrayList.add(new KeysHashes("Summer House key", "Perry",4,59));
        keysHashesArrayList.add(new KeysHashes("Summer House key", "Perry",4,59));
        return keysHashesArrayList;
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

