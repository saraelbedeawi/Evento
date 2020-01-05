package com.sf.evento.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sf.evento.R;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapterDialog extends BaseAdapter {

    public ArrayList<String> mSelectedItemsIds=new ArrayList<>();

    private List<DocumentSnapshot> friends;
    private FirebaseFirestore db;

    public CustomListAdapterDialog(List<DocumentSnapshot> friends,FirebaseFirestore db)
    {
        this.friends = friends;
        this.db = db;
    }
    public CustomListAdapterDialog(List<DocumentSnapshot> friends,FirebaseFirestore db,ArrayList<String> mSelectedItemsIds)
    {
        this.friends = friends;
        this.db = db;
        this.mSelectedItemsIds=mSelectedItemsIds;
    }


    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

//        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_dialog_items, null);
//        }

        //final ViewHolder holder = new ViewHolder();




            final CheckBox name = convertView.findViewById(R.id.guestName);
            String mobile1 = friends.get(position).getId();
            name.setTag(mobile1);

            CollectionReference users = db.collection("users");
            Query query = users.whereEqualTo("phoneNumber", mobile1);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                    name.setText((String) ds.get("name"));

                    name.setChecked(false);
                    for (int i = 0; i < mSelectedItemsIds.size(); i++) {
                        String phone = mSelectedItemsIds.get(i);
                        if (phone.equals(ds.get("phoneNumber")))
                            name.setChecked(true);
                    }

                }
            });

            name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (name.isChecked()) {
                        boolean found = false;
                        for (int i = 0; i < mSelectedItemsIds.size(); i++) {
                            String phone = mSelectedItemsIds.get(i);
                            String friendPhone = buttonView.getTag().toString();
                            if (phone.equals(friendPhone))
                                found = true;
                        }

                        if (!found)
                            mSelectedItemsIds.add((String) name.getTag());

//                    Toast.makeText(parent.getContext(), holder.name.getText()+"", Toast.LENGTH_SHORT).show();
                    } else {
//                    Toast.makeText(parent.getContext(), "sayb "+holder.name.getText()+"", Toast.LENGTH_SHORT).show();
                        mSelectedItemsIds.remove(name.getTag());
                    }
                }
            });


        return convertView;
    }

    public ArrayList<String> getSelectedIds() {

        return mSelectedItemsIds;

    }

}