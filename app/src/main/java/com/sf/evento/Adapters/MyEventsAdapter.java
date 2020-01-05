package com.sf.evento.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sf.evento.Activites.CreateEvent;
import com.sf.evento.Activites.EditEvent;
import com.sf.evento.Activites.MapsActivity;
import com.sf.evento.Classes.Event;
import com.sf.evento.R;
import com.sf.evento.Activites.RetrieveMap;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {

    private List<DocumentSnapshot> requests;
    FirebaseFirestore db;
    StorageReference mStorageRef;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private  GuestsStatusAdapterDialog adapter;

    Button x;
    public MyEventsAdapter(FirebaseFirestore db, List<DocumentSnapshot> requests) {
        this.db = db;
        this.requests = requests;
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyEventsAdapter.ViewHolder(LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.my_event_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final AlertDialog.Builder[] mBuilder = new AlertDialog.Builder[1];
        final DocumentSnapshot req = requests.get(position);
//        holder.reject.setTag(req.getId());
        String EventId= (String) req.getId();
        holder.name.append( "Event Name: "+(String)req.get("name"));
        holder.startTime.setText( "Start Time: "+(String)req.get("startTime"));
        holder.endTime.setText("End Time: "+(String)req.get("endTime"));
        holder.date.setText("Date: "+(String) req.get("eventDate"));
        holder.adresse.setText("Address: "+(String)req.get("adresse"));
        holder.location.setTag(req.get("location"));
//                    holder.reject.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String id = (String) holder.reject.getTag();
//                            Event e=new Event();
//                            e.remove(db, id);
//                            requests.remove(holder.getAdapterPosition());
//                        }
//                    });



        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                GeoPoint x = (GeoPoint) holder.location.getTag();

                LatLng position = new LatLng(x.getLatitude(),x.getLongitude());
                String longg = String.valueOf(position.longitude);
                String latt = String.valueOf(position.latitude);
                Intent intent = new Intent(holder.itemView.getContext(), RetrieveMap.class);
                intent.putExtra("x",longg);
                intent.putExtra("y",latt);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), EditEvent.class);
                intent.putStringArrayListExtra("guests", (ArrayList<String>) req.get("guests"));
                intent.putExtra("name",(String) req.get("name"));
                intent.putExtra("startTime",(String) req.get("startTime"));
                intent.putExtra("endTime",(String) req.get("endTime"));
                intent.putExtra("address",(String) req.get("adresse"));
                intent.putExtra("date",(String) req.get("eventDate"));
                GeoPoint x = (GeoPoint) req.get("location");
                LatLng position = new LatLng(x.getLatitude(),x.getLongitude());
                String longg = String.valueOf(position.longitude);
                String latt = String.valueOf(position.latitude);
                intent.putExtra("x",longg);
                intent.putExtra("y",latt);
                intent.putExtra("eventId",(String) req.getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        Query Friends = rootRef.collection("eventsRequests").whereEqualTo("eventId",req.getId());
        Friends.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {



                        mBuilder[0] = new AlertDialog.Builder(holder.itemView.getContext());
                        List<DocumentSnapshot> ds = task.getResult().getDocuments();
                        adapter= new GuestsStatusAdapterDialog(ds, db);
                        mBuilder[0].setTitle(R.string.dialog_title);
                        mBuilder[0].setAdapter(adapter, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int item)
                            {

                            }
                        });

                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error getting documents: "+ e.getMessage());
                    }
                });




       holder.x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mBuilder[0].setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    }
                });

                AlertDialog mDialog = mBuilder[0].create();
                mDialog.show();
                mDialog.getWindow().setLayout(1200,1800);

            }
        });
       holder.delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       switch (which){
                           case DialogInterface.BUTTON_POSITIVE:
                               Event e= new Event();
                               e.id=req.getId();
                               e.delete();
                               requests.remove(position);
                               notifyDataSetChanged();
                               Toast.makeText(holder.itemView.getContext(), "Deleted successfully. ", Toast.LENGTH_LONG).show();                               break;

                           case DialogInterface.BUTTON_NEGATIVE:
                               //No button clicked
                               break;
                       }
                   }
               };

               AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
               builder.setMessage("Are you sure ?").setPositiveButton("Yes", dialogClickListener)
                       .setNegativeButton("No", dialogClickListener).show();

           }
       });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, " I invite you to my event.  Event Name:"+req.get("name")+"Event Date: "+  req.get("eventDate")+" From : "+req.get("startTime")+" To: "+req.get("endTime")+" Please let me know if you are intrested");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                holder.itemView.getContext().startActivity(sendIntent);
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
        ImageView edit;
        ImageView delete;
        Button x;
        Button reject;
        ImageView location;
        ImageView share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.event_name);
            adresse=itemView.findViewById(R.id.event_address);
            date=itemView.findViewById(R.id.event_date);
            startTime=itemView.findViewById(R.id.event_start_time);
            endTime=itemView.findViewById(R.id.event_end_time);
            reject = itemView.findViewById(R.id.rejectButton);
            edit=itemView.findViewById(R.id.edit_button);
            delete=itemView.findViewById(R.id.trash_button);
            x=itemView.findViewById(R.id.viewGuests);
            location= itemView.findViewById(R.id.gps);
            delete=itemView.findViewById(R.id.trash_button);
            share=itemView.findViewById(R.id.share_button);



        }
    }
}
