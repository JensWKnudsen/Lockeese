package com.p3.aaugroup301.lockeese;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LOLAdapter extends ArrayAdapter<ListOfLocks> {

    Context context;
    int resource;
    List<ListOfLocks> locksList;



    public LOLAdapter(Context context, int resource, List<ListOfLocks> locksList){
        super(context, resource, locksList);

        this.context = context;
        this.resource = resource;
        this.locksList = locksList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DataBaseHandler dataBaseHandler = new DataBaseHandler();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_locks_item, null);
        EditText textViewNameOfTheLock = view.findViewById(R.id.tVnameOfTheLock);
        TextView textViewIsSharedWith = view.findViewById(R.id.tVLockIsSharedWith);
        Spinner listOfSharedKeys;
        int userNumber;
       // ArrayList<User> listOfSharedKeysForTheChosenLock = new ArrayList<>();

        Button buttonDelete = view.findViewById(R.id.buttonDeleteKey);
        Button buttonShare = view.findViewById(R.id.buttonSharkeKey);
        Spinner spinner = view.findViewById(R.id.spinnerOfUsers);

       // ListOfLocks listOfLocks = locksList.get(position);
       // textViewNameOfTheLock.setText((CharSequence) dataBaseHandler.getLocks());
        //spinner.setAdapter(Adapter adapter);

        ArrayList<String> listOfUsersWithKey = dataBaseHandler.getUsers();

        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item,
                listOfUsersWithKey);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //anonymous class onClickListener
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

}
