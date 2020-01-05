package com.sf.evento.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sf.evento.Activites.EditEvent;
import com.sf.evento.Classes.Event;
import com.sf.evento.R;
import com.sf.evento.Activites.RetrieveMap;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyGoingEventsAdapter extends RecyclerView.Adapter<MyGoingEventsAdapter.ViewHolder> {

    private List<DocumentSnapshot> requests;
    FirebaseFirestore db;
    StorageReference mStorageRef;

    public MyGoingEventsAdapter(FirebaseFirestore db, List<DocumentSnapshot> requests) {
        this.db = db;
        this.requests = requests;
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyGoingEventsAdapter.ViewHolder(LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.my_going_event_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        DocumentSnapshot req = requests.get(position);
        holder.reject.setTag(req.getId());
        String phone = (String) req.get("from");
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("phoneNumber", phone);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                holder.from.setText("From: "+(String) ds.get("name"));




            }
        });
        String EventId= (String) req.get("eventId");
        DocumentReference  events = db.collection("events").document(EventId);
        events.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    holder.name.setText( "Event name: "+(String)document.get("name"));
                    holder.startTime.setText( "Start Time: "+(String)document.get("startTime"));
                    holder.endTime.setText("End Time: "+(String)document.get("endTime"));
                    holder.date.setText("Event Date: "+(String) document.get("eventDate"));
                    holder.adresse.setText("Address: "+(String)document.get("adresse"));
                    holder.location.setTag(document.get("location"));


                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            String id = (String) holder.reject.getTag();
                                            Event e=new Event();
                                            e.remove(db, id);
                                            requests.remove(holder.getAdapterPosition());
                                            notifyDataSetChanged();
                                            Toast.makeText(holder.itemView.getContext(), "Declined successfully. ", Toast.LENGTH_LONG).show();                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                            builder.setMessage("Are you sure you want to remove event "+holder.from.getText()+"?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();



                        }
                    });
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                GeoPoint x = (GeoPoint) holder.location.getTag();
                //x.getLatitude();
                //x.getLongitude();
                LatLng position = new LatLng(x.getLatitude(),x.getLongitude());
                String longg = String.valueOf(position.longitude);
                String latt = String.valueOf(position.latitude);
                Intent intent = new Intent(holder.itemView.getContext(), RetrieveMap.class);
                intent.putExtra("x",longg);
                intent.putExtra("y",latt);
                holder.itemView.getContext().startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {

        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name,from, adresse,date;
        TextView startTime;
        TextView endTime;
        String latitude;
        String longitude;

        Button reject;
        ImageView location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_name);
            from  = itemView.findViewById(R.id.event_sender);
            adresse=itemView.findViewById(R.id.event_address);
            date=itemView.findViewById(R.id.event_date);
            startTime=itemView.findViewById(R.id.event_start_time);
            endTime=itemView.findViewById(R.id.event_end_time);
            reject = itemView.findViewById(R.id.rejectButton);
            location= itemView.findViewById(R.id.gps);

        }
    }
}
