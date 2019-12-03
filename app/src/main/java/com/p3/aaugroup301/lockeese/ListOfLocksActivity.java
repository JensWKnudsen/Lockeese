package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListOfLocksActivity extends AppCompatActivity {

    List<ListOfLocks> listOfLocksList;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button buttonGoToKeysActivity = findViewById(R.id.buttonGoToKeysActivity);

        super.onCreate(savedInstanceState);

        listOfLocksList = new ArrayList<>();

        buttonGoToKeysActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, ListOfLocksActivity.class);
                context.startActivity(intent);

            }
        });

       /**



        setContentView(R.layout.activity_list_of_locks);
        listOfSharedKeys = findViewById(R.id.spinnerListOfGivenKeys);

        ArrayList<String> listOfUsersWithKey = dbh.getUsers();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                listOfUsersWithKey);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfSharedKeys.setAdapter(adapter);

        */






    }
}

