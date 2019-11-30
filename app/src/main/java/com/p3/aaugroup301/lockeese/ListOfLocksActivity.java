package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;

import static com.p3.aaugroup301.lockeese.dummy.DummyContent.ITEMS;

public class ListOfLocksActivity extends AppCompatActivity {

    private Spinner listOfSharedKeys;
    private int userNumber;
    private ArrayList<User> listOfSharedKeysForTheChosenLock = new ArrayList<>();
    Context context;
   // private ImageButton imageButtonDEleteUserFromSharedList;
    private ImageButton imageButtonShareTheLock;
    private static final String[] COUNTRY_KEY_ARRAY = {"Denmark", "Greece", "USA", "Spain", "France", "Slovakia", "Romania",
            "Japan", "Norway", "The United Kingdom", "Netherlands", "Italy", "Turkey"};

    private static final String[] LOCKS_DUMMY_ARRAY = {"Home Lock", "Office Lock", "summer house", "car"};
    public View row;
    private DataBaseHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbh = new DataBaseHandler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_locks);


        listOfSharedKeys = findViewById(R.id.spinnerListOfGivenKeys);
       // imageButtonDEleteUserFromSharedList = findViewById(R.id.imageButtonDEleteUserFromSharedList);
        imageButtonShareTheLock = findViewById (R.id.imageButtonShareTheLock);

        //change the dummy array to firebase list of users listOfUsersWithKey

        ArrayList<String> listOfUsersWithKey = new ArrayList<>(Arrays.asList(COUNTRY_KEY_ARRAY));
        listOfUsersWithKey = dbh.getUsers();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfUsersWithKey);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfSharedKeys.setAdapter(adapter);

        LinearLayout LOLdisplayed = findViewById(R.id.LinearLayoutSearch);
        for(userNumber = 0; userNumber < listOfSharedKeysForTheChosenLock.size(); userNumber ++){

            /*
            if(userNumber % 2 == 0){
                row.setBackground(R.color.colorPrimaryDark);
            }
*/

            TextView nameOfALock = new TextView(this);
            TextView tvClick2CWho = new TextView(this);
            //nameOfALock.setText(listOfSharedKeysForTheChosenLock.get(userNumber).getName());
            LOLdisplayed.addView(nameOfALock);
            LOLdisplayed.addView(tvClick2CWho);
            LOLdisplayed.addView(listOfSharedKeys);
            //LOLdisplayed.addView(imageButtonDEleteUserFromSharedList);
            LOLdisplayed.addView(imageButtonShareTheLock);

            context = this;


           // imageButtonShareTheLock.setOnClickListener();
        }

    }


}
