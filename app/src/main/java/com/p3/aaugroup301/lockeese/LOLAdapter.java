package com.p3.aaugroup301.lockeese;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static androidx.core.content.ContextCompat.startActivity;


public class LOLAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<ListOfLocks> locksList;
    DataBaseHandler dbhandler;

    public LOLAdapter(Context context, ArrayList<ListOfLocks> locksList){

        this.context = context;
        this.locksList = locksList;
    }

    @Override
    public int getCount() {
        return locksList.size();
    }

    @Override
    public ListOfLocks getItem(int position) {
        return locksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {


        View row;
        final LockListViewHolder lockListViewHolder;
        //Log.e("LOL Adapter", " getView array size " +  locksList.size());

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_of_locks_item, parent, false);
            lockListViewHolder = new LockListViewHolder();
            lockListViewHolder.lockName = row.findViewById(R.id.tVnameOfTheLock);
            lockListViewHolder.theLockIsSharedWith = row.findViewById(R.id.tVLockIsSharedWith);
            lockListViewHolder.spinnerOfUsers = row.findViewById(R.id.spinnerOfUsers);
            lockListViewHolder.deleteKey = row.findViewById(R.id.buttonDeleteKey);
            lockListViewHolder.shareKey = row.findViewById(R.id.buttonSharkeKey);

        } else {
            row = convertView;
            lockListViewHolder = (LockListViewHolder)row.getTag();
        }


        final ListOfLocks listOfLocks = getItem(position);

        lockListViewHolder.lockName.setText(listOfLocks.lockName);
        //lockListViewHolder.lockName.setBackgroundColor(Color.GRAY);
        lockListViewHolder.theLockIsSharedWith.setText("The lock is shared with: ");
        dbhandler = new DataBaseHandler();
        final ArrayList userList = dbhandler.getUsers(listOfLocks.lockId);
        ArrayList tempUserList = userList;

        Log.e("LOL Adapter", " before spinner code array size of users " +  tempUserList.size());

        tempUserList.add("Select a User");

        final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, tempUserList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lockListViewHolder.spinnerOfUsers.setAdapter(adapter);
        row.setTag(lockListViewHolder);
        Log.e("LOL Adapter", " before spinner code array size " +  locksList.size());

        //onClick delete user
        //onClick share lockAccess

        lockListViewHolder.shareKey.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertShareKeyDialog(context, listOfLocks);
            }
        } );

        lockListViewHolder.deleteKey.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final AlertDialog.Builder deleteKeyAlertDialog = new AlertDialog.Builder(lockListViewHolder.deleteKey.getContext());
                    deleteKeyAlertDialog.setMessage("Are you sure you want to delete this key from user " +
                            lockListViewHolder.spinnerOfUsers.getSelectedItem().toString() + "?");
                    deleteKeyAlertDialog.setCancelable(false);
                    deleteKeyAlertDialog.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    } );
                    deleteKeyAlertDialog.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String userName = lockListViewHolder.spinnerOfUsers.getSelectedItem().toString();
                            DeleteKeyAsyncTask deleteKeyAsyncTask = new DeleteKeyAsyncTask(context, userName, listOfLocks);
                            deleteKeyAsyncTask.execute((Void) null);
                        }
                    });
                AlertDialog reg = deleteKeyAlertDialog.create();
                reg.show();

            }
        } );

        return row;
    }

    /*
     * Show AlertDialog with some form elements.
     */

    public void alertShareKeyDialog(final Context context, final ListOfLocks lol) {
        final DataBaseHandler dbhandler = new DataBaseHandler();
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alertDialogView = inflater.inflate(R.layout.share_key_alert_dialog,
                null, false);

        final EditText usernameToSHareLockEditText = alertDialogView.findViewById(R.id.userNameToShareKeyWith);
        final TextView selectAccessLevel = alertDialogView.findViewById(R.id.select_access_level);
        final RadioGroup accessLevelRadioGroup = alertDialogView.findViewById(R.id.accessLevelRadioGroup);

        Log.e("alertDialog", "before AlertDialog builder ");

        TextView textView = new TextView(context);
        textView.setText("Grant an Access");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(Color.BLUE);
        textView.setTextColor(Color.WHITE);

        new AlertDialog.Builder(context).setView(alertDialogView)
                .setCustomTitle(textView)
                .setPositiveButton("Share the key", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        // find the radiobutton by returned id
                        int selectedId = accessLevelRadioGroup.getCheckedRadioButtonId();
                        RadioButton selectedRadioButton = alertDialogView.findViewById(selectedId);
                        if(selectedRadioButton == null)
                        {
                            showToast("please select the access level");
                        }
                        else {
                            String accessLevel = selectedRadioButton.getText().toString();

                            //Get the text from the EditText
                            String username = usernameToSHareLockEditText.getText().toString().trim();
                            if (username == null || username.isEmpty())
                                showToast("Please enter username");
                            else {
                                //Call the DB and check whether incoming user exists or not
                                //call the async task
                                CheckUserAsync cua = new CheckUserAsync(context, username, accessLevel, lol);
                                cua.execute((Void) null);
                                //ArrayList test = dbhandler.userExists(username);
                                //showToast(test.size() + "q");
                                dialog.cancel();
                            }
                        }
                    }
                    }).show();
    }

    private void showToast(String message){
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.show();
    }

}

class CheckUserAsync extends AsyncTask<Void, Void, Boolean> {

    Context context;
    String username;
    String accessLevel;
    ListOfLocks lol;
    DataBaseHandler dbhandler = new DataBaseHandler();

    public CheckUserAsync(Context context, String username, String accessLevel, ListOfLocks lol) {
        this.context = context;
        this.username = username;
        this.accessLevel = accessLevel;
        this.lol = lol;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean result = false;
        synchronized (this) {
            try {
                ArrayList<String> value = dbhandler.userExists(username);
                Log.w("CheckUserAsync", "user exists = "+ value );
                if(value.size() > 0) {
                    result = true;
                    dbhandler.shareKey(username, lol.lockId, lol.lockName, accessLevel);
                }
                 else result=false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean s) {
        if (s) {
            showToast("Key has been shared!!!!");
        } else {
            showToast("User does not exists");
        }
        Intent intent = new Intent(context, ListOfLocksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(context, intent, Bundle.EMPTY);
    }

    private void showToast(String message){
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.show();
    }
}

  class DeleteKeyAsyncTask extends AsyncTask<Void, Void, String> {

    Context context;
    private ProgressDialog progressDialog;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();
    String userName;
    ListOfLocks listOfLocks;


    public DeleteKeyAsyncTask(Context context ,String userName, ListOfLocks listOfLocks) {
        this.context = context;
        this.userName = userName;
        this.listOfLocks = listOfLocks;

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... voids) {

        // String result = "Something went wrong when searching";

        synchronized (this) {
            try {
                Log.e("DELETEKEYASYNC", "do in background: ");
                ArrayList<String> userId = dataBaseHandler.userExists(userName);
                Log.e("DELETEKEYASYNC", "do in background: userID " + userId.get(0));
                if(  userId.size() > 0) {
                    String userID = userId.get(0);
                    dataBaseHandler.removeKey(userID, listOfLocks.lockId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "Success";
    }


    @Override
    protected void onPostExecute(String result) {
        Toast.makeText( context,"Users key was deleted",Toast.LENGTH_LONG ).show();
        // runnable.run();

        Intent intent = new Intent(context, ListOfLocksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(context, intent, Bundle.EMPTY);

    }
}

