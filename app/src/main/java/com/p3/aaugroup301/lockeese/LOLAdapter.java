package com.p3.aaugroup301.lockeese;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LOLAdapter extends BaseAdapter {

   private Context context;
   public ArrayList<ListOfLocks> locksList;

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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View row;
        final LockListViewHolder lockListViewHolder;
        final DataBaseHandler dataBaseHandler = new DataBaseHandler();

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
        lockListViewHolder.theLockIsSharedWith.setText("the lock " + " lock ID" + "is share with: ");
        ArrayList<String> listOfUsersWithKey = dataBaseHandler.getUsers();
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item,
                listOfUsersWithKey);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lockListViewHolder.spinnerOfUsers.setAdapter(adapter);
        row.setTag(lockListViewHolder);

        //onClick delete user
        //onClick share lockAccess


       // ListOfLocks listOfLocks = locksList.get(position);
       // textViewNameOfTheLock.setText((CharSequence) dataBaseHandler.getLocks());
        //spinner.setAdapter(Adapter adapter);

        //ArrayList<String> listOfUsersWithKey = dataBaseHandler.getUsers();

       // ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item,
        //        listOfUsersWithKey);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerOfUsers.setAdapter(adapter);

        return row;
    }

}
