package com.sf.evento.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sf.evento.Classes.Event;
import com.sf.evento.R;
import com.sf.evento.Activites.RetrieveMap;
import java.util.List;
public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {

    private List<DocumentSnapshot> requests;
    FirebaseFirestore db;
    StorageReference mStorageRef;

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

        DocumentSnapshot req = requests.get(position);
        holder.reject.setTag(req.getId());
        String EventId= (String) req.getId();
        holder.name.setText( "Event Name: "+(String)req.get("name"));
        holder.startTime.setText( "Start Time: "+(String)req.get("startTime"));
        holder.endTime.setText("End Time: "+(String)req.get("endTime"));
        holder.date.setText("Date: "+(String) req.get("eventDate"));
        holder.adresse.setText("Address: "+(String)req.get("adresse"));
        holder.location.setTag(req.get("location"));
                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = (String) holder.reject.getTag();
                            Event e=new Event();
                            e.remove(db, id);
                            requests.remove(holder.getAdapterPosition());
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
            adresse=itemView.findViewById(R.id.event_address);
            date=itemView.findViewById(R.id.event_date);
            startTime=itemView.findViewById(R.id.event_start_time);
            endTime=itemView.findViewById(R.id.event_end_time);
            reject = itemView.findViewById(R.id.rejectButton);
            location= itemView.findViewById(R.id.gps);

        }
    }
}
