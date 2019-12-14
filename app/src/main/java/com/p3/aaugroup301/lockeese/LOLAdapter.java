package com.p3.aaugroup301.lockeese;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.ShowableListMenu;

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
        lockListViewHolder.theLockIsSharedWith.setText("The lock is shared with: ");
        dbhandler = new DataBaseHandler();
        ArrayList userList = dbhandler.getUsers(listOfLocks.lockId);


        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item,
                userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lockListViewHolder.spinnerOfUsers.setAdapter(adapter);
        row.setTag(lockListViewHolder);

        //onClick delete user
        //onClick share lockAccess

        lockListViewHolder.shareKey.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertShareKeyDialog(context, listOfLocks);
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
        final Button cancelSharing = alertDialogView.findViewById(R.id.button_cancel_sharing);
        final Button shareTheKeyWith = alertDialogView.findViewById(R.id.button_share_the_key_with);
        Log.e("alertDialog", "before AlertDialog builder ");

        new AlertDialog.Builder(context).setView(alertDialogView)
                .setTitle("Grant an access")
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
    }

    private void showToast(String message){
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.show();
    }
}


