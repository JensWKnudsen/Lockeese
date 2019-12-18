package com.p3.aaugroup301.lockeese;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static androidx.core.content.ContextCompat.startActivity;

public class ListAdapter extends BaseAdapter {


    public ArrayList<KeysHashes> listOfKeysHashes;
    private Context context;

    public ListAdapter(Context context, ArrayList<KeysHashes> listOfKeysHashes) {
        this.context = context;
        this.listOfKeysHashes = listOfKeysHashes;

    }

    @Override
    public int getCount() {
        return listOfKeysHashes.size();
    }

    @Override
    public KeysHashes getItem(int position) {
        return listOfKeysHashes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView
            , ViewGroup parent) {
        View row;
        final KeysListViewHolder keysListViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.activity_custom_list_view, parent, false);
            keysListViewHolder = new KeysListViewHolder();
            keysListViewHolder.keyNameAndUser = row.findViewById(R.id.keyNameAndUser);
            keysListViewHolder.accessLevel = row.findViewById(R.id.accessLevel);
            keysListViewHolder.timer = row.findViewById(R.id.timer);
            keysListViewHolder.keyIcon = row.findViewById(R.id.keyIcon);
            keysListViewHolder.deleteButton = row.findViewById(R.id.deleteButton );
            row.setTag(keysListViewHolder);
        } else {
            row = convertView;
            keysListViewHolder = (KeysListViewHolder) row.getTag();
        }
        final KeysHashes keysHashes = getItem(position);

        keysListViewHolder.keyNameAndUser.setText(keysHashes.keyName );
        SimpleDateFormat dateFormat = new SimpleDateFormat( " E dd-M-yyyy HH:mm" );
        Date expiration =  keysHashes.expirationDate.toDate();
        Button deleteKey = keysListViewHolder.deleteButton;
        if(keysHashes.getAccessLevel() == 4){
            keysListViewHolder.timer.setText("expires: " +  dateFormat.format(expiration));
        }else{
            keysListViewHolder.timer.setText("Permanent");
        }

        //create updateTimer method
        keysListViewHolder.accessLevel.setText("access level: " + keysHashes.accessLevel );

       deleteKey.setOnClickListener( new MyOnClickListener(keysHashes,keysListViewHolder) {
            } );

        return row;
    }


  public class DeleteKeysAsyncTask extends AsyncTask<Void, Void, String> {

        Context context;
       private ProgressDialog progressDialog;
       ArrayList<KeysHashes> listOfKeys = new ArrayList<>();
       private DBHandler DBHandler = new DBHandler();
       KeysListViewHolder keysListViewHolder;
       KeysHashes keysHashes;

       public DeleteKeysAsyncTask(Context context , KeysHashes keysHashes, KeysListViewHolder keysListViewHolder) {

           this.context=context;
           this.keysHashes=keysHashes;
           this.keysListViewHolder = keysListViewHolder;
       }

       @Override
       protected void onPreExecute() {

       }

       @Override
       protected String doInBackground(Void... voids) {

           // String result = "Something went wrong when searching";

           synchronized (this) {
               try {
                //   Log.e( "KeyHashesName", "do in background: " + keysHashes.getKeyName() );
                  listOfKeys = KeysListActivity.getListOfKeys();
                           DBHandler.removeKey(String.valueOf(keysHashes.keyID),String.valueOf( keysHashes.LockID ));
                   Log.e( "asynctest", "list of keys is size:" + listOfKeys.size() );

               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
           return "Success";
       }


       @Override
       protected void onPostExecute(String result) {
           Toast.makeText( context,"Your key was deleted",Toast.LENGTH_LONG ).show();
          // runnable.run();

           Intent intent = new Intent(context, KeysListActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(context, intent, Bundle.EMPTY);

       }
   }
    class MyOnClickListener implements View.OnClickListener {

        KeysHashes keysHashes ;
        KeysListViewHolder keysListViewHolder;

        MyOnClickListener(KeysHashes keysHashes, KeysListViewHolder keysListViewHolder) {
            this.keysHashes = keysHashes;
            this.keysListViewHolder = keysListViewHolder;
        }

        @Override
        public void onClick(View arg0) {
            final AlertDialog.Builder deleteKey = new AlertDialog.Builder(keysListViewHolder.deleteButton.getContext());
            deleteKey.setMessage("Are you sure you want to delete this key?");
            deleteKey.setCancelable(false);
            deleteKey.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            } );
            final DeleteKeysAsyncTask deleteKeysAsyncTask= new DeleteKeysAsyncTask( context, keysHashes,keysListViewHolder );

            deleteKey.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteKeysAsyncTask.execute( (Void)null);
                }
            });
            AlertDialog reg = deleteKey.create();
            reg.show();
        }

    }

}


