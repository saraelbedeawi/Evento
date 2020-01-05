package com.sf.evento.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

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

public class GuestsStatusAdapterDialog extends BaseAdapter
{


        public ArrayList<String> mSelectedItemsIds=new ArrayList<>();

        private List<DocumentSnapshot> friends;
        private FirebaseFirestore db;

        public GuestsStatusAdapterDialog(List<DocumentSnapshot> friends,FirebaseFirestore db)
        {
            this.friends = friends;
            this.db = db;
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

        public View getView(final int position, View convertView, ViewGroup parent) {

//        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.guests_status_items, null);
//        }

            //final ViewHolder holder = new ViewHolder();
            final TextView name = convertView.findViewById(R.id.name);
            final TextView status = convertView.findViewById(R.id.status);
            String mobile1 = (String) friends.get(position).get("to");
//            name.setTag(mobile1);
            CollectionReference users = db.collection("users");
            Query query = users.whereEqualTo("phoneNumber", mobile1);
            final View finalConvertView = convertView;
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                    name.setText((String) ds.get("name"));
                    status.setText((String)friends.get(position).get("status"));
                    if(friends.get(position).get("status").equals("Approved"))
                    {
                        status.setTextColor(Color.GREEN);
                    }
                    else if(friends.get(position).get("status").equals("pending"))
                    {
                        status.setTextColor(Color.BLUE);

                    }
                    else if(friends.get(position).get("status").equals("Rejected"))
                    {
                        status.setTextColor(Color.RED);

                    }
                }
            });




            return convertView;
        }

        public ArrayList<String> getSelectedIds() {

            return mSelectedItemsIds;

        }

    }

