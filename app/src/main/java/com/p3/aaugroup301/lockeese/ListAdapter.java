package com.p3.aaugroup301.lockeese;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public View getView(final int position, View convertView
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
        if(keysHashes.getAccessLevel() == 4){
            keysListViewHolder.timer.setText("expires: " +  dateFormat.format(expiration));
        }else{
            keysListViewHolder.timer.setText("Permanent");
        }

        //create updateTimer method
        keysListViewHolder.accessLevel.setText("access level: " + keysHashes.accessLevel );
        keysListViewHolder.deleteButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DBHandler remove key from this user list
            }
        } );


        return row;
    }


}


